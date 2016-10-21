package com.cpu.view;

import com.example.schoolpartner.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class AddFooterView extends View{

	private View moreView;
	private ProgressBar bar;
	private TextView loading;
	public abstract void LoadData(ProgressBar bar,TextView loading);
	public AddFooterView(Context context) {
		super(context);
	}

	public View init(Context context) {
		LayoutInflater flater = LayoutInflater.from(context);
		moreView = flater.inflate(R.layout.footer, null);
		bar = (ProgressBar) moreView.findViewById(R.id.progressBar);
		loading = (TextView) moreView.findViewById(R.id.footer);
		moreView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				toInternet();
			}

		});
		return moreView;
	}

	private void toInternet() {
		LoadData( bar, loading);
	}
}
