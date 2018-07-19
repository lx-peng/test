package cn.itcast.erp.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.itcast.erp.biz.IDepBiz;
import cn.itcast.erp.entity.Dep;

public class DepAction {

	private IDepBiz depBiz;
	private int page;//当前页码
	private int rows;//每页个数
	private Dep dep;
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDep(Dep dep) {
		this.dep = dep;
	}

	public Dep getDep() {
		return dep;
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

	public void setDepBiz(IDepBiz depBiz) {
		this.depBiz = depBiz;
	}
	private Dep dep1;
	
	public Dep getDep1() {
		return dep1;
	}
	private Dep dep2;
	
	public void setDep2(Dep dep2) {
		this.dep2 = dep2;
	}
	private Object object;
	
	public void setObject(Object object) {
		this.object = object;
	}

	public void setDep1(Dep dep1) {
		this.dep1 = dep1;
	}
	
	public void list(){
		List<Dep> list=depBiz.getList(dep1,dep2,object);
		for (Dep dep : list) {
			System.out.println(dep.toString());
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
		List<Dep> list=depBiz.getList(dep1,dep2,object,firstResult,rows);
		long count = depBiz.getCount(dep1, dep2, object);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("rows", list);
		map.put("total", count);
		String jsonString=JSON.toJSONString(map);
		write(jsonString);
	}
	public void getCount(){
		long list=depBiz.getCount(dep1,dep2,object);
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
			depBiz.add(dep);
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
			depBiz.delete(id);
			write(ajaxReturn(true,"删除成功"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			write(ajaxReturn(false,"发生异常"));
		}
	}
	public void get(){
		Dep dep=depBiz.get(id);
		String jsonString=JSON.toJSONString(dep);
		write(mapJson(jsonString,"dep"));
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
			depBiz.update(dep);
			write(ajaxReturn(true,"修改成功"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			write(ajaxReturn(false,"发生异常"));
		}
	}
}
