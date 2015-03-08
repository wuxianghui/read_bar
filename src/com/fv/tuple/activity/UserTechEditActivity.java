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
import android.widget.TextView;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;
import com.fv.tuple.content_provider.ContentProviderUtil;
import com.fv.tuple.content_provider.TupleContentProvider;
import com.fv.tuple.content_provider.TupleContentProvider.TUserTech;
import com.fv.tuple.open.OpenAPI;
import com.fv.tuple.open.OpenAPIUtil;
import com.fv.tuple.open.OpenAPIUtil.ResponseMessage;
import com.fv.tuple.util.HttpGetAsyncTask;
import com.fv.tuple.util.HttpGetAsyncTask.CallInterface;
import com.fv.tuple.util.HttpInterface;
import com.fv.tuple.util.Util;
import com.fv.tuple.widget.TechSelectOperation;

public class UserTechEditActivity extends Activity implements CallInterface {
	/** Called when the activity is first created. */
	private String mUserTechId="";
	private String mType="";
	private Intent mIntent=null;
	private TechSelectOperation mTechSelectOperation=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_user_tech_edit);
		mIntent=getIntent();
		mTechSelectOperation=new TechSelectOperation(this);
		initUI();
		getData();
		
	}

	void startRequestTech()
	{
		
		    Intent intentv = new Intent(this, RequestTechActivity.class); 

		    intentv.putExtra("user_tech_id", mUserTechId); 
		    startActivity(intentv);
		
	}

	void initUI()
	{
		mType=mIntent.getStringExtra(("type"));
		if(mType.compareToIgnoreCase("edit")!=0)
		{
			Button b=(Button)findViewById(R.id.id_b_submit);
			b.setVisibility(View.GONE);
			LinearLayout ll=(LinearLayout)findViewById(R.id.id_ll_tech_class);
			ll.setVisibility(View.GONE);
			
			EditText et=(EditText)findViewById(R.id.id_e_tech_name);
			et.setEnabled(false);
			et=(EditText)findViewById(R.id.id_e_tech_comment);
			et.setEnabled(false);
			et=(EditText)findViewById(R.id.id_e_address);
			et.setEnabled(false);
			et=(EditText)findViewById(R.id.id_e_period);
			et.setEnabled(false);
			et=(EditText)findViewById(R.id.id_e_pay);
			et.setEnabled(false);
			
			b=(Button)findViewById(R.id.id_b_request);
			b.setVisibility(View.VISIBLE);
			
			
			b=(Button)findViewById(R.id.id_b_request);
			
			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startRequestTech();// submit the edit data
					
				}
			});
			
		}
		else
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
		
		
		
	}
	void getData()
	{
		mUserTechId=mIntent.getStringExtra(("user_tech_id"));
		//mType=mIntent.getStringExtra(("type"));
		//if(mType.compareToIgnoreCase("edit")==0)
		getUserTechData();
		
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}

	public void finishAndReturn()
	{
		Intent intent = new Intent(UserTechEditActivity.this,MeDetailActivity.class);
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
	public void initUIData(int userTechId,int techId,int userId,String techName,String comment,String address,int pay,int period)
	{

		EditText vv = (EditText) findViewById(R.id.id_e_tech_name);	
		vv.setText(techName);
		
		vv = (EditText) findViewById(R.id.id_e_address);	
		vv.setText(address);
		
		vv = (EditText) findViewById(R.id.id_e_pay);	
		vv.setText(""+pay);
		
		vv = (EditText) findViewById(R.id.id_e_period);	
		vv.setText(""+period);
		
		vv = (EditText) findViewById(R.id.id_e_tech_comment);	
		vv.setText(comment);
		
		mTechSelectOperation.initTechInfo(""+techId);
	}
	

	
	public void getUserTechData()
	{
		int tech_id=0;
			String where=TupleContentProvider.TUserTech.COLUMN_USER_TECH_ID+"="+mUserTechId;
			
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

			for (int i = 0; i < cursor.getCount(); i++) {
				int user_tech_id=cursor.getInt(cursor.getColumnIndex(TUserTech.COLUMN_USER_TECH_ID));
				tech_id=cursor.getInt(cursor.getColumnIndex(TUserTech.COLUMN_TECH_ID));
				int user_id=cursor.getInt(cursor.getColumnIndex(TUserTech.COLUMN_USER_ID));
				String tech_name=cursor.getString(cursor.getColumnIndex(TUserTech.COLUMN_TECH_NAME));
				String comment=cursor.getString(cursor.getColumnIndex(TUserTech.COLUMN_COMMENT));
				String address=cursor.getString(cursor.getColumnIndex(TUserTech.COLUMN_ADDRESS));
				int pay=cursor.getInt(cursor.getColumnIndex(TUserTech.COLUMN_PAY));
				int period=cursor.getInt(cursor.getColumnIndex(TUserTech.COLUMN_PERIOD));
				initUIData(user_tech_id,tech_id,user_id,tech_name,comment,address,pay,period);
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
		String[] params = { OpenAPI.getInstance().getUpdateUserTechUrl(), null ,null};
		t.execute(params);
	}
	
	String startUpload(String url) {


		String techName = ((EditText) findViewById(R.id.id_e_tech_name))
				.getText().toString();
		
		String address = ((EditText) findViewById(R.id.id_e_address))
				.getText().toString();
		
		String pay = ((EditText) findViewById(R.id.id_e_pay))
		.getText().toString();
		
		if(pay.compareToIgnoreCase("")==0)
			pay="0";
		
		String period = ((EditText) findViewById(R.id.id_e_period))
		.getText().toString();
		
		if(period.compareToIgnoreCase("")==0)
			period="3600";
		
		String comment = ((EditText) findViewById(R.id.id_e_tech_comment))
		.getText().toString();


		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		BasicNameValuePair on = new BasicNameValuePair("user_tech_id",
				mUserTechId);
		nameValuePair.add(on);

		on = new BasicNameValuePair("tech_name", techName);
		nameValuePair.add(on);
		
		on = new BasicNameValuePair("address", address);
		nameValuePair.add(on);
		
		on = new BasicNameValuePair("pay", pay);
		nameValuePair.add(on);
		
		on = new BasicNameValuePair("period", period);
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
			ContentProviderUtil.insertUserTechJson(this,rm.mData);
			Util.showToast(TupleApplication.getContext().getResources().getString(R.string.sucess_user_setting));
		}
		else if(result.equalsIgnoreCase("fail"))
		{
			Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_user_setting));
		}
		
		return;
	}
}
