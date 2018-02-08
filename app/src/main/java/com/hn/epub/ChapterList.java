package com.hn.epub;

import java.util.List;

import com.hn.linzi.R;
import com.hn.linzi.R.color;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;

/**   
* @Description: 用于操作章节和书签
*/ 
public class ChapterList extends Activity {

	TableLayout toptable;
	TableLayout table;
	TableLayout table2;
	TextView tv1,tv2;
	DisplayMetrics dm; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chapter_list);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dm =getResources().getDisplayMetrics(); 
		toptable = (TableLayout)this.findViewById(R.id.chaptertable);
		table = (TableLayout)this.findViewById(R.id.listtable);
		table2 = (TableLayout)this.findViewById(R.id.listtable2);
		
		TableLayout.LayoutParams lp2 = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp2.width=dm.widthPixels-20;
		table.setLayoutParams(lp2);
		table2.setLayoutParams(lp2);
		
		toptable.setBackgroundColor(Color.parseColor("#cccccc"));
		table.setBackgroundColor(Color.parseColor("#cccccc"));
		table2.setBackgroundColor(Color.parseColor("#cccccc"));
		
		tv1 = (TextView)this.findViewById(R.id.tv1);
		tv1.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
					showchapter();
				 }
			});			    
		
		tv2 = (TextView)this.findViewById(R.id.tv2);
		tv2.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setBackgroundColor(color.bookmark1);
					showbookmark();
				 }
			});	    		
 
    	showchapter();		
	}
	
	/**   
	* @Description: 显示章节
	*/ 
	public void showchapter()
	{		
		tv1.setBackgroundColor(Color.parseColor("#6699cc"));
		tv1.setTextColor(Color.WHITE);
		tv2.setBackgroundColor(Color.WHITE);
		tv2.setTextColor(Color.BLACK);
		
		table.setVisibility(View.VISIBLE);
        table.removeAllViews(); 
        table2.removeAllViews();
        table2.setVisibility(View.GONE);
        
        List titles=Turnmain3.chapterName;
        for (int i=0; i < titles.size(); i++) {     
        	final int i2=i;
    		TableRow row = new TableRow(this);
    		row.setBackgroundColor(Color.WHITE);
    		
    		TableLayout.LayoutParams lp = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    		lp.setMargins(0, 1, 0, 1);    		
	    	row.setLayoutParams(lp);	 
	    	row.setPadding(10, 20, 10, 20);
	    	TextView tv = new TextView(this);
	    	
	    	String title=(String)titles.get(i);
	    	if(title.length()>16)
	    		title=title.substring(0, 16);
	    	tv.setText(title);
	    	tv.setTextSize(20);
	    	tv.setTextColor(Color.BLACK);	
	    	
	    	row.setOnClickListener( new OnClickListener() {
				@Override
				public void onClick(View v) {						
						Turnmain3.openchapter(i2,0);
					    ChapterList.this.finish();
					 }
				});	    		
	    	row.addView(tv);
	    	table.addView(row);
        }	       
	}
	/**   
	* @Description: 显示书签
	*/ 
	public void showbookmark()
	{
		tv2.setBackgroundColor(Color.parseColor("#6699cc"));
		tv2.setTextColor(Color.WHITE);
		tv1.setBackgroundColor(Color.WHITE);
		tv1.setTextColor(Color.BLACK);
		
        table.removeAllViews();
        table.setVisibility(View.GONE);
        table2.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = getSharedPreferences("reader", Activity.MODE_PRIVATE); 
		String str1 = sharedPreferences.getString("bookmark1", "");
		
		String[] data=str1.split("\\|");
        

            for (int i = data.length-1; i>=0; i--) 
            {           
            	if(data[i].length()==0)
            		continue;
        		TableRow row = new TableRow(this);
        		row.setBackgroundColor(Color.WHITE);
    	    	TableLayout.LayoutParams lp = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        		lp.setMargins(0, 1, 0, 1);
    	    	row.setLayoutParams(lp);	 
    	    	row.setPadding(10, 20, 10, 20);
    	    	
    	    	TextView tv = new TextView(this);
    	    	final String[] listBookmark=data[i].split(" ");
    	    	int bklen=0;
    	    	String bookname =listBookmark[0];
    	    	if(!bookname.equals(Turnmain3.bookid))
    	    		continue;
    	    	int chapterNum =Integer.parseInt(listBookmark[1]);
    	    	int len=Integer.parseInt(listBookmark[2]);
    	    	for(int j=0;j<chapterNum;j++)
    	    		bklen+=Turnmain3.ssb1[j].length();

    	    	String title="";
    	    	float per=(bklen+len)*100.0f/Turnmain3.totalLen;
    			if(per==0)
    				title="0.0%";
    			else
    			{
    				java.text.DecimalFormat df=new java.text.DecimalFormat("#.#");		
    				title=df.format(per)+"%";
    			}     	    	
    			
    	    	title+=" "+listBookmark[3]+"\r\n"+Turnmain3.ssb1[chapterNum].subSequence(len, len+50);
    	    	SpannableStringBuilder sb = new SpannableStringBuilder(title);  
    	    	ForegroundColorSpan fcs = new ForegroundColorSpan(Color.GRAY); 
    	    	//AbsoluteSizeSpan span = new AbsoluteSizeSpan(20);  

    	    	sb.setSpan(fcs, 0, 16, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    	    	//sb.setSpan(span, 0, 16, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    	    	
    	    	tv.setWidth(dm.widthPixels);
    	    	tv.setBackgroundColor(Color.WHITE);
    	    	tv.setPadding(0, 0, 10, 0);
    	    	tv.setText(sb);
    	    	tv.setTextSize(14);
    	    	tv.setTextColor(Color.BLACK);    	    	
    	    	 
    	    	row.setOnClickListener( new OnClickListener() {
    				@Override
    				public void onClick(View v) {
	    					Turnmain3.openchapter(Integer.parseInt(listBookmark[1]),Integer.parseInt(listBookmark[2]));
						    ChapterList.this.finish();
    					 }
    				});	    		
    	    	row.addView(tv);
    	    	table2.addView(row);
            
            }
        
	}
}
