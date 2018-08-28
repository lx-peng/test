package cn.itcast.erp.action;
import cn.itcast.erp.biz.IStoreBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Store;

/**
 * 仓库Action 
 * @author Administrator
 *
 */
public class StoreAction extends BaseAction<Store> {

	private IStoreBiz storeBiz;

	public void setStoreBiz(IStoreBiz storeBiz) {
		this.storeBiz = storeBiz;
		super.setBaseBiz(this.storeBiz);
	}

	public void myList(){
		if(null==getT1()){
			//构建查询条件
			setT1(new Store());
		}
		Emp loginUser = getLoginUser();
		if(null==loginUser){
			ajaxReturn(false,"您还没有登录");
			return;
		}
		//查找当前登录用户下的仓库
		getT1().setEmpuuid(loginUser.getUuid());
		super.list();
	}
	
}
