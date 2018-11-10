package com.example.chenwei.plus.Near;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.nfc.Tag;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenwei.plus.Bluetooth.ServerThread;
import com.example.chenwei.plus.MainActivity;
import com.example.chenwei.plus.Messages.MessageDetails;
import com.example.chenwei.plus.Messages.MessageList;
import com.example.chenwei.plus.Person.activity.ChangeInfo;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Resource.All_evaluate;

import com.example.chenwei.plus.Resource.Evaluate_win;
import com.example.chenwei.plus.Resource.Label_win;
import com.example.chenwei.plus.Resource.Reply_list;
import com.example.chenwei.plus.Resource.ResourceDownloadDb;
import com.example.chenwei.plus.Resource.Resource_download;
import com.example.chenwei.plus.Share;
import com.example.chenwei.plus.Tool.MyScrollView;
import com.example.chenwei.plus.Tool.Near_resource_information;
import com.example.chenwei.plus.Tool.Recy_adapter;
import com.example.chenwei.plus.Upload.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

import static com.example.chenwei.plus.Upload.utils.FileUtil.fileGetBitmap;

public class Near_resource_detail extends AppCompatActivity implements View.OnClickListener {

    private Context mContext=this;
    //顶部固定的TabViewLayout
    private LinearLayout near_mTopTabViewLayout;

    //跟随ScrollView的TabviewLayout
    private LinearLayout near_mTabViewLayout;

    //要悬浮在顶部的View的子View
    private LinearLayout near_mTopView;
    private RelativeLayout near_back_btn,title_resource;
    private RelativeLayout near_message_btn;
    private LinearLayout near_label_btn;
    private LinearLayout near_label_space;
    private LinearLayout near_evaluate_space;
    private Button near_compile_btn;
    private Button near_delete_btn;
    private RecyclerView near_reply_list;
    private ArrayList<Reply_list> near_reply_lists =new ArrayList<>();
    private LinearLayout near_Recycler_header;
    private LinearLayout near_detail_lin;
    private TextWatcher near_username_watcher;
    private Evaluate_win near_evaluate_win;
    private View near_rootview;
    private Label_win near_label_win;

    private LinearLayout near_heigth_stand,near_all_evaluate;
    private LinearLayout near_topview,near_intro_co,near_intro_re,near_center_co,near_recy_tail,near_evaluate_height,near_Recommend_space;
    private View near_soild_view,near_center_div;
    private int near_mScreenWidth;//屏宽
    private int near_mScreenHeight;//屏高
    private int near_information_height;
    private ArrayList<String> near_label_now_list;
    private TextView name_text;
    private ArrayList<Near_resource_information> reply_lists =new ArrayList<>();
    private Near_resource_information now ;
    private TextView near_resource_name;
    private TextView near_name;
    private TextView near_resource_Integer;
    private TextView near_resource_time;
    private TextView near_resource_size;
    private TextView near_resource_comment;
    private TextView near_information_text;
    private static ArrayList<Customer> customers =null;
    NotificationCompat.Builder builder =null;
    NotificationManager notificationManager = null;
    private ArrayList<Near_resource_information> post =new ArrayList<>();
    private ImageView icon;
//    private Handler handler=null;
//    private String path;
    private ResourceDownloadDb resourceDownload;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_resource_detail);
        resourceDownload = new ResourceDownloadDb(getApplicationContext(), "ResourceDownload.db", null, 1);


