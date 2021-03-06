package com.org.great.world.Utils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.org.great.world.activities.MyApplication;
import com.org.great.world.data.PersonalInfoPojo;
import com.org.great.wrold.R;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.CommConfig;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.constants.ErrorCode;
import com.umeng.comm.core.db.ctrl.impl.DatabaseAPI;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.listeners.Listeners.CommListener;
import com.umeng.comm.core.listeners.Listeners.SimpleFetchListener;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.comm.core.nets.Response;
import com.umeng.comm.core.nets.responses.PortraitUploadResponse;
import com.umeng.comm.core.utils.CommonUtils;
import com.umeng.comm.core.utils.ToastMsg;
import com.umeng.comm.ui.activities.GuideActivity;
//import com.umeng.comm.ui.utils.BroadcastUtils;


/*
  data = {"sex":"","accountId":16427,"regTime":"2015-11-04T01:51:51.000Z","nickName":"没有名字的小伙伴","hxPassword":"a493f239048c4d5d0139cae34075a374","grade":"","hxUser":"ddd","photoPath":"","loginName":"ddd"}
	该类主要用来控制用户登录、退出。同时判断用户当前的登录状态
 * **/
public class LoginUtils 
{
	private static LoginUtils gRegisterAndLogin;
	private static final int LOGIN_SUCCESS_MSG = 1;
	private static final int LOGIN_ERROR_MSG = 2;
	private static final int LOGIN_ERROR_MSG_F = 3;
	private static final int REGISTER_SUCCESS_MSG = 4;
	private static final int REGISTER_ERROR_MSG = 5;
	private static final int REGISTER_HAS_EXSIT = 6;
	private static final int SHOW_PROGRESS = 100;
	private static final int HODE_PROGRESS = 101;
	private HttpClient mHttpClient; 
	private HttpParams mHttpParams;
	private onCallBack mOnCallBack;
	private PersonalInfoPojo mPersonalInfoPojo;
	private ProgressDialog mProgressDialog;
	private UIHandler mUIHandler = new UIHandler();
	private Context mContext;
	public static String ICON_PATH = "";
	
	public static LoginUtils getInstance(Context c)
	{
		Log.d("JIWAI","Context = " + c);
		if(gRegisterAndLogin == null)
		{
			Log.d("JIWAI","gRegisterAndLogin == null");
			gRegisterAndLogin = new LoginUtils(c);
			gRegisterAndLogin.getHttpClient();
		}
		return gRegisterAndLogin;
	}
	
	public static void free()
	{
		gRegisterAndLogin = null;
	}
	
	private LoginUtils(Context c) {
		this.mContext = c;
		ICON_PATH = mContext.getFilesDir().getAbsolutePath() + File.separator + "head.png";
	}

	
	public void setCallBack(onCallBack oc)
	{
		mOnCallBack = oc;
	}
	
