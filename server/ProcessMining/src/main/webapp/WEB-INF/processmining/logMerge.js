var file1ID;
var file2ID;
var mergeMethod;

var webTitleHeight =65;
var windowWidth =530;
var windowHeight =500;

var datagridWidth =516;
/**
 * 日志融合方法页面datagrid初始化 JS
 * （不使用数据库，为静态数据加载）
 */
//$(function() {
//    var row_data1 = {
//    	logMergeMethod : '人工免疫日志融合方法',
//    };
//    var row_data2 = {
//        logMergeMethod : '遗传算法日志融合方法',
//    };
//    $('#logmerge_id').datagrid('appendRow', row_data1);
//    $('#logmerge_id').datagrid('appendRow', row_data2);
//});
/**
 * 数据库动态加载数据（日志融合方法）
 */
$(function() {
	// 先通过ajax获取数据，然后再传给datagrid
	$.get("/ProcessMining/mergeEventLog/getLogMergeMethod", function(data) {
		for (var i = 0; i < data.total; i++) {
			//alert(data.rows[file].orifileName);
			var row_data = {
				//name : data.rows[i].name,
				logMergeMethodName : data.rows[i].logMergeMethodName,
			};
			//alert(row_data);
			$('#logmerge_id').datagrid('appendRow', row_data);
		}
		//		}else{
		//			alert("查找失败!");
		//		}
	});
})

/**
 * 选择事件日志文件页面datagrid初始化 JS
 */
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

//监听textbox文件选择的事件
$(function(){
	$("input",$("#file1").next("span")).click(function(){
		var index1 = 1;
		selectEventLogFile(index1);
	});
});
$(function(){
	$("input",$("#file2").next("span")).click(function(){
		var index2 = 2;
		selectEventLogFile(index2);
	});
});
$(function(){
	$("input",$("#file3").next("span")).click(function(){
		var index3 = 3;
		selectEventLogFile(index3);
	});
});
$(function(){
	$("input",$("#file4").next("span")).click(function(){
		var index4 = 4;
		selectEventLogFile(index4);
	});
});

/**
 * 动态加载Tab页()
 * @param title
 * @param url
 */
function AddTabs(title, url) {
	if ($('#jquerylogshow_id').tabs('exists', title)) {
		$('#jquerylogshow_id').tabs('select', title);
	} else {
		var content = '<iframe scrolling="auto" frameborder="0" src=" ' + url
				+ '" style="width:100%;height:100%;"></ifrmae>';
		$('#jquerylogshow_id').tabs('add', {
			title : title,
			content : content,
			closable : true
		});
	}
}

/**
 * 格式化操作列-日志融合
 * @param value  当前列对应字段值
 * @param row  当前的行记录数据
 * @param index  当前的行下标
 */
function logMergeformater(value, row, index) {
	return '<a href="javacript:;" onclick="logMergeSetting(' + index + ');">[应用]</a>'
			+ "  " 
			+ '<a href="javacript:;" onclick="seeDescription(' + index + ');">[说明]</a>';
}

/**
 * 弹窗进行融合参数配置(可选)
 */
