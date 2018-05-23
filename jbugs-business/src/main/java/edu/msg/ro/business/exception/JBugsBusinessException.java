package edu.msg.ro.business.exception;

/**
 * Base class for all business exceptions. TODO extend it
 * 
 * @author Andrei Floricel, msg systems ag
 *
 */
public class JBugsBusinessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2870772958151275815L;

	public JBugsBusinessException(final String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public JBugsBusinessException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JBugsBusinessException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public JBugsBusinessException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public JBugsBusinessException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
