package com.clj.blesample.service.scan;


import com.clj.blesample.service.BleSensor;

import java.util.List;

/**
 * 搜索服务回调函数
 *
 * @author f
 * @version 1.0
 * @updated 25-7月-2020 10:53:05
 */
public abstract class ScanServiceCallback implements ScanServiceCallbackImp {

    public ScanServiceCallback() {

    }

    public void finalize() throws Throwable {
        super.finalize();
    }

    /**
     * 功能：
     * 表示正常启动成功后调用
     * 返回：
     * 无
     *
     * @param success 表示是否成功
     */
    public void onScanStarted(boolean success) {

    }

    /**
     * 功能：
     * 当前已经搜索到一个设备
     * 返回：
     * 无
     *
     * @param bleSensorList 表示当前已经搜索到的BleSensor对象
     */
    public void onScanning(BleSensor bleSensorList) {

    }

    /**
     * 功能：
     * 表示本次搜索结束，返回搜索到的所有设备列表
     * 返回：
     * 无
     *
     * @param bleSensorList 表示本次搜索到的所有设备列表
     */
    public void onScanFinished(List<BleSensor> bleSensorList) {

    }

}//end ScanServiceCallback