package com.cpu.adapter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;

import com.cpu.tools.ImageUrl;
import com.example.schoolpartner.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class GridViewAdapter extends ArrayAdapter<String> {
	
	private GridView mGridView;  
    //图片缓存类  
    private LruCache<String, Bitmap> mLruCache;  
    //记录所有正在下载或等待下载的任务  
    private HashSet<DownloadBitmapAsyncTask> mDownloadBitmapAsyncTaskHashSet;  

    
   
    public GridViewAdapter(Context context,
			int textViewResourceId,String[] objects, GridView gridView) {  
        super(context, textViewResourceId, objects ); 
        Toast.makeText(getContext(), "fffff", Toast.LENGTH_SHORT).show();
		mGridView = gridView;  
        mDownloadBitmapAsyncTaskHashSet = new HashSet<DownloadBitmapAsyncTask>();  
          
        // 获取应用程序最大可用内存  
        int maxMemory = (int) Runtime.getRuntime().maxMemory();  
        // 设置图片缓存大小为maxMemory的1/6  
        int cacheSize = maxMemory/6;  
          
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {  
            @Override  
            protected int sizeOf(String key, Bitmap bitmap) {  
                return bitmap.getByteCount();  
            }  
        };  
	}
	@Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        String url = getItem(position);  
        View view;  
        Toast.makeText(getContext(), "fffff", Toast.LENGTH_SHORT).show();
        if (convertView == null) {  
            view = LayoutInflater.from(
            		getContext()).inflate(R.layout.gridview_item, null);  
        } else {  
            view = convertView;  
        }  
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);  
        //为该ImageView设置一个Tag,防止图片错位  
        imageView.setTag(url);  
        //为该ImageView设置显示的图片  
        setImageForImageView(url, imageView);  
        return view;  
    }  
  
    /** 
     * 为ImageView设置图片(Image) 
     * 1 从缓存中获取图片 
     * 2 若图片不在缓存中则为其设置默认图片 
     */  
    private void setImageForImageView(String imageUrl, ImageView imageView) {  
        Bitmap bitmap = getBitmapFromLruCache(imageUrl);  
        if (bitmap != null) {  
            imageView.setImageBitmap(bitmap);  
        } else {  
            imageView.setImageResource(R.drawable.person1);  
        }  
    }  
  
    /** 
     * 将图片存储到LruCache 
     */  
    public void addBitmapToLruCache(String key, Bitmap bitmap) {  
        if (getBitmapFromLruCache(key) == null) {  
            mLruCache.put(key, bitmap);  
        }  
    }  
  
    /** 
     * 从LruCache缓存获取图片 
     */  
    public Bitmap getBitmapFromLruCache(String key) {  
        return mLruCache.get(key);  
    }  
  
      
  
   /** 
    * 为GridView的item加载图片 
    *  
    * @param firstVisibleItem  
    * GridView中可见的第一张图片的下标 
    *  
    * @param visibleItemCount  
    * GridView中可见的图片的数量 
    *  
    */  
    public void loadBitmaps(int firstVisibleItem, int visibleItemCount) {  
        try {  
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {  
                String imageUrl = ImageUrl.Urls[i];  
                Bitmap bitmap = getBitmapFromLruCache(imageUrl);  
                if (bitmap == null) {  
                    DownloadBitmapAsyncTask downloadBitmapAsyncTask = new
                    		DownloadBitmapAsyncTask();  
                    mDownloadBitmapAsyncTaskHashSet.add(downloadBitmapAsyncTask);  
                    downloadBitmapAsyncTask.execute(imageUrl);  
                } else {  
                    //依据Tag找到对应的ImageView显示图片  
                    ImageView imageView = (ImageView) mGridView.findViewWithTag(
                    		imageUrl);  
                    if (imageView != null && bitmap != null) {  
                        imageView.setImageBitmap(bitmap);  
                    }  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * 取消所有正在下载或等待下载的任务 
     */  
    public void cancelAllTasks() {  
        if (mDownloadBitmapAsyncTaskHashSet != null) {  
            for (DownloadBitmapAsyncTask task : mDownloadBitmapAsyncTaskHashSet) {  
                task.cancel(false);  
            }  
        }  
    }  
    class DownloadBitmapAsyncTask extends AsyncTask<String, Void, Bitmap> {  
        private String imageUrl;  
        @Override  
        protected Bitmap doInBackground(String... params) {  
            imageUrl = params[0];  
            Bitmap bitmap = downloadBitmap(params[0]);  
            if (bitmap != null) {  
                //下载完后,将其缓存到LrcCache  
                addBitmapToLruCache(params[0], bitmap);  
            }  
            return bitmap;  
        }  

        @Override  
        protected void onPostExecute(Bitmap bitmap) {  
            super.onPostExecute(bitmap);  
            //下载完成后,找到其对应的ImageView显示图片  
            ImageView imageView = (ImageView) mGridView.findViewWithTag(imageUrl);  
            if (imageView != null && bitmap != null) {  
                imageView.setImageBitmap(bitmap);  
            }  
            mDownloadBitmapAsyncTaskHashSet.remove(this);  
        }  // 获取Bitmap  
        private Bitmap downloadBitmap(String imageUrl) {  
            Bitmap bitmap = null;  
            HttpURLConnection httpURLConnection = null;  
            try {  
                URL url = new URL(imageUrl);  
                httpURLConnection = (HttpURLConnection) url.openConnection();  
                httpURLConnection.setConnectTimeout(5 * 1000);  
                httpURLConnection.setReadTimeout(10 * 1000);  
                httpURLConnection.setDoInput(true);  
                httpURLConnection.setDoOutput(true);  
                bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());  
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally {  
                if (httpURLConnection != null) {  
                    httpURLConnection.disconnect();  
                }  
            }  
            return bitmap;  
        } 
    }  
}


 
