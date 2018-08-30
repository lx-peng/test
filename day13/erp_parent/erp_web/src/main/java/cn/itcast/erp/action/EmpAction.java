package cn.itcast.erp.action;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Tree;

/**
 * 员工Action 
 * @author Administrator
 *
 */
public class EmpAction extends BaseAction<Emp> {
	private Emp emp;
	private IEmpBiz empBiz;
	private String oldPwd;
	private String newPwd;
	private String checkedStr;//勾选中的角色id字符串，以逗号分割
	public String getOldPwd() {
		return oldPwd;
	}
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	
	public String getNewPwd() {
		return newPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	public Emp getEmp() {
		return emp;
	}
	public void setEmp(Emp emp) {
		this.emp = emp;
	}
	public void setEmpBiz(IEmpBiz empBiz) {
		this.empBiz = empBiz;
		super.setBaseBiz(this.empBiz);
	}
	
	public String getCheckedStr() {
		return checkedStr;
	}
	public void setCheckedStr(String checkedStr) {
		this.checkedStr = checkedStr;
	}
	public void updatePwd(){
		try {
			Emp loginUser=getLoginUser();
			empBiz.updatePwd(loginUser.getUuid(), oldPwd, newPwd);
			ajaxReturn(true,"密码修改成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(true,"密码修改失败");
		}
	}
	public void updatePwd_reset(){
		Emp loginUser=getLoginUser();
		try {
			empBiz.updatePwd_reset(getId(), newPwd);
			ajaxReturn(true,"密码修改成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(true,"密码修改失败");
		}
	}
	//获取用户角色信息
	public void readEmpRoles(){
		List<Tree> roleList=empBiz.readEmpRoles(getId());
		write(JSON.toJSONString(roleList));
	}
	//更新用户角色
	public void updateEmpRoles(){
		try{
			empBiz.updateEmpRoles(getId(), checkedStr);
			ajaxReturn(true,"更新成功");
		}catch(Exception e){
			ajaxReturn(false,"更新失败");
			e.printStackTrace();
		}
	}
}
