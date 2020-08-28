package com.clj.blesample.service;


import com.clj.blesample.service.analyze.AnalyzeDataItem;
import com.clj.blesample.service.analyze.AnalyzeDataService;
import com.clj.blesample.service.beans.BleConfiguration;
import com.clj.blesample.service.connect.ConnectService;
import com.clj.blesample.service.connect.ConnectServiceCallback;
import com.clj.blesample.service.scan.ScanService;
import com.clj.blesample.service.scan.ScanServiceCallback;
import com.clj.fastble.BleManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 启动系统处理对象，主要根据配置文件中的蓝牙设备列表，依次连接各个设备，打开消息的通知接收服务。
 *
 * @author f
 * @version 1.0
 * @created 23-7月-2020 14:44:13
 */
public class CycleService implements ServiceInterface {

    /**
     * 处理接收到的所有数据，根据分析接收到的数据判断当前设备状态
     */
    private AnalyzeDataService analyzeDataService;
    /**
     * 蓝牙搜索设备管理对象
     */
    private ScanService scanService;
    /**
     * 连接所有设备
     */
    private ConnectService connectService;
    /**
     * 保存通信操作的BleSensor对象列表
     */
    private Map<String, BleSensor> bleSensores = new ConcurrentHashMap<String, BleSensor>();

    /**
     * 表示当前是否在运行状态
     */
    private boolean running = false;

    public CycleService() {

    }

    public void finalize() throws Throwable {
        stop();
    }

    /**
     * 表示初始化服务对象
     */
    public boolean initialize() {

        //对系统使用的BLE 管理进行初始化操作，主要实现基础参数的配置操作
        if (!initializeBleManager()) {
            ServiceLog.e("initialize BleManager failed!");
            return false;
        } else {
            ServiceLog.i("initialize BleManager succeed!");
        }

        //根据配置文件中设置的设备列表，创建对应的BleSensor对象
        if (!initializeBleSensores()) {
            ServiceLog.e("initialize BleSensores failed!");
            return false;
        } else {
            ServiceLog.i("initialize BleManager succeed!");
        }

        setAnalyzeDataService(new AnalyzeDataService());
        setScanService(new ScanService());
        setConnectService(new ConnectService());

        //初始化数据分析服务
        if (!initializationAnalyzeDataService()) {
            ServiceLog.e("initialize AnalyzeDataService failed!");
            return false;
        } else {
            ServiceLog.i("initialize AnalyzeDataService succeed!");
        }

        //初始化搜索服务
        if (!initializationScanService()) {
            ServiceLog.e("initialize ScanService failed!");
            return false;
        } else {
            ServiceLog.i("initialize ScanService succeed!");
        }

        //初始化连接服务
        /*
        if (!initializationConnectService()) {
            ServiceLog.e("initialize ConnectService failed!");
            return false;
        } else {
            ServiceLog.i("initialize ConnectService succeed!");
        }
        */

        return true;
    }

    /**
     * 表示启动服务对象
     */
    public boolean start() {

        //启动数据分析服务
        if (!startAnalyzeDataService()) {
            ServiceLog.e("start AnalyzeDataService failed!");
            return false;
        } else {
            ServiceLog.i("start AnalyzeDataService succeed!");
        }

        //启动搜索服务
        if (!startScanService()) {
            ServiceLog.e("start ScanService failed!");
            return false;
        } else {
            ServiceLog.i("start ScanService succeed!");
        }

        //启动连接服务
        /*
        if (!startConnectService()) {
            ServiceLog.e("start ConnectService failed!");
            return false;
        } else {
            ServiceLog.i("start ConnectService succeed!");
        }
        */

        return true;
    }

    /**
     * 表示停止服务对象
     */
    public boolean stop() {

        //停止连接服务
        /*
        if (!stopConnectService()) {
            ServiceLog.e("stop ConnectService failed!");
            //return false;
        } else {
            ServiceLog.i("stop ConnectService succeed!");
        }
         */

        //停止搜索服务
        if (!stopScanService()) {
            ServiceLog.e("stop ScanService failed!");
            //return false;
        } else {
            ServiceLog.i("stop ScanService succeed!");
        }

        //停止数据分析服务
        if (!stopAnalyzeDataService()) {
            ServiceLog.e("stop AnalyzeDataService failed!");
            //return false;
        } else {
            ServiceLog.i("stop AnalyzeDataService succeed!");
        }

        //停止搜索服务
        if (!stopBleManager()) {
            ServiceLog.e("stop BleManager failed!");
            //return false;
        } else {
            ServiceLog.i("stop BleManager succeed!");
        }

        return true;
    }

    /**
     * 处理接收到的所有数据，根据分析接收到的数据判断当前设备状态
     */
    public AnalyzeDataService getAnalyzeDataService() {
        return analyzeDataService;
    }

