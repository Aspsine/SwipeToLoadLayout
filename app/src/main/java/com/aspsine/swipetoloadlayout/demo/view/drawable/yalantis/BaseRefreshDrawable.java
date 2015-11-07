package com.aspsine.swipetoloadlayout.demo.view.drawable.yalantis;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

/**
 * Copy from https://github.com/Yalantis/Phoenix
 * Aspsine makes some changes
 */
public abstract class BaseRefreshDrawable extends Drawable implements Drawable.Callback, Animatable {

    private boolean mEndOfRefreshing;

    private Context mContext;

    private Handler mHandler;

    public BaseRefreshDrawable(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public abstract void setPercent(float percent, boolean invalidate);

    public abstract void offsetTopAndBottom(int offset);

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    /**
     * Our animation depend on type of current work of refreshing.
     * We should to do different things when it's end of refreshing
     *
     * @param endOfRefreshing - we will check current state of refresh with this
     */
    public void setEndOfRefreshing(boolean endOfRefreshing) {
        mEndOfRefreshing = endOfRefreshing;
    }

    protected void post(Runnable runnable) {
        postDelayed(runnable, 0);
    }

    protected void postDelayed(Runnable runnable, int delayMillis) {
        if (mHandler == null) {
            synchronized (BaseRefreshDrawable.class) {
                mHandler = new Handler(Looper.getMainLooper());
            }
        }
        mHandler.postDelayed(runnable, delayMillis);

    }
}
