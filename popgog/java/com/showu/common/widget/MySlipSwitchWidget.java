package com.showu.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.common.util.LogUtil;

public class MySlipSwitchWidget extends LinearLayout {

	private Bitmap switch_on_Bkg;
	private RelativeLayout linearlayout_LL;
	private MySlipSwitch slipswitch;
	private TextView tvOn;
	private TextView tvOff;
	private Context mContext;
	public MySlipSwitchWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public MySlipSwitchWidget(Context context) {
		super(context);
		mContext = context;
		init();
	}

	private void init() {
		
		LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.item_myslip_switch, this);
		
		tvOn = (TextView) view.findViewById(R.id.item_myslip_textView1);
	    tvOff = (TextView) view.findViewById(R.id.item_myslip_textView2);
	    switch_on_Bkg = BitmapFactory.decodeResource(getResources(), R.drawable.btn_switch_bg);
        LayoutParams layoutParams = new LayoutParams(switch_on_Bkg.getWidth(), switch_on_Bkg.getHeight());
        
        linearlayout_LL = (RelativeLayout)view.findViewById(R.id.main_linearlayout);
        linearlayout_LL.setLayoutParams(layoutParams);
        
        slipswitch = (MySlipSwitch) view.findViewById(R.id.item_myslipswitch);
        slipswitch.setSwitchState(true);
        slipswitch.setImageResource(R.drawable.btn_switch_bg, R.drawable.btn_switch_bg, R.drawable.btn_slip_on);
        //setOn();
        LogUtil.e(getClass(), "init slipswitch==>"+slipswitch);
        slipswitch.setOnSwitchListener(new MySlipSwitch.OnSwitchListener() {
        	 
			@Override
			public void onSwitched(boolean isSwitchOn) {
				if(isSwitchOn) {
					//setOn();
					flag = true;
					setStatus(flag);
					//Toast.makeText(mContext, "已经开启", Toast.LENGTH_SHORT).show();
				} else {
					//setOff();
					flag = false;
					setStatus(flag);
					//Toast.makeText(mContext, "已经关闭", Toast.LENGTH_SHORT).show();
				}
				mSaveInter.save(flag);
			}
		});
	}
	
	public interface SaveStatus {
		public void save(boolean status);
	}
	
	private SaveStatus mSaveInter;
	public void setMSaveInter(SaveStatus saveInter) {
		this.mSaveInter = saveInter;
	}
	
	private void setOn() {
		tvOff.setVisibility(View.GONE);
		tvOn.setVisibility(View.VISIBLE);
		//tvOn.setTextColor("#2c95e8");
		slipswitch.setImageResource(R.drawable.btn_switch_bg, 
				R.drawable.btn_switch_bg, R.drawable.btn_slip_on);
	}
    
	private void setOff() {
		tvOn.setVisibility(View.GONE);
		tvOff.setVisibility(View.VISIBLE);
		slipswitch.setImageResource(R.drawable.btn_switch_bg, 
				R.drawable.btn_switch_bg, R.drawable.btn_slip_off);
	}
	
	public void setStatus(boolean on) {
		if(on) {
			setOn();
			//slipswitch.setSwitchState(true);
		} else {
			//slipswitch.setSwitchState(false);
			setOff();
		}
		flag = on;
		slipswitch.updateSwitchState(on);
		//slipswitch.invalidate();
	}
	
	private boolean flag = true;
	/** true 就是开， false 就是关 */
	public boolean getStatus() {
		return flag;
	}
}
