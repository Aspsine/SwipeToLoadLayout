package com.aspsine.swipetoloadlayout.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.aspsine.swipetoloadlayout.demo.R;

/**
 * Created on 2016/10/31.
 * Author: wang
 */

public class VerticalTextView2 extends View {

    public static enum StartAlign {

        LEFT(0x0),
        RIGHT(0x1);

        private int value;

        StartAlign(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 需要绘制的文字
     */
    private String mText = "";
    /**
     * 文本的颜色
     */
    private int mTextColor = Color.BLACK;
    /**
     * 文本的大小
     */
    private float mTextSize = 40;

    /**
     * 每列最多显示数量
     */
    private int vTextNum = -1;


    /**
     * 字体高度
     */
    private float mFontHeight;
    private float mFontBaseLine;

    // 列宽度
    private int mLineWidth = 0;
    // 列间距
    private int mLineSpacing = 20;

    private int specHeight;
    private int specWidth;
    //能够显示文本的最大高度

    // 绘制字体的默认方向
    private StartAlign textStartAlign = StartAlign.LEFT;

    /**
     * 绘制时控制文本绘制的范围
     */
    private Paint mPaint;

    private boolean isMeasure;

    public VerticalTextView2(Context context) {
        this(context, null);
    }

    public VerticalTextView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalTextView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            //获取自定义属性的值
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VerticalTextView2, defStyleAttr, 0);
            try {
                mText = a.getString(R.styleable.VerticalTextView2_ver_text);
                mTextColor = a.getColor(R.styleable.VerticalTextView2_ver_textColor, Color.BLACK);
                mTextSize = a.getDimensionPixelSize(R.styleable.VerticalTextView2_ver_textSize, 40);
                vTextNum = a.getInt(R.styleable.VerticalTextView2_ver_textNum, -1);
                mLineSpacing = a.getDimensionPixelOffset(R.styleable.VerticalTextView2_ver_lineSpacing, 20);
                int align = a.getInt(R.styleable.VerticalTextView2_ver_textStartAlign, StartAlign.RIGHT.getValue());
                if (StartAlign.LEFT.getValue() == align) {
                    textStartAlign = StartAlign.LEFT;
                } else {
                    textStartAlign = StartAlign.RIGHT;
                }
            } finally {
                a.recycle();
            }
        }
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        // 文字居中
        mPaint.setTextAlign(Paint.Align.CENTER);
        // 平滑处理
        mPaint.setAntiAlias(true);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        //绘制文字
        char StringItem;
        boolean isRight = StartAlign.RIGHT == textStartAlign;
        float textHeight = mFontHeight;
        int viewHeight = specHeight;
        float mTextPosY = mFontBaseLine;
        float mTextPosX = isRight ? getWidth() : 0;
        mTextPosX += isRight ? -mLineWidth >> 1 : mLineWidth >> 1;//字体居住绘制 x起始位置-mLineWidth>>1
        int textLength = mText.length();
        for (int i = 0; i < textLength; i++) {
            StringItem = mText.charAt(i);
            if (mTextPosY > viewHeight) {
                mTextPosY = mFontBaseLine;
                mTextPosX += isRight ? -mLineWidth : mLineWidth;
                if (mTextPosX > specWidth) {
                    return;
                }
            }
            canvas.drawText(String.valueOf(StringItem), mTextPosX, mTextPosY, mPaint);
            mTextPosY += textHeight;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //onMeasure方法会调用多次，为了避免重复分段，做判断

        if (!isMeasure) {
            isMeasure = true;
            //计算字体高度大小
            measureTextSize();
        }
        measureWH(heightMeasureSpec);
        //保存测量宽度和测量高度
        setMeasuredDimension(specWidth, specHeight);
    }



    private void measureWH(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec); //获取高的模式
        int heightSize = MeasureSpec.getSize(heightMeasureSpec); //获取高的尺寸

        int textHeight = (int) mFontHeight;
        int textLength = null == mText ? 0 : mText.length();

        //高度
        //match_parent或者具体的值
        if (heightMode == MeasureSpec.EXACTLY) {
            specHeight = heightSize;
        } else {
            //wrap_content
            float tempHeight = Math.min(textHeight * textLength, heightSize);
            if (vTextNum != -1) {
                tempHeight = vTextNum * textHeight;
            }
            specHeight = (int) (getPaddingTop() + tempHeight + getPaddingBottom());
        }

        float lineNum = textHeight * textLength / specHeight;

        int textWidth = (int) (Math.ceil(lineNum) * mLineWidth);
        specWidth = getPaddingLeft() + textWidth + getPaddingRight();
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
        resetMeasure();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        mPaint.setColor(mTextColor);
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
        mPaint.setTextSize(mTextSize);
        resetMeasure();
    }

    private void resetMeasure() {
        isMeasure = false;
        requestLayout();
    }

    /**
     * 计算文字大小高度
     */
    private void measureTextSize() {

        mPaint.setTextSize(mTextSize);
        // 获得行宽包括间隔
        if (mLineWidth == 0) {
            float[] widths = new float[1];
            // 获取单个汉字的宽度
            mPaint.getTextWidths("汉", widths);

            mLineWidth = mLineSpacing + (int) widths[0];
        }

        Paint.FontMetrics fm = mPaint.getFontMetrics();

        // 获得字体高度
        mFontHeight = fm.bottom - fm.top;
        mFontBaseLine = - fm.top;

    }

}
