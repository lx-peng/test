package cn.itcast.erp.biz.impl;


import java.util.Date;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.dao.IOrdersDao;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
/**
 * 订单业务逻辑类
 * @author Administrator
 *
 */
public class OrdersBiz extends BaseBiz<Orders> implements IOrdersBiz {

	private IOrdersDao ordersDao;
	
	public void setOrdersDao(IOrdersDao ordersDao) {
		this.ordersDao = ordersDao;
		super.setBaseDao(this.ordersDao);
	}
	//新增订单
	public void add(Orders orders){
		//新增的采购订单都是未审核
		orders.setState(Orders.STATE_CREATE);
		//设置订单类型未采购
		orders.setType(Orders.TYPE_IN);
		//设置订单创建时间未当前时间
		orders.setCreatetime(new Date());
		//计算总金额
		double total=0;
		for (Orderdetail detail : orders.getOrderDetails()) {
			total+=detail.getMoney();
			//设置明细的状态为未入库
			detail.setState(Orderdetail.STATE_NOT_IN);
			//设置明细对应的订单，原因，orders采用级联更新，且外键的维护交给明细来维护
			detail.setOrders(orders);
		}
		//设置总金额
		orders.setTotalmoney(total);
		//保存订单
		ordersDao.add(orders);
	}
}
