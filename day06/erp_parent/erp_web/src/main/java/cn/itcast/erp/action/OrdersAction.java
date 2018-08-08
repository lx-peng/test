package cn.itcast.erp.action;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.ERPException;
import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;

/**
 * 订单Action 
 * @author Administrator
 *
 */
public class OrdersAction extends BaseAction<Orders> {

	private IOrdersBiz ordersBiz;
	private String json;
	
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public void setOrdersBiz(IOrdersBiz ordersBiz) {
		this.ordersBiz = ordersBiz;
		super.setBaseBiz(this.ordersBiz);
	}
	//采购申请
	public void add(){
		Emp loginUser = getLoginUser();
		//校验用户是否登录
		if(null==loginUser){
			ajaxReturn(false,"亲，您还没有登录");
			return;
		}
		try {
			//获取提价的订单
			Orders orders=getT();
			//设置订单的创建人
			orders.setCreater(loginUser.getUuid());
			//获取订单明细
			List<Orderdetail> list=JSON.parseArray(json,Orderdetail.class);
			//设置订单的明细
			orders.setOrderDetails(list);
			//保存订单
			ordersBiz.add(orders);
			//添加订单成功
			ajaxReturn(true,"添加订单成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false,"添加订单失败");
		}
	}
	//采购订单审核
	public void doCheck(){
		Emp loginUser=getLoginUser();
		if(null==loginUser){
			ajaxReturn(false,"亲！您还没有登录");
			return;
		}
		try {
			ordersBiz.doCheck(getId(), loginUser.getUuid());
			ajaxReturn(true,"审核成功");
		} catch (ERPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false,e.getMessage());
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false,"审核失败");
		}
	}
	//采购订单确认
	public void doStart(){
		Emp loginUser=getLoginUser();
		if(null==loginUser){
			ajaxReturn(false,"亲！您还没有登录");
			return;
		}
		try {
			ordersBiz.doStart(getId(), loginUser.getUuid());
			ajaxReturn(true,"确认成功");
		} catch (ERPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false,e.getMessage());
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false,"确认失败");
		}
		}
}
