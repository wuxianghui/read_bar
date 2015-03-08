package com.fv.tuple.activity;

import java.io.File;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fv.tuple.R;
import com.fv.tuple.core.Constants;
import com.fv.tuple.util.DownloadAsyncTask;
import com.fv.tuple.util.DownloadAsyncTask.DownloadCallInterface;
import com.fv.tuple.util.DownloadAsyncTaskPool;
import com.fv.tuple.util.DownloadAsyncTaskPool.DownloadAsyncTaskPoolElement;
import com.fv.tuple.util.DownloadAsyncTaskPool.DownloadAsyncTaskPoolInterface;
import com.fv.tuple.util.Util;

/**
 * This activity need do some initialize working when login success, get the basic info from server, 
 * contains: 
 * 1. get the user info and insert into db.
 * 2. image server url.
 * 3. video server url.
 * 4. message server url.
 */								   
public class RootActivity extends FragmentActivity implements DownloadAsyncTaskPoolInterface,DownloadCallInterface{
    /** Called when the activity is first created. */
	DownloadAsyncTaskPool mDP=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDP();
    }
    
    public void initDP()
    {
    	mDP=new DownloadAsyncTaskPool(this,this);
    }


@Override
protected void onStart() {
	// TODO Auto-generated method stub
	super.onStart();


}
@Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	mDP.pause();
}
@Override
protected void onRestart() {
	// TODO Auto-generated method stub
	super.onRestart();
}
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	mDP.resume();
}
@Override
protected void onStop() {
	// TODO Auto-generated method stub
	super.onStop();
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);

     menu.add(Menu.NONE, Constants.MENU_ID_TECH, 2, R.string.menu_tech).setIcon(
                R.drawable.icon_menu_tech);
     
     menu.add(Menu.NONE, Constants.MENU_ID_CONTACT, 2, R.string.menu_contact).setIcon(
             R.drawable.icon_menu_contact);
     
     menu.add(Menu.NONE, Constants.MENU_ID_APPOINTMENT, 2, R.string.menu_request).setIcon(
             R.drawable.icon_menu_appointment);
     
     menu.add(Menu.NONE, Constants.MENU_ID_MESSAGE, 2, R.string.menu_message).setIcon(
             R.drawable.icon_menu_message);
     
     menu.add(Menu.NONE, Constants.MENU_ID_SETTING, 2, R.string.menu_setting).setIcon(
             R.drawable.icon_menu_setting);
     
     menu.add(Menu.NONE, Constants.MENU_ID_EXIT, 2, R.string.menu_exit).setIcon(
             R.drawable.icon_menu_exit);

         
    
    return true;
}


@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    	case Constants.MENU_ID_TECH:
    		return true;
        case Constants.MENU_ID_CONTACT:
            return true;
        case Constants.MENU_ID_APPOINTMENT:
            return true;
        case Constants.MENU_ID_MESSAGE:
            return true;
        case Constants.MENU_ID_SETTING:
            return true;
        case Constants.MENU_ID_EXIT:
            return true;
            
    }
    return super.onOptionsItemSelected(item);
}

@Override
public AsyncTask startOneTask(DownloadAsyncTaskPoolElement task) {
	// TODO Auto-generated method stub
	// TODO Auto-generated method stub
	DownloadAsyncTask t=new DownloadAsyncTask(this,this,task);
	String[] params = { "", null ,"download_pic" };
	t.execute(params);
	return t;
}



public Handler mHandler = new Handler() {
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 1001:
			mDP.runTask();
			Log.d("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$","runTask");
			break;
	
		case 1002:
		{
			String id=""+ msg.arg1;
			String pic=(String) msg.obj;
			DownloadAsyncTaskPoolElement de=new DownloadAsyncTaskPoolElement();
			de.mPic=pic;
			de.mOther=id;
	
			{
				mDP.addSingleTask(de);
				Log.d("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$","addSingleTask");
			}
		}
			break;
		}
	
		super.handleMessage(msg);
	}
};

public void downloadPicResult(Object handle, String fileName, String filePath)
{
}
public String getDownloadUrl(String handle) {
	// TODO Auto-generated method stub
   return null;
}
@Override
public String getDownloadUrl(Object handle) {
	// TODO Auto-generated method stub

	return getDownloadUrl((String)((DownloadAsyncTaskPoolElement) handle).mPic);
}

@Override
public String getSavedPath(Object handle) {
	// TODO Auto-generated method stub
	return Util.getSDCardPicDir();
}

@Override
public void downloadResult(Object handle, String fileName, String filePath) {
	// TODO Auto-generated method stub
	downloadPicResult(handle,  fileName,  filePath);
	
	Message message=new Message();
	message.what=1001;
	mHandler.sendMessage(message);
}

@Override
public String getSavedFilePath(Object handle) {
	// TODO Auto-generated method stub
	return Util.getSDCardPicDir()+File.separator+(String)((DownloadAsyncTaskPoolElement) handle).mPic;
}

@Override
public String getSavedFileName(Object handle) {
	// TODO Auto-generated method stub
	 return (String)((DownloadAsyncTaskPoolElement) handle).mPic;
}


}
