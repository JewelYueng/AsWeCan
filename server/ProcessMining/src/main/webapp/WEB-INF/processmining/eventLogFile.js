var windowWidth =530;
var windowHeight =500;

var datagridWidth =516;

/**
 * 事件日志文件页面datagrid初始化 JS
 */
//$(function() {
//	// 先通过ajax获取数据，然后再传给datagrid
//	$.ajax({
//		method : 'GET',
//		url : '/ProcessMining/eventlog/getLog',
//		async : true,
//		dataType : 'json',
//		success : function(data) {
//			// alert(data.total);
//			for (var i = 0; i < data.total; i++) {
//				// alert(data.rows[file].orifileName);
//				var row_data = {
//					id : data.rows[i].id,
//					name : data.rows[i].name,
//					format : data.rows[i].format,
//					date : data.rows[i].date,
//					user : data.rows[i].user.username,
//					link_eventlog : data.rows[i].link_eventlog,
//					link_normlog : data.rows[i].link_normlog,
//				};
//				// alert(row_data);
//				$('#eventlog_id').datagrid('appendRow', row_data);
//			}
//		},
//		error : function() {
//			alert('error');
//		}
//	});
//});
/**
 * 自动隐藏列
 */
$(function() {
	$('#eventlog_id').datagrid('hideColumn','normLogLink');
});

/**
 * 格式化操作列
 * @param value 当前列对应字段值
 * @param row 当前的行记录数据
 * @param index 当前的行下标
 */
function rowformaterEvent(value, row, index) {
	return '<a href="javacript:void(0);" onclick="delEventFile(' + index + ');">删除</a>'
			+ "   " + '<a href="javacript:void(0);" onclick="downloadEventFile(' + index
			+ ');return false;">下载</a>' + "   " + '<a href="javacript:void(0);" onclick="downloadEventFile(' + index
			+ ');">流程挖掘</a>';
}

function eventLogformaterForUser(value,row,index){
	var row = $('#eventlog_id').datagrid('getData').rows[index];
	if(row == null || row == "undefined" || row == ""){
		return;
	}
	return row.user.username;
}

