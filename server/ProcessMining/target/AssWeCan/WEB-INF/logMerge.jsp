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
    <script type="text/javascript" src="processmining/logMerge.js"></script>
    <!--  <script type="text/javascript" src="processmining/loading.js"></script>  -->
    	
  </head>
  
  <body>
    <div class="easyui-layout" data-options="fit:true">
    <div id="center" region="center" border="true" style="padding:0px;">
	  <table id="logmerge_id" class="easyui-datagrid" iconCls="icon-save" rownumbers="true" pagination="true" >
		  <thead>
			 <tr>
			     <th field="logMergeMethodName" width="250" align="center">融合方法</th>
				 <th field="idd" width="150" formatter=logMergeformater align="center">操作</th>
			 </tr>
		  </thead>
	  </table>
	  </div>
	  </div>
	 <!-- <div id="logMergeSettingWin" class="easyui-window" closed="true" style="height:233px;top:10px;left:350px">
	  <div style="margin:10px 0;"></div>
	  <table cellpadding="5">
	     <tr>
	       <td>请选择日志文件1:</td>
	       <td>
	           <input id="file1" type="text" name="name" value="" class="easyui-textbox" data-options="valueField:'id',textField:'name', prompt:'请点击选择日志文件...'" onclick="selectEventLogFile()" />
           </td>
         </tr>
         <tr>
           <td>请选择日志文件2:</td>
	       <td>
	           <input id="file2" type="text" name="name" value="" class="easyui-textbox" data-options="valueField:'id',textField:'name', prompt:'请点击选择日志文件...'" onclick="selectEventLogFile()" />
           </td>
         </tr>
	  </table> 
	    	     
	  <div style="margin:10px 0;"></div>
	  <div class="easyui-panel" title="算法参数设置" style="width:400px">
		 <div style="padding:10px 40px 10px 50px">
	     <form id="ffSetting" method="post">
	    	 <table cellpadding="5">
	    	    <tr>
	    			<td>初始种群生成方法:</td>
	    			<td>
	    				<select class="easyui-combobox" name="name" style="width:150px">
	    				   <option value="启发式方法">启发式方法</option>
	    				   <option value="随机方法">随机方法</option>
	    				</select>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>初始种群规模:</td>
	    			<td><input class="easyui-textbox" type="text" name="initPopulations" data-options="required:true,validType:'number'"></input></td>
	    		</tr>
	    		<tr>
	    			<td>种群最大进化代数:</td>
	    			<td><input class="easyui-textbox" type="text" name="maxGenerations" data-options="required:true,validType:'number'"></input></td>
	    		</tr>
	    		<tr>
	    			<td>退火接受最大退化次数:</td>
	    			<td><input class="easyui-textbox" type="text" name="maxNoOptimalizations" data-options="required:true,validType:'number'"></input></td>
	    		</tr>
	    		<tr>
	    			<td>退火记忆库个体数:</td>
	    			<td><input class="easyui-textbox" type="text" name="maxMemoryBank" data-options="required:true,validType:'number'"></input></td>
	    		</tr>
	    		<tr>
	    			<td>每代克隆选择比率:</td>
	    			<td><input class="easyui-textbox" type="text" name="ratioForClone" data-options="required:true"></input></td>
	    		</tr>
	    	</table>
	    	<div style="margin:10px 0;"></div>
	    	<div style="color:red;font-weight:bold">
	    	   <tr>
	    		   <td>亲和度函数参数配置</td>
	    	   </tr>
	    	</div>
	    	<table cellpadding="5">
	    		<tr>
	    			<td>相同属性值:</td>
	    			<td><input class="easyui-textbox" type="text" name="sameAttribute" data-options="required:true,validType:'number'"></input></td>
	    		</tr>
	    		<tr>
	    			<td>相近实例频次:</td>
	    			<td><input class="easyui-textbox" type="text" name="approximatyFrequency" data-options="required:true,validType:'number'"></input></td>
	    		</tr>
	    		<tr>
	    			<td>重叠的时间区域:</td>
	    			<td><input class="easyui-textbox" type="text" name="overlapTimesatmp" data-options="required:true,validType:'number'"></input></td>
	    		</tr>
	    	</table>
	    </form>
	    </div>
	</div>   
	<div style="text-align:center;padding:8px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitFormLogMerge()" style="margin-right:10px">融合</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFormLogMerge()" style="margin-left:10px">取消</a>
	</div>
    </div>
    -->
    
    <div id="logMergeSettingWin0" class="easyui-window" title="配置" closed="true" style="height: 500px;" >    
        <div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'north'" style="height:75px">
	           	<table cellpadding="5">
	               <tr>
	                   <td>请选择日志文件1:</td>
	                   <td>
	                      <input id="file1" type="text" name="name" value="" class="easyui-textbox" data-options="valueField:'id',textField:'name', prompt:'请点击选择日志文件...'" onclick="selectEventLogFile()" />
                       </td>
                   </tr>
                   <tr>
                       <td>请选择日志文件2:</td>
	                   <td>
	                      <input id="file2" type="text" name="name" value="" class="easyui-textbox" data-options="valueField:'id',textField:'name', prompt:'请点击选择日志文件...'" onclick="selectEventLogFile()" />
                       </td>
                   </tr>		
	    	   </table>     				    			
	        </div>
	        <div data-options="region:'center'">
	         	 <form id="ffSetting" method="post" >    	
	    		      <table id="settingtable" border="false"></table>
	    			  <table id="settingtable1" border="false"></table>
	    		 </form>
	    	</div>
	    	<div data-options="region:'south' " style="height:32px;background-color:#E7F3FE">
	    	     <div style="text-align:center;padding:2px">
	    		     <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitFormLogMerge()" style="margin-right:10px">融合</a>
	    			 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFormLogMerge()" style="margin-left:10px">取消</a>
	    		 </div>
	    	</div>
       </div>      
	</div>
    
    <!--  <div id="aialogMergeSettingWin" class="easyui-window" closed="true" style="height:233px;top:10px;left:350px">
	  <div style="margin:10px 0;"></div>
	  <table cellpadding="5">
	     <tr>
	       <td>请选择日志文件1:</td>
	       <td>
	           <input id="file3" type="text" name="name" value="" class="easyui-textbox" data-options="valueField:'id',textField:'name', prompt:'请点击选择日志文件...'" onclick="selectEventLogFile()" />
           </td>
         </tr>
         <tr>
           <td>请选择日志文件2:</td>
	       <td>
	           <input id="file4" type="text" name="name" value="" class="easyui-textbox" data-options="valueField:'id',textField:'name', prompt:'请点击选择日志文件...'" onclick="selectEventLogFile()" />
           </td>
         </tr>
	  </table>     
	  <div style="margin:10px 0;"></div>
	  <div class="easyui-panel" title="算法参数设置" style="width:400px">
		 <div style="padding:10px 40px 10px 50px">
	     <form id="aiaffSetting" method="post">
	    	 <table cellpadding="5">
	    		<tr>
	    			<td>初始种群规模:</td>
	    			<td><input class="easyui-textbox" type="text" name="initPopulations" data-options="required:true,validType:'number'"></input></td>
	    		</tr>
	    		<tr>
	    			<td>种群最大进化代数:</td>
	    			<td><input class="easyui-textbox" type="text" name="maxGenerations" data-options="required:true,validType:'number'"></input></td>
	    		</tr>
	    		<tr>
	    			<td>每代克隆选择比率:</td>
	    			<td><input class="easyui-textbox" type="text" name="ratioForClone" data-options="required:true"></input></td>
	    		</tr>
	    	</table>
	    </form>
	    </div> 
	</div>   
	<div style="text-align:center;padding:8px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitFormLogMerge()" style="margin-right:10px">融合</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFormLogMerge()" style="margin-left:10px">取消</a>
	</div>
    </div>
    -->
    
    <div id="aialogMergeSettingWin" class="easyui-window" title="配置" closed="true" style="height: 500px;" >    
        <div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'north'" style="height:75px">
	           	<table cellpadding="5">
	               <tr>
	                   <td>请选择日志文件1:</td>
	                   <td>
	                      <input id="file3" type="text" name="name" value="" class="easyui-textbox" data-options="valueField:'id',textField:'name', prompt:'请点击选择日志文件...'" onclick="selectEventLogFile()" />
                       </td>
                   </tr>
                   <tr>
                       <td>请选择日志文件2:</td>
	                   <td>
	                      <input id="file4" type="text" name="name" value="" class="easyui-textbox" data-options="valueField:'id',textField:'name', prompt:'请点击选择日志文件...'" onclick="selectEventLogFile()" />
                       </td>
                   </tr>		
	    	   </table>     				    			
	        </div>
	        <div data-options="region:'center'">
	         	 <form id="aiaffSetting" method="post" >    	
	    		      <table id="aiasettingtable" border="false"></table>
	    			  <table id="aiasettingtable1" border="false"></table>
	    		 </form>
	    	</div>
	    	<div data-options="region:'south' " style="height:32px;background-color:#E7F3FE">
	    	     <div style="text-align:center;padding:2px">
	    		     <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitFormLogMerge()" style="margin-right:10px">融合</a>
	    			 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFormLogMerge()" style="margin-left:10px">取消</a>
	    		 </div>
	    	</div>
       </div>      
	</div>
    
	<div id="logMergeDesWin" class="easyui-window" closed="true" style="height: 233px;top:60px;left:480px">
	    <form id="logMergeDes" method="post">
           <table id="pgDes" style="width:200px"></table>
	    </form>
    </div>
    <div id="selectEventLogWin" class="easyui-window" closed="true" style="height:220px;top:30px;left:80px">
        <div class="easyui-layout" data-options="fit:true">
		     <table id="selectEventLog_id" class="easyui-datagrid" toolbar="#tb3" iconCls="icon-save" rownumbers="true" singleSelect="true" pagination="true">
		         <thead>
			         <tr>
			             <th field="" checkbox='true' title="" width="30" align="center">选择</th>
			             <th field="name" width="150" formatter="eventLogformater" align="center">日志名称</th>
				         <th data-options="field:'format',width:80" align="center">日志格式</th>
				         <th field="link_normlog" width="150" formatter="eventLogformater1" align="center">规范化日志</th>
				         <th field="link_eventlog" width="150" formatter="eventLogformater2" align="center">关联事件日志</th>
				         <th data-options="field:'date',width:150,align:'center'">创建时间</th>
				         <th data-options="field:'user',width:80,align:'center'">创建人</th>
			         </tr>
		         </thead>
	        </table>
	        <div data-options="region:'south' " style="height:32px;background-color:#E7F3FE">
	          <div style="text-align:center;padding:2px">
	             <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitSelection()" style="margin-right:10px">确定</a>
	             <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearSelection()" style="margin-left:10px">取消</a>
	          </div>
	       </div>
	    </div>	
	 </div>
	 <div id="logMergeResultSumariseWin" class="easyui-window" title="融合的事件日志信息" closed="true" style="height: 233px;">
	      <form id="logMergeResultSumarise" method="post">
              <table title="融合的日志摘要信息" id="rsultlog_pg" style="width:400px"></table>
	      </form>
	      <div style="margin:5px 0;"></div>
	      <form id="logMergeResultSumarise2" method="post">
	          <table title="融合的日志过程耗时信息" id="time_pg" style="width:400px"></table>
	     </form>
     </div>  
  </body>
  
</html>