	public void loginFromSever(boolean isRegister,String userName, String password) {
		 mUIHandler.sendEmptyMessage(SHOW_PROGRESS);
		 List<NameValuePair> params = new ArrayList<NameValuePair>();  
	        params.add(new BasicNameValuePair("loginName", userName));  
	        params.add(new BasicNameValuePair("password", password));  
		 /* 建立HTTPPost对象 */  
	        HttpPost httpRequest = new HttpPost("http://121.40.93.89:13080/users/login");
	        String strResult = "doPostError"; 
	        try {  
	            /* 添加请求参数到请求对象 */  
	            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));  
	            /* 发送请求并等待响应 */  
	            HttpResponse httpResponse = mHttpClient.execute(httpRequest); 
	            /* 若状态码为200 ok */  
	            if (httpResponse.getStatusLine().getStatusCode() == 200) {  
	                /* 读返回数据 */  
	                strResult = EntityUtils.toString(httpResponse.getEntity()); 
	                JSONObject jsonObject = new JSONObject(strResult);
	                String status = jsonObject.getString("code");
	                String data = jsonObject.getString("data");
	                Debug.d("status = " + status);
	                Debug.d("data = " + data);
	                mPersonalInfoPojo = JsonTools.GsonToObj(data, PersonalInfoPojo.class);
	                if(status.equals("200") || status.equals("201"))
	                {
	                	if(isRegister)
	                	{
	                		mUIHandler.sendEmptyMessage(REGISTER_HAS_EXSIT);
	                	}
	                	else
	                	{
	                		Debug.d("loginForHX " + System.currentTimeMillis());
		                	//成功以后登录环信
//		                	loginForHX(mPersonalInfoPojo.hxUser,mPersonalInfoPojo.hxPassword);
		                	PersonalUtil.savePersonInfo(mContext,  mPersonalInfoPojo);
							Debug.d("photoPath = " + mPersonalInfoPojo.photoPath);
							Util.saveHeadIcon(mContext,mPersonalInfoPojo.photoPath);
		                	loginCommunity(mContext, mPersonalInfoPojo.getLoginName(), mPersonalInfoPojo.getAccountId(),mPersonalInfoPojo);
		                	Debug.d("loginForHX " + System.currentTimeMillis());
	                	}
	                }
//	                else if(status.equals("201"))
//	                {
	                	//注册成功以后登录环形
//	                	loginForHX(mPersonalInfoPojo.hxUser,mPersonalInfoPojo.hxPassword);
//	                	loginCommunity(mContext, mPersonalInfoPojo.getNickName(), mPersonalInfoPojo.getAccountId());
//	                }
	                else if(status.equals("404"))
	                {
	                	Message msg = mUIHandler.obtainMessage();
	    				msg.what = LOGIN_ERROR_MSG;
	    				msg.obj = "404";
	    				mUIHandler.sendMessage(msg);
	                }
	            } 
	            else { 
	            	mUIHandler.sendEmptyMessage(LOGIN_ERROR_MSG_F);
	            }  
	        } catch (Exception e) {  
	        	Message msg = mUIHandler.obtainMessage();
				msg.what = LOGIN_ERROR_MSG;
				msg.obj = "404";
				mUIHandler.sendMessage(msg);
	            e.printStackTrace();  
	        }  
	    } 
	
//	private void registerForHX(String userName,String pwd)
//	{
//		try {
//			EMChatManager.getInstance().createAccountOnServer(userName, pwd);
//			MyApplication.getInstance().setUserName(userName);
//			mUIHandler.sendEmptyMessage(REGISTER_SUCCESS_MSG);
//		} catch (EaseMobException e) {
//			Message msg = mUIHandler.obtainMessage();
//			msg.what = REGISTER_ERROR_MSG;
//			msg.arg1 = e.getErrorCode();
//			mUIHandler.sendMessage(msg);
//			e.printStackTrace();
//		}
//	}
	
	public HttpClient getHttpClient() {  
        // 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）  
		if(mHttpClient != null)
		{
			return mHttpClient;
		}
        mHttpParams = new BasicHttpParams();  
        // 设置连接超时和 Socket 超时，以及 Socket 缓存大小  
        HttpConnectionParams.setConnectionTimeout(mHttpParams, 10 * 1000);  
        HttpConnectionParams.setSoTimeout(mHttpParams, 10 * 1000);  
        HttpConnectionParams.setSocketBufferSize(mHttpParams, 8192);  
        // 设置重定向，缺省为 true  
        HttpClientParams.setRedirecting(mHttpParams, true);  
        // 设置 user agent  
        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";  
        HttpProtocolParams.setUserAgent(mHttpParams, userAgent);  
        mHttpClient = new DefaultHttpClient(mHttpParams);  
        return mHttpClient;  
    }
	long timeP = 0;
	
