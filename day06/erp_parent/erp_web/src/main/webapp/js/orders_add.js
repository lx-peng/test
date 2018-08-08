//当前编辑的行索引
var existEditIndex=-1;
$(function(){
	
	$("#grid").datagrid({
		singleSelect:true,
		showFooter:true,
		columns:[[
		          {field:'goodsuuid',title:'商品编号',width:100,editor:{type:'numberbox',options:{
		        	  disabled:true
		          }}},
		          {field:'goodsname',title:'商品名称',width:100,editor:{type:"combobox",options:{
		        	  url:'goods_list',
		        	  textField:'name',
		        	  valueField:'name',
		        	  onSelect:function(goods){
		    			  //获取商品编号的编辑器
		    			  var goodsUuidEditor=getEditor('goodsuuid');
		    			  //显示选中的商品编号
		    			  $(goodsUuidEditor.target).val(goods.uuid);
		    			  //获取商品价格编辑器
		    			  var priceEditor=getEditor('price');
		    			  //显示商品的进货价
		    			  $(priceEditor.target).val(goods.inprice);
		    			  //数量编辑器
		    			  var numEditor=getEditor("num");
		    			  //选中数量编辑框
		    			  $(numEditor.target).select();
		    			  //调用绑定事件
		    			  bindGridEvent();
		    			  //计算金额
		    			  cal();
		    			  sum();
		    			 
		    		  }
		          }}},
		          {field:'price',title:'价格',width:100,editor:{type:"numberbox",options:{precision:2}}},
		          {field:'num',title:'数量',width:100,editor:"numberbox"},
		          {field:'money',title:'金额',width:100,editor:{type:"numberbox",options:{
		        	  precision:2,
		        	  disabled:true
		          }}},
		          {field:'--',title:'操作',width:100,formatter:function(value,row,index){
		        	  if(row.num=='合计'){
		        		  return;
		        	  }
		        	  return "<a href='javascript:void(0)' onclick='deleteRow("+index +")'>删除</a>";
		          }},
		          ]],
		          toolbar:[{
		        	  iconCls:'icon-add',
		        	  text:'增加',
		        	  handler:function(){
		        		  if(existEditIndex>-1){//如果当前存在可编辑的行，则关闭他的编辑状态
		        			  $("#grid").datagrid("endEdit",existEditIndex);
		        		  }//在末尾追加行
		        		  $("#grid").datagrid("appendRow",{num:0,money:0});
		        		  //获取最后一行的下标索引
		        		  existEditIndex = $("#grid").datagrid("getRows").length-1;
		        		  //最后一行，即新增的那一行，开启编辑状态
		        		  $("#grid").datagrid("beginEdit",existEditIndex);
		        	  }
		          },{
		        	  iconCls:'icon-save',
		        	  text:'提交',
		        	  handler:function(){
		        		  if(existEditIndex>-1){
		        			  //如果存在编辑行，则关闭编辑行
		        			  $('#grid').datagrid("endEdit",existEditIndex);
		        			  var formdata=$("#orderForm").serializeJSON();
		        			  if(formdata['t.supplieruuid']==""){
		        				  $.messager.alert("提示","请选择供应商","info");
		        				  return;
		        			  }
		        			  //获取商品表格所有行
		        			  var rows=$("#grid").datagrid("getRows");
		        			  //将所有商品明细转成json字符串，以便提交
		        			  formdata.json=JSON.stringify(rows);
		        			  //提交到后台
		        			  $.ajax({
		        				  url:"orders_add",
		        				  data:formdata,
		        				  dataType:'json',
		        				  type:'post',
		        				  success:function(rtn){
		        					  $.messager.alert('提示',rtn.message,'info',function(){
		        						  if(rtn.success){
		        							  //清空供应商
		        							  $("#supplier").combogrid('clear');
		        							  //刷新表格和行脚
		        							  $("grid").datagrid("loadData",{total:0,rows:[],footer:{num:'合计',money:'0'}});
		        						  }
		        					  })
		        				  }
		        			  })
		        			  
		        		  }
		        	  }
		          }],
		  onClickRow:function(rowIndex,rowData){
			  //rowIndex,当前点击的行的索引
			  //rowData,当前点击的行的数据
			  //关闭其他行的可编辑状态
			  $("#grid").datagrid("endEdit",existEditIndex);
			  //设置编辑行的索引 
			  existEditIndex=rowIndex;
			  //开启编辑行
			  $("#grid").datagrid("beginEdit",existEditIndex);
			//调用绑定事件
			  bindGridEvent();
			  
		  }
		          
	});
	//加载行脚
	$("#grid").datagrid('reloadFooter',[{num:'合计',money:'0'}]);
	//初始化供应商列表
	$("#supplier").combogrid({
		url:'supplier_list?t1.type=1',
		panelWidth:750,
		idField:'uuid',
		textField:'name',
		columns:[[
		  		    {field:'uuid',title:'编号',width:100},
		  		    {field:'name',title:'名称',width:100},
		  		    {field:'address',title:'联系地址',width:100},
		  		    {field:'contact',title:'联系人',width:100},
		  		    {field:'tele',title:'联系电话',width:100},
		  		    {field:'email',title:'邮件地址',width:100},
					]],
	});
})
//获取编辑器
function getEditor(field){
	return $("#grid").datagrid("getEditor",{index:existEditIndex,field:field});
}
//计算商品金额
function cal(){
	//取得数量编辑器
	var numEditor=getEditor("num");
	//获得数量值
	var num=$(numEditor.target).val();
	//获取商品价格编辑器
	var priceEditor=getEditor("price");
	var price =$(priceEditor.target).val();
	//计算商品金额，并保留2位
	var money=(price*num).toFixed(2);
	//获取金额编辑器
	var moneyEditor=getEditor("money");
	//给金额赋值
	$(moneyEditor.target).val(money);
	//更新表格中的数据，设置json key里面对应的值
	$("#grid").datagrid("getRows")[existEditIndex].money=money;
}
//绑定表格编辑器的键盘输入事件
function bindGridEvent(){
	//得到价格编辑器
	var priceEditor=getEditor("price");
	//绑定键盘输入事件
	$(priceEditor.target).bind("keyup",function(){
		//调用金额计算方法
		cal();
		sum();
	});
	//得到数量编辑器
	var numEditor=getEditor("num");
	//绑定键盘输入事件
	$(numEditor.target).bind("keyup",function(){
		//调用金额计算方法
		cal();
		sum();
	})
}
//删除行
function deleteRow(rowIndex){
	//关闭编辑
	$("#grid").datagrid("endEdit",existEditIndex);
	//删除行
	$("#grid").datagrid("deleteRow",rowIndex);
	var data=$("#grid").datagrid("getData");
	//重新加载数据
	$("#grid").datagrid("loadData",data);
	sum();
}
//合计所有商品金额
function sum(){
	//获取所有商品记录数
	var rows=$("#grid").datagrid('getRows');
	//保存总金额
	var total=0;
	//循环累加
	$.each(rows,function(i,r){
		total+=parseFloat(r.money);
	});
	$("#grid").datagrid("reloadFooter",[{num:"合计",money:total.toFixed(2)}]);
}
