package com.fv.tuple.open;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class OpenAPIUtil {
	static OpenAPIUtil mOpenAPI = null;
	private final static String URL_API_GETCATEGORY = "sdl.Cloudary_OpenApi.category.getlist" ;
	public static OpenAPIUtil getInstance() {
		if (mOpenAPI == null) {
			mOpenAPI = new OpenAPIUtil();
		}
		return mOpenAPI;
	}
	
	/**
	 * 获取设备登陆token
	 * @return
	 */
	public static String getAuthChallengeToken()
	{
		try {
			return "";
		} catch (Exception e) {   
//			Log.e("APIPool====>", e.getMessage());
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 解析返回的message
	 * @return ResponseMessage
	 */
	public static ResponseMessage parserResponse(String res)
	{
		JSONObject jsonObject=null;
		ResponseMessage rm=null;
		try {
			JSONArray jsonArray = new JSONArray(res);
			jsonObject = jsonArray.getJSONObject(0);
			rm=new ResponseMessage();
			rm.mResult=jsonObject.getString("result");
			rm.mMessage=jsonObject.getString("message");
			rm.mData=jsonObject.getJSONArray("data");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rm;
	}
	
	public static ResponseMessageForMe parserResponseForMe(String res)
	{
		if(res==null)
			return null;
		JSONObject jsonObject=null;
		ResponseMessageForMe rm=null;
		try {
			JSONArray jsonArray = new JSONArray(res);
			jsonObject = jsonArray.getJSONObject(0);
			rm=new ResponseMessageForMe();
			rm.mResult=jsonObject.getString("result");
			rm.mMessage=jsonObject.getString("message");
			rm.mDataMe=jsonObject.getJSONArray("data_me");
			rm.mDataUserTech=jsonObject.getJSONArray("data_user_tech");
			rm.mDataUserStudy=jsonObject.getJSONArray("data_user_study");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rm;
	}
	
	public static ResponseMessageForMeAndServer parserResponseForMeAndServer(String res)
	{
		if(res==null)
			return null;
		JSONObject jsonObject=null;
		ResponseMessageForMeAndServer rm=null;
		try {
			JSONArray jsonArray = new JSONArray(res);
			jsonObject = jsonArray.getJSONObject(0);
			rm=new ResponseMessageForMeAndServer();
			rm.mResult=jsonObject.getString("result");
			rm.mMessage=jsonObject.getString("message");
			rm.mDataMe=jsonObject.getJSONArray("data_me");
			rm.mDataUserTech=jsonObject.getJSONArray("data_user_tech");
			rm.mDataUserStudy=jsonObject.getJSONArray("data_user_study");
			rm.mServer=jsonObject.getJSONArray("server");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rm;
	}
	
	public static class ResponseMessage
	{
		public	String mMessage=null;
		public	String mResult=null;
		public	JSONArray mData=null;
	}
	
	public static class ResponseMessageForMe
	{
		public	String mMessage=null;
		public	String mResult=null;
		public	JSONArray mDataMe=null;
		public	JSONArray mDataUserTech=null;
		public	JSONArray mDataUserStudy=null;
	}
	
	public static class ResponseMessageForMeAndServer
	{
		public	String mMessage=null;
		public	String mResult=null;
		public	JSONArray mDataMe=null;
		public	JSONArray mDataUserTech=null;
		public	JSONArray mDataUserStudy=null;
		public	JSONArray mServer=null;
	}
}
