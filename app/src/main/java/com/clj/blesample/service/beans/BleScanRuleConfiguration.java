package com.clj.blesample.service.beans;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 描述设备扫描规则
 *
 * @author f
 * @version 1.0
 * @created 24-7月-2020 18:15:13
 */
public class BleScanRuleConfiguration {

    /**
     * 扫描指定的服务的设备，可选
     */
    @JacksonXmlProperty(localName = "serviceUUID")
    private String serviceUUID;
    /**
     * 扫描指定广播名的设备，可选
     */
    @JacksonXmlProperty(localName = "deviceName")
    private String deviceName;
    /**
     * 扫描指定mac的设备，可选
     */
    @JacksonXmlProperty(localName = "deviceMac")
    private String deviceMac;
    /**
     * 连接时的autoConnect参数，可选，默认false
     */
    @JacksonXmlProperty(localName = "autoConnect")
    private boolean autoConnect;
    /**
     * 扫描超时时间，可选，默认10秒
     */
    @JacksonXmlProperty(localName = "scanTimeOut")
    private int scanTimeOut = 10000;

    public BleScanRuleConfiguration() {

    }

    public void finalize() throws Throwable {

    }

    /**
     * 扫描指定的服务的设备，可选
     */
    public String getServiceUUID() {
        return serviceUUID;
    }

    /**
     * 扫描指定的服务的设备，可选
     *
     * @param newVal
     */
    public void setServiceUUID(String newVal) {
        serviceUUID = newVal;
    }

    /**
     * 扫描指定广播名的设备，可选
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * 扫描指定广播名的设备，可选
     *
     * @param newVal
     */
    public void setDeviceName(String newVal) {
        deviceName = newVal;
    }

    /**
     * 扫描指定mac的设备，可选
     */
    public String getDeviceMac() {
        return deviceMac;
    }

    /**
     * 扫描指定mac的设备，可选
     *
     * @param newVal
     */
    public void setDeviceMac(String newVal) {
        deviceMac = newVal;
    }

    /**
     * 连接时的autoConnect参数，可选，默认false
     */
    public boolean isAutoConnect() {
        return autoConnect;
    }

    /**
     * 连接时的autoConnect参数，可选，默认false
     *
     * @param newVal
     */
    public void setAutoConnect(boolean newVal) {
        autoConnect = newVal;
    }

    /**
     * 扫描超时时间，可选，默认10秒
     */
    public int getScanTimeOut() {
        return scanTimeOut;
    }

    /**
     * 扫描超时时间，可选，默认10秒
     *
     * @param newVal
     */
    public void setScanTimeOut(int newVal) {
        scanTimeOut = newVal;
    }
}//end BleScanRuleConfiguration