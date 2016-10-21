package com.cpu.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.cpu.adapter.GalleryAdapter;
import com.cpu.adapter.GridImgsAdapter;
import com.cpu.constant.InternetUrl;
import com.cpu.tools.DealHandler;
import com.cpu.tools.ImageUtils;
import com.cpu.view.WrapHeightGridView;
import com.example.schoolpartner.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
//import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 发布约伴Activity
 * 
 * @author ZJC
 * 
 */
public class Send extends Activity implements OnClickListener,OnItemClickListener {
	
	private TextView tvTitle;
	private TextView tvDetail;
	private TextView tvMemo;
	private TextView tvTime;
	private TextView tvImgs;
	private TextView tvNumber; 
	private TextView tvSex; 
	private TextView tvAddress;
	private TextView tvPublish;//发布活动
	
	private ImageView imgBack;
	
	private Intent intentCategory;// Լ�����
	private String strCategory;
	private EditText etTitle;
	private EditText etContent;
	private EditText etStartDate;
	private EditText etEndDate;
	private EditText etAddress;
	private EditText etNumber;
	private EditText etSex;
	private EditText etMemo;
	private ImageView imgAddPic;
	private Button btnSubtract;//减人数
	private Button btnAdd;//加人数
	
	private WrapHeightGridView gvImages;//九宫格显示添加的图片
	private LinearLayout llWrapGrid;//包裹九宫格的线性布局
	private GridImgsAdapter gvAdapter;//适配器
	public static ArrayList<Uri> imgUris = new ArrayList<Uri>();//添加的图片地址集合

	//性别限制
	private String strSex = null;
	// 活动开始日期
	private int startYear;
	private int startMonth;
	private int startDay;

	// 活动 结束日期
	private int endYear;
	private int endMonth;
	private int endDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send);
		intentCategory = getIntent();
		strCategory = intentCategory.getStringExtra("Category");