//	public void loginForHX(final String userName,final String pwd) {
//		Debug.d("login userName = " + userName + " pwd = " + pwd);
//		timeP = System.currentTimeMillis();
//		Debug.d("loginForHX + " + timeP);
//		// 调用sdk登陆方法登陆聊天服务器
//		EMChatManager.getInstance().login(userName, pwd, new EMCallBack() {
//			@Override
//			public void onSuccess() {
//				// 登陆成功，保存用户名密码
//				MyApplication.getInstance().setUserName(userName);
//				MyApplication.getInstance().setPassword(pwd);
//				try {
//					long tx = System.currentTimeMillis() - timeP;
//					Debug.d("1111111111111 + " + tx);
//					// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
//					// ** manually load all local groups and
//				    EMGroupManager.getInstance().loadAllGroups();
//					EMChatManager.getInstance().loadAllConversations();
//					// 处理好友和群组
//					initializeContacts();
//					PersonalUtil.savePersonInfo(mContext,  mPersonalInfoPojo);
//					Debug.d("photoPath = " + mPersonalInfoPojo.photoPath);
//					Util.saveHeadIcon(mContext,mPersonalInfoPojo.photoPath);
//					mUIHandler.sendEmptyMessage(LOGIN_SUCCESS_MSG);
//				} catch (Exception e) {
//					e.printStackTrace();
//					mUIHandler.sendEmptyMessage(LOGIN_ERROR_MSG_F);
//				}
//			}
//
//			@Override
//			public void onProgress(int progress, String status) {
//				Debug.d("progress + " + progress);
//			}
//
//			@Override
//			public void onError(final int code, final String message) {
//				Message msg = mUIHandler.obtainMessage();
//				msg.what = LOGIN_ERROR_MSG;
//				msg.obj = message;
//				mUIHandler.sendMessage(msg);
//			}
//		});
//	}
	
