package cn.itcast.erp.dao;

import java.util.List;

import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
/**
 * 员工数据访问接口
 * @author Administrator
 *
 */
public interface IEmpDao extends IBaseDao<Emp>{

	/**
	 * 根据用户名和密码查询登录用户信息
	 */
	Emp findByUsernameAndPwd(String username,String pwd);
	/**
	 * 修改密码
	 */
	void updatePwd(Long uuid,String newPwd);
	List<Menu> getMenusByEmpuuid(Long uuid);
}
