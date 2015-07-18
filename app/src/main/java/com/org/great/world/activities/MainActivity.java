package com.org.great.world.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.org.great.world.fragments.GreatWorldFragment;
import com.org.great.world.fragments.MeFragment;
import com.org.great.wrold.R;


public class MainActivity extends Activity implements View.OnClickListener {
    private GreatWorldFragment mGreatWorldFragment;
    private MeFragment mMeFragment;
    private ImageButton mGreatWorldBtn;
    private ImageButton mMeBtn;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private int mFrontFragment = -1;
    private final int GREAT_WORLD = 0;
    private final int ME = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mFragmentManager = getFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mGreatWorldBtn = (ImageButton) findViewById(R.id.btn_great_world);
        mMeBtn = (ImageButton) findViewById(R.id.btn_me);
        mGreatWorldBtn.setOnClickListener(this);
        mMeBtn.setOnClickListener(this);
        setDefaultFragment();
    }

    private void setDefaultFragment() {
        mGreatWorldFragment = new GreatWorldFragment();
        mTransaction.replace(R.id.content, mGreatWorldFragment);
        mTransaction.commit();
        mFrontFragment = GREAT_WORLD;
    }

    private void changeFragment() {
        mTransaction = mFragmentManager.beginTransaction();
        if (mFrontFragment == GREAT_WORLD) {
            if (mGreatWorldFragment == null) {
                mGreatWorldFragment = new GreatWorldFragment();
            }
            mTransaction.replace(R.id.content,mGreatWorldFragment);
        }
        else if (mFrontFragment == ME)
        {
            if(mMeFragment == null)
            {
                mMeFragment = new MeFragment();
            }
            mTransaction.replace(R.id.content,mMeFragment);
        }
        mTransaction.commit();
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
                if(mFrontFragment != ME) {
                    mFrontFragment = ME;
                    changeFragment();
                }
                break;
            }
        }
    }
}
