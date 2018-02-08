package com.hn.linzi.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.hn.linzi.adapter.RecyclingPagerAdapter;
import com.hn.linzi.views.BaseActivity;
import com.hn.linzi.views.ClipViewPager;
import com.hn.linzi.views.ScalePageTransformer;
import com.hn.linzi.R;

public class ChangeBGActivity extends BaseActivity implements OnClickListener{

    private ClipViewPager mViewPager;
    private TubatuAdapter mPagerAdapter;
    private TextView select;
    private Button back;
    private List<Integer> list;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_bg);
        
        ActionBar actionBar = getActionBar();
		actionBar.hide();

		sp = getSharedPreferences("SP", MODE_PRIVATE);
        mViewPager = (ClipViewPager) findViewById(R.id.viewpager);
        mViewPager.setPageTransformer(true, new ScalePageTransformer());

        findViewById(R.id.activity_bg).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });
        findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));

        mPagerAdapter = new TubatuAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);
        
        back = (Button) findViewById(R.id.changebg_back);
        select = (TextView) findViewById(R.id.changebg_ok);
        back.setOnClickListener(this);
        select.setOnClickListener(this);
        
        initData();
    }

    private void initData() {
    	list = new ArrayList();
        list.add(R.drawable.mm_bg0);
        list.add(R.drawable.mm_bg1);
        list.add(R.drawable.mm_bg2);
        list.add(R.drawable.mm_bg3);
        list.add(R.drawable.mm_bg4);
        list.add(R.drawable.mm_bg5);

        //设置OffscreenPageLimit
        mViewPager.setOffscreenPageLimit(Math.min(list.size(), 5));
        mPagerAdapter.addAll(list);
    }

    public static class TubatuAdapter extends RecyclingPagerAdapter {

        private final List<Integer> mList;
        private final Context mContext;

        public TubatuAdapter(Context context) {
            mList = new ArrayList();
            mContext = context;
        }

        public void addAll(List<Integer> list) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            ImageView imageView = null;
            if (convertView == null) {
                imageView = new ImageView(mContext);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setTag(position);
            imageView.setImageResource(mList.get(position));
            return imageView;
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.changebg_back:
			finish();
			break;
		case R.id.changebg_ok:
			Editor editor = sp.edit();
			editor.putInt("BGRes", list.get(mViewPager.getCurrentItem()));
			editor.commit();
			findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));
			break;
		}
	}

}

