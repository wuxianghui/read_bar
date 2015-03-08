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

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;
import com.fv.tuple.content_provider.ContentProviderUtil;
import com.fv.tuple.content_provider.TupleContentProvider;
import com.fv.tuple.content_provider.TupleContentProvider.TMe;
import com.fv.tuple.open.OpenAPI;
import com.fv.tuple.open.OpenAPIUtil;
import com.fv.tuple.open.OpenAPIUtil.ResponseMessage;
import com.fv.tuple.util.HttpGetAsyncTask;
import com.fv.tuple.util.HttpGetAsyncTask.CallInterface;
import com.fv.tuple.util.HttpInterface;
import com.fv.tuple.util.Util;

public class SettingSignatureActivity extends Activity implements CallInterface{
    /** Called when the activity is first created. */

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_signature);
        initUI();
    }
    
    
    
void initUI()
{
	Button btns = (Button) findViewById(R.id.id_b_submit);	
	btns.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			HttpGetAsyncTask t=new HttpGetAsyncTask(SettingSignatureActivity.this,SettingSignatureActivity.this,null,R.string.app_name,R.string.app_name,null);
			String[] params = { OpenAPI.getInstance().getUpdateSignatureUrl(), null ,null};
			t.execute(params);
			
		}
	});
	
	btns = (Button) findViewById(R.id.id_b_cancel);	
	btns.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
			
		}
	});

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
	
	String signature=cursor.getString(cursor.getColumnIndex(TMe.COLUMN_SIGNATURE));

	EditText et=((EditText)findViewById(R.id.id_e_signature));
	et.setText(signature);
	
	

}
String startUpload(String url)
{

	String signature=((EditText)findViewById(R.id.id_e_signature)).getText().toString();
	
	List<NameValuePair> nameValuePair= new ArrayList<NameValuePair>();
	BasicNameValuePair on=new BasicNameValuePair("signature",signature);
	nameValuePair.add(on);

	String res=HttpInterface.excutePost(url,nameValuePair);
	
	return res;
}



@Override
public String doing(String mainPath, String viaPath, String other,
		Object otherObject) throws ClientProtocolException, IOException {
	// TODO Auto-generated method stub
	
	return startUpload(mainPath);
}


boolean checkBeforePost()
{
	return true;
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
	this.finish();
	return;
	
}

}
