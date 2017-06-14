/**
 * 规范化日志文件页面datagrid初始化 JS
 */
//$(function() {
//    // 先通过ajax获取数据，然后再传给datagrid
//    	$.ajax({
//    		method : 'GET',
//    		url : '/ProcessMining/normlog/getLog',
//    		async : true,
//    		dataType : 'json',
//    		success : function(data) {
//    			//alert(data.total);
//    			for(var i=0;i<data.total;i++){
//    				//alert(data.rows[file].orifileName);
//    				var row_data = {
//    						id : data.rows[i].id,
//    						name : data.rows[i].name,
//    						format : data.rows[i].format,
//    						date : data.rows[i].date,
//    						user : data.rows[i].user.username,
//    						link_rawlog : data.rows[i].link_rawlog,
//    						link_eventlog : data.rows[i].link_eventlog,
//    					};
//    				//alert(row_data);
//    				$('#normallog_id').datagrid('appendRow', row_data);
//    			}
//    		},
//    		error : function() {
//    			alert('error');
//    		}
//    	});
//    });
/**
 * 自动隐藏列
 */
$(function() {
	$('#normallog_id').datagrid('hideColumn','rawLogLink');
	$('#normallog_id').datagrid('hideColumn','eventLogLink');
});

/**
 * 格式化操作列
 * @param value  当前列对应字段值
 * @param row  当前的行记录数据
 * @param index  当前的行下标
 */
function rowformaterNormal(value, row, index) {
	return '<a href="javacript:void(0);" onclick="delNormalFile(' + index + ');">删除</a>'
			+ "   " + '<a href="javacript:void(0);" onclick="downloadNormalFile(' + index
			+ ');return false;">下载</a>' + "   " + '<a href="javacript:void(0);" onclick="transformNormalToEventFile(' + index
			+ ');">转化事件日志</a>';
}

/**
 * 规范化日志转化为事件日志
 * @param index
 */
function transformNormalToEventFile(index){
	var row = $('#normallog_id').datagrid('getData').rows[index];
	//打开遮罩效果
	MaskUtil.mask();//打开遮罩效果
	$.post("/ProcessMining/transToEvent/translate?name=" + row.name + "&id="
			+ row.id ,
			function(data) {
		        MaskUtil.unmask();
				// alert("ok");
				if (data.result) {
//					alert("转化成功");
					$.messager.show({
						title:'提示',
						msg:'事件日志转化成功!',
						timeout:3000,
						showType:'slide'
					});
					$("#normallog_id").datagrid("reload");
  					$("#eventlog_id").datagrid("reload");
				} else {
//					alert("转化失败");
					$.messager.alert(data.msg,'错误');
				}
			});
}

/**
 * 格式化操作列(规范化日志关联的原始日志)
 * @param value  当前列对应字段值
 * @param row  当前的行记录数据
 * @param index  当前的行下标
 */
