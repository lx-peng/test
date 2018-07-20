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
public class DepDao extends BaseDao<Dep> implements IDepDao {

	
	/**
	 * 构建查询条件
	 */
	public DetachedCriteria getDetachedCriteria(Dep dep1,Dep dep2,Object object){
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
	
}
