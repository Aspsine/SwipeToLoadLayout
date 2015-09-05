package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;

/**
 * Created by aspsine on 15/9/5.
 */
public class CircleTransformation implements Transformation {
    private static final int DEFAULT_BORDER_WIDTH = 10;
    private static final int DEFAULT_BORDER_COLOR = Color.parseColor("#000000");

    int mBorderWidth = 0;
    int mBorderColor = 0;

    public CircleTransformation() {
        mBorderWidth = DEFAULT_BORDER_WIDTH;
        mBorderColor = DEFAULT_BORDER_COLOR;
    }

    public CircleTransformation(int borderWidth) {
        this.mBorderWidth = borderWidth;
        this.mBorderColor = DEFAULT_BORDER_COLOR;
    }

    public CircleTransformation(int borderWidth, int borderColor) {
        this.mBorderWidth = borderWidth;
        this.mBorderColor = borderColor;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();
        int diameter = Math.min(width, height);
        int x = (width - diameter)/2;
        int y =(height - diameter)/2;
        Bitmap squareBitmap = Bitmap.createBitmap(source, x, y, diameter, diameter);
        if (squareBitmap != source) {
            source.recycle();
        }
        Bitmap bitmap = Bitmap.createBitmap(diameter, diameter, source.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squareBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float radius = diameter / 2f;

        if (mBorderWidth > 0) {
            Paint bgPaint = new Paint();
            bgPaint.setColor(mBorderColor);
            bgPaint.setAntiAlias(true);
            canvas.drawCircle(radius , radius , radius , bgPaint);
        }
        canvas.drawCircle(radius, radius, radius-mBorderWidth, paint);
        squareBitmap.recycle();
        squareBitmap = null;
        return bitmap;
    }

    @Override
    public String key() {
        return getClass().getSimpleName();
    }
}