function normalLogformater1(value,row,index){
	if(row.rawLogLinkName == "" || row.rawLogLinkName == null || row.rawLogLinkName == "undefined"){
		return '<a href="javacript:;" onclick="normalLogLink1('+index+');">'+""+'</a>';
	}else{
		return '<a href="javacript:;" onclick="normalLogLink1('+index+');">'+row.rawLogLinkName+'</a>';	
	}
}
function normalLogLink1(index){
    var row = $('#normallog_id').datagrid('getData').rows[index];
	var oriLinkID = row.rawLogLink;
	$('#jquerylogshow_id').tabs('select', '原始日志');;
	var rows = $('#orilog_id').datagrid('getData').rows;
	var i;
	for(i=0; i<rows.length; i++){
		if(rows[i].id == oriLinkID){
			break;
		}
	}
	var myIndex = $('#orilog_id').datagrid('getRowIndex',rows[i]);
	$('#orilog_id').datagrid('selectRow', myIndex);
}

  /**
   * 格式化操作列(规范化日志关联的事件日志)
   * @param value  当前列对应字段值
   * @param row  当前的行记录数据
   * @param index  当前的行下标
   */
  function normalLogformater2(value,row,index){
	  if(row.eventLogLinkName == "" || row.eventLogLinkName == null ||row.eventLogLinkName == "undefined"){
		  return '<a href="javacript:;" onclick="normalLogLink2('+index+');">'+""+'</a>';
	  }else{
		  return '<a href="javacript:;" onclick="normalLogLink2('+index+');">'+row.eventLogLinkName+'</a>';  
	  }
  }
  function normalLogLink2(index){
	    var row = $('#normallog_id').datagrid('getData').rows[index];
		var eventLinkID = row.eventLogLink;
		$('#jquerylogshow_id').tabs('select', '事件日志');
		var rows = $('#eventlog_id').datagrid('getData').rows;
		var i;
		for(i=0; i<rows.length; i++){
			if(rows[i].id == eventLinkID){
				break;
			}
		}
		var myIndex = $('#eventlog_id').datagrid('getRowIndex',rows[i]);
		$('#eventlog_id').datagrid('selectRow', myIndex);
  }
  
  /**
   * 文件删除-依据文件ID删除
   * @param id  文件ID
   */
  function delNormalFile(index) { 
  	//		alert(index);
	  $.messager.defaults = { ok: "是", cancel: "否" };
		$.messager.confirm('确认', '请确认是否删除，删除后数据不可恢复?', function(r){
			if (r){
				var row = $('#normallog_id').datagrid('getData').rows[index];
			  	$.post("/ProcessMining/normlog/deleteLog?name=" + row.name + "&id=" + row.id,
			  			function(data) {
			  				if (data.result) {
//			  					alert("删除成功!");
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
			  				}
			  			});
			}
		});
  }

  /**
   * 文件查询-依据文件名称进行模糊匹配查询
   */
  function doSearchNormalFile() {
  	var content = document.getElementById("searchNormalFile").value;
  	//alert(content);
  	$.get("/ProcessMining/normlog/getLog?name=" + content, function(
  			data) {
  		//alert(data.total);
  		//alert(data.rows[0].orifileName);
  		//    		if("success".equals(data.success)){
  		//alert("²éÕÒ³É¹¦!");
  		$('#normallog_id').datagrid('loadData', {
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
					link_rawlog : data.rows[i].link_rawlog,
					link_eventlog : data.rows[i].link_eventlog,
  			};
  			//alert(row_data);
  			$('#normallog_id').datagrid('appendRow', row_data);
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
  var uploadNormalFile_Window;
  $(function() {
  	uploadNormalFile_Window = $('#upLoadNormalFileWin').window({
  		width : 350,
  		height : 220,
  		modal : true,
  		collapsible : false,
  		minimizable : false,
  		maximizable : false,
  		closable : false
  	});
  });
  function uploadNormalFileClick() { //上传文件点击
	 $("#isShareNormalLog").filebox('setValue', "");
  	uploadNormalFile_Window.window('open');
  }
  function submitFormNormal() { //点击提交上传
	  $('#upNormal').form('submit', {
			url:"/ProcessMining/normlog/uploadLog",
			onSubmit: function(){
			// do some check
			// return false to prevent submit;
			},
			success:function(data){
//				alert("上传成功");
//				window.location.reload();
				$("#normallog_id").datagrid("reload");
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
  
  function clearFormNormal() {
  	$("#isShareNormalLog").filebox('setValue', "");
  	uploadNormalFile_Window.window('close');
  }
  /**
   * 级联获取上传文件类型和格式
   */
  $(function() {
  	// 下拉框选择控件，下拉框的内容是动态查询数据库信息
  	$('#type2').combobox({
  		onHidePanel : function() {
  			$("#format2").combobox("setValue", ''); //清除
  			var type = $('#type2').combobox('getValue');

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
  				$("#format2").combobox("loadData", data1);
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
  			if (type == "规范化日志") {
  				$("#format2").combobox("loadData", data2);
  			}
  			var data3 = [ {
  				"type" : "type1",
  				"format" : "txt"
  			}, {
  				"type" : "type2",
  				"format" : "log"
  			}, {
  				"type" : "type3",
  				"format" : "other"
  			} ];
  			if (type == "规范化日志") {
  				$("#format2").combobox("loadData", data3);
  			}
  		}
  	});
  	$('#format2').combobox({
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
  function downloadNormalFile(index) {
  	var row = $('#normallog_id').datagrid('getData').rows[index];
  	location.href = "/ProcessMining/normlog/downloadLog?name=" + row.id; 
//  	$.get("/ProcessMining/normlog/downloadLog?name=" + row.id, function(data) {
//  		window.open(data);
//  	});
  }
  function normalLogformaterForUser(value,row,index){
		var row = $('#normallog_id').datagrid('getData').rows[index];
		if(row == null || row == "undefined" || row == ""){
			return;
		}
		return row.user.username;
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
  
  
