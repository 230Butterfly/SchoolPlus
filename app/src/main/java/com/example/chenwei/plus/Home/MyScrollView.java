package com.example.chenwei.plus.Home;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.example.chenwei.plus.R;

public class MyScrollView extends LinearLayout implements NestedScrollingParent {
    private static final String TAG="MyScrollView";
    private OnScrollListener mOnScrollListener;
    /*
    public MyScrollView(Context context) {
        super(context);
    }
    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scaledTouchSlop=ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTop = findViewById(R.id.rl_top);
        mTopViewHeight=mTop.getMeasuredHeight();
    }*/
/*
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
            xDistance = yDistance = 0f;
            xLast = ev.getX();
            yLast = ev.getY();
            break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                Log.e("SiberiaDante", "xDistance ：" + xDistance + "---yDistance:" + yDistance);
                return !(xDistance > yDistance || yDistance < scaledTouchSlop) && isNeedScroll;
                //return isNeedScroll;
        }
        return super.onInterceptTouchEvent(ev);
    }*/
@Override
public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes)
{
    Log.e(TAG, "onStartNestedScroll");
    return true;
}

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes)
    {
        Log.e(TAG, "onNestedScrollAccepted");
    }

    @Override
    public void onStopNestedScroll(View target)
    {
        Log.e(TAG, "onStopNestedScroll");
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed)
    {
        Log.e(TAG, "onNestedScroll");
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed)
    {
        Log.e(TAG, "onNestedPreScroll");
        boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight;
        boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenTop || showTop)
        {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    private int TOP_CHILD_FLING_THRESHOLD = 3;
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed)
    {

        //如果是recyclerView 根据判断第一个元素是哪个位置可以判断是否消耗
        //这里判断如果第一个元素的位置是大于TOP_CHILD_FLING_THRESHOLD的
        //认为已经被消耗，在animateScroll里不会对velocityY<0时做处理
        if (target instanceof RecyclerView && velocityY < 0) {
            final RecyclerView recyclerView = (RecyclerView) target;
            final View firstChild = recyclerView.getChildAt(0);
            final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
            consumed = childAdapterPosition > TOP_CHILD_FLING_THRESHOLD;
        }
        if (!consumed) {
            animateScroll(velocityY, computeDuration(0),consumed);
        } else {
            animateScroll(velocityY, computeDuration(velocityY),consumed);
        }
        return true;

    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY)
    {
        //不做拦截 可以传递给子View

        return false;
    }

    @Override
    public int getNestedScrollAxes()
    {
        Log.e(TAG, "getNestedScrollAxes");
        return 0;
    }

    /**
     * 根据速度计算滚动动画持续时间
     * @param velocityY
     * @return
     */
    private int computeDuration(float velocityY) {
        final int distance;
        if (velocityY > 0) {
            distance = Math.abs(mTop.getHeight() - getScrollY());
        } else {
            distance = Math.abs(mTop.getHeight() - (mTop.getHeight() - getScrollY()));
        }


        final int duration;
        velocityY = Math.abs(velocityY);
        if (velocityY > 0) {
            duration = 3 * Math.round(1000 * (distance / velocityY));
        } else {
            final float distanceRatio = (float) distance / getHeight();
            duration = (int) ((distanceRatio + 1) * 150);
        }

        return duration;

    }

    private void animateScroll(float velocityY, final int duration,boolean consumed) {
        final int currentOffset = getScrollY();
        final int topHeight = mTop.getHeight();
        if (mOffsetAnimator == null) {
            mOffsetAnimator = new ValueAnimator();
            mOffsetAnimator.setInterpolator(mInterpolator);
            mOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (animation.getAnimatedValue() instanceof Integer) {
                        scrollTo(0, (Integer) animation.getAnimatedValue());
                    }
                }
            });
        } else {
            mOffsetAnimator.cancel();
        }
        mOffsetAnimator.setDuration(Math.min(duration, 600));

        if (velocityY >= 0) {
            mOffsetAnimator.setIntValues(currentOffset, topHeight);
            mOffsetAnimator.start();
        }else {
            //如果子View没有消耗down事件 那么就让自身滑倒0位置
            if(!consumed){
                mOffsetAnimator.setIntValues(currentOffset, 0);
                mOffsetAnimator.start();
            }

        }
    }

    private View mTop;
    private View mNav;
    private ViewPager mViewPager;
    //private CustomViewPager mViewPager;

    private int mTopViewHeight;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private ValueAnimator mOffsetAnimator;
    private Interpolator mInterpolator;
    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;

    private float mLastY;
    private boolean mDragging;

    public MyScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        mScroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context)
                .getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context)
                .getScaledMinimumFlingVelocity();

    }
    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mTop = findViewById(R.id.rl_top);
        mNav = findViewById(R.id.tabs);
        View view = findViewById(R.id.viewPager_home);
        if (!(view instanceof ViewPager))
        {
            throw new RuntimeException(
                    "id_stickynavlayout_viewpager show used by ViewPager !");
        }
        mViewPager = (ViewPager) view;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        //不限制顶部的高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        //params.height = getMeasuredHeight() - mNav.getMeasuredHeight();
        params.height=getMeasuredHeight()-mNav.getMeasuredHeight()-mNav.getMeasuredHeight() -mNav.getMeasuredHeight()/3;
        setMeasuredDimension(getMeasuredWidth(), mTop.getMeasuredHeight() + mNav.getMeasuredHeight() + mViewPager.getMeasuredHeight());

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTop.getMeasuredHeight();
    }
    @Override
    public void scrollTo(int x, int y)
    {
        if (y < 0)
        {
            y = 0;
        }
        if (y > mTopViewHeight)
        {
            y = mTopViewHeight;
        }
        if (y != getScrollY())
        {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset())
        {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }
    /**
     * 监听ScroView的滑动情况
     *
     * @param l    变化后的X轴位置
     * @param t    变化后的Y轴的位置
     * @param oldl 原先的X轴的位置
     * @param oldt 原先的Y轴的位置
     */


    @Override

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != mOnScrollListener) {
            mOnScrollListener.onScroll(t);
        }
    }
    /**
     * 设置滚动接口
     *
     * @param listener
     */

    public void setOnScrollListener(OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }

    @SuppressLint("RestrictedApi")
    public int getcomputeVerticalScrollRange() {
        return this.computeVerticalScrollRange();
    }

    /**
     * 滚动的回调接口
     */

    public interface OnScrollListener {
        /**
         * MyScrollView滑动的Y方向距离变化时的回调方法
         *
         * @param scrollY
         */
        void onScroll(int scrollY);

    }

}
