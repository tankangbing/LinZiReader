package com.hn.linzi.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;


import com.hn.linzi.R;
import com.hn.linzi.utils.PreferencesUtils;
import com.hn.linzi.views.BaseActivity;


/**
 * Created by Administrator on 2017/5/18 0018.
 */

public class SplashActivity extends BaseActivity implements View.OnClickListener{

    private Button mBtn_doctor;
    private Button mBtn_no_doctor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
//        initData();
        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }

    private void initView() {
        setContentView(R.layout.activity_splash);
        mBtn_doctor = (Button) findViewById(R.id.btn_doctor);
        mBtn_no_doctor = (Button) findViewById(R.id.btn_no_doctor);

        mBtn_doctor.setOnClickListener(this);
        mBtn_no_doctor.setOnClickListener(this);
    }

    private void initData() {
        int chooce = PreferencesUtils.getInt(getApplicationContext(), "chooce");
        if(chooce==1){
            Intent intent2 = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent2);
            finish();
        }else if (chooce==2){
            Intent intent2 = new Intent(SplashActivity.this, MainMenuActivity.class);
            startActivity(intent2);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_doctor:
                Intent intent2 = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent2);
                PreferencesUtils.putInt(getApplicationContext(), "chooce",1);
                break;
            case R.id.btn_no_doctor:
                Intent intent3 = new Intent(SplashActivity.this, MainMenuActivity.class);
                startActivity(intent3);
                PreferencesUtils.putInt(getApplicationContext(), "chooce",2);
                break;
            default:
                break;
        }
//        finish();
    }
}
