package com.cpu.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;
import android.util.Log;

/**
 * ͼƬѹ��������
 * @author ZJC
 *
 */
public class ImageCompressTools {

	/**
	 * ͼƬԭʼ·��
	 */
	private List<String> imgUrls; 
	
	static int n = 1;
	
	public ImageCompressTools(List<String> imgUrls,int w,int h){
		this.imgUrls = imgUrls;
	}
	
	/**
	 * ����ͼƬ������ֵ
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth,int reqHeight){
		//ͼƬԭʼ��� 
		final int height = options.outHeight;
		final int width = options.outWidth;
		
		int inSampleSize = 1;
		
		if(height > reqHeight || width > reqWidth){
			final int heightRatio = Math.round((float)height/(float)reqHeight);//4��5��
			final int widthhRatio = Math.round((float)width/(float)reqWidth);
			inSampleSize = heightRatio < widthhRatio ? heightRatio:widthhRatio;
		}
		return inSampleSize;
	}
	
	/**
	 * ����·�����ͼƬ��ѹ��������bitmap������ʾ(����ͼƬ)
	 * @param filePath
	 * @param width ����Ҫѹ���ɵ�ͼƬ���
	 * @param height ����Ҫѹ���ɵ�ͼƬ�߶�
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath,int width,int height){
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;//����ͼƬ�����ڴ���,����Ȼ���Լ����ͼƬ�Ĵ�С
		BitmapFactory.decodeFile(filePath, options);
		
		//���ڴ��д���bitmap����������������Ŵ�С������
		options.inSampleSize = calculateInSampleSize(options, width, height);
		options.inJustDecodeBounds = false;//����
		return BitmapFactory.decodeFile(filePath, options);
	}
	
	/**
	 * ��bitmapת����String(����ͼƬ)
	 * @param filePath
	 * @return
	 */
	public static String bitmapToString(String filePath,int w,int h){
		Bitmap bm = getSmallBitmap(filePath,w,h);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);//�ٴ�ѹ��
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}
	
	/**
	 * ѹ��ͼƬ�������ֻ����սǶ���ת����,���ش�����ͼƬ·��(����ͼƬ)
	 * @param context
	 * @param filePath ͼƬ����·��
	 * @param width ����Ҫѹ���ɵ�ͼƬ���
	 * @param height ����Ҫѹ���ɵ�ͼƬ�߶�
	 * @param q ����ѹ������(100�򲻶���ѹ��)
	 * @return String path(ѹ����ͼƬ·��)
	 * @throws FileNotFoundException
	 */
	public static String compressImage(Context context ,String filePath,int width,int height,int q)
			throws FileNotFoundException{
		Bitmap bm = getSmallBitmap(filePath,width,height);
		int degree = readPictureDegree(filePath);
		if(degree !=0){//��ת��Ƭ�Ƕ�
			bm = rotateBitmap(bm,degree);
		}
		
//		//�ļ���·��
//		int dirLastIndex = filePath.lastIndexOf("/"); 
//		String dir = filePath.substring(0, dirLastIndex);
//		
//		
//		File imageDir = new File(filePath);
//		String imgNamee = imageDir.getName();//ͼƬ��������׺����
//		Log.i("Filename", imageDir.getName());
//		
//		String[] sepImgName = imgNamee.split("\\.");
//		
//		File outputFile = new File(dir,sepImgName[0] + "(1)" + "." + sepImgName[1]);
		String filePath2 = CreatePath(filePath);//ѹ��ͼƬ��ĸ���·��
		FileOutputStream out = new FileOutputStream(filePath2);
		bm.compress(Bitmap.CompressFormat.JPEG, q, out);//�ٴ�ѹ��
		return filePath2;
	}

	/**
	 * ����һ��·��������һ��·������������ԭ�ļ���+"(1)��(2)��(3)����"�����ɵ�·��
	 * @param filepath �ļ�·��
	 * @param n ·��������(1)��(2)��(3)��������test(1).txt,test(2).txt
	 * @return
	 */
	public static String CreatePath(String filepath){
		//�õ��ļ���·��
		int dirLastIndex = filepath.lastIndexOf("/"); 
		String dir = filepath.substring(0, dirLastIndex);
		
		//�õ��ļ���
		File f = new File(filepath);
		String fname = f.getName();
		String[] sepfName = fname.split("\\.");
		File outputFile = new File(dir,sepfName[0] + "("+ n + ")" + "." + sepfName[1]);
		//�ݹ���ã����ļ���ĩβ����"(1)��(2)��(3)����"
		if(outputFile.exists()){
			Log.i("Tools", "�Ѵ�����ͬ·��:" + outputFile.toString());
			CreatePath(outputFile.toString());
			n++;
		}
		Log.i("Tools", "����·������:" + outputFile.toString());
		return outputFile.toString();
	}
	
	/**
	 * �ж���Ƭ�Ƕ�
	 * @param filePath
	 * @return
	 */
	public static int readPictureDegree(String filePath) {
		int degree = 0;
		try{
			ExifInterface exifInterface = new ExifInterface(filePath);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
				
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
				
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;

			default:
				break;
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return degree;
	}
	
	/**
	 * ��ת��Ƭ
	 * @param bm
	 * @param degree
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
		if(bitmap != null){
			Matrix m = new Matrix();
			m.postRotate(degrees);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), m, true);
		}
		return bitmap;
	}
}
