package cn.itcast.erp.biz.impl;
import java.util.Calendar;
import java.util.List;

import com.redsum.bos.ws.impl.IWaybillWs;

import cn.itcast.erp.ERPException;
import cn.itcast.erp.biz.IOrderdetailBiz;
import cn.itcast.erp.dao.IOrderdetailDao;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.entity.Storeoper;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.exception.ErpException;
/**
 * 订单明细业务逻辑类
 * @author Administrator
 *
 */
public class OrderdetailBiz extends BaseBiz<Orderdetail> implements IOrderdetailBiz {

	private IOrderdetailDao orderdetailDao;
	
	//商品仓库库存dao
	private IStoredetailDao storedetailDao;
	//商品仓库库存变更记录dao
	private IStoreoperDao storeoperDao;
	//物流系统调用
	private IWaybillWs waybillWs;
	//供应商及客户
	private ISupplierDao supplierDao;
	

	public IStoreoperDao getStoreoperDao() {
		return storeoperDao;
	}

	public void setStoreoperDao(IStoreoperDao storeoperDao) {
		this.storeoperDao = storeoperDao;
	}

	public void setOrderdetailDao(IOrderdetailDao orderdetailDao) {
		this.orderdetailDao = orderdetailDao;
		super.setBaseDao(this.orderdetailDao);
	}

	public IStoredetailDao getStoredetailDao() {
		return storedetailDao;
	}

	public void setStoredetailDao(IStoredetailDao storedetailDao) {
		this.storedetailDao = storedetailDao;
	}

	public IWaybillWs getWaybillWs() {
		return waybillWs;
	}

	public void setWaybillWs(IWaybillWs waybillWs) {
		this.waybillWs = waybillWs;
	}

