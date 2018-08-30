$(function(){
	//列表
	$('#grid').datagrid({
		url:'storedetail_listByPage',
		columns:[[
		          {field:'uuid',title:'编号',width:100},
		          {field:'storeName',title:'仓库',width:100},
		          {field:'goodsName',title:'商品',width:100},
		          {field:'num',title:'数量',width:100},
		          ]],
		 singleSelect:true,
		 pagination:true,
		 rownumbers:true,
		 onDblClickRow:function(rowIndex,rowData){
			 $('#goodsuuid').html(rowData.goodsName);
			 $('#storeuuid').html(rowData.storeName);
			 var goodsuuid=rowData.goodsuuid;
			 var storeuuid=rowData.storeuuid;
			 var data={"t1.goodsuuid":goodsuuid,"t1.storeuuid":storeuuid};
			 $('#editDlg').dialog('open');
			//加载明细数据
			 $("#opergrid").datagrid("load",data);
		 }
	});
	
	$('#btnSearch').bind('click',function(){
		var data=$('#searchForm').serializeJSON();
		$.ajax({
			url:'storedetail_listByPage',
			type:'post',
			dataType:'json',
			data:data,
			success:function(btn){
				var data = btn.rows;
				$('#grid').datagrid("loadData",data);
				
			}
		});
	})
});
