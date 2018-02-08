package com.hn.linzi.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.linzi.R;


public class ResultDialogg extends Dialog {
    protected static int default_width = WindowManager.LayoutParams.WRAP_CONTENT; // 默认宽度
    protected static int default_height = WindowManager.LayoutParams.WRAP_CONTENT;// 默认高度
    public static int TYPE_TWO_BT = 2;
    public static int TYPE_NO_BT = 0;
    public TextView dialog_title;
    public EditText dialog_message;
    protected Context mContext;
    private View.OnClickListener listener;
    private View customView;
    //	@Bind(R.id.icon)
    ImageView icon;
    private TextView mTvPpDay;
    private TextView mTvPpTime;
    private TextView mTvPpHigt;
    private TextView mTvPpLight;
    private TextView mTvPpXin;
    private TextView mTvPpOne;
    private TextView mTvPpTwo;
    private TextView mTvPpThree;
    private TextView mTvPpFoure;
    private TextView mTvPpSix;
    private TextView mTvPpFri;
    private Button mBtn_cancel;
    private Button mBtn_save;
    private final TextView mTvIdea;
    private final Button mButCansel;
    private final Button mButSave;

    public ResultDialogg(Context context, int style) {
        super(context, R.style.FullScreenDialog);
        mContext = context;
        customView = LayoutInflater.from(context).inflate(R.layout.popup_measure, null);

        mTvPpDay = (TextView) customView.findViewById(R.id.pp_day);
        mTvPpTime = (TextView) customView.findViewById(R.id.pp_time);
        mTvPpHigt = (TextView) customView.findViewById(R.id.pp_higt);
        mTvPpLight = (TextView) customView.findViewById(R.id.pp_light);
        mTvPpXin = (TextView) customView.findViewById(R.id.pp_xin);
        mTvIdea = (TextView)customView.findViewById(R.id.pp_idea);
        mTvPpOne = (TextView) customView.findViewById(R.id.pp_one);
        mTvPpTwo = (TextView) customView.findViewById(R.id.pp_two);
        mTvPpThree = (TextView) customView.findViewById(R.id.pp_three);
        mTvPpFoure = (TextView) customView.findViewById(R.id.pp_foure);
        mTvPpFri = (TextView) customView.findViewById(R.id.pp_fri);
        mTvPpSix = (TextView) customView.findViewById(R.id.pp_six);
        mButCansel = (Button) customView.findViewById(R.id.pp_cancel);
        mButSave = (Button) customView.findViewById(R.id.pp_save);
//        setTitle("提示信息");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(customView);
        //ButterKnife  view绑定
        //ButterKnife.bind(this,customView);
    }

    public ResultDialogg setcanselClickListener(View.OnClickListener listener) {
        this.listener = listener;
        mButCansel.setOnClickListener(listener);
        return this;
    }

    public ResultDialogg setSaveClickListener(View.OnClickListener listener) {
        this.listener = listener;
        mButSave.setOnClickListener(listener);
        return this;
    }

    public ResultDialogg setDay(String day) {
        mTvPpDay.setText(day);
        return this;
    }

    public ResultDialogg setTime(String time) {
        mTvPpTime.setText(time);
        return this;
    }

    public ResultDialogg setHight(String hight) {
        mTvPpHigt.setText(hight);
        return this;
    }

    public ResultDialogg setLight(String light) {
        mTvPpLight.setText(light);
        return this;
    }
    public ResultDialogg setXin(String xin) {
        mTvPpXin.setText(xin);
        return this;
    }
    public ResultDialogg setIdea(String idea) {
        mTvIdea.setText(idea);
        return this;
    }
    public ResultDialogg setSet(int number) {
        switch (number) {
            case 1:
                mTvPpOne.setVisibility(View.VISIBLE);
                mTvPpTwo.setVisibility(View.INVISIBLE);
                mTvPpThree.setVisibility(View.INVISIBLE);
                mTvPpFoure.setVisibility(View.INVISIBLE);
                mTvPpFri.setVisibility(View.INVISIBLE);
                mTvPpSix.setVisibility(View.INVISIBLE);
                break;
            case 2:
                mTvPpOne.setVisibility(View.INVISIBLE);
                mTvPpTwo.setVisibility(View.VISIBLE);
                mTvPpThree.setVisibility(View.INVISIBLE);
                mTvPpFoure.setVisibility(View.INVISIBLE);
                mTvPpFri.setVisibility(View.INVISIBLE);
                mTvPpSix.setVisibility(View.INVISIBLE);
                break;
            case 3:
                mTvPpOne.setVisibility(View.INVISIBLE);
                mTvPpTwo.setVisibility(View.INVISIBLE);
                mTvPpThree.setVisibility(View.VISIBLE);
                mTvPpFoure.setVisibility(View.INVISIBLE);
                mTvPpFri.setVisibility(View.INVISIBLE);
                mTvPpSix.setVisibility(View.INVISIBLE);
                break;
            case 4:
                mTvPpOne.setVisibility(View.INVISIBLE);
                mTvPpTwo.setVisibility(View.INVISIBLE);
                mTvPpThree.setVisibility(View.INVISIBLE);
                mTvPpFoure.setVisibility(View.VISIBLE);
                mTvPpFri.setVisibility(View.INVISIBLE);
                mTvPpSix.setVisibility(View.INVISIBLE);
                break;
            case 5:
                mTvPpOne.setVisibility(View.INVISIBLE);
                mTvPpTwo.setVisibility(View.INVISIBLE);
                mTvPpThree.setVisibility(View.INVISIBLE);
                mTvPpFoure.setVisibility(View.INVISIBLE);
                mTvPpFri.setVisibility(View.VISIBLE);
                mTvPpSix.setVisibility(View.INVISIBLE);
                break;
            case 6:
                mTvPpOne.setVisibility(View.INVISIBLE);
                mTvPpTwo.setVisibility(View.INVISIBLE);
                mTvPpThree.setVisibility(View.INVISIBLE);
                mTvPpFoure.setVisibility(View.INVISIBLE);
                mTvPpFri.setVisibility(View.INVISIBLE);
                mTvPpSix.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        return this;
    }

    public ResultDialogg setIcon(int iconResId) {
        dialog_title.setVisibility(View.GONE);
        icon.setVisibility(View.VISIBLE);
        icon.setBackgroundResource(iconResId);
        return this;
    }

}
