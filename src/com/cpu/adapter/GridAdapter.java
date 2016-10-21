package com.cpu.adapter;

import java.util.ArrayList;

import com.cpu.activity.MyApplication;
import com.cpu.adapter.GridImgsAdapter.ViewHolder;
import com.example.schoolpartner.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GridAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Uri> datas;
	private GridView gv;
	
	/**
	 * ͼƬ��������
	 */
	private DisplayImageOptions options;

	public GridAdapter(Context context, ArrayList<Uri> datas, GridView gv) {
		this.context = context;
		this.datas = datas;
		this.gv = gv;
		options = ((MyApplication)context.getApplicationContext()).getDisplayOptions();
	}

	@Override
	public int getCount() {
		return datas.size()+1;//ͼƬ����+1����ĩβ���ϡ���ӡ���ťͼƬ
	}

	@Override
	public Uri getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;// Grid�е�Item
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.grid_view, null);
			holder.ivImage = (ImageView) convertView
					.findViewById(R.id.iv_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int horizontalSpacing = gv.getHorizontalSpacing();// ˮƽ��߾�
		int width = (gv.getWidth() - horizontalSpacing * 2
				- gv.getPaddingLeft() - gv.getPaddingRight()) / 3;//��ʾ3�У�ÿ��item���
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);//item�Ŀ�����
		holder.ivImage.setLayoutParams(params);//����ͼƬ���
		
			
		if(position < getCount() -1){
			Uri item = getItem(position);
			ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(item.toString()), holder.ivImage,options);
			
		}
		
		return convertView;
	}

	/**
	 * Grid�е�Item(ͼƬ��ɾ����ť)
	 * 
	 * @author ZJC
	 * 
	 */
	public static class ViewHolder {
		public ImageView ivImage;
	}
}
