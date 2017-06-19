package org.k2.processmining.support.reflect;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;

/**
 * Created by nyq on 2017/6/15.
 */
public class ReflectUtil {
    @SuppressWarnings("unchecked")
    public static <T> T getInstanceFromJar(String jarPath, String classPath) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        return getInstanceFromJar(Collections.singletonList(jarPath), classPath);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstanceFromJar(List<String> jarPaths, String classPath) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String[] j = new String[jarPaths.size()];
        jarPaths.toArray(j);
        return getInstanceFromJar(j, classPath);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstanceFromJar(String[] jarPaths, String classPath) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        URL[] urls = new URL[jarPaths.length];
        for (int i = 0; i < jarPaths.length; i++) {
            urls[i] = new URL("file:"+jarPaths[i]);
        }
        URLClassLoader myClassLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
        Class<?> myClass = myClassLoader.loadClass(classPath);
        Object o = myClass.newInstance();
        return (T) o;
    }
}
