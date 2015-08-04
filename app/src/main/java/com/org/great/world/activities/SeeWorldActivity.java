package com.org.great.world.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.org.great.world.Utils.Debug;
import com.org.great.world.data.CatalogPojo;

import java.util.ArrayList;

/**
 * Created by dj on 2015/7/31.
 * email:dingjun0225@gmail.com
 */
public class SeeWorldActivity extends Activity
{
    private ArrayList<CatalogPojo> mCatalogPojo;
    private int mIndexId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mCatalogPojo = (ArrayList<CatalogPojo>)bundle.getSerializable("list");
        mIndexId = bundle.getInt("index_id");
        Debug.d("mIndexId = " + mIndexId);
        for(int i = 0;i < mCatalogPojo.size();i++)
        {
            Debug.d("catalog " + i + " = " + mCatalogPojo.get(i).getTitle());
        }
    }

    @Override
    public void onBackPressed() {
        Debug.d("onBackPressed");
        setResult(RESULT_OK);
        finish();
        super.onBackPressed();

    }
}
