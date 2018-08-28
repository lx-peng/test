package cn.itcast.erp;
/**
 * 自定义异常，用于业务逻辑错误抛出
 * @author Administrator
 *
 */
public class ERPException extends RuntimeException {

	public ERPException(String message){
		super(message);
	}
}
