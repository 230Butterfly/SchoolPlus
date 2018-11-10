package com.example.chenwei.plus.Person;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenwei.plus.Login.Customer;
import com.example.chenwei.plus.Login.Login;
import com.example.chenwei.plus.Person.activity.ChangeInfo;
import com.example.chenwei.plus.Person.activity.ChangePassword;
import com.example.chenwei.plus.Person.activity.Feedback;
import com.example.chenwei.plus.Person.activity.Goal;
import com.example.chenwei.plus.Person.activity.Invite;
import com.example.chenwei.plus.Person.activity.Phone;
import com.example.chenwei.plus.Person.activity.Setting;
import com.example.chenwei.plus.Person.control.HintPopupWindow;
import com.example.chenwei.plus.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Chenwei on 2018/8/6.
 */

public class Fragment_person extends Fragment {
    private LinearLayout selfLayout;
    private LinearLayout signOnLayout;
    private LinearLayout goalLayout;
    private LinearLayout inviteLayout;
    private PercentRelativeLayout vipLayout;
    private PercentRelativeLayout phoneLayout;
    private PercentRelativeLayout changePasswordLayout;
    private PercentRelativeLayout changeUserLayout;
    private PercentRelativeLayout settingLayout;
    private PercentRelativeLayout feedbackLayout;
    private TextView name;
    private CircleImageView icon;
    private String path;
    private HintPopupWindow hintPopupWindow;
    private int REQUEST=21;
    private SwipeRefreshLayout mRefreshLayout;
    private int goal;

