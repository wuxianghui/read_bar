package com.fv.tuple.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.fv.tuple.TupleApplication;
import com.fv.tuple.TupleConfig;


/**
 * 
 * @author zhanwei
 * @since 1.0
 */
public class TupleLogger {

	private static final String TAG = "EBClientLogger";
	// Logger��ͷ��UVANConfig�е�DEBUG_LOG���ض�����DEBUGΪ׼
	private static final boolean DEBUG = TupleConfig.DEBUG_LOG;
	private static TupleLogger m_instance = null;
	
	public static final int LOG_LEVEL_INFO = 0;
	public static final int LOG_LEVEL_DEBUG = 1;
	public static final int LOG_LEVEL_EXCEPTION = 2;
	public static final int LOG_LEVEL_CRASH = 3;
	public static final int LOG_LEVEL_LOC = 4;
	public static final int LOG_LEVEL_SPEED = 5;
	
	private static String LOG_PATH = "";
	private static final String LOG_INFO_FILENAME = "CLOUDARY_I";
	private static final String LOG_DEBUG_FILENAME = "CLOUDARY_D";
	private static final String LOG_EXCEPTION_FILENAME = "CLOUDARY_E";
	private static final String LOG_CRASH_FILENAME = "CLOUDARY_C";
	private static final String LOG_LOC_FILENAME = "LOC";
	private static final String LOG_SPEED_FILENAME = "SPE";
	private static final String LOG_FILE_EXTNAME = ".log";
	
	public static final int CRASH_LOG_MAX_FILE_LENGTH = 2 * 1024;
	public static final int EXCEPTION_LOG_MAX_FILE_LENGTH = 500 * 1024;
	public static final int LOC_LOG_MAX_FILE_LENGTH = 10 * 1024;
	public static final int SPE_LOG_MAX_FILE_LENGTH = 10 * 1024;
	
	private Object FILE_LOCK = new Object();

	public synchronized static TupleLogger getInstance() {
		if (m_instance == null) {
			m_instance = new TupleLogger();
		} 
		return m_instance;
	}
	
	private TupleLogger() {
		LOG_PATH = createLogPath();
	}

	/**
	 * log�ļ��洢·��
	 * @return
	 */
	private String createLogPath(){
		File dir = new File(Environment.getExternalStorageDirectory(), "/fv/ebclient"+File.separator+"log");
//		File oldDir = new File(Environment.getExternalStorageDirectory(), "cloudary_log");
		if(!dir.exists() || !dir.isDirectory()){
			dir.mkdirs();
		}
//		if (oldDir.exists() && oldDir.isDirectory())
//		{
//	        String[] children = oldDir.list();
//	        if (children != null) { // children will be null if the directory does not exist.
//	            for (int i = 0; i < children.length; i++) {
//	                File child = new File(oldDir, children[i]);
//	                Util.Move(child, dir.getAbsolutePath());
//	                CloudaryLogger.getInstance().d(TAG, "Moving: " + child);
//	            }
//	        }
//	        oldDir.delete();
//		}
		return dir.getAbsolutePath();
	}
	
	public void d(String tag, String message){
		if(DEBUG){
			Log.d(tag, message);
		}
		saveLogToFile(String.format("%s: %s", tag, message), LOG_LEVEL_DEBUG);
	}
	
	public void d(String tag, Throwable ex){
		if(DEBUG){
			Log.d(tag, "Cloudary Exception:", ex);
		}
		saveLogToFile(String.format("%s: %s", tag, getDebugReport(ex)), LOG_LEVEL_DEBUG);
	}
	
	public void i(String tag, String message){
		if(DEBUG){
			Log.i(tag, message);	
		}
		saveLogToFile(String.format("%s: %s", tag, message), LOG_LEVEL_INFO);
	}
	
	public void e(String tag, String message){
		if(DEBUG){
			Log.e(tag, message);
		}
		saveLogToFile(String.format("%s: %s", tag, message), LOG_LEVEL_EXCEPTION);
	}
	
	public void e(String tag, Throwable ex){
		if(DEBUG){
			Log.e(tag, "Cloudary Exception:", ex);
		}
		saveLogToFile(String.format("%s: %s", tag, getDebugReport(ex)), LOG_LEVEL_EXCEPTION);
	}
	
