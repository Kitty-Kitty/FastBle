package com.clj.blesample.service.show;


import android.app.Activity;
import android.os.Handler;
import android.widget.LinearLayout;

import com.clj.blesample.R;
import com.clj.blesample.service.Sensor;
import com.clj.blesample.service.SensorService;
import com.clj.blesample.service.ServiceBase;
import com.clj.blesample.service.ServiceLog;
import com.clj.blesample.service.analyze.AnalyzeDataItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 显示需要处理的设备列表
 *
 * @author f
 * @version 1.0
 * @created 23-7月-2020 14:44:13
 */
public class ShowService extends ServiceBase {

    /**
     * 表示当前数据处理线程对象
     */
    private Thread thread;
    /**
     * 表示当前提交的数据单元对象保存队列
     */
    private ConcurrentLinkedQueue<AnalyzeDataItem> dataItemQueue = new ConcurrentLinkedQueue<AnalyzeDataItem>();
    /**
     * 表示纯属显示父对象
     */
    private LinearLayout sensor_layout;
    //private com.allen.library.SuperButton btn_down;
    //private com.allen.library.SuperButton btn_up;
    /**
     * 当前服务所处的活跃用户界面
     */
    private Activity activity;
    /**
     * 表示传感器对象对应的显示对象
     */
    private Map<String, com.allen.library.SuperButton> sensorViews = new HashMap<>();

    public ShowService() {

    }

