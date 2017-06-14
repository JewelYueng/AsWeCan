///**
// * 上传文件-弹窗配置然后选择文件上传
// */
//var uploadOriFile_Window;
//$(function() {
//	uploadOriFile_Window = $('#upLoadFileWin').window({
//		width : 300,
//		height : 220,
//		modal : true,
//		collapsible : false,
//		minimizable : false,
//		maximizable : false,
//		closable : false
//	});
//});
//function uploadFileClick() { //上传文件点击
////    	$('#win').window('open'); // open a window
//	uploadOriFile_Window.window('open');
//}
//function submitForm() { //点击提交上传
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
//	//    	alert($('#cb').combobox('getText'));  //
//	//    	alert($("input[type='radio']:checked").val()); //
//	$.post("/ProcessMining/rawlog/uploadLog?path=" + path + "&type=" + type
//			+ "&isShare=" + isShare + "&format=" + format, function(data) {
//		if (data.result) {
//			alert("上传成功!");
//			window.location.reload();
//		} else {
//			alert("上传失败!");
//		}
//	});
//}
//function clearForm() {
//	$('#type').combobox('select', "");
//	$('#format').combobox('select', "");
//	$("#txt_head").filebox('setValue', "");
//	uploadOriFile_Window.window('close');
//}
//
///**
// * 级联获取上传文件类型和格式
// */
//$(function() {
//	// 下拉框选择控件，下拉框的内容是动态查询数据库信息
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
//
///**
// * 文件下载-依据文件ID下载
// * @param id  文件ID
// */
//function downloadOriFile(index) {
//	var row = $('#orilog_id').datagrid('getData').rows[index];
//	$.get("/ProcessMining/rawlog/downloadLog?name=" + row.id, function(data) {
//		window.open(data);
//	});
//}