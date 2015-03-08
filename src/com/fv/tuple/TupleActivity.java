package com.fv.tuple;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.fv.tuple.activity.LoginActivity;
import com.fv.tuple.util.HttpGetAsyncTask;
import com.fv.tuple.util.HttpGetAsyncTask.CallInterface;

/**
 * this activity will do some local initialize working, and check the sdcard, network status.
 **/
public class TupleActivity extends Activity implements CallInterface {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        doExcuteHttp();
    }
    
	public void startLoginActivity()
	{
		Intent intentv = new Intent(this, LoginActivity.class);  
		intentv.putExtra("null", ""); 
		intentv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentv); 
        this.finish();
	}
    
	void doExcuteHttp()
	{
		//uri="http://brilliant2011.gicp.net:8000/cg/"+LoginActivity.mUserId+"/"+uri+"/";

		HttpGetAsyncTask t=new HttpGetAsyncTask(this,this,null,R.string.app_name,R.string.app_name,null);
		//String[] params = { OpenAPI.getInstance().getAllMainmenuUrl(), null ,null};\
		String[] params = { null, null ,null};
		t.execute(params);
	}
	
	
	
	@Override
	public String doing(String mainPath, String viaPath, String other,
			Object otherObject) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean PreSyncTaskCall() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void PostSyncTaskCall(String other, String res, Object otherObject) {
		// TODO Auto-generated method stub
		startLoginActivity();
	}
}