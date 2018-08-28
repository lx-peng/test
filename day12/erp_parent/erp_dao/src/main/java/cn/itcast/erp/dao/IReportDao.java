package cn.itcast.erp.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IReportDao{

	/**
	 * 销售报表统计
	 */
	public List orderReport(Date startDate,Date endDate);
	/**
	 * 统计某年中每个月的销售额
	 */
	public List<Map<String,Object>> getSumMoney(int year);
}
