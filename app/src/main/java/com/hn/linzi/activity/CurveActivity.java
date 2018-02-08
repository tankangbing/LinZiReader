package com.hn.linzi.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.hn.linzi.R;
import com.hn.linzi.data.MeasureInfo;
import com.hn.linzi.db.MeasureDao;
import com.hn.linzi.views.BaseActivity;
import com.hn.linzi.views.LineChartView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;

/**
 * Created by Administrator on 2017/5/18 0018.
 */

public class CurveActivity extends BaseActivity implements View.OnClickListener{
    private static final int Count = 10;
    private int offset=0;
    private LineChartView lineChart,lineXin;
    private LineChartData data,dataXin;
    private Axis axisX,axisY;
    private TextView tvCurveTime,tvCurveDay;
    private Button tvShangye,tvNextYe;
    private Button ibPre;
    private Calendar c;
    private int year;
    private int month;
    private int day;
    private String getDay,getTime;

    private List<PointValue> mPointHigt = new ArrayList<PointValue>();
    private List<PointValue> mPointLow = new ArrayList<PointValue>();
    private List<PointValue> mPointXin = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private List<MeasureInfo> infos = new ArrayList<MeasureInfo>();

    private List<Integer> mornHight = new ArrayList<Integer>();//早晨
    private List<Integer> mornLow = new ArrayList<Integer>();
    private List<Integer> mornPuse = new ArrayList<Integer>();
    private List<String> mornDay = new ArrayList<String>();

