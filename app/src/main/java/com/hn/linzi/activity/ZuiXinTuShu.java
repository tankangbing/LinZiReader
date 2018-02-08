package com.hn.linzi.activity;

import android.app.ActionBar;
import android.os.Bundle;

import com.hn.linzi.R;
import com.hn.linzi.views.BaseActivity;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class ZuiXinTuShu extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zuixin);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }
}
