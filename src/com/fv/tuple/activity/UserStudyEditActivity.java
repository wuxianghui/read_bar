package com.fv.tuple.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;
import com.fv.tuple.content_provider.ContentProviderUtil;
import com.fv.tuple.content_provider.TupleContentProvider;
import com.fv.tuple.content_provider.TupleContentProvider.TUserStudy;
import com.fv.tuple.open.OpenAPI;
import com.fv.tuple.open.OpenAPIUtil;
import com.fv.tuple.open.OpenAPIUtil.ResponseMessage;
import com.fv.tuple.util.HttpGetAsyncTask;
import com.fv.tuple.util.HttpGetAsyncTask.CallInterface;
import com.fv.tuple.util.HttpInterface;
import com.fv.tuple.util.Util;
import com.fv.tuple.widget.TechSelectOperation;

public class UserStudyEditActivity extends Activity implements CallInterface {
	/** Called when the activity is first created. */
	private String mUserStudyId="";
	private String mType="";
	private Intent mIntent=null;
	
	private TechSelectOperation mTechSelectOperation=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_user_study_edit);
		mIntent=getIntent();
		mTechSelectOperation=new TechSelectOperation(this);

		initData();
		initUI();
	}
	

	
	void initUI()
	{

			mType=mIntent.getStringExtra(("type"));
			if(mType.compareToIgnoreCase("edit")!=0)
			{
				Button b=(Button)findViewById(R.id.id_b_submit);
				b.setVisibility(View.GONE);
				LinearLayout ll=(LinearLayout)findViewById(R.id.id_ll_study_class);
				ll.setVisibility(View.GONE);
				
				EditText et=(EditText)findViewById(R.id.id_e_name);
				et.setEnabled(false);
				et=(EditText)findViewById(R.id.id_e_comment);
				et.setEnabled(false);
				et=(EditText)findViewById(R.id.id_e_address);
				et.setEnabled(false);

				
			}
			

	}
	void initData()
	{
		mUserStudyId=mIntent.getStringExtra(("user_study_id"));
		mType=mIntent.getStringExtra(("type"));
		getUserStudyData();
		initSubmitButton();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	public void finishAndReturn()
	{
		Intent intent = new Intent(UserStudyEditActivity.this,MeDetailActivity.class);
		intent.putExtra("result","success");
		setResult(0,intent);
		this.finish();
	}
	
	public void initSubmitButton()
	{
		Button b=(Button)findViewById(R.id.id_b_submit);
		
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startGetData();// submit the edit data
				
			}
		});
	}
	public void initUIData(int userTechId,int techId,int userId,String techName,String comment,String address)
	{

		EditText vv = (EditText) findViewById(R.id.id_e_name);	
		vv.setText(techName);
		
		vv = (EditText) findViewById(R.id.id_e_address);	
		vv.setText(address);

		vv = (EditText) findViewById(R.id.id_e_comment);	
		vv.setText(comment);
		mTechSelectOperation.initTechInfo(""+techId);

	}
	public void getUserStudyData()
	{
		int tech_id=0;
			String where=TupleContentProvider.TUserStudy.COLUMN_USER_STUDY_ID+"="+mUserStudyId;
			
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

			for (int i = 0; i < cursor.getCount(); i++) {
				int user_study_id=cursor.getInt(cursor.getColumnIndex(TUserStudy.COLUMN_USER_STUDY_ID));
				tech_id=cursor.getInt(cursor.getColumnIndex(TUserStudy.COLUMN_TECH_ID));
				int user_id=cursor.getInt(cursor.getColumnIndex(TUserStudy.COLUMN_USER_ID));
				String study_name=cursor.getString(cursor.getColumnIndex(TUserStudy.COLUMN_STUDY_NAME));
				String comment=cursor.getString(cursor.getColumnIndex(TUserStudy.COLUMN_COMMENT));
				String address=cursor.getString(cursor.getColumnIndex(TUserStudy.COLUMN_ADDRESS));
				initUIData(user_study_id,tech_id,user_id,study_name,comment,address);
				cursor.moveToNext();
			}
			if(cursor!=null)
			{
				cursor.close();
				cursor= null;
			}
			mTechSelectOperation.initTech(""+tech_id,null);
	}
	
	void startGetData()
	{
		HttpGetAsyncTask t=new HttpGetAsyncTask(this,this,null,R.string.app_name,R.string.app_name,null);
		String[] params = { OpenAPI.getInstance().getUpdateUserStudyUrl(), null ,null};
		t.execute(params);
	}
	
	String startUpload(String url) {


		String studyName = ((EditText) findViewById(R.id.id_e_name))
				.getText().toString();
		
		String address = ((EditText) findViewById(R.id.id_e_address))
				.getText().toString();

		String comment = ((EditText) findViewById(R.id.id_e_comment))
		.getText().toString();


		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		BasicNameValuePair on = new BasicNameValuePair("user_study_id",
				mUserStudyId);
		nameValuePair.add(on);

		on = new BasicNameValuePair("study_name", studyName);
		nameValuePair.add(on);
		
		on = new BasicNameValuePair("address", address);
		nameValuePair.add(on);

		on = new BasicNameValuePair("publish_date", "1000");
		nameValuePair.add(on);
		
		on = new BasicNameValuePair("comment", comment);
		nameValuePair.add(on);

		String res = HttpInterface.excutePost(url, nameValuePair);

		return res;
	}

	boolean checkBeforePost() {
		
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
		String result=null;
		String message=null;
		ResponseMessage rm=null;
		
		rm=OpenAPIUtil.parserResponse(res);
		if(rm==null)
			return;
		result=rm.mResult;
		message=rm.mMessage;
		if(result.equalsIgnoreCase("success"))
		{
			ContentProviderUtil.insertUserStudyJson(this,rm.mData);
			Util.showToast(TupleApplication.getContext().getResources().getString(R.string.sucess_user_setting));
		}
		else if(result.equalsIgnoreCase("fail"))
		{
			Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_user_setting));
		}
		
		finishAndReturn();
		return;
	}
}
