package com.example.chenwei.plus.Messages;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Share;

import java.util.ArrayList;

public class MessageDetails extends AppCompatActivity {
    private RecyclerView rv_m_list;
    private RelativeLayout rl_backbtn;
    private RelativeLayout rl_message;
    private ArrayList<String> m_list =new ArrayList<>();;
    private EditText et;
    private String id;
    private String name;
    private Button send_btn;
    private TextView name_information;
    private final DetailAdapter mAdapter= new DetailAdapter(m_list,MessageDetails.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        ((Share)getApplication()).setsActivity(this);
        ((Share)getApplication()).setsContext(this);
        Intent w =getIntent();
        id = String.valueOf(w.getStringExtra("date"));
        name=String.valueOf(w.getStringExtra("name"));
        if (w.getSerializableExtra("information")!=null){
            recieve_information(String.valueOf(w.getStringExtra("information")));
        }

         Log.v("45",name);
         Log.v("47",id);
        name_information =findViewById(R.id.name_information);
        name_information.setText(name);
        rl_backbtn =findViewById(R.id.message_detail_back_btn);
        rl_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et = findViewById(R.id.edit_content);

        rv_m_list = findViewById(R.id.list_m);
        rl_backbtn = findViewById(R.id.back_btn);
        rl_message = findViewById(R.id.message_btn);
        send_btn=findViewById(R.id.send_btn);
        LinearLayoutManager layoutManager=new LinearLayoutManager(MessageDetails.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("121313132",et.getText().toString());
                m_list.add("# "+et.getText().toString());
                mAdapter.notifyItemInserted(m_list.size()-1);
                ((Share)getApplicationContext()).sendmessage(id,name,et.getText().toString(),3);
            }
        });
        rv_m_list.setLayoutManager(layoutManager);
        rv_m_list.setAdapter(mAdapter);
        rv_m_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > 10) {//滑动距离超过10像素就收起键盘
                    hideKeyboard();
                }
            }
        });


    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void recieve_information(String information){
            m_list.add(information);
            mAdapter.notifyItemInserted(m_list.size()-1);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ((Share)getApplication()).setsActivity(null);
        ((Share)getApplication()).setsContext(null);
    }
}
