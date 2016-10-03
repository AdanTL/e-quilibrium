package com.app.sirdreadlocks.e_quilibrium;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by adan on 7/25/16.
 */
public class CanvasView extends View {
    private float c_x = 0;
    private float c_y = 0;
    private float maxRadius = 0;
    private Path mPath;
    private Paint radar;

    private void init() {
        radar = new Paint();
        radar.setAntiAlias(true);
        radar.setColor(Color.BLACK);
        radar.setStyle(Paint.Style.STROKE);
        radar.setStrokeWidth(2f);

        mPath = new Path();

    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Radar circles
        for (int i = 1; i < maxRadius; i += maxRadius / 4)
            canvas.drawCircle(c_x, c_y, i, radar);

        //Radar lines
        canvas.drawLine(c_x - maxRadius, c_y, c_x + maxRadius, c_y, radar);
        canvas.drawLine(c_x, c_y - maxRadius, c_x, c_y + maxRadius, radar);

        //Movement lines
        canvas.drawPath(mPath, radar);
    }

    public void setPoint(float x, float y) {
        //Map coordinate 20 degrees as end of Zone D of radar.
        mPath.lineTo(c_x + x * (maxRadius / 20), c_y - y * (maxRadius / 20));
        invalidate();
        requestLayout();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float usableWidth = (float) w - (float) (getPaddingLeft() + getPaddingRight());
        float usableHeight = (float) h - (float) (getPaddingTop() + getPaddingBottom());

        maxRadius = Math.min(usableWidth, usableHeight) / 2;
        c_x = getPaddingLeft() + (usableWidth / 2);
        c_y = getPaddingTop() + (usableHeight / 2);
        mPath.moveTo(c_x, c_y);
    }
}
