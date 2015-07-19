package com.org.great.world.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.org.great.world.Utils.Debug;
import com.org.great.wrold.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dj on 2015/7/17.
 * email:dingjun0225@gmail.com
 */
public class GreatWorldFragment extends BaseFragment{
    private SeeWorld mSeeWorld;
    private Joke mJoke;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSeeWorld = new SeeWorld();
        mJoke = new Joke();
        mFragmentList.clear();
        mFragmentList.add(mSeeWorld);
        mFragmentList.add(mJoke);
        mTitleListStr.clear();
        Debug.d("mSeeworld title = " + mSeeWorld.getTitle());
        Debug.d("mJoke title = " + mJoke.getTitle());
        mTitleListStr.add(mSeeWorld.getTitle());
        mTitleListStr.add(mJoke.getTitle());
        updateFragmentList();
    }
}
