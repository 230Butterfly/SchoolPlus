package com.example.chenwei.plus.Resource;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenwei.plus.MainActivity;
import com.example.chenwei.plus.Messages.MessageList;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Tool.MyScrollView;
import com.example.chenwei.plus.Tool.Near_resource_information;
import com.example.chenwei.plus.Upload.bean.ResourceUpload;
import com.example.chenwei.plus.Upload.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.bmob.v3.b.This;


public class Detail_resource extends AppCompatActivity implements View.OnClickListener {


    private Context context=this;
    //顶部固定的TabViewLayout
    private LinearLayout mTopTabViewLayout;

   //跟随ScrollView的TabviewLayout
    private LinearLayout mTabViewLayout;

    //要悬浮在顶部的View的子View
    private TextView tv_resource_name;
    private TextView tv_resource_uploader;
    private TextView tv_resource_mark;
    private TextView tv_resource_time;
    private TextView tv_resource_size;
    private TextView tv_resource_score;
    private TextView all_evaluate_btn;;
    private ImageView all_evaluate_btn1;
    private LinearLayout mTopView;
    private RelativeLayout back_btn,title_resource;
    private RelativeLayout message_btn;
    private LinearLayout label_btn;
    private LinearLayout label_space;
    private LinearLayout evaluate_space;
    private EditText information_text;
    private Button compile_btn;
    private Button delete_btn;
    private RecyclerView reply_list;
    private ArrayList<Reply_list> reply_lists =new ArrayList<>();
    private LinearLayout Recycler_header;
    private LinearLayout detail_lin;
    private TextWatcher username_watcher;
    private Compile_win compile_win;
    private View rootview;
    private Label_win label_win;
    private ImageView resource_bitmap;


