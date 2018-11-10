package com.example.chenwei.plus.Login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenwei.plus.MainActivity;
import com.example.chenwei.plus.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText et_email, et_pass,et_cpass;
    private Button mApplyButton,mFinishRegister;
    int selectIndex=1;
    int tempSelect=selectIndex;
    boolean isReLogin=false;
    private int SERVER_FLAG=0;
    private RelativeLayout countryselect;
    private TextView coutry_phone_sn, coutryName;//
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_pwd_eye;
    private Button bt_cpwd_eye;
    private Button bt_cpwd_clear;
    private TextWatcher username_watcher;
    private TextWatcher password_watcher;
    private  TextWatcher confirm_password_watcher;
    private int keyCode;
    private KeyEvent event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_register);
        Bmob.initialize(this, "0fe7c7cb6eff2cf5a1f4d40288246b42");


        et_email = (EditText) findViewById(R.id.username);
        et_pass = (EditText) findViewById(R.id.password);
        et_cpass=(EditText) findViewById(R.id.confirm_password);

        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_cpwd_clear = (Button) findViewById(R.id.bt_cpwd_clear);
        bt_cpwd_eye = (Button) findViewById(R.id.bt_cpwd_eye);

        bt_username_clear.setOnClickListener(this);
        bt_pwd_clear.setOnClickListener(this);
        bt_pwd_eye.setOnClickListener(this);
        bt_cpwd_clear.setOnClickListener(this);
        bt_cpwd_eye.setOnClickListener(this);

        initWatcher();
        et_email.addTextChangedListener(username_watcher);
        et_pass.addTextChangedListener(password_watcher);
        et_cpass.addTextChangedListener(confirm_password_watcher);
        mApplyButton=(Button)findViewById(R.id.apply);
        mFinishRegister=(Button)findViewById(R.id.finish);
        mApplyButton.setOnClickListener(this);
        mFinishRegister.setOnClickListener(this);

    }
    private void initWatcher() {
        /*文本变化监听接口*/
        /*监听用户名是否变化*/
        username_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    bt_username_clear.setVisibility(View.VISIBLE);
                }else{
                    bt_username_clear.setVisibility(View.INVISIBLE);
                }
            }
        };
        /*监听密码是否变化*/
        password_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {

                if(s.toString().length()>0){
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                    if (!validatePassword(s.toString())) {
                        et_pass.setError("密码长度过短");
                    }
                }else{
                    bt_pwd_clear.setVisibility(View.INVISIBLE);

                }
            }
        };

        confirm_password_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    bt_cpwd_clear.setVisibility(View.VISIBLE);
                    String password=et_pass.getText().toString();
                    if (!confirmPassword(s.toString(),password)) {
                        et_cpass.setError("两次输入密码不符");
                    }
                }else{
                    bt_cpwd_clear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.apply:  //申请
                et_email = (EditText) findViewById(R.id.username);
                et_pass = (EditText) findViewById(R.id.password);
                et_cpass = (EditText) findViewById(R.id.confirm_password);
                String username=et_email.getText().toString();
                String password=et_pass.getText().toString();
                String confirm_password=et_cpass.getText().toString();
                boolean cancel = false;
                View focusView = null;

                hideKeyboard();
                if (!validateEmail(username)) {
                    et_email.setError("无效邮箱");
                    cancel=true;
                    focusView=et_email;
                } else if (!validatePassword(password)) {
                    et_pass.setError("密码长度过短");
                    focusView = et_pass;
                    cancel=true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    // Show a progress spinner, and kick off a background task to
                    // perform the user login attempt.

                    doApply();
                }


                break;
            case R.id.finish: //无法登陆(忘记密码了吧)
                doFinish();
                break;


            case R.id.bt_username_clear:
                et_email.setText("");
                et_pass.setText("");
                break;
            case R.id.bt_pwd_clear:
                et_pass.setText("");
                break;
            case R.id.bt_cpwd_clear:
                et_cpass.setText("");
                break;

            case R.id.bt_pwd_eye:
                if(et_pass.getInputType() == (InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                    bt_pwd_eye.setBackgroundResource(R.drawable.eye_n);
                    et_pass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    bt_pwd_eye.setBackgroundResource(R.drawable.eye_s);
                    et_pass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                et_pass.setSelection(et_pass.getText().toString().length());
                break;
            case R.id.bt_cpwd_eye:
                if(et_cpass.getInputType() == (InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                    bt_cpwd_eye.setBackgroundResource(R.drawable.eye_n);
                    et_cpass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    bt_cpwd_eye.setBackgroundResource(R.drawable.eye_s);
                    et_cpass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                et_pass.setSelection(et_pass.getText().toString().length());
                break;
        }

    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean validatePassword(String password) {

        return password.length() > 5;
    }
    public boolean confirmPassword(String password,String confirm_password){
        return password.equals(confirm_password);
    }

    public void doApply() {
        et_email = (EditText) findViewById(R.id.username);
        et_pass = (EditText) findViewById(R.id.password);
        String username = et_email.getText().toString();
        final String password = et_pass.getText().toString();
        BmobUser bu = new BmobUser();
        bu.setUsername(username);
        bu.setEmail(username);
        bu.setPassword(password);
        bu.signUp(new SaveListener<Customer>() {
            public void done(Customer s, BmobException e) {
                if (e == null) {
                    Toast.makeText(Register.this, "已发送邮箱验证", Toast.LENGTH_SHORT).show();
                    mApplyButton.setVisibility(View.INVISIBLE);
                    mFinishRegister.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(Register.this, "该邮箱已被注册，请使用其他邮箱", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    public void doFinish(){

        et_email = (EditText) findViewById(R.id.username);
        String email=et_email.getText().toString();
        BmobQuery<BmobUser> query=new BmobQuery<BmobUser>();
        query.addWhereEqualTo("email",email);
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> list, BmobException e) {
                if(e==null){
                    BmobUser c1= list.get(0);
                    if(c1.getEmailVerified().toString().equals("false")){
                        Toast.makeText(Register.this, "您还未进行邮箱验证，请验证后重试", Toast.LENGTH_SHORT).show();
                    }else if(c1.getEmailVerified().toString().equals("true")){
                        Toast.makeText(Register.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        //进入主界面
                        Intent login=new Intent();
                        login.setClass(Register.this,MainActivity.class);
                        startActivity(login);
                        finish();
                    }

                }
            }
        });

    }


    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exit();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
    public void exit(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
        dialog.setTitle("温馨提示");
        dialog.setMessage("返回后注册申请将会失效");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                et_email = (EditText) findViewById(R.id.username);
                String email=et_email.getText().toString();
                BmobQuery<BmobUser> query=new BmobQuery<BmobUser>();
                query.addWhereEqualTo("email",email);
                query.findObjects(new FindListener<BmobUser>() {
                    @Override
                    public void done(List<BmobUser> list, BmobException e) {
                        if(e==null){
                            BmobUser c1= list.get(0);
                            c1.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) { }

                            });
                        }
                    }
                });
                finish();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) { }
        });
        dialog.show();
    }

}