	public void v(String tag, String message){
		if(DEBUG){
			Log.v(tag, message);
		}
	}
	
	public void w(String tag, String message){
		if(DEBUG){
			Log.w(tag, message);
		}
	}
	
	//��¼crash��־
	private String getDebugReport(Throwable exception){
		NumberFormat theFormatter = new DecimalFormat("#0.");
		StringBuilder theErrReport = new StringBuilder();
		theErrReport.append(TupleApplication.getContext().getPackageName()+" generated the following exception:\n");
		if(exception != null){
			theErrReport.append(exception.toString()+"\n\n");
			//stack trace
			StackTraceElement[] theStackTrace = exception.getStackTrace();
			if(theStackTrace.length > 0){
				theErrReport.append("======== Stack trace =======\n");
				int length = theStackTrace.length;
				for(int i=0;i<length;i++){
					theErrReport.append(theFormatter.format(i+1)+"\t"+theStackTrace[i].toString()+"\n");
				}
				theErrReport.append("=====================\n\n");
			}
			Throwable theCause = exception.getCause();
			if(theCause != null){
				theErrReport.append("======== Cause ========\n");
				theErrReport.append(theCause.toString()+"\n\n");
				theStackTrace = theCause.getStackTrace();
				int length = theStackTrace.length;
				for(int i=0;i<length;i++){
					theErrReport.append(theFormatter.format(i+1)+"\t"+theStackTrace[i].toString()+"\n");
				}
				theErrReport.append("================\n\n");
			}
			PackageManager pm = TupleApplication.getContext().getPackageManager();
			PackageInfo pi;
			try{
				pi = pm.getPackageInfo(TupleApplication.getContext().getPackageName(), 0);
			}catch(NameNotFoundException e){
				pi = new PackageInfo();
				pi.versionName = "unknown";
				pi.versionCode = 0;
			}
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			theErrReport.append("======== Environment =======\n");
			theErrReport.append("Time="+format.format(now)+"\n");
			theErrReport.append("Device="+Build.FINGERPRINT+"\n");
			try {
				Field mfField = Build.class.getField("MANUFACTURER");
				theErrReport.append("Manufacturer="+mfField.get(null)+"\n");
			} catch (SecurityException e) {
			} catch (NoSuchFieldException e) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
			theErrReport.append("Model="+Build.MODEL+"\n");
			theErrReport.append("Product="+Build.PRODUCT+"\n");
			theErrReport.append("App="+TupleApplication.getContext().getPackageName()+", version "+pi.versionName+" (build "+pi.versionCode+")\n");
			theErrReport.append("=========================\nEnd Report");
		}else{
			theErrReport.append("the exception object is null\n");
		}
		
		return theErrReport.toString();
	}
	

	
	public void saveCrashExceptionLog(String tag, Throwable exception){
		String report = getDebugReport(exception);
		if(DEBUG){
			Log.d(tag, report);
		}
		//д���ļ�
		saveLogToFile(report, LOG_LEVEL_CRASH);
	}
	
	/**
	 * 
	 * @param tag
	 * @param exception
	 */
	public void saveCrashExceptionLog(String tag, String log, int logLevel){
		if(DEBUG){
			Log.d(tag, log);
		}
		//д���ļ�
		saveLogToFile(log, logLevel);
	}
	
