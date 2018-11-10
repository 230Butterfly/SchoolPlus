package com.example.chenwei.plus.Upload.utils;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.chenwei.plus.R;
import com.example.chenwei.plus.Upload.bean.FileInfo;
import com.example.chenwei.plus.Upload.bean.FolderInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;




/**
 * Created by CWJ on 2017/3/20.
 */

public class FileUtil {

    /****
     * 计算文件大小
     *
     * @param length
     * @return
     */
    public static String getFileSzie(Long length) {
        if (length >= 1048576) {
            return (length / 1048576) + "MB";
        } else if (length >= 1024) {
            return (length / 1024) + "KB";
        } else if (length < 1024) {
            return length + "B";
        } else {
            return "0KB";
        }
    }

    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 字符串时间戳转时间格式
     *
     * @param timeStamp
     * @return
     */
    public static String getStrTime(String timeStamp) {
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
        long l = Long.valueOf(timeStamp) * 1000;
        timeString = sdf.format(new Date(l));
        return timeString;
    }

    /**
     * 读取文件的最后修改时间的方法
     */
    public static String getFileLastModifiedTime(File f) {
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }

    /**
     * 获取扩展内存的路径
     *
     * @param mContext
     * @return
     */
    public static String getStoragePath(Context mContext) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getFileTypeImageId(Context mContext, String fileName) {
        int id;
        if (checkSuffix(fileName, new String[]{"mp3","wav", "aac", "amr","ogg"})) {
            id = R.drawable.rc_ad_list_audio_icon;

        } else if (checkSuffix(fileName, new String[]{"wmv", "rmvb", "avi", "mp4"})) {
            id=-1;//标记视频类；
//            id = R.drawable.rc_ad_list_video_icon;
//        } else if (checkSuffix(fileName, new String[]{"wav", "aac", "amr"})) {
//            id = R.drawable.rc_ad_list_video_icon;
        }else if (checkSuffix(fileName, new String[]{"doc", "docx", "dot"})) {
            id = R.mipmap.word;
        }
        else if (checkSuffix(fileName, new String[]{"xls"})) {
            id = R.mipmap.excel;
        }
        else if (checkSuffix(fileName, new String[]{"pdf"})) {
            id = R.mipmap.pdf;
        }
        else if (checkSuffix(fileName, new String[]{"ppt", "pptx"})) {
            id = R.mipmap.ppt;
        }
        else if (checkSuffix(fileName, new String[]{"txt"})) {
            id = R.mipmap.txt;
        }
        else if(checkSuffix(fileName, new String[]{"apk"})){
            id=-2;
        }
        else if(checkSuffix(fileName,new String[]{"zip"})){
            id=R.mipmap.zip;
        }
//        if (checkSuffix(fileName, mContext.getResources().getStringArray(R.array.rc_file_file_suffix)))
//            id = R.drawable.rc_ad_list_file_icon;
//        else if (checkSuffix(fileName, mContext.getResources().getStringArray(R.array.rc_video_file_suffix)))
//            id = R.drawable.rc_ad_list_video_icon;
//        else if (checkSuffix(fileName, mContext.getResources().getStringArray(R.array.rc_audio_file_suffix)))
//            id = R.drawable.rc_ad_list_audio_icon;
        else
            id = R.drawable.rc_ad_list_other_icon;
        return id;
    }

