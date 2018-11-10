package com.example.chenwei.plus.Resource;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chenwei.plus.R;

/**
 * Created by Chenwei on 2018/8/14.
 */

public class Evaluate_win extends PopupWindow implements View.OnClickListener {

    private Context mContext;

    private View view;
    private TextWatcher name_watcher, integral_watcher, intro_watcher;
    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private EditText name_et, integral_et, intor_et;
    private LinearLayout picture_btn, resource_change_btn;
    private Button confirm_btn,delete_btn;
    private RatingBar ratingBar;//星级评分条

    Evaluate_win(Button c,final Context mContext, View.OnClickListener itemsOnClick) {

        this.mContext = mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.win_evaluate, null);
        delete_btn=c;
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar1);
        Button button = (Button) view.findViewById(R.id.down_label_confirm_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * getRating():用于获取等级，表示选中的几颗星
                 * getStepSize():用语获取每次至少要改变多少个星级
                 * getProgress():用语获取进度，获取到的进度值为getRating()方法返回值与getStepSize()方法返回值之积
                 */
                int result = ratingBar.getProgress();
                float rating = ratingBar.getRating();
                float step = ratingBar.getStepSize();
                Log.e("星级评分条","step="+step+"result="+result+"rating="+rating);
                Toast.makeText(mContext,"您评了"+rating+"颗星",Toast.LENGTH_SHORT).show();
                delete_btn.setText("删除资源");
                dismiss();
            }
        });

        /*name_et = view.findViewById(R.id.name_et);
        integral_et = view.findViewById(R.id.integral_et);
        intor_et = view.findViewById(R.id.intro_et);
        btn_cancel = (Button) view.findViewById(R.id.cancel_btn);
        picture_btn = view.findViewById(R.id.picture_btn);
        resource_change_btn = view.findViewById(R.id.resource_change_btn);
        confirm_btn = view.findViewById(R.id.confirm_btn);
        picture_btn.setOnClickListener(itemsOnClick);
        resource_change_btn.setOnClickListener(itemsOnClick);
        confirm_btn.setOnClickListener(itemsOnClick);
        // 取消按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });
        // 设置按钮监听
        name_et.setOnClickListener(this);
        integral_et.setOnClickListener(this);
        intor_et.setOnClickListener(this);
        name_et.addTextChangedListener(name_watcher);
        integral_et.addTextChangedListener(integral_watcher);
        intor_et.addTextChangedListener(intro_watcher);*/

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);

    }
    public void onClick(View v) {
       /* switch (v.getId()){
            case R.id.integral_et:
                integral_et.setCursorVisible(true);
                break;
            case R.id.intro_et:
                intor_et.setCursorVisible(true);
                break;
            case R.id.name_et:
                name_et.setCursorVisible(true);
                break;
        }*/
    }

}