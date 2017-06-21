const base_url = "http://192.168.0.100:8080"

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
  "searchRawLog": [GET, "/rawLog"],
  "getShareRawLog": [GET, "/rawLog/sharedLogs"],
  "getRawLog": [GET, "/rawLog"],
  "shareRawLog": [POST, "/rawLog/share"],
  "unShareRawLog": [POST, "/rawLog/unShare"],
  "uploadRawLog": [POST,"/rawLog/upload"],
  "downLoadRawLog": [POST, '/rawLog/download'],
  'deleteRawLog': [DELETE, '/rawLog/delete'],
  // 规范化日志
  "searchNormalLog": [GET, "/normalLog"],
  "getShareNormalLog": [GET, "/normalLog/sharedLogs"],
  "getNormalLog": [GET, "/normalLog"],
  "shareNormalLog": [POST, "/normalLog/share"],
  "unShareNormalLog": [POST, "/normalLog/unShare"],
  "uploadNormalLog": [POST,"/normalLog/upload"],
  "downLoadNormalLog": [POST, '/normalLog/download'],
  'deleteNormalLog': [DELETE, '/normalLog/delete'],
  // 事件日志
  "searchEventLog": [GET, "/eventLog"],
  "getShareEventLog": [GET, "/eventLog/sharedLogs"],
  "getEventLog": [GET, "/eventLog"],
  "shareEventLog": [POST, "/eventLog/share"],
  "unShareEventLog": [POST, "/eventLog/unShare"],
  "uploadEventLog": [POST,"/eventLog/upload"],
  "downLoadEventLog": [POST, '/eventLog/download'],
  'deleteEventLog': [DELETE, '/eventLog/delete'],
//  事件预处理
  "normalize": [POST, '/rawLog/normalize'],
  "toEventLog": [POST, '/normalLog/toEventLog'],
  // 日志融合
  'getMergeMethods': [GET, '/merge/method'],
  'merge': [POST, '/merge'],
// 流程挖掘
  'getMiningMethods': [GET, '/mining/method'],
  'mining': [POST, '/mining']

}
