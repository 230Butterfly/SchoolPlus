package com.example.chenwei.plus.Home;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
public class ClassifyContent extends Fragment {

    private RecyclerView rv_content;
    private TextView tv;
    private int nummber;
    private  ArrayList<Near_resource_information> near_resource_informations =null;
    public ClassifyContent(int number, ArrayList<Near_resource_information> near_resource_informations) {
        // Required empty public constructor
        this.near_resource_informations =near_resource_informations;
        this.nummber=number;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_classify_content, container, false);
        rv_content=view.findViewById(R.id.rv_content);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ClassifyContentAdapter classifyContentAdapter=new ClassifyContentAdapter(near_resource_informations,getActivity());
        rv_content.setLayoutManager(layoutManager);
        rv_content.setAdapter(classifyContentAdapter);
        classifyContentAdapter.setOnItemClickListener(new ClassifyContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int Position) {
                Intent m=new Intent(getActivity(),Near_resource_detail.class);
                m.putExtra("date", (Serializable) near_resource_informations.get(Position));
                m.putExtra("all",near_resource_informations);
                startActivity(m);
            }
        });
        return view;
    }

}
