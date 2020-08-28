package com.clj.blesample.service.beans;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 描述BleCharacteristic的配置信息，主要描述Service UUID 与 Notify UUID信息
 *
 * @author f
 * @version 1.0
 * @created 25-7月-2020 17:22:39
 */
public class BleCharacteristicConfiguration {

    /**
     * 描述读取蓝牙设备的Service UUID地址
     */
    @JacksonXmlProperty(localName = "serviceUUID")
    private String serviceUUID;
    /**
     * 描述读取蓝牙设备的Notify UUID地址
     */
    @JacksonXmlProperty(localName = "notifyUUID")
    private String notifyUUID;

    public BleCharacteristicConfiguration() {

    }

    public void finalize() throws Throwable {

    }

    /**
     * 描述读取蓝牙设备的Service UUID地址
     */
    public String getServiceUUID() {
        return serviceUUID;
    }

    /**
     * 描述读取蓝牙设备的Service UUID地址
     *
     * @param newVal
     */
    public void setServiceUUID(String newVal) {
        serviceUUID = newVal;
    }

    /**
     * 描述读取蓝牙设备的Notify UUID地址
     */
    public String getNotifyUUID() {
        return notifyUUID;
    }

    /**
     * 描述读取蓝牙设备的Notify UUID地址
     *
     * @param newVal
     */
    public void setNotifyUUID(String newVal) {
        notifyUUID = newVal;
    }
}//end BleCharacteristicConfiguration