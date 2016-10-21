package com.cpu.activity;

import java.util.ArrayList;
import java.util.List;

import com.cpu.adapter.InterestListAdapter;
import com.cpu.adapter.NoticeListAdapter;
import com.cpu.entity.Notice;
import com.example.schoolpartner.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class NoticeActivity extends Activity implements OnClickListener {

	private ListView lvNotice;
	private NoticeListAdapter adapter;
	private List<Notice> mNotices;//֪ͨ����
	private TextView tvDelAll;
	private TextView tvNoNotice;//����ͨ
	private ImageView ivBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);
		initData();
		init();
	}
	
	/**
	 * ���֪ͨ����
	 */
	public void initData(){
		mNotices = new ArrayList<Notice>();
		Notice notice;
//		for(int i=0;i<10;i++){
//			notice = new Notice("系统通知", "\t\t" + i + "通知内容通知内容通知内容通知内容通知内容通知内容通知内容通知内容通知内容");
//			mNotices.add(notice);
//		}
		
		
		
		notice = new Notice("系统通知", "\t\t" + "你已经成功发布[约运动]活动");
		mNotices.add(notice);
		notice = new Notice("系统通知", "\t\t" +  "你申请的[图书馆上自习]活动已经通过");
		mNotices.add(notice);
		notice = new Notice("系统通知", "\t\t" +  "你已经成功发布[约看电影]活动已经过期");
		mNotices.add(notice);
		notice = new Notice("系统通知", "\t\t" +  "用户'逍遥'申请了你发布的[约吃饭]活动,点击转到成员审批页面进行审核");
		mNotices.add(notice);
	}
	
	public void init(){
		tvDelAll = (TextView) findViewById(R.id.tv_delete_all);
		tvNoNotice = (TextView) findViewById(R.id.tv_no_notice);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvDelAll.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		
		lvNotice = (ListView) findViewById(R.id.lv_notice);
		adapter = new NoticeListAdapter(this,lvNotice, mNotices,tvNoNotice);
		lvNotice.setAdapter(adapter);
		
	}
	
	//ɾ����Ϣ�������ʾ
	public void update(){
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_delete_all:
			adapter.datas.clear();//���֪ͨ
			adapter.notifyDataSetChanged();
			tvNoNotice.setVisibility(View.VISIBLE);//��ʾ����֪ͨ
			break;
			
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
	}
}
