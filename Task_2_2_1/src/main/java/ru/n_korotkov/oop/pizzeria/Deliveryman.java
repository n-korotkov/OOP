package ru.n_korotkov.oop.pizzeria;

import java.util.stream.Collectors;

import ru.n_korotkov.oop.pizzeria.PizzeriaConfig.DeliverymanConfig;

public class Deliveryman implements Runnable {

    private final int deliveryDelay;
    private final int bootCapacity;
    private final SynchronizedQueue<Integer> storage;

    public Deliveryman(DeliverymanConfig config, SynchronizedQueue<Integer> storage) {
        this.bootCapacity = config.bootCapacity;
        this.deliveryDelay = config.deliveryDelay;
        this.storage = storage;
    }

    @Override
    public void run() {
        try {
            while (true) {
                var orders = storage.takeAtMost(bootCapacity);
                String orderString = orders.stream().map(Object::toString).collect(Collectors.joining(", "));
                System.out.printf("[%s] taken from storage\n", orderString);

                for (int order : orders) {
                    Thread.sleep(deliveryDelay);
                    System.out.printf("[%d] delivered\n", order);
                }
            }
        } catch (InterruptedException e) {
            return;
        }
    }

}
