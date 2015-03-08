package com.fv.tuple.content_provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class TupleContentProvider extends ContentProvider {
	private static final String databaseName = "tuple.db";
	private static final String PROVIDER_NAME = "com.fv.tuple";
    public static Context context;
	public boolean onCreate() {
		context=this.getContext();
		database = new DatabaseHelper(getContext(), databaseName, null,
				DATABASE_VERSION).getWritableDatabase();
		
		return database != null;
		
	}

	private static SQLiteDatabase database;
	private static UriMatcher uriMatcher;

	
	
	private static final int DATABASE_VERSION = 1;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, TMe.TABLE_NAME + "/#",TMe.ITEM);
		uriMatcher.addURI(PROVIDER_NAME, TMe.TABLE_NAME, TMe.ITEMS);
		uriMatcher.addURI(PROVIDER_NAME, TMe.TABLE_NAME.trim() + "/limit", TMe.LIMIT);
		uriMatcher.addURI(PROVIDER_NAME, TMe.TABLE_NAME.trim() + "/TMe", TMe.JOIN);

		uriMatcher.addURI(PROVIDER_NAME, TUser.TABLE_NAME + "/#",TUser.ITEM);
		uriMatcher.addURI(PROVIDER_NAME, TUser.TABLE_NAME, TUser.ITEMS);
		uriMatcher.addURI(PROVIDER_NAME, TUser.TABLE_NAME.trim() + "/limit", TUser.LIMIT);
		uriMatcher.addURI(PROVIDER_NAME, TUser.TABLE_NAME.trim() + "/TUser", TUser.JOIN);
		
		uriMatcher.addURI(PROVIDER_NAME, TTech.TABLE_NAME + "/#",TTech.ITEM);
		uriMatcher.addURI(PROVIDER_NAME, TTech.TABLE_NAME, TTech.ITEMS);
		uriMatcher.addURI(PROVIDER_NAME, TTech.TABLE_NAME.trim() + "/limit", TTech.LIMIT);
		uriMatcher.addURI(PROVIDER_NAME, TTech.TABLE_NAME.trim() + "/TUser", TTech.JOIN);
		
		uriMatcher.addURI(PROVIDER_NAME, TUserTech.TABLE_NAME + "/#",TUserTech.ITEM);
		uriMatcher.addURI(PROVIDER_NAME, TUserTech.TABLE_NAME, TUserTech.ITEMS);
		uriMatcher.addURI(PROVIDER_NAME, TUserTech.TABLE_NAME.trim() + "/limit", TUserTech.LIMIT);
		uriMatcher.addURI(PROVIDER_NAME, TUserTech.TABLE_NAME.trim() + "/TUser", TUserTech.JOIN);
		
		uriMatcher.addURI(PROVIDER_NAME, TUserStudy.TABLE_NAME + "/#",TUserStudy.ITEM);
		uriMatcher.addURI(PROVIDER_NAME, TUserStudy.TABLE_NAME, TUserStudy.ITEMS);
		uriMatcher.addURI(PROVIDER_NAME, TUserStudy.TABLE_NAME.trim() + "/limit", TUserStudy.LIMIT);
		uriMatcher.addURI(PROVIDER_NAME, TUserStudy.TABLE_NAME.trim() + "/TUser", TUserStudy.JOIN);	
		
		uriMatcher.addURI(PROVIDER_NAME, TStudyRequestInfo.TABLE_NAME + "/#",TStudyRequestInfo.ITEM);
		uriMatcher.addURI(PROVIDER_NAME, TStudyRequestInfo.TABLE_NAME, TStudyRequestInfo.ITEMS);
		uriMatcher.addURI(PROVIDER_NAME, TStudyRequestInfo.TABLE_NAME.trim() + "/limit", TStudyRequestInfo.LIMIT);
		uriMatcher.addURI(PROVIDER_NAME, TStudyRequestInfo.TABLE_NAME.trim() + "/TUser", TStudyRequestInfo.JOIN);	
	
		uriMatcher.addURI(PROVIDER_NAME, TTechAssess.TABLE_NAME + "/#",TTechAssess.ITEM);
		uriMatcher.addURI(PROVIDER_NAME, TTechAssess.TABLE_NAME, TTechAssess.ITEMS);
		uriMatcher.addURI(PROVIDER_NAME, TTechAssess.TABLE_NAME.trim() + "/limit", TTechAssess.LIMIT);
		uriMatcher.addURI(PROVIDER_NAME, TTechAssess.TABLE_NAME.trim() + "/TUser", TTechAssess.JOIN);	
		

	}
