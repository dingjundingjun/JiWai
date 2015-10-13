package com.org.great.world.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.org.great.world.adapters.CommentAdapter.ViewHolder;
import com.org.great.world.data.GamePojo;
import com.org.great.world.data.VideoPojo;
import com.org.great.wrold.R;
import com.umeng.socialize.bean.UMComment;

import java.util.List;

/**
 * Created by dj on 2015/9/7.
 * email:dingjun0225@gmail.com
 */
public class VideoAdapter extends BAdapter{

    private List<VideoPojo> mVideoList;
    private ImageLoader loader;// = ImageLoader.getInstance();
    private DisplayImageOptions options;
    public void setList(List<VideoPojo> list)
    {
        mVideoList = list;
    }
    public VideoAdapter(Context mContext)
    {
        super(mContext);
        loader = ImageLoader.getInstance();
		loader.init(ImageLoaderConfiguration.createDefault(mContext));
    }

    @Override
    public int getCount() {
        return mVideoList.size();
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
            convertView = View.inflate(mContext, R.layout.video_catalog_item_layout, null);
            viewHolder.videoThumbnail = (ImageView) convertView.findViewById(R.id.video_thumbnail);
            viewHolder.title = (TextView) convertView.findViewById(R.id.video_title);
            viewHolder.playCount = (TextView) convertView.findViewById(R.id.video_paly_count);
            convertView.setTag(viewHolder);
        }

        viewHolder.title.setText(mVideoList.get(position).getTitle());
        loader.displayImage(mVideoList.get(position).getThumbnail(), viewHolder.videoThumbnail,
				options, new SimpleImageLoadingListener());
        viewHolder.playCount.setText("播放次数:"+mVideoList.get(position).getView_count());
        viewHolder.videoThumbnail.setImageURI(Uri.parse(mVideoList.get(position).getThumbnail()));
        return convertView;
    }
    
    static class ViewHolder
    {
        ImageView videoThumbnail;
        TextView title;
        TextView playCount;
    }
}
