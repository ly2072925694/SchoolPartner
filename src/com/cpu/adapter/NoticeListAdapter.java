package com.cpu.adapter;

import java.util.List;

import com.cpu.entity.Notice;
import com.example.schoolpartner.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 *  通知列表适配器
 * @author ZJC
 *
 */
public class NoticeListAdapter extends BaseAdapter {

	/**
	 * 消息列表集合
	 */
	public static List<Notice> datas;	
	private Context context;
	private ListView lv;
	private TextView tvNo;//暂无通知提示
	
	public NoticeListAdapter(Context context,ListView lv,List<Notice> datas,TextView tv){
		this.context = context;
		this.datas = datas;
		this.lv = lv;
		this.tvNo = tv;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_list_notice, parent,false);
			holder = new ViewHolder();
			holder.tvNoticeTitle = (TextView) convertView.findViewById(R.id.tv_notice_title);
			holder.tvNoticeContent = (TextView) convertView.findViewById(R.id.tv_notice_content);
			holder.ivClear = (ImageView) convertView.findViewById(R.id.iv_clear);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
//		Log.i("NoticeListAdapter", "数据:" + datas.get(position).getContent());
		holder.tvNoticeTitle.setText(datas.get(position).getTitle());
		holder.tvNoticeContent.setText(datas.get(position).getContent());
		holder.ivClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				datas.remove(position);//从通知集合中移除
				notifyDataSetChanged();//刷新ListView显示
				if(datas.size() == 0){//暂无通知提示
					tvNo.setVisibility(View.VISIBLE);
				}else{
					tvNo.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		return convertView;
	}
	
	public class ViewHolder{
		private TextView tvNoticeTitle;//消息标题
		private TextView tvNoticeContent;//消息类别
		private ImageView ivClear;//清除消息		
	}
	
	/**
	 * 更新单个Item
	 * @param posi
	 * @param listView
	 *//*
	public void updateSelected(int posi, ListView listView){
		 int visibleFirstPosi = listView.getFirstVisiblePosition();  
	        int visibleLastPosi = listView.getLastVisiblePosition();  
	        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {  
	            View view = listView.getChildAt(posi - visibleFirstPosi);  
	            ViewHolder holder = (ViewHolder) view.getTag();  
	            if(holder.ivSelected.getVisibility() == View.INVISIBLE){
	            	holder.ivSelected.setVisibility(View.VISIBLE);
	            }
	            else{
	            	holder.ivSelected.setVisibility(View.INVISIBLE);
	            }
	        }
	}*/

}
