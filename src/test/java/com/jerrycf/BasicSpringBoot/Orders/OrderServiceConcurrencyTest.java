package com.jerrycf.BasicSpringBoot.Orders;

import com.jerrycf.BasicSpringBoot.errors.NotEnoughStockException;
import com.jerrycf.BasicSpringBoot.model.DTOs.CreateOrderRequest;
import com.jerrycf.BasicSpringBoot.model.DTOs.OrderItemRequest;
import com.jerrycf.BasicSpringBoot.model.entity.Client;
import com.jerrycf.BasicSpringBoot.model.entity.Product;
import com.jerrycf.BasicSpringBoot.repository.ClientRepository;
import com.jerrycf.BasicSpringBoot.repository.OrderRepository;
import com.jerrycf.BasicSpringBoot.repository.ProductRepository;
import com.jerrycf.BasicSpringBoot.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Demonstrates the lost-update race condition when decrementing product stock.
 *
 * HOW TO READ THIS TEST
 * ---------------------
 * As of today, with neither optimistic nor pessimistic locking in place, both
 * tests FAIL. That failure is the expected outcome: it is the proof the bug
 * is real.
 *
 * The scenario they reproduce:
 *   1. N threads call createOrder() at the same time for the same product.
 *   2. They all read the same stock value before anyone has committed.
 *   3. They all pass the "enough stock" check.
 *   4. They all commit. More units are sold than ever existed.
 *
 * @Transactional does NOT protect against this: the default isolation level
 * (READ_COMMITTED) prevents reading uncommitted data, but it does not prevent
 * two transactions from reading the same value and overwriting each other.
 *
 * HOW TO FIX IT (pick one)
 * ------------------------
 * A) Optimistic locking - add to the Product entity:
 *
 *        @Version
 *        private Long version;
 *
 *    Hibernate appends "WHERE version = ?" to the UPDATE. The loser gets an
 *    OptimisticLockingFailureException. Cheap, ideal under low contention.
 *    Needs a retry in production (Spring Retry or a hand-rolled loop).
 *
 * B) Pessimistic locking - add to ProductRepository:
 *
 *        @Lock(LockModeType.PESSIMISTIC_WRITE)
 *        @Query("SELECT p FROM Product p WHERE p.id = :id")
 *        Optional<Product> findByIdForUpdate(@Param("id") Long id);
 *
 *    and call it from createOrder() instead of findById(). It emits
 *    SELECT ... FOR UPDATE, so the second thread waits. Easier to reason
 *    about, worse throughput.
 *
 * Either fix makes these tests pass without touching a single line in here.
 * That is exactly the point: the test describes the correct behaviour, not
 * the implementation.
 */
@SpringBootTest
@TestPropertySource(properties = {
        // In-memory, isolated database: never touches ./data/productsdb
        "spring.datasource.url=jdbc:h2:mem:racetest;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.open-in-view=false",
        "spring.jpa.show-sql=false",
        // There must be at least as many connections as threads, otherwise
        // they serialize waiting on the pool and the race never happens.
        "spring.datasource.hikari.maximum-pool-size=25"
})
class OrderServiceConcurrencyTest {

    private static final int THREADS = 20;

    @Autowired private OrderService orderService;
    @Autowired private ProductRepository productRepository;
    @Autowired private ClientRepository clientRepository;
    @Autowired private OrderRepository orderRepository;

    private Long clientId;

    // NOTE: this class is deliberately NOT annotated with @Transactional.
    // If it were, every thread would share the test's transaction and there
    // would be no race to observe.
    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        clientRepository.deleteAll();

