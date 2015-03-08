package com.fv.tuple.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;
import com.fv.tuple.content_provider.ContentProviderUtil;
import com.fv.tuple.open.OpenAPI;
import com.fv.tuple.open.OpenAPIUtil;
import com.fv.tuple.open.OpenAPIUtil.ResponseMessage;
import com.fv.tuple.util.HttpGetAsyncTask;
import com.fv.tuple.util.HttpInterface;
import com.fv.tuple.util.Util;
import com.fv.tuple.util.HttpGetAsyncTask.CallInterface;

public class SettingPasswordActivity extends Activity implements CallInterface{
    /** Called when the activity is first created. */

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_password);
        initUI();
    }
        
void initUI()
{
	Button btns = (Button) findViewById(R.id.id_b_submit);	
	btns.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			HttpGetAsyncTask t=new HttpGetAsyncTask(SettingPasswordActivity.this,SettingPasswordActivity.this,null,R.string.app_name,R.string.app_name,null);
			String[] params = { OpenAPI.getInstance().getUpdatePasswordUrl(), null ,null};
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

String startUpload(String url)
{

	String passwordOld=((EditText)findViewById(R.id.id_e_password_old)).getText().toString();
	String passwordNew1=((EditText)findViewById(R.id.id_e_password_new_1)).getText().toString();
	String passwordNew2=((EditText)findViewById(R.id.id_e_password_new_2)).getText().toString();

	List<NameValuePair> nameValuePair= new ArrayList<NameValuePair>();
	BasicNameValuePair on=new BasicNameValuePair("new_password",passwordNew1);
	nameValuePair.add(on);
	
	on=new BasicNameValuePair("old_password",passwordOld);
	nameValuePair.add(on);
	
	String res=HttpInterface.excutePost(url,nameValuePair);
	
	return res;
}
boolean checkBeforePost()
{
	String passwordOld=((EditText)findViewById(R.id.id_e_password_old)).getText().toString();
	String passwordNew1=((EditText)findViewById(R.id.id_e_password_new_1)).getText().toString();
	String passwordNew2=((EditText)findViewById(R.id.id_e_password_new_2)).getText().toString();
	
	if(passwordOld.equalsIgnoreCase(""))
	{
		Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_register_no_password));
		return false;
	}
	if(passwordNew1.equalsIgnoreCase(""))
	{
		Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_register_no_password));
		return false;
	}
	if(passwordNew2.equalsIgnoreCase(""))
	{
		Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_register_no_password));
		return false;
	}
	if(!passwordNew1.equalsIgnoreCase(passwordNew2))
	{
		Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_register_diff_password));
	}
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
	this.finish();
	return;
}
}
