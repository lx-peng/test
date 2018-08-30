package cn.itcast.erp.biz.impl;
import java.util.List;

import cn.itcast.erp.biz.IDepBiz;
import cn.itcast.erp.dao.IDepDao;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.entity.Dep;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.exception.ErpException;
/**
 * 部门业务逻辑类
 * @author Administrator
 *
 */
public class DepBiz extends BaseBiz<Dep> implements IDepBiz {

	private IDepDao depDao;
	private IEmpDao empDao;

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}
	
	public void setDepDao(IDepDao depDao) {
		this.depDao = depDao;
		super.setBaseDao(this.depDao);
	}
	
	@Override
	public void delete(Long uuid) {
		//构建查询条件
		Emp t1 = new Emp();
		// 构建查询条件中的部门
		t1.setDep(new Dep());
		// 部门编号
		t1.getDep().setUuid(uuid);
		// 这个部门下是否有员工
		List<Emp> empList = empDao.getList(t1, null, null);
		if(empList.size() > 0){
			//这个部门有员工，不能删除
			throw new ErpException("该部门底下有员工，不能删除");
		}
		super.delete(uuid);
	}
	
}
