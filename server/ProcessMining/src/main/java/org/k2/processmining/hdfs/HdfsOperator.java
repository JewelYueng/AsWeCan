package org.k2.processmining.hdfs;

/**
 * Created by Aria on 2017/6/9.
 */
public class HdfsOperator {

    private volatile static HdfsOperator hdfsOperator;
    static final String url = "http://116.56.129.93:50070/webhdfs/v1/ProcessMining/";
    static final String LISTSTATUS = "op=LISTSTATUS";
    static final String MKDIRS = "op=MKDIRS";
    static final String CREATE = "op=CREATE";
    static final String DELETE = "op=DELETE";
    static final String OPEN = "op=OPEN";
    public static final int Success = 200;
    public static final String defulstUser = "abc";
    public static final String rawlog = "RawAbstractLog";
    public static final String eventlog = "EventAbstractLog";
    public static final String normlog = "NormLog";
    public static final String models = "Models";


    private HdfsOperator(){}
    public static HdfsOperator getInstance(){
        if(hdfsOperator == null){
            synchronized(HdfsOperator.class){
                if(hdfsOperator == null){
                    hdfsOperator = new HdfsOperator();
                }
            }
        }
        return hdfsOperator;
    }

    /**
     *
     * @param user 用户名
     * @param type  日志类型
     * @return
     */
    public String getLocationString(String user,int type){

//        locationStr = url + user + "/" + typeStr;
        return null;
    }

    /**
     * 将文字的type转成对应的int
     * @param type
     * @return
     */
    public int getType(String type){
        int Type = 0;
        if(type.equals(eventlog)){
            Type = 1;
        }
        if(type.equals(normlog)){
            Type = 2;
        }
        if(type.equals(models)){
            Type = 3;
        }
        return Type;

    }

}
