package com.showu.common.util;

import android.util.Log;
import com.showu.baogu.BuildConfig;

public class LogUtil {

    private static boolean DEBUG = BuildConfig.DEBUG;
    //private static boolean DEBUG = true;
    private static final String TAG = "baogu";

	public static void d(Class classes, Object msg) {
		if (DEBUG) {
			Log.d(TAG+classes.getSimpleName(), msg.toString());
		}
	}

	public static void i(Class classes, Object msg) {
		if (DEBUG) {
            Log.i(TAG+classes.getSimpleName(), msg.toString());
		}
	}

	public static void v(Class classes, Object msg) {
		if (DEBUG) {
            Log.v(TAG+classes.getSimpleName(), msg.toString());
		}
	}

	public static void e(Class classes, Object msg) {
		if (DEBUG) {
            Log.e(TAG+classes.getSimpleName(), msg.toString());
		}
	}

    public  static String makeLogTag(Class c){
        return c.getSimpleName();
    }

}
