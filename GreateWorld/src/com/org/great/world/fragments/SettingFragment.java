package com.org.great.world.fragments;

import java.io.File;

import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.Util;
import com.org.great.world.Views.LoginView;
import com.org.great.world.Views.SettingView;
import com.org.great.world.Views.LoginView.OnLoginCallback;
import com.org.great.world.Views.RegisterView;
import com.org.great.world.Views.RegisterView.OnRegisterCallback;
import com.org.great.world.Views.SettingView.OnSettingCallback;
import com.org.great.wrold.R;

public class SettingFragment extends BaseFragment
{
	private boolean autoLogin = false;
	private LoginView mLoginView;
	private RegisterView mRegisterView;
	private SettingView mSettingView;
	private RelativeLayout mMainView;
	private int mShowViewType = 0;
	private final int SHOW_LOGIN_VIEW = 1;
	private final int SHOW_REGISTER_VIEW = 2;
	private final int SHOW_SETTING_VIEW = 3;
	private View mAttachView;
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
		init(mMainView);
	    return mMainView;
	}
	
	private void init(View view)
	{
		if (DemoHXSDKHelper.getInstance().isLogined() || Util.IS_LOGINED) {
			autoLogin = true;
			Util.IS_LOGINED = true;
			showSettingView();
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
				showSettingView();
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
				showSettingView();
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
	
	/**
	 * 显示详细界面
	 * @return
	 */
	private void showSettingView()
	{
		Debug.d("showSettingView :" + mSettingView);
		if(mSettingView == null)
		{
			mSettingView = new SettingView(mBaseActivity);
		}
//		mMainView.removeAllViews();
		mMainView.removeAllViewsInLayout();
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mAttachView = mSettingView.getView();
		ViewGroup vg = (ViewGroup)mAttachView.getParent();
		if(vg != null)
		{
			vg.removeAllViewsInLayout();
		}
		mMainView.addView(mAttachView,lp);
		mSettingView.setOnSettingListener(new OnSettingCallback() {
			@Override
			public void onLoginOut() {
				showLoginView();
			}
		});
	}
}
