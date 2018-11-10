package com.example.chenwei.plus.Resource;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Tool.Recy_adapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class All_evaluate_fragment extends Fragment {

    private MyAdapter myAdapter;
    private Context mContext;
    private android.support.v7.widget.RecyclerView re;

    public All_evaluate_fragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public All_evaluate_fragment(MyAdapter myAdapter, Context context) {
        // Required empty public constructor
        this.myAdapter=myAdapter;
        this.mContext=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_evaluate_fragment,container,false);
        re=view.findViewById(R.id.all_evaluate_re);
        init(view);
        return view;
    }

    private void init(View view) {
        LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        re.setFocusableInTouchMode(false);
        re.requestFocus();
        re.setNestedScrollingEnabled(false);
        re.setLayoutManager(layoutManager);
        re.setAdapter(myAdapter);
    }

}