    /**
     * 处理接收到的所有数据，根据分析接收到的数据判断当前设备状态
     *
     * @param newVal
     */
    public void setAnalyzeDataService(AnalyzeDataService newVal) {
        analyzeDataService = newVal;
    }

    /**
     * 蓝牙搜索设备管理对象
     */
    public ScanService getScanService() {
        return scanService;
    }

    /**
     * 蓝牙搜索设备管理对象
     *
     * @param newVal
     */
    public void setScanService(ScanService newVal) {
        scanService = newVal;
    }

    /**
     * 连接所有设备
     */
    public ConnectService getConnectService() {
        return connectService;
    }

    /**
     * 连接所有设备
     *
     * @param newVal
     */
    public void setConnectService(ConnectService newVal) {
        connectService = newVal;
    }

    /**
     * 保存通信操作的BleSensor对象列表
     */
    public Map<String, BleSensor> getBleSensores() {
        return bleSensores;
    }

    /**
     * 保存通信操作的BleSensor对象列表
     *
     * @param newVal
     */
    public void setBleSensores(Map<String, BleSensor> newVal) {
        bleSensores = newVal;
    }

    /**
     * 功能：
     * 根据配置文件信息，初始化需要的设备列表
     * 其中初始化过程中，清除列表中原有的BleSensor对象
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     */
    protected boolean initializeBleSensores() {

        destroyBleSensores();

        for (Map.Entry<String, Sensor> entry
                : SensorService.getInstance().getSensores().entrySet()) {

            getBleSensores().put(entry.getKey(), new BleSensor(entry.getValue()));
        }

        return true;
    }

    /**
     * 功能：
     * 根据配置文件信息，初始化蓝牙管理模块
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     */
    protected boolean initializeBleManager() {
        /*SensorService.getInstance().getCycleService().getBleManager().init(SensorService.getInstance().getContext());
        SensorService.getInstance().getCycleService().getBleManager()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);*/

        //获取蓝牙配置信息并初始化蓝牙管理器
        BleConfiguration tmpBleConfig = SensorService.getInstance().getConfiguration().getBleConfiguration();

        if (null == tmpBleConfig) {
            return false;
        }

        BleManager.getInstance().init(SensorService.getInstance().getActivity().getApplication());
        BleManager.getInstance()
                .enableLog(tmpBleConfig.isEnableLog())
                .setReConnectCount(tmpBleConfig.getReconnectCount(), tmpBleConfig.getReconnectInterval())
                .setConnectOverTime(tmpBleConfig.getConnectOvertime())
                .setOperateTimeout(tmpBleConfig.getOperateTimeout());


        return true;
    }

    /**
     * 功能：
     * 表示停止BleManager管理器
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     */
    protected boolean stopBleManager() {

        BleManager.getInstance().cancelScan();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();

        return true;
    }

    /**
     * 功能：
     * 销毁蓝牙管理模块
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     */
    protected boolean destroyBleManager() {
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
        return true;
    }

    /**
     * 功能：
     * 销毁设备列表
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     */
    protected boolean destroyBleSensores() {

        getBleSensores().clear();
        return true;
    }

    /**
     * 功能：
     * 初始化设备搜索服务
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     */
    protected boolean initializationScanService() {

        if (null == getScanService()) {
            return false;
        }

        //初始化BleSensor对象
        if (!getScanService().initialize()) {
            ServiceLog.e("ScanService initialize failed!");
            return false;
        } else {
            ServiceLog.i("ScanService initialize succeed!");
        }
        return true;
    }

    /**
     * 功能：
     * 启动设备搜索服务
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     */
    protected boolean startScanService() {

        boolean tmpReturnBoolean = false;

        setRunning(true);

        //初始化BleSensor对象
        tmpReturnBoolean = getScanService().start(new ScanServiceCallback() {
            @Override
            public void onScanStarted(boolean success) {
                //super.onScanStarted(success);
            }

            @Override
            public void onScanning(BleSensor bleSensor) {
                //super.onScanning(bleSensorList);
                AnalyzeScannedSensor(bleSensor);
            }

            @Override
            public void onScanFinished(List<BleSensor> bleSensorList) {

                //super.onScanFinished(bleSensorList);
                //for (Map.Entry<String, BleSensor> entry : getBleSensores().entrySet()) {
                            /*
                            if (!entry.getValue().isExisted()) {
                                //根据mac地址进行指定搜索
                                getScanService().startBleScanByMac(entry.getKey(), this);
                                ServiceLog.i("rescaning bleSensor[mac :" + entry.getKey() + "]");
                                break;
                            }
                             */
                //AnalyzeScannedSensor(entry.getValue());
                //}
                if (isRunning()) {
                    //startScanService();
                    getScanService().start(this);
                }
            }
        });

        if (!tmpReturnBoolean) {
            ServiceLog.e("ScanService start failed!");
            return false;
        } else {
            ServiceLog.i("ScanService start succeed!");
        }
        return true;
    }

