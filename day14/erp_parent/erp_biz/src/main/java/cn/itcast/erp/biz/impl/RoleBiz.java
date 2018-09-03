package cn.itcast.erp.biz.impl;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.erp.biz.IRoleBiz;
import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.dao.IRoleDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
import redis.clients.jedis.Jedis;
/**
 * 角色业务逻辑类
 * @author Administrator
 *
 */
public class RoleBiz extends BaseBiz<Role> implements IRoleBiz {
	private Jedis jedis;
	private IRoleDao roleDao;
	private IMenuDao menuDao;
	
	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
		super.setBaseDao(this.roleDao);
	}

	public void setMenuDao(IMenuDao menuDao) {
		this.menuDao = menuDao;
	}

	@Override
	public List<Tree> readRoleMenus(Long uuid) {
		List<Tree> treeList =new ArrayList<Tree>();
		Menu root=menuDao.get("0");
		//获取角色对应的权限菜单列表
		Role role=roleDao.get(uuid);
		List<Menu> roleMenus=role.getMenus();
		//构建tree集合数据
		Tree t1=null;
		Tree t2=null;
		//把菜单转换成属性结构数据
		for (Menu m1 : root.getMenus()) {
			t1=new Tree();//一级菜单
			t1.setId(m1.getMenuid());
			t1.setText(m1.getMenuname());
			for (Menu m2 : m1.getMenus()) {
				t2=new Tree();//二级菜单
				t2.setId(m2.getMenuid());
				t2.setText(m2.getMenuname());
				if(roleMenus.contains(m2)){
					t2.setChecked(true);
				}
				t1.getChildren().add(t2);
			}
			treeList.add(t1);
		}
		return treeList;
	}
	//更新角色权限设置
	@Override
	public void updateRoleMenus(Long uuid, String checkedStr) {
		Role role=roleDao.get(uuid);
		//清空角色下的菜单权限
		role.setMenus(new ArrayList<Menu>());
		//获得菜单ID的数组
		String[] ids=checkedStr.split(",");
		Menu menu=null;
		for (String id : ids) {
			menu=menuDao.get(id);
			role.getMenus().add(menu);//往中间表添加数据
		}
		try {
			//清空所有属于这个角色所有员工的菜单缓存
			List<Emp> empList = role.getEmpList();
			for (Emp emp : role.getEmpList()) {
				jedis.del("menuList_"+emp.getUuid());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
