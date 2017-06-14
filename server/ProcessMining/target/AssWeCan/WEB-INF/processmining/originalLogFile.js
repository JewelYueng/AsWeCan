/**
 * 原始日志文件页面datagrid初始化 JS
 */
//$(function() {
//	// 先通过ajax获取数据，然后再传给datagrid
//	$.get("/ProcessMining/rawlog/getLog", function(data) {
//		//		if("success".equals(data.success)){
//		for (var i = 0; i < data.total; i++) {
//			//alert(data.rows[file].orifileName);
//			var row_data = {
//				id : data.rows[i].id,
//				name : data.rows[i].name,
//				format : data.rows[i].format,
//				date : data.rows[i].date,
//				user : data.rows[i].user.username,
//				link : data.rows[i].normLogLink,				
//			};
//			$('#orilog_id').datagrid('appendRow', row_data);
//		}
//		//		}else{
//		//			alert("查找失败!");
//		//		}
//	});
//})
/**
 * 自动隐藏列
 */
$(function() {
	$('#orilog_id').datagrid('hideColumn','normLogLink');
});

/**
 * 格式化操作列(原始日志关联的事件日志)
 * @param value  当前列对应字段值
 * @param row  当前的行记录数据
 * @param index  当前的行下标
 */
function oriLogformater(value, row, index) {
		var linkName= row.normLogLinkName;
		if(linkName == "" || linkName == null || linkName == "ubdefined"){
			return '<a href="javacript:void(0);" onclick="oriLogLink(' + index + ');">'
			'</a>';
		}else{
			return '<a href="javacript:void(0);" onclick="oriLogLink(' + index + ');">'
			+ linkName + '</a>';
		}	
}
function oriLogLink(index) {
	var row = $('#orilog_id').datagrid('getData').rows[index];
	var normLinkID = row.normLogLink;
//	alert(normLinkID);
	$('#jquerylogshow_id').tabs('select', '规范化日志');
	$('#normallog_id').datagrid('selectRecord',normLinkID);
	var rows = $('#normallog_id').datagrid('getData').rows;
//	alert(rows.length);
	var i = 0;
	for(i=0; i<rows.length; i++){
//		alert(i);
		if(rows[i].id == normLinkID){
			break;
		}
	}
	var myIndex = $('#normallog_id').datagrid('getRowIndex',rows[i]);
	$('#normallog_id').datagrid('selectRow', myIndex);
}

/**
 * 格式化操作列
 * @param value  当前列对应字段值
 * @param row  当前的行记录数据
 * @param index  当前的行下标
 */
function rowformaterOri(value, row, index) {
	return '<a href="javacript:void(0);" onclick="delOriFile(' + index + ');">删除</a>'
			+ "   " + '<a href="javacript:void(0);" onclick="downloadOriFile(' + index
			+ ');return false;">下载</a>' + "   " + '<a href="javacript:void(0);" onclick="normtrans(' + index
			+ ');">规范化</a>';
}

var TransSetting_Window
var myindex
var editRow = undefined;

var webTitleHeight =65;
var windowWidth =530;
var windowHeight =500;

var datagridWidth =516;

var datagridHeight1=400;
var datagridHeight2=80;
var datagridHeight3=304;
var datagridHeight4=385;

function normtrans(index){
	myindex=index;
	var settingTitle="配置信息";
	TransSetting_Window = $('#NormalTransSetWin').window({
		title:settingTitle,
		width : windowWidth,
		height : windowHeight,
		top: ($(window).height()-webTitleHeight - windowHeight) * 0.5+webTitleHeight,
	    left: ($(window).width() - windowWidth) * 0.5,
		modal : true,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		resizable:false,
		closable : true
	});
	TransSetting_Window.window('open');
	settable1();
	settable2();
	settable3();
	settable4();
	transdefaultset();
	
	
//	$('#transsetshow_id').tabs('select','数据项整合(时间项)');
//	$('#transsetshow_id').tabs('select','数据项格式配置');
	
	var row = $('#orilog_id').datagrid('getData').rows[index];
//	$.post("/ProcessMining/rawlog/deleteLog?name=" + row.name + "&id=" + row.id,
//			function(data) {
//				if (data.result) {
//					alert("删除成功!");
//					window.location.reload();
//				} else {
//					alert("删除失败!");
//				}
//			});
}


