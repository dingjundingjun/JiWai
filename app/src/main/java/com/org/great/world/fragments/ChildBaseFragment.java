package com.org.great.world.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by dj on 2015/7/19.
 * email:dingjun0225@gmail.com
 */
public class ChildBaseFragment extends Fragment {
    public Activity mBaseActivity;
    public String mTitle = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        mBaseActivity = getActivity();
        super.onCreate(savedInstanceState);
    }

    public String getTitle()
    {
        return mTitle;
    }
}
