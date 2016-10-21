package com.cpu.activity;

import java.util.List;

import com.cpu.adapter.ConstellationListAdapter;
import com.example.schoolpartner.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 兴趣列表
 * @author ZJC
 *
 */
public class ConstellationListActivity extends Activity implements OnClickListener {

	private ListView lvConstell;
	private ConstellationListAdapter adapter;
	private String[] datas;
	private String userConstell = "";//用户已设置的星座
	private ImageView ivBack;
	
	public final static int REQUEST_CODE_Constellation = 0x12;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_constellationlist);
		Intent intent = this.getIntent();
		userConstell = intent.getStringExtra("userConstell");
		init();
	}
	
	public void init(){
		datas = this.getResources().getStringArray(R.array.constellation);//得到星座列表
		Log.i("ConstellationListActivity", datas[2]);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		lvConstell = (ListView) findViewById(R.id.lv_constellation);
		ivBack.setOnClickListener(this);
		
		adapter = new ConstellationListAdapter(this, datas, userConstell);
		lvConstell.setAdapter(adapter);
		lvConstell.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int before = adapter.selIndex;//上一次选中项
				Log.i("Constell", "上次" + before);
				adapter.updateSelected(before,position, lvConstell);
				transportData();
				close();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;

		default:
			break;
		}
	}
	
	public void transportData(){
		Intent intent = new Intent(this, MyInfoActivity.class);
		intent.putExtra("Constellation", ConstellationListAdapter.userConstell);
		setResult(ConstellationListActivity.REQUEST_CODE_Constellation, intent);
	}
	
	public void close(){
		this.finish();
	}
}
