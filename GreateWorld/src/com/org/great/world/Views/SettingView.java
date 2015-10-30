package com.org.great.world.Views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.DemoHXSDKModel;
import com.easemob.chatuidemo.activity.OfflinePushNickActivity;
import com.easemob.chatuidemo.activity.UserProfileActivity;
import com.org.great.world.Views.RegisterView.OnRegisterCallback;
import com.org.great.world.Views.RegisterView.UIHandler;
import com.org.great.wrold.R;

public class SettingView implements OnClickListener
{
	private final int SETTING_LOGIN_OUT_SUCCESS = 1;
	private View mMainView;
	private Context mContext;
	private UIHandler mUIHandler = new UIHandler();
	private ProgressDialog mProgressDialog;
	private OnSettingCallback mOnSettingCallback;
	/**
	 * 设置新消息通知布局
	 */
	private RelativeLayout rl_switch_notification;
	/**
	 * 设置声音布局
	 */
	private RelativeLayout rl_switch_sound;
	/**
	 * 设置震动布局
	 */
	private RelativeLayout rl_switch_vibrate;
	/**
	 * 设置扬声器布局
	 */
	private RelativeLayout rl_switch_speaker;

	/**
	 * 打开新消息通知imageView
	 */
	private ImageView iv_switch_open_notification;
	/**
	 * 关闭新消息通知imageview
	 */
	private ImageView iv_switch_close_notification;
	/**
	 * 打开声音提示imageview
	 */
	private ImageView iv_switch_open_sound;
	/**
	 * 关闭声音提示imageview
	 */
	private ImageView iv_switch_close_sound;
	/**
	 * 打开消息震动提示
	 */
	private ImageView iv_switch_open_vibrate;
	/**
	 * 关闭消息震动提示
	 */
	private ImageView iv_switch_close_vibrate;
	/**
	 * 打开扬声器播放语音
	 */
	private ImageView iv_switch_open_speaker;
	/**
	 * 关闭扬声器播放语音
	 */
	private ImageView iv_switch_close_speaker;

	/**
	 * 声音和震动中间的那条线
	 */
	private TextView textview1, textview2;

	private LinearLayout blacklistContainer;
	
	private LinearLayout userProfileContainer;
	
	/**
	 * 退出按钮
	 */
	private Button logoutBtn;

	private RelativeLayout rl_switch_chatroom_leave;
	private ImageView iv_switch_room_owner_leave_allow;
	private ImageView iv_switch_room_owner_leave_disallow;
	
	private EMChatOptions chatOptions;
 
	/**
	 * 诊断
	 */
	private LinearLayout llDiagnose;
	/**
	 * iOS离线推送昵称
	 */
	private LinearLayout pushNick;
	
	DemoHXSDKModel model;
	public SettingView(Context context) {
		mContext = context;
		mMainView = View.inflate(context, R.layout.setting_layout, null).findViewById(R.id.main_layout);
		init(mMainView);
	}
	
	public View view()
	{
		return mMainView;
	}
	
	public View getView()
	{
		return mMainView;
	}
	
