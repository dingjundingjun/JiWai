package com.org.great.world.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.org.great.wrold.R;

/**
 * Created by dj on 2015/7/17.
 * email:dingjun0225@gmail.com
 */
public class MeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.me_layout, container, false);
    }
}
