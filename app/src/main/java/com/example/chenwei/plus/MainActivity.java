package com.example.chenwei.plus;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.chenwei.plus.Bluetooth.ServerThread;
import com.example.chenwei.plus.Home.Fragment_home;
import com.example.chenwei.plus.Near.Customer;
import com.example.chenwei.plus.Near.Fragment_near;
import com.example.chenwei.plus.Person.Fragment_person;
import com.example.chenwei.plus.Resource.Fragment_resource;
import com.example.chenwei.plus.Upload.activity.MainFragmentActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private FrameLayout ly_content;
    private Fragment_home fragment_home;
    private Fragment_near fragment_near;
    private Fragment_person fragment_person;
    private Fragment_resource fragment_resource;
    private FragmentManager fManager;
    private Context mContext;
    private RadioGroup radioGroup;
    private RadioButton home_btn;
    private ImageButton upload;
    private static ArrayList<Customer> customers = new ArrayList<Customer>();

    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new
                StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(
                new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        super.onCreate(savedInstanceState);
        //申请权限
        ActivityCompat.requestPermissions(
                this,
                new String[] {
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                1
        );

        Bmob.initialize(this, "0fe7c7cb6eff2cf5a1f4d40288246b42");
        setContentView(R.layout.activity_main);
        ((Share)getApplicationContext()).Bmobconnect();
        mContext=MainActivity.this;
        customers.add(BmobUser.getCurrentUser(Customer.class));

        fManager = getFragmentManager();
        radioGroup =findViewById(R.id.group);
        radioGroup.setOnCheckedChangeListener(this);
        upload=(ImageButton) findViewById(R.id.upload_btn);
        upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(),MainFragmentActivity.class);
                startActivity(intent);
            }
        });

        bindViews();
        //获取第一个单选按钮，并设置其为选中状态
        home_btn = (RadioButton) findViewById(R.id.home_btn);
        home_btn.setChecked(true);
    }

    private void bindViews() {
        ly_content = (FrameLayout) findViewById(R.id.ly_content);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (checkedId){
            case R.id.home_btn:
                if(fragment_home == null){
                    fragment_home = new Fragment_home( mContext);
                    fTransaction.add(R.id.ly_content,fragment_home);

                }else{
                    fTransaction.show(fragment_home);
                }
                break;
            case R.id.near_btn:
                if(fragment_near == null){
                    fragment_near = new Fragment_near(fManager, mContext);
                    fTransaction.add(R.id.ly_content,fragment_near);
                }else{
                    fTransaction.show(fragment_near);
                }
                break;
            case R.id.resource_btn:
                if(fragment_resource == null){
                    fragment_resource = new Fragment_resource(fManager, mContext);
                    fTransaction.add(R.id.ly_content,fragment_resource);
                }else{
                    fTransaction.show(fragment_resource);
                }
                break;
            case R.id.person_btn:
                Log.v("hahah","hahahah");
                String m =getBtAddressByReflection();
                Log.v("11"+m+"11","蓝牙地址");
                Toast.makeText(getApplicationContext(),"11"+m+"11",Toast.LENGTH_SHORT).show();
                if(fragment_person == null){
                    fragment_person = new Fragment_person();
                    fTransaction.add(R.id.ly_content,fragment_person);
                }else{
                    fTransaction.show(fragment_person);
                }
                break;
            case R.id.upload_btn:
                Toast.makeText(MainActivity.this,"上传",Toast.LENGTH_SHORT).show();
                break;
        }
        fTransaction.commit();
    }
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fragment_person != null)fragmentTransaction.hide(fragment_person);
        if(fragment_resource!= null)fragmentTransaction.hide(fragment_resource);
        if(fragment_near != null)fragmentTransaction.hide(fragment_near);
        if(fragment_home != null)fragmentTransaction.hide(fragment_home);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (((keyCode == KeyEvent.KEYCODE_BACK) ||
                (keyCode == KeyEvent.KEYCODE_HOME))
                && event.getRepeatCount() == 0) {
            dialog_Exit(MainActivity.this);
        }
        return false;

        //end onKeyDown
    }
    public static void dialog_Exit(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //    设置Title的图标
        builder.setIcon(R.mipmap.share);
        //    设置Title的内容
        builder.setTitle("操作确认");
        //    设置Content来显示一个信息
        builder.setMessage("           确定要退出吗?");
        //    设置一个PositiveButton
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                android.os.Process.killProcess(android.os.Process
                        .myPid());
            }
        });
        //    设置一个NeutralButton
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        //    显示出该对话框
        builder.show();

    }

    public void setcustomer(ArrayList<Customer> customers1){
        customers =customers1;
        ((Share)getApplicationContext()).setCustomers(customers1);
    }
    public ArrayList<Customer> getcustomer(){
        return customers;
    }

    public static String getBtAddressByReflection() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Field field = null;
        try {
            field = BluetoothAdapter.class.getDeclaredField("mService");
            field.setAccessible(true);
            Object bluetoothManagerService = field.get(bluetoothAdapter);
            if (bluetoothManagerService == null) {
                return null;
            }
            Method method = bluetoothManagerService.getClass().getMethod("getAddress");
            if(method != null) {
                Object obj = method.invoke(bluetoothManagerService);
                if(obj != null) {
                    return obj.toString();
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.out.println("此处接收被调用方法内部未被捕获的异常");
            e.printStackTrace();
        }
        return null;
    }

}
