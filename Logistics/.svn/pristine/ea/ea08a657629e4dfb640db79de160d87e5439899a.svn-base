package com.ems.express.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.ems.express.R;

public class AnimationUtil extends Dialog{

	private ImageView  mLoadingImageView;
    private AnimationDrawable mLoadingAnimationDrawable;
    public AnimationUtil(Context context,int theme) {
        super(context,theme);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View loadingView=LayoutInflater.from(getContext()).inflate(R.layout.dialog_animation, null);
        mLoadingImageView=(ImageView) loadingView.findViewById(R.id.send_chrysanthemum);
        mLoadingImageView.setImageResource(R.drawable.progress_villain);
        setContentView(loadingView);
        setCanceledOnTouchOutside(false);
    }
     
    @Override
    public void show() {
        super.show();
        new Handler(){}.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadingAnimationDrawable =(AnimationDrawable) mLoadingImageView.getDrawable();
                mLoadingAnimationDrawable.start();
            }
        }, 10);
    }
    @Override
    public void dismiss() {
        super.dismiss();
        mLoadingAnimationDrawable =(AnimationDrawable) mLoadingImageView.getDrawable();
        mLoadingAnimationDrawable.stop();
    }
	
}
