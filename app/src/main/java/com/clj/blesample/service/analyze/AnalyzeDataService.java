package com.clj.blesample.service.analyze;


import com.clj.blesample.service.SensorService;
import com.clj.blesample.service.ServiceInterface;
import com.clj.blesample.service.ServiceLog;
import com.clj.blesample.service.audio.AudioReportDataItem;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 处理接收到的所有数据，根据分析接收到的数据判断当前设备状态
 *
 * @author f
 * @version 1.0
 * @created 23-7月-2020 14:44:12
 */
public class AnalyzeDataService implements ServiceInterface {

    /**
     * 表示最大堆中保存的元素最大数据
     */
    private static final int MAX_HEAP_SIZE = 1024;
    /**
     * 表示超期时间（单位：毫秒）
     */
    private static final int EXPIRE_TIME_MILLIS = 1000;
    /**
     * 表示当前提交的数据单元对象保存队列
     */
    private ConcurrentLinkedQueue<AnalyzeDataItem> dataItemQueue = new ConcurrentLinkedQueue<AnalyzeDataItem>();
    /**
     * 表示当前数据处理线程对象
     */
    private Thread thread;
    /**
     * 用于数据处理的最大堆
     */
    private PriorityQueue<AnalyzeDataItem> maxHeap;

    /**
     * 表示用于判断设备同步的基准时间
     */
    private int syncTimeMillis = 100;
    /**
     * 表示判断相似的相似度数值
     */
    private float similarity = (float) 0.6;/*80%*/

    public AnalyzeDataService() {

        //创建一个最大堆容器，用于处理同步数据对象
        maxHeap = new PriorityQueue<AnalyzeDataItem>(MAX_HEAP_SIZE, new Comparator<AnalyzeDataItem>() { //通过数据的接收时间，创建大顶堆
            @Override
            public int compare(AnalyzeDataItem ad1, AnalyzeDataItem ad2) {
                return (int) (ad1.getBleSensor().getBleDevice().getTimestampNanos()
                        - ad2.getBleSensor().getBleDevice().getTimestampNanos());
            }
        });
    }


    /**
     * @param syncTimeMillis 表示用于判断设备同步的基准时间
     */
    public AnalyzeDataService(int syncTimeMillis) {

        //创建一个最大堆容器，用于处理同步数据对象
        maxHeap = new PriorityQueue<AnalyzeDataItem>(MAX_HEAP_SIZE, new Comparator<AnalyzeDataItem>() { //通过数据的接收时间，创建大顶堆
            @Override
            public int compare(AnalyzeDataItem ad1, AnalyzeDataItem ad2) {
                return (int) (ad2.getBleSensor().getBleDevice().getTimestampNanos()
                        - ad1.getBleSensor().getBleDevice().getTimestampNanos());
            }
        });

        //更新设备同步基准时间
        this.syncTimeMillis = syncTimeMillis;
    }

    public void finalize() throws Throwable {

    }

