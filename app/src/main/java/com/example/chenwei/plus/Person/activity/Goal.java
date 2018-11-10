package com.example.chenwei.plus.Person.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chenwei.plus.Near.Customer;
import com.example.chenwei.plus.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class Goal extends AppCompatActivity {

    private RelativeLayout backLayout;
    private CircleImageView iconView;
    private TextView nameView;
    private TextView goalView;
    private String path;
    private String name;
    private int goal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        backLayout=findViewById(R.id.evaluate_down_back_btn);
        iconView=findViewById(R.id.icon);
        nameView=findViewById(R.id.name);
        goalView=findViewById(R.id.goal);

        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        path=intent.getStringExtra("path");
        nameView.setText(name);
        Bitmap bm = BitmapFactory.decodeFile(path);
        iconView.setImageBitmap(bm);

        BmobQuery<Customer> query = new BmobQuery<>();
        query.getObject(Customer.getCurrentUser().getObjectId(), new QueryListener<Customer>() {

            @Override
            public void done(Customer object, BmobException e) {
                if(e==null){
                    goal=object.getGoal();
                    goalView.setText(goal+"");
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });

        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
