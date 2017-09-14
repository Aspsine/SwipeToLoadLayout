package com.aspsine.swipetoloadlayout.demo.viewfactory;

import android.content.Context;
import android.view.LayoutInflater;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.aspsine.swipetoloadlayout.SwipeViewFactory;
import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.view.footer.ClassicLoadMoreFooterView;
import com.aspsine.swipetoloadlayout.demo.view.header.TwitterRefreshHeaderView;

/**
 * Created by Alex on 2017/9/14.
 */

public class TwitterRefreshSwipeViewFactory implements SwipeViewFactory<TwitterRefreshHeaderView,ClassicLoadMoreFooterView>{
	private Context mContext;

	public TwitterRefreshSwipeViewFactory(Context context) {
		mContext = context;
	}

	@Override
	public TwitterRefreshHeaderView createHeaderView(SwipeToLoadLayout parent) {
		return (TwitterRefreshHeaderView) LayoutInflater.from(mContext).inflate(R.layout.layout_twitter_header,parent,false);
	}

	@Override
	public ClassicLoadMoreFooterView createFooterView(SwipeToLoadLayout parent) {
		return (ClassicLoadMoreFooterView) LayoutInflater.from(mContext).inflate(R.layout.layout_classic_footer,parent,false);
	}
}
