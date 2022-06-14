package ru.n_korotkov.oop.pizzeria;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Pizzeria {

    private final ExecutorService workerPool;
    private final SynchronizedQueue<Integer> incomingOrders;
    private final SynchronizedQueue<Integer> storage;

    public Pizzeria(PizzeriaConfig config) {
        int workersTotal = config.cooks.length + config.deliverymen.length;
        workerPool = Executors.newFixedThreadPool(workersTotal);
        incomingOrders = new SynchronizedQueue<>(Integer.MAX_VALUE);
        storage = new SynchronizedQueue<>(config.storageCapacity);

        for (var cookConfig : config.cooks)
            workerPool.execute(new Cook(cookConfig, incomingOrders, storage));
        
        for (var deliverymanConfig : config.deliverymen)
            workerPool.execute(new Deliveryman(deliverymanConfig, storage));
    }

    public void putOrder(int order) throws InterruptedException {
        incomingOrders.put(order);
        System.out.printf("[%d] arrived\n", order);
    }

    public void stop() {
        workerPool.shutdownNow();
    }

}
