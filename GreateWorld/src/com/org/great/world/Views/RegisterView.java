package com.org.great.world.Views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.exceptions.EaseMobException;
import com.org.great.world.Views.LoginView.OnLoginCallback;
import com.org.great.world.Views.LoginView.UIHandler;
import com.org.great.world.activities.MyApplication;
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
	private ProgressDialog mProgressDialog;
	private UIHandler mUIHandler = new UIHandler();
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
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setMessage(mContext.getResources().getString(R.string.Is_the_registered));
			mProgressDialog.show();

			new Thread(new Runnable() {
				public void run() {
					try {
						// 调用sdk注册方法
						EMChatManager.getInstance().createAccountOnServer(username, pwd);
						MyApplication.getInstance().setUserName(username);
						mUIHandler.sendEmptyMessage(REGISTER_SUCCESS_MSG);
					} catch (final EaseMobException e) {
						Message msg = mUIHandler.obtainMessage();
						msg.what = REGISTER_ERROR_MSG;
						msg.arg1 = e.getErrorCode();
						mUIHandler.sendMessage(msg);
					}
				}
			}).start();

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
	
	public class UIHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what)
			{
			case REGISTER_SUCCESS_MSG:
			{
				if(mProgressDialog != null && mProgressDialog.isShowing())
				{
					mProgressDialog.dismiss();
				}
				Toast.makeText(mContext, mContext.getResources().getString(R.string.Registered_successfully), 0).show();
				mOnRegisterCallback.onSuccess();
				break;
			}
			case REGISTER_ERROR_MSG:
			{
				if(mProgressDialog != null && mProgressDialog.isShowing())
				{
					mProgressDialog.dismiss();
				}
				int errorCode = msg.arg1;
				if(errorCode==EMError.NONETWORK_ERROR){
					Toast.makeText(mContext, mContext.getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
				}else if(errorCode == EMError.USER_ALREADY_EXISTS){
					Toast.makeText(mContext, mContext.getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
				}else if(errorCode == EMError.UNAUTHORIZED){
					Toast.makeText(mContext, mContext.getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
				}else if(errorCode == EMError.ILLEGAL_USER_NAME){
				    Toast.makeText(mContext, mContext.getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(mContext, mContext.getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
				}
				mOnRegisterCallback.onError();
				break;
			}
			}
		}
	}
	
	public void back() 
	{
		mOnRegisterCallback.onBack();
	}
	
}
