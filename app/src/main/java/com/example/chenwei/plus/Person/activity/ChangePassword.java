package com.example.chenwei.plus.Person.activity;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ChangePassword extends AppCompatActivity {

    private static BmobUser customer =new BmobUser();
    private EditText oldPasswordInput;
    private EditText newPasswordFirstInput;
    private EditText newPasswordSecondInput;
    private RelativeLayout backLayout;

    private Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
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
        oldPasswordInput= (EditText) findViewById(R.id.old_password_input);
        newPasswordFirstInput=(EditText) findViewById(R.id.new_password_first_input);
        newPasswordSecondInput= (EditText) findViewById(R.id.new_password_second_input);

        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setId(0);
        submitButton.setOnClickListener(input);

    }
    private Button.OnClickListener input=new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == 0) {
                String oldPassword = oldPasswordInput.getText().toString();
                String newFirstPassword = newPasswordFirstInput.getText().toString();
                String newSecondPassword = newPasswordSecondInput.getText().toString();

                if(!newFirstPassword.equals(newSecondPassword)){
                    Toast.makeText(ChangePassword.this,"输入密码不一致，请重新输入",Toast.LENGTH_LONG).show();
                }
                else{
                    BmobUser.updateCurrentUserPassword(oldPassword, newFirstPassword, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(ChangePassword.this,"密码修改成功，可以用新密码进行登录啦",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(ChangePassword.this,"失败:" + e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Intent intent=new Intent(ChangePassword.this,ChangeInfo.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    };
}
