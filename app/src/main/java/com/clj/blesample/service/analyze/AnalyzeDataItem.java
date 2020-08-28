package com.clj.blesample.service.analyze;

import com.clj.blesample.service.BleSensor;
import com.clj.blesample.service.SensorOrientation;
import com.clj.blesample.service.ServiceException;
import com.clj.blesample.service.ServiceLog;
import com.clj.blesample.service.beacon.BeaconData;
import com.clj.blesample.service.beacon.BeaconDataByteArrayParser;
import com.clj.blesample.service.beacon.BeaconDataByteDefined;
import com.clj.blesample.service.beacon.BeaconDataItemAdapter;
import com.clj.blesample.service.beacon.BeaconDataStructures;
import com.clj.blesample.service.beacon.BeaconItem;
import com.clj.blesample.service.beacon.BeaconParser;
import com.clj.blesample.service.utils.ByteUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 表示一个单一的分析数据单元
 *
 * @author f
 * @version 1.0
 * @created 01-8月-2020 11:32:15
 */
public class AnalyzeDataItem {

    /**
     * 保存通信操作的BleSensor对象
     */
    private BleSensor bleSensor;
    /**
     * <b>表示是否为活动状态。true 表示未触发；false 表示当前处于触发状态；
     */
    private boolean action;
    /**
     * 表示传感器的方向数据
     */
    private SensorOrientation sensorOrientation = SensorOrientation.UNKNOW;
    /**
     * 表示当前的传感器时间，（单位：毫秒）
     */
    private long sensorTimeMillis;
    /**
     * 加速计归一化
     */
    private long accelerometerNormalise;
    /**
     * 陀螺仪归一化
     */
    private long gyroscopeNormalise;
    /**
     * 表示Beacon的描述信息
     */
    private BeaconData beaconData = new BeaconData();
    /**
     * 表示数据单元的创建时间，用于数据的过期判断
     */
    private long createTimeMillis = System.currentTimeMillis();
    /**
     * 表示四元数数值中Q0 乘以 32767 所得到的结果
     */
    private short quaternionQ0;
    /**
     * 表示四元数数值中Q1 乘以 32767 所得到的结果
     */
    private short quaternionQ1;
    /**
     * 表示四元数数值中Q2 乘以 32767 所得到的结果
     */
    private short quaternionQ2;
    /**
     * 表示四元数数值中Q3 乘以 32767 所得到的结果
     */
    private short quaternionQ3;

    /**
     * 表示Beacon广播数据处理适配器
     */
    /*
    private BeaconDataItemAdapter beaconDataItemAdapter = new BeaconDataItemAdapter(
            new ArrayList<BeaconDataProcessWrap>() {{
                add(new BeaconDataProcessWrap(BeaconDataStructures.PRIMARY_SERVICE_DATA){
                    @Override
                    public boolean parse(BeaconItem beaconItem) {
                        return parseSensorPrimaryData(beaconItem);
                    }
                });
                add(new BeaconDataProcessWrap(BeaconDataStructures.SECONDARY_SERVICE_DATA){
                    @Override
                    public boolean parse(BeaconItem beaconItem) {
                        return parseSensorSecondaryData(beaconItem);
                    }
                });
            }
            }
    );
     */
    public AnalyzeDataItem() {

    }

    /**
     * 功能：
     * 根据Sensor对象创建AnalyzeDataItem对象
     *
     * @param bleSensor 表示当前数据对象所属的Sensor对象
     */
    public AnalyzeDataItem(BleSensor bleSensor) {
        setBleSensor(bleSensor);
    }

    public void finalize() throws Throwable {

    }

    /**
     * 保存通信操作的BleSensor对象
     */
    public BleSensor getBleSensor() {
        return bleSensor;
    }

    /**
     * 保存通信操作的BleSensor对象
     *
     * @param newVal
     */
    public void setBleSensor(BleSensor newVal) {
        bleSensor = newVal;
    }

    /**
     * <b>表示是否为活动状态。true 表示未触发；false 表示当前处于触发状态；</b>
     */
    public boolean isAction() {
        return action;
    }

    /**
     * <b>表示是否为活动状态。true 表示未触发；false 表示当前处于触发状态；</b>
     *
     * @param newVal
     */
    public void setAction(boolean newVal) {
        action = newVal;
    }

    /**
     * 表示传感器的方向数据
     */
    public SensorOrientation getSensorOrientation() {
        return sensorOrientation;
    }

    /**
     * 表示传感器的方向数据
     *
     * @param newVal
     */
    public void setSensorOrientation(SensorOrientation newVal) {
        sensorOrientation = newVal;
    }

    /**
     * 表示当前的传感器时间，（单位：毫秒）
     */
    public long getSensorTimeMillis() {
        return sensorTimeMillis;
    }

