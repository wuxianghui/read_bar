package com.fv.tuple.widget;



import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;


public class TimePickerPopupWindow extends PopupWindow{  
	Handler mHandler=null;
	View mInflateLayout=null;
    public TimePickerPopupWindow(Context context,  Handler handler,
            int colorBgTabMenu,int aniTabMenu){  
        super(context);  
        mHandler=handler;
        
        LayoutInflater inflater = 
    		(LayoutInflater) TupleApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflateLayout  = inflater.inflate(R.layout.layout_time_picker_popupwindow,null);  
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
				TimePicker dp=(TimePicker)mInflateLayout.findViewById(R.id.id_tp_time);
				int h=dp.getCurrentHour();
				int m=dp.getCurrentMinute();

				String da=h+":"+m;
				Message message=new Message();
				message.what=1010;
				message.obj=da;
				mHandler.sendMessage(message);
		
				TimePickerPopupWindow.this.dismiss();
			}
		});
        
		btns = (Button) mInflateLayout.findViewById(R.id.id_b_cancel);
		btns.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimePickerPopupWindow.this.dismiss();
			}
		});
        
    }
    
    public void showAtLocation(View parent,String date,int gravity,int x,int y)
    {
    	String times[]=date.split(":");
    	int h=Integer.parseInt(times[0]);
    	int m=Integer.parseInt(times[1]);
    	TimePicker dp=(TimePicker)mInflateLayout.findViewById(R.id.id_tp_time);
    	dp.setCurrentHour(h);
    	dp.setCurrentMinute(m);

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