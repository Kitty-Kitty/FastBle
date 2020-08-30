package com.clj.blesample.service.beacon;


/**
 * 表示传感器传输的Beacon信息结构
 *
 * @author f
 * @version 1.0
 * @created 03-8月-2020 18:16:06
 */
public enum BeaconDataStructures {
    /**
     * 描述广播属性信息
     */
    ADVERTISING_PROPERTIES((short) 0x02, (short) 0x01, new byte[]{0x04}, "Advertising properties"),
    /**
     * 描述制造商指定信息
     */
    MANUFACTURER_SPECIFIC_DATA((short) 0x0A, (short) 0xFF, new byte[]{0x59, 0x00}, "Manufacturer specific data"),
    /**
     * 表示附加的传感器主要信息
     */
    PRIMARY_SERVICE_DATA((short) 0x0B, (short) 0x16, new byte[]{0x01, (byte) 0xFF}, "Primary service data"),
    /**
     * 表示附加的传感器次要信息
     */
    SECONDARY_SERVICE_DATA((short) 0x0B, (short) 0x16, new byte[]{0x02, (byte) 0xFF}, "Secondary service data"),
    /**
     * 描述设备名称信息
     */
    LOCAL_NAME((short) 0x06, (short) 0x09, new byte[]{'N', 'o', 'B', 'e', 'a'}, "Short local device name.");
    /**
     * 表示数据长度
     */
    private short length;
    /**
     * 表示数据类型
     */
    private short type;
    /**
     * 保存默认的字节数据
     */
    private byte[] value;
    /**
     * 表示数据描述信息
     */
    private String description;

    /**
     * @param len         表示数据长度
     * @param type        表示数据类型
     * @param description 表示数据描述信息
     */
    private BeaconDataStructures(short len, short type, byte[] value, String description) {
        this.length = len;
        this.type = type;
        this.value = value.clone();
        this.description = description;
    }

    /**
     * 表示数据长度
     */
    public short getLength() {
        return length;
    }

    /**
     * 表示数据长度
     *
     * @param newVal
     */
    public void setLength(short newVal) {
        length = newVal;
    }

    /**
     * 表示数据类型
     */
    public short getType() {
        return type;
    }

    /**
     * 表示数据类型
     *
     * @param newVal
     */
    public void setType(short newVal) {
        type = newVal;
    }

    /**
     * 表示数据描述信息
     */
    public String getDescription() {
        return description;
    }

    /**
     * 表示数据描述信息
     *
     * @param newVal
     */
    public void setDescription(String newVal) {
        description = newVal;
    }

    /**
     * 保存默认的字节数据
     */
    public byte[] getValue() {
        return value;
    }

    /**
     * 保存默认的字节数据
     *
     * @param newVal
     */
    private void setValue(byte[] newVal) {
        value = newVal;
    }
}