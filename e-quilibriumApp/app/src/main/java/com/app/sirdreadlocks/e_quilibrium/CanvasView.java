package com.app.sirdreadlocks.e_quilibrium;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by adan on 7/25/16.
 */
public class CanvasView extends View {
    private ShapeDrawable mShape;
    int x = 10;
    int y = 10;
    int width = 300;
    int height = 50;
    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mShape = new ShapeDrawable(new OvalShape());
        mShape.getPaint().setColor(0xff74AC23);
        mShape.setBounds(x, y, x + width, y + height);
    }

    public void onDraw(Canvas canvas){
        x +=10;
        mShape.setBounds(x, y, x + width, y + height);
        mShape.draw(canvas);
    }
}
