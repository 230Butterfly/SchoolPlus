package com.example.chenwei.plus.Upload.fragment;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.chenwei.plus.MainActivity;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Upload.base.BaseFragment;
import com.example.chenwei.plus.Upload.bean.EventCenter;
import com.example.chenwei.plus.Upload.adapter.ExpandableItemAdapter;
import com.example.chenwei.plus.Upload.bean.FileInfo;
import com.example.chenwei.plus.Upload.utils.FileUtil;
import com.example.chenwei.plus.Upload.bean.Filehere;
import com.example.chenwei.plus.Upload.bean.LocalFileTool;
import com.example.chenwei.plus.Upload.bean.SubItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LCJ on 2018/10/27.
 */

public class AVFragment extends BaseFragment {

    private List<FileInfo> fileInfos = new ArrayList<>();

    RecyclerView rlv_av;
    ExpandableItemAdapter mExpandableItemAdapter;
    private ArrayList<MultiItemEntity> mEntityArrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    ListView file;
    List<Filehere> fileheres;
    ArrayAdapter<String> adapter=null;
    List<String> showinfo =new ArrayList<>();
    SwipeRefreshLayout mRefreshLayout=null;
    @Override
    public boolean isBindEventBusHere() {
        return true;
    }

    @Override
    public void onEventComming(EventCenter var1) {

    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_av;
    }

    @Override
    public void initView() {

        fileInfos.clear();
        mEntityArrayList.clear();
        rlv_av = (RecyclerView) getActivity().findViewById(R.id.rlv_av);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("数据加载中");
        progressDialog.setCancelable(false);
        progressDialog.show();  //将进度条显示出来
        ReadVideoFile();
        rlv_av.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExpandableItemAdapter = new ExpandableItemAdapter(mEntityArrayList, false);
        rlv_av.setAdapter(mExpandableItemAdapter);
        mRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView();

                mRefreshLayout.setRefreshing(false);
            }
        });


    }




    private void ReadVideoFile() {
        String[] n = LocalFileTool.aviType;
        final String[] finalN = n;

        String[] m =LocalFileTool.volumType;
        final String[] finalM = m;


        LocalFileTool.readFile(n, getContext(), new LocalFileTool.IReadCallBack() {

            @Override
            public void callBack(List<Filehere> localPath) {
                fileheres = localPath;
                File file ;
                Date date;
                for (Filehere path : localPath) {
//                        showinfo.add(path.getName());
                    file =new File(path.getPath());
                    String time = new SimpleDateFormat("yyyy-MM-dd")
                            .format(new Date(file.lastModified()));
                    fileInfos.add(new FileInfo(file.getName(), file.getPath(), file.length(), true, path.getState(), time, true, false));
                }
            }
        });

        LocalFileTool.readFile(m, getContext(), new LocalFileTool.IReadCallBack() {

            @Override
            public void callBack(List<Filehere> localPath) {
                fileheres = localPath;
                File file ;
                Date date;
                for (Filehere path : localPath) {
//                        showinfo.add(path.getName());
                    file =new File(path.getPath());
                    String time = new SimpleDateFormat("yyyy-MM-dd")
                            .format(new Date(file.lastModified()));
                    fileInfos.add(new FileInfo(file.getName(), file.getPath(), file.length(), true, path.getState(), time, true, false));
                }

                progressDialog.dismiss();


                if (fileInfos.size() > 0) {
                    SubItem musicItem = new SubItem("音乐文件");
                    SubItem videoItem = new SubItem("视频文件");
//                    SubItem recordItem = new SubItem("录音文件");
                    for (int j = 0; j < fileInfos.size(); j++) {
                        if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"mp3","wav", "aac", "amr","ogg"})) {
                            musicItem.addSubItem(fileInfos.get(j));
                        } else if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"wmv", "rmvb", "avi", "mp4"})) {
                             videoItem.addSubItem(fileInfos.get(j));
//                        } else if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"wav", "aac", "amr"})) {
//                            recordItem.addSubItem(fileInfos.get(j));
                        }
                    }

                    mEntityArrayList.add(musicItem);
                    mEntityArrayList.add(videoItem);
//                    mEntityArrayList.add(recordItem);
                    mExpandableItemAdapter.setNewData(mEntityArrayList);
                    mExpandableItemAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "sorry,没有读取到文件!", Toast.LENGTH_LONG).show();
                }

            }
        });
//        List<File> m = new ArrayList<>();
//        m.add(new File(Environment.getExternalStorageDirectory() + "/tencent/"));//微信QQ
//        m.add(new File(Environment.getExternalStorageDirectory() + "/dzsh/"));//自定义
//        Observable.from(m)
//                .flatMap(new Func1<File, Observable<File>>() {
//                    @Override
//                    public Observable<File> call(File file) {
//                        return listFiles(file);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        new Subscriber<File>() {
//                            @Override
//                            public void onCompleted() {
//                                progressDialog.dismiss();
//                                Log.e("onCompleted", "onCompleted()");
//                                if (fileInfos.size() > 0) {
//                                    SubItem musicItem = new SubItem("音乐文件");
//                                    SubItem videoItem = new SubItem("视频文件");
//                                    SubItem recordItem = new SubItem("录音文件");
//                                    for (int j = 0; j < fileInfos.size(); j++) {
//                                        if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"mp3"})) {
//                                            musicItem.addSubItem(fileInfos.get(j));
//                                        } else if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"wmv", "rmvb", "avi", "mp4"})) {
//                                            videoItem.addSubItem(fileInfos.get(j));
//                                        } else if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"wav", "aac", "amr"})) {
//                                            recordItem.addSubItem(fileInfos.get(j));
//                                        }
//                                    }
//
//                                    mEntityArrayList.add(musicItem);
//                                    mEntityArrayList.add(videoItem);
//                                    mEntityArrayList.add(recordItem);
//                                    mExpandableItemAdapter.setNewData(mEntityArrayList);
//                                    mExpandableItemAdapter.notifyDataSetChanged();
//                                } else {
//                                    Toast.makeText(getActivity(), "sorry,没有读取到文件!", Toast.LENGTH_LONG).show();
//                                }
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                progressDialog.dismiss();
//                            }
//
//                            @Override
//                            public void onNext(File file) {
//                                Log.e("onNext", "onNext()");
//                                FileInfo fileInfo = FileUtil.getFileInfoFromFile(file);
//                                Log.e("文件路径", "文件路径：：：" + fileInfo.getFilePath());
//                                fileInfos.add(fileInfo);
//
//                            }
//                        }
//                );
    }

//    public static Observable<File> listFiles(final File f) {
//        if (f.isDirectory()) {
//            return Observable.from(f.listFiles()).flatMap(new Func1<File, Observable<File>>() {
//                @Override
//                public Observable<File> call(File file) {
//                    return listFiles(file);
//                }
//            });
//        } else {
//            return Observable.just(f).filter(new Func1<File, Boolean>() {
//                @Override
//                public Boolean call(File file) {
//                    return f.exists() && f.canRead() && FileUtil.checkSuffix(f.getAbsolutePath(), new String[]{"mp3", "aac", "amr", "wav", "wmv", "avi", "mp4", "rmvb"});
//                }
//            });
//        }
//    }
}
