package com.fv.tuple;

import java.io.File;

import org.apache.http.HttpHost;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.StatFs;
import android.preference.PreferenceManager;

import com.fv.tuple.core.Constants;
import com.fv.tuple.core.TupleCache;
import com.fv.tuple.error.TupleUncaughtExceptionHandler;
import com.fv.tuple.util.TupleLogger;
import com.fv.tuple.util.Util;


/**
 * 
 * @author panda wu 2013.04.01
 * @since 1.0
 */
public class TupleApplication extends Application{

	
	
	
	private static final String TAG = "TupleApplication";	
	
	
	public static String mMeInfoName="";
	public static int mMeInfoUserId=0;
	
	
	private static SharedPreferences m_preferences = null;//总的状态放在这
	private static SharedPreferences m_userpreference = null;//用户的状态存储在这
	
	private static boolean m_appNeedAutoLogin = true;
	private static boolean m_bOAStarted = false;
	private static boolean sdCardAvailable = true;//SDCard是否可用
	private static boolean bSystemMemoryVast = false;	// 内部剩余存储空间很大，比如200M，这时候不需要把cache放到SD卡上
	private static String apnType = "cmnet";	
	private static TupleApplication mInstance = null;
	
    
    private HandlerThread mTaskThread;
	//Called when the application is starting,
	//before any other application objects have been created.
	@Override
	public void  onCreate()	{
		super.onCreate();
		mInstance = this;
		//initialize all singleton instance here
		TupleCache.getInstance();


		m_preferences = getSharedPreferences(Constants.appName, Activity.MODE_PRIVATE);
		m_userpreference = PreferenceManager.getDefaultSharedPreferences(this);
		Thread.setDefaultUncaughtExceptionHandler(new TupleUncaughtExceptionHandler());
		
        // Sometimes we want the application to do some work on behalf of the
        // Activity. Lets do that
        // asynchronously.
        mTaskThread = new HandlerThread(TAG + "-AsyncThread");
        mTaskThread.start();      
		
		new MediaCardStateBroadcastReceiver().register();
		readSystem();
		initPreferences();
		Util.createSDCardPicDir();
	}


	
	//Called when the application is stopping.
	@Override
	public void onTerminate(){
		super.onTerminate();
		//NSAPI.NSAPI_Terminate();
	}
	
	public static boolean isNeedAutoLogin(){
		return m_appNeedAutoLogin;
	}

	synchronized public static void setAutoLogined(){
		m_appNeedAutoLogin = false;
	}
	
	
	public static Context getContext() {
		if (mInstance != null)
			return mInstance.getApplicationContext();
		else
			return null;
	}	
	
	private void initPreferences() {
	
	}
	
	public SharedPreferences getPreferences(){
		return m_preferences;
	}
	
	public SharedPreferences getUserPreferences(){
		return m_userpreference;
	}

	
	public String getApnType() {
	    	return apnType;
	}
	public static boolean isSystemMemoryVast() {
	    	return bSystemMemoryVast || Build.MODEL.equalsIgnoreCase("GT-I9000");
	}
	private void readSystem() {
		File root = Environment.getDataDirectory();  
		StatFs sf = new StatFs(root.getPath());  
		long blockSize = sf.getBlockSize();  
		long blockCount = sf.getBlockCount();  
		long availCount = sf.getAvailableBlocks();
		if (availCount * blockSize > 200 * 1024 * 1024)
			bSystemMemoryVast = true;
		TupleLogger.getInstance().d(TAG, "Internal block size:"+ blockSize+",block num:"+ blockCount+",total:"+blockSize*blockCount/1024+"KB");  
		TupleLogger.getInstance().d(TAG, "Internal available block num:"+ availCount+",available size:"+ availCount*blockSize/1024+"KB");  
	}
	

    public static boolean isSDCardAvailable() {
    	String status = Environment.getExternalStorageState();
    	if(sdCardAvailable && status.equals(Environment.MEDIA_MOUNTED)){
    		return true;
    	}
    	return false;
    }    

	
	private void readSDCard() {
		String state = Environment.getExternalStorageState();  
		if(Environment.MEDIA_MOUNTED.equals(state)) {  
			File sdcardDir = Environment.getExternalStorageDirectory();  
			TupleLogger.getInstance().d(TAG, "SDCard dir:"+sdcardDir.getAbsolutePath());
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();  
			long blockCount = sf.getBlockCount();  
			long availCount = sf.getAvailableBlocks();  
			TupleLogger.getInstance().d(TAG, "SDcard block size:"+ blockSize+",block num:"+ blockCount+",total:"+blockSize*blockCount/1024+"KB");  
			TupleLogger.getInstance().d(TAG, "SDcard available block num:"+ availCount+",available size:"+ availCount*blockSize/1024+"KB");  
		}
	}  
    

	
	public final static HttpHost getHttpProxy() {
    	HttpHost httpHost = null;
    	try {
    		ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo netInfo = cm.getActiveNetworkInfo();
    		if(netInfo != null && netInfo.isAvailable()) {
    			String typeName = netInfo.getTypeName();
    			String extra = netInfo.getExtraInfo();
    			if(typeName.equalsIgnoreCase("MOBILE")) {
    				if (extra == null) {
    					apnType = "unknown";
    				} else {
    					apnType = extra;
    				}
    				if (extra != null) {
    					if (extra.toLowerCase().startsWith("cmwap") || extra.toLowerCase().startsWith("uniwap") || extra.toLowerCase().startsWith("3gwap")){
        					httpHost = new HttpHost("10.0.0.172",80);
        				} else if (extra.startsWith("#777")){
        					Uri apn_uri = Uri.parse("content://telephony/carriers/preferapn");
        					Cursor c = getContext().getContentResolver().query(apn_uri, null, null, null, null);
        					if (c.getCount() > 0) {
        						c.moveToFirst();
        						String ctapn = c.getString(c.getColumnIndex("user"));
        						if (ctapn != null && !ctapn.equals("")) {
        							if (ctapn.startsWith("ctwap")) {
        								httpHost = new HttpHost("10.0.0.200",80);
        								apnType = "ctwap";
        							} else if(ctapn.toLowerCase().startsWith("wap")) {
        								httpHost = new HttpHost("10.0.0.200",80);
        								apnType = "1x_wap";
        							} else if(ctapn.startsWith("ctnet")) {
        								apnType = "ctnet";
        							} else if(ctapn.toLowerCase().startsWith("card")) {
        								apnType = "1x_net";
        							}
        						}
        					}
        					if(c!=null)
							{
								c.close();
								c=null;
							}
        				}
    				}
    			} else if (typeName.equalsIgnoreCase("WIFI") || typeName.equalsIgnoreCase("WI FI")) {
    				apnType = "wifi";
    			}
    		}
    	} catch (Exception e) {
    		TupleLogger.getInstance().e(TAG, e);
    		httpHost = null;
    		apnType = "unknown";
    	}
    	return httpHost;
    }
	
    
    private class MediaCardStateBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			TupleLogger.getInstance().d(TAG, "Media state changed, intentAction:"+intent.getAction());
			if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
				sdCardAvailable = false;
			} else if (Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())) {
				sdCardAvailable = true;
			}
		}
    	
		public void register() {
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
			intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
			intentFilter.addDataScheme("file");
			registerReceiver(this, intentFilter);
		}
		
    }
    
    

    
}