function settable1 () {
		
//    var editRow = undefined;
    
    /**
     * 数据项格式配置
     * 
     * */   
     $("#data_Table1").datagrid({
        height: datagridHeight1,
        width: datagridWidth,
        title: '数据项格式配置',
        collapsible: true,
        singleSelect: true,
//        url: '/Home/StuList',
        idField: 'data1',
        columns: [[
         { field: 'data1', title: '数据项名', width: 100,align: 'center',editor: { type: 'text', options: { required: true } } },
            { field: 'data2', title: '占位符', width: 100,align: 'center',editor: { type: 'text', options: { required: true } } },
            { field: 'data21', title: '格式标识项', width: 100,align: 'center',editor: { type: 'text', options: { required: true } } },
            { field: 'data3', title: '源格式', width: 100,align: 'center',editor: { type: 'text', options: { required: true } } ,formatter: function (data3) {
                return "<span title='" + data3 + "'>" + data3 + "</span>"}},
            { field: 'data4', title: '目标格式', width: 100,align: 'center',editor: { type: 'text', options: { required: true } } }
        ]],
        toolbar: [{
            text: '添加', iconCls: 'icon-add', handler: function () {
                if (editRow != undefined) {
                    $("#data_Table1").datagrid('endEdit', editRow);
                }
                if (editRow == undefined) {
                    $("#data_Table1").datagrid('insertRow', {
                        index: 0,
                        row: {}
                    });
 
                    $("#data_Table1").datagrid('beginEdit', 0);
                    editRow = 0;
                }
            }
        }, '-', {
            text: '删除', iconCls: 'icon-remove', handler: function () {
                var rows = $("#data_Table1").datagrid('getSelections');
                if(rows.length>0){
                	var index;
                	for(var i=0;i<rows.length;i++){
                		index =$("#data_Table1").datagrid('getRowIndex', rows[i]);
                		$("#data_Table1").datagrid('deleteRow', index);            		
                	}
                }
                 
            }
        },'-',
                
        ],
        onAfterEdit: function (rowIndex, rowData, changes) {
            editRow = undefined;
        },
        onDblClickRow:function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#data_Table1").datagrid('endEdit', editRow);
            }
 
            if (editRow == undefined) {
                $("#data_Table1").datagrid('beginEdit', rowIndex);
                editRow = rowIndex;
            }
        },
        onClickRow:function(rowIndex,rowData){
            if (editRow != undefined) {
                $("#data_Table1").datagrid('endEdit', editRow);
 
            }
            
        }
        
    }); 
     $('div.datagrid-body').click(function(){
    	 if(editRow != undefined)
    	 $('#data_Table1').datagrid('endEdit', editRow);	
    	 })

}


/**
 * 数据项整合(时间项)
 * 
 * */
function settable2() {
//    var editRow = undefined;
 
     $("#data_Table2").datagrid({
        height: datagridHeight2,
        width: datagridWidth,
        title: '时间项整合',
        collapsible: true,
        singleSelect: true,
//        url: '/Home/StuList',
        idField: 'data5',
        columns: [[
         { field: 'data5', title: '原数据项', width: 250,align: 'center',editor: { type: 'text', options: { required: true } } },
            { field: 'data6', title: '目标数据项', width: 250,align: 'center',editor: { type: 'text', options: { required: true } } }
        ]],
        
        
        onAfterEdit: function (rowIndex, rowData, changes) {
            editRow = undefined;
        },
        onDblClickRow:function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#data_Table2").datagrid('endEdit', editRow);
            }
 
            if (editRow == undefined) {
                $("#data_Table2").datagrid('beginEdit', rowIndex);
                editRow = rowIndex;
            }
        },
        onClickRow:function(rowIndex,rowData){
            if (editRow != undefined) {
                $("#data_Table2").datagrid('endEdit', editRow);
 
            }
            
        }
        
    }); 

}


