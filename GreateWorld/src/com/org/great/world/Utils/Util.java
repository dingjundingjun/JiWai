package com.org.great.world.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
//import com.org.great.world.data.AllAD;
import com.org.great.world.data.BaseCatalogPojo;
import com.org.great.world.data.BaseUserInfoPojo;
import com.org.great.world.data.CatalogPojo;
import com.org.great.world.data.UserInfo;
import com.org.great.world.db.UserDBHelp;
import com.org.great.wrold.R;

public class Util 
{
	public static UserDBHelp gUserDBHelp;
	public static String gGroupId;
	public static boolean B_XH = false;
//	public static boolean IS_LOGINED = false;
	/**刷新的时间间隔*/
	public static int REFRESH_TIME_INTERVAL = 1;
	/**游戏时间间隔 （分）*/
	public static int PLAY_GAME_INTERVAL = 30;
	/**更新聊天信息*/
	public static int LOAD_USER_INFO_INTERVAL = 3;
	public static boolean isMain = true;
	public static byte[] charToByte(char c) {   
        byte[] b = new byte[2];   
        b[0] = (byte) ((c & 0xFF00) >> 8);   
        b[1] = (byte) (c & 0xFF);   
        return b;   
    }  
	
	public static void saveHeadIcon(Context c,String path)
	{
		Bitmap bmp = null;
		try {
		byte head[] = getImage(path);
		if(head != null)
		{
			bmp = BitmapFactory.decodeByteArray(head, 0, head.length);
		}
		else
		{
			bmp = BitmapFactory.decodeResource(c.getResources(), R.drawable.default_avatar);
		}
			bmp.compress(CompressFormat.PNG, 100, new FileOutputStream(LoginUtils.ICON_PATH));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bmp.recycle();
	}
public static byte[] getImage(String path) throws Exception{  
		
		if(TextUtils.isEmpty(path)){
			return null;
		}
		if(path.equals("null") || path == null)
		{
			return null;
		}
        URL url = new URL(path); 
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
        conn.setConnectTimeout(5 * 1000);  
        conn.setRequestMethod("GET");  
        InputStream inStream = conn.getInputStream();  
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){  
            return readStream(inStream);  
        }  
        
        return null;  
    }  

public static final String PicIconPath = "/mnt/sdcard/jiwai/pic/";
public static boolean getImage(UserInfo user,String path) throws Exception{  
	
	if(TextUtils.isEmpty(path)){
		return false;
	}
	if(path.equals("null") || path == null)
	{
		return false;
	}
    URL url = new URL(path); 
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
    conn.setConnectTimeout(5 * 1000);  
    conn.setRequestMethod("GET");  
    InputStream inStream = conn.getInputStream();  
    byte[] bmpByte = null;
    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){  
        bmpByte = readStream(inStream);  
    }  
    Bitmap tempBmp = BitmapFactory.decodeByteArray(bmpByte, 0, bmpByte.length);
    File file = new File(PicIconPath);
    if(!file.exists())
    {
    	file.mkdirs();
    }
    String iconPath = PicIconPath + user.getLoginName() + ".jpg";
    Debug.d("iconPath = " + iconPath);
    FileOutputStream os = new FileOutputStream(iconPath);
    tempBmp.compress(CompressFormat.JPEG, 50, os);
    os.close();
    tempBmp.recycle();
    return true;  
} 

