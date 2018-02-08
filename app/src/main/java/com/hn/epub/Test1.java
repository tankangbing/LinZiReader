package com.hn.epub;

import com.hn.linzi.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

/**
* @Description: 书架页面，显示3本epub
*/ 
public class Test1 extends Activity {

	static Activity acta;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test1);
		
		acta=this;
		
		ImageButton bt1=(ImageButton)findViewById(R.id.imageButton1);
		bt1.setOnClickListener(new View.OnClickListener() {				
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Test1.this, Turnmain3.class);		
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);	
				intent.putExtra("bookurl", "download/26.epub");  
				startActivity(intent);	
			}
		});
		
		ImageButton bt2=(ImageButton)findViewById(R.id.imageButton2);
		bt2.setOnClickListener(new View.OnClickListener() {				
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Test1.this, Turnmain3.class);		
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);	
				intent.putExtra("bookurl", "download/57.epub");  
				startActivity(intent);	
			}
		});
		
		ImageButton bt3=(ImageButton)findViewById(R.id.imageButton3);
		bt3.setOnClickListener(new View.OnClickListener() {				
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Test1.this, Turnmain3.class);		
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);		
				intent.putExtra("bookurl", "download/133.epub");  
				startActivity(intent);	
			}
		});
	}

}