function settable3() {
		
	
//    var editRow = undefined; 
    
    /**
     * 数据项整合(不含时间项)
     * 
     * */   
     $("#data_Table3").datagrid({
        height: datagridHeight3,
        width: datagridWidth,
        title: '数据项整合(不含时间项)',
        collapsible: true,
        singleSelect: true,
//        url: '/Home/StuList',
        idField: 'data7',
        columns: [[
         { field: 'data7', title: '原数据项', width: 250,align: 'center',editor: { type: 'text', options: { required: true } } },
            { field: 'data8', title: '目标数据项', width: 250,align: 'center',editor: { type: 'text', options: { required: true } } }
        ]],
        toolbar: [{
            text: '添加', iconCls: 'icon-add', handler: function () {
                if (editRow != undefined) {
                    $("#data_Table3").datagrid('endEdit', editRow);
                }
                if (editRow == undefined) {
                    $("#data_Table3").datagrid('insertRow', {
                        index: 0,
                        row: {}
                    });
 
                    $("#data_Table3").datagrid('beginEdit', 0);
                    editRow = 0;
                }
            }
        }, '-', {
            text: '删除', iconCls: 'icon-remove', handler: function () {
                var rows = $("#data_Table3").datagrid('getSelections');
                if(rows.length>0){
                	var index;
                	for(var i=0;i<rows.length;i++){
                		index =$("#data_Table3").datagrid('getRowIndex', rows[i]);
                		$("#data_Table3").datagrid('deleteRow', index);            		
                	}
                }
                 
            }
        }, '-', {
            text: '上移', iconCls: 'icon-up', handler: function () {
                MoveUp();
            }
        }, '-', {
            text: '下移', iconCls: 'icon-down', handler: function () {
                MoveDown();
            }
        },'-',
        ],
        onAfterEdit: function (rowIndex, rowData, changes) {
            editRow = undefined;
        },
        onDblClickRow:function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#data_Table3").datagrid('endEdit', editRow);
            }
 
            if (editRow == undefined) {
                $("#data_Table3").datagrid('beginEdit', rowIndex);
                editRow = rowIndex;
            }
        },
        onClickRow:function(rowIndex,rowData){
            if (editRow != undefined) {
                $("#data_Table3").datagrid('endEdit', editRow);
 
            }
            
        }
        
    }); 
     $('div.datagrid-body').click(function(){
    	 if(editRow != undefined){
        	 $('#data_Table3').datagrid('endEdit', editRow);
        	 $('#data_Table2').datagrid('endEdit', editRow);
             }
    	 })
}

/**
 * 记录格式配置
 * 
 * */
function settable4() {
//    var editRow = undefined;
 
     $("#data_Table4").datagrid({
        height: datagridHeight4,
        width: datagridWidth,
        title: '记录格式配置',
        collapsible: true,
        singleSelect: true,
//        url: '/Home/StuList',
        idField: 'data9',
        columns: [[
            { field: 'data9', title: '原数据项分隔符', width: 150,align: 'center',editor: { type: 'text', options: { required: true } } },
            { field: 'data10', title: '原名称值分隔符', width: 150,align: 'center',editor: { type: 'text', options: { required: true } } },
            { field: 'data11', title: '原空值字符串', width: 150,align: 'center',editor: { type: 'text', options: { required: true } } }
        ]],
        
        onAfterEdit: function (rowIndex, rowData, changes) {
            editRow = undefined;
        },
        onDblClickRow:function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#data_Table4").datagrid('endEdit', editRow);
            }
 
            if (editRow == undefined) {
                $("#data_Table4").datagrid('beginEdit', rowIndex);
                editRow = rowIndex;
            }
        },
        onClickRow:function(rowIndex,rowData){
            if (editRow != undefined) {
                $("#data_Table4").datagrid('endEdit', editRow);
 
            }
            
        }
        
    }); 
     $('div.datagrid-body').click(function(){
    	 if(editRow != undefined)
    	 $('#data_Table4').datagrid('endEdit', editRow);	
    	 })

}


