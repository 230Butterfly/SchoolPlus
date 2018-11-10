package com.example.chenwei.plus.Resource;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenwei.plus.Home.*;
import com.example.chenwei.plus.Messages.MessageList;
import com.example.chenwei.plus.Near.Near_resource_detail;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Tool.MyScrollView;
import com.example.chenwei.plus.Tool.Near_resource_information;
import com.example.chenwei.plus.Tool.Recy_adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Resource_download extends AppCompatActivity implements View.OnClickListener {

    private Context context=this;
    //顶部固定的TabViewLayout
    private LinearLayout mTopTabViewLayout;

    //跟随ScrollView的TabviewLayout
    private LinearLayout mTabViewLayout;

    //要悬浮在顶部的View的子View
    private LinearLayout mTopView;
    private RelativeLayout back_btn,title_resource;
    private RelativeLayout message_btn;
    private LinearLayout label_btn;
    private LinearLayout label_space;
    private LinearLayout evaluate_space;
    private TextView information_text;
    private Button compile_btn;
    private Button delete_btn;
    private RecyclerView reply_list;
    private ArrayList<Near_resource_information> reply_lists =new ArrayList<>();
    private LinearLayout Recycler_header;
    private LinearLayout detail_lin;
    private TextWatcher username_watcher;
    private Evaluate_win evaluate_win;
    private View rootview;
    private Label_win label_win;

    private LinearLayout heigth_stand,all_evaluate;
    private LinearLayout topview,intro_co,intro_re,center_co,recy_tail,evaluate_height,Recommend_space;
    private View soild_view,center_div;
    private int mScreenWidth;//屏宽
    private int mScreenHeight;//屏高
    private int information_height;
    private ArrayList<String> label_now_list;
    private RelativeLayout resource_down_message_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resource_download);
        label_now_list=new ArrayList<>();

        label_now_list.add("视频");
        label_now_list.add("孩童");
        label_now_list.add("书籍");
        label_now_list.add("程序");

        Display display = getWindowManager().getDefaultDisplay(); //Activity#getWindowManager()
        Point size = new Point();
        display.getSize(size);
        mScreenWidth = size.x;
        mScreenHeight = size.y;

        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        information_height= rect.top;

        Recommend_space=findViewById(R.id.Recommend_space);
        recy_tail=findViewById(R.id.down_recy_tail);
        center_div=findViewById(R.id.down_center_div);
        center_co=findViewById(R.id.down_center_co);
        soild_view=findViewById(R.id.down_soild_view);
        topview=findViewById(R.id.down_topview);
        intro_co=findViewById(R.id.down_intro_co);
        intro_re=findViewById(R.id.down_intro_re);
        final MyScrollView mMyScrollView = (MyScrollView) findViewById(R.id.down_my_scrollview);
        mTabViewLayout = (LinearLayout) findViewById(R.id.down_ll_tabView);
        mTopTabViewLayout = (LinearLayout) findViewById(R.id.down_ll_tabTopView);
        mTopView = (LinearLayout) findViewById(R.id.down_tv_topView);
        heigth_stand =findViewById(R.id.down_height_stand);
        evaluate_height=findViewById(R.id.evaluate_height);

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
                if (scrollY > 0 && scrollY >= mHeight+center_div.getHeight()+center_co.getHeight()+evaluate_height.getHeight()+20) {
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

        detail_lin=findViewById(R.id.down_detail_lin);
        back_btn =findViewById(R.id.down_back_btn);
        message_btn=findViewById(R.id.resource_down_message_btn);
        label_space=findViewById(R.id.down_label_space);
        evaluate_space=findViewById(R.id.down_evaluate_space);
        information_text =findViewById(R.id.down_information_text);
        compile_btn =findViewById(R.id.down_compile_btn);
        delete_btn =findViewById(R.id.down_delete_btn);
        all_evaluate=findViewById(R.id.all_evaluate);
        all_evaluate.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        message_btn.setOnClickListener(this);
        compile_btn.setOnClickListener(this);
        delete_btn.setOnClickListener(this);

        title_resource=findViewById(R.id.down_title_resource);
        evaluate_win = new Evaluate_win(delete_btn,context, onClickListener);
        rootview = LayoutInflater.from(Resource_download.this).inflate(R.layout.activity_main, null);

        init();
    }

    private void init() {
        for(int i=0;i<6;i++){
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

        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        Recy_adapter recy_adapter = new Recy_adapter(reply_lists, this);
        //MyAdapter myAdapter= new MyAdapter(reply_lists,this);
        recy_adapter.setOnItemClickListener(new Recy_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int Position) {
                Intent intent=new Intent(context,Near_resource_detail.class);
                startActivity(intent);
            }
        });
        reply_list=findViewById(R.id.Recommend_list);
        reply_list.setFocusableInTouchMode(false);
        reply_list.requestFocus();
        reply_list.setNestedScrollingEnabled(false);
        reply_list.setLayoutManager(layoutManager);
        reply_list.setAdapter(recy_adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.down_back_btn:
                finish();
                break;
            case R.id.resource_down_message_btn:
                Intent i = new Intent(context, MessageList.class);
                startActivity(i);
                break;
            case R.id.down_compile_btn:
                //compile_win.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.down_delete_btn:
                if(((Button)v).getText().equals("评价资源")){
                    evaluate_win.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
                    //((Button)v).setText("删除资源");
                }else{
                    delete_reshource();
                }
                break;
            case R.id.all_evaluate:
                Intent m=new Intent(context,All_evaluate.class);
                startActivity(m);
                break;
        }

    }

    public void delete_reshource(){
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //    设置Title的图标
        builder.setIcon(R.mipmap.share);
        //    设置Title的内容
        builder.setTitle("操作确认");
        //    设置Content来显示一个信息
        builder.setMessage("        确定删除该资源吗？（重新下载会需要重复积分）");
        //    设置一个PositiveButton
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(context,"该资源成功删除", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        //    设置一个NeutralButton
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(context,"取消删除", Toast.LENGTH_SHORT).show();
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

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ViewGroup.LayoutParams lp;
        lp = Recommend_space.getLayoutParams();
        information_height=getStatusBarHeight(context);
        int height_13 = mScreenHeight - information_height - topview.getHeight() - mTopTabViewLayout.getHeight() - title_resource.getHeight() - recy_tail.getHeight();
        if(information_height!=0){
            lp.height = height_13;
        }

        //lp.height=mScreenHeight-information_height-topview.getHeight()-mTopTabViewLayout.getHeight()-title_resource.getHeight()-recy_tail.getHeight();

        Recommend_space.setLayoutParams(lp);
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
}
