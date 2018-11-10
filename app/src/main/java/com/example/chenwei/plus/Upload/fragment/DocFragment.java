package com.example.chenwei.plus.Upload.fragment;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class DocFragment extends BaseFragment {
    private RecyclerView rlv_doc;
    private List<FileInfo> fileInfos = new ArrayList<>();
    List<Filehere> fileheres;
    ExpandableItemAdapter mExpandableItemAdapter;
    private ArrayList<MultiItemEntity> mEntityArrayList = new ArrayList<>();
    ProgressDialog progressDialog;
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
        return R.layout.fragment_doc;
    }

    @Override
    public void initView() {
        rlv_doc = (RecyclerView) getActivity().findViewById(R.id.rlv_doc);
        fileInfos.clear();
        mEntityArrayList.clear();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("数据加载中");
        progressDialog.setCancelable(false);

        progressDialog.show();  //将进度条显示出来
        ReadDOCFile();
        rlv_doc.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExpandableItemAdapter = new ExpandableItemAdapter(mEntityArrayList, false);
        rlv_doc.setAdapter(mExpandableItemAdapter);

        mRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onCreate(null);
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void ReadDOCFile() {
        String[] m = LocalFileTool.docType;
        final String[] finalM = m;
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
                    fileInfos.add(new FileInfo(file.getName(), file.getPath(), file.length(), true, path.getState(), time, true, true));
                }

                progressDialog.dismiss();

                Log.e("onCompleted", "onCompleted()");
                if (fileInfos.size() > 0) {
                    SubItem wordItem = new SubItem("WORD");
                    SubItem excelItem = new SubItem("EXCEL");
                    SubItem pdfItem = new SubItem("PDF");
                    SubItem PPTItem = new SubItem("PPT");
                    SubItem textItem = new SubItem("TXT");
                    for (int j = 0; j < fileInfos.size(); j++) {
                        if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"doc", "docx", "dot"})) {
                            wordItem.addSubItem(fileInfos.get(j));
                        } else if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"xls"})) {
                            excelItem.addSubItem(fileInfos.get(j));
                        } else if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"pdf"})) {
                            pdfItem.addSubItem(fileInfos.get(j));
                        } else if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"ppt", "pptx"})) {
                            PPTItem.addSubItem(fileInfos.get(j));
                        } else if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"txt"})) {
                            textItem.addSubItem(fileInfos.get(j));
                        }
                    }

                    mEntityArrayList.add(wordItem);
                    mEntityArrayList.add(excelItem);
                    mEntityArrayList.add(pdfItem);
                    mEntityArrayList.add(PPTItem);
                    mEntityArrayList.add(textItem);
                    mExpandableItemAdapter.setNewData(mEntityArrayList);
                    mExpandableItemAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "sorry,没有读取到文件!", Toast.LENGTH_LONG).show();
                }

            }
        });
//        List<File> m = new ArrayList<>();
//        m.add(new File(Environment.getExternalStorageDirectory() + "/tencent/"));
//        m.add(new File(Environment.getExternalStorageDirectory() + "/dzsh/"));
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
//                                    SubItem wordItem = new SubItem("WORD");
//                                    SubItem excelItem = new SubItem("EXCEL");
//                                    SubItem pdfItem = new SubItem("PDF");
//                                    SubItem PPTItem = new SubItem("PPT");
//                                    SubItem textItem = new SubItem("TXT");
//                                    for (int j = 0; j < fileInfos.size(); j++) {
//                                        if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"doc", "docx", "dot"})) {
//                                            wordItem.addSubItem(fileInfos.get(j));
//                                        } else if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"xls"})) {
//                                            excelItem.addSubItem(fileInfos.get(j));
//                                        } else if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"pdf"})) {
//                                            pdfItem.addSubItem(fileInfos.get(j));
//                                        } else if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"ppt", "pptx"})) {
//                                            PPTItem.addSubItem(fileInfos.get(j));
//                                        } else if (FileUtil.checkSuffix(fileInfos.get(j).getFilePath(), new String[]{"txt"})) {
//                                            textItem.addSubItem(fileInfos.get(j));
//                                        }
//                                    }
//
//                                    mEntityArrayList.add(wordItem);
//                                    mEntityArrayList.add(excelItem);
//                                    mEntityArrayList.add(pdfItem);
//                                    mEntityArrayList.add(PPTItem);
//                                    mEntityArrayList.add(textItem);
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
                    return f.exists() && f.canRead() && FileUtil.checkSuffix(f.getAbsolutePath(), new String[]{"doc", "docx", "dot", "xls", "pdf", "ppt", "pptx", "txt"});
                }
            });
        }
    }
}
