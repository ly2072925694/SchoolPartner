package com.cpu.adapter;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.cpu.adapter.PublishSimpleAdapter.ViewHolder;
import com.cpu.constant.GetUserInformation;
import com.example.schoolpartner.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class SpinnerBaseAdapter extends BaseAdapter {
	private ArrayList<String> list = new ArrayList<String>();
	private Context context;
	public SpinnerBaseAdapter(Context context) {
		this.context = context;
		for(int i = 0;i<GetUserInformation.m.length;i++){
			list.add(GetUserInformation.m[i]);
		}
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView)
        {
			holder = new ViewHolder();
			LayoutInflater flater = LayoutInflater.from(context);
			convertView = flater.inflate(R.layout.spinner, null);
			holder.textView = (TextView) convertView.findViewById(R.id.textView1);
            convertView.setTag(holder);
        }else{

            holder = (ViewHolder) convertView.getTag();
        }
		holder.textView.setText(""+list.get(position));
		return convertView;
	}
	class ViewHolder {
		public TextView textView;

		public TextView getTextView() {
			return textView;
		}

		public void setTextView(TextView textView) {
			this.textView = textView;
		}
		
	}
}
