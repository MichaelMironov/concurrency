package course.concurrency.m3_shared.hw;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MyBlockingQueue<E> implements Queue<E> {
    private final Queue<E> deque;

    public MyBlockingQueue(int maxSize) {
        this.maxSize = maxSize;
        deque = new ArrayDeque<>(maxSize);
    }

    public MyBlockingQueue() {
        this.maxSize = Integer.MAX_VALUE;
        deque = new ArrayDeque<>(maxSize);
    }

    private final Lock lock = new ReentrantLock();


    private final int maxSize;

    /*
        Методы dequeue() и enqueue() блокируют продвижение,
        до тех пор пока очередь не перейдет в нужное состояние,
        т.е. будет не пустой и не полной
     */

    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();


    /*
        Условный предикат - notEmpty. Должна быть проверка,
        что очередь не пуста, в противном случае - ждать
     */
    public E dequeue() throws InterruptedException {

        lock.lockInterruptibly();

        try {
            while (deque.isEmpty()) {
                notEmpty.await();
            }
            return deque.poll();
        } finally {
            lock.unlock();
        }

    }

    /*
        Условный предикат - notFull. Должна быть проверка,
        что очередь не заполнена, в противном случае - ждать
     */
    public void enqueue(E element) throws InterruptedException {

        lock.lockInterruptibly();

        try {
            while (deque.size() == maxSize) {
                notFull.await();
            }
            deque.add(element);
            notFull.signal();
        } finally {
            lock.unlock();
        }

    }

    @Override
    public String toString() {
        return deque.toString();
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E element() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return Queue.super.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Spliterator<E> spliterator() {
        return Queue.super.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return Queue.super.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return Queue.super.parallelStream();
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int size() {
        return deque.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return Queue.super.toArray(generator);
    }

}
