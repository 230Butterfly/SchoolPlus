package com.example.chenwei.plus.Home;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chenwei.plus.MainActivity;
import com.example.chenwei.plus.Messages.MessageList;
import com.example.chenwei.plus.Near.Customer;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Tool.Near_resource_information;
import com.example.chenwei.plus.Upload.bean.ResourceUpload;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Chenwei on 2018/8/6.
 */

public class Fragment_home extends Fragment{
    /**
     * 顶部固定的TabViewLayout
     */
    private LinearLayout mTopTabViewLayout;
    /**
     * 跟随ScrollView的TabviewLayout
     */
    private LinearLayout mTabViewLayout;
    /**
     * 要悬浮在顶部的View的子View
     */
    private LinearLayout mTopView;
    //以下是首页上方导航的变量定义
    private View view1;
    private View view2;
    private ViewPager viewPager_tool;
    private ArrayList<View> pageView;
    //用来存放圆点。
    private ImageView[] tips = new ImageView[2];
    private ImageView imageView;
    //圆点组的对象
    private ViewGroup group;

    //以下是首页下方内容的变量定义
    private ViewPager vp;
    private com.example.chenwei.plus.Tool.PagerSlidingTabStrip tabs;
    private Context context;

    private ArrayList<Near_resource_information> posts;
    private ArrayList<Near_resource_information> posts1;
    private ArrayList<Near_resource_information> posts2;

    private GridView gv;
    private String[] first_recommend={"教育","视频","书籍","音乐","娱乐","体育","应用","图片"};
    private String[] second_recommend={"理科","工科","文科","计算机","游戏","PPT","EXCEL","WORD"};
    private int[] first_resIds={R.mipmap.edu,R.mipmap.video,R.mipmap.book,R.mipmap.music,R.mipmap.entertainment,R.mipmap.pe,R.mipmap.app,R.mipmap.picture};
    private int[] second_resIds={R.mipmap.science,R.mipmap.engineering,R.mipmap.liberal,R.mipmap.computer,R.mipmap.game,R.mipmap.ppt,R.mipmap.excel,R.mipmap.word};
    Fragment_near_home near;
    Fragment_near_home near1;
    Fragment_near_home near2;
    PercentRelativeLayout rl_message;
    private String[] string={"教育","视频","书籍","音乐","娱乐","体育","应用","图片"};
    private String[] string2={"理科","工科","文科","计算机","游戏","PPT","EXCEL","WORD"};
    PercentRelativeLayout pr_layout;

    public Fragment_home() {
    }

    @SuppressLint("ValidFragment")
    public Fragment_home(Context context) {
        this.context = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final MyScrollView mMyScrollView = (MyScrollView) view.findViewById(R.id.my_scrollview);
        mTabViewLayout = (LinearLayout) view.findViewById(R.id.ll_tabView);
        mTopTabViewLayout = (LinearLayout) view.findViewById(R.id.ll_tabTopView);
        mTopView = (LinearLayout) view.findViewById(R.id.tv_topView);
        pr_layout=view.findViewById(R.id.pr_search);
        rl_message = view.findViewById(R.id.message_btn);
        rl_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MessageList.class);
                startActivity(i);
            }
        });

        posts=new ArrayList<Near_resource_information>();
        posts1=new ArrayList<Near_resource_information>();
        posts2=new ArrayList<Near_resource_information>();
        vp = view.findViewById(R.id.viewPager_home);
        BmobQuery<ResourceUpload> comment_query=new BmobQuery<ResourceUpload>();
        comment_query.addWhereGreaterThan("comment", -1);
