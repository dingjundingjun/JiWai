package com.org.great.world.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2015/8/27.
 */
public class GamePojo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int cardId;  // 卡片的id
    private int type;  // 卡片类型，暂定游戏为1，其他分类待定；
    private String title;
    private String describe;
    private String image;  // 封面图
    private int imageCount; // 如果>0，则说明该文章里有多个图
    private String url; // 文章地址
    private int readCount;
    private int commentCount;
    private int likeCount;
    private String publishTime;  // 发布时间

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}
