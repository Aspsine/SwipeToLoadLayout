package com.aspsine.swipetoloadlayout.demo.view.drawable.yalantis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.view.header.YalantisPhoenixRefreshHeaderView;


/**
 * Created by Oleksii Shliama on 22/12/2014.
 * https://dribbble.com/shots/1650317-Pull-to-Refresh-Rentals
 *
 * Copy from https://github.com/Yalantis/Phoenix
 * Aspsine makes some changes
 */
public class SunRefreshDrawable extends BaseRefreshDrawable implements Animatable {

    private static final float SCALE_START_PERCENT = 0.5f;
    private static final int ANIMATION_DURATION = 1000;

    private final static float SKY_RATIO = 0.65f;
    private static final float SKY_INITIAL_SCALE = 1.05f;

    private final static float TOWN_RATIO = 0.22f;
    private static final float TOWN_INITIAL_SCALE = 1.20f;
    private static final float TOWN_FINAL_SCALE = 1.30f;

    private static final float SUN_FINAL_SCALE = 0.75f;
    private static final float SUN_INITIAL_ROTATE_GROWTH = 1.2f;
    private static final float SUN_FINAL_ROTATE_GROWTH = 1.5f;

    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

    private YalantisPhoenixRefreshHeaderView mHeaderView;

    private int mTotalDragDistance;

    private int mRefreshHeaderWidth;


    private Matrix mMatrix;
    private Animation mAnimation;

    private int mTop;
    private int mScreenWidth;

    private int mSkyHeight;
    private float mSkyTopOffset;
    private float mSkyMoveOffset;

    private int mTownHeight;
    private float mTownInitialTopOffset;
    private float mTownFinalTopOffset;
    private float mTownMoveOffset;

    private int mSunSize = 100;
    private float mSunLeftOffset;
    private float mSunTopOffset;

    private float mPercent = 0.0f;
    private float mRotate = 0.0f;

    private Bitmap mSky;
    private Bitmap mSun;
    private Bitmap mTown;

    private boolean isRefreshing = false;

    public SunRefreshDrawable(Context context, final YalantisPhoenixRefreshHeaderView headerView, int totalDistance, int headerWidth) {
        super(context);
        mHeaderView = headerView;
        mTotalDragDistance = totalDistance;
        mRefreshHeaderWidth = headerWidth;
        mMatrix = new Matrix();

        setupAnimations();
        headerView.post(new Runnable() {
            @Override
            public void run() {
                initiateDimens(mRefreshHeaderWidth);
            }
        });
    }

    public void initiateDimens(int viewWidth) {
        if (viewWidth <= 0 || viewWidth == mScreenWidth) return;

        mScreenWidth = viewWidth;
        mSkyHeight = (int) (SKY_RATIO * mScreenWidth);
        mSkyTopOffset = (mSkyHeight * 0.38f);
        mSkyMoveOffset = dp2px(15);

        mTownHeight = (int) (TOWN_RATIO * mScreenWidth);
        mTownInitialTopOffset = (mTotalDragDistance - mTownHeight * TOWN_INITIAL_SCALE);
        mTownFinalTopOffset = (mTotalDragDistance - mTownHeight * TOWN_FINAL_SCALE);
        mTownMoveOffset = dp2px(10);

        mSunLeftOffset = 0.3f * (float) mScreenWidth;
        mSunTopOffset = (mTotalDragDistance * 0.1f);

        mTop = -mTotalDragDistance;

        createBitmaps();
    }

