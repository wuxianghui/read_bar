package com.fv.tuple.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;

public class TechSelectBaseAdapter  extends BaseAdapter{

		ArrayList mRecordsTech=new ArrayList();
		ArrayList mRecordsTechId=new ArrayList();
	    public TechSelectBaseAdapter() {
	    	super();
	    }
	    public int mSelectedItem=-1;
	    public float mFocusedRawX=0;
	    public float mFocusedRawY=0;
	   public void initGroups(ArrayList recordsTech,ArrayList recordsTechId)
	    {
	    	mRecordsTech=(ArrayList) recordsTech;
	    	mRecordsTechId=(ArrayList) recordsTechId;
	    	this.notifyDataSetChanged();
	    }

		public View newView() {

			LayoutInflater inflater = 
					(LayoutInflater) TupleApplication.getContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			return inflater.inflate(R.layout.item_tech_select,null);   
		}
	    public View getView(int position, View convertView, ViewGroup parent) {
	        final View v;
	        if (convertView == null) {
	            v = newView();
	           
		  
		      
		        
	        } else {
	            v =convertView;

	        }
	        TextView ivi=(TextView) v.findViewById(R.id.id_tv_item_tech); 
	        ivi.setText((String)mRecordsTech.get(position));
            ivi.setTag(""+(String)mRecordsTechId.get(position));
	       return v;
	    }
 
	    public final int getCount() {
	        return mRecordsTech.size();
	    }

	    public final Object getItem(int position) {
	        return mRecordsTech.get(position);
	    }

	    public final long getItemId(int position) {
	        return position;
	    }

}