/**
 * 插入规范化配置默认值
 */
function transdefaultset() {
//	alert(1);
	$("#data_Table1").datagrid('insertRow', {
        index: 0,
        row: {
        	data1:'[QC]',
        	data2:'ABCD',
        	data21:'[Method]',
        	data3:'Incident:A-B-C-D,Plan:C/B/ATD,Task:A/B/CTD,DEFAULT:A-B-CTD',
        	data4:'A-B-CTD'
        }
    });
//	alert(2);
	$("#data_Table2").datagrid('insertRow', {
        index: 0,
        row: {
        	data5:'[QC]',
        	data6:'Time'
        }
    });
//	alert(3);
	$("#data_Table3").datagrid('insertRow', {
        index: 0,
        row: {
        	data7:'[Method];[Status]',
        	data8:'EventName'
        }
    });
	$("#data_Table3").datagrid('insertRow', {
        index: 1,
        row: {
        	data7:'[FKPlanID]',
        	data8:'FKPlanID'
        }
    });
	$("#data_Table3").datagrid('insertRow', {
        index: 2,
        row: {
        	data7:'[PKIncidentID]',
        	data8:'PKIncidentID'
        }
    });
	$("#data_Table3").datagrid('insertRow', {
        index: 3,
        row: {
        	data7:'[PKTaskID]',
        	data8:'PKTaskID'
        }
    });
	$("#data_Table3").datagrid('insertRow', {
        index: 4,
        row: {
        	data7:'[PKPlanID]',
        	data8:'PKPlanID'
        }
    });
	$("#data_Table3").datagrid('insertRow', {
        index: 5,
        row: {
        	data7:'[FKIncidentID]',
        	data8:'FKIncidentID'
        }
    });
	$("#data_Table4").datagrid('insertRow', {
        index: 0,
        row: {
        	data9:'\\t',
        	data10:' ',
        	data11:''
        }
    });
	
}

/**
 * 配置表格按钮操作
 */


function MoveUp() {
    var row = $("#data_Table3").datagrid('getSelected');
    var index = $("#data_Table3").datagrid('getRowIndex', row);
    mysort(index, 'up', 'data_Table3');
     
}
//下移
function MoveDown() {
    var row = $("#data_Table3").datagrid('getSelected');
    var index = $("#data_Table3").datagrid('getRowIndex', row);
    mysort(index, 'down', 'data_Table3');
     
}
 
 
function mysort(index, type, gridname) {
    if ("up" == type) {
        if (index != 0) {
            var toup = $('#' + gridname).datagrid('getData').rows[index];
            var todown = $('#' + gridname).datagrid('getData').rows[index - 1];
            $('#' + gridname).datagrid('getData').rows[index] = todown;
            $('#' + gridname).datagrid('getData').rows[index - 1] = toup;
            $('#' + gridname).datagrid('refreshRow', index);
            $('#' + gridname).datagrid('refreshRow', index - 1);
            $('#' + gridname).datagrid('selectRow', index - 1);
        }
    } else if ("down" == type) {
        var rows = $('#' + gridname).datagrid('getRows').length;
        if (index != rows - 1) {
            var todown = $('#' + gridname).datagrid('getData').rows[index];
            var toup = $('#' + gridname).datagrid('getData').rows[index + 1];
            $('#' + gridname).datagrid('getData').rows[index + 1] = todown;
            $('#' + gridname).datagrid('getData').rows[index] = toup;
            $('#' + gridname).datagrid('refreshRow', index);
            $('#' + gridname).datagrid('refreshRow', index + 1);
            $('#' + gridname).datagrid('selectRow', index + 1);
        }
    }
 
}

