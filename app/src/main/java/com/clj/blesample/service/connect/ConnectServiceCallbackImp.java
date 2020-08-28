package com.clj.blesample.service.connect;


import com.clj.blesample.service.BleSensor;
import com.clj.blesample.service.ServiceException;

/**
 * 连接服务回调接口
 *
 * @author f
 * @version 1.0
 * @created 25-7月-2020 21:06:06
 */
public interface ConnectServiceCallbackImp {

    /**
     * 功能：
     * 成功启动连接回调函数
     * 返回：
     * 无
     */
    public void onStartConnect();

    /**
     * 功能：
     * 连接异常回调函数
     * 返回：
     * 无
     *
     * @param bleSensor 当前连接的目标BleSensor对象
     * @param exception 服务异常信息
     */
    public void onConnectFail(BleSensor bleSensor, ServiceException exception);

    /**
     * 功能：
     * 连接成功的回调函数
     * 返回：
     * 无
     *
     * @param bleSensor 当前连接的目标BleSensor对象
     */
    public void onConnectSuccess(BleSensor bleSensor);

    /**
     * 功能：
     * 断开连接时的回调函数
     * 返回：
     * 无
     *
     * @param bleSensor 当前连接的目标BleSensor对象
     */
    public void onDisConnected(BleSensor bleSensor);

}