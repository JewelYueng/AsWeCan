import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.UUID;

/**
 * Created by Aria on 2017/6/9.
 */
public class Main {

    public static void main(String[] args){
        byte b1 = (byte) 0XEF;
        byte b2 = (byte) 0XBB;
        byte b3 = (byte) 0XBF;

        String str = UUID.randomUUID().toString();
        System.out.println(str+"   size:"+str.length());
        System.out.println(UUID.randomUUID());

        System.out.println("b1:"+b1+"b2:"+b2+"b3:"+b3);
    }
}
