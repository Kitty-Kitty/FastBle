package com.clj.blesample.service.beacon;


import com.clj.blesample.service.ServiceException;
import com.clj.blesample.service.ServiceLog;

import java.util.List;
import java.util.UUID;

/**
 * 表示接收到的数据内容
 *
 * @author f
 * @version 1.0
 * @created 03-8月-2020 17:04:14
 */
public class BeaconData {

    /**
     * 表示蓝牙设备类型
     */
    private BeaconType beaconType = BeaconType.UNKNOW;
    /**
     * 表示公司编码数据
     */
    private int companyIdentifier;
    /**
     * 表示设备Beacon的UUID
     */
    private UUID uuid;
    /**
     * 表示公司描述信息的长度
     */
    private int advDataLength;
    /**
     * 表示设备主类型
     */
    private int major;
    /**
     * 表示设备子类型
     */
    private int minor;
    /**
     * 表示发送数据信号强度
     */
    private int rssi;
    /**
     * 表示设备名称
     */
    private String name;

    public BeaconData() {

    }

    public void finalize() throws Throwable {

    }

    /**
     * 功能：
     * 表示信息字符流
     * 返回：
     * 无
     *
     * @param data 表示信息字符流
     */
    public BeaconData(byte[] data) {

    }

    /**
     * 表示公司编码数据
     */
    public int getCompanyIdentifier() {
        return companyIdentifier;
    }

    /**
     * 表示公司编码数据
     *
     * @param newVal
     */
    public void setCompanyIdentifier(int newVal) {
        companyIdentifier = newVal;
    }

    /**
     * 表示设备Beacon的UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * 表示设备Beacon的UUID
     *
     * @param newVal
     */
    public void setUuid(UUID newVal) {
        uuid = newVal;
    }

    /**
     * 表示公司描述信息的长度
     */
    public int getAdvDataLength() {
        return advDataLength;
    }

    /**
     * 表示公司描述信息的长度
     *
     * @param newVal
     */
    public void setAdvDataLength(int newVal) {
        advDataLength = newVal;
    }

    /**
     * 表示设备主类型
     */
    public int getMajor() {
        return major;
    }

    /**
     * 表示设备主类型
     *
     * @param newVal
     */
    public void setMajor(int newVal) {
        major = newVal;
    }

    /**
     * 表示设备子类型
     */
    public int getMinor() {
        return minor;
    }

    /**
     * 表示设备子类型
     *
     * @param newVal
     */
    public void setMinor(int newVal) {
        minor = newVal;
    }

    /**
     * 表示发送数据信号强度
     */
    public int getRssi() {
        return rssi;
    }

    /**
     * 表示发送数据信号强度
     *
     * @param newVal
     */
    public void setRssi(int newVal) {
        rssi = newVal;
    }

    /**
     * 表示设备名称
     */
    public String getName() {
        return name;
    }

    /**
     * 表示设备名称
     *
     * @param newVal
     */
    public void setName(String newVal) {
        name = newVal;
    }

    /**
     * 功能：
     * 表示根据信息字符流，设置BeaconData对象
     * 返回：
     * true  :  成功；
     * false  :  失败；
     *
     * @param data 表示信息字符流
     */
    public boolean parse(byte[] data) {
        return parse(BeaconParser.parseBeacon(data));
    }

    /**
     * 功能：
     * 根据属性数据列表信息解析设置BeaconData对象
     * 返回：
     * true  :  成功；
     * false  :  失败；
     *
     * @param itemList 表示属性数据列表
     */
    public boolean parse(List<BeaconItem> itemList) {
        if (null == itemList || itemList.isEmpty()) {
            return false;
        }

        //处理厂商信息
        BeaconItem tmpItem = BeaconDataItemAdapter.getItem(BeaconDataStructures.MANUFACTURER_SPECIFIC_DATA, itemList);
        if (null == tmpItem || !parseManufacturerSpecificData(tmpItem)) {
            return false;
        }

        //处理设备名称
        //beaconDataItemAdapter.run(tmpBeaconItemList);
        tmpItem = BeaconDataItemAdapter.getItem(BeaconDataStructures.LOCAL_NAME, itemList);
        if (null == tmpItem || !parseLocalName(tmpItem)) {
            return false;
        }

        return true;
    }

    /**
     * 表示蓝牙设备类型
     */
    public BeaconType getBeaconType() {
        return beaconType;
    }

    /**
     * 表示蓝牙设备类型
     *
     * @param newVal
     */
    public void setBeaconType(BeaconType newVal) {
        beaconType = newVal;
    }

    /**
     * 功能：
     * 解析接收到产品信息
     * 返回：
     * true  :  成功；
     * false  :  失败；
     *
     * @param beaconItem 表示当前处理的BeaconItem单元
     */
    private boolean parseManufacturerSpecificData(BeaconItem beaconItem) {

        setAdvDataLength(beaconItem.len);

        //处理蓝牙设备类型
        try {
            int tmpBeaconType = BeaconDataByteArrayParser.toInteger(beaconItem.bytes, BeaconDataByteDefined.BEACON_TYPE);
            if (BeaconType.BEACON.getValue() != tmpBeaconType) {
                ServiceLog.e("Beacon type[%d] failed!", tmpBeaconType);
                return false;
            } else {
                setBeaconType(BeaconType.BEACON);
            }
        } catch (ServiceException e) {
            ServiceLog.e(e.getErrorCode());
        }

        //处理公司编码数据
        try {
            setCompanyIdentifier(BeaconDataByteArrayParser.toInteger(beaconItem.bytes, BeaconDataByteDefined.COMPANY_IDENTIFIER));
        } catch (ServiceException e) {
            ServiceLog.e(e.getErrorCode());
        }


        /*
        //处理设备Beacon的UUID
        try {
            //byte[] tmpUUID = BeaconDataByteArrayParser.arrayCopy(beaconItem.bytes, BeaconDataByteDefined.UUID);
            setUuid(UUID.nameUUIDFromBytes(BeaconDataByteArrayParser.arrayCopy(beaconItem.bytes, BeaconDataByteDefined.UUID)));
        } catch (ServiceException e) {
            ServiceLog.e(e.getErrorCode());
        }
        */

        //处理设备主类型
        try {
            setMajor(BeaconDataByteArrayParser.toInteger(beaconItem.bytes, BeaconDataByteDefined.MAJOR));
        } catch (ServiceException e) {
            ServiceLog.e(e.getErrorCode());
        }

        //处理设备子类型
        try {
            setMinor(BeaconDataByteArrayParser.toInteger(beaconItem.bytes, BeaconDataByteDefined.MINOR));
        } catch (ServiceException e) {
            ServiceLog.e(e.getErrorCode());
        }
        //处理发送数据信号强度
        try {
            setRssi(BeaconDataByteArrayParser.toInteger(beaconItem.bytes, BeaconDataByteDefined.RSSI));
        } catch (ServiceException e) {
            ServiceLog.e(e.getErrorCode());
        }

        return true;
    }

    /**
     * 功能：
     * 解析接收到产品名称
     * 返回：
     * true  :  成功；
     * false  :  失败；
     *
     * @param beaconItem 表示当前处理的BeaconItem单元
     */
    private boolean parseLocalName(BeaconItem beaconItem) {

        //处理设备名称
        try {
            setName(new String(BeaconDataByteArrayParser.arrayCopy(beaconItem.bytes, BeaconDataByteDefined.NAME)));
        } catch (ServiceException e) {
            ServiceLog.e(e.getErrorCode());
        }

        return true;
    }
}//end BeaconData