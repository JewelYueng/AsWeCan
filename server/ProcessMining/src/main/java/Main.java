import org.k2.processmining.model.log.RawLog;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by Aria on 2017/6/9.
 */
public class Main {

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        Integer i = new Integer(5);

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

    public static boolean testStack(){
        int []pushA = {1,2,3,4,5};
        int []popA = {4,5,3,2,1};
        Stack<Integer> stack = new Stack<>();
        int popIndex = 0;
        for (int i=0;i<pushA.length;i++){
            stack.push(pushA[i]);
            while (!stack.empty() && stack.peek() == popA[popIndex]){
                stack.pop();
                popIndex++;
            }
        }

        return stack.empty();
    }
}
