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
     * 表示未知状态
     */
    private static final int STATE_UNKNOW = 0;
    /**
     * 表示被初始化状态
     */
    private static final int STATE_INITIALIZED = 1;
    /**
     * 表示启动状态
     */
    private static final int STATE_STARTED = 2;
    /**
     * 表示停止状态
     */
    private static final int STATE_STOPPED = 3;


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
     * 功能：
     * 表示重置服务状态
     * 返回：
     * 无
     */
    protected void resetState() {
        setState(STATE_UNKNOW);
    }

    /**
     * 表示初始化服务对象
     */
    public boolean initialize() {
        setInitialized();
        return true;
    }

    /**
     * 表示启动服务对象
     */
    public boolean start() {
        setStarted();
        return true;
    }

    /**
     * 表示停止服务对象
     */
    public boolean stop() {
        setStopped();
        return true;
    }

    /**
     * 功能：
     * 表示未初始化
     * 返回：
     * true  :  表示成功；
     * false  :  表示失败；
     */
    public boolean isUnInitialized() {
        return STATE_UNKNOW == getState();
    }

    /**
     * 功能：
     * 表示是否已经初始化
     * 返回：
     * true  :  表示成功；
     * false  :  表示失败；
     */
    public boolean isInitialized() {
        return STATE_INITIALIZED == getState();
    }

    /**
     * 功能：
     * 表示是否已经启动
     * 返回：
     * true  :  表示成功；
     * false  :  表示失败；
     */
    public boolean isStarted() {
        return STATE_STARTED == getState();
    }

    /**
     * 功能：
     * 表示是否已经停止
     * 返回：
     * true  :  表示成功；
     * false  :  表示失败；
     */
    public boolean isStopped() {
        return STATE_STOPPED == getState();
    }


    /**
     * 功能：
     * 表示设置成已经启动状态
     * 返回：
     * 无
     */
    protected void setStarted() {
        setState(STATE_STARTED);
    }

    /**
     * 功能：
     * 表示设置成已经停止状态
     * 返回：
     * 无
     */
    protected void setStopped() {
        setState(STATE_STOPPED);
    }

    /**
     * 功能：
     * 表示设置成已经被初始化状态
     * 返回：
     * 无
     */
    protected void setInitialized() {
        setState(STATE_INITIALIZED);
    }
}//end ServiceBase