var logMergeSetting_Window
function mySettingWindow(settingTitle) {
	logMergeSetting_Window = $('#logMergeSettingWin0').window({
		title:settingTitle,
		width : windowWidth,
		height : windowHeight+20,
		top: ($(window).height()-webTitleHeight - windowHeight) * 0.5+webTitleHeight-50,
	    left: ($(window).width() - windowWidth) * 0.5,
		modal : true,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		closable : true
	});
};
var aialogMergeSetting_Window
function myaiaSettingWindow(settingTitle) {
	aialogMergeSetting_Window = $('#aialogMergeSettingWin').window({
		title:settingTitle,
		width : windowWidth,
		height : windowHeight+20,
		top: ($(window).height()-webTitleHeight - windowHeight) * 0.5+webTitleHeight-50,
	    left: ($(window).width() - windowWidth) * 0.5,
		modal : true,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		closable : true
	});
};
var settingTitle;
function logMergeSetting(index){
	var row = $('#logmerge_id').datagrid('getData').rows[index];
	mergeMethod = row.logMergeMethodName;
	if(row.logMergeMethodName == "混合人工免疫日志融合方法"){
		settingTitle = row.logMergeMethodName + '参数设置';
		mySettingWindow(settingTitle);
		logMergeSetting_Window.window('open');
		settingtablePanel();
	}else{
		settingTitle = row.logMergeMethodName + '参数设置';
		myaiaSettingWindow(settingTitle);
		aialogMergeSetting_Window.window('open');
		aiasettingtablePanel();
	}
}
$(function() { //获取带选择融合文件的名称列表
	// 先通过ajax获取数据，然后再传给combox
	$.ajax({
		method : 'GET',
		url : '/ProcessMining/eventlog/getLog',
		async : true,
		dataType : 'json',
		success : function(data) {
			// alert(data.total);
			$("#cc1").combobox("loadData", data.rows);
			$("#cc2").combobox("loadData", data.rows);
		},
		error : function() {
			alert('error');
		}
	});
});

function clearFormLogMerge(){
	if("混合人工免疫日志融合方法" == mergeMethod){
		$('#file1').textbox('setValue', "");
		$('#file2').textbox('setValue', "");
		$('#ffSetting').form('clear');
		logMergeSetting_Window.window('close');
	}else if("人工免疫日志融合方法" == mergeMethod){
		$('#file3').textbox('setValue', "");
		$('#file4').textbox('setValue', "");
		$('#aiaffSetting').form('clear');
		aialogMergeSetting_Window.window('close');
	}
}

var editRow = undefined;
function settingtablePanel(){
	var tipValue = "双击可重设配置参数";
	$("#settingtable").datagrid({
        height: 200,
        width: datagridWidth-2,
        title: '算法参数设置',
        collapsible: true,
        singleSelect: true,
        columns: [[
         { field: 'name', title: '参数', width: 250,align: 'center'},
            { field: 'value', title: '设置', width: 250,align: 'center',editor: { type: 'text', options: { required: true } },
            	formatter: function (value) {return "<span title='" + tipValue + "'>" + value + "</span>";}
            }
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
              name:'初始种群规模',
              value:'60'
        	}
        });
    $("#settingtable").datagrid('insertRow', {
        index: 1,
        row: {
        	name:'种群最大进化代数',
        	value:'10000'
       	}
       });
    $("#settingtable").datagrid('insertRow', {
        index: 2,
        row: {
        	name:'退火接受最大连续退化次数',
        	value:'50'
       	}
       });
    $("#settingtable").datagrid('insertRow', {
        index: 3,
        row: {
        	name:'退火记忆库个体数',
        	value:'6'
       	}
       });
    $("#settingtable").datagrid('insertRow', {
        index: 4,
        row: {
        	name:'每代克隆选择比率',
        	value:'0.6'
       	}
       });
    
    $("#settingtable1").datagrid({
        height: 150,
        width: datagridWidth-2,
        title: '亲和度参数设置',
        collapsible: true,
        singleSelect: true,
        columns: [[
         { field: 'name', title: '参数', width: 250,align: 'center'},
            { field: 'value', title: '设置', width: 250,align: 'center',editor: { type: 'text', options: { required: true } },
            	formatter: function (value) {return "<span title='" + tipValue + "'>" + value + "</span>";}
            }
        ]],
        toolbar: [],
        onAfterEdit: function (rowIndex, rowData, changes) {
            editRow = undefined;
        },
        onDblClickRow:function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#settingtable1").datagrid('endEdit', editRow);
            }
 
            if (editRow == undefined) {
                $("#settingtable1").datagrid('beginEdit', rowIndex);
                editRow = rowIndex;
            }
        },
        onClickRow:function(rowIndex,rowData){
            if (editRow != undefined) {
                $("#settingtable1").datagrid('endEdit', editRow);
            }     
        }   
    });
	
    $('div.datagrid-body').click(function(){
    	if(editRow != undefined){
        	$('#settingtable1').datagrid('endEdit', editRow);
            }
    	}) 
    $("#settingtable1").datagrid('insertRow', {
         index: 0,
         row: {
              name:'相同属性值',
              value:'10'
        	}
        });
    $("#settingtable1").datagrid('insertRow', {
        index: 1,
        row: {
        	name:'相近实例频次',
        	value:'10'
       	}
       });
    $("#settingtable1").datagrid('insertRow', {
        index: 2,
        row: {
        	name:'重叠的时间区域',
        	value:'15'
       	}
       });
} 

