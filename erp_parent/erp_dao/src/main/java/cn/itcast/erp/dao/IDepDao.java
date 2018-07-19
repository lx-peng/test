package cn.itcast.erp.dao;

import java.util.List;

import cn.itcast.erp.entity.Dep;

/**
 * 部门数据访问接口
 * @author Administrator
 *
 */
public interface IDepDao {

	public List<Dep> getList(Dep dep, Dep dep2, Object object);
	public List<Dep> getList(Dep dep, Dep dep2, Object object,int firstResult,int maxResult);
	public long getCount(Dep dep, Dep dep2, Object object);
	public void add(Dep dep);
	public void delete(Long id);
	public Dep get(Long id);
	public void update(Dep dep);
}
