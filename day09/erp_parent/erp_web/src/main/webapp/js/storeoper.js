$(function(){
	$("#opergrid").datagrid({
		url:'storeoper_listByPage',
		columns:[[
		          {field:'uuid',title:'编号',width:50},
		          {field:'empName',title:'员工',width:100},
		          {field:'opertime',title:'操作日期',width:100,formatter:function(value){
		        	  return new Date(value).Format('yyyy-MM-dd');
		          }},
		          {field:'storeName',title:'仓库',width:100},
		          {field:'goodsName',title:'商品',width:100},
		          {field:'num',title:'数量',width:100},
		          {field:'type',title:'类型',width:100,formatter:function(value){
		        	  switch(value*1){
		        	  case 1:return '入库';
		        	  case 2:return '出库';
		        	  default:return '';
		        	  }
		          }},
        ]],
        singleSelect:true,
        pagination:true,
        rownumbers:true
	});
	$('#btnSearch').bind('click',function(){
		var submitData = $('#searchForm').serializeJSON();
		//提交查询条件，远程重新加载数据
		$('#opergrid').datagrid('load',submitData);
	});
	
	
})