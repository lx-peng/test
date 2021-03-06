package cn.itcast.erp.action;
import java.util.List;

import javax.mail.MessagingException;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.ERPException;
import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.entity.Storealert;
import cn.itcast.erp.entity.Storedetail;

/**
 * 仓库库存Action 
 * @author Administrator
 *
 */
public class StoredetailAction extends BaseAction<Storedetail> {

	private IStoredetailBiz storedetailBiz;

	public void setStoredetailBiz(IStoredetailBiz storedetailBiz) {
		this.storedetailBiz = storedetailBiz;
		super.setBaseBiz(this.storedetailBiz);
	}

	//库存预警
	public void getStorealertList(){
		List<Storealert> storealertList = storedetailBiz.getStorealertList();
		write(JSON.toJSONString(storealertList));
	}
	/**
	 * 发送预警邮件
	 */
	public void sendStorealertMail(){
		//调用预警业务员发送邮件
		try {
			storedetailBiz.sendStoreAlertMail();
			ajaxReturn(true,"发送预警邮件成功！");
		} catch (MessagingException e) {
			ajaxReturn(false,"构建预警邮件失败！");
			e.printStackTrace();
		}catch (ERPException e) {
			ajaxReturn(true,e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			ajaxReturn(true,"发送预警邮件失败！");
			e.printStackTrace();
		}
		
	}
}
