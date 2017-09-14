package com.aspsine.swipetoloadlayout.demo.viewfactory;

import android.content.Context;
import android.view.LayoutInflater;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.aspsine.swipetoloadlayout.SwipeViewFactory;
import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.view.header.JdRefreshHeaderView;

/**
 * Created by Alex on 2017/9/14.
 */

public class JDSwipeViewFactory implements SwipeViewFactory<JdRefreshHeaderView,SwipeLoadMoreFooterLayout>{
	private Context mContext;

	public JDSwipeViewFactory(Context context) {
		mContext = context;
	}

	@Override
	public JdRefreshHeaderView createHeaderView(SwipeToLoadLayout parent) {
		return (JdRefreshHeaderView) LayoutInflater.from(mContext).inflate(R.layout.layout_jd_header,parent,false);
	}

	@Override
	public SwipeLoadMoreFooterLayout createFooterView(SwipeToLoadLayout parent) {
		return null;
	}
}
