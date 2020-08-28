package com.clj.blesample.service;


/**
 * 这是所有服务的基础类，主要描述服务共同的状态等信息
 *
 * @author f
 * @version 1.0
 * @created 24-7月-2020 20:08:09
 */
public abstract class ServiceBase implements ServiceInterface {

    /**
     * 表示当前服务的状态码
     */
    private int state = STATE_UNKNOW;
    /**
     * 表示被初始化状态
     */
    private static final int STATE_INITIALIZE = 1;
    /**
     * 表示启动状态
     */
    private static final int STATE_START = 2;
    /**
     * 表示停止状态
     */
    private static final int STATE_STOP = 3;
    /**
     * 表示未知状态
     */
    private static final int STATE_UNKNOW = 0;

    public ServiceBase() {

    }

    public void finalize() throws Throwable {

    }

    /**
     * 表示当前服务的状态码
     */
    protected int getState() {
        return state;
    }

    /**
     * 表示当前服务的状态码
     *
     * @param newVal
     */
    protected void setState(int newVal) {
        state = newVal;
    }

    /**
     * 表示初始化服务对象
     */
    //public boolean initialize(){
    //	return false;
    //}

    /**
     * 表示启动服务对象
     */
    //public boolean start(){
    //	return false;
    //}

    /**
     * 表示停止服务对象
     */
    //public boolean stop(){
    //	return false;
    //}
}//end ServiceBase