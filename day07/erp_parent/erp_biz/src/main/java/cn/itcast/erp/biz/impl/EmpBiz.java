package cn.itcast.erp.biz.impl;
import org.apache.shiro.crypto.hash.Md5Hash;

import com.mysql.jdbc.exceptions.jdbc4.MySQLDataException;

import cn.itcast.erp.ERPException;
import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.entity.Emp;
/**
 * 员工业务逻辑类
 * @author Administrator
 *
 */
public class EmpBiz extends BaseBiz<Emp> implements IEmpBiz {

	private IEmpDao empDao;
	/**散列次数*/
	private int hashIterations=2;
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
		super.setBaseDao(this.empDao);
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
}
