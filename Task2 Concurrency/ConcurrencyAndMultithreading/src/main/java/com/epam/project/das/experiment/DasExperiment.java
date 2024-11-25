package com.epam.project.das.experiment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DasExperiment {

    public static void threadsWorkWithHashMapAndConcurrentModificationException() {
        // Get ConcurrentModificationException with HashMap, threads work before catching ConcurrentModificationException
        Map<Integer, Integer> map = new HashMap<>();
        // add elements
        Thread writerThread = new Thread(() -> {
            for (int i = 1; i <= 100; i++) {
                map.put(i, i * 2); // Add elements
                System.out.println("Writer: Added Key = " + i + ", Value = " + (i * 2));
                try {
                    Thread.sleep(10); // Simulate delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        // sum values
        Thread readerThread = new Thread(() -> {
            while (true) {
                int sum = 0;
                for (int value : map.values()) {
                    sum += value;
                }
                System.out.println("Reader: Current Sum = " + sum);
                try {
                    Thread.sleep(100); // Simulate periodic reading
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        writerThread.start();
        readerThread.start();

        try {
            writerThread.join();// Wait for writerThread to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        readerThread.interrupt(); // Stop readerThread
    }

    public static void threadsWorkWithConcurrentHashMap() {
        // Problem fix with ConcurrentHashMap and Collections.synchronizedMap()
        Map<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        // add elements
        Thread writerThread = new Thread(() -> {
            for (int i = 1; i <= 100; i++) {
                concurrentHashMap.put(i, i * 2);
                System.out.println("Writer: Added Key = " + i + ", Value = " + (i * 2));
                try {
                    Thread.sleep(10); // Some delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        // sum
        Thread readerThread = new Thread(() -> {
            while (true) {
                int sum = concurrentHashMap.values().stream().mapToInt(Integer::intValue).sum();
                System.out.println("Reader: Current Sum = " + sum);
                try {
                    Thread.sleep(100); // Periodic reading
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        writerThread.start();
        readerThread.start();

        try {
            writerThread.join(); // Wait for writerThread to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        readerThread.interrupt(); // Stop readerThread
    }

    public static void threadsWorkWithSynchronizedMap() {
        Map<Integer, Integer> map = Collections.synchronizedMap(new HashMap<>());

        Thread writerThread = new Thread(() -> {
            for (int i = 1; i <= 100; i++) {
                map.put(i, i * 2);
                System.out.println("Writer: Added Key = " + i + ", Value = " + (i * 2));
                try {
                    Thread.sleep(10); // Simulate some delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        Thread readerThread = new Thread(() -> {
            while (true) {
                int sum = 0;
                synchronized (map) {
                    for (int value : map.values()) {
                        sum += value;
                    }
                }
                System.out.println("Reader: Current Sum = " + sum);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        writerThread.start();
        readerThread.start();

        try {
            writerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        readerThread.interrupt();
    }

}
