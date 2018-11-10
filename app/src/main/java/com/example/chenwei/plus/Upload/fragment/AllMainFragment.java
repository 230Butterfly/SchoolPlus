package com.example.chenwei.plus.Upload.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Upload.activity.SDCardActivity;
import com.example.chenwei.plus.Upload.base.BaseFragment;
import com.example.chenwei.plus.Upload.bean.EventCenter;
import com.example.chenwei.plus.Upload.bean.FileDao;
import com.example.chenwei.plus.Upload.bean.FileInfo;
import com.example.chenwei.plus.Upload.bean.ResourceUpload;
import com.example.chenwei.plus.Upload.utils.FileUtil;
import com.example.chenwei.plus.Upload.utils.SystemUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class AllMainFragment extends BaseFragment {

    private Context context;


    public AllMainFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public AllMainFragment(Context context){
        this.context=context;
    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_all_fragment,container,false);
//
//        return view;
//    }


    @Bind(R.id.tv_all_size)
    TextView tv_all_size;
    @Bind(R.id.tv_send)
    TextView tv_send;

    private Boolean isOnClickable=true;
    @OnClick(R.id.tv_send)
    void tv_send(){
        if(isOnClickable){
            isOnClickable=false;
            final List<FileInfo> mList = FileDao.queryAll();

            if (mList.size()==0){
                Toast.makeText(getApplicationContext(),"无选中内容",Toast.LENGTH_LONG).show();
            }
            else if(mList.size()>10){
                Toast.makeText(getApplicationContext(),"一次只能上传10个文件",Toast.LENGTH_LONG).show();

            }
            else if(mList.size()==1){
                final BmobFile bmobFile = new BmobFile(new File(mList.get(0).getFilePath()));
                bmobFile.uploadblock(new UploadFileListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            //bmobFile.getFileUrl()--返回的上传文件的完整地址
                            Toast.makeText(getApplicationContext(),"上传成功",Toast.LENGTH_LONG).show();
                            ResourceUpload resource=new ResourceUpload();
                            resource.setFile(bmobFile);
                            resource.setUser(BmobUser.getCurrentUser().getObjectId());
                            resource.setPath(mList.get(0).getFilePath());
                            resource.setName(mList.get(0).getFileName());
                            resource.setComment(0);
                            resource.setInterger(0);
                            resource.setIntroduction("暂无简介");
                            resource.setLabel("暂无标签");
                            resource.setSize(String.valueOf(mList.get(0).getFileSize()));


                            resource.save(new SaveListener<String>() {
                                @Override
                                public void done(String objectId, BmobException e) {
                                    if(e==null){
                                        Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                                        intent.putExtra("upload_refresh","refresh");
                                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                                        getActivity().sendBroadcast(intent);

                                        getActivity().finish();
//                                    Toast.makeText(getApplicationContext(),"数据保存成功",Toast.LENGTH_LONG).show();
                                    }else{
//                                    Toast.makeText(getApplicationContext(),"数据保存失败",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(),"文件上传失败",Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onProgress(Integer value) {
                        // 返回的上传进度（百分比）
                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(),"上传中",Toast.LENGTH_LONG).show();
                final String[] filePaths = new String[mList.size()];
                for(int i=0;i<mList.size();i++){
                    filePaths[i]=mList.get(i).getFilePath();
                }
                BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

                    public  boolean isSuccess=false;
                    public  List<BmobFile> bmobFiles;
                    @Override
                    public void onSuccess(List<BmobFile> files, List<String> urls) {
                        //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                        //2、urls-上传文件的完整url地址
                        for(int i=0;i<files.size();i++)
                            Log.e("mList",files.get(i).getFilename());
                        if(urls.size()==filePaths.length){//如果数量相等，则代表文件全部上传完成
                            Toast.makeText(getApplicationContext(),"上传成功",Toast.LENGTH_LONG).show();
                            isSuccess=true;
                            bmobFiles=files;
                        }
                        //上传文件信息到个人
                        //逐个上传
                        if(isSuccess){
//                        Toast.makeText(getApplicationContext(),"逐个上传",Toast.LENGTH_LONG).show();
                            List<BmobObject> resources = new ArrayList<BmobObject>();

                            for(int i =0;i<bmobFiles.size();i++){
                                ResourceUpload resource = new ResourceUpload();
                                resource.setFile(bmobFiles.get(i));
                                resource.setUser(BmobUser.getCurrentUser().getObjectId());
                                resource.setPath(mList.get(0).getFilePath());
                                resource.setName(mList.get(0).getFileName());
                                resource.setComment(0);
                                resource.setInterger(0);
                                resource.setIntroduction("暂无简介");
                                resource.setLabel("暂无标签");
                                resource.setSize(String.valueOf(mList.get(0).getFileSize()));

                                resources.add(resource);
                            }
                            //bmob：v3.5.0开始提供
                            new BmobBatch().insertBatch(resources).doBatch(new QueryListListener<BatchResult>() {

                                @Override
                                public void done(List<BatchResult> o, BmobException e) {
                                    if(e==null){
                                        for(int i=0;i<o.size();i++){
                                            BatchResult result = o.get(i);
                                            BmobException ex =result.getError();
                                            if(ex==null){
                                                Log.e("bmob","第"+i+"个数据批量添加成功："+result.getCreatedAt()+","+result.getObjectId()+","+result.getUpdatedAt());
                                            }else{
                                                Log.e("bmob","第"+i+"个数据批量添加失败："+ex.getMessage()+","+ex.getErrorCode());
                                            }
                                        }
                                        getActivity().finish();
                                    }else{
                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });
                        }
                    }
                    @Override
                    public void onError(int statuscode, String errormsg) {
                        Toast.makeText(getApplicationContext(),"错误码"+statuscode +",错误描述："+errormsg,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                        Toast.makeText(getApplicationContext(),"总上传进度："+totalPercent+"%",Toast.LENGTH_LONG).show();
                        //1、curIndex--表示当前第几个文件正在上传

                        //2、curPercent--表示当前上传文件的进度值（百分比）
                        //3、total--表示总的上传文件数
                        //4、totalPercent--表示总的上传进度（百分比）
                    }


                });



            }

        }
        else{

        }

    }


    @OnClick(R.id.rl_mobile_memory)
    void rl_mobile_memory() {
        Intent intent = new Intent(getActivity(), SDCardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("path", Environment.getDataDirectory().getParentFile().getAbsolutePath());
        bundle.putString("name", "手机内存");
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @OnClick(R.id.rl_extended_memory)
    void rl_extended_memory() {
        if (checkExtentEnvironment()) {
            Intent intent = new Intent(getActivity(), SDCardActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("path", FileUtil.getStoragePath(getActivity()));
            bundle.putString("name", "扩展卡内存");
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(),"您手机没有外置SD卡！",Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.rl_sd_card)
    void rl_sd_card() {

        if (checkSDEnvironment()) {
            Intent intent = new Intent(getActivity(), SDCardActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("path", Environment.getExternalStorageDirectory().getAbsolutePath());
            bundle.putString("name", "SD卡");
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
//            Alerter.create(getActivity())
//                    .setTitle("通知")
//                    .setText("您手机没有内置SD卡！")
//                    .show();
            Toast.makeText(getContext(),"您手机没有内置SD卡！",Toast.LENGTH_LONG).show();
        }

    }



    private boolean checkSDEnvironment() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        return sdCardExist;

    }

    private boolean checkExtentEnvironment() {
        if (checkSDEnvironment() && TextUtils.isEmpty(FileUtil.getStoragePath(getActivity()))) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onEventComming(EventCenter var1) {
        Log.e("getEventCode", "getEventCode:::" + var1.getEventCode());
        switch (var1.getEventCode()) {
            case 3://点击 选中和不选中都通知更新一下
                updateSizAndCount();
                break;
        }
    }

    @Override
    public boolean isBindEventBusHere() {
        return true;
    }


    public void updateSizAndCount() {
        List<FileInfo> mList = FileDao.queryAll();
        if (mList.size() == 0) {
            tv_send.setBackgroundResource(R.drawable.shape_bt_send);
            tv_send.setTextColor(getResources().getColor(R.color.md_grey_700));
            tv_all_size.setText(getString(R.string.size, "0B"));
        } else {
            tv_send.setBackgroundResource(R.drawable.shape_bt_send_blue);
            tv_send.setTextColor(getResources().getColor(R.color.md_white_1000));
            long count = 0L;
            for (int i = 0; i < mList.size(); i++) {
                count = count + mList.get(i).getFileSize();
            }
            tv_all_size.setText(getString(R.string.size, FileUtil.FormetFileSize(count)));
        }
        tv_send.setText(getString(R.string.send, "" + mList.size()));
    }


    @Override
    public int getLayoutResource() {
        return R.layout.fragment_main_all;
    }

    @Override
    public void initView() {

        tv_all_size.setText(getString(R.string.size, "0B"));
        tv_send.setText(getString(R.string.send, "0"));
        SystemUtil.init(getActivity());
        updateSizAndCount();

    }



}
