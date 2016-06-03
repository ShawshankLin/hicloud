package cn.edu.cylg.cis.hicloud.core.exception;

/**
 * 自定义异常
 * @author zjz
 *
 */
public class AppException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AppException() {
		super();
	}
	
	public AppException(String message) {
		super(message);
	}
	
	public AppException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AppException(Throwable cause) {
		super(cause);
	}
}
