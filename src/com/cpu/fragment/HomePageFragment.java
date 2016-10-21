
package com.cpu.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cpu.activity.Detail;
import com.cpu.activity.Group;
import com.cpu.activity.LoginActivity;
import com.cpu.activity.MemberListActivity;
import com.cpu.adapter.GalleryAdapter;
import com.cpu.adapter.GridImgsAdapter;
import com.cpu.adapter.PublishSimpleAdapter;
import com.cpu.adapter.SpinnerBaseAdapter;
import com.cpu.constant.GetUserInformation;
import com.cpu.constant.InternetUrl;
import com.cpu.entity.PublicInformation;
import com.cpu.tools.DealHandler;
import com.cpu.view.AddFooterView;
import com.cpu.view.Refresh;
import com.example.schoolpartner.R;


import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HomePageFragment extends Fragment implements OnClickListener {
    private GridView mGridView;  
    private GridImgsAdapter mGridViewAdapter; 
	private GridView gridView;
	private ImageView badge;
	private SimpleAdapter simpleadapter;
	private PublishSimpleAdapter simplePublish;

	private SwipeRefreshLayout HomeInfoLayout;
	private ListView list;
	ArrayList<HashMap<String, Object>> homesquareList;
	ArrayList<HashMap<String, Object>> publishList  = new ArrayList<HashMap<String, Object>>();;
	ArrayList<PublicInformation> information = new ArrayList<PublicInformation>();
	ArrayList< PublicInformation> l = new ArrayList<PublicInformation>();
	private View ll;
	private ImageView spinner;
	private ArrayAdapter<String> adapter;
	private AddFooterView footer;
	private Boolean Prove;
	private String data ;
	private int pos = 0;
	private int position =0;
	private int posState;
	private View mPopupWindowView;
	private PopupWindow popupWindow ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ll = inflater.inflate(R.layout.square_info, null);
		gridView = (GridView) ll.findViewById(R.id.square_info_grid);
		list = (ListView) ll.findViewById(R.id.square_info_list);
		spinner = (ImageView) ll.findViewById(R.id.spinner);
		badge = (ImageView) ll.findViewById(R.id.massage_tip);
		spinner.setOnClickListener(this);
		homesquareList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>(); 
		map.put("ItemTitle", "学习");
		homesquareList.add(map);
		HashMap<String, Object> ma = new HashMap<String, Object>(); 
		ma.put("ItemTitle", "旅游");
		homesquareList.add(ma);
		HashMap<String, Object> m = new HashMap<String, Object>(); 
		m.put("ItemTitle", "美食");
		homesquareList.add(m);
		HashMap<String, Object> map1 = new HashMap<String, Object>(); 
		map1.put("ItemTitle", "运动");
		homesquareList.add(map1);
		HashMap<String, Object> ma1 = new HashMap<String, Object>(); 
		ma1.put("ItemTitle", "其他");
		homesquareList.add(ma1);
		HashMap<String, Object> m1 = new HashMap<String, Object>(); 
		m1.put("ItemTitle", "看电影");
		homesquareList.add(m1);
		simpleadapter = new SimpleAdapter(getActivity(), homesquareList,
				R.layout.square_info_info,new String[] {"ItemTitle"},
                new int[] {R.id.square_info_info_tvCity});
		init();
		footer = new AddFooterView(getActivity()) {
			
			@Override
			public void LoadData(final ProgressBar bar, final TextView loading) {
				
				loading.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(loading.getText().toString().equals("正在加载")){
							bar.setVisibility(INVISIBLE);
							loading.setText("加载更多");
						}else{
							loading.setText("正在加载");
							bar.setVisibility(VISIBLE);
						}
						toInternet(l.size(),GetUserInformation.type[pos],-1);
						list.setAdapter(simplePublish);
					}
				});
			}
		};
		list.addFooterView(footer.init(getActivity()));
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					Intent it = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("size", ""+l.get(arg2).getId());
					it.putExtras(bundle);
					it.setClass(getActivity(), Detail.class);
					startActivity(it);
			}
		});
		HomeInfoLayout = (SwipeRefreshLayout) 
				ll.findViewById(R.id.square_info_linear);
		HomeInfoLayout.setColorSchemeResources(R.color.yellow,
                R.color.blue,
                R.color.pink,
                R.color.purple);
		HomeInfoLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                toInternet(0, GetUserInformation.type[pos],-1);
                mHandler.sendEmptyMessage(1);
            }
        });
		list.setAdapter(simplePublish);
        setGridView();
        initPopupWindow();
        return ll;
	}
	private void initPopupWindow(){
		initPopupWindowView();
		//初始化popupwindow，绑定显示view，设置该view的宽度/高度
		popupWindow = new PopupWindow(
				mPopupWindowView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景；使用该方法点击窗体之外，才可关闭窗体
		popupWindow.setBackgroundDrawable(
				getResources().getDrawable(R.drawable.back));
		//Background不能设置为null，dismiss会失效
//		      popupWindow.setBackgroundDrawable(null);
		//设置渐入、渐出动画效果
//		      popupWindow.setAnimationStyle(R.style.popupwindow);
		popupWindow.update();

	}
	private void showPopupWindow(){
		if(!popupWindow.isShowing()){
		popupWindow.showAsDropDown(spinner, spinner.getLayoutParams().width/2, 0);
		}else{
		popupWindow.dismiss();
		}
	}
	private void initPopupWindowView(){
		mPopupWindowView = LayoutInflater.from(getActivity())
				.inflate(R.layout.popupwindow, null);
		TextView textview_edit = (TextView) mPopupWindowView
				.findViewById(R.id.textview_edit);
		textview_edit.setOnClickListener(this);
		TextView textview_file = (TextView) mPopupWindowView
				.findViewById(R.id.textview_file);
		textview_file.setOnClickListener(this);
		TextView textview_about = (TextView) mPopupWindowView
				.findViewById(R.id.textview_about);
		textview_about.setOnClickListener(this);
		TextView textview_man = (TextView) mPopupWindowView
				.findViewById(R.id.textview_man);
		textview_man.setOnClickListener(this);
		TextView textview_woman = (TextView) mPopupWindowView
				.findViewById(R.id.textview_woman);
		textview_woman.setOnClickListener(this);

		}	 
	private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case 1:
            	HomeInfoLayout.setRefreshing(false);
                list.setAdapter(simplePublish);
                //swipeRefreshLayout.setEnabled(false);
                break;
            default:
                break;
            }
        }
    };
	private void setGridView() {
		
	 	int size = homesquareList.size();
        int length = 80;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length - 15) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gridView.setLayoutParams(params); // GirdView
        gridView.setColumnWidth(itemWidth); // 宽度
        gridView.setHorizontalSpacing(1); // 水平距离
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); //设置大小
        gridView.setAdapter(simpleadapter);
        gridView.setHorizontalScrollBarEnabled(false);
        gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
					position = arg2;
					publishList.clear();
					FromInternet(GetUserInformation.m[pos]);
			}
		});
	}
	public void toInternet(int st,String str,int pos){
			if(st == 0){
				st = st+1;
			}
		 	ArrayList<BasicNameValuePair> params;
			params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("TypeName",
						""+str));
			if(pos == 1)
				params.add(new BasicNameValuePair("TimePublish",
						"")); 
			else
				params.add(new BasicNameValuePair("TimePublish",
					"")); 
			if(pos == 2)
				params.add(new BasicNameValuePair("praiseNum",
					"")); 
			else
				params.add(new BasicNameValuePair("praiseNum",
						"")); 
			if(pos == 3)
				params.add(new BasicNameValuePair("Sex",
						"女")); 
			else
				params.add(new BasicNameValuePair("Sex",
						"")); 
			if(pos == 4)
				params.add(new BasicNameValuePair("Sex",
						"男"));
			else
				params.add(new BasicNameValuePair("Sex",
						"")); 
				
			params.add(new BasicNameValuePair("from",
					st+"")); 
			params.add(new BasicNameValuePair("pagerCount",
					10+"")); 
			
			new DealHandler(params,InternetUrl.HeaderPath 
					+ "API/Activity/GetActivityList") {
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
//						Toast.makeText(getActivity(), " 成功显示"+json.length() , Toast.LENGTH_SHORT).show();
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
	}
	private void FromInternet(String str) {
		
		 ArrayList<BasicNameValuePair> params;
			params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("TypeName",
						""+str));
			params.add(new BasicNameValuePair("TimePublish",
					"")); 
			params.add(new BasicNameValuePair("praiseNum",
					"")); 
			params.add(new BasicNameValuePair("Sex",
					"")); 
			params.add(new BasicNameValuePair("from",
					1+"")); 
			params.add(new BasicNameValuePair("pagerCount",
					10+"")); 
			
			new DealHandler(params,InternetUrl.HeaderPath 
					+ "API/Activity/GetActivityList") {
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
//							Toast.makeText(getActivity(), " 成功显示"+json.length() , Toast.LENGTH_SHORT).show();
							for(int i=0;i<json.length();i++){
								jsonObject = (JSONObject) json.get(i);
								 PublicInformation o = new PublicInformation();
									 o.setActivityTime("2015");

//									 jsonObject.getString("etString("TimeEnd")
//								 Toast.makeText(getActivity(),
//										 " 成功显示"+jsonObject.getInt("Content") ,
//										 Toast.LENGTH_SHORT).show();
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
						list.setAdapter(simplePublish);
					}else{
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	}
	private void setData() {
		for(int i = 0;i<l.size();i++){
			HashMap<String, Object> map = new HashMap<String, Object>(); 
			map.put("user_name", l.get(i).getUserName());  
			map.put("title", l.get(i).getTitle()); 
			map.put("publish_time", "20151515"); //l.get(i).getPublishTime()
			map.put("public_content", l.get(i).getPublicContent()); 
			map.put("prove", i); //l.get(i).getProve()+
			map.put("message", i); //l.get(i).getMessage()
			map.put("activity_time", "20150202"); //l.get(i).getActivityTime()
			map.put("state", GetUserInformation.n[1]);//l.get(i).getTypeName()
			map.put("person", i);//l.get(i).getPerson()
			map.put("data", l.get(i).getData());
			map.put("id", l.get(i).getId());
			publishList.add(map);
		}
		
		simplePublish = new PublishSimpleAdapter(getActivity(), publishList,
				R.layout.publish_info,
				new String[] {"user_name",
			"title","publish_time","public_content",
			"prove","activity_time","state","person"},
                new int[] {R.id.user_name,R.id.title,
			R.id.publish_time,R.id.public_content,
			R.id.prove,R.id.activity_time,R.id.state,R.id.person}) {
			
			@Override
			public void convert(com.cpu.adapter.PublishSimpleAdapter.ViewHolder helper,
					final int position) {
				posState = position;
				helper.getProve().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						try {
							if(GetUserInformation.ReadId("Token.txt")==null){
								Intent it = new Intent();
								it.setClass(getActivity(), LoginActivity.class);
								startActivity(it);
							}else{
								if(Integer.valueOf(String.valueOf(
										publishList.get(position).get("data")))
										.intValue() == 1){
									int j = Integer.valueOf(String.valueOf(
											publishList.get(position)
											.get("prove"))).intValue()+1;
									
									publishList.get(position).put("prove",j);
									simplePublish.notifyDataSetChanged();
									simplePublish.notifyDataSetInvalidated();
									j = 0;
									notifyDataSetChanged();
									publishList.get(position).put("data",0);
								}else{
									int j = Integer.valueOf(String.valueOf(
											publishList.get(position).get("prove")))
											.intValue()-1;
									
									publishList.get(position).put("prove",j);
									simplePublish.notifyDataSetChanged();
									simplePublish.notifyDataSetInvalidated();
									j = 0;
									notifyDataSetChanged();
									publishList.get(position).put("data",1);
								}
								proveToInternet(position);
							}
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				helper.getMessage().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						try {
							if(GetUserInformation.ReadId("Token.txt")==null){
								Intent it = new Intent();
								it.setClass(getActivity(), LoginActivity.class);
								startActivity(it);
							}else{
								Intent it = new Intent();

								Bundle bundle = new Bundle();
								bundle.putString("size", ""+l.get(position).getId());
								it.putExtras(bundle);
								it.setClass(getActivity(), Detail.class);
								startActivity(it);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				helper.getPerson().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
							try {
								if(GetUserInformation.ReadId("Token.txt")==null){
									Intent it = new Intent();
									it.setClass(getActivity(), LoginActivity.class);
									startActivity(it);
								}else{
									Intent it = new Intent();
									Bundle bundle = new Bundle();
									bundle.putString("size", ""+l.get(position).getId());
									it.putExtras(bundle);
									it.setClass(getActivity(), MemberListActivity.class);
									startActivity(it);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
				});
				helper.getState().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						try {
							
							if(GetUserInformation.ReadId("Token.txt")==null){
								Intent it = new Intent();
								it.setClass(getActivity(), LoginActivity.class);
								startActivity(it);
							}else{
								if(publishList.get(position).get("state").equals("等待审核")){
									publishList.get(position).put("state","申请加入");
								}else{
									publishList.get(position).put("state","等待审核");
								}
								getState();
								simplePublish.notifyDataSetChanged();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
		};
		simpleadapter.notifyDataSetChanged();
		list.setAdapter(simplePublish);
	}
	private void getState() {
		ArrayList<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		try {
			params.add(new BasicNameValuePair("Telephone",
					GetUserInformation.ReadTele("Token.txt")));
			params.add(new BasicNameValuePair("id",
					publishList.get(posState).get("id")+""));
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

							Toast.makeText(getActivity(), " 成功jinru", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
			}
			
		};
	}
	public void proveToInternet(int position){
		
		ArrayList<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		try {
			params.add(new BasicNameValuePair("Telephone",
					GetUserInformation.ReadTele("Token.txt")));
			params.add(new BasicNameValuePair("id",
					publishList.get(position).get("id")+""));
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

							Toast.makeText(getActivity(), " 成功赞", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getActivity(), " yi成功赞", Toast.LENGTH_SHORT).show();
							
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
			}
			
		};
	}
	 void init(){
		 ArrayList<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("TypeName",
					""));
		params.add(new BasicNameValuePair("TimePublish",
				"")); 
		params.add(new BasicNameValuePair("praiseNum",
				"")); 
		params.add(new BasicNameValuePair("Sex",
				"")); 
		params.add(new BasicNameValuePair("from",
				0+"")); 
		params.add(new BasicNameValuePair("pagerCount",
				10+"")); 
		
		new DealHandler(params,InternetUrl.HeaderPath 
				+ "API/Activity/GetActivityList") {
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
//						Toast.makeText(getActivity(), " 成功显示"+json.length() , Toast.LENGTH_SHORT).show();
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
						Toast.makeText(getActivity(), "错误提示", Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};

	 }

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
	
		case R.id.spinner:
			showPopupWindow();
			break;
		
		case R.id.textview_about:
			publishList.clear();
			toInternet(l.size(),GetUserInformation.m[pos],position);
			list.setAdapter(simplePublish);
			popupWindow.dismiss();
			break;
		
		case R.id.textview_edit:
			publishList.clear();
			toInternet(l.size(),GetUserInformation.type[pos],position);
			list.setAdapter(simplePublish);
			popupWindow.dismiss();
			break;
		
		case R.id.textview_file:
			publishList.clear();
			toInternet(l.size(),GetUserInformation.type[pos],position);
			list.setAdapter(simplePublish);
			popupWindow.dismiss();
			break;
		case R.id.textview_man:
			publishList.clear();
			toInternet(l.size(),GetUserInformation.type[pos],pos);
			list.setAdapter(simplePublish);
			popupWindow.dismiss();
			break;
		case R.id.textview_woman:
			publishList.clear();
			toInternet(l.size(),GetUserInformation.type[pos],pos);
			list.setAdapter(simplePublish);
			popupWindow.dismiss();
			break;
		}
	}
}

