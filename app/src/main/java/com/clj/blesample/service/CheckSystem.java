package com.clj.blesample.service;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.clj.blesample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 检查系统当前状态，是否存在蓝牙耳机
 *
 * @author f
 * @version 1.0
 * @created 23-7月-2020 14:44:13
 */
public class CheckSystem {

    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;
    /**
     * 当前服务所处的活跃用户界面
     */
    private Activity activity;

    private CheckSystem() {

    }

    public void finalize() throws Throwable {

    }

    /**
     * @param ac 当前服务所处的活跃用户界面
     */
    public CheckSystem(Activity ac) {
        activity = ac;
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
     * 该函数主要用于校验蓝牙设备是否准备就绪
     * 返回：
     * true  :  表示一切准备就绪；
     * false :  表示蓝牙设备还没就绪；
     */
    public boolean checkBluetoothDvice() {
        return checkPermissions();
    }

    /**
     * 功能：
     * 校验系统权限，提示蓝牙设备状态
     * 返回：
     * true  :  校验成功；
     * false :  校验失败；
     */
    private boolean checkPermissions() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(getActivity(), SensorServiceString.openBluetoothDevice, Toast.LENGTH_LONG).show();
            //系统会显示对话框,允许后直接打开
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            getActivity().startActivityForResult(enabler, 1);
            return false;
        }

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                if (permissionGranted(permission)) {
                    return true;
                }
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(getActivity()
                    , deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
        return false;
    }

    /**
     * 功能：
     * 进行权限配置确认操作
     * 返回：
     * true  :  表示权限确认成功，可以顺利进行操作；
     * false  :  表示需要等待权限配置操作
     *
     * @param permission 表示权限描述字符串
     */
    private boolean permissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.notifyTitle)
                            .setMessage(R.string.gpsNotifyMsg)
                            .setNegativeButton(R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getActivity().finish();
                                        }
                                    })
                            .setPositiveButton(R.string.setting,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            getActivity().startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                        }
                                    })

                            .setCancelable(false)
                            .show();
                } else {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * 功能：
     * 检测GPS是否打开
     * 返回：
     * true : 表示已经打开；
     * false : 表示还没有打开；
     */
    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }
}//end CheckSystem