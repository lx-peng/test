package cn.itcast.erp.action;
import cn.itcast.erp.ERPException;
import cn.itcast.erp.biz.IOrderdetailBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;

/**
 * 订单明细Action 
 * @author Administrator
 *
 */
public class OrderdetailAction extends BaseAction<Orderdetail> {

	//仓库编号
	private Long storeuuid;
	
	private IOrderdetailBiz orderdetailBiz;

	public void setOrderdetailBiz(IOrderdetailBiz orderdetailBiz) {
		this.orderdetailBiz = orderdetailBiz;
		super.setBaseBiz(this.orderdetailBiz);
	}

	public Long getStoreuuid() {
		return storeuuid;
	}

	public void setStoreuuid(Long storeuuid) {
		this.storeuuid = storeuuid;
	}

	//入库操作
	public void doInStore(){
		Emp loginUser = getLoginUser();
		if(null==loginUser){
			ajaxReturn(false,"亲，您还没有登录！");
			return;
		}
		try {
			//入库业务
			orderdetailBiz.doInStore(getId(), storeuuid, loginUser.getUuid());
			ajaxReturn(true,"入库成功");
		} catch (ERPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false,e.getMessage());
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false,"入库失败");
		}
		
	}
}