//	private void initializeContacts() {
//		long tx = System.currentTimeMillis() - timeP;
//		Debug.d("initializeContacts = " + tx);
//		Map<String, User> userlist = new HashMap<String, User>();
//		// 添加user"申请与通知"
//		User newFriends = new User();
//		newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
//		String strChat = mContext.getResources().getString(
//				R.string.Application_and_notify);
//		newFriends.setNick(strChat);
//
//		userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
//		// 添加"群聊"
//		User groupUser = new User();
//		String strGroup = mContext.getResources().getString(R.string.group_chat);
//		groupUser.setUsername(Constant.GROUP_USERNAME);
//		groupUser.setNick(strGroup);
//		groupUser.setHeader("");
//		userlist.put(Constant.GROUP_USERNAME, groupUser);
//		
//		// 添加"Robot"
//		User robotUser = new User();
//		String strRobot = mContext.getResources().getString(R.string.robot_chat);
//		robotUser.setUsername(Constant.CHAT_ROBOT);
//		robotUser.setNick(strRobot);
//		robotUser.setHeader("");
//		userlist.put(Constant.CHAT_ROBOT, robotUser);
//		
//		// 存入内存
////		((DemoHXSDKHelper)HXSDKHelper.getInstance()).setContactList(userlist);
//		// 存入db
//		UserDao dao = new UserDao(mContext);
//		List<User> users = new ArrayList<User>(userlist.values());
//		dao.saveContactList(users);
//		
//		List<EMGroup> grouplistHasJoin;
//		tx = System.currentTimeMillis() - timeP;
//		Debug.d("begin login group + " + tx);
//		try {
//			grouplistHasJoin = EMGroupManager.getInstance().getGroupsFromServer();
//			EMGroup emGroup = null;
//			Debug.d("join local group list : " + grouplistHasJoin.size());
//			if (grouplistHasJoin.size() > 0) {
//				Debug.d("join local group list : " + grouplistHasJoin.get(0).getGroupName());
//				emGroup = grouplistHasJoin.get(0);
//				Debug.d("join group :" + emGroup.getGroupName() + " users = " + emGroup.getMaxUsers());
//				Util.gGroupId = emGroup.getId();
//				Util.saveJoGroupId(mContext, Util.gGroupId);
//				EMGroupManager.getInstance().joinGroup(Util.gGroupId);	
//			} else {
//				List<EMGroupInfo> groupsList = EMGroupManager.getInstance().getAllPublicGroupsFromServer();
//				Debug.d("async group from server group list : " + groupsList.size());
//				for (int i = 0; i < groupsList.size(); i++) {
//					Debug.d("join group list:" + groupsList.get(i).getGroupName());
//				}
//				for (int i = 0; i < groupsList.size(); i++) {
//					
//					EMGroupInfo emgInfo = groupsList.get(i);
//					EMGroup emg = EMGroupManager.getInstance().getGroupFromServer(emgInfo.getGroupId());
//					Debug.d("join group list:" + emg.getGroupName() + " users:" + emg.getMaxUsers());
//					int maxUser = emg.getMaxUsers();
//					int hasUsers = emg.getMembers().size();
//					if (hasUsers < maxUser - 1) {
//						Util.gGroupId = emg.getId();
//						Util.saveJoGroupId(mContext, Util.gGroupId);
//						try {
//							EMGroupManager.getInstance().joinGroup(Util.gGroupId);
//							break;
//						} catch (EaseMobException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}	
//						break;
//					}
//				}
//				
////				EMGroupManager.getInstance().asyncGetGroupsFromServer(new EMValueCallBack<List<EMGroup>>() {
////				    @Override
////					public void onSuccess(List<EMGroup> value) {
////				    	for (int i = 0; i < value.size(); i++) {
////							Debug.d("join group list:" + value.get(i).getGroupName() + " users = " + value.get(i).getMembers().size());
////						}
////						for (int i = 0; i < value.size(); i++) {
////							EMGroup emg = value.get(i);
////							int maxUser = emg.getMaxUsers();
////							int hasUsers = emg.getMembers().size();
////							if (hasUsers < maxUser - 1) {
////								Util.gGroupId = emg.getId();
////								Util.saveJoGroupId(mContext, Util.gGroupId);
////								try {
////									EMGroupManager.getInstance().joinGroup(Util.gGroupId);
////								} catch (EaseMobException e) {
////									// TODO Auto-generated catch block
////									e.printStackTrace();
////								}	
////								break;
////							}
////						}
////					}
////				 
////					@Override
////					public void onError(int error, String errorMsg) {
////				 
////					}
////				});
//			}
//		} catch (EaseMobException e1) {
//			e1.printStackTrace();
//			Debug.d("join group failed");
//		}
//		tx = System.currentTimeMillis() - timeP;
//		Debug.d("finish login group + " + tx);
//	}
	
	/**
	 * 只更新昵称和头像
	 * @return
	 */
	public boolean updatePersonInfo(String nickName) {
        HttpPost httpRequest = new HttpPost("http://121.40.93.89:13080/users/update");
        String strResult = "doPostError";
        try {
        	PersonalInfoPojo pi = PersonalUtil.getPersonInfo(mContext);
            MultipartEntity mpEntity = new MultipartEntity(); //文件传输
            Debug.d("nickName = " + nickName + " pwd = " + pi.getPassword() + " accountId = " + pi.getAccountId());
            mpEntity.addPart("password", new StringBody(pi.getPassword()));
            mpEntity.addPart("nickName", new StringBody(nickName,Charset.forName("UTF-8")));
            mpEntity.addPart("accountId", new StringBody(pi.getAccountId()));
            mpEntity.addPart("sex", new StringBody(""));
            mpEntity.addPart("grade", new StringBody("",Charset.forName("UTF-8")));
//            mpEntity.addPart("photoPath", new StringBody("",Charset.forName("UTF-8")));
            if(Util.isFileExsit(ICON_PATH))
            {
            	mpEntity.addPart("files", new FileBody(new File(ICON_PATH)));
            }
            /* 添加请求参数到请求对象 */
//            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            httpRequest.setEntity(mpEntity);
            /* 发送请求并等待响应 */
            HttpResponse httpResponse = mHttpClient.execute(httpRequest);
            /* 若状态码为200 ok */
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                /* 读返回数据 */
                strResult = EntityUtils.toString(httpResponse.getEntity());
                Debug.d("result = " + strResult);
                JSONObject jsonObject = new JSONObject(strResult);
                String status = jsonObject.getString("code");
                String message = jsonObject.getString("data");
                if(status.equals("200"))
                {
                    pi.nickName = nickName;
                    PersonalUtil.savePersonInfo(mContext, pi);
                    PersonalUtil.isLogined(mContext);
                    return true;
                }
                else if(status.equals("404"))
                {
                    return false;
                }
            }
            else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
	
	public interface onCallBack
	{
		void onLoginSuccess();
		void onLoginError(String errorMsg);
		void onRegisterSuccess();
		void onRegisterError();
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
				sendEmptyMessage(HODE_PROGRESS);
				if(mOnCallBack != null)
				{
					mOnCallBack.onLoginSuccess();
				}
				break;
			}
			case LOGIN_ERROR_MSG:
			{
				sendEmptyMessage(HODE_PROGRESS);
//				DemoHXSDKHelper.getInstance().logout(true,null);
				Toast.makeText(mContext, mContext.getString(R.string.Login_failed), 1).show();
				if(mOnCallBack != null)
				{
					mOnCallBack.onLoginError(mContext.getString(R.string.Login_failed));
				}
				break;
			}
			case LOGIN_ERROR_MSG_F:
			{
				sendEmptyMessage(HODE_PROGRESS);
//				DemoHXSDKHelper.getInstance().logout(true,null);
				Toast.makeText(mContext, mContext.getString(R.string.login_failure_failed) + (String)msg.obj, 1).show();
				if(mOnCallBack != null)
				{
					mOnCallBack.onLoginError(mContext.getString(R.string.login_failure_failed));
				}
				break;
			}
			case REGISTER_SUCCESS_MSG:
			{
				sendEmptyMessage(HODE_PROGRESS);
				if(mOnCallBack != null)
				{
					mOnCallBack.onRegisterSuccess();
				}
				break;
			}
			case REGISTER_ERROR_MSG:
			{
				sendEmptyMessage(HODE_PROGRESS);
				if(mOnCallBack != null)
				{
					mOnCallBack.onRegisterError();
				}
				break;
			}
			case REGISTER_HAS_EXSIT:
			{
				sendEmptyMessage(HODE_PROGRESS);
				Toast.makeText(mContext, mContext.getString(R.string.register_name_is_exist), 1).show();
				if(mOnCallBack != null)
				{
					mOnCallBack.onRegisterError();
				}
				break;
			}
			case SHOW_PROGRESS:
			{
				if(mProgressDialog == null)
				{
					mProgressDialog = new ProgressDialog(mContext);
					mProgressDialog.setCanceledOnTouchOutside(false);
					mProgressDialog.setOnCancelListener(new OnCancelListener() {
			
						@Override
						public void onCancel(DialogInterface dialog) {
						}
					});
					mProgressDialog.setMessage(mContext.getString(R.string.Is_landing));
				}
				Debug.d("mProgressDialog = " + mProgressDialog);
				mProgressDialog.show();
				break;
			}
			case HODE_PROGRESS:
			{
				if(mProgressDialog != null && mProgressDialog.isShowing())
				{
					mProgressDialog.dismiss();
				}
				break;
			}
			}
		}
	}
	
	/**登录微社区*/
	private void loginCommunity(Context context,String name,String id,final PersonalInfoPojo personalInfo)
    {
    	CommunitySDK sdk = CommunityFactory.getCommSDK(context);
        CommUser user = new CommUser();
        user.name = name;
        user.id = id;
        sdk.loginToUmengServer(context, user, new LoginListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(int stCode, CommUser commUser) {
                if (ErrorCode.NO_ERROR==stCode) {
                	Debug.d("登录社区成功");
                	mUIHandler.sendEmptyMessage(LOGIN_SUCCESS_MSG);
                	//登录成功以后要设置用户名和头像
                	CommConfig.getConfig().loginedUser.name = personalInfo.getNickName();
                    CommConfig.getConfig().loginedUser.iconUrl = personalInfo.getPhotoPath();
                    Debug.d("CommConfig.getConfig().loginedUser.iconUrl = " + CommConfig.getConfig().loginedUser.iconUrl);
                    CommunitySDK sdk = CommunityFactory.getCommSDK(mContext);
                    sdk.updateUserProfile(CommConfig.getConfig().loginedUser, new CommListener() {
                        @Override
                        public void onStart() {
                        	Debug.d("onStart updateUserProfile");
                        }

                        @Override
                        public void onComplete(Response response) {
                        	Debug.d("respose = " + response);
                            if (response.errCode == ErrorCode.NO_ERROR) {
                            	Debug.d("updateUserProfile");
        	                    updateUserPortrait(BitmapFactory.decodeFile(mContext.getFilesDir().getAbsolutePath() + File.separator + "head.png"));
                                //	                                mUIHandler.sendEmptyMessage(UPDATE_INFO_SUCCESS);
                            } 
                            else if(response.errCode == ErrorCode.ERR_CODE_USER_NAME_DUPLICATE)
                            {
//                            	mUIHandler.sendEmptyMessage(LOGIN_ERROR_MSG_F);
                            }
                        }
                    });
                }
                else
                {
//                	mUIHandler.sendEmptyMessage(LOGIN_SUCCESS_MSG);
                	mUIHandler.sendEmptyMessage(LOGIN_ERROR_MSG_F);
                }

           }
        });
    }
	
	/**
     * 更新用户头像
     */
    private void updateUserPortrait(final Bitmap bmp) {
        CommunityFactory.getCommSDK(mContext).updateUserProtrait(bmp,
                new SimpleFetchListener<PortraitUploadResponse>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(PortraitUploadResponse response) {
                        if (response != null && response.errCode == ErrorCode.NO_ERROR) {
                            Debug.d("头像更新成功 : " + response.mJsonObject.toString());
                            CommUser user = CommConfig.getConfig().loginedUser;
                            user.iconUrl = response.mIconUrl;

                            Debug.d("#### 登录用户的头像 : "
                                    + CommConfig.getConfig().loginedUser.iconUrl);
                            // 同步到数据库中
                            DatabaseAPI.getInstance().getUserDBAPI().saveUserInfoToDB(user);
                            CommonUtils.saveLoginUserInfo(mContext, user);
//                            BroadcastUtils.sendUserUpdateBroadcast(mContext, user);
//                            mUIHandler.sendEmptyMessage(LOGIN_SUCCESS_MSG);
                        } else {
                            ToastMsg.showShortMsgByResName("umeng_comm_update_icon_failed");
                        }
                    }

                });
    }
	
	/**后台默默登录微社区*/
	public void loginCommunityInBack(final Context context)
    {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				PersonalInfoPojo pu = PersonalUtil.getPersonInfo(context);
				String name = pu.getNickName();
				String id = pu.getAccountId();
		    	final CommunitySDK sdk = CommunityFactory.getCommSDK(context);
		        CommUser user = new CommUser();
		        Debug.d("开始登录微社区 name = " + name);
		        user.name = name;
		        user.id = id;
		        sdk.loginToUmengServer(context, user, new LoginListener() {
		            @Override
		            public void onStart() {
		            }

		            @Override
		            public void onComplete(int stCode, final CommUser commUser) {
		                if (ErrorCode.NO_ERROR==stCode) {
		                	Debug.d("登录社区成功");
		                	Debug.d("commUser name = " + commUser.name);
		                	CommConfig.getConfig().loginedUser = commUser;
//		                	commUser.iconUrl = PersonalUtil.mSnsAccount.getAccountIconUrl();
//		                	commUser.name = "11111111111111";//PersonalUtil.mSnsAccount.getUserName();
		                	Debug.d("commUser name111 = " + commUser.name);
		                	Util.setLogin(mContext, true);
//		                    CommConfig.getConfig().loginedUser.name = PersonalUtil.mSnsAccount.getUserName();
//		                    CommConfig.getConfig().loginedUser.iconUrl = PersonalUtil.mSnsAccount.getAccountIconUrl();
		                    
//		                    sdk.updateUserProfile(commUser, new CommListener() {
//		                    
//		                        @Override
//		                        public void onStart() {
//		                        	Debug.d("onStart updateUserProfile");
//		                        }
//
//		                        @Override
//		                        public void onComplete(Response response) {
//		                        	Debug.d("respose = " + response);
//		                            if (response.errCode == ErrorCode.NO_ERROR) {
//		                            	Debug.d("updateUserProfile");
//		                                CommConfig.getConfig().loginedUser = commUser;
//		                                DatabaseAPI.getInstance().getUserDBAPI().saveUserInfoToDB(CommConfig.getConfig().loginedUser);
//		                                CommonUtils.saveLoginUserInfo(mContext, CommConfig.getConfig().loginedUser);
//		                                BroadcastUtils.sendUserUpdateBroadcast(mContext, CommConfig.getConfig().loginedUser);
//		                            } else {
//		                            }
//		                        }
//		                    });
		                }
		                else
		                {
		                	mUIHandler.sendEmptyMessage(LOGIN_ERROR_MSG_F);
		                }

		           }
		        });				
			}
		});
		thread.start();
		
    }
	
	/**登录微社区*/
	public void loginCommunityInBack(Context context,String name,String id)
    {
    	CommunitySDK sdk = CommunityFactory.getCommSDK(context);
        CommUser user = new CommUser();
        user.name = name;
        user.id = id;
        sdk.loginToUmengServer(context, user, new LoginListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(int stCode, CommUser commUser) {
                if (ErrorCode.NO_ERROR==stCode) {
                	Debug.d("登录社区成功");
                	commUser.iconUrl = PersonalUtil.mSnsAccount.getAccountIconUrl();
                	Util.setLogin(mContext, true);
                }
                else
                {
                	mUIHandler.sendEmptyMessage(LOGIN_ERROR_MSG_F);
                }

           }
        });
    }
	
	/**
	 * 后台自动登录
	 * @param userName
	 * @param password
	 */
	public void loginInBack(Context context)
	{
		Debug.d("loginInBack11111111111");
    	PersonalInfoPojo pu = PersonalUtil.getPersonInfo(context);
    	String userName = pu.getLoginName();
    	String password = pu.getPassword();
		 	List<NameValuePair> params = new ArrayList<NameValuePair>();  
	        params.add(new BasicNameValuePair("loginName", userName));  
	        params.add(new BasicNameValuePair("password", password));  
		 /* 建立HTTPPost对象 */  
	        HttpPost httpRequest = new HttpPost("http://121.40.93.89:13080/users/login");
	        String strResult = "doPostError"; 
	        try {  
	            /* 添加请求参数到请求对象 */  
	            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));  
	            /* 发送请求并等待响应 */  
	            HttpResponse httpResponse = mHttpClient.execute(httpRequest); 
	            /* 若状态码为200 ok */  
	            if (httpResponse.getStatusLine().getStatusCode() == 200) {  
	                /* 读返回数据 */  
	                strResult = EntityUtils.toString(httpResponse.getEntity()); 
	                JSONObject jsonObject = new JSONObject(strResult);
	                String status = jsonObject.getString("code");
	                String data = jsonObject.getString("data");
	                Debug.d("status = " + status);
	                Debug.d("data = " + data);
	                mPersonalInfoPojo = JsonTools.GsonToObj(data, PersonalInfoPojo.class);
	                if(status.equals("200") || status.equals("201"))
	                {
		                	//成功以后登录环信
//		                	loginForHX(mPersonalInfoPojo.hxUser,mPersonalInfoPojo.hxPassword);
		                	PersonalUtil.savePersonInfo(mContext,  mPersonalInfoPojo);
							Debug.d("photoPath = " + mPersonalInfoPojo.photoPath);
							Util.saveHeadIcon(mContext,mPersonalInfoPojo.photoPath);
							loginCommunityInBack(mContext, mPersonalInfoPojo.getNickName(), mPersonalInfoPojo.getAccountId());
	                }
	                else if(status.equals("404"))
	                {
	                	Message msg = mUIHandler.obtainMessage();
	    				msg.what = LOGIN_ERROR_MSG;
	    				msg.obj = "404";
	    				mUIHandler.sendMessage(msg);
	                }
	            } 
	            else { 
	            	mUIHandler.sendEmptyMessage(LOGIN_ERROR_MSG_F);
	            }  
	        } catch (Exception e) {  
	        	Message msg = mUIHandler.obtainMessage();
				msg.what = LOGIN_ERROR_MSG;
				msg.obj = "404";
				mUIHandler.sendMessage(msg);
	            e.printStackTrace();  
	        }  
	}
}
