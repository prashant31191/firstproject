package com.showu.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.showu.baogu.R;

public class LoadingDialog extends Dialog {
private Context mContext ;
private String message ;
	public LoadingDialog(Context context, String mesString) {
		super(context, R.style.loading_dialog);
//        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mContext=context ;
		this.message=mesString ;
        setCanceledOnTouchOutside(false);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 LayoutInflater inflater = LayoutInflater.from(mContext);  
	        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
	        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
	        // main.xml中的ImageView  
	        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
	        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
	        // 加载动画  
	        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(  
	        		mContext, R.anim.loading_animation);
	        // 使用ImageView显示动画  
	        spaceshipImage.startAnimation(hyperspaceJumpAnimation);  
	        tipTextView.setText(message);// 设置加载信息  
//	        loadingDialog.setCancelable(false);// 不可以用“返回键”取消  
	     setContentView(layout, new LinearLayout.LayoutParams(  
	                LinearLayout.LayoutParams.FILL_PARENT,  
	                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局  
	}

}
