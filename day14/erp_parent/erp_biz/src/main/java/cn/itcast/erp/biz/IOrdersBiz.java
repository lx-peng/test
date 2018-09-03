package cn.itcast.erp.biz;
import java.io.OutputStream;

import cn.itcast.erp.entity.Orders;
/**
 * 订单业务逻辑层接口
 * @author Administrator
 *
 */
public interface IOrdersBiz extends IBaseBiz<Orders>{

	//采购订单审核业务
	void doCheck(Long uuid,Long empuuid);
	//采购订单审核业务
	void doStart(Long uuid,Long empuuid);
	//导出订单为excel文件
	public void exportById(OutputStream os,Long uuid) throws Exception;
}

