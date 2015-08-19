package com.org.great.world.pojo;

import java.io.Serializable;

public class PersonalInfoPojo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String accountId;
	public String loginName;
	public String password;
	
	public String nickName;
	public String sex;
	public String grade;
	public String photoPath;
	
	public PersonalInfoPojo() {
		
		setNickName("阿斗");
		setSex("0");
	}

	
	public String getPhotoPath() {
		return photoPath;
	}


	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}


	public String getGrade()
	{
		return grade;
	}
	
	public void setGrade(String grade)
	{
		this.grade = grade;
	}
	
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String userId) {
		this.accountId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}


	@Override
	public String toString() {
		return "PersonalInfoPojo [userId=" + accountId + ", loginName=" + loginName + ", password="
				+ password + ", nickName=" + nickName + ", sex=" + sex
				+ ",grade=" + grade + "]";
	}
	

}




