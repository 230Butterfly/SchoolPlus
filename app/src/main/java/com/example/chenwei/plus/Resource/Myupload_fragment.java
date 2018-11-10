package com.example.chenwei.plus.Resource;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
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

import com.example.chenwei.plus.Login.Customer;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Upload.bean.ResourceUpload;
import com.example.chenwei.plus.Upload.utils.FileUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Myupload_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Myupload_fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;
    private TextView day_title_btn;
    private TextView week_title_btn;
    private TextView month_title_btn;
    private TextView month_ago_title_btn;
    private LinearLayout day;
    private LinearLayout week;
    private LinearLayout month;
    private LinearLayout mongth_ago;
    private RadioButton day_btn;
    private RadioButton week_btn;
    private RadioButton month_btn;
    private RadioButton month_ago_btn;
    private ScrollView upload_scroll;
    private int d =0;
    private int w =0;
    private int m =0;
    private int ma =0;
    private int today=1;
    private int weekIn=2;
    private int monthIn=3;
    private int monthYe=4;
    private ArrayList<ResourceUpload> arr=new ArrayList<>();
    private ArrayList<ResourceUpload> arr1=new ArrayList<>();
    private ArrayList<ResourceUpload> arr2=new ArrayList<>();
    private ArrayList<ResourceUpload> arr3=new ArrayList<>();
    private ArrayList<ResourceUpload> arr4=new ArrayList<>();
    private SwipeRefreshLayout mRefreshLayout;

    public Myupload_fragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public Myupload_fragment(Context context) {
        this.context =context;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Myupload_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Myupload_fragment newInstance(String param1, String param2) {
        Myupload_fragment fragment = new Myupload_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //接收刷新的广播
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("upload_refresh");
                if("refresh".equals(msg)){
                    refresh();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);

        BmobQuery<ResourceUpload> query = new BmobQuery<>();
        query.addWhereEqualTo("user", Customer.getCurrentUser().getObjectId());
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        //        query.setLimit(50);

        query.findObjects(new FindListener<ResourceUpload>() {
            @Override
            public void done(List<ResourceUpload> object, BmobException e) {
                if(e==null){
                    Toast.makeText(getContext(),"查询成功：共"+object.size()+"条数据。",Toast.LENGTH_LONG).show();
                    for (ResourceUpload ru : object) {
                        arr.add(ru);
                    }
                    sortArr(arr);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void refresh() {//隐藏已经展开的
        arr.clear();
        arr1.clear();
        arr2.clear();
        arr3.clear();
        arr4.clear();
        if(day_btn.isChecked()) {//既然只能同时存在一个isChecked为true，那么用else if更有效率
            day_btn.setChecked(false);
            d = 0;
            hide(day);
        }else if(week_btn.isChecked()){
            week_btn.setChecked(false);
            w=0;
            hide(week);
        }else if(month_btn.isChecked()) {
            month_btn.setChecked(false);
            m = 0;
            hide(month);
        }else if(month_ago_btn.isChecked()){
            month_ago_btn.setChecked(false);
            ma=0;
            hide(mongth_ago);
        }
        onCreate(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myupload,container,false);
        day_title_btn=view.findViewById(R.id.day_title_btn);
        week_title_btn =view.findViewById(R.id.week_title_btn);
        month_title_btn=view.findViewById(R.id.month_title_btn);
        month_ago_title_btn =view.findViewById(R.id.month_ago_title_btn);
        day =view.findViewById(R.id.day);
        week =view.findViewById(R.id.week);
        month =view.findViewById(R.id.month);
        mongth_ago =view.findViewById(R.id.month_ago);
        day_btn=view.findViewById(R.id.day_btn);
        week_btn=view.findViewById(R.id.week_btn);
        month_btn=view.findViewById(R.id.month_btn);
        month_ago_btn=view.findViewById(R.id.month_ago_btn);

        upload_scroll=view.findViewById(R.id.upload_scroll);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);

        intiaction();
        return view;
    }

    private void intiaction() {
        day_title_btn.setOnClickListener(this);
        week_title_btn.setOnClickListener(this);
        month_title_btn.setOnClickListener(this);
        month_ago_title_btn.setOnClickListener(this);
        day_btn.setOnClickListener(this);
        week_btn.setOnClickListener(this);
        month_btn.setOnClickListener(this);
        month_ago_btn.setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                mRefreshLayout.setRefreshing(false);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.day_title_btn:
                if(day_btn.isChecked()){
                    day_btn.setChecked(false);
                    d=0;
                    hide(day);
                }else{
                    day_btn.setChecked(true);
                    d=1;
                    show(day,today);
                }
                break;
            case R.id.week_title_btn:
                if(week_btn.isChecked()){
                    week_btn.setChecked(false);
                    w=0;
                    hide(week);
                }else {
                    week_btn.setChecked(true);
                    w=1;
                    show(week,weekIn);
                }
                break;
            case R.id.month_title_btn:
                if(month_btn.isChecked()){
                    month_btn.setChecked(false);
                    m=0;
                    hide(month);
                }else{
                    month_btn.setChecked(true);
                    m=1;
                    show(month,monthIn);
                }
                break;
            case R.id.month_ago_title_btn:
                if(month_ago_btn.isChecked()){
                    month_ago_btn.setChecked(false);
                    ma=0;
                    hide(mongth_ago);
                }else {
                    month_ago_btn.setChecked(true);
                    ma=1;
                    show(mongth_ago,monthYe);
                }
                break;
            case R.id.day_btn:
                if(d==0){
                    day_btn.setChecked(true);
                    d=1;
                    show(day,today);
                }else {
                    day_btn.setChecked(false);
                    d=0;
                    hide(day);
                }
                break;
            case R.id.week_btn:
                if(w==0){
                    week_btn.setChecked(true);
                    w=1;
                    show(week,weekIn);
                }else {
                    week_btn.setChecked(false);
                    w=0;
                    hide(week);
                }
                break;
            case R.id.month_btn:
                if(m==0){
                    month_btn.setChecked(true);
                    m=1;
                    show(month,monthIn);
                }else {
                    month_btn.setChecked(false);
                    m=0;
                    hide(month);
                }
                break;
            case R.id.month_ago_btn:
                if(ma==0){
                    month_ago_btn.setChecked(true);
                    ma=1;
                    show(mongth_ago,monthYe);
                }else {
                    month_ago_btn.setChecked(false);
                    ma=0;
                    hide(mongth_ago);
                }
                break;
        }
    }

    private void hide(LinearLayout v) {
        upload_scroll.scrollTo(0,0);
        int m =v.getChildCount();
        for(int i=2;i<m;i++){
            v.removeViewAt(2);
        }
    }

    private void show(LinearLayout v,int type) {
        //将未选择的关闭
        openchoose(v);
        ArrayList<ResourceUpload> arr = new ArrayList<>();
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
            final ResourceUpload resource=arr.get(i);
            ImageView imageView =new ImageView(context);
            String path=resource.getPath();
            File file;
            Bitmap bitmap = null;
            file=new File(path);
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
            name.setText(resource.getName());
            name.setSingleLine();
            name.setMaxEms(18);
            name.setTextSize(20);
            name.setHeight(50);
            name.setWidth(500);
            name.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            TextView time =new TextView(context);
            String[]  str=resource.getCreatedAt().split(" ");
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
            price.setText("积分" + ":" + resource.getInterger());
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
                    intent.putExtra("username",c);
                    intent.putExtra("ResourceUpload",resource);
                    intent.setClass(getActivity(),Detail_resource.class);
                    startActivity(intent);
                }
            });
        }
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

    public void sortArr(ArrayList<ResourceUpload> arr){
//        Toast.makeText(getContext(),arr.size()+"",Toast.LENGTH_LONG).show();
        for(int i=0;i<arr.size();i++){
            ResourceUpload resource=arr.get(i);
            int dul=(int)getDistanceDays(resource.getCreatedAt());
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
    private void openchoose(LinearLayout v) {
        if(day_btn.isChecked()){
            if(!v.equals(day)){
                day_btn.setChecked(false);
                d=0;
                hide(day);
            }
        }
        if(week_btn.isChecked()){
            if(!v.equals(week)){
                week_btn.setChecked(false);
                w=0;
                hide(week);
            }
        }
        if(month_btn.isChecked()){
            if(!v.equals(month)){
                month_btn.setChecked(false);
                m=0;
                hide(month);
            }
        }
        if(month_ago_btn.isChecked()){
            if(!v.equals(mongth_ago)){
                month_ago_btn.setChecked(false);
                ma=0;
                hide(mongth_ago);
            }
        }
    }


}
