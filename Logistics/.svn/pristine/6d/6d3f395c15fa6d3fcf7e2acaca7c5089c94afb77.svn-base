package com.ems.express.ui;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import com.ems.express.util.DialogUtils;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by mengxianzheng on 15-2-11.
 */
public class EditPersonalInfoActivity extends BaseActivity{

    @InjectView(R.id.et_name)
    EditText mNameView;
    private int mAction;

    @InjectView(R.id.bt_jump_send)
    TextView mBtnSave;
    
    @InjectView(R.id.tex_title)
    TextView mTitle;

    private String mCommitContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal);
        ButterKnife.inject(this);
        
        
        mBtnSave.setVisibility(View.VISIBLE);
        mBtnSave.setText("保存");
        mAction = getIntent().getIntExtra("action",-1);
        String name = getIntent().getStringExtra("name");

        if(mAction == 1){
        	mTitle.setText("我的名字");
            setHeadTitle(getString(R.string.C_NAME));
            mNameView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            mNameView.setHint(getString(R.string.C_NAME));
        }else if(mAction == 2){//手机号
        	mTitle.setText("我的电话");
            setHeadTitle(getString(R.string.C_PHONE));
            mNameView.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_VARIATION_NORMAL);
            mNameView.setHint(getString(R.string.C_PHONE));
        }else{
            setHeadTitle(getString(R.string.C_ADDRESS));
            mNameView.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_NORMAL);
            mNameView.setHint(getString(R.string.C_ADDRESS));
        }

        mNameView.setText(name);
        if(name.length() > 0){
            mNameView.setSelection(name.length());
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

    @OnClick(R.id.bt_jump_send)
    void toSave(){
        mCommitContent =  mNameView.getText().toString().trim();
        if(TextUtils.isEmpty(mCommitContent)){
            String toast = "";
            if(mAction == 1){
                toast = getString(R.string.C_NAME_TIP);
                SpfsUtil.saveName(mCommitContent);
            }else if(mAction == 2){
            	SpfsUtil.saveName(mCommitContent);
                toast = getString(R.string.C_HINT_REGIST_USER);
            }else if(mAction == 3){
            	SpfsUtil.saveName(mCommitContent);
                toast = getString(R.string.C_ADDRESS_TIP);
            }
            ToastUtil.show(this,toast);
            return;
        } else {
            if(mAction == 1){
//            	//校验姓名
//            	if(mCommitContent.getBytes().length>15){
//            		ToastUtil.show(this, "输入的名字过长，请重新输入");
//            		return;
//            	}
                SpfsUtil.saveName(mCommitContent);
            }else if(mAction == 2){
            	//校验电话
            	if(!DialogUtils.isMobileNO(mCommitContent)){
            		ToastUtil.show(this, "请检查电话是否正确");
        			return;
            	}
            	SpfsUtil.saveTelephone(mCommitContent);
            }else if(mAction == 3){
            	SpfsUtil.saveAddress(mCommitContent);
            }
        }
        setResult(RESULT_OK);
        finish();

//        HashMap<String, Object> json = new HashMap<String, Object>();
//        json.put("phone_num", SpfsUtil.loadPhone());
//        json.put("name", mAction == 1 ? mCommitContent : SpfsUtil.loadName());
//        json.put("address", mAction == 3 ? mCommitContent : SpfsUtil.loadAddress());
//        json.put("telephone", mAction == 2 ? mCommitContent : SpfsUtil.loadPhone());
//        json.put("image",  SpfsUtil.loadHeader());
//
//        String params =  ParamsUtil.getUrlParamsByMap(json);
//        System.out.println("json-pre:" +params);
//        MyRequest<Object> req = new MyRequest<Object>(
//                Request.Method.POST,
//                null,
//                Constant.UpdateUserInfo,
//                new Response.Listener<Object>() {
//
//                    @Override
//                    public void onResponse(Object arg0) {
//                        try{
//                            String result = (String) arg0;
//                            JSONObject object = new JSONObject(result.toString());
//                            if(object.getInt("result") == 1){
//                                String sucess = object.getJSONObject("body").getString("success");
//                                if(mAction == 1){
//                                    SpfsUtil.saveName(mCommitContent);
//                                }else if(mAction == 2){
//                                    SpfsUtil.savePhone(mCommitContent);
//                                }else if(mAction == 3){
//                                    SpfsUtil.saveAddress(mCommitContent);
//                                }
//                                setResult(Activity.RESULT_OK);
//                                finish();
//                            }else {
//                                String error = object.getJSONObject("body").getString("error");
//                                ToastUtil.show(EditPersonalInfoActivity.this,error);
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError arg0) {
//                arg0.printStackTrace();
//                ToastUtil.show(EditPersonalInfoActivity.this,getString(R.string.C_NET_ERROR));
//
//            }
//        }, params);
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(req);
    }


}
