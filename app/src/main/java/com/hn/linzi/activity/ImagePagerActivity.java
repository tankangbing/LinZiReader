package com.hn.linzi.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.umeng.analytics.MobclickAgent;
import com.hn.linzi.utils.MyTools;
import com.hn.linzi.views.HackyViewPager;
import com.hn.linzi.views.ImageDetailFragment;
import com.hn.linzi.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ImagePagerActivity extends FragmentActivity {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";
	public static final String TYPE = "type";
	
	private static final int onsave = 1;

	String[] urls;
	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	private ImageButton delete;
	private ImageButton save;
	private String type;
	private int pos;
	
	public Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case onsave:
				Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_detail_pager);

		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
		type = getIntent().getStringExtra(TYPE);
		
		delete = (ImageButton) findViewById(R.id.delete_select);
		save = (ImageButton) findViewById(R.id.save_select);
		
		if(type.equals("LOOK")){
			save = (ImageButton) findViewById(R.id.save_select);
			save.setImageResource(R.drawable.save_pic);
			save.setVisibility(View.VISIBLE);
			save.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DownLoadPic(pagerPosition);
				}
			});
		}else if(type.equals("SELECT")){
			System.out.println("delete visibility");
			delete = (ImageButton) findViewById(R.id.delete_select);
			delete.setImageResource(R.drawable.delete_pic);
			delete.setVisibility(View.VISIBLE);
			delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra("position", pagerPosition);
					ImagePagerActivity.this.setResult(RESULT_OK, intent);
					ImagePagerActivity.this.finish();
				}
			});
		}

		mPager = (HackyViewPager) findViewById(R.id.pager);
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(
				getSupportFragmentManager(), urls);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);
		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
				.getAdapter().getCount());
		indicator.setText(text);
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
				System.out.println(arg0);
				pos = arg0;
				if(type.equals("LOOK")){
					save = (ImageButton) findViewById(R.id.save_select);
//					save.setVisibility(View.VISIBLE);
					save.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							DownLoadPic(pos);
						}
					});
				}else if(type.equals("SELECT")){
					System.out.println("delete visibility");
					delete.setImageResource(R.drawable.delete_pic);
//					delete.setVisibility(View.VISIBLE);''
					delete.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							intent.putExtra("position", pos);
							ImagePagerActivity.this.setResult(RESULT_OK, intent);
							ImagePagerActivity.this.finish();
						}
					});
				}
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public String[] fileList;

		public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.length;
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList[position];
			return ImageDetailFragment.newInstance(url);
		}

	}
	
	/** 
     * Get image from newwork 
     * @param path The path of image 
     * @return InputStream 
     * @throws Exception 
     */  
    public InputStream getImageStream(String path) throws Exception{  
        URL url = new URL(path);  
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
        conn.setConnectTimeout(5 * 1000);  
        conn.setRequestMethod("GET");  
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){  
            return conn.getInputStream();  
        }  
        return null;  
    } 
    
    /** 
     * 保存文件 
     * @param bm 
     * @param fileName 
     * @throws IOException 
     */  
    public void saveFile(Bitmap bm, String fileName) throws IOException {  
    	String path;
		if(MyTools.getExtSDCardPaths().size() > 0){
			path = MyTools.getExtSDCardPaths().get(0) + "/Android/data/com.wr.reader2/files/PIC/";
		}else {
			path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.wr.reader2/files/PIC/";
		}
        File dirFile = new File(path);  
        if(!dirFile.exists()){  
            dirFile.mkdirs();  
        }  
        File myCaptureFile = new File(path + fileName); 
        if(!myCaptureFile.exists()){  
        	myCaptureFile.createNewFile();
        } 
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));  
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);  
        bos.flush();  
        bos.close();
        handler.sendEmptyMessage(ImagePagerActivity.onsave);
    }  
	
	private void DownLoadPic(final int pos) {
		new Thread(){
			public void run() {
				try {
					String s[] = urls[pos].split("/");
					System.out.println("name===" + s[s.length-1]);
					Bitmap mBitmap = BitmapFactory.decodeStream(getImageStream(urls[pos]));
					saveFile(mBitmap, s[s.length-1]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		MobclickAgent.onResume(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub.
		MobclickAgent.onPause(this);
		super.onPause();
	}
}