package com.org.great.world.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.org.great.world.Utils.Debug;
import com.org.great.world.Views.FragmentViewPaper;
import com.org.great.world.data.CatalogPojo;
import com.org.great.world.fragments.BaseContentFragment;
import com.org.great.wrold.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dj on 2015/7/31.
 * email:dingjun0225@gmail.com
 */
public class SeeWorldActivity extends FragmentActivity
{
    private ArrayList<CatalogPojo> mCatalogPojo;
    public FragmentViewPaper mFragmentViewPaper;
    private FragmentManager mFragmentManager = null;
    private int mIndexId = -1;
    private List<BaseContentFragment> mBaseContentList;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_parent_layout);
        init();
    }

    private void init()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mCatalogPojo = (ArrayList<CatalogPojo>)bundle.getSerializable("list");
        mIndexId = bundle.getInt("index_id");
        Debug.d("mIndexId = " + mIndexId);
        mBaseContentList = new ArrayList<BaseContentFragment>();
//        for(int i = 0;i < mCatalogPojo.size();i++)
//        {
//            Debug.d("catalog " + i + " = " + mCatalogPojo.get(i).getTitle());
//            BaseContentFragment base = new BaseContentFragment();
//            mBaseContentList.add(base);
//        }
        CatalogPojo catalogPojo = mCatalogPojo.get(mIndexId);
        BaseContentFragment base = new BaseContentFragment();
        base.setCatalogPojo(catalogPojo);
        mBaseContentList.add(base);

        mFragmentManager = this.getSupportFragmentManager();
        mFragmentViewPaper = (FragmentViewPaper)findViewById(R.id.viewpager);
        mFragmentViewPaper.setAdapter(new PaperAdapter(mFragmentManager, mBaseContentList));
        mFragmentViewPaper.setCurrentItem(0);
        mFragmentViewPaper.setOnPageChangeListener(new BaseOnPageChangeListener());
    }

    @Override
    public void onBackPressed() {
        Debug.d("onBackPressed");
        setResult(RESULT_OK);
        finish();
        super.onBackPressed();

    }

public class BaseOnPageChangeListener implements ViewPager.OnPageChangeListener {

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageSelected(int arg0) {
    }
}

    public class PaperAdapter extends FragmentPagerAdapter {
        private List<BaseContentFragment> mList;
        public PaperAdapter(FragmentManager fm, List<BaseContentFragment> list) {
            super(fm);
            mList = list;
        }

        public Fragment getItem(int position) {
            return mList.get(position);
        }

        public int getCount() {
            return mList.size();
        }
    }
}
