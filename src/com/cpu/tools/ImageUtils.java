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
 * ͼƬѡ�񹤾���
 * 
 * @author ZJC
 * 
 */
public class ImageUtils {

	public static final int REQUEST_CODE_FROM_CAMERA = 5001;
	public static final int REQUEST_CODE_FROM_ALBUM = 5002;

	/**
	 * �������ͼƬ��uri��ַ
	 */
	public static Uri imageUriFromCamera;

	/**
	 * ѡ���ȡͼƬ��ʽ
	 * 
	 * @param activity
	 */
	public static void showImagePickDialog(final Activity activity) {
		String title = "ѡ��ͼƬ";
		String[] items = new String[] { "����", "���" };
		new AlertDialog.Builder(activity).setTitle(title)
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:// ����
							pickImageFromCamera(activity);
							break;
						case 1:// ���
							pickImageFromAlbum(activity);
							break;

						default:
							break;
						}
					}
				}).show();
	}

	/**
	 * ������л�ȡͼƬ,��ʽIntent�����û�����װ��ͼƬӦ��
	 * 
	 * @param activity
	 */
	public static void pickImageFromAlbum(Activity activity) {
//		Intent intent = new Intent();// ��ʽ
//		intent.setAction(Intent.ACTION_GET_CONTENT);// ���ö�������ȡĳһ���ض����͵�����
//		intent.setType("image/*");// ��������,jpeg,png��
		Intent intent = new Intent(activity, GalleryActivity.class);
		activity.startActivityForResult(intent, REQUEST_CODE_FROM_ALBUM);// ���ô˷����ĵ�Activity����Intent����÷���ֵ
	}

	/**
	 * ������л�ȡͼƬ,��ʾIntent
	 * 
	 * @param activity
	 */
	public static void pickImageFromAlbum2(Activity activity) {
		Intent intent = new Intent();// ��ʽ
		intent.setAction(Intent.ACTION_PICK);// ���ö�����ѡȡ����
		intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// ����Ŀ��·��uriΪ�ⲿ�洢
		activity.startActivityForResult(intent, REQUEST_CODE_FROM_ALBUM);// ���ô˷����ĵ�Activity����Intent����÷���ֵ
	}

	/**
	 * ��������ջ�ȡͼƬ,��ʽ
	 * 
	 * @param activity
	 */
	public static void pickImageFromCamera(Activity activity) {
//		imageUriFromCamera = createImagUri(activity);

		String path = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/";//���·��
		File file = new File(path, System.currentTimeMillis() + ".jpg");
		imageUriFromCamera = Uri.fromFile(file);
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);// ���ö�����ͼƬץȡ
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera);//ָ�����պ��ͼƬ�洢·��, activity����getData��������ʱ����ԭʼ���ݣ�
																	//���������EXTRA_OUTPUT,�򷵻�һ��BitMapλͼ��ѹ���ģ�
		activity.startActivityForResult(intent, REQUEST_CODE_FROM_CAMERA);
	}

	/**
	 * ����һ��ͼƬuri���������պ����Ƭ
	 * 
	 * @param activity
	 * @return
	 */
	public static Uri createImagUri(Context context) {
		String name = "schoolpartner" + System.currentTimeMillis();// ��ǰ����֮��ֵ
		ContentValues values = new ContentValues();// ��ֵ�Լ���
		values.put(MediaStore.Images.Media.TITLE, name);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpeg");// �ļ���
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");// �ļ�����
		Uri uri = context.getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);// ContentResolver()���ݽ����ߣ��ṩ����
																		// ��ѯ�����롢�༭��ɾ��
		return uri;
	}

	/**
	 * ɾ��ͼƬ
	 */
	public static void deleteImageUri(Context context, Uri uri) {
		context.getContentResolver().delete(uri, null, null);
	}

	//*************Android4.4���ϰ汾����********************//
	/**
	 * ����Uri��ȡͼƬ����·�������Android4.4���ϰ汾Uriת��
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
