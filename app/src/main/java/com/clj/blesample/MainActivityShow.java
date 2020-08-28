package com.clj.blesample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.clj.blesample.adapter.DeviceAdapter;
import com.clj.blesample.service.ConfigurationLoader;
import com.clj.blesample.service.SensorService;
import com.clj.blesample.service.beans.BleConfiguration;
import com.clj.blesample.service.beans.Configuration;
import com.clj.fastble.BleManager;

import android.os.Environment;

import java.io.File;


public class MainActivityShow extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivityShow.class.getSimpleName();
    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;

    private LinearLayout layout_setting;
    private TextView txt_setting;
    private Button btn_accept;
    private EditText et_name, et_mac, et_uuid;
    private Switch sw_auto;
    private ImageView img_loading;

    private Animation operatingAnim;
    private DeviceAdapter mDeviceAdapter;
    private ProgressDialog progressDialog;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        initView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_accept:
                if (btn_accept.getText().equals(getString(R.string.start_accept))) {
                    if (!startSensorService()) {
                        stopSensorService();
                    } else {
                        btn_accept.setText(getString(R.string.stop_accept));
                    }
                } else if (btn_accept.getText().equals(getString(R.string.stop_accept))) {
                    if (stopSensorService()) {
                        btn_accept.setText(getString(R.string.start_accept));
                    }
                }
                break;
        }
        /*
        Configuration conf = new Configuration();

        conf.getBleConfiguration().getBleScanConfiguration().setDeviceMac("F1:E1:D9:BD:13:51");

        File tmpStorageDirectory = Environment.getExternalStorageDirectory();
        File tmpFullFilename = new File(tmpStorageDirectory, getString(R.string.configuration_file_path));
        ConfigurationLoader.setConfiguration(tmpFullFilename.getPath(), conf);
         */
    }

    /**
     * Start sensor service boolean.
     *
     * @return the boolean
     */
    protected boolean startSensorService() {
        File tmpStorageDirectory = Environment.getExternalStorageDirectory();
        File tmpFullFilename = new File(tmpStorageDirectory, getString(R.string.configuration_file_path));
        if (!SensorService.getInstance().initialize(this, tmpFullFilename.getPath())) {
            return false;
        }

        return SensorService.getInstance().start();
    }

    /**
     * Stop sensor service boolean.
     *
     * @return the boolean
     */
    protected boolean stopSensorService() {
        return SensorService.getInstance().stop();
    }

    /**
     * 为了得到传回的数据，必须在前面的Activity中（指MainActivity类）重写onActivityResult方法
     * <p>
     * requestCode 请求码，即调用startActivityForResult()传递过去的值
     * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            if (!startSensorService()) {
                finishAffinity();
            }
        } else {
            finishAffinity();
        }
    }


    private void initView() {
        btn_accept = (Button) findViewById(R.id.btn_accept);

    }
}
