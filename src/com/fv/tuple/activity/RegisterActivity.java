package com.fv.tuple.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewFlipper;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;
import com.fv.tuple.content_provider.ContentProviderUtil;
import com.fv.tuple.open.OpenAPI;
import com.fv.tuple.open.OpenAPIUtil;
import com.fv.tuple.open.OpenAPIUtil.ResponseMessage;
import com.fv.tuple.util.HttpGetAsyncTask;
import com.fv.tuple.util.HttpGetAsyncTask.CallInterface;
import com.fv.tuple.util.HttpInterface;
import com.fv.tuple.util.Util;

public class RegisterActivity extends Activity implements CallInterface{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_register);
        initUI();
    }
    
    void startSettingPhoto()
    {
   	 	Intent intentv = new Intent(this, SettingPhotoActivity.class);  
   	 	startActivityForResult(intentv, SETTING_PHOTO_REQUEST_CODE);
    }
    
    void startSettingSignature()
    {
    	  Intent intentv = new Intent(this, SettingSignatureActivity.class); 
  	    intentv.putExtra("type","add"); 
  	    intentv.putExtra("class","RegisterActivity"); 
  	    intentv.putExtra("user_id", "0"); 
  	    startActivityForResult(intentv, SETTING_SIGNATURE_REQUEST_CODE);
    
    }
    void startSettingBasic()
    {
    	
        Intent intentv = new Intent(this, SettingBasicActivity.class); 
	    intentv.putExtra("type","add"); 
	    intentv.putExtra("class","RegisterActivity"); 
	    intentv.putExtra("user_id", "0"); 
	    startActivityForResult(intentv, SETTING_BASIC_REQUEST_CODE);

    }
    
    void startSettingPassword()
    {
     	Intent intentv = new Intent(this, SettingPasswordActivity.class);  
     	startActivityForResult(intentv, SETTING_PASSWORD_REQUEST_CODE);
    }
void initUI()
{
	Button	btns=null;
	btns = (Button) findViewById(R.id.id_b_submit);	
	btns.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			HttpGetAsyncTask t=new HttpGetAsyncTask(RegisterActivity.this,RegisterActivity.this,null,R.string.app_name,R.string.app_name,null);
			String[] params = { OpenAPI.getInstance().getRegisterUrl(), null ,null};
			t.execute(params);
		}
	});
	
}

private static final int RESULT_REQUEST_CODE = 0;
private static final int SETTING_PHOTO_REQUEST_CODE = 1;
private static final int SETTING_SIGNATURE_REQUEST_CODE = 2;
private static final int SETTING_BASIC_REQUEST_CODE = 3;
private static final int SETTING_PASSWORD_REQUEST_CODE = 4;


@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// ����벻����ȡ��ʱ��
	if (resultCode != RESULT_CANCELED) {

		switch (requestCode) {
		case SETTING_PHOTO_REQUEST_CODE:
			

			break;
		case SETTING_SIGNATURE_REQUEST_CODE:
			

			break;
		case SETTING_BASIC_REQUEST_CODE:
			

			break;
		case SETTING_PASSWORD_REQUEST_CODE:
			

			break;
		case RESULT_REQUEST_CODE:
			
			break;
		}
	}
	super.onActivityResult(requestCode, resultCode, data);
}
boolean checkBeforePost()
{
	String email=((EditText)findViewById(R.id.id_e_email)).getText().toString();
	String password=((EditText)findViewById(R.id.id_e_password)).getText().toString();
	if(email.equalsIgnoreCase(""))
	{
		Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_register_no_email));
		return false;
	}
	if(password.equalsIgnoreCase(""))
	{
		Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_register_no_password));
		return false;
	}
	return true;
}
String startRegister(String url)
{

	/*
	String email=((EditText)findViewById(R.id.id_e_email)).getText().toString();
	String password=((EditText)findViewById(R.id.id_e_password)).getText().toString();
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
	BasicNameValuePair on=new BasicNameValuePair("email",email);
	nameValuePair.add(on);
	
	on=new BasicNameValuePair("password",password);
	nameValuePair.add(on);
	
	on=new BasicNameValuePair("name",name);
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
	*/
	return null;
}

@Override
public String doing(String mainPath, String viaPath, String other,
		Object otherObject) throws ClientProtocolException, IOException {
	// TODO Auto-generated method stub
	 return startRegister(mainPath);
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
			ViewFlipper vf = (ViewFlipper) findViewById(R.id.f);
			vf.setDisplayedChild(1);
		}
		else if(result.equalsIgnoreCase("fail"))
		{
			if(message.equalsIgnoreCase("duplicated"))
			{
				Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_user_register_email));
			}
			else
			Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_user_register));
		}
		
	}
	return;
}


}
