package cn.itcast.erp.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IReportBiz {
	/**
	 * 销售报表统计
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List orderReport(Date startDate,Date endDate);
	/**
	 * 统计某年中每个月的销售额
	 */
	public List<Map<String,Object>> getSumMoney(int year);
	/**
	 * 销售趋势
	 */
	public List<Map<String,Object>> trendReport(int year);
}
