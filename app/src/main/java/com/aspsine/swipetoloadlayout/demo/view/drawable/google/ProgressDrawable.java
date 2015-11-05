package com.aspsine.swipetoloadlayout.demo.view.drawable.google;

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

    /**
     * smooth animation requires 60 frame in 1000ms
     * We can know fresh 1 frame requires 1000/60 ms
     */
    public static final int DELAY = 1000 / 60;

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

    public abstract void setPercent(float progress, boolean isUser);

    public void setPercent(float progress) {
        setPercent(progress, false);
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

    protected void removeCallBacks(Runnable runnable) {
        if (mHandler != null) {
            mHandler.removeCallbacks(runnable);
        }
    }


    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }
}
