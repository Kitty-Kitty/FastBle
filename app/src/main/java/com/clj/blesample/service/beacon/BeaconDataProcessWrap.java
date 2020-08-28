package com.clj.blesample.service.beacon;


/**
 * 表示数据处理包，主要保存数据与对应处理函数的关系
 *
 * @author f
 * @version 1.0
 * @created 06-8月-2020 11:07:18
 */
public abstract class BeaconDataProcessWrap {

    /**
     * 表示传感器传输的Beacon信息结构
     */
    private BeaconDataStructures dataStructures;

    public BeaconDataProcessWrap() {

    }


    /**
     * @param dataStructures Beacon广播数据处理对象对应的数据结构体
     */
    public BeaconDataProcessWrap(BeaconDataStructures dataStructures) {

    }

    public void finalize() throws Throwable {

    }

    /**
     * 表示传感器传输的Beacon信息结构
     */
    public BeaconDataStructures getDataStructures() {
        return dataStructures;
    }

    /**
     * 表示传感器传输的Beacon信息结构
     *
     * @param newVal
     */
    public void setDataStructures(BeaconDataStructures newVal) {
        dataStructures = newVal;
    }

    /**
     * 功能：
     * 数据处理接口函数
     * 返回：
     * true  :  成功；
     * false  :  失败；
     *
     * @param beaconItem 表示当前处理的BeaconItem单元
     */
    public abstract boolean parse(BeaconItem beaconItem);
}//end BeaconDataProcessWrap