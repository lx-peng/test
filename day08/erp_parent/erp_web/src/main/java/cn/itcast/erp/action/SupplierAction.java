package cn.itcast.erp.action;
import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.entity.Supplier;

/**
 * 供应商Action 
 * @author Administrator
 *
 */
public class SupplierAction extends BaseAction<Supplier> {
	/**remote传过来的参数 **/
	private String q;
	private ISupplierBiz supplierBiz;
	public void setSupplierBiz(ISupplierBiz supplierBiz) {
		this.supplierBiz = supplierBiz;
		super.setBaseBiz(this.supplierBiz);
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	/**
	 * 自动补全
	 */
	public void list(){
		if(null==getT1()){
			setT1(new Supplier());
		}
		getT1().setName(q);
		super.list();
	}

}
