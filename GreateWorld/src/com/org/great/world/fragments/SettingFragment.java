package com.org.great.world.fragments;

import java.io.File;

import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Intent;
import android.content.Loader;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.RegisterAndLogin;
import com.org.great.world.Utils.Util;
import com.org.great.world.Views.LoginView;
import com.org.great.world.Views.MyInfomationView;
import com.org.great.world.Views.MyInfomationView.OnSettingCallback;
import com.org.great.world.Views.SettingView;
import com.org.great.world.Views.LoginView.OnLoginCallback;
import com.org.great.world.Views.RegisterView;
import com.org.great.world.Views.RegisterView.OnRegisterCallback;
import com.org.great.wrold.R;
import com.soundcloud.android.crop.Crop;
import com.soundcloud.android.crop.CropImageActivity;

public class SettingFragment extends BaseFragment
{
	private boolean autoLogin = false;
	private LoginView mLoginView;
	private RegisterView mRegisterView;
//	private SettingView mSettingView;
	private MyInfomationView mMyInfomationView;
	private RelativeLayout mMainView;
	private View mAttachView;
	
	private HttpClient httpClient; 
	private HttpParams httpParams;
	
	 @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBaseActivity = getActivity();
	 }

	@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 如果用户名密码都有，直接进入主页面
		Debug.d("onCreateView mMainView = " + mMainView);
		mMainView = (RelativeLayout) RelativeLayout.inflate(mBaseActivity, R.layout.setting_main_layout, null);
		mBaseActivity = getActivity();
		init(mMainView);
	    return mMainView;
	}
	
	private void init(View view)
	{
		if (Util.getLogined(mBaseActivity)) {
			autoLogin = true;
//			showSettingView();
			showMyInfomationView();
		}
		else
		{
			showLoginView();
		}
	}
	
	/**
	 * 显示登录界面
	 * @return
	 */
	private void showLoginView()
	{
		Debug.d("showLoginView " + mLoginView);
		if(mLoginView == null)
		{
			mLoginView = new LoginView(mBaseActivity);
		}
		mMainView.removeAllViewsInLayout();
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mAttachView = mLoginView.getView();
		ViewGroup vg = (ViewGroup)mAttachView.getParent();
		if(vg != null)
		{
			vg.removeAllViewsInLayout();
		}
		mMainView.addView(mAttachView,lp);
		mLoginView.setOnLoginListener(new OnLoginCallback() {
			
			@Override
			public void onSuccess() {
//				showSettingView();
				showMyInfomationView();
			}
			
			@Override
			public void onRegister() {
				showRegisterView();
			}
			
			@Override
			public void onError() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * 显示注册界面
	 * @return
	 */
	private void showRegisterView()
	{
		if(mRegisterView == null)
		{
			mRegisterView = new RegisterView(mBaseActivity);
		}
		mMainView.removeAllViewsInLayout();
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mAttachView = mRegisterView.getView();
		ViewGroup vg = (ViewGroup)mAttachView.getParent();
		if(vg != null)
		{
			vg.removeAllViewsInLayout();
		}
		mMainView.addView(mAttachView,lp);
		mRegisterView.setOnRegisterListener(new OnRegisterCallback() {
			
			@Override
			public void onSuccess() {
//				showSettingView();
				showMyInfomationView();
			}
			
			@Override
			public void onError() {
			}

			@Override
			public void onBack() {
				showLoginView();
			}
		});
	}
	
	private void showMyInfomationView()
	{
		Debug.d("showSettingView :" + mMyInfomationView);
		if(mMyInfomationView == null)
		{
			mMyInfomationView = new MyInfomationView(mBaseActivity);
		}
//		mSettingView.update();
		mMainView.removeAllViewsInLayout();
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mAttachView = mMyInfomationView.getView();
		ViewGroup vg = (ViewGroup)mAttachView.getParent();
		if(vg != null)
		{
			vg.removeAllViewsInLayout();
		}
		mMainView.addView(mAttachView,lp);
		mMyInfomationView.setOnSettingListener(new OnSettingCallback() {
			@Override
			public void onLoginOut() {
				showLoginView();
			}

			@Override
			public void onSettingHead() {
				gotoSelectHead();
			}
		});
	}
	
	private void gotoSelectHead()
	{
		Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 111);
	}
	
    private void beginCrop(Uri source) {
    	String ICON_PATH = mBaseActivity.getFilesDir().getAbsolutePath() + File.separator + "head_temp.png";
        Uri destination = Uri.fromFile(new File(ICON_PATH));
        Intent cropIntent = new Intent();
        cropIntent.setData(source);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, destination);
        cropIntent.setClass(this.getActivity(), CropImageActivity.class);
        startActivityForResult(cropIntent, Crop.REQUEST_CROP);
    }
    
    private void handleCrop(int resultCode, Intent result) {

        if (resultCode == mBaseActivity.RESULT_OK && result != null) {
        	mMyInfomationView.uploadInfo();
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(mBaseActivity, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111)
        {
            if(data != null)
            {
                beginCrop(data.getData());
            }
        }
        else if(requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }
	
	/**
	 * 显示详细界面
	 * @return
	 */
//	private void showSettingView()
//	{
//		Debug.d("showSettingView :" + mSettingView);
//		if(mSettingView == null)
//		{
//			mSettingView = new SettingView(mBaseActivity);
//		}
//		mSettingView.update();
//		mMainView.removeAllViewsInLayout();
//		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		mAttachView = mSettingView.getView();
//		ViewGroup vg = (ViewGroup)mAttachView.getParent();
//		if(vg != null)
//		{
//			vg.removeAllViewsInLayout();
//		}
//		mMainView.addView(mAttachView,lp);
//		mSettingView.setOnSettingListener(new OnSettingCallback() {
//			@Override
//			public void onLoginOut() {
//				showLoginView();
//			}
//		});
//	}
}
