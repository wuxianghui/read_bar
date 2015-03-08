package com.fv.tuple.activity;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;
import com.fv.tuple.adapter.RequestMeAdapter;
import com.fv.tuple.content_provider.ContentProviderUtil;
import com.fv.tuple.content_provider.TupleContentProvider;
import com.fv.tuple.open.OpenAPI;
import com.fv.tuple.open.OpenAPIUtil;
import com.fv.tuple.open.OpenAPIUtil.ResponseMessage;
import com.fv.tuple.util.HttpGetAsyncTask;
import com.fv.tuple.util.HttpGetAsyncTask.CallInterface;
import com.fv.tuple.util.HttpInterface;
import com.fv.tuple.util.Util;

/**
 * This activity need do some initialize working when login success, get the basic info from server, 
 * contains: 
 * 1. get the user info and insert into db.
 * 2. image server url.
 * 3. video server url.
 * 4. message server url.
 */
public class RequestFragment extends Fragment implements CallInterface {
    /** Called when the activity is first created. */
	
	  public static RequestFragment newInstance(int index) {
		  RequestFragment f = new RequestFragment();

	        // Supply index input as an argument.
	        Bundle args = new Bundle();
	        args.putInt("index", index);
	        f.setArguments(args);

	        return f;
	    }

	    public int getShownIndex() {
	        return getArguments().getInt("index", 0);
	    }
	    
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        if (container == null) {
	            // We have different layouts, and in one of them this
	            // fragment's containing frame doesn't exist.  The fragment
	            // may still be created from its saved state, but there is
	            // no reason to try to create its view hierarchy because it
	            // won't be displayed.  Note this is not needed -- we could
	            // just run the code below, where we would create and return
	            // the view hierarchy; it would just never be used.
	            return null;
	        }

	        View vv = (View) LayoutInflater.from(this.getActivity()).inflate(R.layout.main_request, null); 
	        return vv;
	    }
	    
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	    }
	   
	    
	@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
		       initUI();
		       startGetRequestInfo();
		       startGetTechAssess();
		   
		}

    
void initUI()
{
	 Button b=(Button)this.getActivity().findViewById(R.id.b_me);
     b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		    	ViewFlipper vf = (ViewFlipper) RequestFragment.this.getActivity().findViewById(R.id.f);
		     	  
		    	vf.setDisplayedChild(0);
				
			}
		});
     
     b=(Button)this.getActivity().findViewById(R.id.b_my);
     b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ViewFlipper vf = (ViewFlipper) RequestFragment.this.getActivity().findViewById(R.id.f);
		     	  
		    	vf.setDisplayedChild(1);
			}
		});
     
     b=(Button)this.getActivity().findViewById(R.id.b_history);
     b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ViewFlipper vf = (ViewFlipper) RequestFragment.this.getActivity().findViewById(R.id.f);
		     	  
		    	vf.setDisplayedChild(2);
				
			}
		});
     initMe();
     initMy();
     initHistory();
}
RequestMeAdapter mAdapterMe=null;
RequestMeAdapter mAdapterMy=null;
private Cursor mChangeCursorMe;
private Cursor mChangeCursorMy;

RequestMeAdapter mAdapterHistory=null;
private Cursor mChangeCursorHistory;
public Cursor getCursorMe()
{
	String where=TupleContentProvider.TStudyRequestInfo.COLUMN_USER_ID+"="+TupleApplication.mMeInfoUserId;
	return mChangeCursorMe = this.getActivity().getContentResolver()
	.query(TupleContentProvider.TStudyRequestInfo.CONTENT_URI,
			null,
			where,
			null,
			TupleContentProvider.TStudyRequestInfo.COLUMN_STATE_CHANGE_DATE + " asc");
	
}
public Cursor getCursorMy()
{
	String where=TupleContentProvider.TStudyRequestInfo.COLUMN_USER_TECH_USER_ID+"="+TupleApplication.mMeInfoUserId;
	return mChangeCursorMy = this.getActivity().getContentResolver()
	.query(TupleContentProvider.TStudyRequestInfo.CONTENT_URI,
			null,
			where,
			null,
			TupleContentProvider.TStudyRequestInfo.COLUMN_STATE_CHANGE_DATE + " asc");
	
}
public Cursor getCursorHistory()
{
	String where=TupleContentProvider.TTechAssess.COLUMN_USER_ID+"="+TupleApplication.mMeInfoUserId;
	return mChangeCursorHistory = this.getActivity().getContentResolver()
	.query(TupleContentProvider.TTechAssess.CONTENT_URI,
			null,
			where,
			null,
			TupleContentProvider.TTechAssess.COLUMN_REAL_STUDY_DATE + " asc");
	
}
ObsMe obs = new ObsMe(new Handler());
ObsHistory obsHistory = new ObsHistory(new Handler());

