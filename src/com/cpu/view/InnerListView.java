package com.cpu.view;

import java.util.logging.LogManager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class InnerListView extends ListView {
	public InnerListView(Context context) {
		super(context);
	}
	public InnerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public InnerListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

	int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

	MeasureSpec.AT_MOST);

	super.onMeasure(widthMeasureSpec, expandSpec);

	}
}
