package com.org.great.world.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.Util;
import com.org.great.world.adapters.CommentAdapter.ViewHolder;
import com.org.great.world.data.CatalogPojo;
import com.org.great.wrold.R;
import com.umeng.socialize.bean.UMComment;

import java.io.File;
import java.util.List;

/**
 * Created by dj on 2015/7/31.
 * email:dingjun0225@gmail.com
 */
public class SeeWorldAdapter extends BAdapter
{
    private List<UMComment> mUMCommentList;
    private ImageLoader mLoader;
    private DisplayImageOptions mOptions;
    private List<CatalogPojo> mCatalogList;

    public void setList(List<CatalogPojo> list)
    {
        mCatalogList = list;
    }

    public SeeWorldAdapter(Context mContext) {
        super(mContext);
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        mOptions = builder.displayer(new FadeInBitmapDisplayer(200, true, true, true)).build();
        mLoader = ImageLoader.getInstance();
        mLoader.init(ImageLoaderConfiguration.createDefault(mContext));
    }

    @Override
    public int getCount() {
        return mCatalogList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder viewHolder;
        if(convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();

        } else {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.seeworld_list_item_layout, null);
            viewHolder.catalogText = (TextView) convertView.findViewById(R.id.catalog_text);
            viewHolder.catalogPic = (ImageView) convertView.findViewById(R.id.catalog_pic);
            convertView.setTag(viewHolder);
        }
        CatalogPojo cp = mCatalogList.get(position);
        viewHolder.catalogText.setText(cp.getTitle());
        String pUrl = cp.getSnapshot();
        if(pUrl != null)
        {
        	//先判断本地是否有缓存，有则从缓存加载，没有则从网络加载并且下载
        	File file = new File("/mnt/sdcard/叽歪/目录/" + cp.getTitle() + ".jpg");
        	if(!file.exists())
        	{
        		mLoader.displayImage(pUrl, viewHolder.catalogPic, mOptions, new SimpleImageLoadingListener());
        		Util.downloadCatalogPic(mContext, cp.getTitle()+".jpg", pUrl);
        	}
        	else
        	{
        		mLoader.displayImage("file:///mnt/sdcard/叽歪/目录/" + cp.getTitle() + ".jpg", viewHolder.catalogPic,mOptions);
        	}
        	viewHolder.catalogPic.setVisibility(View.VISIBLE);
        }
        else
        {
        	viewHolder.catalogPic.setVisibility(View.GONE);
        }
        return convertView;
    }
    
    static class ViewHolder
    {
        TextView catalogText;
        ImageView catalogPic;
    }

}
