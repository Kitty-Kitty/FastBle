package com.clj.blesample.service.beans;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 主要描述蓝牙相关的配置项
 *
 * @author f
 * @version 1.0
 * @created 23-7月-2020 18:31:58
 */
public class BleConfiguration {

    /**
     * 表示ble manager处理时，是否打开日志功能
     */
    @JacksonXmlProperty(localName = "enableLog")
    private boolean enableLog = true;
    /**
     * 表示重新连接的尝试次数，0表示不尝试
     */
    @JacksonXmlProperty(localName = "reconnectCount")
    private int reconnectCount = 3;
    /**
     * 表示重新连接时的时间间隔，其他0表示没有间隔（单位：毫秒）
     */
    @JacksonXmlProperty(localName = "reconnectInterval")
    private int reconnectInterval = 3000;
    /**
     * 表示连接超时时间（单位：毫秒）
     */
    @JacksonXmlProperty(localName = "connectOvertime")
    private int connectOvertime = 20000;
    /**
     * 数据处理超时时间（单位：毫秒）
     */
    @JacksonXmlProperty(localName = "operateTimeout")
    private int operateTimeout = 5000;

    public BleConfiguration() {

    }

    public void finalize() throws Throwable {

    }

    /**
     * 表示重新连接时的时间间隔，其他0表示没有间隔（单位：毫秒）
     */
    public int getReconnectInterval() {
        return reconnectInterval;
    }

    /**
     * 表示重新连接时的时间间隔，其他0表示没有间隔（单位：毫秒）
     *
     * @param newVal
     */
    public void setReconnectInterval(int newVal) {
        reconnectInterval = newVal;
    }

    /**
     * 表示重新连接的尝试次数，0表示不尝试
     */
    public int getReconnectCount() {
        return reconnectCount;
    }

    /**
     * 表示重新连接的尝试次数，0表示不尝试
     *
     * @param newVal
     */
    public void setReconnectCount(int newVal) {
        reconnectCount = newVal;
    }

    /**
     * 数据处理超时时间（单位：毫秒）
     */
    public int getOperateTimeout() {
        return operateTimeout;
    }

    /**
     * 数据处理超时时间（单位：毫秒）
     *
     * @param newVal
     */
    public void setOperateTimeout(int newVal) {
        operateTimeout = newVal;
    }

    /**
     * 表示ble manager处理时，是否打开日志功能
     */
    public boolean isEnableLog() {
        return enableLog;
    }

    /**
     * 表示ble manager处理时，是否打开日志功能
     *
     * @param newVal
     */
    public void setEnableLog(boolean newVal) {
        enableLog = newVal;
    }

    /**
     * 表示连接超时时间（单位：毫秒）
     */
    public int getConnectOvertime() {
        return connectOvertime;
    }

    /**
     * 表示连接超时时间（单位：毫秒）
     *
     * @param newVal
     */
    public void setConnectOvertime(int newVal) {
        connectOvertime = newVal;
    }

    /**
     * Getter for property 'bleScanConfiguration'.
     *
     * @return Value for property 'bleScanConfiguration'.
     */
    public BleScanRuleConfiguration getBleScanConfiguration() {
        return bleScanConfiguration;
    }

    /**
     * Setter for property 'bleScanConfiguration'.
     *
     * @param bsc Value to set for property 'bleScanConfiguration'.
     */
    public void setBleScanConfiguration(BleScanRuleConfiguration bsc) {
        this.bleScanConfiguration = bsc;
    }

    @JacksonXmlProperty(localName = "scanConfiguration")
    private BleScanRuleConfiguration bleScanConfiguration = new BleScanRuleConfiguration();

    /**
     * 描述BleCharacteristic的配置信息，主要描述Service UUID 与 Notify UUID信息
     */
    public BleCharacteristicConfiguration getCharacteristicConfiguration() {
        return characteristicConfiguration;
    }

    /**
     * 描述BleCharacteristic的配置信息，主要描述Service UUID 与 Notify UUID信息
     *
     * @param newVal
     */
    public void setCharacteristicConfiguration(BleCharacteristicConfiguration newVal) {
        this.characteristicConfiguration = newVal;
    }

    /**
     * 描述BleCharacteristic的配置信息，主要描述Service UUID 与 Notify UUID信息
     */
    @JacksonXmlProperty(localName = "characteristicConfiguration")
    private BleCharacteristicConfiguration characteristicConfiguration = new BleCharacteristicConfiguration();
}//end BleConfiguration