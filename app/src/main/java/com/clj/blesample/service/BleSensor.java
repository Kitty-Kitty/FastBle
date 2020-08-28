package com.clj.blesample.service;

import android.bluetooth.BluetoothGatt;

import com.clj.fastble.data.BleDevice;

/**
 * 保存通信操作的BleSensor对象
 *
 * @author f
 * @version 1.0
 * @created 23-7月-2020 14:44:13
 */
public class BleSensor {

    /**
     * 表示保存Sensor对象
     */
    private Sensor sensor;
    /**
     * 表示搜索到的蓝牙设备对象
     */
    private BleDevice bleDevice;
    /**
     * 表示当前设备的GATT (Generic Attribute Profile) 内容
     */
    private BluetoothGatt gatt;

    public BleSensor() {

    }

    /**
     * @param sensor 表示传感器对应的Sensor信息对象
     */
    public BleSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public void finalize() throws Throwable {

    }

    /**
     * 表示搜索到的蓝牙设备对象
     */
    public BleDevice getBleDevice() {
        return this.bleDevice;
    }

    /**
     * 表示搜索到的蓝牙设备对象
     *
     * @param newVal
     */
    public void setBleDevice(BleDevice newVal) {
        this.bleDevice = newVal;
    }

    /**
     * 功能：
     * 判断是否已经连接
     * 返回：
     * true ： 表示已经连接；
     * false： 表示还没有连接；
     */
    public boolean isConnected() {
        return false;
    }

    /**
     * 功能：
     * 判断是否已经扫描存在
     * 返回：
     * true ： 表示扫描存在；
     * false： 表示没有扫描到，未发现；
     */
    public boolean isExisted() {
        if (null != getBleDevice()) {
            return true;
        }
        return false;
    }

    /**
     * 表示保存Sensor对象
     */
    public Sensor getSensor() {
        return this.sensor;
    }

    /**
     * 表示保存Sensor对象
     *
     * @param newVal
     */
    public void setSensor(Sensor newVal) {
        this.sensor = newVal;
    }

    /**
     * 表示当前设备的GATT (Generic Attribute Profile) 内容
     */
    public BluetoothGatt getGatt() {
        return this.gatt;
    }

    /**
     * 表示当前设备的GATT (Generic Attribute Profile) 内容
     *
     * @param newVal
     */
    public void setGatt(BluetoothGatt newVal) {
        this.gatt = newVal;
    }

    @Override
    public String toString() {
        return String.format("BleSensor[ %s | object: %s ]"
                , getSensor().getDevice().toString()
                , super.toString());
        //return "BleSensor[ " + getSensor().getDevice().toString() + "object:" + super.toString() + " ]";
    }
}//end BleSensor