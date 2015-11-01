package com.newcdc.tools;

import android.content.Context;
import android.widget.Toast;

public class ShowToast {
	
	 private static Toast mToast;
	 
	public static void showToast(Context context, String msg, int duration) {

        if (mToast == null) {
        	
                mToast = Toast.makeText(context, msg, duration);
        } else {
        		
                mToast.setText(msg);
                
        }
        mToast.show();
}

}