/*function next1(){
	 $('#transsetshow_id').tabs('select','数据项整合(时间项)');
}
function next2(){
	 $('#transsetshow_id').tabs('select','数据项整合(不含时间项)');
}
function next3(){
	 $('#transsetshow_id').tabs('select','记录格式配置');
}*/

function transform(){
//	alert(myindex);
	var row = $('#orilog_id').datagrid('getData').rows[myindex];	
//	alert(myindex);
	// formats, timeNames, dataNames, itemSeparator, nameValSeparator, nulVal
	var formats="";               // 格式： [QC],ABCD,A-B-CTD,A-B-CTD; ...
	
	var timeNames="";             // 格式：[];[];[]...
	var dataNames="";             // 格式： [];[]:[],[]:[], ...
	
	var oriitemSeparator="";
	var orinameValSeparator="";
	var orinulVal="";
	
	var targetitemSeparator="";
	var targetnameValSeparator="";
	var targetnulVal="";
	
//	var myData = $('#data_Table3').datagrid('getData');
//	var rowData = $('#data_Table3').datagrid('getData').rows[0];
//	var rcData = $('#data_Table3').datagrid('getData').rows[0]['data7'];
//	var options = $('#data_Table3').datagrid('getRows').length;  
//	alert('myData : ' + JSON.stringify(myData));
//	alert('rowData : ' + JSON.stringify(rowData));
//	alert('rcData : ' + JSON.stringify(rcData));
//	alert('options : ' + JSON.stringify(options));
//	alert(totalRowNum);
	
	
	var dataTable1 = $('#data_Table1').datagrid('getData');
	var dataTable2 = $('#data_Table2').datagrid('getData');
	var dataTable3 = $('#data_Table3').datagrid('getData');
	var dataTable4 = $('#data_Table4').datagrid('getData');
	var rowNum1 = $('#data_Table1').datagrid('getRows').length;
	var rowNum3 = $('#data_Table3').datagrid('getRows').length;
	
	for(var i=0;i<rowNum1;i++){
		var temp="";
		temp+=dataTable1.rows[i]['data1']+",";
		temp+=dataTable1.rows[i]['data2']+",";
		temp+=dataTable1.rows[i]['data21']+",";
		temp+=dataTable1.rows[i]['data3']+",";
		temp+=dataTable1.rows[i]['data4'];
		
		formats += temp;
		if(i<rowNum1-1)
			formats+=";";
	}
	
	
//	alert(formats);
	for(var i=0;i<rowNum3;i++){
		var temp="";
		temp+=dataTable3.rows[i]['data7']+":";
		temp+=dataTable3.rows[i]['data8'];
		
		dataNames += temp;
		if(i<rowNum3-1)
			dataNames+=",";
	}
	
//	
	timeNames=dataTable2.rows[0]['data5'];
	
	oriitemSeparator=dataTable4.rows[0]['data9'];
	orinameValSeparator=dataTable4.rows[0]['data10'];
	orinulVal=dataTable4.rows[0]['data11'];
	
/*	targetitemSeparator=dataTable4.rows[0]['data12'];
	targetnameValSeparator=dataTable4.rows[0]['data13'];
	targetnulVal=dataTable4.rows[0]['data14'];*/
	
//    alert(dataNames);
	
	$.post("/ProcessMining/rawlog/normalizeLog?name="+row.name
			                                  +"&id="+row.id
			                                  +"&formats="+formats
			                                  +"&timeNames="+timeNames
			                                  +"&dataNames="+dataNames
			                                  +"&oriitemSeparator="+oriitemSeparator
			                                  +"&orinameValSeparator="+orinameValSeparator
			                                  +"&orinulVal="+orinulVal
			                                  +"&targetitemSeparator="+targetitemSeparator
			                                  +"&targetnameValSeparator="+targetnameValSeparator
			                                  +"&targetnulVal="+targetnulVal
			                                  ,
			function(data) {
//		        alert("ok");
				if (data.result) {
//					alert("规范化成功!");
					$.messager.show({
						title:'提示',
						msg:'规范化成功！',
						timeout:3000,
						showType:'slide'
					});
					TransSetting_Window.window('close');
					$('#jquerylogshow_id').tabs('select', '规范化日志');
					$("#normallog_id").datagrid("reload");
					$("#orilog_id").datagrid("reload");
//					TransSetting_Window.window('close');
//					freshNormDatagrid();
					
//				window.location.reload();
//					$('#jquerylogshow_id').tabs('select','规范化日志');
//					TransSetting_Window.window('close');
				} else {
//					alert("规范化失败!");
					$.messager.alert('规范化失败!','错误');
				}
			});
}

