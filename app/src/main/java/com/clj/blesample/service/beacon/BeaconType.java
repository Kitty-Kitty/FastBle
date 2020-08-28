package com.clj.blesample.service.beacon;


/**
 * 表示蓝牙设备类型
 *
 * @author f
 * @version 1.0
 * @created 03-8月-2020 17:04:17
 */
public enum BeaconType {
    /**
     * 表示未定义类型
     */
    UNKNOW(0x00, "unknow"),
    /**
     * 表示Beacon设备类型
     */
    BEACON(0x02, "beacon");
    /**
     * 表示类型数值
     */
    private int value;
    /**
     * 表示类型描述信息
     */
    private String description;

    /**
     * @param value       表示类型数值
     * @param description 表示类型描述信息
     */
    private BeaconType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 表示类型数值
     */
    public int getValue() {
        return this.value;
    }

    /**
     * 表示类型数值
     *
     * @param newVal
     */
    private void setValue(int newVal) {
        value = newVal;
    }

    /**
     * 表示类型描述信息
     */
    public String getDescription() {
        return description;
    }

    /**
     * 表示类型描述信息
     *
     * @param newVal
     */
    private void setDescription(String newVal) {
        description = newVal;
    }

    /**
     * 功能：
     * 根据类型数值，获取转换后的BluetoothDeviceType对象
     * 返回：
     * BluetoothDeviceType对象
     *
     * @param value 表示类型数值
     */
    public BeaconType convert(int value) {
        switch (value) {
            case 0x02:
                return BEACON;
            default:
                return UNKNOW;
        }
    }
}