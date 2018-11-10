package com.example.chenwei.plus.Near;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chenwei.plus.Home.SearchActivity;
import com.example.chenwei.plus.MainActivity;
import com.example.chenwei.plus.Messages.MessageList;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Tool.MyViewpage;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_near extends android.app.Fragment {


    private Context mContext;
    MyViewpage viewPager;
    com.example.chenwei.plus.Tool.PagerSlidingTabStrip tabs;
    PercentRelativeLayout near_pr_layout;
    PercentRelativeLayout near_message;
    private static ArrayList<Customer> customers = new ArrayList<Customer>();
    public Fragment_near() {

    }
    @SuppressLint("ValidFragment")
    public Fragment_near(android.app.FragmentManager fragmentManager, Context context) {
        mContext =context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.near_fragment,container,false);
        near_message=view.findViewById(R.id.near_message_btn);
        near_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MessageList.class);
                startActivity(i);
            }
        });
        near_pr_layout=view.findViewById(R.id.near_pr_search);
        near_pr_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),SearchActivity.class);
                startActivity(i);
            }
        });
        tabs = view.findViewById(R.id.near_tabs);
        tabs.setIndicatorColor(Color.BLACK);
        tabs.setDividerColor(Color.WHITE);
        tabs.setShouldExpand(true);
        tabs.setUnderlineHeight(2);
        tabs.setTextSize(38);
        viewPager =  view.findViewById(R.id.near_viewPager);
        viewPager.setScrollble(false);
        viewPager.setAdapter(new Fragment_near.myPagerAdapter(((MainActivity)mContext).getSupportFragmentManager()));
        tabs.setViewPager(viewPager);

        return view;
    }

    class myPagerAdapter extends FragmentPagerAdapter {
        String[] title = {"附近的人", "附近资源"};
        Map_fragment map_fragment;
        Near_resource_fragment near_resource_fragment;

        public myPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    map_fragment = new Map_fragment(mContext);
                    return map_fragment;
                case 1:
                    near_resource_fragment = new Near_resource_fragment(mContext);
                    return near_resource_fragment;
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
