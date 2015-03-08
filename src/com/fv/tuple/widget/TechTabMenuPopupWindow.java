package com.fv.tuple.widget;



import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;


public class TechTabMenuPopupWindow extends PopupWindow{  
    private ListView mList=null;
    private LinearLayout mLayout;   
    
    public TechTabMenuPopupWindow(Context context,OnItemClickListener titleClick,  
            int colorBgTabMenu,int aniTabMenu){  
        super(context);  
          
        mLayout = new LinearLayout(context);  
        mLayout.setOrientation(LinearLayout.VERTICAL);
        
        LayoutInflater inflater = 
    		(LayoutInflater) TupleApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View inflateLayout  = inflater.inflate(R.layout.tech_tech_menu_popupwindow_listview,null);  
    		mList=(ListView)inflateLayout.findViewById(R.id.id_list_listview);
        this.setContentView(inflateLayout);  
        this.setWidth(LayoutParams.WRAP_CONTENT);  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        
        this.setBackgroundDrawable(context.getResources().getDrawable(colorBgTabMenu));// 
        this.setAnimationStyle(aniTabMenu);
        this.setFocusable(true); 
    }  
      

      
    public void setMyAdapter(TechMenuAdapter bodyAdapter)  
    {  
    	mList.setAdapter(bodyAdapter);  
    }  
        
    	static public class TechMenuAdapter extends BaseAdapter {  
        private Context mContext;  
        private ArrayList mTexts;  
        private ArrayList mIds;  
        OnClickListener mBodyClick;
        public TechMenuAdapter(Context context, OnClickListener bodyClick,ArrayList texts,ArrayList ids)   
        {  
            this.mContext = context;  
            this.mTexts = texts;  
            this.mBodyClick=bodyClick;
            this.mIds=ids;
        }  
        public void reinitMenu(ArrayList texts,ArrayList ids)
        {
        	 this.mTexts = texts; 
        	 this.mIds=ids;
        	 this.notifyDataSetChanged();
        }
        public int getCount() {  
            return mTexts.size();  
        }  
        public Object getItem(int position) {  
              
            return makeMenuBody(position);  
        }  
        public long getItemId(int position) {  
            return position;  
        }  
    
        private LinearLayout makeMenuBody(int position)  
        {  
    		LayoutInflater inflater = 
    		(LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View inflateLayout  = inflater.inflate(R.layout.item_list_tech_tech_menu_popupwindow,null);   
    		//id_tv_item
    		TextView t=(TextView)inflateLayout.findViewById(R.id.id_tv_item);
    		t.setText((String)(mTexts.get(position)));
    		t.setTag(""+(String)(this.mIds.get(position)));
    		inflateLayout.setOnClickListener(this.mBodyClick);
            return (LinearLayout) inflateLayout;  
        }  
        public View getView(int position, View convertView, ViewGroup parent) {  
            return makeMenuBody( position);  
        }  
    }  
}  