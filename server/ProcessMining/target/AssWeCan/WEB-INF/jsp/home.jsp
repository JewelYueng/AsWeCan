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
     
	<meta charset="UTF-8" >
	<title>Basic Layout - jQuery EasyUI Demo</title>
	<link rel="stylesheet" type="text/css" href="js/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="js/demo/demo.css">
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<!-- 加载自定义js -->
    <script type="text/javascript" src="processmining/originalLogFile.js"></script>
    <script type="text/javascript" src="processmining/navigation.js"></script>
    <script type="text/javascript" src="processmining/eventLogFile.js"></script>
    <script type="text/javascript" src="processmining/commonOperation.js"></script>
    <script type="text/javascript" src="processmining/uploadAndDownloadFile.js"></script>
    <script type="text/javascript" src="processmining/logMerge.js"></script>
    <script type="text/javascript" src="processmining/normalLogFile.js"></script>
    <script type="text/javascript" src="processmining/login.js"></script>
    
    <style type="text/css">
       #showtime {
color:#FFDC35;
vertical-align: top;
bottom: 5px;
position: absolute;
right:5px;
       }
       #username {
color:#FFDC35;
vertical-align: top;
bottom: 5px;
position: absolute;
right:5px;
       }
       #welcom {
color:#FFDC35;
vertical-align: top;
bottom: 5px;
position: absolute;
right:5px;
       }
       #blank {
color:#FFDC35;
vertical-align: top;
bottom: 5px;
position: absolute;
right:5px;
       }
    </style>
    
    <script type="text/javascript">
       function showTime(){ 
          var show_day=new Array('星期一','星期二','星期三','星期四','星期五','星期六','星期日'); 
          var time=new Date();
          var year=time.getFullYear();
          var month=time.getMonth(); 
          var date=time.getDate(); 
          var day=time.getDay(); 
          var hour=time.getHours(); 
          var minutes=time.getMinutes(); 
          var second=time.getSeconds(); 
          month=month+1; 
          month<10?month='0'+month:month; 
          hour<10?hour='0'+hour:hour; 
          minutes<10?minutes='0'+minutes:minutes; 
          second<10?second='0'+second:second; 
          var now_time='当前时间：'+year+'年'+month+'月'+date+'日'+' '+show_day[day-1]+' '+hour+':'+minutes+':'+second;
          document.getElementById('showtime').innerHTML=now_time; 
          setTimeout("showTime();",1000); 
       } 
       function welcom(){
          var welcom = "欢迎，";
          document.getElementById('welcom').innerHTML= welcom; 
       }
       function username(){
          var username = getCookie('result') + "  ";
          //var username = "xxxxx";  //username在用户登录时候从cookies中获取
          document.getElementById('username').innerHTML= username; 
       }
       function blank(){
          var blank = "  ";
          //var username = "xxxxx";  //
          document.getElementById('blank').innerHTML= blank; 
       }
       $(function(){
         welcom();
         username();
         blank();
       })
    </script>