    private MeasureDao measureDao;
    private List<MeasureInfo> dayInfo;
    private GestureDetector mGestureDetector;
    private String[] mItems = new String[] { "早晨(06-08点)","上午(09-11点)", "下午(15-17点)", "夜晚(21-23点)", "每天" };;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        getAxisXLables();//获取x轴和坐标的标注
        initLineChart();//初始化
        hua();//手划动控制上下页
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
                    Next();
                    return true;// true：消费
                }

                if(x2-x1>50){
                    // 上一页
                   Shang();
                    return true;// true：消费
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_curve);
        lineChart = (LineChartView)findViewById(R.id.line_chart);//收缩舒张表
        lineXin = (LineChartView)findViewById(R.id.line_xin);//心率表
        tvCurveTime = (TextView)findViewById(R.id.te_curve_time);//日期
        tvCurveDay = (TextView)findViewById(R.id.te_curve_day);//时间段
        ibPre = (Button)findViewById(R.id.chat_back) ;
        tvShangye = (Button)findViewById(R.id.te_curve_shangye);
        tvNextYe = (Button)findViewById(R.id.te_curve_nextye);

        tvShangye.setOnClickListener(this);
        tvNextYe.setOnClickListener(this);
        ibPre.setOnClickListener(this);
        tvCurveTime.setOnClickListener(this);
        tvCurveDay.setOnClickListener(this);
    }

    private void initData() {
        //测量完点确认传过来的初始数据。
        String measureTime = getIntent().getStringExtra("time");
        if(measureTime!=null){
            //判断时间段
            String measureTime1 = getMeasureTime(measureTime);
            tvCurveDay.setText(measureTime1);

        }
        measureDao = new MeasureDao(this);
        infos = measureDao.AllMeasureInfos();//查询数据库
//        infos =  measureDao.queryPartBlackInfos(offset,Count);
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
        tvCurveTime.setText(year+"-"+omonth+"-"+oday);
        //获取系统的时间
    }

    private String getMeasureTime(String measureTime) {
        String result = null;
        String str = measureTime.substring(0, 2);
        int house = Integer.parseInt(str);
        if(house >= 6 && house <= 8){
            result = mItems[0];
        }else if(house >= 9 && house <= 11){
            result = mItems[1];
        }else if(house >= 15 && house <= 17){
            result = mItems[2];
        }else if(house >= 21 && house <= 23){
            result = mItems[3];
        }else {
            result = mItems[4];
        }
        return result;
    }

    //---------------------------------------------------------------------------------------------------------
    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart(){
        List<Line> lines = new ArrayList<Line>();
        List<Line> listXin = new ArrayList<Line>();
        //收缩压
        Line xin = new Line(mPointXin).setColor(Color.parseColor("#FFFF7391"));  //折线的颜色
        xin.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        xin.setCubic(false);//曲线是否平滑
//	    line.setStrokeWidth(3);//线条的粗细，默认是3
        xin.setFilled(true);//是否填充曲线的面积
        xin.setAreaTransparency(12);
        xin.setHasLabels(false);//曲线的数据坐标是否加上备注
        xin.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        xin.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        xin.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        listXin.add(xin);
        //收缩压
        Line lineHight = new Line(mPointHigt).setColor(Color.parseColor("#FF1CD3AF"));  //折线的颜色
        lineHight.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        lineHight.setCubic(false);//曲线是否平滑
//	    line.setStrokeWidth(3);//线条的粗细，默认是3
        lineHight.setFilled(true);//是否填充曲线的面积
        lineHight.setAreaTransparency(12);
        lineHight.setHasLabels(false);//曲线的数据坐标是否加上备注
        lineHight.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        lineHight.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        lineHight.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(lineHight);
        //舒张压
        Line lineLow = new Line(mPointLow).setColor(Color.parseColor("#FFF2AF3B"));  //折线的颜色
        lineLow.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        lineLow.setCubic(false);//曲线是否平滑
//	    line.setStrokeWidth(3);//线条的粗细，默认是3
        lineLow.setFilled(true);//是否填充曲线的面积
        lineLow.setAreaTransparency(50);
        lineLow.setHasLabels(false);//曲线的数据坐标是否加上备注
        lineLow.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        lineLow.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        lineLow.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(lineLow);

        data = new LineChartData();//收缩舒展压
        dataXin = new LineChartData();//心率
        dataXin.setLines(listXin);
        data.setLines(lines);
        //坐标轴
        axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//	    axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextColor(Color.parseColor("#D6D6D9"));//灰色

        //设置X轴
//	    axisX.setName("时间");  //表格名称
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(5); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的
          //填充X轴的  //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部

        dataXin.setAxisXBottom(axisX); //x 轴在底部
//	    data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(false); //x 轴分割线
        //设置Y轴
       axisY = new Axis();  //Y轴
//        axisY.setName("血压图");//y轴标注
        axisY.setTextSize(11);//设置字体大小
        axisY.setHasLines(true);//y 轴分割线
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        dataXin.setAxisYLeft(axisY);
        //data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动以及平移

        //=====================
        //=====================

        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        lineChart.setMaxZoom((float) 3);//缩放比例
        lineChart.setLineChartData(data);
//        lineChart.setZoomEnabled(true);
        lineChart.setVisibility(View.VISIBLE);

        lineXin.setInteractive(true);
        lineXin.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        lineXin.setMaxZoom((float) 3);//缩放比例
        lineXin.setLineChartData(dataXin);
        lineXin.setVisibility(View.VISIBLE);

        /**注：下面的7，10只是代表一个数字去类比而已
         * 见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         * 下面几句可以设置X轴数据的显示个数（x轴0-7个数据），当数据点个数小于（29）的时候，缩小到极致hellochart默认的是所有显示。当数据点个数大于（29）的时候，
         * 若不设置axisX.setMaxLabelChars(int count)这句话,则会自动适配X轴所能显示的尽量合适的数据个数。
         * 若设置axisX.setMaxLabelChars(int count)这句话,
         * 33个数据点测试，若 axisX.setMaxLabelChars(10);里面的10大于v.right= 7; 里面的7，则
         刚开始X轴显示7条数据，然后缩放的时候X轴的个数会保证大于7小于10
         若小于v.right= 7;中的7,反正我感觉是这两句都好像失效了的样子 - -!
         * 并且Y轴是根据数据的大小自动设置Y轴上限
         * 若这儿不设置 v.right= 7; 这句话，则图表刚开始就会尽可能的显示所有数据，交互性太差
         */
        Viewport mVChar = new Viewport(lineChart.getMaximumViewport());
        mVChar.left = 0;
        mVChar.right= 4;
        lineChart.setCurrentViewport(mVChar);
        Viewport vXin = new Viewport(lineXin.getMaximumViewport());
        vXin.left = 0;
        vXin.right= 4;
        lineXin.setCurrentViewport(vXin);

//        lineChart.setVerticalFadingEdgeEnabled(false);
//         Viewport v = new Viewport(lineChart.getMaximumViewport());
////        Log.e(TAG, "onCreate: "+v.left+"#"+v.top+"#"+v.right+"$"+v.bottom );
//        v.left = 0;
//        v.right= 1;
//        lineChart.setCurrentViewport(v);
//
////        Log.e(TAG, "onCreate: "+v.left+"#"+v.top+"#"+v.right+"$"+v.bottom );
//
//        Viewport maxV=new Viewport(lineChart.getMaximumViewport());
//        maxV.left=0;
//        maxV.right= mornHight.size()-1;
//        lineChart.setMaximumViewport(maxV);
//        lineChart.setZoomEnabled(false);
    }
    /**
     * X 轴的显示
     */
    private void getAxisXLables(){
        getDay = tvCurveDay.getText().toString();
        getTime = tvCurveTime.getText().toString();

        mornHight.clear();
        mornLow.clear();
        mornPuse.clear();
        mornDay.clear();
        switch (getDay) {
            case "早晨(06-08点)":

                getMorn(6,8);
                break;
            case "上午(09-11点)":
                getMorn(9,11);
                break;
            case "下午(15-17点)":
                getMorn(15,17);
                break;
            case "夜晚(21-23点)":
                getMorn(21,23);
                break;
            case "每天":
                getEvery(getTime);
                //日期的设置
                break;
            default:
                break;
        }
        MornX();//X轴赋值
        MornPoints();//画曲线
    }

    private void getMorn(int min, int max) {
        for(int i = 0;i < infos.size(); i ++) {
            MeasureInfo measureInfo = infos.get(i);
            String time = measureInfo.time;
            String str = time.substring(0, 2);
            int house = Integer.parseInt(str);
            if(house>=min&&house<=max){                                     //早晨
                mornHight.add( measureInfo.high_handed);
                mornLow.add(measureInfo.low_handed);
                mornPuse.add(measureInfo.pules);
                mornDay.add(measureInfo.day);

            }
        }
    }
    private void getEvery(String getTime){
        dayInfo = measureDao.DayMeasureInfos(getTime);
        for(int i =0 ;i<dayInfo.size();i++){
            MeasureInfo measureInfo = dayInfo.get(i);
            mornHight.add(measureInfo.high_handed);
            mornLow.add(measureInfo.low_handed);
            mornPuse.add(measureInfo.pules);
            mornDay.add(measureInfo.time);
        }
    }
    //x早
    private void MornX() {
            for(int i = 0;i< 10 && i+offset  <mornDay.size(); i ++){
                mAxisXValues.add(new AxisValue(i).setLabel(mornDay.get(i+offset)));//X轴
            }
    }

    private void MornPoints() {
            for(int i = 0;i < 10 && i+offset <mornHight.size(); i ++){
                mPointHigt.add(new PointValue(i,mornHight.get(i+offset)));//收缩压
                mPointLow.add(new PointValue(i,mornLow.get(i+offset)));//舒张压
                mPointXin.add(new PointValue(i,mornPuse.get(i+offset)));//心率
            }
    }
    //---------------------------------------------------------------------------------------------------

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.te_curve_shangye:
                Shang();
                break;
            case R.id.te_curve_nextye:
                Next();
                break;
            case R.id.te_curve_time:
                //日期的设置
                    setTime();
                break;
            case R.id.te_curve_day:
                //s时间段的设置
                setDay();
                break;
            case R.id.chat_back:
                Pre();
                break;
            default:
                break;
        }
    }

    private void Shang() {

        if(getDay.equals("每天")){
            //上一天
//            Preday(getTime);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date nowDate = null;
            try {
                nowDate = df.parse(getTime);
            } catch (ParseException e) {
                e.printStackTrace();
                }

            Date newDate2 = new Date(nowDate.getTime() - (long)24 * 60 * 60 * 1000);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateOk = simpleDateFormat.format(newDate2);
            tvCurveTime.setText(dateOk);

        }else {
            if(offset>=10){
                offset = offset-10;
            }
        }
        Clear();
        getAxisXLables();
        initLineChart();
    }

    private void Next() {
        if(getDay.equals("每天")){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date nowDate = null;
            try {
                nowDate = df.parse(getTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //如果需要向后计算日期 -改为+
            Date newDate2 = new Date(nowDate.getTime() + (long)24 * 60 * 60 * 1000);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateOk = simpleDateFormat.format(newDate2);
            tvCurveTime.setText(dateOk);
        }else {
            if(offset + 10 < mornHight.size()){
                offset = offset+10;
            }
        }
        Clear();
        getAxisXLables();
        initLineChart();
    }

    //上一天
    private void Preday(String getTime) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(getTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //如果需要向后计算日期 -改为+
        Date newDate2 = new Date(nowDate.getTime() - (long)24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        tvCurveTime.setText(dateOk);

//        getEvery(dateOk);
//        for(int i = 0;i < mornDay.size(); i ++){
//            mAxisXValues.add(new AxisValue(i).setLabel(mornDay.get(i)));//X轴
//        }
//        for(int i = 0;i < mornHight.size(); i ++){
//            mPointHigt.add(new PointValue(i,mornHight.get(i)));//收缩压
//            mPointLow.add(new PointValue(i,mornLow.get(i)));//舒张压
//            mPointXin.add(new PointValue(i,mornPuse.get(i)));//心率
//        }
    }

    private void Pre() {
        finish();
    }

    private void setTime() {
        // 创建一个TimePickerDialog实例，并把它显示出来
        new DatePickerDialog(CurveActivity.this, new DatePickerDialog.OnDateSetListener() {
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
//                day = dayOfMonth;
                tvCurveTime.setText(new StringBuffer().append(year).append("-").append(omonth).append("-").append(oday));
                //当时间改变的时候重新加载
                mPointXin.clear();
                mPointLow.clear();
                mPointHigt.clear();
                mAxisXValues.clear();
                getAxisXLables();
                initLineChart();
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();

    }
    private void setDay() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CurveActivity.this);
        builder.setTitle("请选择时间");
        builder.setSingleChoiceItems(mItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvCurveDay.setText(mItems[which]);
                dialog.dismiss();
                //当时间段改变的时候重新加载

                isEveryDay(mItems[which]);//判断是否“每天”
                Clear();//清除久的曲线痕迹
                offset = 0;
                getAxisXLables();
                initLineChart();

            }
       });
        builder.show();
    }
    //清除久的曲线
    private void Clear(){
        mPointXin.clear();
        mPointLow.clear();
        mPointHigt.clear();
        mAxisXValues.clear();
    }
    private void isEveryDay(String day){
        if(day.equals("每天")){
            tvCurveTime.setVisibility(View.VISIBLE);
//            tvShangye.setVisibility(View.GONE);
//            tvNextYe.setVisibility(View.GONE);
        }else {
            tvCurveTime.setVisibility(View.GONE);
            tvShangye.setVisibility(View.VISIBLE);
            tvNextYe.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}
