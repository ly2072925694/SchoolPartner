package com.cpu.tools;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.cpu.adapter.PublishSimpleAdapter;
import com.tencent.mm.sdk.platformtools.BackwardSupportUtil.BitmapFactory;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class ImageLoader {
	private ImageView mImageView;
	private String mUrl ;
	private String mUrls;
	private LruCache<String, Bitmap> mCaches;
	private Set<NewsAsyncTask> mTask;
	public ImageLoader() {
		int maxMermory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMermory/4;
		mTask = new HashSet<NewsAsyncTask>();
		mCaches = new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};
	}
	public void addBitmapToCache(String key, Bitmap value){
		if(getBitmapFromCache(key)==null){
			mCaches.put(key, value);
		}
	}
	public Bitmap getBitmapFromCache(String key) {
		return mCaches.get(key);
	}
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(mImageView.getTag().equals(mUrl)){
				mImageView.setImageBitmap((Bitmap) msg.obj);
				addBitmapToCache(mUrl, getBitmapFromCache(mUrl));
			}
		}
	};
	public void loadImages(int start,int end){
		for(int i= start;i<end;i++){
			mUrls = PublishSimpleAdapter.urls[i];
			Bitmap mBitmap = getBitmapFromCache(mUrls);
			if(mBitmap == null){
				NewsAsyncTask task = new NewsAsyncTask(mImageView,mUrls);
				task.execute(mUrls);
				mTask.add(task);
			}else{
				mImageView.setImageBitmap(mBitmap);
			}
		}
	}
	public void showImageByThread(ImageView imageView , final String url){
		mImageView = imageView;
		mUrl = url;
		Bitmap mBitmap = getBitmapFromCache(url);
		if(mBitmap == null){
			new Thread(){
				
				@Override
				public void run() {
					try {
						Bitmap bitmap = getBitmapFromUrl(url);
						Message message = Message.obtain();
						message.obj = bitmap;
						handler.sendMessage(message);
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					super.run();
				}
			}.start();
		}else{
			mImageView.setImageBitmap(mBitmap);
		}
	}
	public void showImageByAsyncTask(ImageView imageView , final String url){
		mImageView = imageView;
		mUrl = url;
		Bitmap mBitmap = getBitmapFromCache(url);
		if(mBitmap == null){
			new NewsAsyncTask(imageView,url).execute(url);
		}else{
			mImageView.setImageBitmap(mBitmap);
		}
	}
	private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap>{
		private ImageView mImageView;
		private String mUrl;
		public NewsAsyncTask(ImageView imageView ,String url) {
			mImageView  = imageView;
			mUrl = url;
		}
		@Override
		protected Bitmap doInBackground(String... params) {
			String url = params[0];
			Bitmap mBitmap;
			try {
				mBitmap = getBitmapFromUrl(url);
				if(mBitmap != null){
					addBitmapToCache(url, mBitmap);
				}
				return mBitmap;
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			return null;
			
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(mImageView.getTag().equals(mUrl)){
				mImageView.setImageBitmap(result);
			}
			mTask.remove(this);
		}
		
	}
	public void cancellAllTask(){
		if(mTask!=null){
			for(NewsAsyncTask task: mTask){
				task.cancel(false);
			}
		}
	}
	public Bitmap getBitmapFromUrl(String urlString) throws URISyntaxException{
		Bitmap bitmap;
		InputStream is ;
		URL url;
		try {
			url = new URL(urlString);
			try {
				HttpURLConnection connection = (HttpURLConnection) 
						url.openConnection();
				is = new BufferedInputStream(connection.getInputStream());
				bitmap = BitmapFactory.decodeStream(is);
				connection.disconnect();
				is.close();
				return bitmap;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
