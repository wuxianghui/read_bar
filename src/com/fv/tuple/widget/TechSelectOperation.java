package com.fv.tuple.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.fv.tuple.R;
import com.fv.tuple.adapter.TechSelectBaseAdapter;
import com.fv.tuple.content_provider.TupleContentProvider;
import com.fv.tuple.content_provider.TupleContentProvider.TTech;
import com.fv.tuple.util.Util;

public class TechSelectOperation {
	TechSelectBaseAdapter mTSBA=null;
	Activity mAct=null;
	public TechSelectOperation(Activity act)
	{
		mAct=act;
	}
	String getTechPath(String tId)
	{
		String[] techName=new String[1];
		String tech= "";
		while(true)
		{
			tId=getParentTechId(tId,techName);
			if(tId==null)
				break;
			tech=tech+"==>"+techName[0];
		}
		return tech;
	}
	void updateTechIdInfo()
	{
		Spinner s1 = (Spinner) mAct.findViewById(R.id.id_sp_tech);
		View vv=s1.getSelectedView();
		TextView ivi=(TextView) vv.findViewById(R.id.id_tv_item_tech); 
  	  	String tId=(String)ivi.getTag();
 		String tech= getTechPath( tId);
		 TextView tv = (TextView)  mAct.findViewById(R.id.id_tv_tech_info);	
		 tv.setText(tech);
	}
	String getParentTechId(String techId,String[] techName)
	{
		String where=TupleContentProvider.TTech.COLUMN_TECH_ID+"="+techId;
		Cursor cursor =  mAct.getContentResolver()
		.query(TupleContentProvider.TTech.CONTENT_URI,
				null,
				where,
				null,
				null);
		cursor.moveToFirst();
		if(cursor.getCount()<1)
		{
			cursor.close();
			cursor= null;
			techName=null;
			return null;
		}

		String p_tech_id=""+cursor.getInt(cursor.getColumnIndex(TTech.COLUMN_PARENT_TECH_ID));
		techName[0]=cursor.getString(cursor.getColumnIndex(TTech.COLUMN_TECH));
		if(cursor!=null)
		{
			cursor.close();
			cursor= null;
		}
		return p_tech_id;
	}
	void initTechSelect()
	{
		Button b=(Button) mAct.findViewById(R.id.id_b_child);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Spinner s1 = (Spinner)  mAct.findViewById(R.id.id_sp_tech);
				View vv=s1.getSelectedView();
				TextView ivi=(TextView) vv.findViewById(R.id.id_tv_item_tech); 
          	  	String techId=(String)ivi.getTag();
				initTech(techId,null);// submit the edit data
				updateTechIdInfo();
			}
		});
		
		
		
		b=(Button) mAct.findViewById(R.id.id_b_parent);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Spinner s1 = (Spinner)  mAct.findViewById(R.id.id_sp_tech);
				View vv=s1.getSelectedView();
				TextView ivi=(TextView) vv.findViewById(R.id.id_tv_item_tech); 
          	  	String techId=(String)ivi.getTag();
          	  	String[] techName=new String[1];
          	    String ppt=getParentTechId(techId,techName);
          	    String pt=null;
          	    if(ppt!=null)
          	    {
          	    	
          	    	pt=getParentTechId(ppt,techName);
          	    }
          	    if(pt!=null)
				initTech(pt,ppt);// submit the edit data
          	    updateTechIdInfo();
			}
		});
	}
	
	public void initTechInfo(String techId)
	{
		String tech= getTechPath( techId);
		 TextView tv = (TextView) mAct.findViewById(R.id.id_tv_tech_info);	
		 tv.setText(tech);
	}
	public void initTech(String techId,String selectTechId)
	{

		ArrayList recordsTech=new ArrayList();
		ArrayList recordsTechId=new ArrayList();
		
		String where=TupleContentProvider.TTech.COLUMN_PARENT_TECH_ID+"="+techId;
		
		Cursor cursor =  mAct.getContentResolver()
		.query(TupleContentProvider.TTech.CONTENT_URI,
				null,
				where,
				null,
				null);

		
		cursor.moveToFirst();
		if(cursor.getCount()<1)
		{
			cursor.close();
			cursor= null;
			return;
		}

		for (int i = 0; i < cursor.getCount(); i++) {
			int tech_id=cursor.getInt(cursor.getColumnIndex(TTech.COLUMN_TECH_ID));
			int p_tech_id=cursor.getInt(cursor.getColumnIndex(TTech.COLUMN_PARENT_TECH_ID));
			String tech=cursor.getString(cursor.getColumnIndex(TTech.COLUMN_TECH));
			recordsTech.add(tech);
			recordsTechId.add(""+tech_id);
			cursor.moveToNext();
		}
		if(cursor!=null)
		{
			cursor.close();
			cursor= null;
		}
		
		
		
		 Spinner s1 = (Spinner)  mAct.findViewById(R.id.id_sp_tech);
		 if(mTSBA==null)
		{
			 mTSBA=new TechSelectBaseAdapter();
			 s1.setAdapter(mTSBA);
			 s1.setOnItemSelectedListener(
		                new OnItemSelectedListener() {
		                    public void onItemSelected(
		                            AdapterView<?> parent, View view, int position, long id) {
		                    	  TextView ivi=(TextView) view.findViewById(R.id.id_tv_item_tech); 
		                    	 
		              	        
		              	          Util.showToast("==========>>>>"+(String)ivi.getTag());
		              	        updateTechIdInfo();
		                    }

		                    public void onNothingSelected(AdapterView<?> parent) {
		                      //  setDefaultKeyMode(DEFAULT_KEYS_DISABLE);
		                    }
		                });
			 initTechSelect();
		}
		 mTSBA.initGroups(recordsTech,recordsTechId);
		 int pos=0;
		 if(selectTechId!=null)
		 {
			 
			 for(int i=0;i<recordsTechId.size();i++)
			 {
				 if(((String)(recordsTechId.get(i))).compareToIgnoreCase(selectTechId)==0)
				 {
					 pos=i;
					 break;
				 }
				 
			 }
		 }
		 s1.setSelection(pos);
	}
}
