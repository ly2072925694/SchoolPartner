package com.cpu.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.cpu.adapter.GalleryAdapter;
import com.cpu.adapter.ListDirAdapter;
import com.cpu.entity.ImageFolder;
import com.cpu.tools.ImageCompressTools;
import com.example.schoolpartner.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ͼƬ���Gallery
 * 
 * @author ZJC
 * 
 */
public class GalleryActivity extends Activity implements OnClickListener {

	private ProgressDialog mProgressDialog;
	private int mScreenHeight;

	private GridView mGirdView;
	private CheckBox mcboxOriginalPic;
	
	private Button btnSubmit;
	private TextView tvShowFolsers;//popwindow����
	private LinearLayout mPopWinowTitle;
	private ListView lvFolders;
	private GalleryAdapter mAdapter;
	
	/**
	 * ��ʱ�ĸ����࣬���ڷ�ֹͬһ���ļ��еĶ��ɨ��
	 */
	private HashSet<String> mDirPaths = new HashSet<String>();

	/**
	 * ɨ�赽�����е�ͼƬ�ļ���
	 */
	private List<ImageFolder> mImageFloders = new ArrayList<ImageFolder>();

	/**
	 * չʾͼƬ�ļ����б��PopupWindow
	 */
	private PopupWindow mPopupWindow;
	
	/**
	 * ͼƬ���������ļ���
	 */
	private File mImgDir;

	/**
	 * �洢�ļ����е�ͼƬ����
	 */
	private int mPicsSize;

	/**
	 * ͼƬ����(jpg,png)
	 */
	int totalCount = 0;

	/**
	 * ���ļ��������е�ͼƬ
	 */
	private List<String> mImgs;

	/**
	 * ��ѡ���ͼƬ·������
	 */
	private List<String> selectedImgUrls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gallery);
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;// ��

		initView();
		getImages();
		InitColor();
		// initEvent();
	}
	void InitColor(){
		// 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 自定义颜色
        tintManager.setTintColor(Color.parseColor("#0099FF"));
	}
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
	/**
	 * ��ʼ��View
	 */
	private void initView() {
		mGirdView = (GridView) findViewById(R.id.id_gridView);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		tvShowFolsers = (TextView) findViewById(R.id.tv_show_folderlist);
		mPopWinowTitle = (LinearLayout) findViewById(R.id.ll_title);
		mcboxOriginalPic = (CheckBox) findViewById(R.id.cbox_is_original_pic);
		
		mPopWinowTitle.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
	}

	
	
	/**
	 * ����ContentProviderɨ���ֻ��е�ͼƬ���˷������������߳��� ���ͼƬɨ�裬���ջ��ͼƬ�����Ǹ��ļ���
	 */
	private void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "�����ⲿ�洢", Toast.LENGTH_SHORT).show();
			return;
		}

		// ��ʾ������
		mProgressDialog = ProgressDialog.show(this, null, "���ڼ���...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				String firstImage = null;

				Uri mImagUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = GalleryActivity.this
						.getContentResolver();// ���ݽ�����

				// ֻ��ѯjpeg��pngͼƬ
				Cursor mCursor = mContentResolver.query(mImagUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);
				Log.i("TAG", mCursor.getCount() + "");
				while (mCursor.moveToNext()) {
					// ��ȡͼƬ·��
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					Log.i("TAG", path);
					// �õ���һ��ͼƬ��·��
					firstImage = path;

					// ��ȡ��ͼƬ�ĸ�·����
					File parentFile = new File(path).getParentFile();
					if (parentFile == null) {
						continue;
					}
					String dirPath = parentFile.getAbsolutePath();// ����·��
					ImageFolder imageFolder = null;
					// ��һ��HashSet��ֹ���ɨ��ͬһ�ļ���
					if (mDirPaths.contains(dirPath)) {
						continue;
					} else {
						mDirPaths.add(dirPath);// ��ӽ�·������
						// ��ʼ��ImageFolder���ļ���ʵ���ࣩ
						imageFolder = new ImageFolder();
						imageFolder.setDir(dirPath);
						imageFolder.setFirstImagePath(firstImage);
					}
					if (parentFile.list() == null)
						continue;

					int picSize = parentFile.list(new FilenameFilter() {

						@Override
						public boolean accept(File dir, String filename) {
							if (filename.endsWith(".jpg")
									|| filename.endsWith(".jpeg")
									|| filename.endsWith(".png")) {
								return true;
							}
							return false;
						}
					}).length;
					totalCount += picSize;

					imageFolder.setCount(picSize);// ���ø��ļ����е�ͼƬ��jpg��png������
					// ��ӵ��ļ��м�����
					mImageFloders.add(imageFolder);

					if (picSize > mPicsSize) {
						mPicsSize = picSize;
						mImgDir = parentFile;// ͼƬ�����ļ���
					}
				}
				mCursor.close();

				// ɨ����ɣ��ͷ�HashSet
				mDirPaths = null;

				// ֪ͨHandlerɨ��ͼƬ���
				mHandler.sendEmptyMessage(0x110);
			}
		}).start();
	}

