package com.fv.tuple.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;
import com.fv.tuple.content_provider.TupleContentProvider;
import com.fv.tuple.content_provider.TupleContentProvider.TUserTech;
import com.fv.tuple.open.OpenAPI;
import com.fv.tuple.open.OpenAPIUtil;
import com.fv.tuple.open.OpenAPIUtil.ResponseMessage;
import com.fv.tuple.util.HttpGetAsyncTask;
import com.fv.tuple.util.HttpGetAsyncTask.CallInterface;
import com.fv.tuple.util.HttpInterface;
import com.fv.tuple.util.Util;
import com.fv.tuple.widget.DataPickerPopupWindow;
import com.fv.tuple.widget.TimePickerPopupWindow;

/**
 * This activity need do some initialize working when login success, get the basic info from server, 
 * contains: 
 * 1. get the user info and insert into db.
 * 2. image server url.
 * 3. video server url.
 * 4. message server url.
 */
public class RequestTechActivity extends Activity  implements CallInterface{
    /** Called when the activity is first created. */
	private String mUserTechId="";
	private Intent mIntent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_request_tech);
        mIntent=getIntent();
        mUserTechId=mIntent.getStringExtra(("user_tech_id"));
        
        initUI();
       
    }
    
    void initUI()
    {
   
    	initDateAndTime(1000);
    	
    	Button b=(Button)findViewById(R.id.id_b_submit);
        b.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 				Util.showToast("预约已发送，请等待回复");
 				startGetData();
 			}
 		});
        
        b=(Button)findViewById(R.id.id_b_request_data);
        b.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 				ShowDataPicker();
 			}
 		});
        b=(Button)findViewById(R.id.id_b_request_time);
        b.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 				ShowTimePicker();
 			}
 		});
        initUIData();
    }
    
    
	void startGetData()
	{
		HttpGetAsyncTask t=new HttpGetAsyncTask(this,this,null,R.string.app_name,R.string.app_name,null);
		String[] params = { OpenAPI.getInstance().getAddRequestInfoUrl(), null ,null};
		t.execute(params);
	}
	String startUpload(String url) {

		String techName=((TextView)findViewById(R.id.id_tv_tech_name)).getText().toString();
		String address=((EditText)findViewById(R.id.id_e_address)).getText().toString();
		String request_comment=((EditText)findViewById(R.id.id_e_message)).getText().toString();
        String time=""+getDateAndTime();
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		BasicNameValuePair on = new BasicNameValuePair("user_tech_id",
				mUserTechId);
		nameValuePair.add(on);

		on = new BasicNameValuePair("tech_name", techName);
		nameValuePair.add(on);
		
		on = new BasicNameValuePair("address", address);
		nameValuePair.add(on);
		
		on = new BasicNameValuePair("request_comment", request_comment);
		nameValuePair.add(on);
		
		on = new BasicNameValuePair("tech_date", time);
		nameValuePair.add(on);
		

		String res = HttpInterface.excutePost(url, nameValuePair);

		return res;
	}
	boolean checkBeforePost() {
		
		return true;
	}
  void  initUIData()
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

			int user_tech_id=cursor.getInt(cursor.getColumnIndex(TUserTech.COLUMN_USER_TECH_ID));
			tech_id=cursor.getInt(cursor.getColumnIndex(TUserTech.COLUMN_TECH_ID));
			int user_id=cursor.getInt(cursor.getColumnIndex(TUserTech.COLUMN_USER_ID));
			String tech_name=cursor.getString(cursor.getColumnIndex(TUserTech.COLUMN_TECH_NAME));
			String comment=cursor.getString(cursor.getColumnIndex(TUserTech.COLUMN_COMMENT));
			String address=cursor.getString(cursor.getColumnIndex(TUserTech.COLUMN_ADDRESS));
			int pay=cursor.getInt(cursor.getColumnIndex(TUserTech.COLUMN_PAY));
			int period=cursor.getInt(cursor.getColumnIndex(TUserTech.COLUMN_PERIOD));
	
		if(cursor!=null)
		{
			cursor.close();
			cursor= null;
		}
	
		((TextView)findViewById(R.id.id_tv_tech_name)).setText(tech_name);
		((EditText)findViewById(R.id.id_e_address)).setText(address);
		
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
	if(res==null)
		return;
	rm=OpenAPIUtil.parserResponse(res);
	if(rm==null)
		return;
	result=rm.mResult;
	message=rm.mMessage;
	if(result.equalsIgnoreCase("success"))
	{
		Util.showToast(TupleApplication.getContext().getResources().getString(R.string.sucess_user_setting));
	}
	else if(result.equalsIgnoreCase("fail"))
	{
		if(message.compareToIgnoreCase("duplicated")==0)
		{
			Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_add_request_info_duplicated));
		}
		else
			Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_user_setting));
	}
