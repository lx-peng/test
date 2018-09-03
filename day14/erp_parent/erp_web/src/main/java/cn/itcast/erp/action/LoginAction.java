package cn.itcast.erp.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.entity.Emp;

/**
 * 实现用户登录与登出功能
 * @author Administrator
 *
 */
public class LoginAction{
	
	//登录用户名
	private String username;
	//密码
	private String pwd;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	

	/**
	 * 登录验证请求
	 */
	public void checkUser(){
		try {
			//验证登录
			/*Emp loginUser=empBiz.findByUsernameAndPwd(username, pwd);
			if(null==loginUser){
				ajaxReturn(false,"用户名或密码不正确");
				return;
			}
			//保存到session中，表明用户已经登录了
			ActionContext.getContext().getSession().put("loginUser", loginUser);
			ajaxReturn(true,"");*/
			//创建令牌
			UsernamePasswordToken upt = new UsernamePasswordToken(username,pwd);
			//2.获得主体subject
			Subject subject = SecurityUtils.getSubject();
			//3。执行login方法
			subject.login(upt);
			ajaxReturn(true,"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false,"发生异常");
		}
	}
	/**
	 * 返回前端操作结果
	 * @param success
	 * @param message
	 */
	public void ajaxReturn(boolean success, String message){
		//返回前端的JSON数据
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.put("success",success);
		rtn.put("message",message);
		write(JSON.toJSONString(rtn));
	}
	
	/**
	 * 输出字符串到前端
	 * @param jsonString
	 */
	private void write(String jsonString){
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
	/**
	 * 显示登录用户名称
	 */
	public void showName(){
		//Emp emp = (Emp)ActionContext.getContext().getSession().get("loginUser");
		Emp emp=(Emp)SecurityUtils.getSubject().getPrincipal();
		if(null!=emp){
			ajaxReturn(true,emp.getName());
		}else{
			ajaxReturn(false,"");
		}
	}
	/**
	 * 退出登录
	 */
	public void loginOut(){
		//ActionContext.getContext().getSession().remove("loginUser");
		SecurityUtils.getSubject().logout();
	}
}
