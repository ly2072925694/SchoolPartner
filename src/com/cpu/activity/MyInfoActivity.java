package com.cpu.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import cn.sharesdk.framework.ResizeLayout;

import com.cpu.adapter.InterestListAdapter;
import com.cpu.constant.GetUserInformation;
import com.cpu.constant.InternetUrl;
import com.cpu.tools.DealHandler;
import com.example.schoolpartner.R;
import com.example.schoolpartner.R.layout;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyInfoActivity extends Activity implements OnClickListener {

	private LinearLayout llMyinfo;
	private LinearLayout llSex;
	private EditText etSex;
	private LinearLayout llLoveStatus;
	private EditText etLoveStatus;
	private LinearLayout llBirthday;
	private EditText etBirthday;
	private LinearLayout llConstellation;
	private EditText etConstellation;
	private TextView tvSave;
	private LinearLayout llBackground;
	private ImageView ivHeadPhoto;
	private RelativeLayout rlInterestHoby;
	private TextView tvInterestHoby;
	private ImageView ivBack;
	private String strToken;
	
	//个人信息
	private EditText etNickName,etRealName,etEmail,etAddress,etDetail;
	private Form form;//验证表单
	
	private String strTele;//手机号
	private String strNickName;//昵称
//	private String strRealName;//真实姓名
//	private String strSchool;//学校
//	private String strEmail;//邮箱
	private String strSex;
	private String strBirthday;
	private String strConstellation;//星座
	private String strLovestatus;//恋爱状况
	private String strAddress;
	private String strDetail;//交友宣言
	
	private String strInterest ="";//兴趣爱好
	private List<String> mInterests = new ArrayList<String>();//兴趣爱好(集合)
	
	public static int REQUEST_CODE_INTEREST = 0x11;
	public static int REQUEST_CODE_Constellation = 0x12;
	public boolean isEdit = false;//是否点击了修改
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinfo);
		form = new Form();
		init();
		initData();
	}
	
	
	
	public void init(){
		llMyinfo = (LinearLayout) findViewById(R.id.ll_myinfo);
		llSex = (LinearLayout) findViewById(R.id.ll_sex);
		etSex = (EditText) findViewById(R.id.etxt_sex);
		llLoveStatus = (LinearLayout) findViewById(R.id.ll_love_status);
		etLoveStatus = (EditText) findViewById(R.id.etxt_love_status);
		llBirthday = (LinearLayout) findViewById(R.id.ll_birthday);
		etBirthday = (EditText) findViewById(R.id.etxt_birthday);
		llConstellation = (LinearLayout) findViewById(R.id.ll_Constellation);
		etConstellation = (EditText) findViewById(R.id.etxt_Constellation);
		tvSave = (TextView) findViewById(R.id.tv_save);
		llBackground = (LinearLayout) findViewById(R.id.ll_background);
		ivHeadPhoto = (ImageView) findViewById(R.id.iv_head_photo);
		etNickName = (EditText) findViewById(R.id.etxt_nick_name);
		etEmail = (EditText) findViewById(R.id.etxt_email);
		etRealName = (EditText) findViewById(R.id.etxt_real_name);
		etAddress = (EditText) findViewById(R.id.etxt_address);
		etDetail = (EditText) findViewById(R.id.etxt_detail);
		rlInterestHoby = (RelativeLayout) findViewById(R.id.rl_interesthoby);
		tvInterestHoby = (TextView) findViewById(R.id.tv_interesthoby);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		
		llSex.setOnClickListener(this);
		etSex.setOnClickListener(this);
		llLoveStatus.setOnClickListener(this);
		etLoveStatus.setOnClickListener(this);
		llBirthday.setOnClickListener(this);
		etBirthday.setOnClickListener(this);
		llConstellation.setOnClickListener(this);
		etConstellation.setOnClickListener(this);
		tvSave.setOnClickListener(this);
		ivHeadPhoto.setOnClickListener(this);
		rlInterestHoby.setOnClickListener(this);
		ivBack.setOnClickListener(this);
	}

	/**
	 * 获取个人信息
	 */
	public void initData(){
		
		//读取token、Tele
		try {
			strToken =  GetUserInformation.ReadId("Token.txt");
//			strTele = GetUserInformation.ReadTele("");
			
			Log.i("MyinfoActivity", "Token:" + strToken+" Tele" + strTele);
		} catch (Exception e) {
			Log.i("MyinfoActivity", "读取Token或Tele失败");
		}
		
		List<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("Telephone",strTele));
		params.add(new BasicNameValuePair("token",strToken));
		new DealHandler(params,InternetUrl.HeaderPath + "/API/AppLogin/GetPersonInfo") {

			@Override
			public void success(String result) {

				JSONObject jsonObject;
				Log.i("MyinfoActivity", "返回消息:"+result);
				try {
					jsonObject  = new JSONObject(result);
					int code  = jsonObject.getInt("Code");
					if (code == 200) {
//						Toast.makeText(getApplicationContext(), "获取个人信息成功", 0).show();
						String info = jsonObject.getString("data");
						jsonObject = new JSONObject(info);
						String model = jsonObject.getString("model");
						jsonObject = new JSONObject(model);
						
						//获得个人信息
						strNickName=jsonObject.getString("Username");
						strSex = jsonObject.getString("Sex");
						strBirthday = jsonObject.getString("Birthday");
						strConstellation = jsonObject.getString("Constellation");
						strLovestatus = jsonObject.getString("Lovesituation");
						strAddress = jsonObject.getString("Address");
						strDetail = jsonObject.getString("Detail");
						String strInterest = jsonObject.getString("Interest");
						
						//信息处理
						//处理生日
						strBirthday = strBirthday.toLowerCase();
						strBirthday = strBirthday.replace("/date(", "");
						strBirthday = strBirthday.replace(")/","");
						long bir = Long.parseLong(strBirthday);
						Calendar calBir = Calendar.getInstance();
						calBir.setTimeInMillis(bir);
						strBirthday = calBir.get(Calendar.YEAR)+ "-" + (calBir.get(Calendar.MONTH)+1) + "-" + calBir.get(Calendar.DAY_OF_MONTH);
						
						//处理兴趣
						String[] interest = strInterest.split("_");
						for(int i =0;i<interest.length;i++){
							if(interest[i] != null){
								mInterests.add(interest[i]);
							}
						}
								
						//将个人信息填写到页面中
						etNickName.setText(strNickName);
						etSex.setText(strSex);
						etBirthday.setText(strBirthday);
						etConstellation.setText(strConstellation);
						etLoveStatus.setText(strLovestatus);
						etAddress.setText(strAddress);
						etDetail.setText(strDetail);
//						String inters = "";
						strInterest = "";
						for(int i=0;i<mInterests.size();i++){
							strInterest += mInterests.get(i) + "、";
						}
						tvInterestHoby.setText(strInterest.substring(0, strInterest.lastIndexOf("、")));//去除最后一个"、"
						
					}else if(code == 201){
//						Toast.makeText(getApplicationContext(), "发布失败，请检查网络连接", 0).show();
						
						String info = jsonObject.getString("Data");
						jsonObject = new JSONObject(info);
						Log.i("MyinfoActivity", "获取个人信息失败:" + jsonObject.getString("retrunInfo"));
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					Log.i("MyinfoActivity", "获取个人信息失败:" + e.toString());
				}
			}
			
		};
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			if(isEdit){
				showIsGiveUpDialog();
			}else{
				finish();
			}
			break;
		
		//修改资料
		case R.id.tv_save:
//			if(isEdit == false){
//				isEdit = true;
//				tvSave.setText("保存");
//				llMyinfo.setBackgroundResource(R.drawable.myinfo_border_edit);
				
//			}else {//修改资料
			
				//验证昵称非空
				Validate vlNickName = new Validate(etNickName);
				NotEmptyValidator notEmpty = new NotEmptyValidator(this);
				vlNickName.addValidator(notEmpty);
				
				//如果填写了邮件，验证邮件格式
				if(etEmail.getText().toString() != ""){
					Validate vlEmail = new Validate(etEmail);
					EmailValidator email = new EmailValidator(this);
					vlEmail.addValidator(email);
					form.addValidates(vlEmail);
				}
				
				//把验证类添加到Form表单里
				form.addValidates(vlNickName);
				boolean isOk = form.validate();//验证
				
				if(isOk == false){
					return;
				}
				save();//请求网络，保存信息
				isEdit = false;
//				tvSave.setText("修改");
//				llMyinfo.setBackgroundResource(R.drawable.myinfo_border);
//			}
			break;
		//头像
		case R.id.iv_head_photo:
			Intent intent = new Intent(Intent.ACTION_PICK,null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent, 2);
			break;
			
		//性别选择
		case R.id.ll_sex:
			showSexPickDialog();
			break;
		case R.id.etxt_sex:
			showSexPickDialog();
			break;
			
		//出生日期
		case R.id.ll_birthday:
			showDateDialog();
			break;
		case R.id.etxt_birthday:
			showDateDialog();
			break;
			
		//星座
		case R.id.ll_Constellation:
			Intent intentConstell = new Intent(this, ConstellationListActivity.class);
			intentConstell.putExtra("userConstell", etConstellation.getText().toString());
			startActivityForResult(intentConstell, REQUEST_CODE_Constellation);
			break;
		case R.id.etxt_Constellation:
			Intent intentConstell2 = new Intent(this, ConstellationListActivity.class);
			intentConstell2.putExtra("userConstell", etConstellation.getText().toString());
			startActivityForResult(intentConstell2, REQUEST_CODE_Constellation);
			break;
			
			
		//恋爱状况选择
		case R.id.ll_love_status:
			showLoveStatuPickDialog();
			break;
		case R.id.etxt_love_status:
			showLoveStatuPickDialog();
			break;
			
		//兴趣爱好
		case R.id.rl_interesthoby:
			Intent intentHoby = new Intent(this, InterestListActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("interest", (Serializable) mInterests);
			intentHoby.putExtras(bundle);
			startActivityForResult(intentHoby, REQUEST_CODE_INTEREST);
			break;
			
		
		default:
			break;
		}
	}
	
	/**
	 * 请求网络，保存个人信息
	 */
	private void save() {

		List<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		String inter= "";
		
		for(int i=0;i< mInterests.size();i++){
			inter += mInterests.get(i) + "_";
		}
		
		params.add(new BasicNameValuePair("Telephone","18983663110"));
		params.add(new BasicNameValuePair("Username",etNickName.getText().toString()));
		params.add(new BasicNameValuePair("Sex",etSex.getText().toString()));
		params.add(new BasicNameValuePair("Birthday",etBirthday.getText().toString()));
		params.add(new BasicNameValuePair("Constellation",etConstellation.getText().toString()));
		params.add(new BasicNameValuePair("Lovesituation",etLoveStatus.getText().toString()));
		params.add(new BasicNameValuePair("Address",etAddress.getText().toString()));
		params.add(new BasicNameValuePair("Detail",etDetail.getText().toString()));
//		params.add(new BasicNameValuePair("Email",));
//		params.add(new BasicNameValuePair("School",));
		params.add(new BasicNameValuePair("Interest",inter));
		new DealHandler(params,InternetUrl.HeaderPath + "/API/AppLogin/UpdateInfo") {
			
			@Override
			public void success(String result) {

				JSONObject jsonObject;
				Log.i("Myinfo", "返回消息:" + result);
				try {
					jsonObject = new JSONObject(result);
					int code  = jsonObject.getInt("Code");
					if(code == 200){
						Toast.makeText(getApplicationContext(), "保存成功", 0).show();
					}else if(code ==201){
						Toast.makeText(getApplicationContext(), "保存失败，请检查网络连接", 0).show();
						String info = jsonObject.getString("Data");
						jsonObject = new JSONObject(info);
						Log.i("send", "保存失败返回消息" + jsonObject.getString("retrunInfo"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		};
	}

	/**
	 * 性别选择Dialog
	 */
	public void showSexPickDialog(){
		String title = "性别";
		final String[] items = new String[] { "男", "女" ,"保密"};
		new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT).setTitle(title).setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				etSex.setText(items[which]);
			}
		}).show();
	}
	
	/**
	 * 弹出是否放弃更改弹窗
	 */
	public void showIsGiveUpDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		builder.setMessage("放弃更改？");
		builder.setTitle("提示");
		builder.setPositiveButton("放弃", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				finish();
			}
		});
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		
		builder.create().show();
	}

	
	/**
	 * 恋爱状况Dialog
	 */
	public void showLoveStatuPickDialog(){
		String title = "恋爱状况";
		final String[] items = new String[] { "恋爱中", "单身" ,"保密","已婚"};
		new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT).setTitle(title).setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				etLoveStatus.setText(items[which]);
			}
		}).show();
	}
	
	/**
	 *  日期选择Dialog
	 */
	public void showDateDialog() {

		// 获取当前时间
		Calendar curDate = Calendar.getInstance(Locale.CHINA);//获得一个日历对象
		int curyear = curDate.get(Calendar.YEAR);
		int curmonth = curDate.get(Calendar.MONTH);
		int curday = curDate.get(Calendar.DAY_OF_MONTH);

		//弹出日期dialog,并初始化为当前日期
		DatePickerDialog datePicker =  new DatePickerDialog(MyInfoActivity.this,AlertDialog.THEME_HOLO_LIGHT ,
				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						
						etBirthday.setText(year + "-" + (monthOfYear + 1)
								+ "-" + dayOfMonth);
					}
				}, curyear - 20, 0, 1);
		datePicker.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 2:
			Uri uri = null;
            if (data == null) {
                return;
            }
            if (resultCode == RESULT_OK) {
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(this, "SD卡不可用",1).show();
                    return;
                }
                uri = data.getData();
                startImageAction(uri, 200, 200,3, true);
            } else {
                Toast.makeText(this, "照片获取失败",1).show();
            }
			break;
			
		case 3:
            if (data == null) {
                return;
            } else {
                saveCropAvator(data);
            }
            break;
            
		case 0x11:
			if(resultCode == RESULT_CANCELED){
				Toast.makeText(this, "没有选择兴趣", 0);
				return;
			}
			Bundle bundle = data.getExtras();
			mInterests = (List<String>) bundle.getSerializable("interest");
