package com.org.great.world.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.Util;
import com.org.great.world.Views.BaseListView;
import com.org.great.world.adapters.CommentAdapter;
import com.org.great.world.data.CatalogPojo;
import com.org.great.wrold.R;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dj on 2015/8/4.
 * email:dingjun0225@gmail.com
 *
 */
public class BaseContentFragment extends Fragment implements View.OnClickListener
{
    private Activity mBaseActivity;
    private View mParentView;
    private WebView mWebView;
    private TextView mTitleTextView;
    private CatalogPojo mCatalogPojo;
    private ProgressDialog mProgressDialog;
    private UMSocialService mSocialService;
    private List<UMComment> mComments;
    private LinearLayout mCommentLayout;
    private TextView mMoreComment;
    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService(Util.Constants.DESCRIPTOR);
    public BaseContentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseActivity = getActivity();
        View view = LayoutInflater.from(mBaseActivity).inflate(R.layout.seeworld_layout, null);
        mParentView = view;
        init();
        return mParentView;
    }

    public void setCatalogPojo(CatalogPojo cp)
    {
        mCatalogPojo = cp;

    }

    private void init()
    {
        mTitleTextView = (TextView)mParentView.findViewById(R.id.title);
        mWebView = (WebView)mParentView.findViewById(R.id.webview);
        mMoreComment = (TextView)mParentView.findViewById(R.id.more_comment);
        mCommentLayout = (LinearLayout)mParentView.findViewById(R.id.comments_layout);
        mMoreComment.setOnClickListener(this);
        initWebView();
        initProgressDialog();
        addQQQZonePlatform();
        addWXPlatform();
        initComment();
        loadData();
    }

    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wxa0a4dcc78756d00f";
        String appSecret = "29d7f92c73f1ffff42b2512c4f28b741";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(getActivity(), appId, appSecret);
        wxHandler.setTitle(mCatalogPojo.getTitle());
        wxHandler.setTargetUrl(mCatalogPojo.getUrl());
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appId, appSecret);
        wxCircleHandler.setTitle(mCatalogPojo.getTitle());
        wxCircleHandler.setTargetUrl(mCatalogPojo.getUrl());
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-微信。http://www.umeng.com/social");
        weixinContent.setTitle("友盟社会化分享组件-微信");
        weixinContent.setTargetUrl("http://www.umeng.com/social");
        //weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-朋友圈。http://www.umeng.com/social");
        circleMedia.setTitle("友盟社会化分享组件-朋友圈");
        // circleMedia.setShareMedia(urlImage);
        // circleMedia.setShareMedia(uMusic);
        // circleMedia.setShareMedia(video);
        circleMedia.setTargetUrl("http://www.umeng.com/social");
        mController.setShareMedia(circleMedia);
    }

    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     * @return
     */
    private void addQQQZonePlatform() {
        String appId = "1104234972";
        String appKey = "Sgvun7Ksq6ov6VOu";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), appId, appKey);
        qqSsoHandler.addToSocialSDK();
        qqSsoHandler.setTargetUrl(mCatalogPojo.getUrl());
        qqSsoHandler.setTitle(mCatalogPojo.getTitle());

        /*QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent("来自带你看世界，掌握世界");
        qqShareContent.setTitle(mMessage.getTitle());
        qqShareContent.setTargetUrl(mMessage.getUrl());
        qqShareContent.setShareImage(new UMImage(mContext, "http://www.baidu.com/img/bdlogo.png"));
        mController.setShareMedia(qqShareContent);*/

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(), appId, appKey);
        qZoneSsoHandler.setTargetUrl(mCatalogPojo.getUrl());
        qZoneSsoHandler.addToSocialSDK();

    }

    private void getCommentFromUM(long sinceTime)
    {
        mSocialService.getComments(mBaseActivity, new SocializeListeners.FetchCommetsListener() {
            @Override
            public void onStart() {
                System.out.println("====onStart==>>> ");
            }

            @Override
            public void onComplete(int status, List<UMComment> comments, SocializeEntity arg2) {
                Debug.d("status = " + status);
                if(status == 200)    //ok
                {
                    Debug.d("comments size = " + comments.size());
                    if(mCommentLayout.getChildCount() > 0)
                    {
                        return;
                    }
                    if(comments != null && !comments.isEmpty())
                    {
                        mComments.clear();
                        if(comments.size() > 3)
                        {
                            mComments.addAll(comments.subList(0,3));
                        }
                        else if(comments.size() == 0)
                        {
                            mMoreComment.setVisibility(View.GONE);
                        }
                        else
                        {
                            mComments.addAll(comments);
                        }
                        for(int i = 0;i < mComments.size();i++) {
                            addCommentLayout(mComments.get(i));
                        }
                    }
                    else
                    {

                    }
                }
                else if(status == -104)
                {
                    getCommentFromUM(-1);
                }
            }
        }, -1);
    }

    private void initComment()
    {
        mComments = new ArrayList<UMComment>();
        if (mSocialService == null) {
            Debug.d("getUMSocial name = " + "JJYY_" + mCatalogPojo.getTitle());
            mSocialService = UMServiceFactory.getUMSocialService("JJYY_" + mCatalogPojo.getTitle());
        }
        getCommentFromUM(-1);
    }

    private void initProgressDialog()
    {
        mProgressDialog = new ProgressDialog(mBaseActivity);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }
    public void loadData()
    {
        if(mCatalogPojo == null)
        {
            return;
        }
        mWebView.loadUrl(mCatalogPojo.getUrl());
        mTitleTextView.setText(mCatalogPojo.getTitle());
        mProgressDialog.show();
    }

    private void initWebView() {
        registerForContextMenu(mWebView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setLoadWithOverviewMode(true);

        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        setWebClientListener();
    }

    void setWebClientListener()
    {
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                Debug.d("onPageFinished url = " + url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl)
            {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                super.onProgressChanged(view, newProgress);
                Debug.d("onProgressChanged  = " + newProgress);
                if(newProgress >= 40)
                {
                    mProgressDialog.dismiss();
                }
            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }

    private void addCommentLayout(UMComment comnent)
    {
        View convertView = View.inflate(mBaseActivity, R.layout.comment_layout_item, null);
        TextView content = (TextView) convertView.findViewById(R.id.content);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        content.setText(comnent.mText);
        date.setText(DateUtils.formatDateTime(mBaseActivity, comnent.mDt, DateUtils.FORMAT_12HOUR));
        name.setText(comnent.mUname);
        mCommentLayout.addView(convertView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.more_comment:
            {
                gotoCommentActivity();
                break;
            }
        }
    }

    public void gotoCommentActivity()
    {
        Debug.d("gotoCommentActvitiy");
    }
}
