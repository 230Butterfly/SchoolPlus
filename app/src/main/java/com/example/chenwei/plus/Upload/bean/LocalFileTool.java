package com.example.chenwei.plus.Upload.bean;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.chenwei.plus.Upload.bean.Filehere;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/7/13 0013.
 */
public class LocalFileTool
{
    public static final String[] picType=new String[]{"image/bmp","image/jpeg","image/png"};
    public static final String[] aviType=new String[]{"video/3gpp","video/x-ms-asf","video/x-msvideo",
            "video/vnd.mpegurl","video/x-m4v","video/quicktime","video/mp4","video/mpeg",
    };
    public static  final String[] volumType=new String[]{"audio/x-mpegurl","audio/mp4a-latm","audio/x-mpeg","audio/mpeg","audio/ogg","audio/x-wav","audio/x-ms-wma"};
    public static final String[] docType=new String[]{"application/msword","application/pdf","application/vnd.ms-powerpoint","application/vnd.ms-works"};
    public static final String[] zipType=new String[]{"application/x-gtar","application/x-gzip","application/x-compress","application/zip"};
    public static final String[] apkType=new String[]{"application/vnd.android.package-archive"};
    // final static Object lock=new Object();

    public static void readFile(final String[] mimeType, Context context, final IReadCallBack iReadCallBack)
    {

        Observable.just(context).map(new Func1<Context, List<Filehere>>() {
            @Override
            public List<Filehere> call(Context context1) {


                List<Filehere> paths = new ArrayList<Filehere>();
                Uri[] fileUri = null;
                fileUri = new Uri[]{MediaStore.Files.getContentUri("external")};

                String[] colums = new String[]{MediaStore.Files.FileColumns.DATA};
                String[] extension = mimeType;

                //构造筛选语句
                String selection = "";
                for (int i = 0; i < extension.length; i++) {
                    if (i != 0) {
                        selection = selection + " OR ";
                    }
                    selection = selection + MediaStore.Files.FileColumns.MIME_TYPE + " LIKE '%" + extension[i] + "'";
                }

                //获取内容解析器对象
                ContentResolver resolver = context1.getContentResolver();
                //获取游标
                for (int i = 0; i < fileUri.length; i++) {
                    Cursor cursor =null;
                    if(mimeType.equals(volumType)){
                         cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC);

                    }else if(mimeType.equals(picType)) {
                        cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Images.ImageColumns.IS_PRIVATE);
                    }else if(mimeType.equals(aviType)) {
                        cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.VideoColumns.IS_PRIVATE);
                    }else{
                        cursor = resolver.query(fileUri[i], colums, selection, null, null);
                    }
                    if (cursor == null) {
                        return null;
                    }//游标从最后开始往前递减，以此实现时间递减顺序（最近访问的文件，优先显示）
                    long beginTime = System.currentTimeMillis();
                    if (cursor.moveToLast()) {

                        do {
                            //输出文件的完整路径
                            Filehere filehere =new Filehere();
                            if(mimeType.equals(volumType)){
                                filehere.setSong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                                filehere.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                                //filehere.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                                filehere.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                                filehere.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                                filehere.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                                filehere.setState("audio/*");
                            }else if(mimeType.equals(picType)) {
                                filehere.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                                filehere.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
                                filehere.setState("image/*");
                            }else if(mimeType.equals(aviType)) {
                                filehere.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                                filehere.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
                                filehere.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
                                File file =new File(filehere.getPath());
                                filehere.setName(file.getName());
                                filehere.setState("video/*");
                            }else if(mimeType.equals(docType)) {
                                filehere.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                                File file =new File(filehere.getPath());
                                filehere.setName(file.getName());
                                filehere.setState("application/msword");
                            }else{
                                filehere.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                                File file =new File(filehere.getPath());
                                filehere.setName(file.getName());
                                filehere.setState("application/vnd.android.package-archive");
                            }
                            paths.add(filehere);

                        } while (cursor.moveToPrevious());

                    }
                    cursor.close();
                    android.util.Log.e("endTime", System.currentTimeMillis() - beginTime + "");
                }
                return paths;


            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).
                subscribe(new Action1<List<Filehere>>() {
                    @Override
                    public void call(List<Filehere> strings) {
                        iReadCallBack.callBack(strings);
                    }
                });

    }



    public interface IReadCallBack
    {
        void callBack(List<Filehere> localPath);
    }




}
