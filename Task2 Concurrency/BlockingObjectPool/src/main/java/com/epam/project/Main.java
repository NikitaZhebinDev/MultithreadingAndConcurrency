package com.epam.project;

public class Main {

    public static void main(String[] args) {
        BlockingObjectPool pool = new BlockingObjectPool(3);

        // Simulate putting and getting objects in the pool
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    System.out.println("putting object " + i + " into the pool");
                    pool.take(new Object());
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    Object obj = pool.get();
                    System.out.println("gott object from the pool");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }

}