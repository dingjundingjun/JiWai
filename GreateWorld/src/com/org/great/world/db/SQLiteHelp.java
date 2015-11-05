package com.org.great.world.db;

import com.org.great.world.data.UserInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelp extends SQLiteOpenHelper{
    //用来保存UserID、Access Token、Access Secret的表名
    public static final String TB_NAME= "users";
    private final String CREATE_USER_INFO = "CREATE TABLE IF NOT EXISTS "+
            TB_NAME+ "(accountId integer primary key,loginName varchar,nickName varchar,photoPath varchar)";
    public SQLiteHelp(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( CREATE_USER_INFO);
    }
    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TB_NAME );
        onCreate(db);
    }
    //更新列
    public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn){
        try{
            db.execSQL( "ALTER TABLE " +
                    TB_NAME + " CHANGE " +
                    oldColumn + " "+ newColumn +
                    " " + typeColumn
            );
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
}
