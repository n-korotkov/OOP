package ru.n_korotkov.oop.pizzeria;

import ru.n_korotkov.oop.pizzeria.PizzeriaConfig.CookConfig;

public class Cook implements Runnable {

    private final int cookingDelay;
    private final SynchronizedQueue<Integer> incomingOrders;
    private final SynchronizedQueue<Integer> storage;

    public Cook(CookConfig config, SynchronizedQueue<Integer> incomingOrders, SynchronizedQueue<Integer> storage) {
        this.cookingDelay = config.cookingDelay;
        this.incomingOrders = incomingOrders;
        this.storage = storage;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int order = incomingOrders.take();
                System.out.printf("[%d] accepted\n", order);
                Thread.sleep(cookingDelay);
                System.out.printf("[%d] ready\n", order);
                storage.put(order);
                System.out.printf("[%d] sent to storage\n", order);
            }
        } catch (InterruptedException e) {
            return;
        }
    }

}
