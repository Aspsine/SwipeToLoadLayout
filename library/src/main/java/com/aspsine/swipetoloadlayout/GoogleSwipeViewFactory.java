package com.aspsine.swipetoloadlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Alex on 2017/9/12.
 */

public class GoogleSwipeViewFactory implements SwipeViewFactory<GoogleCircleHookRefreshHeaderView,GoogleCircleHookLoadMoreFooterView>{
	private Context mContext ;

	public GoogleSwipeViewFactory(Context context) {
		mContext = context;
	}

	@Override
	public GoogleCircleHookRefreshHeaderView createHeaderView(SwipeToLoadLayout parent) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.layout_google_style_header,parent,false);
		return (GoogleCircleHookRefreshHeaderView) view;
	}

	@Override
	public GoogleCircleHookLoadMoreFooterView createFooterView(SwipeToLoadLayout parent) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.layout_google_style_footer,parent,false);
		return (GoogleCircleHookLoadMoreFooterView) view;
	}
}