public static SQLiteDatabase getWritableDB()
{
	return database;
}
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		String tTableName = null;
		String tColumnId = null;
		boolean isItem = false;
		switch (uriMatcher.match(uri)) {
		case TMe.ITEM:
			isItem = true;
			tTableName = TMe.TABLE_NAME;
			tColumnId = TMe.COLUMN_ID;
			break;
		case TMe.ITEMS:
			isItem = false;
			tTableName = TMe.TABLE_NAME;
			break;
		case TUser.ITEM:
			isItem = true;
			tTableName = TUser.TABLE_NAME;
			tColumnId = TUser.COLUMN_ID;
			break;
		case TUser.ITEMS:
			isItem = false;
			tTableName = TUser.TABLE_NAME;
			break;
		case TTech.ITEM:
			isItem = true;
			tTableName = TTech.TABLE_NAME;
			tColumnId = TTech.COLUMN_ID;
			break;
		case TTech.ITEMS:
			isItem = false;
			tTableName = TTech.TABLE_NAME;
			break;
		case TUserTech.ITEM:
			isItem = true;
			tTableName = TUserTech.TABLE_NAME;
			tColumnId = TUserTech.COLUMN_ID;
			break;
		case TUserTech.ITEMS:
			isItem = false;
			tTableName = TUserTech.TABLE_NAME;
			break;
		case TUserStudy.ITEM:
			isItem = true;
			tTableName = TUserStudy.TABLE_NAME;
			tColumnId = TUserStudy.COLUMN_ID;
			break;
		case TUserStudy.ITEMS:
			isItem = false;
			tTableName = TUserStudy.TABLE_NAME;
			break;
		case TStudyRequestInfo.ITEM:
			isItem = true;
			tTableName = TStudyRequestInfo.TABLE_NAME;
			tColumnId = TStudyRequestInfo.COLUMN_ID;
			break;
		case TStudyRequestInfo.ITEMS:
			isItem = false;
			tTableName = TStudyRequestInfo.TABLE_NAME;
			break;
		case TTechAssess.ITEM:
			isItem = true;
			tTableName = TTechAssess.TABLE_NAME;
			tColumnId = TTechAssess.COLUMN_ID;
			break;
		case TTechAssess.ITEMS:
			isItem = false;
			tTableName = TTechAssess.TABLE_NAME;
			break;
		default:
			throw new IllegalArgumentException("unknown uri: " + uri);
		}
		int ret = -1;
		if (isItem) {
			ret = database.delete(tTableName,
					tColumnId + "=" + uri.getPathSegments().get(1) + " and ("
							+ selection + ")", selectionArgs);
		} else {
			ret = database.delete(tTableName, selection, selectionArgs);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return ret;
	}

	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case TMe.ITEM:
			return "";
		case TMe.ITEMS:
			return "";
		case TUser.ITEM:
			return "";
		case TUser.ITEMS:
			return "";
		case TTech.ITEM:
			return "";
		case TTech.ITEMS:
			return "";
		case TUserTech.ITEM:
			return "";
		case TUserTech.ITEMS:
			return "";
		case TUserStudy.ITEM:
			return "";
		case TUserStudy.ITEMS:
			return "";
		case TStudyRequestInfo.ITEM:
			return "";
		case TStudyRequestInfo.ITEMS:
			return "";
		case TTechAssess.ITEM:
			return "";
		case TTechAssess.ITEMS:
			return "";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	public Uri insert(Uri uri, ContentValues contentValues) {

		Uri contenUri = null;

		String tTableName = null;
		String tColumnId = null;
		boolean isItem = false;
		switch (uriMatcher.match(uri)) {
		case TMe.ITEMS:
			isItem = true;
			tTableName = TMe.TABLE_NAME;
			contenUri = TMe.CONTENT_URI;
			break;
		case TUser.ITEMS:
			isItem = true;
			tTableName = TUser.TABLE_NAME;
			contenUri = TUser.CONTENT_URI;
			break;
		case TTech.ITEMS:
			isItem = true;
			tTableName = TTech.TABLE_NAME;
			contenUri = TTech.CONTENT_URI;
			break;
		case TUserTech.ITEMS:
			isItem = true;
			tTableName = TUserTech.TABLE_NAME;
			contenUri = TUserTech.CONTENT_URI;
			break;
		case TUserStudy.ITEMS:
			isItem = true;
			tTableName = TUserStudy.TABLE_NAME;
			contenUri = TUserStudy.CONTENT_URI;
			break;
		case TStudyRequestInfo.ITEMS:
			isItem = true;
			tTableName = TStudyRequestInfo.TABLE_NAME;
			contenUri = TStudyRequestInfo.CONTENT_URI;
			break;
		case TTechAssess.ITEMS:
			isItem = true;
			tTableName = TTechAssess.TABLE_NAME;
			contenUri = TTechAssess.CONTENT_URI;
			break;
		default:
			throw new IllegalArgumentException("unknown uri: " + uri);
		}
		if (isItem) {
			long rowId = database.insert(tTableName, "", contentValues);

			if (!(rowId > 0)) {
				throw new SQLException("failed to insert row into " + uri);
			}

			Uri _uri = ContentUris.withAppendedId(contenUri, rowId);
			getContext().getContentResolver().notifyChange(_uri, null);
			return _uri;
		}
		return contenUri;
	}

	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		String tTableName = null;
		String tColumnId = null;
		boolean isItem = false;
		boolean isLimit = false;
		switch (uriMatcher.match(uri)) {
		case TMe.ITEM:
			isItem = true;
			tTableName = TMe.TABLE_NAME;
			tColumnId = TMe.COLUMN_ID;
			break;
		case TMe.ITEMS:
			isItem = false;
			tTableName = TMe.TABLE_NAME;
			break;

		case TUser.ITEM:
			isItem = true;
			tTableName = TUser.TABLE_NAME;
			tColumnId = TUser.COLUMN_ID;
			break;
		case TUser.ITEMS:
			isItem = false;
			tTableName = TUser.TABLE_NAME;
			break;
			
		case TTech.ITEM:
			isItem = true;
			tTableName = TTech.TABLE_NAME;
			tColumnId = TTech.COLUMN_ID;
			break;
		case TTech.ITEMS:
			isItem = false;
			tTableName = TTech.TABLE_NAME;
			break;
			
		case TUserTech.ITEM:
			isItem = true;
			tTableName = TUserTech.TABLE_NAME;
			tColumnId = TUserTech.COLUMN_ID;
			break;
		case TUserTech.ITEMS:
			isItem = false;
			tTableName = TUserTech.TABLE_NAME;
			break;
			
		case TUserStudy.ITEM:
			isItem = true;
			tTableName = TUserStudy.TABLE_NAME;
			tColumnId = TUserStudy.COLUMN_ID;
			break;
		case TUserStudy.ITEMS:
			isItem = false;
			tTableName = TUserStudy.TABLE_NAME;
			break;
			
		case TStudyRequestInfo.ITEM:
			isItem = true;
			tTableName = TStudyRequestInfo.TABLE_NAME;
			tColumnId = TStudyRequestInfo.COLUMN_ID;
			break;
		case TStudyRequestInfo.ITEMS:
			isItem = false;
			tTableName = TStudyRequestInfo.TABLE_NAME;
			break;
			
		case TTechAssess.ITEM:
			isItem = true;
			tTableName = TTechAssess.TABLE_NAME;
			tColumnId = TTechAssess.COLUMN_ID;
			break;
		case TTechAssess.ITEMS:
			isItem = false;
			tTableName = TTechAssess.TABLE_NAME;
			break;
			
			
		case TMe.LIMIT:
			tTableName = TMe.TABLE_NAME;
			isLimit = true;
			break;
		case TUser.LIMIT:
			tTableName = TUser.TABLE_NAME;
			isLimit = true;
			break;
		case TTech.LIMIT:
			tTableName = TTech.TABLE_NAME;
			isLimit = true;
			break;      
		case TUserTech.LIMIT:
			tTableName = TUserTech.TABLE_NAME;
			isLimit = true;
			break;     
		case TUserStudy.LIMIT:
			tTableName = TUserStudy.TABLE_NAME;
			isLimit = true;
			break;  
		case TStudyRequestInfo.LIMIT:
			tTableName = TStudyRequestInfo.TABLE_NAME;
			isLimit = true;
			break;  
		case TTechAssess.LIMIT:
			tTableName = TTechAssess.TABLE_NAME;
			isLimit = true;
			break;  
		default:
			throw new IllegalArgumentException("unknown uri: " + uri);
		}
		if (isItem) {
			return database.query(tTableName, projection, tColumnId + "="
					+ uri.getPathSegments().get(1), selectionArgs, null, null,
					null);
		} else if (isLimit) {
			String start=null;
			String limit="";
			if(selection.contains("@limit")){
			start=selection.substring(0, selection.indexOf("@limit"));
			if("".equals(start.trim()))
			start=null;
			limit=selection.substring(selection.indexOf("@limit")+7);
			}
			return database.query(tTableName, projection, start, selectionArgs, null, null,sortOrder,limit);
		} else {
			return database.query(tTableName, projection, selection,
					selectionArgs, null, null, sortOrder);
		}

	}

	public int update(Uri uri, ContentValues contentValues, String selection,
			String[] selectionArgs) {
		String tTableName = null;
		String tColumnId = null;
		boolean isItem = false;
		switch (uriMatcher.match(uri)) {
		case TMe.ITEM:
			isItem = true;
			tTableName = TMe.TABLE_NAME;
			tColumnId = TMe.COLUMN_ID;
			break;
		case TMe.ITEMS:
			isItem = false;
			tTableName = TMe.TABLE_NAME;
			break;

		case TUser.ITEM:
			isItem = true;
			tTableName = TUser.TABLE_NAME;
			tColumnId = TUser.COLUMN_ID;
			break;
		case TUser.ITEMS:
			isItem = false;
			tTableName = TUser.TABLE_NAME;
			break;

		case TTech.ITEM:
			isItem = true;
			tTableName = TTech.TABLE_NAME;
			tColumnId = TTech.COLUMN_ID;
			break;
		case TTech.ITEMS:
			isItem = false;
			tTableName = TTech.TABLE_NAME;
			break;
			
		case TUserTech.ITEM:
			isItem = true;
			tTableName = TUserTech.TABLE_NAME;
			tColumnId = TUserTech.COLUMN_ID;
			break;
		case TUserTech.ITEMS:
			isItem = false;
			tTableName = TUserTech.TABLE_NAME;
			break;
			
		case TUserStudy.ITEM:
			isItem = true;
			tTableName = TUserStudy.TABLE_NAME;
			tColumnId = TUserStudy.COLUMN_ID;
			break;
		case TUserStudy.ITEMS:
			isItem = false;
			tTableName = TUserStudy.TABLE_NAME;
			break;
		case TStudyRequestInfo.ITEM:
			isItem = true;
			tTableName = TStudyRequestInfo.TABLE_NAME;
			tColumnId = TStudyRequestInfo.COLUMN_ID;
			break;
		case TStudyRequestInfo.ITEMS:
			isItem = false;
			tTableName = TStudyRequestInfo.TABLE_NAME;
			break;
		case TTechAssess.ITEM:
			isItem = true;
			tTableName = TTechAssess.TABLE_NAME;
			tColumnId = TTechAssess.COLUMN_ID;
			break;
		case TTechAssess.ITEMS:
			isItem = false;
			tTableName = TTechAssess.TABLE_NAME;
			break;
		default:
			throw new IllegalArgumentException("unknown uri: " + uri);
		}
		int ret = -1;
		if (isItem) {
			ret = database.update(tTableName, contentValues,
					tColumnId + "=" + uri.getPathSegments().get(1),null);
			
		} else {
			
			ret = database.update(tTableName, contentValues, selection,
					selectionArgs);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return ret;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			TMe.onCreate(database);
			TUser.onCreate(database); 
			TTech.onCreate(database);
			TUserTech.onCreate(database);
			TUserStudy.onCreate(database);
			TStudyRequestInfo.onCreate(database);
			TTechAssess.onCreate(database);
		}

		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion,
				int newVersion) {
			TMe.dropTable(database);
			TUser.dropTable(database);
			TTech.dropTable(database);
			TUserTech.dropTable(database);
			TUserStudy.dropTable(database);
			TStudyRequestInfo.dropTable(database);
			TTechAssess.dropTable(database);
			onCreate(database);
			TMe.onCreate(database);
			TUser.onCreate(database);
			TTech.onCreate(database);
			TUserTech.onCreate(database);
			TUserStudy.onCreate(database);
			TStudyRequestInfo.onCreate(database);
			TTechAssess.onCreate(database);
		}

	}

	public static class TMe {
		private static final String TABLE_NAME = "t_me";
		
		public static final String COLUMN_ID = "_id"; //integer
		public static final String COLUMN_USER_ID = "user_id"; //integer
		public static final String COLUMN_EMAIL = "email"; //integer
		public static final String COLUMN_PASSWORD = "password"; //text
		public static final String COLUMN_NAME = "name"; //text
		public static final String COLUMN_AGE = "age"; //textger
		public static final String COLUMN_SEX = "sex"; //textger
		public static final String COLUMN_ADDRESS = "address"; //BIGINT
		public static final String COLUMN_JOB = "job"; //BIGINT
		public static final String COLUMN_SCHOOL = "school"; //BIGINT
		public static final String COLUMN_COMMENTS = "comments"; //BIGINT
		public static final String COLUMN_SIGNATURE = "signature"; //BIGINT
		public static final String COLUMN_IS_AUTHENTICATED = "is_authenticated"; //BIGINT
		public static final String COLUMN_STATE = "state"; //BIGINT
		public static final String COLUMN_LATITUDE = "latitude"; //BIGINT
		public static final String COLUMN_LONGITUDE = "longitude"; //BIGINT
		public static final String COLUMN_PHOTO = "photo"; //BIGINT
		public static final String COLUMN_PHOTO_STATUS = "photo_status"; //BIGINT

		
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ PROVIDER_NAME + "/" + TABLE_NAME);
		private static final String DATABASE_CREATE_TABLE_CATEGORY =

		"create table " + TABLE_NAME + " (" + 
				COLUMN_ID
				+ " integer primary key autoincrement, " + 
				COLUMN_USER_ID
				+ " integer,"  + 
				COLUMN_EMAIL
				+ " text,"  + 
				COLUMN_PASSWORD 
				+ " text," + 
				COLUMN_NAME
				+ " text," + 
				COLUMN_SEX
				+ " text,"+
				COLUMN_AGE
				+ " integer,"+
				COLUMN_ADDRESS
				+ " text,"+
				COLUMN_JOB
				+ " text,"+
				COLUMN_SCHOOL
				+ " text,"+
				COLUMN_COMMENTS
				+ " text,"+
				COLUMN_SIGNATURE
				+ " text,"+
				COLUMN_IS_AUTHENTICATED
				+ " integer,"+
				COLUMN_STATE
				+ " integer,"+
				COLUMN_LATITUDE
				+ " real,"+
				COLUMN_LONGITUDE
				+ " real,"+
				COLUMN_PHOTO_STATUS
				+ " integer,"+
				COLUMN_PHOTO
				+ " text)";
		private static final int ITEM = 1;
		private static final int ITEMS = 2;
		public static final int LIMIT = 101;
		public static final int JOIN = 102;
		public static void onCreate(SQLiteDatabase database) {
			database.execSQL(DATABASE_CREATE_TABLE_CATEGORY);
//			SQLiteStatement statement=null ;	
		}

	

		public static void dropTable(SQLiteDatabase database) {
			database.execSQL("drop table if exists " + TABLE_NAME);
		}
	}

	public static class TUser {
		private static final String TABLE_NAME = "t_user";
		
		public static final String COLUMN_ID = "_id"; //integer
		public static final String COLUMN_USER_ID = "user_id"; //integer
		public static final String COLUMN_EMAIL = "email"; //integer
		public static final String COLUMN_NAME = "name"; //text
		public static final String COLUMN_AGE = "age"; //textger
		public static final String COLUMN_ADDRESS = "address"; //BIGINT
		public static final String COLUMN_JOB = "job"; //BIGINT
		public static final String COLUMN_SEX = "sex"; //textger
		public static final String COLUMN_SCHOOL = "school"; //BIGINT
		public static final String COLUMN_COMMENTS = "comments"; //BIGINT
		public static final String COLUMN_SIGNATURE = "signature"; //BIGINT
		public static final String COLUMN_IS_AUTHENTICATED = "is_authenticated"; //BIGINT
		public static final String COLUMN_STATE = "state"; //BIGINT
		public static final String COLUMN_LATITUDE = "latitude"; //BIGINT
		public static final String COLUMN_LONGITUDE = "longitude"; //BIGINT
		public static final String COLUMN_PHOTO = "photo"; //BIGINT
		public static final String COLUMN_PHOTO_STATUS = "photo_status"; //BIGINT
	    
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ PROVIDER_NAME + "/" + TABLE_NAME);
		private static final String DATABASE_CREATE_TABLE_CATEGORY =

			"create table " + TABLE_NAME + " (" + 
			COLUMN_ID
			+ " integer primary key autoincrement, " + 
			COLUMN_USER_ID
			+ " integer,"  + 
			COLUMN_EMAIL
			+ " text," + 
			COLUMN_NAME
			+ " text," + 
			COLUMN_SEX
			+ " text,"+
			COLUMN_AGE
			+ " integer,"+
			COLUMN_ADDRESS
			+ " text,"+
			COLUMN_JOB
			+ " text,"+
			COLUMN_SCHOOL
			+ " text,"+
			COLUMN_COMMENTS
			+ " text,"+
			COLUMN_SIGNATURE
			+ " text,"+
			COLUMN_IS_AUTHENTICATED
			+ " integer,"+
			COLUMN_STATE
			+ " integer,"+
			COLUMN_LATITUDE
			+ " real,"+
			COLUMN_LONGITUDE
			+ " real,"+
			COLUMN_PHOTO_STATUS
			+ " integer,"+
			COLUMN_PHOTO
			+ " text)";

		private static final int ITEM = 3;
		private static final int ITEMS = 4;
		public static final int LIMIT = 103;
		public static final int JOIN = 104;

		public static void onCreate(SQLiteDatabase database) {
			database.execSQL(DATABASE_CREATE_TABLE_CATEGORY);

		}

		public static void dropTable(SQLiteDatabase database) {
			database.execSQL("drop table if exists " + TABLE_NAME);
		}
	}

	
	public static class TTech {
		

		private static final String TABLE_NAME = "t_tech";
		
		public static final String COLUMN_ID = "_id"; //integer
		public static final String COLUMN_TECH_ID = "tech_id"; //integer
		public static final String COLUMN_PARENT_TECH_ID = "parent_tech_id"; //integer
		public static final String COLUMN_TECH = "tech"; //integer
		public static final String COLUMN_COMMENT = "comment"; //textger
		public static final String COLUMN_PIC = "pic"; //BIGINT
		public static final String COLUMN_PIC_STATUS = "pic_status"; //BIGINT

	    
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ PROVIDER_NAME + "/" + TABLE_NAME);
		private static final String DATABASE_CREATE_TABLE_CATEGORY =

			"create table " + TABLE_NAME + " (" + 
			COLUMN_ID
			+ " integer primary key autoincrement," + 
			COLUMN_TECH_ID
			+ " integer," + 
			COLUMN_PARENT_TECH_ID
			+ " integer," + 
			COLUMN_TECH
			+ " text,"+
			COLUMN_COMMENT
			+ " text,"+
			COLUMN_PIC_STATUS
			+ " integer," + 
			COLUMN_PIC
			+ " text)";

		private static final int ITEM = 5;
		private static final int ITEMS = 6;
		public static final int LIMIT = 105;
		public static final int JOIN = 106;

		public static void onCreate(SQLiteDatabase database) {
			database.execSQL(DATABASE_CREATE_TABLE_CATEGORY);

		}

		public static void dropTable(SQLiteDatabase database) {
			database.execSQL("drop table if exists " + TABLE_NAME);
		}
	}

	public static class TUserTech {

		private static final String TABLE_NAME = "t_user_tech";
		
		public static final String COLUMN_ID = "_id"; //integer
		public static final String COLUMN_USER_TECH_ID = "user_tech_id"; //integer
		public static final String COLUMN_USER_ID = "user_id"; //integer
		public static final String COLUMN_TECH_ID = "tech_id"; //integer
		public static final String COLUMN_TECH_NAME = "tech_name"; //integer
		public static final String COLUMN_COMMENT = "comment"; //textger
		public static final String COLUMN_ADDRESS = "address"; //BIGINT
		public static final String COLUMN_PAY = "pay"; //BIGINT
		public static final String COLUMN_PERIOD = "period"; //BIGINT

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ PROVIDER_NAME + "/" + TABLE_NAME);
		private static final String DATABASE_CREATE_TABLE_CATEGORY =

			"create table " + TABLE_NAME + " (" + 
			COLUMN_ID
			+ " integer primary key autoincrement," + 
			COLUMN_USER_TECH_ID
			+ " integer," + 
			COLUMN_USER_ID
			+ " integer," + 
			COLUMN_TECH_ID
			+ " integer," + 
			COLUMN_TECH_NAME
			+ " text," + 
			COLUMN_COMMENT
			+ " text," + 
			COLUMN_ADDRESS
			+ " text," + 
			COLUMN_PAY
			+ " integer,"+
			COLUMN_PERIOD
			+ " integer)";

		private static final int ITEM = 7;
		private static final int ITEMS = 8;
		public static final int LIMIT = 107;
		public static final int JOIN = 108;

		public static void onCreate(SQLiteDatabase database) {
			database.execSQL(DATABASE_CREATE_TABLE_CATEGORY);

		}

		public static void dropTable(SQLiteDatabase database) {
			database.execSQL("drop table if exists " + TABLE_NAME);
		}
	}
	
	public static class TUserStudy {

		private static final String TABLE_NAME = "t_user_study";
		
		public static final String COLUMN_ID = "_id"; //integer
		public static final String COLUMN_USER_STUDY_ID = "user_study_id"; //integer
		public static final String COLUMN_USER_ID = "user_id"; //integer
		public static final String COLUMN_TECH_ID = "tech_id"; //integer
		public static final String COLUMN_STUDY_NAME = "study_name"; //integer
		public static final String COLUMN_COMMENT = "comment"; //textger
		public static final String COLUMN_ADDRESS = "address"; //BIGINT
		public static final String COLUMN_PUBLISH_DATE = "publish_date"; //BIGINT

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ PROVIDER_NAME + "/" + TABLE_NAME);
		private static final String DATABASE_CREATE_TABLE_CATEGORY =

			"create table " + TABLE_NAME + " (" + 
			COLUMN_ID
			+ " integer primary key autoincrement," + 
			COLUMN_USER_STUDY_ID
			+ " integer," + 
			COLUMN_USER_ID
			+ " integer," + 
			COLUMN_TECH_ID
			+ " integer," + 
			COLUMN_STUDY_NAME
			+ " text," + 
			COLUMN_COMMENT
			+ " text," + 
			COLUMN_ADDRESS
			+ " text," + 
			COLUMN_PUBLISH_DATE
			+ " integer)";

		private static final int ITEM = 9;
		private static final int ITEMS = 10;
		public static final int LIMIT = 109;
		public static final int JOIN = 110;

		public static void onCreate(SQLiteDatabase database) {
			database.execSQL(DATABASE_CREATE_TABLE_CATEGORY);

		}

		public static void dropTable(SQLiteDatabase database) {
			database.execSQL("drop table if exists " + TABLE_NAME);
		}
	}
	
	public static class TStudyRequestInfo{

		private static final String TABLE_NAME = "t_study_request_info";
		
		public static final String COLUMN_ID = "_id"; //integer
		public static final String COLUMN_STUDY_REQUEST_INFO_ID = "study_request_info_id"; //integer
		public static final String COLUMN_USER_ID = "user_id"; //integer
		public static final String COLUMN_USER_NAME = "user_name"; //integer
		public static final String COLUMN_USER_TECH_ID = "user_tech_id"; //integer
		public static final String COLUMN_USER_TECH_NAME = "user_tech_name"; //integer
		public static final String COLUMN_USER_TECH_USER_ID = "user_tech_user_id"; //integer
		public static final String COLUMN_USER_TECH_USER_NAME = "user_tech_user_name"; //integer
		public static final String COLUMN_REQUEST_COMMENT = "request_comment"; //textger
		public static final String COLUMN_RESPONSE_COMMENT = "response_comment"; //textger
		public static final String COLUMN_STATE = "state"; //BIGINT
		public static final String COLUMN_STATE_CHANGE_DATE = "state_change_date"; //BIGINT
		public static final String COLUMN_PAY = "pay"; //BIGINT
		public static final String COLUMN_ADDRESS = "address"; //BIGINT
		public static final String COLUMN_TECH_DATE = "tech_date"; //BIGINT

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ PROVIDER_NAME + "/" + TABLE_NAME);
		private static final String DATABASE_CREATE_TABLE_CATEGORY =

			"create table " + TABLE_NAME + " (" + 
			COLUMN_ID
			+ " integer primary key autoincrement," + 
			COLUMN_STUDY_REQUEST_INFO_ID
			+ " integer," + 
			COLUMN_USER_ID
			+ " integer," + 
			COLUMN_USER_NAME
			+ " text," + 
			COLUMN_USER_TECH_ID
			+ " integer," + 
			COLUMN_USER_TECH_NAME
			+ " text," + 
			COLUMN_USER_TECH_USER_ID
			+ " integer," + 
			COLUMN_USER_TECH_USER_NAME
			+ " text," + 
			COLUMN_REQUEST_COMMENT
			+ " text," + 
			COLUMN_RESPONSE_COMMENT
			+ " text," + 
			COLUMN_STATE
			+ " integer," + 
			COLUMN_STATE_CHANGE_DATE
			+ " integer," + 
			COLUMN_PAY
			+ " integer," + 
			COLUMN_ADDRESS
			+ " text," +  
			COLUMN_TECH_DATE
			+ " integer)";

		private static final int ITEM = 11;
		private static final int ITEMS = 12;
		public static final int LIMIT = 111;
		public static final int JOIN = 112;

		public static void onCreate(SQLiteDatabase database) {
			database.execSQL(DATABASE_CREATE_TABLE_CATEGORY);

		}

		public static void dropTable(SQLiteDatabase database) {
			database.execSQL("drop table if exists " + TABLE_NAME);
		}
	}
	
	public static class TTechAssess{

		private static final String TABLE_NAME = "t_tech_assess";
		
		public static final String COLUMN_ID = "_id"; //integer
		public static final String COLUMN_TECH_ASSESS_ID = "tech_assess_id"; //integer
		public static final String COLUMN_USER_ID = "user_id"; //integer
		public static final String COLUMN_USER_NAME = "user_name"; //integer
		public static final String COLUMN_USER_TECH_USER_ID = "user_tech_user_id"; //integer
		public static final String COLUMN_USER_TECH_USER_NAME = "user_tech_user_name"; //integer
		public static final String COLUMN_USER_TECH_ID = "user_tech_id"; //integer
		public static final String COLUMN_USER_TECH_NAME = "user_tech_name"; //integer
		public static final String COLUMN_STAR = "star"; //integer
		public static final String COLUMN_COMMENT = "comment"; //textger
		public static final String COLUMN_REAL_STUDY_DATE = "real_study_date"; //textger
		public static final String COLUMN_REAL_STUDY_PERIOD = "real_study_period"; //BIGINT
		public static final String COLUMN_REAL_PAY = "real_pay"; //BIGINT

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ PROVIDER_NAME + "/" + TABLE_NAME);
		private static final String DATABASE_CREATE_TABLE_CATEGORY =

			"create table " + TABLE_NAME + " (" + 
			COLUMN_ID
			+ " integer primary key autoincrement," + 
			COLUMN_TECH_ASSESS_ID
			+ " integer," + 
			COLUMN_USER_ID
			+ " integer," + 
			COLUMN_USER_NAME
			+ " text," + 
			COLUMN_USER_TECH_USER_ID
			+ " integer," + 
			COLUMN_USER_TECH_USER_NAME
			+ " text," + 
			COLUMN_USER_TECH_ID
			+ " integer," + 
			COLUMN_USER_TECH_NAME
			+ " text," + 
			COLUMN_STAR
			+ " integer," + 
			COLUMN_COMMENT
			+ " text," + 
			COLUMN_REAL_STUDY_DATE
			+ " integer," + 
			COLUMN_REAL_STUDY_PERIOD
			+ " integer," + 
			COLUMN_REAL_PAY
			+ " integer)";

		private static final int ITEM = 13;
		private static final int ITEMS = 14;
		public static final int LIMIT = 113;
		public static final int JOIN = 114;

		public static void onCreate(SQLiteDatabase database) {
			database.execSQL(DATABASE_CREATE_TABLE_CATEGORY);

		}

		public static void dropTable(SQLiteDatabase database) {
			database.execSQL("drop table if exists " + TABLE_NAME);
		}
	}
}
