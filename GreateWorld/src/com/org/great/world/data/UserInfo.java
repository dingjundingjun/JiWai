package com.org.great.world.data;

import java.io.Serializable;

import com.org.great.world.Utils.Debug;

public class UserInfo implements Serializable
{
	private int accountId;
	private String loginName;
	private String nickName;
	private String photoPath;
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	@Override
	public String toString() {
		Debug.d("accountId:" + accountId + " loginName:" + loginName + " nickName:" + nickName + " photoPath:" + photoPath);
		return super.toString();
	}
	
}
