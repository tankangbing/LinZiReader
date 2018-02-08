package com.hn.linzi.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.hn.linzi.R;
import com.hn.linzi.activity.ShuJiaActivity;
import com.hn.linzi.views.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

/**
 * @author chenzheng_java
 * @description 该类的部分实现模仿了SimpleAdapter
 */
public class ListAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, Object>> data;
	/**
	 * LayoutInflater 类是代码实现中获取布局文件的主要形式 LayoutInflater layoutInflater =
	 * LayoutInflater.from(context); View convertView =
	 * layoutInflater.inflate();
	 * LayoutInflater的使用,在实际开发种LayoutInflater这个类还是非常有用的,它的作用类似于 findViewById(),
	 * 不同点是LayoutInflater是用来找layout下xml布局文件，并且实例化！ 而findViewById()是找具体xml下的具体
	 * widget控件(如:Button,TextView等)。
	 */
	private LayoutInflater layoutInflater;
	private Context context;
	private int num = 0;

	public ListAdapter(Context context, ArrayList<HashMap<String, Object>> data) {

		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
	}

	/**
	 * 获取列数
	 */
	public int getCount() {
		return data.size();
	}

	/**
	 * 获取某一位置的数据
	 */
	public Object getItem(int position) {
		return data.get(position);
	}

	/**
	 * 获取唯一标识
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * android绘制每一列的时候，都会调用这个方法
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		List list = null;
		if (convertView == null) {
			list = new List();
			// 获取组件布局
			convertView = layoutInflater.inflate(R.layout.list, null);

			list.button1 = (Button) convertView.findViewById(R.id.list_btn1);
			list.button2 = (Button) convertView.findViewById(R.id.list_btn2);
			list.button3 = (Button) convertView.findViewById(R.id.list_btn3);
			// 这里要注意，是使用的tag来存储数据的。
			convertView.setTag(list);
		} else {
			list = (List) convertView.getTag();
		}
		// 绑定数据、以及事件触发
		num++;
		list.button1.setText("分类" + num);
		list.button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, ShuJiaActivity.class);
				context.startActivity(intent);
			}
		});
		num++;
		list.button2.setText("分类" + num);
//		list.button2.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				System.out.println(num);
//			}
//		});
		num++;
		list.button3.setText("分类" + num);
//		list.button3.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				System.out.println(num);
//			}
//		});
		return convertView;
	}

	// /**
	// *当用户点击按钮时触发的事件，会弹出一个确认对话框
	// */
	// public void showInfo(){
	//
	// new AlertDialog.Builder(context)
	//
	// .setTitle("我的listview")
	//
	// .setMessage("介绍...")
	//
	// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	//
	// public void onClick(DialogInterface dialog, int which) {
	//
	// }
	//
	// })
	//
	// .show();
	//
	//
	//
	// }

}
