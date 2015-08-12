package com.org.great.world.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.org.great.world.Utils.Debug;
import com.org.great.world.Views.ContentPaperAdapter;
import com.org.great.world.Views.FragmentViewPaper;
import com.org.great.world.Views.TitleScroolView;
import com.org.great.world.data.CatalogPojo;
import com.org.great.wrold.R;

/**
 * Created by dj on 2015/8/4.
 * email:dingjun0225@gmail.com
 */
public class BaseContentFragment extends Fragment {
    private Activity mBaseActivity;
    private View mParentView;
    private WebView mWebView;
    private TextView mTitleTextView;
    private CatalogPojo mCatalogPojo;
    private ProgressDialog mProgressDialog;
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
        initWebView();
        initProgressDialog();
        loadData();
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
}
