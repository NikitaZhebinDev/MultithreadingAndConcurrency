package com.epam.project.das.experiment;

import java.util.HashMap;
import java.util.Map;

public class CustomThreadSafeMap <K, V> {
    private final Map<K, V> map;

    public CustomThreadSafeMap() {
        map = new HashMap<>();
    }

    public synchronized void put(K key, V value) {
        map.put(key, value);
    }

    public synchronized V get(K key) {
        return map.get(key);
    }

    public synchronized V remove(K key) {
        return map.remove(key);
    }

    public synchronized boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public synchronized int size() {
        return map.size();
    }
}
