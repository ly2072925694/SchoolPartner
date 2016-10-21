package com.cpu.tools;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpUtils {
	 public static String sendPostResquest(
			 String path,List<BasicNameValuePair> data, String encoding) {
		 //json数据完全解析为字符串data
//           List<NameValuePair> list = new
//        		   ArrayList<NameValuePair>();      //封装请求体参数
//           list.add(new BasicNameValuePair(
//                		   "data", data));
          try {
              UrlEncodedFormEntity entity = 
            		  new UrlEncodedFormEntity(
            				  data, encoding);//对请求体参数进行URL编码
              
              HttpPost httpPost = 
            		  new HttpPost(path);  //创建一个POST方式的HttpRequest对象
              httpPost.setEntity(entity);  //设置POST方式的请求体
              DefaultHttpClient client = new DefaultHttpClient();
              HttpResponse httpResponse = 
            		  client.execute(httpPost);       //执行POST请求
              int reponseCode = httpResponse
            		  .getStatusLine().getStatusCode(); //获得服务器的响应码
              if(reponseCode == HttpStatus.SC_OK) {
            	  Log.i("wangluoqingqiuchenggong", "网络请求成功");
                  String resultData = EntityUtils
                		  .toString(httpResponse.getEntity());//获得服务器的响应内容
//            	  Log.i("数据", " "+resultData);
                  return resultData;
              }
              return "123";
          } catch (IOException e) {
              e.printStackTrace();
              return "22";
         }
//         return "11";
     }
}
