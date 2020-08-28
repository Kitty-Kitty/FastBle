package com.clj.blesample.service.audio;


import android.app.Activity;

import com.clj.blesample.service.ServiceInterface;
import com.clj.blesample.service.ServiceLog;

import java.util.Comparator;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.gotev.speech.Logger;
import net.gotev.speech.Speech;

/**
 * 语音播放当前状态信息
 *
 * @author f
 * @version 1.0
 * @created 23-7月-2020 14:44:12
 */
public class AudioReportService implements ServiceInterface {
    /**
     * 表示最大堆中保存的元素最大数据
     */
    private static final int MAX_HEAP_SIZE = 64;
    /**
     * 表示语音播报的间隔时间
     */
    private static final int INTERVAL_TIME_MILLIS = 1000;
    /**
     * 表示相同的数据单元的语音播报的间隔时间
     */
    private static final int SAME_DATA_ITEM_AUDIO_REPORT_INTERVAL_TIME_MILLIS = 2000;

    /**
     * 表示当前处理的播报数据队列
     */
    private ConcurrentLinkedQueue<AudioReportDataItem> dataItemQueue = new ConcurrentLinkedQueue<AudioReportDataItem>();
    /**
     * 表示数据处理最大堆对象
     */
    private PriorityQueue<AudioReportDataItem> maxHeap;
    /**
     * 当前服务所处的活跃用户界面
     */
    private Activity activity;
    /**
     * 表示当前数据处理线程对象
     */
    private Thread thread;

    public AudioReportService() {

    }

    public void finalize() throws Throwable {

    }

    /**
     * 表示初始化服务对象
     */
    @Deprecated
    public boolean initialize() {

        return false;
    }

