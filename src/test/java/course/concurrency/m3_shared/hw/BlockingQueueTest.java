package course.concurrency.m3_shared.hw;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlockingQueueTest {

    @Tag("SequentialEnqueue")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3000, 1_000_000})
    void shouldReturnActualSizeAfterAddElements(int elements) throws InterruptedException {

        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(elements);

        for (int i = 0; i < elements; i++) {
            queue.enqueue(i);
        }
        assertEquals(elements, queue.size());
    }


    @Tag("SequentialDequeue")
    @ParameterizedTest
    @CsvSource({"1, 12", "9, 12", "500, 1000"})
    void shouldReturnCorrectLastElement(int index, int size) throws InterruptedException {

        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(size);

        for (int i = 0; i < size; i++) {
            queue.enqueue(i);
        }

        for (int i = 0; i < index; i++) {
            queue.dequeue();
        }

        assertEquals(index, queue.dequeue());
    }


    @Tag("ConcurrentDequeue")
    @ParameterizedTest
    @ValueSource(ints = {1, 999, 10_000})
    void shouldNotHaveElementsAfterConcurrentDequeued(int elements) throws InterruptedException {

        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(elements);

        for (int i = 1; i < elements; i++) {
            try {
                queue.enqueue(i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < elements; i++) {
            new Thread(() -> {
                try {
                    latch.await();
                    queue.dequeue();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

        latch.countDown();
        sleep(1_000L);

        assertEquals(0, queue.size(), "Not all elements dequeued");
    }


    @Tag("ConcurrentEnqueue")
    @RepeatedTest(5)
    void shouldReturnActualSizeAfterConcurrentEnqueued() throws InterruptedException {

        int expectedSize = 1000;
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(expectedSize);

        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < expectedSize; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    latch.await();
                    queue.enqueue(finalI);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
        latch.countDown();
        sleep(100L);
        assertEquals(expectedSize, queue.size());
    }


    @Tag("ConcurrentEnqueue")
    @RepeatedTest(1)
    void shouldReturnActualSizeInEndQueue() throws InterruptedException {

        int maxSize = 99;

        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(maxSize);

        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < maxSize; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    latch.await();
                    queue.enqueue(finalI);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

        latch.countDown();
        sleep(500L);
        for (int i = 0; i < maxSize; i++) {
            System.out.println(queue.dequeue());
        }
        assertEquals(0, queue.size());
    }


    @Tags({@Tag("ConcurrentEnqueue"), @Tag("CF")})
    @RepeatedTest(5)
    void shouldReturnActualSizeInEndQueue1() {

        int maxSize = 50;
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(maxSize);

        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < maxSize; i++) {
                try {
                    queue.enqueue(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).thenRunAsync(() -> {
            for (int i = 0; i < maxSize; i++) {
                try {
                    queue.dequeue();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).join();

        assertEquals(0, queue.size());
    }

}


