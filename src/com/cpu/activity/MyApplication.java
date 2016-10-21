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
 * ȫ��
 * 
 * @author ZJC
 * 
 */
public class MyApplication extends Application {

	DisplayImageOptions options;
	
	@Override
	public void onCreate() {
		super.onCreate();
		// ����Ĭ�ϵ�ImageLoader���ò���
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
		.writeDebugLogs()//��ӡLog��Ϣ
		.build();
		
		//��ʼ��
		ImageLoader.getInstance().init(configuration);

	}
	
	public DisplayImageOptions getDisplayOptions(){
			
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.pictures_no) // ����ͼƬ�������ڼ���ʾ��ͼƬ
		.showImageForEmptyUri(R.drawable.pictures_no)// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
		.showImageOnFail(R.drawable.pictures_no)// ����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
		.cacheInMemory(false)// �������ص�ͼƬ�Ƿ񻺴����ڴ���
		.cacheOnDisc(false)// �������ص�ͼƬ�Ƿ񻺴���SD����
		.considerExifParams(true) // �Ƿ���JPEGͼ��EXIF��������ת����ת��
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// ����ͼƬ����εı��뷽ʽ��ʾ
		.bitmapConfig(Bitmap.Config.RGB_565)// ����ͼƬ�Ľ�������//
		// .delayBeforeLoading(int delayInMillis)//int
		// delayInMillisΪ�����õ�����ǰ���ӳ�ʱ��
		// ����ͼƬ���뻺��ǰ����bitmap��������
		// .preProcessor(BitmapProcessor preProcessor)
		.resetViewBeforeLoading(true)// ����ͼƬ������ǰ�Ƿ����ã���λ
		//.displayer(new RoundedBitmapDisplayer(20))// �Ƿ�����ΪԲ�ǣ�����Ϊ����
		.displayer(new FadeInBitmapDisplayer(100))// �Ƿ�ͼƬ���غú���Ķ���ʱ��
		.build();// �������
		
		return options;
	}
}
