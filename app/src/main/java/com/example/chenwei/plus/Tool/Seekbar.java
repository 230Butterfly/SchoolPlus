package com.example.chenwei.plus.Tool;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * Created by Chenwei on 2018/8/17.
 */

public class Seekbar extends android.support.v7.widget.AppCompatSeekBar {
    public Seekbar(Context context) {
        super(context);
    }
    public Seekbar(Context context,AttributeSet attrs) {
        super(context, attrs);
    }
    public Seekbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(), 0);
        super.onDraw(c);
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int i = 0;
                i = getMax() - (int) (getMax() * event.getY() / getHeight());
                setProgress(i);
                Log.i("Progress", getProgress() + "");
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;
        }
        return true;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }
}
