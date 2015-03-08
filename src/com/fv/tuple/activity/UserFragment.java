package com.fv.tuple.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;
import com.fv.tuple.adapter.UserAdapter;
import com.fv.tuple.content_provider.ContentProviderUtil;
import com.fv.tuple.content_provider.TupleContentProvider;
import com.fv.tuple.content_provider.TupleContentProvider.TTech;
import com.fv.tuple.open.OpenAPI;
import com.fv.tuple.open.OpenAPIUtil;
import com.fv.tuple.open.OpenAPIUtil.ResponseMessage;
import com.fv.tuple.util.DownloadAsyncTaskPool.DownloadAsyncTaskPoolElement;
import com.fv.tuple.util.HttpGetAsyncTask;
import com.fv.tuple.util.HttpGetAsyncTask.CallInterface;
import com.fv.tuple.util.HttpInterface;
import com.fv.tuple.util.Util;
import com.fv.tuple.widget.TechTabMenuPopupWindow;

public class UserFragment extends RootFragment implements CallInterface {
	/** Called when the activity is first created. */
	private ListView mListView = null;
	private UserAdapter mAdapter = null;

	
	  public static UserFragment newInstance(int index) {
		  UserFragment f = new UserFragment();

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

	        View vv = (View) LayoutInflater.from(this.getActivity()).inflate(R.layout.main_tech, null); 
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
			mIntent=this.getActivity().getIntent();
			getData();
		}
	

void getData()
{
	String pp=mIntent.getStringExtra("type");
	if(mIntent.getStringExtra("type").compareToIgnoreCase("UserDetailActivity")==0)
		return;
	mTechId = mIntent.getStringExtra(("tech_id"));
	initTabMenu();
	startGetUserByTech();
}
	View.OnClickListener menuTechClassClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			TextView t = (TextView) v.findViewById(R.id.id_tv_item);
			Util.showToast("=====>" + (String) (t.getTag()));
			mTechId = (String) (t.getTag());
			startGetUserByTech();
			initTechClassMenuList();
			HideTabMenuTechClass();
		}
	};
	
	View.OnClickListener menuTSClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			TextView t = (TextView) v.findViewById(R.id.id_tv_item);
			Util.showToast("=====>" + (String) (t.getTag()));
			mTechId = (String) (t.getTag());
			if(mTechId.compareToIgnoreCase("0")==0)
			{
			startGetUserByTech();
			}
			else if(mTechId.compareToIgnoreCase("1")==0)
			{
				startGetUserByStudy();
			}
			HideTabMenuTS();
		}
	};
	
	private static final int MENU_TECH_CLASS = 3;
	TechTabMenuPopupWindow.TechMenuAdapter mMenuAdapterTechClass = null;
	TechTabMenuPopupWindow mTabMenuTechClass = null;
	
	TechTabMenuPopupWindow.TechMenuAdapter mMenuAdapterTSClass = null;
	TechTabMenuPopupWindow mTabMenuTSClass = null;
	
	public void ShowTabMenuTechClass() {
		if (mTabMenuTechClass != null) {
			if (mTabMenuTechClass.isShowing())
				mTabMenuTechClass.dismiss();
			else {
				mTabMenuTechClass.showAtLocation(this.getActivity().getWindow().getDecorView(),
						Gravity.TOP, 0, 0);
			}
		}
	}

	public void HideTabMenuTS() {
		if (mTabMenuTSClass != null) {
			if (mTabMenuTSClass.isShowing())
				mTabMenuTSClass.dismiss();
		}
	}
	public void ShowTabMenuTS() {
		if (mTabMenuTSClass != null) {
			if (mTabMenuTSClass.isShowing())
				mTabMenuTSClass.dismiss();
			else {
				mTabMenuTSClass.showAtLocation(this.getActivity().getWindow().getDecorView(),
						Gravity.TOP, 0, 0);
			}
		}
	}

	public void HideTabMenuTechClass() {
		if (mTabMenuTechClass != null) {
			if (mTabMenuTechClass.isShowing())
				mTabMenuTechClass.dismiss();
		}
	}
	void initTSMenuList() {
		ArrayList records = new ArrayList();
		ArrayList recordsId = new ArrayList();
	
		records.add( "教");
		recordsId.add("" + 0);
		
		records.add( "学");
		recordsId.add("" + 1);

		Button b = (Button)getActivity().findViewById(R.id.id_b_teach_study);
		b.setText((String)(records.get(0)));

		if (mMenuAdapterTSClass == null) {
			mMenuAdapterTSClass = new TechTabMenuPopupWindow.TechMenuAdapter(this.getActivity(),
					menuTSClickListener, records, recordsId);
			if (mTabMenuTSClass == null) {
				mTabMenuTSClass = new TechTabMenuPopupWindow(this.getActivity(), null,
						R.drawable.menu_bg, R.style.PopupAnimation);
				mTabMenuTSClass.update();
				mTabMenuTSClass.setMyAdapter(mMenuAdapterTSClass);
			}
		} else {
			mMenuAdapterTSClass.reinitMenu(records, recordsId);
		}
	}
	
	
	
	void initTechClassMenuList() {
		ArrayList recordsTech = new ArrayList();
		ArrayList recordsTechId = new ArrayList();

		String where = TupleContentProvider.TTech.COLUMN_PARENT_TECH_ID + "="
				+ mTechId;

		Cursor cursor = getActivity().getContentResolver()
				.query(TupleContentProvider.TTech.CONTENT_URI, null, where,
						null, null);

		cursor.moveToFirst();

		for (int i = 0; i < cursor.getCount(); i++) {
			int tech_id = cursor.getInt(cursor
					.getColumnIndex(TTech.COLUMN_TECH_ID));
			int p_tech_id = cursor.getInt(cursor
					.getColumnIndex(TTech.COLUMN_PARENT_TECH_ID));
			String tech = cursor.getString(cursor
					.getColumnIndex(TTech.COLUMN_TECH));
			recordsTech.add(tech);
			recordsTechId.add("" + tech_id);
			cursor.moveToNext();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}

		where = TupleContentProvider.TTech.COLUMN_TECH_ID + "=" + mTechId;
		cursor = getActivity().getContentResolver()
				.query(TupleContentProvider.TTech.CONTENT_URI, null, where,
						null, null);

		cursor.moveToFirst();
		if (cursor.getCount() < 1) {
			cursor.close();
			cursor = null;
			return;
		}
		int p_tech_id = cursor.getInt(cursor
				.getColumnIndex(TTech.COLUMN_PARENT_TECH_ID));
		String tech = cursor
				.getString(cursor.getColumnIndex(TTech.COLUMN_TECH));
		recordsTech.add(0, "返回上级");
		recordsTechId.add(0, "" + p_tech_id);

		Button b = (Button) getActivity().findViewById(R.id.id_b_tech_class);
		b.setText(tech);

		cursor.close();
		cursor = null;

		if (mMenuAdapterTechClass == null) {
			mMenuAdapterTechClass = new TechTabMenuPopupWindow.TechMenuAdapter(this.getActivity(),
					menuTechClassClickListener, recordsTech, recordsTechId);
			if (mTabMenuTechClass == null) {
				mTabMenuTechClass = new TechTabMenuPopupWindow(this.getActivity(), null,
						R.drawable.menu_bg, R.style.PopupAnimation);
				mTabMenuTechClass.update();
				mTabMenuTechClass.setMyAdapter(mMenuAdapterTechClass);
			}
		} else {
			mMenuAdapterTechClass.reinitMenu(recordsTech, recordsTechId);
		}
	}
	private static final int USER_DETAIL_REQUEST_CODE = 5;
	void startUserDetail(String userId)
    {
        Intent intentv = new Intent(this.getActivity(), UserDetailActivity.class); 
	    intentv.putExtra("user_id",userId); 
	    startActivityForResult(intentv, USER_DETAIL_REQUEST_CODE);
    }
	private boolean mNeedRefresh=true;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != this.getActivity().RESULT_CANCELED) {

			switch (requestCode) {
			case USER_DETAIL_REQUEST_CODE:
				//startGetData();
				mIntent=data;
					break;
			
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	void initUI() {
		mListView = (ListView) getActivity().findViewById(R.id.id_list_tech_listview);
		// mChangeCursor=getCursor();
		LayoutInflater inflater = (LayoutInflater) this.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflateLayout = inflater.inflate(R.layout.item_list_tech_footer,
				null);
		mListView.addFooterView(inflateLayout, null, false);

		mAdapter = new UserAdapter(this.getActivity(), mChangeCursor, mHandler);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					final int position, long id) {
				TextView t = (TextView) view.findViewById(R.id.item_name);
				startUserDetail((String)(t.getTag()));
				// startMe();
			}
		});

		Button btns = null;
		btns = (Button) getActivity().findViewById(R.id.id_b_tech_class);
		btns.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowTabMenuTechClass();
			}
		});
		btns = (Button) getActivity().findViewById(R.id.id_b_teach_study);
		btns.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowTabMenuTS();
			}
		});
		
	}

	void hideEmptyFooter() {
		View v = (View) getActivity().findViewById(R.id.id_ll_empty_progress);
		v.setVisibility(View.GONE);
	}

	void showEmptyFooter() {
		View v = (View) getActivity().findViewById(R.id.id_ll_empty_progress);
		v.setVisibility(View.VISIBLE);
	}

	private Cursor mChangeCursor;

	public Cursor getCursor() {
		// String where=TupleContentProvider.TUser.COLUMN_ID+"=0";
		String where = null;
		return mChangeCursor = getActivity().getContentResolver().query(
				TupleContentProvider.TUser.CONTENT_URI, null, where, null,
				TupleContentProvider.TUser.COLUMN_ID + " asc");
	}

	Obs obs = new Obs(new Handler());

	private void initObserver() {
		this.getActivity().getContentResolver().registerContentObserver(
				TupleContentProvider.TUser.CONTENT_URI, true, obs);
		// mAdapter.notifyDataSetChanged();
	}

	private void unInitObserver() {
		this.getActivity().getContentResolver().unregisterContentObserver(obs);

	}

	void updateAdapterCursor() {
		mChangeCursor = getCursor();
		mAdapter.changeCursor(mChangeCursor);
	}

	private class Obs extends ContentObserver {
		public int count = 0;

		public Obs(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			updateAdapterCursor();
			count++;
			Util.showToast("===========>" + count);
		}

	}

	public String getDownloadUrl(String handle) {
		// TODO Auto-generated method stub
		return OpenAPI.getInstance().getSystemImageUrl(handle);
	}

	public void downloadPicResult(Object handle, String fileName,
			String filePath) {
		String where = TupleContentProvider.TUser.COLUMN_ID + "="
				+ ((DownloadAsyncTaskPoolElement) handle).mOther;
		ContentProviderUtil.updateDownPictureStatus(
				TupleContentProvider.TUser.CONTENT_URI,
				TupleContentProvider.TUser.COLUMN_PHOTO_STATUS, where, 1);
	}

	void startGetUserByTech() {

		HttpGetAsyncTask t = new HttpGetAsyncTask(UserFragment.this.getActivity(),
				UserFragment.this, null, R.string.app_name, R.string.app_name,
				null);
		String[] params = { OpenAPI.getInstance().getGetUserByTech(), null,
				null };
		t.execute(params);
	}
	
	void startGetUserByStudy() {

		HttpGetAsyncTask t = new HttpGetAsyncTask(UserFragment.this.getActivity(),
				UserFragment.this, null, R.string.app_name, R.string.app_name,
				null);
		String[] params = { OpenAPI.getInstance().getGetUserByStudy(), null,
				null };
		t.execute(params);
	}

	String startUpload(String url) {
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		BasicNameValuePair on = new BasicNameValuePair("tech_id", "" + mTechId);
		nameValuePair.add(on);
		String res = HttpInterface.excutePost(url, nameValuePair);

		return res;
	}

	boolean checkBeforePost() {

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
		showEmptyFooter();
		return checkBeforePost();
	}

	@Override
	public void PostSyncTaskCall(String other, String res, Object otherObject) {
		// TODO Auto-generated method stub
		if (res != null) {
			String result = null;
			ResponseMessage rm = null;

			rm = OpenAPIUtil.parserResponse(res);
			result = rm.mResult;
			if (result.equalsIgnoreCase("success")) {
				unInitObserver();
				ContentProviderUtil.insertSimpleUsersJson(this.getActivity(), rm.mData);
				initObserver();
				updateAdapterCursor();
				Util.showToast(TupleApplication.getContext().getResources()
						.getString(R.string.sucess_user_setting));
			} else if (result.equalsIgnoreCase("fail")) {
				Util.showToast(TupleApplication.getContext().getResources()
						.getString(R.string.error_user_setting));
			}

		}
		hideEmptyFooter();
	}

	public void finishAndReturn() {
		Intent intent = null;
		intent = new Intent(this.getActivity(), TechMainActivity.class);
		intent.putExtra("result", "success");
		getActivity().setResult(0, intent);
	}

	private String mTechId = "";
private Intent mIntent =null;
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}
void initTabMenu()
{
	initTechClassMenuList();
	initTSMenuList() ;
}
}
