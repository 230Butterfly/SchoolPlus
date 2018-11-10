package com.example.chenwei.plus.Near;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Resource.Detail_resource;

import java.util.ArrayList;

/**
 * Created by Chenwei on 2018/8/13.
 */

public class Near_label_win extends PopupWindow implements View.OnClickListener {

    private ArrayList<String> label_now_list;
    private ArrayList<String> label_all_list;

     private Near_resource_fragment near_resource_fragment;
     private final Context mContext;
     private View view;
     private LinearLayout near_label_choose;
     private Button near_label_cancel_btn,near_label_confirm_btn;
     Near_label_win(ArrayList<String> label_list, final Context mContext, Near_resource_fragment context, View.OnClickListener itemsOnClick) {

         near_resource_fragment=context;
        this.label_now_list=label_list;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.near_label_win, null);

        this.mContext=mContext;
         label_all_list = new ArrayList<>();
         label_all_list.add("教育");
         label_all_list.add("娱乐");
         label_all_list.add("图片");
         label_all_list.add("视频");
         label_all_list.add("数学");
         label_all_list.add("学习");
         label_all_list.add("健身");
         label_all_list.add("孩童");
         label_all_list.add("视频");
         label_all_list.add("书籍");
         label_all_list.add("程序");
         label_all_list.add("益脑");



         near_label_cancel_btn =view.findViewById(R.id.near_label_cancel_btn);
         near_label_confirm_btn=view.findViewById(R.id.near_label_confirm_btn);
         near_label_cancel_btn.setOnClickListener(this);
         near_label_confirm_btn.setOnClickListener(this);
        //现有标签
         near_label_choose=view.findViewById(R.id.near_label_choose);



         int m=(label_all_list.size())/6;
         int number=0;
         for(int j=0;j<m+1;j++){
             LinearLayout linearLayout_all =new LinearLayout(mContext);
             linearLayout_all.setOrientation(LinearLayout.HORIZONTAL);
             LinearLayout.LayoutParams layoutParams_all = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
             layoutParams_all.setMargins(25, 0, 25, 10);//4个参数按顺序分别是左上右下
             linearLayout_all.setLayoutParams(layoutParams_all);
             for(int i=0;i<6;i++){
                 LinearLayout label_lin =new LinearLayout(mContext);
                 label_lin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                 if(number<label_all_list.size()){
                     final Button button=new Button(mContext);
                     button.setText(label_all_list.get(number));
                     button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                     button.setHeight(20);
                     button.setTextColor(Color.parseColor("#4F4F4F"));
                     int state =0;
                     for(int o=0;o<label_now_list.size();o++){
                         if(label_now_list.get(o).equals(button.getText())){
                             state=1;
                             break;
                         }
                     }
                     if(state==1){
                         number++;
                         i--;
                         continue;
                     } else {
                         number++;
                     }
                     button.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             for(int j=0;j<label_now_list.size();j++){
                                 if(label_now_list.get(j).equals(button.getText())){
                                     button.setTextColor(Color.parseColor("#4F4F4F"));
                                     label_now_list.remove(j);
                                     return;
                                 }
                             }
                             button.setTextColor(Color.parseColor("#FF0000"));
                             label_now_list.add((String) button.getText());
                         }
                     });
                     label_lin.addView(button);
                 }
                 linearLayout_all.addView(label_lin);
             }
             near_label_choose.addView(linearLayout_all);
         }

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {

            int height = view.findViewById(R.id.near_pop_layout).getTop();

            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
            if (y < height) {
                ((Near_resource_fragment)near_resource_fragment).setLabel_now_list(1,label_now_list);
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

@Override
       public void onClick(View v) {
       switch (v.getId()){
           case R.id.near_label_cancel_btn:
               ((Near_resource_fragment)near_resource_fragment).setLabel_now_list(1,label_now_list);
               dismiss();
               break;
           case R.id.near_label_confirm_btn:
               Toast.makeText(mContext,"筛选成功",Toast.LENGTH_SHORT).show();
               ((Near_resource_fragment)near_resource_fragment).setLabel_now_list(2,label_now_list);
               dismiss();
               break;

           }
       }

    /*public void label_enough(){
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //    设置Title的图标
        builder.setIcon(R.mipmap.person_active);
        //    设置Title的内容
        builder.setTitle("标签过多");
        //    设置Content来显示一个信息
        builder.setMessage("        为了用户有更好的体验，每个资源至多有6个标签！（为您带来不便，敬请谅解）");
        //    设置一个PositiveButton
        builder.setPositiveButton("好的", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
               // Toast.makeText(mContext,"该资源成功下架", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }*/


}