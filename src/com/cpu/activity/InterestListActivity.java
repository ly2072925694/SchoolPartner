package com.cpu.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.cpu.adapter.InterestListAdapter;
import com.cpu.constant.InternetUrl;
import com.cpu.tools.DealHandler;
import com.example.schoolpartner.R;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InterestListActivity extends Activity implements OnClickListener {

	private ListView lvInterest;
	private InterestListAdapter adapter;
	private List<String> datas = new ArrayList<String>();//��Ȥ�б�(�ӷ���������list)
	private List<String> userInterest = new ArrayList<String>();//�û����е���Ȥ�б�(�ӷ���������list)
	private ImageView ivBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interestlist);
		initData();
		init();
	}
	
	/**
	 * �õ���Ȥ�б�����
	 */
	public void initData(){
		
		//获得之前页面传过来的用户已拥有的兴趣
		Bundle bundle = getIntent().getExtras();
		userInterest = (List<String>) bundle.getSerializable("interest");
		Log.i("InterestList", userInterest.toString());
		//网络加载兴趣列表
		getInterestList();
		
	}
	
	public void getInterestList(){
		List<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		
		new DealHandler(params,InternetUrl.HeaderPath + "/API/Evaluate/GetInterestList") {
			
			@Override
			public void success(String result) {
				JSONObject jsonObject;
				Log.i("InterestActivity", "返回消息"+result);
				try {
					jsonObject  = new JSONObject(result);
					int code  = jsonObject.getInt("Code");
					if (code == 200) {
						String info = jsonObject.getString("data");
						jsonObject = new JSONObject(info);
						JSONArray jsonArray = jsonObject.getJSONArray("model");
						Log.i("InterestActivity", jsonArray.toString());
						for(int i =0;i<jsonArray.length();i++){
							JSONObject jsonInterest = (JSONObject) jsonArray.get(i);
							Log.i("InterestActivity", "兴趣 " + i + ":" +  jsonInterest.getString("Interest"));
							datas.add(jsonInterest.getString("Interest"));
						}
						
					}else if(code == 201){
						Toast.makeText(getApplicationContext(), "发布失败，请检查网络连接", 0).show();
						
						String info = jsonObject.getString("Data");
						jsonObject = new JSONObject(info);
						Log.i("InterestActivity", "失败" + jsonObject.getString("retrunInfo"));
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}
	
	public void init(){
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(this);
		lvInterest = (ListView) findViewById(R.id.lv_interest);
		
		adapter = new InterestListAdapter(this, datas,userInterest);
		lvInterest.setAdapter(adapter);
		
		lvInterest.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					adapter.updateSelected(position, lvInterest);//����ѡ��ͼƬ����ʾ
			}
		});		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			transportData();
			this.finish();
			break;

		default:
			break;
		}
	}
	
	
	/**
	 * ������Ȥ����
	 */
	public void transportData(){
		Intent intent = new Intent(this, MyInfoActivity.class);
		Bundle bundle = new Bundle();
		if(InterestListAdapter.userInterest != null && InterestListAdapter.userInterest.size()>0){
			bundle.putSerializable("interest", (Serializable) InterestListAdapter.userInterest);
			intent.putExtras(bundle);
			setResult(MyInfoActivity.REQUEST_CODE_INTEREST, intent);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK){//点击了返回键
			transportData();
		}
		return super.onKeyDown(keyCode, event);
	}
}
