package com.ems.express.frame.widget.gallery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.ems.express.R;
import com.ems.express.frame.widget.gallery.ImageGridAdapter.TextCallback;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class ImageGridActivity extends Activity {
	
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;
	AlbumHelper helper;
	Button bt;
    Button btnreturn;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ImageGridActivity.this, "最多选择6张图片", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_image_grid);
		
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);//从pickPhotoActivity中得到数据

		initView();
		int tempsize=PickPhotoActivity.COUNTPIC+Bimp.drrtemp.size();
	    Toast.makeText(getApplicationContext(), "您已选择了"+tempsize+"张图片,您最多选择6张图片！", Toast.LENGTH_SHORT).show();//在此可以友情提示一下，已选择了几张图片；
		bt = (Button) findViewById(R.id.btn_selected);//“完成”按钮
		btnreturn=(Button)this.findViewById(R.id.button1);//"返回按钮"
		btnreturn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
			}
			
		});
		bt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}
				int k=PickPhotoActivity.COUNTPIC+list.size();
				/*if(Bimp.drrtemp.size()>0){
					//说明是第二次进行选择
					k=k+Bimp.drrtemp.size();
				}*/
				if(k<=PickPhotoActivity.MAXPIC){
					for (int i = 0; i < list.size(); i++) {
						if (Bimp.drr.size() < 6) {
							Bimp.drr.add(list.get(i));
						}
					}
					//Common.COUNTPIC=k;
					Intent intent = new Intent();
					setResult(Activity.RESULT_FIRST_USER,intent);
					finish();
				}
				else{
					Toast.makeText(getApplicationContext(), "您最多选择6张图片！", Toast.LENGTH_SHORT).show();
				}
			
			}

		});
	}

		
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridView1);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
				mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.notifyDataSetChanged();
			}

		});

	}

}
