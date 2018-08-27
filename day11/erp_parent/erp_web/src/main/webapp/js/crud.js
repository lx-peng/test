//提交的方法名称
var method = "";
var listParam = "";
//保存附带条件
var saveParam="";
$(function(){
	if(Request['type']==1){
		listParam='?t1.type=1';
		saveParam='?t.type=1';
	}
	if(Request['type']==2){
		listParam='?t1.type=2';
		saveParam='?t.type=2';
	}
	
	//加载表格数据
	$('#grid').datagrid({
		url:name + '_listByPage'+listParam,
		columns:columns,
		singleSelect: true,
		pagination: true,
		toolbar: [{
			text: '新增',
			iconCls: 'icon-add',
			handler: function(){
				//设置保存按钮提交的方法为add
				method = "add";
				//关闭编辑窗口
				$('#editDlg').dialog('open');
			}
		},'-',{
			text:'导出',
			iconCls:'icon-excel',
			handler:function(){
				var submitData = $('#searchForm').serializeJSON();
				$.download(name+'_export'+listParam,submitData);
			}
		},'-',{
			text:'导入',
			iconCls:'icon-save',
			handler:function(){
				var importDlg = document.getElementById('importDlg');
				//判断是否存在导入窗口
				if(importDlg){
					$('#importDlg').dialog('open');
				}
			}
		}]
	});
	var importDlg = document.getElementById('importDlg');
	//判断是否存在导入窗口
	if(importDlg){
		$('#importDlg').dialog({
			title:'导入数据',
			height:106,
			width:330,
			closed:true,
			buttons:[
			         {
			        	 text:'导入',
			        	 handler:function(){
			        		 //异步上传文件
			        		 $.ajax({
			        			 url:name+"_doImport",
			        			 type:'post',
			        			 data:new FormData($('#importForm')[0]),
			        			 dataType:'json',
			        			 processData:false,
			        			 contentType:false,
			        			 success:function(rtn){
			        				 $.messager.alert('提示信息',rtn.message,'info',function(){
			        					 if(rtn.success){
			        						 //关闭上的窗口
			        						 $('#importDlg').dialog('close');
			        						 //刷新表格内容
			        						 $('#grid').datagrid('reload');
			        					 }
			        				 })
			        			 }
			        		 })
			        	 }
			         }
			         ]
		})
	}
	//点击查询按钮
	$('#btnSearch').bind('click',function(){
		//把表单数据转换成json对象
		var formData = $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',formData);
	});
	
	var height = 200;//默认高度
	if(typeof(_height) != 'undefined'){
		//定义的高度
		height = _height;
	}
	
	//初始化编辑窗口
	$('#editDlg').dialog({
		title: '编辑',//窗口标题
		width: 300,//窗口宽度
		height: height,//窗口高度
		closed: true,//窗口是是否为关闭状态, true：表示关闭
		modal: true,//模式窗口
		buttons:[{
			text:'保存',
			iconCls: 'icon-save',
			handler:function(){
				//验证, 当所有字段都有效的时候返回true, 如果有一个验证不通过，则为false
				if($('#editForm').form('validate') == false){
					return;
				}
				//用记输入的部门信息
				var submitData= $('#editForm').serializeJSON();
				$.ajax({
					url: name + '_' + method+saveParam,
					data: submitData,
					dataType: 'json',
					type: 'post',
					success:function(rtn){
						//{success:true, message: 操作失败}
						$.messager.alert('提示',rtn.message, 'info',function(){
							if(rtn.success){
								//关闭弹出的窗口
								$('#editDlg').dialog('close');
								//刷新表格
								$('#grid').datagrid('reload');
							}
						});
					}
				});
			}
		},{
			text:'关闭',
			iconCls:'icon-cancel',
			handler:function(){
				//关闭弹出的窗口
				$('#editDlg').dialog('close');
			}
		}]
	});

});


/**
 * 删除
 */
function del(uuid){
	$.messager.confirm("确认","确认要删除吗？",function(yes){
		if(yes){
			$.ajax({
				url: name + '_delete?id=' + uuid,
				dataType: 'json',
				type: 'post',
				success:function(rtn){
					$.messager.alert("提示",rtn.message,'info',function(){
						//刷新表格数据
						$('#grid').datagrid('reload');
					});
				}
			});
		}
	});
}

/**
 * 修改
 */
function edit(uuid){
	//弹出窗口
	$('#editDlg').dialog('open');

	//清空表单内容
	$('#editForm').form('clear');

	//设置保存按钮提交的方法为update
	method = "update";

	//加载数据
	$('#editForm').form('load',name + '_get?id=' + uuid);
	//$('#editForm').form('load',{"t.address":"建材城西路中腾商务大厦","t.birthday":"1949-10-01","t.dep.name":"管理员组","t.dep.tele":"000000","t.dep.uuid":1,"t.email":"admin@itcast.cn","t.gender":1,"t.name":"超级管理员","t.tele":"12345678","t.username":"admin","t.uuid":1});
}