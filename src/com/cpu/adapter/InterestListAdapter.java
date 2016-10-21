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
 * 兴趣列表适配器
 * @author ZJC
 *
 */
public class InterestListAdapter extends BaseAdapter {

	/**
	 * 兴趣列表集合
	 */
	private List<String> datas;
	
	/**
	 * 用户兴趣列表
	 */
	public static List<String> userInterest;
	private Context context;
	
	public InterestListAdapter(Context context,List<String> datas,List<String> userInterest){
		this.context = context;
		this.datas = datas;
		this.userInterest = userInterest;
	}
	
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
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
		
		holder.tvName.setText(datas.get(position));
		if(userInterest.contains(datas.get(position))){
			holder.ivSelected.setVisibility(View.VISIBLE);
		}else{
			holder.ivSelected.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}
	
	public class ViewHolder{
		private TextView tvName;//兴趣名
		private ImageView ivSelected;//是否选择		
	}
	
	/**
	 * 更新单个Item
	 * @param posi
	 * @param listView
	 */
	public void updateSelected(int posi, ListView listView){
		 int visibleFirstPosi = listView.getFirstVisiblePosition();  
	        int visibleLastPosi = listView.getLastVisiblePosition();  
	        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {  
	            View view = listView.getChildAt(posi - visibleFirstPosi);  
	            ViewHolder holder = (ViewHolder) view.getTag();  
	            if(holder.ivSelected.getVisibility() == View.INVISIBLE){
	            	holder.ivSelected.setVisibility(View.VISIBLE);
	            	this.userInterest.add((String) holder.tvName.getText());
	            }
	            else{
	            	holder.ivSelected.setVisibility(View.INVISIBLE);
	            	this.userInterest.remove((String)holder.tvName.getText());
	            }
	            Log.i("InterestListAdapter", "兴趣个数:" + this.userInterest.size());
	        }
	}

}
