package com.org.great.world.activities;

import com.org.great.world.Utils.PersonalUtil;
import com.org.great.world.Utils.Util;
import com.youku.player.YoukuPlayerBaseConfiguration;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

/**
 * 接入时自定义的Application需要继承YoukuPlayerBaseConfiguration 
 *
 */
public class MyApplication extends Application {

	public static YoukuPlayerBaseConfiguration configuration;
	public static Context applicationContext;
	private static MyApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";
	
	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	@Override
	public void onCreate() {
		super.onCreate();
		
		 applicationContext = this;
	     instance = this;
	     
	     int dip = this.getResources().getDisplayMetrics().densityDpi;
	     if(dip == 160)
	     {
	    	 Util.B_XH = false;
	     }
	     else
	     {
	    	 Util.B_XH = true;
	     }
	        PersonalUtil.isLogined(this);
		String device = Build.CPU_ABI;
		Log.d("device","devcice = " + device);
		if(!Build.CPU_ABI.equals("mips"))
		{
			configuration = new YoukuPlayerBaseConfiguration(this)
			{
				/**
				 * 通过覆写该方法，返回“正在缓存视频信息的界面”，
				 * 则在状态栏点击下载信息时可以自动跳转到所设定的界面.
				 * 用户需要定义自己的缓存界面
				 */
				@Override
				public Class<? extends Activity> getCachingActivityClass() {
					// TODO Auto-generated method stub
					return CachingActivity.class;
				}
	
				/**
				 * 通过覆写该方法，返回“已经缓存视频信息的界面”，
				 * 则在状态栏点击下载信息时可以自动跳转到所设定的界面.
				 * 用户需要定义自己的已缓存界面
				 */
	
				@Override
				public Class<? extends Activity> getCachedActivityClass() {
					// TODO Auto-generated method stub
					return CachedActivity.class;
				}
	
				/**
				 * 配置视频的缓存路径，格式举例： /appname/videocache/
				 * 如果返回空，则视频默认缓存路径为： /应用程序包名/videocache/
				 *
				 */
				@Override
				public String configDownloadPath() {
					// TODO Auto-generated method stub
	
					//return "/myapp/videocache/";			//举例
					return null;
				}
			};
		}
	}
	
	public static MyApplication getInstance() {
		return instance;
	}
}
