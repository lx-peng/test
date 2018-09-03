package cn.itcast.erp.action;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IRoleBiz;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;

/**
 * 角色Action 
 * @author Administrator
 *
 */
public class RoleAction extends BaseAction<Role> {

	private IRoleBiz roleBiz;
	private String checkedStr;//勾选中的菜单ID字符串，以逗号分割
	
	
	public String getCheckedStr() {
		return checkedStr;
	}
	public void setCheckedStr(String checkedStr) {
		this.checkedStr = checkedStr;
	}
	public void setRoleBiz(IRoleBiz roleBiz) {
		this.roleBiz = roleBiz;
		super.setBaseBiz(this.roleBiz);
	}
	//读取角色菜单
	public void readRoleMenus(){
		List<Tree> treeList = roleBiz.readRoleMenus(getId());
		write(JSON.toJSONString(treeList));
	}
	//更新角色权限设置
	public void updateRoleMenus(){
		try{
			roleBiz.updateRoleMenus(getId(), checkedStr);
			ajaxReturn(true,"更新成功");
		}catch(Exception e){
			ajaxReturn(false,"更新失败");
			e.printStackTrace();
		}
		
	}
	
}