var aiaeditRow = undefined;
function aiasettingtablePanel(){ 
	var tipValue = "双击可重设配置参数";
	$("#aiasettingtable").datagrid({
        height: 200,
        width: datagridWidth-2,
        title: '算法参数设置',
        collapsible: true,
        singleSelect: true,
        columns: [[
         { field: 'name', title: '参数', width: 250,align: 'center'},
            { field: 'value', title: '设置', width: 250,align: 'center',editor: { type: 'text', options: { required: true } },
            	formatter: function (value) {return "<span title='" + tipValue + "'>" + value + "</span>";}
            }
        ]],
        toolbar: [],
        onAfterEdit: function (rowIndex, rowData, changes) {
            aiaeditRow = undefined;
        },
        onDblClickRow:function (rowIndex, rowData) {
            if (aiaeditRow != undefined) {
                $("#aiasettingtable").datagrid('endEdit', aiaeditRow);
            }
 
            if (aiaeditRow == undefined) {
                $("#aiasettingtable").datagrid('beginEdit', rowIndex);
                aiaeditRow = rowIndex;
            }
        },
        onClickRow:function(rowIndex,rowData){
            if (aiaeditRow != undefined) {
                $("#aiasettingtable").datagrid('endEdit', aiaeditRow);
            }     
        }   
    });
	
    $('div.datagrid-body').click(function(){
    	if(aiaeditRow != undefined){
        	$('#aiasettingtable').datagrid('endEdit', aiaeditRow);
            }
    	}) 
    $("#aiasettingtable").datagrid('insertRow', {
         index: 0,
         row: {
              name:'初始种群规模',
              value:'60'
        	}
        });
    $("#aiasettingtable").datagrid('insertRow', {
        index: 1,
        row: {
        	name:'种群最大进化代数',
        	value:'10000'
       	}
       });
    $("#aiasettingtable").datagrid('insertRow', {
        index: 4,
        row: {
        	name:'每代克隆选择比率',
        	value:'0.6'
       	}
       });
    
    $("#aiasettingtable1").datagrid({
        height: 150,
        width: datagridWidth-2,
        title: '亲和度参数设置',
        collapsible: true,
        singleSelect: true,
        columns: [[
         { field: 'name', title: '参数', width: 250,align: 'center'},
            { field: 'value', title: '设置', width: 250,align: 'center',editor: { type: 'text', options: { required: true } },
            	formatter: function (value) {return "<span title='" + tipValue + "'>" + value + "</span>";}
            }
        ]],
        toolbar: [],
        onAfterEdit: function (rowIndex, rowData, changes) {
            aiaeditRow = undefined;
        },
        onDblClickRow:function (rowIndex, rowData) {
            if (aiaeditRow != undefined) {
                $("#aiasettingtable1").datagrid('endEdit', aiaeditRow);
            }
 
            if (aiaeditRow == undefined) {
                $("#aiasettingtable1").datagrid('beginEdit', rowIndex);
                aiaeditRow = rowIndex;
            }
        },
        onClickRow:function(rowIndex,rowData){
            if (aiaeditRow != undefined) {
                $("#aiasettingtable1").datagrid('endEdit', aiaeditRow);
            }     
        }   
    });
	
    $('div.datagrid-body').click(function(){
    	if(aiaeditRow != undefined){
        	$('#aiasettingtable1').datagrid('endEdit', aiaeditRow);
            }
    	}) 
    $("#aiasettingtable1").datagrid('insertRow', {
         index: 0,
         row: {
              name:'相同实例ID',
              value:'20'
        	}
        });
    $("#aiasettingtable1").datagrid('insertRow', {
        index: 1,
        row: {
        	name:'相同实例属性',
        	value:'10'
       	}
       });
}

