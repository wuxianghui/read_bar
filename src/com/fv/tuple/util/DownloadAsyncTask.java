package com.fv.tuple.util;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.fv.tuple.core.TupleCache;

public class DownloadAsyncTask extends
		AsyncTask<String, Integer, String> {
	DownloadCallInterface mc;
	Context mContext=null;
	boolean isCancelled = false;
    private Object handle=null;

    private String mFileName=null;
    private String mFileUrl=null;
    private String mFileSavedPath=null;

	public Object otherObject=null;
	public DownloadAsyncTask(Context context,
			DownloadCallInterface mc,Object handle) {
		mContext=context;
		this.mc = mc;
		this.handle=handle;
		isCancelled = false;
	}
	
	private boolean downLoadAndSave()
	{   
		String mFileName=mc.getSavedFileName(handle);
		if(Util.isPicExist(mFileName))
		{
			return true;
		}
		else
		{
		mFileUrl=mc.getDownloadUrl(handle);
		String fileSavedDir=mc.getSavedPath(handle);
	
    	//if(mFileUrl==null||fileSavedDir==null)
    	if(mFileUrl==null)
    		return false;
    	Bitmap bitmap=HttpInterface.getHttpBitmap(mFileUrl);


    	//int s=mFileUrl.lastIndexOf("/");
        //int e=mFileUrl.length();
       // if (s!=-1 && e!=-1) {
       // 	mFileName= mFileUrl.substring(s+1, e);
        	try {
        		mFileSavedPath=TupleCache.getInstance().saveBitmapToSDCard(bitmap,fileSavedDir,mFileName);
			   
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
		//}
        return true;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String mRes=null;
		String url=params[0];
		if(url==null)
			return null;
		try {
			// publishProgress((int) ((count / (float) length) * 100));
			  try {
				  downLoadAndSave();
				 // mRes= mc.doing(params[0], params[1], params[2]);

		        } catch (Exception e) {
		            e.printStackTrace();
		            isCancelled=true;
		            return null;
		        }
			
			return mRes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mRes;
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
		isCancelled = true;
		mc.downloadResult( handle,mFileName,mFileSavedPath);
	}

	@Override
	protected void onPreExecute() {
		//startProgressBar();
		isCancelled = false;
	}


	public interface DownloadCallInterface {
		
		public String getDownloadUrl(Object handle);
		public String getSavedPath(Object handle);
		public String getSavedFilePath(Object handle);
		public String getSavedFileName(Object handle);
		
		public void downloadResult(Object handle,String fileName,String filePath);
		
	}
}
