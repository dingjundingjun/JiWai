package com.org.great.world.Views;

import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.LoginUtils;
import com.org.great.world.Utils.Util;
import com.org.great.world.Utils.LoginUtils.onCallBack;
import com.org.great.world.activities.MyApplication;
import com.org.great.wrold.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginView 
{
	public static final int REQUEST_CODE_SETNICK = 1;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private String currentUsername;
	private String currentPassword;
	private View mMainView;
	private TextView mShowError;
	private OnLoginCallback mOnLoginCallback;
	private Context mContext;
	public LoginView(Context context) {
		mContext = context;
		mMainView = View.inflate(context, R.layout.setting_login_layout, null).findViewById(R.id.main_layout);
		init(mMainView);
	}
	
	public View getView()
	{
		return mMainView;
	}
	
	private void init(View view)
	{
		usernameEditText = (EditText) view.findViewById(R.id.username);
		passwordEditText = (EditText) view.findViewById(R.id.password);
		mShowError = (TextView)view.findViewById(R.id.show_error);
//		view.findViewById(R.id.btn_register).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				register();
//			}
//		});
		
		view.findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				login();
			}
		});
		// 如果用户名改变，清空密码
		usernameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordEditText.setText(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}
	
	/**
	 * 登录
	 * 
	 * @param view
	 */
	public void login() {
		if (!Util.checkWifiConnected(mContext)) {
			Toast.makeText(mContext, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return;
		}
		currentUsername = usernameEditText.getText().toString().trim();
		currentPassword = passwordEditText.getText().toString().trim();

		if (TextUtils.isEmpty(currentUsername)) {
			Toast.makeText(mContext, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(currentPassword)) {
			Toast.makeText(mContext, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if(currentUsername.length() > 15)
		{
			Toast.makeText(mContext, R.string.User_name_is_too_long, Toast.LENGTH_SHORT).show();
			return;
		}
		final LoginUtils ra = LoginUtils.getInstance(mContext);
		new Thread(new Runnable() {
			public void run() {
	            	Util.hideSoftKeyboard(mContext, passwordEditText);
	            	ra.loginFromSever(false,currentUsername, currentPassword);
			}
		}).start();
		ra.setCallBack(new onCallBack() {
			
			@Override
			public void onRegisterSuccess() {
			}
			
			@Override
			public void onRegisterError() {
			}
			
			@Override
			public void onLoginSuccess() {
				Util.setLogin(mContext, true);
				mOnLoginCallback.onSuccess();
			}
			
			@Override
			public void onLoginError(String errorMsg) {
				Util.setLogin(mContext, false);
				mOnLoginCallback.onError();
				mShowError.setText(errorMsg);
			}
		});
	}

	
	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register() {
		mOnLoginCallback.onRegister();
	}
	
	public void setOnLoginListener(OnLoginCallback oc)
	{
		mOnLoginCallback = oc;
	}
	
	public interface OnLoginCallback 
	{
		public void onSuccess();
		public void onError();
		public void onRegister();
	}
}
