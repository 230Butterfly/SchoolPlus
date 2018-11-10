package com.example.chenwei.plus.Upload.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chenwei.plus.R;


public class MyFragment extends Fragment {
    private Context context;


    public MyFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public MyFragment(Context context){
        this.context=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_local,container,false);

        return view;
    }
}
