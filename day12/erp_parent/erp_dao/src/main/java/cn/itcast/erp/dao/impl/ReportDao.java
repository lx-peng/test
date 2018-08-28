package cn.itcast.erp.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import cn.itcast.erp.dao.IReportDao;
/**
 * 报表数据访问实现类
 */
public class ReportDao extends HibernateDaoSupport implements IReportDao {

	/**
	 * 销售报表统计
	 */
	@Override
	public List orderReport(Date startDate, Date endDate) {
		String hql = "select new Map(gt.name as name,sum(od.num) as y) from Goodstype gt"+ 
" ,Goods g"+ 
" ,Orderdetail od"+ 
" ,Orders o"+
" where gt = g.goodstype and g.uuid = od.goodsuuid and od.orders = o and o.type='2'";
		List<Date> queryParam = new ArrayList<Date>();
		if(null!=startDate){
			hql+=" and o.createtime>=?";
			queryParam.add(startDate);
		}
		if(null!=endDate){
			hql+=" and o.createtime<=?";
			queryParam.add(endDate);
		}
		hql+=" group by gt.name";
		if(queryParam.size()>0){
			return getHibernateTemplate().find(hql,queryParam.toArray(new Object[]{}));
		}
		return getHibernateTemplate().find(hql);
	}
	/**
	 * 统计某年中每个月的销售额
	 */
	@Override
	public List<Map<String, Object>> getSumMoney(int year) {
		String hql = "select new Map(month(o.createtime) || '月' as month,sum(od.money) as y)"+
	" from Goodstype gt,Goods g,Orderdetail od,Orders o where gt=g.goodstype and g.uuid = od.goodsuuid and od.orders = o and o.type='2' and year(o.createtime)=?"+
	" group by month(o.createtime)";			
		return (List<Map<String, Object>>) getHibernateTemplate().find(hql,year);
	}

}
