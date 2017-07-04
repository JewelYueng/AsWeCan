import org.k2.processmining.model.log.RawLog;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Aria on 2017/6/9.
 */
public class Main {

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        String s = "N+N7OtUqhB42haFS5lO/vQ==";
//        System.out.println(urlEncode(s));
//        System.out.println(urlDecode(urlEncode(s)));

        Runnable r1 = () -> System.out.println("hello");
        List<RawLog> list = new ArrayList<RawLog>();
        Collections.sort(list, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {

                return 1;
            }
        });

        Collections.sort(list,(p1,p2)->{
            p1.getIsShared();
            return 1;

                });

//        String str = () -> System.out.println("???");
    }
    public static String encode(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();

        return base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
    }

    public static boolean decode(String newPwd,String oldPwd) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (newPwd.equals(encode(oldPwd)))
            return true;
        else return false;
    }

    public static String urlEncode(String s){
        try {
            return URLEncoder.encode(s,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String urlDecode(String s){
        try {
            return URLDecoder.decode(s,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void shell_sort(int []array){
        int length = array.length;
        int step = length / 2;
        int temp;

        while (step > 0){
            for (int i=step;i<length;i++){
                temp = array[i];
                int j = i - step;
                while (j >=0 && temp < array[j]){
                    array[j + step] = array[j];
                    j -= step;
                }
            }
        }
    }
}
