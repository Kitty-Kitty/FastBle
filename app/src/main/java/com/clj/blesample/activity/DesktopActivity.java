package com.clj.blesample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.allen.library.SuperButton;
import com.clj.blesample.service.SensorService;
import com.clj.blesample.service.ServiceLog;
import com.gyf.immersionbar.ImmersionBar;
import com.clj.blesample.R;

import java.io.File;

/**
 * @author geyifeng
 */
public class DesktopActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = DesktopActivity.class.getSimpleName();
    private static final int MESSAGE_STARTED_SHOW_AND_AUDIO_REPORT_SERVICE = 1;
    private static final int MESSAGE_STARTED_SENSOR_SERVICE = 2;
    private static final int MESSAGE_STOPPED_SENSOR_SERVICE = 3;
    private static final int MESSAGE_STARTING_SENSOR_SERVICE = 4;
    private static boolean loadFinish = false;
    private com.github.clans.fab.FloatingActionButton btn_accept;
    private LinearLayout sensor_layout;
    private Thread thread;

    /*private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_VIEW) {

            }
        }
    };*/
    //定时刷新界面模块的显示状态
    private Handler timerHandler = new Handler();
    Runnable timerRun = new Runnable()                //创建一个runnable对象
    {
        @Override
        public void run() {
            //要做的事情这里
            //触发重新绘制
            if (sensor_layout.getVisibility() == android.view.View.VISIBLE) {
                for (View tmpView : sensor_layout.getTouchables()) {

                    if (tmpView.getVisibility() == android.view.View.VISIBLE) {
                        //com.allen.library.SuperButton对象的postInvalidate()方法无效，需要在UI线程中使用.setUseShape()，使属性更新生效
                        ((com.allen.library.SuperButton) tmpView).setUseShape();
                    }
                }
                sensor_layout.postInvalidate();
            }
            timerHandler.postDelayed(this, 500);      //再次调用myTimerRun对象，实现每一秒一次的定时器操作
        }
    };

    //消息处理器
    private Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STARTED_SHOW_AND_AUDIO_REPORT_SERVICE:
                    if (null != SensorService.getInstance().getAudioReportService()) {
                        btn_accept.setEnabled(true);
                        btn_accept.postInvalidate();
                        SensorService.getInstance().getAudioReportService().say(getString(R.string.system_loaded));
                    }
                    break;
                case MESSAGE_STARTING_SENSOR_SERVICE:
                    SensorService.getInstance().getAudioReportService().say(getString(R.string.system_running));
                    break;
                case MESSAGE_STARTED_SENSOR_SERVICE:
                    SensorService.getInstance().getAudioReportService().say(getString(R.string.system_start_succeed));
                    break;
                case MESSAGE_STOPPED_SENSOR_SERVICE:
                    SensorService.getInstance().getAudioReportService().say(getString(R.string.system_stopped));
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_desktop;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desktop);
        initView();

        updateButtonStateBySensorService();
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
    protected void initData() {
        super.initData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initView() {
        super.initView();
        btn_accept = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.btn_accept);
        btn_accept.setEnabled(false);
        btn_accept.postInvalidate();
        sensor_layout = (LinearLayout) findViewById(R.id.layout_sensors);
        sensor_layout.removeAllViews();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub

        if (hasFocus) {
            if (!loadFinish) {
                //初始化语音与显示服务内容
                initShowAndAudioReportService(this);
                loadFinish = true;
            } else {
                if (SensorService.getInstance().isStarted()) {
                    sendMessage(MESSAGE_STARTING_SENSOR_SERVICE);
                } else {
                    sendMessage(MESSAGE_STARTED_SHOW_AND_AUDIO_REPORT_SERVICE);
                }
            }
        }

        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setListener() {
        super.setListener();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(R.id.toolbar).keyboardEnable(true).init();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_accept:
                if (btn_accept.getLabelText().equals(getString(R.string.start_accept))) {
                    //btn_accept.setProgress(100, true);
                    if (!startSensorService()) {
                        stopSensorService();
                    }
                    updateButtonStateBySensorService();
                    timerHandler.postDelayed(timerRun, 1000);

                    //启动语音播放
                    sendMessage(MESSAGE_STARTED_SENSOR_SERVICE);
                } else if (btn_accept.getLabelText().equals(getString(R.string.stop_accept))) {
                    stopSensorService();
                    updateButtonStateBySensorService();
                    timerHandler.removeCallbacks(timerRun);

                    //停止语音播放
                    sendMessage(MESSAGE_STOPPED_SENSOR_SERVICE);
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
     * init sensor service boolean.
     *
     * @return the boolean
     */
    protected boolean initSensorService() {
        File tmpStorageDirectory = Environment.getExternalStorageDirectory();
        File tmpFullFilename = new File(tmpStorageDirectory, getString(R.string.configuration_file_path));
        if (!SensorService.getInstance().initialize(this, tmpFullFilename.getPath())) {
            return false;
        }
        return true;
    }

    /**
     * Start sensor service boolean.
     *
     * @return the boolean
     */
    protected boolean startSensorService() {
        /*File tmpStorageDirectory = Environment.getExternalStorageDirectory();
        File tmpFullFilename = new File(tmpStorageDirectory, getString(R.string.configuration_file_path));
        if (!SensorService.getInstance().initialize(this, tmpFullFilename.getPath())) {
            return false;
        }*/
        initSensorService();

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
     * Update button state by sensor service.
     */
    protected void updateButtonStateBySensorService() {
        if (SensorService.getInstance().isStarted()) {
            btn_accept.setLabelText(getString(R.string.stop_accept));
            btn_accept.setImageResource(android.R.drawable.ic_media_pause);
            btn_accept.setProgress(100, false);
        } else {
            btn_accept.setLabelText(getString(R.string.start_accept));
            btn_accept.setProgress(0, false);
            btn_accept.setImageResource(android.R.drawable.ic_media_play);
        }
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

    /**
     * Init show and audio report service.
     *
     * @param activity the activity
     */
    protected void initShowAndAudioReportService(final Activity activity) {
        initSensorService();
        SensorService.getInstance().startShowService(activity);
        SensorService.getInstance().startAudioReportService(activity);
        sensor_layout.postInvalidate();

        thread = new Thread(new Runnable() {//这里也可用Runnable接口实现
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);//每隔1s执行一次
                    sendMessage(MESSAGE_STARTED_SHOW_AND_AUDIO_REPORT_SERVICE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        thread.start();
    }

    /**
     * 发送消息.
     *
     * @param msgCode 表示需要发送的信息码
     */
    private void sendMessage(int msgCode) {
        Message msg = new Message();
        msg.what = msgCode;
        messageHandler.sendMessage(msg);
    }
}