//			String strInterest = "";
			strInterest = "";
			for(int i=0;i< mInterests.size();i++){
				strInterest += mInterests.get(i) + "、";
			}
			strInterest = strInterest.substring(0, strInterest.lastIndexOf("、"));
			tvInterestHoby.setText(strInterest);
			
			break;

		case 0x12:
			if(resultCode == RESULT_CANCELED){
				return;
			}
			String constell = data.getStringExtra("Constellation");
			etConstellation.setText(constell);
			break;
		default:
			break;
		}
	}

	/**
	 * 图像裁剪
	 * @param uri
	 * @param outputX
	 * @param outputY
	 * @param requestCode
	 * @param isCrop
	 */
	public void startImageAction(Uri uri, int outputX, int outputY, int requestCode, boolean isCrop) {

		Intent intent = null;
        if (isCrop) {
            intent = new Intent("com.android.camera.action.CROP");
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
	}
	
	/**
	 * 头像裁剪过后，将图片变为圆角
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
	
	/**
	 * 保存图片
	 * @param data
	 */
	public void saveCropAvator(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			Log.i("life", "avatar - bitmap = " + bitmap);
			if (bitmap != null) {
				bitmap = toRoundCorner(bitmap, 10);// ����Բ�Ǵ�����
				ivHeadPhoto.setImageBitmap(bitmap);
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle();
				}
			}
		}
	}

}
