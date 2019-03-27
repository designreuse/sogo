package com.yihexinda.core.Execption;

/**
 * *
 * 业务系统异常处理类（不能直接使用）.
 *
 * @author
 */
public abstract class AbstractBussException extends RuntimeException {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -3664225596755566456L;

    /**
     * The Constructor.
     *
     * @param exceptionInfo the exception code
     */
    public AbstractBussException(String exceptionInfo) {
        super(exceptionInfo);
        this.setExceptionInfo(exceptionInfo);
    }

    /**
     * The Constructor.
     *
     * @param exceptionCode the exception code
     * @param exceptionInfo the exception info
     */
    public AbstractBussException(String exceptionCode, String exceptionInfo) {
        super(exceptionInfo);
        this.setExceptionCode(exceptionCode);
        this.setExceptionInfo(exceptionInfo);
    }

    /**
     * The Constructor.
     *
     * @param exceptionCode the exception code
     * @param throwable     the throwable
     */
    public AbstractBussException(String exceptionCode, Throwable throwable) {
        super(exceptionCode, throwable);
        this.setExceptionCode(exceptionCode);
    }

    /**
     * The Constructor.
     *
     * @param exceptionCode the exception code
     * @param exceptionInfo the exception info
     * @param throwable     the throwable
     */
    public AbstractBussException(String exceptionCode, String exceptionInfo, Throwable throwable) {
        super(exceptionInfo, throwable);
        this.setExceptionCode(exceptionCode);
        this.setExceptionInfo(exceptionInfo);
    }

    /**
     * 错误代码.
     */
    private String exceptionCode;

    /**
     * 错误描述.
     */
    private String exceptionInfo;

    /**
     * Gets the * 错误代码.
     *
     * @return the * 错误代码
     */
    public String getExceptionCode() {
        return exceptionCode;
    }

    /**
     * Sets the * 错误代码.
     *
     * @param exceptionCode the new * 错误代码
     */
    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    /**
     * Gets the * 错误描述.
     *
     * @return the * 错误描述
     */
    public String getExceptionInfo() {
        return exceptionInfo;
    }

    /**
     * Sets the * 错误描述.
     *
     * @param exceptionInfo the new * 错误描述
     */
    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

}
