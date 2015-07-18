package com.org.great.world.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.org.great.wrold.R;

/**
 * Created by dj on 2015/7/17.
 * email:dingjun0225@gmail.com
 */
public class GreatWorldFragment extends BaseFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.great_world_layout, container, false);
    }
}
