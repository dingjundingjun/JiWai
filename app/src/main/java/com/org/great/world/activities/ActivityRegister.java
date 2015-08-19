package com.org.great.world.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.org.great.world.Utils.JsonTools;
import com.org.great.world.Utils.PersonalUtil;
import com.org.great.world.pojo.PersonalInfoPojo;
import com.org.great.wrold.R;


public class ActivityRegister extends Activity{
	
	private static final String TAG = " RegisterActivity -- > ";	
	
	public static final int NO_FINISH = 1;
	public static final int GO_FINISH = 2;
	
	
	public static String UESR_INFO_CFG = "user_info_cfg";
	public static String UESR_INFO_ID = "user_info_id";
	public static String UESR_INFO_PASSWORD = "user_info_password";
	
	private static final int LOGIN_REGISTER = 1;
	private static final int LOGIN_SUCCESS = 2;
	private static final int LOGIN_FAILURE = 3;
	public static final int LOGIN_SUCCESS_RESULT = 2;
	
	
	private EditText nameEdit; 
	private EditText passwordEdit; 
	private TextView forgetWord;
	private Button goIntoBt;
	
	private String mUserName;
	private String mPassword;
	
	private HttpClient httpClient; 
	private HttpParams httpParams;
	private UIHandler mUIHandler = new UIHandler();
	private ProgressDialog mProgressDialog;
	private PopupWindow mPopupWindow1;
	
