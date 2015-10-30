package com.org.great.world.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.org.great.world.Utils.JsonTools;
import com.org.great.world.Utils.PersonalUtil;
import com.org.great.world.activities.ActivityRegister;
import com.org.great.world.activities.GalleryUrlActivity;
import com.org.great.world.adapters.PopGridListAdapter;
import com.org.great.world.data.PersonalInfoPojo;
import com.org.great.wrold.R;
import com.soundcloud.android.crop.Crop;
import com.soundcloud.android.crop.CropImageActivity;
import com.umeng.fb.FeedbackAgent;

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

import java.io.File;
import java.nio.charset.Charset;

public class MeFragment extends BaseFragment
        implements android.widget.RadioGroup.OnCheckedChangeListener,
        OnClickListener, OnItemClickListener, MediaScannerConnectionClient
{
    public final static String TAG = " MeFragment -->   ";

    private View mMainView = null;
    private LinearLayout mLinearLayout;

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
    private RadioGroup mRadioGroup;
    private RadioButton mBoy;
    private RadioButton mGirl;
    private TextView mChangeGrade;
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

    private FeedbackAgent mFeedBackAgent;

    private static String ICON_PATH;
    private static final int UPDATE_INFO_SUCCESS = 0;
    private static final int UPDATE_INFO_FAILED = 1;
    private UIHandler mUIHandler = new UIHandler();

    private MediaScannerConnection conn;
    private String SCAN_PATH ;
    private static final String FILE_TYPE="image/*";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_me_layout, container, false);
        Log.d("onCreateView"," ICON_PATH = " + ICON_PATH);
        ICON_PATH = getActivity().getFilesDir().getAbsolutePath() + File.separator + "head.png";
        initView();
        return mMainView;
    }

    private void initView()
    {
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
        mRadioGroup = (RadioGroup) mMainView.findViewById(R.id.layout_sex);
        mRadioGroup.setOnCheckedChangeListener(this);

        mGirl = (RadioButton) mMainView.findViewById(R.id.girl);
        mBoy = (RadioButton) mMainView.findViewById(R.id.boy);

        mChangeGrade = (TextView) mMainView.findViewById(R.id.person_set_grade);
        mChangeGradePull = (ImageView) mMainView.findViewById(R.id.person_gradearrorw);

        mFeedback = (TextView)mMainView.findViewById(R.id.person_feedback);
        mFeedback.setOnClickListener(this);
        mQuit = (TextView)mMainView.findViewById(R.id.person_exit);
        mQuit.setOnClickListener(this);
        mMyPic = (TextView)mMainView.findViewById(R.id.person_picture);
        mMyPic.setOnClickListener(this);


        mChangeGrade.setOnClickListener(this);
        mChangeGrade.setOnClickListener(this);
        mSetBt.setOnClickListener(this);
        mPhotoLayout.setOnClickListener(this);

        mFeedBackAgent = new FeedbackAgent(getActivity());
        mFeedBackAgent.sync();

        getUserInfo();
        initMyInfo();
    }

    private void getUserInfo() {
        mPersonInfo = PersonalUtil.getPersonInfo(getActivity());
        if( null == mPersonInfo )
        {
            Toast.makeText(getActivity(), getActivity().getString(R.string.find_black_person), Toast.LENGTH_SHORT).show();
        }
    }

    private void initMyInfo() {
        setImageBitmap();
        if (!isEditMode) {
            mNormalLayout.setVisibility(View.VISIBLE);
            mChangeLayout.setVisibility(View.GONE);
            mSetBt.setBackgroundResource(R.drawable.person_change);
            String nickName = mPersonInfo.getNickName();
            mNickTextView.setText((nickName == null || nickName.equals("null") ? "无名小盆友" : nickName)); // 没有别名则使用登录名
            mGrade.setText((mPersonInfo.getGrade() == null || mPersonInfo.getGrade().equals("null")) ? "一年级" : mPersonInfo.getGrade());
            mPhotoLayout.animate().setInterpolator(new OvershootInterpolator()).scaleX(0.9f).scaleY(0.9f).start();
            mSexPic.setImageResource(mPersonInfo.getSex().equals("1") ? R.drawable.person_girl_normal : R.drawable.person_boy_normal);
        } else {
            mNormalLayout.setVisibility(View.GONE);
            mChangeLayout.setVisibility(View.VISIBLE);
            mSetBt.setBackgroundResource(R.drawable.person_change_finish);
            mPhotoLayout.animate().setInterpolator(new OvershootInterpolator())
                    .scaleX(0.9f).scaleY(0.9f).start();
            mChangeGrade.setText(mGrade.getText());
            mChangeNickName.setText(mNickTextView.getText());
            mSex = mPersonInfo.getSex();
            mRadioGroup.check(("1".equals(mPersonInfo.getSex()) ? mGirl.getId():mBoy.getId()));
            mChangeNickName.selectAll();
        }
        PersonalUtil.isLogined(getActivity());
    }

    private void setImageBitmap() {
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
        loader.init(ImageLoaderConfiguration.createDefault(getActivity()));
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
                    new Thread() {
                        public void run() {
                            boolean r = savePersonInfo();
                            if(r)
                            {
                                mUIHandler.sendEmptyMessage(UPDATE_INFO_SUCCESS);
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

            case R.id.person_set_grade:
            case R.id.person_gradearrorw:
            {
                popPhotoSelection();
                break;
            }

            case R.id.person_photo:
            {
                //goto set icon
                setCrop();
                break;
            }

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
                PersonalUtil.delPersonInfo(getActivity());

                Intent intent = new Intent();

                getActivity().setResult(ActivityRegister.NO_FINISH, intent);
                getActivity().setIntent(intent);
                getActivity().finish();
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
            Intent intent = new Intent(getActivity(), GalleryUrlActivity.class);
            String imageUri = mPersonInfo.getPhotoPath();
            if( null == imageUri || true == imageUri.isEmpty() ) {
                imageUri = "drawable://" + R.drawable.default_avatar;
            }

            intent.putExtra("urls", imageUri);
            startActivity(intent);
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

            Toast.makeText(getActivity(), "還沒收藏圖片呢啊", Toast.LENGTH_SHORT).show();
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
        conn = new MediaScannerConnection(getActivity(),this);
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
//            ContentBody cbFile = new FileBody(file);
            Log.d(TAG,"update info = " + mPersonInfo.toString());
            Log.d(TAG,"mGrade.getText().toString() = " + mGrade.getText().toString());
            mPersonInfo.setNickName(mChangeNickName.getText().toString());
//        	mpEntity.addPart("loginName", new StringBody(mPersonInfo.getLoginName()));
            mpEntity.addPart("password", new StringBody(mPersonInfo.getPassword()));
            mpEntity.addPart("nickName", new StringBody(mChangeNickName.getText().toString(),Charset.forName("UTF-8")));
            mpEntity.addPart("accountId", new StringBody(mPersonInfo.getAccountId()));
            mpEntity.addPart("sex", new StringBody(mSex));
            mpEntity.addPart("grade", new StringBody(mGrade.getText().toString(),Charset.forName("UTF-8")));
            mpEntity.addPart("photoPath", new StringBody(mPersonInfo.getPhotoPath(),Charset.forName("UTF-8")));
            mpEntity.addPart("files", new FileBody(new File(ICON_PATH)));
            /* 添加请求参数到请求对象 */
//            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            httpRequest.setEntity(mpEntity);
            /* 发送请求并等待响应 */
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            /* 若状态码为200 ok */
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                /* 读返回数据 */
                strResult = EntityUtils.toString(httpResponse.getEntity());
                Log.d(TAG, "result = " + strResult);
                JSONObject jsonObject = new JSONObject(strResult);
                String status = jsonObject.getString("code");
                String message = jsonObject.getString("data");
                if(status.equals("200"))
                {

                    System.out.println("更新成功.........");
                    mPersonInfo = JsonTools.GsonToObj(message, PersonalInfoPojo.class);
                    PersonalUtil.savePersonInfo(this.getActivity(), mPersonInfo);
                    return true;
                }
                else if(status.equals("404"))
                {
                    System.out.println("更新失败.........");
                    return false;
                }
            }
            else {
                System.out.println("链接失败.........");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void setCrop()
    {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 111);
    }

    private void handleCrop(int resultCode, Intent result) {

        if (resultCode == this.getActivity().RESULT_OK && result != null) {
            mPhotoView.setImageBitmap(BitmapFactory.decodeFile(ICON_PATH));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this.getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(ICON_PATH));
        Intent cropIntent = new Intent();
        cropIntent.setData(source);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, destination);
        cropIntent.setClass(this.getActivity(), CropImageActivity.class);
        startActivityForResult(cropIntent, Crop.REQUEST_CROP);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.boy) {
            mSex = "2";
        } else {
            mSex = "1";
        }
    }

    private void popPhotoSelection() {

        closeIME();

        if (null == mPopupWindow1) {

            LayoutInflater layoutInflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mPopview = layoutInflater.inflate(R.layout.popwindow_grade, null);
            mPopupWindow1 = new PopupWindow(mPopview, (int)getResources().getDimension(R.dimen.personinfo_popwidth),
                    (int)getResources().getDimension(R.dimen.personinfo_popheight));

            mPopList = (ListView) mPopview.findViewById(R.id.pop_grade_list);
        }

        mPopList.setAdapter(new PopGridListAdapter(getActivity(), mPersonInfo.getGrade()));
        mPopList.setOnItemClickListener(this);
        mPopupWindow1.setFocusable(true);
        mPopupWindow1.setOutsideTouchable(true);
        mPopupWindow1.setBackgroundDrawable(new BitmapDrawable());

        mPopupWindow1.showAsDropDown(mChangeGradePull, (int)getResources().getDimension(R.dimen.personinfo_pop_margin_x),
                (int)getResources().getDimension(R.dimen.personinfo_pop_margin_y_port));

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
        String gradeString = mPopList.getAdapter().getItem(pos).toString();
        mChangeGrade.setText(gradeString);
        mPersonInfo.setGrade(gradeString);
        mPopupWindow1.dismiss();
    }

    public void closeIME() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
                startActivity(intent);
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
                    initMyInfo();
                    break;
                }
                case UPDATE_INFO_FAILED:
                {
                    initMyInfo();
                    break;
                }
            }
        }
    }
}