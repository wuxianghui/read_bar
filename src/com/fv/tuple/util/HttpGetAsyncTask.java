package com.fv.tuple.util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import com.fv.tuple.TupleApplication;

public class HttpGetAsyncTask extends
		AsyncTask<String, Integer, String> {
	CallInterface mc;
	ProgressDialog progressDialog;
	Context mContext=null;
	boolean isCancelled = false;
	int titleId;
	int contentId;
	Message cancelledMessage;
	String mRes=null;

	String params0=null;
	String  params1=null;
	String params2=null;
	public Object otherObject=null;
	public HttpGetAsyncTask(Context context,
			CallInterface mc, Message cancelledMessage, int titleId,
			int contentId,Object other) {
		mContext=context;
		this.mc = mc;
		this.titleId = titleId;
		this.contentId = contentId;
		this.cancelledMessage = cancelledMessage;
		isCancelled = false;
		otherObject=other;
	}

	void startProgressBar() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(mContext);
			String title;
			if (titleId > 0)
				title = TupleApplication.getContext().getResources().getString(titleId);
			else
				title = "";
			String message = TupleApplication.getContext().getResources().getString(contentId);
			progressDialog.setTitle(title);
			progressDialog.setMessage(message);
			progressDialog.setCancelable(true);
			if (this.cancelledMessage != null)
				progressDialog.setCancelMessage(this.cancelledMessage);
			progressDialog.show();
		} else
			progressDialog.show();
	}

	void endProgressBar() {
		progressDialog.dismiss();
	}

	@Override
	protected String doInBackground(String... params) {
		if(isCancelled)
			return null;
		try {
			// publishProgress((int) ((count / (float) length) * 100));
			  try {
				  params0=params[0];
				  params1=params[1];
				  params2=params[2];
				  mRes= mc.doing(params[0], params[1], params[2],otherObject);

		        } catch (Exception e) {
		            e.printStackTrace();
		            isCancelled=true;
		            return null;
		        }
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		isCancelled = true;
		//endProgressBar();
	}

	@Override
	protected void onPostExecute(String result) {
		//endProgressBar();
		mc.PostSyncTaskCall(params2,mRes,otherObject);
		isCancelled = true;
	}

	@Override
	protected void onPreExecute() {
		//startProgressBar();
		isCancelled = false;
		isCancelled=!mc.PreSyncTaskCall();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {

	}

	public interface CallInterface {
		public String doing(String mainPath, String viaPath, String other,Object otherObject) throws ClientProtocolException, IOException;

		public boolean PreSyncTaskCall();

		public void PostSyncTaskCall(String other,String res,Object otherObject);
	}
}
