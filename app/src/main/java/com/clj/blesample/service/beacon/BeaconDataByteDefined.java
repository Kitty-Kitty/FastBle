package com.clj.blesample.service.beacon;


/**
 * 表示广播字节流数组的定义
 *
 * @author f
 * @version 1.0
 * @created 06-8月-2020 17:01:55
 */
public enum BeaconDataByteDefined {
    /**
     * 表示广播设备类型
     */
    BEACON_TYPE(2, 1),
    /**
     * 公司编码数据
     */
    COMPANY_IDENTIFIER(0, 2),
    /**
     * 表示设备Beacon的UUID
     */
    //UUID(4, 16),
    /**
     * 表示设备主类型
     */
    MAJOR(4, 2),
    /**
     * 表示设备子类型
     */
    MINOR(6, 2),
    /**
     * 表示发送数据信号强度
     */
    RSSI(8, 1),
    /**
     * 表示设备名称
     */
    NAME(0, 5),
    /**
     * 表示是否为活动状态。true 表示未触发；false 表示当前处于触发状态；
     */
    ACTION(2, 1),
    /**
     * 表示传感器的方向数据
     */
    SENSOR_ORIENTATION(3, 1),
    /**
     * 表示当前的传感器时间，（单位：毫秒）
     */
    SENSOR_TIME_MILLIS(4, 4),
    /**
     * 加速计归一化
     */
    ACCELEROMETER_NORMALISE(2, 4),
    /**
     * 陀螺仪归一化
     */
    GYROSCOPE_NORMALISE(6, 4);
    /**
     * 表示字节流数组截取的起始位置
     */
    private int index;
    /**
     * 表示需要截取的数据长度
     */
    private int length;

    /**
     * @param index  表示字节流数组截取的起始位置
     * @param length 表示需要截取的数据长度
     */
    private BeaconDataByteDefined(int index, int length) {
        this.index = index;
        this.length = length;
    }

    /**
     * 表示字节流数组截取的起始位置
     */
    public int getIndex() {
        return index;
    }

    /**
     * 表示字节流数组截取的起始位置
     *
     * @param newVal
     */
    private void setIndex(int newVal) {
        index = newVal;
    }

    /**
     * 表示需要截取的数据长度
     */
    public int getLength() {
        return length;
    }

    /**
     * 表示需要截取的数据长度
     *
     * @param newVal
     */
    private void setLength(int newVal) {
        length = newVal;
    }
}