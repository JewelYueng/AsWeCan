package org.k2.processmining.config;

/**
 * Created by nyq on 2017/7/3.
 */
public class AppConfig {
    public static final int PAGE_SIZE = 2;

    public static int pageNum(int count) {
        return (int) Math.ceil(1.*count/AppConfig.PAGE_SIZE);
    }
}
