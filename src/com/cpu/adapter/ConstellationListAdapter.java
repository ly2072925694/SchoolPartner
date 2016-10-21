package com.cpu.adapter;

import java.util.List;

import com.example.schoolpartner.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 星座列表
 * @author ZJC
 *
 */
public class ConstellationListAdapter extends BaseAdapter {

	/**
	 * 星座数据
	 */
	private String[] datas;
	
	/**
	 * �û���Ȥ�б�
	 */
	public static String userConstell;//用户已选星座
	private boolean isSelected = false;//是否已选星座
	public static int selIndex = -1;//如果已选星座，记住下标

	private Context context;
	
	public ConstellationListAdapter(Context context,String[] datas,String userConstell){
		this.context = context;
		this.datas = datas;
		this.userConstell = userConstell;
	}
	
	@Override
	public int getCount() {
		return datas.length;
	}

	@Override
	public Object getItem(int position) {
		return datas[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_list_interestlist, parent,false);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_interest_name);
			holder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_selected);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvName.setText(datas[position]);
		if(userConstell.equals(datas[position])){
			holder.ivSelected.setVisibility(View.VISIBLE);
			isSelected = true;
			selIndex = position;
		}else{
			holder.ivSelected.setVisibility(View.INVISIBLE);
			isSelected = false;
		}
		
		return convertView;
	}
	
	public class ViewHolder{
		private TextView tvName;//��Ȥ��
		private ImageView ivSelected;//�Ƿ�ѡ��		
	}
	
	/**
	 * ���µ���Item
	 * @param beforeposi 上次选中项
	 * @param posi
	 * @param listView
	 */
	public void updateSelected(int beforeposi ,int posi, ListView listView){
		 int visibleFirstPosi = listView.getFirstVisiblePosition();  
	        int visibleLastPosi = listView.getLastVisiblePosition();  
	        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {  

	        	//现在点击的项
	            View view2 = listView.getChildAt(posi - visibleFirstPosi);
	            ViewHolder holder2 = (ViewHolder) view2.getTag();  
	            
	            if(holder2.ivSelected.getVisibility() == View.INVISIBLE){
	            	holder2.ivSelected.setVisibility(View.VISIBLE);
	            	userConstell = datas[posi];
	            	//之前已选择的项,让之前选择项的 勾 消失
	            	if(beforeposi != -1){
			        	View view = listView.getChildAt(beforeposi);
			        	ViewHolder holder = (ViewHolder) view.getTag();
		            	holder.ivSelected.setVisibility(View.INVISIBLE);
		            	selIndex = posi;
	            	}
	            }
	           /* else{
	            	holder2.ivSelected.setVisibility(View.INVISIBLE);
	            }*/
	        }
	}

}
