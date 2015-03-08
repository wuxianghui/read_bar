package com.fv.tuple.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fv.tuple.R;
import com.fv.tuple.content_provider.TupleContentProvider.TTech;
import com.fv.tuple.util.Util;

public class TechRootMenuSimpleCursorAdapter extends SimpleCursorAdapter{

private Handler mHandler=null;

	    public TechRootMenuSimpleCursorAdapter(Context context, Cursor cursor,int layout,
	            String[] from, int[] to,int type,Handler handler) {
	    	super(context, layout, cursor, from, to,CursorAdapter.NO_SELECTION);
	        mHandler=handler;
	    }
	    
	    @Override
		protected void onContentChanged() {
			// TODO Auto-generated method stub
			super.onContentChanged();
			int kk=0;
		}

		@Override
	    public void bindView(View view, Context context, Cursor cursor) {
          	ImageView b=(ImageView)view.findViewById(R.id.item_pic);
          	

          		int status=cursor.getInt(cursor.getColumnIndex(TTech.COLUMN_PIC_STATUS));
          		Bitmap bitmap=null;
         		if(status==1)
          		{
          			String imgName=cursor.getString(cursor.getColumnIndex(TTech.COLUMN_PIC));
          			bitmap=Util.getSDCardImage(imgName);
          			b.setImageBitmap(bitmap); 
          		}
          		else
          		{
          			b.setImageBitmap(bitmap);
          			int id=cursor.getInt(cursor.getColumnIndex(TTech.COLUMN_ID));
          			String pic=cursor.getString(cursor.getColumnIndex(TTech.COLUMN_PIC));
          	    	Message message=new Message();
          	    	message.what=1002;
          	    	message.arg1=id;
          	    	message.obj=pic;
          	    	mHandler.sendMessage(message);
          		}
          	
          	TextView t=(TextView)view.findViewById(R.id.item_tech );
    		String str=cursor.getString(cursor.getColumnIndex(TTech.COLUMN_TECH));
    		int tech_id=cursor.getInt(cursor.getColumnIndex(TTech.COLUMN_TECH_ID));
    		t.setText(str);
    		t.setTag(tech_id);
    		
    		t=(TextView)view.findViewById(R.id.item_comment);
     	    str=cursor.getString(cursor.getColumnIndex(TTech.COLUMN_COMMENT));
     	    t.setText(str);
    		
            return ;  
	    }
	}
