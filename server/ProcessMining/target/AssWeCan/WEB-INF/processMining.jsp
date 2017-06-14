<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
// System.out.print(path);
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<base href="<%=basePath%>">

	<meta charset="UTF-8">
	<title>Basic Layout - jQuery EasyUI Demo</title>
	<link rel="stylesheet" type="text/css" href="js/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="js/demo/demo.css">
	<link rel="stylesheet" href="css/demo.css">
	<link rel="stylesheet" href="css/graph_render.css">
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="processmining/pm.js"></script>
	<script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
	<script src="js/dagre-d3.js"></script>
	<script type="text/javascript" src="js/dagre.min.js"></script>
	<script type="text/javascript" src="js/d3-sankey.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<!-- 加载自定义js -->
    <!-- <script type="text/javascript" src="processmining/processMining.js"></script> -->
    <script type="text/javascript" src="js/raphael.js"></script>
    <!-- <script type="text/javascript" src="processmining/eventLogFile.js"></script> -->
    
	    <script type="text/javascript">
	    
	    var row;
	    $(function() {
	    	// 先通过ajax获取数据，然后再传给datagrid
	    	$.get("/ProcessMining/mining/getMiningMethod", function(data) {
	    		//		if("success".equals(data.success)){
	    		for (var i = 0; i < data.total; i++) {
	    			//alert(data.rows[file].orifileName);
	    			var row_data = {
	    				name : data.rows[i].name,
	    				packageName : data.rows[i].packageName,
	    				miningName : data.rows[i].miningName,
	    				miningMethod : data.rows[i].miningMethod
	    			};
	    			//alert(row_data);
	    			$('#processmining_id').datagrid('appendRow', row_data);
	    		}
	    		//		}else{
	    		//			alert("查找失败!");
	    		//		}
	    	});
	    })
	    
	    	function miningMethodformater(value,row,index){
		  		return '<a href="javacript:void(0);" onclick="processmining('+index+');">'+row.name+'</a>';
			}
	    	
	    	var settingTitle;
	    	function processmining(index){
	    		row = $('#processmining_id').datagrid('getData').rows[index];
	    		settingTitle = '参数设置';
	    		mySettingWindow(settingTitle,index);   		
	    		miningSetting_Window.window('open');
	    		if(index == 0)
	    			settingtablePanel();
	    		if(index == 1)
	    			settingtablePanel1();
	    	}
	    	
	    	var miningSetting_Window;
	    	function mySettingWindow(settingTitle,index) {
	    			miningSetting_Window = $('#miningSettingWin' + index).window({
	    			title:settingTitle,
	    			width : 400,
	    			height : 400,
	    			top: ($(window).height() - 400) * 0.5,
	    		    left: ($(window).width() - 400) * 0.5,
	    			modal : true,
	    			collapsible : false,
	    			minimizable : true,
	    			maximizable : true,
	    			closable : true
	    		});
	    	};    	
	    	
	    	$(function() { 
	    		// 先通过ajax获取数据，然后再传给combox
	    		$.ajax({
	    			method : 'GET',
	    			url : '/ProcessMining/eventlog/getLog',
	    			async : true,
	    			dataType : 'json',
	    			success : function(data) {
	    				// alert(data.total);
	    				$("#cc1").combobox("loadData", data.rows);
	    			},
	    			error : function() {
	    				alert('error');
	    			}
	    		});
	    	});
	
	    	function clearFormLogMining(){
	    		$('#file1').textbox('setValue', "");
	    		$('#miningSetting').form('clear');
	    		miningSetting_Window.window('close');
	    		
	        	window.location.reload();
	    	}
	    	
	    	var miningResultWinWidth = 350;
	    	var miningResultWinHeight = 160;
	    	var miningResult_Window;
	    	function miningResultWin() {
	    			miningResult_Window = $('#miningResultWin').window({
	    			title: "结果展示",
	    			width : miningResultWinWidth,
	    			height : miningResultWinHeight,
	    			top: ($(window).height() - miningResultWinHeight) * 0.5,
	    		    left: ($(window).width() - miningResultWinWidth) * 0.5,
	    			modal : true,
	    			collapsible : true,
	    			minimizable : true,
	    			maximizable : true,
	    			closable : true
	    		});
	    	};
	    	
	    	var editRow = undefined;
	    	function settingtablePanel(){
	    		   		
	    		$("#settingtable").datagrid({
	    	        height: 280,
	    	        width: 383,
	    	        title: '算法参数设置',
	    	        collapsible: true,
	    	        singleSelect: true,
	    	        idField: 'data7',
	    	        columns: [[
	    	         { field: 'data7', title: '参数', width: 200,align: 'center'},
	    	            { field: 'data8', title: '设置', width: 180,align: 'center',editor: { type: 'text', options: { required: true } } }
	    	        ]],
	    	        toolbar: [],
	    	        onAfterEdit: function (rowIndex, rowData, changes) {
	    	            editRow = undefined;
	    	        },
	    	        onDblClickRow:function (rowIndex, rowData) {
	    	            if (editRow != undefined) {
	    	                $("#settingtable").datagrid('endEdit', editRow);
	    	            }
	    	 
	    	            if (editRow == undefined) {
	    	                $("#settingtable").datagrid('beginEdit', rowIndex);
	    	                editRow = rowIndex;
	    	            }
	    	        },
	    	        onClickRow:function(rowIndex,rowData){
	    	            if (editRow != undefined) {
	    	                $("#settingtable").datagrid('endEdit', editRow);
	    	 
	    	            }
	    	            
	    	        }
	    	        
	    	    });
	    		
	    	    $('div.datagrid-body').click(function(){
	    	    	if(editRow != undefined){
	    	        	$('#settingtable').datagrid('endEdit', editRow);
	    	            }
	    	    	})
	    	    	 
	    	    $("#settingtable").datagrid('insertRow', {
	    	         index: 0,
	    	         row: {
	    	              data7:'相对频度阈值',
	    	              data8:'0.9'
	    	        	}
	    	        });
	    	    $("#settingtable").datagrid('insertRow', {
	   	         index: 1,
	   	         row: {
	   	              data7:'依赖度阈值',
	   	           	  data8:'0.9'
	   	        	}
	   	        });
	    	    $("#settingtable").datagrid('insertRow', {
	   	         index: 2,
	   	         row: {
	   	              data7:'一元循环阈值',
	   	           	  data8:'0.9'
	   	        	}
	   	        });
	    	    $("#settingtable").datagrid('insertRow', {
	   	         index: 3,
	   	         row: {
	   	              data7:'二元循环阈值',
	   	           	  data8:'0.9'
	   	        	}
	   	        });
	    	    
	    	}    	   	    	   	   	
	    	
	    	function settingtablePanel1(){
		   		
	    		$("#settingtable1").datagrid({
	    	        height: 280,
	    	        width: 383,
	    	        title: '算法参数设置',
	    	        collapsible: true,
	    	        singleSelect: true,
	    	        idField: 'data7',
	    	        columns: [[
	    	         { field: 'data7', title: '参数', width: 200,align: 'center'},
	    	            { field: 'data8', title: '设置', width: 180,align: 'center',editor: { type: 'text', options: { required: true } } }
	    	        ]],
	    	        toolbar: [],
	    	        onAfterEdit: function (rowIndex, rowData, changes) {
	    	            editRow = undefined;
	    	        },
	    	        onDblClickRow:function (rowIndex, rowData) {
	    	            if (editRow != undefined) {
	    	                $("#settingtable").datagrid('endEdit', editRow);
	    	            }
	    	 
	    	            if (editRow == undefined) {
	    	                $("#settingtable").datagrid('beginEdit', rowIndex);
	    	                editRow = rowIndex;
	    	            }
	    	        },
	    	        onClickRow:function(rowIndex,rowData){
	    	            if (editRow != undefined) {
	    	                $("#settingtable").datagrid('endEdit', editRow);
	    	 
	    	            }
	    	            
	    	        }
	    	        
	    	    });
	    		
	    	    $('div.datagrid-body').click(function(){
	    	    	if(editRow != undefined){
	    	        	$('#settingtable').datagrid('endEdit', editRow);
	    	            }
	    	    	})
	    	    	 
	    	    $("#settingtable1").datagrid('insertRow', {
	    	         index: 0,
	    	         row: {
	    	              data7:'交叉率',
	    	              data8:'0.8'
	    	        	}
	    	        });
	    	    $("#settingtable1").datagrid('insertRow', {
	   	         index: 1,
	   	         row: {
	   	              data7:'变异率',
	   	           	  data8:'0.2'
	   	        	}
	   	        });
	    	    $("#settingtable1").datagrid('insertRow', {
	   	         index: 2,
	   	         row: {
	   	              data7:'终止适应度值',
	   	           	  data8:'0.9'
	   	        	}
	   	        });
	    	    $("#settingtable1").datagrid('insertRow', {
	   	         index: 3,
	   	         row: {
	   	              data7:'种群规模',
	   	           	  data8:'10'
	   	        	}
	   	        });
	    	    $("#settingtable1").datagrid('insertRow', {
	      	         index: 4,
	      	         row: {
	      	              data7:'最大遗传代数',
	      	           	  data8:'100'
	      	        	}
	      	        });
	    	    $("#settingtable1").datagrid('insertRow', {
	     	         index: 5,
	     	         row: {
	     	              data7:'精英率',
	     	           	  data8:'0.2'
	     	        	}
	     	        });
	    	} 
	    	
	    	var result;
	    	var result1;
	    	var result2;
	    	var usedTime;
	    	function submitFormLogMining(){
	    		//var logName = $("#cc1").combobox("getText"); 		
	    		var logId = file1ID;
	    		
	    		var packageName = row.packageName;
	    		var miningName = row.miningName;
	    		var miningMethod = row.miningMethod;
	    		var relativeToBestThreshold = "";
	        	var dependencyThreshold = "";
	        	var l1lThreshold = "";
	        	var l2lThreshold = "";
	        	
	    		var dataTable1 = $('#settingtable').datagrid('getData');
	    		if(dataTable1.rows[0]['data8'] != "undefined"){
	    			relativeToBestThreshold = dataTable1.rows[0]['data8'];
	    		}
	    		if(dataTable1.rows[1]['data8'] != "undefined"){
	    			dependencyThreshold = dataTable1.rows[1]['data8'];
	    		}
	    		if(dataTable1.rows[2]['data8'] != "undefined"){
	    			l1lThreshold = dataTable1.rows[2]['data8'];
	    		}
	    		if(dataTable1.rows[3]['data8'] != "undefined"){
	    			l2lThreshold = dataTable1.rows[3]['data8'];
	    		}
	    		
	    		$('#file1').textbox('setValue', "");
	    		$('#miningSetting').form('clear');
	    		miningSetting_Window.window('close');
	    		MaskUtil.mask();//打开遮罩效果
	    		
	    		$('#miningSetting').form('submit', {
	    		    
	    			url:"/ProcessMining/mining/logMining?logId=" + logId + "&packageName=" + packageName
	    					+ "&miningName=" + miningName + "&miningMethod=" + miningMethod + "&relativeToBestThreshold=" + relativeToBestThreshold
	    					+ "&dependencyThreshold=" + dependencyThreshold + "&l1lThreshold=" + l1lThreshold + "&l2lThreshold=" + l2lThreshold, 
	    		    success: function (data) {
	    		    	//alert(data);
	    		    	jsonData=JSON.parse(data);
	    		    	result1 = jsonData.res;
	    		    	var jsonStr=JSON.parse(jsonData.jsonStr);
	    		    	result2={"nodes":jsonStr.nodes,"links":jsonStr.links,"traces":jsonData.traces,"allTraces":jsonData.Alltraces};
	    		    	//alert(jsonStr.links);
	    		    	//alert(jsonStr.nodes);
	    		    	//alert(jsonData.res);
	    		    	//alert(jsonData.traces);
	    		    	//alert(jsonData.Alltraces);
	    		    	MaskUtil.unmask();
	    		    	
	    		    	usedTime = result1[0].time + " ms";
	    		    	//alert(usedTime);
	    		    	miningResultWin();
	    		    	document.getElementById('usetime').innerHTML=usedTime;
	    	    		miningResult_Window.window('open');
	    	    		//mapdatatablePanel();
	    	    		
	    		    	//showMiningMap(result);   		       		    	
	    		        	    		    		    	    		    	
	    		    },
	    			error : function() {
						alert('error');
					}
	    		});
				
	    		
	    	}
	    	
	    	function submitGeneticMining(){
	    		var logId = file2ID;
	    		
	    		
	    		$('#file2').textbox('setValue', "");
	    		$('#miningSetting1').form('clear');
	    		miningSetting_Window.window('close');
	    		MaskUtil.mask();//打开遮罩效果
	    		
				$('#miningSetting1').form('submit', {
	    		    
	    			url:"/ProcessMining/mining/geneticMining?logId=" + logId, 
	    		    success: function (data) {
	    		    	
	    		    	alert(data);
	    		    	result1 = JSON.parse(data);
	    		    	MaskUtil.unmask();
	    		    	miningResultWin();
	    		    	usedTime = result1[0].time + " ms";
	    		    	document.getElementById('usetime').innerHTML=usedTime;
	    		    	miningResult_Window.window('open');	    		    		    	    		    	
	    		    },
	    			error : function() {
						alert('error');
					}
	    		});
	    	}
	    	function submitRegionLogMining(){
	    		var logId = file3ID;
	    		$('#file3').textbox('setValue', "");
	    		miningSetting_Window.window('close');
	    		MaskUtil.mask();//打开遮罩效果
	    		$.ajax({
	    			method : "post",
	    			url:"/ProcessMining/mining/regionMining?logId=" + logId,
	    			async : true,
	    			dataType : 'json',
	    			success : function(data) {
	    				alert(data);
	    		    	//result1 = JSON.parse(data);
	    		    	result1=data;
	    		    	MaskUtil.unmask();
	    		    	miningResultWin();
	    		    	usedTime = result1[0].time + " ms";
	    		    	document.getElementById('usetime').innerHTML=usedTime;
	    		    	miningResult_Window.window('open');
	    			},
	    			error : function() {
	    				alert('error');
	    			}
	    		});
	    	}
	    	
	    	var MaskUtil = (function(){
	    		var $mask,$maskMsg;
	    		var defMsg = '正在处理，请稍待。。。';
	    		function init(){
	    			if(!$mask){
	    				$mask = $("<div class=\"datagrid-mask mymask\"></div>").appendTo("body");
	    			}
	    			if(!$maskMsg){
	    				$maskMsg = $("<div class=\"datagrid-mask-msg mymask\">"+defMsg+"</div>")
	    					.appendTo("body").css({'font-size':'12px'});
	    			}
	    			$mask.css({width:"100%",height:$(document).height()});
	    			var scrollTop = $(document.body).scrollTop();
	    			$maskMsg.css({
	    				left:( $(document.body).outerWidth(true) - 190 ) / 2
	    				,top:( ($(window).height() - 45) / 2 ) + scrollTop
	    			}); 		
	    		}
	    		return {
	    			mask:function(msg){
	    				init();
	    				$mask.show();
	    				$maskMsg.html(msg||defMsg).show();
	    			},
	    			unmask:function(){
	    				$mask.hide();
	    				$maskMsg.hide();
	    			}
	    		};	
	    	}());    	
	    	
	    	//监听textbox文件选择的事件
	        $(function(){
				$("input",$("#file1").next("span")).click(function(){
				var index1 = 1;
				selectEventLogFile(index1);
				});
				$("input",$("#file2").next("span")).click(function(){
					var index = 2;
					selectEventLogFile(index);
				});
				$("input",$("#file3").next("span")).click(function(){
					var index = 3;
					selectEventLogFile(index);
				});
			});
	    	
	        var logMergeSelect_Window;
	        function mySelectWindow(settingTitle) {
	        	logMergeSelect_Window = $('#selectEventLogWin').window({
	        		title:settingTitle,
	        		width : 850,
	        		height : 500,
	        		modal : true,
	        		collapsible : false,
	        		minimizable : true,
	        		maximizable : true,
	        		closable : true
	        	});
	        };
	        var globleIndex;
	    	function selectEventLogFile(index){
	    		globleIndex = index;
	    		mySelectWindow("选择日志文件");
	    		logMergeSelect_Window.window('open');
	    	}
	    	
	    	function submitSelection(){
	    		var row = $('#selectEventLog_id').datagrid('getSelected');
	//    		if (row){
	//    			alert('EventLog ID:'+row.id + "EventLog NAME:"+row.name);
	//    		}
	    		if(globleIndex == 1){
	    			$('#file1').textbox('setText',row.id);
	    			$('#file1').textbox('setValue',row.name);
	    			file1ID = row.id;
	    		}else if(globleIndex == 2){
	    			$('#file2').textbox('setText',row.id);
	    			$('#file2').textbox('setValue',row.name);
	    			file2ID = row.id;
	    		}else if(globleIndex == 3){
	    			$('#file3').textbox('setText',row.id);
	    			$('#file3').textbox('setValue',row.name);
	    			file3ID = row.id;
	    		}else if(globleIndex == 4){
	    			$('#file4').textbox('setText',row.id);
	    			$('#file4').textbox('setValue',row.name);
	    			file2ID = row.id;
	    		}
	    		logMergeSelect_Window.window('close');
	    	}
	
	    	function clearSelection(){
	    		logMergeSelect_Window.window('close');
	    	}
	    	
	    	$(function() {
	    		// 先通过ajax获取数据，然后再传给datagrid
	    		$.ajax({
	    			method : 'GET',
	    			url : '/ProcessMining/eventlog/getLog',
	    			async : true,
	    			dataType : 'json',
	    			success : function(data) {
	    				// alert(data.total);
	    				for (var i = 0; i < data.total; i++) {
	    					// alert(data.rows[file].orifileName);
	    					var row_data = {
	    						id : data.rows[i].id,
	    						name : data.rows[i].name,
	    						format : data.rows[i].format,
	    						date : data.rows[i].date,
	    						user : data.rows[i].user.username,
	    						link_eventlog : data.rows[i].link_eventlog,
	    						link_normlog : data.rows[i].link_normlog,
	    					};
	    					// alert(row_data);
	    					$('#selectEventLog_id').datagrid('appendRow', row_data);
	    				}
	    			},
	    			error : function() {
	    				alert('error');
	    			}
	    		});
	    	});
	    	
	    	//结果数据展示
	    	function mapdatatablePanel(){
	    		   		
	    		$("#mapdatatable").datagrid({
	    	        height: 225,
	    	        width: 380,
	    	        collapsible: true,
	    	        singleSelect: true,
	    	        title: '挖掘结果',
	    	        idField: 'data1',
	    	        columns: [[
	    	         { field: 'data1', title: '名称', width: 200,align: 'center'},
	    	            { field: 'data2', title: '项值', width: 178,align: 'center',editor: { type: 'text', options: { required: true } } }
	    	        ]],
	    	        toolbar: [],
	    	        onAfterEdit: function (rowIndex, rowData, changes) {
	    	            editRow = undefined;
	    	        },
	    	        onDblClickRow:function (rowIndex, rowData) {
	    	            if (editRow != undefined) {
	    	                $("#mapdatatable").datagrid('endEdit', editRow);
	    	            }
	    	 
	    	            if (editRow == undefined) {
	    	                $("#mapdatatable").datagrid('beginEdit', rowIndex);
	    	                editRow = rowIndex;
	    	            }
	    	        },
	    	        onClickRow:function(rowIndex,rowData){
	    	            if (editRow != undefined) {
	    	                $("#mapdatatable").datagrid('endEdit', editRow);
	    	 
	    	            }
	    	            
	    	        }
	    	        
	    	    });
	    		
	    	    $('div.datagrid-body').click(function(){
	    	    	if(editRow != undefined){
	    	        	$('#mapdatatable').datagrid('endEdit', editRow);
	    	            }
	    	    	})
	    	    	 
	    	    $("#mapdatatable").datagrid('insertRow', {
	    	         index: 0,
	    	         row: {
	    	              data1:'总实例数'
	    	        	}
	    	        });
	    	    $("#mapdatatable").datagrid('insertRow', {
	   	         index: 1,
	   	         row: {
	   	              data1:'总事件数'
	   	        	}
	   	        });
	    	    $("#mapdatatable").datagrid('insertRow', {
	   	         index: 2,
	   	         row: {
	   	              data1:'平均每实例中事件数'
	   	        	}
	   	        });
	    	    $("#mapdatatable").datagrid('insertRow', {
	   	         index: 3,
	   	         row: {
	   	              data1:'流程活动事件'
	   	        	}
	   	        });
	    	    $("#mapdatatable").datagrid('insertRow', {
	      	         index: 4,
	      	         row: {
	      	              data1:'流程活动操作人'
	      	        	}
	      	        });
	    	    $("#mapdatatable").datagrid('insertRow', {
	     	         index: 5,
	     	         row: {
	     	              data1:'挖掘耗时'
	     	        	}
	     	        });
	    	    
	    	} 
	    	
	    	/* function adjust(){
	    			var div = document.getElementById("my-canvas");
		    		var svg = div.getElementsByTagName("rect");
		    		var clickEvent = document.createEvent("HTMLEvents");  
		    		clickEvent.initEvent("click",false,true);
		    		svg[0].dispatchEvent(clickEvent);
		    		
		    		for (var i = 0, ii = labels.length; i < ii; i++) {
	            		var labelname = "label" + i;
	            		var textx = hashMap.Get(labelname).x;
	            		var texty = hashMap.Get(labelname).y;
	            		var att1 = { x: textx, y: texty };
	            		labels[i].attr(att1);
	            	} 
		    		
	    	} */
	    	
	    	function closeMiningResultWin(){
	    	    window.location.reload();
	    		/*$('#miningResultWin').form('clear');
	    		miningResult_Window.window('close');*/
	    	}
	    	
	    	var processmapWinWidth = 1000;
	    	var processmapWinHeight = 500;
	    	var processmap_Window;
	    	function processmapWin() {
	    			processmap_Window = $('#processmapWin').window({
	    			title: "流程模型展示",
	    			width : processmapWinWidth,
	    			height : processmapWinHeight,
	    			top: ($(window).height() - processmapWinHeight) * 0.2,
	    		    left: ($(window).width() - processmapWinWidth) * 0.5,
	    			modal : true,
	    			collapsible : true,
	    			minimizable : true,
	    			maximizable : true,
	    			closable : true,
	    		});
	    	};
	    	
	    	$(function () {
	            $('#processmapWin').window({
	                onBeforeClose: function () { //当面板关闭之前触发的事件
	                	//window.location.reload();
	                }
	
	
	            });
	        });
	    	function miningmodelselect(){
	    		processmapWin();
	    		processmap_Window.window('open');

	    		var type = $("#miningMapCombo").combobox("getText");
	    		
	    		var renderFn;
	    		var input;
	    		switch(type) {
	    			case "工作流网":
	    			renderFn = renderWorkflow;
	    			input = result1;
	    			break;
	    			case "流程图":
	    			renderFn = renderDiagraph;
	    			input = result2;
	    			break;
	    			case "力导向图":
	    			renderFn = renderForceLayout;
	    			input = result2;
	    			break;
	    			case "桑基图":
	    			renderFn = renderSankey;
	    			input = result2;break;
	    			default:
	    			return;
	    		}

	    		$('#processmapWin').html('');
	    		$('#processmapWin').append('<svg id="canvas" width="1000" height="450"></svg>');
	    		renderFn(input, '#canvas');
	    		/* setTimeout("adjust()", 100); */
	    	}
	    </script>
	    
