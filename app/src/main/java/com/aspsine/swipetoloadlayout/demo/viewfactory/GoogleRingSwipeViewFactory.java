package com.aspsine.swipetoloadlayout.demo.viewfactory;

import android.content.Context;
import android.view.LayoutInflater;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.aspsine.swipetoloadlayout.SwipeViewFactory;
import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.view.footer.GoogleLoadMoreFooterView;
import com.aspsine.swipetoloadlayout.demo.view.header.GoogleRefreshHeaderView;

/**
 * Created by Alex on 2017/9/14.
 */

public class GoogleRingSwipeViewFactory implements SwipeViewFactory<GoogleRefreshHeaderView,GoogleLoadMoreFooterView>{
	private Context mContext;

	public GoogleRingSwipeViewFactory(Context context) {
		mContext = context;
	}

	@Override
	public GoogleRefreshHeaderView createHeaderView(SwipeToLoadLayout parent) {
		return (GoogleRefreshHeaderView) LayoutInflater.from(mContext).inflate(R.layout.layout_google_header,parent,false);
	}

	@Override
	public GoogleLoadMoreFooterView createFooterView(SwipeToLoadLayout parent) {
		return (GoogleLoadMoreFooterView) LayoutInflater.from(mContext).inflate(R.layout.layout_google_footer,parent,false);
	}
}
