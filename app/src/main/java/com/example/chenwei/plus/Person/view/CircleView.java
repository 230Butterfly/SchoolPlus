package com.example.chenwei.plus.Person.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Toast;

import com.example.chenwei.plus.Person.control.ScreenUtil;
import com.example.chenwei.plus.R;

import java.util.Random;

public class CircleView extends View {
    private int lastX;
    private int lastY;

    int containerWidth = ScreenUtil.getScreenWidth(getContext());
    int containerHeight =  ScreenUtil.getScreenHeight(getContext());

    private Scroller mScroller;
    // 圆的画笔
    private Paint mPaint = new Paint();

    // 圆的半径
    private final int r = 80;

    // 圆的颜色
    private int color = 0;

    // 是否初始
    private boolean isFirst = true;

    private int mCount = 40;   // 小球个数
 //   public Ball[] mBalls;   // 用来保存所有小球的数组

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public CircleView(Context context) {
        super(context);
    }


    public boolean onTouchEvent(MotionEvent event) {
        //获取到手指处的横坐标和纵坐标
        int x = (int) event.getX();
        int y = (int) event.getY();
        float distanceX = lastX - event.getRawX();
        float distanceY = lastY - event.getRawY();

        float nextY = y - distanceY;
        float nextX = x - distanceX;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                lastX = x;
                lastY = y;

                break;

            case MotionEvent.ACTION_MOVE:

//                //计算移动的距离
//                int offsetX = x - lastX;
//                int offsetY = y - lastY;

//                 不能移出屏幕
                if (nextY <= 0) {
                    Toast.makeText(getContext(), "nextY<=0", Toast.LENGTH_SHORT).show();
                    nextY = 0;
                } else if (nextY >= containerHeight) {
                    nextY = containerHeight;
                    Toast.makeText(getContext(), "nextY>", Toast.LENGTH_SHORT).show();
                }
                if (nextX <= 0){
                    nextX = 0;
                    Toast.makeText(getContext(), "nextX<=0", Toast.LENGTH_SHORT).show();
                }
                else if (nextX >= containerWidth){
                    nextX = containerWidth ;
                    Toast.makeText(getContext(), "nextX>", Toast.LENGTH_SHORT).show();
                }


                int offsetX=(int) (nextX - event.getRawX());
                int offsetY=(int) (nextY - event.getRawY());
                ((View)getParent()).scrollBy(-offsetX,-offsetY);


                break;

            case MotionEvent.ACTION_UP:

                if((nextX-containerWidth/2)*(nextX-containerWidth/2)+(nextY-containerHeight/2)*(nextY-containerHeight/2)<=120*120){
                    changeColor();
                    Toast.makeText(getContext(),"成功添加兴趣爱好",Toast.LENGTH_LONG).show();
                    invalidate();
                }
                else{
                    Toast.makeText(getContext(),"请拖动球球到头像\n添加个人兴趣爱好",Toast.LENGTH_LONG).show();
                }
                this.smoothScrollTo(0,0);

        }

        return true;
    }
    public void smoothScrollTo(int destX,int destY){
        int scrollX=getScrollX();
        int deltaX=destX-scrollX;
        //1000秒内滑向destX
        int scrollY=getScrollY();
        int deltaY=destY-scrollY;
        mScroller.startScroll(scrollX,scrollY,deltaX,deltaY,0);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            ((View) getParent()).scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            //通过不断的重绘不断的调用computeScroll方法
            invalidate();
        }

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height ;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = 160;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = 160;
        }

        setMeasuredDimension(width, height);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 设置颜色
        if (isFirst){
            mPaint.setColor(getResources().getColor(R.color.bubble_gray));
        }else {
            mPaint.setColor(color);
        }

        // 设置画笔的锯齿效果
        mPaint.setAntiAlias(true);

        // 绘制圆形
        canvas.drawCircle(r, r, r, mPaint);

    }
    public void changeColor() {
        // 随机颜色
//        Random random = new Random();
//        color = 0xff000000 | random.nextInt(0x00ffffff);
        //蓝色
        color=getResources().getColor(R.color.bubble_blue);
        isFirst = false;
    }
}