package cn.itcast.erp.biz.impl;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.crypto.hash.Md5Hash;

import cn.itcast.erp.ERPException;
import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IRoleDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
/**
 * 员工业务逻辑类
 * @author Administrator
 *
 */
public class EmpBiz extends BaseBiz<Emp> implements IEmpBiz {
	private IRoleDao roleDao;
	private IEmpDao empDao;
	/**散列次数*/
	private int hashIterations=2;
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
		super.setBaseDao(this.empDao);
	}

	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
	}

	@Override
	public Emp findByUsernameAndPwd(String username, String pwd) throws Exception{
		return empDao.findByUsernameAndPwd(username, encrypt(pwd,username));
	}
	public void add(Emp emp){
		emp.setPwd(encrypt(emp.getUsername(),emp.getUsername()));
		empDao.add(emp);
	}
	
	public String encrypt(String src,String salt){
		Md5Hash md=new Md5Hash(src,salt,hashIterations);
		return md.toString();
	}

	@Override
	public void updatePwd(Long uuid,String oldPwd, String newPwd){
		Emp emp = empDao.get(uuid);
		String encryptedOldPwd = encrypt(oldPwd,emp.getUsername());
		if(!encryptedOldPwd.equals(emp.getPwd())){
			throw new ERPException("原密码不正确");
		}
		empDao.updatePwd(uuid, encrypt(newPwd,emp.getUsername()));
	}

	@Override
	public void updatePwd_reset(Long uuid, String newPwd) {
		Emp emp = empDao.get(uuid);
		emp.setPwd(encrypt(newPwd,emp.getUsername()));
	}
	//读取用户角色
	@Override
	public List<Tree> readEmpRoles(Long uuid) {
		List<Tree> treeList=new ArrayList<Tree>();
		//获取用户
		Emp emp=empDao.get(uuid);
		//得到用户下的角色列表
		List<Role> empRoles = emp.getRoles();
		//获取所有角色
		List<Role> roleList=roleDao.getList(null, null, null);
		Tree t1=null;
		for (Role role : roleList) {
			t1=new Tree();
			t1.setId(String.valueOf(role.getUuid()));
			t1.setText(role.getName());
			if(empRoles.contains(role)){
				//如果该用户包含该角色，则让他选中
				t1.setChecked(true);
			}
			treeList.add(t1);
		}
		return treeList;
	}
	//更新用户角色
	public void updateEmpRoles(Long uuid,String checkedStr){
		Emp emp=empDao.get(uuid);
		emp.setRoles(new ArrayList<Role>());//清空用户角色
		//得到勾选中的角色ID数组
		String[] ids = checkedStr.split(",");
		Role role=null;
		for (String id : ids) {
			//通过id获取角色
			role=roleDao.get(Long.valueOf(id));
			//给用户添加角色
			emp.getRoles().add(role);
		}
	}

	@Override
	public List<Menu> getMenusByEmpuuid(Long uuid) {
		// TODO Auto-generated method stub
		return empDao.getMenusByEmpuuid(uuid);
	}
}
