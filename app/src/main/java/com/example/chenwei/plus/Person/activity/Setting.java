package com.example.chenwei.plus.Person.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenwei.plus.Home.SearchActivity;
import com.example.chenwei.plus.Person.view.GuideView;
import com.example.chenwei.plus.R;

public class Setting extends AppCompatActivity {

    private ImageButton menu;
    private Button btnTest;
    private Button btnTest2;
    private GuideView guideView;
    private GuideView guideView3;
    private GuideView guideView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toast.makeText(Setting.this,"开始下载", Toast.LENGTH_SHORT).show();


    }


}