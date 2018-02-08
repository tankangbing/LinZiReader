package com.hn.linzi.activity;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hn.linzi.R;
import com.hn.linzi.db.MeasureDao;
import com.hn.linzi.utils.Constants;
import com.hn.linzi.utils.PreferencesUtils;
import com.hn.linzi.views.BaseActivity;
import com.hn.linzi.views.ResultDialogg;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/5/18 0018.
 */

public class MeasureActivity extends BaseActivity implements View.OnClickListener{
    private TextView tvMeasureDay,tvMeasureTime;
    private Calendar c;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private Button ivPre;
    private EditText HighHanded;
    private EditText LowHanded;
    private EditText Pules;
    private EditText HighHandedTwo,LowHandedTwo,PulesTwo;
    private EditText Remark;
    private Button tvSave;
    private MeasureDao measureDao;
    private GestureDetector mGestureDetector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);
        initView();
        initDate();
        hua();
        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }

    private void hua() {
        mGestureDetector=new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            // 按下去第一点：e1
            // 快速滑动结束点：e2
            // velocityX：x轴方向速度
            // velocityY：y轴方向速度
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                int x1=(int) e1.getX();
                int x2=(int) e2.getX();
                if(x1-x2>50){
                    // 下一页
//                    Next();
                    return true;// true：消费
                }

                if(x2-x1>50){
                    // 上一页
//                    Pre();
                    return true;// true：消费
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    private void initView() {
        tvMeasureDay = (TextView)findViewById(R.id.tv_mreasure_day);
        tvMeasureTime = (TextView)findViewById(R.id.tv_mreasure_time);
        HighHanded = (EditText) findViewById(R.id.edt_height_handed);//高血压
        LowHanded = (EditText) findViewById(R.id.edt_low_handed);//低血压
        Pules = (EditText) findViewById(R.id.edt_pules);//心率
        HighHandedTwo = (EditText) findViewById(R.id.edt_height_handedtwo);//高血压
        LowHandedTwo = (EditText) findViewById(R.id.edt_low_handedtwo);//低血压
        PulesTwo = (EditText) findViewById(R.id.edt_pulestwo);//心率
//        Remark = (EditText)findViewById(R.id.edt_remark);edt_remark
        ivPre = (Button)findViewById(R.id.measure_back);
        tvSave = (Button)findViewById(R.id.tv_mreasure_save);


        ivPre.setOnClickListener(this);
        tvMeasureDay.setOnClickListener(this);
        tvMeasureTime.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        measureDao = new MeasureDao(this);
    }
    private void initDate() {
        c = Calendar.getInstance();
        //获取系统的日期
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);
        String omonth = month + "";
        String oday = day + "";
        if(month<10){
            omonth = "0" + month;
        }
        if(day<10){
            oday = "0" + day;
        }
        tvMeasureDay.setText(year+"-"+omonth+"-"+oday);
        //获取系统的时间
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        String shour = hour + "";
        String hminute = minute + "";
        if(hour<10){
            shour = "0" + hour;
        }
        if(minute<10){
            hminute = "0" + minute;
        }
        tvMeasureTime.setText(shour+":"+hminute);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.measure_back:
                //回退
                Pre();
                break;
            case R.id.tv_mreasure_day:
                //设置日期
                GetDay();
                break;
            case R.id.tv_mreasure_time:
                //设置时间
                GetTime();
                break;
            case R.id.tv_mreasure_save:
                //保存
                Save();
                break;
            default:
                break;
        }
    }

    private void Pre() {
       finish();
    }

    private void GetDay() {
        // 创建一个TimePickerDialog实例，并把它显示出来
        new DatePickerDialog(MeasureActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String omonth = month + "";
                String oday = dayOfMonth + "";
                if(month<10){
                    month = month+1;
                    omonth = "0" + month;
                }
                if(dayOfMonth<10){
                    oday = "0" + dayOfMonth;
                }
//                int i = Integer.parseInt(oday);
//                day = dayOfMonth;
                tvMeasureDay.setText(new StringBuffer().append(year).append("-").append(omonth).append("-").append(oday));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void GetTime() {
        // 创建一个TimePickerDialog实例，并把它显示出来
        new TimePickerDialog(MeasureActivity.this,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    //设置时间
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String s = minute + "";
                        String hour = hourOfDay + "";
                        if(minute<10){
                            s = "0" + minute;
                        }
                        if(hourOfDay<10){
                            hour = "0" + hourOfDay;
                        }
                        tvMeasureTime.setText( hour +":" + s);
                    }
                }
                // 设置初始时间
                , c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                // true表示采用24小时制
                true).show();
    }

    private void Save() {
        PreferencesUtils.putBoolean(getApplicationContext(), Constants.IS_CE,true);
        PreferencesUtils.putBoolean(getApplicationContext(), Constants.IS_QU,true);
        String getDay = tvMeasureDay.getText().toString();
        String getTime = tvMeasureTime.getText().toString();
        String GetHighHanded = HighHanded.getText().toString();
        String GetLowHanded = LowHanded.getText().toString();
        String getPules = Pules.getText().toString();

        //第二次
        String GetHighHandedTwo = HighHandedTwo.getText().toString();
        String GetLowHandedTwo = LowHandedTwo.getText().toString();
        String getPulesTwo = PulesTwo.getText().toString();
//        String getRemark = Remark.getText().toString();
        if(TextUtils.isEmpty(getDay)|| TextUtils.isEmpty(getTime)|| TextUtils.isEmpty(GetHighHanded)|| TextUtils.isEmpty(GetLowHanded)
                || TextUtils.isEmpty(getPules)|| TextUtils.isEmpty(GetHighHandedTwo)|| TextUtils.isEmpty(GetLowHandedTwo)|| TextUtils.isEmpty(getPulesTwo)){
            Toast.makeText(getApplicationContext(), "信息未填完", Toast.LENGTH_SHORT).show();
            return;
        }
        int intHighHanded = Integer.parseInt(GetHighHanded);
        int intHighHandedTwo = Integer.parseInt(GetHighHandedTwo);
        if( intHighHanded > 250||intHighHandedTwo>250){
            Toast.makeText(getApplicationContext(), "收缩压过大", Toast.LENGTH_SHORT).show();
            return;
        }
        int intLowHanded = Integer.parseInt(GetLowHanded);
        int intLowHandedTwo = Integer.parseInt(GetLowHandedTwo);
        if(intLowHanded<35||intLowHandedTwo<35){
            Toast.makeText(getApplicationContext(), "舒张压过小", Toast.LENGTH_SHORT).show();
            return;
        }
        if(intLowHanded>intHighHanded||intLowHandedTwo>intHighHandedTwo){
            Toast.makeText(getApplicationContext(), "舒张压不能大于收缩压", Toast.LENGTH_SHORT).show();
            return;
        }
        int intPules= Integer.parseInt(getPules);
        int intPulesTwo= Integer.parseInt(getPulesTwo);

        if(intPules<30||intPulesTwo<30){
            Toast.makeText(getApplicationContext(), "脉搏过小", Toast.LENGTH_SHORT).show();
            return;
        }
        if(intPules>200||intPulesTwo>200){
            Toast.makeText(getApplicationContext(), "脉搏过大", Toast.LENGTH_SHORT).show();
            return;
        }
        int Hight = (intHighHanded + intHighHandedTwo) / 2;
        int low = (intLowHanded + intLowHandedTwo) / 2;
        int pules = (intPules + intPulesTwo) / 2;
        int i = 0;
        String remark = null;
        if(Hight < 90){
            i = 1;
            remark = "您的的血压测量值偏低，建议均衡营养，坚持锻炼，改善体质，祝好心情!!!";
        }else if(Hight >= 90&& Hight <= 130){
            i = 2;
            remark = "您的的血压测量值正常，请继续保持当前的健康生活方式，祝好心情!!!";
        }else if(Hight >130 && Hight <= 140){
            i = 3 ;
            remark = "您的的血压测量值正常稍高，请采取的健康生活方式，戒烟限酒，限制钠盐的摄入，加强锻炼。祝好心情!!!";
        }else if(Hight >140 && Hight <= 159){
            i = 4;
            remark = "您的的血压测量值正常偏高，请戒烟限酒，限制钠盐的摄入，加强锻炼。祝好心情!!!";
        }else if(Hight >159 && Hight <= 179){
            i = 5;
            remark = "您的的血压测量值正常过高，请严格调整作息，控制饮食。身体不适，请及时就诊。";
        }else if(Hight >179){
            i = 6;
            remark = "您的的血压测量值严重偏高，请及时就诊，配合治疗。";
        }

//        for(int i = 0;i<37;i++){
//        measureDao.add(getDay,getTime,Hight,low,pules,null);
//        }
//        finish();
        ResultWindow(getDay,getTime,Hight,low,pules,remark,i);
    }

    private void ResultWindow(final String getDay, final String getTime, final int hight, final int low, final int pules, final String remark, int i) {
        final ResultDialogg resultDialogg = new ResultDialogg(MeasureActivity.this, R.style.Theme_dialog);
        resultDialogg.show();
        resultDialogg.setDay(getDay);
        resultDialogg.setTime(getTime);
        resultDialogg.setHight(hight+"");
        resultDialogg.setLight(low+"");
        resultDialogg.setXin(pules+"");
        resultDialogg.setIdea(remark);
        resultDialogg.setSet(i);
        resultDialogg.setcanselClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resultDialogg.dismiss();
//                finish();
            }
        });
        resultDialogg.setSaveClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                measureDao.add(getDay,getTime,hight,low,pules,remark);
                resultDialogg.dismiss();
                Intent intent=new Intent(MeasureActivity.this,CurveActivity.class);
                intent.putExtra("time", getTime);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
