package com.cpu.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cpu.adapter.ImageAdapter;
import com.cpu.adapter.PublishSimpleAdapter;
import com.cpu.constant.GetUserInformation;
import com.cpu.constant.InternetUrl;
import com.cpu.entity.PublicInformation;
import com.cpu.tools.DealHandler;
import com.cpu.tools.OperateFile;
import com.example.schoolpartner.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Detail extends Activity implements OnClickListener{
	private ListView list;
	private SimpleAdapter adapter;
	private ViewPager mViewpager;
	private PublishSimpleAdapter simplePublish;
	private View botton;
	private View top;
	ArrayList<HashMap<String, Object>> homesquareList;
	ArrayList<HashMap<String, Object>> publishList;
	ArrayList<PublicInformation> information = new ArrayList<PublicInformation>();
	private ArrayList<HashMap<String, Object>> arr;
	ArrayList< String> al;
	ArrayList< PublicInformation> l;
	Dialog dialog;
	private boolean First = true;
	private ScrollView view;
	private int position;
	private EditText text;
	private ViewGroup parent;
	private ImageView edit;
	private Button send;
	private String data;
	private String size;
	PublicInformation o = new PublicInformation();
	private TextView title;
	private TextView publicTime;
	private TextView activityTime;
	private TextView content;
	private TextView mainTitle;
	private TextView join;
	private TextView peopleNum;
	private TextView proveNum;
	ArrayList<ImageView> imageViewList;
	private static final String[] n = {"申请加入","等待审核"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_layout);
		//bundle
		Bundle bunde = this.getIntent().getExtras();

		//取得Bundle对象中的数据
		size = bunde.getString("size");
    	final Animation alpha=AnimationUtils.loadAnimation(this, R.anim.alpha);
		list = (ListView) findViewById(R.id.list);
		edit = (ImageView) findViewById(R.id.edit);
		
		proveNum = (TextView) findViewById(R.id.prove);
		peopleNum = (TextView) findViewById(R.id.person);
		join = (TextView) findViewById(R.id.message_layout);
		join.setOnClickListener(this);
		findViewById(R.id.proveImage_layout).setOnClickListener(this);
		findViewById(R.id.person_image_layout).setOnClickListener(this);
		findViewById(R.id.message_layout).setOnClickListener(this);
		view = (ScrollView) findViewById(R.id.detail).findViewById(R.id.scroll);
		mViewpager = (ViewPager) findViewById(R.id.detail).findViewById(R.id.vp);
		
		mainTitle = (TextView) findViewById(R.id.main_title);
		title = (TextView) findViewById(R.id.detail).findViewById(R.id.title);
		publicTime = (TextView) findViewById(R.id.detail)
				.findViewById(R.id.publish_time);
		activityTime = (TextView) findViewById(R.id.detail)
				.findViewById(R.id.activity_time);
		content = (TextView) findViewById(R.id.detail)
				.findViewById(R.id.public_content);
		
		
		prepareData();
		mViewpager.setAdapter(new ImageAdapter(imageViewList));
		botton = findViewById(R.id.botton);
		top = findViewById(R.id.fragment_layout);
		top.setVisibility(View.INVISIBLE);
		edit.setOnClickListener(this);
		view.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: 
					
	                break; 
				case MotionEvent.ACTION_UP: 
					First = false;
	                break; 
	            case MotionEvent.ACTION_MOVE: 
	                if (v.getScrollY() <= 0) { 
	                	top.setVisibility(View.INVISIBLE);
	                } else if (view.getChildAt(0).getMeasuredHeight() <= 
	                		v.getHeight() + v.getScrollY()) { 
	                	botton.setVisibility(View.INVISIBLE);
	                }else{
	                	botton.setVisibility(View.VISIBLE);
	                	top.setVisibility(View.VISIBLE);
	            		InitColor();
	                }
	                break; 
	            default: 
	                break; 
	            } 
	            return false; 
			}
		});
		initView();
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
	 private void prepareData() {  
		 imageViewList = new ArrayList<ImageView>();
         ImageView iv;  
         for (int i = 0; i < 5; i++) {  
             iv = new ImageView(this);  
             iv.setBackgroundResource(R.drawable.pager);  
             imageViewList.add(iv);  
         }  
     } 
	private void dialog() {
		
		dialog = new Dialog(Detail.this, R.style.MyDialogStyle);
		
		dialog.setContentView(R.layout.edit);//����dialog����
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM);//����dialog��ʾ��λ��
		window.setWindowAnimations(R.style.MyWindowStyle);//��Ӷ���
		dialog.show();
		window.getDecorView().setPadding(0, 0, 0, 0);//����dialog���ڱ߾�Ϊ0
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;//dialog�Ŀ����丸����
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
		text = (EditText) window.findViewById(R.id.edit);
		send = (Button) window.findViewById(R.id.send);
		send.setOnClickListener(this);
	}
	private void initView() {
		initList();
		adapter = new SimpleAdapter(getApplicationContext(), arr,
				R.layout.detail_info,
				new String[] {"user_name","time","title"},
                new int[] {R.id.textView11,R.id.textView22,R.id.textView33});
		list.setAdapter(adapter);
		list.setFocusable(false);
	}
	//
	private void initList() {
		List<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("id",size+""));
		params.add(new BasicNameValuePair("from",0+""));
		params.add(new BasicNameValuePair("pagerCount",10+""));

		new DealHandler(params,InternetUrl.HeaderPath + "API/Evaluate/GetEvaluateList") {
			
			
			@Override
			public void success(String result) {
				
				
				JSONObject jsonObject;
				Toast.makeText(getApplicationContext(),"you"+result, Toast.LENGTH_SHORT).show();
				try {
					jsonObject = new JSONObject(result);
					int code = jsonObject.getInt("Code");
					//System.out.println("Code---------->" + code);
					//Toast.makeText(getApplicationContext(),"you", Toast.LENGTH_SHORT).show();
					if(code == 200){
						String data = null;
						data = jsonObject.getString("data");
						JSONObject obj1 = new JSONObject(data);
						String AppUserModel = obj1.getString("returnInfo");
						Toast.makeText(getApplicationContext(),"登录成功"+AppUserModel, Toast.LENGTH_SHORT).show();
					
					}else if(code == 201){
						String Info = jsonObject.getString("data");
						jsonObject = new JSONObject(Info);
						Toast.makeText(getApplicationContext(), jsonObject.getString("returnInfo"),Toast.LENGTH_SHORT).show();
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		arr = new ArrayList<HashMap<String,Object>>();
		for(int i = 0;i<10;i++){
			HashMap<String, Object> map = new HashMap<String, Object>(); 
			map.put("user_name", "姐姐");  
			map.put("time", "2014-12-12");
			map.put("title", "姐姐,hellow"); 
			arr.add(map);
		}
		setData();
	}
	private void setData() {
		init();
	}
	public void proveToInternet(int position){
		ArrayList<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		try {
			params.add(new BasicNameValuePair("Telephone",
					GetUserInformation.ReadTele("Token.txt")));
			params.add(new BasicNameValuePair("id",
					size+""));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		new DealHandler(params,InternetUrl.HeaderPath + "API/Activity/GivePraise"){

			@Override
			public void success(String result) {
				JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(result);
						int code = jsonObject.getInt("Code");
						if(code == 200){

							Toast.makeText(getApplicationContext(), " 成功赞", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getApplicationContext(), " yi成功赞", Toast.LENGTH_SHORT).show();
							
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
			}
			
		};
	}
	void init(){
		List<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("size",
					size));
		
		new DealHandler(params,InternetUrl.HeaderPath + "API/Activity/GetActivityInfo") {
			@Override
			public void success(String result) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					String code = jsonObject.getString("Code");
					String str = jsonObject.getString("data");
					jsonObject = new JSONObject(str);
					String str1 = jsonObject.getString("ActivityModel");
					if(code.equals("200")){
						Toast.makeText(getApplicationContext(), " 成功显示"+str1 , Toast.LENGTH_SHORT).show();
						jsonObject = new JSONObject(str1);
								 o.setActivityTime("2015");

//								 jsonObject.getString("etString("TimeEnd")
//							 Toast.makeText(getActivity(),
//									 " 成功显示"+jsonObject.getInt("Content") ,
//									 Toast.LENGTH_SHORT).show();
							 o.setMessage(1);//jsonObject.getInt("praiseNum")
							 o.setPerson(1);//jsonObject.getInt("Number")
							 o.setProve(1);//jsonObject.getInt("praiseNum")
							 o.setPublicContent(jsonObject.getString("Content"));
							 o.setPublishTime("2015");//jsonObject.getString("TimePublish")
							 o.setSex("nv");//jsonObject.getString("Sex")
							 o.setState(1);//jsonObject.getInt("AuditStatus")
							 o.setTelephone(jsonObject.getString("Telephone"));
							 o.setTitle(jsonObject.getString("Title"));
							 o.setUserName("eee");//jsonObject.getString("UserName")
							 o.setTypeName(1);//jsonObject.getInt("TypeName")
							 o.setData(1);//赞的状态
							 o.setId(jsonObject.getInt("id"));

								mainTitle.setText(o.getUserName());
								title.setText(o.getTitle());
								publicTime.setText(o.getPublishTime());
								activityTime.setText(o.getActivityTime());
								content.setText(o.getPublicContent());
						}
					else{
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	 }
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.edit:
			dialog();
			break;

		case R.id.send:
			if(!text.getText().toString().equals("")){
				HashMap<String, Object> map = new HashMap<String, Object>(); 
				map.put("user_name", "姐姐");  
				map.put("title", text.getText().toString()); 
				map.put("time", "2014-12-1");
				arr.add(map);
				adapter.notifyDataSetChanged();
				dialog.dismiss();
				toIntern();
			}
		case R.id.proveImage_layout:
			int j = Integer.valueOf(String.valueOf(
					proveNum.getText())).intValue()+1;
			proveNum.setText(j+"");
			proveToInternet(position);
			break;
		case R.id.person_image_layout:
			Intent it = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("size", ""+size);
			it.putExtras(bundle);
			it.setClass(this, MemberListActivity.class);
			startActivity(it);
			break;
		case R.id.message_layout:
			try {
				if(GetUserInformation.ReadId("Token.txt")==null){
					Intent intent = new Intent();
					intent.setClass(Detail.this, LoginActivity.class);
					startActivity(intent);
				}else{
					getState();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		}
	}
	private void getState() {
		ArrayList<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		try {
			params.add(new BasicNameValuePair("Telephone",
					GetUserInformation.ReadTele("Token.txt")));
			params.add(new BasicNameValuePair("id",
					size+""));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		new DealHandler(params,InternetUrl.HeaderPath + "API/Activity/AddActivity"){

			@Override
			public void success(String result) {
				JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(result);
						int code = jsonObject.getInt("Code");
						if(code == 200){
							Toast.makeText(getApplicationContext(), 
									" 成功jinru", Toast.LENGTH_SHORT).show();
							if(join.getText().equals("等待审核")){
								join.setText("申请加入");
							}else{
								join.setText("等待审核");
							}
						}else{

							Toast.makeText(getApplicationContext(), 
									" yi成功jinru", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
			}
			
		};
	}
	private void toIntern() {
		ArrayList<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		try {
			params.add(new BasicNameValuePair("Telephone",
					GetUserInformation.ReadTele("Token.txt")+""));
			params.add(new BasicNameValuePair("id",
					size+""));
			params.add(new BasicNameValuePair("Content",
					text.getText().toString()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		new DealHandler(params,
				InternetUrl.HeaderPath 
				+ "API/Activity/GiveEvaluate"){

			@Override
			public void success(String result) {
				JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(result);
						int code = jsonObject.getInt("Code");
						if(code == 200){
							Toast.makeText(getApplicationContext(), "ssss", 
									Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
			}
			
		};
	}
}
