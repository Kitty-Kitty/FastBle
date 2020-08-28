package com.clj.blesample.service;


/**
 * 表示传感器服务异常类，主要为了描述服务器的几个处理接口而定义。相对返回错误码，返回异常可以简化代码。
 *
 * @author f
 * @version 1.0
 * @updated 22-7月-2020 19:09:18
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = 1L;
    /**
     * 服务器异常码
     */
    private int serviceExceptionCode = 0;
    /**
     * 表示服务定义的错误码
     */
    private ServiceErrorCode errorCode;

    public ServiceException() {
        super();
    }

    public void finalize() throws Throwable {

    }

    /**
     * @param serviceExceptionCode 表示服务器自定义的异常错误码
     */
    public ServiceException(int serviceExceptionCode) {
        //super(getString(serviceExceptionCode));
        setServiceExceptionCode(serviceExceptionCode);
    }

    /**
     * @param exception 表示服务器异常描述信息
     */
    public ServiceException(String exception) {
        super(exception);
    }

    /**
     * @param errorCode 表示服务器异常描述信息
     */
    public ServiceException(ServiceErrorCode errorCode) {
        super(errorCode.getDescription());
        setErrorCode(errorCode);
    }

    /**
     * 服务器异常码
     */
    public int getServiceExceptionCode() {
        return serviceExceptionCode;
    }

    /**
     * 服务器异常码
     *
     * @param newVal
     */
    private void setServiceExceptionCode(int newVal) {
        serviceExceptionCode = newVal;
    }

    /**
     * 表示服务定义的错误码
     */
    public ServiceErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * 表示服务定义的错误码
     *
     * @param newVal
     */
    protected void setErrorCode(ServiceErrorCode newVal) {
        errorCode = newVal;
    }
}//end ServiceException//end SensorServiceException