    private void createBitmaps() {
        mSky = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.yalantis_phoenix_sky);
        mSky = Bitmap.createScaledBitmap(mSky, mScreenWidth, mSkyHeight, true);
        mTown = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.yalantis_phoenix_buildings);
        mTown = Bitmap.createScaledBitmap(mTown, mScreenWidth, (int) (mScreenWidth * TOWN_RATIO), true);
        mSun = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.yalantis_phoenix_sun);
        mSun = Bitmap.createScaledBitmap(mSun, mSunSize, mSunSize, true);
    }

    @Override
    public void setPercent(float percent, boolean invalidate) {
        setPercent(percent);
        if (invalidate) setRotate(percent);
    }

    @Override
    public void offsetTopAndBottom(int offset) {
        mTop += offset;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mScreenWidth <= 0) return;

        final int saveCount = canvas.save();

        canvas.translate(0, mTop);
        canvas.clipRect(0, -mTop, mScreenWidth, mTotalDragDistance);

        drawSky(canvas);
        drawSun(canvas);
        drawTown(canvas);

        canvas.restoreToCount(saveCount);
    }

    private void drawSky(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = Math.min(1f, Math.abs(mPercent));

        float skyScale;
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
        if (scalePercentDelta > 0) {
            /** Change skyScale between {@link #SKY_INITIAL_SCALE} and 1.0f depending on {@link #mPercent} */
            float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
            skyScale = SKY_INITIAL_SCALE - (SKY_INITIAL_SCALE - 1.0f) * scalePercent;
        } else {
            skyScale = SKY_INITIAL_SCALE;
        }

        float offsetX = -(mScreenWidth * skyScale - mScreenWidth) / 2.0f;
        float offsetY = (1.0f - dragPercent) * mTotalDragDistance - mSkyTopOffset // Offset canvas moving
                - mSkyHeight * (skyScale - 1.0f) / 2 // Offset sky scaling
                + mSkyMoveOffset * dragPercent; // Give it a little move top -> bottom

        matrix.postScale(skyScale, skyScale);
        matrix.postTranslate(offsetX, offsetY);
        canvas.drawBitmap(mSky, matrix, null);
    }

    private void drawTown(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = Math.min(1f, Math.abs(mPercent));

        float townScale;
        float townTopOffset;
        float townMoveOffset;
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
        if (scalePercentDelta > 0) {
            /**
             * Change townScale between {@link #TOWN_INITIAL_SCALE} and {@link #TOWN_FINAL_SCALE} depending on {@link #mPercent}
             * Change townTopOffset between {@link #mTownInitialTopOffset} and {@link #mTownFinalTopOffset} depending on {@link #mPercent}
             */
            float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
            townScale = TOWN_INITIAL_SCALE + (TOWN_FINAL_SCALE - TOWN_INITIAL_SCALE) * scalePercent;
            townTopOffset = mTownInitialTopOffset - (mTownFinalTopOffset - mTownInitialTopOffset) * scalePercent;
            townMoveOffset = mTownMoveOffset * (1.0f - scalePercent);
        } else {
            float scalePercent = dragPercent / SCALE_START_PERCENT;
            townScale = TOWN_INITIAL_SCALE;
            townTopOffset = mTownInitialTopOffset;
            townMoveOffset = mTownMoveOffset * scalePercent;
        }

        float offsetX = -(mScreenWidth * townScale - mScreenWidth) / 2.0f;
        float offsetY = (1.0f - dragPercent) * mTotalDragDistance // Offset canvas moving
                + townTopOffset
                - mTownHeight * (townScale - 1.0f) / 2 // Offset town scaling
                + townMoveOffset; // Give it a little move

        matrix.postScale(townScale, townScale);
        matrix.postTranslate(offsetX, offsetY);

        canvas.drawBitmap(mTown, matrix, null);
    }

    private void drawSun(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = mPercent;
        if (dragPercent > 1.0f) { // Slow down if pulling over set height
            dragPercent = (dragPercent + 9.0f) / 10;
        }

        float sunRadius = (float) mSunSize / 2.0f;
        float sunRotateGrowth = SUN_INITIAL_ROTATE_GROWTH;

        float offsetX = mSunLeftOffset;
        float offsetY = mSunTopOffset
                + (mTotalDragDistance / 2) * (1.0f - dragPercent) // Move the sun up
                - mTop; // Depending on Canvas position

        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
        if (scalePercentDelta > 0) {
            float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
            float sunScale = 1.0f - (1.0f - SUN_FINAL_SCALE) * scalePercent;
            sunRotateGrowth += (SUN_FINAL_ROTATE_GROWTH - SUN_INITIAL_ROTATE_GROWTH) * scalePercent;

            matrix.preTranslate(offsetX + (sunRadius - sunRadius * sunScale), offsetY * (2.0f - sunScale));
            matrix.preScale(sunScale, sunScale);

            offsetX += sunRadius;
            offsetY = offsetY * (2.0f - sunScale) + sunRadius * sunScale;
        } else {
            matrix.postTranslate(offsetX, offsetY);

            offsetX += sunRadius;
            offsetY += sunRadius;
        }

        matrix.postRotate(
                (isRefreshing ? -360 : 360) * mRotate * (isRefreshing ? 1 : sunRotateGrowth),
                offsetX,
                offsetY);

        canvas.drawBitmap(mSun, matrix, null);
    }

    public void setPercent(float percent) {
        mPercent = percent;
    }

    public void setRotate(float rotate) {
        mRotate = rotate;
        invalidateSelf();
    }

    public void resetOriginals() {
        setPercent(0);
        setRotate(0);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, mSkyHeight + top);
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

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void start() {
        mAnimation.reset();
        isRefreshing = true;
        mHeaderView.startAnimation(mAnimation);
    }

    @Override
    public void stop() {
        mHeaderView.clearAnimation();
        isRefreshing = false;
        resetOriginals();
    }

    private void setupAnimations() {
        mAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                setRotate(interpolatedTime);
            }
        };
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setInterpolator(LINEAR_INTERPOLATOR);
        mAnimation.setDuration(ANIMATION_DURATION);
    }

    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

}
