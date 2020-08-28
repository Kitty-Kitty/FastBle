package com.clj.blesample.service.beacon;


import android.support.annotation.NonNull;

import com.clj.blesample.service.ServiceLog;
import com.clj.blesample.service.utils.ByteUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 表示Beacon数据广播处理适配器
 *
 * @author f
 * @version 1.0
 * @created 06-8月-2020 11:18:44
 */
public class BeaconDataItemAdapter {

    /**
     * 保存数据处理封装对象
     */
    private List<BeaconDataProcessWrap> dataProcessWrapList = new ArrayList<BeaconDataProcessWrap>();

    public BeaconDataItemAdapter() {

    }

    public void finalize() throws Throwable {

    }

    /**
     * @param dataProcessWrapList 保存数据处理封装对象
     */
    public BeaconDataItemAdapter(@NonNull List<BeaconDataProcessWrap> dataProcessWrapList) {

    }

    /**
     * 保存数据处理封装对象
     */
    public List<BeaconDataProcessWrap> getdataProcessWrapList() {
        return dataProcessWrapList;
    }

    /**
     * 保存数据处理封装对象
     *
     * @param newVal
     */
    public void setdataProcessWrapList(@NonNull List<BeaconDataProcessWrap> newVal) {
        dataProcessWrapList = newVal;
    }

    /**
     * 功能：
     * 运行Adapter，对各个数据进行适配置处理
     * 返回：
     * true   :   表示匹配成功；
     * false   :   表示不匹配失败；
     *
     * @param itemList 表示当前的Beacon数据列表
     */
    public boolean run(@NonNull List<BeaconItem> itemList) {

        for (BeaconDataProcessWrap tmpWrap : getdataProcessWrapList()) {
            for (BeaconItem tmpItem : itemList) {
                if (isMatch(tmpWrap, tmpItem)) {
                    if (!tmpWrap.parse(tmpItem)) {
                        ServiceLog.w("Process [%s] failed!", tmpWrap.getDataStructures().getDescription());
                        return false;
                    } else {
                        ServiceLog.i("Process [%s] succeed!", tmpWrap.getDataStructures().getDescription());
                        continue;
                    }
                }
            }
            ServiceLog.w("Process [%s] failed! not found BeaconItem...", tmpWrap.getDataStructures().getDescription());
        }
        return true;
    }

    /**
     * 功能：
     * 判断数据单元与对应的数据处理对象是否匹配
     * 返回：
     * true   :   表示匹配成功；
     * false   :   表示不匹配失败；
     *
     * @param dataProcessWrap 表示数据处理对象
     * @param item            表示数据内容单元
     */
    protected boolean isMatch(@NonNull BeaconDataProcessWrap dataProcessWrap, @NonNull BeaconItem item) {

        //运行时仍然需要判断数据的合法性
        if (null == dataProcessWrap || null == item) {
            return false;
        }

        return isMatch(dataProcessWrap.getDataStructures(), item);
    }

    /**
     * 功能：
     * 判断数据单元与对应的数据结构描述信息是否匹配
     * 返回：
     * true   :   表示匹配成功；
     * false   :   表示不匹配失败；
     *
     * @param dataStructures 表示数据结构描述
     * @param item           表示数据内容单元
     */
    protected static boolean isMatch(BeaconDataStructures dataStructures, BeaconItem item) {
        //运行时仍然需要判断数据的合法性
        if (null == dataStructures || null == item) {
            return false;
        }

        //优先校验数据长度与数据类型，如果出现任何一项不一样，则说明不匹配
        if (dataStructures.getLength() != item.len
                || dataStructures.getType() != item.type) {
            return false;
        }

        //如果存在默认的数据字符，则判断是否一致；如果不存在，则默认表示没有再需要判断的内容了，也不需要再继续判断了，默认已经符合要求，返回匹配成功；
        if (dataStructures.getValue().length <= 0) {
            return true;
        }

        //判断两个字节流是否相等
        return ByteUtils.equals(dataStructures.getValue(), item.bytes, dataStructures.getValue().length);
        /*
        //如果item 中保存的数据长度少，则明显不合适，返回失败；
        if (dataStructures.getValue().length > item.bytes.length) {
            return false;
        }

        //判断item的前面几个字节是否包含dataProcessWrap中的默认数据
        for (byte destByte : dataStructures.getValue()) {
            for (byte srcByte : item.bytes) {
                if (destByte != srcByte) {
                    return false;
                }
            }
        }
        return true;
         */
    }

    /**
     * 功能：
     * 根据广播描述信息从列表中获取数据信息
     * 返回：
     * 返回列表中的数据对象
     * 错误则返回null
     *
     * @param dataStructures 表示当前需要获取的数据结构描述
     * @param beaconItemList 表示需要查找的广播数据列表
     */
    public static BeaconItem getItem(BeaconDataStructures dataStructures, List<BeaconItem> beaconItemList) {
        if (null == dataStructures || null == beaconItemList) {
            return null;
        }

        for (BeaconItem tmpItem : beaconItemList) {
            if (isMatch(dataStructures, tmpItem)) {
                return tmpItem;
            }
        }
        return null;
    }
}//end BeaconDataItemAdapter