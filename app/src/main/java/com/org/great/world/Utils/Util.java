package com.org.great.world.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.org.great.wrold.R;

public class Util 
{
	public static byte[] charToByte(char c) {   
        byte[] b = new byte[2];   
        b[0] = (byte) ((c & 0xFF00) >> 8);   
        b[1] = (byte) (c & 0xFF);   
        return b;   
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
}
