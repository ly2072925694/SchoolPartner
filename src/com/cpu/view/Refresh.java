package com.cpu.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.schoolpartner.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Refresh extends ListView implements OnScrollListener{
	View header;// ���������ļ���
	int headerHeight;// ���������ļ��ĸ߶ȣ�
	int firstVisibleItem;// ��ǰ��һ���ɼ���item��λ�ã�
	int scrollState;// listview ��ǰ����״̬��
	boolean isRemark;// ��ǣ���ǰ����listview������µģ�
	int startY;// ����ʱ��Yֵ��

	int state;// ��ǰ��״̬��
	final int NONE = 0;// ����״̬��
	final int PULL = 1;// ��ʾ����״̬��
	final int RELESE = 2;// ��ʾ�ͷ�״̬��
	final int REFLASHING = 3;// ˢ��״̬��

	public Refresh(Context context) {
		super(context);
		initView(context);
	}	
	public Refresh(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	public Refresh(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

		/**
		 * ��ʼ�����棬��Ӷ��������ļ��� listview
		 * 
		 * @param context
		 */
		private void initView(Context context) {
			LayoutInflater inflater = LayoutInflater.from(context);
			header = inflater.inflate(R.layout.header, null);
			measureView(header);
			headerHeight = header.getMeasuredHeight();
			Log.i("tag", "headerHeight = " + headerHeight);
			topPadding(-headerHeight);
			this.addHeaderView(header);
			this.setOnScrollListener(this);
		}

		/**
		 * ֪ͨ�����֣�ռ�õĿ��ߣ�
		 * 
		 * @param view
		 */
		private void measureView(View view) {
			ViewGroup.LayoutParams p = view.getLayoutParams();
			if (p == null) {
				p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
			}
			int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
			int height;
			int tempHeight = p.height;
			if (tempHeight > 0) {
				height = MeasureSpec.makeMeasureSpec(tempHeight,
						MeasureSpec.EXACTLY);
			} else {
				height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
			}
			view.measure(width, height);
		}

		/**
		 * ����header ���� �ϱ߾ࣻ
		 * 
		 * @param topPadding
		 */
		private void topPadding(int topPadding) {
			header.setPadding(header.getPaddingLeft(), topPadding,
					header.getPaddingRight(), header.getPaddingBottom());
			header.invalidate();
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			this.firstVisibleItem = firstVisibleItem;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			this.scrollState = scrollState;
		}

		@Override
		public boolean onTouchEvent(MotionEvent ev) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstVisibleItem == 0) {
					isRemark = true;
					startY = (int) ev.getY();
				}
				break;

			case MotionEvent.ACTION_MOVE:
				onMove(ev);
				break;
			case MotionEvent.ACTION_UP:
				if (state == RELESE) {
					state = REFLASHING;
					// �����������ݣ�
					reflashViewByState();
				} else if (state == PULL) {
					state = NONE;
					isRemark = false;
					reflashViewByState();
				}
				break;
			}
			return super.onTouchEvent(ev);
		}

		/**
		 * �ж��ƶ����̲�����
		 * 
		 * @param ev
		 */
		private void onMove(MotionEvent ev) {
			if (!isRemark) {
				return;
			}
			int tempY = (int) ev.getY();
			int space = tempY - startY;
			int topPadding = space - headerHeight;
			switch (state) {
			case NONE:
				if (space > 0) {
					state = PULL;
					reflashViewByState();
				}
				break;
			case PULL:
				topPadding(topPadding);
				if (space > headerHeight + 30
						&& scrollState == SCROLL_STATE_TOUCH_SCROLL) {
					state = RELESE;
					reflashViewByState();
				}
				break;
			case RELESE:
				topPadding(topPadding);
				if (space < headerHeight + 30) {
					state = PULL;
					reflashViewByState();
				} else if (space <= 0) {
					state = NONE;
					isRemark = false;
					reflashViewByState();
				}
				break;
			}
		}

		/**
		 * ���ݵ�ǰ״̬���ı������ʾ��
		 */
		private void reflashViewByState() {
			TextView tip = (TextView) header.findViewById(R.id.loading);
			ImageView arrow = (ImageView) header.findViewById(R.id.pull);
			ProgressBar progress = (ProgressBar) header.findViewById(R.id.bar);
			RotateAnimation anim = new RotateAnimation(0, 180,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			anim.setDuration(100);
			anim.setFillAfter(true);
			RotateAnimation anim1 = new RotateAnimation(180, 0,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			anim1.setDuration(100);
			anim1.setFillAfter(true);
			switch (state) {
			case NONE:
				arrow.clearAnimation();
				topPadding(-headerHeight);
				break;

			case PULL:
				arrow.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
				tip.setText("下拉刷新");
				arrow.clearAnimation();
				arrow.setAnimation(anim1);
				break;
			case RELESE:
				arrow.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
				tip.setText("释放刷新");
				arrow.clearAnimation();
				arrow.setAnimation(anim);
				break;
			case REFLASHING:
				topPadding(50);
				arrow.setVisibility(View.GONE);
				progress.setVisibility(View.VISIBLE);
				tip.setText("刷新中...");
				reflashComplete();
				arrow.clearAnimation();
				break;
			}
		}
		@SuppressLint("SimpleDateFormat") 
		public void reflashComplete() {
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... arg0) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {

					state = NONE;
					isRemark = false;
					reflashViewByState();
					TextView lastupdatetime = (TextView) header
							.findViewById(R.id.loading_time);
					SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
					Date date = new Date(System.currentTimeMillis());
					String time = format.format(date);
					lastupdatetime.setText(time);
					super.onPostExecute(result);
				}
				
			}.execute();
		}

}
