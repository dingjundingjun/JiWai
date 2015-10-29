package com.org.great.world.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;

import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.Util;
import com.org.great.world.Views.FragmentViewPaper;
import com.org.great.world.data.AllAD;
import com.org.great.world.data.CatalogPojo;
import com.org.great.world.data.GamePojo;
import com.org.great.world.fragments.BaseContentFragment;
import com.org.great.wrold.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class GameActivity extends Activity
{
    private ArrayList<GamePojo> mmGamePojoList;
    public FragmentViewPaper mFragmentViewPaper;
    private FragmentManager mFragmentManager = null;
    private GamePojo mGamePojo;
    private WebView mWebView;
    private ProgressDialog mProgressDialog;
    private int mIndexId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.savePlayGameTime(this);
        setContentView(R.layout.game_layout);
        init();
    }

    private void init()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mmGamePojoList = (ArrayList<GamePojo>)bundle.getSerializable("list");
        mIndexId = bundle.getInt("index_id");
        Debug.d("mIndexId = " + mIndexId);
        mGamePojo = mmGamePojoList.get(mIndexId);
        mWebView = (WebView)findViewById(R.id.webview);
        initProgressDialog();
        initWebView();
    }

    private void initProgressDialog()
    {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
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
        loadData();
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
    public void loadData()
    {
        if(mGamePojo == null)
        {
            return;
        }
        mWebView.loadUrl(mGamePojo.getUrl());
        mProgressDialog.show();
    }

    @Override
    public void onBackPressed() {
        Debug.d("onBackPressed");
        setResult(RESULT_OK);
        finish();
        super.onBackPressed();
    }
    
    
    @Override
	protected void onResume() {
		super.onResume();
		addAd();
	}

	public void addAd()
	{
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		addContentView(AllAD.getGDTBannerView(this), layoutParams);
	}
}
