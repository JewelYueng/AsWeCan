package org.k2.processmining.cache;

import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.support.algorithm.Algorithm;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by nyq on 2017/6/29.
 */
@Component("netKeyGenerator")
public class NetKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object o, Method method, Object... objects) {
        Algorithm algorithms = (Algorithm) objects[0];
        EventLog eventLog = (EventLog) objects[1];
        TreeMap<String, Object> params = new TreeMap<>((Map<String,Object>) objects[3]);
        StringBuilder sb = new StringBuilder();
        sb.append(o.getClass().getName()).append("-")
                .append(method.getName()).append("-")
                .append(algorithms.getId()).append("-")
                .append(eventLog.getId());
        params.forEach((key, value)->{
            sb.append("-").append(value.toString());
        });
        return sb.toString();
    }
}
