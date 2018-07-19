package cn.itcast.erp.biz;

import java.util.List;

import cn.itcast.erp.entity.Dep;

public interface IDepBiz {

	/**
	 * 查询全部列表
	 */
	public List<Dep> getList(Dep dep, Dep dep2, Object object);
	/**
	 * 查询全部列表
	 */
	public List<Dep> getList(Dep dep, Dep dep2, Object object,int firstResult,int maxResult);
	/**
	 * 
	 */
	public long getCount(Dep dep, Dep dep2, Object object);
	/**
	 * 增加部门
	 */
	public void add(Dep dep);
	/**
	 * 删除部门
	 */
	public void delete(Long id);
	/**
	 * 查询实体
	 */
	public Dep get(Long id);
	/**
	 * 更新
	 */
	public void update(Dep dep);
}
