function submitForm(){
	$('#pluginFileUpload').form('submit', {
		url:"/ProcessMining/mining/fileUpload",
		onSubmit: function(){
		// do some check
		// return false to prevent submit;
		},
		success:function(data){
			alert("上传成功");
			$('#processmining_id').datagrid('load');
		}
		
	});
}


//function miningformater(value,row,index){
//	return '<a href="javacript:;" onclick="eventLogSumarise('+index+');">'+row.name+'</a>';
//  }

function processmining(index){
	alert(index);
}

var hashMap = {   
	    Set : function(key,value){this[key] = value},   
	    Get : function(key){return this[key]},   
	    Contains : function(key){return this.Get(key) == null?false:true},   
	    Remove : function(key){delete this[key]}   
	} 


