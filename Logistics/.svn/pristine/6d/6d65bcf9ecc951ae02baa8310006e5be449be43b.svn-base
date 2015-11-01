package com.ems.express.ui.chat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ems.express.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoLView extends Activity {
    String path = "";
    String type ="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_lview);
//		UtilMethod.showProgressDialog(PhotoLView.this);
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options=new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_photo).cacheInMemory(false)
		.cacheOnDisc(false).bitmapConfig(Bitmap.Config.RGB_565).build();
		path = getIntent().getStringExtra("photo_path");
		type = getIntent().getStringExtra("type");
		LinearLayout linearRight = (LinearLayout) findViewById(R.id.topbar_linear2);
		LinearLayout linearLeft = (LinearLayout) findViewById(R.id.topbar_linear1);
		if("0".equals(type)){
			linearRight.setVisibility(View.VISIBLE);
			linearRight.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent data = new Intent();
					data.putExtra("sendImg", path);
					setResult(Activity.RESULT_OK,data);
					finish();
				}
			});
		}else{
			linearRight.setVisibility(View.INVISIBLE);
		}
		TextView tView = (TextView) findViewById(R.id.topbar_tv);
		tView.setText("个人图像");
		ImageView photo_img = (ImageView)findViewById(R.id.large_photo);
		linearLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		try {
			if("0".equals(type)){
			imageLoader.displayImage("file://"+path, photo_img, options);
			}else{
			imageLoader.displayImage(path, photo_img, options);
			}
//			UtilMethod.dismissProgressDialog(PhotoLView.this);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(PhotoLView.this, "文件没有找到", 2000).show();
		}
		
	}

}
