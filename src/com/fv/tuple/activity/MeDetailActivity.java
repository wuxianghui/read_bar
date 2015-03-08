package com.fv.tuple.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;
import com.fv.tuple.content_provider.ContentProviderUtil;
import com.fv.tuple.content_provider.TupleContentProvider;
import com.fv.tuple.content_provider.TupleContentProvider.TMe;
import com.fv.tuple.content_provider.TupleContentProvider.TUserStudy;
import com.fv.tuple.content_provider.TupleContentProvider.TUserTech;
import com.fv.tuple.open.OpenAPI;
import com.fv.tuple.open.OpenAPIUtil;
import com.fv.tuple.open.OpenAPIUtil.ResponseMessageForMe;
import com.fv.tuple.util.HttpGetAsyncTask;
import com.fv.tuple.util.HttpGetAsyncTask.CallInterface;
import com.fv.tuple.util.HttpInterface;
import com.fv.tuple.util.Util;

/**
 * This activity need do some initialize working when login success, get the
 * basic info from server, contains: 1. get the user info and insert into db. 2.
 * image server url. 3. video server url. 4. message server url.
 */
public class MeDetailActivity extends Activity implements CallInterface{
	/** Called when the activity is first created. */
	ExpandableListView mListTL = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_me);

		initUI();
		getBasicDBData();
		getTeachData() ;
		getStudyData() ;
		initObserver();
	}

	/*
	 * void initUI() {
	 * mListTL=(ExpandableListView)findViewById(R.id.id_explv_teach_learn);
	 * mUTListAdapter=new UserTeachLearnExpandableListAdapter();
	 * mListTL.setAdapter(mUTListAdapter); }
	 */
    void startSettingBasic()
    {
    	
        Intent intentv = new Intent(this, SettingBasicActivity.class); 
	    intentv.putExtra("type","edit"); 
	    intentv.putExtra("class","MeActivity"); 
	    intentv.putExtra("user_id", TupleApplication.mMeInfoUserId); 
	    startActivityForResult(intentv, SETTING_BASIC_REQUEST_CODE);

    }
    void startSettingPhoto()
    {
   	 	Intent intentv = new Intent(this, SettingPhotoActivity.class);  
   	 	startActivityForResult(intentv, SETTING_PHOTO_REQUEST_CODE);
    }
	void startSettingSignature()
	{
		  Intent intentv = new Intent(this, SettingSignatureActivity.class); 
	  	    intentv.putExtra("type","edit"); 
	  	    intentv.putExtra("class","RegisterActivity"); 
	  	    intentv.putExtra("user_id", TupleApplication.mMeInfoUserId); 
	  	    startActivityForResult(intentv, SETTING_SIGNATURE_REQUEST_CODE);
	}
	void initUI() {
		
		Button	btns=null;
		btns = (Button) findViewById(R.id.id_b_add_teach);	
		btns.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startUserTechEditActivity("add","-1");
			}
		});
		
		btns = (Button) findViewById(R.id.id_b_add_study);	
		btns.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startUserStudyEditActivity("add","-1");
			}
		});
		
		
		btns = (Button) findViewById(R.id.id_b_set_signature);	
		btns.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startSettingSignature();
			}
		});
		
		btns = (Button) findViewById(R.id.id_b_set_basic);	
		btns.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startSettingBasic();
			}
		});
		
		ImageView im = (ImageView) findViewById(R.id.id_img_portray);	
		im.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startSettingPhoto();
			}
		});
		
		/*
		for (int i = 0; i < 5; i++) {
			final LinearLayout ll = (LinearLayout) findViewById(R.id.id_ll_teach);
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View inflateLayout = inflater.inflate(
					R.layout.item_list_user_teach_child, null);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 0;
			params.topMargin = 0;

			inflateLayout.setLayoutParams(params);
			inflateLayout.setId(ssid++);
			inflateLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Util.showToast("" + v.getId());
				}
			});

			ll.addView(inflateLayout);

			Button b = (Button) findViewById(R.id.id_b_i_can_teach);

			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub'
					if (ll.getVisibility() == View.VISIBLE)
						ll.setVisibility(View.GONE);
					else
						ll.setVisibility(View.VISIBLE);
					Util.showToast("" + v.getId());
				}
			});

		}

		for (int i = 0; i < 5; i++) {
			final LinearLayout ll = (LinearLayout) findViewById(R.id.id_ll_learn);
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View inflateLayout = inflater.inflate(
					R.layout.item_list_user_learn_child, null);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 0;
			params.topMargin = 0;

			inflateLayout.setLayoutParams(params);
			inflateLayout.setId(ssid++);
			inflateLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Util.showToast("" + v.getId());
				}
			});

			ll.addView(inflateLayout);

			Button b = (Button) findViewById(R.id.id_b_i_wanna_learn);

			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub'
					if (ll.getVisibility() == View.VISIBLE)
						ll.setVisibility(View.GONE);
					else
						ll.setVisibility(View.VISIBLE);
					Util.showToast("" + v.getId());
				}
			});
		}
		*/
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


	String startUpload(String url)
	{

		List<NameValuePair> nameValuePair= new ArrayList<NameValuePair>();
		BasicNameValuePair on=new BasicNameValuePair("user_id",""+TupleApplication.mMeInfoUserId);
		nameValuePair.add(on);

		String res=HttpInterface.excutePost(url,nameValuePair);
		
		return res;
	}
	void startGetData()
	{
		HttpGetAsyncTask t=new HttpGetAsyncTask(this,this,null,R.string.app_name,R.string.app_name,null);
		String[] params = { OpenAPI.getInstance().getMeUrl(), null ,null};
		t.execute(params);
	}
	

	private static final int USER_TECH_EDIT = 1;
	private static final int USER_STUDY_EDIT = 2;
	private static final int SETTING_SIGNATURE_REQUEST_CODE = 3;
	private static final int SETTING_PHOTO_REQUEST_CODE = 4;
	private static final int SETTING_BASIC_REQUEST_CODE = 5;
	private static final int SETTING_PASSWORD_REQUEST_CODE = 6;
	void startUserTechEditActivity(String type,String userTechID)
	{
	    Intent intentv = new Intent(this, UserTechEditActivity.class); 
	    intentv.putExtra("type", type); 
	    intentv.putExtra("user_tech_id", userTechID); 
	     	startActivityForResult(intentv, USER_TECH_EDIT);
	}
	
	void startUserStudyEditActivity(String type,String userStudyID)
	{
	    Intent intentv = new Intent(this, UserStudyEditActivity.class); 
	    intentv.putExtra("type", type); 
	    intentv.putExtra("user_study_id", userStudyID); 
	     	startActivityForResult(intentv, USER_STUDY_EDIT);
	}
	  
	  
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {

			switch (requestCode) {
			case USER_TECH_EDIT:
				//startGetData();
				getTeachData();
					break;
			case USER_STUDY_EDIT:
			//startGetData();
				getStudyData();
			break;
			case SETTING_SIGNATURE_REQUEST_CODE:
				getBasicDBData();
				break;
			case SETTING_BASIC_REQUEST_CODE:
				getBasicDBData();
				break;

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	ArrayList mAllTechView=new ArrayList();
	ArrayList mAllStudyView=new ArrayList();
	void clearAllTechView()
	{
		LinearLayout ll = (LinearLayout) findViewById(R.id.id_ll_teach);
		ll.removeAllViews();
		mAllTechView.clear();
	}
	void clearAllStudyView()
	{
		LinearLayout ll = (LinearLayout) findViewById(R.id.id_ll_learn);
		ll.removeAllViews();
		mAllStudyView.clear();
	}
	void addTechToView(int userTechId,int techId,int userId,String techName,String comment, String address,int pay,int period)
	{
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.id_ll_teach);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View inflateLayout = inflater.inflate(
				R.layout.item_list_user_teach_child, null);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = 0;
		params.topMargin = 0;

		inflateLayout.setLayoutParams(params);
		inflateLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startUserTechEditActivity("edit",(String)v.getTag());
				Util.showToast("" + v.getId());
			}
		});
		
		TextView vv=null;
		vv=(TextView)inflateLayout.findViewById(R.id.item_name);
		vv.setText(techName);
		
		vv=(TextView)inflateLayout.findViewById(R.id.item_comment);
		vv.setText(comment);
		
		inflateLayout.setTag(""+userTechId);
		ll.addView(inflateLayout);
		mAllTechView.add(inflateLayout);

	}

	void addStudyToView(int user_study_id,int techId,int userId,String studyName,String comment, String address,String publishDate)
	{
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.id_ll_learn);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View inflateLayout = inflater.inflate(
				R.layout.item_list_user_learn_child, null);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = 0;
		params.topMargin = 0;

		inflateLayout.setLayoutParams(params);
		inflateLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.getTag();
				Util.showToast("" + v.getId());
				startUserStudyEditActivity("edit",(String)v.getTag());
			}
		});
		
		TextView vv=null;
		vv=(TextView)inflateLayout.findViewById(R.id.item_name);
		vv.setText(studyName);
		
		vv=(TextView)inflateLayout.findViewById(R.id.item_comment);
		vv.setText(comment);
		inflateLayout.setTag(""+user_study_id);
		
		ll.addView(inflateLayout);
		mAllStudyView.add(inflateLayout);

	}
	void getBasicDBData() {
		
		Cursor cursor = getContentResolver()
		.query(TupleContentProvider.TMe.CONTENT_URI,
				null,
				null,
				null,
				null);

		if(cursor.getCount()<1)
			return;

		cursor.moveToFirst();
		String name=cursor.getString(cursor.getColumnIndex(TMe.COLUMN_NAME));
		String email=cursor.getString(cursor.getColumnIndex(TMe.COLUMN_EMAIL));
		int age=cursor.getInt(cursor.getColumnIndex(TMe.COLUMN_AGE));
		String sex=cursor.getString(cursor.getColumnIndex(TMe.COLUMN_SEX));
		String address=cursor.getString(cursor.getColumnIndex(TMe.COLUMN_ADDRESS));
		String job=cursor.getString(cursor.getColumnIndex(TMe.COLUMN_JOB));
		String school=cursor.getString(cursor.getColumnIndex(TMe.COLUMN_SCHOOL));
		String comments=cursor.getString(cursor.getColumnIndex(TMe.COLUMN_COMMENTS));
		String signature=cursor.getString(cursor.getColumnIndex(TMe.COLUMN_SIGNATURE));
		int is_authenticated=cursor.getInt(cursor.getColumnIndex(TMe.COLUMN_IS_AUTHENTICATED));
		int state=cursor.getInt(cursor.getColumnIndex(TMe.COLUMN_STATE));
		double latitude=cursor.getDouble(cursor.getColumnIndex(TMe.COLUMN_LATITUDE));
		double longitude=cursor.getDouble(cursor.getColumnIndex(TMe.COLUMN_LONGITUDE));
		String photo=cursor.getString(cursor.getColumnIndex(TMe.COLUMN_PHOTO));
		int photo_status=cursor.getInt(cursor.getColumnIndex(TMe.COLUMN_PHOTO_STATUS));
		
		TextView tv=null;
		
		tv=(TextView)findViewById(R.id.id_tv_name);
		tv.setText(name);
		
		tv=(TextView)findViewById(R.id.id_tv_age);
		tv.setText(""+age);
		
		tv=(TextView)findViewById(R.id.id_tv_sex);
		tv.setText(sex);
		
		tv=(TextView)findViewById(R.id.id_tv_address);
		tv.setText(address);

		tv=(TextView)findViewById(R.id.id_tv_job);
		tv.setText(job);
		
		tv=(TextView)findViewById(R.id.id_tv_school);
		tv.setText(school);
		
		tv=(TextView)findViewById(R.id.id_tv_comment);
		tv.setText(comments);
		
		tv=(TextView)findViewById(R.id.id_tv_signature);
		tv.setText(signature);
		
		tv=(TextView)findViewById(R.id.id_tv_is_authenticated);
		tv.setText(""+is_authenticated);

		
		
	}
	
	void getTeachData() {

		String where=TupleContentProvider.TUserTech.COLUMN_USER_ID+"="+TupleApplication.mMeInfoUserId;
		
		Cursor cursor = getContentResolver()
		.query(TupleContentProvider.TUserTech.CONTENT_URI,
				null,
				where,
				null,
				null);

		
		cursor.moveToFirst();
		if(cursor.getCount()<1)
		{
			cursor.close();
			cursor= null;
			return;
		}
		
		clearAllTechView();
		for (int i = 0; i < cursor.getCount(); i++) {
			int user_tech_id=cursor.getInt(cursor.getColumnIndex(TUserTech.COLUMN_USER_TECH_ID));
			int tech_id=cursor.getInt(cursor.getColumnIndex(TUserTech.COLUMN_TECH_ID));
			int user_id=cursor.getInt(cursor.getColumnIndex(TUserTech.COLUMN_USER_ID));
			String tech_name=cursor.getString(cursor.getColumnIndex(TUserTech.COLUMN_TECH_NAME));
			String comment=cursor.getString(cursor.getColumnIndex(TUserTech.COLUMN_COMMENT));
			String address=cursor.getString(cursor.getColumnIndex(TUserTech.COLUMN_ADDRESS));
			int pay=cursor.getInt(cursor.getColumnIndex(TUserTech.COLUMN_PAY));
			int period=cursor.getInt(cursor.getColumnIndex(TUserTech.COLUMN_PERIOD));
			addTechToView(user_tech_id,tech_id,user_id,tech_name,comment, address,pay,period);
			cursor.moveToNext();
		}
		if(cursor!=null)
		{
			cursor.close();
			cursor= null;
		}
	}
	
	void getStudyData() {

		String where=TupleContentProvider.TUserStudy.COLUMN_USER_ID+"="+TupleApplication.mMeInfoUserId;
		
		Cursor cursor = getContentResolver()
		.query(TupleContentProvider.TUserStudy.CONTENT_URI,
				null,
				where,
				null,
				null);

		
		cursor.moveToFirst();
		if(cursor.getCount()<1)
		{
			cursor.close();
			cursor= null;
			return;
		}
		
		clearAllStudyView();
		for (int i = 0; i < cursor.getCount(); i++) {
	
			int user_study_id=cursor.getInt(cursor.getColumnIndex(TUserStudy.COLUMN_USER_STUDY_ID));
			int tech_id=cursor.getInt(cursor.getColumnIndex(TUserStudy.COLUMN_TECH_ID));
			int user_id=cursor.getInt(cursor.getColumnIndex(TUserStudy.COLUMN_USER_ID));
			String study_name=cursor.getString(cursor.getColumnIndex(TUserStudy.COLUMN_STUDY_NAME));
			String comment=cursor.getString(cursor.getColumnIndex(TUserStudy.COLUMN_COMMENT));
			String address=cursor.getString(cursor.getColumnIndex(TUserStudy.COLUMN_ADDRESS));
			String publish_date=	(String)cursor.getString(cursor.getColumnIndex(TUserStudy.COLUMN_PUBLISH_DATE));
			addStudyToView(user_study_id,tech_id,user_id,study_name,comment, address,publish_date);
			cursor.moveToNext();
		}
		if(cursor!=null)
		{
			cursor.close();
			cursor= null;
		}
	}

	boolean checkBeforePost()
	{
		return true;
	}
	
	@Override
	public String doing(String mainPath, String viaPath, String other,
			Object otherObject) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		return startUpload(mainPath);
	}

	@Override
	public boolean PreSyncTaskCall() {
		// TODO Auto-generated method stub
		return checkBeforePost();
	}

	@Override
	public void PostSyncTaskCall(String other, String res, Object otherObject) {
		// TODO Auto-generated method stub
		if(res!=null)
		{
			String result=null;
			String message=null;
			ResponseMessageForMe rm=null;
			
			rm=OpenAPIUtil.parserResponseForMe(res);
			result=rm.mResult;
			message=rm.mMessage;
			if(result.equalsIgnoreCase("success"))
			{
				ContentProviderUtil.insertMeJson(this,rm.mDataMe);
				ContentProviderUtil.insertUserTechJson(this,rm.mDataUserTech);
				ContentProviderUtil.insertUserStudyJson(this,rm.mDataUserStudy);

				Util.showToast(TupleApplication.getContext().getResources().getString(R.string.sucess_user_setting));
			}
			else if(result.equalsIgnoreCase("fail"))
			{
				Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_user_setting));
			}
			
		}

		return;
	}
	
	Obs obs = new Obs(new Handler());
	private void initObserver()
	{
		this.getContentResolver().registerContentObserver(TupleContentProvider.TMe.CONTENT_URI,
				true, obs);
		this.getContentResolver().registerContentObserver(TupleContentProvider.TUserTech.CONTENT_URI,
				true, obs);
		this.getContentResolver().registerContentObserver(TupleContentProvider.TUserStudy.CONTENT_URI,
				true, obs);
		//mAdapter.notifyDataSetChanged();
	}
	private void unInitObserver()
	{
		this.getContentResolver().unregisterContentObserver(obs);
		
	}
	private class Obs extends ContentObserver{
		public int count=0;
		public Obs(Handler handler) {
			super(handler);
		}
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			getBasicDBData();
			getTeachData() ;
			getStudyData() ;
			count++;
			Util.showToast("===========>"+count);
		}
		
	}
	
}
