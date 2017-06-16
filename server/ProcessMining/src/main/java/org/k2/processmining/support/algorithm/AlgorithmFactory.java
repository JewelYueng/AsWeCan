package org.k2.processmining.support.algorithm;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by nyq on 2017/6/15.
 */
public abstract class AlgorithmFactory<T> {
    private Map<String, Algorithm<T>> algorithmMap = new ConcurrentHashMap<>();

    public Collection<Algorithm<T>> getAlgorithms() {
        return algorithmMap.values();
    }

    public Algorithm<T> put(String key, Algorithm<T> val) {
        return algorithmMap.put(key, val);
    }

    public Algorithm<T> deleteAlgorithm(String key) {
        return algorithmMap.remove(key);
    }

    public Algorithm<T> getAlgorithm(String key) {
        return algorithmMap.get(key);
    }
}
