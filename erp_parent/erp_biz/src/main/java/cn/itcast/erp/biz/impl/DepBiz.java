package cn.itcast.erp.biz.impl;

import java.util.List;

import cn.itcast.erp.biz.IDepBiz;
import cn.itcast.erp.dao.IDepDao;
import cn.itcast.erp.entity.Dep;
/**
 * 业务逻辑类
 * @author Administrator
 *
 */
public class DepBiz implements IDepBiz {
	private IDepDao depDao;
	public void setDepDao(IDepDao depDao){
		this.depDao=depDao;
	}
	public List<Dep> getList(Dep dep1,Dep dep2,Object object) {
		// TODO Auto-generated method stub
		return depDao.getList(dep1,dep2,object);
	}
	public List<Dep> getList(Dep dep1,Dep dep2,Object object,int firstResult,int maxResult) {
		// TODO Auto-generated method stub
		return depDao.getList(dep1,dep2,object,firstResult,maxResult);
	}
	public long getCount(Dep dep1,Dep dep2,Object object) {
		// TODO Auto-generated method stub
		return depDao.getCount(dep1,dep2,object);
	}
	public void add(Dep dep) {
		// TODO Auto-generated method stub
		depDao.add(dep);
	}
}
