package org.k2.processmining.support.normal.transform;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
* ���ϴ��ļ��Ĳ����У�������û��ϴ����ļ����һ����϶��ᷢ��ǵ�
* �����Ϊ�˽��������⣬���Բ���Ϊ�ϴ��ļ��Զ�����ķ�ʽ
* �Զ�������õĵ��ļ���ʽ���£�IP��ַ+ʱ���+��λ�����
* 
* @author Voishion
*
*/
public class IPTimeStamp {
        public SimpleDateFormat sdf = null;
        public String ip = null;
        public String filename = null;
        
        public IPTimeStamp() {
        }
        public IPTimeStamp(String ip, String filename) {
                this.ip = ip;
                this.filename = filename;
        }
        /**
         * �õ� IP��ַ+ʱ���+��λ����� �����ļ���
         * @return
         */
        
        public String getNewFileName(String newName,String state,String fileExt){
        	 StringBuffer buf = new StringBuffer();
        	buf.append(newName+"-"+state+"."+fileExt);
        	 return buf.toString();
        }
        public String getNewFileName(String newName,String state){
       	 StringBuffer buf = new StringBuffer();
     	buf.append(newName+"-"+state+".txt");
   	 return buf.toString();
       }
        public String getIPTimeRandName(){
                StringBuffer buf = new StringBuffer();
                if(this.ip != null){
                        String str[] = this.ip.split("\\.");
                        for(int i = 0; i < str.length; i++){
                                buf.append(this.addZero(str[i], 3));
                        }
                }//����IP��ַ
                buf.append(this.getTimeStamp());//��������
                Random random = new Random();
                for(int i = 0; i < 3; i++){
                        buf.append(random.nextInt(10));//ȡ��������׷�ӵ�StringBuffer
                }
    //            buf.append("."+this.getFileExt());//������չ��
                return buf.toString();
                
        }
        
        /**
         * ��0���������ָ��λ������ǰ�油0��
         * @param str
         * @param len
         * @return
         */
        private String addZero(String str,int len){
                StringBuffer s = new StringBuffer();
                s.append(str);
                while(s.length() < len){
                        s.insert(0, "0");
                }
                return s.toString();                
        }
        
        /**
         * ȡ��ʱ���
         * @return
         */
        private String getTimeStamp(){
                this.sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                return this.sdf.format(new Date());
        }
        
        /**
         * ��ȡ�ļ���չ��
         * @return
         */
        private String getFileExt(){
        int i = this.filename.lastIndexOf(".");//�������һ�����λ��        
            String extension = this.filename.substring(i+1);//ȡ����չ��
                return extension;
        }
}