public static byte[] readStream(InputStream inStream) throws Exception{  
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
    byte[] buffer = new byte[1024];  
    int len = 0;  
    while( (len=inStream.read(buffer)) != -1){  
        outStream.write(buffer, 0, len);  
    }  
    outStream.close();  
    inStream.close();  
    return outStream.toByteArray();  
}  
	public static boolean isFileExsit(String path)
	{
		File file = new File(path);
		if(file.exists())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public static String decodeUnicode(final String dataStr) {   
        int start = 0;   
          int end = 0;   
         final StringBuffer buffer = new StringBuffer();   
          while (start > -1) {   
             end = dataStr.indexOf("\\u", start + 2);   
              String charStr = "";   
              if (end == -1) {   
                  charStr = dataStr.substring(start + 2, dataStr.length());   
             } else {   
                 charStr = dataStr.substring(start + 2, end);   
              }   
              char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。   
            buffer.append(new Character(letter).toString());   
            start = end;   
          }   
          return buffer.toString();   
      }  
	
	public static String getAllUnicode(int unic)
	{
		final StringBuffer buffer = new StringBuffer();  
		char letter = 0;
//		for(int i = 0; i < 65535;i++)
		{
			letter = (char) unic;
			String t = new Character(letter).toString();
			buffer.append(t);
		}
		return buffer.toString();
	}
	
	public static String getAllUnicode(int start,int count)
	{
		final StringBuffer buffer = new StringBuffer();  
		char letter = 0;
		for(int i = 0; i < 65535;i++)
		{
			letter = (char) i;
			String t = new Character(letter).toString() + "(" + i +")";
			buffer.append(t);
		}
		return buffer.toString();
	}
	
	public static byte[] readFile(String path)
	{
		byte[] rData = null;
		try {
			FileInputStream fis = new FileInputStream(path);
			int length = fis.available();
			rData = new byte[length];
			fis.read(rData, 0, length);
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rData;
	}
	
	    public static String getCurrentTime() {
			return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
		}
	    
	    public static String getCurrentTime(String format) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
			String currentTime = sdf.format(date);
			return currentTime;
		}

    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public static boolean checkWifiConnected(Context context)
    {
        ConnectivityManager net = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((net == null) || (net.getActiveNetworkInfo() == null) || (!net.getActiveNetworkInfo().isConnected()))
        {
            return false;
        }
        return true;
    }

    public static void showAlertDialog(Context context,String title,String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.tip_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    
    /**
     * 是否自动刷新，从服务器获取最新列表。这样做是为了防止每次进入都从服务器获取一次，浪费资源
     * @return
     */
    public static boolean isCanFresh(Context context)
    {
    	String lastTimeStr = getLastFreshTime(context);
    	long lastTime = Long.decode(lastTimeStr);
    	Debug.d("lastTime = " + lastTime);
    	if(lastTime != -1)
    	{
    		if(System.currentTimeMillis()/1000 - lastTime > REFRESH_TIME_INTERVAL*3600)
    		{
    			return true;
    		}
    		else
    		{
    			return false;
    		}
    	}
    	else
    	{
    		return true;
    	}
    }
    
    /**
     * 返回一个long值，表示距离上一次相隔多少秒
     * @param context
     * @return
     */
    public static boolean isCanPlaygame(Context context)
    {
//    	if(AllAD.bShowAD == false)
//    	{
//    		return true;
//    	}
    	String lastTimeStr = getPlayGameTime(context);
    	long lastTime = Long.decode(lastTimeStr);
    	Debug.d("lastTime = " + lastTime);
    	if(lastTime != -1)
    	{
    		long minute = (System.currentTimeMillis()/1000 - lastTime)/60;
    		if(PLAY_GAME_INTERVAL - minute > 0)
    		{
    			int dur = (int) (PLAY_GAME_INTERVAL - minute);
    			//Toast.makeText(context, "再过" + dur + "分钟才可以玩哦!如果点击广告，马上可以进游戏", Toast.LENGTH_SHORT).show();
    			return true;
    		}
    		else
    		{
    			return false;
    		}
    	}
    	else
    	{
    		return false;
    	}
    }
    
    /**
     * 返回一个long值，表示距离上一次相隔多少秒
     * @param context
     * @return
     */
    public static boolean isCanLoadUserInfo(Context context)
    {
    	String lastTimeStr = getLoadUserInfoTime(context);
    	long lastTime = Long.decode(lastTimeStr);
    	Debug.d("lastTime = " + lastTime);
    	if(lastTime != -1)
    	{
    		long minute = (System.currentTimeMillis()/1000 - lastTime)/60;
    		if(LOAD_USER_INFO_INTERVAL - minute > 0)
    		{
    			return false;
    		}
    		else
    		{
    			return true;
    		}
    	}
    	else
    	{
    		return true;
    	}
    }
    
    public static void saveFreshTime(Context context)
    {
    	SharedPreferences preferences;
		Editor prefsEditor;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		prefsEditor = preferences.edit();
		prefsEditor.putString("last_fresh_time","" + System.currentTimeMillis()/1000);
		prefsEditor.commit();
    }
    
    public static String getLastFreshTime(Context context)
    {
    	SharedPreferences preferences;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		return preferences.getString("last_fresh_time", "-1");
    }
    
    public static void setLogin(Context context,boolean logined)
    {
    	SharedPreferences preferences;
		Editor prefsEditor;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		prefsEditor = preferences.edit();
		prefsEditor.putBoolean("isLogined",logined);
		prefsEditor.commit();
    }
    
    public static boolean getLogined(Context context)
    {
    	SharedPreferences preferences;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		return preferences.getBoolean("isLogined", false);
    }
    
    public static void saveSeeWorldJson(Context context,String json)
    {
    	SharedPreferences preferences;
		Editor prefsEditor;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		prefsEditor = preferences.edit();
		prefsEditor.putString("seeworld_json",json);
		prefsEditor.commit();
    }
    
    public static String getSeeWorldJson(Context context)
    {
    	SharedPreferences preferences;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		return preferences.getString("seeworld_json", null);
    }
    
    public static String getGameJson(Context context)
    {
    	SharedPreferences preferences;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		return preferences.getString("game_json", null);
    }
    
    public static void saveGameJson(Context context,String json)
    {
    	SharedPreferences preferences;
		Editor prefsEditor;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		prefsEditor = preferences.edit();
		prefsEditor.putString("game_json",json);
		prefsEditor.commit();
    }
    
    public static String getPlayGameTime(Context context)
    {
    	SharedPreferences preferences;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		return preferences.getString("last_play_game_time", "-1");
    }
    
    public static void savePlayGameTime(Context context)
    {
    	SharedPreferences preferences;
		Editor prefsEditor;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		prefsEditor = preferences.edit();
		prefsEditor.putString("last_play_game_time","" + System.currentTimeMillis()/1000);
		prefsEditor.commit();
    }
    
    public static String getLoadUserInfoTime(Context context)
    {
    	SharedPreferences preferences;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		return preferences.getString("last_load_user_info_time", "-1");
    }
    
    public static void saveLoadUserInfoTime(Context context)
    {
    	SharedPreferences preferences;
		Editor prefsEditor;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		prefsEditor = preferences.edit();
		prefsEditor.putString("last_load_user_info_time","" + System.currentTimeMillis()/1000);
		prefsEditor.commit();
    }
    
    public static void resetPlayGameTime(Context context)
    {
    	SharedPreferences preferences;
		Editor prefsEditor;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		prefsEditor = preferences.edit();
		prefsEditor.putString("last_play_game_time","" + -1);
		prefsEditor.commit();
    }
    
    public static void saveJokeJson(Context context,String json)
    {
    	SharedPreferences preferences;
		Editor prefsEditor;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		prefsEditor = preferences.edit();
		prefsEditor.putString("joke_json",json);
		prefsEditor.commit();
    }
    
    public static void saveJoGroupId(Context context,String id)
    {
    	SharedPreferences preferences;
		Editor prefsEditor;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		prefsEditor = preferences.edit();
		prefsEditor.putString("groupId",id);
		prefsEditor.commit();
    }
    
    public static void setUpdateInfo(Context context,boolean bUpdate)
    {
    	SharedPreferences preferences;
		Editor prefsEditor;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		prefsEditor = preferences.edit();
		prefsEditor.putBoolean("update_info",bUpdate);
		prefsEditor.commit();
    }
    
    public static boolean getUpdateInfo(Context context)
    {
    	SharedPreferences preferences;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		return preferences.getBoolean("update_info", false);
    }
    
    public static String getGroupId(Context context)
    {
    	SharedPreferences preferences;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		return preferences.getString("groupId", "-1");
    }
    
    public static String getJokeJson(Context context)
    {
    	SharedPreferences preferences;
		preferences = context.getSharedPreferences("JIWAI", Context.MODE_PRIVATE);
		return preferences.getString("joke_json", null);
    }
    
    public static void hideSoftKeyboard(Context context,View view)
    {
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
    }
    
    public static void IsEnableAd()
	{
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
				HttpGet get = new HttpGet("http://int.dpool.sina.com.cn/iplookup/iplookup.php?");
				HttpClient client = new DefaultHttpClient();
				final HttpParams httpParameters = client.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters, 60 * 1000);
				HttpConnectionParams.setSoTimeout(httpParameters, 60 * 1000);
				HttpResponse responseGet = null;
				responseGet = client.execute(get);
				HttpEntity he = responseGet.getEntity();
				String contentData = EntityUtils.toString(he, "gb2312");
				if(contentData.contains("东莞"))
            	{
					isMain = true;
            	}
            	else
            	{
            		isMain = false;
            	}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
    public static class Constants {

        public static final String DESCRIPTOR = "com.umeng.share";

        private static final String TIPS = "请移步官方网站 ";
        private static final String END_TIPS = ", 查看相关说明.";
        public static final String TENCENT_OPEN_URL = TIPS + "http://wiki.connect.qq.com/android_sdk使用说明"
                + END_TIPS;
        public static final String PERMISSION_URL = TIPS + "http://wiki.connect.qq.com/openapi权限申请"
                + END_TIPS;

        public static final String SOCIAL_LINK = "http://www.umeng.com/social";
        public static final String SOCIAL_TITLE = "友盟社会化组件帮助应用快速整合分享功能";
        public static final String SOCIAL_IMAGE = "http://www.umeng.com/images/pic/banner_module_social.png";

        public static final String SOCIAL_CONTENT = "友盟社会化组件（SDK）让移动应用快速整合社交分享功能，我们简化了社交平台的接入，为开发者提供坚实的基础服务：（一）支持各大主流社交平台，" +
                "（二）支持图片、文字、gif动图、音频、视频；@好友，关注官方微博等功能" +
                "（三）提供详尽的后台用户社交行为分析。http://www.umeng.com/social";

    }
    
    
    /**
     * 从服务器取信息，存数据库
     * @param context
     * @param loginName
     */
    public static void getOneUserInfo(final Context context,String loginName)
    {
    	if(loginName == null || loginName.equals(""))
    		return;
    	//这里做一个定时控制  不能每次都从服务器去取
//    	if(gUserDBHelp.hasUserInfo(loginName))
//    	{
//    		return;
//    	}
//    	saveLoadUserInfoTime(context);
    	Debug.d("1111111111111111111111111111111111111111111111111111111 load  11111111111111111");
    	AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    	String url = HttpUtils.RequestUrl.URL_GET_USER_INFO + "?loginName=" + loginName;
		RequestHandle handle = asyncHttpClient.get(url, new BaseJsonHttpResponseHandler()
		{
		    @Override
		    public void onFailure(int arg0, Header[] arg1, Throwable arg2, String arg3, Object arg4)
		    {
		    }
		
		    @Override
		    public void onSuccess(int arg0, Header[] arg1, String arg2, Object arg3)
		    {
		        if(!Util.isEmpty(arg2))
		        {
		            Gson gson = new Gson();
		            BaseUserInfoPojo pojo = gson.fromJson(arg2, BaseUserInfoPojo.class);
		            if(pojo.getStatus().equals("success"))
		            {
		                Debug.d("json = " + arg2);
		                final UserInfo tempUsr = pojo.getData();
		                if(tempUsr != null )
		                {
		                	if(!gUserDBHelp.isCanUpdataUser(tempUsr))
		                	{
		                		return;
		                	}
		                	Thread thread = new Thread(new Runnable() {
								
								@Override
								public void run() {
									Debug.d("tempUsr = " + tempUsr.getLoginName());
				                	boolean b;
			                		try {
										b = getImage(tempUsr,tempUsr.getPhotoPath());
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
				                	if(!gUserDBHelp.hasUserInfo(tempUsr.getLoginName()))
				                	{
				                		gUserDBHelp.insertUserInfo(tempUsr);
				                	}
				                	else
				                	{
				                		gUserDBHelp.UpdateUserInfo(tempUsr);
				                	}
								}
							});
		                	thread.start();
		                }
		            }
		        }
		    }
		
		    @Override
		    protected Object parseResponse(String arg0, boolean arg1) throws Throwable
		    {
		        return arg0;
		    }
		});
    }
    
    public static UserInfo getUserByName(String name)
    {
    	return gUserDBHelp.getUserInfo(name);
    }
    
    public static void createDB(Context c)
    {
    	if(gUserDBHelp == null)
    	{
    		gUserDBHelp = new UserDBHelp(c);
    	}
    }
    
    public static void closeDB()
    {
    	if(gUserDBHelp != null)
    	{
    		gUserDBHelp.Close();
    	}
    }
    
    private Bitmap revitionImageSize(String path, int size) throws IOException {  
        // 取得图片  
        InputStream temp = new FileInputStream(path);  
        BitmapFactory.Options options = new BitmapFactory.Options();  
        // 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化  
        options.inJustDecodeBounds = true;  
        // 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）  
        BitmapFactory.decodeStream(temp, null, options);  
        // 关闭流  
        temp.close();  
  
        // 生成压缩的图片  
        int i = 0;  
        Bitmap bitmap = null;  
        while (true) {  
            // 这一步是根据要设置的大小，使宽和高都能满足  
            if ((options.outWidth >> i <= size)  
                    && (options.outHeight >> i <= size)) {  
                // 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！  
                temp = new FileInputStream(path);  
                // 这个参数表示 新生成的图片为原始图片的几分之一。  
                options.inSampleSize = (int) Math.pow(2.0D, i);  
                // 这里之前设置为了true，所以要改为false，否则就创建不出图片  
                options.inJustDecodeBounds = false;  
  
                bitmap = BitmapFactory.decodeStream(temp, null, options);  
                break;  
            }  
            i += 1;  
        }  
        return bitmap;  
    }
    
    public static void downloadCatalogPic(final Context context,final String name,final String url)
    {
    	if(TextUtils.isEmpty(url))
		{
			Toast.makeText(context, "图片链接有问题，稍后重试。", Toast.LENGTH_SHORT).show();
			return;
		}
		
//		if( !isSDMounted() ) 
//		{
//			Toast.makeText(context, "已连接USB，请关闭后重试。", Toast.LENGTH_SHORT).show();
//			return;
//		}
		
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() 
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) 
			{
				System.out.println("==========statusCode >> " + statusCode);
				int idx = url.lastIndexOf(".");
				String ext = url.substring(idx);
					
				String sdcard = Environment.getExternalStorageDirectory().toString();
				File file = new File(sdcard + "/叽歪/目录");
				if (!file.exists()) {
					file.mkdirs();
				}
				
				OutputStream outputStream = null;
		
				Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
				CompressFormat format = Bitmap.CompressFormat.JPEG;				
				int quality = 100;
				
				try 
				{
					file = new File(sdcard + "/叽歪/目录/" + name);
					outputStream = new FileOutputStream(file);
					bmp.compress(format, quality, outputStream);
//					Toast.makeText(context, "图片已保存至：" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
				} 
				catch (IOException e) 
				{					
					e.printStackTrace();
//					Toast.makeText(context, "下载失败，稍后重试。" + e.toString(), Toast.LENGTH_SHORT).show();
				} 
				finally 
				{
					if(outputStream != null) 
					{
						try 
						{
							outputStream.close();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}
				}
			}
			
			public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) 
			{
//				Toast.makeText(context, "下载失败，稍后重试。" + error.toString(), Toast.LENGTH_SHORT).show();
			}

		});
    }
    
    /**
	 * @param url
	 *            要下载的文件URL
	 * @throws Exception
	 */
	public static void downloadFile(final Context context, final String nameSuffix, final String url) 
	{
		if(TextUtils.isEmpty(url))
		{
			Toast.makeText(context, "图片链接有问题，稍后重试。", Toast.LENGTH_SHORT).show();
			return;
		}
		
		/*if( !isSDMounted() ) 
		{
			Toast.makeText(context, "已连接USB，请关闭后重试。", Toast.LENGTH_SHORT).show();
			return;
		}*/
		
		System.out.println("==========nameSuffix >> " + nameSuffix);
		System.out.println("==========url >> " + url);
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() 
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) 
			{
				System.out.println("==========statusCode >> " + statusCode);
				int idx = url.lastIndexOf(".");
				String ext = url.substring(idx);
					
				String sdcard = Environment.getExternalStorageDirectory().toString();
				File file = new File(sdcard + "/叽歪/");
				if (!file.exists()) {
					file.mkdirs();
				}
				
				OutputStream outputStream = null;
		
				Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
				CompressFormat format = Bitmap.CompressFormat.JPEG;				
				int quality = 100;
				
				try 
				{
					file = new File(file.getAbsolutePath(), "图片_" + System.currentTimeMillis() + ext);
					outputStream = new FileOutputStream(file);
					bmp.compress(format, quality, outputStream);
					
					Toast.makeText(context, "图片已保存至：" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

				} 
				catch (IOException e) 
				{					
					e.printStackTrace();
					Toast.makeText(context, "下载失败，稍后重试。" + e.toString(), Toast.LENGTH_SHORT).show();
				} 
				finally 
				{
					if(outputStream != null) 
					{
						try 
						{
							outputStream.close();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}
				}
			}
			
			public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) 
			{
				Toast.makeText(context, "下载失败，稍后重试。" + error.toString(), Toast.LENGTH_SHORT).show();
			}

		});
	}
}