//关闭弹窗
function closeSumariseWin(){
	  eventLogSumariseWin_Window.window('close');
}
  /**
   * 格式化操作列(事件日志摘要)
   * @param value  当前列对应字段值
   * @param row  当前的行记录数据
   * @param index  当前的行下标
   */
  function eventLogformater(value,row,index){
	  return '<a href="javacript:;" onclick="eventLogSumarise('+index+');">'+row.name+'</a>';
  }
  /**
   * 事件日志摘要-弹窗显示（只读）
   */
  var eventLogSumariseWin_Window;
  $(function() {
     eventLogSumariseWin_Window = $('#eventLogSumariseWin').window({
  	  width:windowWidth,
  	  height:windowHeight,
  	  top: ($(window).height()-85 - windowHeight) * 0.5+85,
  	  left: ($(window).width() - windowWidth) * 0.5,
  	  modal:true,
  	  collapsible:false,
  	  minimizable:false,
  	  maximizable:false,
  	  closable:true
     });
  });
  function eventLogSumarise(index){
  	var row = $('#eventlog_id').datagrid('getData').rows[index];
  	var id = row.id;
  	eventLogSumariseWin_Window.window('open');
	$('#pg').datagrid({
//        fitColumns: true,
		width:datagridWidth,
        nowrap: false,
        columns: [[
           {
	          field:"name",title:'名称', width:'160', align:'center'
           },
           { 
        	  field: "value", title: '项值', width:'350', align:'center', formatter: function (value) {
                return "<span title='" + value + "'>" + value + "</span>";
             }
           }
        ]]
   });
  	// 先通过ajax获取数据，然后再传给propertygrid
  	$.get("/ProcessMining/eventFile/parseEventFile?id=" + id, function(data){
//  		if("success".equals(data.success)){
				//alert("查找成功!");
  		$('#pg').datagrid('loadData',{total:0,rows:[]});
  	
			var row_data1 = {
				name : '总实例数',
				value : data.rows.cases,
			};
			$('#pg').datagrid('appendRow',row_data1);
			var row_data2 = {
				name : '总事件数',
				value : data.rows.events,
			};
			$('#pg').datagrid('appendRow',row_data2);
			var row_data3 = {
				name : '平均每实例中事件数',
				value : data.rows.perEventInCase,
			};
			$('#pg').datagrid('appendRow',row_data3);
			var row_data4 = {
				name : '流程活动事件',
				value : data.rows.evnetNmaes,
			};
			$('#pg').datagrid('appendRow',row_data4);
			var row_data5 = {
				name : '流程活动操作人',
				value : data.rows.operatorNmaes,
			};
			$('#pg').datagrid('appendRow',row_data5);
  		
//  		$('#pg').propertygrid('loadData',{total:0,rows:[]});
//				for(var i=0;i<data.total;i++){
//  				var row_data1 = {
//  					name : '总实例数',
//  					value : data.rows.cases,
//  					group : 'event org.k2.processmining',
//  				};
//  				$('#pg').propertygrid('appendRow',row_data1);
//  				var row_data2 = {
//      				name : '总事件数',
//      				value : data.rows.events,
//      				group : 'event org.k2.processmining',
//      			};
//      			$('#pg').propertygrid('appendRow',row_data2);
//      			var row_data3 = {
//      				name : '平均每实例中事件数',
//      				value : data.rows.perEventInCase,
//      				group : 'event org.k2.processmining',
//      			};
//      			$('#pg').propertygrid('appendRow',row_data3);
//      			var row_data4 = {
//      				name : '流程活动事件',
//      				value : data.rows.evnetNmaes,
//      				group : 'event org.k2.processmining',
//      			};
//      			$('#pg').propertygrid('appendRow',row_data4);
//      			var row_data5 = {
//      				name : '流程活动操作人',
//      				value : data.rows.operatorNmaes,
//      				group : 'event org.k2.processmining',
//      			};
//      			$('#pg').propertygrid('appendRow',row_data5);
//  		 }

		});
  }
  
  /**
   * 格式化操作列(事件日志关联的规范化日志)
   * @param value  当前列对应字段值
   * @param row  当前的行记录数据
   * @param index  当前的行下标
   */
  function eventLogformater1(value,row,index){
	  if(row.normLogLinkName == "" || row.normLogLinkName == null || row.normLogLinkName == "undefined"){
		  return '<a href="javacript:;" onclick="eventLogLink1('+index+');">'+""+'</a>';  
	  }else{
		  return '<a href="javacript:;" onclick="eventLogLink1('+index+');">'+row.normLogLinkName+'</a>';  
	  }
  }
  function eventLogLink1(index){
	    var row = $('#eventlog_id').datagrid('getData').rows[index];
		var normLinkID = row.normLogLink;
		$('#jquerylogshow_id').tabs('select', '规范化日志');;
		var rows = $('#normallog_id').datagrid('getData').rows;
		var i;
		for(i=0; i<rows.length; i++){
			if(rows[i].id == normLinkID){
				break;
			}
		}
		var myIndex = $('#normallog_id').datagrid('getRowIndex',rows[i]);
		$('#normallog_id').datagrid('selectRow', myIndex);
  }

  /**
   * 格式化操作列(事件日志关联的事件日志)
   * @param value  当前列对应字段值
   * @param row  当前的行记录数据
   * @param index  当前的行下标
   */
  function eventLogformater2(value,row,index){
	  if(row.eventLogLink == "" || row.eventLogLink == null){
		  return '<a href="javacript:;" onclick="eventLogLink2('+index+');">'+""+'</a>'; 
	  }else{
		  return '<a href="javacript:;" onclick="eventLogLink2('+index+');">'+row.eventLogLink+'</a>';
	  }
  }
  function eventLogLink2(index){
	    var row = $('#eventlog_id').datagrid('getData').rows[index];
		var eventLinkIDs = row.eventLogLink;
		var strs = new Array(); 
		strs = eventLinkIDs.split(",");
		for (var i=0;i<strs.length ;i++){ 
//		    document.write(strs[i]+"<br/>"); //分割后的字符输出 
			$('#jquerylogshow_id').tabs('select', '事件日志');
			var rows = $('#eventlog_id').datagrid('getData').rows;
			var j;
			for(j=0; j<rows.length; j++){
				if(rows[j].id == strs[i]){
					break;
				}
			}
			var myIndex = $('#eventlog_id').datagrid('getRowIndex',rows[j]);
			$('#eventlog_id').datagrid('selectRow', myIndex);
		} 
  }
  
  /**
   * 文件删除-依据文件ID删除
   * @param id  文件ID
   */
  function delEventFile(index) {
  	//		alert(index);
	  $.messager.defaults = { ok: "是", cancel: "否" };
		$.messager.confirm('确认', '请确认是否删除，删除后数据不可恢复?', function(r){
			if (r){
				var row = $('#eventlog_id').datagrid('getData').rows[index];
			  	var flag = true;
			  	$.post("/ProcessMining/eventlog/deleteLog?name=" + row.name + "&id=" + row.id,
			  			function(data) {
			  				if (data.result) {
//			  					alert("删除成功!");
			  					flag = true;
//			  					window.location.reload();
			  					$("#normallog_id").datagrid("reload");
			  					$("#orilog_id").datagrid("reload");
			  					$("#eventlog_id").datagrid("reload");
			  					$.messager.show({
									title:'提示',
									msg:'删除成功!',
									timeout:3000,
									showType:'slide'
								});
			  				} else {
//			  					alert("删除失败!");
			  					$.messager.alert('删除失败!','错误');
			  					flag = false;
			  				}
			  	});
			  	if(true == flag){
//			  		$("#eventlog_id").datagrid("reload",{ });
			  		$('#jquerylogshow_id').tabs('select', '事件日志');
			  	}
			}
		});
  }

  /**
   * 文件查询-依据文件名称进行模糊匹配查询
   */
  function doSearch() {
  	var content = document.getElementById("search").value;
  	//alert(content);
  	$.get("/ProcessMining/eventlog/getLog?name=" + content, function(data) {
  		$('#eventlog_id').datagrid('loadData', {
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
  				link_eventlog : data.rows[i].link_eventlog,
				link_normlog : data.rows[i].link_normlog,
  			};
  			//alert(row_data);
  			$('#eventlog_id').datagrid('appendRow', row_data);
  		}
  	});
  }

  /**
   * 上传文件-弹窗配置然后选择文件上传
   */
  var uploadEventFile_Window;
  $(function() {
  	uploadEventFile_Window = $('#upLoadEventFileWin').window({
  		width : 350,
  		height : 220,
  		modal : true,
  		collapsible : false,
  		minimizable : false,
  		maximizable : false,
  		closable : false
  	});
  });
  function uploadEventFileClick() { //上传文件点击
//      	$('#win').window('open'); // open a window
	 $("#isShareEventLog").filebox('setValue', "");
  	uploadEventFile_Window.window('open');
  }
  
  function submitFormEvent() { //点击提交上传
	  $('#upEvent').form('submit', {
			url:"/ProcessMining/eventlog/uploadLog",
			onSubmit: function(){
			// do some check
			// return false to prevent submit;
			},
			success:function(data){
//				alert("上传成功");
//				window.location.reload();
				uploadEventFile_Window.window('close');
				$("#eventlog_id").datagrid("reload");
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
  
  function clearFormEvent() {
  	$("#isShareEventLog").filebox('setValue', "");
  	uploadEventFile_Window.window('close');
  }
  /**
   * 级联获取上传文件类型和格式
   */
  $(function() {
  	// 下拉框选择控件，下拉框的内容是动态查询数据库信息
  	$('#type1').combobox({
  		onHidePanel : function() {
  			$("#format1").combobox("setValue", ''); //清除
  			var type = $('#type1').combobox('getValue');

  			var data1 = [ {
  				"type" : "type1",
  				"format" : "xes"
  			}, {
  				"type" : "type2",
  				"format" : "log"
  			}, {
  				"type" : "type3",
  				"format" : "mxml"
  			}, {
  				"type" : "type4",
  				"format" : "other"
  			} ];
  			if (type == "原始日志") {
  				$("#format1").combobox("loadData", data1);
  			}

  			var data2 = [ {
  				"type" : "type1",
  				"format" : "xes"
  			}, {
  				"type" : "type2",
  				"format" : "mxml"
  			}, {
  				"type" : "type3",
  				"format" : "other"
  			} ];
  			if (type == "事件日志") {
  				$("#format1").combobox("loadData", data2);
  			}
  		}
  	});
  	$('#format1').combobox({
  		//url:'itemManage!categorytbl', 
  		editable : false, //不可编辑状态
  		cache : false,
  		panelHeight : '150',//自动高度适合
  		valueField : 'type',
  		textField : 'format'
  	});
  });

  /**
   * 文件下载-依据文件ID下载
   * @param id  文件ID
   */
  function downloadEventFile(index) {
  	var row = $('#eventlog_id').datagrid('getData').rows[index];
  	location.href = "/ProcessMining/eventlog/downloadLog?name=" + row.id;
//  	$.get("/ProcessMining/eventlog/downloadLog?name=" + row.id, function(data) {
//  		window.open(data);
//  	});
  }
  