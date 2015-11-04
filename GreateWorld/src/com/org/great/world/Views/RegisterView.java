package com.org.great.world.Views;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.org.great.world.Utils.RegisterAndLogin;
import com.org.great.world.Utils.RegisterAndLogin.onCallBack;
import com.org.great.world.Utils.Util;
import com.org.great.wrold.R;

public class RegisterView 
{
	private static final int REGISTER_SUCCESS_MSG = 1;
	private static final int REGISTER_ERROR_MSG = 2;
	private View mMainView;
	private Context mContext;
	private EditText userNameEditText;
	private EditText passwordEditText;
	private EditText confirmPwdEditText;
	private OnRegisterCallback mOnRegisterCallback;
	public RegisterView(Context context) {
		mContext = context;
		mMainView = View.inflate(context, R.layout.setting_register_layout, null).findViewById(R.id.main_layout);
		init(mMainView);
	}
	
	public View getView()
	{
		return mMainView;
	}
	
	private void init(View view)
	{
		userNameEditText = (EditText) view.findViewById(R.id.username);
		passwordEditText = (EditText) view.findViewById(R.id.password);
		confirmPwdEditText = (EditText) view.findViewById(R.id.confirm_password);
		view.findViewById(R.id.btn_register).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				register();
			}
		});
		
		view.findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				back();
			}
		});
	}
	
	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register() {
		final String username = userNameEditText.getText().toString().trim();
		final String pwd = passwordEditText.getText().toString().trim();
		String confirm_pwd = confirmPwdEditText.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(mContext, mContext.getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
			userNameEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(mContext, mContext.getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
			passwordEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(confirm_pwd)) {
			Toast.makeText(mContext, mContext.getResources().getString(R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
			confirmPwdEditText.requestFocus();
			return;
		} else if (!pwd.equals(confirm_pwd)) {
			Toast.makeText(mContext, mContext.getResources().getString(R.string.Two_input_password), Toast.LENGTH_SHORT).show();
			return;
		}
		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
			final RegisterAndLogin ra = RegisterAndLogin.getInstance(mContext);
			new Thread(new Runnable() {
				public void run() {
		            	Util.hideSoftKeyboard(mContext, passwordEditText);
		            	ra.loginFromSever(true,username, pwd);
				}
			}).start();
			ra.setCallBack(new onCallBack() {
				
				@Override
				public void onRegisterSuccess() {
					mOnRegisterCallback.onSuccess();
				}
				
				@Override
				public void onRegisterError() {
					mOnRegisterCallback.onError();
				}
				
				@Override
				public void onLoginSuccess() {
					mOnRegisterCallback.onSuccess();
				}
				
				@Override
				public void onLoginError() {
					mOnRegisterCallback.onError();
				}
			});

		}
	}

	public void setOnRegisterListener(OnRegisterCallback oc)
	{
		mOnRegisterCallback = oc;
	}
	
	public interface OnRegisterCallback 
	{
		public void onSuccess();
		public void onError();
		public void onBack();
	}
	
	public void back() 
	{
		mOnRegisterCallback.onBack();
	}
}