    /**
     * 表示初始化服务对象
     */
    public boolean initialize() {

        setThread(new Thread(new Runnable() {
            //
            @Override
            public void run() {
                while (true) {
                    AnalyzeDataItem item = getDataItemQueue().poll();

                    if (null != item) {
                        processDataItem(item);
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
        return true;
    }

    /**
     * 表示启动服务对象
     */
    public boolean start() {
        getThread().start();
        ServiceLog.i("start thread[ %s ] succeed!", getThread().toString());
        return true;
    }

    /**
     * 表示停止服务对象
     */
    public boolean stop() {
        getThread().interrupt();
        return true;
    }

    /**
     * 表示当前提交的数据单元对象保存队列
     */
    protected ConcurrentLinkedQueue<AnalyzeDataItem> getDataItemQueue() {
        return dataItemQueue;
    }

    /**
     * 表示当前提交的数据单元对象保存队列
     *
     * @param newVal
     */
    protected void setDataItemQueue(ConcurrentLinkedQueue<AnalyzeDataItem> newVal) {
        dataItemQueue = newVal;
    }

    /**
     * 功能：
     * 将数据单元添加到数据队列中
     * 返回：
     * true  :  表示成功；
     * false  :  表示失败；
     *
     * @param dataItem 表示一个单一的分析数据单元
     */
    public boolean addDataItem(AnalyzeDataItem dataItem) {
        return getDataItemQueue().offer(dataItem);
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
     * 功能：
     * 处理一个分析数据单元
     * 返回：
     * 无
     *
     * @param dataItem 数据单元
     */
    private void processDataItem(AnalyzeDataItem dataItem) {
        //System.out.println(dataItem.toString());
        //ServiceLog.d(dataItem.toString());

        if (!dataItem.isAction() || !dataItem.isDown()) {
            //如果不是活跃状态，不是朝下对象，则不做处理
            return;
        }

        ServiceLog.d(dataItem.toString());
        //先对队列数据进行校验处理
        AnalyzeDataItem tmpDataItem = getMaxHeap().peek();
        if (null == tmpDataItem) {
            getMaxHeap().add(dataItem);
            return;
        } else {
            if (isExpire(dataItem, tmpDataItem)) {
                getMaxHeap().clear();
                getMaxHeap().add(dataItem);
                return;
            }
        }

        //判断是否有同时移动的对象
        syncProcess(dataItem);
    }

    /**
     * 功能：
     * 分析一个数据单元是否同步
     * 返回：
     * 无
     *
     * @param dataItem 数据单元
     */
    private void syncProcess(AnalyzeDataItem dataItem) {
        for (AnalyzeDataItem tmpDataItem : getMaxHeap()) {
            //判断是否过期
            if (isExpire(dataItem, tmpDataItem)) {
                getMaxHeap().remove(tmpDataItem);
                ServiceLog.d("Data item is expire!!！");
                continue;
            }
            //判断两个设备匹配，则进行语音播放处理
            if (!isMatched(dataItem, tmpDataItem)) {
                ServiceLog.d("Data item mismatched!!！");
                continue;
            }

            //如果需要播报，则发送播报信息
            AudioReportDataItem tmpAudioReportDataItem = new AudioReportDataItem();

            tmpAudioReportDataItem.getBleSensores().add(dataItem.getBleSensor());
            tmpAudioReportDataItem.getBleSensores().add(tmpDataItem.getBleSensor());

            //将播报信息发送到播报服务
            SensorService.getInstance().getAudioReportService().addReport(tmpAudioReportDataItem);
            break;
        }
    }

    /**
     * 用于数据处理的最大堆
     */
    public PriorityQueue<AnalyzeDataItem> getMaxHeap() {
        return maxHeap;
    }

    /**
     * 用于数据处理的最大堆
     *
     * @param newVal
     */
    public void setMaxHeap(PriorityQueue<AnalyzeDataItem> newVal) {
        maxHeap = newVal;
    }

    /**
     * 表示用于判断设备同步的基准时间
     */
    public int getSyncTimeMillis() {
        return syncTimeMillis;
    }

    /**
     * 表示用于判断设备同步的基准时间
     *
     * @param newVal
     */
    public void setSyncTimeMillis(int newVal) {
        syncTimeMillis = newVal;
    }

    /**
     * 表示判断相似的相似度数值
     */
    public float getSimilarity() {
        return similarity;
    }

    /**
     * 表示判断相似的相似度数值
     *
     * @param newVal
     */
    public void setSimilarity(float newVal) {
        similarity = newVal;
    }

    /**
     * 功能：
     * 判断当前最新数据对象与之前的数据对象之间的时间差是否过期。如果过期返回true；否则返回false；
     * 返回：
     * true  :  表示过期；
     * false  :  表示不过期
     *
     * @param newDataItem 表示当前最新的数据元素对象
     * @param oldDataItem 表示之前的数据元素对象
     */
    private boolean isExpire(AnalyzeDataItem newDataItem, AnalyzeDataItem oldDataItem) {

        if (offsetTimeMillis(newDataItem, oldDataItem) > EXPIRE_TIME_MILLIS) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：
     * 判断当前最新数据对象与之前的数据对象之间的时间差是否在合法同步时间内。
     * 返回：
     * true  :  表示在合法同步时间内；
     * false  :  表示不合法同步时间内；
     *
     * @param newDataItem 表示当前最新的数据元素对象
     * @param oldDataItem 表示之前的数据元素对象
     */
    private boolean isValidSyncTime(AnalyzeDataItem newDataItem, AnalyzeDataItem oldDataItem) {
        if (offsetTimeMillis(newDataItem, oldDataItem) <= getSyncTimeMillis()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：
     * 判断当前最新数据对象与之前的数据对象之间的是否匹配。
     * 返回：
     * true  :  表示匹配；
     * false  :  表示不匹配；
     *
     * @param newDataItem 表示当前最新的数据元素对象
     * @param oldDataItem 表示之前的数据元素对象
     */
    private boolean isMatched(AnalyzeDataItem newDataItem, AnalyzeDataItem oldDataItem) {

        //判断是否在合法同步时间内
        if (!isValidSyncTime(newDataItem, oldDataItem)) {
            return false;
        }

        //判断是否为相同设备
        if (equalsDataItem(newDataItem, oldDataItem)) {
            return false;
        }

        //判断是否匹配相似度
        return isMatchedSimilarity(newDataItem, oldDataItem);
    }

    /**
     * 功能：
     * 判断当前最新数据对象与之前的数据对象之间是否匹配相似度。
     * 返回：
     * true  :  表示匹配；
     * false  :  表示不匹配；
     *
     * @param newDataItem 表示当前最新的数据元素对象
     * @param oldDataItem 表示之前的数据元素对象
     */
    private boolean isMatchedSimilarity(AnalyzeDataItem newDataItem, AnalyzeDataItem oldDataItem) {
        double tmpMin = 0;
        double tmpMax = 0;


        //比较accelerometerNormalise部分数据，如果差值在最小值的"similarity"倍，就认为是相似的
        tmpMin = Math.min(newDataItem.getAccelerometerNormalise(), oldDataItem.getAccelerometerNormalise());
        tmpMax = Math.max(newDataItem.getAccelerometerNormalise(), oldDataItem.getAccelerometerNormalise());
        if (tmpMin / tmpMax < getSimilarity()) {
            return false;
        }

        //比较gyroscopeNormalise部分数据，如果差值在最小值的"similarity"倍，就认为是相似的
        tmpMin = Math.min(newDataItem.getGyroscopeNormalise(), oldDataItem.getGyroscopeNormalise());
        tmpMax = Math.max(newDataItem.getGyroscopeNormalise(), oldDataItem.getGyroscopeNormalise());
        if (tmpMin / tmpMax < getSimilarity()) {
            return false;
        }

        return true;
    }

    /**
     * 功能：
     * 计算两个数据对象的时间差绝对值
     * 返回：
     * 返回两个数据对象的时间差绝对值数值；
     *
     * @param newDataItem 表示当前最新的数据元素对象
     * @param oldDataItem 表示之前的数据元素对象
     */
    private long offsetTimeMillis(AnalyzeDataItem newDataItem, AnalyzeDataItem oldDataItem) {
        return Math.abs(newDataItem.getCreateTimeMillis() - oldDataItem.getCreateTimeMillis());
    }

    /**
     * 功能：
     * 判断当前最新数据对象与之前的数据对象是否是同一个设备。
     * 返回：
     * true  :  表示同一个设备；
     * false  :  表示不同一个设备；
     *
     * @param newDataItem 表示当前最新的数据元素对象
     * @param oldDataItem 表示之前的数据元素对象
     */
    private boolean equalsDataItem(AnalyzeDataItem newDataItem, AnalyzeDataItem oldDataItem) {
        if (newDataItem.getBleSensor().equals(oldDataItem.getBleSensor())) {
            return true;
        }
        return false;
    }
}//end AnalyzeData