this.finish();
	
	return;
}

DataPickerPopupWindow mDataPicker = null;
TimePickerPopupWindow mTimePicker = null;
long getDateAndTime()
{
	TextView tv=(TextView)findViewById(R.id.id_tv_data);
	
	String dates[]=tv.getText().toString().split("-");
	int y=Integer.parseInt(dates[0]);
	int m=Integer.parseInt(dates[1]);
	int d=Integer.parseInt(dates[2]);
	
   
	tv=(TextView)findViewById(R.id.id_tv_time);
	String times[]=tv.getText().toString().split(":");
	int h=Integer.parseInt(times[0]);
	int min=Integer.parseInt(times[1]);
	Calendar calendar=Calendar.getInstance();
 	calendar.set(Calendar.YEAR, y);
 	calendar.set(Calendar.MONTH, m);
 	calendar.set(Calendar.DAY_OF_MONTH, d);
 	calendar.set(Calendar.HOUR_OF_DAY, h);
 	calendar.set(Calendar.MINUTE, m);
 	
 	return calendar.getTimeInMillis();
	
}
void initDateAndTime(long time)
{
	
	
 	Calendar calendar=Calendar.getInstance();
 	//calendar.setTimeInMillis(time);

    int year=calendar.get(Calendar.YEAR);
    int month=calendar.get(Calendar.MONTH);
    int day=calendar.get(Calendar.DAY_OF_MONTH);
	TextView tv=(TextView)findViewById(R.id.id_tv_data);
	tv.setText(year+"-"+month+"-"+day);
	
	int h=calendar.get(Calendar.HOUR_OF_DAY);
    int m=calendar.get(Calendar.MINUTE);
   
	tv=(TextView)findViewById(R.id.id_tv_time);
	tv.setText(h+":"+m);
}
public void ShowDataPicker() {
	if (mDataPicker == null) {
		mDataPicker = new DataPickerPopupWindow(this, mHandler,
				R.drawable.menu_bg, R.style.PopupAnimation);
	}
	if (mDataPicker != null) {
		if (mDataPicker.isShowing())
			mDataPicker.dismiss();
		else {
			TextView tv=(TextView)findViewById(R.id.id_tv_data);
			mDataPicker.showAtLocation(this.getWindow().getDecorView(),tv.getText().toString(),
					Gravity.CENTER, 0, 0);
		}
	}
}
public void ShowTimePicker() {
	if (mTimePicker == null) {
		mTimePicker = new TimePickerPopupWindow(this, mHandler,
				R.drawable.menu_bg, R.style.PopupAnimation);
	}
	if (mTimePicker != null) {
		if (mTimePicker.isShowing())
			mTimePicker.dismiss();
		else {
			TextView tv=(TextView)findViewById(R.id.id_tv_time);
			mTimePicker.showAtLocation(this.getWindow().getDecorView(),tv.getText().toString(),
					Gravity.CENTER, 0, 0);
		}
	}
}
public Handler mHandler = new Handler() {
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 1001:
			
			break;
	
		case 1009:
		{
	    	TextView tv=(TextView)findViewById(R.id.id_tv_data);
	    	tv.setText((String)(msg.obj));
	    	break;
		}
		case 1010:
		{
	    	TextView tv=(TextView)findViewById(R.id.id_tv_time);
	    	tv.setText((String)(msg.obj));
	    	 Date currentTime = new Date();
	    	 currentTime.getTime();
	    	
	    	break;
		}
		}
	
		super.handleMessage(msg);
	}
};

}
