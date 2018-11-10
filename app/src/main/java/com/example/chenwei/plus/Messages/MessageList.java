package com.example.chenwei.plus.Messages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.chenwei.plus.R;

import java.util.ArrayList;

public class MessageList extends AppCompatActivity {
    private RecyclerView rv_message_list;
    private RelativeLayout rl_backbtn;
    private RelativeLayout rl_message;
    private ArrayList<String> messages_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        rl_backbtn =findViewById(R.id.message_back_btn);
        rl_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        messages_list = new ArrayList<>();
        for(int i=0;i<10;i++){
            messages_list.add("s");
        }
        rv_message_list = findViewById(R.id.list_message);
        rl_backbtn = findViewById(R.id.back_btn);
        rl_message = findViewById(R.id.message_btn);
        LinearLayoutManager layoutManager=new LinearLayoutManager(MessageList.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        MessageAdapter messageAdapter= new MessageAdapter(messages_list,MessageList.this);
        rv_message_list.setLayoutManager(layoutManager);
        rv_message_list.setAdapter(messageAdapter);
        messageAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int Position) {
                Intent i = new Intent(MessageList.this,MessageDetails.class);
                startActivity(i);
            }
        });
    }
}
