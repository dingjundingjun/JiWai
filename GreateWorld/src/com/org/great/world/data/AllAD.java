package com.org.great.world.data;


import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.org.great.world.Utils.Util;
import com.org.great.wrold.R;
import com.qq.e.ads.AdListener;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;


public class AllAD 
{
	private Activity mActivity;
	public static RelativeLayout mAdLayout;
	public static boolean bShowAD = false;
	public AllAD(Activity ac) {
		super();
		this.mActivity = ac;
		
	}

//	public void setInsertAD(Activity ac)
//    {
//    	final InterstitialAd iad = new InterstitialAd(mActivity, "1104673565","4060403449827357");
//        iad.setAdListener(new InterstitialAdListener() {
//            @Override
//            public void onFail() {
//            }
//            public void onFail(int errorCode){
//            }
//            @Override
//            public void onBack() {
//            }
//            @Override
//            public void onAdReceive() {
//            	iad.show();
//            }
//            public void onExposure(){
//            }
//            public void onClicked(){
//            }           
//        });
//        iad.loadAd();
//        iad.show();
//    }
//	
//	public static void initPushAD(Context context)
//    {
//    	Ckm.getInstance(context).setCooId(context, "825a4812bcfa4890bd218f742d334500");
//		Ckm.getInstance(context).setChannelId(context, "k-goapk");
//		Ckm.getInstance(context).receiveMessage(context, true);
//    }
	
	public static RelativeLayout getGDTBannerView(final Context context) {
		IsEnableAd();
		mAdLayout = new RelativeLayout(context);
		
		int height = (int) context.getResources().getDimension(R.dimen.ad_height);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, height);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//		mAdLayout.setBackgroundColor(Color.RED);
//		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//		initPushAD(context);
		// ����Banner���AdView����
		// appId : �� http://e.qq.com/dev/ �ܿ�����appΨһ�ַ���
		// posId : �� http://e.qq.com/dev/ ���ɵ����ִ������� appid ���� appkey
		com.qq.e.ads.AdView adv = new com.qq.e.ads.AdView((Activity) context,
				AdSize.BANNER, "1104859259", "6000908664471278");
		mAdLayout.addView(adv, layoutParams);
//		layout.addView(adv);
		// ����������ݣ��������ù���ֲ�ʱ�䣬Ĭ��Ϊ30s
		AdRequest adr = new AdRequest();
		// ���ù��ˢ��ʱ�䣬Ϊ30~120֮������֣���λΪs,0��ʶ���Զ�ˢ��
		adr.setRefresh(30);
		// ��banner�����չʾ�رհ�ť
		adr.setShowCloseBtn(true);
		Log.d("bannerLayout", "getGDTBannerView");
		// ���ÿչ����״��յ�������ݻص�
		// ����fetchAd������ᷢ�������� */
		adv.setAdListener(new AdListener() {
			// ���ع��ʧ��ʱ�Ļص�
			public void onNoAd() {
				Log.d("bannerLayout", "onNoAd");
			}

			// ���ع��ʧ��ʱ�Ļص�
			public void onNoAd(int errorCode) {
				Log.d("bannerLayout", "onNoAd error");
				bShowAD = false;
			}

			// ���ع��ɹ�ʱ�Ļص�
			public void onAdReceiv() {
				Log.d("bannerLayout", "onAdReceiv");
				if(!bShowAD)
				{
					mAdLayout.setVisibility(View.GONE);
				}
				else
				{
//					initp(context);
				}
			}

			// Banner�ر�ʱ�Ļص�������չʾBanner�رհ�ťʱ��Ч
			public void onBannerClosed() {
				Log.d("bannerLayout", "onBannerClosed");
			}

			// Banner����ع�ʱ�Ļص�
			public void onAdExposure() {
				Log.d("bannerLayout", "onAdExposure");
			}

			// �������ʱ����
			public void onAdClicked() {
				Log.d("bannerLayout", "onAdClicked");
//				Util.resetPlayGameTime(context);
				Util.savePlayGameTime(context);
			}
		});
		adv.fetchAd(adr);
		return mAdLayout;
	}

	
	public static void IsEnableAd()
	{
		connectShowAD();
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
//				System.out.println("p=================" + contentData);
				if(contentData.contains("东莞"))
            	{
            		bShowAD = false;
            		myHandler.sendEmptyMessageDelayed(1,3000);
            	}
            	else
            	{
            		bShowAD = true;
            		myHandler.sendEmptyMessageDelayed(2,3000);
            	}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	
	public static Handler myHandler = new Handler() {  
        public void handleMessage(Message msg) {   
             switch (msg.what) {   
                  case 1:  
                	   if(mAdLayout != null)
                	   {
                		   mAdLayout.setVisibility(View.GONE);
                	   }
                       break;
                  case 2:
                	  if(mAdLayout != null)
                	  {
                		  mAdLayout.setVisibility(View.VISIBLE);
                	  }
                	  break;
             }   
             super.handleMessage(msg);   
        }   
   };
   
//   public static void initp(Context context)
//   {
//		String[] array = new String[] { 
//				"42c74e1cb7504bb3832861af7a8e0893",
//				"9c46975ea7f44ba380faac14f5491929",
//				"825a4812bcfa4890bd218f742d334500",
//				"e0a737311ada4ef3a187c641401b8823",
//				"95f75a4fd7fe4da6ac0088ef09949d6e",
//				"70bdb68a9a4845eea87bc3e43d35b125",
//				"5632cb9d105c4d0489da6065d3386be1",
//				"85d38b63330d4228830aa504fd1a0452",
//				"157a8ec036984a669b0eb4f3da373bd0", 
//				"986664e8ea2c4df0a2e3d2dd079042da"};
//		Calendar calendar = Calendar.getInstance();  
//	    int created = calendar.get(Calendar.MINUTE); 
//	    int index = created % (array.length + 1);   
//	    Ckm pm = Ckm.getInstance(context);
//		pm.setCooId(context, array[index]);//
//		pm.setChannelId(context, "k-gm");
//   }
   
   
   public static void connectShowAD()
	{
		new AsyncHttpClient().get("http://jyadmgr.sinaapp.com/checkADByPackage.php?package=com.org.great.wrold", new TextHttpResponseHandler() {
  			@Override
  			public void onSuccess(int arg0, Header[] arg1, String arg2) {
  				JSONObject jsonObject;
  				String adkey = "";
  				String adtype = "";
				try {
					jsonObject = new JSONObject(arg2);
					adkey = jsonObject.getString("adkey");
		            adtype = jsonObject.getString("adtype");
				} catch (JSONException e) {
					e.printStackTrace();
				}
  				if(true == adkey.equals("enable")){
  				}
  				else{
  					bShowAD = false;
  					myHandler.sendEmptyMessageDelayed(1, 3000);
  				}
  			}
  			@Override
  			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
  			}
  		});
	}
}