    private LinearLayout heigth_stand;
    private LinearLayout topview,intro_co,intro_re,center_co,recy_tail;
    private View soild_view,center_div;
    private int mScreenWidth;//屏宽
    private int mScreenHeight;//屏高
    private int information_height;
    private ArrayList<String> label_now_list;
    ResourceUpload currentResource;
    RelativeLayout resource_detail_message_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_resource);
        Intent i = getIntent();

        currentResource= (ResourceUpload)i.getSerializableExtra("ResourceUpload");

        String s = (String)i.getSerializableExtra("username");
        resource_bitmap=(ImageView)findViewById(R.id.bitmap);
        Bitmap bitmap = null;
        File file=new File(currentResource.getPath());
        try {
            bitmap= FileUtil.fileGetBitmap(getApplicationContext(),file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resource_bitmap.setImageBitmap(bitmap);
        label_now_list=new ArrayList<>();
        /*label_now_list.add("视频");
        label_now_list.add("孩童");
        label_now_list.add("书籍");
        label_now_list.add("程序");*/
        label_now_list.add(currentResource.getLabel());
        //获取手机屏幕的宽高
        Display display = getWindowManager().getDefaultDisplay(); //Activity#getWindowManager()
        Point size = new Point();
        display.getSize(size);
        mScreenWidth = size.x;
        mScreenHeight = size.y;

        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        information_height= rect.top;

        tv_resource_name=findViewById(R.id.tv_resource_name);
        tv_resource_uploader=findViewById(R.id.tv_resource_uploader);
        tv_resource_mark=findViewById(R.id.tv_resource_mark);
        tv_resource_time=findViewById(R.id.tv_resource_time);
        tv_resource_size=findViewById(R.id.tv_resource_size);
        tv_resource_score=findViewById(R.id.tv_resource_score);
        tv_resource_name.setText(currentResource.getName());
        tv_resource_uploader.setText(s);
        tv_resource_time.setText(currentResource.getCreatedAt());
        tv_resource_size.setText(getPrintSize(Long.parseLong(currentResource.getSize())));
        tv_resource_score.setText("评分:"+currentResource.getInterger());
        recy_tail=findViewById(R.id.recy_tail);
        center_div=findViewById(R.id.center_div);
        center_co=findViewById(R.id.center_co);
        soild_view=findViewById(R.id.soild_view);
        topview=findViewById(R.id.topview);
        intro_co=findViewById(R.id.intro_co);
        intro_re=findViewById(R.id.intro_re);
        final MyScrollView mMyScrollView = (MyScrollView) findViewById(R.id.my_scrollview);
        mTabViewLayout = (LinearLayout) findViewById(R.id.ll_tabView);
        mTopTabViewLayout = (LinearLayout) findViewById(R.id.ll_tabTopView);
        mTopView = (LinearLayout) findViewById(R.id.tv_topView);
        heigth_stand =findViewById(R.id.height_stand);


        //滑动监听
        mMyScrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                int mHeight = mTabViewLayout.getTop();
                int c=intro_re.getTop();
                //判断滑动距离scrollY是否大于0，因为大于0的时候就是可以滑动了，此时mTabViewLayout.getTop()才能取到值。
                if((mMyScrollView.getScrollY() + mMyScrollView.getHeight()) >= mMyScrollView.getcomputeVerticalScrollRange())
                {
                    reply_list.setNestedScrollingEnabled(true);
                }else {
                    reply_list.setNestedScrollingEnabled(false);
                }
                if (scrollY > 0 && scrollY >= mHeight+center_div.getHeight()+center_co.getHeight()+20) {
                    if (intro_co.getParent() != topview) {
                        intro_re.removeView(intro_co);
                        intro_re.removeView(soild_view);
                        topview.addView(intro_co);
                        topview.addView(soild_view);
                    }
                }
                else{
                    if (intro_co.getParent() != intro_re) {
                        topview.removeView(intro_co);
                        topview.removeView(soild_view);
                        intro_re.addView(intro_co);
                        intro_re.addView(soild_view);
                    }
                }

                if (scrollY > 0 && scrollY >= mHeight) {
                    if (mTopView.getParent() != mTopTabViewLayout) {
                        mTabViewLayout.removeView(mTopView);
                        mTopTabViewLayout.addView(mTopView);
                    }
                }
                else{
                        if (mTopView.getParent() != mTabViewLayout) {
                            mTopTabViewLayout.removeView(mTopView);
                            mTabViewLayout.addView(mTopView);
                            reply_list.setNestedScrollingEnabled(false);
                        }
                    }
                }

        });

        //..
        all_evaluate_btn=findViewById(R.id.all_evaluate_btn);
        all_evaluate_btn1=findViewById(R.id.all_evaluate_btn1);
        detail_lin=findViewById(R.id.detail_lin);
        back_btn =findViewById(R.id.back_btn);
        message_btn=findViewById(R.id.resourcemn_detail_message_btn);
        label_btn=findViewById(R.id.label_btn);
        label_space=findViewById(R.id.label_space);
        evaluate_space=findViewById(R.id.evaluate_space);
        information_text =findViewById(R.id.information_text);
        information_text.setText(currentResource.getIntroduction());
        compile_btn =findViewById(R.id.compile_btn);
        delete_btn =findViewById(R.id.delete_btn);

        information_text.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        message_btn.setOnClickListener(this);
        label_btn.setOnClickListener(this);
        evaluate_space.setOnClickListener(this);
        compile_btn.setOnClickListener(this);
        delete_btn.setOnClickListener(this);
        all_evaluate_btn.setOnClickListener(this);
        all_evaluate_btn1.setOnClickListener(this);
        resource_bitmap.setOnClickListener(this);
        title_resource=findViewById(R.id.title_resource);
        information_text.setCursorVisible(false);
        initWatcher();
        information_text.addTextChangedListener(username_watcher);

        compile_win = new Compile_win(context, onClickListener);
        label_win = new Label_win(label_now_list,label_space,context, onClickListener);
        //showAtLocation(View parent, int gravity, int x, int y)
        rootview = LayoutInflater.from(Detail_resource.this).inflate(R.layout.activity_main, null);


        init();
    }

    private void initWatcher() {
        username_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if(detail_lin.getChildCount()<=1){
                    Button button_confirm=new Button(context);
                    button_confirm.setText("取消");
                    detail_lin.addView(button_confirm);
                    Button button =new Button(context);
                    button.setText("保存");
                    detail_lin.addView(button);
                    button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                    button_confirm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            information_text.setCursorVisible(false);
                            Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
                            detail_lin.removeViewAt(0);
                            detail_lin.removeViewAt(0);
                        }
                    });
                    button_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            information_text.setCursorVisible(false);
                            Toast.makeText(context,"修改取消",Toast.LENGTH_SHORT).show();
                            detail_lin.removeViewAt(1);
                            detail_lin.removeViewAt(0);
                        }
                    });
                }
            }
        };
    }

    private void init() {

        for(int i=0;i<5;i++){
            LinearLayout label_lin =new LinearLayout(context);
            label_lin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            if(i<label_now_list.size()){
                Button button=new Button(this);
                button.setText(label_now_list.get(i));
                button.setHeight(100);
                button.setWidth(40);
                button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                label_lin.addView(button);
            }
            label_space.addView(label_lin);
        }
        for(int i=0;i<30;i++){
            Reply_list replyList =new Reply_list(null,"我是你浩哥","        "+"这个其实真的不错啊",7,"1997.09.29");
            reply_lists.add(replyList);
        }
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        MyAdapter myAdapter= new MyAdapter(reply_lists,this);
        reply_list=findViewById(R.id.reply_list);
        reply_list.setFocusableInTouchMode(false);
        reply_list.requestFocus();
        reply_list.setNestedScrollingEnabled(false);
        reply_list.setLayoutManager(layoutManager);
        reply_list.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.resourcemn_detail_message_btn:
                Intent i = new Intent(context, MessageList.class);
                startActivity(i);
                break;
            case R.id.label_btn:
                label_win = new Label_win(label_now_list,label_space,context, onClickListener);
               label_win.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.evaluate_space:
                break;
            case R.id.compile_btn:
                compile_win.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.delete_btn:
                delete_reshource();
                break;
            case R.id.information_text:
                information_text.setCursorVisible(true);
                information_text.requestFocus();
                break;
            case R.id.all_evaluate_btn:
                Intent m=new Intent(context,All_evaluate.class);
                startActivity(m);
                break;
            case R.id.all_evaluate_btn1:
                Intent m1=new Intent(context,All_evaluate.class);
                startActivity(m1);
                break;
            case R.id.bitmap:
                Intent intent = new Intent();
                File file = new File(currentResource.getPath());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//设置标记
                intent.setAction(Intent.ACTION_VIEW);//动作，查看
                intent.setDataAndType(Uri.fromFile(file), getMIMEType(file));//设置类型
                context.startActivity(intent);
        }
    }

    private  View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.confirm_btn:
                    Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
                    compile_win.dismiss();
                    compile_win = new Compile_win(context, onClickListener);
                    break;
                case R.id.resource_change_btn:
                    Toast.makeText(context,"更换资源",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.picture_btn:
                    Toast.makeText(context,"修改图片",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ViewGroup.LayoutParams lp;
        lp = evaluate_space.getLayoutParams();
        information_height=getStatusBarHeight(context);
        int height_13 = mScreenHeight - information_height - topview.getHeight() - mTopTabViewLayout.getHeight() - title_resource.getHeight() - recy_tail.getHeight();
        if(information_height!=0){
            lp.height = height_13;
        }

        //lp.height=mScreenHeight-information_height-topview.getHeight()-mTopTabViewLayout.getHeight()-title_resource.getHeight()-recy_tail.getHeight();

        evaluate_space.setLayoutParams(lp);
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

    public void delete_reshource(){
            //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            //    设置Title的图标
            builder.setIcon(R.mipmap.share);
            //    设置Title的内容
            builder.setTitle("确定将此资源下架吗？");
            //    设置Content来显示一个信息
            builder.setMessage("        下架后别人无法下载此资源，且您将无法享受该资源带来的积分收益!");
            //    设置一个PositiveButton
            builder.setPositiveButton("下架", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Toast.makeText(context,"该资源成功下架", Toast.LENGTH_SHORT).show();
                }
            });
            //    设置一个NegativeButton
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Toast.makeText(context, "取消下架", Toast.LENGTH_SHORT).show();
                }
            });
            //    设置一个NeutralButton
            builder.setNeutralButton("忽略", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Toast.makeText(context,"取消下架", Toast.LENGTH_SHORT).show();
                }
            });
            //    显示出该对话框
            builder.show();
        }

        public void setLabel_now_list(ArrayList<String> m){
        label_now_list=m;
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
    private String getMIMEType(File file) {

        String type="*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0)
            return type;
        /* 获取文件的后缀名 */
        String fileType = fName.substring(dotIndex,fName.length()).toLowerCase();
        if(fileType == null || "".equals(fileType))
            return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<MIME_MapTable.length;i++){
            if(fileType.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }
    private static final String[][] MIME_MapTable={
            //{后缀名，    MIME类型}
            {".3gp",    "video/3gpp"},
            {".apk",    "application/vnd.android.package-archive"},
            {".asf",    "video/x-ms-asf"},
            {".avi",    "video/x-msvideo"},
            {".bin",    "application/octet-stream"},
            {".bmp",      "image/bmp"},
            {".c",        "text/plain"},
            {".class",    "application/octet-stream"},
            {".conf",    "text/plain"},
            {".cpp",    "text/plain"},
            {".doc",    "application/msword"},
            {".exe",    "application/octet-stream"},
            {".gif",    "image/gif"},
            {".gtar",    "application/x-gtar"},
            {".gz",        "application/x-gzip"},
            {".h",        "text/plain"},
            {".htm",    "text/html"},
            {".html",    "text/html"},
            {".jar",    "application/java-archive"},
            {".java",    "text/plain"},
            {".jpeg",    "image/jpeg"},
            {".jpg",    "image/jpeg"},
            {".js",        "application/x-javascript"},
            {".log",    "text/plain"},
            {".m3u",    "audio/x-mpegurl"},
            {".m4a",    "audio/mp4a-latm"},
            {".m4b",    "audio/mp4a-latm"},
            {".m4p",    "audio/mp4a-latm"},
            {".m4u",    "video/vnd.mpegurl"},
            {".m4v",    "video/x-m4v"},
            {".mov",    "video/quicktime"},
            {".mp2",    "audio/x-mpeg"},
            {".mp3",    "audio/x-mpeg"},
            {".mp4",    "video/mp4"},
            {".mpc",    "application/vnd.mpohun.certificate"},
            {".mpe",    "video/mpeg"},
            {".mpeg",    "video/mpeg"},
            {".mpg",    "video/mpeg"},
            {".mpg4",    "video/mp4"},
            {".mpga",    "audio/mpeg"},
            {".msg",    "application/vnd.ms-outlook"},
            {".ogg",    "audio/ogg"},
            {".pdf",    "application/pdf"},
            {".png",    "image/png"},
            {".pps",    "application/vnd.ms-powerpoint"},
            {".ppt",    "application/vnd.ms-powerpoint"},
            {".prop",    "text/plain"},
            {".rar",    "application/x-rar-compressed"},
            {".rc",        "text/plain"},
            {".rmvb",    "audio/x-pn-realaudio"},
            {".rtf",    "application/rtf"},
            {".sh",        "text/plain"},
            {".tar",    "application/x-tar"},
            {".tgz",    "application/x-compressed"},
            {".txt",    "text/plain"},
            {".wav",    "audio/x-wav"},
            {".wma",    "audio/x-ms-wma"},
            {".wmv",    "audio/x-ms-wmv"},
            {".wps",    "application/vnd.ms-works"},
            //{".xml",    "text/xml"},
            {".xml",    "text/plain"},
            {".z",        "application/x-compress"},
            {".zip",    "application/zip"},
            {"",        "*/*"}
    };
}
