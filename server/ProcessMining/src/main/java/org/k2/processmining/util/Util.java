package org.k2.processmining.util;

import java.util.UUID;

/**
 * Created by nyq on 2017/6/19.
 */
public class Util {
    public static String getUUIDString() {
        return UUID.randomUUID().toString();
    }

    public static String getMergeName(String eventLog1Name, String eventLog2Name) {
        int i = eventLog1Name.lastIndexOf(".");
        int j = eventLog2Name.lastIndexOf(".");
        if (i != -1) {
            eventLog1Name = eventLog1Name.substring(0, i);
        }
        if (j != -1) {
            eventLog2Name = eventLog2Name.substring(0, j);
        }
        return eventLog1Name + "-" + eventLog2Name + "-merge.xes";
    }
}
