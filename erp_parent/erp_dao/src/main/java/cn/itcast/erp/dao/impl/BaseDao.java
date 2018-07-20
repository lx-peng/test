package cn.itcast.erp.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import cn.itcast.erp.dao.IBaseDao;
/**
 * 部门数据访问类
 * @author Administrator
 *
 */
public class BaseDao<T> extends HibernateDaoSupport implements IBaseDao<T> {

	private Class<T> entityClass;
	public BaseDao(){
		//通过子类来获取父类
		Type baseDaoClass = getClass().getGenericSuperclass();
		//转成参数化类型
		ParameterizedType pType=(ParameterizedType)baseDaoClass;
		//获取参数类型的数组
		Type[] types = pType.getActualTypeArguments();
		//得到泛型里T的类型
		Type targetType = types[0];
		//转成class类型
		entityClass=(Class<T>)targetType;
	}
	/**
	 * 查询全部列表
	 */
	public List<T> getList() {
		// TODO Auto-generated method stub
		return (List<T>)getHibernateTemplate().find("from T");
	}
	/**
	 * 查询全部列表
	 */
	public List<T> getList(T dep1,T dep2,Object object) {
		// TODO Auto-generated method stub
		DetachedCriteria dc=getDetachedCriteria(dep1, dep2, object);
		return (List<T>) this.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * 查询全部列表
	 */
	public List<T> getList(T dep1,T dep2,Object object,int firstResult,int maxResult) {
		// TODO Auto-generated method stub
		DetachedCriteria dc=getDetachedCriteria(dep1, dep2, object);
		List<T> list=(List<T>)this.getHibernateTemplate().findByCriteria(dc,firstResult,maxResult);
		return list;
	}
	
	/**
	 * 统计记录个数
	 */
	public long getCount(T dep1,T dep2,Object object) {
		// TODO Auto-generated method stub
		DetachedCriteria dc=getDetachedCriteria(dep1, dep2, object);
		dc.setProjection(Projections.rowCount());
		List<Long> list= (List<Long>) this.getHibernateTemplate().findByCriteria(dc);
		return list.get(0);
	}
	/**
	 * 构建查询条件
	 */
	public DetachedCriteria getDetachedCriteria(T dep1,T dep2,Object object){
		
		return null;
	}
	public void add(T dep){
		getHibernateTemplate().save(dep);
	}
	public void delete(Long id) {
		// TODO Auto-generated method stub
		getHibernateTemplate().delete(getHibernateTemplate().get(entityClass, id));
	}
	public T get(Long id) {
		// TODO Auto-generated method stub
		return getHibernateTemplate().get(entityClass, id);
	}
	public void update(T dep) {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(dep);
	}
	
	
}
