package com.fv.tuple.content_provider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.fv.tuple.TupleApplication;
import com.fv.tuple.open.OpenAPI;

public class ContentProviderUtil {
	public static  String insertUserJsonAndServer(Context context,JSONArray res)
	{
		insertMeJson(context, res);
		try {
			setServerIpJson(res.getJSONArray(1));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";
	}
	
	public static  String insertJsonServer(Context context,JSONArray res)
	{
		setServerIpJson(res);
		return "success";
	}
	public static String  setServerIpJson(JSONArray arr)
	{
		JSONObject res=null;
		try {
			res = arr.getJSONObject(0);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			OpenAPI.getInstance().setImageServerUrl(res.getString(OpenAPI.FIELD_IMAGE_SERVER_IP));
			OpenAPI.getInstance().setVideoServerUrl(res.getString(OpenAPI.FIELD_VIDEO_SERVER_IP));
			OpenAPI.getInstance().setMessageServerUrl(res.getString(OpenAPI.FIELD_MESSAGE_SERVER_IP));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";
	}
	public static  String insertMeJson(Context context,JSONArray res)
	{
		if(res==null)
			return null;
	      JSONArray jsonArray;
			context.getContentResolver().delete(TupleContentProvider.TMe.CONTENT_URI, null,null);
			 jsonArray = res;
			 Log.d("==================","insert begin");
			 Log.d("==================","insert count"+jsonArray.length());
			 
			 
			 SQLiteDatabase db = TupleContentProvider.getWritableDB();
			 
			 db.beginTransaction();  
			 try{  

			    JSONObject jsonObject = jsonArray.getJSONObject(0);
			    		               
			    ContentValues values = new ContentValues();
			    values.put(TupleContentProvider.TMe.COLUMN_USER_ID, jsonObject.getInt(TupleContentProvider.TMe.COLUMN_USER_ID));
			    values.put(TupleContentProvider.TMe.COLUMN_EMAIL, jsonObject.getString(TupleContentProvider.TMe.COLUMN_EMAIL));
			    values.put(TupleContentProvider.TMe.COLUMN_PASSWORD, jsonObject.getString(TupleContentProvider.TMe.COLUMN_PASSWORD));
			    values.put(TupleContentProvider.TMe.COLUMN_NAME, jsonObject.getString(TupleContentProvider.TMe.COLUMN_NAME));
			    values.put(TupleContentProvider.TMe.COLUMN_SEX, jsonObject.getString(TupleContentProvider.TMe.COLUMN_SEX));
			    values.put(TupleContentProvider.TMe.COLUMN_AGE, jsonObject.getInt(TupleContentProvider.TMe.COLUMN_AGE));
			    values.put(TupleContentProvider.TMe.COLUMN_ADDRESS, jsonObject.getString(TupleContentProvider.TMe.COLUMN_ADDRESS));
			    values.put(TupleContentProvider.TMe.COLUMN_JOB, jsonObject.getString(TupleContentProvider.TMe.COLUMN_JOB));
			    values.put(TupleContentProvider.TMe.COLUMN_SCHOOL, jsonObject.getString(TupleContentProvider.TMe.COLUMN_SCHOOL));
			    values.put(TupleContentProvider.TMe.COLUMN_COMMENTS, jsonObject.getString(TupleContentProvider.TMe.COLUMN_COMMENTS));
			    values.put(TupleContentProvider.TMe.COLUMN_SIGNATURE, jsonObject.getString(TupleContentProvider.TMe.COLUMN_SIGNATURE));
			    values.put(TupleContentProvider.TMe.COLUMN_IS_AUTHENTICATED, jsonObject.getInt(TupleContentProvider.TMe.COLUMN_IS_AUTHENTICATED));
			    values.put(TupleContentProvider.TMe.COLUMN_STATE, jsonObject.getInt(TupleContentProvider.TMe.COLUMN_STATE));
			    values.put(TupleContentProvider.TMe.COLUMN_LATITUDE, jsonObject.getDouble(TupleContentProvider.TMe.COLUMN_LATITUDE));
			    values.put(TupleContentProvider.TMe.COLUMN_LONGITUDE, jsonObject.getDouble(TupleContentProvider.TMe.COLUMN_LONGITUDE));
			    values.put(TupleContentProvider.TMe.COLUMN_PHOTO, jsonObject.getString(TupleContentProvider.TMe.COLUMN_PHOTO));
			    values.put(TupleContentProvider.TUser.COLUMN_PHOTO_STATUS, 0);
			    
			    context.getContentResolver().insert(TupleContentProvider.TMe.CONTENT_URI, values);

			    TupleApplication.mMeInfoName=jsonObject.getString(TupleContentProvider.TMe.COLUMN_NAME);
			    TupleApplication.mMeInfoUserId=jsonObject.getInt(TupleContentProvider.TMe.COLUMN_USER_ID);

			    
			//������������������������  
			   //������������������������  
			   db.setTransactionSuccessful();  
			   //��setTransactionSuccessful��endTransaction������������������������  
			   }catch(Exception e){  
			     //����������������������������������������������  
			     db.endTransaction();  
			      
			   }  
			   //��������������������������������  
			   db.endTransaction();  

			Log.d("==================","insert done");
			return "success";
	}
	public static  void deleteAllUserTech(Context context)
	{
		context.getContentResolver().delete(TupleContentProvider.TUserTech.CONTENT_URI, null,null);
	}
	public static  String insertUserTechJson(Context context,JSONArray res)
	{
		if(res==null)
			return null;
	      JSONArray jsonArray;
			//context.getContentResolver().delete(TupleContentProvider.TUserTech.CONTENT_URI, null,null);
			 jsonArray = res;
			 Log.d("==================","insert begin");
			 Log.d("==================","insert count"+jsonArray.length());
			 
			 
			 SQLiteDatabase db = TupleContentProvider.getWritableDB();
			 
			 db.beginTransaction();  
			 try{  

				 for (int i = 0; i < jsonArray.length(); ++i) {
					    JSONObject jsonObject = jsonArray.getJSONObject(i);

			    ContentValues values = new ContentValues();
			    values.put(TupleContentProvider.TUserTech.COLUMN_USER_TECH_ID, jsonObject.getInt(TupleContentProvider.TUserTech.COLUMN_USER_TECH_ID));
			    values.put(TupleContentProvider.TUserTech.COLUMN_USER_ID, jsonObject.getInt(TupleContentProvider.TUserTech.COLUMN_USER_ID));
			    values.put(TupleContentProvider.TUserTech.COLUMN_TECH_ID, jsonObject.getInt(TupleContentProvider.TUserTech.COLUMN_TECH_ID));
			    values.put(TupleContentProvider.TUserTech.COLUMN_TECH_NAME, jsonObject.getString(TupleContentProvider.TUserTech.COLUMN_TECH_NAME));
			    values.put(TupleContentProvider.TUserTech.COLUMN_COMMENT, jsonObject.getString(TupleContentProvider.TUserTech.COLUMN_COMMENT));
			    values.put(TupleContentProvider.TUserTech.COLUMN_ADDRESS, jsonObject.getString(TupleContentProvider.TUserTech.COLUMN_ADDRESS));
			    values.put(TupleContentProvider.TUserTech.COLUMN_PAY, jsonObject.getInt(TupleContentProvider.TUserTech.COLUMN_PAY));
			    values.put(TupleContentProvider.TUserTech.COLUMN_PERIOD, jsonObject.getInt(TupleContentProvider.TUserTech.COLUMN_PERIOD));
			    
			    context.getContentResolver().insert(TupleContentProvider.TUserTech.CONTENT_URI, values);

			//������������������������  
			   //������������������������  
				 }
			   db.setTransactionSuccessful();  
			   //��setTransactionSuccessful��endTransaction������������������������  
			   }catch(Exception e){  
			     //����������������������������������������������  
			     db.endTransaction();  
			      
			   }  
			   //��������������������������������  
			   db.endTransaction();  

			Log.d("==================","insert done");
			return "success";
	}
	
	public static  void deleteAllUserStudy(Context context)
	{
		context.getContentResolver().delete(TupleContentProvider.TUserStudy.CONTENT_URI, null,null);
	}
	public static  String insertUserStudyJson(Context context,JSONArray res)
	{
		if(res==null)
			return null;
	      JSONArray jsonArray;
		//	context.getContentResolver().delete(TupleContentProvider.TUserStudy.CONTENT_URI, null,null);
			 jsonArray = res;
			 if(jsonArray.length()<1)
				 return null;
			 Log.d("==================","insert begin");
			 Log.d("==================","insert count"+jsonArray.length());
			 
			 
			 SQLiteDatabase db = TupleContentProvider.getWritableDB();
			 
			 db.beginTransaction();  
			 try{  
				 for (int i = 0; i < jsonArray.length(); ++i) {
					    JSONObject jsonObject = jsonArray.getJSONObject(i);


			    ContentValues values = new ContentValues();
			    values.put(TupleContentProvider.TUserStudy.COLUMN_USER_STUDY_ID, jsonObject.getInt(TupleContentProvider.TUserStudy.COLUMN_USER_STUDY_ID));
			    values.put(TupleContentProvider.TUserStudy.COLUMN_USER_ID, jsonObject.getInt(TupleContentProvider.TUserStudy.COLUMN_USER_ID));
			    values.put(TupleContentProvider.TUserStudy.COLUMN_TECH_ID, jsonObject.getInt(TupleContentProvider.TUserStudy.COLUMN_TECH_ID));
			    values.put(TupleContentProvider.TUserStudy.COLUMN_STUDY_NAME, jsonObject.getString(TupleContentProvider.TUserStudy.COLUMN_STUDY_NAME));
			    values.put(TupleContentProvider.TUserStudy.COLUMN_COMMENT, jsonObject.getString(TupleContentProvider.TUserStudy.COLUMN_COMMENT));
			    values.put(TupleContentProvider.TUserStudy.COLUMN_ADDRESS, jsonObject.getString(TupleContentProvider.TUserStudy.COLUMN_ADDRESS));
			    values.put(TupleContentProvider.TUserStudy.COLUMN_PUBLISH_DATE, jsonObject.getInt(TupleContentProvider.TUserStudy.COLUMN_PUBLISH_DATE));
			    
			    context.getContentResolver().insert(TupleContentProvider.TUserStudy.CONTENT_URI, values);
				 }
			//������������������������  
			   //������������������������  
			   db.setTransactionSuccessful();  
			   //��setTransactionSuccessful��endTransaction������������������������  
			   }catch(Exception e){  
			     //����������������������������������������������  
			     db.endTransaction();  
			      
			   }  
			   //��������������������������������  
			   db.endTransaction();  

			Log.d("==================","insert done");
			return "success";
	}
	
	
	public static  String insertSimpleUsersJson(Context context,JSONArray res)
	{
		if(res==null)
			return null;
	      JSONArray jsonArray;
			context.getContentResolver().delete(TupleContentProvider.TUser.CONTENT_URI, null,null);
			 jsonArray = res;
			 Log.d("==================","insert begin");
			 Log.d("==================","insert count"+jsonArray.length());
			 SQLiteDatabase db = TupleContentProvider.getWritableDB();
			 db.beginTransaction();  
		try{  
			for (int i = 0; i < jsonArray.length(); ++i) {
			    JSONObject jsonObject = jsonArray.getJSONObject(i);
			    		               
			    ContentValues values = new ContentValues();
			    values.put(TupleContentProvider.TUser.COLUMN_USER_ID, jsonObject.getInt(TupleContentProvider.TUser.COLUMN_USER_ID));
			    values.put(TupleContentProvider.TUser.COLUMN_NAME, jsonObject.getString(TupleContentProvider.TUser.COLUMN_NAME));
			    values.put(TupleContentProvider.TUser.COLUMN_SIGNATURE, jsonObject.getString(TupleContentProvider.TUser.COLUMN_SIGNATURE));
			    values.put(TupleContentProvider.TUser.COLUMN_IS_AUTHENTICATED, jsonObject.getInt(TupleContentProvider.TUser.COLUMN_IS_AUTHENTICATED));
			    values.put(TupleContentProvider.TUser.COLUMN_STATE, jsonObject.getInt(TupleContentProvider.TUser.COLUMN_STATE));
			    values.put(TupleContentProvider.TUser.COLUMN_PHOTO, jsonObject.getString(TupleContentProvider.TUser.COLUMN_PHOTO));
			    values.put(TupleContentProvider.TUser.COLUMN_PHOTO_STATUS, 0);
			    
			    context.getContentResolver().insert(TupleContentProvider.TUser.CONTENT_URI, values);
			    
			}
			//������������������������  
			   //������������������������  
			   db.setTransactionSuccessful();  
			   //��setTransactionSuccessful��endTransaction������������������������  
			   }catch(Exception e){  
			     //����������������������������������������������  
			     db.endTransaction();  
			      
			   }  
			   //��������������������������������  
			   db.endTransaction();  

			Log.d("==================","insert done");
			return "success";
	}
	public static  String insertUsersJson(Context context,JSONArray res)
	{
		if(res==null)
			return null;
	      JSONArray jsonArray;
			context.getContentResolver().delete(TupleContentProvider.TUser.CONTENT_URI, null,null);
			 jsonArray = res;
			 Log.d("==================","insert begin");
			 Log.d("==================","insert count"+jsonArray.length());
			 SQLiteDatabase db = TupleContentProvider.getWritableDB();
			 db.beginTransaction();  
		try{  
			for (int i = 0; i < jsonArray.length(); ++i) {
			    JSONObject jsonObject = jsonArray.getJSONObject(i);
			    		               
			    ContentValues values = new ContentValues();
			    values.put(TupleContentProvider.TUser.COLUMN_USER_ID, jsonObject.getInt(TupleContentProvider.TUser.COLUMN_USER_ID));
			    values.put(TupleContentProvider.TUser.COLUMN_EMAIL, jsonObject.getString(TupleContentProvider.TUser.COLUMN_EMAIL));
			    values.put(TupleContentProvider.TUser.COLUMN_NAME, jsonObject.getString(TupleContentProvider.TUser.COLUMN_NAME));
			    values.put(TupleContentProvider.TUser.COLUMN_AGE, jsonObject.getInt(TupleContentProvider.TUser.COLUMN_AGE));
			    values.put(TupleContentProvider.TMe.COLUMN_SEX, jsonObject.getString(TupleContentProvider.TMe.COLUMN_SEX));
			    values.put(TupleContentProvider.TUser.COLUMN_ADDRESS, jsonObject.getString(TupleContentProvider.TUser.COLUMN_ADDRESS));
			    values.put(TupleContentProvider.TUser.COLUMN_JOB, jsonObject.getString(TupleContentProvider.TUser.COLUMN_JOB));
			    values.put(TupleContentProvider.TUser.COLUMN_SCHOOL, jsonObject.getString(TupleContentProvider.TUser.COLUMN_SCHOOL));
			    values.put(TupleContentProvider.TUser.COLUMN_COMMENTS, jsonObject.getString(TupleContentProvider.TUser.COLUMN_COMMENTS));
			    values.put(TupleContentProvider.TUser.COLUMN_SIGNATURE, jsonObject.getString(TupleContentProvider.TUser.COLUMN_SIGNATURE));
			    values.put(TupleContentProvider.TUser.COLUMN_IS_AUTHENTICATED, jsonObject.getInt(TupleContentProvider.TUser.COLUMN_IS_AUTHENTICATED));
			    values.put(TupleContentProvider.TUser.COLUMN_STATE, jsonObject.getInt(TupleContentProvider.TUser.COLUMN_STATE));
			    values.put(TupleContentProvider.TUser.COLUMN_LATITUDE, jsonObject.getDouble(TupleContentProvider.TUser.COLUMN_LATITUDE));
			    values.put(TupleContentProvider.TUser.COLUMN_LONGITUDE, jsonObject.getDouble(TupleContentProvider.TUser.COLUMN_LONGITUDE));
			    values.put(TupleContentProvider.TUser.COLUMN_PHOTO, jsonObject.getString(TupleContentProvider.TUser.COLUMN_PHOTO));
			    values.put(TupleContentProvider.TUser.COLUMN_PHOTO_STATUS, 0);
			    
			    context.getContentResolver().insert(TupleContentProvider.TUser.CONTENT_URI, values);
			    
			}
			//������������������������  
			   //������������������������  
			   db.setTransactionSuccessful();  
			   //��setTransactionSuccessful��endTransaction������������������������  
			   }catch(Exception e){  
			     //����������������������������������������������  
			     db.endTransaction();  
			      
			   }  
			   //��������������������������������  
			   db.endTransaction();  

			Log.d("==================","insert done");
			return "success";
	}
	
	
	public static  String insertTechJson(Context context,JSONArray res)
	{
		if(res==null)
			return null;
	      JSONArray jsonArray;
			context.getContentResolver().delete(TupleContentProvider.TTech.CONTENT_URI, null,null);
			 jsonArray = res;
			 Log.d("==================","insert begin");
			 Log.d("==================","insert count"+jsonArray.length());
			 SQLiteDatabase db = TupleContentProvider.getWritableDB();
			 db.beginTransaction();  
		try{  
			for (int i = 0; i < jsonArray.length(); ++i) {
			    JSONObject jsonObject = jsonArray.getJSONObject(i);
			    		               
			    ContentValues values = new ContentValues();
			    values.put(TupleContentProvider.TTech.COLUMN_PARENT_TECH_ID, jsonObject.getInt(TupleContentProvider.TTech.COLUMN_PARENT_TECH_ID));
			    values.put(TupleContentProvider.TTech.COLUMN_TECH, jsonObject.getString(TupleContentProvider.TTech.COLUMN_TECH));
			    values.put(TupleContentProvider.TTech.COLUMN_TECH_ID, jsonObject.getInt(TupleContentProvider.TTech.COLUMN_TECH_ID));
			    values.put(TupleContentProvider.TTech.COLUMN_PIC, jsonObject.getString(TupleContentProvider.TTech.COLUMN_PIC));
			    values.put(TupleContentProvider.TTech.COLUMN_COMMENT, jsonObject.getString(TupleContentProvider.TTech.COLUMN_COMMENT));
			    values.put(TupleContentProvider.TTech.COLUMN_PIC_STATUS, 0);
			    
			    context.getContentResolver().insert(TupleContentProvider.TTech.CONTENT_URI, values);
			    
			}
			//������������������������  
			   //������������������������  
			   db.setTransactionSuccessful();  
			   //��setTransactionSuccessful��endTransaction������������������������  
			   }catch(Exception e){  
			     //����������������������������������������������  
			     db.endTransaction();  
			      
			   }  
			   //��������������������������������  
			   db.endTransaction();  

			Log.d("==================","insert done");
			return "success";
	}
	
	
	
	public static boolean updateDownPictureStatus(Uri uri,String column,String where,int statu)
	{
		Cursor qcursor = TupleApplication.getContext().getContentResolver()
		.query(uri,
				null,
				where,
				null,
				null);
		if(qcursor.getCount()==0)
			return false;
		if (qcursor.moveToFirst() == false)
			return false;
		
		int status=qcursor.getInt(qcursor.getColumnIndex(column));
		if(status==statu)
		return false;

		ContentValues values = new ContentValues();
		values.put(column, statu);
		TupleApplication.getContext().getContentResolver().update(
				uri, values, 
				where, null);
		
		return true;
	}
	
	
	
	
	
	
	
	

	

	
	public static  String insertStudyRequestInfoJson(Context context,JSONArray res)
	{
		if(res==null)
			return null;
	    JSONArray jsonArray;
		context.getContentResolver().delete(TupleContentProvider.TStudyRequestInfo.CONTENT_URI, null,null);
		jsonArray = res;
		Log.d("==================","insert begin");
		Log.d("==================","insert count"+jsonArray.length());
		SQLiteDatabase db = TupleContentProvider.getWritableDB();
		db.beginTransaction();  
		try{  
			for (int i = 0; i < jsonArray.length(); ++i) {
			    JSONObject jsonObject = jsonArray.getJSONObject(i);
			    		               
			    ContentValues values = new ContentValues();
			    values.put(TupleContentProvider.TStudyRequestInfo.COLUMN_STUDY_REQUEST_INFO_ID, jsonObject.getInt(TupleContentProvider.TStudyRequestInfo.COLUMN_STUDY_REQUEST_INFO_ID));
			    values.put(TupleContentProvider.TStudyRequestInfo.COLUMN_USER_ID, jsonObject.getInt(TupleContentProvider.TStudyRequestInfo.COLUMN_USER_ID));
			    values.put(TupleContentProvider.TStudyRequestInfo.COLUMN_USER_NAME, jsonObject.getString(TupleContentProvider.TStudyRequestInfo.COLUMN_USER_NAME));
			    values.put(TupleContentProvider.TStudyRequestInfo.COLUMN_USER_TECH_ID, jsonObject.getInt(TupleContentProvider.TStudyRequestInfo.COLUMN_USER_TECH_ID));
			    values.put(TupleContentProvider.TStudyRequestInfo.COLUMN_USER_TECH_NAME, jsonObject.getString(TupleContentProvider.TStudyRequestInfo.COLUMN_USER_TECH_NAME));
			    values.put(TupleContentProvider.TStudyRequestInfo.COLUMN_USER_TECH_USER_ID, jsonObject.getInt(TupleContentProvider.TStudyRequestInfo.COLUMN_USER_TECH_USER_ID));
			    values.put(TupleContentProvider.TStudyRequestInfo.COLUMN_USER_TECH_USER_NAME, jsonObject.getString(TupleContentProvider.TStudyRequestInfo.COLUMN_USER_TECH_USER_NAME));

			    values.put(TupleContentProvider.TStudyRequestInfo.COLUMN_REQUEST_COMMENT, jsonObject.getString(TupleContentProvider.TStudyRequestInfo.COLUMN_REQUEST_COMMENT));
			    values.put(TupleContentProvider.TStudyRequestInfo.COLUMN_RESPONSE_COMMENT, jsonObject.getString(TupleContentProvider.TStudyRequestInfo.COLUMN_RESPONSE_COMMENT));
			    values.put(TupleContentProvider.TStudyRequestInfo.COLUMN_STATE, jsonObject.getInt(TupleContentProvider.TStudyRequestInfo.COLUMN_STATE));
			    values.put(TupleContentProvider.TStudyRequestInfo.COLUMN_STATE_CHANGE_DATE, jsonObject.getInt(TupleContentProvider.TStudyRequestInfo.COLUMN_STATE_CHANGE_DATE));
			    values.put(TupleContentProvider.TStudyRequestInfo.COLUMN_PAY, jsonObject.getInt(TupleContentProvider.TStudyRequestInfo.COLUMN_PAY));
			    values.put(TupleContentProvider.TStudyRequestInfo.COLUMN_ADDRESS, jsonObject.getString(TupleContentProvider.TStudyRequestInfo.COLUMN_ADDRESS));
			    values.put(TupleContentProvider.TStudyRequestInfo.COLUMN_TECH_DATE, jsonObject.getInt(TupleContentProvider.TStudyRequestInfo.COLUMN_TECH_DATE));
		
			    context.getContentResolver().insert(TupleContentProvider.TStudyRequestInfo.CONTENT_URI, values);
			    
			}
			//������������������������  
			   //������������������������  
			   db.setTransactionSuccessful();  
			   //��setTransactionSuccessful��endTransaction������������������������  
			   }catch(Exception e){  
			     //����������������������������������������������  
			     db.endTransaction();  
			      
			   }  
			   //��������������������������������  
			   db.endTransaction();  

			Log.d("==================","insert done");
			return "success";
	}

	
	
	public static  String insertTechAssess(Context context,JSONArray res)
	{
		if(res==null)
			return null;
	    JSONArray jsonArray;
		context.getContentResolver().delete(TupleContentProvider.TTechAssess.CONTENT_URI, null,null);
		jsonArray = res;
		Log.d("==================","insert begin");
		Log.d("==================","insert count"+jsonArray.length());
		SQLiteDatabase db = TupleContentProvider.getWritableDB();
		db.beginTransaction();  
		try{  
			for (int i = 0; i < jsonArray.length(); ++i) {
			    JSONObject jsonObject = jsonArray.getJSONObject(i);
			    		               
			    ContentValues values = new ContentValues();
			    values.put(TupleContentProvider.TTechAssess.COLUMN_TECH_ASSESS_ID, jsonObject.getInt(TupleContentProvider.TTechAssess.COLUMN_TECH_ASSESS_ID));
			    values.put(TupleContentProvider.TTechAssess.COLUMN_USER_ID, jsonObject.getInt(TupleContentProvider.TTechAssess.COLUMN_USER_ID));
			    values.put(TupleContentProvider.TTechAssess.COLUMN_USER_NAME, jsonObject.getString(TupleContentProvider.TTechAssess.COLUMN_USER_NAME));
			    values.put(TupleContentProvider.TTechAssess.COLUMN_USER_TECH_USER_ID, jsonObject.getInt(TupleContentProvider.TTechAssess.COLUMN_USER_TECH_USER_ID));
			    values.put(TupleContentProvider.TTechAssess.COLUMN_USER_TECH_USER_NAME, jsonObject.getString(TupleContentProvider.TTechAssess.COLUMN_USER_TECH_USER_NAME));
			    values.put(TupleContentProvider.TTechAssess.COLUMN_USER_TECH_ID, jsonObject.getInt(TupleContentProvider.TTechAssess.COLUMN_USER_TECH_ID));
			    values.put(TupleContentProvider.TTechAssess.COLUMN_USER_TECH_NAME, jsonObject.getString(TupleContentProvider.TTechAssess.COLUMN_USER_TECH_NAME));

			    values.put(TupleContentProvider.TTechAssess.COLUMN_STAR, jsonObject.getInt(TupleContentProvider.TTechAssess.COLUMN_STAR));
			    values.put(TupleContentProvider.TTechAssess.COLUMN_COMMENT, jsonObject.getString(TupleContentProvider.TTechAssess.COLUMN_COMMENT));
			    values.put(TupleContentProvider.TTechAssess.COLUMN_REAL_STUDY_DATE, jsonObject.getInt(TupleContentProvider.TTechAssess.COLUMN_REAL_STUDY_DATE));
			    values.put(TupleContentProvider.TTechAssess.COLUMN_REAL_STUDY_PERIOD, jsonObject.getInt(TupleContentProvider.TTechAssess.COLUMN_REAL_STUDY_PERIOD));
			    values.put(TupleContentProvider.TTechAssess.COLUMN_REAL_PAY, jsonObject.getInt(TupleContentProvider.TTechAssess.COLUMN_REAL_PAY));
		
			    context.getContentResolver().insert(TupleContentProvider.TTechAssess.CONTENT_URI, values);
			    
			}
			//������������������������  
			   //������������������������  
			   db.setTransactionSuccessful();  
			   //��setTransactionSuccessful��endTransaction������������������������  
			   }catch(Exception e){  
			     //����������������������������������������������  
			     db.endTransaction();  
			      
			   }  
			   //��������������������������������  
			   db.endTransaction();  

			Log.d("==================","insert done");
			return "success";
	}
	
	
}
