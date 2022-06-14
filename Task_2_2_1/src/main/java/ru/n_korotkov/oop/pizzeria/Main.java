package ru.n_korotkov.oop.pizzeria;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;

public class Main {

    private static final int orderDelay = 2000;

    public static void main(String[] args) throws InterruptedException {
        Gson gson = new Gson();
        InputStream configStream = Main.class.getClassLoader().getResourceAsStream("config.json");

        PizzeriaConfig config = gson.fromJson(new InputStreamReader(configStream), PizzeriaConfig.class);
        Pizzeria pizzeria = new Pizzeria(config);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> pizzeria.stop()));

        int order = 0;
        while (true) {
            order++;
            pizzeria.putOrder(order);
            Thread.sleep(orderDelay);
        }

    }

}
