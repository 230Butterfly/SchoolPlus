package com.example.chenwei.plus.Person.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.chenwei.plus.Login.Customer;
import com.example.chenwei.plus.Person.activity.ChangeInfo;
import com.example.chenwei.plus.Person.control.ChildAdapter;
import com.example.chenwei.plus.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;


public class ShowPhoto extends Activity {
    private static BmobUser user =BmobUser.getCurrentUser();
    private Customer customer=new Customer();
    private BmobFile bmobFile;
    private GridView mGridView;
    private List<String> list;
    private ChildAdapter adapter;
    String picPath;
    private Button submitButton;
    List<BmobObject> photo = new ArrayList<BmobObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);

        mGridView = (GridView) findViewById(R.id.child_grid);
        list = getIntent().getStringArrayListExtra("data");

        adapter = new ChildAdapter(this, list, mGridView);
        mGridView.setAdapter(adapter);

        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setId(0);
        submitButton.setOnClickListener(submit);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "选中 " + adapter.getSelectItems().size() + " item", Toast.LENGTH_LONG).show();

        super.onBackPressed();
    }
    private Button.OnClickListener submit=new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            while (true){
                if(adapter.getCount()!=0){
                    if (v.getId() == 0) {
//									picPath = adapter.getSelectItemsPath().get(0);
//					bmobFile = new BmobFile(new File(picPath));
//					bmobFile.uploadblock(new UploadFileListener() {
//
//						@Override
//						public void done(BmobException e) {
//							if(e==null){
//								//bmobFile.getFileUrl()--返回的上传文件的完整地址
//								customer.setIcon(bmobFile);
//								customer.update(BmobUser.getCurrentUser().getObjectId(), new UpdateListener() {
//									@Override
//									public void done(BmobException e) {
//										if(e==null){
//											Log.i("bmob","更新成功");
//										}else{
//											Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
//										}
//									}
//								});
//								Toast.makeText(ShowPhotoActivity.this,"上传文件成功:" + bmobFile.getFileUrl(),Toast.LENGTH_LONG).show();
//							}else{
//								Toast.makeText(ShowPhotoActivity.this,"上传文件失败：" + e.getMessage(),Toast.LENGTH_LONG).show();
//							}
//						}
//
//						@Override
//						public void onProgress(Integer value) {
//							// 返回的上传进度（百分比）
//						}
//					});
//第二种方式：v3.5.0开始提供
//逐个删除该用户的图库(点击删除图标），然后逐个添加（bmobFile）（点击添加图标）
                    }
                    Intent intent=new Intent(ShowPhoto.this,ChangeInfo.class);

                    startActivity(intent);
                    finish();
                    break;
                }
                else{
//				Intent intent=new Intent(ShowPhotoActivity.this,ShowPhotoActivity.class);
//				startActivity(intent);
//				finish();
                    Toast.makeText(ShowPhoto.this,"请选择一张照片",Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
    };

//	public void copyFile(String oldPath, String newPath) {
//		try {
//			int bytesum = 0;
//			int byteread = 0;
//			File oldfile = new File(oldPath);
//			if (oldfile.exists()) { //文件存在时
//				InputStream inStream = new FileInputStream(oldPath); //读入原文件
//				FileOutputStream fs = new FileOutputStream(newPath);
//				byte[] buffer = new byte[1444];
//				while ( (byteread = inStream.read(buffer)) != -1) {
//					bytesum += byteread; //字节数 文件大小
//					System.out.println(bytesum);
//					fs.write(buffer, 0, byteread);
//				}
//				inStream.close();
//			}
//		}
//		catch (Exception e) {
//			System.out.println("复制单个文件操作出错");
//			e.printStackTrace();
//
//		}
//
//	}

}