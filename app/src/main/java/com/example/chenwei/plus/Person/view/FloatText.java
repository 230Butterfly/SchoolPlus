package com.example.chenwei.plus.Person.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.chenwei.plus.R;


/**
 * Created by DeanGuo on 1/7/17.
 */

public class FloatText extends FloatObject {
    String text="资源";

    public FloatText(float posX, float posY, String text) {
        super(posX, posY);
        this.text = text;
        setAlpha(88);
        setColor(R.color.IndianRed1);
    }

    @Override
    public void drawFloatObject(Canvas canvas, float x, float y, Paint paint) {
        paint.setTextSize(65);
        canvas.drawText(text, x, y, paint);
    }
}
