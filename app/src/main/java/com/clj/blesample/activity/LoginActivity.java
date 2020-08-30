package com.clj.blesample.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;

import com.clj.blesample.MainActivityShow;
import com.gyf.immersionbar.ImmersionBar;
import com.clj.blesample.R;

/**
 * @author geyifeng
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(R.id.toolbar).keyboardEnable(true).init();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                EditText tmpEditText = (EditText) findViewById(R.id.et_password);
                String tmpStringPassword = tmpEditText.getText().toString();

                //这里主要是校验密码，密码成功则跳转到主界面
                if (tmpStringPassword.equalsIgnoreCase("123456")) {
                    Intent tmpIntent = new Intent(this, DesktopActivity.class);
                    if (tmpIntent != null) {
                        startActivity(tmpIntent);
                    }
                } else {
                    //密码错误提示
                    tmpEditText.setText("");
                    Toast tmpToast = Toast.makeText(getApplicationContext(), getString(R.string.password_error_message), Toast.LENGTH_LONG);

                    tmpToast.setGravity(Gravity.CENTER, 0, 0);
                    tmpToast.show();
                }

                break;
        }
    }
}
