package com.epam.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The code does not introduce deadlock because all threads are acquiring the same lock (ReentrantLock) in the same order.
 * Using Lock instead of explicit synchronization block.
 *
 * Othewise would be:
 * synchronized (collection) {
 *      collection.add(randomNumber);
 * }
 * */
public class Main {

    private static final List<Integer> collection = new ArrayList<>();
    private static final Lock lock = new ReentrantLock(); // to ensure thread-safety and prevent race conditions

    public static void main(String[] args) {
        // 1 Write random numbers to the collection
        Thread writerThread = new Thread(() -> {
            while (true) {
                try {
                    // Generate a random number and add it to the collection
                    int randomNumber = new Random().nextInt(100);

                    lock.lock(); // thread-safe access to the collection
                    collection.add(randomNumber);
                    lock.unlock();

                    System.out.println("Added number: " + randomNumber);
                    Thread.sleep(500); // before adding next num
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // 2 Calculate and print the sum of numbers in the collection
        Thread sumThread = new Thread(() -> {
            while (true) {
                try {
                    lock.lock(); // lock to safely read the collection
                    int sum = collection.stream().mapToInt(Integer::intValue).sum();
                    lock.unlock();

                    System.out.println("Sum of numbers: " + sum);
                    Thread.sleep(1000); // before calculating again
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // 3Calculate and print the average of the numbers in the collection
        Thread avgThread = new Thread(() -> {
            while (true) {
                try {
                    lock.lock(); // Lock to safely read the collection
                    if (!collection.isEmpty()) {
                        double average = collection.stream().mapToInt(Integer::intValue).average().orElse(0);
                        System.out.println("Average of numbers: " + average);
                    } else {
                        System.out.println("Collection is empty, cannot calculate average.");
                    }
                    lock.unlock();

                    Thread.sleep(1500); // before calculating again
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        writerThread.start();
        sumThread.start();
        avgThread.start();
    }

}