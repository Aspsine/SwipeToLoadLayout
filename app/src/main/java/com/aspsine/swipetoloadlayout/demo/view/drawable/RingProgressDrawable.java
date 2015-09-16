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

    int mWidth;

    int mHeight;

    private float mAngle;

    private int mLevel;

    public RingProgressDrawable(Context context) {
        super(context);
        mBounds = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(DEFAULT_BORDER_WIDTH));
        mPath = new Path();
        mPaint.setColor(Color.WHITE);
    }

    @Override
    public void setPercent(float percent, boolean isUser) {
        if (percent > 1) {
            percent = 1;
        }
        int colors[] = getColors();
        mPaint.setColor(colors[0]);
        mAngle = 360 * percent;
        invalidateSelf();
    }

    private int evaluate(float percent, int startValue, int endValue) {
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return ((startA + (int) (percent * (endA - startA))) << 24) |
                ((startR + (int) (percent * (endR - startR))) << 16) |
                ((startG + (int) (percent * (endG - startG))) << 8) |
                ((startB + (int) (percent * (endB - startB))));
    }

    @Override
    public void start() {
        mLevel = 50;
        setRunning(true);
        post(mAnimRunnable);
    }

    @Override
    public void stop() {
        removeCallBacks(mAnimRunnable);
        mAngle = 0;
        mLevel = 0;
    }

    private int MAX_LEVEL = 200;
    private Runnable mAnimRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning()) {
                mLevel++;
                if (mLevel > MAX_LEVEL)
                    mLevel = 0;
                updateLevel(mLevel);
                invalidateSelf();
                postDelayed(this, 20);
            }
        }
    };

    private void updateLevel(int level) {
        int[] colors = getColors();
        int animationLevel = level == MAX_LEVEL ? 0 : level;

        int stateForLevel = (animationLevel / 50);

        float percent = level % 50 / 50f;
        int startColor = colors[stateForLevel];
        int endColor = colors[(stateForLevel + 1) % colors.length];
        mPaint.setColor(evaluate(percent, startColor, endColor));

        mDegrees = 360 * percent;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mBounds.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    @Override
    public void draw(Canvas canvas) {
//        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2 - 15, mPaint);
        canvas.save();
        canvas.rotate(mDegrees, canvas.getWidth() / 2, canvas.getHeight() / 2);
        mPath.reset();
        int d = Math.min(canvas.getWidth(), canvas.getHeight());
        mBounds.set(dp2px(DEFAULT_BORDER_WIDTH), dp2px(DEFAULT_BORDER_WIDTH), d - dp2px(DEFAULT_BORDER_WIDTH), d - dp2px(DEFAULT_BORDER_WIDTH));
        mPath.arcTo(mBounds, 270, mAngle, true);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }
}