    private Handler handler=null;
    public Fragment_person(){

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.self_info_fragment,container,false);
        handler=new Handler();
        return view;
    }

    private void refresh() {

        onActivityCreated(null);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("info_refresh");
                if("refresh".equals(msg)){
                    refresh();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
        selfLayout = (LinearLayout) getActivity().findViewById(R.id.self_info);
        signOnLayout = (LinearLayout) getActivity().findViewById(R.id.sign_on);
        goalLayout = (LinearLayout) getActivity().findViewById(R.id.goal);
        inviteLayout = (LinearLayout) getActivity().findViewById(R.id.invite);
        vipLayout = (PercentRelativeLayout) getActivity().findViewById(R.id.vip);
        phoneLayout = (PercentRelativeLayout) getActivity().findViewById(R.id.phone);
        changePasswordLayout = (PercentRelativeLayout) getActivity().findViewById(R.id.change_password);
        changeUserLayout = (PercentRelativeLayout) getActivity().findViewById(R.id.change_user);
        settingLayout = (PercentRelativeLayout) getActivity().findViewById(R.id.setting);
        feedbackLayout = (PercentRelativeLayout) getActivity().findViewById(R.id.feedback);
        name = (TextView) getActivity().findViewById(R.id.name);


        icon = (CircleImageView) getActivity().findViewById(R.id.icon);
        name.setText(Customer.getCurrentUser().getUsername());

        BmobQuery<Customer> query = new BmobQuery<Customer>();
        query.getObject(Customer.getCurrentUser().getObjectId(), new QueryListener<Customer>() {

            @Override
            public void done(Customer object, BmobException e) {
                if (e == null) {
                    BmobFile bmobFile = object.getIcon();

//                    final String path=getActivity().getApplicationContext().getCacheDir()+"/bmob/" +bmobFile.getFilename();
                    path = "/storage/emulated/0/Android/data/com.example.chenwei.plus/cache/" + bmobFile.getFilename();

                    Log.i("bmob", "成功：" + path);
                    File file = new File(path);

                    if (!new File(path).exists()) {
                        BmobQuery<Customer> bmobQuery = new BmobQuery<Customer>();
                        bmobQuery.findObjects(new FindListener<Customer>() {
                            @Override
                            public void done(List<Customer> object, BmobException e) {
                                if (e == null) {
                                    for (Customer customer : object) {
                                        BmobFile bmobfile = customer.getIcon();
                                        if (bmobfile != null&&customer.getObjectId().equals(Customer.getCurrentUser().getObjectId())) {
                                            //调用bmobfile.download方法
                                            downloadFile(bmobfile);
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "查询失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                    Bitmap bm = BitmapFactory.decodeFile(path);
                    icon.setImageBitmap(bm);

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });




        selfLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),ChangeInfo.class);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("path",path);
                startActivity(intent);
            }
        });
        signOnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
//获取当前时间
                Date date = new Date(System.currentTimeMillis());
                String today=simpleDateFormat.format(date);
                String signOnDate = pref.getString("signOnDate", "");


                if(signOnDate.equals(today)){
                    Toast.makeText(getActivity().getApplicationContext(),"今天已签到",Toast.LENGTH_LONG).show();
                }else{//第一次签到或者签到时间和存储的时间不一样
                    editor.putString("signOnDate",today );
                    hintPopupWindow.showPopupWindow(v);
                    addGoal();
                }

                editor.commit();
            }
        });
        ArrayList<String> strList = new ArrayList<>();
        strList.add("+5积分");

        ArrayList<View.OnClickListener> clickList = new ArrayList<>();
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        clickList.add(clickListener);
        clickList.add(clickListener);

        //具体初始化逻辑看下面的图
        hintPopupWindow = new HintPopupWindow(getActivity(), strList, clickList);
        goalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),Goal.class);
                intent.putExtra("name",name.getText());
                intent.putExtra("path",path);
                startActivity(intent);
            }
        });
        inviteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),Invite.class);
                intent.putExtra("name",name.getText());
                intent.putExtra("path",path);
                startActivity(intent);
            }
        });
        vipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),ChangePassword.class);
                startActivity(intent);
            }
        });
        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),Phone.class);
                startActivity(intent);
            }
        });
        changeUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("是否退出登录")//设置对话框的标题
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BmobUser.logOut();   //清除缓存用户对象
                                BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
                                Intent intent=new Intent();
                                intent.setClass(getActivity(), Login.class);
                                startActivity(intent);
                                Toast.makeText(getActivity(),"暂不支持更改用户",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();

            }
        });
        inviteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),Invite.class);
                startActivity(intent);
            }
        });
        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),Setting.class);
                startActivity(intent);
            }
        });
        feedbackLayout.setOnClickListener(new View.OnClickListener() {//反馈
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),Feedback.class);
                startActivity(intent);

            }
        });
    }
    Runnable runnableUi=new Runnable() {
        @Override
        public void run() {
            Bitmap bm = BitmapFactory.decodeFile(path);
            icon.setImageBitmap(bm);
        }
    };
    private void downloadFile(BmobFile file){
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        File saveFile = new File("/storage/emulated/0/Android/data/com.example.chenwei.plus/cache/" , file.getFilename());
        file.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {
//                Toast.makeText(getActivity(),"开始下载。。。",Toast.LENGTH_LONG).show();
            }


            @Override
            public void done(String savePath,BmobException e) {
                if(e==null){
//                    Toast.makeText(getActivity(),"下载成功,保存路径:"+savePath,Toast.LENGTH_LONG).show();
                    new Thread(){
                        public void run(){
                            handler.post(runnableUi);
                        }
                    }.start();

                }else{
                    Toast.makeText(getActivity(),"头像获取失败："+e.getErrorCode()+","+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("bmob","下载进度："+value+","+newworkSpeed);
            }

        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode==REQUEST&&resultCode==77){
            path=intent.getStringExtra("path");
            Bitmap bm = BitmapFactory.decodeFile(path);
            icon.setImageBitmap(bm);
        }
        if(requestCode==REQUEST&&resultCode==66){

            name.setText(intent.getStringExtra("name"));
//            Toast.makeText(SelfInfo.this,"已经返回的参数"+name,Toast.LENGTH_LONG).show();
        }

    }
    public void addGoal(){
        BmobQuery<Customer> query = new BmobQuery<>();
        query.getObject(Customer.getCurrentUser().getObjectId(), new QueryListener<Customer>() {
            @Override
            public void done(Customer object, BmobException e) {
                if(e==null){
                    goal=object.getGoal();
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
        Customer customer = new Customer();
        customer.setGoal(goal+5);
        customer.update(Customer.getCurrentUser().getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","更新成功");
                }else{
                    Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
