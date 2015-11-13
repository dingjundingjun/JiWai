package com.org.great.world.db;


import java.util.List;

import com.org.great.world.Utils.Debug;
import com.org.great.world.data.UserInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDBHelp {
	private Context mContext;
	private static String DB_NAME = "jiwai.db";
    // 数据库版本
    private static int DB_VERSION = 2;
    private SQLiteDatabase db;
    private SQLiteHelp dbHelper;
	public UserDBHelp(Context c) {
		this.mContext = c;
		dbHelper = new SQLiteHelp(mContext, DB_NAME, null, DB_VERSION );
        db = dbHelper.getWritableDatabase();
	}
	
	  public void Close() {
          db.close();
          dbHelper.close();
    }
	  
	public Long insertUserInfo(UserInfo user) {
		 Debug.d("insertUserInfo = " + user);
         ContentValues values = new ContentValues();
         values.put("accountId", ""+user.getAccountId());
         values.put("loginName", user.getLoginName());
         values.put("nickName", user.getNickName());
         values.put("photoPath", user.getPhotoPath());
         Long uid = db.insert(SQLiteHelp.TB_NAME, null, values);
         return uid;
    }
	
	// 更新users表的记录
    public int UpdateUserInfo(UserInfo user) {
    	 
         ContentValues values = new ContentValues();
         values.put("accountId", ""+user.getAccountId());
         values.put("loginName", user.getLoginName());
         values.put("nickName", user.getNickName());
         values.put("photoPath", user.getPhotoPath());
         String[] str = new String[]{user.getLoginName()};
         int id = db.update(SQLiteHelp.TB_NAME, values, "loginName=?", str);
         return id;
    }
    
    public boolean isCanUpdataUser(UserInfo user)
    {
    	UserInfo tempUsr = getUserInfo(user.getLoginName());
    	if(tempUsr == null)
    	{
    		Debug.d("return true");
    		return true;
    	}
    	if(tempUsr.getPhotoPath() == null || user.getPhotoPath() == null)
    	{
	   	 	if(tempUsr.getPhotoPath().equals(user.getPhotoPath()) && tempUsr.getNickName().equals(user.getNickName()))
	   	 	{
	   	 		Debug.d("return false");
	   	 		return false;
	   	 	}
    	}
   	 	return true;
    }
    
    public Boolean hasUserInfo(String loginName) {
    	
        Boolean b = false;
        Cursor cursor = db.query(SQLiteHelp.TB_NAME, null, "loginName"
                 + "=?", new String[]{loginName}, null, null, null );
        b = cursor.moveToFirst();
        
        cursor.close();
        Debug.d("hasUserInfo + " + loginName + " b = " + b + " cursor.getCount() = " + cursor.getCount());
        return b;
   }
    
    public UserInfo getUserInfo(String loginName) {
        Boolean b = false;
        Cursor cursor = db.query(SQLiteHelp.TB_NAME, null, "loginName"
                 + "=?", new String[]{loginName}, null, null, null );
        b = cursor.moveToFirst();
        if(!b)
        {
        	return null;
        }
        UserInfo user = new UserInfo();
        user.setLoginName(cursor.getString(cursor.getColumnIndex("loginName")));
        user.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
        user.setPhotoPath(cursor.getString(cursor.getColumnIndex("photoPath")));
        cursor.close();
        return user;
   }
    
    public byte[] getUserImage(String loginName)
    {
    	Boolean b = false;
        Cursor cursor = db.query(SQLiteHelp.TB_NAME, null, "loginName"
                 + "=?", new String[]{loginName}, null, null, null );
        b = cursor.moveToFirst();
        if(!b)
        {
        	return null;
        }
        byte[] bmp = cursor.getBlob(cursor.getColumnIndex("image"));
        cursor.close();
        return bmp;
    }
}
