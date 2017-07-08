package org.k2.processmining.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Aria on 2017/6/20.
 */
public class GsonParser {
    public static <T> T fromJson(String json,Class<T> clazz){
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json,clazz);
    }

    public static String parseToCodeAndMessage(String code,String message){
        return "{\"code\":\""+code+"\",\"message\":\""+message+"\"}";
    }
}
