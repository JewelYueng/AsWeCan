<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
// System.out.print(path);
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
  <head>
    <base href="<%=basePath%>">
     
	<meta charset="UTF-8">
	<title>Basic Layout - jQuery EasyUI Demo</title>
	<link rel="stylesheet" type="text/css" href="../js/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="../js/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="../js/demo/demo.css">
	<script type="text/javascript" src="../js/jquery.min.js"></script>
	<script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<!-- 加载自定义js -->
    <script type="text/javascript" src="../processmining/originalLogFile.js"></script>
    <script type="text/javascript" src="../processmining/navigation.js"></script>
    <script type="text/javascript" src="../processmining/eventLogFile.js"></script>
    <script type="text/javascript" src="../processmining/commonOperation.js"></script>
    <script type="text/javascript" src="../processmining/logMerge.js"></script>
    <!--  <script type="text/javascript" sorg.k2.processminingning/loading.js"></script>  -->

  </head>
  
  <body>
    <div id="selectEventLogWin" class="easyui-window" closed="true" style="height: 233px;">
		<table id="selectEventLog_id" class="easyui-datagrid" toolbar="#tb3" iconCls="icon-save" rownumbers="true" singleSelect="true" pagination="true">
		     <thead>
			     <tr>
			         <th field="" checkbox='true' title="" width="30" align="center">日志名称</th>
			         <th field="name" width="150" formatter="eventLogformater" align="center">日志名称</th>
				     <th data-options="field:'format',width:100" align="center">日志格式</th>
				     <th field="link_normlog" width="150" formatter="eventLogformater1" align="center">规范化日志</th>
				     <th field="link_eventlog" width="150" formatter="eventLogformater2" align="center">关联事件日志</th>
				     <th data-options="field:'date',width:150,align:'center'">创建时间</th>
				     <th data-options="field:'user',width:100,align:'center'">创建人</th>
				     <th field="idd" width="200" formatter="rowformaterEvent" align="center">操作</th>
			      </tr>
		      </thead>
	      </table>
	 </div>
	 
  </body>
</html>