        Client client = new Client();
        client.setName("Race Tester");
        client.setEmail("race-" + UUID.randomUUID() + "@test.com");
        client.setPassword("irrelevant");
        client.setAge(30);
        clientId = clientRepository.save(client).getId();
    }

    @Test
    @DisplayName("With 1 unit in stock and 20 concurrent buyers, only 1 order must succeed")
    @Disabled
    void shouldNotSellMoreUnitsThanAvailable() throws InterruptedException {
        Long productId = givenProductWithStock(1);

        Outcome outcome = buyConcurrently(productId, 1);

        assertEquals(0, outcome.unexpected(),
                "Unexpected exceptions were thrown. Check the test log: " + outcome.firstUnexpected());

        assertEquals(1, outcome.success(),
                "OVERSELL: " + outcome.success() + " orders were confirmed for 1 unit of stock. "
                        + "The extra " + (outcome.success() - 1) + " order(s) read the stock before the "
                        + "winner committed. Missing @Version or pessimistic locking.");

        int finalStock = currentStock(productId);
        assertEquals(0, finalStock,
                "Final stock should be 0 but was " + finalStock
                        + (finalStock < 0 ? " (negative: inventory that never existed was sold)" : ""));

        assertEquals(1, orderRepository.count(), "Exactly 1 order should have been persisted");
    }

    @Test
    @DisplayName("With 20 units and 20 concurrent single-unit buyers, stock must land on exactly 0")
    @Disabled
    void shouldNotLoseAnyStockDecrement() throws InterruptedException {
        Long productId = givenProductWithStock(THREADS);

        Outcome outcome = buyConcurrently(productId, 1);

        assertEquals(0, outcome.unexpected(),
                "Unexpected exceptions were thrown. Check the test log: " + outcome.firstUnexpected());

        int finalStock = currentStock(productId);
        int unitsSold = THREADS - finalStock;

        assertEquals(outcome.success(), unitsSold,
                "LOST UPDATE: " + outcome.success() + " orders were confirmed but only "
                        + unitsSold + " units were decremented. "
                        + (outcome.success() - unitsSold) + " decrement(s) were lost because several "
                        + "transactions wrote on top of the same stock read.");

        assertTrue(finalStock >= 0, "Stock should never go negative, but it was " + finalStock);
    }

    /* ------------------------- helpers ------------------------- */

    private Long givenProductWithStock(int stock) {
        Product product = new Product();
        product.setName("Limited Edition");
        product.setPrice(new BigDecimal("100.00"));
        product.setStock(stock);
        return productRepository.save(product).getId();
    }

    private int currentStock(Long productId) {
        return productRepository.findById(productId).orElseThrow().getStock();
    }

    /**
     * Spawns THREADS threads that wait on the same barrier and then fire
     * createOrder() simultaneously. The barrier (CountDownLatch) is what makes
     * this test worth anything: without it the threads start staggered and the
     * race may never reproduce.
     */
    private Outcome buyConcurrently(Long productId, int quantityPerOrder) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch finished = new CountDownLatch(THREADS);

        AtomicInteger succeeded = new AtomicInteger();
        AtomicInteger rejectedForStock = new AtomicInteger();
        AtomicInteger lockConflicts = new AtomicInteger();
        AtomicInteger unexpected = new AtomicInteger();
        StringBuilder firstFailure = new StringBuilder();

        CreateOrderRequest request = new CreateOrderRequest(
                clientId,
                "concurrency test",
                List.of(new OrderItemRequest(productId, quantityPerOrder))
        );

        for (int i = 0; i < THREADS; i++) {
            pool.submit(() -> {
                try {
                    startGate.await();
                    orderService.createOrder(request);
                    succeeded.incrementAndGet();
                } catch (NotEnoughStockException e) {
                    // Correct rejection: the system detected there was not enough stock.
                    rejectedForStock.incrementAndGet();
                } catch (ConcurrencyFailureException e) {
                    // Covers OptimisticLockingFailureException and
                    // PessimisticLockingFailureException: this is the signal that
                    // locking is doing its job. In production, retry here.
                    lockConflicts.incrementAndGet();
                } catch (Throwable t) {
                    unexpected.incrementAndGet();
                    synchronized (firstFailure) {
                        if (firstFailure.isEmpty()) {
                            firstFailure.append(t.getClass().getSimpleName()).append(": ").append(t.getMessage());
                        }
                    }
                    t.printStackTrace();
                } finally {
                    finished.countDown();
                }
            });
        }

        startGate.countDown();
        boolean finishedInTime = finished.await(60, TimeUnit.SECONDS);
        pool.shutdownNow();
        assertTrue(finishedInTime,
                "Threads did not finish within 60s: possible deadlock or exhausted connection pool");

        System.out.printf("[concurrency] succeeded=%d rejectedForStock=%d lockConflicts=%d unexpected=%d%n",
                succeeded.get(), rejectedForStock.get(), lockConflicts.get(), unexpected.get());

        return new Outcome(succeeded.get(), rejectedForStock.get(), lockConflicts.get(),
                unexpected.get(), firstFailure.toString());
    }

    private record Outcome(int success, int rejected, int conflicts, int unexpected, String firstUnexpected) {
    }
}