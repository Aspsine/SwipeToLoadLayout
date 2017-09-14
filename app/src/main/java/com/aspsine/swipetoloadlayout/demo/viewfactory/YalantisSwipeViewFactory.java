package com.aspsine.swipetoloadlayout.demo.viewfactory;

import android.content.Context;
import android.view.LayoutInflater;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.aspsine.swipetoloadlayout.SwipeViewFactory;
import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.view.header.YalantisPhoenixRefreshHeaderView;

/**
 * Created by Alex on 2017/9/14.
 */

public class YalantisSwipeViewFactory implements SwipeViewFactory<YalantisPhoenixRefreshHeaderView,SwipeLoadMoreFooterLayout>{
	private Context mContext;

	public YalantisSwipeViewFactory(Context context) {
		mContext = context;
	}

	@Override
	public YalantisPhoenixRefreshHeaderView createHeaderView(SwipeToLoadLayout parent) {
		return (YalantisPhoenixRefreshHeaderView) LayoutInflater.from(mContext).inflate(R.layout.layout_yalantis_header,parent,false);
	}

	@Override
	public SwipeLoadMoreFooterLayout createFooterView(SwipeToLoadLayout parent) {
		return null;
	}
}
