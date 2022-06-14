package ru.n_korotkov.oop.pizzeria;

public class PizzeriaConfig {

    public static class CookConfig {
        public int cookingDelay;
    }

    public static class DeliverymanConfig {
        public int deliveryDelay;
        public int bootCapacity;
    }

    public int storageCapacity;
    public CookConfig[] cooks;
    public DeliverymanConfig[] deliverymen;

}
