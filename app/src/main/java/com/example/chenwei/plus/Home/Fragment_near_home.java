package com.example.chenwei.plus.Home;


import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chenwei.plus.Near.Near_resource_detail;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Tool.Near_resource_information;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Fragment_near_home extends Fragment {
    private FragmentManager fragmentManager;
    private ArrayList<Near_resource_information> posts;
    private RecyclerView rv_post_list;

    @SuppressLint("ValidFragment")
    public Fragment_near_home() {
    }
    public Fragment_near_home(ArrayList<Near_resource_information> posts) {
        // Required empty public constructor
        this.posts=posts;
    }
    @SuppressLint("ValidFragment")
    public Fragment_near_home(Context context) {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_near_home, container, false);
        rv_post_list=view.findViewById(R.id.list_post);
        rv_post_list.setFocusableInTouchMode(false); //设置不需要焦点
        rv_post_list.requestFocus(); //设置焦点不需要
        rv_post_list.setHasFixedSize(true);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        MyAdapter myAdapter= new MyAdapter(posts,getActivity());
        rv_post_list.setLayoutManager(layoutManager);
        rv_post_list.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int Position) {
                Intent intent=new Intent(getActivity(),Near_resource_detail.class);
                intent.putExtra("date", (Serializable) posts.get(Position));
                intent.putExtra("all",posts);
                startActivity(intent);
            }
        });
        return view;
    }


}
