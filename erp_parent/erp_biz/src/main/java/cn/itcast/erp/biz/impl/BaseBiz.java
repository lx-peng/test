package cn.itcast.erp.biz.impl;

import java.util.List;

import cn.itcast.erp.biz.IBaseBiz;
import cn.itcast.erp.dao.IBaseDao;
/**
 * 业务逻辑类
 * @author Administrator
 *
 */
public class BaseBiz<T> implements IBaseBiz<T> {
	private IBaseDao baseDao;
	public void setBaseDao(IBaseDao baseDao){
		this.baseDao=baseDao;
	}
	public List<T> getList(T t1,T t2,Object object) {
		// TODO Auto-generated method stub
		return baseDao.getList(t1,t2,object);
	}
	public List<T> getList(T t1,T t2,Object object,int firstResult,int maxResult) {
		// TODO Auto-generated method stub
		return baseDao.getList(t1,t2,object,firstResult,maxResult);
	}
	public long getCount(T t1,T t2,Object object) {
		// TODO Auto-generated method stub
		return baseDao.getCount(t1,t2,object);
	}
	public void add(T t) {
		// TODO Auto-generated method stub
		baseDao.add(t);
	}
	public void delete(Long id) {
		// TODO Auto-generated method stub
		baseDao.delete(id);
	}
	public T get(Long id) {
		// TODO Auto-generated method stub
		return (T) baseDao.get(id);
	}
	public void update(T t) {
		// TODO Auto-generated method stub
		baseDao.update(t);
	}
}
