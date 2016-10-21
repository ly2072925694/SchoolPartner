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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cpu.constant.InternetUrl;
import com.cpu.tools.DealHandler;
import com.example.schoolpartner.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class RegisterPassword extends Activity implements OnClickListener{
	private ImageView register_next_back = null;
	private Button register_next_btn_sure;
	
	private EditText register_next_edit_pwd = null;
	private EditText register_next_edit_pwd_again = null;
	
	private String phone = null;
	
	private ProgressDialog mProgressDialog = null;
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			
			
		};
	};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_view_next);
		
		initView();
		
		//InitColor();
	}
	private void initView() {
		register_next_back = (ImageView) findViewById(R.id.register_next_back);
		register_next_edit_pwd = (EditText) findViewById(R.id.register_next_edit_pwd);
		register_next_edit_pwd_again = (EditText) findViewById(R.id.register_next_edit_pwd_again);
		register_next_btn_sure = (Button) findViewById(R.id.register_next_btn_sure);
		
		register_next_back.setOnClickListener(this);
		register_next_btn_sure.setOnClickListener(this);
		
		Intent intent =  getIntent();
		this.phone = intent.getStringExtra("phone");
		Toast.makeText(getApplicationContext(),phone, Toast.LENGTH_SHORT).show();
		
		
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
		switch (arg0.getId()) {
		case R.id.register_next_back:
			finish();
			break;
		case R.id.register_next_btn_sure://点击下一步按钮
			String pwd = this.register_next_edit_pwd.getText().toString().trim();
			String pwd_again = this.register_next_edit_pwd_again.getText().toString().trim();
			Toast.makeText(getApplicationContext(),pwd + "," +  pwd_again, Toast.LENGTH_SHORT).show();
			if(pwd.equals(pwd_again) && pwd != null && pwd != "") {
				try {
					registe(this.phone,pwd);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				Toast.makeText(getApplicationContext(),"原密码和新密码输入有误", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
	private void registe(String phone, String password) throws IOException{
		List<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("Telephone",phone));
		params.add(new BasicNameValuePair("Password",password));
		
		new DealHandler(params,InternetUrl.HeaderPath + "/API/AppLogin/AppUserRegister") {
			
			@Override
			public void success(String result) {
				
				//Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
				JSONObject jsonObject;
				
				try {
					jsonObject = new JSONObject(result);
					int code = jsonObject.getInt("Code");
					if(code == 200){
						Toast.makeText(getApplicationContext(),"注册成功", Toast.LENGTH_SHORT).show();
					}else if(code == 201){
						String data = jsonObject.getString("data");
						jsonObject = new JSONObject(data);
						String returnInfo = jsonObject.getString("returnInfo");
						Toast.makeText(getApplicationContext(),returnInfo, Toast.LENGTH_SHORT).show();
						
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		};
	}
	
}
