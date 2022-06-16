package ru.n_korotkov.oop.pizzeria;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class SynchronizedQueue<T> {

    private int capacity;
    private Queue<T> queue;

    public SynchronizedQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new ArrayDeque<>();
    }

    public int getCapacity() {
        return capacity;
    }

    public synchronized void put(T value) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.add(value);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T value = queue.remove();
        notifyAll();
        return value;
    }

    public synchronized List<T> takeAtMost(int maxElements) throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        List<T> values = new ArrayList<>(maxElements);
        for (int i = 0; i < maxElements && !queue.isEmpty(); i++) {
            values.add(queue.remove());
        }
        notifyAll();
        return values;
    }

    public synchronized boolean offer(T value) {
        if (queue.size() == capacity) {
            return false;
        }
        queue.add(value);
        notifyAll();
        return true;
    }

    public synchronized T poll() {
        if (queue.isEmpty()) {
            return null;
        }
        T value = queue.remove();
        notifyAll();
        return value;
    }

}
