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
    public static final String INVALID_PARAMS = "参数不合法，请检查输入！";
    public static final String MISS_PARAM = "参数缺失";
    public static final String INVALID_METHOD = "算法包未实现所需接口";
    //用户登录
    public static final String USER_LOGIN_SUCCESS = "用户登录成功！";
    public static final String USER_LOGIN_SUCCESS_CODE = "200";
    public static final String USER_NAME_NOT_FOUND = "邮箱不存在！";
    public static final String USER_NAME_NOT_FOUND_CODE = "401";
    public static final String USER_PASSWORD_WRONG = "密码错误！请检查后输入！";
    public static final String USER_PASSWORD_WRONG_CODE = "402";
    public static final String USER_VALIDATECODE_NULL = "验证码为空！";
    public static final String USER_VALIDATECODE_NULL_CODE = "403";
    public static final String USER_VALIDATECODE_WRONG = "验证码错误！请重新输入！";
    public static final String USER_VALIDATECODE_WRONG_CODE = "404";
    public static final String USER_EMAIL_FREEZE = "账号未激活！请检查邮件激活账号！";
    public static final String USER_EMAIL_FREEZE_CODE = "405";
    public static final String USER_FORBINDDEN = "账号被禁用！请联系管理员解禁！";
    public static final String USER_FORBIDDEN_CODE = "406";
    public static final String USER_LOGOUT_SUCCESS = "用户登出成功！";
    public static final String USER_LOGOUT_SUCCESS_CODE = "200";

    //用户注册
    public static final String REGISTER_VALIDATECODE_NULL = "验证码为空！";
    public static final String REGISTER_VALIDATECODE_NULL_CODE = "400";
    public static final String REGISTER_VALIDATECODE_WRONG = "验证码错误！";
    public static final String REGISTER_VALIDATECODE_WRONG_CODE = "401";
    public static final String REGISTER_PASSWORD_INCONSISTENT = "两次输入的密码不一致！";
    public static final String REGISTER_PASSWORD_INCONSISTENT_CODE = "402";
    public static final String REGISTER_EMAIL_NULL = "邮箱为空！";
    public static final String REGISTER_EMAIL_NULL_CODE = "403";
    public static final String REGISTER_EMAIL_REPEAT = "该邮箱已被注册！";
    public static final String REGISTER_EMAIL_REPEAT_CODE = "404";
    public static final String REGISTER_NAME_NULL = "用户名为空！";
    public static final String REGISTER_NAME_NULL_CODE = "405";
    public static final String REGISTER_EMAIL_WRONG = "邮箱不合法！";
    public static final String REGISTER_EMAIL_WRONG_CODE = "406";
    public static final String REGISTER_NAME_ILLEGAL = "用户名太长或不合法！";
    public static final String REGISTER_NAME_ILLEGAL_CODE = "407";
    public static final String REGISTER_SUCCESS = "注册成功！";
    public static final String REGISTER_SUCCESS_CODE = "200";


    //管理员登录
    public static final String ADMIN_LOGIN_SUCCESS = "管理员登录成功";
    public static final String ADMIN_LOGIN_SUCCESS_CODE = "200";
    public static final String ADMIN_NOT_FOUND = "工作号不存在！";
    public static final String ADMIN_NOT_FOUND_CODE = "401";
    public static final String ADMIN_PASSWORD_WRONG = "密码错误！请检查后输入!";
    public static final String ADMIN_PASSWORD_WRONG_CODE = "402";
    public static final String ADMIN_VALIDATECODE_NULL = "验证码为空!";
    public static final String ADMIN_VALIDATECODE_NULL_CODE = "403";
    public static final String ADMIN_VALIDATECODE_WRONG = "验证码错误！请重新输入！";
    public static final String ADMIN_VALIDATECODE_WRONG_CODE = "404";
    public static final String ADMIN_LOGOUT_SUCCESS = "管理员登出成功！";
    public static final String ADMIN_LOGOUT_SUCCESS_CODE = "200";
    public static final String ACCESSDENIED = "你没有权限访问该页面";
    public static final String ACCESSDENIED_CODE = "403";

    //密码验证
    public static final String PASSWORD_WRONG = "原密码错误！";
    public static final String PASSWORD_WRONG_CODE = "400";
    public static final String NEWPASSWORD_INCONSISTENT = "两次输入的密码不一致";
    public static final String NEWPASSWORD_INCONSISTENT_CODE = "401";
    public static final String CHANGE_SUCCESS = "修改密码成功！";
    public static final String CHANGE_SUCCESS_CODE = "200";
}