//        handler=new Handler();
        ((Share)getApplicationContext()).setNear_resource_detail(this);
        Intent i = getIntent();
        if (i.getSerializableExtra("date")!=null){
            customers=((Share)getApplicationContext()).getCustomers();
            near_label_now_list=new ArrayList<>();
            now =(Near_resource_information)i.getSerializableExtra("date");
        }
        if (i.getSerializableExtra("all")!=null){
            post = (ArrayList<Near_resource_information>) i.getSerializableExtra("all");
        }
        if (now.getLabel().equals("暂无标签")){
            near_label_now_list.add("暂无标签");
        }else {
            String m[] =now.getLabel().split("#");
            for (int c=0;c<now.getLabel().split("#").length;c++){
                near_label_now_list.add(m[c]);
            }
        }
        near_information_text =findViewById(R.id.near_information_text);
        near_information_text.setText(now.getIntroduction());
         near_resource_name =findViewById(R.id.near_resource_name);
         near_name =findViewById(R.id.name_text);
         near_resource_Integer=findViewById(R.id.near_resource_Integer);
         near_resource_time=findViewById(R.id.near_resource_time);
         near_resource_size=findViewById(R.id.near_resource_size);
         near_resource_comment=findViewById(R.id.near_resource_comment);
        near_resource_name.setText(now.getResource_name());
        near_resource_name.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        near_resource_name.setSingleLine();
        near_name.setText(now.getUser_name());
        near_resource_Integer.setText(String.valueOf(now.getPrice())+"积分");
        near_resource_time.setText(now.getTime());
        near_resource_size.setText("大小" + getPrintSize(Long.parseLong(now.getSize())));
        near_resource_comment.setText("评价："+String.valueOf(now.getResource_grade()));

        icon=findViewById(R.id.icon);
        try {
            icon.setImageBitmap(fileGetBitmap(mContext,now.getFile_url()));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Display display = getWindowManager().getDefaultDisplay(); //Activity#getWindowManager()
        Point size = new Point();
        display.getSize(size);
        near_mScreenWidth = size.x;
        near_mScreenHeight = size.y;

        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        near_information_height= rect.top;

        near_Recommend_space=findViewById(R.id.near_Recommend_space);
        near_recy_tail=findViewById(R.id.near_recy_tail);
        near_center_div=findViewById(R.id.near_center_div);
        near_center_co=findViewById(R.id.near_center_co);
        near_soild_view=findViewById(R.id.near_soild_view);
        near_topview=findViewById(R.id.near_topview);
        near_intro_co=findViewById(R.id.near_intro_co);
        near_intro_re=findViewById(R.id.near_intro_re);
        final MyScrollView mMyScrollView = (MyScrollView) findViewById(R.id.near_my_scrollview);
        near_mTabViewLayout = (LinearLayout) findViewById(R.id.near_ll_tabView);
        near_mTopTabViewLayout = (LinearLayout) findViewById(R.id.near_ll_tabTopView);
        near_mTopView = (LinearLayout) findViewById(R.id.near_tv_topView);
        near_heigth_stand =findViewById(R.id.near_height_stand);
        near_evaluate_height=findViewById(R.id.near_evaluate_height);

        mMyScrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                int mHeight = near_mTabViewLayout.getTop();
                int c=near_intro_re.getTop();
                //判断滑动距离scrollY是否大于0，因为大于0的时候就是可以滑动了，此时mTabViewLayout.getTop()才能取到值。
                if((mMyScrollView.getScrollY() + mMyScrollView.getHeight()) >= mMyScrollView.getcomputeVerticalScrollRange())
                {
                    near_reply_list.setNestedScrollingEnabled(true);
                }else {
                    near_reply_list.setNestedScrollingEnabled(false);
                }
                if (scrollY > 0 && scrollY >= mHeight+near_center_div.getHeight()+near_center_co.getHeight()+near_evaluate_height.getHeight()+20) {
                    if (near_intro_co.getParent() != near_topview) {
                        near_intro_re.removeView(near_intro_co);
                        near_intro_re.removeView(near_soild_view);
                        near_topview.addView(near_intro_co);
                        near_topview.addView(near_soild_view);
                    }
                }
                else{
                    if (near_intro_co.getParent() != near_intro_re) {
                        near_topview.removeView(near_intro_co);
                        near_topview.removeView(near_soild_view);
                        near_intro_re.addView(near_intro_co);
                        near_intro_re.addView(near_soild_view);
                    }
                }

                if (scrollY > 0 && scrollY >= mHeight) {
                    if (near_mTopView.getParent() != near_mTopTabViewLayout) {
                        near_mTabViewLayout.removeView(near_mTopView);
                        near_mTopTabViewLayout.addView(near_mTopView);
                    }
                }
                else{
                    if (near_mTopView.getParent() != near_mTabViewLayout) {
                        near_mTopTabViewLayout.removeView(near_mTopView);
                        near_mTabViewLayout.addView(near_mTopView);
                        near_reply_list.setNestedScrollingEnabled(false);
                    }
                }
            }

        });

        near_detail_lin=findViewById(R.id.near_detail_lin);
        near_back_btn =findViewById(R.id.near_back_btn);
        near_message_btn=findViewById(R.id.near_resource_message_btn);
        near_label_space=findViewById(R.id.near_label_space);
        near_evaluate_space=findViewById(R.id.near_evaluate_space);
        near_information_text =findViewById(R.id.near_information_text);
        near_compile_btn =findViewById(R.id.near_compile_btn);
        near_delete_btn =findViewById(R.id.near_delete_btn);
        near_all_evaluate=findViewById(R.id.near_all_evaluate);
        near_all_evaluate.setOnClickListener(this);
        near_back_btn.setOnClickListener(this);
        near_message_btn.setOnClickListener(this);
        near_compile_btn.setOnClickListener(this);
        near_delete_btn.setOnClickListener(this);

        title_resource=findViewById(R.id.near_title_resource);
        //near_evaluate_win = new (near_delete_btn,mContext, onClickListener);
        near_rootview = LayoutInflater.from(Near_resource_detail.this).inflate(R.layout.activity_main, null);

        init();
    }

    private void init() {
        for(int i=0;i<6;i++){
            LinearLayout label_lin =new LinearLayout(mContext);
            label_lin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            if(i<near_label_now_list.size()){
                Button button=new Button(this);
                button.setText(near_label_now_list.get(i));
                button.setHeight(100);
                button.setWidth(40);
                button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                label_lin.addView(button);
            }
            near_label_space.addView(label_lin);
        }

        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        Recy_adapter recy_adapter = new Recy_adapter(post, this);
        recy_adapter.setOnItemClickListener(new Recy_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int Position) {
                Intent intent=new Intent(mContext,Near_resource_detail.class);
                intent.putExtra("all",post);
                intent.putExtra("date",post.get(Position));
                startActivity(intent);
            }
        });
        //MyAdapter myAdapter= new MyAdapter(reply_lists,this);
        near_reply_list=findViewById(R.id.near_Recommend_list);
        near_reply_list.setFocusableInTouchMode(false);
        near_reply_list.requestFocus();
        near_reply_list.setNestedScrollingEnabled(false);
        near_reply_list.setLayoutManager(layoutManager);
        near_reply_list.setAdapter(recy_adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.near_back_btn:
                finish();
                break;
            case R.id.near_resource_message_btn:
                Intent i1 = new Intent(mContext, MessageList.class);
                startActivity(i1);
                break;
            case R.id.near_compile_btn:
                Intent w =new Intent(mContext,MessageDetails.class);
                w.putExtra("date",now.getUser_id());
                w.putExtra("name",now.getUser_name());
                startActivity(w);
                //compile_win.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.near_delete_btn:
                if(near_delete_btn.getText().equals("请求资源")){

                    request_reshource();
                }else {
                    Toast.makeText(mContext,"该资源正在下载或已经下载完成！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.near_all_evaluate:
                Intent m=new Intent(mContext,All_evaluate.class);
                startActivity(m);
                break;
        }

    }
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ViewGroup.LayoutParams lp;
        lp = near_Recommend_space.getLayoutParams();
        near_information_height=getStatusBarHeight(mContext);
        int height_13 = near_mScreenHeight - near_information_height - near_topview.getHeight() - near_mTopTabViewLayout.getHeight() - title_resource.getHeight() - near_recy_tail.getHeight();
        if(near_information_height!=0){
            lp.height = height_13;
        }

        //lp.height=mScreenHeight-information_height-topview.getHeight()-mTopTabViewLayout.getHeight()-title_resource.getHeight()-recy_tail.getHeight();

        near_Recommend_space.setLayoutParams(lp);
    }
    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public void request_reshource(){
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //    设置Title的图标
        builder.setIcon(R.mipmap.share);
        //    设置Title的内容
        builder.setTitle("操作确认！");
        //    设置Content来显示一个信息
        builder.setMessage("        确定消耗5积分来请求该资源吗？（请求失败不扣除积分）");
        //    设置一个PositiveButton
        builder.setPositiveButton("蓝牙下载", new DialogInterface.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(mContext,"正在下载", Toast.LENGTH_SHORT).show();
                near_delete_btn.setText("正在下载");
                String id =null;
                for (Customer p:customers){
                    if (p.getUsername().equals(now.getUser_name())){
                        id =p.getObjectId();
                    }
                }
                Log.v(now.getPath(),"wenjiandizhi");
                ((Share)getApplicationContext()).request_resource(id,now.getUser_name(),now.getPath());
                show3();
            }

        });
        builder.setNegativeButton("流量下载",new DialogInterface.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(mContext,"正在下载", Toast.LENGTH_SHORT).show();
                near_delete_btn.setText("正在下载");


//                File file=new File(now.getFile_url());
                BmobFile bmobFile=new BmobFile(now.getResource_name(),"",now.getFile_url());
//                path = "/storage/emulated/0/Android/data/com.example.chenwei.plus/cache/" + bmobFile.getFilename();
                downloadFile(bmobFile);


                show3();
            }
        });
        //    显示出该对话框
        //    设置一个NeutralButton
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(mContext,"取消请求", Toast.LENGTH_SHORT).show();
            }
        });
        //    显示出该对话框
        builder.show();
    }


    private  View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    private static final int NO_3 =0x3;

    @RequiresApi(api = Build.VERSION_CODES.O)

    public  void show3(){

        //NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
        notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        final String channelId = "channelId";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "下载资源";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        builder = new NotificationCompat.Builder(this, channelId);
        builder.setSmallIcon(R.mipmap.share);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.share));
        builder.setContentTitle("高等数学");
        builder.setContentText("正在下载");
        Notification notification = builder.build();
        notificationManager.notify(1, notification);
        builder.setProgress(100,0,false);

    }

    public void update(int progress_number){
        //下载以及安装线程模拟
        builder.setProgress(100,progress_number,false);
        Notification notification = builder.build();
        notificationManager.notify(1, notification);
        //下载进度提示
        builder.setContentText("下载"+progress_number+"%");
        if (progress_number==100){
            builder.setContentText("下载完成.");
            Notification notification1 = builder.build();
            notificationManager.notify(1, notification1);;
            notificationManager.cancel(1);//设置关闭通知栏
            Message msg = new Message();
            handle.sendMessage(msg);
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
    Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(mContext,"下载完成",Toast.LENGTH_SHORT).show();
            near_delete_btn.setText("下载完成");
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
                    Log.e("savePath","未插入"+now.getResource_name());

                    Near_resource_information resource_information=now;
                    SQLiteDatabase db = resourceDownload.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    values.put("user_id",resource_information.getUser_id());
                    values.put("distance",resource_information.getDistance());
                    values.put("person_name", resource_information.getUser_name());
                    values.put("resource_name", resource_information.getResource_name());
                    values.put("resource_grade", resource_information.getResource_grade());
                    values.put("price", resource_information.getPrice());
                    values.put("path", savePath);
                    values.put("label", resource_information.getLabel());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    values.put("time", df.format(new Date()));
                    values.put("size", resource_information.getSize());
                    values.put("introduction", resource_information.getIntroduction());
                    values.put("url",resource_information.getFile_url());

                    db.insert("ResourceDownload", null, values); //  插入第一条数据

                    Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                    intent.putExtra("download_refresh","refresh");
                    LocalBroadcastManager.getInstance(Near_resource_detail.this).sendBroadcast(intent);
                    sendBroadcast(intent);

                }else{
                    Toast.makeText(getApplicationContext(),"文件下载失败："+e.getErrorCode()+","+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("bmob","下载进度："+value+","+newworkSpeed);
            }

        });
    }
}
