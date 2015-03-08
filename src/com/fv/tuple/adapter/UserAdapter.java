package com.fv.tuple.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fv.tuple.R;
import com.fv.tuple.content_provider.TupleContentProvider.TTech;
import com.fv.tuple.content_provider.TupleContentProvider.TUser;
import com.fv.tuple.util.Util;

public class UserAdapter extends CursorAdapter{

private Handler mHandler=null;
	    public UserAdapter(Context context, Cursor cursor,Handler handler) {
	        super(context,cursor);
	    }
	    
	    private View MakeItem(Context context)  
        {  
    		LayoutInflater inflater = 
    		(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View inflateLayout  = inflater.inflate(R.layout.item_list_tech,null);   
            return  inflateLayout;  
        }  
	    public View newView(Context context, Cursor cursor, ViewGroup parent) {
	        
	        return MakeItem(context);
	    }


	    @Override
	    public void bindView(View view, Context context, Cursor cursor) {
          	ImageView b=(ImageView)view.findViewById(R.id.item_pic);
          	

          		int status=cursor.getInt(cursor.getColumnIndex(TUser.COLUMN_PHOTO_STATUS));
          		Bitmap bitmap=null;
         		if(status==1)
          		{
          			String imgName=cursor.getString(cursor.getColumnIndex(TUser.COLUMN_PHOTO));
          			bitmap=Util.getSDCardImage(imgName);
          			b.setImageBitmap(bitmap); 
          		}
          		else
          		{
          			b.setImageBitmap(bitmap);
          			int id=cursor.getInt(cursor.getColumnIndex(TUser.COLUMN_ID));
          			String pic=cursor.getString(cursor.getColumnIndex(TUser.COLUMN_PHOTO));
          	    	Message message=new Message();
          	    	message.what=1002;
          	    	message.arg1=id;
          	    	message.obj=pic;
          	    	mHandler.sendMessage(message);
          		}
          	
          	TextView t=(TextView)view.findViewById(R.id.item_name );
    		String str=cursor.getString(cursor.getColumnIndex(TUser.COLUMN_NAME));
    		int strUserId=cursor.getInt(cursor.getColumnIndex(TUser.COLUMN_USER_ID));
    		t.setText(str);
    		t.setTag(""+strUserId);
    		
    		t=(TextView)view.findViewById(R.id.item_signature);
     	    str=cursor.getString(cursor.getColumnIndex(TUser.COLUMN_SIGNATURE));
     	    t.setText(str);
    		
            return ;  
	    }
	}
