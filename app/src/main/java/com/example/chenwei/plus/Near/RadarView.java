package com.example.chenwei.plus.Near;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.chenwei.plus.R;

/**
 * Created by Chenwei on 2018/9/21.
 */

public class RadarView extends View {
    /**
     * 思路：我们首先初始化画笔，并且获取到控件的宽高，在onMeasure()中设置铺满，然后在onDraw()方法中绘制四个静态圆和一个渐变圆，
     * 我们通过Matrix矩阵来让他不停的旋转就达到我们想要的效果了
     */
    private Paint mPaintLine, mPaintCircle;
    private int w, h;
    // 动画
    private Matrix matrix;
    // 旋转角度
    private int start;
    // Handler定时动画
    private Handler handler = new Handler();
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            start = start + 1;
            matrix = new Matrix();
// 参数：旋转角度，围绕点坐标的x,y坐标点
            matrix.postRotate(start, w / 2, h / 2);
// 刷新重绘
            RadarView.this.invalidate();
// 继续循环
            handler.postDelayed(run, 30);
        }
    };
    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
// 获取高宽
        w =700;
        h = 700;
// 一致旋转
        handler.post(run);
    }
    private void initView() {
        mPaintLine = new Paint();
        mPaintLine.setColor(Color.WHITE);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Style.STROKE);
        mPaintCircle = new Paint();
        mPaintCircle.setColor(Color.BLUE);
        mPaintCircle.setAntiAlias(true);
        matrix = new Matrix();
    }
    /**
     * 测量
     *
     * @author LGL
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
// 设置铺满
        setMeasuredDimension(w, h);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
// 画四个圆形
        canvas.drawCircle(w / 2, h / 2, w / 2, mPaintLine);
        canvas.drawCircle(w / 2, h / 2, w / 3, mPaintLine);
        canvas.drawCircle(w / 2, h / 2, w * 7 / 10, mPaintLine);
        canvas.drawCircle(w / 2, h / 2, w / 4, mPaintLine);
// 绘制渐变圆
        Shader mShader = new SweepGradient(w / 2, h / 2, Color.TRANSPARENT,
                Color.parseColor("#1296db"));
// 绘制时渐变
        mPaintCircle.setShader(mShader);
// 增加旋转动画，使用矩阵实现
        canvas.concat(matrix); // 前置动画
        canvas.drawCircle(w / 2, h / 2, w * 7 / 10, mPaintCircle);
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

}