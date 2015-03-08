package com.fv.tuple.util;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;

public class DownloadAsyncTaskPool {
	
	public static class DownloadAsyncTaskPoolElement
	{
		public String mPic="";
		public String mOther="";
		public  DownloadAsyncTaskPoolElement()
		{
			
			mPic="";
			mOther="";
		}
		public  DownloadAsyncTaskPoolElement(DownloadAsyncTaskPoolElement other)
		{
			mPic=other.mPic;
			mOther=other.mOther;
		}
		public boolean compareTo(DownloadAsyncTaskPoolElement other)
		{
			if((this.mPic.compareTo(other.mPic)==0)&&(this.mOther.compareTo(other.mOther)==0))
			return true;
			return false;
		}
	}
	
private ArrayList<DownloadAsyncTaskPoolElement> mTask=new ArrayList();
private Object LOCK = new Object();
private AsyncTask mCurrentAsyncTask=null;
DownloadAsyncTaskPoolInterface mc;
Context mContext;
private boolean isPause=false;
public DownloadAsyncTaskPool(Context context,
		DownloadAsyncTaskPoolInterface mc) {
	mContext=context;
	this.mc = mc;
}
public boolean addSingleTask(DownloadAsyncTaskPoolElement task)
{
	for(int i=0;i<mTask.size();i++)
	{
		if(((DownloadAsyncTaskPoolElement)mTask.get(i)).compareTo(task))
			return false;
	}
	synchronized(LOCK){
		mTask.add(task);
	}
	runTask();
	return true;
}

public void removeTask(DownloadAsyncTaskPoolElement task)
{
	for(int i=0;i<mTask.size();i++)
	{
		if(((DownloadAsyncTaskPoolElement)mTask.get(i)).compareTo(task))
		{
			synchronized(LOCK){
			mTask.remove(i);
			}
			return;
		}
	}
	
}

public void pause()
{
	isPause=true;
	if(mCurrentAsyncTask!=null)
	{
		if(mCurrentAsyncTask.getStatus().name()=="RUNNING")
		{
			mCurrentAsyncTask.cancel(true);
			mCurrentAsyncTask=null;
		}
	}
}

public void resume()
{
	isPause=false;
	runTask();
}

public DownloadAsyncTaskPoolElement runTask()
{
	if(isPause)
		return null;
	if(mTask.size()<1)
		return null;
	DownloadAsyncTaskPoolElement task=new DownloadAsyncTaskPoolElement((DownloadAsyncTaskPoolElement) mTask.get(mTask.size()-1));
	if(mCurrentAsyncTask!=null)
	{
		if(mCurrentAsyncTask.getStatus().name()=="RUNNING")
			return null;
		mCurrentAsyncTask=mc.startOneTask(task);
		mTask.remove(mTask.get(mTask.size()-1));
	}
	else
	{
		mCurrentAsyncTask=mc.startOneTask(task);
		mTask.remove(mTask.get(mTask.size()-1));
	}
	return task;
}

public boolean addTask(DownloadAsyncTaskPoolElement task)
{
	synchronized(LOCK){
		mTask.add(0, task);
	}
	return true;
}

public interface DownloadAsyncTaskPoolInterface {
	
	public AsyncTask startOneTask(DownloadAsyncTaskPoolElement task);
	
}
}