	private String ICON_PATH;// = getActivity().getFilesDir().getAbsolutePath() + File.separator + "head.png";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ICON_PATH = getFilesDir().getAbsolutePath() + File.separator + "head.png";
		initView();
		initUserInfo();
		getHttpClient();
	}

	private void initView() {
		
		nameEdit = (EditText)findViewById(R.id.register_name);
		passwordEdit = (EditText)findViewById(R.id.register_password);
		forgetWord = (TextView)findViewById(R.id.register_text_forgetpassword);
		goIntoBt = (Button)findViewById(R.id.register_bt_login);
		nameEdit.addTextChangedListener(new SearchWather(nameEdit ));
		passwordEdit.addTextChangedListener(new SearchWather(passwordEdit ));
		goIntoBt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if( getInfo() ){
					goLogIn();
				}
			}
		});
		
		forgetWord.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				popForgetPassword();				
			}
		});
		
		mProgressDialog = new ProgressDialog(this);
	}
	
	protected void popForgetPassword() {
		
		closeIME();
		
        if (null == mPopupWindow1) {

            TextView mPopview = new TextView(this);
            mPopview.setTextSize(20);
            mPopview.setText(getResources().getString(R.string.forget_remaind));
            mPopupWindow1 = new PopupWindow(mPopview, (int)getResources().getDimension(R.dimen.forget_popwidth),
            		(int)getResources().getDimension(R.dimen.forget_popheight));

        }

        mPopupWindow1.setFocusable(true);
        mPopupWindow1.setOutsideTouchable(true);
        mPopupWindow1.setBackgroundDrawable(new BitmapDrawable());

        mPopupWindow1.showAsDropDown(forgetWord, (int)getResources().getDimension(R.dimen.personinfo_pop_margin_x),
        		(int)getResources().getDimension(R.dimen.personinfo_pop_margin_y_port));
	}

    public void closeIME() {
    	
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nameEdit.getWindowToken(), 0);
    }

	private void initUserInfo() {
		
		PersonalInfoPojo personInfo = PersonalUtil.getPersonInfo(this);
		
		if( null == personInfo ){
			
			return;
		}
		
	
		//goIntoMainActivity();
	}

	private void goIntoMainActivity() {
//		nameEdit.setText(mUserName);
//		passwordEdit.setText("********");
		Intent intent = new Intent(ActivityRegister.this, MainActivity.class);
		startActivityForResult(intent, 100);		
	}
	
	private boolean getInfo()
	{
		mUserName = nameEdit.getText().toString();
		mPassword = passwordEdit.getText().toString();
		if(mUserName.equals("") || mUserName.trim().equals(""))
		{
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(mPassword.equals("") || mPassword.trim().equals(""))
		{
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!PersonalUtil.isTextCanInput(this,mUserName) || !PersonalUtil.isTextCanInput(this, mPassword))
		{
			return false;
		}
		return true;
	}
	
	
	private void goLogIn() {
		showProgressDialog(this.getString(R.string.is_logining));
		new Thread() {  
            public void run() {  
            	InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            	inputMethodManager.hideSoftInputFromWindow(ActivityRegister.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            	boolean r = login(mUserName,mPassword);
            };  
        }.start(); 
	}
	
	public HttpClient getHttpClient() {  
        // 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）  
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
	
	public boolean login(String userName, String password) {  
		 
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
	            HttpResponse httpResponse = httpClient.execute(httpRequest);  
	            /* 若状态码为200 ok */  
	            if (httpResponse.getStatusLine().getStatusCode() == 200) {  
	                /* 读返回数据 */  
	                strResult = EntityUtils.toString(httpResponse.getEntity()); 
	                Log.d(TAG, "result = " + strResult);
	                JSONObject jsonObject = new JSONObject(strResult);
	                String status = jsonObject.getString("code");
	                String data = jsonObject.getString("data");
	                if(status.equals("200"))
	                {
	                	Message msg = mUIHandler.obtainMessage();
	                	msg.what = LOGIN_SUCCESS;
	                	msg.obj = data;
	                	mUIHandler.sendMessage(msg);
	                }
	                else if(status.equals("201"))
	                {
	                	Message msg = mUIHandler.obtainMessage();
	                	msg.what = LOGIN_SUCCESS;
	                	msg.obj = data;
	                	mUIHandler.sendMessage(msg);
	                }
	                else if(status.equals("404"))
	                {
	                	Message msg = mUIHandler.obtainMessage();
	                	msg.what = LOGIN_FAILURE;
	                	msg.obj = "登陆失败";
	                	mUIHandler.sendMessage(msg);
	                	return false;
	                }
	                return true;
	            } else {  
	                System.out.println("链接失败.........");  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return false;
	    } 
	
	public byte[] getImage(String path) throws Exception{  
		
		if(TextUtils.isEmpty(path)){
			return null;
		}
		if(path.equals("null"))
		{
			return null;
		}
        URL url = new URL(path); 
        Log.d(TAG,"url = " + url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
        conn.setConnectTimeout(5 * 1000);  
        conn.setRequestMethod("GET");  
        InputStream inStream = conn.getInputStream();  
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){  
            return readStream(inStream);  
        }  
        return null;  
    }  
	
	public static byte[] readStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while( (len=inStream.read(buffer)) != -1){  
            outStream.write(buffer, 0, len);  
        }  
        outStream.close();  
        inStream.close();  
        return outStream.toByteArray();  
    }  
	
	private void showProgressDialog(String str)
	{
		 if(!mProgressDialog.isShowing())
		 {
			 mProgressDialog.setMessage(str);
			 mProgressDialog.show();
		 }
	 }
	 
	 private void hideProgressDialog()
	 {
		 if(mProgressDialog.isShowing())
		 {
			 mProgressDialog.dismiss();
		 }
	 }
	
	class SearchWather implements TextWatcher{ 
	    //监听改变的文本框   
	    private EditText editText;   
	 
	    /** 
	     * 构造函数 
	     */   
	    public SearchWather(EditText editText){   
	        this.editText = editText;   
	    }   
	 
	    @Override   
	    public void onTextChanged(CharSequence ss, int start, int before, int count) {   
	        String editable = editText.getText().toString();   
	        String str = stringFilter(editable.toString()); 
	        if(!editable.equals(str)){ 
	            editText.setText(str); 
	            editText.setSelection(str.length()); 
	        } 
	    }   
	 
	    @Override   
	    public void afterTextChanged(Editable s) {   
	 
	    }   
	    @Override   
	    public void beforeTextChanged(CharSequence s, int start, int count,int after) {   
	 
	    }   
	 
	}   
	
	
	public class UIHandler extends Handler
	 {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what)
			{

			case LOGIN_SUCCESS:
			{
				hideProgressDialog();
				String json = msg.obj.toString();
				final PersonalInfoPojo personPojo = JsonTools.GsonToObj(json, PersonalInfoPojo.class);
				personPojo.setLoginName(mUserName);
				personPojo.setPassword(mPassword);
				PersonalUtil.savePersonInfo(ActivityRegister.this,  personPojo);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Bitmap bmp = null;
							byte head[] = getImage(personPojo.getPhotoPath());
							if(head != null)
							{
								bmp = BitmapFactory.decodeByteArray(head, 0, head.length);
							}
							else
							{
								bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
							}
							bmp.compress(CompressFormat.PNG, 100, new FileOutputStream(ICON_PATH));
							bmp.recycle();
							Log.d(TAG, "load head pic success!!!!");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
				Intent intent = new Intent();
				intent.putExtra("user_name", mUserName);
				goIntoMainActivity();
				break;
			}
			case LOGIN_FAILURE:
			{
				hideProgressDialog();
				Toast.makeText(ActivityRegister.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
			}
		}
	 }
	
	public static String stringFilter(String str)throws PatternSyntaxException{      
	    // 只允许字母和数字        
	    String   regEx  =  "[^a-zA-Z0-9@_.]";                      
	    Pattern   p   =   Pattern.compile(regEx);      
	    Matcher   m   =   p.matcher(str);      
	    return   m.replaceAll("").trim();      
	} 
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NO_FINISH)
		{
			if (data != null)
			{
				passwordEdit.setText("");
			}
		}
		else {
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);

	}
}
