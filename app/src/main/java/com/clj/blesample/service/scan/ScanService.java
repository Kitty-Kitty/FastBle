package com.clj.blesample.service.scan;


import android.bluetooth.le.ScanFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.clj.blesample.service.BleSensor;
import com.clj.blesample.service.SensorService;
import com.clj.blesample.service.ServiceInterface;
import com.clj.blesample.service.ServiceLog;
import com.clj.blesample.service.beans.BleScanRuleConfiguration;
import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.UUID;

/**
 * 搜索需要的所有蓝牙设备
 *
 * @author f
 * @version 1.0
 * @created 23 -7月-2020 14:44:13
 */
public class ScanService implements ServiceInterface {

    /**
     * Instantiates a new Scan service.
     */
    public ScanService() {

    }

    public void finalize() throws Throwable {

    }

    /**
     * 表示初始化服务对象
     */
    public boolean initialize() {

        //初始化蓝牙设备的扫描规则，大数据量的同类设备时，尤其重要
        if (!initializeBleScanRule()) {
            ServiceLog.e("initialize ScanRule failed!");
            return false;
        } else {
            ServiceLog.i("initialize ScanRule succeed!");
        }

        return true;
    }

    /**
     * 表示启动服务对象
     */
    public boolean start() {
        return startBleScan();
    }

    /**
     * 表示停止服务对象
     */
    public boolean stop() {
        BleManager.getInstance().cancelScan();
        return true;
    }

    /**
     * 功能：
     * 表示启动服务对象，并设置回调函数
     * 返回：
     * true  : 表示成功；
     * false  : 表示失败；
     *
     * @param scanServiceCallback 表示搜索结果的回调对象
     */
    public boolean start(ScanServiceCallback scanServiceCallback) {
        return startBleScan(scanServiceCallback);
    }

    /**
     * 功能：
     * 初始化搜索规则信息
     * 返回：
     * true  :  表示初始化成功；
     * false  :  表示失败；
     *
     * @return
     */
    private boolean initializeBleScanRule() {

        BleScanRuleConfiguration bsrc = SensorService.getInstance().getConfiguration()
                .getBleConfiguration().getBleScanConfiguration();


        if (null == bsrc) {
            return false;
        }

        //可以描述指定的多个uuid，用“，”隔开
        String[] uuids;
        String str_uuid = bsrc.getServiceUUID();
        if (TextUtils.isEmpty(str_uuid)) {
            uuids = null;
        } else {
            uuids = str_uuid.split(",");
        }
        UUID[] serviceUuids = null;
        if (uuids != null && uuids.length > 0) {
            serviceUuids = new UUID[uuids.length];
            for (int i = 0; i < uuids.length; i++) {
                String name = uuids[i];
                String[] components = name.split("-");
                if (components.length != 5) {
                    serviceUuids[i] = null;
                } else {
                    serviceUuids[i] = UUID.fromString(uuids[i]);
                }
            }
        }

        //可以描述指定的多个名称的设备，用“，”隔开
        String[] names;
        String str_name = bsrc.getDeviceName();
        if (TextUtils.isEmpty(str_name)) {
            names = null;
        } else {
            names = str_name.split(",");
        }

        //可以描述指定的多个名称的设备，用“，”隔开
        String[] macArray;
        String strMac = bsrc.getDeviceMac();
        if (TextUtils.isEmpty(strMac)) {
            macArray = null;
        } else {
            macArray = strMac.split(",");
        }

        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(serviceUuids)                        // 只扫描指定的服务的设备，可选
                .setDeviceName(true, names)                     // 只扫描指定广播名的设备，可选
                .setDeviceMac(macArray)                               // 只扫描指定mac的设备，可选
                .setAutoConnect(bsrc.isAutoConnect())                 // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(bsrc.getScanTimeOut())                // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);

        return true;
    }


    /**
     * 功能：
     * 开始进行设备搜索
     * 返回：
     * true  :  表示初始化成功；
     * false  :  表示失败；
     */
    private boolean startBleScan() {
        return startBleScan(null);
    }

