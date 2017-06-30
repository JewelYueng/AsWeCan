package org.k2.processmining.support.reflect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by nyq on 2017/6/15.
 */
public class ReflectUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectUtil.class);

    private ConcurrentHashMap<String, URLClassLoader> loaderMap = new ConcurrentHashMap<>();

    private ReflectUtil() {}

    private static class HOLDER {private static ReflectUtil INSTANCE = new ReflectUtil();}

    public static ReflectUtil getInstance() {return HOLDER.INSTANCE;}
    @SuppressWarnings("unchecked")
    public <T> T getInstanceFromJar(String id, String jarPath, String classPath) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        return getInstanceFromJar(id, Collections.singletonList(jarPath), classPath);
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstanceFromJar(String id ,List<String> jarPaths, String classPath) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String[] j = new String[jarPaths.size()];
        jarPaths.toArray(j);
        return getInstanceFromJar(id, j, classPath);
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstanceFromJar(String id, String[] jarPaths, String classPath) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        URL[] urls = new URL[jarPaths.length];
        for (int i = 0; i < jarPaths.length; i++) {
            urls[i] = new URL("file:"+jarPaths[i]);
        }
        URLClassLoader myClassLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
        Class<?> myClass = myClassLoader.loadClass(classPath);
        Object o = myClass.newInstance();
        loaderMap.put(id, myClassLoader);
        return (T) o;
    }

    public boolean closeClassLoader(String id) {
        try {
            URLClassLoader urlClassLoader = loaderMap.remove(id);
            if (urlClassLoader != null) {
                urlClassLoader.close();
            }
        }
        catch (IOException e) {
            LOGGER.error("Fail to close class loader<{}>:", id, e);
            return false;
        }
        return true;
    }
}
