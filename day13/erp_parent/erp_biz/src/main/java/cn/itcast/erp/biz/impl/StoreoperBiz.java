package cn.itcast.erp.biz.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.erp.biz.IStoreoperBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IStoreDao;
import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Storeoper;
/**
 * 仓库操作记录业务逻辑类
 * @author Administrator
 *
 */
public class StoreoperBiz extends BaseBiz<Storeoper> implements IStoreoperBiz {

	private IStoreoperDao storeoperDao;
	private IStoreDao storeDao;
	private IGoodsDao goodsDao;
	private IEmpDao empDao;
	public void setStoreoperDao(IStoreoperDao storeoperDao) {
		this.storeoperDao = storeoperDao;
		super.setBaseDao(this.storeoperDao);
	}
	public IStoreDao getStoreDao() {
		return storeDao;
	}
	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
	}
	public IGoodsDao getGoodsDao() {
		return goodsDao;
	}
	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}
	public IEmpDao getEmpDao() {
		return empDao;
	}
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}
	public List<Storeoper> getListByPage(Storeoper t1,Storeoper t2,Object param,int firstResult,int maxResults){
		List<Storeoper> logList = super.getListByPage(t1, t2, param, firstResult, maxResults);
		//缓存员工名称
		Map<Long,String> empNameMap = new HashMap<Long,String>();
		Map<Long,String> storeNameMap = new HashMap<Long,String>();
		//缓存商品名称
		Map<Long,String> goodsNameMap = new HashMap<Long,String>();
		for (Storeoper so : logList) {
			so.setEmpName(getEmpName(so.getEmpuuid(),empNameMap));
			so.setStoreName(getStoreName(so.getEmpuuid(),storeNameMap));
			so.setGoodsName(getGoodsName(so.getGoodsuuid(),goodsNameMap));
		}
		return logList;
	}
	public String getEmpName(Long uuid,Map<Long,String> empNameMap){
		if(null==uuid){
			return null;
		}
		String empName = empNameMap.get(uuid);
		if(null==empName){
			empName = empDao.get(uuid).getName();
			empNameMap.put(uuid, empName);
		}
		return empName;
	}
	public String getStoreName(Long uuid,Map<Long,String> storeNameMap){
		if(null==uuid){
			return null;
		}
		String storeName = storeNameMap.get(uuid);
		if(null==storeName){
			storeName = storeDao.get(uuid).getName();
			storeNameMap.put(uuid, storeName);
		}
		return storeName;
	}
	public String getGoodsName(Long uuid,Map<Long,String> goodsNameMap){
		if(null==uuid){
			return null;
		}
		String goodsName = goodsNameMap.get(uuid);
		if(null==goodsName){
			goodsName = goodsDao.get(uuid).getName();
			goodsNameMap.put(uuid, goodsName);
		}
		return goodsName;
	}
}
