package com.clj.blesample.service.connect;


import android.bluetooth.BluetoothGatt;

import com.clj.blesample.service.BleSensor;
import com.clj.blesample.service.SensorService;
import com.clj.blesample.service.ServiceException;
import com.clj.blesample.service.ServiceInterface;
import com.clj.blesample.service.ServiceLog;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;

import java.util.Map;

/**
 * 连接所有设备，获取设备返回信息
 *
 * @author f
 * @version 1.0
 * @created 23-7月-2020 14:44:13
 */
public class ConnectService implements ServiceInterface {

    public ConnectService() {

    }

    public void finalize() throws Throwable {

    }

    /**
     * 表示初始化服务对象
     */
    public boolean initialize() {
        return true;
    }

    /**
     * 表示启动服务对象
     */
    public boolean start() {
        return false;
    }

    /**
     * 表示停止服务对象
     */
    public boolean stop() {
        return false;
    }

    /**
     * 表示启动服务对象
     *
     * @param connectServiceCallback 网络连接回调函数
     */
    public boolean start(ConnectServiceCallback connectServiceCallback) {

        for (Map.Entry<String, BleSensor> entry
                : SensorService.getInstance().getCycleService().getBleSensores().entrySet()) {

            if (!entry.getValue().isExisted()) {
                continue;
            }

            //使用BleDevice对象连接该设备
            BleManager.getInstance().connect(entry.getValue().getBleDevice()
                    , makeBleConnectCallbackWrap(entry.getValue(), connectServiceCallback));
        }

        return true;
    }

    /**
     * 功能：
     * 启动服务对象，通过Mac地址连接该设备
     * 返回：
     * true  :  成功；
     * false  :  失败；
     *
     * @param connectServiceCallback 网络连接回调函数
     */
    public boolean startByMac(ConnectServiceCallback connectServiceCallback) {

        for (Map.Entry<String, BleSensor> entry
                : SensorService.getInstance().getCycleService().getBleSensores().entrySet()) {

            //使用Mac地址连接该设备
            BleManager.getInstance().connect(entry.getKey()
                    , makeBleConnectCallbackWrap(entry.getValue(), connectServiceCallback));
        }

        return true;
    }

    /**
     * 功能：
     * 创建一个连接回调函数
     * 返回：
     * 返回一个连接回调函数对象
     *
     * @param bleSensor              表示当前操作的bleSensor对象
     * @param connectServiceCallback 网络连接回调函数
     */
    private BleConnectCallbackWrap makeBleConnectCallbackWrap(BleSensor bleSensor
            , ConnectServiceCallback connectServiceCallback) {

        return new BleConnectCallbackWrap(bleSensor, connectServiceCallback) {
            @Override
            public void onStartConnect() {
                //回调给上层对象
                if (null != getConnectServiceCallback()) {
                    getConnectServiceCallback().onStartConnect();
                }
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                BleSensor bleSensor = getBleSensor();


                if (null == bleSensor) {
                    bleSensor = SensorService.getInstance().getCycleService().getBleSensores().get(bleDevice.getMac());
                }

                updateBleSensor(bleSensor, bleDevice);

                //回调给上层对象
                if (null != getConnectServiceCallback()) {
                    getConnectServiceCallback().onConnectFail(
                            getBleSensor()
                            , new ServiceException(exception.toString())
                    );
                }
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {

                BleSensor bleSensor = getBleSensor();


                if (null == bleSensor) {
                    bleSensor = SensorService.getInstance().getCycleService().getBleSensores().get(bleDevice.getMac());
                }

                updateBleSensor(bleSensor, bleDevice);

                //回调给上层对象
                if (null != getConnectServiceCallback()) {
                    getConnectServiceCallback().onConnectSuccess(getBleSensor());
                }
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected
                    , BleDevice bleDevice
                    , BluetoothGatt gatt
                    , int status) {

                BleSensor bleSensor = getBleSensor();


                if (null == bleSensor) {
                    bleSensor = SensorService.getInstance().getCycleService().getBleSensores().get(bleDevice.getMac());
                }

                updateBleSensor(bleSensor, bleDevice);

                //回调给上层对象
                if (null != getConnectServiceCallback()) {
                    getConnectServiceCallback().onDisConnected(getBleSensor());
                }
            }

            private void updateBleSensor(BleSensor bleSensor, BleDevice bleDevice) {
                if (null != bleSensor) {
                    //更新BleSensor对应的BleDevice对象
                    if (bleSensor.getBleDevice() != bleDevice) {
                        bleSensor.setBleDevice(bleDevice);
                    }
                }
            }
        };
    }

    /**
     * 功能：
     * 启动通知特征连接与接收
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     *
     * @param bleSensor 表示当前的BleSensor对象
     */
    protected boolean startNotifyCharacteristic(final BleSensor bleSensor) {
        if (!BleManager.getInstance().isConnected(bleSensor.getBleDevice())) {
            ServiceLog.d(bleSensor.toString() + " disconnected failed!");
            return false;
        }

        String tmpServiceUUID = SensorService.getInstance().getConfiguration().getBleConfiguration().getCharacteristicConfiguration().getServiceUUID();
        String tmpNotifyUUID = SensorService.getInstance().getConfiguration().getBleConfiguration().getCharacteristicConfiguration().getNotifyUUID();

        BleManager.getInstance().notify(
                bleSensor.getBleDevice(),
                tmpServiceUUID,
                tmpNotifyUUID,
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        ServiceLog.d(bleSensor.toString() + " notify succeed!");
                    }

                    @Override
                    public void onNotifyFailure(final BleException exception) {
                        ServiceLog.w(bleSensor.toString() + " notify failed!");

                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        ServiceLog.i(HexUtil.formatHexString(data, true));
                    }
                });
        return true;
    }

    /**
     * 功能：
     * 停止通知特征连接与接收
     * 返回：
     * true  :  表示成功；
     * false :  表示失败；
     *
     * @param bleSensor 表示当前的BleSensor对象
     */
    protected boolean stopNotifyCharacteristic(BleSensor bleSensor) {
        return BleManager.getInstance().stopNotify(
                bleSensor.getBleDevice(),
                SensorService.getInstance().getConfiguration().getBleConfiguration().getCharacteristicConfiguration().getServiceUUID(),
                SensorService.getInstance().getConfiguration().getBleConfiguration().getCharacteristicConfiguration().getNotifyUUID());
    }
}//end ConnectDevices