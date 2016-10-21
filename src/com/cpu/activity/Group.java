package com.cpu.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cpu.constant.GetUserInformation;
import com.cpu.constant.InternetUrl;
import com.cpu.entity.GroupInfo;
import com.cpu.entity.PublicInformation;
import com.cpu.tools.DealHandler;
import com.example.schoolpartner.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Group extends Activity {
	private ListView list;
	private SimpleAdapter adapter;
	private ArrayList<HashMap<String, Object>> arr;
	private int position;
	private String data;
	private ArrayList<GroupInfo> groupInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.group);
		init();
		list = (ListView) findViewById(R.id.listView1);
		adapter = new SimpleAdapter(getApplicationContext(), arr,
				R.layout.grouper_list,
				new String[] {"user_name","title"},
                new int[] {R.id.textView1,R.id.textView2});
		list.setAdapter(adapter);
//		InitColor();
		super.onCreate(savedInstanceState);
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
	private void init() {
		formInternet();
		arr = new ArrayList<HashMap<String,Object>>();
		for(int i = 0;i<10;i++){
			HashMap<String, Object> map = new HashMap<String, Object>(); 
			map.put("user_name", groupInfo.get(i).getName());  
			map.put("title", groupInfo.get(i).getAddr()); 
			arr.add(map);
		}
	}
	private void formInternet() {
		groupInfo = new ArrayList<GroupInfo>();
		List<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("id",
				position+""));

		new DealHandler(params,
				InternetUrl.HeaderPath 
				+ "/API/AppLogin/Login"){

			@Override
			public void success(String result) {
				JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(result);
						int code = jsonObject.getInt("Code");
						if(code == 200){
							String str = jsonObject.getString("data");
							JSONArray json = new JSONArray(str);
							for(int i=0;i<json.length();i++){
								GroupInfo group = new GroupInfo();
								jsonObject = (JSONObject) json.get(i);
								//数据库没有地址详情
//								group.setAddr(jsonObject.getString("Addr"));
								group.setName(jsonObject.getString("TimeStart"));
								groupInfo.add(group);
							}
						}else{
							//参与者传输错误
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
			}
			
		};
	}
}