    /**
     * 功能：
     * 停止设备搜索服务
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     */
    protected boolean stopScanService() {
        boolean tmpReturnBoolean = false;


        setRunning(false);

        //停止ScanService对象
        tmpReturnBoolean = getScanService().stop();

        if (!tmpReturnBoolean) {
            ServiceLog.e("ScanService stop failed!");
            return false;
        } else {
            ServiceLog.i("ScanService stop succeed!");
        }
        return true;
    }

    /**
     * 功能：
     * 初始化设备连接服务
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     */
    protected boolean initializationConnectService() {
        return getConnectService().initialize();
    }

    /**
     * 功能：
     * 启动设备连接服务
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     */
    protected boolean startConnectService() {
        //启动连接服务对象
        boolean tmpReturnBoolean = getConnectService().start(
                new ConnectServiceCallback() {
                    @Override
                    public void onStartConnect() {

                    }

                    @Override
                    public void onConnectFail(BleSensor bleSensor, ServiceException exception) {

                    }

                    @Override
                    public void onConnectSuccess(BleSensor bleSensor) {

                    }

                    @Override
                    public void onDisConnected(BleSensor bleSensor) {

                    }
                }
        );

        if (!tmpReturnBoolean) {
            ServiceLog.e("ScanService initialize failed!");
            return false;
        } else {
            ServiceLog.i("ScanService initialize succeed!");
        }
        return true;
    }

    /**
     * 功能：
     * 停止设备连接服务
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     */
    protected boolean stopConnectService() {
        return getConnectService().stop();
    }


    /**
     * 功能：
     * 初始化数据分析服务
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     */
    protected boolean initializationAnalyzeDataService() {
        if (null == getAnalyzeDataService()) {
            return false;
        }

        //初始化AnalyzeDataService对象
        if (!getAnalyzeDataService().initialize()) {
            ServiceLog.e("AnalyzeDataService initialize failed!");
            return false;
        } else {
            ServiceLog.i("AnalyzeDataService initialize succeed!");
        }
        return true;
    }

    /**
     * 功能：
     * 启动数据分析服务
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     */
    protected boolean startAnalyzeDataService() {

        getAnalyzeDataService().setSyncTimeMillis(
                SensorService.getInstance().getConfiguration().getBleConfiguration().getBleScanConfiguration().getScanTimeOut());

        //启动AnalyzeDataService对象
        boolean tmpReturnBoolean = getAnalyzeDataService().start();

        if (!tmpReturnBoolean) {
            ServiceLog.e("AnalyzeDataService start failed!");
            return false;
        } else {
            ServiceLog.i("AnalyzeDataService start succeed!");
        }
        return true;
    }

    /**
     * 功能：
     * 停止数据分析服务
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     */
    protected boolean stopAnalyzeDataService() {
        //停止AnalyzeDataService对象
        boolean tmpReturnBoolean = getAnalyzeDataService().stop();

        if (!tmpReturnBoolean) {
            ServiceLog.e("AnalyzeDataService stop failed!");
            return false;
        } else {
            ServiceLog.i("AnalyzeDataService stop succeed!");
        }
        return true;
    }

    /**
     * 功能：
     * 根据BleSensor对象创建一个AnalyzeDataItem对象
     * 返回：
     * 成功     ：   创建成功返回一个AnalyzeDataItem对象
     * 失败     ：   null
     *
     * @param bleSensor 表示需要处理的BleSensor对象
     */
    private AnalyzeDataItem CreateAnalyzeDataItem(BleSensor bleSensor) {

        AnalyzeDataItem tmpBeaconItem = new AnalyzeDataItem(bleSensor);


        if (!tmpBeaconItem.parse(bleSensor.getBleDevice().getScanRecord())) {
            return null;
        }

        return tmpBeaconItem;
    }

    /**
     * 功能：
     * 分析已经接收到的Sensor对象
     * 返回：
     * true  :  成功；
     * false  :  失败；
     *
     * @param bleSensor 表示Sensor设备对象
     */
    private boolean AnalyzeScannedSensor(BleSensor bleSensor) {
        //根据扫描数据，创建AnalyzeDataItem对象
        AnalyzeDataItem tmpBeaconItem = CreateAnalyzeDataItem(bleSensor);
        if (null == tmpBeaconItem) {
            return false;
        }

        SensorService.getInstance().getShowService().addDataItem(tmpBeaconItem);
        //将数据对象添加到分析数据处理服务中
        return getAnalyzeDataService().addDataItem(tmpBeaconItem);
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
}//end CycleService