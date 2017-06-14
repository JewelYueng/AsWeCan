/**
 * 原始日志文件 JS
 */
    
    function doSearch(){
    	var value=document.getElementById("search").value;
    	$.get("/test_ssh/OriFile/serchOriFile?oriFileName=" + value, function(data){
			if("success" == data.result){
				alert("删除成功!");
				window.location.reload();
			}else{
				alert("删除失败!");
			}
		});
    }
    
    function cancelImportClick(){
    	document.getElementById("txt_head").value = "";
    }
