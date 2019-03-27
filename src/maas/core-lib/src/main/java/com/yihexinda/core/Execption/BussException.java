package com.yihexinda.core.Execption;
/**
 * *
 * 数据层处理异常.
 *
 * @author
 */
public class BussException extends AbstractBussException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7498517390326327837L;

	public BussException(String exceptionInfo) {
		super(exceptionInfo);
	}
	/**
	 * The Constructor.
	 *
	 * @param exceptionCode the exception code
	 * @param exceptionInfo the exception info
	 */
	public BussException(String exceptionCode, String exceptionInfo ){
		super(exceptionCode,exceptionInfo);
	}
	
	/**
	 * The Constructor.
	 *
	 * @param exceptionCode the exception code
	 * @param exceptionInfo the exception info
	 * @param throwable the throwable
	 */
	public BussException(String exceptionCode, String exceptionInfo, Throwable throwable ){
		super(exceptionCode,exceptionInfo,throwable);
	}
   
}
