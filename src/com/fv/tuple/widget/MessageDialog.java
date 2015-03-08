package com.fv.tuple.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fv.tuple.R;


public class MessageDialog extends Dialog {

    Context context;
    String mTitle=null;
    android.view.View.OnClickListener mConfirmClickListener=null;
    public MessageDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }
    public MessageDialog(Context context, int theme,String title,android.view.View.OnClickListener confirmClickListener){
        super(context, theme);
        this.context = context;
        this.mTitle=title;
        this.mConfirmClickListener=confirmClickListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.message_dialog);
        initUI();
    }
    void initUI()
    {
    	Button button=(Button) this.findViewById(R.id.id_b_confirm);
    	button.setOnClickListener((android.view.View.OnClickListener) mConfirmClickListener);
    	button = (Button) findViewById(R.id.id_b_cancel);
        button.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MessageDialog.this.dismiss();
			}

        });
    }

}