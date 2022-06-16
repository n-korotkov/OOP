package ru.n_korotkov.oop.pizzeria;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SynchronizedQueueTest {

    @Test
    void putTest() throws InterruptedException {
        SynchronizedQueue<Integer> queue = new SynchronizedQueue<>(5);
        for (int i = 0; i < queue.getCapacity(); i++) {
            queue.put(i);
        }

        Thread takeThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                queue.take();
            } catch (InterruptedException e) {
                return;
            }
        });

        takeThread.run();
        queue.put(0);
    }

    @Test
    void takeTest() throws InterruptedException {
        SynchronizedQueue<Integer> queue = new SynchronizedQueue<>(5);
        for (int i = 0; i < queue.getCapacity(); i++) {
            queue.put(i);
        }

        for (int i = 0; i < queue.getCapacity(); i++) {
            assertEquals(i, queue.take());
        }

        Thread putThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                queue.put(0);
            } catch (InterruptedException e) {
                return;
            }
        });

        putThread.run();
        assertEquals(0, queue.take());
    }

    @Test
    void takeAtMostTest() throws InterruptedException {
        SynchronizedQueue<Integer> queue = new SynchronizedQueue<>(5);
        for (int i = 0; i < queue.getCapacity(); i++) {
            queue.put(i);
        }

        assertThat(queue.takeAtMost(3)).containsExactly(0, 1, 2);
        assertThat(queue.takeAtMost(3)).containsExactly(3, 4);

        Thread putThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                queue.put(0);
            } catch (InterruptedException e) {
                return;
            }
        });

        putThread.run();
        assertThat(queue.takeAtMost(3)).containsExactly(0);
    }

    @Test
    void offerTest() {
        SynchronizedQueue<Integer> queue = new SynchronizedQueue<>(5);
        for (int i = 0; i < queue.getCapacity(); i++) {
            assertTrue(queue.offer(i));
        }

        assertFalse(queue.offer(0));
    }

    @Test
    void pollTest() {
        SynchronizedQueue<Integer> queue = new SynchronizedQueue<>(5);
        for (int i = 0; i < queue.getCapacity(); i++) {
            queue.offer(i);
        }

        for (int i = 0; i < queue.getCapacity(); i++) {
            assertEquals(i, queue.poll());
        }

        assertNull(queue.poll());
    }

}
