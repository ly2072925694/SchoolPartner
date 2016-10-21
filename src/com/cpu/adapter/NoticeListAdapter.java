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
 *  ֪ͨ�б�������
 * @author ZJC
 *
 */
public class NoticeListAdapter extends BaseAdapter {

	/**
	 * ��Ϣ�б���
	 */
	public static List<Notice> datas;	
	private Context context;
	private ListView lv;
	private TextView tvNo;//����֪ͨ��ʾ
	
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
		
//		Log.i("NoticeListAdapter", "����:" + datas.get(position).getContent());
		holder.tvNoticeTitle.setText(datas.get(position).getTitle());
		holder.tvNoticeContent.setText(datas.get(position).getContent());
		holder.ivClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				datas.remove(position);//��֪ͨ�������Ƴ�
				notifyDataSetChanged();//ˢ��ListView��ʾ
				if(datas.size() == 0){//����֪ͨ��ʾ
					tvNo.setVisibility(View.VISIBLE);
				}else{
					tvNo.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		return convertView;
	}
	
	public class ViewHolder{
		private TextView tvNoticeTitle;//��Ϣ����
		private TextView tvNoticeContent;//��Ϣ���
		private ImageView ivClear;//�����Ϣ		
	}
	
	/**
	 * ���µ���Item
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
