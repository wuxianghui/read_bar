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
import com.fv.tuple.content_provider.TupleContentProvider.TStudyRequestInfo;
import com.fv.tuple.content_provider.TupleContentProvider.TTech;
import com.fv.tuple.util.Util;

public class RequestMeAdapter extends CursorAdapter{

private Handler mHandler=null;
	    public RequestMeAdapter(Context context, Cursor cursor,Handler handler) {
	        super(context,cursor);
	        mHandler=handler;
	    }
	    
	    private View MakeItem(Context context)  
        {  
    		LayoutInflater inflater = 
    		(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View inflateLayout  = inflater.inflate(R.layout.item_list_request,null);   
            return  inflateLayout;  
        }  
	    public View newView(Context context, Cursor cursor, ViewGroup parent) {
	        
	        return MakeItem(context);
	    }

	    @Override
	    public void bindView(View view, Context context, Cursor cursor) {
          	TextView t=(TextView)view.findViewById(R.id.id_tv_user );
    		String str=cursor.getString(cursor.getColumnIndex(TStudyRequestInfo.COLUMN_USER_NAME));
    		String techName =cursor.getString(cursor.getColumnIndex(TStudyRequestInfo.COLUMN_USER_TECH_NAME));
    		t.setText(str);
    		t=(TextView)view.findViewById(R.id.id_tv_tech );
    		t.setText(techName);

            return ;  
	    }
	}
