package com.epam.project.das.experiment;

import java.util.HashMap;
import java.util.Map;

public class NonSynchronizedMap<K, V> {
    private final Map<K, V> map;

    public NonSynchronizedMap() {
        map = new HashMap<>();
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

    public V get(K key) {
        return map.get(key);
    }

    public V remove(K key) {
        return map.remove(key);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public int size() {
        return map.size();
    }
}