/**
 * 融合的结果日志信息-弹窗显示（只读）
 */
var eventLogMergeSumariseWin_Window;
$(function() {
	eventLogMergeSumariseWin_Window = $('#logMergeResultSumariseWin').window({
	  width:400,
	  height:420,
	  modal:true,
	  collapsible:false,
	  minimizable : false,
	  maximizable : false,
	  closable:true
   });
});



/**
 * 提交日志融合方法参数表单
 */
function submitFormLogMerge(){
	if(null == mergeMethod || "undefined" == mergeMethod || "" == mergeMethod){
		alert("error occured,please check!");
	}else{
		if("混合人工免疫日志融合方法" == mergeMethod){
			var file1Id = file1ID;
			var file2Id = file2ID;
			//打开遮罩效果
			MaskUtil.mask();//打开遮罩效果
			$('#ffSetting').form('submit', {
			    url:"/ProcessMining/mergeEventLog/merge?file1Id=" + file1Id + "&file2Id=" + file2Id,
			    success: function (data) {
			    	//关闭遮罩效果
			    	var myData = eval("("+data+")");//转换为json对象
			    	MaskUtil.unmask();
//			    	window.location.reload();
			    	$("#eventlog_id").datagrid("reload");
			    	$('#jquerylogshow_id').tabs('select', '事件日志');
			    	eventLogMergeSumariseWin_Window.window('open');
			    	$('#rsultlog_pg').datagrid({
			            fitColumns: true,
			            nowrap: false,
			            columns: [[
			               {
			    	          field:"name",title:'名称', width:'120', align:'center'
			               },
			               { 
			            	  field: "value", title: '项值', width:'300', align:'center', formatter: function (value) {
			                    return "<span title='" + value + "'>" + value + "</span>";
			                 }
			               }
			            ]]
			       });
			       $('#time_pg').datagrid({
			            fitColumns: true,
			            nowrap: false,
			            columns: [[
			               {
			    	          field:"name",title:'名称', width:'120', align:'center'
			               },
			               { 
			            	  field: "value", title: '项值', width:'300', align:'center', formatter: function (value) {
			                    return "<span title='" + value + "'>" + value + "</span>";
			                 }
			               }
			            ]]
			       });
			    	$('#rsultlog_pg').datagrid('loadData',{total:0,rows:[]});
			    	$('#time_pg').datagrid('loadData',{total:0,rows:[]});
					var row_data1 = {
						name : '总实例数',
						value : myData.rows.cases,
					};
					$('#rsultlog_pg').datagrid('appendRow',row_data1);
					var row_data2 = {
						name : '总事件数',
						value : myData.rows.events,
					};
					$('#rsultlog_pg').datagrid('appendRow',row_data2);
					var row_data3 = {
						name : '平均每实例中事件数',
						value : myData.rows.perEventInCase,
					};
					$('#rsultlog_pg').datagrid('appendRow',row_data3);
					var row_data4 = {
						name : '流程活动事件',
						value : myData.rows.evnetNmaes,
					};
					$('#rsultlog_pg').datagrid('appendRow',row_data4);
					var row_data5 = {
						name : '流程活动操作人',
						value : myData.rows.operatorNmaes,
					};
					$('#rsultlog_pg').datagrid('appendRow',row_data5);
			        
					//融合时间相关数据
					var row_data6 = {
						name : "初始化耗时",
						value : myData.times.inittime + "(ms)"
					};
					$('#time_pg').datagrid('appendRow',row_data6);
					var row_data7 = {
						name : "种群进化耗时",
						value : myData.times.poptime + "(ms)"
					};
					$('#time_pg').datagrid('appendRow',row_data7);
					var row_data8 = {
						name : "总耗时",
						value : myData.times.min + "(min)" + ":" + myData.times.sec + "(s)" + ":" + myData.times.msec + "(ms)"
					};
					$('#time_pg').datagrid('appendRow',row_data8);
					var row_data9 = {
						name : "已配对数目",
						value : myData.times.numOfLinks
					};
					$('#time_pg').datagrid('appendRow',row_data9);
			    }
			});
			clearFormLogMerge();
			logMergeSetting_Window.window('close');
		}
		else if("人工免疫日志融合方法" == mergeMethod){
			var file1Id = file1ID;
			var file2Id = file2ID;
			//打开遮罩效果
			MaskUtil.mask();//打开遮罩效果
			$('#aiaffSetting').form('submit', {
			    url:"/ProcessMining/mergeEventLog/aiaMerge?file1Id=" + file1Id + "&file2Id=" + file2Id,
			    success: function (data) {
			    	//关闭遮罩效果
			    	var myData = eval("("+data+")");//转换为json对象 
			    	MaskUtil.unmask();
//			    	window.location.reload();
			    	$('#jquerylogshow_id').tabs('select', '事件日志');
			    	$("#eventlog_id").datagrid("reload");
			    	eventLogMergeSumariseWin_Window.window('open');		    	
			    	$('#rsultlog_pg').datagrid({
			            fitColumns: true,
			            nowrap: false,
			            columns: [[
			               {
			    	          field:"name",title:'名称', width:'120', align:'center'
			               },
			               { 
			            	  field: "value", title: '项值', width:'300', align:'center', formatter: function (value) {
			                    return "<span title='" + value + "'>" + value + "</span>";
			                 }
			               }
			            ]]
			       });
			       $('#time_pg').datagrid({
			            fitColumns: true,
			            nowrap: false,
			            columns: [[
			               {
			    	          field:"name",title:'名称', width:'120', align:'center'
			               },
			               { 
			            	  field: "value", title: '项值', width:'300', align:'center', formatter: function (value) {
			                    return "<span title='" + value + "'>" + value + "</span>";
			                 }
			               }
			            ]]
			       });
			    	$('#rsultlog_pg').datagrid('loadData',{total:0,rows:[]});
			    	$('#time_pg').datagrid('loadData',{total:0,rows:[]});
					var row_data1 = {
						name : '总实例数',
						value : myData.rows.cases,
					};
					$('#rsultlog_pg').datagrid('appendRow',row_data1);
					var row_data2 = {
						name : '总事件数',
						value : myData.rows.events,
					};
					$('#rsultlog_pg').datagrid('appendRow',row_data2);
					var row_data3 = {
						name : '平均每实例中事件数',
						value : myData.rows.perEventInCase,
					};
					$('#rsultlog_pg').datagrid('appendRow',row_data3);
					var row_data4 = {
						name : '流程活动事件',
						value : myData.rows.evnetNmaes,
					};
					$('#rsultlog_pg').datagrid('appendRow',row_data4);
					var row_data5 = {
						name : '流程活动操作人',
						value : myData.rows.operatorNmaes,
					};
					$('#rsultlog_pg').datagrid('appendRow',row_data5);
			        
					//融合时间相关数据
					var row_data6 = {
						name : "初始化耗时",
						value : myData.times.inittime + "(ms)"
					};
					$('#time_pg').datagrid('appendRow',row_data6);
					var row_data7 = {
						name : "种群进化耗时",
						value : myData.times.poptime + "(ms)"
					};
					$('#time_pg').datagrid('appendRow',row_data7);
					var row_data8 = {
						name : "总耗时",
						value : myData.times.min + "(min)" + ":" + myData.times.sec + "(s)" + ":" + myData.times.msec + "(ms)"
					};
					$('#time_pg').datagrid('appendRow',row_data8);
					var row_data9 = {
						name : "已配对数目",
						value : myData.times.numOfLinks
					};
					$('#time_pg').datagrid('appendRow',row_data9);
			    }
			});
			clearFormLogMerge();
			aialogMergeSetting_Window.window('close');
		}
	}
}


