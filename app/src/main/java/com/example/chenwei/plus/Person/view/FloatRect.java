package com.example.chenwei.plus.Person.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.chenwei.plus.R;

/**
 * Created by DeanGuo on 1/7/17.
 */

public class FloatRect extends FloatObject {

    int rotate;
    int width;

    public FloatRect(float posX, float posY, int rotate, int width) {
        super(posX, posY);
        this.rotate = rotate;
        this.width = width;
        setAlpha(88);
        setColor(R.color.bubble_blue);
    }
    @Override
    public void drawFloatObject(Canvas canvas, float x, float y, Paint paint) {
        RectF f = new RectF(x-width/8, y-width, x+width/8, y+width);
        canvas.drawRect(f, paint);
    }
}
