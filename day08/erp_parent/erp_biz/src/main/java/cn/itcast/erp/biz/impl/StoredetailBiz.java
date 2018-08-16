package cn.itcast.erp.biz.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IStoreDao;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.entity.Storedetail;
/**
 * 仓库库存业务逻辑类
 * @author Administrator
 *
 */
public class StoredetailBiz extends BaseBiz<Storedetail> implements IStoredetailBiz {

	private IStoredetailDao storedetailDao;
	private IGoodsDao goodsDao;
	private IStoreDao storeDao;
	
	public void setStoredetailDao(IStoredetailDao storedetailDao) {
		this.storedetailDao = storedetailDao;
		super.setBaseDao(this.storedetailDao);
	}

	public IGoodsDao getGoodsDao() {
		return goodsDao;
	}

	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}

	public IStoreDao getStoreDao() {
		return storeDao;
	}

	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
	}
	private String getGoodsName(Long uuid,Map<Long,String> goodsNameMap){
		if(null == uuid){
			return null;
		}
		String goodsName = goodsNameMap.get(uuid);
		if(null == goodsName){
			goodsName = goodsDao.get(uuid).getName();
			goodsNameMap.put(uuid, goodsName);
		}
		return goodsName;
	}
	private String getStoreName(Long uuid,Map<Long,String> storeNameMap){
		if(null == uuid){
			return null;
		}
		String storeName = storeNameMap.get(uuid);
		if(null == storeName){
			storeName = storeDao.get(uuid).getName();
			storeNameMap.put(uuid, storeName);
		}
		return storeName;
	}
	/**
	 * 分页查询
	 * 
	 */
	public List<Storedetail> getListByPage(Storedetail t1,Storedetail t2,Object param,int firstResult,int maxResults){
		List<Storedetail> list = super.getListByPage(t1, t2, param, firstResult, maxResults);
		Map<Long,String> goodsNameMap = new HashMap<Long,String>();
		Map<Long,String> storeNameMap = new HashMap<Long,String>();
		for (Storedetail sd : list) {
			sd.setGoodsName(getGoodsName(sd.getGoodsuuid(),goodsNameMap));
			sd.setStoreName(getStoreName(sd.getStoreuuid(),storeNameMap));
		}
		return list;
	}
}
