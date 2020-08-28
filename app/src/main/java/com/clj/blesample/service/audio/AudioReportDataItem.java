package com.clj.blesample.service.audio;

import com.clj.blesample.service.BleSensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 描述需要语音播报的信息
 *
 * @author f
 * @version 1.0
 * @created 11-8月-2020 18:40:13
 */
public class AudioReportDataItem {

    /**
     * 表示当前需要播报的设备列表
     */
    private List<BleSensor> bleSensores = new ArrayList<BleSensor>();
    /**
     * 表示当时的播报时间
     */
    private long reportTimeMillis;
    /**
     * 表示创建播报数据对象的时间
     */
    private long timeMillis = System.currentTimeMillis();

    public AudioReportDataItem() {

    }

    public void finalize() throws Throwable {

    }

    /**
     * 表示当前需要播报的设备列表
     */
    public List<BleSensor> getBleSensores() {
        return bleSensores;
    }

    /**
     * 表示当前需要播报的设备列表
     *
     * @param newVal
     */
    public void setBleSensores(List<BleSensor> newVal) {
        bleSensores = newVal;
    }

    /**
     * 表示创建播报数据对象的时间
     */
    public long getTimeMillis() {
        return timeMillis;
    }

    /**
     * 表示创建播报数据对象的时间
     *
     * @param newVal
     */
    protected void setTimeMillis(long newVal) {
        timeMillis = newVal;
    }

    /**
     * 表示当时的播报时间
     */
    public long getReportTimeMillis() {
        return reportTimeMillis;
    }

    /**
     * 表示当时的播报时间
     *
     * @param newVal
     */
    public void setReportTimeMillis(long newVal) {
        reportTimeMillis = newVal;
    }

    /**
     * 功能：
     * 组织需要演讲的文本字符信息
     * 返回：
     * 返回需要演讲的字符串
     */
    public String speechText() {
        String tmpText = "";

        List<String> tmpTextList = new ArrayList<String>();
        for (BleSensor item : getBleSensores()) {
            tmpTextList.add(item.getSensor().getDevice().getName());
        }
        //对演讲字符串进行排序
        Collections.sort(tmpTextList, new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {

                // 升序
                if (str1.length() == str2.length()) {
                    return str1.compareToIgnoreCase(str2);
                } else if (str1.length() > str2.length()) {
                    return 1;
                } else if (str1.length() < str2.length()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        for (int i = 0; i < tmpTextList.size(); i++) {

            if (0 == i) {
                tmpText += tmpTextList.get(i);
            } else {
                tmpText += " " + tmpTextList.get(i);
            }
        }

        /*for (BleSensor item : getBleSensores()) {
            tmpText += item.getSensor().getDevice().getName() + " ";
        }*/
        /*for (String tmpString : tmpTextList) {
            tmpText += tmpString + "";
        }*/
        return tmpText;
    }
}//end AudioReportDataItem