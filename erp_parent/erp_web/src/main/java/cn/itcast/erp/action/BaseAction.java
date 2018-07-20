package cn.itcast.erp.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.itcast.erp.biz.IBaseBiz;

public class BaseAction<T> {

	private IBaseBiz baseBiz;
	private int page;//当前页码
	private int rows;//每页个数
	private T t;
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setT(T t) {
		this.t = t;
	}

	public T getT() {
		return t;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setBaseBiz(IBaseBiz baseBiz) {
		this.baseBiz = baseBiz;
	}
	private T t1;
	
	public T getT1() {
		return t1;
	}
	private T t2;
	
	public void setT2(T t2) {
		this.t2 = t2;
	}
	private Object object;
	
	public void setObject(Object object) {
		this.object = object;
	}

	public void setT1(T t1) {
		this.t1 = t1;
	}
	
	public void list(){
		List<T> list=baseBiz.getList(t1,t2,object);
		for (T t : list) {
			System.out.println(t.toString());
		}
		String jsonString=JSON.toJSONString(list);
		write(jsonString);
	}
	/**
	 * 分页
	 */
	public void listByPage(){
		System.out.println(page+", "+rows);
		int firstResult=(page-1)*rows;
		List<T> list=baseBiz.getList(t1,t2,object,firstResult,rows);
		long count = baseBiz.getCount(t1, t2, object);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("rows", list);
		map.put("total", count);
		String jsonString=JSON.toJSONString(map);
		write(jsonString);
	}
	public void getCount(){
		long list=baseBiz.getCount(t1,t2,object);
		String jsonString=JSON.toJSONString(list);
		write(jsonString);
	}
	
	public void write(String jsonString){
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().print(jsonString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void add(){
		try {
			baseBiz.add(t);
			write(ajaxReturn(true,"增加成功"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			write(ajaxReturn(false,"发生异常"));
		}
		
	}
	private String ajaxReturn(boolean success,String message){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("success", success);
		map.put("message", message);
		return JSON.toJSONString(map);
	}
	public void delete(){
		try {
			baseBiz.delete(id);
			write(ajaxReturn(true,"删除成功"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			write(ajaxReturn(false,"发生异常"));
		}
	}
	public void get(){
		T t=(T) baseBiz.get(id);
		String jsonString=JSON.toJSONString(t);
		write(mapJson(jsonString,"t"));
	}
	/**
	 * 加前缀
	 */
	public String mapJson(String jsonString,String prefix){
		Map<String,Object> map = JSON.parseObject(jsonString);
		Map<String,Object> newMap=new HashMap<String,Object>();
		for (String key : map.keySet()) {
			newMap.put(prefix+"."+key, map.get(key));
		}
		return JSON.toJSONString(newMap);
	}
	/**
	 * 更新
	 */
	public void update(){
		try {
			baseBiz.update(t);
			write(ajaxReturn(true,"修改成功"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			write(ajaxReturn(false,"发生异常"));
		}
	}
}
