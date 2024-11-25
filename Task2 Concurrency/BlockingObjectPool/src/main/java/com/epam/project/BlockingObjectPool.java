package com.epam.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Pool that blocks when it has no items or it is full.
 */
public class BlockingObjectPool {

    private final List<Object> pool;
    private final int maxSize;

    /**
     * Creates filled pool of passed size
     *
     * @param size of the pool
     */
    public BlockingObjectPool(int size) {
        this.maxSize = size;
        this.pool = new ArrayList<>(size);
    }

    /**
     * Gets object from pool or blocks if pool is empty
     *
     * @return object from pool
     */
    public synchronized Object get() {
        while (pool.isEmpty()) {
            try {
                wait(); // Block the current thread until an object is available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return pool.remove(pool.size() - 1); // Remove and return the last object
    }

    /**
     * Puts object to pool or blocks if pool is full
     *
     * @param object to be taken back to pool
     */
    public synchronized void take(Object object) {
        while (pool.size() >= maxSize) {
            try {
                wait(); // Block the current thread until there is space in the pool
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        pool.add(object); // Add object back to the pool
        notifyAll(); // Notify all waiting threads that the pool has been updated
    }
}

