package com.cpu.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cpu.constant.InternetUrl;
import com.cpu.tools.DealHandler;
import com.cpu.view.CircleImageDrawable;
import com.example.schoolpartner.R;

/**
 * 
 * @author Tommy
 *该Activity用来呈现成员列表。
 *注意：MemberList的Adapter声明在了本类中,以及所有的与之相关的网络获取，等等都写在该Java文件中
 */

public class MemberListActivity extends Activity implements OnClickListener{

	private ImageView titlebar_iv_left = null;
	private ImageView titlebar_iv_right = null;
	private TextView titlebar_tv_right = null;
	
	
	private ListView listview_member_list = null;//用来呈现成员列表的listView
	private MemberListAdapter mMemberListAdapter = null;//适配器
	private ArrayList<ItemMemberList> mList = new ArrayList<ItemMemberList>();//数据集合
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_list);
		
		initView() ;
		initDatas();
		
	}
	private void initView(){
		/*
		 * titleBar中的一些控件的初始化
		 */
		titlebar_iv_left = (ImageView) findViewById(R.id.titlebar_iv_left);
		titlebar_tv_right = (TextView) findViewById(R.id.titlebar_tv_right);
		titlebar_iv_right = (ImageView) findViewById(R.id.titlebar_iv_right);
		titlebar_iv_right.setVisibility(View.INVISIBLE);
		titlebar_tv_right.setText("成员列表");
		titlebar_iv_left.setImageResource(R.drawable.userinfo_navigationbar_back_sel);
		titlebar_iv_left.setOnClickListener(this);
		
		
		listview_member_list = (ListView) findViewById(R.id.listview_member_list);
		mMemberListAdapter = new MemberListAdapter(this, mList);
		listview_member_list.setAdapter(mMemberListAdapter);
		
		
	}
	
	private void initDatas() {
		for(int i=0;i<10;i++) {
			ItemMemberList item = new ItemMemberList();
			mList.add(item);
		}
		getDateFromNet();
	}
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.titlebar_iv_left:
			this.finish();
			break;
		
		default:
			break;
		}
		
	}
	
	private void getDateFromNet() {
		Bundle bd = this.getIntent().getExtras();
		String id = bd.getString("size");
		List<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("id",id));
		
		new DealHandler(params,InternetUrl.HeaderPath + "/API/Activity/DeleteActivityInfo") {

			@Override
			public void success(String result) {
				
				Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
				System.out.println(result);
				
			}
			
		};

		
		
	}
	
}



class MemberListAdapter extends BaseAdapter {

	private Context mContext = null;
	private ArrayList<ItemMemberList> mList = null;
	
	
	public MemberListAdapter(Context mContext,ArrayList<ItemMemberList> mList) {
		this.mContext = mContext;
		this.mList = mList;
		
		
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_member_list, null);
			holder.item_member_list_img_head = (ImageView) convertView.findViewById(R.id.item_member_list_img_head);
			holder.item_member_list_tv_name = (TextView) convertView.findViewById(R.id.item_member_list_tv_name);
			holder.item_member_list_img_sex = (ImageView) convertView.findViewById(R.id.item_member_list_img_sex);
			holder.item_member_list_tv_basic_info = (TextView) convertView.findViewById(R.id.item_member_list_tv_basic_info);
			convertView.setTag(holder);
			
			
			
		}else {
			holder = (ViewHolder) convertView.getTag();
			
		}
		
		//Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.icon_head_pic);
		
		
		holder.item_member_list_img_head.setImageResource(R.drawable.icon_head_pic);
		holder.item_member_list_tv_name.setText("小雪");
		holder.item_member_list_img_sex.setBackgroundResource(R.drawable.ic_launcher);
		holder.item_member_list_tv_basic_info.setText("21岁 女  重庆，九龙坡");
		
		return convertView;
	}
	
	class ViewHolder{
		public ImageView item_member_list_img_head = null;
		public TextView item_member_list_tv_name = null;
		public ImageView item_member_list_img_sex = null;
		public TextView item_member_list_tv_basic_info = null;
	}
	
}

class ItemMemberList{
	private String headPicUrl = "";
	private String name = "";
	private String basicInfo = "";
	private String sex = "";
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getHeadPicUrl() {
		return headPicUrl;
	}
	public void setHeadPicUrl(String headPicUrl) {
		this.headPicUrl = headPicUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBasicInfo() {
		return basicInfo;
	}
	public void setBasicInfo(String basicInfo) {
		this.basicInfo = basicInfo;
	}
	
}