//返回50条数据，如
        comment_query.findObjects(new FindListener<ResourceUpload>() {
            @Override
            public void done(List<ResourceUpload> list, BmobException e) {
                if (e==null) {
                    Log.v("查询成功：共" + list.size() + "条数据。", "1");
                    for (ResourceUpload p : list) {
                        Near_resource_information near_resource_information = new Near_resource_information(p.getUser(), p.getName(), p.getComment(), p.getInterger(), 10000, p.getLabel(), p.getCreatedAt(), p.getSize(), p.getIntroduction(), p.getPath(),p.getUsername(),p.getFile().getFileUrl());
                        posts.add(near_resource_information);
                        posts1.add(near_resource_information);
                        posts2.add(near_resource_information);

                    }
                    near = new Fragment_near_home(posts);
                    near1 = new Fragment_near_home(posts1);
                    near2 = new Fragment_near_home(posts2);
                    vp.setAdapter(new myPagerAdapter(((MainActivity) context).getSupportFragmentManager()));
                    tabs.setViewPager(vp);
                }
            }
        });

        pr_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),SearchActivity.class);
                startActivity(i);
            }
        });
        tabs = view.findViewById(R.id.tabs);
        tabs.setIndicatorColor(Color.BLACK);
        tabs.setBackgroundColor(Color.WHITE);
        tabs.setDividerColor(Color.WHITE);
        tabs.setShouldExpand(true);
        tabs.setUnderlineHeight(2);
        tabs.setTextSize(38);


        //将view加进pageview中
        viewPager_tool = (ViewPager)view.findViewById(R.id.viewPager_tool);
        view1 = inflater.inflate(R.layout.home_view_1, container, false);
        gv=(GridView) view1.findViewById(R.id.gridView);
        gv.setAdapter(new GridViewAdapter(first_resIds,first_recommend,getActivity()));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(getActivity(),"Item"+position,Toast.LENGTH_SHORT).show();
                Intent i= new Intent();
                i.putExtra("title",string[position]);
                i.putExtra("date",posts);
                i.setClass(getActivity(),ClassifyActivity.class);
                startActivity(i);

            }
        });
        view2 = inflater.inflate(R.layout.home_view_1, container, false);
        GridView gv1 = (GridView) view2.findViewById(R.id.gridView);
        gv1.setAdapter(new GridViewAdapter(second_resIds,second_recommend,getActivity()));
        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getActivity(),"Item"+position,Toast.LENGTH_SHORT).show();
                Intent i= new Intent();
                i.putExtra("title",string2[position]);
                i.putExtra("date",posts);
                i.setClass(getActivity(),ClassifyActivity.class);
                startActivity(i);

            }
        });

        pageView = new ArrayList<View>();

        pageView.add(view1);
        pageView.add(view2);
        //viewPager下面的圆点，ViewGroup
        group = (ViewGroup)view.findViewById(R.id.viewGroup);
        tips = new ImageView[pageView.size()];
        for(int i =0;i<pageView.size();i++){
            imageView = new ImageView(getActivity());
            ViewGroup.MarginLayoutParams layoutParams= new ViewGroup.MarginLayoutParams(20,20);
            layoutParams.setMargins(15,0,0,0);
            //imageView.setLayoutParams(new ViewGroup.LayoutParams(30,30));
            imageView.setLayoutParams(layoutParams);
            imageView.setPadding(140, 0, 140, 0);

            tips[i] = imageView;

            //默认第一张图显示为选中状态
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.select_active);
            } else {
                tips[i].setBackgroundResource(R.drawable.select_normal);
            }

            group.addView(tips[i]);
        }
        //这里的mypagerAdapter是第三步定义好的。
        viewPager_tool.setAdapter(new pagerAdapter(pageView));
        //这里的GuiPageChangeListener是第四步定义好的。
        viewPager_tool.addOnPageChangeListener(new GuidePageChangeListener());

        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.resetHeight(0);*/
        return view;
    }
    /*
    implements AdapterView.OnItemClickListener
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }*/

    class myPagerAdapter extends FragmentPagerAdapter {
        String[] title = {"附近", "热门", "最新"};
        public myPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    //near = new Fragment_near(posts);
                    return near;
                case 1:
                    //near1 = new Fragment_near(posts);
                    return near1;
                case 2:
                    //near2 = new Fragment_near(posts);
                    return near2;

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


    class pagerAdapter extends PagerAdapter {
        private ArrayList<View> pageview1;
        public pagerAdapter(ArrayList<View> pageview1){
            super();
            //super(pageview1);
            this.pageview1 = pageview1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.d("MainActivityDestroy",position+"");
            if (pageview1.get(position)!=null) {
                container.removeView(pageview1.get(position));
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pageview1.get(position));
            Log.d("MainActivityInstanti",position+"");
            return pageview1.get(position);
        }

        @Override
        public int getCount() {
            return pageview1.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object==view;
        }
    }

    class GuidePageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }


        @Override
        //切换view时，下方圆点的变化。
        public void onPageSelected(int position) {
            tips[position].setBackgroundResource(R.drawable.select_active);
            //这个图片就是选中的view的圆点
            for(int i=0;i<pageView.size();i++){
                if (position != i) {
                    tips[i].setBackgroundResource(R.drawable.select_normal);
                    //这个图片是未选中view的圆点
                }
            }
        }
    }

}
