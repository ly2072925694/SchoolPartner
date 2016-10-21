package com.cpu.adapter;

import java.util.ArrayList;

import com.cpu.activity.MyApplication;
import com.example.schoolpartner.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.content.Context;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Grid�Ź���ͼƬ��ʾ������
 * 
 * @author ZJC
 * 
 */
public class GridImgsAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Uri> datas;
	private GridView gv;
	
	/**
	 * ͼƬ��������
	 */
	private DisplayImageOptions options;

	public GridImgsAdapter(Context context, ArrayList<Uri> datas, GridView gv) {
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
			convertView = View.inflate(context, R.layout.item_grid_image, null);
			holder.ivImage = (ImageView) convertView
					.findViewById(R.id.iv_image);
			holder.ivDeleteImage = (ImageView) convertView
					.findViewById(R.id.iv_delete_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		int g = gv.getHorizontalSpacing();
		int horizontalSpacing = gv.getHorizontalSpacing();// ˮƽ��߾�
		int width = (gv.getWidth() - horizontalSpacing * 2
				- gv.getPaddingLeft() - gv.getPaddingRight()) / 3;//��ʾ3�У�ÿ��item���
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);//item�Ŀ�����
		holder.ivImage.setLayoutParams(params);//����ͼƬ���
		
		
		//��ʾͼƬ����		
		if(position < getCount() -1){
			//��������
			Uri item = getItem(position);
//			holder.ivImage.setImageURI(item);
			//ʹ��ImageLoader��Դ�����ͼƬ
			ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(item.toString()), holder.ivImage,options);
			holder.ivDeleteImage.setVisibility(View.VISIBLE);//��ʾitem�����Ͻ�ɾ����ť
			//���ɾ����ťɾ��ͼƬ
			holder.ivDeleteImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					datas.remove(position);//�Ƴ�ͼƬ
					notifyDataSetChanged();//ˢ��
					Log.i("GridImgsAdapter","���ϴ���ͼƬ:"+ + datas.size() + "��");
					for(int i=0;i<datas.size();i++){
						Log.i("GridImgsAdapter", datas.get(i).toString());
					}
					
				}
			});
		}else {
			//���µ�1�д������2�д���Ч���ظ�����������������ȷ���� ����ӡ�ͼƬ��ԭ������
			holder.ivImage.setImageResource(R.drawable.item_grid_pic_add_selector);
			//ʹ��ImageLoader��Դ�����ͼƬ
			ImageLoader.getInstance().displayImage(Scheme.DRAWABLE.wrap("R.drawable.item_grid_pic_add_selector"), holder.ivImage);
			holder.ivDeleteImage.setVisibility(View.GONE);
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
		public ImageView ivDeleteImage;
	}
}
