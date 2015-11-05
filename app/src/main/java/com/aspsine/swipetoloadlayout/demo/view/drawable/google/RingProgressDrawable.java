package com.aspsine.swipetoloadlayout.demo.view.drawable.google;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by aspsine on 15/9/11.
 */
public class RingProgressDrawable extends ProgressDrawable {
    /**
     * in dp
     */
    private static final int DEFAULT_BORDER_WIDTH = 3;

    private static final int DEFAULT_START_ANGLE = 270;

    private static final int DEFAULT_FINAL_DEGREES = 360;

    private Paint mPaint;

    private Path mPath;

    private RectF mBounds;

    private int mAlpha;

    private float mDegrees;

    private float mAngle;

    private int mColorIndex;

    private float mPercent;


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
        if (percent >= 1) {
            percent = 1;
        }
        int colors[] = getColors();
        mPaint.setColor(colors[0]);
        mAngle = DEFAULT_FINAL_DEGREES * percent - 0.001f;
        mAlpha = (int) (255 * percent);
        mDegrees = 360 * percent;
        invalidateSelf();
    }

    @Override
    public void start() {
        setRunning(true);
        post(mAnimRunnable);
    }

    @Override
    public void stop() {
        setRunning(false);
        removeCallBacks(mAnimRunnable);
        mAngle = 0;
        mDegrees = 0;
    }

    private Runnable mAnimRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning()) {
                int[] colors = getColors();
                int length = colors.length;
                mDegrees += 5;
                if (mDegrees >= 360) {
                    mDegrees = 0;
                    mColorIndex++;
                    if (mColorIndex >= length) {
                        mColorIndex = 0;
                    }
                    mPaint.setColor(colors[mColorIndex]);
                }
                mAngle = mDegrees;
                invalidateSelf();
                postDelayed(this, DELAY);
            }
        }
    };

    @Override
    public void setAlpha(int alpha) {
        mAlpha = alpha;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(mDegrees, canvas.getWidth() / 2, canvas.getHeight() / 2);

        mPath.reset();
        float d = Math.min(canvas.getWidth(), canvas.getHeight());
        float left = dp2px(DEFAULT_BORDER_WIDTH);
        float top = dp2px(DEFAULT_BORDER_WIDTH);
        float right = (d - dp2px(DEFAULT_BORDER_WIDTH));
        float bottom = (d - dp2px(DEFAULT_BORDER_WIDTH));
        mBounds.set(left, top, right, bottom);
        mPath.arcTo(mBounds, DEFAULT_START_ANGLE, mAngle, true);
        mPaint.setAlpha(mAlpha);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }
}
