package com.cpu.tools;

import java.util.List;
import java.util.Map;
import org.apache.http.message.BasicNameValuePair;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public abstract class DealHandler {
	Map<String ,String> data;
	public InnerHandler handler;
	public class InnerHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String result = b.getString("result");
			 success(result);
		}
	}

	public DealHandler(final List<BasicNameValuePair> postData,final String url){
		handler = new InnerHandler();
		new Thread(){
			
			@Override
			public void run(){
				String result = 
						HttpUtils.sendPostResquest(url, postData, "UTF-8");
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putString("result", result);
				msg.setData(b);
				Log.i("数据", " "+result);
				handler.sendMessage(msg);
			}
		}.start();
	}
	public abstract void success(String result);
}

