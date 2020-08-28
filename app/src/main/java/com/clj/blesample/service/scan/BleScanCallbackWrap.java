package com.clj.blesample.service.scan;

import com.clj.fastble.callback.BleScanCallback;

/**
 * 对BleScanCallback的封装，包含BleSensor对象信息
 *
 * @author f
 * @version 1.0
 * @created 25-7月-2020 10:59:43
 */
public abstract class BleScanCallbackWrap extends BleScanCallback {

    /**
     * 表示需要回调的对象
     */
    private ScanServiceCallback scanServiceCallback;

    public BleScanCallbackWrap() {

    }

    /**
     * @param scanServiceCallback 表示需要回调的对象
     */
    public BleScanCallbackWrap(ScanServiceCallback scanServiceCallback) {
        this.scanServiceCallback = scanServiceCallback;
    }

    public void finalize() throws Throwable {

    }

    /**
     * 表示需要回调的对象
     */
    public ScanServiceCallback getScanServiceCallback() {
        return scanServiceCallback;
    }

    /**
     * 表示需要回调的对象
     *
     * @param newVal
     */
    public void setScanServiceCallback(ScanServiceCallback newVal) {
        scanServiceCallback = newVal;
    }
}//end BleScanCallbackWrap