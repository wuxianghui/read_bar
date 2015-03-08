package com.fv.tuple.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;

import com.fv.tuple.TupleApplication;
import com.fv.tuple.util.TupleLogger;
import com.fv.tuple.util.Util;


public class TupleCache {
	
	private static final String TAG = "CACHE";
//	/**
//	 * cache的阀值，大于此数值的数据就存为文件，否则直接存到SQLite中
//	 */
//	private static final int CACHE_STORE_BIG = 3 * 1024;
//	private static final int CACHE_LIMMIT_SIZE_MIN = 100 * 1024;
//	private static final int CACHE_LIMMIT_SIZE_MAX_INTERNAL = 10 * 1024 * 1024;
//	private static final int CACHE_LIMMIT_SIZE_MAX_SDCARD = 100 * 1024 * 1024;
//	private static final int CACHE_AVAILABLE_DISK_MEMORY = 20 * 1024 * 1024;
	
	private static TupleCache mInstance = null;
	private Context mContext;
	private Object FILE_LOCK = new Object();
	
	//get instance of CloudaryCache
	public static TupleCache getInstance() {
		if (mInstance == null) {
			mInstance = new TupleCache(TupleApplication.getContext());
			return mInstance;
		} else {
			return mInstance;
		}
	}

	private TupleCache(Context context) {
		mContext = context;			
	}

	
	 /**
     * @param data 存入的二进制数据
     * @param dir  存放文件的目录 /snda/cloudary/ 下的目录名,如果是存书，那么为书的ID，如果为null，那么为cache目录
     * @param fileName  /snda/cloudary/dir/下的文件名    如果是存书，那么是书的章节编号,假设是图，那么为图的URL
     * @param isShouldWriteSDCard 是否强制写入SD卡，对于一些图片可以根据需要写入到App cache中（存放与内部存储空间），而书或者证书就写在SD文件目录
     */
	public boolean writeFile(byte[] data,String dir,String fileName, boolean isShouldWriteSDCard) {
		synchronized(FILE_LOCK){
			String dfullName = null;		
			//sdcard可用
			if(TupleApplication.isSDCardAvailable() && isShouldWriteSDCard) {				
				dfullName = Util.createDir(dir) + "/" + fileName;
			} else {//从cache读数据
				if (TupleApplication.isSystemMemoryVast()) {
					dfullName = mContext.getCacheDir().getAbsolutePath()
						+ File.separator + fileName;
				} else {
					if (dir == null) {
						dir = "cache";
					}
					dfullName = Util.createDir(dir) + "/" + fileName;
				}
			}
			BufferedOutputStream bos = null;
			try {

				File file = new File(dfullName);
				if (!file.exists())
					file.createNewFile();
				bos = new BufferedOutputStream(new FileOutputStream(file));
				bos.write(data);
				bos.flush();
				return true;
			} catch (FileNotFoundException e) {
				TupleLogger.getInstance().d(TAG, e);
			} catch (IOException e) {
				TupleLogger.getInstance().d(TAG, e);
			}
			finally {
				if (bos != null)
					try {
						bos.close();
					} catch (IOException e) {
						TupleLogger.getInstance().d(TAG, e);
					}
			}
			return false;
		}
	}

	public byte[] readFile(boolean isShouldReadSD, String dir,String fileName) {
		synchronized (FILE_LOCK) {
			byte[] data = null;
			String dfullName;
			//sdcard可用
			if(TupleApplication.isSDCardAvailable() && isShouldReadSD) {				
				dfullName = Util.createDir(dir) +  "/" + fileName;
			} else {//从cache读数据
				if (TupleApplication.isSystemMemoryVast()) {
					dfullName = mContext.getCacheDir().getAbsolutePath()
						+ File.separator + fileName;
				} else {
					if (dir == null) {
						dir = "cache";
					}
					dfullName = Util.createDir(dir) +  "/" + fileName;
				}
			}
			BufferedInputStream in = null;
			ByteArrayOutputStream out = null;
			try{
				in = new BufferedInputStream(new FileInputStream(dfullName));
				out = new ByteArrayOutputStream(1024);
				byte[] temp = new byte[1024];
				int size = 0;
				while((size = in.read(temp)) != -1){
					out.write(temp, 0, size);
				}
				data = out.toByteArray();
			}catch(Exception e){
				data = null;
				TupleLogger.getInstance().d(TAG, e);
			}finally {
				try{
					if(in != null) in.close();
					if(out != null) out.close();
				}
				catch(IOException ie){
					TupleLogger.getInstance().d(TAG, ie);
				}
			}
			return data;
		}
	};

	public void removeFile(boolean isShouldRemoveSD, String dir,String fileName) {
		synchronized (FILE_LOCK) {
			String dfullName;
			//sdcard可用
			if(TupleApplication.isSDCardAvailable() && isShouldRemoveSD) {				
				dfullName = Util.createDir(dir) + fileName;
			} else {//从cache读数据
				if (TupleApplication.isSystemMemoryVast()) {
					dfullName = mContext.getCacheDir().getAbsolutePath()
						+ File.separator + fileName;
				} else {
					if (dir == null) {
						dir = "cache";
					}
					dfullName = Util.createDir(dir) +  "/" + fileName;
				}
			}
			File file = new File(dfullName);
			if(file.exists()){
				file.delete();
			}
		}
	}

	 public String saveBitmapToSDCard(Bitmap bm, String fileName) throws IOException {   
		 	if(!TupleApplication.isSDCardAvailable())
		 		return null;
	        File dirFile = new File(Util.getSDCardPicDir());   
	        if(!dirFile.exists()){   
	            dirFile.mkdir();   
	        }   
	        String fn=Util.getSDCardPicDir()+File.separator+fileName;
	        File myCaptureFile = new File(fn);   
	        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));   
	        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);   
	        bos.flush();   
	        bos.close();   
	        return fn;
	    }   
	
	 public String saveBitmapToSDCard(Bitmap bm, String fileDir, String fileName) throws IOException {   
		 	if(!TupleApplication.isSDCardAvailable())
		 		return null;
	        File dirFile = new File(fileDir);   
	        if(!dirFile.exists()){   
	            dirFile.mkdir();   
	        }   
	        String fn=fileDir+File.separator+fileName;;
	        File myCaptureFile = new File(fn);   
	        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));   
	        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);   
	        bos.flush();   
	        bos.close();   
	        return fn;
	    }   
	
}
