package com.ems.express.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ems.express.R;
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.ui.view.CircleImageView;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.FileUtil;
import com.ems.express.util.LogUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by mengxianzheng on 15-2-9.
 */
public class PersonalCenterActivity extends BaseActivity {
	private static final int CHANGE_NAME = 1;
	private static final int CHANGE_PHONE = 2;
	private static final int CHANGE_ADDRESS = 3;

	public static final int REQUEST_CODE_CAMERA = 100;
	public static final int REQUEST_CODE_LOCAL = 101;
	public static final int REQUEST_CROP_BIG_IMAGE = 102;
	public static final int REQUEST_CODE_LOCAL_KITKAT = 103;

	private int mChangeAction = -1;
	private Dialog mDialog;

	private File mTempFile;
	private Uri tempImageUri;
	private Uri tempDstImageUri;

	@InjectView(R.id.iv_icon)
	CircleImageView mIconView;

	@InjectView(R.id.tv_name)
	TextView mNameView;

	@InjectView(R.id.tv_phone)
	TextView mPhoneView;

	@InjectView(R.id.tv_address)
	TextView mAddressView;
	
	@InjectView(R.id.tv_qrcode)
	TextView mQrCodeView;
	
	private String mPhotoStr;
	private String mName;
	private String mTelephone;
	private String mAddress;
	private TextView jumpSend;

	private static final int PHOTO = 0;
	private static final int NAME = 1;
	private static final int TELEPHONE = 2;
	private static final int ADDRESS = 3;
	private static final int ALL = 4;

	public void back(View v) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_center);
		ButterKnife.inject(this);
		initDialog();
		jumpSend=(TextView)findViewById(R.id.bt_jump_send);
		if(!TextUtils.isEmpty(SpfsUtil.loadName()) && !"null".equals(SpfsUtil.loadName())){
			mNameView.setText(SpfsUtil.loadName());
		}
		if(TextUtils.isEmpty(SpfsUtil.loadTelephone()) | "null".equals(SpfsUtil.loadTelephone())){
			mPhoneView.setText(SpfsUtil.loadPhone());
		}else {
			mPhoneView.setText(SpfsUtil.loadTelephone());
		}
		if(!TextUtils.isEmpty(SpfsUtil.loadAddress()) && !"null".equals(SpfsUtil.loadAddress())){
			mAddressView.setText(SpfsUtil.loadAddress());
		}
		
		String headImageUrl = SpfsUtil.loadHeadImageUrl();
		if (headImageUrl != "") {
			Bitmap photo = BitmapFactory.decodeFile(headImageUrl);
			mIconView.setImageBitmap(photo);
		} else {
			Bitmap aa = BitmapFactory.decodeResource(getResources(), R.drawable.img_default_user);
			mIconView.setImageBitmap(aa);
		}
		setHeadTitle("个人中心");
