package com.aspsine.swipetoloadlayout.demo.view.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by aspsine on 15/9/11.
 */
public class RingProgressDrawable extends ProgressDrawable {
    /**
     * in dp
     */
    private static final int DEFAULT_BORDER_WIDTH = 3;

    private Paint mPaint;

    private Path mPath;

    private RectF mBounds;

    private float mDegrees;

    private float mAngle;

    private float mLevel;

    private boolean mRunning;

    public RingProgressDrawable(Context context) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(DEFAULT_BORDER_WIDTH));
        mPath = new Path();
        mPaint.setColor(Color.WHITE);
    }

    @Override
    public void setProgress(float progress, boolean isUser) {
        mAngle = 360 * progress;
        invalidateSelf();
    }

    @Override
    public void start() {
        mLevel = 50;
        mRunning = true;
        post(mAnimRunnable);
    }

    @Override
    public void stop() {

    }

    private Runnable mAnimRunnable = new Runnable() {
        @Override
        public void run() {
            if (mRunning) {
                mLevel++;
                if (mLevel > 200) {
                    mLevel = 0;
                }
                updateLevel(mLevel);
                invalidateSelf();
//                postDelayed(this, 20);
            }
        }
    };

    private void updateLevel(float level) {
        float percent = level % 50 / 50f;
        mPaint.setColor(Color.WHITE);
        mDegrees = 360 * percent;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mBounds = new RectF(bounds.width(), bounds.top, bounds.right, bounds.bottom);
        mBounds.inset(dp2px(15), dp2px(15));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2 - 15, mPaint);
//        canvas.save();
//        canvas.rotate(mDegrees, mBounds.centerX(), mBounds.centerX());
//        drawRing();
//        canvas.restore();
    }

    private void drawRing() {
        mPath.reset();
        mPath.arcTo(mBounds, 270, mAngle, true);
    }
}
