package com.org.great.world.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.easemob.exceptions.EaseMobException;
import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.RegisterAndLogin;
import com.org.great.world.Utils.Util;
import com.org.great.world.Views.TabView;
import com.org.great.world.data.AllAD;
//import com.org.great.world.fragments.ChatFragment;
import com.org.great.world.fragments.CommunionFragment;
import com.org.great.world.fragments.GreatWorldFragment;
import com.org.great.world.fragments.MeFragment;
import com.org.great.world.fragments.SettingFragment;
import com.org.great.wrold.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private GreatWorldFragment mGreatWorldFragment;
    private SettingFragment mMeFragment;
//    private ChatFragment mChatFragment;
    private TabView mGreatWorldBtn;
    private TabView mMeBtn;
    private TabView mCommunionBtn;
    private List<TabView> mTabViewList = new ArrayList<TabView>();
    private FragmentManager mFragmentManager = null;
    private FragmentTransaction mTransaction = null;
    private int mFrontFragment = -1;
    private final int GREAT_WORLD = 0;
    private final int ME = 1;
    private final int COMMU = 2;
    private LinearLayout mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Util.createDB(this);
        setContentView(R.layout.activity_main);
        RegisterAndLogin.getInstance(this);    
        init();
    }

    private void init() {
        mFragmentManager = this.getSupportFragmentManager();
        mGreatWorldBtn = (TabView) findViewById(R.id.btn_great_world);
        mMeBtn = (TabView) findViewById(R.id.btn_me);
        mCommunionBtn = (TabView)findViewById(R.id.btn_communion);
        mAdView = (LinearLayout)findViewById(R.id.ad_layout);
        mGreatWorldBtn.setOnClickListener(this);
        mMeBtn.setOnClickListener(this);
        mCommunionBtn.setOnClickListener(this);
        mTabViewList.add(mGreatWorldBtn);
        mTabViewList.add(mMeBtn);
        mTabViewList.add(mCommunionBtn);
        setDefaultFragment();
    }

    private void setDefaultFragment() {
        mFrontFragment = GREAT_WORLD;
        changeFragment();
    }

    private void changeFragment() {
        mTransaction = mFragmentManager.beginTransaction();
        if (mFrontFragment == GREAT_WORLD) {
            if (mGreatWorldFragment == null) {
                mGreatWorldFragment = new GreatWorldFragment();
            }
            mTransaction.replace(R.id.content, mGreatWorldFragment);
            mGreatWorldBtn.setSelected(true);
        } else if (mFrontFragment == ME) {
            if (mMeFragment == null) {
                mMeFragment = new SettingFragment();
            }
            mTransaction.replace(R.id.content, mMeFragment);
            mMeBtn.setSelected(true);
        }
        else if(mFrontFragment == COMMU)
        {
        	if(DemoHXSDKHelper.getInstance().isLogined() == false)
        	{
        		Toast.makeText(this, R.string.please_login, Toast.LENGTH_SHORT).show();
        		return;
        	}
//        	if (mChatFragment == null) {
//        		mChatFragment = new ChatFragment();
//            }
//            mTransaction.replace(R.id.content, mChatFragment);
            mCommunionBtn.setSelected(true);
        }
        mTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkWifi();
        if(mAdView != null)
		{
        	mAdView.removeAllViews();
			mAdView.addView(AllAD.getGDTBannerView(this));
		}
    }

    private void checkWifi()
    {
        if(!Util.checkWifiConnected(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.tip_title)
                    .setMessage(R.string.please_connect_network)
                    .setPositiveButton(R.string.tip_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_great_world: {
                if (mFrontFragment != GREAT_WORLD) {
                    mFrontFragment = GREAT_WORLD;
                    changeFragment();
                }
                break;
            }
            case R.id.btn_me: {
                if (mFrontFragment != ME) {
                    mFrontFragment = ME;
                    changeFragment();
                }
                break;
            }
            case R.id.btn_communion:
            {
            	if(DemoHXSDKHelper.getInstance().isLogined() == false)
            	{
            		Toast.makeText(this, R.string.please_login, Toast.LENGTH_SHORT).show();
            		if (mFrontFragment != ME) {
                        mFrontFragment = ME;
                        changeFragment();
                        Debug.d("match id = R.id.btn_communiton");
//                        mMeBtn.setSelected(true);
                        changeTabViewStatus(R.id.btn_me);
                        return;
                    }
            		return;
            	}
            	if (mFrontFragment != COMMU) {
                    mFrontFragment = COMMU;
//                	changeFragment();
                }
            	enterChatRoom();
            	return;
            }
        }
        changeTabViewStatus(id);
    }

    public void changeTabViewStatus(int id) {
        int len = mTabViewList.size();
        for (int i = 0; i < len; i++) {
            boolean bMatchId = mTabViewList.get(i).getId() == id;
            mTabViewList.get(i).setSelector(bMatchId);
        }
    }
    
    private void enterChatRoom()
    {
    	//需异步处理
//    	String charId = "122980374539141604";
    	String charId = Util.getGroupId(this);
		// 进入群聊
		Intent intent = new Intent(this, ChatActivity.class);
		// it is group chat
//		intent.putExtra("chatType", ChatActivity.CHATTYPE_CHATROOM);
		intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
		intent.putExtra("groupId", charId);
		startActivity(intent);
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Util.closeDB();
	}
    
    
}
