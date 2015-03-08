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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;
import com.fv.tuple.content_provider.ContentProviderUtil;
import com.fv.tuple.open.OpenAPI;
import com.fv.tuple.open.OpenAPIUtil;
import com.fv.tuple.open.OpenAPIUtil.ResponseMessageForMeAndServer;
import com.fv.tuple.util.HttpGetAsyncTask;
import com.fv.tuple.util.HttpGetAsyncTask.CallInterface;
import com.fv.tuple.util.HttpInterface;
import com.fv.tuple.util.Util;

/**
 * panda 2015-02-25
 * This activity need do some initialize working when login success, get the basic info from server, 
 * contains: 
 * 1. get the user info and insert into db.
 * 2. image server url.
 * 3. video server url.
 * 4. message server url.
 */
public class LoginActivity extends Activity  implements CallInterface{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login);
        getPreference();
        initUI();
    	if(isDynamicLogin())
    	{
    		startLogin();
    	}
    }
    
void initUI()
{
	Button	btns=null;
	btns = (Button) findViewById(R.id.id_b_register);	
	btns.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startRegister();
		}
	});
	
	
	btns=null;
	btns = (Button) findViewById(R.id.id_b_login);	
	btns.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startLogin();
		}
	});
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
/**
 * Check the passcode setting info
 * 4. message server url.
 * ��ʾ��
 * @param 
 * @param 
 * @param 
 * @return 
 */
void getPreference()
{
		SharedPreferences shared = getSharedPreferences("tuple",
				Activity.MODE_PRIVATE);
		
		String account=shared.getString("account", "email1");
		EditText et1 = (EditText) findViewById(R.id.id_e_email);	
		et1.setText(account);
		
		
		int v=shared.getInt("password", 0);
		if(v==0)
		{
			CheckBox ch = (CheckBox) findViewById(R.id.id_ch_password);	
			ch.setChecked(false);
		}
		else
		{
			CheckBox ch = (CheckBox) findViewById(R.id.id_ch_password);	
			ch.setChecked(true);
			String password=shared.getString("password_s", "password1");
			EditText et = (EditText) findViewById(R.id.id_e_password);	
			et.setText(password);
		}
		
		v=shared.getInt("dynamic_login", 0);
		if(v==0)
		{
			CheckBox ch = (CheckBox) findViewById(R.id.id_ch_dynamic_login);	
			ch.setChecked(false);
		}
		else
		{
			CheckBox ch = (CheckBox) findViewById(R.id.id_ch_dynamic_login);	
			ch.setChecked(true);
		}
}
boolean isDynamicLogin()
{
	SharedPreferences shared = getSharedPreferences("tuple",
			Activity.MODE_PRIVATE);
	int v=shared.getInt("dynamic_login", 0);
	if(v==0)
	{
		return false;
	}
	else
	{
		return true;
	}
}
void savePreference()
{

		SharedPreferences shared = getSharedPreferences("tuple",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = shared.edit();
		
		
		EditText et1 = (EditText) findViewById(R.id.id_e_email);	
		Editable s1=et1.getText();
		if(s1.length()>0)
		{
			editor.putString("account", s1.toString());
			editor.commit();
		}
		else
		{
			
			editor.putString("account","");
			editor.commit();
		}
		
		
		CheckBox ch = (CheckBox) findViewById(R.id.id_ch_password);	
		if(ch.isChecked())
		{
			EditText et = (EditText) findViewById(R.id.id_e_password);	
			Editable s=et.getText();
			if(s.length()>0)
			{
				editor.putString("password_s", s.toString());
				editor.putInt("password", 1);
				editor.commit();
			}
			else
			{
				editor.putString("password_s", "");
				editor.putInt("password", 0);
				editor.commit();
			}
			
		}
		CheckBox ch1 = (CheckBox) findViewById(R.id.id_ch_dynamic_login);	
		if(ch.isChecked()&&ch1.isChecked())
		{
			editor.putInt("dynamic_login", 1);
			editor.commit();
		}
		else
		{
			editor.putInt("dynamic_login", 0);
			editor.commit();
		}
		
		
		
}


void startLogin()
{
	HttpGetAsyncTask t=new HttpGetAsyncTask(this,this,null,R.string.app_name,R.string.app_name,null);
	String[] params = { OpenAPI.getInstance().getLoginUrl(), null ,null};
	t.execute(params);
}

public void startTest()
{
	Intent intentv = new Intent(this, LoaderCursorTechMainActivity.class);  
	intentv.putExtra("null", ""); 
	intentv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intentv); 
    this.finish();
	
}
public void startMainFragmentActivity()
{
	Intent intentv = new Intent(this, MainFragmentActivity.class);  
	intentv.putExtra("null", ""); 
	intentv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intentv); 
    this.finish();
	
}

public void startTechMain()
{
	//if(true)
	//{
	//	startTest();
	//	return;
	//}

	Intent intentv = new Intent(this, TechMainActivity.class);  
	intentv.putExtra("null", ""); 
	intentv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intentv); 
    this.finish();
}

public void startRequest()
{
	//if(true)
	//{
	//	startTest();
	//	return;
	//}

	Intent intentv = new Intent(this, RequestActivity.class);  
	intentv.putExtra("null", ""); 
	intentv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intentv); 
    this.finish();
}

public void startMe()
{
	Intent intentv = new Intent(this, MeDetailActivity.class);  
	intentv.putExtra("null", ""); 
	intentv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intentv); 
    this.finish();
}

public void startRegister()
{
	Intent intentv = new Intent(this, RegisterActivity.class);  
	intentv.putExtra("null", ""); 
	intentv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intentv); 
    // this.finish();
}
String startUpload(String url)
{

	String email=((EditText)findViewById(R.id.id_e_email)).getText().toString();
	String password=((EditText)findViewById(R.id.id_e_password)).getText().toString();
	
	List<NameValuePair> nameValuePair= new ArrayList<NameValuePair>();
	BasicNameValuePair on=new BasicNameValuePair("email",email);
	nameValuePair.add(on);
	
	on=new BasicNameValuePair("password",password);
	nameValuePair.add(on);
	String res=HttpInterface.excutePost(url,nameValuePair);
	
	return res;
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
		if(res!=null)
		{
			String result=null;
			String message=null;
			ResponseMessageForMeAndServer rm=null;
			
			rm=OpenAPIUtil.parserResponseForMeAndServer(res);
			result=rm.mResult;
			message=rm.mMessage;
			if(result.equalsIgnoreCase("success"))
			{
				ContentProviderUtil.insertMeJson(this,rm.mDataMe);
				
				ContentProviderUtil.deleteAllUserStudy(this);
				ContentProviderUtil.insertUserStudyJson(this,rm.mDataUserStudy);
				ContentProviderUtil.deleteAllUserTech(this);
				ContentProviderUtil.insertUserTechJson(this,rm.mDataUserTech);
				ContentProviderUtil.insertJsonServer(this,rm.mServer);
				
				savePreference();
				startMainFragmentActivity();
				//startTechMain();
				//startRequest();
				//startMe();
				this.finish();
				Util.showToast(TupleApplication.getContext().getResources().getString(R.string.sucess_user_setting));
			}
			else if(result.equalsIgnoreCase("fail"))
			{
				Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_user_setting));
			}
			
		}
		
	}

}

}