</head>
<body onload = "showTime();">
	<div class="easyui-layout" data-options="fit:true">
	    <div id="north" region="north" border="true" style="height:65px;background:#0000FF;overflow:auto;padding:5px;">
	       <div align="left" style="color:#000;"> <h2 align="left" style="color:#FFF;">K2流程挖掘与分析平台</h2></div>
	       <div align="right" id="showtime" style="float:right;text-indent:2em;color:#FFF;"></div>
	       <div align="left" id="blank" style="float:right;text-indent:2em"></div>
	       <div align="left" id="username" style="color:#FFF;float:right;"></div>
	       <div align="left" id="welcom" style="float:right;color:#FFF;"></div>
        </div>
		<div data-options="region:'west',split:true" title="导航栏" style="width:258px;border:false">
		   <div class="easyui-accordion" style="width:250px;height:562px;border:0px">
		         <div id="logmanagement_id" class="div" title="日志管理" data-options="iconCls:'icon-ok'" style="overflow:auto;padding:10px;">
			         <h3 style="color:#0099FF;"></h3>
			         <ul>
			             <li>
			                 <a href="javascript:void(0)" onclick="showcontent(1)">原始日志</a></li>
			             </li>
		             </ul>
		             <h3 style="color:#0099FF;"></h3>
			         <ul>
			             <li>
			                 <a href="javascript:void(0)" onclick="showcontent(2)">规范化日志</a></li>
			             </li>
		             </ul>
		             <ul>
			             <li>
			                 <a href="javascript:void(0)" onclick="showcontent(3)">事件日志</a></li>
			             </li>
		             </ul>
		       </div>
		       <div class="xxx" title="日志融合" data-options="iconCls:'icon-help'" style="padding:10px;">
		          <h3 style="color:#0099FF;"></h3>
			         <ul>
			             <li>
			                 <a href="javascript:void(0)" onclick="AddTabs('日志融合','logMerge.jsp')">日志融合</a></li>
			             </li>
		             </ul>
		       </div>
		       <div class="xxx" title="流程挖掘" data-options="iconCls:'icon-search'" style="padding:10px;">
		       		<h3 style="color:#0099FF;"></h3>
			         <ul>
			             <li>
			                 <a href="javascript:void(0)" onclick="AddTabs('流程挖掘','processMining.jsp')">流程挖掘</a></li>
			             </li>
			             <li>
			                 <a href="javascript:void(0)" onclick="AddTabs('流程图测试','miningMap.jsp')">流程图测试</a></li>
			             </li>
		             </ul>
		       </div>
		   </div>
		</div>
		<div id="123" data-options="region:'center',iconCls:'icon-ok'" style="width:200px;">
		    <div id="jquerylogshow_id" class="easyui-tabs" data-options="fit:true";">
	            <div title="原始日志" closable="false" style="padding:0px;">
	            <table id="orilog_id" class="easyui-datagrid" toolbar="#tb1" iconCls="icon-save" rownumbers="true" singleSelect="true" pagination="true" url="rawlog/getLog" method="get">
		              <thead>
			             <tr>
				            <th data-options="field:'name',width:200" align="center">日志名称</th>
				            <th data-options="field:'format',width:100" align="center">日志格式</th>
				            <th field="normLogLinkName" width="150" formatter="oriLogformater" align="center">规范化日志</th>
				            <th data-options="field:'normLogLink',width:150" align="center">规范化日志ID</th>
				            <th data-options="field:'date',width:150,align:'center'">创建时间</th>
				            <!-- <th data-options="field:'user',width:150,align:'center'">创建人</th> -->
				            <th field="myuser" width="150" formatter="rawLogformaterForUser" align="center">创建人</th>
				            <th field="idd" width="200" formatter="rowformaterOri" align="center">操作</th>
			             </tr>
		              </thead>
	               </table>
	            </div>
	            <div title="规范化日志" closable="false" style="padding:0px;">
		           <table id="normallog_id" class="easyui-datagrid" toolbar="#tb5" iconCls="icon-save" rownumbers="true" singleSelect="true" pagination="true" url="normlog/getLog" method="get">
		              <thead>
			             <tr>
			                <th field="name" width="150"  align="center">日志名称</th>
				            <th data-options="field:'format',width:100" align="center">日志格式</th>
				            <th field="rawLogLinkName" width="150" formatter="normalLogformater1" align="center">原始日志</th>
				            <th data-options="field:'rawLogLink',width:100" align="center">原始日志ID</th>
				            <th field="eventLogLinkName" width="150" formatter="normalLogformater2" align="center">事件日志</th>
				            <th data-options="field:'eventLogLink',width:100" align="center">事件日志ID</th>
				            <th data-options="field:'date',width:150,align:'center'">创建时间</th>
				            <!-- <th data-options="field:'user',width:100,align:'center'">创建人</th> -->
				            <th field="myuser" width="150" formatter="normalLogformaterForUser" align="center">创建人</th>
				            <th field="idd" width="250" formatter="rowformaterNormal" align="center">操作</th>
			             </tr>
		              </thead>
	               </table>
	            </div>
	            <div title="事件日志" closable="false" style="padding:0px;">
		           <table id="eventlog_id" class="easyui-datagrid" toolbar="#tb3" iconCls="icon-save" rownumbers="true" singleSelect="false" pagination="true" url="eventlog/getLog" method="get">
		              <thead>
			             <tr>
			                <th field="" checkbox='true' title="" width="30" align="center">日志名称</th>
			                <th field="name" width="150" formatter="eventLogformater" align="center">日志名称</th>
				            <th data-options="field:'format',width:100" align="center">日志格式</th>
				            <th field="normLogLinkName" width="150" formatter="eventLogformater1" align="center">规范化日志</th>
				            <th data-options="field:'normLogLink',width:100" align="center">规范化日志ID</th>
				            <th field="eventLogLink" width="150" formatter="eventLogformater2" align="center">关联事件日志</th>
				            <th data-options="field:'date',width:150,align:'center'">创建时间</th>
				            <!--  <th data-options="field:'user',width:100,align:'center'">创建人</th> -->
				            <th field="user" width="150" formatter="eventLogformaterForUser" align="center">创建人</th>
				            <th field="idd" width="200" formatter="rowformaterEvent" align="center">操作</th>
			             </tr>
		              </thead>
	               </table>
	            </div>
             </div>
		</div>
	</div>
	<form id='tb1' class=easyui-form >
       <a id="uploadFile" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="uploadOriFileClick()" href="javascript:void(0);">上传文件</a>
       <div id="tb2" style="padding:3px; float:right">
	      <input id="search" placeholder="输入文件名称模糊搜索" style="line-height:15px;border:1px solid #ccc"></input>
	      <a href="#" class="easyui-linkbutton" plain="true" onclick="doSearch()">查找</a>
       </div>
     </form>
     <form id='tb3' class=easyui-form >
       <a id="uploadFile" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="uploadEventFileClick()" href="javascript:void(0);">上传文件</a>
       <div id="tb4" style="padding:3px; float:right">
	      <input id="search" placeholder="输入文件名称模糊搜索" style="line-height:15px;border:1px solid #ccc"></input>
	      <a href="#" class="easyui-linkbutton" plain="true" onclick="doSearch()">查找</a>
       </div>
     </form>
     <form id='tb5' class=easyui-form >
       <a id="uploadNormalFile" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="uploadNormalFileClick()" href="javascript:void(0);">上传文件</a>
       <div id="tb6" style="padding:3px; float:right">
	      <input id="searchNormalFile" placeholder="输入文件名称模糊搜索" style="line-height:15px;border:1px solid #ccc"></input>
	      <a href="#" class="easyui-linkbutton" plain="true" onclick="doSearchNormalFile()">查找</a>
       </div>
     </form>

      <div id="NormalTransSetWin" class="easyui-window" title="配置信息" closed="true" style="height: 550px;" >

            <div class="easyui-layout" data-options="fit:true">
	           	<div data-options="region:'north'" border="false" style="height:480px">
	           	    <div id="transsetshow_id" class="easyui-tabs" border="false" data-options="height:480";">   <!--  data-options="fit:true" -->
	                   <div title="数据项格式配置" closable="false" style="padding:0px;">
	                      <table id="data_Table1" border="false"></table>
	                   </div>
	                   <div title="数据项整合配置" closable="false" style="padding:0px;">
		                  <table id="data_Table2" border="false"></table>
		                  <table id="data_Table3" border="false"></table>
	                   </div>
	                   <div title="记录格式配置" closable="false" style="padding:0px;">
		                  <table id="data_Table4" border="false"></table>
	                  </div>
                    </div>
	           	</div>
	         	<div data-options="region:'south'" style="padding:0px;height:32px;background-color:#E7F3FE">
	         	    <div style="text-align:center;padding:2px">
	                 	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="transform()" style="margin-right:10px">规范化</a>
	                 	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="back()" style="margin-left:0px">取消</a>
	                </div>
	    	    </div>
            </div>
	 </div>

    <div id="upLoadOriFileWin" class="easyui-window" title="上传文件" closed="true" style="height: 233px;" >
	    <form id="upOri" method="post" enctype="multipart/form-data">
	    	<table cellpadding="5">
	    		<!-- <tr>
	    			<td align="right">文件类型:</td>
	    			<td align="center">
	    			     <select id="type" class="easyui-combobox" name="type" data-options="prompt:'请选择文件类型'" style="width:120px;">
                           <option>原始日志</option>
                           <option>规范化日志</option>
                           <option>事件日志</option>
                        </select>
                      </td>
	    		</tr> -->
	    		<tr>
	    			<td align="right">文件格式:</td>
	    			<td align="center">
	    			     <input id="format" class="text" name="format" prompt="请输入文件格式" style="width:120px;"></input>
                      </td>
	    		</tr>
	    		<tr>
	    			<td align="right">是否分享:</td>
	    			<td align="center">
	    		<!-- 	  <input type="radio" name="activation" value="1" data-options="required:true,">是</input>
	    			  <input type="radio" name="activation" value="0" data-options="required:true,">否</input> -->
	    			  <select id="isShare" class="easyui-combobox" name="isShare" data-options="prompt:'请选择'" style="width:120px;">
                           <option value="1">是</option>
                           <option value="0">否</option>
                        </select>
	    			</td>
	    		</tr>
	    		<tr>
	    		    <td> </td>
	    		    <td>
	    		    <input type="file" class="file" name="file">
	    		    <!-- <input id="txt_head" name="name" class="easyui-filebox" data-options="buttonText:'选择文件',prompt:'请选择文件...',editable:false" ></input> -->
	    		    <!-- <input type="file" id="uploadfile" name="file"/> -->
	    		    </td>
	    		</tr>
	    	</table>
	      </form>
	    <div style="text-align:center;padding:8px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitFormOri()" style="margin-right:10px">上传</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFormOri()" style="margin-left:10px">取消</a>
	    </div>
	 </div>

	 <div id="upLoadNormalFileWin" class="easyui-window" title="上传文件" closed="true" style="height: 233px;" >
	    <form id="upNormal" method="post" enctype="multipart/form-data">
	    	<table cellpadding="5">
	    		<tr>
	    			<td align="right">文件格式:</td>
	    			<td align="center">
	    			     <input id="format" class="text" name="format" prompt="请输入文件格式" style="width:120px;"></input>
                      </td>
	    		</tr>
	    		<tr>
	    			<td align="right">是否分享:</td>
	    			<td align="center">
	    			  <select id="isShareNormalLog" class="easyui-combobox" name="isShare" data-options="prompt:'请选择'" style="width:120px;">
                           <option value="1">是</option>
                           <option value="0">否</option>
                        </select>
	    			</td>
	    		</tr>
	    		<tr>
	    		    <td> </td>
	    		    <td>
	    		    <input type="file" class="file" name="file">
	    		    </td>
	    		</tr>
	    	</table>
	      </form>
	    <div style="text-align:center;padding:8px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitFormNormal()" style="margin-right:10px">上传</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFormNormal()" style="margin-left:10px">取消</a>
	    </div>
	 </div>

	 <div id="upLoadEventFileWin" class="easyui-window" title="上传文件" closed="true" style="height: 233px;" >
	    <form id="upEvent" method="post" enctype="multipart/form-data">
	    	<table cellpadding="5">
	    		<tr>
	    			<td align="right">文件格式:</td>
	    			<td align="center">
	    			     <input id="format" class="text" name="format" prompt="请输入文件格式" style="width:120px;"></input>
                      </td>
	    		</tr>

	    		<tr>
	    			<td align="right">是否分享:</td>
	    			<td align="center">
	    			  <select id="isShareEventLog" class="easyui-combobox" name="isShare" data-options="prompt:'请选择'" style="width:120px;">
                           <option value="1">是</option>
                           <option value="0">否</option>
                        </select>
	    			</td>
	    		</tr>
	    		<tr>
	    		    <td> </td>
	    		    <td>
	    		    <input type="file" class="file" name="file">
	    		    </td>
	    		</tr>
	    	</table>
	      </form>
	    <div style="text-align:center;padding:8px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitFormEvent()" style="margin-right:10px">上传</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFormEvent()" style="margin-left:10px">取消</a>
	    </div>
	 </div>
	 <div id="eventLogSumariseWin" class="easyui-window" title="事件日志摘要信息" closed="true" style="height: 233px;top:200px;left:500px"">
	    <div class="easyui-layout" data-options="fit:true">
	       <div data-options="region:'north'" style="height:560px" border="false">
	          <form id="eventLogSumarise" method="post">
	             <!--<table id="pg" class="easyui-propertygrid" style="width:300px" data-options="
				        url:'propertygrid_data1.json',
				        method:'get',
				        showGroup:true,
				        scrollbarSize:0
			       ">
	               </table> -->
                   <table id="pg" style="width:400px" border="false"></table>

                   <!-- <table id="pg" class="easyui-datagrid" data-options="nowrap:false" style="height:auto" iconCls="icon-save" rownumbers="false" pagination="false" >
		                    <thead>
			                   <tr>
				                   <th data-options="field:'name',width:120" align="center">名称</th>
				                   <th data-options="field:'value',width:265" align="center">项值</th>
			                   </tr>
		                    </thead>
	               </table> -->

	            </form>
	       </div>
	       <div data-options="region:'south'" style="padding:0px;height: 32px;background-color:#E7F3FE" >
	          <div style="text-align:center;padding:2px">
	              <a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeSumariseWin()" style="margin-right:8px">关闭</a>
	          </div>
	       </div>
	    </div>
     </div>
     
  </body>
</html>