	public ISupplierDao getSupplierDao() {
		return supplierDao;
	}

	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}

	/**
	 * 采购入库
	 */
	@Override
	public void doInStore(Long uuid, Long storeUuid, Long empUuid) {
		// 第一步：更新的商品明细
		Orderdetail orderDetail=this.orderdetailDao.get(uuid);
		//检查是否已经入库了，不能重复入库，只有未入库的明细才能进行入库操作
		if(!Orderdetail.STATE_NOT_IN.equals(orderDetail.getState())){
			throw new ErpException("改明细已经入库了");
		}
		//状态为已入库
		orderDetail.setState(Orderdetail.STATE_IN);
		//3.入库操作员
		orderDetail.setEnder(empUuid);
		//4.入到哪个仓库
		orderDetail.setStoreuuid(storeUuid);
		//5.入库时间
		orderDetail.setEndtime(Calendar.getInstance().getTime());
		//第二步：更新商品仓库库存
		//根据商品编辑和仓库编号 查询是否存在库存记录
		//构建查询条件
		Storedetail storeDetail = new Storedetail();
		storeDetail.setStoreuuid(storeUuid);
		storeDetail.setGoodsuuid(orderDetail.getGoodsuuid());
		//storeDetail.setNum(orderDetail.getNum());
		List<Storedetail> storeList = storedetailDao.getList(storeDetail,null,null);
		//2.判断是否存在库存信息
		if(null!=storeList && storeList.size()>0){
			//如果存在，则更新数量
			storeList.get(0).setNum(storeList.get(0).getNum()+orderDetail.getNum());
		}else{
			//如果不存在，则增加库存信息
			storedetailDao.add(storeDetail);
		}
		//第三步：增加商品仓库库存更新记录
		Storeoper operlog = new Storeoper();
		//设置操作人
		operlog.setEmpuuid(empUuid);
		//2.入库哪个商品
		operlog.setGoodsuuid(orderDetail.getGoodsuuid());
		//3.入库的数量
		operlog.setNum(orderDetail.getNum());
		//4.入库时间
		operlog.setOpertime(orderDetail.getEndtime());
		//5.入在哪个仓库
		operlog.setStoreuuid(storeUuid);
		//6.操作类型为入库
		operlog.setType(Storeoper.TYPE_IN);
		storeoperDao.add(operlog);
		//第四步：是否需要更新订单状态的判断
		//1.获取明细对应的订单信息
		Orders orders = orderDetail.getOrders();
		//2.统计改订单所有state=0的明细个数，看是否还存在，没有入库的明细
		//构建查询条件
		Orderdetail countParam = new Orderdetail();
		countParam.setState(Orderdetail.STATE_NOT_IN);
		countParam.setOrders(orders);
		Long count = orderdetailDao.getCount(countParam,null,null);
		//3.count=0表示，所有的明细都入库了，这时要更新订单的状态，入库完成时间，入库的操作员
		if(count==0){
			//更新订单状态
			orders.setState(Orders.STATE_END);
			//设置操作人
			orders.setEnder(empUuid);
			//设置操作时间
			orders.setEndtime(orderDetail.getEndtime());
		}
	}
	/**
	 * 采购出库
	 */
	@Override
	public void doOutStore(Long uuid, Long storeUuid, Long empUuid) {
		// 第一步：更新的商品明细
		Orderdetail orderDetail=this.orderdetailDao.get(uuid);
		//检查是否已经出库了，不能重复出库，只有未出库的明细才能进行出库操作
		if(!Orderdetail.STATE_NOT_OUT.equals(orderDetail.getState())){
			throw new ErpException("改明细已经出库了");
		}
		//状态为已出库
		orderDetail.setState(Orderdetail.STATE_OUT);
		//3.出库操作员
		orderDetail.setEnder(empUuid);
		//4.从哪个仓库出库
		orderDetail.setStoreuuid(storeUuid);
		//5.出库时间
		orderDetail.setEndtime(Calendar.getInstance().getTime());
		//第二步：查询仓库库存
		//根据商品编辑和仓库编号 查询是否存在库存记录
		//构建查询条件
		Storedetail storeDetail = new Storedetail();
		storeDetail.setStoreuuid(storeUuid);
		storeDetail.setGoodsuuid(orderDetail.getGoodsuuid());
		//storeDetail.setNum(orderDetail.getNum());
		List<Storedetail> storeList = storedetailDao.getList(storeDetail,null,null);
		//2.判断库存是否足够
		Long num=-1L;
		if(null!=storeList && storeList.size()>0){
			num=storeList.get(0).getNum().longValue()-orderDetail.getNum().longValue();
		}
		if(num>0){
			//库存充足，则更新库存数量
			storeDetail.setNum(num);
		}else{
			//库存不足，则提示用户
			throw new ERPException("库存不足！");
		}
		//第三步：增加商品仓库库存更新记录
		Storeoper operlog = new Storeoper();
		//设置操作人
		operlog.setEmpuuid(empUuid);
		//2.出库哪个商品
		operlog.setGoodsuuid(orderDetail.getGoodsuuid());
		//3.出库的数量
		operlog.setNum(orderDetail.getNum());
		//4.出库时间
		operlog.setOpertime(orderDetail.getEndtime());
		//5.出在哪个仓库
		operlog.setStoreuuid(storeUuid);
		//6.操作类型为出库
		operlog.setType(Storeoper.TYPE_OUT);
		storeoperDao.add(operlog);
		//第四步：是否需要更新订单状态的判断
		//1.获取明细对应的订单信息
		Orders orders = orderDetail.getOrders();
		//2.统计改订单所有state=0的明细个数，看是否还存在，没有出库的明细
		//构建查询条件
		Orderdetail countParam = new Orderdetail();
		countParam.setState(Orderdetail.STATE_NOT_OUT);
		countParam.setOrders(orders);
		Long count = orderdetailDao.getCount(countParam,null,null);
		//3.count=0表示，所有的明细都入库了，这时要更新订单的状态，入库完成时间，入库的操作员
		if(count==0){
			//更新订单状态
			orders.setState("1");
			//设置操作人
			orders.setEnder(empUuid);
			//设置操作时间
			orders.setEndtime(orderDetail.getEndtime());
			//获取客户信息
			Supplier supplier = supplierDao.get(orders.getSupplieruuid());
			//调用红日物流系统，进行在线预约下单
			Long waybillSn=waybillWs.addWaybill(1l, supplier.getAddress(), supplier.getName(), supplier.getTele(), "--");
			//设置运单编号
			orders.setWaybillsn(waybillSn);
		}
	}
	
}
