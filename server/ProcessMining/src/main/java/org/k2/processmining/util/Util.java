package org.k2.processmining.util;

import org.k2.processmining.exception.JSONBadRequestException;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.model.log.LogType;
import org.k2.processmining.model.user.User;
import org.k2.processmining.support.mining.model.DiagramType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    public static String getNormalizeName(String rawLogName) {
        int i = rawLogName.lastIndexOf(".");
        if (i != -1) {
            rawLogName = rawLogName.substring(0, i);
        }
        return rawLogName + "-normal.txt";
    }

    public static String getTransEventName(String normalLogName) {
        int i = normalLogName.lastIndexOf(".");
        if (i != -1) {
            normalLogName = normalLogName.substring(0, i);
        }
        return normalLogName + "-event.xes";
    }

    public static boolean isActive(AbstractLog log) {
        return log != null && LogState.isActive(log.getState());
    }

    public static boolean isActiveAndShared(AbstractLog log) {
        return isActive(log) && LogShareState.isShared(log.getIsShared());
    }

    public static boolean isBelongTo(AbstractLog log, User user) {
        return log != null && user != null && user.getId() != null && user.getId().equals(log.getUserId());
    }

    public static boolean isActiveAndBelongTo(AbstractLog log, User user) {
        return isActive(log) && isBelongTo(log, user);
    }

    public static String encodeForURL(String name) {
        try {
            name = URLEncoder.encode(name,"UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static String[] getLogTypeNames() {
        LogType[] logTypes = LogType.values();
        String[] names = new String[logTypes.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = logTypes[i].value;
        }
        return names;
    }

    public static DiagramType toDiagramType(String type) {
        try {
            return DiagramType.valueOf(type);
        }
        catch (Exception e) {
            throw new JSONBadRequestException("The type of diagram does not exist!");
        }
    }
}
