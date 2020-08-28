package com.clj.blesample.service;


import com.clj.blesample.service.beans.Device;

/**
 * 表示传感器设备信息
 *
 * @author f
 * @version 1.0
 * @created 23-7月-2020 14:44:13
 */
public class Sensor {

    /**
     * sensor所包含的基础Device信息对象
     */
    private Device device;

    public Sensor() {

    }

    /**
     * @param device 当前传感器对象对应的设备配置信息
     */
    public Sensor(Device device) {
        this.device = device;
    }

    public void finalize() throws Throwable {

    }

    /**
     * sensor所包含的基础Device信息对象
     */
    public Device getDevice() {
        return device;
    }

    /**
     * sensor所包含的基础Device信息对象
     *
     * @param newVal
     */
    public void setDevice(Device newVal) {
        device = newVal;
    }
}//end Sensor