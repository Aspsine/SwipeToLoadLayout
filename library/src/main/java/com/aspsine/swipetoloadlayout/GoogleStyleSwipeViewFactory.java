package com.aspsine.swipetoloadlayout;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;

/**
 * Created by Alex on 2017/9/12.
 */

public class GoogleStyleSwipeViewFactory implements SwipeViewFactory<GoogleCircleHookRefreshHeaderView,GoogleCircleHookLoadMoreFooterView>{
	private Context mContext ;
	private final int[] mColorResIds = {
			R.color.google_blue,
			R.color.google_red,
			R.color.google_yellow,
			R.color.google_green};

	public GoogleStyleSwipeViewFactory(Context context) {
		mContext = context;
	}

	@Override
	public GoogleCircleHookRefreshHeaderView createHeaderView(SwipeToLoadLayout parent) {
		GoogleCircleHookRefreshHeaderView view = (GoogleCircleHookRefreshHeaderView) LayoutInflater.from(mContext)
				.inflate(R.layout.layout_google_style_header,parent,false);
		view.setColorSchemeColors(getColorSchemeColors());
		return view;
	}

	@Override
	public GoogleCircleHookLoadMoreFooterView createFooterView(SwipeToLoadLayout parent) {
		GoogleCircleHookLoadMoreFooterView view = (GoogleCircleHookLoadMoreFooterView) LayoutInflater.from(mContext)
				.inflate(R.layout.layout_google_style_footer,parent,false);
		view.setColorSchemeColors(getColorSchemeColors());
		return view;
	}

	public int[] getColorSchemeColors(){
		final Resources res = mContext.getResources();
		int[] colorRes = new int[mColorResIds.length];
		for (int i = 0; i < mColorResIds.length; i++) {
			colorRes[i] = res.getColor(mColorResIds[i]);
		}
		return colorRes ;
	}
}
