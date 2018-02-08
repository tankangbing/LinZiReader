package com.hn.linzi.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hn.linzi.R;
import com.hn.linzi.activity.NetActivity;
import com.hn.linzi.activity.PublicClassContentActivity.UrlData;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class KeChengDirUrlAdapter extends BaseExpandableListAdapter  {

    private Context context;
    private ArrayList<String> father;
    private List<List<UrlData>> chilerd;
    
    public KeChengDirUrlAdapter (Context context, ArrayList<String> faterList, List<List<UrlData>> childList) {
		super();
		this.context = context;
		this.father = faterList;
		this.chilerd = childList;
	}
	
    @Override
	public Object getChild(int groupPosition, int childPosition) {
		return chilerd.get(groupPosition).get(childPosition);   //获取父类下面的每一个子类项
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;  //子类位置
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) { //显示子类数据的view
		View view = null;
		view = LayoutInflater.from(context).inflate(
				R.layout.kc_contenturl, null);
		TextView textView = (TextView) view
				.findViewById(R.id.kccontenturl_name);
		textView.setText(chilerd.get(groupPosition).get(childPosition).name);
		final String url = chilerd.get(groupPosition).get(childPosition).url;
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				Intent intent = new Intent();
				bundle.putString("url", url);
				intent.putExtras(bundle);
				intent.setClass(context, NetActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
			}
		});
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return chilerd.get(groupPosition).size();  //子类item的总数
	}

	@Override
	public Object getGroup(int groupPosition) {   //父类数据
		return father.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return father.size();  ////父类item总数
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;   //父类位置
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.kc_contenturl, null);
		TextView textView = (TextView) view.findViewById(R.id.kccontenturl_name);
		textView.setText(father.get(groupPosition));
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {  //点击子类触发事件
		

		return true;
	}

}
