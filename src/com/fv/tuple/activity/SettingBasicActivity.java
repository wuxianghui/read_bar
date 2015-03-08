package com.fv.tuple.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;
import com.fv.tuple.content_provider.ContentProviderUtil;
import com.fv.tuple.content_provider.TupleContentProvider;
import com.fv.tuple.content_provider.TupleContentProvider.TMe;
import com.fv.tuple.open.OpenAPI;
import com.fv.tuple.open.OpenAPIUtil;
import com.fv.tuple.open.OpenAPIUtil.ResponseMessage;
import com.fv.tuple.util.HttpGetAsyncTask;
import com.fv.tuple.util.HttpInterface;
import com.fv.tuple.util.Util;
import com.fv.tuple.util.HttpGetAsyncTask.CallInterface;

public class SettingBasicActivity extends Activity implements CallInterface{
    /** Called when the activity is first created. */

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_basic);
        initUI();
    }
    
    
    
void initUI()
{
	Button btns = (Button) findViewById(R.id.id_b_submit);	
	btns.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			HttpGetAsyncTask t=new HttpGetAsyncTask(SettingBasicActivity.this,SettingBasicActivity.this,null,R.string.app_name,R.string.app_name,null);
			String[] params = { OpenAPI.getInstance().getUpdateBasicUrl(), null ,null};
			t.execute(params);
			
		}
	});
	
	btns = (Button) findViewById(R.id.id_b_cancel);	
	btns.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finishAndReturn();
			
		}
	});

}


String startUpload(String url)
{

	String name=((EditText)findViewById(R.id.id_e_name)).getText().toString();
	String age=((EditText)findViewById(R.id.id_e_age)).getText().toString();
	if(age.equalsIgnoreCase(""))
		age="0";
	String sex=((EditText)findViewById(R.id.id_e_sex)).getText().toString();
	String address=((EditText)findViewById(R.id.id_e_address)).getText().toString();
	String school=((EditText)findViewById(R.id.id_e_school)).getText().toString();
	String job=((EditText)findViewById(R.id.id_e_job)).getText().toString();
	String comments=((EditText)findViewById(R.id.id_e_comments)).getText().toString();
	
	List<NameValuePair> nameValuePair= new ArrayList<NameValuePair>();
	BasicNameValuePair on=new BasicNameValuePair("name",name);
	nameValuePair.add(on);
	
	on=new BasicNameValuePair("sex",sex);
	nameValuePair.add(on);
	
	on=new BasicNameValuePair("age",age);
	nameValuePair.add(on);
	
	on=new BasicNameValuePair("address",address);
	nameValuePair.add(on);
	
	on=new BasicNameValuePair("school",school);
	nameValuePair.add(on);
	
	on=new BasicNameValuePair("job",job);
	nameValuePair.add(on);
	
	on=new BasicNameValuePair("comments",comments);
	nameValuePair.add(on);


	String res=HttpInterface.excutePost(url,nameValuePair);
	
	return res;
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
		ResponseMessage rm=null;
		
		rm=OpenAPIUtil.parserResponse(res);
		result=rm.mResult;
		message=rm.mMessage;
		if(result.equalsIgnoreCase("success"))
		{
			ContentProviderUtil.insertMeJson(this,rm.mData);
			Util.showToast(TupleApplication.getContext().getResources().getString(R.string.sucess_user_setting));
		}
		else if(result.equalsIgnoreCase("fail"))
		{
			Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_user_setting));
		}
		
	}
	finishAndReturn();
	return;
	
}


private String mType="";
private String mUserId="";
private String mClass="";
@Override
protected void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	Intent v=getIntent();
	mType=v.getStringExtra(("type"));
	mClass=v.getStringExtra(("class"));
	if(mType.compareToIgnoreCase("edit")==0)
	{
		mUserId=v.getStringExtra(("user_id"));
		
		getBasicDBData();
	}
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
	
	
	EditText et=((EditText)findViewById(R.id.id_e_name));
	et.setText(name);
	
	et=((EditText)findViewById(R.id.id_e_age));
	et.setText(""+age);
	
	et=((EditText)findViewById(R.id.id_e_sex));
		et.setText(sex);
		
	et=((EditText)findViewById(R.id.id_e_address));
		et.setText(address);
		
	et=((EditText)findViewById(R.id.id_e_school));
		et.setText(school);
	
	et=((EditText)findViewById(R.id.id_e_job));
		et.setText(job);	
		
	et=((EditText)findViewById(R.id.id_e_comments));
		et.setText(comments);		

}


public void finishAndReturn()
{
	Intent intent =null;
	if(mClass.compareToIgnoreCase("MeActivity")==0)
	intent=new Intent(this,MeDetailActivity.class);
	else if(mClass.compareToIgnoreCase("RegisterActivity")==0)
	intent=new Intent(this,RegisterActivity.class);
	intent.putExtra("result","success");
	setResult(0,intent);
	this.finish();
}

}