	/**
	 * ����־д���ļ���
	 * crash��־ÿ���ļ����Ϊ2K��������С�Ľ��в�֡�
	 * ���ڲ�ֵ��ļ�����Ϊ�ļ�����ʱ��
	 * exception��־��ÿ���ļ����Ϊ500k��������С�Ľ��в�֣��������������ɵ��ں������_B�ĺ�׺
	 * debug��־��ֻ����debugģʽ�²Ż�д���ļ��������ļ���С����
	 * info��־��ͬ��
	 * @param message
	 */
	public void saveLogToFile(String message, int mode){
		String text = message;
		//sdcard����
		if(TupleApplication.isSDCardAvailable()){
			synchronized(FILE_LOCK){
				LOG_PATH = createLogPath();
				switch(mode){
				case LOG_LEVEL_INFO:
				case LOG_LEVEL_DEBUG:
					if(DEBUG){
						String dfullName = "";
						if(mode == LOG_LEVEL_INFO){
							dfullName = LOG_PATH + File.separator + LOG_INFO_FILENAME + LOG_FILE_EXTNAME;
						}else{
							dfullName = LOG_PATH + File.separator + LOG_DEBUG_FILENAME + LOG_FILE_EXTNAME;
						}
						String currentTimeString = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss ").format(new Date());
						text = currentTimeString + message;
						writeFile(dfullName, text);
					}
					break;
				case LOG_LEVEL_EXCEPTION:
					String efullName = LOG_PATH + File.separator + LOG_EXCEPTION_FILENAME + LOG_FILE_EXTNAME;
					File efile = new File(efullName);
					if(efile.exists() && efile.isFile()){
						if(efile.length() >= EXCEPTION_LOG_MAX_FILE_LENGTH){
							String newEFileName = LOG_PATH + File.separator + LOG_EXCEPTION_FILENAME + "_B" + LOG_FILE_EXTNAME;
							File bfile = new File(newEFileName);
							if(bfile.exists() && bfile.isFile()){
								deleteFile(newEFileName);
							}
							efile.renameTo(new File(newEFileName));
						}
					}		
					writeFile(efullName, message);
					break;
				case LOG_LEVEL_CRASH:
					String fullName = LOG_PATH + File.separator + LOG_CRASH_FILENAME + LOG_FILE_EXTNAME;
					File file = new File(fullName);
					if(file.exists() && file.isFile()){
						if(file.length() >= CRASH_LOG_MAX_FILE_LENGTH){
							String newFileName = LOG_PATH + File.separator + LOG_CRASH_FILENAME 
														  + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
														  + LOG_FILE_EXTNAME;
							file.renameTo(new File(newFileName));
						}
					}			
					writeFile(fullName, message);
					break;
				case LOG_LEVEL_LOC:
					String lfullName = LOG_PATH + File.separator + LOG_LOC_FILENAME + LOG_FILE_EXTNAME;
					File lfile = new File(lfullName);
					if(lfile.exists() && lfile.isFile()){
						if(lfile.length() >= LOC_LOG_MAX_FILE_LENGTH){
							//�ļ�����������д��
							break;
						}
					}			
					writeFile(lfullName, message);
					break;
				case LOG_LEVEL_SPEED:
					String sfullName = LOG_PATH + File.separator + LOG_SPEED_FILENAME + LOG_FILE_EXTNAME;
					File sfile = new File(sfullName);
					if(sfile.exists() && sfile.isFile()){
						if(sfile.length() >= SPE_LOG_MAX_FILE_LENGTH){
							break;
						}
					}
			
					writeFile(sfullName, message);
					break;
				default:break;
				}
			}
		}
	}
	
	/**
	 * д���ļ�
	 * @param data
	 */
	private void writeFile(String fileName, String data) {
		BufferedWriter buf = null;
		try {
			buf = new BufferedWriter(new FileWriter(fileName, true));
			buf.write(data, 0, data.length());
			buf.newLine();
		}catch(Exception e) {
		}finally{
			try{
				if(buf != null) buf.close();
			}catch(IOException e){
			}
		}
	
	}
	/**
	 * ����־Ŀ¼������crash��־�ļ���byte����
	 * @return
	 */
	public HashMap<String, String> readCrashLogFromFile(){ 
		HashMap<String, String> map = new HashMap<String, String>();
		if(TupleApplication.isSDCardAvailable()){
			synchronized(FILE_LOCK){
				LOG_PATH = createLogPath();
				File dir = new File(LOG_PATH);
				File[] files = dir.listFiles();
				int length = files.length;
				for(int i=0;i<length;i++){
					File f = files[i];
					if(f.getName().startsWith(LOG_CRASH_FILENAME)){
						String fullName = files[i].getAbsolutePath();
						String data = readFileToString(fullName);
						if(data != null && !data.equals("")){
							map.put(fullName, data);
						}
					}
				}
			}
		}
		return map;
	}
	/**
	 * ����־Ŀ¼������Exception log
	 * @return
	 */
	public String readExceptionLogFromFile(){
		StringBuffer sbuffer = new StringBuffer();
		if(TupleApplication.isSDCardAvailable()){
			synchronized(FILE_LOCK){
				LOG_PATH = createLogPath();
				File dir = new File(LOG_PATH);
				File[] files = dir.listFiles();
				int length = files.length;
				for(int i=0;i<length;i++){
					if(files[i].getName().startsWith(LOG_EXCEPTION_FILENAME)){
						String fullName = files[i].getAbsolutePath();
						String log = readFileToString(fullName);
						sbuffer.append(log + "\n");
					}
				}
			}
		}
		return sbuffer.toString();
	}
	/**
	 * ����־Ŀ¼�µ�Exception log����ʱֻȡ���µ���һ������һ���Ժ��ٿ���
	 * @return
	 */
	public File readExceptionLogFile(){
		if(TupleApplication.isSDCardAvailable()){
			synchronized(FILE_LOCK){
				LOG_PATH = createLogPath();
				File dir = new File(LOG_PATH);
				File[] files = dir.listFiles();
				int length = files.length;
				for(int i=0;i<length;i++){
					if(files[i].getName().equals(LOG_EXCEPTION_FILENAME + LOG_FILE_EXTNAME)){
						return files[i];
					}
				}
			}
		}
		return null;
	}
	
