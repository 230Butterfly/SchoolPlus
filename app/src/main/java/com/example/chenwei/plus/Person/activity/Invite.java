package com.example.chenwei.plus.Person.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenwei.plus.Login.Customer;
import com.example.chenwei.plus.R;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class Invite extends AppCompatActivity {

    private int REQUEST_PATH = 0;
    private RelativeLayout backLayout;
    private String picPath;
    private String name;

    private ImageView iconView;
    private TextView nameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        picPath = getIntent().getStringExtra("path");

        name = getIntent().getStringExtra("name");

        findViews();
        setListener();

        backLayout=findViewById(R.id.evaluate_down_back_btn);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void findViews() {

        iconView = (ImageView) findViewById(R.id.icon);
        nameView = (TextView) findViewById(R.id.name);
//        Intent intent = getIntent();
//        String path = intent.getStringExtra("path");
//        String name=intent.getStringExtra("name");
//
//        nameView.setText(name);
//        Bitmap bm = BitmapFactory.decodeFile(path);
//                    iconView.setImageBitmap(bm);

    }
    private void setListener() {

        nameView.setText(Customer.getCurrentUser().getUsername());
        BmobQuery<Customer> query = new BmobQuery<Customer>();
        query.getObject(Customer.getCurrentUser().getObjectId(), new QueryListener<Customer>() {

            @Override
            public void done(Customer object, BmobException e) {
                if (e == null) {
                    BmobFile bmobFile = object.getIcon();

//                    final String path= getApplicationContext().getCacheDir()+"/bmob/" +bmobFile.getFilename();
                    final String path = "/storage/emulated/0/" + bmobFile.getFilename();
                    picPath=path;
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
                                        if (bmobfile != null) {
                                            //调用bmobfile.download方法
                                            downloadFile(bmobfile);
                                        }
                                    }
                                } else {

                                }
                            }
                        });
                    }
                    Bitmap bm = BitmapFactory.decodeFile(path);
                    iconView.setImageBitmap(bm);


                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });

    }

    private void downloadFile(BmobFile file){
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        File saveFile = new File(Environment.getExternalStorageDirectory(), file.getFilename());
        file.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {
//                Toast.makeText(getActivity(),"开始下载。。。",Toast.LENGTH_LONG).show();
            }


            @Override
            public void done(String savePath,BmobException e) {
                if(e==null){
//                    Toast.makeText(getActivity(),"下载成功,保存路径:"+savePath,Toast.LENGTH_LONG).show();
                    onCreate(null);
                }else{
                    Toast.makeText(getApplicationContext(),"头像获取失败："+e.getErrorCode()+","+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("bmob","下载进度："+value+","+newworkSpeed);
            }

        });
    }
}
