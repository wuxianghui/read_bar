package com.fv.tuple.error;

import java.lang.Thread.UncaughtExceptionHandler;

import com.fv.tuple.util.TupleLogger;

/**
 * 
 * @author zhanwei
 * @since 1.0
 */
public class TupleUncaughtExceptionHandler implements UncaughtExceptionHandler{

	private static final String TAG = "UVANUncaughtExceptionHandler";
	
	private UncaughtExceptionHandler exceptionHandler;
	
	public TupleUncaughtExceptionHandler(){
		this.exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
	}
	
	public void uncaughtException(Thread thread, Throwable ex) {
		TupleLogger.getInstance().saveCrashExceptionLog(TAG, ex);
		if(exceptionHandler != null){
			exceptionHandler.uncaughtException(thread, ex);
		}
	}

}
