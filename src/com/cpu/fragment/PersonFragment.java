package com.cpu.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.evernote.o;

import com.cpu.activity.Detail;
import com.cpu.activity.Examine;
import com.cpu.activity.Group;
import com.cpu.activity.LoginActivity;
import com.cpu.activity.MemberListActivity;
import com.cpu.activity.MyInfoActivity;
import com.cpu.adapter.PublishSimpleAdapter;
import com.cpu.constant.GetUserInformation;
import com.cpu.constant.InternetUrl;
import com.cpu.entity.PublicInformation;
import com.cpu.tools.DealHandler;
import com.cpu.tools.ImageLoader;
import com.cpu.tools.OperateFile;
import com.cpu.view.AddFooterView;
import com.cpu.view.Refresh;
import com.example.schoolpartner.R;
import com.jauker.widget.BadgeView;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PersonFragment extends Fragment implements OnClickListener{
	private View person;
	private ImageView userPhoto;
	private TextView userName;
	private TextView publishText;
	private TextView joinText;
	private ListView personInfo;
	private AddFooterView footer;
	private String data ;
	ArrayList< PublicInformation> l= new ArrayList<PublicInformation>();
	private SwipeRefreshLayout personInfoLayout;
	private PublishSimpleAdapter simplePublish;
	ArrayList<HashMap<String, Object>> userPublishlist = new ArrayList<HashMap<String, Object>>();
	ArrayList< PublicInformation> ll;
	private ImageView tip;
	private String path1 = "API/Activity/GetActivityListPerson";
	private  String path2 = "API/Activity/IEnjoinActivity";
	private String note = "我发布的";
	private static final String[] n = {"申请加入","等待审批"};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		person = inflater.inflate(R.layout.person_info, null);
		tip = (ImageView) person.findViewById(R.id.massage_tip);
		
		FromInternet(0);
		init();
//		setData();
		broadcast();
		personInfo.setAdapter(simplePublish);
		personInfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					Intent it = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("size", ""+arg2);
					it.putExtras(bundle);
					it.setClass(getActivity(), Detail.class);
					startActivity(it);
			}
		});
		publishText.setBackgroundColor(Color.parseColor("#0099FF"));
		joinText.setBackgroundColor(Color.parseColor("#FFFFFF"));
		
		userPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try {
					if(GetUserInformation.ReadId("Token.txt")==null){
						Intent it = new Intent();
						it.setClass(getActivity(), LoginActivity.class);
						startActivity(it);
					}else{
						Log.i("PersonFragment", "读取的Token:" + GetUserInformation.ReadId("Token.txt"));
						Intent it = new Intent();
						it.setClass(getActivity(), MyInfoActivity.class);
						startActivity(it);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	    return person;
	}
	private void broadcast() {
		LocalBroadcastManager broadcastManager = LocalBroadcastManager.
				getInstance(getActivity());
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("Me");//�������дһ�������ı��������﷽���Ķ��Ͳ�д�ˡ�
		BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
		       
					@Override
					public void onReceive(Context arg0, Intent arg1) {
						userPublishlist.clear();
						simplePublish.notifyDataSetChanged();
					}
		 };
		 broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);

	}
	private void FromInternet(int size) {
		if(size == 0){
			size = size+1;
		}
		ArrayList<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		try {
			params.add(new BasicNameValuePair("Telephone",
						""+GetUserInformation.ReadTele("Token.txt")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		params.add(new BasicNameValuePair("from",
				""+size));
		params.add(new BasicNameValuePair("pagerCount",
				10+"")); 
		
		if(note.equals("我发布的"))
		{
		new DealHandler(params,InternetUrl.HeaderPath + path1) {
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
						JSONArray json = new JSONArray(str1);
						for(int i=0;i<json.length();i++){
							jsonObject = (JSONObject) json.get(i);
							 PublicInformation o = new PublicInformation();
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
							 l.add(o);
						}
							setData();
					}else{
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		}else{
				new DealHandler(params,InternetUrl.HeaderPath + path2) {
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
								JSONArray json = new JSONArray(str1);
								Toast.makeText(getActivity(), " 成功显示"+json.length() , Toast.LENGTH_SHORT).show();
								for(int i=0;i<json.length();i++){
									jsonObject = (JSONObject) json.get(i);
									 PublicInformation o = new PublicInformation();
										 o.setActivityTime("2015");

//										 jsonObject.getString("etString("TimeEnd")
//									 Toast.makeText(getActivity(),
//											 " 成功显示"+jsonObject.getInt("Content") ,
//											 Toast.LENGTH_SHORT).show();
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
									 l.add(o);
								}
									setData();
							}else{
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				};
				
		}
	}
	private void setData() {
		for(int i = 0;i<l.size();i++){
			HashMap<String, Object> map = new HashMap<String, Object>(); 
			map.put("user_name", l.get(i).getUserName());  
			map.put("title", l.get(i).getTitle()); 
			map.put("publish_time", l.get(i).getPublishTime()); 
			map.put("public_content", l.get(i).getPublicContent()); 
			map.put("prove", l.get(i).getProve()); 
			map.put("message", l.get(i).getMessage()); 
			map.put("activity_time", l.get(i).getActivityTime()); 
			map.put("state", n[l.get(i).getState()]);
			map.put("data", l.get(i).getData());
			map.put("person", l.get(i).getPerson());
			map.put("id", l.get(i).getId());
			userPublishlist.add(map);
		}
		simplePublish = new PublishSimpleAdapter(getActivity(), userPublishlist,
				R.layout.publish_info,
				new String[] {"user_name",
			"title","publish_time","public_content",
			"prove","message","activity_time","state","person"},
                new int[] {R.id.user_name,R.id.title,
			R.id.publish_time,R.id.public_content,
			R.id.prove1,R.id.message1,R.id.activity_time,R.id.state,R.id.person1}) {
		
		@Override
		public void convert(com.cpu.adapter.PublishSimpleAdapter.ViewHolder helper,
				final int position) {
			helper.getState().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent it = new Intent();
						it.setClass(getActivity(), Examine.class);
						startActivity(it);
					}
				});
			helper.getProveImage().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(Integer.valueOf(String.valueOf(
							userPublishlist.get(position)
							.get("data"))).intValue() == 1){
						int j = Integer.valueOf(String.valueOf(
								userPublishlist.get(position).get("prove")))
								.intValue()+1;
						
						userPublishlist.get(position).put("prove",j);
						simplePublish.notifyDataSetChanged();
						simplePublish.notifyDataSetInvalidated();
						j = 0;
						notifyDataSetChanged();
						userPublishlist.get(position).put("data",0);
					}else{
						int j = Integer.valueOf(String.valueOf(
								userPublishlist.get(position).get("prove")))
								.intValue()-1;
						
						userPublishlist.get(position).put("prove",j);
						simplePublish.notifyDataSetChanged();
						simplePublish.notifyDataSetInvalidated();
						j = 0;
						notifyDataSetChanged();
						userPublishlist.get(position).put("data",1);
					}
					proveToInternet( position);
				}
			});
			helper.getMessageImage().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent it = new Intent();
					it.setClass(getActivity(), Detail.class);
					startActivity(it);
				}
			});
			helper.getPersonImage().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent it = new Intent();
					it.setClass(getActivity(), MemberListActivity.class);
					startActivity(it);
				}
			});
		}
	};
	simplePublish.notifyDataSetChanged();
	personInfo.setAdapter(simplePublish);
	}
	public void init() {
		userPhoto = (ImageView) person.findViewById(R.id.user_photo);
		userName = (TextView) person.findViewById(R.id.user_name);
		publishText = (TextView) person.findViewById(R.id.publish);
		joinText = (TextView) person.findViewById(R.id.join);
		personInfo = (ListView) person.findViewById(R.id.person_info_list);
		personInfoLayout = (SwipeRefreshLayout) 
				person.findViewById(R.id.person_info_linear);
//		new ImageLoader().showImageByThread(userPhoto, "www.baidu.com");

		personInfoLayout.setColorSchemeResources(R.color.yellow,
                R.color.blue,
                R.color.pink,
                R.color.purple);
		personInfoLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                FromInternet(l.size());
                mHandler.sendEmptyMessage(1);
            }
        });
		footer = new AddFooterView(getActivity()) {
			
			@Override
			public void LoadData(ProgressBar bar, TextView loading) {
				if(loading.getText().toString().equals("正在加载")){
					bar.setVisibility(INVISIBLE);
					loading.setText("加载更多");
				}else{
					loading.setText("正在加载");
					bar.setVisibility(VISIBLE);
				}
				FromInternet(l.size());
				personInfo.setAdapter(simplePublish);
			}
		};
		personInfo.addFooterView(footer.init(getActivity()));
		personInfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
		});
		userName.setText("哟哟");
