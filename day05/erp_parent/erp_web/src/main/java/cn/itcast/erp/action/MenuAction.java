package cn.itcast.erp.action;
import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IMenuBiz;
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
		//查询顶级菜单，即可带出其下的每个子菜单
		Menu menu = menuBiz.get("0");
		//转化成JSON字符串
		String menuJsonString = JSON.toJSONString(menu);
		write(menuJsonString);
	}
}
