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
//	 * cache�ķ�ֵ�����ڴ���ֵ�����ݾʹ�Ϊ�ļ�������ֱ�Ӵ浽SQLite��
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
     * @param data ����Ķ���������
     * @param dir  ����ļ���Ŀ¼ /snda/cloudary/ �µ�Ŀ¼��,����Ǵ��飬��ôΪ���ID�����Ϊnull����ôΪcacheĿ¼
     * @param fileName  /snda/cloudary/dir/�µ��ļ���    ����Ǵ��飬��ô������½ڱ��,������ͼ����ôΪͼ��URL
     * @param isShouldWriteSDCard �Ƿ�ǿ��д��SD��������һЩͼƬ���Ը�����Ҫд�뵽App cache�У�������ڲ��洢�ռ䣩���������֤���д��SD�ļ�Ŀ¼
     */
	public boolean writeFile(byte[] data,String dir,String fileName, boolean isShouldWriteSDCard) {
		synchronized(FILE_LOCK){
			String dfullName = null;		
			//sdcard����
			if(TupleApplication.isSDCardAvailable() && isShouldWriteSDCard) {				
				dfullName = Util.createDir(dir) + "/" + fileName;
			} else {//��cache������
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
			//sdcard����
			if(TupleApplication.isSDCardAvailable() && isShouldReadSD) {				
				dfullName = Util.createDir(dir) +  "/" + fileName;
			} else {//��cache������
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
			//sdcard����
			if(TupleApplication.isSDCardAvailable() && isShouldRemoveSD) {				
				dfullName = Util.createDir(dir) + fileName;
			} else {//��cache������
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