function back(){
	TransSetting_Window.window('close');
}
/*
function freshNormDatagrid() {
	$('#normallog_id').datagrid("loadData",{"total":"0","rows":[]});
    // 先通过ajax获取数据，然后再传给datagrid
    	$.ajax({
    		method : 'GET',
    		url : '/ProcessMining/normlog/getLog',
    		async : true,
    		dataType : 'json',
    		success : function(data) {
    			//alert(data.total);
    			for(var i=0;i<data.total;i++){
    				//alert(data.rows[file].orifileName);
    				var row_data = {
    						id : data.rows[i].id,
    						name : data.rows[i].name,
    						format : data.rows[i].format,
    						date : data.rows[i].date,
    						user : data.rows[i].user.username,
    						link_rawlog : data.rows[i].link_rawlog,
    						link_eventlog : data.rows[i].link_eventlog,
    					};
    				//alert(row_data);
    				$('#normallog_id').datagrid('appendRow', row_data);
    			}
    		},
    		error : function() {
    			alert('error');
    		}
    	});
    }

*/
/**
 * 文件删除-依据文件ID删除
 * @param id  文件ID
 */
function delOriFile(index) {
	//		alert(index);
	$.messager.defaults = { ok: "是", cancel: "否" };
	$.messager.confirm('确认', '请确认是否删除，删除后数据不可恢复?', function(r){
		if (r){
//			alert('confirmed: '+r);
			var row = $('#orilog_id').datagrid('getData').rows[index];
			$.post("/ProcessMining/rawlog/deleteLog?name=" + row.name + "&id=" + row.id,
					function(data) {
						if (data.result) {
//							alert("删除成功!");
							$("#orilog_id").datagrid("reload");
							$("#normallog_id").datagrid("reload");
							$("#eventlog_id").datagrid("reload");
							$.messager.show({
								title:'提示',
								msg:'删除成功!',
								timeout:3000,
								showType:'slide'
							});
						} else {
//							alert("删除失败!");
							$.messager.alert('删除失败!','错误');
						}
			});
		}
	});
}

/**
 * 文件查询-依据文件名称进行模糊匹配查询
 */
function doSearch() {
	var content = document.getElementById("search").value;
	//alert(content);
	$.get("/ProcessMining/rawlog/getLog?name=" + content, function(
			data) {
		//alert(data.total);
		//alert(data.rows[0].orifileName);
		//    		if("success".equals(data.success)){
		//alert("²éÕÒ³É¹¦!");
		$('#orilog_id').datagrid('loadData', {
			total : 0,
			rows : []
		});
		for (var i = 0; i < data.total; i++) {
			//alert(data.rows[file].orifileName);
			var row_data = {
				id : data.rows[i].id,
				name : data.rows[i].name,
				format : data.rows[i].format,
				date : data.rows[i].date,
				user : data.rows[i].user.username,
				link : data.rows[i].link,
			};
			//alert(row_data);
			$('#orilog_id').datagrid('appendRow', row_data);
		}
		//window.location.reload();
		//			}else{
		//				alert("²éÕÒÊ§°Ü!");
		//			}
	});
}

/**
 * 上传文件-弹窗配置然后选择文件上传
 */
