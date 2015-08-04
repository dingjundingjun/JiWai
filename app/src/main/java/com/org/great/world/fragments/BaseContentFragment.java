package com.org.great.world.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.org.great.world.Views.ContentPaperAdapter;
import com.org.great.world.Views.FragmentViewPaper;
import com.org.great.world.Views.TitleScroolView;
import com.org.great.wrold.R;

/**
 * Created by dj on 2015/8/4.
 * email:dingjun0225@gmail.com
 */
public class BaseContentFragment extends Fragment {
    private Activity mBaseActivity;
    private View mParentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseActivity = getActivity();
        View view = LayoutInflater.from(mBaseActivity).inflate(R.layout.seeworld_layout, null);
        mParentView = view;
        return mParentView;
    }

    public void updateFragmentList()
    {
        init();
    }

    private void init()
    {

    }
}
