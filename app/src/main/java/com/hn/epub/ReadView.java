package com.hn.epub;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;
/**   
* @Description: 阅读正文的textview，实现测量文字数量、行数功能
*/ 
public class ReadView extends TextView {

	public int chapter=0;//当前章节，0开始
	public int beginPos=0;//文字开始位置
	public int endPos=0;
	Context c;
	public ReadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		c=context;
	}

	public ReadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		c=context;
	}

	public ReadView(Context context) {
		super(context);
		c=context;
	}

	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		
		resize();
	}
	
	/**
	 *去除当前页无法显示的字
     * @return 去掉的字数
	 */
	public int resize() {
		CharSequence oldContent = getText();
		CharSequence newContent = oldContent.subSequence(0, getCharNum());
		setText(newContent);
		return oldContent.length() - newContent.length();
	}
	
	/**
	 * 获取当前页总字数
	 */
	public int getCharNum() {
		if(getLayout()!=null)
			return getLayout().getLineEnd(getLineNum());
		else
			return 0;
	}
	
	/**
	 * 获取当前页总行数
	 */
	public int getLineNum() {
		Layout layout = getLayout();
		int topOfLastLine = getHeight() - getPaddingTop() - getPaddingBottom() - getLineHeight();
		return layout.getLineForVertical(topOfLastLine);
	}

}
