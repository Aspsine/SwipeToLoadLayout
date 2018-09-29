package com.aspsine.swipetoloadlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Aspsine on 2015/8/13.
 */
public class SwipeRefreshHeaderLayout extends FrameLayout implements SwipeRefreshTrigger, SwipeTrigger {

    private ImageView iv;
    private Animation.AnimationListener mListener;

    public SwipeRefreshHeaderLayout(Context context) {
        this(context, null);
    }

    public SwipeRefreshHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRefreshHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iv = findViewById(R.id.loading_view);
    }

    @Override
    public void setAnimationListener(Animation.AnimationListener listener) {
        mListener = listener;
    }

    @Override
    public void onAnimationStart() {
        super.onAnimationStart();
        if (mListener != null) {
            mListener.onAnimationStart(getAnimation());
        }
    }

    @Override
    public void onAnimationEnd() {
        super.onAnimationEnd();
        if (mListener != null) {
            mListener.onAnimationEnd(getAnimation());
        }
    }
    @Override
    public void onPrepare() {
        if (iv!=null){
            iv.setImageResource(R.drawable.refresh);
        }
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onRefresh() {
        if (iv!=null){
            Glide.with(iv)
                    .asGif()
                    .load(R.drawable.gif_refresh)
                    .into(iv);
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onReset() {
        if (iv!=null){
            iv.setImageResource(R.drawable.refresh);
        }
    }
}
