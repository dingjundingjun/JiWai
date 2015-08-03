package com.org.great.world.data;

import java.io.Serializable;

public class CatalogPojo implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String articleId;
	private String title;
	private String url;
	private String publish;
	private String articleType;
	
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublish() {
		return publish;
	}
	public void setPublish(String publish) {
		this.publish = publish;
	}
	public String getArticleType() {
		return articleType;
	}
	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	
}
