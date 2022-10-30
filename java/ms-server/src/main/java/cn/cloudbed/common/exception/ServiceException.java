package cn.cloudbed.common.exception;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**细化运行时异常,自己定义一个业务异常*/
public class ServiceException extends RuntimeException {
	Logger logger= LogManager.getLogger(this.getClass().getSimpleName());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceException() {
		super();
	}
	public ServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ServiceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
		logger.error("执行失败",cause);
	}

}
