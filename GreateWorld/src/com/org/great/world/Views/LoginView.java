package com.org.great.world.Views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.org.great.world.activities.MyApplication;
import com.org.great.wrold.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginView 
{
	public static final int REQUEST_CODE_SETNICK = 1;
	private static final int LOGIN_SUCCESS_MSG = 1;
	private static final int LOGIN_ERROR_MSG = 2;
	private EditText usernameEditText;
	private EditText passwordEditText;

	private boolean progressShow;
	private boolean autoLogin = false;

	private String currentUsername;
	private String currentPassword;
	
	private View mMainView;

	private OnLoginCallback mOnLoginCallback;
	private Context mContext;
	private ProgressDialog mProgressDialog;
	private UIHandler mUIHandler = new UIHandler();
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
		view.findViewById(R.id.btn_register).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				register();
			}
		});
		
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
		if (MyApplication.getInstance().getUserName() != null) {
			usernameEditText.setText(MyApplication.getInstance().getUserName());
		}
	}
	
	/**
	 * 登录
	 * 
	 * @param view
	 */
	public void login() {
		if (!CommonUtils.isNetWorkConnected(mContext)) {
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

		progressShow = true;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				progressShow = false;
			}
		});
		mProgressDialog.setMessage(mContext.getString(R.string.Is_landing));
		mProgressDialog.show();

		final long start = System.currentTimeMillis();
		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

			@Override
			public void onSuccess() {
				if (!progressShow) {
					return;
				}
				// 登陆成功，保存用户名密码
				MyApplication.getInstance().setUserName(currentUsername);
				MyApplication.getInstance().setPassword(currentPassword);

				try {
					// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
					// ** manually load all local groups and
				    EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					// 处理好友和群组
					initializeContacts();
				} catch (Exception e) {
					e.printStackTrace();
					mUIHandler.sendEmptyMessage(LOGIN_ERROR_MSG);
					return;
				}
				// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
				boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
						MyApplication.currentUserNick.trim());
				mUIHandler.sendEmptyMessage(LOGIN_SUCCESS_MSG);
			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(final int code, final String message) {
				if (!progressShow) {
					return;
				}
				mUIHandler.sendEmptyMessage(LOGIN_ERROR_MSG);
			}
		});
	}

	private void initializeContacts() {
		Map<String, User> userlist = new HashMap<String, User>();
		// 添加user"申请与通知"
		User newFriends = new User();
		newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
		String strChat = mContext.getResources().getString(
				R.string.Application_and_notify);
		newFriends.setNick(strChat);

		userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
		// 添加"群聊"
		User groupUser = new User();
		String strGroup = mContext.getResources().getString(R.string.group_chat);
		groupUser.setUsername(Constant.GROUP_USERNAME);
		groupUser.setNick(strGroup);
		groupUser.setHeader("");
		userlist.put(Constant.GROUP_USERNAME, groupUser);
		
		// 添加"Robot"
		User robotUser = new User();
		String strRobot = mContext.getResources().getString(R.string.robot_chat);
		robotUser.setUsername(Constant.CHAT_ROBOT);
		robotUser.setNick(strRobot);
		robotUser.setHeader("");
		userlist.put(Constant.CHAT_ROBOT, robotUser);
		
		// 存入内存
		((DemoHXSDKHelper)HXSDKHelper.getInstance()).setContactList(userlist);
		// 存入db
		UserDao dao = new UserDao(mContext);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);
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
	
	public class UIHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what)
			{
			case LOGIN_SUCCESS_MSG:
			{
				if(mProgressDialog != null && mProgressDialog.isShowing())
				{
					mProgressDialog.dismiss();
				}
				mOnLoginCallback.onSuccess();
				break;
			}
			case LOGIN_ERROR_MSG:
			{
				if(mProgressDialog != null && mProgressDialog.isShowing())
				{
					mProgressDialog.dismiss();
				}
				DemoHXSDKHelper.getInstance().logout(true,null);
				Toast.makeText(mContext, R.string.login_failure_failed, 1).show();
				mOnLoginCallback.onError();
				break;
			}
			}
		}
	}
}