    /**
     * 功能：
     * 开始进行设备搜索
     * 返回：
     * true  :  表示初始化成功；
     * false  :  表示失败；
     *
     * @param scanServiceCallback 表示搜索结果的回调对象
     */
    private boolean startBleScan(ScanServiceCallback scanServiceCallback) {

        BleManager.getInstance().scan(new BleScanCallbackWrap(scanServiceCallback) {
            @Override
            public void onScanStarted(boolean success) {
                ServiceLog.i("Started BleScan succeed!");

                //回调上层的启动搜索函数
                if (null != getScanServiceCallback()) {
                    getScanServiceCallback().onScanStarted(success);
                }
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                BleSensor tmpBleSensor = updateBleSensor(bleDevice);

                //回调上层的启动搜索函数
                if (null != getScanServiceCallback()) {
                    getScanServiceCallback().onScanning(tmpBleSensor);
                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {

                List<BleSensor> tmpBleSensores = new ArrayList<BleSensor>();


                try {
                    for (BleDevice tmpBleDevice : scanResultList) {
                        BleSensor tmpBleSensor = updateBleSensor(tmpBleDevice);
                        if (null != tmpBleSensor) {
                            tmpBleSensores.add(tmpBleSensor);
                        }
                    }
                } catch (ConcurrentModificationException e) {
                    ServiceLog.e(e.getMessage());
                }

                ServiceLog.i("All device scaned succeed!");
                /*for (Map.Entry<String, BleSensor> entry
                        : SensorService.getInstance().getCycleService().getBleSensores().entrySet()) {
                    if (null == entry.getValue()) {
                        //根据mac地址进行指定搜索
                        startBleScanByMac(entry.getKey(), getScanServiceCallback());
                        return;
                    }
                }*/

                //回调上层的启动搜索函数
                if (null != getScanServiceCallback()) {
                    getScanServiceCallback().onScanFinished(tmpBleSensores);
                }
            }
        });
        return true;
    }

    /**
     * 功能：
     * 设置指定Mac地址的搜索规则
     * 返回：
     * true  :  表示初始化成功；
     * false  :  表示失败；
     *
     * @param mac 表示需要被查找的设备mac地址
     */
    private boolean setBleScanRuleByMac(String[] mac) {

        BleScanRuleConfiguration bsrc = SensorService.getInstance().getConfiguration()
                .getBleConfiguration().getBleScanConfiguration();


        if (null == bsrc) {
            return false;
        }

        UUID[] serviceUuids = null;
        String[] names = null;

        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(serviceUuids)                        // 只扫描指定的服务的设备，可选
                .setDeviceName(true, names)                    // 只扫描指定广播名的设备，可选
                .setDeviceMac(mac)                    // 只扫描指定mac的设备，可选
                .setAutoConnect(bsrc.isAutoConnect())                    // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(bsrc.getScanTimeOut())                // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);

        return true;
    }

    /**
     * 功能：
     * 搜索指定Mac的设备
     * 返回：
     * true  :  表示初始化成功；
     * false  :  表示失败；
     *
     * @param mac                 表示需要被查找的设备mac地址
     * @param scanServiceCallback 表示搜索结果的回调对象
     */
    public boolean startBleScanByMac(@NonNull String[] mac, @Nullable ScanServiceCallback scanServiceCallback) {
        if (!setBleScanRuleByMac(mac)) {
            return false;
        }

        return startBleScan(scanServiceCallback);
    }

    /**
     * 功能：
     * 使用BleDevice设备信息更新指定的BleSensor对象信息
     * 返回：
     * 成功：返回当前被修改的BleSensor对象
     * 失败：返回null
     *
     * @param bleDevice 表示已经搜索到的设备信息
     */
    private BleSensor updateBleSensor(BleDevice bleDevice) {

        //使用蓝牙设备的mac地址作为设备的唯一标识
        BleSensor tmpBleSensor = SensorService.getInstance().getCycleService().getBleSensores().get(bleDevice.getMac());

        if (null != tmpBleSensor) {

            if (tmpBleSensor.getBleDevice() != bleDevice) {
                //如果发现的设备与已经存在列表中的设备不同，则关闭原来的BleDevice设备，同时更新本地的设备列表
                if (BleManager.getInstance().isConnected(tmpBleSensor.getBleDevice())) {
                    BleManager.getInstance().disconnect(tmpBleSensor.getBleDevice());
                }

                tmpBleSensor.setBleDevice(bleDevice);
            }

            return tmpBleSensor;
        }
        return null;
    }
}//end ScanDevices