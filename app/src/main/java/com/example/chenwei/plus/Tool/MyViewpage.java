package com.example.chenwei.plus.Tool;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Chenwei on 2018/8/17.
 */

public class MyViewpage extends ViewPager {
    private boolean scrollble=true;
    private float startX;
    private float moveX;

    public MyViewpage(Context context){
        super(context);
    }

    public MyViewpage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (scrollble == false) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
}