	private void init(View view)
	{
		rl_switch_notification = (RelativeLayout) view.findViewById(R.id.rl_switch_notification);
		rl_switch_sound = (RelativeLayout) view.findViewById(R.id.rl_switch_sound);
		rl_switch_vibrate = (RelativeLayout) view.findViewById(R.id.rl_switch_vibrate);
		rl_switch_speaker = (RelativeLayout) view.findViewById(R.id.rl_switch_speaker);
		rl_switch_chatroom_leave = (RelativeLayout) view.findViewById(R.id.rl_switch_chatroom_owner_leave);

		iv_switch_open_notification = (ImageView) view.findViewById(R.id.iv_switch_open_notification);
		iv_switch_close_notification = (ImageView)view.findViewById(R.id.iv_switch_close_notification);
		iv_switch_open_sound = (ImageView) view.findViewById(R.id.iv_switch_open_sound);
		iv_switch_close_sound = (ImageView) view.findViewById(R.id.iv_switch_close_sound);
		iv_switch_open_vibrate = (ImageView) view.findViewById(R.id.iv_switch_open_vibrate);
		iv_switch_close_vibrate = (ImageView) view.findViewById(R.id.iv_switch_close_vibrate);
		iv_switch_open_speaker = (ImageView) view.findViewById(R.id.iv_switch_open_speaker);
		iv_switch_close_speaker = (ImageView) view.findViewById(R.id.iv_switch_close_speaker);
		
		iv_switch_room_owner_leave_allow = (ImageView) view.findViewById(R.id.iv_switch_chatroom_owner_leave_allow);
		iv_switch_room_owner_leave_disallow = (ImageView) view.findViewById(R.id.iv_switch_chatroom_owner_leave_not_allow);
		
		
		logoutBtn = (Button) view.findViewById(R.id.btn_logout);
		if(!TextUtils.isEmpty(EMChatManager.getInstance().getCurrentUser())){
			logoutBtn.setText(mContext.getString(R.string.button_logout) + "(" + EMChatManager.getInstance().getCurrentUser() + ")");
		}

		textview1 = (TextView) view.findViewById(R.id.textview1);
		textview2 = (TextView) view.findViewById(R.id.textview2);
		
		blacklistContainer = (LinearLayout) view.findViewById(R.id.ll_black_list);
		userProfileContainer = (LinearLayout) view.findViewById(R.id.ll_user_profile);
		llDiagnose=(LinearLayout) view.findViewById(R.id.ll_diagnose);
		pushNick=(LinearLayout) view.findViewById(R.id.ll_set_push_nick);
		
		blacklistContainer.setOnClickListener(this);
		userProfileContainer.setOnClickListener(this);
		rl_switch_notification.setOnClickListener(this);
		rl_switch_sound.setOnClickListener(this);
		rl_switch_vibrate.setOnClickListener(this);
		rl_switch_speaker.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		llDiagnose.setOnClickListener(this);
		pushNick.setOnClickListener(this);
		rl_switch_chatroom_leave.setOnClickListener(this);
		
		chatOptions = EMChatManager.getInstance().getChatOptions();
		
		model = (DemoHXSDKModel) HXSDKHelper.getInstance().getModel();
		
		// 震动和声音总开关，来消息时，是否允许此开关打开
		// the vibrate and sound notification are allowed or not?
		if (model.getSettingMsgNotification()) {
			iv_switch_open_notification.setVisibility(View.VISIBLE);
			iv_switch_close_notification.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_notification.setVisibility(View.INVISIBLE);
			iv_switch_close_notification.setVisibility(View.VISIBLE);
		}
		
		// 是否打开声音
		// sound notification is switched on or not?
		if (model.getSettingMsgSound()) {
			iv_switch_open_sound.setVisibility(View.VISIBLE);
			iv_switch_close_sound.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_sound.setVisibility(View.INVISIBLE);
			iv_switch_close_sound.setVisibility(View.VISIBLE);
		}
		
		// 是否打开震动
		// vibrate notification is switched on or not?
		if (model.getSettingMsgVibrate()) {
			iv_switch_open_vibrate.setVisibility(View.VISIBLE);
			iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
			iv_switch_close_vibrate.setVisibility(View.VISIBLE);
		}

		// 是否打开扬声器
		// the speaker is switched on or not?
		if (model.getSettingMsgSpeaker()) {
			iv_switch_open_speaker.setVisibility(View.VISIBLE);
			iv_switch_close_speaker.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_speaker.setVisibility(View.INVISIBLE);
			iv_switch_close_speaker.setVisibility(View.VISIBLE);
		}

		// 是否允许聊天室owner leave
		if(model.isChatroomOwnerLeaveAllowed()){
		    iv_switch_room_owner_leave_allow.setVisibility(View.VISIBLE);
		    iv_switch_room_owner_leave_disallow.setVisibility(View.INVISIBLE);
		}else{
		    iv_switch_room_owner_leave_allow.setVisibility(View.INVISIBLE);
            iv_switch_room_owner_leave_disallow.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_switch_notification:
			if (iv_switch_open_notification.getVisibility() == View.VISIBLE) {
				iv_switch_open_notification.setVisibility(View.INVISIBLE);
				iv_switch_close_notification.setVisibility(View.VISIBLE);
				rl_switch_sound.setVisibility(View.GONE);
				rl_switch_vibrate.setVisibility(View.GONE);
				textview1.setVisibility(View.GONE);
				textview2.setVisibility(View.GONE);
				chatOptions.setNotificationEnable(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);

				HXSDKHelper.getInstance().getModel().setSettingMsgNotification(false);
			} else {
				iv_switch_open_notification.setVisibility(View.VISIBLE);
				iv_switch_close_notification.setVisibility(View.INVISIBLE);
				rl_switch_sound.setVisibility(View.VISIBLE);
				rl_switch_vibrate.setVisibility(View.VISIBLE);
				textview1.setVisibility(View.VISIBLE);
				textview2.setVisibility(View.VISIBLE);
				chatOptions.setNotificationEnable(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgNotification(true);
			}
			break;
		case R.id.rl_switch_sound:
			if (iv_switch_open_sound.getVisibility() == View.VISIBLE) {
				iv_switch_open_sound.setVisibility(View.INVISIBLE);
				iv_switch_close_sound.setVisibility(View.VISIBLE);
				chatOptions.setNoticeBySound(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgSound(false);
			} else {
				iv_switch_open_sound.setVisibility(View.VISIBLE);
				iv_switch_close_sound.setVisibility(View.INVISIBLE);
				chatOptions.setNoticeBySound(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgSound(true);
			}
			break;
		case R.id.rl_switch_vibrate:
			if (iv_switch_open_vibrate.getVisibility() == View.VISIBLE) {
				iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
				iv_switch_close_vibrate.setVisibility(View.VISIBLE);
				chatOptions.setNoticedByVibrate(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(false);
			} else {
				iv_switch_open_vibrate.setVisibility(View.VISIBLE);
				iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
				chatOptions.setNoticedByVibrate(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(true);
			}
			break;
		case R.id.rl_switch_speaker:
			if (iv_switch_open_speaker.getVisibility() == View.VISIBLE) {
				iv_switch_open_speaker.setVisibility(View.INVISIBLE);
				iv_switch_close_speaker.setVisibility(View.VISIBLE);
				chatOptions.setUseSpeaker(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgSpeaker(false);
			} else {
				iv_switch_open_speaker.setVisibility(View.VISIBLE);
				iv_switch_close_speaker.setVisibility(View.INVISIBLE);
				chatOptions.setUseSpeaker(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(true);
			}
			break;
		case R.id.rl_switch_chatroom_owner_leave:
		    if(this.iv_switch_room_owner_leave_allow.getVisibility() == View.VISIBLE){
		        iv_switch_room_owner_leave_allow.setVisibility(View.INVISIBLE);
                iv_switch_room_owner_leave_disallow.setVisibility(View.VISIBLE);
                chatOptions.allowChatroomOwnerLeave(false);
                EMChatManager.getInstance().setChatOptions(chatOptions);
                model.allowChatroomOwnerLeave(false);

		    }else{
		        iv_switch_room_owner_leave_allow.setVisibility(View.VISIBLE);
                iv_switch_room_owner_leave_disallow.setVisibility(View.INVISIBLE);
                chatOptions.allowChatroomOwnerLeave(true);
                EMChatManager.getInstance().setChatOptions(chatOptions);
                model.allowChatroomOwnerLeave(true);
		    }
		    break;
		case R.id.btn_logout: //退出登陆
			logout();
			break;
		case R.id.ll_black_list:
//			mContext.startActivity(new Intent(mContext, BlacklistActivity.class));
			break;
		case R.id.ll_diagnose:
//			mContext.startActivity(new Intent(mContext, DiagnoseActivity.class));
			break;
		case R.id.ll_set_push_nick:
			mContext.startActivity(new Intent(mContext, OfflinePushNickActivity.class));
			break;
		case R.id.ll_user_profile:
			mContext.startActivity(new Intent(mContext, UserProfileActivity.class).putExtra("setting", true));
			break;
		default:
			break;
		}
	}
	
	void logout() {
		mProgressDialog = new ProgressDialog(mContext);
		String st = mContext.getResources().getString(R.string.Are_logged_out);
		mProgressDialog.setMessage(st);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();
		DemoHXSDKHelper.getInstance().logout(true,new EMCallBack() {
			
			@Override
			public void onSuccess() {
				mUIHandler.sendEmptyMessage(SETTING_LOGIN_OUT_SUCCESS);
			}
			
			@Override
			public void onProgress(int progress, String status) {
				
			}
			
			@Override
			public void onError(int code, String message) {
			}
		});
	}
	
	public class UIHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what)
			{
			case SETTING_LOGIN_OUT_SUCCESS:
			{
				if(mProgressDialog != null && mProgressDialog.isShowing())
				{
					mProgressDialog.dismiss();
				}
				mOnSettingCallback.onLoginOut();
				break;
			}
			}
		}
	}
	
	public void setOnSettingListener(OnSettingCallback oc)
	{
		mOnSettingCallback = oc;
	}
	
	public interface OnSettingCallback 
	{
		public void onLoginOut();
	}
}
