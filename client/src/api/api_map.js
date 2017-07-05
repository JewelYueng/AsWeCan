// const base_url = ""
const base_url = "http://116.56.129.93:8088/AssWeCan"
// const base_url = "http://192.168.0.100:8080"

const
  GET = "get",
  HEAD = "head",
  DELETE = "delete",
  POST = "post",
  PUT = "put",
  PATCH = "patch"

export default {
  __base_url__: base_url,
// 日志文件管理
  // 原始日志
  "searchShareRawLog":[GET,"/rawLog/sharedLogs/search"],
  "searchRawLog": [GET, "/rawLog/search"],
  "getShareRawLog": [GET, "/rawLog/sharedLogs"],
  "getRawLog": [GET, "/rawLog"],
  "shareRawLog": [POST, "/rawLog/share"],
  "unShareRawLog": [POST, "/rawLog/unShare"],
  "uploadRawLog": [POST, "/rawLog/upload"],
  "downLoadRawLog": [GET, "/rawLog/download"],
  "deleteRawLog": [DELETE, "/rawLog/delete"],
  "getRawLogPage": [GET, '/rawLog/page'],
  "getShareRawPage": [GET, '/rawLog/sharedLogs/page'],
  // 规范化日志
  "searchNormalLog": [GET, "/normalLog/search"],
  "searchSharedNormalLog": [GET, '/normalLog/sharedLogs/search'],
  "getShareNormalLog": [GET, "/normalLog/sharedLogs"],
  "getNormalLog": [GET, "/normalLog"],
  "shareNormalLog": [POST, "/normalLog/share"],
  "unShareNormalLog": [POST, "/normalLog/unShare"],
  "downLoadNormalLog": [GET, "/normalLog/download"],
  "uploadNormalLog": [POST, "/normalLog/upload"],
  "deleteNormalLog": [DELETE, "/normalLog/delete"],
  "getNormalLogPage": [GET, '/normalLog/page'],
  "getShareNormalPage": [GET, '/normalLog/sharedLogs/page'],
  // 事件日志
  "searchEventLog": [GET, "/eventLog/search"],
  "searchSharedEventLog": [GET, '/eventLog/sharedLogs/search'],
  "getShareEventLog": [GET, "/eventLog/sharedLogs"],
  "getEventLog": [GET, "/eventLog"],
  "shareEventLog": [POST, "/eventLog/share"],
  "unShareEventLog": [POST, "/eventLog/unShare"],
  "downLoadEventLog": [GET, "/eventLog/download"],
  "deleteEventLog": [DELETE, "/eventLog/delete"],
  "getEventLogPage": [GET, '/eventLog/page'],
  "getShareEventPage": [GET, '/eventLog/sharedLogs/page'],
//  事件预处理
  "normalize": [POST, "/rawLog/normalize"],
  "toEventLog": [POST, "/normalLog/toEventLog"],
  // 日志融合
  "getMergeMethods": [GET, "/merge/method"],
  "merge": [POST, "/merge"],
// 流程挖掘
  "getMiningMethods": [GET, "/mining/method"],
  "mining": [POST, "/mining"],

//  用户系统的接口
  "login": [POST, '/home/login'],
  "register": [POST,'/user/register'],
  "getUser": [GET, '/user/getUser'],
  "modifyPass": [POST, "/user/password"],
  "logout": [POST, '/home/logout'],
  'checkOut': [POST, '/user/checkout']
}
