package com.example.chenwei.plus.Person.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import com.example.chenwei.plus.R;


/**
 * Created by DeanGuo on 1/7/17.
 */

public class FloatBitmap extends FloatObject {
    Activity activity;
    Bitmap bitmap;
    public FloatBitmap(Activity activity, float posX, float posY, int img) {
        super(posX, posY);
        this.activity = activity;

        setAlpha(120);
//        setColor(R.color.color_48baf3);
        bitmap = ((BitmapDrawable) activity.getResources().getDrawable(img))
                .getBitmap();
    }

    @Override
    public void drawFloatObject(Canvas canvas, float x, float y, Paint paint) {
        int width = 40;
        canvas.drawBitmap(bitmap, x, y, paint);
    }
}
