package com.example.chenwei.plus.Person.activity;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chenwei.plus.Login.Customer;
import com.example.chenwei.plus.Person.control.CropPhotoUtil;
import com.example.chenwei.plus.Person.control.ImageUtils;
import com.example.chenwei.plus.R;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ChangeIcon extends AppCompatActivity {

    private static final int CODE_REQUEST_CAMERA_PHOTO = 200;
    private static final int CODE_REQUEST_CROP_PHOTO = 201;
    private static final int CODE_REQUEST_PICTURE = 202;
    private ImageView pictureImg;
    private String mCurrentPhotoName;
    private String mNewCurrentPhotoName;
    private String path;
    private String picPath;
    private String oldFileUrl;
    private Customer customer = new Customer();
    private RelativeLayout backLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }


//        checkPermission();
        setContentView(R.layout.activity_change_icon);
        picPath = getIntent().getStringExtra("path");

        backLayout=(RelativeLayout) findViewById(R.id.evaluate_down_back_btn);

        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton cameraBtn = (ImageButton) findViewById(R.id.btn_take_camera);
        ImageButton photoBtn = (ImageButton) findViewById(R.id.btn_take_photo);
        pictureImg = (ImageView) findViewById(R.id.img_picture);

        if(picPath!=null){
            Uri uri = Uri.parse(picPath);
            pictureImg.setImageURI(uri);
        }

        assert cameraBtn != null;
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mCurrentPhotoName = System.currentTimeMillis() + ".jpg";
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), mCurrentPhotoName)));
                startActivityForResult(intent, CODE_REQUEST_CAMERA_PHOTO);
            }
        });
        assert photoBtn != null;
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*");//选择图片
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentFromGallery, CODE_REQUEST_PICTURE);
            }
        });


    }


    private void checkPermission() {
        String permission1 = "android.permission.CAMERA";
        String permission2 = "android.permission.WRITE_EXTERNAL_STORAGE";
        String permission3 = "android.permission.READ_EXTERNAL_STORAGE";
        String[] permissionArray = {permission1, permission2, permission3};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //requestPermissions(permissionArray, 123);
            ActivityCompat.requestPermissions(ChangeIcon.this, permissionArray, 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 判断是否有存储卡，有返回TRUE，否则FALSE
     *
     * @return
     */
    public static boolean isSDcardExist() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST_CAMERA_PHOTO) {//相机请求
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    return;
                }
                File tempFile = new File(
                        Environment.getExternalStorageDirectory(),
                        mCurrentPhotoName);
                mNewCurrentPhotoName = "pz_" + mCurrentPhotoName;
                String outPath = Environment.getExternalStorageDirectory() + File.separator + mNewCurrentPhotoName;
                CropPhotoUtil.cropRawPhoto(this, Uri.fromFile(tempFile), CODE_REQUEST_CROP_PHOTO, outPath, 200, 200);
            }
        } else if (requestCode == CODE_REQUEST_CROP_PHOTO) {//裁剪请求
            if (resultCode == Activity.RESULT_OK) {
                path = Environment.getExternalStorageDirectory() + File.separator + mNewCurrentPhotoName;
                ImageUtils.getInstance(this).setHeadViewFile(pictureImg, path, ImageUtils.ImageType.IMAGE_TYPE_CRICLE);
                Toast.makeText(ChangeIcon.this, path, Toast.LENGTH_LONG).show();
                find();
                upload();
                picPath = getPath();

                Intent intent = new Intent();
                intent.putExtra("path", picPath);
                setResult(111, intent);

            }
        } else if (requestCode == CODE_REQUEST_PICTURE && resultCode == Activity.RESULT_OK) {//相册请求
            if (data == null) return;
            mNewCurrentPhotoName = System.currentTimeMillis() + ".jpg";
            String outPath = Environment.getExternalStorageDirectory() + File.separator + mNewCurrentPhotoName;
            CropPhotoUtil.cropRawPhoto(this, data.getData(), CODE_REQUEST_CROP_PHOTO, outPath, 200, 200);
        }
    }

    public String getPath() {
        return path;
    }

    public void upload() {
        String picPath = getPath();
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    customer.setIcon(bmobFile);
                    customer.update(BmobUser.getCurrentUser().getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.i("bmob", "更新成功");
                            } else {
                                Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });
                    Toast.makeText(ChangeIcon.this, "上传文件成功:" + bmobFile.getFileUrl(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ChangeIcon.this, "上传文件失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    public String find() {
        BmobQuery<Customer> query = new BmobQuery<Customer>();
        query.getObject(Customer.getCurrentUser().getObjectId(), new QueryListener<Customer>() {

            @Override
            public void done(Customer object, BmobException e) {
                if (e == null) {
                    BmobFile bmobFile = object.getIcon();
                    oldFileUrl = bmobFile.getUrl();

                    Log.i("bmob", "成功：" + oldFileUrl);
                    Log.i("bmob", "chenggong：" + object.getIcon());
                    delete(oldFileUrl);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });
        Log.i("bmob", "成功:" + oldFileUrl);
        return oldFileUrl;
    }

    public void delete(String url) {
        BmobFile file = new BmobFile();
        file.setUrl(url);//此url是上传文件成功之后通过bmobFile.getUrl()方法获取的。
        file.delete(new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "文件删除成功：");
                } else {
                    Log.i("bmob", "文件删除失败：" + e.getErrorCode() + "," + e.getMessage());
                }
            }
        });
    }
}