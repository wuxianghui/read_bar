package com.fv.tuple.core;

import android.view.Menu;

import com.fv.tuple.R;

/**
 * 存放常量的地方
 * 
 * @author zhanwei
 * @since 1.0
 */
public class Constants {

	//application name
	public static final String appName = "EBCLIENT_MOBILE";
	
	public static final int  APP_CLIENT_TYPE = 0;
	
	public final static int k_BUFFER_SIZE = 1024 * 4;

	public final static int k_INVALID_VALUE = -1;
	
	public final static String k_MARK_PFX = "sndacloudary";
	
	
	public final static String k_CAN_AUTO_LOGIN = "canAutoLogin";

	
	
	// user defined system message,may be all the message will be defined here
	public final static int WIDGET_MESSAGE_LOAD_MORE       = 200;
	
	public final static int PER_PAGE_SIZE       = 10;
	
	
	public final static String INTENT_PARAM_TOPLISTID		= "TOPLISTID";
	public final static String INTENT_PARAM_TOPLISTNAME		= "TOPLISTNAME";
	
	public final static String INTENT_PARAM_BOOKID			= "BOOKID";
	public final static String INTENT_PARAM_BOOKRID			= "BOOKRID";
	public final static String INTENT_PARAM_BOOKNAME		= "BOOKNAME";
	public final static String INTENT_PARAM_BOOKAUTHOR		= "BOOKAUTHOR";
	public final static String INTENT_PARAM_BOOKCOVERTURL	= "BOOKCOVERTURL";
	
	
	// 分类列表到分类书籍列表intent
	public final static String INTENT_PARAM_CATEGORYLISTID 	= "CATEGORYLISTID" ;
	public final static String INTENT_PARAM_CATEGORYLISTNAME= "CATEGORYLISTNAME";
	
	//搜索intent
	public final static String INTENT_PARAM_SEARCH			= "SEARCH" ;
	
	//专题活动 intent
	public final static String INTENT_PARAM_SUBJECTID		= "SUBJECTID" ;
	public final static String INTENT_PARAM_SUBJECTNAME		= "SUBJECTNAME" ;
	public final static String INTENT_PARAM_SUBJECTDETAIL	= "SUBJECTDETAIL";
	public final static String INTENT_PARAM_SUBJECTBOOKLISTID		= "SUBJECTBOOKLISTID" ;
	
	//名家推荐 intent
	public final static String INTENT_PARAM_FAMOUSNICKNAME	= "FAMOUSNICKNAME" ;
	public final static String INTENT_PARAM_FAMOUSID		= "FAMOUSID" ;
	
	public final static String INTENT_PARAM_TABID		= "TABID" ;
	
	public final static String PREFERENCE_DEVICEINFO					= "device_info";
	public final static String PREFERENCE_DEVICETYPE					= "device_type";
	public final static String PREFERENCE_DEVICESN						= "device_sn";
	public final static String PREFERENCE_DEVICECN						= "device_cn";
	public final static String PREFERENCE_EKEYPWD						= "device_ekeypwd";
	public final static String PREFERENCE_CHALLENGE						= "challenge";
	
	//运营商类型
	
	public final static int OPERATORSID_YD					= 0 ;
	public final static int OPERATORSID_LT					= 1 ;
	public final static int OPERATORSID_UNSUPPORT			= -1 ;
	
	public final static String PREFERENCE_PUBKEY						= "device_pubkey";
	public final static String PREFERENCE_PRIEKY						= "device_prikey";
	
	public final static String PREFERENCE_LOGOUT						= "user_logout";
	
	//menu id
	public final static int MENU_ID_TECH    					= 1;
	public final static int MENU_ID_CONTACT 						= 2;
	public final static int MENU_ID_APPOINTMENT 					= 3;
	public final static int MENU_ID_MESSAGE    					= 4;
	public final static int MENU_ID_SETTING						= 5;
	public final static int MENU_ID_EXIT   					= 6;
	//clientupdate
	public final static String PREFERENCE_LASTTIME		= "LASTTIME" ;
}
