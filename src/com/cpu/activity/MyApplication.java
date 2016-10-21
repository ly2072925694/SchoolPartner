package com.cpu.activity;

import com.example.schoolpartner.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Application;
import android.graphics.Bitmap;

/**
 * 全局
 * 
 * @author ZJC
 * 
 */
public class MyApplication extends Application {

	DisplayImageOptions options;
	
	@Override
	public void onCreate() {
		super.onCreate();
		// 创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
		.writeDebugLogs()//打印Log信息
		.build();
		
		//初始化
		ImageLoader.getInstance().init(configuration);

	}
	
	public DisplayImageOptions getDisplayOptions(){
			
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.pictures_no) // 设置图片在下载期间显示的图片
		.showImageForEmptyUri(R.drawable.pictures_no)// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.pictures_no)// 设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(false)// 设置下载的图片是否缓存在内存中
		.cacheOnDisc(false)// 设置下载的图片是否缓存在SD卡中
		.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
		.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
		// .delayBeforeLoading(int delayInMillis)//int
		// delayInMillis为你设置的下载前的延迟时间
		// 设置图片加入缓存前，对bitmap进行设置
		// .preProcessor(BitmapProcessor preProcessor)
		.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
		//.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
		.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
		.build();// 构建完成
		
		return options;
	}
}
