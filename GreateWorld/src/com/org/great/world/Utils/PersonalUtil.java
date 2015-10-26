package com.org.great.world.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.org.great.world.data.PersonalInfoPojo;
import com.org.great.wrold.R;
import com.umeng.socialize.bean.Gender;
import com.umeng.socialize.bean.SnsAccount;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;

public class PersonalUtil {
	public static SnsAccount mSnsAccount = new SnsAccount(Build.SERIAL,Gender.MALE,"","");
	/**
	 * judge is logined
	 * @return
	 */
	public static boolean isLogined(Context context)
	{
		PersonalInfoPojo pi = getPersonInfo(context);
		if(pi == null)
		{
			mSnsAccount.setUserName("小明");
			mSnsAccount.setUsid(Build.SERIAL);
			mSnsAccount.setAccountIconUrl(null);
			return false;
		}
		mSnsAccount = new SnsAccount(pi.getNickName(), Gender.MALE, pi.getPhotoPath(),pi.getAccountId().toString());
		return true;
	}
	
	
	public static boolean isTextCanInput(Context context, String str) {
		// ������%��&,$���κ��ַ���
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~��@#��%����&*��������+|{}������������������������]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if(m.find() || str.contains(" "))
		{
			Toast.makeText(context, R.string.can_not_contain,Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	public static void savePersonInfo(Context context, PersonalInfoPojo personalInfo)
	{
		SharedPreferences preferences;
		String jsonStr = null;
		Editor prefsEditor;
		
		preferences = context.getSharedPreferences("personalinfo", Context.MODE_PRIVATE);
		prefsEditor = preferences.edit();
		jsonStr = JsonTools.objToGson(personalInfo);
		Log.d("savePersonInfo","jsonStr = " + jsonStr);
		prefsEditor.putString("personalinfo", jsonStr);

		prefsEditor.commit();
	}
	
	public static void delPersonInfo(Context context)
	{
		SharedPreferences preferences;
		Editor prefsEditor;
		
		preferences = context.getSharedPreferences("personalinfo", Context.MODE_PRIVATE);
		prefsEditor = preferences.edit();
		prefsEditor.clear();
		prefsEditor.commit();
	}
	
	
	public static PersonalInfoPojo getPersonInfo(Context context)
	{
		PersonalInfoPojo personnalInfo = null;
		String personInfoStr = null;
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("personalinfo", Context.MODE_PRIVATE);
		personInfoStr = preferences.getString("personalinfo", "");
		if( !TextUtils.isEmpty(personInfoStr)){
			personnalInfo = JsonTools.GsonToObj(personInfoStr,PersonalInfoPojo.class);
		}
		
		return personnalInfo;
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
				File file = new File(sdcard + "/带你看世界/");
				if (!file.exists()) {
					file.mkdirs();
				}
				
				OutputStream outputStream = null;
		
				Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
				CompressFormat format = CompressFormat.JPEG;
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

	public static byte[] getImage(String path) throws Exception{  
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
	
	public static void login(Context context,UMSocialService socialService)
	{
		socialService.login(context, mSnsAccount, new SocializeListeners.SocializeClientListener() {
            @Override
            public void onStart() {

            }
            @Override
            public void onComplete(int arg0, SocializeEntity arg1) {
            }
        });
	}
}
