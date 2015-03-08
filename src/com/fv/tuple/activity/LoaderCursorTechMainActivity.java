package com.fv.tuple.activity;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;
import com.fv.tuple.adapter.TechRootMenuSimpleCursorAdapter;
import com.fv.tuple.content_provider.ContentProviderUtil;
import com.fv.tuple.content_provider.TupleContentProvider;
import com.fv.tuple.open.OpenAPI;
import com.fv.tuple.open.OpenAPIUtil;
import com.fv.tuple.open.OpenAPIUtil.ResponseMessage;
import com.fv.tuple.util.DownloadAsyncTaskPool.DownloadAsyncTaskPoolElement;
import com.fv.tuple.util.HttpGetAsyncTask;
import com.fv.tuple.util.HttpGetAsyncTask.CallInterface;
import com.fv.tuple.util.HttpInterface;
import com.fv.tuple.util.Util;



public class LoaderCursorTechMainActivity extends RootActivity implements CallInterface{
    /** Called when the activity is first created. */
    private ListView mListView=null;
    
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tech_root_menu);
        
        initUI();
        initRiverCursorLoader();

       // initObserver();
        startGetMenu();
    }
    

void initUI()
{
	mListView = (ListView) findViewById(R.id.id_list_tech_root_menu_listview);
    //	mChangeCursor=getCursor();
    	
	
	//mListView.setAdapter(mAdapter);
	mListView.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, final View view,
				final int position, long id) {
			TextView t=(TextView)view.findViewById(R.id.item_tech );
		}
	});
}


public String getDownloadUrl(String handle) {
	// TODO Auto-generated method stub
   return OpenAPI.getInstance().getSystemImageUrl(handle);
}

public void downloadPicResult(Object handle, String fileName, String filePath)
{
	String where=TupleContentProvider.TTech.COLUMN_ID+"="+((DownloadAsyncTaskPoolElement)handle).mOther;
	where=null;
	ContentProviderUtil.updateDownPictureStatus(TupleContentProvider.TTech.CONTENT_URI,TupleContentProvider.TTech.COLUMN_PIC_STATUS,where,1);
}


void startGetMenu()
{
	HttpGetAsyncTask t=new HttpGetAsyncTask(LoaderCursorTechMainActivity.this,LoaderCursorTechMainActivity.this,null,R.string.app_name,R.string.app_name,null);
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
			ContentProviderUtil.insertTechJson(this,rm.mData);
			Util.showToast(TupleApplication.getContext().getResources().getString(R.string.sucess_user_setting));
		}
		else if(result.equalsIgnoreCase("fail"))
		{
			Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_user_setting));
		}
		
	}
}
TechRootMenuSimpleCursorAdapter mAdapter=null;
private void initRiverCursorLoader() {
    // TODO Auto-generated method stub
//First, create (and set up) the CursorAdapter.the cursor is null

	mAdapter = new TechRootMenuSimpleCursorAdapter(LoaderCursorTechMainActivity.this,
	         null,R.layout.item_list_tech_root_menu,
	         new String[]{
    		TupleContentProvider.TTech.COLUMN_ID,
    		TupleContentProvider.TTech.COLUMN_TECH_ID,
    		TupleContentProvider.TTech.COLUMN_PARENT_TECH_ID,
    		TupleContentProvider.TTech.COLUMN_TECH,
    		TupleContentProvider.TTech.COLUMN_COMMENT,
    		TupleContentProvider.TTech.COLUMN_PIC,
    		TupleContentProvider.TTech.COLUMN_PIC_STATUS,
    		},
	        null, 0,mHandler);

    //Next, initialize the loader.
    getSupportLoaderManager().initLoader(0,null,
                    new LoaderCallbacks<Cursor>(){

                            /**The LoaderManager calls onCreateLoader(int id, Bundle args).
                             * returns a subclass of the Loader<Cursor>
                             * This Cursorloader will perform the initial query and will update itself 
                             * when the data set changes.
                             */
                            @Override
                            public CursorLoader onCreateLoader(int id, Bundle arg1) {
                                    Log.d("listview","on create loader");
                                    String where=TupleContentProvider.TTech.COLUMN_PARENT_TECH_ID+"=0";
                                    where=null;

                                    
                                    return new CursorLoader(LoaderCursorTechMainActivity.this, TupleContentProvider.TTech.CONTENT_URI,
                                    		new String[]{
                                    		TupleContentProvider.TTech.COLUMN_ID,
                                    		TupleContentProvider.TTech.COLUMN_TECH_ID,
                                    		TupleContentProvider.TTech.COLUMN_PARENT_TECH_ID,
                                    		TupleContentProvider.TTech.COLUMN_TECH,
                                    		TupleContentProvider.TTech.COLUMN_COMMENT,
                                    		TupleContentProvider.TTech.COLUMN_PIC,
                                    		TupleContentProvider.TTech.COLUMN_PIC_STATUS,
                                    		}, where, null,
                                    		TupleContentProvider.TTech.COLUMN_TECH_ID + " asc");
                                    
                            }

                            /**
                             * The queried cursor is passed to the adapter.
                             * Immediately after the CursorLoader is instantiated and returned , 
                             * the CursorLoader performs the initial query on a separate thread and a cursor is returned. 
                             * When the CursorLoader finishes the query, it returns the newly queried cursor to the LoaderManager,
                             * which then passes the cursor to the onLoadFinished method. 
                             * From the documentation, "the onLoadFinished method is called 
                             * when a previously created loader has finished its load."
                             * @param arg0
                             * @param cursor
                             */
                            @Override
                            public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
                                    Log.d("listview","on create finished");
                                    mAdapter.swapCursor(cursor);
                                    
                            }

                            @Override
                            public void onLoaderReset(Loader<Cursor> arg0) {
                                    Log.d("listview","on create reset");
                                    mAdapter.swapCursor(null);
                            }

			
            
    });
    mListView.setAdapter(mAdapter);
    mListView.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
                    Util.showToast("listItem"+ ">>" + id);

                    
            }
            
    });                
}    



}
