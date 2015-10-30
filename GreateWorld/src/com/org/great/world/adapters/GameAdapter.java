package com.org.great.world.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.org.great.world.adapters.VideoAdapter.ViewHolder;
import com.org.great.world.data.CatalogPojo;
import com.org.great.world.data.GamePojo;
import com.org.great.wrold.R;

import java.util.List;

/**
 * Created by Administrator on 2015/8/27.
 */
public class GameAdapter extends BAdapter
{
    private List<GamePojo> mGameList;
    private ImageLoader loader;
    private DisplayImageOptions options;
    public void setList(List<GamePojo> list)
    {
        mGameList = list;
    }
    public GameAdapter(Context mContext) {
        super(mContext);
        loader = ImageLoader.getInstance();
		loader.init(ImageLoaderConfiguration.createDefault(mContext));
    }


    @Override
    public int getCount() {
        return mGameList.size();
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
            convertView = View.inflate(mContext, R.layout.game_catalog_item_layout, null);
            viewHolder.videoThumbnail = (ImageView) convertView.findViewById(R.id.video_thumbnail);
            viewHolder.title = (TextView) convertView.findViewById(R.id.video_title);
            convertView.setTag(viewHolder);
        }

        viewHolder.title.setText(mGameList.get(position).getTitle());
        loader.displayImage(mGameList.get(position).getImage(), viewHolder.videoThumbnail,
				options, new SimpleImageLoadingListener());
        viewHolder.videoThumbnail.setImageURI(Uri.parse(mGameList.get(position).getImage()));
        return convertView;
        
//        TextView view = null;
//        if (convertView == null) {
//            view = new TextView(mContext);
//        } else {
//            view = (TextView) convertView;
//        }
//        view.setText(mGameList.get(position).getTitle());
////        view.setBackgroundColor(Color.GREEN);
//        view.setWidth(500);
//        view.setHeight(100);
//        view.setGravity(Gravity.CENTER);
//        view.setTextSize(30);
//        return view;
    }
    
    static class ViewHolder
    {
        ImageView videoThumbnail;
        TextView title;
        TextView playCount;
    }
}
