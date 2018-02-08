package com.hn.epub;

import com.hn.linzi.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.SeekBar;
/**   
* @Description: 跳转进度条菜单
*/ 
public class SBjump extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sbjump);
				
		LayoutParams p = getWindow().getAttributes(); 
		p.height = MenuBottomActivity.dip2px(this,100); 
		p.width = LayoutParams.MATCH_PARENT;
		p.gravity = Gravity.BOTTOM; 
		
		SeekBar seekbar1 = (SeekBar)findViewById(R.id.seekBar1);//进度
		seekbar1.setMax(100);
		
		int bklen=0;
    	for(int j=0;j<Turnmain3.curChapter;j++)
    		bklen+=Turnmain3.ssb1[j].length();
    	    	
		float per=(bklen+Turnmain3.beginPos)*100.0f/Turnmain3.totalLen;
		seekbar1.setProgress((int)per);
		seekbar1.setOnSeekBarChangeListener(new sbListener1());		
	}
	
	@SuppressLint("WrongCall")
	public class sbListener1 implements SeekBar.OnSeekBarChangeListener {//调整进度
	    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	    	
	    	float pos1=Turnmain3.totalLen*progress/100f;
			int chapter=0;
			for(int i=0;i<Turnmain3.ssb1.length;i++)
			{
				if(pos1>Turnmain3.ssb1[i].length())
				{
					pos1=pos1-Turnmain3.ssb1[i].length();
					chapter+=1;
				}
				else
					break;
			}
			if(chapter==6&&pos1>=Turnmain3.ssb1[6].length())
			{
				return;
			}
			Turnmain3.openchapter(chapter, (int)pos1);
	    		
	    }
	    public void onStartTrackingTouch(SeekBar seekBar) {}
	    public void onStopTrackingTouch(SeekBar seekBar) {}
	}
}
