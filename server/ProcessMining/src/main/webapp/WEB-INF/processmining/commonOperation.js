///**
// * 文件查询-依据文件名称进行模糊匹配查询
// */
//function doSearch() {
//	var content = document.getElementById("search").value;
//	//alert(content);
//	$.get("/ProcessMining/rawlog/getLog?name=" + content, function(
//			data) {
//		//alert(data.total);
//		//alert(data.rows[0].orifileName);
//		//    		if("success".equals(data.success)){
//		//alert("²éÕÒ³É¹¦!");
//		$('#orilog_id').datagrid('loadData', {
//			total : 0,
//			rows : []
//		});
//		for (var i = 0; i < data.total; i++) {
//			//alert(data.rows[file].orifileName);
//			var row_data = {
//				id : data.rows[i].id,
//				name : data.rows[i].name,
//				format : data.rows[i].format,
//				date : data.rows[i].date,
//				user : data.rows[i].user.username,
//				link : data.rows[i].link,
//			};
//			//alert(row_data);
//			$('#orilog_id').datagrid('appendRow', row_data);
//		}
//		//window.location.reload();
//		//			}else{
//		//				alert("²éÕÒÊ§°Ü!");
//		//			}
//	});
//}
function getImgURL(node) {
	var imgURL = "";
	try {
		var file = null;
		if (node.files && node.files[0]) {
			file = node.files[0];
		} else if (node.files && node.files.item(0)) {
			file = node.files.item(0);
		}
		//Firefox 因安全性问题已无法直接通过input[file].value 获取完整的文件路径
		try {
			//Firefox7.0
			imgURL = file.getAsDataURL();
			//alert("//Firefox7.0"+imgRUL);
		} catch (e) {
			//Firefox8.0以上
			imgURL = window.URL.createObjectURL(file);
			//alert("//Firefox8.0以上"+imgRUL);
		}
	} catch (e) { //这里不知道怎么处理了，如果是遨游的话会报这个异常
		//支持html5的浏览器,比如高版本的firefox、chrome、ie10
		if (node.files && node.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				imgURL = e.target.result;
			};
			reader.readAsDataURL(node.files[0]);
		}
	}
	return imgURL;
}

///**
// * 文件删除-依据文件ID删除
// * @param id  文件ID
// */
//function delOriFile(index) {
//	//		alert(index);
//	var row = $('#orilog_id').datagrid('getData').rows[index];
//	$.post("/ProcessMining/rawlog/deleteLog?name=" + row.name + "&id=" + row.id,
//			function(data) {
//				if (data.result) {
//					alert("删除成功!");
//					window.location.reload();
//				} else {
//					alert("删除失败!");
//				}
//			});
//}

///**
// * 格式化操作列
// * @param value  当前列对应字段值
// * @param row  当前的行记录数据
// * @param index  当前的行下标
// */
//function rowformater(value, row, index) {
//	return '<a href="javacript:;" onclick="delOriFile(' + index + ');">删除</a>'
//			+ "  " + '<a href="javacript:;" onclick="downloadOriFile(' + index
//			+ ');">下载</a>';
//}