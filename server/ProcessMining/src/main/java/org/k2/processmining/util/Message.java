package org.k2.processmining.util;

/**
 * Created by nyq on 2017/6/30.
 */
public class Message {
    public static final String DOWNLOAD_FAIL = "下载失败，请稍后尝试！";
    public static final String UPLOAD_FAIL = "上传失败，请稍后尝试！";
    public static final String NORMALIZE_FAIL = "规范化日志失败，请检查输入，稍后再尝试！";
    public static final String TRANS_TO_EVENT_LOG_FAIL = "转化为事件日志失败，请检查输入，稍后尝试！";
    public static final String MERGE_FAIL = "融合日志失败，请检查日志输入，稍后尝试！";
    public static final String INTERNAL_SERVER_ERROR = "系统出错，请稍后尝试！";
    public static final String MINING_FAIL = "流程挖掘失败，请检查输入，稍后尝试！";
    public static final String LOG_IS_NOT_EXIST = "日志不存在！";
    public static final String METHOD_IS_NOT_EXIST = "算法不存在！";
    public static final String USE_DIFF_LOG = "请使用两个不同的日志！";
    public static final String INVALID_EVENT_LOG = "无法解析事件日志！";
}
