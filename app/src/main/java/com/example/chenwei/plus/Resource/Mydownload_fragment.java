package com.example.chenwei.plus.Resource;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Tool.Near_resource_information;
import com.example.chenwei.plus.Upload.bean.ResourceUpload;
import com.example.chenwei.plus.Upload.utils.FileUtil;

import org.greenrobot.greendao.database.DatabaseOpenHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.bmob.v3.BmobUser;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Mydownload_fragment extends Fragment implements View.OnClickListener {
    private Context context;
    private TextView down_day_title_btn;
    private TextView down_week_title_btn;
    private TextView down_month_title_btn;
    private TextView down_month_ago_title_btn;
    private LinearLayout down_day;
    private LinearLayout down_week;
    private LinearLayout down_month;
    private LinearLayout down_mongth_ago;
    private RadioButton down_day_btn;
    private RadioButton down_week_btn;
    private RadioButton down_month_btn;
    private RadioButton down_month_ago_btn;
    private ScrollView down_scroll;
    private int d =0;
    private int w =0;
    private int m =0;
    private int ma =0;
    private SwipeRefreshLayout mRefreshLayout;
        private ArrayList<Near_resource_information> arr=new ArrayList<>();
    private ArrayList<Near_resource_information> arr1=new ArrayList<>();
    private ArrayList<Near_resource_information> arr2=new ArrayList<>();
    private ArrayList<Near_resource_information> arr3=new ArrayList<>();
    private ArrayList<Near_resource_information> arr4=new ArrayList<>();
    private int id;
    private String person_name;
    private String resource_name;
    private int price;
    private int distance;
    private String path;
    private String label;
    private String time;
    private String size;
    private String url;
    private String introduction;
    private String user_id;
    private int resource_grade;
    private int today=1;
    private int weekIn=2;
    private int monthIn=3;
    private int monthYe=4;
    SQLiteDatabase db;
    Near_resource_information resource;
    private ResourceDownloadDb dbHelper;
    public Mydownload_fragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public Mydownload_fragment(Context context){
        this.context=context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //接收刷新的广播
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("download_refresh");
                if("refresh".equals(msg)){
                    refresh();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);

        dbHelper = new ResourceDownloadDb(getApplicationContext(), "ResourceDownload.db", null, 1);
        db = dbHelper.getWritableDatabase();
        query(db);
        db.close();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mydownload,container,false);
        down_day_title_btn=view.findViewById(R.id.down_day_title_btn);
        down_week_title_btn =view.findViewById(R.id.down_week_title_btn);
        down_month_title_btn=view.findViewById(R.id.down_month_title_btn);
        down_month_ago_title_btn =view.findViewById(R.id.down_month_ago_title_btn);
        down_day =view.findViewById(R.id.down_day);
        down_week =view.findViewById(R.id.down_week);
        down_month =view.findViewById(R.id.down_month);
        down_mongth_ago =view.findViewById(R.id.down_month_ago);
        down_day_btn=view.findViewById(R.id.down_day_btn);
        down_week_btn=view.findViewById(R.id.down_week_btn);
        down_month_btn=view.findViewById(R.id.down_month_btn);
        down_month_ago_btn=view.findViewById(R.id.down_month_ago_btn);

        down_scroll=view.findViewById(R.id.down_scroll);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        intiaction();
        return view;
    }

    private void intiaction() {
        down_day_title_btn.setOnClickListener(this);
        down_week_title_btn.setOnClickListener(this);
        down_month_title_btn.setOnClickListener(this);
        down_month_ago_title_btn.setOnClickListener(this);
        down_day_btn.setOnClickListener(this);
        down_week_btn.setOnClickListener(this);
        down_month_btn.setOnClickListener(this);
        down_month_ago_btn.setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                mRefreshLayout.setRefreshing(false);
            }
        });
    }
        private void refresh() {//隐藏已经展开的
        arr.clear();
        arr1.clear();
        arr2.clear();
        arr3.clear();
        arr4.clear();
        if(down_day_btn.isChecked()) {//既然只能同时存在一个isChecked为true，那么用else if更有效率
            down_day_btn.setChecked(false);
            d = 0;
            hide(down_day);
        }else if(down_week_btn.isChecked()){
            down_week_btn.setChecked(false);
            w=0;
            hide(down_week);
        }else if(down_month_btn.isChecked()) {
            down_month_btn.setChecked(false);
            m = 0;
            hide(down_month);
        }else if(down_month_ago_btn.isChecked()){
            down_month_ago_btn.setChecked(false);
            ma=0;
            hide(down_mongth_ago);
        }
        onCreate(null);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.down_day_title_btn:
                if(down_day_btn.isChecked()){
                    down_day_btn.setChecked(false);
                    d=0;
                    hide(down_day);
                }else{
                    down_day_btn.setChecked(true);
                    d=1;
                    show(down_day,today);
                }
                break;
            case R.id.down_week_title_btn:
                if(down_week_btn.isChecked()){
                    down_week_btn.setChecked(false);
                    w=0;
                    hide(down_week);
                }else {
                    down_week_btn.setChecked(true);
                    w=1;
                    show(down_week,weekIn);
                }
                break;
            case R.id.down_month_title_btn:
                if(down_month_btn.isChecked()){
                    down_month_btn.setChecked(false);
                    m=0;
                    hide(down_month);
                }else{
                    down_month_btn.setChecked(true);
                    m=1;
                    show(down_month,monthIn);
                }
                break;
            case R.id.down_month_ago_title_btn:
                if(down_month_ago_btn.isChecked()){
                    down_month_ago_btn.setChecked(false);
                    ma=0;
                    hide(down_mongth_ago);
                }else {
                    down_month_ago_btn.setChecked(true);
                    ma=1;
                    show(down_mongth_ago,monthYe);
                }
                break;
            case R.id.down_day_btn:
                if(d==0){
                    down_day_btn.setChecked(true);
                    d=1;
                    show(down_day,today);
                }else {
                    down_day_btn.setChecked(false);
                    d=0;
                    hide(down_day);
                }
                break;
            case R.id.down_week_btn:
                if(w==0){
                    down_week_btn.setChecked(true);
                    w=1;
                    show(down_week,weekIn);
                }else {
                    down_week_btn.setChecked(false);
                    w=0;
                    hide(down_week);
                }
                break;
            case R.id.down_month_btn:
                if(m==0){
                    down_month_btn.setChecked(true);
                    m=1;
                    show(down_month,monthIn);
                }else {
                    down_month_btn.setChecked(false);
                    m=0;
                    hide(down_month);
                }
                break;
            case R.id.down_month_ago_btn:
                if(ma==0){
                    down_month_ago_btn.setChecked(true);
                    ma=1;
                    show(down_mongth_ago,monthYe);
                }else {
                    down_month_ago_btn.setChecked(false);
                    ma=0;
                    hide(down_mongth_ago);
                }
                break;
        }
    }

        private void show(LinearLayout v,int type) {
        //将未选择的关闭
        openchoose(v);
        ArrayList<Near_resource_information> arr = new ArrayList<>();
        switch (type){
            case 1:
                arr=arr1;
                break;
            case 2:
                arr=arr2;
                break;
            case 3:
                arr=arr3;
                break;
            case 4:
                arr=arr4;
                break;
        }

        for(int i=0;i<arr.size();i++){
            resource=arr.get(i);
            ImageView imageView =new ImageView(context);
            String path=resource.getPath();
            File file;
            Bitmap bitmap = null;
            file=new File(path);


            if(!file.exists()){
                //点进去显示不存在
            }


            try {
                bitmap= FileUtil.fileGetBitmap(getApplicationContext(),file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);

            LinearLayout lv =new LinearLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(i==0){
                layoutParams.setMargins(20, 10, 15, 10);//4个参数按顺序分别是左上右下
            }else{
                layoutParams.setMargins(20, 0  , 15, 10);//4个参数按顺序分别是左上右下
            }
            lv.setLayoutParams(layoutParams);
            lv.setOrientation(LinearLayout.HORIZONTAL);

            lv.addView(imageView);
            ViewGroup.LayoutParams para;
            para = imageView.getLayoutParams();
            para.height = 200;
            para.width = 200;
            imageView.setLayoutParams(para);
            LinearLayout information =new LinearLayout(context);
            information.setOrientation(LinearLayout.VERTICAL);
            LinearLayout title =new LinearLayout(context);
            title.setOrientation(LinearLayout.HORIZONTAL);
            TextView name =new TextView(context);
            name.setText(resource.getResource_name());
            name.setSingleLine();
            name.setMaxEms(18);
            name.setTextSize(20);
            name.setHeight(50);
            name.setWidth(500);
            name.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            TextView time =new TextView(context);
            String[]  str=resource.getTime().split(" ");
            time.setPadding(30,0,0,0);
            time.setText(str[0]);
            time.setGravity(View.TEXT_ALIGNMENT_CENTER);
            name.setHeight(100);
            title.addView(name);
            title.addView(time);
            final LinearLayout details =new LinearLayout(context);
            TextView size =new TextView(context);
            size.setText("文件大小" + ":" + getPrintSize(Long.parseLong(resource.getSize())) + "        ");
            TextView price =new TextView(context);
            price.setText("积分" + ":" + resource.getResource_grade());
            details.setPadding(0,0,0,0);
            details.addView(size);
            details.addView(price);
            information.addView(title);
            information.addView(details);
            information.setPadding(40,20,20,20);
            lv.addView(information);
            lv.setPadding(70,20,20,20);
            lv.setBackground(getResources().getDrawable(R.drawable.shadow));
            v.addView(lv);
            lv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent();
                    String c = BmobUser.getCurrentUser().getUsername();
                    ResourceUpload temp=new ResourceUpload(resource.getUser_id(),
                            resource.getPath(),resource.getResource_name(),
                            resource.getIntroduction(),resource.getPrice(),
                            resource.getResource_grade(),resource.getLabel(),
                            resource.getSize(),resource.getUser_name());
                    intent.putExtra("username",c);
                    intent.putExtra("ResourceUpload",temp);
                    intent.setClass(getActivity(),Detail_resource.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void openchoose(LinearLayout v) {
        if(down_day_btn.isChecked()){
            if(!v.equals(down_day)){
                down_day_btn.setChecked(false);
                d=0;
                hide(down_day);
            }
        }
        if(down_week_btn.isChecked()){
            if(!v.equals(down_week)){
                down_week_btn.setChecked(false);
                w=0;
                hide(down_week);
            }
        }
        if(down_month_btn.isChecked()){
            if(!v.equals(down_month)){
                down_month_btn.setChecked(false);
                m=0;
                hide(down_month);
            }
        }
        if(down_month_ago_btn.isChecked()){
            if(!v.equals(down_mongth_ago)){
                down_month_ago_btn.setChecked(false);
                ma=0;
                hide(down_mongth_ago);
            }
        }
    }

    private void hide(LinearLayout v) {
        down_scroll.scrollTo(0,0);
        int m =v.getChildCount();
        for(int i=2;i<m;i++){
            v.removeViewAt(2);
        }
    }
    private void query(SQLiteDatabase db) {


        //查询获得游标
        Cursor cursor = db.query ("ResourceDownload",null,null,null,null,null,null);
        Toast.makeText(getApplicationContext(),"游标的数量"+cursor.getCount(),Toast.LENGTH_LONG).show();
        //判断游标是否为空
        if(cursor.moveToFirst()){
        //遍历游标
            for(int i=0;i<cursor.getCount();i++){
                cursor.move(i);
        //获得ID
                id = cursor.getInt(0);
                user_id=cursor.getString(1);
                resource_name=cursor.getString(2);
                resource_grade=cursor.getInt(3);
                price=cursor.getInt(4);
                distance=cursor.getInt(5);
                label=cursor.getString(6);
                time=cursor.getString(7);
                size=cursor.getString(8);
                introduction=cursor.getString(9);
                path=cursor.getString(10);
                person_name=cursor.getString(11);
                url=cursor.getString(12);

                arr.add(new Near_resource_information(user_id,resource_name,resource_grade,price,distance,label,time,size,introduction,path,person_name,url));
                Log.e("savePath","插入成功"+arr.get(i).getTime());
            }
        }
        cursor.close();
        sortArr(arr);
    }
    public static String getPrintSize(long size) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        double value = (double) size;
        if (value < 1024) {
            return String.valueOf(value) + "B";
        } else {
            value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (value < 1024) {
            return String.valueOf(value) + "KB";
        } else {
            value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }
        if (value < 1024) {
            return String.valueOf(value) + "MB";
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            return String.valueOf(value) + "GB";
        }
    }

    public void sortArr(ArrayList<Near_resource_information> arr){
//        Toast.makeText(getContext(),arr.size()+"",Toast.LENGTH_LONG).show();
        for(int i=0;i<arr.size();i++){
            Near_resource_information resource=arr.get(i);
            int dul=(int)getDistanceDays(resource.getTime());
//            Toast.makeText(getContext(),"第"+i+"个文件距今"+dul+"",Toast.LENGTH_LONG).show();
            if(dul==0){
                arr1.add(resource);
            }
            else if(dul>=-7){
                arr2.add(resource);
            }
            else if(dul>=-30){
                arr3.add(resource);
            }
            else{
                arr4.add(resource);
            }
        }
    }
    public static long getDistanceDays(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        long days = 0;
        try {
            Date time = df.parse(date);//String转Date
            Date now = new Date();//获取当前时间
            long time1 = time.getTime();
            long time2 = now.getTime();
            long diff = time1 - time2;
            days = diff / (1000 * 60 * 60 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;//正数表示在当前时间之后，负数表示在当前时间之前
    }


}