    /**
     * 表示初始化服务对象
     *
     * @param activity 当前活跃的用户界面对象
     * @return
     */
    public boolean initialize(Activity activity) {

        setActivity(activity);

        Logger.setLogLevel(Logger.LogLevel.DEBUG);

        //startActivity(new Intent("com.android.settings.TTS_SETTINGS"));

        Speech.init(activity);
        Speech.getInstance().setLocale(Locale.ENGLISH);

        //创建一个最大堆容器，用于处理同步数据对象
        maxHeap = new PriorityQueue<AudioReportDataItem>(MAX_HEAP_SIZE, new Comparator<AudioReportDataItem>() { //通过数据的接收时间，创建大顶堆
            @Override
            public int compare(AudioReportDataItem ad1, AudioReportDataItem ad2) {
                return (int) (ad2.getReportTimeMillis() - ad1.getReportTimeMillis());
            }
        });

        //创建一个音频处理线程
        setThread(new Thread(new Runnable() {
            //
            @Override
            public void run() {
                while (true) {
                    AudioReportDataItem item = getDataItemQueue().poll();

                    if (null != item) {
                        clearExpire();
                        if (processDataItem(item)) {
                            //Thread.sleep(INTERVAL_TIME_MILLIS);
                            //如果播放成功，则需要进行适当的间隔；避免所有声音混合在一起
                            try {
                                Thread.sleep(INTERVAL_TIME_MILLIS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }));
        ServiceLog.i("create thread[ %s ] succeed!", getThread().toString());

        //Speech.getInstance().say("系统正在初始化");
        return true;
    }

    /**
     * 表示启动服务对象
     */
    public boolean start() {
        //Speech.getInstance().say("A B C D E F G H I J K L M N");
        getThread().start();
        ServiceLog.i("start thread[ %s ] succeed!", getThread().toString());
        //Speech.getInstance().say("系统已经准备就绪，可以正常使用！");
        return true;
    }

    /**
     * 表示停止服务对象
     */
    public boolean stop() {
        getThread().interrupt();
        Speech.getInstance().shutdown();
        return true;
    }

    /**
     * 当前服务所处的程序上下文
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * 当前服务所处的程序上下文
     *
     * @param newVal
     */
    public void setActivity(Activity newVal) {
        activity = newVal;
    }

    /**
     * 表示当前处理的播报数据队列
     */
    public ConcurrentLinkedQueue<AudioReportDataItem> getDataItemQueue() {
        return dataItemQueue;
    }

    /**
     * 表示当前处理的播报数据队列
     *
     * @param newVal
     */
    public void setDataItemQueue(ConcurrentLinkedQueue<AudioReportDataItem> newVal) {
        dataItemQueue = newVal;
    }

    /**
     * 表示当前数据处理线程对象
     */
    protected Thread getThread() {
        return thread;
    }

    /**
     * 表示当前数据处理线程对象
     *
     * @param newVal
     */
    protected void setThread(Thread newVal) {
        thread = newVal;
    }

    /**
     * 表示数据处理最大堆对象
     */
    public PriorityQueue<AudioReportDataItem> getMaxHeap() {
        return maxHeap;
    }

    /**
     * 表示数据处理最大堆对象
     *
     * @param newVal
     */
    public void setMaxHeap(PriorityQueue<AudioReportDataItem> newVal) {
        maxHeap = newVal;
    }

    /**
     * 功能：
     * 将播报数据单元添加到处理队列中
     * 返回：
     * true  :  表示成功；
     * false  :  表示失败；
     *
     * @param dataItem 表示播报数据单元
     */
    public boolean addReport(AudioReportDataItem dataItem) {
        return getDataItemQueue().offer(dataItem);
    }

    /**
     * 功能：
     * 处理一个数据单元
     * 返回：
     * true  :  表示成功；
     * false  :  表示失败；
     *
     * @param dataItem 数据单元
     */
    private boolean processDataItem(AudioReportDataItem dataItem) {

        AudioReportDataItem tmpAudioReportDataItem = findFirstSameDataItem(dataItem);
        long tmpCurrentTimeMillis = System.currentTimeMillis();
        boolean tmpReturn = false;

        if (null != tmpAudioReportDataItem
                && Math.abs(tmpCurrentTimeMillis - tmpAudioReportDataItem.getReportTimeMillis())
                < SAME_DATA_ITEM_AUDIO_REPORT_INTERVAL_TIME_MILLIS) {
            //如果之前有相同的语音已经播放过了，则在一段时间内不用播放
            ServiceLog.w("report is expired!!! [%d ms] more than [%d ms]"
                    , Math.abs(tmpCurrentTimeMillis - tmpAudioReportDataItem.getReportTimeMillis())
                    , SAME_DATA_ITEM_AUDIO_REPORT_INTERVAL_TIME_MILLIS
            );
            return false;
        }

        tmpReturn = playAudioByDataItem(dataItem);
        if (tmpReturn) {
            dataItem.setReportTimeMillis(System.currentTimeMillis());
            getMaxHeap().add(dataItem);
        }
        return tmpReturn;
    }

    /**
     * 功能：
     * 表示根据数据单元播放语音
     * 返回：
     * true  :  表示成功；
     * false  :  表示失败；
     *
     * @param dataItem 数据单元
     */
    private boolean playAudioByDataItem(AudioReportDataItem dataItem) {
        String tmpSpeechText = dataItem.speechText();

        if (tmpSpeechText.isEmpty()) {
            return false;
        }
        Speech.getInstance().say(tmpSpeechText);
        ServiceLog.d("speech [%s] succeed!", tmpSpeechText);
        return true;
    }


    /**
     * 功能：
     * 判断当前最新数据对象与之前的数据对象是否是相同设备。
     * 返回：
     * true  :  表示同一个设备；
     * false  :  表示不同一个设备；
     *
     * @param newDataItem 表示当前最新的数据元素对象
     * @param oldDataItem 表示之前的数据元素对象
     */
    private boolean equalsDataItem(AudioReportDataItem newDataItem, AudioReportDataItem oldDataItem) {
        return newDataItem.getBleSensores().equals(oldDataItem.getBleSensores());
    }

    /**
     * 功能：
     * 从最大堆中查找第一个不一样的播放语音数据单元
     * 返回：
     * null  :  表示没有找到
     * 非null :  表示查找到
     *
     * @param dataItem 数据单元
     */
    private AudioReportDataItem findFirstDifferentDataItem(AudioReportDataItem dataItem) {

        for (AudioReportDataItem item : getMaxHeap()) {
            if (!equalsDataItem(dataItem, item)) {
                return item;
            }
        }

        return null;
    }

    /**
     * 功能：
     * 从最大堆中查找第一个相同的播放语音数据单元
     * 返回：
     * null  :  表示没有找到
     * 非null :  表示查找到
     *
     * @param dataItem 数据单元
     */
    private AudioReportDataItem findFirstSameDataItem(AudioReportDataItem dataItem) {

        for (AudioReportDataItem item : getMaxHeap()) {
            if (equalsDataItem(dataItem, item)) {
                return item;
            }
        }

        return null;
    }

    /**
     * 功能：
     * 清除最大堆中的所有过期节点
     * 返回：
     * 无
     */
    private void clearExpire() {
        if (getMaxHeap().size() >= MAX_HEAP_SIZE) {
            long tmpCurrentTimeMillis = System.currentTimeMillis();
            long tmpMaxExpire = MAX_HEAP_SIZE * INTERVAL_TIME_MILLIS;

            for (AudioReportDataItem item : getMaxHeap()) {
                if (Math.abs(tmpCurrentTimeMillis - item.getReportTimeMillis()) > tmpMaxExpire) {
                    getMaxHeap().remove(item);
                }
            }
        }
    }
}//end AudioReport