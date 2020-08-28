package com.clj.blesample.service;


/**
 * 表示传感器的方向数据
 *
 * @author f
 * @version 1.0
 * @created 01-8月-2020 10:43:56
 */
public enum SensorOrientation {
    /**
     * 表示未定义方向
     */
    UNKNOW(0, "unknow"),
    /**
     * 表示向前方向
     */
    TOP(1, "top"),
    /**
     * 表示向后方向
     */
    BOTTOM(2, "bottom"),
    /**
     * 表示向左方向
     */
    LEFT(3, "left"),
    /**
     * 表示向右方向
     */
    RIGHT(4, "right"),
    /**
     * 表示向上方向
     */
    UP(5, "up"),
    /**
     * 表示向下方向
     */
    DOWN(6, "down");

    /**
     * 表示当前方向对应的数值
     */
    private int value;
    /**
     * 表示当前方向的描述信息
     */
    private String description;

    /**
     * @param value       表示当前方向对应的数值
     * @param description 表示当前方向的描述信息
     */
    private SensorOrientation(int value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 表示当前方向对应的数值
     */
    public int getValue() {
        return value;
    }

    /**
     * 表示当前方向对应的数值
     *
     * @param newVal
     */
    private void setValue(int newVal) {
        value = newVal;
    }

    /**
     * 表示当前方向的描述信息
     */
    public String getDescription() {
        return description;
    }

    /**
     * 表示当前方向的描述信息
     *
     * @param newVal
     */
    private void setDescription(String newVal) {
        description = newVal;
    }

    /**
     * 功能：
     * 转化为字符串信息
     * 返回：
     * 转化后的字符串
     */
    @Override
    public String toString() {
        return String.format("%d : %s", getValue(), getDescription());
    }
}