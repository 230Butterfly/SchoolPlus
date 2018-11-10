package com.example.chenwei.plus.Resource;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.example.chenwei.plus.MainActivity;
import com.example.chenwei.plus.Messages.MessageList;
import com.example.chenwei.plus.R;

/**
 * Created by Chenwei on 2018/8/6.
 */

public class Fragment_resource extends Fragment {
     //Page的数量
    ViewPager viewPager;
    com.example.chenwei.plus.Tool.PagerSlidingTabStrip tabs;
    Context context;
    PercentRelativeLayout resource_message_btn;


    public Fragment_resource(){

    }
    @SuppressLint("ValidFragment")
    public Fragment_resource(android.app.FragmentManager fragmentManager, Context context) {
            this.context =context;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resource_fragment,container,false);
        resource_message_btn =view.findViewById(R.id.resource_message_btn);
        resource_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MessageList.class);
                startActivity(i);
            }
        });
        tabs = view.findViewById(R.id.tabs);
        tabs.setIndicatorColor(Color.BLACK);
        tabs.setDividerColor(Color.WHITE);
        tabs.setShouldExpand(true);
        tabs.setUnderlineHeight(2);
        tabs.setTextSize(38);
        viewPager = (ViewPager) view.findViewById(R.id.resource_viewPager);
        viewPager.setAdapter(new myPagerAdapter(((MainActivity)context).getSupportFragmentManager()));
        tabs.setViewPager(viewPager);

        return view;
    }

    class myPagerAdapter extends FragmentPagerAdapter {
        String[] title = { "我的上传", "我的下载", "传输记录" };
        Myupload_fragment myupload_fragment;
        Mydownload_fragment mydownload_fragment;
        Record_fragment record_fragment;

        public myPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    myupload_fragment = new Myupload_fragment(context);
                    return myupload_fragment;
                case 1:
                    mydownload_fragment = new Mydownload_fragment(context);
                    return mydownload_fragment;
                case 2:
                    record_fragment = new Record_fragment(context);
                    return record_fragment;

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

        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }

    }


}

