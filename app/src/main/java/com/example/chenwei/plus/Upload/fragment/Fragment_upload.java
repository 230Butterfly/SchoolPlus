package com.example.chenwei.plus.Upload.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chenwei.plus.MainActivity;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Resource.Record_fragment;
import com.example.chenwei.plus.Upload.fragment.AllMainFragment;
import com.example.chenwei.plus.Upload.fragment.MyFragment;

/**
 * Created by Chenwei on 2018/8/6.
 *
 * 未使用
 */

public class Fragment_upload extends Fragment {
    //Page的数量
    ViewPager viewPager;
    com.example.chenwei.plus.Tool.PagerSlidingTabStrip tabs;
    Context context;

    public Fragment_upload(){

    }
    @SuppressLint("ValidFragment")
    public Fragment_upload(android.app.FragmentManager fragmentManager, Context context) {
        this.context =context;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_fragment,container,false);
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
        String[] title = { "全部", "分类"};
        AllMainFragment all_fragment;
        MyFragment my_fragment;
        Record_fragment record_fragment;

        public myPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    all_fragment = new AllMainFragment(context);
                    return all_fragment;
                case 1:
                    my_fragment = new MyFragment(context);
                    return my_fragment;

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