    /**
     * 表示当前的传感器时间，（单位：毫秒）
     *
     * @param newVal
     */
    public void setSensorTimeMillis(long newVal) {
        sensorTimeMillis = newVal;
    }

    /**
     * 加速计归一化
     */
    public long getAccelerometerNormalise() {
        return accelerometerNormalise;
    }

    /**
     * 加速计归一化
     *
     * @param newVal
     */
    public void setAccelerometerNormalise(long newVal) {
        accelerometerNormalise = newVal;
    }

    /**
     * 陀螺仪归一化
     */
    public long getGyroscopeNormalise() {
        return gyroscopeNormalise;
    }

    /**
     * 陀螺仪归一化
     *
     * @param newVal
     */
    public void setGyroscopeNormalise(long newVal) {
        gyroscopeNormalise = newVal;
    }

    /**
     * 表示Beacon的描述信息
     */
    public BeaconData getBeaconData() {
        return beaconData;
    }

    /**
     * 表示Beacon的描述信息
     *
     * @param newVal
     */
    public void setBeaconData(BeaconData newVal) {
        beaconData = newVal;
    }


    /**
     * 表示数据单元的创建时间，用于数据的过期判断
     */
    public long getCreateTimeMillis() {
        return createTimeMillis;
    }

    /**
     * 表示数据单元的创建时间，用于数据的过期判断
     *
     * @param newVal
     */
    protected void setCreateTimeMillis(long newVal) {
        createTimeMillis = newVal;
    }

    /**
     * 功能：
     * 解析接收到的字符流信息
     * 返回：
     * true  :  成功；
     * false  :  失败；
     *
     * @param data 表示接收的信息内容
     */
    public boolean parse(byte[] data) {

        List<BeaconItem> tmpBeaconItemList = BeaconParser.parseBeacon(data);

        if (null == tmpBeaconItemList || tmpBeaconItemList.isEmpty()) {
            return false;
        }
        //先解析BeaconData数据
        if (!getBeaconData().parse(tmpBeaconItemList)) {
            return false;
        }

        //处理主信息
        BeaconItem tmpItem = BeaconDataItemAdapter.getItem(BeaconDataStructures.PRIMARY_SERVICE_DATA, tmpBeaconItemList);
        if (null == tmpItem || !parseSensorPrimaryData(tmpItem)) {
            return false;
        }

        //处理子信息
        //beaconDataItemAdapter.run(tmpBeaconItemList);
        tmpItem = BeaconDataItemAdapter.getItem(BeaconDataStructures.SECONDARY_SERVICE_DATA, tmpBeaconItemList);
        if (null == tmpItem || !parseSensorSecondaryData(tmpItem)) {
            return false;
        }

        return true;
    }

    /**
     * 功能：
     * 解析接收到传感器主信息
     * 返回：
     * true  :  成功；
     * false  :  失败；
     *
     * @param beaconItem 表示当前处理的BeaconItem单元
     */
    private boolean parseSensorPrimaryData(BeaconItem beaconItem) {

        //处理是否为活动状态。true 表示未触发；false 表示当前处于触发状态；
        try {
            int tmpAction = BeaconDataByteArrayParser.toInteger(beaconItem.bytes, BeaconDataByteDefined.ACTION);

            if (0 == tmpAction) {
                setAction(false);
            } else {
                setAction(true);
            }
        } catch (ServiceException e) {
            ServiceLog.e(e.getErrorCode());
        }

        //处理公司编码数据
        try {
            setSensorOrientation(
                    Int2Orientation(BeaconDataByteArrayParser.toInteger(beaconItem.bytes, BeaconDataByteDefined.SENSOR_ORIENTATION)));
        } catch (ServiceException e) {
            ServiceLog.e(e.getErrorCode());
        }

        //处理当前的传感器时间，（单位：毫秒）
        try {
            setSensorTimeMillis(BeaconDataByteArrayParser.toInteger(beaconItem.bytes, BeaconDataByteDefined.SENSOR_TIME_MILLIS));
        } catch (ServiceException e) {
            ServiceLog.e(e.getErrorCode());
        }

        return true;
    }

