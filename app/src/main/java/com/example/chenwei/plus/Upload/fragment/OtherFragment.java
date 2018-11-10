package com.example.chenwei.plus.Upload.fragment;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Upload.adapter.ExpandableItemAdapter;
import com.example.chenwei.plus.Upload.bean.Filehere;
import com.example.chenwei.plus.Upload.bean.LocalFileTool;
import com.example.chenwei.plus.Upload.base.BaseFragment;
import com.example.chenwei.plus.Upload.bean.EventCenter;
import com.example.chenwei.plus.Upload.bean.FileInfo;
import com.example.chenwei.plus.Upload.bean.SubItem;
import com.example.chenwei.plus.Upload.utils.FileUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by LCJ on 2018/10/27.
 */

public class OtherFragment extends BaseFragment {
    RecyclerView rlv_other;
    private List<FileInfo> fileInfos = new ArrayList<>();
    ExpandableItemAdapter mExpandableItemAdapter;
    private ArrayList<MultiItemEntity> mEntityArrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    List<Filehere> fileheres;
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
        return R.layout.fragment_other;
    }

    @Override
    public void initView() {
        rlv_other = (RecyclerView) getActivity().findViewById(R.id.rlv_other);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("数据加载中");
        progressDialog.setCancelable(false);
        progressDialog.show();  //将进度条显示出来
        ReadOtherFile();
        rlv_other.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExpandableItemAdapter = new ExpandableItemAdapter(mEntityArrayList, false);
        rlv_other.setAdapter(mExpandableItemAdapter);

        mRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onCreate(null);
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void ReadOtherFile() {
        String[] n = LocalFileTool.apkType;
        LocalFileTool.readFile(n, getContext(), new LocalFileTool.IReadCallBack() {

                    @Override
                    public void callBack(List<Filehere> localPath) {
                        fileheres = localPath;
                        fileheres = localPath;
                        File file;
                        Date date;
                        for (Filehere path : localPath) {
                            file = new File(path.getPath());
                            String time = new SimpleDateFormat("yyyy-MM-dd")
                                    .format(new Date(file.lastModified()));
                            String name = null;
                            PackageManager pm = getContext().getPackageManager();
                            PackageInfo packageInfo = pm.getPackageArchiveInfo(file.getPath(), PackageManager.GET_ACTIVITIES);
                            ApplicationInfo appInfo = null;
                            String packageName=null;
                            ApplicationInfo applicationInfo = null;
                            if (packageInfo != null) {
                                appInfo = packageInfo.applicationInfo;
                                packageName = appInfo.packageName;
                            }

                            try {
                                applicationInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                            if (applicationInfo != null) {
                                name =(String) pm.getApplicationLabel(applicationInfo);
                            }

                            fileInfos.add(new FileInfo(packageName, file.getPath(), file.length(), true, path.getState(), time, true, true));
                        }
                    }
                });
        String[] m = LocalFileTool.zipType;
        LocalFileTool.readFile(m, getContext(), new LocalFileTool.IReadCallBack() {

            @Override
            public void callBack(List<Filehere> localPath) {
                fileheres=localPath;
                fileheres = localPath;
                File file ;
                Date date;
                for (Filehere path : localPath) {
//                        showinfo.add(path.getName());
                    file =new File(path.getPath());
                    String time = new SimpleDateFormat("yyyy-MM-dd")
                            .format(new Date(file.lastModified()));
                    fileInfos.add(new FileInfo(file.getName(), file.getPath(), file.length(), true, path.getState(), time, true, true));
                }

                progressDialog.dismiss();

                if (fileInfos.size() > 0) {
                    SubItem ZipItem = new SubItem("ZIP文件");
//                    SubItem APPItem = new SubItem("APP文件");
                    for (int j = 0; j < fileInfos.size(); j++) {
                        if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"zip"})) {
                            ZipItem.addSubItem(fileInfos.get(j));
                        } else if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"apk"})) {
//                            APPItem.addSubItem(fileInfos.get(j));
                        }
                    }

                    mEntityArrayList.add(ZipItem);
//                    mEntityArrayList.add(APPItem);
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
//                                if (fileInfos.size() > 0) {
//                                    SubItem ZipItem = new SubItem("ZIP文件");
//                                    SubItem APPItem = new SubItem("APP文件");
//                                    for (int j = 0; j < fileInfos.size(); j++) {
//                                        if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"zip"})) {
//                                            ZipItem.addSubItem(fileInfos.get(j));
//                                        } else if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"apk"})) {
//                                            APPItem.addSubItem(fileInfos.get(j));
//                                        }
//                                    }
//
//                                    mEntityArrayList.add(ZipItem);
//                                    mEntityArrayList.add(APPItem);
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
//                                FileInfo fileInfo = FileUtil.getFileInfoFromFile(file);
//                                Log.e("文件路径", "文件路径：：：" + fileInfo.getFilePath());
//                                fileInfos.add(fileInfo);
//
//                            }
//                        }
//                );
    }

    public static Observable<File> listFiles(final File f) {
        if (f.isDirectory()) {
            return Observable.from(f.listFiles()).flatMap(new Func1<File, Observable<File>>() {
                @Override
                public Observable<File> call(File file) {
                    return listFiles(file);
                }
            });
        } else {
            return Observable.just(f).filter(new Func1<File, Boolean>() {
                @Override
                public Boolean call(File file) {
                    return f.exists() && f.canRead() && FileUtil.checkSuffix(f.getAbsolutePath(), new String[]{"zip", "apk"});
                }
            });
        }
    }
}
