/**
*日期格式化
*/
function formatDate(dateValue){
	return new Date(dateValue).Format('yyyy-MM-dd');
};
/**
*获取订单状态
*/
function getState(value){
	switch(value*1){
	case 0: return '未审核';
	case 1: return '已审核';
	case 2: return '已确认';
	case 3: return '已入库';
	default: return '';
	}
};
//获取明细状态
function getDetailState(value){
	switch(value*1){
	case 0:return "未入库";
	case 1:return "已入库";
	default:return '';
	}
};
$(function(){
	var url="orders_listByPage?t1.type=1";
	//审核
	if(Request['oper']=='doCheck'){
		url+="&t1.state=0";
	}
	//审核
	if(Request['oper']=='doStart'){
		url+="&t1.state=1";
	}
	//入库
	if(Request['oper']=='doInStore'){
		url+="&t1.state=2";
	}
	//入库窗口
	$("#itemDlg").dialog({
		title:'入库',
		height:200,
		width:300,
		modal:true,
		closed:true,
		buttons:[{
			text:'入库',
			iconCls:'icon-save',
			handler:doInStore
		}]
	});
	//加载表格数据
	$('#grid').datagrid({
		url:url,
		columns:[[
		          {field:'uuid',title:'编号',width:100},
		          {field:'createtime',title:'生成日期',width:100,formatter:formatDate},
		          {field:'checktime',title:'审核日期',width:100,formatter:formatDate},
		          {field:'starttime',title:'确认日期',width:100,formatter:formatDate},
		          {field:'endtime',title:'入库或出库日期',width:100,formatter:formatDate},
		          {field:'createrName',title:'下单员',width:100},
		          {field:'checkerName',title:'审核员',width:100},
		          {field:'starterName',title:'采购员',width:100},
		          {field:'enderName',title:'库管员',width:100},
		          {field:'supplierName',title:'供应商',width:100},
		          {field:'totalmoney',title:'合计金额',width:100},
		          {field:'state',title:'状态',width:100},
		          {field:'waybillsn',title:'运单号',width:100},
		          ]],
		singleSelect:true,
		pagination:true,
		fitColumns:true,
		onDblClickRow:function(rowIndex,rowData){
			//显示订单详情内容
			$("#uuid").html(rowData.uuid);
			$("#suppliername").html(rowData.supplierName);
			$("#creater").html(rowData.createrName);
			$("#checker").html(rowData.checkName);
			$('#starter').html(rowData.starterName);
			$('#enderer').html(rowData.enderName);
			$("#createtime").html(new Date(rowData.createtime).Format('yyyy-MM-dd'));
			$("#checktime").html(new Date(rowData.checktime).Format('yyyy-MM-dd'));
			$("#starttime").html(new Date(rowData.starttime).Format('yyyy-MM-dd'));
			$("#endtime").html(new Date(rowData.endtime).Format('yyyy-MM-dd'));
			$("#state").html(getState(rowData.state));
			//打开窗口
			$("#ordersDlg").dialog('open');
			//加载明细数据
			$("#itemgrid").datagrid("loadData",rowData.orderDetails);
			
		}
	});
	//审核按钮
	if(Request['oper']=='doCheck'){
		$("#ordersDlg").dialog({
			toolbar:[
			         {
			        	 text:'审核',
			        	 iconCls:'icon-search',
			        	 handler:doCheck
			         }]
		})
	};
	//确认按钮
	if(Request['oper']=='doStart'){
		$("#ordersDlg").dialog({
			toolbar:[
			         {
			        	 text:'确认',
			        	 iconCls:'icon-search',
			        	 handler:doStart
			         }]
		})
	};
	//明细表格
	$('#itemgrid').datagrid({
		columns:[[
				{field:'uuid',title:'编号',width:100},
				{field:'goodsuuid',title:'商品编号',width:100},
				{field:'goodsname',title:'商品名称',width:100},
				{field:'price',title:'价格',width:100},
				{field:'num',title:'数量',width:100},
				{field:'money',title:'金额',width:100},
				{field:'state',title:'状态',width:100,formatter:getDetailState},
		          ]],
        singleSelect:true,
  		fitColumns:true
	});
	
	//入库，双击打开入库窗口
	if(Request['oper']=='doInStore'){
		$('#itemgrid').datagrid({
			onDblClickRow:function(rowIndex,rowData){
				$('#itemuuid').val(rowData.uuid);
				$('#goodsuuid').html(rowData.goodsuuid);
				$('#goodsname').html(rowData.goodsname);
				$('#goodsnum').html(rowData.num);
				$('#itemDlg').dialog('open');
			}
		})
	}
});
//审核
function doCheck(){
	//询问用户确认是否要进行审核操作
	$.messager.confirm('确认','确认要审核吗',function(yes){
		if(yes){
			//确认后就提交，带上订单的uuid
			$.ajax({
				url:'orders_doCheck?id='+$("#uuid").html(),
				dataType:'json',
				type:'post',
				success:function(rtn){
					$.messager.alert('提示',rtn.message,'info',function(){
						if(rtn.success){
							//关闭订单详情窗口
							$("#ordersDlg").dialog('close');
							//刷新订单列表
							$("#grid").datagrid("reload");
						}
					})
				}
			});
		}
	})
};
//确认
function doStart(){
	//询问用户确认是否要进行确认操作
	$.messager.confirm('确认','确定要确认吗',function(yes){
		if(yes){
			//确认后就提交，带上订单的uuid
			$.ajax({
				url:'orders_doStart?id='+$("#uuid").html(),
				dataType:'json',
				type:'post',
				success:function(rtn){
					$.messager.alert('提示',rtn.message,'info',function(){
						if(rtn.success){
							//关闭订单详情窗口
							$("#ordersDlg").dialog('close');
							//刷新订单列表
							$("#grid").datagrid("reload");
						}
					})
				}
			});
		}
	})
};
//入库
function doInStore(){
	var formdata=$('#itemForm').serializeJSON();
	//询问用户确认是否要进行确认操作
	$.messager.confirm('确认','确定要入库吗',function(yes){
		if(yes){
			//确认后就提交，带上订单的uuid
			$.ajax({
				url:'orderdetail_doInStore',
				dataType:'json',
				data:formdata,
				type:'post',
				success:function(rtn){
					$.messager.alert('提示',rtn.message,'info',function(){
						if(rtn.success){
							//关闭入库窗口
							$("#itemDlg").dialog('close');
							//修改入库后明细的状态
							$("#itemgrid").datagrid("getSelected").state=1;
							//刷新明细列表，更新显示的状态
							var data = $("#itemgrid").datagrid("getData");
							$('#itemgrid').datagrid("loadData".data);
							//判断所有的明细是否都有入库
							var allIn=true;
							$.each(data.rows,function(i,row){
								if(row.state*1==0){
									//只要有一个的状态为0，则表示还有明细没有入库
									allIn = false;
									//跳出循环
									return false;
								}
							});
							//如果所有明细都已经入库
							if(allIn == true){
								//关闭明细窗口
								$("#ordersDlg").dialog('close');
								//刷新订单列表
								$("#grid").datagrid("reload");
							}
						}
					});
				}
			});
		}
	});
};