//		OperateFile.readTxtFile("Token").get(2)
		userPhoto.setOnClickListener(this);
		publishText.setOnClickListener(this);
		joinText.setOnClickListener(this);
		userPhoto.setOnClickListener(this);
		
	}
	 private Handler mHandler = new Handler(){
	        @Override
	        public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	            switch (msg.what) {
	            case 1:
	            	personInfoLayout.setRefreshing(false);
					personInfo.setAdapter(simplePublish);
	                break;
	            default:
	                break;
	            }
	        }
	    };
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.join:
			note = "我参与的";
			publishText.setBackgroundColor(Color.parseColor("#FFFFFF"));
			joinText.setBackgroundColor(Color.parseColor("#0099FF"));
			FromInternet(0);
			personInfo.setAdapter(simplePublish);
			break;
		
		case R.id.publish:
			note = "我发布的";
			publishText.setBackgroundColor(Color.parseColor("#0099FF"));
			joinText.setBackgroundColor(Color.parseColor("#FFFFFF"));
			FromInternet(0);
			personInfo.setAdapter(simplePublish);
			break;
		}
	}
public void proveToInternet(int position){
		
		ArrayList<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		try {
			params.add(new BasicNameValuePair("Telephone",
					GetUserInformation.ReadId("Token.txt")));
			params.add(new BasicNameValuePair("id",

					userPublishlist.get(position).get("id")+""));
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
							
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
			}
			
		};
	}
}
