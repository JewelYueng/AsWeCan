import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Aria on 2017/6/9.
 */
public class Main {

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String s = "N+N7OtUqhB42haFS5lO/vQ==";
        System.out.println(urlEncode(s));
        System.out.println(urlDecode(urlEncode(s)));
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
}
