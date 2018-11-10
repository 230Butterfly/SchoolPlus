package com.example.chenwei.plus.Upload.activity;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Upload.adapter.MultipleItemQuickAdapter;
import com.example.chenwei.plus.Upload.base.baseActivity;
import com.example.chenwei.plus.Upload.bean.EventCenter;
import com.example.chenwei.plus.Upload.bean.FileDao;
import com.example.chenwei.plus.Upload.bean.FileInfo;
import com.example.chenwei.plus.Upload.bean.MultipleItem;
import com.example.chenwei.plus.Upload.bean.ResourceUpload;
import com.example.chenwei.plus.Upload.utils.FileUtil;
import com.example.chenwei.plus.Upload.view.CheckBox;
import com.example.chenwei.plus.Upload.view.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

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

import static com.example.chenwei.plus.Upload.utils.FileUtil.fileFilter;
import static com.example.chenwei.plus.Upload.utils.FileUtil.getFileInfosFromFileArray;


public class SDCardActivity extends baseActivity {
    @Bind(R.id.rlv_sd_card)
    RecyclerView rlv_sd_card;
    @Bind(R.id.tv_path)
    TextView tv_path;
    @Bind(R.id.tv_all_size)
    TextView tv_all_size;
    @Bind(R.id.tv_send)
    TextView tv_send;
    private List<FileInfo> fileInfos = new ArrayList<>();
    private List<MultipleItem> mMultipleItems = new ArrayList<>();
    private MultipleItemQuickAdapter mAdapter;
    private File mCurrentPathFile = null;
    private File mSDCardPath = null;
    private String path;

    @OnClick(R.id.iv_title_back)
    void iv_title_back() {
        if (mSDCardPath.getAbsolutePath().equals(mCurrentPathFile.getAbsolutePath())) {
            finish();
        } else {
            mCurrentPathFile = mCurrentPathFile.getParentFile();
            showFiles(mCurrentPathFile);
        }
    }

    @Bind(R.id.tv_title_middle)
    TextView tv_title_middle;
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
                                        LocalBroadcastManager.getInstance(SDCardActivity.this).sendBroadcast(intent);
                                        sendBroadcast(intent);

                                        finish();
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
                                        finish();
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
    @Override
    public void onEventComming(EventCenter var1) {

    }

    @Override
    public boolean isBindEventBusHere() {
        return false;
    }

    @Override
    public void initViewAndEvent() {
        tv_all_size.setText(getString(R.string.size, "0B"));
        tv_send.setText(getString(R.string.send, "0"));
        path = getIntent().getStringExtra("path");
        tv_title_middle.setText(getIntent().getStringExtra("name"));
        mSDCardPath = new File(path);
        rlv_sd_card.setLayoutManager(new LinearLayoutManager(this));
        rlv_sd_card.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, R.drawable.divide_line));
        mAdapter = new MultipleItemQuickAdapter(mMultipleItems);
        rlv_sd_card.setAdapter(mAdapter);
        showFiles(mSDCardPath);
        updateSizAndCount();
        rlv_sd_card.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (adapter.getItemViewType(position) == MultipleItem.FILE) {
                    boolean isCheck = fileInfos.get(position).getIsCheck();
                    fileInfos.get(position).setIsCheck(!isCheck);
                    if (fileInfos.get(position).getIsCheck()) {
                        FileDao.insertFile(fileInfos.get(position));
                        ((CheckBox) view.findViewById(R.id.cb_file)).setChecked(true, true);
                    } else {
                        FileDao.deleteFile(fileInfos.get(position));
                        ((CheckBox) view.findViewById(R.id.cb_file)).setChecked(false, true);
                    }
                    EventBus.getDefault().post(new EventCenter<>(3));
                    updateSizAndCount();
                } else {
                    showFiles(new File(fileInfos.get(position).getFilePath()));
                }

            }
        });
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
    public void onBackPressed() {
        if (mSDCardPath.getAbsolutePath().equals(mCurrentPathFile.getAbsolutePath())) {
            finish();
        } else {
            mCurrentPathFile = mCurrentPathFile.getParentFile();
            showFiles(mCurrentPathFile);
        }
    }

    private void showFiles(File folder) {
        mMultipleItems.clear();
        tv_path.setText(folder.getAbsolutePath());
        mCurrentPathFile = folder;
        File[] files = fileFilter(folder);
        if (null == files || files.length == 0) {
            mAdapter.setEmptyView(getEmptyView());
            Log.e("files", "files::为空啦");
        } else {
            //获取文件信息
            fileInfos = getFileInfosFromFileArray(files);
            for (int i = 0; i < fileInfos.size(); i++) {
                if (fileInfos.get(i).isDirectory) {
                    mMultipleItems.add(new MultipleItem(MultipleItem.FOLD, fileInfos.get(i)));
                } else {
                    mMultipleItems.add(new MultipleItem(MultipleItem.FILE, fileInfos.get(i)));
                }

            }
            //查询本地数据库，如果之前有选择的就显示打钩
            List<FileInfo> mList = FileDao.queryAll();
            for (int i = 0; i < fileInfos.size(); i++) {
                for (FileInfo fileInfo : mList) {
                    if (fileInfo.getFileName().equals(fileInfos.get(i).getFileName())) {
                        fileInfos.get(i).setIsCheck(true);
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private View getEmptyView() {
        return getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) rlv_sd_card.getParent(), false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sdcard;
    }


}