    /**
     * 功能：
     * 解析接收到传感器子信息
     * 返回：
     * true  :  成功；
     * false  :  失败；
     *
     * @param beaconItem 表示当前处理的BeaconItem单元
     */
    private boolean parseSensorSecondaryData(BeaconItem beaconItem) {

        /*
        setQuaternionQ0(ByteUtils.bytes2ShortLittleEndian(beaconItem.bytes, BeaconDataByteDefined.ACCELEROMETER_NORMALISE.getIndex()));
        setQuaternionQ1(ByteUtils.bytes2ShortLittleEndian(beaconItem.bytes, BeaconDataByteDefined.ACCELEROMETER_NORMALISE.getIndex() + 2));
        setQuaternionQ2(ByteUtils.bytes2ShortLittleEndian(beaconItem.bytes, BeaconDataByteDefined.GYROSCOPE_NORMALISE.getIndex()));
        setQuaternionQ3(ByteUtils.bytes2ShortLittleEndian(beaconItem.bytes, BeaconDataByteDefined.GYROSCOPE_NORMALISE.getIndex() + 2));
       */
        //处理加速计归一化
        try {
            setAccelerometerNormalise(BeaconDataByteArrayParser.toInteger(beaconItem.bytes, BeaconDataByteDefined.ACCELEROMETER_NORMALISE));
        } catch (ServiceException e) {
            ServiceLog.e(e.getErrorCode());
        }

        //处理陀螺仪归一化
        try {
            setGyroscopeNormalise(BeaconDataByteArrayParser.toInteger(beaconItem.bytes, BeaconDataByteDefined.GYROSCOPE_NORMALISE));
        } catch (ServiceException e) {
            ServiceLog.e(e.getErrorCode());
        }

        return true;
    }

    /**
     * 功能：
     * 查找指定数值的方向对象
     * 返回：
     * 方向对象
     *
     * @param value 表示方向的数值
     */
    public static SensorOrientation Int2Orientation(int value) {
        switch (value) {
            case 1:
                return SensorOrientation.TOP;
            case 2:
                return SensorOrientation.BOTTOM;
            case 3:
                return SensorOrientation.LEFT;
            case 4:
                return SensorOrientation.RIGHT;
            case 5:
                return SensorOrientation.UP;
            case 6:
                return SensorOrientation.DOWN;
            default:
                return SensorOrientation.UNKNOW;
        }
    }

    /**
     * 功能：
     * 判断数据对象的方向是否向下。
     * 返回：
     * true  :  表示向下；
     * false  :  表示不是向下
     */
    public boolean isDown() {
        return (getSensorOrientation() == SensorOrientation.DOWN);
    }


    /**
     * 功能：
     * 转化为字符串信息
     * 返回：
     * 转化后的字符串
     */
    @Override
    public String toString() {
        return String.format("%s [action : %b | orientation : %s | sensorTimeMillis : %d | accelerometerNormalise : %d | gyroscopeNormalise : %d | TimestampNanos : %d]"
                , getBleSensor().toString()
                , isAction()
                , getSensorOrientation().toString()
                , getSensorTimeMillis()
                , getAccelerometerNormalise()
                , getGyroscopeNormalise()
                , getBleSensor().getBleDevice().getTimestampNanos()
        );

        /*
        return String.format("%s [action : %b | orientation : %s | sensorTimeMillis : %d | accelerometerNormalise : %d | gyroscopeNormalise : %d | q0 : %d | q1 : %d | q2 : %d | q3 : %d | TimestampNanos : %d]"
                , getBleSensor().toString()
                , isAction()
                , getSensorOrientation().toString()
                , getSensorTimeMillis()
                , getAccelerometerNormalise()
                , getGyroscopeNormalise()
                , getQuaternionQ0()
                , getQuaternionQ1()
                , getQuaternionQ2()
                , getQuaternionQ3()
                , getBleSensor().getBleDevice().getTimestampNanos()
        );
        */
    }


    /**
     * 表示四元数数值中Q0 乘以 32767 所得到的结果
     */
    public short getQuaternionQ0() {
        return quaternionQ0;
    }

    /**
     * 表示四元数数值中Q0 乘以 32767 所得到的结果
     *
     * @param newVal
     */
    public void setQuaternionQ0(short newVal) {
        quaternionQ0 = newVal;
    }

    /**
     * 表示四元数数值中Q1 乘以 32767 所得到的结果
     */
    public short getQuaternionQ1() {
        return quaternionQ1;
    }

    /**
     * 表示四元数数值中Q1 乘以 32767 所得到的结果
     *
     * @param newVal
     */
    public void setQuaternionQ1(short newVal) {
        quaternionQ1 = newVal;
    }

    /**
     * 表示四元数数值中Q2 乘以 32767 所得到的结果
     */
    public short getQuaternionQ2() {
        return quaternionQ2;
    }

    /**
     * 表示四元数数值中Q2 乘以 32767 所得到的结果
     *
     * @param newVal
     */
    public void setQuaternionQ2(short newVal) {
        quaternionQ2 = newVal;
    }

    /**
     * 表示四元数数值中Q3 乘以 32767 所得到的结果
     */
    public short getQuaternionQ3() {
        return quaternionQ3;
    }

    /**
     * 表示四元数数值中Q3 乘以 32767 所得到的结果
     *
     * @param newVal
     */
    public void setQuaternionQ3(short newVal) {
        quaternionQ3 = newVal;
    }
}//end AnalyzeDataItem