private void initObserver()
{
	this.getActivity().getContentResolver().registerContentObserver(TupleContentProvider.TStudyRequestInfo.CONTENT_URI,
			true, obs);
	this.getActivity().getContentResolver().registerContentObserver(TupleContentProvider.TTechAssess.CONTENT_URI,
			true, obsHistory);
	//mAdapter.notifyDataSetChanged();
}
private void unInitObserver()
{
	this.getActivity().getContentResolver().unregisterContentObserver(obs);
	this.getActivity().getContentResolver().unregisterContentObserver(obsHistory);
	
}
void updateAdapterCursor()
{
	mChangeCursorMe=getCursorMe();
	mAdapterMe.changeCursor(mChangeCursorMe);
	
	mChangeCursorMy=getCursorMy();
	mAdapterMy.changeCursor(mChangeCursorMy);
}

void updateAdapterCursorHistory()
{
	mChangeCursorHistory=getCursorHistory();
	mAdapterHistory.changeCursor(mChangeCursorHistory);
	
}

private class ObsMe extends ContentObserver{
	public int count=0;
	public ObsMe(Handler handler) {
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

private class ObsHistory extends ContentObserver{
	public int count=0;
	public ObsHistory(Handler handler) {
		super(handler);
	}
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		updateAdapterCursorHistory();
		count++;
		Util.showToast("===========>"+count);
	}
	
}

void initMe()
{
	Button b=(Button)this.getActivity().findViewById(R.id.id_b_all_request_me);
    b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
			}
		});
    
     b=(Button)this.getActivity().findViewById(R.id.id_b_success_request_me);
    b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
			}
		});
    
    ListView listView = (ListView) this.getActivity().findViewById(R.id.id_list_me);
    //	mChangeCursor=getCursor();
    	
    mAdapterMe = new RequestMeAdapter(this.getActivity(), mChangeCursorMe,mHandler);
    listView.setAdapter(mAdapterMe);
    listView.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, final View view,
				final int position, long id) {
			Util.showToast(((TextView)(view.findViewById(R.id.id_tv_user ))).getText().toString());
			//startMe();

			
			
		}
	});
}

void initMy()
{
	Button b=(Button)this.getActivity().findViewById(R.id.id_b_all_request_my);
    b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
			}
		});
    
     b=(Button)this.getActivity().findViewById(R.id.id_b_success_request_my);
    b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
			}
		});
    
    ListView listView = (ListView) this.getActivity().findViewById(R.id.id_list_my);
    //	mChangeCursor=getCursor();
    	
    mAdapterMy = new RequestMeAdapter(this.getActivity(), mChangeCursorMy,mHandler);
    listView.setAdapter(mAdapterMy);
    listView.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, final View view,
				final int position, long id) {
			Util.showToast(((TextView)(view.findViewById(R.id.id_tv_user ))).getText().toString());
			//startMe();

			
			
		}
	});
}

void initHistory()
{
	
    ListView listView = (ListView) this.getActivity().findViewById(R.id.id_list_history);
    //	mChangeCursor=getCursor();
    	
    mAdapterHistory = new RequestMeAdapter(this.getActivity(), mChangeCursorHistory,mHandler);
    listView.setAdapter(mAdapterHistory);
    listView.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, final View view,
				final int position, long id) {
			Util.showToast(((TextView)(view.findViewById(R.id.id_tv_user ))).getText().toString());
			//startMe();

			
			
		}
	});
}
@Override
public void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
}

@Override
public void onStart() {
	// TODO Auto-generated method stub
	super.onStart();


}

@Override
public void onStop() {
	// TODO Auto-generated method stub
	super.onStop();
}
final String TASK_REQUEST_INFO="request_info";
final String TASK_TECH_ASSESS="tech_assess";
void startGetRequestInfo()
{
	HttpGetAsyncTask t=new HttpGetAsyncTask(RequestFragment.this.getActivity(),RequestFragment.this,null,R.string.app_name,R.string.app_name,null);
	String[] params = { OpenAPI.getInstance().getGetRequestInfo(), null ,TASK_REQUEST_INFO};
	t.execute(params);
}
void startGetTechAssess()
{
	HttpGetAsyncTask t=new HttpGetAsyncTask(RequestFragment.this.getActivity(),RequestFragment.this,null,R.string.app_name,R.string.app_name,null);
	String[] params = { OpenAPI.getInstance().getGetTechAssess(), null ,TASK_TECH_ASSESS};
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
			if(other.compareToIgnoreCase(TASK_REQUEST_INFO)==0)
			{
			unInitObserver();
			ContentProviderUtil.insertStudyRequestInfoJson(this.getActivity(),rm.mData);
			initObserver();
			
			updateAdapterCursor();
			Util.showToast(TupleApplication.getContext().getResources().getString(R.string.sucess_user_setting));
			}
			else if(other.compareToIgnoreCase(TASK_TECH_ASSESS)==0)
			{
				unInitObserver();
				ContentProviderUtil.insertTechAssess(this.getActivity(),rm.mData);
				initObserver();
				
				updateAdapterCursorHistory();
				Util.showToast(TupleApplication.getContext().getResources().getString(R.string.sucess_user_setting));

			}
		}
		else if(result.equalsIgnoreCase("fail"))
		{
			Util.showToast(TupleApplication.getContext().getResources().getString(R.string.error_user_setting));
		}
		
	}
}

public Handler mHandler = new Handler() {
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 1001:
			
			break;
	
		case 1002:
		{
			
		}
			break;
		}
	
		super.handleMessage(msg);
	}
};

}
