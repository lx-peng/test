package cn.itcast.erp.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import cn.itcast.erp.dao.IDepDao;
import cn.itcast.erp.entity.Dep;
/**
 * 部门数据访问类
 * @author Administrator
 *
 */
public class DepDao extends HibernateDaoSupport implements IDepDao {

	/**
	 * 查询全部列表
	 */
	public List<Dep> getList() {
		// TODO Auto-generated method stub
		return (List<Dep>)getHibernateTemplate().find("from Dep");
	}
	/**
	 * 查询全部列表
	 */
	public List<Dep> getList(Dep dep1,Dep dep2,Object object) {
		// TODO Auto-generated method stub
		DetachedCriteria dc=getDetachedCriteria(dep1, dep2, object);
		return (List<Dep>) this.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * 查询全部列表
	 */
	public List<Dep> getList(Dep dep1,Dep dep2,Object object,int firstResult,int maxResult) {
		// TODO Auto-generated method stub
		DetachedCriteria dc=getDetachedCriteria(dep1, dep2, object);
		return (List<Dep>) this.getHibernateTemplate().findByCriteria(dc,firstResult,maxResult);
	}
	
	/**
	 * 统计记录个数
	 */
	public long getCount(Dep dep1,Dep dep2,Object object) {
		// TODO Auto-generated method stub
		DetachedCriteria dc=getDetachedCriteria(dep1, dep2, object);
		dc.setProjection(Projections.rowCount());
		List<Long> list= (List<Long>) this.getHibernateTemplate().findByCriteria(dc);
		return list.get(0);
	}
	/**
	 * 构建查询条件
	 */
	private DetachedCriteria getDetachedCriteria(Dep dep1,Dep dep2,Object object){
		DetachedCriteria dc=DetachedCriteria.forClass(Dep.class);
		if(dep1!=null){
			if(dep1.getName()!=null && dep1.getName().length()>0){
				dc.add(Restrictions.like("name",dep1.getName(),MatchMode.ANYWHERE));
			}
			if(dep1.getTele()!=null && dep1.getTele().length()>0){
				dc.add(Restrictions.like("tele", dep1.getTele(),MatchMode.ANYWHERE));
			}
		}
		return dc;
	}
	public void add(Dep dep){
		getHibernateTemplate().save(dep);
	}
}