	public String readFromFileByLevel(int mode){
		StringBuffer sbuffer = new StringBuffer();
		if(TupleApplication.isSDCardAvailable()){
			synchronized (FILE_LOCK) {
				LOG_PATH = createLogPath();
				File dir = new File(LOG_PATH);
				File[] files = dir.listFiles();
				int length = files.length;
				String name = "";
				switch(mode){
				case LOG_LEVEL_LOC:
					name = LOG_LOC_FILENAME;
					break;
				case LOG_LEVEL_SPEED:
					name = LOG_SPEED_FILENAME;
					break;
				default:break;
				}
				for(int i=0;i<length;i++){
					if(files[i].getName().startsWith(name)){
						String fullName = files[i].getAbsolutePath();
						String log = readFileToString(fullName);
						sbuffer.append(log + "\n");
					}
				}
			}
		}
		return sbuffer.toString();
	}
	
	public byte[] readFileToByte(String fullName){
		byte[] data = null;
		BufferedInputStream in = null;
		ByteArrayOutputStream out = null;
		try{
			in = new BufferedInputStream(new FileInputStream(fullName));
			out = new ByteArrayOutputStream(1024);
			byte[] temp = new byte[1024];
			int size = 0;
			while((size = in.read(temp)) != -1){
				out.write(temp, 0, size);
			}
			data = out.toByteArray();
		}catch(Exception e){
			data = null;
		}finally{
			try{
				if(in != null) in.close();
				if(out != null) out.close();
			}catch(IOException ie){
			}
		}
		return data;
	}
	
	public String readFileToString(String fullName){
		StringBuffer sbuffer = new StringBuffer();
		BufferedReader bufferedReader = null;
		try{
			bufferedReader = new BufferedReader(new FileReader(fullName));
			String line = null;
			while((line = bufferedReader.readLine()) != null){
				sbuffer.append(line + "\n");
			}
		}catch(Exception e){	
		}finally{
			try{
				if(bufferedReader != null)
					bufferedReader.close();
			}catch(IOException ex){
			}
		}
		return sbuffer.toString();
	}
	
	/**
	 * ɾ���ļ�
	 * @param fullFileName
	 */
	public void deleteFile(String fullFileName){
		if(TupleApplication.isSDCardAvailable()){
			synchronized(FILE_LOCK){
				File file = new File(fullFileName);
				if(file.exists()){
					file.delete();
				}
			}
		}
	}
	/**
	 * ɾ��ĳһ���͵���־
	 * @param mode
	 */
	public void deleteFileByMode(int mode){
		if(TupleApplication.isSDCardAvailable()){
			synchronized(FILE_LOCK){
				String name = "";
				switch(mode){
				case LOG_LEVEL_INFO:
					name = LOG_INFO_FILENAME;
					break;
				case LOG_LEVEL_DEBUG:
					name = LOG_DEBUG_FILENAME;
					break;
				case LOG_LEVEL_EXCEPTION:
					name = LOG_EXCEPTION_FILENAME;
					break;
				case LOG_LEVEL_CRASH:
					name = LOG_CRASH_FILENAME;
					break;
				case LOG_LEVEL_LOC:
					name = LOG_LOC_FILENAME;
					break;
				case LOG_LEVEL_SPEED:
					name = LOG_SPEED_FILENAME;
					break;
				default:break;
				}
				if(!name.equals("")){
					LOG_PATH = createLogPath();
					File dir = new File(LOG_PATH);
					File[] files = dir.listFiles();
					int length = files.length;
					for(int i=0;i<length;i++){
						if(files[i].getName().startsWith(name)){
							File f = files[i];
							f.delete();
						}
					}
				}
			}
		}
	}
	
