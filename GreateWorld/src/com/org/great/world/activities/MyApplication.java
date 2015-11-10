package com.org.great.world.activities;

import com.easemob.EMCallBack;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.org.great.world.Utils.PersonalUtil;
import com.org.great.world.Utils.Util;
import com.youku.player.YoukuPlayerBaseConfiguration;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

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
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();
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
	        /**
	         * this function will initialize the HuanXin SDK
	         * 
	         * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
	         * 
	         * 环信初始化SDK帮助函数
	         * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
	         * 
	         * for example:
	         * 例子：
	         * 
	         * public class DemoHXSDKHelper extends HXSDKHelper
	         * 
	         * HXHelper = new DemoHXSDKHelper();
	         * if(HXHelper.onInit(context)){
	         *     // do HuanXin related work
	         * }
	         */
	        hxSDKHelper.onInit(applicationContext);
	        PersonalUtil.isLogined(this);
		
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
	
	public static MyApplication getInstance() {
		return instance;
	}
 

	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public String getUserName() {
	    return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param user
	 */
	public void setUserName(String username) {
	    hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 *
	 * @param pwd
	 */
	public void setPassword(String pwd) {
	    hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final boolean isGCM,final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
	    hxSDKHelper.logout(isGCM,emCallBack);
	}


}