//		Toast.makeText(Send.this, "发布Activity:" + strCategory, 0).show();
		initView();
		init();
		
	}
	
	/**
	 * 中文字体加粗
	 */
	public void initView(){
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDetail = (TextView) findViewById(R.id.tv_detail);
		tvMemo = (TextView) findViewById(R.id.tv_memo);
		tvTime = (TextView) findViewById(R.id.tv_time);
		tvImgs = (TextView) findViewById(R.id.tv_imgs);
		tvAddress = (TextView) findViewById(R.id.tv_address);
		tvNumber = (TextView) findViewById(R.id.tv_number);
		tvSex = (TextView) findViewById(R.id.tv_sex);
		
		
		//字体加粗
		TextPaint tp1 = tvTitle.getPaint(); 
		tp1.setFakeBoldText(true);
		TextPaint tp2 = tvDetail.getPaint(); 
		tp2.setFakeBoldText(true);
		TextPaint tp3 = tvMemo.getPaint(); 
		tp3.setFakeBoldText(true);
		TextPaint tp4 = tvTime.getPaint(); 
		tp4.setFakeBoldText(true);
		TextPaint tp5 = tvImgs.getPaint(); 
		tp5.setFakeBoldText(true);
		TextPaint tp6 = tvNumber.getPaint();
		tp6.setFakeBoldText(true);
		TextPaint tp7 = tvAddress.getPaint();
		tp7.setFakeBoldText(true);
		TextPaint tp8 = tvSex.getPaint();
		tp8.setFakeBoldText(true);
	}

	/**
	 * 初始化
	 */
	public void init() {
		imgBack = (ImageView) findViewById(R.id.imageView1);
		llWrapGrid = (LinearLayout) findViewById(R.id.linearLayout_gridtableLayout);
		gvImages = (WrapHeightGridView) findViewById(R.id.grid_images);
		etTitle = (EditText) findViewById(R.id.et_title);
		etContent = (EditText) findViewById(R.id.et_detail);
		etMemo = (EditText) findViewById(R.id.et_memo);
		etStartDate = (EditText) findViewById(R.id.et_start_date);
		etEndDate = (EditText) findViewById(R.id.et_end_date);
		etNumber = (EditText) findViewById(R.id.et_number);
		etSex = (EditText) findViewById(R.id.et_sex);
		etAddress = (EditText) findViewById(R.id.et_address);
		tvPublish = (TextView) findViewById(R.id.tv_publish);
		btnSubtract = (Button) findViewById(R.id.btn_subtract);
		btnAdd = (Button) findViewById(R.id.btn_add);
		
		gvAdapter = new GridImgsAdapter(this, imgUris, gvImages);//实例化图片显示适配器
		gvImages.setAdapter(gvAdapter);//设置适配器
		gvImages.setOnItemClickListener(this);
		
		imgBack.setOnClickListener(this);
		etStartDate.setOnClickListener(this);
		etEndDate.setOnClickListener(this);
		tvPublish.setOnClickListener(this);
		btnSubtract.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
		etSex.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView1://返回
			finish();
			break;
		case R.id.et_start_date:
			showStartDateDialog();
			break;

		case R.id.et_end_date:
			showEndDateDialog();
			break;
		case R.id.btn_subtract:
			int num = Integer.parseInt(etNumber.getText().toString());
			if(num -1 >= 0){
				etNumber.setText(String.valueOf(num -1));
			}
			break;
		case R.id.btn_add:
			int num2 = Integer.parseInt(etNumber.getText().toString());
			etNumber.setText(String.valueOf(num2 + 1));
			break;
		case R.id.et_sex://性别限制
			showSexPickDialog();
			break;
			
		case R.id.tv_publish://发布活动
			publish();
			break;

		
		default:
			break;
		}

	}

	/**
	 * 性别限制选择
	 */
	private void showSexPickDialog() {

		String title = "性别限制";
		final String[] items = new String[] {"无限制","仅限男生","仅限女生"};
		new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT).setTitle(title).setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				etSex.setText(items[which]);
				switch (which) {
				case 0:
					strSex = null;//无性别限制
					break;
				case 1:
					strSex = "m";
					break;
				case 2:
					strSex = "w";
					break;

				default:
					break;
				}
			}
		}).show();
	}

	/**
	 * 发布活动
	 */
	private void publish(){
		
		List<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("Telephone","18983663110"));
		params.add(new BasicNameValuePair("TypeName",strCategory));//
		params.add(new BasicNameValuePair("Title", etTitle.getText().toString()));
		params.add(new BasicNameValuePair("Content",etContent.getText().toString()));
		params.add(new BasicNameValuePair("Number",etNumber.getText().toString()));
		params.add(new BasicNameValuePair("Address", etAddress.getText().toString()));
		params.add(new BasicNameValuePair("TimeStart",etStartDate.getText().toString()));
		params.add(new BasicNameValuePair("TimeEnd", etEndDate.getText().toString()));
		params.add(new BasicNameValuePair("Memo",etMemo.getText().toString() ));
		params.add(new BasicNameValuePair("Sex",strSex));//性别限制暂时不启用，为null
		
		new DealHandler(params,InternetUrl.HeaderPath + "/API/Activity/AddActivityInfo") {
			
			@Override
			public void success(String result) {
				JSONObject jsonObject;
//				Toast.makeText(getApplicationContext(),
//						"返回消息"+result, Toast.LENGTH_LONG).show();
				Log.i("Send", "返回消息"+result);
				try {
					jsonObject  = new JSONObject(result);
					int code  = jsonObject.getInt("Code");
					if (code == 200) {
						Toast.makeText(getApplicationContext(), "发布成功", 0).show();
					}else if(code == 201){
						Toast.makeText(getApplicationContext(), "发布失败，请检查网络连接", 0).show();
						
						String info = jsonObject.getString("Data");
						jsonObject = new JSONObject(info);
						Log.i("send", "发布失败返回消息" + jsonObject.getString("retrunInfo"));
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case ImageUtils.REQUEST_CODE_FROM_ALBUM:// 选择相册图片后返回
//			if (resultCode == RESULT_CANCELED) {//点了取消键
//			return;
//		}
//		//Toast.makeText(Send.this, "Send：从相册中返回了图片", 0).show();
//		Uri imageUri = data.getData();// 获取图片地址
//		imgUris.add(imageUri);
//		updateImgs();
			if(GalleryAdapter.mSelectedImage.size() > 0){
				for(int i=0;i<GalleryAdapter.mSelectedImage.size();i++){
					imgUris.add(Uri.parse(GalleryAdapter.mSelectedImage.get(i)));
					Log.i("Send", "已选择路径" + GalleryAdapter.mSelectedImage.get(i));
				}
			}
			updateImgs();
			break;

		case ImageUtils.REQUEST_CODE_FROM_CAMERA://相机拍照后返回
			if (resultCode == RESULT_CANCELED) {//点了取消键
				//ImageUtils.deleteImageUri(this,ImageUtils.imageUriFromCamera);//删除uri
				return;
			}else {
				String strimageUriCamera = null;
				Uri imageUriCamera = ImageUtils.imageUriFromCamera;//得到原始图片uri，如果ImageUtils中data的putExtra没设置EXTRA_OUTPUT, 
																	//则调用data.getData()将得到一个压缩图片
				String temp = "file://";
				strimageUriCamera = imageUriCamera.toString().replace(temp, "");//移除路径中的file://前缀
				Log.i("Send", "处理后的路径:" + strimageUriCamera);
				
				//压缩拍照后的图片
//				try {
//					strimageUriCamera = ImageCompressTools.compressImage(this, imageUriCamera.toString(),400,800, 60);
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				imgUris.add(Uri.parse(strimageUriCamera));
				
				updateImgs();
			}
		
			break;
			
		default:
			break;
		}
	}

	/**
	 * 更新图片显示
	 */
	private void updateImgs() {
		gvAdapter.notifyDataSetChanged();//刷新图片显示
		
		
		int colWidth = gvImages.getColumnWidth();//每列宽度
		int rowNum = 0;
		int count = gvImages.getCount();
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Object itemAdapter = parent.getAdapter();//得到当前点击时的适配器
		if(itemAdapter instanceof GridImgsAdapter){//如果是九宫格图片适配器
			if(position == gvAdapter.getCount() -1){//如果点击添加图片按钮，则显示“选择添加图片方式”的Dialog
				ImageUtils.showImagePickDialog(this);//弹出选择添加图片方式的Dialog
			}
		}
	}

	
	/**
	 * 弹出开始日期选择窗口
	 */
	public void showStartDateDialog() {

		Calendar curDate = Calendar.getInstance(Locale.CHINA);// 获得一个日历对象
		int curyear = curDate.get(Calendar.YEAR);
		int curmonth = curDate.get(Calendar.MONTH);
		int curday = curDate.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog datePicker = new DatePickerDialog(Send.this,AlertDialog.THEME_HOLO_LIGHT,
				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						startYear = year;
						startMonth = monthOfYear;
						startDay = dayOfMonth;
						etStartDate.setText(year + "-" + (monthOfYear + 1)
								+ "-" + dayOfMonth);
					}
				}, curyear, curmonth, curday);
//		datePicker.getDatePicker().setMinDate(curDate.getTimeInMillis());// 设置最小日期
		datePicker.show();
	}

	/**
	 *弹出结束日期选择窗口
	 */
	public void showEndDateDialog() {

		// 获取当前时间
		Calendar curDate = Calendar.getInstance(Locale.CHINA);//获得一个日历对象
		int curyear = curDate.get(Calendar.YEAR);
		int curmonth = curDate.get(Calendar.MONTH);
		int curday = curDate.get(Calendar.DAY_OF_MONTH);

		// 弹出日期dialog,如果已经设置开始日期，则初始化为开始日期，否则初始化为当前日期
		DatePickerDialog datePicker;
		if (startYear == 0 && startMonth == 0 && startDay == 0) {
			datePicker = new DatePickerDialog(Send.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,dateSetListener,
					curyear, curmonth, curday);
			datePicker.getDatePicker().setMinDate(curDate.getTimeInMillis());//设置最小日期
		} else {
			datePicker = new DatePickerDialog(Send.this, dateSetListener,
					startYear, startMonth, startDay);
//			datePicker.getDatePicker().setMinDate(curDate.getTimeInMillis());// 设置最小日期
		}
		datePicker.show();
	}

	/**
	 * 第二个日期控件的选择日期后 的监听
	 */
	private OnDateSetListener dateSetListener = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			endYear = year;
			endMonth = monthOfYear;
			endDay = dayOfMonth;
			etEndDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
		}
	};
}
