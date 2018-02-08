package com.hn.linzi.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hn.linzi.R;
import com.hn.linzi.data.MeasureInfo;
import com.hn.linzi.db.MeasureDao;
import com.hn.linzi.views.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/18 0018.
 */

public class ShowActivity extends BaseActivity {
    private MeasureDao measureDao;
    private MeasurAdapter measurAdapter;
    private ListView lvIi;
    private List<MeasureInfo> infos = new ArrayList<MeasureInfo>();
    private  MeasureInfo measureInfo;
    private int offset = 0 ;
    private static final int Count = 30;
    private boolean isLoading;
    private View footer;
    private Button mShowBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }

    private void initView() {
        setContentView(R.layout.activity_show);
        lvIi = (ListView)findViewById(R.id.lv_ii);
        mShowBack = (Button) findViewById(R.id.show_back);
//        footer = View.inflate(getApplicationContext(), R.layout.footer, null);
    }

    private void initData() {
        measureDao = new MeasureDao(this);
        new Thread(new Runnable() {

            @Override
            public void run() {
                infos = measureDao.AllMeasureInfos();
//                List<MeasureInfo> partInfos = measureDao.queryPartBlackInfos(offset, Count);
//                infos.addAll(partInfos);
                SystemClock.sleep(200);
                // 更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        measurAdapter = new MeasurAdapter();
//                        lvIi.addFooterView(footer);
                        lvIi.setAdapter(measurAdapter);
//                        lvIi.removeFooterView(footer);
//                        lvBlack.setEmptyView(emptyView);
                    }
                });
            }
        }).start();
    }

    private void initEvent() {
        lvIi.setOnScrollListener(new AbsListView.OnScrollListener() {

            // 状态发生改变调用
            // AbsListView view：滑动的View:可以是ListView或者GridView
            // scrollState:view：滑动状态
            // 1 SCROLL_STATE_IDLE(0):view 没有正在滑动
            // 2 SCROLL_STATE_TOUCH_SCROLL(1):触摸滑动，手指仍然要在屏幕上
            // 3 SCROLL_STATE_FLING(2)：快速滑动
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//				L.d("scrollState:"+scrollState);
                // 怎么才知道滑动底部？？？
                // 最后一个可见项等于Adapter的Item的大小减一
                int lastVisiblePosition = lvIi.getLastVisiblePosition();
                if(scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE && (lastVisiblePosition==(measurAdapter.getCount()-1)) && !isLoading){

                    // 显示加载进度条
                    isLoading=true;
//                    lvIi.addFooterView(footer);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 加载数据
                            offset=infos.size();
                            List<MeasureInfo> partInfos = measureDao.queryPartBlackInfos(offset, Count);
                            infos.addAll(partInfos);
                            SystemClock.sleep(3000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if(measurAdapter!=null){
                                        measurAdapter.notifyDataSetChanged();
                                    }
                                    isLoading=false;
//                                    if (lvIi.getFooterViewsCount() > 0) {
//                                        lvIi.removeFooterView(footer);
//                                    }
                                    // 更新UI
                                    // 加载完数据：	代表没有再加载
                                }
                            });
                        }
                    }).start();
                }

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//				L.d("firstVisibleItem:"+firstVisibleItem+";visibleItemCount:"+visibleItemCount+";totalItemCount:"+totalItemCount);
            }
        });

    }

    class MeasurAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int position) {
            return infos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getApplicationContext(), R.layout.view_lv_measure, null);
                convertView.setTag(holder);
                holder.tvIiDay = (TextView) convertView.findViewById(R.id.tv_view_day);
                holder.tvIiTime = (TextView) convertView.findViewById(R.id.tv_view_time);
                holder.tvIiHight = (TextView) convertView.findViewById(R.id.tv_view_hight);
                holder.tvIiLow = (TextView) convertView.findViewById(R.id.tv_view_low);
                holder.tvIiPules = (TextView) convertView.findViewById(R.id.tv_view_pules);
//                holder.tvIiRemark = (TextView) convertView.findViewById(R.id.tv_view_remark);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            measureInfo = infos.get(position);
            holder.tvIiDay.setText(measureInfo.day+" ");
            holder.tvIiTime.setText(measureInfo.time);
            holder.tvIiHight.setText(measureInfo.high_handed+"/");
            holder.tvIiLow.setText(measureInfo.low_handed+"");
            holder.tvIiPules.setText(measureInfo.pules+"");
//            holder.tvIiRemark.setText("医嘱："+measureInfo.remark);

            return convertView;
        }
    }

    class ViewHolder {
        public TextView tvIiDay;
        public TextView tvIiTime;
        public TextView tvIiHight;
        public TextView tvIiLow;
        public TextView tvIiPules;
        public TextView tvIiRemark;
    }

    public void iibacke(View view){
        finish();
    }
}
