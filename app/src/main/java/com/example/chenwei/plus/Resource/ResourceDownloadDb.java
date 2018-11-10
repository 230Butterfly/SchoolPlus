package com.example.chenwei.plus.Resource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class ResourceDownloadDb extends SQLiteOpenHelper {
    public static final String CREATE_Resource_Download = "create table ResourceDownload("
            +"id integer primary key autoincrement, "
            + "user_id text , "
            + "resource_name text, "
            + "resource_grade integer,"
            + "price integer,"
            + "distance  integer,"
            + "label text, "
            + "time text, "
            + "size text, "
            + "introduction text, "
            + "path text,"
            + "person_name  text ,"
            + "url text)";

    private Context mContext;
    public ResourceDownloadDb(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Resource_Download);

        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).
                show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists ResourceDownload");
        onCreate(db);
    }
}
