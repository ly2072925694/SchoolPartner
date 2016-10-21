package com.cpu.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.cpu.constant.GetUserInformation;
import com.cpu.constant.InternetUrl;
import com.cpu.tools.DealHandler;
import com.example.schoolpartner.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Clear extends Activity implements OnClickListener{
	private boolean dele;
	AlertDialog dial;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clear);
		findViewById(R.id.linear01).setOnClickListener(this);
		findViewById(R.id.linear02).setOnClickListener(this);
		findViewById(R.id.linear03).setOnClickListener(this);
//		InitColor();
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
    public void onClick(View arg0)
    {
		switch (arg0.getId()) {
		case R.id.linear01:
			if(diloag()){
//				try {
//					toInternet("我参与的");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			 Intent intent01 = new Intent("Me");
			 LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(intent01);
			}
			 break;

		case R.id.linear02:
			if(diloag()){
//				try {
//					toInternet("我发布的");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			 Intent intent02 = new Intent("Other");
			 LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(intent02);
			}
			break;
		case R.id.linear03:
			if(diloag()){
//				try {
//					toInternet("全部");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			 Intent intent03 = new Intent("All");
			 LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(intent03);
			}
			break;
		}
    }
	public boolean diloag() {
		dele = false;
		 LayoutInflater flater = LayoutInflater.from(Clear.this);
		 
			View dialog = flater.inflate(R.layout.make_sure, null);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(
					Clear.this);  
			
	        builder.setView(dialog);  
	        builder.create();  
	          
	        TextView bt_test = (TextView) dialog.findViewById(R.id.textView2);  
	        bt_test.setOnClickListener(new android.view.View.OnClickListener(){  
	              
	            @Override  
	            public void onClick(View v) { 
	            	dele = true;
	            	dial.dismiss();
	            }  
	        });  
	        TextView bt_test1 = (TextView) dialog.findViewById(R.id.textView3);  
	        bt_test1.setOnClickListener(new android.view.View.OnClickListener(){  
	              
	            @Override  
	            public void onClick(View v) { 
	            	dial.dismiss();
	            }  
	        });   
	        dial = builder.show(); 
	        return dele;
	}
	public void toInternet(String name) throws IOException{
			List<BasicNameValuePair> params;
			params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Myadvice",
					name));
			params.add(new BasicNameValuePair("UserId",
						GetUserInformation.ReadId("Token")));

			new DealHandler(params,InternetUrl.HeaderPath + "API/AppLogin/Login") {
				
				
				@Override
				public void success(String result) {
					JSONObject jsonObject;
					Toast.makeText(getApplicationContext(),
							"you"+result, Toast.LENGTH_SHORT).show();
					try {
						jsonObject = new JSONObject(result);
						int code = jsonObject.getInt("Code");
						Toast.makeText(getApplicationContext(),
								"you", Toast.LENGTH_SHORT).show();
						if(code == 200){
						}else if(code == 201){
							Toast.makeText(getApplicationContext(), 
									""+jsonObject.getString("returnInfo"),
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			};
		}
}
