package com.fv.tuple.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;


public class HttpInterface {
	AndroidHttpClient mClient = null;
	private static HttpInterface mInstance = null;
	private HttpInterface()
	{
		
	}
	
	public static String excuteGet(String uri)
	{
		HttpGet httpGet = new HttpGet (uri);
		if(mInstance==null)
		{
			mInstance=new HttpInterface();
		}
		mInstance.mClient = AndroidHttpClient.newInstance("your_user_agent");
		HttpResponse response=null;
		 try {
	           // HttpGet httpGet = new HttpGet (uri);
			 response = mInstance.mClient.execute(httpGet);
	            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
	               String bb=""+response.getStatusLine().getStatusCode() ;
	               String aa=bb;
	            }
	            
	            mInstance.mClient.close();
	            
	        } catch (Exception ee) {
	        	String bb=ee.getMessage();
	        	String aa=bb;
	        }
	        if(response!=null)
	        {
				try {
					long kk=response.getEntity().getContentLength();
					return EntityUtils.toString( response.getEntity(), "UTF-8" );
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
				}
				return null;
			}
			else
	        return null;
	}
	
	//public final static String mCookie=null;
	public final static BasicCookieStore mBC= new BasicCookieStore();
	public static String excutePost(String uri, List<NameValuePair> nameValuePair)
	{
		String returns=null;
	        StringBuilder builder = new StringBuilder();
		
	        HttpPost httpPost = new HttpPost (uri);
	        
	        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");  
	        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.23) Gecko/20110920 Firefox/3.6.23");   
	       
	        try {
	        		if(nameValuePair!=null)
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,HTTP.UTF_8));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        
		if(mInstance==null)
		{
			mInstance=new HttpInterface();
		}
		mInstance.mClient = AndroidHttpClient.newInstance("Mozilla/5.0 (Linux; U; Android 2.1-update1; en-us; ADR6300 Build/ERE27) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
		HttpResponse response=null;
		 try {
	           // HttpGet httpGet = new HttpGet (uri);
			 BasicHttpContext context=new BasicHttpContext();
			 //if(mCookie!=null)
		        {
				  context=new BasicHttpContext();
				 context.setAttribute(ClientContext.COOKIE_STORE, mBC);
		        }
	
		        response = mInstance.mClient.execute(httpPost,context);
			
	            if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK) {
	            	BufferedReader reader = new BufferedReader(new InputStreamReader(
	                        response.getEntity().getContent()));
	                for (String s = reader.readLine(); s != null; s = reader.readLine()) {
	                    builder.append(s);
	                }
	                Log.i("json_str", builder.toString());
	                returns=builder.toString();
	            }
	            else
	            {
	            	String bb=""+response.getStatusLine().getStatusCode() ;
	            }
	            mInstance.mClient.close();
	            
	        } catch (Exception ee) {
	        	mInstance.mClient.close();
	        }
	        
	        return returns;
	}
	
	
	
	
	public static String requestByPost(String urlpath,String requestData) throws IOException{
		// HTTP connection reuse which was buggy pre-froyo

		        //froyo��������������httpurlconnection����bug

		/*Workaround for bug pre-Froyo, see here for more info:
		               http://android-developers.blogspot.com/2011/09/androids-http-clients.html*/
		String str=null;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {

		            System.setProperty("http.keepAlive", "false");

		        }

		URL url = new URL(urlpath);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setConnectTimeout(20000);
		conn.setReadTimeout(20000);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		//�������� gzip�������� ����������Content-Encoding������
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		conn.connect();
		String urlEncodedRequestStr = URLEncoder.encode(requestData,"utf-8");
		String requestStr = "jsonStr="+urlEncodedRequestStr;
		conn.getOutputStream().write(requestStr.getBytes("utf-8"));
		conn.getOutputStream().flush();
		conn.getOutputStream().close();
		// //������������������
//		    Map< String,List< String>> map = conn.getHeaderFields();
//		    //��������������������
//		    if(null!=map){
//		      for (String key : map.keySet()){
//		      System.out.println(key + "--->" + map.get(key));
//		      }
//		    }
		String content_encode = conn.getContentEncoding();
		System.out.println("content_encode:"+content_encode);
		// int responseCode = conn.getResponseCode();
		// System.out.println("responseCode:"+responseCode);
		// if(responseCode != 200){
		// String message = conn.getResponseMessage();
		// throw new IOException("ResponseCode:"+responseCode+",Message:"+message);
		// }

		//������gzip�������� ��������������
		if(null!=content_encode&&!"".equals(content_encode)&&content_encode.equals("gzip")){
		GZIPInputStream in = new GZIPInputStream(conn.getInputStream());
		if(in == null){
		return "";
		}
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		int len ;
		byte [] buffer = new byte[1024];
		while((len=in.read(buffer))!= -1){
		arrayOutputStream.write(buffer, 0, len);
		}
		in.close();
		arrayOutputStream.close();
		conn.disconnect();
		str = new String(arrayOutputStream.toByteArray(),"utf-8");
		//����������
		}else{
		InputStream in = conn.getInputStream();
		if(in == null){
		return "";
		}
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		int len ;
		byte [] buffer = new byte[1024];
		while((len=in.read(buffer))!= -1){
		arrayOutputStream.write(buffer, 0, len);
		}
		in.close();
		arrayOutputStream.close();
		conn.disconnect();
		str = new String(arrayOutputStream.toByteArray(),"utf-8");
		}
		return str;
		}
	
	
	
	
	public static Bitmap getHttpBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
             myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
             e.printStackTrace();
        }
        try {
             HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
       
    		conn.setDoInput(true);
    		conn.setConnectTimeout(20000);
    		conn.setReadTimeout(25000);
             conn.connect();
             InputStream is = conn.getInputStream();
             bitmap = BitmapFactory.decodeStream(is);
             is.close();
        } catch (IOException e) {
             e.printStackTrace();
        }
        return bitmap;
   }
	
	
	
}
