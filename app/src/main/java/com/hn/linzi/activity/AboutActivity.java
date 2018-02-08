package com.hn.linzi.activity;

import com.hn.linzi.views.BaseActivity;
import com.hn.linzi.R;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends BaseActivity implements OnClickListener{
	private Button back;
	private TextView version;
	private TextView erweimaText;
	private ImageView erweima;
	private AboutActivity activity;
	
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		ActionBar actionBar = getActionBar();
		actionBar.hide();
		activity = this;
		
		back = (Button) findViewById(R.id.about_back);
		version = (TextView) findViewById(R.id.about_version);
		erweima = (ImageView) findViewById(R.id.about_erweima);

		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.about_back:
			activity.finish();
			break;
			
		}
	}
}