    /**
     * @param ac 当前服务所处的活跃用户界面
     */
    public void ShowService(Activity ac) {
        this.activity = ac;
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

        initializeLayout();
        initializeViewBySensor();

        setThread(new Thread(new Runnable() {
            //
            @Override
            public void run() {
                while (isStarted()) {
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
        return super.initialize();

    }

    /**
     * 表示启动服务对象
     */
    public boolean start() {
        super.start();
        getThread().start();
        ServiceLog.i("start thread[ %s ] succeed!", getThread().toString());
        return super.start();
    }

    /**
     * 表示停止服务对象
     */
    public boolean stop() {
        super.stop();
        try {
            getThread().join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return super.stop();
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
     * 当前服务所处的活跃用户界面
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * 当前服务所处的活跃用户界面
     *
     * @param newVal
     */
    public void setActivity(Activity newVal) {
        activity = newVal;
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
     * 表示当前提交的数据单元对象保存队列
     */
    public ConcurrentLinkedQueue<AnalyzeDataItem> getDataItemQueue() {
        return dataItemQueue;
    }

    /**
     * 表示当前提交的数据单元对象保存队列
     *
     * @param newVal
     */
    public void setDataItemQueue(ConcurrentLinkedQueue<AnalyzeDataItem> newVal) {
        dataItemQueue = newVal;
    }

    /**
     * 表示传感器对象对应的显示对象
     */
    private Map<String, com.allen.library.SuperButton> getSensorViews() {
        return sensorViews;
    }

    /**
     * 表示传感器对象对应的显示对象
     *
     * @param newVal
     */
    private void setSensorViews(Map<String, com.allen.library.SuperButton> newVal) {
        sensorViews = newVal;
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

        /*if (!dataItem.isAction() || !dataItem.isDown()) {
            //如果不是活跃状态，不是朝下对象，则不做处理
            return;
        }*/

        com.allen.library.SuperButton tmpEntry = getSensorViews().get(dataItem.getBleSensor().getSensor().getDevice().getMac());
        if (null == tmpEntry) {
            return;
        }

        if (dataItem.isDown()) {
            tmpEntry.setShapeSolidColor(getActivity().getResources().getColor(R.color.btn8));
        } else {
            tmpEntry.setShapeSolidColor(getActivity().getResources().getColor(R.color.btn15));
        }
        //com.allen.library.SuperButton对象的postInvalidate()方法无效，需要在UI线程中使用.setUseShape()，使属性更新生效
        //tmpEntry.postInvalidate();

        ServiceLog.d(dataItem.toString());

    }

    /**
     * 功能：
     * 表示初始化线性布局对象
     * 返回：
     * 无
     */
    protected boolean initializeLayout() {
        sensor_layout = (LinearLayout) getActivity().findViewById(R.id.layout_sensors);
        sensor_layout.removeAllViews();
        //btn_down = (com.allen.library.SuperButton) getActivity().findViewById(R.id.btn_down);
        //btn_up = (com.allen.library.SuperButton) getActivity().findViewById(R.id.btn_up);
        return true;
    }

    /**
     * 功能：
     * 根据配置的传感器设备列表创建对应的UI显示对象
     * 返回：
     * 无
     */
    private boolean initializeViewBySensor() {

        //对演讲字符串进行排序
        Map<String, Sensor> tmpSortSensors = sortMapBySensorName(SensorService.getInstance().getSensores());

        for (Map.Entry<String, Sensor> tmpEntry : tmpSortSensors.entrySet()) {

            //CopyView(sensor_layout);
            //sensor_layout.cop
            com.allen.library.SuperButton tmpSensorView
                    = new com.allen.library.SuperButton(sensor_layout.getContext());

            tmpSensorView.setShapeType(com.allen.library.SuperButton.RECTANGLE)
                    .setShapeCornersRadius(6)
                    .setShapeSizeWidth(80)
                    .setShapeSizeHeight(120)
                    .setShapeSolidColor(getActivity().getResources().getColor(R.color.btn15))
                    .setShapeStrokeColor(getActivity().getResources().getColor(R.color.btn16))
                    .setShapeStrokeWidth(2)
                    .setShapeStrokeDashGap(5)
                    //.setShapeStrokeDashWidth(10)
                    .setTextGravity(com.allen.library.SuperButton.TEXT_GRAVITY_RIGHT)
                    .setShapeUseSelector(false)
                    .setShapeSelectorPressedColor(getActivity().getResources().getColor(R.color.black))
                    .setShapeSelectorNormalColor(getActivity().getResources().getColor(R.color.orange))
                    .setShapeSelectorDisableColor(getActivity().getResources().getColor(R.color.colorPrimary))
                    .setUseShape();

            tmpSensorView.setId(tmpEntry.getValue().hashCode());
            //tmpSensorView.setWidth(80);
            //tmpSensorView.setHeight(120);
            tmpSensorView.setText(tmpEntry.getValue().getDevice().getName());
            tmpSensorView.setTextColor(getActivity().getResources().getColor(android.R.color.black));
            tmpSensorView.setTextSize(30);
            tmpSensorView.setShadowLayer((float) 6, (float) 0.5, (float) 0.5, getActivity().getResources().getColor(R.color.line));

            getSensorViews().put(tmpEntry.getKey(), tmpSensorView);
            sensor_layout.addView(tmpSensorView);
        }
        return true;
    }

    /**
     * 根据传感器的Map创建一个根据Name排序的Map对象
     *
     * @param map 一个未排序的传感器列表
     * @return 依据Name排序后传感器列表
     */
    public static Map<String, Sensor> sortMapBySensorName(Map<String, Sensor> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, Sensor> tmpSortedMap = new LinkedHashMap<String, Sensor>();
        List<Map.Entry<String, Sensor>> tmpEntryList = new ArrayList<Map.Entry<String, Sensor>>(map.entrySet());

        Collections.sort(tmpEntryList, new Comparator<Map.Entry<String, Sensor>>() {
                    @Override
                    public int compare(Map.Entry<String, Sensor> entry1, Map.Entry<String, Sensor> entry2) {

                        String str1 = entry1.getValue().getDevice().getName();
                        String str2 = entry2.getValue().getDevice().getName();

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
                }
        );

        Iterator<Map.Entry<String, Sensor>> iter = tmpEntryList.iterator();
        Map.Entry<String, Sensor> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            tmpSortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return tmpSortedMap;
    }
}//end ShowDevices