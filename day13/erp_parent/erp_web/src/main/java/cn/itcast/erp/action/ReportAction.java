package cn.itcast.erp.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IReportBiz;

public class ReportAction {

	private IReportBiz reportBiz;
	private Date startDate;
	private Date endDate;
	private int year;
	
	public Date getStartDate() {
		return startDate;
	}

	public void setReportBiz(IReportBiz reportBiz) {
		this.reportBiz = reportBiz;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * 销售趋势报表
	 */
	public void trendReport(){
		List<Map<String,Object>> reportData = reportBiz.trendReport(year);
		write(JSON.toJSONString(reportData));
	}
	/**
	 * 统计某年中每月销售额
	 */
	public void getSumMoney(){
		List<Map<String,Object>> list=reportBiz.getSumMoney(year);
		write(JSON.toJSONString(list));
	}

	/**
	 * 销售报表统计
	 */
	public void orderReport(){
		List or = reportBiz.orderReport(startDate, endDate);
		write(JSON.toJSONString(or));
	}
	
	/**
	 * 输出字符串到前端
	 * @param jsonString
	 */
	public void write(String jsonString){
		try {
			//响应对象
			HttpServletResponse response = ServletActionContext.getResponse();
			//设置编码
			response.setContentType("text/html;charset=utf-8"); 
			//输出给页面
			response.getWriter().write(jsonString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
