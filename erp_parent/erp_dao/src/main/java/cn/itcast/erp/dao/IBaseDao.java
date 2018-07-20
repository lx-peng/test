package cn.itcast.erp.dao;

import java.util.List;

import cn.itcast.erp.entity.Dep;

/**
 * 部门数据访问接口
 * @author Administrator
 *
 */
public interface IBaseDao<T>{

	public List<T> getList(T t, T t2, Object object);
	public List<T> getList(T t, T t2, Object object,int firstResult,int maxResult);
	public long getCount(T t, T t2, Object object);
	public void add(T t);
	public void delete(Long id);
	public T get(Long id);
	public void update(T t);
}
