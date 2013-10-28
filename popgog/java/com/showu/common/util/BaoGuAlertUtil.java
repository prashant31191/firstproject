package com.showu.common.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class BaoGuAlertUtil {
	public static void showAlert(Context onContext, String title,
			String message, OnClickListener onClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(onContext);
		builder.setMessage(message).setTitle(title).setCancelable(true)
				.setPositiveButton("确定", onClickListener)
				.setNegativeButton("取消", new OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show() ;
	}
}
