package com.example.chenwei.plus.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenwei.plus.MainActivity;
import com.example.chenwei.plus.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText et_name, et_pass;
    private Button mLoginButton,mLoginError,mRegister;
    int selectIndex=1;
    int tempSelect=selectIndex;
    boolean isReLogin=false;
    private int SERVER_FLAG=0;
    private RelativeLayout countryselect;
    private TextView coutry_phone_sn, coutryName;//
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_pwd_eye;
    private TextWatcher username_watcher;
    private TextWatcher password_watcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);
        Bmob.initialize(this, "0fe7c7cb6eff2cf5a1f4d40288246b42");
        BmobUser bmobUser=BmobUser.getCurrentUser();
        if(bmobUser!=null){
            Customer customerInfo=BmobUser.getCurrentUser(Customer.class);
            Intent i= new Intent();
            i.putExtra("Customer",bmobUser);
            i.setClass(Login.this,MainActivity.class);
            startActivity(i);
            finish();
        }

        et_name = (EditText) findViewById(R.id.username);
        et_pass = (EditText) findViewById(R.id.password);

        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_username_clear.setOnClickListener(this);
        bt_pwd_clear.setOnClickListener(this);
        bt_pwd_eye.setOnClickListener(this);
        initWatcher();
        et_name.addTextChangedListener(username_watcher);
        et_pass.addTextChangedListener(password_watcher);

        mLoginButton = (Button) findViewById(R.id.login);
        mLoginError = (Button) findViewById(R.id.forget_password);
        mRegister = (Button) findViewById(R.id.register);
        mLoginButton.setOnClickListener(this);
        mLoginError.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }
    private void initWatcher() {
        /*文本变化监听接口*/
        /*监听用户名是否变化*/
        username_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                et_pass.setText("");
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
                }else{
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login:  //登陆
                et_name = (EditText) findViewById(R.id.username);
                et_pass = (EditText) findViewById(R.id.password);
                String username=et_name.getText().toString();
                String password=et_pass.getText().toString();
                boolean cancel = false;
                View focusView = null;
                hideKeyboard();
                if (!validateEmail(username)) {
                    et_name.setError("无效邮箱地址");
                    cancel=true;
                    focusView=et_name;
                } else if (!validatePassword(password)) {
                    et_pass.setError("密码长度过短");
                    focusView = et_pass;
                    cancel=true;
                }
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    dologin();
                }

                break;
            case R.id.forget_password: //无法登陆(忘记密码了吧)
                hideKeyboard();
                Intent forget_password=new Intent();
                forget_password.setClass(this,Forgetpassword.class);
                startActivity(forget_password);
                break;

            case R.id.register:
                Intent register=new Intent();
                register.setClass(this,Register.class);
                startActivity(register);
                break;
            case R.id.bt_username_clear:
                et_name.setText("");
                et_pass.setText("");
                break;
            case R.id.bt_pwd_clear:
                et_pass.setText("");
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
        }

    }
    /*            隐藏键盘          */
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
    /*    判断密码是否大于五位     */
    public boolean validatePassword(String password) {
        return password.length() > 5;
    }
    /*  实现登录   */
    public void dologin(){
        et_name = (EditText) findViewById(R.id.username);
        et_pass = (EditText) findViewById(R.id.password);
        String username=et_name.getText().toString();
        final String password=et_pass.getText().toString();
        final BmobUser bu=new BmobUser();
        bu.setUsername(username);
        bu.setEmail(username);
        bu.setPassword(password);
        bu.login(new SaveListener<BmobUser>() {
            public void done(BmobUser bmobUser,BmobException e){
                if (e==null){
                    Intent login_error_intent=new Intent();
                    login_error_intent.putExtra("Customer",bu);
                    login_error_intent.setClass(Login.this,MainActivity.class);
                    startActivity(login_error_intent);
                    finish();
                }
                else{
                    Toast.makeText(Login.this, "用户名或密码错误，请重试" +e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