	/**
	 * �ж��Ƿ���crash log�ļ�
	 * @return
	 */
	public boolean hasErrLogFile(){
		boolean hasFile = false;
		if(TupleApplication.isSDCardAvailable()){
			LOG_PATH = createLogPath();
			File dir = new File(LOG_PATH);
			File[] files = dir.listFiles();
			int count = 0;
			int length = files.length;
			for(int i=0;i<length;i++){
				if(files[i].getName().startsWith(LOG_CRASH_FILENAME)){
					count++;
				}
			}
			if(count > 0){
				hasFile = true;
			}
		}
		return hasFile;
	}
	/**
	 * �ж��Ƿ���log�ļ��Ҵ�С�ǲ��ǵ�����
	 * @param mode ��־����
	 * @param maxLength �Ƿ����ƴ�С��-1Ϊ������
	 * @return
	 */
	public boolean hasDataLogFile(int mode, int maxLength){
		boolean hasFile = false;
		if(TupleApplication.isSDCardAvailable()){
			LOG_PATH = createLogPath();
			File dir = new File(LOG_PATH);
			File[] files = dir.listFiles();
			int length = files.length;
			String name = "";
			switch(mode){
			case LOG_LEVEL_LOC:
				name = LOG_LOC_FILENAME;
				break;
			case LOG_LEVEL_SPEED:
				name = LOG_SPEED_FILENAME;
				break;
			default:break;
			}
			for(int i=0;i<length;i++){
				if(files[i].getName().startsWith(name)){
					if(maxLength > 0){
						if(files[0].length() >= maxLength){
							hasFile = true;
							break;
						}
					}else{
						hasFile = true;
					}
				}
			}
		}
		return hasFile;
	}
	
	//ÿһ������ݵķָ���
	private static final String ITEM_SEPARATOR = "|";
	public String getSpeedReport(long id, String url, int packageLength, long start, long end){
		try{
			StringBuffer sbuffer = new StringBuffer();
			//��¼ID
			sbuffer.append(id).append(ITEM_SEPARATOR);
			
			//����url
			StringBuffer domainMethod = new StringBuffer("m2/");
			int strStart = url.indexOf("do=");
			int strEnd = url.indexOf("&");
			if(strStart > -1 && strEnd > strStart + 3){
				String method = url.substring(strStart+3, strEnd);
				domainMethod.append(method);
			}
			//
			sbuffer.append(domainMethod.toString()).append(ITEM_SEPARATOR);
			//�������ݰ���С
			sbuffer.append(packageLength).append(ITEM_SEPARATOR);
			//��ʱ��
			sbuffer.append(String.valueOf(end-start)).append(ITEM_SEPARATOR);
			//���������
			sbuffer.append(((TupleApplication)TupleApplication.getContext()).getApnType()).append(ITEM_SEPARATOR);
			//����ģʽ
			sbuffer.append("-").append(ITEM_SEPARATOR);
			//��Ӫ������
			sbuffer.append("-").append(ITEM_SEPARATOR);
			//�豸����
			TelephonyManager tm = (TelephonyManager)TupleApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
			CellLocation cloc = tm.getCellLocation();
			if(cloc != null){
				int type = 0;
				if(cloc instanceof GsmCellLocation){
					type = 1;
				}else if(cloc instanceof CdmaCellLocation){
					type = 2;
				}
				sbuffer.append(type);
			}else{
				sbuffer.append("-");
			}
			return sbuffer.toString();
		}catch(Exception e){
			return "";
		}
	}
	
	
	
	
}
