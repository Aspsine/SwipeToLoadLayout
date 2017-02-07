package com.aspsine.swipetoloadlayout.demo.view.header;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.view.VerticalTextView2;

/**
 * Created on 2016/9/20.
 * Author: wang
 */
public class WRefreshHeaderView extends LinearLayout implements SwipeTrigger, SwipeRefreshTrigger {

    private ImageView mArrowImg;

    private ImageView mLoadingImg;

    private ImageView mSuccessImg;

    private VerticalTextView2 mRefreshTV;

    private AnimationDrawable mAnimDrawable;

    private int mHeaderHeight;

    private Animation rotateUp;

    private Animation rotateDown;

    private boolean rotated = false;

    public WRefreshHeaderView(Context context) {
        this(context, null);
    }

    public WRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.refresh_header_height);
        rotateUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up);
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.rotate_down);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mRefreshTV = (VerticalTextView2) findViewById(R.id.refresh_tv);
        mArrowImg = (ImageView) findViewById(R.id.arrow_img);
        mSuccessImg = (ImageView) findViewById(R.id.success_img);
        mLoadingImg = (ImageView) findViewById(R.id.loading_img);
        mAnimDrawable = (AnimationDrawable) mLoadingImg.getDrawable();
    }

    @Override
    public void onRefresh() {
        mSuccessImg.setVisibility(GONE);
        mArrowImg.clearAnimation();
        mArrowImg.setVisibility(GONE);
        mLoadingImg.setVisibility(VISIBLE);
        mAnimDrawable.start();
        mRefreshTV.setText("加载中...");
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            mArrowImg.setVisibility(VISIBLE);
            mAnimDrawable.stop();
            mLoadingImg.setVisibility(GONE);
            mSuccessImg.setVisibility(GONE);
            if (y > mHeaderHeight) {
                mRefreshTV.setText("释放刷新");
                if (!rotated) {
                    mArrowImg.clearAnimation();
                    mArrowImg.startAnimation(rotateUp);
                    rotated = true;
                }
            } else if (y < mHeaderHeight) {
                if (rotated) {
                    mArrowImg.clearAnimation();
                    mArrowImg.startAnimation(rotateDown);
                    rotated = false;
                }
                mRefreshTV.setText("右滑刷新");
            }
        }
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        rotated = false;
        mSuccessImg.setVisibility(VISIBLE);
        mArrowImg.clearAnimation();
        mArrowImg.setVisibility(GONE);
        mAnimDrawable.stop();
        mLoadingImg.setVisibility(GONE);
        mRefreshTV.setText("刷新完成");
    }

    @Override
    public void onReset() {
        rotated = false;
        mSuccessImg.setVisibility(GONE);
        mArrowImg.clearAnimation();
        mArrowImg.setVisibility(VISIBLE);
        mAnimDrawable.stop();
        mLoadingImg.setVisibility(GONE);
    }

}
