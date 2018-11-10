package com.example.chenwei.plus.Upload.base;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.admom.mygreendaotest.DaoMaster;
import com.admom.mygreendaotest.DaoSession;

import cn.bmob.v3.Bmob;

/**
 * Created by CWJ on 2017/3/20.
 */

public class BaseApplication extends Application {
    private static DaoSession daoSession ;


    @Override
    public void onCreate() {
        super.onCreate();
        //配置数据库
        Bmob.initialize(this, "0fe7c7cb6eff2cf5a1f4d40288246b42");
        setupDatabase();
    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库shop.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "file.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
        Log.e("base",daoSession.toString());
    }

    public static DaoSession getDaoInstant() {
        Log.e("base",daoSession.toString());
        return daoSession;
    }
}
