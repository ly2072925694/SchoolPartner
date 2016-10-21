package com.cpu.adapter;

import java.io.File;
import java.util.List;

import com.cpu.activity.MyApplication;
import com.cpu.entity.ImageFolder;
import com.example.schoolpartner.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

import android.content.Context;
import android.net.Uri;
import android.transition.Scene;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListDirAdapter extends BaseAdapter {

	private Context context;
	private List<ImageFolder> datas;// �ļ����б�
	private DisplayImageOptions options;//ͼƬ��ʾ����

	public ListDirAdapter(Context context,List<ImageFolder> datas){
		this.context = context;
		this.datas = datas;
		options = ((MyApplication)context.getApplicationContext()).getDisplayOptions();//��MyApplication�еõ���ʾͼƬ����
	}
	
	@Override
	public int getCount() {
		return datas.size();
	}

	/**
	 * �õ��ļ��м����е�һ���ļ���
	 */
	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_dir_item, parent, false);
			holder = new ViewHolder();
			holder.itemImage = (ImageView) convertView.findViewById(R.id.id_dir_item_image);
			holder.tvDirName = (TextView) convertView.findViewById(R.id.id_dir_item_name);
			holder.tvDirImageCount = (TextView) convertView.findViewById(R.id.id_dir_item_count);
			holder.ivDirSelected = (ImageView) convertView.findViewById(R.id.iv_dir_select);
			
			//����
			Log.i("ListDirAdapter","���ļ�����:" + datas.size() + 
					",�ļ���:" +datas.get(position).getName() + 
					",����1��ͼ:" +datas.get(position).getLatestImagePath()
					+ " ͼƬ����:" + datas.get(position).getCount());
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		//��item����
		File curFile = new File(datas.get(position).getDir());
		//����һ��ͼƬ
		ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(datas.get(position).getLatestImagePath()), holder.itemImage);
		holder.tvDirName.setText(datas.get(position).getName());
		holder.tvDirImageCount.setText(datas.get(position).getCount() + "��");
		return convertView;
	}

	public class ViewHolder {
		private ImageView itemImage;
		private TextView tvDirName;
		private TextView tvDirImageCount;
		private ImageView ivDirSelected;
	}
}
