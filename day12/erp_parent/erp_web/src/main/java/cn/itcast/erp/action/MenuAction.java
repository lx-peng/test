package cn.itcast.erp.action;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;

/**
 * 菜单Action 
 * @author Administrator
 *
 */
public class MenuAction extends BaseAction<Menu> {

	private IMenuBiz menuBiz;

	public void setMenuBiz(IMenuBiz menuBiz) {
		this.menuBiz = menuBiz;
		super.setBaseBiz(this.menuBiz);
	}

	/**
	 * 获取菜单树
	 */
	public void getMenuTree(){
		//通过获取主菜单，自关联就会带旗下所有的菜单
		Menu menu = menuBiz.readMenusByEmpuuid(getLoginUser().getUuid());
		//转化成JSON字符串
		String menuJsonString = JSON.toJSONString(menu);
		write(menuJsonString);
	}
	//根据员工编号获取菜单
	public void getMenusByEmpuuid(){
		//得到当前登录的用户
		Emp emp=getLoginUser();
		List<Menu> menuList=menuBiz.getMenusByEmpuuid(emp.getUuid());
		write(JSON.toJSONString(menuList));
		
	}
}
