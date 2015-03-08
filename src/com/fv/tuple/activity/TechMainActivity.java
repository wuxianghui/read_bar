package com.fv.tuple.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;
import com.fv.tuple.adapter.TechRootMenuAdapter;
import com.fv.tuple.content_provider.ContentProviderUtil;
import com.fv.tuple.content_provider.TupleContentProvider;
import com.fv.tuple.open.OpenAPI;
import com.fv.tuple.open.OpenAPIUtil;
import com.fv.tuple.open.OpenAPIUtil.ResponseMessage;
import com.fv.tuple.util.HttpGetAsyncTask;
import com.fv.tuple.util.HttpInterface;
import com.fv.tuple.util.Util;
import com.fv.tuple.util.DownloadAsyncTaskPool.DownloadAsyncTaskPoolElement;
import com.fv.tuple.util.HttpGetAsyncTask.CallInterface;

public class TechMainActivity extends RootActivity implements CallInterface{
    /** Called when the activity is first created. */
    private ListView mListView=null;
    private TechRootMenuAdapter mAdapter=null;
    
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tech_root_menu);
        initUI();
       // initObserver();
        startGetMenu();
    }
    
void initUI()
{
	mListView = (ListView) findViewById(R.id.id_list_tech_root_menu_listview);
    //	mChangeCursor=getCursor();
    	
	mAdapter = new TechRootMenuAdapter(this, mChangeCursor,mHandler);
	mListView.setAdapter(mAdapter);
	mListView.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, final View view,
				final int position, long id) {
			TextView t=(TextView)view.findViewById(R.id.item_tech );
			//startMe();

			startTech((String)(t.getTag()));
			
			
			
			
		}
	});
}
public void startMe()
{
	Intent intentv = new Intent(this, MeDetailActivity.class);  
	intentv.putExtra("null", ""); 
	intentv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intentv); 
    this.finish();
}
private static final int TECH_REQUEST_CODE = 4;
void startTech(String tech_id)
{
	  Intent intentv = new Intent(this, UserActivity.class); 
  	    intentv.putExtra("tech_id",tech_id); 
  	  intentv.putExtra("type","TechMainActivity"); 
  	    startActivityForResult(intentv, TECH_REQUEST_CODE);
}
private Cursor mChangeCursor;
public Cursor getCursor()
{
	String where=TupleContentProvider.TTech.COLUMN_PARENT_TECH_ID+"=0";
	return mChangeCursor = getContentResolver()
	.query(TupleContentProvider.TTech.CONTENT_URI,
			null,
			where,
			null,
			TupleContentProvider.TTech.COLUMN_TECH_ID + " asc");
	
}


Obs obs = new Obs(new Handler());
private void initObserver()
{
	this.getContentResolver().registerContentObserver(TupleContentProvider.TTech.CONTENT_URI,
			true, obs);
	//mAdapter.notifyDataSetChanged();
}
private void unInitObserver()
{
	this.getContentResolver().unregisterContentObserver(obs);
	
}
void updateAdapterCursor()
{
	mChangeCursor=getCursor();
	mAdapter.changeCursor(mChangeCursor);
}
private class Obs extends ContentObserver{
	public int count=0;
	public Obs(Handler handler) {
		super(handler);
	}
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		updateAdapterCursor();
		count++;
		Util.showToast("===========>"+count);
	}
	
}
public String getDownloadUrl(String handle) {
	// TODO Auto-generated method stub
   return OpenAPI.getInstance().getSystemImageUrl(handle);
}

public void downloadPicResult(Object handle, String fileName, String filePath)
{
	String where=TupleContentProvider.TTech.COLUMN_ID+"="+((DownloadAsyncTaskPoolElement)handle).mOther;
	ContentProviderUtil.updateDownPictureStatus(TupleContentProvider.TTech.CONTENT_URI,TupleContentProvider.TTech.COLUMN_PIC_STATUS,where,1);
}


void startGetMenu()
{
	HttpGetAsyncTask t=new HttpGetAsyncTask(TechMainActivity.this,TechMainActivity.this,null,R.string.app_name,R.string.app_name,null);
	String[] params = { OpenAPI.getInstance().getGetTechMenu(), null ,null};
	t.execute(params);
}

String startUpload(String url)
{

	String res=HttpInterface.excutePost(url,null);
	
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
		ResponseMessage rm=null;
		
		rm=OpenAPIUtil.parserResponse(res);
		result=rm.mResult;
		if(result.equalsIgnoreCase("success"))
		{
			unInitObserver();
			ContentProviderUtil.insertTechJson(this,rm.mData);
			initObserver();
			
			updateAdapterCursor();
			Util.showToast(TupleApplication.getContext().getResources().getString(R.string.sucess_user_setting));
		}
		else if(result.equalsIgnoreCase("fail"))
		{
			Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_user_setting));
		}
		
	}
}
}
