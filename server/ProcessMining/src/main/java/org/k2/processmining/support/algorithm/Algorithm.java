package org.k2.processmining.support.algorithm;

import java.util.Map;

/**
 * Created by nyq on 2017/6/15.
 */
public class Algorithm<T> {
    private T algorithm;
    private Map<String, Object> configMap;

    public Algorithm() {}

    public Algorithm(T algorithm, Map<String, Object> configMap) {
        this.algorithm = algorithm;
        this.configMap =configMap;
    }

    public T getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(T algorithm) {
        this.algorithm = algorithm;
    }

    public Map<String, Object> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<String, Object> configMap) {
        this.configMap = configMap;
    }
}
