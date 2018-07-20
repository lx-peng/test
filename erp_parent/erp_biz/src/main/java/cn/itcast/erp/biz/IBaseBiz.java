package cn.itcast.erp.biz;

import java.util.List;


public interface IBaseBiz<T> {

	/**
	 * 查询全部列表
	 */
	public List<T> getList(T t, T t2, Object object);
	/**
	 * 查询全部列表
	 */
	public List<T> getList(T t, T t2, Object object,int firstResult,int maxResult);
	/**
	 * 
	 */
	public long getCount(T t, T t2, Object object);
	/**
	 * 增加部门
	 */
	public void add(T t);
	/**
	 * 删除部门
	 */
	public void delete(Long id);
	/**
	 * 查询实体
	 */
	public T get(Long id);
	/**
	 * 更新
	 */
	public void update(T t);
}
