package com.cpu.tools;

import java.io.File;

import com.cpu.activity.GalleryActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * 图片选择工具类
 * 
 * @author ZJC
 * 
 */
public class ImageUtils {

	public static final int REQUEST_CODE_FROM_CAMERA = 5001;
	public static final int REQUEST_CODE_FROM_ALBUM = 5002;

	/**
	 * 存放拍照图片的uri地址
	 */
	public static Uri imageUriFromCamera;

	/**
	 * 选择获取图片方式
	 * 
	 * @param activity
	 */
	public static void showImagePickDialog(final Activity activity) {
		String title = "选择图片";
		String[] items = new String[] { "拍照", "相册" };
		new AlertDialog.Builder(activity).setTitle(title)
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:// 拍照
							pickImageFromCamera(activity);
							break;
						case 1:// 相册
							pickImageFromAlbum(activity);
							break;

						default:
							break;
						}
					}
				}).show();
	}

	/**
	 * 从相册中获取图片,隐式Intent调用用户所安装的图片应用
	 * 
	 * @param activity
	 */
	public static void pickImageFromAlbum(Activity activity) {
//		Intent intent = new Intent();// 隐式
//		intent.setAction(Intent.ACTION_GET_CONTENT);// 设置动作，获取某一个特定类型的数据
//		intent.setType("image/*");// 设置类型,jpeg,png等
		Intent intent = new Intent(activity, GalleryActivity.class);
		activity.startActivityForResult(intent, REQUEST_CODE_FROM_ALBUM);// 调用此方法的的Activity启动Intent并获得返回值
	}

	/**
	 * 从相册中获取图片,显示Intent
	 * 
	 * @param activity
	 */
	public static void pickImageFromAlbum2(Activity activity) {
		Intent intent = new Intent();// 隐式
		intent.setAction(Intent.ACTION_PICK);// 设置动作，选取数据
		intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 设置目标路劲uri为外部存储
		activity.startActivityForResult(intent, REQUEST_CODE_FROM_ALBUM);// 调用此方法的的Activity启动Intent并获得返回值
	}

	/**
	 * 打开相机拍照获取图片,隐式
	 * 
	 * @param activity
	 */
	public static void pickImageFromCamera(Activity activity) {
//		imageUriFromCamera = createImagUri(activity);

		String path = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/";//相册路径
		File file = new File(path, System.currentTimeMillis() + ".jpg");
		imageUriFromCamera = Uri.fromFile(file);
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);// 设置动作，图片抓取
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera);//指定拍照后的图片存储路劲, activity调用getData（）方法时返回原始数据，
																	//如果不设置EXTRA_OUTPUT,则返回一个BitMap位图（压缩的）
		activity.startActivityForResult(intent, REQUEST_CODE_FROM_CAMERA);
	}

	/**
	 * 创建一条图片uri，保存拍照后的照片
	 * 
	 * @param activity
	 * @return
	 */
	public static Uri createImagUri(Context context) {
		String name = "schoolpartner" + System.currentTimeMillis();// 当前毫秒之后值
		ContentValues values = new ContentValues();// 键值对集合
		values.put(MediaStore.Images.Media.TITLE, name);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpeg");// 文件名
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");// 文件类型
		Uri uri = context.getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);// ContentResolver()内容解析者，提供内容
																		// 查询、插入、编辑、删除
		return uri;
	}

	/**
	 * 删除图片
	 */
	public static void deleteImageUri(Context context, Uri uri) {
		context.getContentResolver().delete(uri, null, null);
	}

	//*************Android4.4以上版本处理********************//
	/**
	 * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
	 * 
	 * @param context
	 * @param imageUri
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getImageAbsolutePath19(Activity context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
				&& DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}

		// MediaStore (and general)
		if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	private static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	private static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	private static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	private static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	private static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}
}
