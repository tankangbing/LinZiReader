package com.hn.linzi.views;

/**
 * Created by Administrator on 2017/6/9 0009.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import lecho.lib.hellocharts.listener.DummyLineChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.provider.LineChartDataProvider;
import lecho.lib.hellocharts.renderer.LineChartRenderer;
import lecho.lib.hellocharts.view.AbstractChartView;

public class LineChartView extends AbstractChartView implements LineChartDataProvider {
    private static final String TAG = "LineChartView";
    protected LineChartData data;
    protected LineChartOnValueSelectListener onValueTouchListener;

    public LineChartView(Context context) {
        this(context, (AttributeSet)null, 0);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.onValueTouchListener = new DummyLineChartOnValueSelectListener();
        this.setChartRenderer(new LineChartRenderer(context, this, this));
        this.setLineChartData(LineChartData.generateDummyData());
    }

    public LineChartData getLineChartData() {
        return this.data;
    }

    public void setLineChartData(LineChartData data) {
        if(null == data) {
            this.data = LineChartData.generateDummyData();
        } else {
            this.data = data;
        }

        super.onChartDataChange();
    }

    public ChartData getChartData() {
        return this.data;
    }

    public void callTouchListener() {
        SelectedValue selectedValue = this.chartRenderer.getSelectedValue();
        if(selectedValue.isSet()) {
            PointValue point = (PointValue)((Line)this.data.getLines().get(selectedValue.getFirstIndex())).getValues().get(selectedValue.getSecondIndex());
            this.onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), point);
        } else {
            this.onValueTouchListener.onValueDeselected();
        }

    }

    public LineChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(LineChartOnValueSelectListener touchListener) {
        if(null != touchListener) {
            this.onValueTouchListener = touchListener;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
