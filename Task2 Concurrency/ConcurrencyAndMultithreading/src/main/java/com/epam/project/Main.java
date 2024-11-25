package com.epam.project;

import com.epam.project.das.experiment.CustomThreadSafeMap;
import com.epam.project.das.experiment.DasExperiment;
import com.epam.project.das.experiment.NonSynchronizedMap;

public class Main {

    public static void main(String[] args) {

        // --------------- Das Experiment ---------------

        /*threadsWorkWithHashMapAndConcurrentModificationException();

        threadsWorkWithConcurrentHashMap();

        threadsWorkWithSynchronizedMap();

        customNonSynchronizedMapUsage();

        customThreadSafeMapUsage();*/


        // --------------- Performance Test ---------------
        System.out.println("Starting Performance Tests...\n");

        // Measure time for threadsWorkWithHashMapAndConcurrentModificationException
        // (Java 17 - 1610 milliseconds; Java 11 - 1624 milliseconds; Java 8 - 1593 milliseconds)
        measureExecutionTime("Threads Work With HashMap And ConcurrentModificationException",
            DasExperiment::threadsWorkWithHashMapAndConcurrentModificationException);

        // Measure time for threadsWorkWithConcurrentHashMap
        // (Java 17 - 1594 milliseconds; Java 11 - 1608 milliseconds; Java 8 - 1596 milliseconds)
        measureExecutionTime("Threads Work With ConcurrentHashMap", DasExperiment::threadsWorkWithConcurrentHashMap);

        // Measure time for threadsWorkWithSynchronizedMap
        // (Java 17 - 1598 milliseconds; Java 11 - 1598 milliseconds; Java 8 - 1598 milliseconds)
        measureExecutionTime("Threads Work With SynchronizedMap", DasExperiment::threadsWorkWithSynchronizedMap);

        // Measure time for customThreadSafeMapUsage
        // (Java 17 - 1 milliseconds; Java 11 - 10 milliseconds; Java 8 - 1 milliseconds)
        measureExecutionTime("CustomThreadSafeMap Usage", Main::customThreadSafeMapUsage);

        // Measure time for customNonSynchronizedMapUsage
        // (Java 17 - 0 milliseconds; Java 11 - 14 milliseconds; Java 8 - 1 milliseconds)
        measureExecutionTime("CustomNonSynchronizedMap Usage", Main::customNonSynchronizedMapUsage);
    }

    public static void customNonSynchronizedMapUsage() {
        // --------------- CustomThreadSafeMap usage example ---------------
        CustomThreadSafeMap<Integer, String> safeMap = new CustomThreadSafeMap<>();
        // Writing to the map from one thread
        Thread writerThread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                safeMap.put(i, "Writer value " + i);
            }
        });
        // Reading from the map from another thread
        Thread readerThread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                String value = safeMap.get(i);
                System.out.println("Reading Key = " + i + " Value = " + value);
            }
        });
        writerThread.start();
        readerThread.start();
    }

    public static void customThreadSafeMapUsage() {
        // --------------- CustomThreadSafeMap usage example ---------------
        NonSynchronizedMap<Integer, String> unsafeMap = new NonSynchronizedMap<>();
        // Writing to the map from one thread
        Thread writerThread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                unsafeMap.put(i, "Writer value " + i);
            }
        });
        // Reading from the map from another thread
        Thread readerThread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                String value = unsafeMap.get(i);
                System.out.println("Reading Key = " + i + " Value = " + value);
            }
        });
        writerThread.start();
        readerThread.start();
    }

    /**
     * Utility method to measure execution time of a method.
     *
     * @param description The description of the test
     * @param task        The task to measure
     */
    public static void measureExecutionTime(String description, Runnable task) {
        long startTime = System.nanoTime();
        try {
            task.run();
        } catch (Exception e) {
            System.out.println("Error during " + description + ": " + e.getMessage());
        }
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println(description + " took " + duration / 1_000_000 + " milliseconds.\n");
    }
}