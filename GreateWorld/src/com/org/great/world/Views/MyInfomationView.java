package com.org.great.world.Views;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.JsonTools;
import com.org.great.world.Utils.PersonalUtil;
import com.org.great.world.Utils.LoginUtils;
import com.org.great.world.activities.ActivityRegister;
import com.org.great.world.activities.GalleryUrlActivity;
import com.org.great.world.adapters.PopGridListAdapter;
import com.org.great.world.data.PersonalInfoPojo;
import com.org.great.wrold.R;
import com.soundcloud.android.crop.Crop;
import com.soundcloud.android.crop.CropImageActivity;
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
import com.umeng.comm.core.utils.ResFinder;
import com.umeng.comm.core.utils.ToastMsg;
import com.umeng.fb.FeedbackAgent;

public class MyInfomationView implements OnClickListener,MediaScannerConnectionClient{
	
	// 两个状态
    private RelativeLayout mNormalLayout;
    private RelativeLayout mChangeLayout;

    private View mPhotoLayout;
    private ImageView mPhotoView;
    // 正常
    private ImageButton mSetBt;
    private TextView mNickTextView;
    private TextView mGrade;
    private ImageView mSexPic;
    private TextView mPersonAccount;

    // 编辑
    private EditText mChangeNickName;
    private ImageView mChangeGradePull;

    // grade pop
    private View mPopview;
    private PopupWindow mPopupWindow1;
    private ListView mPopList;

    //text
    private TextView mFeedback;
    private TextView mQuit;
    private TextView mMyPic;


    private String mSex;

    private boolean isEditMode = false;
    private PersonalInfoPojo mPersonInfo;

    private HttpClient httpClient;
    private HttpParams httpParams;

    private static String ICON_PATH;
    private static final int UPDATE_INFO_SUCCESS = 0;
    private static final int UPDATE_INFO_FAILED = 1;
    private static final int UPDATE_INFO_FAILED_USER_NAME_DUMP = 2;
    private MediaScannerConnection conn;
    private String SCAN_PATH ;
    private static final String FILE_TYPE="image/*";
    
    
    
    
    
