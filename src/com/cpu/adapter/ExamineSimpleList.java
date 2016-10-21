package com.cpu.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.schoolpartner.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public abstract class ExamineSimpleList extends SimpleAdapter {
	ArrayList<HashMap<String, Object>> publishList;
	
	@SuppressWarnings("unchecked")
	public ExamineSimpleList(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		publishList = (ArrayList<HashMap<String, Object>>) data;
	}
	@Override
	public Object getItem(int position) {
		return publishList.get(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView)
        {
			holder = new ViewHolder();
			convertView = super.getView(position, convertView, parent);
			holder.agree = (TextView) convertView.findViewById(R.id.textView3);
			holder.disagree = (TextView) convertView.findViewById(R.id.textView4);
			
			holder.setAgree(holder.agree);
			holder.setDisagree(holder.disagree);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
		if(publishList.get(position).get("dis").equals("1")){

			holder.agree.setText("");
			holder.disagree.setText("已拒绝");
		}else if(publishList.get(position).get("agree").equals("1")){

			holder.agree.setText("已同意");
			holder.disagree.setText("");
		}else{

			holder.agree.setText("同意");
			holder.disagree.setText("拒绝");
		}
        convert(holder,position);
		return convertView;
	}
	public static class ViewHolder
    {
		TextView agree;
		TextView disagree;
		public TextView getAgree() {
			return agree;
		}
		public void setAgree(TextView agree) {
			this.agree = agree;
		}
		public TextView getDisagree() {
			return disagree;
		}
		public void setDisagree(TextView disagree) {
			this.disagree = disagree;
		}
		
    }
	public abstract void convert(ViewHolder helper,int position); 
}
