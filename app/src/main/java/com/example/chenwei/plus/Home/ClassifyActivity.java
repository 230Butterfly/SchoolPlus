package com.example.chenwei.plus.Home;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Tool.Near_resource_information;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassifyActivity extends AppCompatActivity {
    private RecyclerView rv_title;
    private ArrayList<String> s;
    //private Fragment_near f;
    private ClassifyContent classifyContent;
    private static int mPosition;
    private String current_title;
    private ArrayList<Near_resource_information> posts =null;
    private RelativeLayout back_btn_classify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        Intent intent = getIntent();
        current_title = (String) intent.getSerializableExtra("title");
        posts = (ArrayList<Near_resource_information>) intent.getSerializableExtra("date");
        rv_title=findViewById(R.id.rv_title);
        s = new ArrayList<String>(Arrays.asList("教育","视频","书籍","音乐","娱乐","体育","应用","图片","理科","工科","文科","计算机","游戏","PPT","EXCEL","WORD"));

        //s= new ArrayList<String>();
       for (int i=0;i<16;i++){
            if((s.get(i)).equals(current_title))
                mPosition = i;
        }

        back_btn_classify =findViewById(R.id.back_btn_classify);
        back_btn_classify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*for (int i=0;i<20;i++){
            if(("123"+i).equals(current_title))
                mPosition = i;
            s.add("123"+i);
        }*/
        final SorttitleAdapter adapter=new SorttitleAdapter(s,this,mPosition);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_title.setLayoutManager(layoutManager);
        rv_title.setAdapter(adapter);
        ArrayList<Near_resource_information> near_resource_informations =new ArrayList<>();
        String label =s.get(mPosition);
        for (Near_resource_information p:posts){
            String lb[] =p.getLabel().split("#");
            for (int i=0;i<p.getLabel().split("#").length;i++){
                if (lb[i].equals(label)){
                    near_resource_informations.add(p);
                    break;
                }
            }
        }
        classifyContent=new ClassifyContent(mPosition,near_resource_informations);
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.resource_content,classifyContent);
        fragmentTransaction.commit();

        adapter.setOnItemClickListener(new SorttitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int Position) {
                ArrayList<Near_resource_information> near_resource_informations =new ArrayList<>();
                String label =s.get(Position);
                for (Near_resource_information p:posts){
                    String lb[] =p.getLabel().split("#");
                    for (int i=0;i<p.getLabel().split("#").length;i++){
                        if (lb[i].equals(label)){
                            near_resource_informations.add(p);
                            break;
                        }
                    }
                }
                //totalHeight=rv_title.getMeasuredHeight();
                //rv_title.scrollBy(0,123);
                moveToMiddle(view);
                adapter.notifyDataSetChanged();
                for(int i=0;i<s.size();i++){
                    classifyContent=new ClassifyContent(Position,near_resource_informations);
                    FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.resource_content,classifyContent);
                    fragmentTransaction.commit();
                }
                //Log.e("tag",s.get(Position));

            }
        });
    }
    public void moveToMiddle(View clickedView){
            int itemHeight=clickedView.getHeight();
            int screenHeight=getResources().getDisplayMetrics().heightPixels;
            int scrollHeight=clickedView.getTop()-(screenHeight/2-itemHeight/2);
            rv_title.scrollBy(0,scrollHeight);
    }
}
