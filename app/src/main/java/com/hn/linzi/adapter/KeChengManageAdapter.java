package com.hn.linzi.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.aphidmobile.utils.UI;
import com.hn.linzi.R;
import com.hn.linzi.utils.KeChengDBHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class KeChengManageAdapter extends BaseAdapter {
	
	private Context context;
	private ListView listView;
	private KeChengDBHelper helper;
	private ArrayList<HashMap<String, String>> dataList;
	private SharedPreferences sp;
	
	public KeChengManageAdapter(Context context, ListView listView) {
		super();
		this.context = context;
		this.listView = listView;
		helper = new KeChengDBHelper(context);
		dataList = new ArrayList<HashMap<String, String>>();
		sp = context.getSharedPreferences("SP",
				Context.MODE_PRIVATE);
		Cursor cursor = helper.select(null, "username=?", new String[]{sp.getString("UserName", "")});
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex("kecheng_name"));
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("name", name);
			dataList.add(item);
		}
		System.out.println(dataList.size());
		cursor.close();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void removeItem(String name)
	{
		String tmp;
		for (int i = 0; i < dataList.size(); i++)
		{
			tmp = dataList.get(i).get("name");
			if (tmp.equals(name))
			{
				dataList.remove(i);
				this.notifyDataSetChanged();
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(
					R.layout.dllist_item, null);
		}
		HashMap<String, String> itemData = dataList.get(position);
		final String name = itemData.get("name");
		
		UI.<ProgressBar>findViewById(convertView, R.id.dlmanage_bar).setVisibility(View.GONE);
		UI.<ImageView>findViewById(convertView, R.id.manageItem_img).setImageResource(R.drawable.sp_icon);
		UI.<TextView>findViewById(convertView, R.id.dlitem_title).setText(name);
		UI.<Button>findViewById(convertView, R.id.btn_delete).setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Dialog dialog = new AlertDialog.Builder(context)
	            .setTitle("删除课程")
	            .setMessage("是否删除该课程")
	            .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	Cursor cursor = helper.select(null, "username=? and kecheng_name=?", new String[]{sp.getString("UserName", ""),name});
	    				cursor.moveToNext();
	    				
	    				helper.delete(cursor.getInt(cursor.getColumnIndex("kecheng_id")));
	    				cursor.close();
	    				removeItem(name);
	                }
	            })//设置确定按钮
	            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                }
	            })//设置取消按钮
	            .create();
				dialog.show();
			}
		});
		
		return convertView;
	}

}
