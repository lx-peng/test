package cn.itcast.erp.biz.impl;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.entity.Menu;
/**
 * 菜单业务逻辑类
 * @author Administrator
 *
 */
public class MenuBiz extends BaseBiz<Menu> implements IMenuBiz {

	private IMenuDao menuDao;
	
	public void setMenuDao(IMenuDao menuDao) {
		this.menuDao = menuDao;
		super.setBaseDao(this.menuDao);
	}

	@Override
	public List<Menu> getMenusByEmpuuid(Long uuid) {
		return menuDao.getMenusByEmpuuid(uuid);
	}
	//菜单复制，但不复制子菜单
	private Menu cloneMenu(Menu src){
		Menu _new=new Menu();
		_new.setIcon(src.getIcon());
		_new.setMenuid(src.getMenuid());
		_new.setMenuname(src.getMenuname());
		_new.setMenus(new ArrayList<Menu>());
		_new.setUrl(src.getUrl());
		return _new;
	}
	//根据员工获取菜单
	@Override
	public Menu readMenusByEmpuuid(Long uuid) {
		List<Menu> menuList=menuDao.getMenusByEmpuuid(uuid);
		Menu menu=menuDao.get("0");
		//复制根菜单
		Menu tMenu=cloneMenu(menu);
		//复制一级菜单
		Menu _m1=null;
		Menu _m2=null;
		for (Menu m1 : menu.getMenus()) {
			_m1=cloneMenu(m1);
			//复制二级菜单
			for (Menu m2 : m1.getMenus()) {
				//如果用户包含有该菜单
				if(menuList.contains(m2)){
					_m2=cloneMenu(m2);
					_m1.getMenus().add(_m2);
				}
			}
			if(_m1.getMenus().size()>0){
				//如果一级菜单下有二级菜单，则把一级菜单添加到根菜单下
				tMenu.getMenus().add(_m1);
			}
		}
		return tMenu;
	}
}
