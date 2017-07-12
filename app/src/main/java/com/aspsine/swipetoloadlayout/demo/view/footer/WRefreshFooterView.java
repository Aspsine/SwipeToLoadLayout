package com.aspsine.swipetoloadlayout.demo.view.footer;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.view.VerticalTextView2;


/**
 * Created on 2016/1/14.
 * Author: wang
 */
public class WRefreshFooterView extends LinearLayout implements SwipeTrigger, SwipeLoadMoreTrigger {

    private ImageView mLoadingImg;

    private VerticalTextView2 mLoadMoreTV;

    private AnimationDrawable mAnimDrawable;

    private int mFooterHeight;


    public WRefreshFooterView(Context context) {
        super(context);
        mFooterHeight = getResources().getDimensionPixelOffset(R.dimen.refresh_footer_height);
    }

    public WRefreshFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFooterHeight = getResources().getDimensionPixelOffset(R.dimen.refresh_footer_height);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLoadingImg = (ImageView) findViewById(R.id.loading_img);
        mLoadMoreTV = (VerticalTextView2) findViewById(R.id.load_more_tv);
        mAnimDrawable = (AnimationDrawable) mLoadingImg.getDrawable();
    }


    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            mAnimDrawable.stop();
            mLoadingImg.setVisibility(GONE);
            mLoadMoreTV.setVisibility(VISIBLE);
            if (-y >= mFooterHeight) {
                mLoadMoreTV.setText("释放加载更多");
            } else {
                mLoadMoreTV.setText("左滑加载更多");
            }
        }
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onLoadMore() {
        mLoadMoreTV.setVisibility(GONE);
        mLoadingImg.setVisibility(VISIBLE);
        mAnimDrawable.start();
    }

    @Override
    public void onComplete() {
        mAnimDrawable.stop();
        mLoadingImg.setVisibility(GONE);
        mLoadMoreTV.setVisibility(VISIBLE);
        mLoadMoreTV.setText("加载完成");
    }

    @Override
    public void onReset() {
        mAnimDrawable.stop();
        mLoadingImg.setVisibility(GONE);
        mLoadMoreTV.setVisibility(VISIBLE);
        mLoadMoreTV.setText("左滑加载更多");
    }

}
