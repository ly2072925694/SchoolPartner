package com.cpu.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.cpu.adapter.ExamineSimpleList;
import com.example.schoolpartner.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Examine extends Activity {
	private ListView list;
	private SimpleAdapter adapter;
	private ArrayList<HashMap<String, Object>> arr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.examine_list);
		init();
		list = (ListView) findViewById(R.id.listView1);
		adapter = new ExamineSimpleList(getApplicationContext(), arr,
				R.layout.examine,
				new String[] {"user_name","title"},
                new int[] {R.id.textView1,R.id.textView2}) {
			
			@Override
			public void convert(ViewHolder helper, final int position) {
				helper.getAgree().setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(arr.get(position).get("agree").equals("0")){
							arr.get(position).put("agree", "1");
							adapter.notifyDataSetChanged();
						}
					}
				});
				helper.getDisagree().setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(arr.get(position).get("dis").equals("0")){
							arr.get(position).put("dis", "1");
							adapter.notifyDataSetChanged();
						}
					}
				});
			}
		};
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
		arr = new ArrayList<HashMap<String,Object>>();
		for(int i = 0;i<10;i++){
			HashMap<String, Object> map = new HashMap<String, Object>(); 
			map.put("user_name", "姐姐");  
			map.put("title", "姐姐"); 
			map.put("dis", "0");
			map.put("agree", "0");
			arr.add(map);
		}
	}
}
