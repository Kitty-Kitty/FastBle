package com.clj.blesample.service.connect;


import com.clj.blesample.service.BleSensor;
import com.clj.fastble.callback.BleGattCallback;

/**
 * 对BleConnectCallback的封装，包含BleSensor对象信息
 *
 * @author f
 * @version 1.0
 * @created 25-7月-2020 21:06:06
 */
public abstract class BleConnectCallbackWrap extends BleGattCallback {

    /**
     * 连接服务回调函数对象
     */
    private ConnectServiceCallback connectServiceCallback;
    /**
     * 表示当前操作的bleSensor对象
     */
    private BleSensor bleSensor;

    private BleConnectCallbackWrap() {

    }

    public void finalize() throws Throwable {

    }

    /**
     * @param bleSensor 表示当前操作的bleSensor对象
     * @param cc        连接服务回调函数对象
     */
    public BleConnectCallbackWrap(BleSensor bleSensor, ConnectServiceCallback cc) {
        this.bleSensor = bleSensor;
        this.connectServiceCallback = cc;
    }

    /**
     * 连接服务回调函数对象
     */
    public ConnectServiceCallback getConnectServiceCallback() {
        return connectServiceCallback;
    }

    /**
     * 连接服务回调函数对象
     *
     * @param newVal
     */
    public void setConnectServiceCallback(ConnectServiceCallback newVal) {
        connectServiceCallback = newVal;
    }

    /**
     * 表示当前操作的bleSensor对象
     */
    public BleSensor getBleSensor() {
        return bleSensor;
    }

    /**
     * 表示当前操作的bleSensor对象
     *
     * @param newVal
     */
    public void setBleSensor(BleSensor newVal) {
        bleSensor = newVal;
    }
}//end BleConnectCallbackWrap