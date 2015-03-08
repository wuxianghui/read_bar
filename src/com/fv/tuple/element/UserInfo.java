package com.fv.tuple.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;



public class UserInfo {
	
	private static final String TAG = "USER_INFO";

	private static UserInfo mInstance = null;
	
	//get instance of CloudaryCache

  public String mEmail=null;
  public String mPassword=null;
  public String mName=null;
  public String mAge=null;
  public String mAddress=null;
  public String mJob=null;
  public String mShool=null;
  public String mComments=null;
  public String mSignature=null;
  public String mPhoto=null;
	
	public static UserInfo getInstance() {
		if (mInstance == null) {
			mInstance = new UserInfo();
			return mInstance;
		} else {
			return mInstance;
		}
	}

	private UserInfo() {
			
	}
	
	public void setUserInfo(String Email,String Password,
			String Name,String Age,String Address,String Job,
			String Shool,String Comments,String Signature,String Photo)
	{
		mEmail=Email;
		mPassword=Password;
		mName=Name;
		mAge=Age;
		mAddress=Address;
		mJob=Job;
		mShool=Shool;
		mComments=Comments;
		mSignature=Signature;
		mPhoto=Photo;
	}

	public String getUserInfoEmail()
	{
		return mEmail;
	}
	public String getUserInfoPassword()
	{
		return mPassword;
	}
	public String getUserInfoName()
	{
		return mName;
	}
	public String getUserInfoAge()
	{
		return mAge;
	}
	public String getUserInfoAddress()
	{
		return mAddress;
	}
	public String getUserInfoJob()
	{
		return mJob;
	}
	public String getUserInfoShool()
	{
		return mShool;
	}
	public String getUserInfoComments()
	{
		return mComments;
	}
	public String getUserInfoSignature()
	{
		return mSignature;
	}
	public String getUserInfoPhoto()
	{
		return mPhoto;
	}
	public void setUserInfoEmail(String Email)
	{
		 mEmail=Email;
	}
	public void setUserInfoPassword(String Password)
	{
		 mPassword=Password;
	}
	public void setUserInfoName(String Name)
	{
		 mName=Name;
	}
	public void setUserInfoAge(String Age)
	{
		 mAge=Age;
	}
	public void setUserInfoAddress(String Address)
	{
		 mAddress=Address;
	}
	public void setUserInfoJob(String Job)
	{
		 mJob=Job;
	}
	public void setUserInfoShool(String Shool)
	{
		 mShool=Shool;
	}
	public void setUserInfoComments(String Comments)
	{
		 mComments=Comments;
	}
	public void setUserInfoSignature(String Signature)
	{
		 mSignature=Signature;
	}
	public void setUserInfoPhoto(String Photo)
	{
		 mPhoto=Photo;
	}
public List<NameValuePair> getNameValuePair()
{
	if((mEmail==null&&(mEmail.compareToIgnoreCase("")==0))||
			(mPassword==null&&(mPassword.compareToIgnoreCase("")==0)))
	{
		
	}
	List<NameValuePair> nameValuePair= new ArrayList<NameValuePair>();
	BasicNameValuePair on=new BasicNameValuePair("test","1111");
	nameValuePair.add(on);
	return null;
}
}
