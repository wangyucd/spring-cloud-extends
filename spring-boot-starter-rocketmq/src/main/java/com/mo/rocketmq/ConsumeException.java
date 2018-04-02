package com.mo.rocketmq;
public class ConsumeException extends RuntimeException {

	/**
	 * serialVersionUID:TODO
	 * 
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 5437682479042543998L;

	public ConsumeException(String message) {
		super(message);
	}

	public ConsumeException(Throwable cause) {
		super(cause);
	}

	public ConsumeException(String message, Throwable cause) {
		super(message, cause);
	}
}
