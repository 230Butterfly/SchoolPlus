package com.example.chenwei.plus.Person.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.chenwei.plus.R;


/**
 * Created by DeanGuo on 1/7/17.
 */

public class FloatRing extends FloatObject {
    int strokeWidth;
    int ridus;

    public FloatRing(float posX, float posY, int strokeWidth, int ridus) {
        super(posX, posY);
        this.strokeWidth = strokeWidth;
        this.ridus = ridus;
        setAlpha(88);
        setColor(R.color.Goldenrod1);
    }

    @Override
    public void drawFloatObject(Canvas canvas, float x, float y, Paint paint) {
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(x, y, ridus, paint);
    }
}
