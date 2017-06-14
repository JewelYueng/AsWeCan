//菜单导航控制
function showcontent(id){
	if(1 == id){
		$('#jquerylogshow_id').tabs('select','原始日志');
	}else if(2 == id){
		$('#jquerylogshow_id').tabs('select','规范化日志'); 
	}else if(3 == id){
//		$('#jquerylogshow_id').tabs('select','事件日志');
		if ($('#jquerylogshow_id').tabs('exists', '事件日志')){
			$('#jquerylogshow_id').tabs('select', '事件日志');
		} else {
			$('#jquerylogshow_id').tabs('add',{
				'id': 'eventlog_id',
				title:'事件日志',
				fit: true,
				content:' ',
				closable:true
			});
		}
	}

}