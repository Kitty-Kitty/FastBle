package com.clj.blesample.service;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.Message;

import com.clj.blesample.service.audio.AudioReportService;
import com.clj.blesample.service.beans.Configuration;
import com.clj.blesample.service.beans.Device;
import com.clj.blesample.service.show.ShowService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 传感器服务对象，是整个系统的主类
 *
 * @author f
 * @version 1.0
 * @updated 22 -7月-2020 18:29:26
 */
public class SensorService implements ServiceInterface {

    /**
     * 表示系统配置文件完整路径
     */
    private String configurationFilePath;
    /**
     * 表示当前从配置文件中获取的配置信息
     */
    private static Configuration configuration;
    /**
     * 表示当前系统的传感器列表
     */
    private static Map<String, Sensor> sensores = new ConcurrentHashMap<String, Sensor>();
    /**
     * 表示服务循环处理对象
     */
    private CycleService cycleService;
    /**
     * 语音播放对象
     */
    private AudioReportService audioReportService;
    /**
     * 显示设备列表对象
     */
    private ShowService showService;
    /**
     * 显示异常信息对象
     */
    private ShowException showException;
    /**
     * 当前服务所处的程序上下文
     */
    private Application context;
    /**
     * 当前服务所处的活跃用户界面
     */
    private Activity activity;
    /**
     * 表示当前是否在运行状态
     */
    private boolean running = false;

