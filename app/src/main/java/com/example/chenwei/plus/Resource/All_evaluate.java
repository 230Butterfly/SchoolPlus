package com.example.chenwei.plus.Resource;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.chenwei.plus.MainActivity;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Tool.Recy_adapter;

import java.util.ArrayList;

public class All_evaluate extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private com.example.chenwei.plus.Tool.PagerSlidingTabStrip tabs;
    private Context context=this;
    private ArrayList<Reply_list> reply_lists =new ArrayList<>();
    private MyAdapter myAdapter;
    private RelativeLayout evaluate_down_back_btn,evaluate_down_message_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_evaluate);

        evaluate_down_back_btn=findViewById(R.id.evaluate_down_back_btn);
        evaluate_down_message_btn=findViewById(R.id.evaluate_down_message_btn);
        evaluate_down_message_btn.setOnClickListener(this);
        evaluate_down_back_btn.setOnClickListener(this);

        for(int i=0;i<30;i++){
            Reply_list replyList =new Reply_list(null,"我是你浩哥","        "+"这个其实真的不错啊",7,"1997.09.29");
            reply_lists.add(replyList);
        }
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        myAdapter= new MyAdapter(reply_lists,this);
        tabs = findViewById(R.id.evaluate_tabs);
        tabs.setIndicatorColor(Color.BLACK);
        tabs.setDividerColor(Color.WHITE);
        tabs.setShouldExpand(true);
        tabs.setUnderlineHeight(2);
        tabs.setTextSize(38);
        viewPager = findViewById(R.id.evaluate_viewPager);
        viewPager.setAdapter(new myPagerAdapter(((All_evaluate)context).getSupportFragmentManager()));
        tabs.setViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.evaluate_down_back_btn:
                finish();
                break;
            case R.id.evaluate_down_message_btn:
                break;
        }
    }

    class myPagerAdapter extends FragmentPagerAdapter {
        String[] title = { "好评", "中评", "差评" };
        All_evaluate_fragment evaluate_good_fragment;
        All_evaluate_fragment evaluate_bad_fragment;
        All_evaluate_fragment evaluate_soso_fragment;

        public myPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    evaluate_good_fragment = new All_evaluate_fragment(myAdapter,context);
                    return evaluate_good_fragment;
                case 1:
                    evaluate_soso_fragment = new All_evaluate_fragment(myAdapter,context);
                    return evaluate_soso_fragment;
                case 2:
                    evaluate_bad_fragment = new All_evaluate_fragment(myAdapter,context);
                    return evaluate_bad_fragment;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {

            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

    }

}
