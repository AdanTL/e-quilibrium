package com.app.sirdreadlocks.e_quilibrium;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by adan on 7/25/16.
 */
public class CanvasView extends View {
    final float c_x = getWidth() * 1.5f;
    final float c_y = getHeight() * 1.5f;
    private float ini_x = 0.0f;
    private float ini_y = 0.0f;
    private float end_x = 0.0f;
    private float end_y = 0.0f;
    private Paint circles;

    private void init(){
        circles = new Paint();
        circles.setAntiAlias(true);
        circles.setColor(Color.BLACK);
        circles.setStyle(Paint.Style.STROKE);
        circles.setStrokeWidth(2f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    }


    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {

        for(int i = 1; i <= 110; i += 10)
            canvas.drawCircle(c_x, c_y, i, circles);


    }

    public void setPoint(float x, float y){
        ini_x = end_x;
        ini_y = end_y;
        end_x = x;
        end_y = y;
        invalidate();
        requestLayout();
    }
}
