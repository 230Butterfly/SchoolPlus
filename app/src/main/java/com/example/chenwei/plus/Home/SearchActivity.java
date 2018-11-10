package com.example.chenwei.plus.Home;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DrawableUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenwei.plus.R;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private FlowLayout fl_history;
    private FlowLayout fl_recommend;
    private SearchView sv_search;
    private TextView tv_history;
    private TextView tv_recommend;
    private ImageView iv_history;
    private AutoCompleteTextView actv;
    private RelativeLayout rl_history;
    private ArrayList<String> history;
    private RelativeLayout back_btn_search;
    private String[] data =  new String[]{"热门推荐","这是","测试标签",
            "这是测试标签","FlowLayout","衣服","鞋子",
            "春","夏","深秋","寒冬",
            "测一下看看效果如何","心情还不错哦","这是测试标签",
            "这是测试标签","受益匪浅啊","123456789","电话号码"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();
        initData1();

    }

    private void initView() {
        back_btn_search=findViewById(R.id.back_btn_search);
        back_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fl_history= (FlowLayout) findViewById(R.id.fl_history);
        fl_recommend=findViewById(R.id.fl_recommend);
        rl_history=findViewById(R.id.rl_history);
        tv_history=findViewById(R.id.tv_history);
        iv_history=findViewById(R.id.iv_history);
        tv_recommend=findViewById(R.id.tv_recommend);
        iv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ll_history.setVisibility(View.GONE);
                delete_history();
            }
        });

        tv_history.setTypeface(Typeface.DEFAULT_BOLD);
        tv_recommend.setTypeface(Typeface.DEFAULT_BOLD);
        sv_search =(SearchView) findViewById(R.id.sv_search);
        sv_search.setSubmitButtonEnabled(true);
        // 设置该SearchView默认是否自动缩小为图标
        sv_search.setIconifiedByDefault(false);
        sv_search.setQueryHint("开始搜索之旅");
        int id = sv_search.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) sv_search.findViewById(id);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);


    }

    private void initData() {
        history = new ArrayList<String>();
        for(int i=0;i<10;i++){
            history.add("历史记录"+i);
        }
        int padding = dip2px(5);
        fl_history.setPadding(padding,padding,padding,padding);// 设置内边距
        for (int i = 0; i < history.size(); i++) {
            final String tag = history.get(i);
            TextView tv = new TextView(this);
            tv.setText(tag);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv.setPadding(2*padding,padding,2*padding,padding);
            tv.setGravity(Gravity.CENTER);
            //Paint mp = new Paint();
            //Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
            tv.setTypeface(Typeface.DEFAULT);
            StateListDrawable selector=new StateListDrawable();
            int pressed = android.R.attr.state_pressed;
            selector.addState(new int []{-pressed}, getResources().getDrawable(R.drawable.text_tag_normal));
            selector.addState(new int []{pressed}, getResources().getDrawable(R.drawable.text_tag_pressed));
            tv.setBackground(selector);
            fl_history.addView(tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doDisplay(tag);
                }
            });
        }

    }
    private void initData1() {
        int padding = dip2px(5);
        fl_recommend.setPadding(padding,padding,padding,padding);// 设置内边距
        for (int i = 0; i < data.length; i++) {
            final String tag = data[i];
            TextView tv = new TextView(this);
            tv.setText(tag);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv.setPadding(2*padding,padding,2*padding,padding);

            tv.setGravity(Gravity.CENTER);
            //Paint mp = new Paint();
            //Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
            tv.setTypeface(Typeface.DEFAULT);
            StateListDrawable selector=new StateListDrawable();
            StateListDrawable selector1=new StateListDrawable();
            int pressed = android.R.attr.state_pressed;
            //int focused = android.R.attr.state_focused;
            selector.addState(new int []{-pressed}, getResources().getDrawable(R.drawable.text_tag_normal));
            selector.addState(new int []{pressed}, getResources().getDrawable(R.drawable.text_tag_pressed));
            selector1.addState(new int []{}, getResources().getDrawable(R.drawable.text_first_tag));
            //selector.addState(new int []{focused}, getResources().getDrawable(R.drawable.text_tag_pressed));
            if (i==0){
                tv.setTextColor(getResources().getColor(R.color.text_yellow));
                //selector = DrawableUtils.getSelector(false,Color.parseColor("#2c90d7"), color, dip2px(30));
                tv.setBackground(selector1);
            }else {
                //selector = DrawableUtils.getSelector(true,Color.WHITE, color, dip2px(30));
                tv.setBackground(selector);
            }

            fl_recommend.addView(tv);
            tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    doDisplay(tag);

                }
            });
        }

    }

    public int dip2px(float dip) {
        float density = this.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }
    public void delete_history(){
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //    设置Title的图标
        //builder.setIcon(R.mipmap.person_active);
        //    设置Title的内容
        builder.setTitle("确认删除所有历史记录？");
        //    设置Content来显示一个信息
        //builder.setMessage("        确定消耗5积分来请求该资源吗？（请求失败不扣除积分）");
        //    设置一个PositiveButton
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(SearchActivity.this,"开始下载", Toast.LENGTH_SHORT).show();
                //fl_history.setVisibility(View.GONE);
                rl_history.setVisibility(View.GONE);
                //near_delete_btn.setText("正在下载");
                //show3();
            }
        });
        //    设置一个NeutralButton
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Toast.makeText(SearchActivity.this,"取消请求", Toast.LENGTH_SHORT).show();
            }
        });
        //    显示出该对话框
        builder.show();
    }
    //将标签文字显示到searchview
    private void doDisplay(String tag){
        int id = sv_search.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) sv_search.findViewById(id);
        textView.setText(tag);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        int completeTextId = sv_search.getResources().getIdentifier("android:id/search_src_text", null, null);
        AutoCompleteTextView completeText = (AutoCompleteTextView) sv_search
                .findViewById(completeTextId) ;
        completeText.setSelection(tag.length());
    }
}