//		jumpSend.setVisibility(View.GONE);
		
		Intent intent = getIntent();
		if("HomeImg".equals(intent.getStringExtra("from"))){
			if (mDialog != null && !mDialog.isShowing()) {
				mDialog.show();
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//友盟统计
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		//友盟统计
		MobclickAgent.onPause(this);
	}

	private void initDialog() {
		mDialog = new Dialog(this, R.style.opDialogTheme);
		mDialog.setContentView(R.layout.pop_image_op);
		mDialog.getWindow().setWindowAnimations(R.style.menuAnimation);
		mDialog.getWindow().setGravity(Gravity.BOTTOM);
		mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
		mDialog.setCanceledOnTouchOutside(true);

		mDialog.findViewById(R.id.btn_tack_photo).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!FileUtil.isSdcardReady()) {
					ToastUtil.show(PersonalCenterActivity.this, getString(R.string.C_NO_SDCARD));
					return;
				}
				mTempFile = new File(FileUtil.getTempFileName());
				if (mTempFile != null) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempFile));
					startActivityForResult(intent, REQUEST_CODE_CAMERA);
				}
				mDialog.dismiss();
			}
		});

		mDialog.findViewById(R.id.btn_select_image).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!FileUtil.isSdcardReady()) {
					ToastUtil.show(PersonalCenterActivity.this, getString(R.string.C_NO_SDCARD));
					return;
				}
				openPhotoAlbum();
				mDialog.dismiss();
			}
		});
		mDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});

	}

	@OnClick(R.id.btn_icon)
	void toEditImage() {
		if (mDialog != null && !mDialog.isShowing()) {
			mDialog.show();
		}
	}

	@OnClick(R.id.btn_name)
	void toEditName() {
		mChangeAction = CHANGE_NAME;
		String name = SpfsUtil.loadName();
		if(TextUtils.isEmpty(name) || "null".equals(name)){
			name = "";
		}
		startIntent(mChangeAction, name);
	}

	@OnClick(R.id.btn_phone)
	void toEditPhone() {
		mChangeAction = CHANGE_PHONE;
		startIntent(mChangeAction, SpfsUtil.loadPhone());
	}

	@OnClick(R.id.btn_address)
	void toEditAddress() {
		mChangeAction = CHANGE_ADDRESS;
		startIntent(mChangeAction, SpfsUtil.loadAddress());
	}
	
	@OnClick(R.id.btn_qrcode)
	void showQrcode(){
		DialogUtils.getQrcodeDialog(this).show();
	}

	private void startIntent(int action, String content) {
		Intent intent = new Intent(this, EditPersonalInfoActivity.class);
		intent.putExtra("action", action);
		intent.putExtra("name", content);
		startActivityForResult(intent, action);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode == Activity.RESULT_OK) {
				if (requestCode == REQUEST_CODE_CAMERA) {
					File tempFile = new File(FileUtil.getTempFileName());
					tempDstImageUri = Uri.fromFile(tempFile);
					tempImageUri = Uri.fromFile(mTempFile);
					cropImage(tempImageUri, 320, 320, REQUEST_CROP_BIG_IMAGE, tempDstImageUri);
				} else if (requestCode == REQUEST_CROP_BIG_IMAGE) {
					Bitmap photo = null;
					if (tempDstImageUri != null) {
						String a = tempDstImageUri.getEncodedPath();
						SpfsUtil.saveHeadImageUrl(a);
						LogUtil.print(a);
						photo = BitmapFactory.decodeFile(tempDstImageUri.getPath());
						
						
						
						/** added by sweetvvck */
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
						byte[] byteArray = byteArrayOutputStream.toByteArray();
						mPhotoStr = Base64.encodeToString(byteArray, Base64.DEFAULT);
						LogUtil.print("\r\n\r\n" + mPhotoStr + "\r\n\r\n");
						updateUserInfo(PHOTO);
						mIconView.setImageBitmap(photo);
					}
				} else if (requestCode == REQUEST_CODE_LOCAL) {
					File tempFile = new File(FileUtil.getTempFileName());
					tempDstImageUri = Uri.fromFile(tempFile);
					tempImageUri = data.getData();
					cropImage(tempImageUri, 320, 320, REQUEST_CROP_BIG_IMAGE, tempDstImageUri);
				} else if (requestCode == REQUEST_CODE_LOCAL_KITKAT) {
					String path = getPath(this, data.getData());
					File tempFile = new File(FileUtil.getTempFileName());
					tempDstImageUri = Uri.fromFile(tempFile);
					tempImageUri = Uri.fromFile(new File(path));
					cropImage(tempImageUri, 320, 320, REQUEST_CROP_BIG_IMAGE, tempDstImageUri);
				} else if (requestCode == CHANGE_NAME) {
					mNameView.setText(SpfsUtil.loadName());
					mName = SpfsUtil.loadName();
					updateUserInfo(NAME);
				} else if (requestCode == CHANGE_PHONE) {
					mTelephone = SpfsUtil.loadTelephone();
					mPhoneView.setText(mTelephone);
					updateUserInfo(TELEPHONE);
				} else if (requestCode == CHANGE_ADDRESS) {
					mAddressView.setText(SpfsUtil.loadAddress());
					mAddress = (SpfsUtil.loadAddress());
					updateUserInfo(ADDRESS);
				}
			} else {
				super.onActivityResult(requestCode, resultCode, data);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateUserInfo(int infoType) {
		String mPhone = SpfsUtil.loadPhone();
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("phone_num", mPhone);
		switch (infoType) {
		case PHOTO:
			json.put("image", mPhotoStr.replace("+", "%2B"));
			Log.e("msg","上传图片的string值："+mPhotoStr);
			break;
		case NAME:
			json.put("name", mName);
			break;
		case TELEPHONE:
			json.put("telephone", mTelephone);
			break;
		case ADDRESS:
			json.put("address", mAddress);
			break;
		case ALL:
			json.put("telephone", mTelephone);
			json.put("image", mPhotoStr);
			json.put("name", mName);
			json.put("address", mAddress);
			break;
		default:
			break;
		}
		String params = ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" + params);
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.UpdateUserInfo,
				new Response.Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						Log.e("msg", arg0.toString());
						try {
							String result = (String) arg0;
							JSONObject object = new JSONObject(result.toString());
							if (object.getInt("result") == 1) {
								String resultJson = object.getJSONObject("body").getString("success");
								ToastUtil.show(PersonalCenterActivity.this, resultJson);
							} else {
								String error = object.getJSONObject("body").getString("error");
								ToastUtil.show(PersonalCenterActivity.this, error);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();

					}
				}, params);

		RequestQueue queue = Volley.newRequestQueue(this);
		queue.add(req);
		queue.start();
	}

	// 截取拍照图片
	private void cropImage(Uri sourceUri, int outputX, int outputY, int requestCode, Uri outPutUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(sourceUri, "image/*");
		// 裁剪框的比例
		intent.putExtra("aspectX", outputX);
		intent.putExtra("aspectY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra("crop", true);
		intent.putExtra("scaleUpIfNeeded", true);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		// 图片格式
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
		startActivityForResult(intent, requestCode);
	}

	public void openPhotoAlbum() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			startActivityForResult(intent, REQUEST_CODE_LOCAL_KITKAT);
		} else {
			startActivityForResult(intent, REQUEST_CODE_LOCAL);
		}
	}

	@SuppressLint("NewApi")
	public String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

}
