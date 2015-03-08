package com.fv.tuple.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;

public class SettingPhotoActivity extends Activity {
    /** Called when the activity is first created. */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_photo);
        initUI();
    }
    
    
    
void initUI()
{
	Button btns = (Button) findViewById(R.id.id_b_camera);	
	btns.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			takePhotoFromCamera();
			
		}
	});
	
	btns = (Button) findViewById(R.id.id_b_fileimage);	
	btns.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			takePhotoFromFile();
			
		}
	});
	
	btns = (Button) findViewById(R.id.id_b_cancel);	
	btns.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
			
		}
	});
	
	btns = (Button) findViewById(R.id.id_b_upload);	
	btns.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startUpload();
			
		}
	});

}

void startUpload()
{
	
}

void takePhotoFromCamera()
{
	Intent intentFromCapture = new Intent(
			MediaStore.ACTION_IMAGE_CAPTURE);
	// 判断存储卡是否可以用，可用进行存储
	if (TupleApplication.isSDCardAvailable()) {

		intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
				.fromFile(new File(Environment
						.getExternalStorageDirectory(),
						IMAGE_FILE_NAME)));
	}else {
		Toast.makeText(this, "未找到存储卡！",
				Toast.LENGTH_LONG).show();
	}

	startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
}

void takePhotoFromFile()
{
	Intent intentFromGallery = new Intent();
	intentFromGallery.setType("image/*"); // 设置文件类型
	intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
	startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
}
public void startPhotoZoom(Uri uri) {

	Intent intent = new Intent("com.android.camera.action.CROP");
	intent.setDataAndType(uri, "image/*");
	// 设置裁剪
	intent.putExtra("crop", "true");
	// aspectX aspectY 是宽高的比例
	intent.putExtra("aspectX", 1);
	intent.putExtra("aspectY", 1);
	// outputX outputY 是裁剪图片宽高
	intent.putExtra("outputX", 320);
	intent.putExtra("outputY", 320);
	intent.putExtra("return-data", true);
	startActivityForResult(intent, 2);
}
private BitmapDrawable drawable = null;
private void getImageToView(Intent data) {
	Bundle extras = data.getExtras();
	if (extras != null) {
		Bitmap photo = extras.getParcelable("data");
		drawable = new BitmapDrawable(photo);
		ImageView iv = (ImageView) findViewById(R.id.id_img_portry);
		iv.setImageDrawable(drawable);
	}
}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// 结果码不等于取消时候
	if (resultCode != RESULT_CANCELED) {

		switch (requestCode) {
		case IMAGE_REQUEST_CODE:
			startPhotoZoom(data.getData());

			break;
		case CAMERA_REQUEST_CODE:
			if (TupleApplication.isSDCardAvailable()) {
				File tempFile = new File(
						Environment.getExternalStorageDirectory()
								+ File.separator + IMAGE_FILE_NAME);
				startPhotoZoom(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(this, "未找到存储卡，无法存储照片！",
						Toast.LENGTH_LONG).show();
			}

			break;
		case RESULT_REQUEST_CODE:
			if (data != null) {
				getImageToView(data);
			}
			break;
		}
	}
	super.onActivityResult(requestCode, resultCode, data);
}

}