//	/**
//	 * ��һ������ͼƬ���ļ���File������һ��ʱ�����µ�ͼƬ����·��
//	 */
//	public String findLatestPicPath(File dir){
//		String latestPicPath = null;
//		
//		return latestPicPath;
//	}
	
	/**
	 * ͼƬɨ����ɺ���
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mProgressDialog.dismiss();
			// ΪView������
			data2View(mImgDir, mImgs);
			// ��ʾ����ͼƬ���ļ����б�
			// showImageFolderList();
		}
	};

	/**
	 * չʾ����ͼƬ���ļ��е�ListView
	 */
	protected void showImageFolderList() {

		lvFolders = (ListView) findViewById(R.id.list_imagefolder);
		lvFolders.setAdapter(new ListDirAdapter(this, mImageFloders));
	}

	public void data2View(File Dir, List<String> imgUrls) {

		if (Dir == null) {
			Toast.makeText(this, "û��ɨ�赽ͼƬ", 0).show();
			return;
		}
		imgUrls = Arrays.asList(Dir.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				if (filename.endsWith(".jpg")
						|| filename.endsWith(".jpeg")
						|| filename.endsWith(".png")) {
					return true;
				}
				return false;
			}
		}));
		//����ת����ʱ����������
		Collections.reverse(imgUrls);
		
		Log.i("FileAdapter", "��ʼ��ʱͼƬ����"+imgUrls.size());
		mAdapter = new GalleryAdapter(this,btnSubmit, Dir.getAbsolutePath(), imgUrls,mGirdView);
		mGirdView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ll_title:
			// չʾ�ļ����б�
			showImageFolders(mPopWinowTitle);
			break;
		case R.id.btn_submit://�����ѡͼƬ·��
			if(mAdapter.mSelectedImage.size() == 0){
				return;
			}
			Log.i("SelectedPath",mAdapter.mSelectedImage.toString());
			
			if(mcboxOriginalPic.isChecked()){//ԭͼ·��
				for(int i=0;i<mAdapter.mSelectedImage.size();i++){
					Log.i("GalleryActivity","ԭͼ·��:"+ mAdapter.mSelectedImage.get(i));
				}
			}else{//ѹ��ͼ·��,��Ҫѹ��ʱɾ��ע�ͼ��ɣ�������ȷ
//				try {
//					//ѭ��ѹ��ÿ����ѡͼƬ
//					for(int i=0;i<mAdapter.mSelectedImage.size();i++){
//						//ͼƬѹ��
//						String strcompress = ImageCompressTools.compressImage(this, mAdapter.mSelectedImage.get(i),400,800, 60);//ѹ����400X800��С,���һ����������ڶ���ѹ������,100%��ʾ�����еڶ���ѹ��
//						Log.i("Compress", "ѹ��·��:" + strcompress);
//					}
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
			}
			this.finish();//�رյ�ǰactivity
			break;
			
		default:
			break;
		}
	}

	/**
	 * չʾͼƬ�ļ����б��PopupWindow
	 */
	private void showImageFolders(View view) {

//		PopupWindow popupWindow = null;
		  
		
		// һ���Զ���Ĳ��֣���Ϊ��ʾ������
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.popup_imagefolders, null);
		// ����ʾͼƬ�ļ�������
		lvFolders = (ListView) contentView.findViewById(R.id.list_imagefolder);
		final ListDirAdapter adapter = new ListDirAdapter(getApplicationContext(),
				mImageFloders);
		Log.i("mImageFloders", "�ļ�����:" + mImageFloders.size());
		lvFolders.setAdapter(adapter);

		//ˢ������
		lvFolders.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				ImageFolder curImageFolder;//��ǰ���ѡ����ļ���·��
				File curDir;//��ǰ���ѡ����ļ���
				List<String> curImgs;//��ǰ�ļ��������е�ͼƬ
				
				curImageFolder = (ImageFolder) adapter.getItem(position);
				tvShowFolsers.setText(curImageFolder.getName());
				curDir = new File(curImageFolder.getDir());
				curImgs = Arrays.asList(curDir.list(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String filename) {

						if (filename.endsWith(".jpg") || filename.endsWith(".png")  
			                    || filename.endsWith(".jpeg")){  
							Log.i("File", filename);
			                return true;  
						}
						return false;
					}
				}));
				
				Log.i("File", "ͼƬ��:"+curImgs.size());
				mAdapter.mSelectedImage.clear();//�����ѡ���ͼƬ
				//ˢ��GridView����
				data2View(curDir, curImgs);
				//�ر�PopupWindow
				mPopupWindow.dismiss();
			}
		});
		
		//��ʼ��PopupWindow
		mPopupWindow = new PopupWindow(contentView,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setTouchInterceptor(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("PopupShowImageFolders", "onTouch");
				return false;
				// �����������true�Ļ���touch�¼���������
                // ���غ� PopupWindow��onTouchEvent�������ã���������ⲿ�����޷�dismiss
			}
		});

		// ���������PopupWindow�ı����������ǵ���ⲿ������Back�����޷�dismiss����
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		
		//��ʾ
		mPopupWindow.showAsDropDown(tvShowFolsers);
	}
	
	public interface ShowSelectedImgNum{
		public void ShowSlectedImgNum();
	}
}
