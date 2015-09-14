package com.aspsine.swipetoloadlayout.demo.view.drawable;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;

/**
 * Created by Aspsine on 2015/9/11.
 */
public abstract class ProgressDrawable extends Drawable implements Animatable {

    private final Context mContext;

    private boolean mRunning;

    private Handler mHandler;

    private int[] mColors;


    public ProgressDrawable(Context context) {
        mContext = context;
    }

    public void setColors(int... colors) {
        mColors = colors;
    }

    public int[] getColors() {
        return mColors;
    }

    public abstract void setProgress(float progress, boolean isUser);

    public void setProgress(float progress) {
        setProgress(progress, false);
    }

    public void setRunning(boolean running) {
        this.mRunning = running;
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    protected Context getContext() {
        return mContext;
    }

    protected void post(Runnable runnable) {
        postDelayed(runnable, 0);
    }

    protected void postDelayed(Runnable runnable, int delayMillis) {
        if (mHandler == null) {
            synchronized (ProgressDrawable.class) {
                mHandler = new Handler(Looper.getMainLooper());
            }
        }
        mHandler.postDelayed(runnable, delayMillis);
    }

    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }
}
