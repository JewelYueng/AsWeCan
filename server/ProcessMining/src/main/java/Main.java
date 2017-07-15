import org.k2.processmining.model.log.RawLog;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
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

        String str = "你好";
        System.out.println(str.length());

    }

}
