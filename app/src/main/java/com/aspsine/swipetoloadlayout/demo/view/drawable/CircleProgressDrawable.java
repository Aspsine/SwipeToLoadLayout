package com.aspsine.swipetoloadlayout.demo.view.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by aspsine on 15/9/11.
 */
public class CircleProgressDrawable extends ProgressDrawable {
    /**
     * in dp
     */
    private static final int DEFAULT_BORDER_WIDTH = 3;

    private Paint mPaint;

    private Path mPath;

    private RectF mBounds;

    public CircleProgressDrawable(Context context) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(DEFAULT_BORDER_WIDTH));
        mPath = new Path();
    }

    @Override
    public void setProgress(float progress, boolean isUser) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
    }

}
