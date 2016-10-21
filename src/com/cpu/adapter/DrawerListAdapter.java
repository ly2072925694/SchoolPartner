package com.cpu.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.schoolpartner.R;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public abstract class DrawerListAdapter extends BaseAdapter{
	private View convertView;
	private Context context;
	private ImageView photo;
	private TextView clearText;
	private TextView adviceText;
	private TextView refreshText;
	private TextView bestText;
	private TextView aboutText;
	private TextView logoutText;
	private Intent it;
	private View dialog;
	private List<View> list = new ArrayList<View>();
	public DrawerListAdapter(Context context) {
		this.context = context;
	}
	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (null == convertView)
        {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context)
	        		.inflate(R.layout.drawer_view, null);

			photo = (ImageView) convertView.findViewById(R.id.user);
			clearText = (TextView) convertView.findViewById(R.id.clear);
			adviceText = (TextView) convertView.findViewById(R.id.advice);
			refreshText = (TextView) convertView.findViewById(R.id.refresh);
			bestText = (TextView) convertView.findViewById(R.id.best);
			aboutText = (TextView) convertView.findViewById(R.id.about);
			logoutText = (TextView) convertView.findViewById(R.id.logout);
			
			holder.setAdviceText(adviceText);
			holder.setBestText(bestText);
			holder.setClearText(clearText);
			holder.setLogoutText(logoutText);
			holder.setPhoto(photo);
			holder.setRefreshText(refreshText);
			holder.setAboutText(aboutText);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
		
        list.add(convertView);

        convert(holder,arg0);
		return convertView;
	}
	public abstract void convert(ViewHolder helper,int position);  
	public static class ViewHolder
    {
		ImageView photo;
		TextView clearText;
		TextView adviceText;
		TextView refreshText;
		TextView bestText ;
		TextView aboutText ;
		TextView logoutText;
		public ImageView getPhoto() {
			return photo;
		}
		public void setPhoto(ImageView photo) {
			this.photo = photo;
		}
		public TextView getClearText() {
			return clearText;
		}
		public void setClearText(TextView clearText) {
			this.clearText = clearText;
		}
		public TextView getAdviceText() {
			return adviceText;
		}
		public void setAdviceText(TextView adviceText) {
			this.adviceText = adviceText;
		}
		public TextView getRefreshText() {
			return refreshText;
		}
		public void setRefreshText(TextView refreshText) {
			this.refreshText = refreshText;
		}
		public TextView getBestText() {
			return bestText;
		}
		public void setBestText(TextView bestText) {
			this.bestText = bestText;
		}
		public TextView getAboutText() {
			return aboutText;
		}
		public void setAboutText(TextView aboutText) {
			this.aboutText = aboutText;
		}
		public TextView getLogoutText() {
			return logoutText;
		}
		public void setLogoutText(TextView logoutText) {
			this.logoutText = logoutText;
		}
		
    }
	   

}
