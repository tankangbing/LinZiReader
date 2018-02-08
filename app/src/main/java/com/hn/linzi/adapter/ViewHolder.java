package com.hn.linzi.adapter;

import java.util.HashMap;

import com.hn.linzi.R;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ViewHolder
{

	public static final int KEY_URL = 0;
	public static final int KEY_SPEED = 1;
	public static final int KEY_PROGRESS = 2;
	public static final int KEY_IS_PAUSED = 3;

	public TextView titleText;
	public ProgressBar progressBar;
//	public TextView speedText;
	public Button pauseButton;
	public Button deleteButton;
	public Button continueButton;
	private String name;
	private boolean hasInited = false;

	public ViewHolder(View parentView, String name)
	{
		if (parentView != null)
		{
			this.name = name;
			titleText = (TextView) parentView.findViewById(R.id.dlitem_title);
//			speedText = (TextView) parentView.findViewById(R.id.speed);
			progressBar = (ProgressBar) parentView
					.findViewById(R.id.dlmanage_bar);
			pauseButton = (Button) parentView.findViewById(R.id.btn_pause);
			deleteButton = (Button) parentView.findViewById(R.id.btn_delete);
			continueButton = (Button) parentView
					.findViewById(R.id.btn_start);
			hasInited = true;
		}
	}

	public static HashMap<Integer, String> getItemDataMap(String url,
			String speed, String progress, String isPaused)
	{
		HashMap<Integer, String> item = new HashMap<Integer, String>();
		item.put(KEY_URL, url);
		item.put(KEY_SPEED, speed);
		item.put(KEY_PROGRESS, progress);
		item.put(KEY_IS_PAUSED, isPaused);
		return item;
	}

	public void setData(HashMap<Integer, String> item)
	{
		if (hasInited)
		{
			
			titleText.setText(name);
//			speedText.setText(item.get(KEY_SPEED));
			String progress = item.get(KEY_PROGRESS);
			if (TextUtils.isEmpty(progress))
			{
				progressBar.setProgress(0);
			} else
			{
				progressBar.setProgress(Integer.parseInt(progress));
			}
			if (Boolean.parseBoolean(item.get(KEY_IS_PAUSED)))
			{
				onPause();
			}
		}
	}

	public void onPause()
	{
		if (hasInited)
		{
			pauseButton.setVisibility(View.GONE);
			continueButton.setVisibility(View.VISIBLE);
		}
	}

	public void setData(String url, String speed, String progress)
	{
		setData(url, speed, progress, false + "");
	}

	public void setData(String url, String speed, String progress,
			String isPaused)
	{
		if (hasInited)
		{
			HashMap<Integer, String> item = getItemDataMap(url, speed,
					progress, isPaused);
			titleText.setText(name);
//			speedText.setText(speed);
			if (TextUtils.isEmpty(progress))
			{
				progressBar.setProgress(0);
			} else
			{
				progressBar
						.setProgress(Integer.parseInt(item.get(KEY_PROGRESS)));
			}

		}
	}

}
