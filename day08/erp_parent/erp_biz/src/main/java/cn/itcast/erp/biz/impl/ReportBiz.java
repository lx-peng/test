package cn.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.erp.biz.IReportBiz;
import cn.itcast.erp.dao.IReportDao;

public class ReportBiz implements IReportBiz {

	private IReportDao reportDao;
	
	public void setReportDao(IReportDao reportDao) {
		this.reportDao = reportDao;
	}
	/**
	 * 销售报表统计
	 */
	@Override
	public List orderReport(Date startDate, Date endDate) {
		return reportDao.orderReport(startDate, endDate);
	}
	@Override
	public List<Map<String, Object>> getSumMoney(int year) {
		return reportDao.getSumMoney(year);
	}
	/**
	 * 销售趋势
	 */
	@Override
	public List<Map<String, Object>> trendReport(int year) {
		//保存每个月份的销售额
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>(12);
		//获取当年每月销售额
		List<Map<String,Object>> yearData = reportDao.getSumMoney(year);
		//把从DB中获取的当年每月销售额转换成map集合，用于下面的查缺补漏，因为可能存在某些月份没有销售额的情况
		Map<String,Map<String,Object>> map=new HashMap<String,Map<String,Object>>();
		for (Map<String, Object> m : yearData) {
			map.put((String)m.get("month"), m);
		}
		Map<String,Object> data=null;
		//按12个月，对每个月份的数据进行封装，最终以List<Map<String,Object>>形势返回
		for (int i = 0; i < 12; i++) {
			data=map.get(i+"月");
			if(null==data){
				//如果当月没有销售额，则补上当月的月份和数据
				data = new HashMap<String,Object>();
				data.put("month", i+"月");
				data.put("y", 0);
			}
			result.add(data);
		}
		
		return result;
	}

}