    ////////////////////////////
	private final int SETTING_LOGIN_OUT_SUCCESS = 3;
	private View mMainView;
	private Context mContext;
	private UIHandler mUIHandler = new UIHandler();
	private ProgressDialog mProgressDialog;
	private OnSettingCallback mOnSettingCallback;
	/**
	 * 退出按钮
	 */
	private Button logoutBtn;
	/**反馈按钮*/
	private Button feedBackBtn;
	private FeedbackAgent mFeedBackAgent;
	

//	private RelativeLayout rl_switch_chatroom_leave;
//	private ImageView iv_switch_room_owner_leave_allow;
//	private ImageView iv_switch_room_owner_leave_disallow;
	
 
	public MyInfomationView(Context context) {
		mContext = context;
		mMainView = View.inflate(context, R.layout.fragment_me_layout, null).findViewById(R.id.main_layout);
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
	
//	public void update()
//	{
//		if(!TextUtils.isEmpty(EMChatManager.getInstance().getCurrentUser())){
//			logoutBtn.setText(mContext.getString(R.string.button_logout) + "(" + EMChatManager.getInstance().getCurrentUser() + ")");
//		}
//	}
	
	private void init(View view)
	{
		mProgressDialog = new ProgressDialog(mContext);
		        mNormalLayout = (RelativeLayout) mMainView.findViewById(R.id.person_baseinfo);
		        mChangeLayout = (RelativeLayout) mMainView.findViewById(R.id.person_changeinfo);
		
		        mSetBt = (ImageButton) mMainView.findViewById(R.id.person_change);
		        mPhotoView = (ImageView) mMainView.findViewById(R.id.person_photo);
		        mPhotoLayout = mMainView.findViewById(R.id.person_photo_layout);
		
		        mNickTextView = (TextView) mMainView.findViewById(R.id.person_name);
		        mGrade = (TextView) mMainView.findViewById(R.id.person_grade);
		        mSexPic = (ImageView) mMainView.findViewById(R.id.person_sex);
		        mPersonAccount = (TextView) mMainView.findViewById(R.id.person_account);
		
		        mChangeNickName = (EditText) mMainView.findViewById(R.id.person_changename);
		
		        mFeedback = (TextView)mMainView.findViewById(R.id.person_feedback);
		        mFeedback.setOnClickListener(this);
		        mQuit = (TextView)mMainView.findViewById(R.id.person_exit);
		        mQuit.setOnClickListener(this);
		        mMyPic = (TextView)mMainView.findViewById(R.id.person_picture);
		        mMyPic.setOnClickListener(this);


		        mSetBt.setOnClickListener(this);
		        mPhotoLayout.setOnClickListener(this);

		        mFeedBackAgent = new FeedbackAgent(mContext);
		        mFeedBackAgent.sync();

		        getUserInfo();
		        initMyInfo();
	}
	 
	
	void logout() {
//		mProgressDialog = new ProgressDialog(mContext);
//		String st = mContext.getResources().getString(R.string.Are_logged_out);
//		mProgressDialog.setMessage(st);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				CommunitySDK sdk = CommunityFactory.getCommSDK(mContext);
				sdk.logout(mContext, new LoginListener() {
					
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void onComplete(int arg0, CommUser arg1) {
						PersonalUtil.delPersonInfo(mContext);
						File file = new File(mContext.getFilesDir().getAbsolutePath() + File.separator + "head.png");
						file.delete();
						if (arg0 == ErrorCode.NO_ERROR)
						{
							mUIHandler.sendEmptyMessage(SETTING_LOGIN_OUT_SUCCESS);
						}
						else
						{
							mUIHandler.sendEmptyMessage(SETTING_LOGIN_OUT_SUCCESS);
						}
					}
				});
			}
		});
		thread.start();
	}
	
	
	public void setOnSettingListener(OnSettingCallback oc)
	{
		mOnSettingCallback = oc;
	}
	
	public interface OnSettingCallback 
	{
		public void onLoginOut();
		public void onSettingHead();
	}
	
	 private void getUserInfo() {
	        mPersonInfo = PersonalUtil.getPersonInfo(mContext);
	        if( null == mPersonInfo )
	        {
	            Toast.makeText(mContext, mContext.getString(R.string.find_black_person), Toast.LENGTH_SHORT).show();
	        }
	    }

	    private void initMyInfo() {
	        setImageBitmap();
	        if (!isEditMode) {
	            mNormalLayout.setVisibility(View.VISIBLE);
	            mChangeLayout.setVisibility(View.GONE);
	            mSetBt.setBackgroundResource(R.drawable.person_change);
	            String nickName = mPersonInfo.getNickName();
	            mNickTextView.setText((nickName == null || nickName.equals("null") ? "起个名字真难" : nickName)); // 没有别名则使用登录名
	            mGrade.setText((mPersonInfo.getGrade() == null || mPersonInfo.getGrade().equals("null")) ? "一年级" : mPersonInfo.getGrade());
	            mPhotoLayout.animate().setInterpolator(new OvershootInterpolator()).scaleX(0.9f).scaleY(0.9f).start();
	            mSexPic.setImageResource(mPersonInfo.getSex().equals("1") ? R.drawable.person_girl_normal : R.drawable.person_boy_normal);
	        } else {
	            mNormalLayout.setVisibility(View.GONE);
	            mChangeLayout.setVisibility(View.VISIBLE);
	            mSetBt.setBackgroundResource(R.drawable.person_change_finish);
	            mPhotoLayout.animate().setInterpolator(new OvershootInterpolator())
	                    .scaleX(0.9f).scaleY(0.9f).start();
	            mChangeNickName.setText(mNickTextView.getText());
	            mSex = mPersonInfo.getSex();
	            mChangeNickName.selectAll();
	        }
//	        PersonalUtil.isLogined(mContext);
	    }

	    private void setImageBitmap() {
	    	ICON_PATH = mContext.getFilesDir().getAbsolutePath() + File.separator + "head.png";
	        Log.d("setImageBitmap", "ICON_PATH = " + ICON_PATH);
	        File imageFile = new File(ICON_PATH);
	        String imageUri = "";
	        if(!imageFile.exists())
	        {
	            imageUri = "drawable://" + R.drawable.default_avatar;
	        }
	        else
	        {
	            imageUri = Uri.fromFile(new File(ICON_PATH)).toString();
	        }
	        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
	        DisplayImageOptions options = builder.displayer(new FadeInBitmapDisplayer(200, true, true, true)).build();
	        ImageLoader loader = ImageLoader.getInstance();
	        loader.init(ImageLoaderConfiguration.createDefault(mContext));
	        loader.displayImage(imageUri, mPhotoView, options, new SimpleImageLoadingListener());
	    }

	    @Override
	    public void onClick(View view)
	    {
	        switch(view.getId())
	        {

	            case R.id.person_change:
	            {
	                isEditMode = !isEditMode;
	                initMyInfo();
	                if( !isEditMode ){
	                	if(mProgressDialog != null && !mProgressDialog.isShowing())
	                	{
	                		mProgressDialog.show();
	                	}
	                    new Thread() {
	                        public void run() {
	                            boolean r = savePersonInfo();
	                            if(r)
	                            {
	                                
	                            }
	                            else
	                            {
	                                mUIHandler.sendEmptyMessage(UPDATE_INFO_FAILED);
	                            }
	                        };
	                    }.start();
	                }
	                break;
	            }
	//
//	            case R.id.person_set_grade:
//	            case R.id.person_gradearrorw:
//	            {
//	                popPhotoSelection();
//	                break;
//	            }
	//
//	            case R.id.person_photo:
//	            {
//	                //goto set icon
//	                setCrop();
//	                break;
//	            }
	//
	            case R.id.person_photo_layout:
	            {
	            	clickPhoto();
	                break;
	            }

	            case R.id.person_feedback:
	            {
	                mFeedBackAgent.startFeedbackActivity();
	                break;
	            }

	            case R.id.person_exit:
	            {
//	                PersonalUtil.delPersonInfo(mContext);
	            	logout();
	                break;
	            }

	            case R.id.person_picture:
	            {
	                clickMyPic( );
	                break;
	            }
	            default:
	                break;
	        }
	    }


	    private void clickPhoto() {

	        if(!isEditMode ){
	            Intent intent = new Intent(mContext, GalleryUrlActivity.class);
	            String imageUri = mPersonInfo.getPhotoPath();
	            if( null == imageUri || true == imageUri.isEmpty() ) {
	                imageUri = "drawable://" + R.drawable.default_avatar;
	            }

	            intent.putExtra("urls", imageUri);
	            mContext.startActivity(intent);
	        }
	        else{
	            setCrop();
	        }
	    }

	    private void clickMyPic( ){

	        String[] allFiles = null;
	        String sdcard = Environment.getExternalStorageDirectory().toString();
	        File file = new File(sdcard + "/带你看世界/");
	        allFiles = file.list();

	        if( null == allFiles ){

	            Toast.makeText(mContext, "還沒收藏圖片呢啊", Toast.LENGTH_SHORT).show();
	        }
	        for(int i=0;i<allFiles.length;i++)
	        {
	            Log.d("all file path"+i, allFiles[i]+allFiles.length);
	        }

	        SCAN_PATH= sdcard + "/带你看世界/";

	        Log.d("Connected","success"+conn);
	        if(conn!=null)
	        {
	            conn.disconnect();
	        }
	        conn = new MediaScannerConnection(mContext,this);
	        conn.connect();
	    }

	    public HttpClient getHttpClient() {
	        // 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
	        if(httpClient != null)
	        {
	            return httpClient;
	        }
	        this.httpParams = new BasicHttpParams();
	        // 设置连接超时和 Socket 超时，以及 Socket 缓存大小
	        HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
	        HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
	        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
	        // 设置重定向，缺省为 true
	        HttpClientParams.setRedirecting(httpParams, true);
	        // 设置 user agent
	        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
	        HttpProtocolParams.setUserAgent(httpParams, userAgent);
	        httpClient = new DefaultHttpClient(httpParams);
	        return httpClient;
	    }

	    private boolean savePersonInfo() {
	        getHttpClient();
		 /* 建立HTTPPost对象 */
	        HttpPost httpRequest = new HttpPost("http://121.40.93.89:13080/users/update");
	        String strResult = "doPostError";
	        try {

	            MultipartEntity mpEntity = new MultipartEntity(); //文件传输
//	            ContentBody cbFile = new FileBody(file);
//	            mPersonInfo.setNickName(mChangeNickName.getText().toString());
//	        	mpEntity.addPart("loginName", new StringBody(mPersonInfo.getLoginName()));
	            mpEntity.addPart("password", new StringBody(mPersonInfo.getPassword()));
	            mpEntity.addPart("nickName", new StringBody(mChangeNickName.getText().toString(),Charset.forName("UTF-8")));
	            mpEntity.addPart("accountId", new StringBody(mPersonInfo.getAccountId()));
	            mpEntity.addPart("sex", new StringBody(mSex));
	            mpEntity.addPart("grade", new StringBody(mGrade.getText().toString(),Charset.forName("UTF-8")));
	            mpEntity.addPart("photoPath", new StringBody(mPersonInfo.getPhotoPath(),Charset.forName("UTF-8")));
	            File file = new File(mContext.getFilesDir().getAbsolutePath() + File.separator + "head_temp.png");
	            if(file.exists())
	            {
	            	mpEntity.addPart("files", new FileBody(new File(mContext.getFilesDir().getAbsolutePath() + File.separator + "head_temp.png")));
	            }
	            else
	            {
	            	mpEntity.addPart("files", new FileBody(new File(mContext.getFilesDir().getAbsolutePath() + File.separator + "head.png")));
	            }
	            /* 添加请求参数到请求对象 */
//	            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	            httpRequest.setEntity(mpEntity);
	            /* 发送请求并等待响应 */
	            HttpResponse httpResponse = httpClient.execute(httpRequest);
	            /* 若状态码为200 ok */
	            if (httpResponse.getStatusLine().getStatusCode() == 200) {
	                /* 读返回数据 */
	                strResult = EntityUtils.toString(httpResponse.getEntity());
	                JSONObject jsonObject = new JSONObject(strResult);
	                String status = jsonObject.getString("code");
	                String message = jsonObject.getString("data");
	                if(status.equals("200"))
	                {
	                    System.out.println("更新成功......... message = " + message);
	                    CommConfig.getConfig().loginedUser.name = mChangeNickName.getText().toString();
	                    CommConfig.getConfig().loginedUser.iconUrl = mPersonInfo.getPhotoPath();
	                    Debug.d("CommConfig.getConfig().loginedUser.iconUrl = " + CommConfig.getConfig().loginedUser.iconUrl);
	                    if(mChangeNickName.getText().toString().equals(mPersonInfo.getNickName()))
	                    {
	                    	//没有更改昵称
	                    	DatabaseAPI.getInstance().getUserDBAPI().saveUserInfoToDB(CommConfig.getConfig().loginedUser);
                            CommonUtils.saveLoginUserInfo(mContext, CommConfig.getConfig().loginedUser);
//                            BroadcastUtils.sendUserUpdateBroadcast(mContext, CommConfig.getConfig().loginedUser);
                            if(file.exists())   //只是换了头像
                            {
                            	changeTempFileToFile();
        	                    updateUserPortrait(BitmapFactory.decodeFile(mContext.getFilesDir().getAbsolutePath() + File.separator + "head.png"));
                            }
                            else
                            {
                            	mUIHandler.sendEmptyMessage(UPDATE_INFO_SUCCESS);
                            }
                            return true;
	                    }
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
	                            	mPersonInfo.setNickName(mChangeNickName.getText().toString());
	        	                    PersonalUtil.savePersonInfo(mContext, mPersonInfo);
	                            	Debug.d("updateUserProfile");
//	                                DatabaseAPI.getInstance().getUserDBAPI().saveUserInfoToDB(CommConfig.getConfig().loginedUser);
//	                                CommonUtils.saveLoginUserInfo(mContext, CommConfig.getConfig().loginedUser);
//	                                BroadcastUtils.sendUserUpdateBroadcast(mContext, CommConfig.getConfig().loginedUser);
	                                changeTempFileToFile();
	        	                    updateUserPortrait(BitmapFactory.decodeFile(mContext.getFilesDir().getAbsolutePath() + File.separator + "head.png"));
	                                //	                                mUIHandler.sendEmptyMessage(UPDATE_INFO_SUCCESS);
	                            } 
	                            else if(response.errCode == ErrorCode.ERR_CODE_USER_NAME_DUPLICATE)
	                            {
	                            	mUIHandler.sendEmptyMessage(UPDATE_INFO_FAILED_USER_NAME_DUMP);
	                            }
	                        }
	                    });
	                    
	                    return true;
	                }
	                else if(status.equals("404"))
	                {
	                    return false;
	                }
	            }
	            else {
	                return false;
	            }
	        } catch (Exception e) {
	           return false;
	        }
	        return false;
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
//	                            BroadcastUtils.sendUserUpdateBroadcast(mContext, user);
	                            mUIHandler.sendEmptyMessage(UPDATE_INFO_SUCCESS);
	                        } else {
	                            ToastMsg.showShortMsgByResName("umeng_comm_update_icon_failed");
	                        }
	                    }

	                });
	    }
	    
	    /**
	     * 上传成功以后，将临时头像替换成正式头像
	     */
	    private void changeTempFileToFile()
	    {
	    	File fileTemp = new File(mContext.getFilesDir().getAbsolutePath() + File.separator + "head_temp.png");
	    	if(!fileTemp.exists())
	    	{
	    		return;
	    	}
	    	File file = new File(mContext.getFilesDir().getAbsolutePath() + File.separator + "head.png");
	    	file.delete();
	    	fileTemp.renameTo(new File(mContext.getFilesDir().getAbsolutePath() + File.separator + "head.png"));
	    }

	    public void setCrop()
	    {
	    	if(mOnSettingCallback != null)
	    	{
	    		mOnSettingCallback.onSettingHead();
	    	}
//	        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
//	        intent.addCategory(Intent.CATEGORY_OPENABLE);
//	        intent.setType("image/*");
//	        intent.putExtra("return-data", true);
//	        mContext.startActivityForResult(intent, 111);
	    }

	    private void popPhotoSelection() {

	        closeIME();

	        if (null == mPopupWindow1) {

	            LayoutInflater layoutInflater = (LayoutInflater) mContext
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            mPopview = layoutInflater.inflate(R.layout.popwindow_grade, null);
//	            mPopupWindow1 = new PopupWindow(mPopview, (int)getResources().getDimension(R.dimen.personinfo_popwidth),
//	                    (int)getResources().getDimension(R.dimen.personinfo_popheight));

	            mPopList = (ListView) mPopview.findViewById(R.id.pop_grade_list);
	        }

//	        mPopList.setAdapter(new PopGridListAdapter(getActivity(), mPersonInfo.getGrade()));
//	        mPopList.setOnItemClickListener(this);
	        mPopupWindow1.setFocusable(true);
	        mPopupWindow1.setOutsideTouchable(true);
	        mPopupWindow1.setBackgroundDrawable(new BitmapDrawable());

//	        mPopupWindow1.showAsDropDown(mChangeGradePull, (int)getResources().getDimension(R.dimen.personinfo_pop_margin_x),
//	                (int)getResources().getDimension(R.dimen.personinfo_pop_margin_y_port));

	    }
	    
	    public void closeIME() {
	        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.hideSoftInputFromWindow(mChangeNickName.getWindowToken(), 0);
	    }


	    @Override
	    public void onMediaScannerConnected() {
	        Log.d("onMediaScannerConnected","success"+conn);
	        conn.scanFile(SCAN_PATH, FILE_TYPE);
	    }
	    @Override
	    public void onScanCompleted(String path, Uri uri) {

	        try {
	            Log.d("onScanCompleted",uri + "success"+conn);
	            System.out.println("URI " + uri);

	            if (uri != null)
	            {
	                Intent intent = new Intent(Intent.ACTION_VIEW);
	                intent.setData(uri);
//	                startActivity(intent);
	            }
	        }
	        finally {
	            conn.disconnect();
	            conn = null;
	        }
	    }

	    public class UIHandler extends Handler
	    {
	        @Override
	        public void handleMessage(Message msg)
	        {
	            super.handleMessage(msg);
	            switch(msg.what)
	            {
	                case UPDATE_INFO_SUCCESS:
	                {
	                	if(mProgressDialog != null && mProgressDialog.isShowing())
	                	{
	                		mProgressDialog.dismiss();
	                	}
	                	final LoginUtils ra = LoginUtils.getInstance(mContext);
	                	Thread thread = new Thread(new Runnable() {
							@Override
							public void run() {
								ra.loginInBack(mContext);
							}
						});
	                	thread.start();
	                    initMyInfo();
	                    break;
	                }
	                case UPDATE_INFO_FAILED:
	                {
	                	if(mProgressDialog != null && mProgressDialog.isShowing())
	                	{
	                		mProgressDialog.dismiss();
	                	}
	                    initMyInfo();
	                    break;
	                }
	                case SETTING_LOGIN_OUT_SUCCESS:
					{
						if(mProgressDialog != null && mProgressDialog.isShowing())
						{
							mProgressDialog.dismiss();
						}
						mOnSettingCallback.onLoginOut();
						break;
					}
	                case UPDATE_INFO_FAILED_USER_NAME_DUMP:
	                {
	                	if(mProgressDialog != null && mProgressDialog.isShowing())
	                	{
	                		mProgressDialog.dismiss();
	                	}
	                	initMyInfo();
	                	Toast.makeText(mContext, R.string.update_failed_user_name_exist, Toast.LENGTH_SHORT).show();
	                	break;
	                }
	            }
	        }
	    }

		public void uploadInfo() {
			ICON_PATH = mContext.getFilesDir().getAbsolutePath() + File.separator + "head_temp.png";
	        Log.d("setImageBitmap", "ICON_PATH = " + ICON_PATH);
	        File imageFile = new File(ICON_PATH);
	        String imageUri = "";
	        if(!imageFile.exists())
	        {
	            imageUri = "drawable://" + R.drawable.default_avatar;
	        }
	        else
	        {
	            imageUri = Uri.fromFile(new File(ICON_PATH)).toString();
	        }
	        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
	        DisplayImageOptions options = builder.displayer(new FadeInBitmapDisplayer(200, true, true, true)).build();
	        ImageLoader loader = ImageLoader.getInstance();
	        loader.init(ImageLoaderConfiguration.createDefault(mContext));
	        loader.displayImage(imageUri, mPhotoView, options, new SimpleImageLoadingListener());
		}
}
