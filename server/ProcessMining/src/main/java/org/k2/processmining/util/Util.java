package org.k2.processmining.util;

import org.k2.processmining.exception.BadRequestException;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.model.log.LogType;
import org.k2.processmining.model.user.Administrator;
import org.k2.processmining.model.user.User;
import org.k2.processmining.security.admin.AdminDetails;
import org.k2.processmining.security.user.MyUserDetails;
import org.k2.processmining.service.AdminService;
import org.k2.processmining.service.UserService;
import org.k2.processmining.support.mining.model.DiagramType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import sun.misc.BASE64Encoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by nyq on 2017/6/19.
 */


public class Util {


    static Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();

    public static final int LOG_NAME_LENGTH = 255;
    public static final int METHOD_NAME_LENGTH = 255;


    public static String getUUIDString() {
        return UUID.randomUUID().toString();
    }

    public static String getMergeName(String eventLog1Name, String eventLog2Name, String mergeMethodName) {
        int i = eventLog1Name.lastIndexOf(".");
        int j = eventLog2Name.lastIndexOf(".");
        if (i != -1) {
            eventLog1Name = eventLog1Name.substring(0, i);
        }
        if (j != -1) {
            eventLog2Name = eventLog2Name.substring(0, j);
        }
        if (6+mergeMethodName.length()+eventLog1Name.length()+eventLog2Name.length() > LOG_NAME_LENGTH) {
            int len = (LOG_NAME_LENGTH - 6) / 3;
            mergeMethodName = mergeMethodName.substring(0, len);
            eventLog1Name = eventLog1Name.substring(0, len);
            eventLog2Name = eventLog2Name.substring(0, len);
        }
        return mergeMethodName + "-" + eventLog1Name + "-" + eventLog2Name + ".xes";
    }

    public static String getNormalizeName(String rawLogName) {
        String suffix = "-normal.txt";
        int i = rawLogName.lastIndexOf(".");
        if (i != -1) {
            rawLogName = rawLogName.substring(0, i);
        }
        String normalLogName = rawLogName + suffix;
        if (normalLogName.length() > LOG_NAME_LENGTH) {
            normalLogName = rawLogName.substring(0, LOG_NAME_LENGTH-suffix.length()) + suffix;
        }
        return normalLogName;
    }

    public static String getTransEventName(String normalLogName) {
        String suffix = "-event.xes";
        int i = normalLogName.lastIndexOf(".");
        if (i != -1) {
            normalLogName = normalLogName.substring(0, i);
        }
        String eventLogName = normalLogName + suffix;
        if (eventLogName.length() > LOG_NAME_LENGTH) {
            eventLogName = normalLogName.substring(0, LOG_NAME_LENGTH-suffix.length()) + suffix;
        }
        return eventLogName;
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
            throw new BadRequestException("The type of diagram does not exist!");
        }
    }

    public static String validateString(String keyWord) {
        if (keyWord == null || (keyWord=keyWord.trim()).length() == 0) {
            throw new BadRequestException("Illegal keyWord!");
        }
        return keyWord;
    }

    public static boolean isAjax(HttpServletRequest request) {
        String requestedWithHeader = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWithHeader);
    }

    public static User getLoginUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails){
            User user = ((MyUserDetails)principal).getUser();
            user.setPassword("");
            return user;
        }else {
            User user = new User();
            user.setId("1");
            user.setEmail("y2k@y2k.com");
            user.setName("y2k");
            return user;
        }
    }

    public static String encryptStr(String str){
        if ("".equals(str))
            return null;
        return md5PasswordEncoder.encodePassword(str,"666");
    }

    public static Cookie getCookie(Cookie[] cookies, String cookieName){
        for (Cookie cookie:cookies){
            if (cookie.getName().equals(cookieName))
                return cookie;
        }
        return null;
    }

    public static void delCookie(HttpServletResponse response, Cookie cookie){
        if (cookie!=null){
//            cookie.setPath("/");
//            System.out.println("delCookie:"+cookie);
            cookie.setMaxAge(0);
            cookie.setValue(null);
            response.addCookie(cookie);
        }
    }

}
