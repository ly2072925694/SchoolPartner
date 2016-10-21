package com.cpu.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cpu.tools.ImageLoader;
import com.cpu.tools.ImageUrl;
import com.cpu.view.WrapHeightGridView;
import com.example.schoolpartner.R;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public abstract class PublishSimpleAdapter extends SimpleAdapter
	implements OnClickListener,OnScrollListener{
	ArrayList<HashMap<String, Object>> publishList;
	GridViewAdapter mGridViewAdapter;
	private Context mContext;
	private int mStart;
	private int mEnd;
	public static String[] urls;
	private ImageLoader mImageLoader;
	private boolean firstIn;
	public static ArrayList<Uri> imgUris = new ArrayList<Uri>();//��ӵ�ͼƬ��ַ����
	@SuppressWarnings("unchecked")
	public PublishSimpleAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.mContext = context;
		urls = new String[5];
//		for(int i=0;i<2;i++){
//			imgUris.add(Uri.parse("http://redirect02.sogou.co743e12981aa5e3b7ee2b1285"));
//		}
		firstIn = true;
		mImageLoader = new ImageLoader();
		publishList = (ArrayList<HashMap<String, Object>>) data;
	}
	@Override
	public Object getItem(int position) {
		return publishList.get(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView)
        {
			holder = new ViewHolder();
			convertView = super.getView(position, convertView, parent);
			holder.state = (TextView) convertView.findViewById(R.id.state1);
			holder.publishMessage = (TextView) convertView.findViewById(R.id.message1);
			holder.proveImage = (ImageView) convertView.findViewById(R.id.prove_image);
			holder.messageImage = (ImageView) convertView
					.findViewById(R.id.message_image);
			holder.personImage = (ImageView) convertView
					.findViewById(R.id.person_image);
			holder.publishProve = (TextView) convertView.findViewById(R.id.prove1);
			holder.publishPerson = (TextView) convertView.findViewById(R.id.person1);
			holder.mGride = (GridView) convertView.findViewById(R.id.grid);
			holder.one = convertView.findViewById(R.id.one);
			holder.two = convertView.findViewById(R.id.two);
			holder.three = convertView.findViewById(R.id.three);
			holder.Message = convertView.findViewById(R.id.messageView);
			holder.Person = convertView.findViewById(R.id.personView);
			holder.Prove = convertView.findViewById(R.id.proveView);
			
			holder.setOne(holder.one);
			holder.setmGride(holder.mGride);
			holder.setTwo(holder.two);
			holder.setThree(holder.three);
			holder.setPublishMessage(holder.publishMessage);
			holder.setState(holder.state);
			holder.setProveImage(holder.proveImage);
			holder.setMessageImage(holder.messageImage);
			holder.setPersonImage(holder.personImage);
			holder.setProve(holder.Prove);
			holder.setMessage(holder.Message);
			holder.setPerson(holder.Person);
			holder.setPublishProve(holder.publishProve);
			holder.setPublishPerson(holder.publishPerson);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
		holder.publishProve.setText(""+publishList.get(position).get("prove"));
		holder.publishPerson.setText(""+publishList.get(position).get("person"));
		holder.publishMessage.setText(""+publishList.get(position).get("message"));
		holder.state.setText(""+publishList.get(position).get("state"));
		holder.mGride.setAdapter(new GridAdapter(mContext, imgUris, holder.mGride));
		holder.mGride.setPressed(false);
		holder.mGride.setEnabled(false);
        convert(holder,position);
		return convertView;
	}
	@Override
	public void onClick(View arg0) {
		
	}
	public static class ViewHolder
    {
        TextView state ;
        TextView publishMessage ;
        ImageView proveImage ;
        ImageView messageImage;
        ImageView personImage;
        TextView publishProve;
        TextView publishPerson;
        GridView mGride;
        View Message;
        View Person;
        View Prove;
        View one;
        View two;
        View three;
        
		public View getMessage() {
			return Message;
		}
		public void setMessage(View message) {
			Message = message;
		}
		public View getPerson() {
			return Person;
		}
		public void setPerson(View person) {
			Person = person;
		}
		public View getProve() {
			return Prove;
		}
		public void setProve(View prove) {
			Prove = prove;
		}
		public GridView getmGride() {
			return mGride;
		}
		public void setmGride(GridView mGride) {
			this.mGride = mGride;
		}
		public TextView getState() {
			return state;
		}
		public void setState(TextView state) {
			this.state = state;
		}
		public TextView getPublishMessage() {
			return publishMessage;
		}
		public void setPublishMessage(TextView publishMessage) {
			this.publishMessage = publishMessage;
		}
		public ImageView getProveImage() {
			return proveImage;
		}
		public void setProveImage(ImageView proveImage) {
			this.proveImage = proveImage;
		}
		public ImageView getMessageImage() {
			return messageImage;
		}
		public void setMessageImage(ImageView messageImage) {
			this.messageImage = messageImage;
		}
		public ImageView getPersonImage() {
			return personImage;
		}
		public void setPersonImage(ImageView personImage) {
			this.personImage = personImage;
		}
		public TextView getPublishProve() {
			return publishProve;
		}
		public void setPublishProve(TextView publishProve) {
			this.publishProve = publishProve;
		}
		public TextView getPublishPerson() {
			return publishPerson;
		}
		public void setPublishPerson(TextView publishPerson) {
			this.publishPerson = publishPerson;
		}
		public View getOne() {
			return one;
		}
		public void setOne(View one) {
			this.one = one;
		}
		public View getTwo() {
			return two;
		}
		public void setTwo(View two) {
			this.two = two;
		}
		public View getThree() {
			return three;
		}
		public void setThree(View three) {
			this.three = three;
		}
        
    }
	public abstract void convert(ViewHolder helper,int position);  
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		if(scrollState == SCROLL_STATE_IDLE){
//			mImageLoader.loadImages(mStart, mEnd);
//		}else{
//			mImageLoader.cancellAllTask();
//		}
	}
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
    		int visibleItemCount, int totalItemCount) {
//    	mEnd = firstVisibleItem + visibleItemCount;
//    	mStart = firstVisibleItem;
//    	if(firstIn && visibleItemCount >0){
//    		mImageLoader.loadImages(firstVisibleItem, mEnd);
//    		firstIn = false;
//    	}
    }
}
