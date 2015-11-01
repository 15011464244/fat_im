package com.ems.express.frame.widget.gallery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ems.express.R;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PickPhotoActivity extends Activity {
	
	
	public static final int PHOSELECT=6;
	public static int COUNTPIC=0;
	public static final int MAXPIC=6;
	
	private GridView mGridView;
	private static String TAG = "Thumbnails";
	private ArrayList<HashMap<String, String>> list;
	private ContentResolver cr;
	private int tempsize;
	List<ImageBucket> dataList;
	ImageBucketAdapter adapter;
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	Button btnReturn;
	TextView btnOk;
	String type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_pick_photo);
		type=getIntent().getStringExtra("type");
		//findViews();
		btnReturn=(Button)this.findViewById(R.id.pick_button_left);
		btnReturn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			 Bimp.drrtemp=new ArrayList<String>();
			 finish();
			}
		});
		btnOk=(TextView)this.findViewById(R.id.pick_button_right);
		this.findViewById(R.id.pick_button_right).setVisibility(View.INVISIBLE);
		showAlbum();
	}
		
	public void showAlbum() {
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		initData();
		initView();	
	}
	
	private void initData() {
		dataList = helper.getImagesBucketList(false);	
		BitmapCache.bimap=BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
	}
	
	private void initView() {
		mGridView = (GridView) findViewById(R.id.image_gridView);
		adapter = new ImageBucketAdapter(PickPhotoActivity.this, dataList);
		mGridView.setAdapter(adapter);

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
		        intent = new Intent(PickPhotoActivity.this, ImageGridActivity.class);
				intent.putExtra(PickPhotoActivity.EXTRA_IMAGE_LIST, (Serializable) dataList.get(position).imageList);
				startActivityForResult(intent,PHOSELECT);
			}

		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//Common.COUNTPIC=Bimp.drrtemp.size()+Common.COUNTPIC;
		if((requestCode==PHOSELECT)&&(data!=null)){
			setResult(Activity.RESULT_FIRST_USER, data); 
			finish();
			
		}
		
	}
}
