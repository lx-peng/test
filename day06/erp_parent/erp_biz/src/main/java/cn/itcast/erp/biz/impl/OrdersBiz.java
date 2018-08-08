package cn.itcast.erp.biz.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.erp.ERPException;
import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IOrdersDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
/**
 * 订单业务逻辑类
 * @author Administrator
 *
 */
public class OrdersBiz extends BaseBiz<Orders> implements IOrdersBiz {
	private IEmpDao empDao;
	private ISupplierDao supplierDao;
	private IOrdersDao ordersDao;
	
	public void setOrdersDao(IOrdersDao ordersDao) {
		this.ordersDao = ordersDao;
		super.setBaseDao(this.ordersDao);
	}
	
	public IEmpDao getEmpDao() {
		return empDao;
	}

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}

	public ISupplierDao getSupplierDao() {
		return supplierDao;
	}

	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
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
	//重写listbypage方法
	public List<Orders> getListByPage(Orders t1,Orders t2,Object param,int firstResult,int maxResults){
		//获取分页结果
		List<Orders> orderList = super.getListByPage(t1, t2, param, firstResult, maxResults);
		//缓存员工名称，key为员工的编号，value为员工的姓名
		Map<Long,String> empNameMap=new HashMap<Long,String>();
		for (Orders o : orderList) {
			//设置下单员名称
			o.setCreaterName(getEmpName(o.getCreater(),empNameMap));
			o.setCheckName(getEmpName(o.getChecker(),empNameMap));
			o.setStarterName(getEmpName(o.getStarter(),empNameMap));
			o.setEnderName(getEmpName(o.getEnder(),empNameMap));
			o.setSupplierName(getEmpName(o.getSupplieruuid(),empNameMap));
		}
		return super.getListByPage(t1, t2, param, firstResult, maxResults);
	}
	//根据员工编号获取员工名称
	private String getEmpName(Long uuid,Map<Long,String> empNameMap){
		//如果员工编号为空，则返回空
		if(null==uuid){
			return null;
		}
		//通过员工id,尝试从缓存中获取员工名称
		String empName=empNameMap.get(uuid);
		if(null==empName){
			//如果在缓存中没有找到，则调用dao查询后，获取到员工名称
			empName=empDao.get(uuid).getName();
			//按员工编号保存员工名称到缓存中
			empNameMap.put(uuid, empName);
		}
		return empName;
	}
	//采购订单审核
	@Override
	public void doCheck(Long uuid, Long empuuid) {
		//获取订单信息
		Orders orders = ordersDao.get(uuid);
		//检查订单的状态是否为未审核
		if(!Orders.STATE_CREATE.equals(orders.getState())){
			throw new ERPException("亲！该订单已经审核过了");
		}
		//更新审核员
		orders.setChecker(empuuid);
		//更新审核时间
		orders.setChecktime(new Date());
		//更新订单状态为已审核
		orders.setState(Orders.STATE_CHECK);
	}
	//采购订单确认
	@Override
	public void doStart(Long uuid, Long empuuid) {
		//获取订单信息
		Orders orders = ordersDao.get(uuid);
		//检查订单的状态是否为未审核
		if(!Orders.STATE_CHECK.equals(orders.getState())){
			throw new ERPException("亲！该订单已经确认过了");
		}
		//更新确认员
		orders.setStarter(empuuid);
		//更新确认时间
		orders.setStarttime(new Date());
		//更新订单状态为已审核
		orders.setState(Orders.STATE_START);
	}
}
