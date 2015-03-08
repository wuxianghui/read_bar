package com.fv.tuple.widget;



import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;


public class DataPickerPopupWindow extends PopupWindow{  
	Handler mHandler=null;
	View mInflateLayout=null;
    public DataPickerPopupWindow(Context context,  Handler handler,
            int colorBgTabMenu,int aniTabMenu){  
        super(context);  
        mHandler=handler;
        
        LayoutInflater inflater = 
    		(LayoutInflater) TupleApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflateLayout  = inflater.inflate(R.layout.layout_data_picker_popupwindow,null);  
        this.setContentView(mInflateLayout);  
        this.setWidth(LayoutParams.WRAP_CONTENT);  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        
        this.setBackgroundDrawable(context.getResources().getDrawable(colorBgTabMenu));// 
        this.setAnimationStyle(aniTabMenu);
        this.setFocusable(true); 
        
        Button btns = null;
		btns = (Button) mInflateLayout.findViewById(R.id.id_b_confirm);
		btns.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePicker dp=(DatePicker)mInflateLayout.findViewById(R.id.id_dp_date);
				int d=dp.getDayOfMonth();
				int m=dp.getMonth()+1;
				int y=dp.getYear();
				
				String da=y+"-"+m+"-"+d;
				Message message=new Message();
				message.what=1009;
				message.obj=da;
				mHandler.sendMessage(message);
		
				DataPickerPopupWindow.this.dismiss();
			}
		});
        
		btns = (Button) mInflateLayout.findViewById(R.id.id_b_cancel);
		btns.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DataPickerPopupWindow.this.dismiss();
			}
		});
        
    }
    
    public void showAtLocation(View parent,String date,int gravity,int x,int y)
    {
    	String dates[]=date.split("-");
    	int year=Integer.parseInt(dates[0]);
    	int mon=Integer.parseInt(dates[1])-1;
    	int day=Integer.parseInt(dates[2]);
    	DatePicker dp=(DatePicker)mInflateLayout.findViewById(R.id.id_dp_date);
    	dp.init(year, mon, day, null);

    	/*
    	Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int monthOfYear=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener(){

            public void onDateChanged(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
                dateEt.setText("您选择的日期是："+year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日。");
            }
            
        });
        */
    	
    	
    	this.showAtLocation(parent,
    			gravity, x, y);
	}  
      

}  