    public static boolean checkSuffix(String fileName,
                                      String[] fileSuffix) {
        for (String suffix : fileSuffix) {
            if (fileName != null) {
                if (fileName.toLowerCase().endsWith(suffix)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 文件过滤,将手机中隐藏的文件给过滤掉
     */
    public static File[] fileFilter(File file) {
        File[] files = file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return !pathname.isHidden();
            }
        });
        return files;
    }


    public static List<FileInfo> getFilesInfo(List<String> fileDir, Context mContext) {
        List<FileInfo> mlist = new ArrayList<>();
        for (int i = 0; i < fileDir.size(); i++) {
            if (new File(fileDir.get(i)).exists()) {
                mlist = FilesInfo(new File(fileDir.get(i)), mContext);
            }
        }
        return mlist;
    }

    private static List<FileInfo> FilesInfo(File fileDir, Context mContext) {
        List<FileInfo> videoFilesInfo = new ArrayList<>();
        File[] listFiles = fileFilter(fileDir);
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    FilesInfo(file, mContext);
                } else {
                    FileInfo fileInfo = getFileInfoFromFile(file);
                    videoFilesInfo.add(fileInfo);
                }
            }
        }
        return videoFilesInfo;
    }

    public static List<FileInfo> getFileInfosFromFileArray(File[] files) {
        List<FileInfo> fileInfos = new ArrayList<>();
        for (File file : files) {
            FileInfo fileInfo = getFileInfoFromFile(file);
            fileInfos.add(fileInfo);
        }
        Collections.sort(fileInfos, new FileNameComparator());
        return fileInfos;
    }

    /**
     * 根据文件名进行比较排序
     */
    public static class FileNameComparator implements Comparator<FileInfo> {
        protected final static int
                FIRST = -1,
                SECOND = 1;

        @Override
        public int compare(FileInfo lhs, FileInfo rhs) {
            if (lhs.isDirectory() || rhs.isDirectory()) {
                if (lhs.isDirectory() == rhs.isDirectory())
                    return lhs.getFileName().compareToIgnoreCase(rhs.getFileName());
                else if (lhs.isDirectory()) return FIRST;
                else return SECOND;
            }
            return lhs.getFileName().compareToIgnoreCase(rhs.getFileName());
        }
    }

    public static FileInfo getFileInfoFromFile(File file) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(file.getName());
        fileInfo.setFilePath(file.getPath());
        fileInfo.setFileSize(file.length());
        fileInfo.setDirectory(file.isDirectory());
        fileInfo.setTime(FileUtil.getFileLastModifiedTime(file));
        int lastDotIndex = file.getName().lastIndexOf(".");
        if (lastDotIndex > 0) {
            String fileSuffix = file.getName().substring(lastDotIndex + 1);
            fileInfo.setSuffix(fileSuffix);
        }
        return fileInfo;
    }

    public static List<FolderInfo> queryFolderInfo(Context context, List<Uri> mlist) {
        List<FolderInfo> folderInfos = new ArrayList<>();

        for (int i = 0; i < mlist.size(); i++) {
            String[] projection = new String[]{
                    MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    MediaStore.Files.FileColumns.DATE_MODIFIED
            };
            Cursor cursor = context.getContentResolver().query(
                    mlist.get(i),
                    projection, null,
                    null, projection[2] + " DESC");

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int dataindex = cursor
                            .getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    int nameindex = cursor
                            .getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                    int timeindex = cursor
                            .getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED);
                    do {
                        FileInfo fileInfo = new FileInfo();
                        String path = cursor.getString(dataindex);
                        String name = cursor.getString(nameindex);
                        String time = cursor.getString(timeindex);
                        fileInfo.setFileSize(new File(path).length());
                        fileInfo.setFilePath(path);
                        fileInfo.setFileName(name);
                        fileInfo.setTime(time);
                        FolderInfo folderInfo = getImageFolder(path, folderInfos);
                        folderInfo.getImages().add(fileInfo);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        }
        return folderInfos;

    }

    public static List<FileInfo> queryFilerInfo(Context context, List<Uri> mlist, String selection, String[] selectionArgs) {
        List<FileInfo> fileInfos = new ArrayList<>();
        for (int i = 0; i < mlist.size(); i++) {
            String[] projection = new String[]{
                    MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.TITLE,
                    MediaStore.Files.FileColumns.DATE_MODIFIED
            };
            Cursor cursor = context.getContentResolver().query(
                    mlist.get(i),
                    projection, selection,
                    selectionArgs, projection[2] + " DESC");

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int dataindex = cursor
                            .getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    int nameindex = cursor
                            .getColumnIndex(MediaStore.Files.FileColumns.TITLE);
                    int timeindex = cursor
                            .getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED);
                    do {
                        FileInfo fileInfo = new FileInfo();
                        String path = cursor.getString(dataindex);
                        String name = cursor.getString(nameindex);
                        String time = cursor.getString(timeindex);
                        fileInfo.setFileSize(new File(path).length());
                        fileInfo.setFilePath(path);
                        fileInfo.setFileName(name);
                        fileInfo.setTime(time);
                        fileInfos.add(fileInfo);

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        }
        return fileInfos;

    }

    public static FolderInfo getImageFolder(String path, List<FolderInfo> imageFolders) {
        File imageFile = new File(path);
        File folderFile = imageFile.getParentFile();

        for (FolderInfo folder : imageFolders) {
            if (folder.getName().equals(folderFile.getName())) {
                return folder;
            }
        }
        FolderInfo newFolder = new FolderInfo();
        newFolder.setName(folderFile.getName());
        newFolder.setPath(folderFile.getAbsolutePath());
        imageFolders.add(newFolder);
        return newFolder;
    }
    public static Bitmap fileGetBitmap(Context mContext,File file) throws IOException {
        Bitmap bitmap;
        Resources resources = mContext.getResources();
        Drawable drawable = null;
        BitmapDrawable bd;
        if (checkSuffix(file.getName(), new String[]{"mp3","wav", "aac", "amr","ogg"})) {
            drawable = resources.getDrawable(R.drawable.rc_ad_list_audio_icon);
        } else if (checkSuffix(file.getName(), new String[]{"wmv", "rmvb", "avi", "mp4"})) {//获取视频第一帧
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(file.getPath());
            return  media.getFrameAtTime();

        } else if (checkSuffix(file.getName(), new String[]{"wav", "aac", "amr"})) {
            drawable = resources.getDrawable(R.drawable.rc_ad_list_video_icon);
        }else if (checkSuffix(file.getName(), new String[]{"doc", "docx", "dot"})) {
            drawable = resources.getDrawable(R.mipmap.word);
        }
        else if (checkSuffix(file.getName(), new String[]{"xls"})) {
            drawable = resources.getDrawable( R.mipmap.excel);
        }
        else if (checkSuffix(file.getName(), new String[]{"pdf"})) {
            drawable = resources.getDrawable( R.mipmap.pdf);
        }
        else if (checkSuffix(file.getName(), new String[]{"ppt", "pptx"})) {
            drawable = resources.getDrawable( R.mipmap.ppt);
        }
        else if (checkSuffix(file.getName(), new String[]{"txt"})) {
            drawable = resources.getDrawable( R.mipmap.txt);
        }
        else if(checkSuffix(file.getName(), new String[]{"apk"})){

        }
        else if(checkSuffix(file.getName(),new String[]{"zip"})){
            drawable = resources.getDrawable(R.mipmap.zip);
        }
        else if(checkSuffix(file.getName(),new String[]{"jpg","png","gif","bmp"})){
            FileInputStream fis = new FileInputStream(file.getPath());
            bitmap  = BitmapFactory.decodeStream(fis);
            fis.close();
            return bitmap;
        }

        else
            drawable = resources.getDrawable( R.drawable.rc_ad_list_other_icon);

        bd = (BitmapDrawable) drawable;
        bitmap= bd.getBitmap();
        return bitmap;
    }
    public static Bitmap fileGetBitmap(Context mContext,String url) throws IOException {
        Bitmap bitmap;

        Resources resources = mContext.getResources();
        Drawable drawable = null;
        BitmapDrawable bd;
        if (checkSuffix(url, new String[]{"mp3","wav", "aac", "amr","ogg"})) {
            drawable = resources.getDrawable(R.drawable.rc_ad_list_audio_icon);
        } else if (checkSuffix(url, new String[]{"wmv", "rmvb", "avi", "mp4"})) {//获取视频第一帧

            return  createVideoThumbnail(url,MediaStore.Images.Thumbnails.MINI_KIND);

        } else if (checkSuffix(url, new String[]{"wav", "aac", "amr"})) {
            drawable = resources.getDrawable(R.drawable.rc_ad_list_video_icon);
        }else if (checkSuffix(url, new String[]{"doc", "docx", "dot"})) {
            drawable = resources.getDrawable(R.mipmap.word);
        }
        else if (checkSuffix(url, new String[]{"xls"})) {
            drawable = resources.getDrawable( R.mipmap.excel);
        }
        else if (checkSuffix(url, new String[]{"pdf"})) {
            drawable = resources.getDrawable( R.mipmap.pdf);
        }
        else if (checkSuffix(url, new String[]{"ppt", "pptx"})) {
            drawable = resources.getDrawable( R.mipmap.ppt);
        }
        else if (checkSuffix(url, new String[]{"txt"})) {
            drawable = resources.getDrawable( R.mipmap.txt);
        }
        else if(checkSuffix(url, new String[]{"apk"})){

        }
        else if(checkSuffix(url,new String[]{"zip"})){
            drawable = resources.getDrawable(R.mipmap.zip);
        }
        else if(checkSuffix(url,new String[]{"jpg","png","gif","bmp"})){

            bitmap  =getBitmap(url);
            return bitmap;
        }

        else
            drawable = resources.getDrawable( R.drawable.rc_ad_list_other_icon);

        bd = (BitmapDrawable) drawable;
        bitmap= bd.getBitmap();
        return bitmap;
    }
    public static Bitmap getBitmap(String path) throws IOException {

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }
    public static Bitmap createVideoThumbnail(String filePath, int kind)
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try
        {
            if (filePath.startsWith("http://")
                    || filePath.startsWith("https://")
                    || filePath.startsWith("widevine://"))
            {
                retriever.setDataSource(filePath, new Hashtable<String, String>());
            }
            else
            {
                retriever.setDataSource(filePath);
            }
            bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC); //retriever.getFrameAtTime(-1);
        }
        catch (IllegalArgumentException ex)
        {
            // Assume this is a corrupt video file
            ex.printStackTrace();
        }
        catch (RuntimeException ex)
        {
            // Assume this is a corrupt video file.
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                retriever.release();
            }
            catch (RuntimeException ex)
            {
                // Ignore failures while cleaning up.
                ex.printStackTrace();
            }
        }

        if (bitmap == null)
        {
            return null;
        }

        if (kind == MediaStore.Images.Thumbnails.MINI_KIND)
        {//压缩图片 开始处
            // Scale down the bitmap if it's too large.
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int max = Math.max(width, height);
            if (max > 512)
            {
                float scale = 512f / max;
                int w = Math.round(scale * width);
                int h = Math.round(scale * height);
                bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
            }//压缩图片 结束处
        }
        else if (kind == MediaStore.Images.Thumbnails.MICRO_KIND)
        {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                    96,
                    96,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

}
