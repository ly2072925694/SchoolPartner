package com.cpu.adapter;

import java.util.LinkedList;
import java.util.List;

import com.cpu.activity.MyApplication;
import com.cpu.activity.Send;
import com.cpu.tools.ImageUtils;
import com.example.schoolpartner.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class GalleryAdapter extends BaseAdapter {

	private Context context;
	private String folderpath;//�ļ���·��
	private List<String> ImgUrls;//ͼƬ·��,����·��
	private GridView gv;
	
	private Button btnShowNum;
	private DisplayImageOptions options;//ͼƬ��ʾ����
	
	/**
	 * �û�ѡ���ͼƬ���洢ΪͼƬ������·�� 
	 */
	public static List<String> mSelectedImage = new LinkedList<String>();
	
	public GalleryAdapter(Context context,Button btn,String folderpath,List<String> ImgUrls,GridView grid){
		this.context = context;
		this.btnShowNum = btn;
		this.folderpath = folderpath;
		this.ImgUrls = ImgUrls;
		this.gv = grid;
		options = ((MyApplication)context.getApplicationContext()).getDisplayOptions();//��MyApplication�еõ���ʾͼƬ����
	}
	
	@Override
	public int getCount() {
		return ImgUrls.size();
	}

	@Override
	public Object getItem(int position) {
		return ImgUrls.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_gallery_image, parent, false);
			holder = new ViewHolder();
			holder.mImageView = (ImageView) convertView.findViewById(R.id.id_item_image);
			holder.mImageButton = (ImageButton) convertView.findViewById(R.id.id_item_select);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		//����GridView��item�� 
		int width = (gv.getWidth() - gv.getPaddingLeft() - gv.getPaddingRight()) / 3;//��ʾ3�У�ÿ��item���
		LayoutParams params = new LayoutParams(width, width);//�����ͬ
		holder.mImageView.setLayoutParams(params);
		
		//ʹ��ImageLoader����ͼƬ,Scheme.FILE.wrap����·������ImageLoaer֧��
		ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(folderpath + "/" + ImgUrls.get(position)), holder.mImageView
				,options,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				Log.i("FileAdapter", folderpath + "/" + ImgUrls.get(position) + ",position:" +position);
				Log.i("FileAdapter", "ͼƬ����"+ImgUrls.size());
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

				Log.i("Img", "onLoadingFailed:" + Scheme.FILE.wrap(folderpath + "/" + ImgUrls.get(position)));
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				Log.i("Img", "onLoadingComplete" + Scheme.FILE.wrap(folderpath + "/" + ImgUrls.get(position)));
				
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//ͼƬ��ѡ����
		holder.mImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				if (mSelectedImage.contains(folderpath + "/"
						+ ImgUrls.get(position))) {
					mSelectedImage.remove(folderpath + "/"
							+ ImgUrls.get(position));
					holder.mImageButton
							.setImageResource(R.drawable.pictures_unselected);
					holder.mImageView.setColorFilter(null);
				} else {// δѡ���ͼƬ
					if (Send.imgUris.size() + mSelectedImage.size() >= 9) {// ѡ���ͼƬ��<=9
						Toast.makeText(context, "���ֻ��ѡ��9��ͼƬ", 0).show();
						return;
					}
					mSelectedImage.add(folderpath + "/"
							+ ImgUrls.get(position));
					holder.mImageButton
							.setImageResource(R.drawable.pictures_selected);
					holder.mImageView.setColorFilter(Color
							.parseColor("#77000000"));
				}
				btnShowNum.setText("ȷ��(" + mSelectedImage.size() + ")");
				Log.i("GalleryAdapter", "��ѡ��ͼƬ����:" + mSelectedImage.size());
			}
		});
		
		/**
		 * �Ѿ�ѡ�����ͼƬ����ʾ����ӰЧ��
		 */
		if (mSelectedImage.contains(folderpath + "/"
				+ ImgUrls.get(position))) {
			holder.mImageButton
					.setImageResource(R.drawable.pictures_selected);
			holder.mImageView.setColorFilter(Color.parseColor("#77000000"));
		}else{
			holder.mImageButton
			.setImageResource(R.drawable.pictures_unselected);
		}
		
		return convertView;
	}

	public class ViewHolder{
		ImageView mImageView;
		ImageButton mImageButton;
	}
}