    private SensorService() {

    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static SensorService getInstance() {
        return SensorService.SensorServiceHolder.instanceSensorService;
    }

    private static class SensorServiceHolder {
        private static final SensorService instanceSensorService = new SensorService();
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
     * @param activity              当前活跃的用户界面对象
     * @param configurationFilePath 当前系统的配置文件
     * @return
     */
    public boolean initialize(Activity activity, String configurationFilePath) {

        CheckSystem tmpCheckSystem = new CheckSystem(activity);


        //使用CheckSystem类，实现系统环境的检测。主要实现针对蓝牙设备是否打开的检测
        if (!tmpCheckSystem.checkBluetoothDvice()) {
            //蓝牙设备出现异常
            ServiceLog.e(ServiceErrorCode.CHECK_SYSTEM_ENVIRONMENT);
            return false;
        }

        setActivity(activity);

        return initialize(activity.getApplication(), configurationFilePath);
    }

    /**
     * 表示初始化服务对象
     *
     * @param app
     * @param configurationFilePath
     * @return
     */
    private boolean initialize(Application app, String configurationFilePath) {
        setContext(app);
        setConfigurationFilePath(configurationFilePath);

        //从文件中获取配置项信息
        configuration = ConfigurationLoader.getConfiguration(getConfigurationFilePath());
        if (null == configuration) {
            ServiceLog.e(ServiceErrorCode.LOAD_CONFIGURATION_ERROR);
            return false;
        }

        //根据配置文件中的信息，创建对应的Sensor对象
        getSensores().clear();
        if (null != getConfiguration().getDevices()
                && !getConfiguration().getDevices().getDevicesList().isEmpty()) {
            for (Device tmpDevice : getConfiguration().getDevices().getDevicesList()) {
                getSensores().put(tmpDevice.getMac(), new Sensor(tmpDevice));
            }
        }

        //创建需要的几个基础对象

        //系统设备显示服务
        setShowService(new ShowService());
        if (!getShowService().initialize(getActivity())) {
            ServiceLog.e("ShowService initialize failed!");
            return false;
        } else {
            ServiceLog.i("ShowService initialize succeed!");
        }

        //系统语音播放服务
        setAudioReportService(new AudioReportService());
        if (!getAudioReportService().initialize(getActivity())) {
            ServiceLog.e("AudioReportService initialize failed!");
            return false;
        } else {
            ServiceLog.i("AudioReportService initialize succeed!");
        }

        //系统主要服务
        setCycleService(new CycleService());
        if (!getCycleService().initialize()) {
            ServiceLog.e("CycleService initialize failed!");
            return false;
        } else {
            ServiceLog.i("CycleService initialize succeed!");
        }

        setShowException(new ShowException());

        return true;
    }

    /**
     * 表示启动服务对象
     */
    public boolean start() {

        if (isRunning()) {
            stop();
        }

        //启动系统设备显示服务
        if (null == getShowService() || !getShowService().start()) {
            ServiceLog.e("ShowService start failed!");
            return false;
        } else {
            ServiceLog.i("ShowService start succeed!");
        }

        //启动系统语音服务
        if (null == getAudioReportService() || !getAudioReportService().start()) {
            ServiceLog.e("AudioReportService start failed!");
            return false;
        } else {
            ServiceLog.i("AudioReportService start succeed!");
        }

        //启动系统主要服务
        if (null == getCycleService() || !getCycleService().start()) {
            ServiceLog.e("CycleService start failed!");
            return false;
        } else {
            ServiceLog.i("CycleService start succeed!");
        }
        setRunning(true);

        return true;
    }

    /**
     * 表示停止服务对象
     */
    public boolean stop() {

        //停止系统主要服务
        if (null == getCycleService() || !getCycleService().stop()) {
            ServiceLog.e("CycleService stop failed!");
        } else {
            ServiceLog.i("CycleService stop succeed!");
        }

        //停止系统语音服务
        if (null == getAudioReportService() || !getAudioReportService().stop()) {
            ServiceLog.e("AudioReportService stop failed!");
        } else {
            ServiceLog.i("AudioReportService stop succeed!");
        }

        //停止系统设备显示服务
        if (null == getShowService() || !getShowService().stop()) {
            ServiceLog.e("ShowService stop failed!");
        } else {
            ServiceLog.i("ShowService stop succeed!");
        }

        setRunning(false);

        return true;
    }

    /**
     * 表示系统配置文件完整路径
     *
     * @return the string
     */
    public String getConfigurationFilePath() {
        return configurationFilePath;
    }

    /**
     * 表示系统配置文件完整路径
     *
     * @param newVal the new val
     */
    public void setConfigurationFilePath(String newVal) {
        configurationFilePath = newVal;
    }

    /**
     * 表示当前从配置文件中获取的配置信息
     *
     * @return the configuration
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * 表示当前从配置文件中获取的配置信息
     *
     * @param newVal the new val
     */
    protected void setConfiguration(Configuration newVal) {
        configuration = newVal;
    }

    /**
     * 表示当前系统的传感器列表
     *
     * @return the map
     */
    public Map<String, Sensor> getSensores() {
        return sensores;
    }

    /**
     * 表示当前系统的传感器列表
     *
     * @param newVal the new val
     */
    protected void setSensores(Map<String, Sensor> newVal) {
        sensores = newVal;
    }

    /**
     * 表示服务循环处理对象
     *
     * @return the cycle service
     */
    public CycleService getCycleService() {
        return cycleService;
    }

    /**
     * 表示服务循环处理对象
     *
     * @param newVal the new val
     */
    public void setCycleService(CycleService newVal) {
        cycleService = newVal;
    }

    /**
     * 语音播放对象
     */
    public AudioReportService getAudioReportService() {
        return audioReportService;
    }

    /**
     * 语音播放对象
     *
     * @param newVal
     */
    public void setAudioReportService(AudioReportService newVal) {
        audioReportService = newVal;
    }

    /**
     * 显示设备列表对象
     *
     * @return the show devices
     */
    public ShowService getShowService() {
        return showService;
    }

    /**
     * 显示设备列表对象
     *
     * @param newVal the new val
     */
    public void setShowService(ShowService newVal) {
        showService = newVal;
    }

    /**
     * 显示异常信息对象
     *
     * @return the show exception
     */
    public ShowException getShowException() {
        return showException;
    }

    /**
     * 显示异常信息对象
     *
     * @param newVal the new val
     */
    public void setShowException(ShowException newVal) {
        showException = newVal;
    }

    /**
     * 当前程序上下文
     *
     * @return the application
     */
    public Application getContext() {
        return context;
    }

    /**
     * 当前程序上下文
     *
     * @param newVal the new val
     */
    public void setContext(Application newVal) {
        context = newVal;
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
     * 表示当前是否在运行状态
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * 表示当前是否在运行状态
     *
     * @param newVal
     */
    protected void setRunning(boolean newVal) {
        running = newVal;
    }
}//end SensorService