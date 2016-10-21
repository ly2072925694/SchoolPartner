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
	private String folderpath;//文件夹路径
	private List<String> ImgUrls;//图片路径,单级路径
	private GridView gv;
	
	private Button btnShowNum;
	private DisplayImageOptions options;//图片显示配置
	
	/**
	 * 用户选择的图片，存储为图片的完整路径 
	 */
	public static List<String> mSelectedImage = new LinkedList<String>();
	
	public GalleryAdapter(Context context,Button btn,String folderpath,List<String> ImgUrls,GridView grid){
		this.context = context;
		this.btnShowNum = btn;
		this.folderpath = folderpath;
		this.ImgUrls = ImgUrls;
		this.gv = grid;
		options = ((MyApplication)context.getApplicationContext()).getDisplayOptions();//从MyApplication中得到显示图片配置
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
		
		//计算GridView中item的 
		int width = (gv.getWidth() - gv.getPaddingLeft() - gv.getPaddingRight()) / 3;//显示3列，每个item宽度
		LayoutParams params = new LayoutParams(width, width);//宽高相同
		holder.mImageView.setLayoutParams(params);
		
		//使用ImageLoader加载图片,Scheme.FILE.wrap包裹路劲，让ImageLoaer支持
		ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(folderpath + "/" + ImgUrls.get(position)), holder.mImageView
				,options,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				Log.i("FileAdapter", folderpath + "/" + ImgUrls.get(position) + ",position:" +position);
				Log.i("FileAdapter", "图片数量"+ImgUrls.size());
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
		
		//图片多选处理
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
				} else {// 未选择该图片
					if (Send.imgUris.size() + mSelectedImage.size() >= 9) {// 选择的图片数<=9
						Toast.makeText(context, "最多只能选择9张图片", 0).show();
						return;
					}
					mSelectedImage.add(folderpath + "/"
							+ ImgUrls.get(position));
					holder.mImageButton
							.setImageResource(R.drawable.pictures_selected);
					holder.mImageView.setColorFilter(Color
							.parseColor("#77000000"));
				}
				btnShowNum.setText("确定(" + mSelectedImage.size() + ")");
				Log.i("GalleryAdapter", "已选择图片张数:" + mSelectedImage.size());
			}
		});
		
		/**
		 * 已经选择过的图片，显示出阴影效果
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
