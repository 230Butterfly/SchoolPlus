package com.example.chenwei.plus.Login;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chenwei.plus.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class Forgetpassword extends AppCompatActivity implements View.OnClickListener{

    private EditText et_email;
    private Button apply;
    private Button bt_email_clear;
    private TextWatcher email_watcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_forgetpassword);
        et_email=(EditText) findViewById(R.id.email);
        bt_email_clear=(Button)findViewById(R.id.bt_eamil_clear);
        apply=(Button) findViewById(R.id.apply);
        apply.setOnClickListener(this);
        bt_email_clear.setOnClickListener(this);
        initWatcher();
        et_email.addTextChangedListener(email_watcher);}

    private void initWatcher() {
        email_watcher= new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    bt_email_clear.setVisibility(View.VISIBLE);
                }else{
                    bt_email_clear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.apply:
                et_email = (EditText) findViewById(R.id.email);
                String email=et_email.getText().toString();
                boolean cancel = false;
                View focusView = null;
                hideKeyboard();
                if (!validateEmail(email)) {
                    et_email.setError("无效邮箱地址");
                    cancel=true;
                    focusView=et_email;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    doApply();
                }
                break;
            case R.id.bt_eamil_clear:
                et_email.setText("");
                break;
        }
    }
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";//正则表达式，判断是否是邮箱格式
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;
    /*      判断是否是邮箱格式的函数       */
    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public void doApply(){
        et_email = (EditText) findViewById(R.id.email);
        final String email=et_email.getText().toString();
        BmobUser.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(Forgetpassword.this, "申请成功，请前往邮箱修改密码", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(Forgetpassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