///**
// * 提交日志融合方法参数表单
// */
//function submitFormLogMerge(){
//	if(null == mergeMethod || "undefined" == mergeMethod || "" == mergeMethod){
//		alert("error occured,please check!");
//	}else{
//		if("混合人工免疫日志融合方法" == mergeMethod){
////			var file1Name = $("#cc1").combobox("getText");
////			var file1Id = $("#cc1").combobox("getValue");
////			var file2Name = $("#cc2").combobox("getText");
////			var file2Id = $("#cc2").combobox("getValue");
//			var file1Id = file1ID;
//			var file2Id = file2ID;
//			//打开遮罩效果
//			MaskUtil.mask();//打开遮罩效果
//			$('#ffSetting').form('submit', {
//			    url:"/ProcessMining/mergeEventLog/merge?file1Id=" + file1Id + "&file2Id=" + file2Id,
//			    success: function (data) {
//			    	//关闭遮罩效果
//			    	var myData = eval("("+data+")");//转换为json对象
//			    	MaskUtil.unmask();
////			    	window.location.reload();
//			    	$('#jquerylogshow_id').tabs('select', '事件日志');
//			    	$("#eventlog_id").datagrid("reload");
//			    	eventLogMergeSumariseWin_Window.window('open');
//			    	$('#rsultlog_pg').datagrid({
//			            fitColumns: true,
//			            nowrap: false,
//			            columns: [[
//			               {
//			    	          field:"name",title:'名称', width:'120', align:'center'
//			               },
//			               { 
//			            	  field: "value", title: '项值', width:'300', align:'center', formatter: function (value) {
//			                    return "<span title='" + value + "'>" + value + "</span>";
//			                 }
//			               }
//			            ]]
//			       });
//			       $('#time_pg').datagrid({
//			            fitColumns: true,
//			            nowrap: false,
//			            columns: [[
//			               {
//			    	          field:"name",title:'名称', width:'120', align:'center'
//			               },
//			               { 
//			            	  field: "value", title: '项值', width:'300', align:'center', formatter: function (value) {
//			                    return "<span title='" + value + "'>" + value + "</span>";
//			                 }
//			               }
//			            ]]
//			       });
//			    	$('#rsultlog_pg').datagrid('loadData',{total:0,rows:[]});
//			    	$('#time_pg').datagrid('loadData',{total:0,rows:[]});
//					var row_data1 = {
//						name : '总实例数',
//						value : myData.rows.cases,
//					};
//					$('#rsultlog_pg').datagrid('appendRow',row_data1);
//					var row_data2 = {
//						name : '总事件数',
//						value : myData.rows.events,
//					};
//					$('#rsultlog_pg').datagrid('appendRow',row_data2);
//					var row_data3 = {
//						name : '平均每实例中事件数',
//						value : myData.rows.perEventInCase,
//					};
//					$('#rsultlog_pg').datagrid('appendRow',row_data3);
//					var row_data4 = {
//						name : '流程活动事件',
//						value : myData.rows.evnetNmaes,
//					};
//					$('#rsultlog_pg').datagrid('appendRow',row_data4);
//					var row_data5 = {
//						name : '流程活动操作人',
//						value : myData.rows.operatorNmaes,
//					};
//					$('#rsultlog_pg').datagrid('appendRow',row_data5);
//			        
//					//融合时间相关数据
//					var row_data6 = {
//						name : "初始化耗时",
//						value : myData.times.inittime + "(ms)"
//					};
//					$('#time_pg').datagrid('appendRow',row_data6);
//					var row_data7 = {
//						name : "种群进化耗时",
//						value : myData.times.poptime + "(ms)"
//					};
//					$('#time_pg').datagrid('appendRow',row_data7);
//					var row_data8 = {
//						name : "总耗时",
//						value : myData.times.min + "(min)" + ":" + myData.times.sec + "(s)" + ":" + myData.times.msec + "(ms)"
//					};
//					$('#time_pg').datagrid('appendRow',row_data8);
//					var row_data9 = {
//						name : "已配对数目",
//						value : myData.times.numOfLinks
//					};
//					$('#time_pg').datagrid('appendRow',row_data9);
//			    }
//			});
//			clearFormLogMerge();
//			logMergeSetting_Window.window('close');
//		}
//		else if("人工免疫日志融合方法" == mergeMethod){
//			var file1Id = file1ID;
//			var file2Id = file2ID;
//			//打开遮罩效果
//			MaskUtil.mask();//打开遮罩效果
//			$('#aiaffSetting').form('submit', {
//			    url:"/ProcessMining/mergeEventLog/aiaMerge?file1Id=" + file1Id + "&file2Id=" + file2Id,
//			    success: function (data) {
//			    	//关闭遮罩效果
//			    	var myData = eval("("+data+")");//转换为json对象 
//			    	MaskUtil.unmask();
////			    	window.location.reload();
//			    	$('#jquerylogshow_id').tabs('select', '事件日志');
//			    	$("#eventlog_id").datagrid("reload");
//			    	eventLogMergeSumariseWin_Window.window('open');
////			    	$('#rsultlog_pg').datagrid({
////			            fitColumns: true,
////			            nowrap: false,
////			            columns: [[
////			               {
////			    	          field:"name",title:'名称', width:'120', align:'center'
////			               },
////			               { 
////			            	  field: "value", title: '项值', width:'300', align:'center', formatter: function (value) {
////			                    return "<span title='" + value + "'>" + value + "</span>";
////			                 }
////			               }
////			            ]]
////			       });
////			       $('#rsultlog_pg').propertygrid('loadData',{total:0,rows:[]});
////			    	var row_data1 = {
////							name : '总实例数',
////							value : myData.rows.cases,
////						};
////						$('#rsultlog_pg').datagrid('appendRow',row_data1);
////						var row_data2 = {
////							name : '总事件数',
////							value : myData.rows.events,
////						};
////						$('#rsultlog_pg').datagrid('appendRow',row_data2);
////						var row_data3 = {
////							name : '平均每实例中事件数',
////							value : myData.rows.perEventInCase,
////						};
////						$('#rsultlog_pg').datagrid('appendRow',row_data3);
////						var row_data4 = {
////							name : '流程活动事件',
////							value : myData.rows.evnetNmaes,
////						};
////						$('#rsultlog_pg').datagrid('appendRow',row_data4);
////						var row_data5 = {
////							name : '流程活动操作人',
////							value : myData.rows.operatorNmaes,
////						};
////						$('#rsultlog_pg').datagrid('appendRow',row_data5);
////			    }
////			});
////			aiaclearFormLogMerge();
////			aialogMergeSetting_Window.window('close');
//			    	
//			    	$('#rsultlog_pg').datagrid({
//			            fitColumns: true,
//			            nowrap: false,
//			            columns: [[
//			               {
//			    	          field:"name",title:'名称', width:'120', align:'center'
//			               },
//			               { 
//			            	  field: "value", title: '项值', width:'300', align:'center', formatter: function (value) {
//			                    return "<span title='" + value + "'>" + value + "</span>";
//			                 }
//			               }
//			            ]]
//			       });
//			       $('#time_pg').datagrid({
//			            fitColumns: true,
//			            nowrap: false,
//			            columns: [[
//			               {
//			    	          field:"name",title:'名称', width:'120', align:'center'
//			               },
//			               { 
//			            	  field: "value", title: '项值', width:'300', align:'center', formatter: function (value) {
//			                    return "<span title='" + value + "'>" + value + "</span>";
//			                 }
//			               }
//			            ]]
//			       });
//			    	$('#rsultlog_pg').datagrid('loadData',{total:0,rows:[]});
//			    	$('#time_pg').datagrid('loadData',{total:0,rows:[]});
//					var row_data1 = {
//						name : '总实例数',
//						value : myData.rows.cases,
//					};
//					$('#rsultlog_pg').datagrid('appendRow',row_data1);
//					var row_data2 = {
//						name : '总事件数',
//						value : myData.rows.events,
//					};
//					$('#rsultlog_pg').datagrid('appendRow',row_data2);
//					var row_data3 = {
//						name : '平均每实例中事件数',
//						value : myData.rows.perEventInCase,
//					};
//					$('#rsultlog_pg').datagrid('appendRow',row_data3);
//					var row_data4 = {
//						name : '流程活动事件',
//						value : myData.rows.evnetNmaes,
//					};
//					$('#rsultlog_pg').datagrid('appendRow',row_data4);
//					var row_data5 = {
//						name : '流程活动操作人',
//						value : myData.rows.operatorNmaes,
//					};
//					$('#rsultlog_pg').datagrid('appendRow',row_data5);
//			        
//					//融合时间相关数据
//					var row_data6 = {
//						name : "初始化耗时",
//						value : myData.times.inittime + "(ms)"
//					};
//					$('#time_pg').datagrid('appendRow',row_data6);
//					var row_data7 = {
//						name : "种群进化耗时",
//						value : myData.times.poptime + "(ms)"
//					};
//					$('#time_pg').datagrid('appendRow',row_data7);
//					var row_data8 = {
//						name : "总耗时",
//						value : myData.times.min + "(min)" + ":" + myData.times.sec + "(s)" + ":" + myData.times.msec + "(ms)"
//					};
//					$('#time_pg').datagrid('appendRow',row_data8);
//					var row_data9 = {
//						name : "已配对数目",
//						value : myData.times.numOfLinks
//					};
//					$('#time_pg').datagrid('appendRow',row_data9);
//			    }
//			});
//			clearFormLogMerge();
//			aialogMergeSetting_Window.window('close');
//		}
//	}
//}


