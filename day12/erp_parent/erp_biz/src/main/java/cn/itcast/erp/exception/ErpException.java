package cn.itcast.erp.exception;

/**
 * 自定义异常
 * 对已知因为不符合业务逻辑时，抛出的异常，为了终于业务继续进行
 */
public class ErpException extends RuntimeException {
	private static final long serialVersionUID = 8914765131035633695L;

	public ErpException(String message){
		super(message);
	}
}
