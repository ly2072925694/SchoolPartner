package com.cpu.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cpu.constant.GetUserInformation;
import com.cpu.constant.InternetUrl;
import com.cpu.tools.DealHandler;
import com.cpu.tools.OperateFile;
import com.example.schoolpartner.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;


public class LoginActivity extends Activity implements OnClickListener{
	
	
	private EditText editTextPhone = null;
	private EditText editTextPassword = null;
	private Button btnLogin = null;
	private Button btnForgetPassword = null;
	private Button btnRegist = null;
	
	private ImageView imageBack = null;
	private String data;
	private String url;
	
	private TextView json_content = null;
	private ProgressDialog progressBar = null;
	private ArrayList<String> info = new ArrayList<String>();
//	Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			
//			//json_content.setText((String)msg.obj);
//			if(msg.what == 200) {
//				progressBar.dismiss();
//				
//			}else if(msg.what == 201) {
//				
//			}
//		};
//	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
//		InitColor();
	}
	private void init() {
		
		json_content = (TextView) findViewById(R.id.json_content);
		
		editTextPhone = (EditText) findViewById(R.id.login_txt_name);
		editTextPassword = (EditText) findViewById(R.id.login_txt_password);
		//editTextPhone.setText("18983663110");
		btnLogin = (Button) findViewById(R.id.login_btn_login);
		btnForgetPassword = (Button) findViewById(R.id.login_btn_forgetPsw);
		btnRegist = (Button) findViewById(R.id.login_btn_regist);
		imageBack = (ImageView) findViewById(R.id.back_image);
		
		btnLogin.setOnClickListener(this);
		btnForgetPassword.setOnClickListener(this);
		btnRegist.setOnClickListener(this);
		imageBack.setOnClickListener(this);
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
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()){
		case R.id.login_btn_login:
			showProgressDialog();
			try {
				toInternet();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case R.id.login_btn_regist:
			Intent intent1 = new Intent(LoginActivity.this,RegisterMessage.class);
			this.startActivity(intent1);
			break;
		case R.id.login_btn_forgetPsw:
			Intent intent = new Intent(LoginActivity.this,MemberListActivity.class);
			this.startActivity(intent);
			break;
		case R.id.back_image:
			this.finish();
			break;
		}
		
	}
	public void toInternet() throws IOException{
		List<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("Telephone",editTextPhone.getText().toString()));
		params.add(new BasicNameValuePair("Password",editTextPassword.getText().toString()));

		new DealHandler(params,InternetUrl.HeaderPath + "API/AppLogin/Login") {
			
			
			@Override
			public void success(String result) {
				
				
				JSONObject jsonObject;
				//Toast.makeText(getApplicationContext(),"you"+result, Toast.LENGTH_SHORT).show();
				try {
					jsonObject = new JSONObject(result);
					int code = jsonObject.getInt("Code");
					//System.out.println("Code---------->" + code);
					//Toast.makeText(getApplicationContext(),"you", Toast.LENGTH_SHORT).show();
					if(code == 200){
						String data = null;
						data = jsonObject.getString("data");
						JSONObject obj1 = new JSONObject(data);
						String AppUserModel = obj1.getString("AppUserModel");
						JSONObject obj2 = new JSONObject(AppUserModel);
						String Token = obj2.getString("Token");
						Log.i("LoginActivity", "Token:"+Token);
						
						String id = obj2.getString("Id");
						String name = obj2.getString("Username");
						String sex = obj2.getString("Sex");
						info.add(Token);
						info.add(id);
						info.add(name);
						info.add(obj2.getString("Telephone"));
						info.add(sex);
						try {
							OperateFile.writeTxtFile(info, "Token.txt");
						} catch (IOException e) {
							e.printStackTrace();
							Log.i("LoginActivity", "写入Token失败");
						}
						
						try {
							Log.i("LoginActivity","读取的Token:" + GetUserInformation.ReadToken("Token.txt"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.i("LoginActivity", "读取Token失败");
						}
//						Message msg = new Message();
//						msg.obj = Token;
//						msg.what = 200;
//						handler.sendMessage(msg);
						progressBar.dismiss();
						Toast.makeText(getApplicationContext(),"登录成功", Toast.LENGTH_SHORT).show();
						finish();
					}else if(code == 201){
						String Info = jsonObject.getString("data");
						jsonObject = new JSONObject(Info);
						Toast.makeText(getApplicationContext(), jsonObject.getString("returnInfo"),Toast.LENGTH_SHORT).show();
						
						Message msg = new Message();
						msg.what = 201;
						handler.sendMessage(msg);
						
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	}
	
	private void showProgressDialog () {
		
		progressBar = new ProgressDialog(this);
		progressBar.setCancelable(true);
		progressBar.setMessage("登录中...");
		progressBar.show();
		
		
	}
	

}