var uploadOriFile_Window;
$(function() {
	uploadOriFile_Window = $('#upLoadOriFileWin').window({
		width : 350,
		height : 220,
		modal : true,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		closable : false
	});
});
function uploadOriFileClick() { //上传文件点击
//    	$('#win').window('open'); // open a window
//	$('#type').combobox('select',"");
//	$('#format').combobox('select',"");
	$("#isShare").filebox('setValue',"");
	uploadOriFile_Window.window('open');
	
}
function submitFormOri() { //点击提交上传
//	var type; //文件类型
//	if ($('#type').combobox('getText') == "原始日志") {
//		type = 0;
//	}
//	if ($('#type').combobox('getText') == "事件日志") {
//		type = 1;
//	}
//	var format = $('#format').combobox('getText'); //文件格式
//	var path = $("#txt_head").filebox('getValue'); //文件路径
//	var isShare = $("input[type='radio']:checked").val(); //是否共享文件
	//    	alert($('#cb').combobox('getText'));  //
	//    	alert($("input[type='radio']:checked").val()); //
	
//	$.post("/ProcessMining/rawlog/uploadLog?path=" + path + "&type=" + type
//			+ "&isShare=" + isShare + "&format=" + format, function(data) {
//		if (data.result) {
//			alert("上传成功!");
//			window.location.reload();
//		} else {
//			alert("上传失败!");
//		}
//	});
	
	$('#upOri').form('submit', {
		url:"/ProcessMining/rawlog/uploadLog",
		onSubmit: function(){
			uploadOriFile_Window.window('close');
		// do some check
		// return false to prevent submit;
		},
		success:function(data){
//			alert("上传成功");
//			uploadOriFile_Window.window('close');
			$("#orilog_id").datagrid("reload");
			$.messager.show({
				title:'提示',
				msg:'上传成功!',
				timeout:3000,
				showType:'slide'
			});
		},
		error:function(){
			$.messager.alert('上传失败!','错误');
		}
		
	});
	
	
}
function clearFormOri() {
//	$('#type').combobox('select', "");
//	$('#format').combobox('select', "");
	$("#isShare").filebox('setValue', "");
	uploadOriFile_Window.window('close');
}
/**
 * 级联获取上传文件类型和格式
 */
//$(function() {
//	// 下拉框选择控件，下拉框的内容是静态(动态查询数据库)数据
//	$('#type').combobox({
//		onHidePanel : function() {
//			$("#format").combobox("setValue", ''); //清除
//			var type = $('#type').combobox('getValue');
//
//			var data1 = [ {
//				"type" : "type1",
//				"format" : "xes"
//			}, {
//				"type" : "type2",
//				"format" : "log"
//			}, {
//				"type" : "type3",
//				"format" : "mxml"
//			}, {
//				"type" : "type4",
//				"format" : "other"
//			} ];
//			if (type == "原始日志") {
//				$("#format").combobox("loadData", data1);
//			}
//
//			var data2 = [ {
//				"type" : "type1",
//				"format" : "xes"
//			}, {
//				"type" : "type2",
//				"format" : "mxml"
//			}, {
//				"type" : "type3",
//				"format" : "other"
//			} ];
//			if (type == "事件日志") {
//				$("#format").combobox("loadData", data2);
//			}
//		}
//	});
//	$('#format').combobox({
//		//url:'itemManage!categorytbl', 
//		editable : false, //不可编辑状态
//		cache : false,
//		panelHeight : '150',//自动高度适合
//		valueField : 'type',
//		textField : 'format'
//	});
//});

/**
 * 文件下载-依据文件ID下载
 * @param id  文件ID
 */
function downloadOriFile(index) {
	var row = $('#orilog_id').datagrid('getData').rows[index];
	location.href = "/ProcessMining/rawlog/downloadLog?name=" + row.id; 
//	$.get("/ProcessMining/rawlog/downloadLog?name=" + row.id, function(data) {
//		
//	});
}
function rawLogformaterForUser(value,row,index){
	var row = $('#orilog_id').datagrid('getData').rows[index];
	if(row == null || row == "undefined" || row == ""){
		return;
	}
	return row.user.username;
}
