package com.org.great.world.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.org.great.world.Utils.Debug;

/**
 * Created by dj on 2015/7/17.
 * email:dingjun0225@gmail.com
 */
public class GreatWorldFragment extends BaseFragment{
    private SeeWorld mSeeWorld;
    private Joke mJoke;
    private Game mGame;
    private Video mVideo;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSeeWorld = new SeeWorld();
        mJoke = new Joke();
        mGame = new Game();
        mVideo = new Video();
        mFragmentList.clear();
        mFragmentList.add(mSeeWorld);
        mFragmentList.add(mJoke);
        mFragmentList.add(mGame);
        mFragmentList.add(mVideo);
        mTitleListStr.clear();
        Debug.d("mSeeworld title = " + mSeeWorld.getTitle());
        Debug.d("mJoke title = " + mJoke.getTitle());
        mTitleListStr.add(mSeeWorld.getTitle());
        mTitleListStr.add(mJoke.getTitle());
        mTitleListStr.add(mGame.getTitle());
        mTitleListStr.add(mVideo.getTitle());
        updateFragmentList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Debug.d("onActivityResult 11");
        super.onActivityResult(requestCode, resultCode, data);

    }
}
