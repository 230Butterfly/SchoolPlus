package com.example.chenwei.plus.Person.activity;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chenwei.plus.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ChangeName extends Activity{
    private static BmobUser customer =BmobUser.getCurrentUser();
    private RelativeLayout backLayout;
    private EditText nameInput;
    private Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_name);
        backLayout=findViewById(R.id.evaluate_down_back_btn);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        if(actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//
//        }

        setView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setView(){
        nameInput = (EditText) findViewById(R.id.name_input);
        Intent intent = getIntent();
        nameInput.setText(intent.getStringExtra("name"));

        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setId(0);
        submitButton.setOnClickListener(submit);


    }

    private Button.OnClickListener submit=new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == 0) {
                String name = nameInput.getText().toString();
                customer.setUsername(name);
                customer.update(BmobUser.getCurrentUser().getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","更新成功");
                            Toast.makeText(ChangeName.this,BmobUser.getCurrentUser().getObjectId()+"修改成功",Toast.LENGTH_LONG).show();
                        }else{
                            Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                            Toast.makeText(ChangeName.this,"修改失败",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                Intent intent2=new Intent();
                intent2.putExtra("name",name);
                setResult(88,intent2);
            }
            onBackPressed();
        }
    };
}