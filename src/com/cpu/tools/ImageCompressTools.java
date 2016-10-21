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
 * 图片压缩工具类
 * @author ZJC
 *
 */
public class ImageCompressTools {

	/**
	 * 图片原始路径
	 */
	private List<String> imgUrls; 
	
	static int n = 1;
	
	public ImageCompressTools(List<String> imgUrls,int w,int h){
		this.imgUrls = imgUrls;
	}
	
	/**
	 * 计算图片的缩放值
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth,int reqHeight){
		//图片原始宽高 
		final int height = options.outHeight;
		final int width = options.outWidth;
		
		int inSampleSize = 1;
		
		if(height > reqHeight || width > reqWidth){
			final int heightRatio = Math.round((float)height/(float)reqHeight);//4舍5入
			final int widthhRatio = Math.round((float)width/(float)reqWidth);
			inSampleSize = heightRatio < widthhRatio ? heightRatio:widthhRatio;
		}
		return inSampleSize;
	}
	
	/**
	 * 根据路径获得图片并压缩，返回bitmap用于显示(单张图片)
	 * @param filePath
	 * @param width 期望要压缩成的图片宽度
	 * @param height 期望要压缩成的图片高度
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath,int width,int height){
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;//不把图片读到内存中,但依然可以计算出图片的大小
		BitmapFactory.decodeFile(filePath, options);
		
		//在内存中创建bitmap对象，这个对象按照缩放大小创建的
		options.inSampleSize = calculateInSampleSize(options, width, height);
		options.inJustDecodeBounds = false;//解码
		return BitmapFactory.decodeFile(filePath, options);
	}
	
	/**
	 * 把bitmap转换成String(单张图片)
	 * @param filePath
	 * @return
	 */
	public static String bitmapToString(String filePath,int w,int h){
		Bitmap bm = getSmallBitmap(filePath,w,h);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);//再次压缩
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}
	
	/**
	 * 压缩图片，处理手机拍照角度旋转问题,返回处理后的图片路径(单张图片)
	 * @param context
	 * @param filePath 图片绝对路径
	 * @param width 期望要压缩成的图片宽度
	 * @param height 期望要压缩成的图片高度
	 * @param q 二次压缩质量(100则不二次压缩)
	 * @return String path(压缩后图片路径)
	 * @throws FileNotFoundException
	 */
	public static String compressImage(Context context ,String filePath,int width,int height,int q)
			throws FileNotFoundException{
		Bitmap bm = getSmallBitmap(filePath,width,height);
		int degree = readPictureDegree(filePath);
		if(degree !=0){//旋转照片角度
			bm = rotateBitmap(bm,degree);
		}
		
//		//文件夹路径
//		int dirLastIndex = filePath.lastIndexOf("/"); 
//		String dir = filePath.substring(0, dirLastIndex);
//		
//		
//		File imageDir = new File(filePath);
//		String imgNamee = imageDir.getName();//图片名（含后缀名）
//		Log.i("Filename", imageDir.getName());
//		
//		String[] sepImgName = imgNamee.split("\\.");
//		
//		File outputFile = new File(dir,sepImgName[0] + "(1)" + "." + sepImgName[1]);
		String filePath2 = CreatePath(filePath);//压缩图片后的副本路径
		FileOutputStream out = new FileOutputStream(filePath2);
		bm.compress(Bitmap.CompressFormat.JPEG, q, out);//再次压缩
		return filePath2;
	}

	/**
	 * 给定一个路径，创建一个路径副本，即：原文件名+"(1)或(2)、(3)……"所构成的路径
	 * @param filepath 文件路径
	 * @param n 路径副本号(1)或(2)或(3)……，如test(1).txt,test(2).txt
	 * @return
	 */
	public static String CreatePath(String filepath){
		//得到文件夹路径
		int dirLastIndex = filepath.lastIndexOf("/"); 
		String dir = filepath.substring(0, dirLastIndex);
		
		//得到文件名
		File f = new File(filepath);
		String fname = f.getName();
		String[] sepfName = fname.split("\\.");
		File outputFile = new File(dir,sepfName[0] + "("+ n + ")" + "." + sepfName[1]);
		//递归调用，给文件名末尾加上"(1)或(2)或(3)……"
		if(outputFile.exists()){
			Log.i("Tools", "已存在相同路径:" + outputFile.toString());
			CreatePath(outputFile.toString());
			n++;
		}
		Log.i("Tools", "创建路径结束:" + outputFile.toString());
		return outputFile.toString();
	}
	
	/**
	 * 判断照片角度
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
	 * 旋转照片
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
