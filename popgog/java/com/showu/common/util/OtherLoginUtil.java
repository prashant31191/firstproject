package com.showu.common.util;

import android.app.Activity;
import android.view.View;

import com.tencent.tauth.Tencent;

public class OtherLoginUtil {

	private static OtherLoginUtil myInstance;

    private OtherLoginUtil() {}

    public static OtherLoginUtil getInstance(){
        if (null == myInstance) {
            myInstance = new OtherLoginUtil();
        }
        return myInstance;
    }

	private Tencent mTencent;
	
	public Tencent getmTencent() {
		return mTencent;
	}

	public void setmTencent(Tencent mTencent) {
		this.mTencent = mTencent;
	}

	/**
	 * 点击返回监听事件
	 * 
	 * @param activity
	 * @return
	 */
	public static View.OnClickListener finish(final Activity activity) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				activity.finish();
			}
		};
	}

//    public void onClickQQLogin(final Context context, IUiListener authListener,
//    		final String type, final IAddShareListener shareListener) {
//    	if (mTencent == null) {
//    		mTencent = Tencent.createInstance(Const.APP_ID, context);
//		}
//        if (!mTencent.isSessionValid()) {
//            mTencent.login((Activity) context, Const.SCOPE, authListener);
//        } else {
//        	if (!type.equals(Const.JUSTAUTH)) {
//        		addTencentTopic(type, mTencent, shareListener);
//        	} else {
//        		mTencent.logout(context);
//        	}
//        }
//    }

    public interface IAddShareListener {
        void onShareComplete();
        void onShareFailed();
    }

	

	 

	

    
}