/**
 * 弹窗查看日志融合方法描述(只读)
 */
var logMergeDes_Window;
function myDesWindow(desContent,desTitle) {
	logMergeDes_Window = $('#logMergeDesWin').window({
	  title:desTitle,
	  width:300,
	  height:200,
	  content:desContent,
	  modal:true,
	  collapsible:false,
	  minimizable : false,
	  maximizable : false,
	  closable:true
   });
};
var desContent;
var desTitle;
function seeDescription(index){
	var row = $('#logmerge_id').datagrid('getData').rows[index];
	if(row.logMergeMethodName == '人工免疫日志融合方法'){
		desTitle = row.logMergeMethodName + '说明';
		desContent = row.logMergeMethodName + '是一种基于人工免疫算法的日志融合方法，是在实例级别进行日志融合。\n' 
		+ '\n' + '您可以点击[应用]进行参数配置，然后进行融合计算。';
	}else if(row.logMergeMethodName == '混合人工免疫日志融合方法'){
		desTitle = row.logMergeMethodName + '说明';
		desContent = row.logMergeMethodName + '是基于遗传算法的事件日志融合方法。\n'
		+ '\n' + '您可以点击[应用]进行参数配置，然后进行融合计算。';
	}
	myDesWindow(desContent,desTitle);
	logMergeDes_Window.window('open');
}


/**
 * 使用方法:
 * 开启:MaskUtil.mask();
 * 关闭:MaskUtil.unmask();
 * MaskUtil.mask('其它提示文字...');
 */
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


/**
 * 弹窗选择事件日志文件(可选)
 */
var logMergeSelect_Window;
function mySelectWindow(settingTitle) {
	logMergeSelect_Window = $('#selectEventLogWin').window({
		title:settingTitle,
		width : 850,
		height : 500,
		modal : true,
		collapsible : false,
		minimizable : false,
		maximizable : false,
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
//	if (row){
//		alert('EventLog ID:'+row.id + "EventLog NAME:"+row.name);
//	}
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
		file1ID = row.id;
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
