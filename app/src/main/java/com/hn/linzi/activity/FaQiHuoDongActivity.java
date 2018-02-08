package com.hn.linzi.activity;

import com.hn.linzi.utils.DateTimePickDialogUtil;
import com.hn.linzi.views.BaseActivity;
import com.hn.linzi.R;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class FaQiHuoDongActivity extends BaseActivity implements OnClickListener {
	private Button back;
	private ImageView huodongfengmian;
	private EditText huodongname;
	private EditText baomingtime;
	private EditText starttime;
	private EditText endtime;
	private CheckBox box;
	private EditText numofpeople;
	private EditText huodongjieshao;
	private ImageButton tijiao;
	private String defaultTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faqihuodong);
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		System.out.println("111");

		back = (Button) findViewById(R.id.faqi_huodong_back);
		huodongfengmian = (ImageView) findViewById(R.id.faqi_huodong_fengmian);
		huodongname = (EditText) findViewById(R.id.faqi_huodong_name);
		baomingtime = (EditText) findViewById(R.id.faqi_huodong_baomingtime);
		starttime = (EditText) findViewById(R.id.faqi_huodong_starttime);
		endtime = (EditText) findViewById(R.id.faqi_huodong_endtime);
		box = (CheckBox) findViewById(R.id.faqi_huodong_checkbox);
		numofpeople = (EditText) findViewById(R.id.faqi_huodong_numofpeople);
		huodongjieshao = (EditText) findViewById(R.id.faqi_huodong_jieshao);
		tijiao = (ImageButton) findViewById(R.id.faqi_huodong_tijiao);
		
		defaultTime = "2016年2月18日 14:44";
		System.out.println("222");
		numofpeople.setVisibility(View.GONE);
		System.out.println("333");
		back.setOnClickListener(this);
		baomingtime.setOnClickListener(this);
		starttime.setOnClickListener(this);
		endtime.setOnClickListener(this);
		tijiao.setOnClickListener(this);
		System.out.println("444");
		box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					numofpeople.setVisibility(View.VISIBLE);
				}else{
					numofpeople.setVisibility(View.GONE);
				}
			}
		});
		System.out.println("555");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(  
				FaQiHuoDongActivity.this, defaultTime); 
		switch (v.getId()) {
		case R.id.faqi_huodong_back:
			finish();
			break;

		case R.id.faqi_huodong_baomingtime:
            dateTimePicKDialog.dateTimePicKDialog(baomingtime);
			break;

		case R.id.faqi_huodong_starttime:
            dateTimePicKDialog.dateTimePicKDialog(starttime);
			break;

		case R.id.faqi_huodong_endtime:
			dateTimePicKDialog.dateTimePicKDialog(endtime);
			break;

		case R.id.faqi_huodong_tijiao:
			HuoDongCommit();
			break;
		}
	}

	private void HuoDongCommit() {
		// TODO Auto-generated method stub
		
	}
}
