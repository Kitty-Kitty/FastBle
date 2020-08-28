package com.clj.blesample.service;


/**
 * 描述服务的错误码信息
 *
 * @author f
 * @version 1.0
 * @created 23-7月-2020 16:34:46
 */
public enum ServiceErrorCode {
    CHECK_SYSTEM_ENVIRONMENT("E001000001", "系统环境错误，蓝牙设备未打开！"),
    LOAD_CONFIGURATION_ERROR("e001000002", "加载配置文件错误！"),
    ARRAY_BYTE_LENGTH_ERROR("e001000003", "字节流的长度错误！"),
    ARRAY_BYTE_DEFINED_ERROR("e001000004", "字节流数据定义错误！");

    /**
     * 表示当前的错误码编号字符串
     */
    private String value;
    /**
     * 表示当前的错误详细的描述说明
     */
    private String description;

    /**
     * @param value       表示当前的错误码编号字符串
     * @param description 表示当前的错误详细的描述说明
     */
    private ServiceErrorCode(String value, String description) {
        this.setValue(value);
        this.setDescription(description);
    }

    /**
     * 表示当前的错误码编号字符串
     */
    public String getValue() {
        return value;
    }

    /**
     * 表示当前的错误码编号字符串
     *
     * @param newVal
     */
    private void setValue(String newVal) {
        value = newVal;
    }

    /**
     * 表示当前的错误详细的描述说明
     */
    public String getDescription() {
        return description;
    }

    /**
     * 表示当前的错误详细的描述说明
     *
     * @param newVal
     */
    private void setDescription(String newVal) {
        description = newVal;
    }

    /**
     * 功能：
     * 转化为错误描述字符串信息
     */
    @Override
    public String toString() {
        return "[" + this.value + "] " + this.description;
    }
}