package com.fv.tuple.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

import com.fv.tuple.TupleApplication;
import com.fv.tuple.core.Constants;


/**
 * ������
 * @author zhanwei
 * @since 1.0
 */
public class Util {
	
	private static final String TAG = "Util";

    private static int curSDKVersion = -1;
    
    private Object FILE_LOCK = new Object();
    
    public static int getCurrentSDKVersion()
    {
    	if (curSDKVersion == -1)
    	{
    		curSDKVersion = android.os.Build.VERSION.SDK_INT;
    	}
    	return curSDKVersion;
    }
	
    private static String appVersionName = null;
    
    public static String getApplicationVersionName(ContextWrapper contextWrapper) {
    	if (appVersionName != null)
    		return appVersionName;
		try {			  
			PackageInfo pinfo = contextWrapper.getPackageManager().getPackageInfo(contextWrapper.getPackageName(), 0);
			appVersionName = pinfo.versionName;
			return appVersionName;		
		} catch (android.content.pm.PackageManager.NameNotFoundException e)	{
			TupleLogger.getInstance().e(TAG, e);
			return "";
		}
    }
    
    private static int s_appVersionCode = Integer.MAX_VALUE;
    public static int getApplicationVersionCode(ContextWrapper contextWrapper) {
    	if (s_appVersionCode != Integer.MAX_VALUE)
    		return s_appVersionCode;
		try {			  
			PackageInfo pinfo = contextWrapper.getPackageManager().getPackageInfo(contextWrapper.getPackageName(), 0);
			s_appVersionCode = pinfo.versionCode;
			return s_appVersionCode;		
		} catch (android.content.pm.PackageManager.NameNotFoundException e) {
			return Integer.MAX_VALUE;
		}
    }


	
	  /**
	   *  �жϴ洢���Ƿ���� 
	   * @return
	   */
	 public static boolean checkSDCard() {   
	    
	    if(android.os.Environment.getExternalStorageState().equals
	    (android.os.Environment.MEDIA_MOUNTED)) {
	      return true;
	    }
	    else
	    {
	      return false;
	    }
	  }    
	
	  
	public static String encode(String s) {
		String result;
		try {
			result = URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			result = URLEncoder.encode(s);
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean Move(File srcFile, String destPath) {
		// Destination directory
		File dir = new File(destPath);

		// Move file to new directory
		boolean success = srcFile.renameTo(new File(dir, srcFile.getName()));

		return success;
	}

	public static boolean Move(String srcFile, String destPath) {
		// File (or directory) to be moved
		File file = new File(srcFile);

		// Destination directory
		File dir = new File(destPath);

		// Move file to new directory
		boolean success = file.renameTo(new File(dir, file.getName()));

		return success;
	}

	/**
	 * ����10���ƣ�����Ϊpwd_len�ֽڵ��ַ�
	 * @param pwd_len
	 * @return
	 */
	public static String genHexRandomNum(int pwd_len){
		final int maxNum = 10;
		int i;
		int count = 0;
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		StringBuffer pwd = new StringBuffer("");  
        Random r = new Random();  
        while (count < pwd_len) {  
            // �����������ȡ����ֵ����ֹ���ɸ�����  
            i = Math.abs(r.nextInt(maxNum)); // ���ɵ������Ϊ15  
            if (i >= 0 && i < str.length) {  
                pwd.append(str[i]);  
                count++;  
            }
        }
        return pwd.toString();
	}	

	
	public static String createDir(String dirName){
		File dir = null;
		if (dirName != null) {
			dir = new File(Environment.getExternalStorageDirectory(), File.separator+"tuple"+File.separator+dirName);
		} else {
			dir = new File(Environment.getExternalStorageDirectory(), File.separator+"tuple");
		}
		if(!dir.exists() || !dir.isDirectory()){
			dir.mkdirs();
		}
		return dir.getAbsolutePath();
	}
	
	/**
	 * 
	 * @param data ����Ķ���������
	 * @param dir /snda/cloudary/ �µ�Ŀ¼��
	 * @param fileName /snda/cloudary/dir/�µ��ļ���
	 * @return
	 */
    public static String saveDataToFile(byte[] data,String dir,String fileName) {
    	String path = createDir(dir);
    	String filePath = path + fileName;
        FileOutputStream f = null;
        try {
            f = new FileOutputStream(filePath);
            f.write(data);
        } catch (IOException e) {
            return null;
        } 
        return filePath;
    }
    
    /**
     * �ж�Android�� �ֻ���Ӫ������
     */
    
    public static int getOperators(Context context){
    	TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  
    	
    	String imsi = telManager.getSubscriberId();  
    	if(imsi!=null){  
    	    if(imsi.startsWith("46000") || imsi.startsWith("46002")){  
    	        //�й��ƶ�  
    	    	return Constants.OPERATORSID_YD ;
    	    }else if(imsi.startsWith("46001")){  
    	        //�й���ͨ 
    	    	return Constants.OPERATORSID_LT ;
    	    }else {
    	    	return Constants.OPERATORSID_UNSUPPORT ;
    	    }
    	}
		return Constants.OPERATORSID_UNSUPPORT ;  
    }
    

    
    /**
     * ����SNB�Ƿ���SD���д���
     * @param dirPath
     */
    
    public static boolean isBookExist(String rpidbookid){
    	if(TupleApplication.isSDCardAvailable()){
    		String path =  Environment.getExternalStorageDirectory().toString() + "/snda/cloudary/book/" + rpidbookid ;
    		File dir = new File(path);
    		if(dir.exists()){
    			if(dir.list()!= null && dir.list().length > 0){
    				return true ;
    			}
    			return false ;
    		}
    	    return false ;
    	}    	
    	return false ;
    }
    
    /**
     * �����½�snb�Ƿ���sd���д���
     * @param dirPath
     */
    
    public static boolean isChapterExist(String rpidbookid,int chapterid){
    	if(TupleApplication.isSDCardAvailable()){
    		String path =  Environment.getExternalStorageDirectory().toString() + "/snda/cloudary/book/" + rpidbookid + "/" + chapterid + ".snb" ;
    		File snbFile = new File(path);
    		return snbFile.exists();
    	}
    	return false ;
    }
    
    public static void cleanDirAllFile(String dirPath) {
    	if(TupleApplication.isSDCardAvailable()) {
			// delete from SDCARD
			File dir = new File(dirPath);
			if (dir != null) {
		        // Clear the whole cache. Coolness.
		        String[] children = dir.list();
		        if (children != null) { // children will be null if the directory does not exist.
		            for (int i = 0; i < children.length; i++) {
		                File child = new File(dir, children[i]);	               
	                    child.delete();
		            }
		        }
			}
    	}
   }
    

    
    public static void sendSMS(String content ,String mobile){
    	SmsManager smsManager = SmsManager.getDefault();
    	smsManager.sendTextMessage(mobile, null, content, null,null);
    }
    
    /**
     * ����pic�ļ��Ƿ���SD���д���
     * @param pic
     */
    
    public static boolean isPicExist(String pic){
    	if(TupleApplication.isSDCardAvailable()){
    		String path =  mSDCardPicDir+ File.separator+ pic ;
    		File dir = new File(path);
    		if(dir.exists()){
    				return true ;
    		}
    	    return false ;
    	}    	
    	return false ;
    }
    
    /**
     * ����ͼƬ�ļ���·��
     * @param dirPath
     */
    public static String getSDCardPicDir(){
    	if(TupleApplication.isSDCardAvailable()){
    		String path =  mSDCardPicDir;
    		File file = new File(path);
    		if(file.exists())
    			return path;
    	}
    	return null;
    }
    
    /**
     * ����ͼƬ�ļ���·��
     * @param dirPath
     */
    public static String mSDCardPicDir="";
    public static String createSDCardPicDir(){
    	if(TupleApplication.isSDCardAvailable()){
    		mSDCardPicDir=createDir("image");
    	}
    	return null;
    }
    
    public static Bitmap getSDCardImage(String name) {
    	String path =  mSDCardPicDir+ File.separator+name ;
        return getLoacalBitmap(path); //�ӱ���ȡͼƬ(��cdcard�л�ȡ)  //
      }
    
    public static Bitmap getSDCardImage1(String path) {
    	
        return getLoacalBitmap(path); //�ӱ���ȡͼƬ(��cdcard�л�ȡ)  //
      }

    /**
    * ���ر���ͼƬ
    * @param url
    * @return
    */
    @SuppressWarnings("finally")
	public static Bitmap getLoacalBitmap(String url) {
    	if(url==null)
    		return null;
    	 try {
             FileInputStream fis = new FileInputStream(url);
             return BitmapFactory.decodeStream(fis);  ///����ת��ΪBitmapͼƬ        

          } catch (FileNotFoundException e) {
             e.printStackTrace();
             return null;
        }
         }
    
    
    public static Toast mInfoToast;
    
	public static void showToast(String text)
	{
		if(mInfoToast==null)
			mInfoToast=Toast.makeText(TupleApplication.getContext(), "", Toast.LENGTH_SHORT);
     	mInfoToast.setText(text);
     	mInfoToast.setGravity(Gravity.CENTER, 
				 0, 0);
     	mInfoToast.show();
	}
    
    
}
