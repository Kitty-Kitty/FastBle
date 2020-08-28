package com.clj.blesample.service.beacon;


import com.clj.blesample.service.ServiceErrorCode;
import com.clj.blesample.service.ServiceException;
import com.clj.blesample.service.utils.ByteUtils;

/**
 * 主要对字节数组进行各种解析处理
 *
 * @author f
 * @version 1.0
 * @created 06-8月-2020 17:23:17
 */
public class BeaconDataByteArrayParser {

    public BeaconDataByteArrayParser() {

    }

    public void finalize() throws Throwable {

    }

    /**
     * 功能：
     * 从字节数组中获取指定位置的字节，并转化为int类型
     * 返回：
     * 转化后的int数值
     * 出现各种错误，则抛出异常
     *
     * @param src             表示需要被处理源字节数组对象
     * @param dataByteDefined 表示字节数组的截取位置信息
     */
    public static int toInteger(byte[] src, BeaconDataByteDefined dataByteDefined) throws ServiceException {
        if (src.length < dataByteDefined.getIndex() + dataByteDefined.getLength()) {
            throw new ServiceException(ServiceErrorCode.ARRAY_BYTE_LENGTH_ERROR);
        }
        switch (dataByteDefined.getLength()) {
            case 1:
                return ByteUtils.ubyteToInt(src[dataByteDefined.getIndex()]);
            case 2:
                return ByteUtils.bytes2ShortLittleEndian(src, dataByteDefined.getIndex());
            case 4:
                return ByteUtils.bytes2IntLittleEndian(src, dataByteDefined.getIndex());
            default:
                throw new ServiceException(ServiceErrorCode.ARRAY_BYTE_DEFINED_ERROR);
        }
        //return 0;
    }

    /**
     * 功能：
     *
     * @param src             表示需要被处理源字节数组对象
     * @param dataByteDefined 表示字节数组的截取位置信息
     */
    public static byte[] arrayCopy(byte[] src, BeaconDataByteDefined dataByteDefined) throws ServiceException {
        if (src.length < dataByteDefined.getIndex() + dataByteDefined.getLength()) {
            throw new ServiceException(ServiceErrorCode.ARRAY_BYTE_LENGTH_ERROR);
        }
        return ByteUtils.get(src, dataByteDefined.getIndex(), dataByteDefined.getLength());
    }
}//end BeaconDataByteArrayParser