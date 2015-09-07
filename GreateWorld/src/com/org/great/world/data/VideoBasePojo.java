package com.org.great.world.data;

import java.util.ArrayList;

/**
 * Created by dj on 2015/9/7.
 * email:dingjun0225@gmail.com
 */
public class VideoBasePojo
{
    private int page;
    private int count;
    private int total;
    private ArrayList<VideoPojo> videos;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<VideoPojo> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<VideoPojo> videos) {
        this.videos = videos;
    }
}
