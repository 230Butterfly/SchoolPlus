package com.example.chenwei.plus.Resource;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.chenwei.plus.R;


public class Record_fragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView day_title_btn;
    private TextView week_title_btn;
    private LinearLayout day;
    private LinearLayout week;
    private RadioButton day_btn;
    private RadioButton week_btn;
    private ScrollView record_scroll;
    private int d =0;
    private int w =0;
    private SwipeRefreshLayout mRefreshLayout;


    private Context mContext;

    public Record_fragment() {

    }
    @SuppressLint("ValidFragment")
    public Record_fragment(Context context){
        mContext=context;
    }




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_record,container,false);
        day_title_btn=view.findViewById(R.id.record_day_title_btn);
        week_title_btn =view.findViewById(R.id.record_week_title_btn);
        day =view.findViewById(R.id.record_day);
        week =view.findViewById(R.id.record_week);
        day_btn=view.findViewById(R.id.record_day_btn);
        week_btn=view.findViewById(R.id.record_week_btn);
        record_scroll =view.findViewById(R.id.record_scroll);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        intiaction();
        return view;
    }

    private void intiaction() {
        day_title_btn.setOnClickListener(this);
        week_title_btn.setOnClickListener(this);
        day_btn.setOnClickListener(this);
        week_btn.setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onCreate(null);
                mRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_day_title_btn:
                if(day_btn.isChecked()){
                    day_btn.setChecked(false);
                    d=0;
                    hide(day);
                }else{
                    day_btn.setChecked(true);
                    d=1;
                    show(day,1);
                }
                break;
            case R.id.record_week_title_btn:
                if(week_btn.isChecked()){
                    week_btn.setChecked(false);
                    w=0;
                    hide(week);
                }else {
                    week_btn.setChecked(true);
                    w=1;
                    show(week,2);
                }
                break;
            case R.id.record_day_btn:
                if(d==0){
                    day_btn.setChecked(true);
                    d=1;
                    show(day,1);
                }else {
                    day_btn.setChecked(false);
                    d=0;
                    hide(day);
                }
                break;
            case R.id.record_week_btn:
                if(w==0){
                    week_btn.setChecked(true);
                    w=1;
                    show(week,2);
                }else {
                    week_btn.setChecked(false);
                    w=0;
                    hide(week);
                }
                break;
        }
    }

    private void hide(LinearLayout v) {
        record_scroll.scrollTo(0,0);
        int m =v.getChildCount();
        for(int i=2;i<m;i++){
            v.removeViewAt(2);
        }
    }

    private void show(LinearLayout v,int c) {
        openchoose(v);
        for(int i=0;i<2;i++){
            LinearLayout lv =new LinearLayout(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(i==0){
                layoutParams.setMargins(0, 10, 0, 0);//4个参数按顺序分别是左上右下
            }else{
                layoutParams.setMargins(0, 0  , 0, 0);//4个参数按顺序分别是左上右下
            }
            lv.setLayoutParams(layoutParams);
            lv.setOrientation(LinearLayout.HORIZONTAL);
            ImageView imageView =new ImageView(mContext);
            imageView.setBackground(getResources().getDrawable(R.mipmap.upload));
            lv.addView(imageView);
            ViewGroup.LayoutParams para;
            para = imageView.getLayoutParams();
            para.height = 200;
            para.width = 200;
            imageView.setLayoutParams(para);
            LinearLayout information =new LinearLayout(mContext);
            information.setOrientation(LinearLayout.VERTICAL);
            LinearLayout title =new LinearLayout(mContext);
            title.setOrientation(LinearLayout.HORIZONTAL);
            TextView name =new TextView(mContext);
            name.setText("高等数学");
            name.setTextSize(20);
            name.setHeight(50);
            name.setWidth(300);
            TextView time =new TextView(mContext);
            time.setText("1997.7.23");
            time.setGravity(View.TEXT_ALIGNMENT_CENTER);
            name.setHeight(100);
            title.addView(name);
            title.addView(time);
            final LinearLayout details =new LinearLayout(mContext);
            details.setOrientation(LinearLayout.HORIZONTAL);
            TextView black=new TextView(mContext);
            black.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            TextView size =new TextView(mContext);
            if(c==1){
                size.setText("回应方："+"我是你浩哥");
            }else {
                size.setText("请求方："+"我是你浩哥");
            }
            size.setPadding(40,0,0,0);
            TextView price =new TextView(mContext);
            price.setText("积分" + ":30");
            details.setPadding(0,0,0,0);
            ImageView imageView1=new ImageView(mContext);
            imageView1.setBackground(getResources().getDrawable(R.mipmap.person_active));
            price.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            imageView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            size.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            details.addView(price);
            details.addView(black);
            details.addView(size);
            details.addView(imageView1);
            information.addView(title);
            information.addView(details);
            information.setPadding(40,20,20,20);
            lv.addView(information);
            lv.setPadding(70,20,20,20);
            lv.setBackground(getResources().getDrawable(R.color.real_white));
            View view=new View(mContext);
            view.setBackground(getResources().getDrawable(R.color.div_white));
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
            layoutParams1.setMargins(0,0,0,10);
            view.setLayoutParams(layoutParams1);
            v.addView(lv);
            v.addView(view);
        }
    }

    private void openchoose(LinearLayout v) {
        if(day_btn.isChecked()){
            if(!v.equals(day)){
                day_btn.setChecked(false);
                d=0;
                hide(day);
            }
        }
        if(week_btn.isChecked()){
            if(!v.equals(week)){
                week_btn.setChecked(false);
                w=0;
                hide(week);
            }
        }
    }
}
