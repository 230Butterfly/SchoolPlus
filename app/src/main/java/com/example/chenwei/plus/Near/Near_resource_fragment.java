package com.example.chenwei.plus.Near;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.example.chenwei.plus.MainActivity;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Resource.Compile_win;
import com.example.chenwei.plus.Resource.Detail_resource;
import com.example.chenwei.plus.Resource.Label_win;
import com.example.chenwei.plus.Resource.MyAdapter;
import com.example.chenwei.plus.Share;
import com.example.chenwei.plus.Tool.Near_resource_adapter;
import com.example.chenwei.plus.Tool.Near_resource_information;
import com.example.chenwei.plus.Tool.Recy_adapter;
import com.example.chenwei.plus.Upload.bean.ResourceUpload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Near_resource_fragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private LinearLayout distance_btn,heat_btn,screen_btn;
    private ImageView distance_up,distance_down,heat_up,heat_down,screen_open;
    private Near_resource_adapter recy_adapter;
    private RecyclerView re;
    private int distance_state=0,heat_state=0,screen_state=0;
    private ArrayList<Near_resource_information> reply_lists =new ArrayList<>();
    private ArrayList<String> label_now_list =new ArrayList<>();
    private Near_label_win near_label_win;
    private View rootview;
    private Near_resource_fragment context=this;
    private static ArrayList<Customer> customers = new ArrayList<Customer>();
    private static ArrayList<ResourceUpload> resourceUploads = new ArrayList<ResourceUpload>();
    public Near_resource_fragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public Near_resource_fragment(Context context){
        mContext=context;
    }
    private  View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_near_resource,container,false);
        ((Share)getApplicationContext()).setSfragment(this);
        distance_down=view.findViewById(R.id.distance_down);
        distance_up=view.findViewById(R.id.distance_up);
        heat_down=view.findViewById(R.id.heat_down);
        heat_up=view.findViewById(R.id.heat_up);
        screen_open=view.findViewById(R.id.screen_open);

        distance_btn=view.findViewById(R.id.distance_btn);
        heat_btn=view.findViewById(R.id.heat_btn);
        screen_btn=view.findViewById(R.id.screen_btn);
        distance_btn.setOnClickListener(this);
        heat_btn.setOnClickListener(this);
        screen_btn.setOnClickListener(this);

        return view;
    }

    public void show(){
        reply_lists.clear();
        resourceUploads.clear();
        customers =((MainActivity)mContext).getcustomer();
        if (!customers.isEmpty()){
            final int[] m = {0};
            for (Customer p:customers){
                Log.v("customers111",p.getObjectId()+"shuliang");
                BmobQuery<ResourceUpload> comment_query=new BmobQuery<ResourceUpload>();
                comment_query.addWhereEqualTo("user",p.getObjectId());
//返回50条数据，如
                comment_query.findObjects(new FindListener<ResourceUpload>() {
                    @Override
                    public void done(List<ResourceUpload> list, BmobException e) {
                        if (e==null){
                            Log.v("查询成功：共"+list.size()+"条数据。","1");
                            for (ResourceUpload gameScore : list) {
                                resourceUploads.add((ResourceUpload) gameScore);
                            }
                            m[0] = m[0]+1;
                            Log.v(String.valueOf(m[0]),String.valueOf(customers.size())+"shuliang");
                            if (m[0]==customers.size()) {
                                for (Customer p : customers) {
                                    for (ResourceUpload w : resourceUploads) {
                                        if (w.getUser().equals(p.getObjectId())) {
                                            Log.v("查询成功",w.getName());
                                            Near_resource_information near_resource_information = new Near_resource_information(p.getUsername(), w.getName(), w.getComment(), w.getInterger(), (int) p.getDistance(),w.getLabel(),w.getCreatedAt(),w.getSize(),w.getIntroduction(),w.getPath(),w.getUsername(),w.getFile().getFileUrl());
                                            reply_lists.add(near_resource_information);
                                        }
                                    }
                                }
                                donext(view);
                            }
                        }else {
                            Log.v("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });

            }
        }
        else {
            for(int i=0;i<30;i++){
                Near_resource_information near_resource_information=new Near_resource_information("我是你浩哥","高等数学第三章ppt",4,5,5,"暂无标签","","","","","","");
                reply_lists.add(near_resource_information);
                donext(view);
            }
        }
    }

    private void donext(View view){
        ((Share)getApplicationContext()).setReply_lists(reply_lists);
        ((Share)getApplicationContext()).setCustomers(customers);
        Log.v(String.valueOf(reply_lists.size()),String.valueOf(customers.size())+"shuliang");
        LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recy_adapter=new Near_resource_adapter(reply_lists,mContext);
        recy_adapter.setOnItemClickListener(new Near_resource_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int Position) {
                reply_lists.get(Position);
                Intent m=new Intent(mContext,Near_resource_detail.class);
                m.putExtra("date", (Serializable) reply_lists.get(Position));
                m.putExtra("all",reply_lists);
                startActivity(m);
            }
        });
        re=view.findViewById(R.id.near_re_list);
        re.setLayoutManager(layoutManager);
        re.setAdapter(recy_adapter);

        near_label_win = new Near_label_win(label_now_list,mContext,context, onClickListener);
        //showAtLocation(View parent, int gravity, int x, int y)
        rootview = LayoutInflater.from(mContext).inflate(R.layout.activity_main, null);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.distance_btn:
                if(distance_state==0){
                    distance_up.setBackground(getResources().getDrawable(R.drawable.up_selected));
                    distance_state=1;
                    re.scrollToPosition(0);
                }else if(distance_state==1){
                    distance_up.setBackground(getResources().getDrawable(R.drawable.up_unselected));
                    distance_down.setBackground(getResources().getDrawable(R.drawable.down_selected));
                    distance_state=2;
                    re.scrollToPosition(0);
                }else {
                    distance_down.setBackground(getResources().getDrawable(R.drawable.down_unselected));
                    distance_state=0;
                    re.scrollToPosition(0);
                }
                break;
            case R.id.heat_btn:
                if(heat_state==0){
                    heat_up.setBackground(getResources().getDrawable(R.drawable.up_selected));
                    heat_state=1;
                    re.scrollToPosition(0);
                }else if(heat_state==1){
                    heat_up.setBackground(getResources().getDrawable(R.drawable.up_unselected));
                    heat_down.setBackground(getResources().getDrawable(R.drawable.down_selected));
                    heat_state=2;
                    re.scrollToPosition(0);
                }else {
                    heat_down.setBackground(getResources().getDrawable(R.drawable.down_unselected));
                    heat_state=0;
                    re.scrollToPosition(0);
                }
                break;
            case R.id.screen_btn:
                near_label_win.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
                screen_open.setBackground(getResources().getDrawable(R.drawable.down_open));
                screen_state=1;
                break;

        }

    }

    public void setLabel_now_list(int state,ArrayList<String> a){
        if(state==2){
            label_now_list=a;
        }
        screen_open.setBackground(getResources().getDrawable(R.drawable.up_close));
    }

    private  View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((Share)getApplicationContext()).setSfragment(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((Share)getApplicationContext()).setSfragment(null);

    }
}