</head>

<body>

	<!-- <form id="pluginFileUpload" name="Form" method="post" enctype="multipart/form-data">
		<div>
			<input type="file" class="file" name="file">
			 <label for="name">名称:</label>
			<input class="text" type="text" name="name" />
			<label for="name">参数类:</label>
			<input class="text" type="text" name="setting" />
			<label for="name">挖掘类:</label>
			<input class="text" type="text" name="mining" />
			<label for="name">挖掘方法:</label>
			<input class="text" type="text" name="method" />  
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">上传</a>
		</div>
	</form> -->
 	
 	<table id="processmining_id" class="easyui-datagrid" iconCls="icon-save" rownumbers="true" pagination="true" >
		<thead>
			<tr>
			    <th field="name" width="150" formatter="miningMethodformater" align="center">流程挖掘方法</th>
			    <th field="packageName" width="150" align="center">包名</th>
			    <th field="miningName" width="500" align="center">类名</th>
			    <th field="miningMethod" width="150" align="center">方法名</th>
			</tr>
		</thead>
	</table>	
 	
 	<div id="selectEventLogWin" class="easyui-window" closed="true" style="height:233px;top:40px;left:80px">
		<table id="selectEventLog_id" class="easyui-datagrid" toolbar="#tb3" iconCls="icon-save" rownumbers="true" singleSelect="true" pagination="true">
		     <thead>
			     <tr>
			         <th field="" checkbox='true' title="" width="30" align="center">日志名称</th>
			         <th field="name" width="150" align="center">日志名称</th>
				     <th data-options="field:'format',width:80" align="center">日志格式</th>
				     <th field="link_normlog" width="150" align="center">规范化日志</th>
				     <th field="link_eventlog" width="150" align="center">关联事件日志</th> 
				     <th data-options="field:'date',width:150,align:'center'">创建时间</th>
				     <th data-options="field:'user',width:80,align:'center'">创建人</th>
			      </tr>
		      </thead>
	      </table>
	    <div style="text-align:center;padding:10px">
	       <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitSelection()" style="margin-right:10px">确定</a>
	       <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearSelection()" style="margin-left:10px">取消</a>
	    </div>
	 </div>	
 	
 	<div id="miningSettingWin0" class="easyui-window" title="配置" closed="true" style="height: 500px;" >
            
            <div class="easyui-layout" data-options="fit:true">
	           	<div data-options="region:'north'" style="height:38px">
	           	     <table cellpadding="5">
	    				<tr>
	    		   			<td>
	    		   				<label for="name">请选择事件日志:</label>
                      			<!-- <input id="cc1" class="easyui-combobox" name="dept" data-options="valueField:'id',textField:'name'"> -->
                      			<input id="file1" type="text" name="name" value="" class="easyui-textbox" data-options="valueField:'id',textField:'name', prompt:'请点击选择日志文件...'" onclick="selectEventLogFile()" />
                      		</td>
	    				</tr>	    		
	    			</table> 
	    				    			
	           	</div>
	         	<div data-options="region:'center'">
	         	    <form id="miningSetting" method="post" >
	    	
	    				<table id="settingtable"></table>
	    	
	    			</form>

	
	    			<div style="text-align:center;padding:8px">
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitFormLogMining()" style="margin-right:10px">挖掘</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFormLogMining()" style="margin-left:10px">取消</a>
	    			</div>
	    	    </div>
            </div> 
                 
	 </div>
 	 	    
     <div id="miningSettingWin1" class="easyui-window" closed="true" style="height: 233px;">
     
     	<div class="easyui-layout" data-options="fit:true">
	           	<div data-options="region:'north'" style="height:38px">
	           	     <table cellpadding="5">
	    				<tr>
	    		   			<td>
	    		   				<label for="name">请选择事件日志:</label>
                      			<!-- <input id="cc1" class="easyui-combobox" name="dept" data-options="valueField:'id',textField:'name'"> -->
                      			<input id="file2" type="text" name="name" value="" class="easyui-textbox" data-options="valueField:'id',textField:'name', prompt:'请点击选择日志文件...'" onclick="selectEventLogFile()" />
                      		</td>
	    				</tr>	    		
	    			</table> 	    				    			
	           	</div>
	           	
	         	<div data-options="region:'center'">
	         	    <form id="miningSetting1" method="post" >
	    	
	    				<table id="settingtable1"></table>
	    	
	    			</form>

	
	    			<div style="text-align:center;padding:8px">
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitGeneticMining()" style="margin-right:10px">挖掘</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFormLogMining()" style="margin-left:10px">取消</a>
	    			</div>
	    	    </div>
            </div> 
            
            
	    	<!-- <table cellpadding="5">
	    		<tr>
	    		   <td>
	    		   	<label for="name">事件日志:</label>
                      <input id="cc1" class="easyui-combobox" name="dept"
                        data-options="valueField:'id',textField:'name'">
	    		   </td>
	    		</tr>	    		
	    	</table>
			
		<div class="easyui-panel" title="算法参数设置" style="width:400px">
		<div style="padding:10px 40px 10px 50px">
	    <form id="miningSetting1" method="post">
	    
	    	<table cellpadding="5">
	    		<tr>
	    			<td>交叉率:</td>
	    			<td><input class="easyui-textbox" type="text" name="relativeToBestThreshold" data-options="validType:'number'"></input></td>
	    		</tr>
	    		<tr>
	    			<td>变异率:</td>
	    			<td><input class="easyui-textbox" type="text" name="dependencyThreshold" data-options="validType:'number'"></input></td>
	    		</tr>
	    		<tr>
	    			<td>终止适应度值:</td>
	    			<td><input class="easyui-textbox" type="text" name="l1lThreshold" data-options="validType:'number'"></input></td>
	    		</tr>
	    		<tr>
	    			<td>种群规模:</td>
	    			<td><input class="easyui-textbox" type="text" name="l2lThreshold" data-options="validType:'number'"></input></td>
	    		</tr>
	    		<tr>
	    			<td>最大遗传代数:</td>
	    			<td><input class="easyui-textbox" type="text" name="l2lThreshold" data-options="validType:'number'"></input></td>
	    		</tr>
	    		<tr>
	    			<td>精英率:</td>
	    			<td><input class="easyui-textbox" type="text" name="l2lThreshold" data-options="validType:'number'"></input></td>
	    		</tr>
	    		<tr>
	    			<td>适应度计算方式:</td>
	    			<td>
	    				<select id="isLoop" class="easyui-combobox" name="extraInfo" style="width:150px">
	    				   <option value="Stop Semantics">Stop Semantics</option>
	    				   <option value="Continuous Semantics">Continuous Semantics</option>
	    				   <option value="ExtraBehavior">ExtraBehavior</option>
	    				</select>
	    			</td>
	    		</tr>
	    	</table> 	    		    	
	    	
	    </form>
	    </div>
	</div>
	    <div style="text-align:center;padding:8px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitGeneticMining()" style="margin-right:10px">挖掘</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFormLogMining()" style="margin-left:10px">取消</a>
	    </div> -->
	    
     </div>
     <div id="miningSettingWin2" class="easyui-window" title="配置" closed="true" style="height: 500px;" >
            
            <div class="easyui-layout" data-options="fit:true">
	           	<div data-options="region:'north'" style="height:38px">
	           	     <table cellpadding="5">
	    				<tr>
	    		   			<td>
	    		   				<label for="name">请选择事件日志:</label>
                      			<!-- <input id="cc1" class="easyui-combobox" name="dept" data-options="valueField:'id',textField:'name'"> -->
                      			<input id="file3" type="text" name="name" value="" class="easyui-textbox" data-options="valueField:'id',textField:'name', prompt:'请点击选择日志文件...'" onclick="selectEventLogFile()" />
                      		</td>
	    				</tr>	    		
	    			</table> 
	    				    			
	           	</div>
	         	<div data-options="region:'center'">

	
	    			<div style="text-align:center;padding:8px">
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitRegionLogMining()" style="margin-right:10px">挖掘</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearRegionLogMining()" style="margin-left:10px">取消</a>
	    			</div>
	    	    </div>
            </div> 
                 
	 </div>
     <div id="miningResultWin" class="easyui-window" title="挖掘结果" closed="true" style="height: 400px;">
     		                   	                   
			
	           	      <div title="数据展示" closable="false" style="padding:2px;">
	                   		<table id="mapdatatable"></table>
	                   </div>	    				    			
	           		         	
	         	    <table cellpadding="5">
	         	    	<tr>
	    					<td>
	    						<label for="ut">算法耗时:</label>
	    						<div id="usetime" class="usetime"></div>
	    					</td>
	    				</tr> 
	    				<tr>
	    		   			<td>
	    		   				<label for="name">选择流程表示模型:</label>
                      			 <select id="miningMapCombo" class="easyui-combobox" name="dept"  data-options="valueField:'id',textField:'name'" style="width:200px;">
                      			 	<option name="graph0">工作流网</option>
                           			<option name="graph1">流程图</option>
                           			<option name="graph2">力导向图</option>
									<option name="graph3">桑基图</option>
                           		</select>      			                       			
                      		</td>
	    				</tr>	    		
	    			</table>   			
	    	    	    	    	    	    
	         	    <div style="text-align:center;padding:8px">
	         	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="miningmodelselect()" style="margin-right:10px">确定</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeMiningResultWin()" style="margin-right:10px">关闭</a>
	    			</div>
            </div>
	   <!-- <div id="my-canvas" style="width:140px;padding:0px;margin:0px;"></div> -->
	   </div>
	   
	   <div id="processmapWin" class="easyui-window" title="流程模型" closed="true" >
		     	<!-- <svg id='canvas' width="1000" height="450"></svg> -->
	    </div> 
 
</body>
</html>