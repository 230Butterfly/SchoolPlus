package com.example.chenwei.plus.Upload.bean;

import com.admom.mygreendaotest.FileInfoDao;
import com.example.chenwei.plus.Share;
import com.example.chenwei.plus.Upload.base.BaseApplication;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class FileDao {
    /**
     * 添加数据，如果有重复则覆盖
     */
    public static void insertFile(FileInfo fileInfo) {
        Share.getDaoInstant().getFileInfoDao().insertOrReplace(fileInfo);
    }

    /**
     * 删除数据
     */
    public static void deleteFile(FileInfo fileInfo) {
        Share.getDaoInstant().getFileInfoDao().delete(fileInfo);
    }

    /**
     * 更新数据
     */
    public static void updateFile(FileInfo fileInfo) {
        Share.getDaoInstant().getFileInfoDao().update(fileInfo);
    }


    /**
     * 查询全部数据
     */
    public static List<FileInfo> queryAll() {
        Share m =new Share();
        return m.getDaoInstant().getFileInfoDao().loadAll();

    }

    /**
     * 删除全部数据
     */
    public static void deleteAll1() {
        Share.getDaoInstant().getFileInfoDao().deleteAll();
    }

    public static boolean isContain(String ID) {
        QueryBuilder<FileInfo> qb = Share.getDaoInstant().getFileInfoDao().queryBuilder();
        qb.where(FileInfoDao.Properties.FileName.eq(ID));
        qb.buildCount().count();
        return qb.buildCount().count() > 0 ? true : false;
    }
}
