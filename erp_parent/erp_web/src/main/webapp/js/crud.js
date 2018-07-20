var method="";
$(function(){
	$("#grid").datagrid({
		url:name+'_listByPage.action',
		columns:columns,
		singleSelect:true,
		pagination:true,
		toolbar:[{
			iconCls:'icon-add',
			text:'增加',
			handler:function(){
				method="add";
				$("#editForm").form('clear');
				$("#editWindow").window('open');
			}
		}]
	});
});
$("#btnSearch").bind('click',function(){
	//form的数据转json对象
	var formdata=$("#searchForm").serializeJSON();
	//json对象转json字符串
	$("#grid").datagrid('load',formdata);
})
function test(){
	var data=$("#grid").datagrid("getData");
	alert(JSON.stringify(data));
}
//保存
$("#btnSave").bind('click',function(){
	//表单editForm的数据转json对象
	var formdata=$("#editForm").serializeJSON();
	$.ajax({
		url:name+'_'+method+'.action',
		data:formdata,
		dataType:'json',
		type:'post',
		success:function(value){
			if(value.success){
				$('#editWindow').window('close');
				$('#grid').datagrid('reload');
			}
			$.messager.alert('提示',value.message);
		}
	})
})
//删除
function dele(id){
	$.messager.confirm("提示","确定要删除吗",function(value){
		$.ajax({
			url:name+'_delete.action?id='+id,
			dataType:'json',
			type:'post',
			success:function(value){
				$.messager.alert('提示',value.message);
				if(value.success){
					$('#grid').datagrid('reload');
				}
			}
		})
	})
	
}
//修改
function edit(id){
	$('#editWindow').window('open');
	$('#editForm').form('clear');
	$('#editForm').form('load',name+'_get.action?id='+id);
	method="update";
}