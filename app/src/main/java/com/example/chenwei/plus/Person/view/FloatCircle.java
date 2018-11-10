package com.example.chenwei.plus.Person.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.chenwei.plus.R;

import b.Android;


/**
 * Created by DeanGuo on 1/7/17.
 */

public class FloatCircle extends FloatObject {
    public FloatCircle(float posX, float posY) {
        super(posX, posY);
        setAlpha(120);
        setColor(R.color.color_48baf3);
    }

    @Override
    public void drawFloatObject(Canvas canvas, float x, float y, Paint paint) {
        canvas.drawCircle(x, y, 10, paint);
    }
}
