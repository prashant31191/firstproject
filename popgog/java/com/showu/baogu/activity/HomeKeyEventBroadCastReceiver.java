package com.showu.baogu.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by showu on 13-6-14.
 */
class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {
    private OnHomeClick click ;
    public interface  OnHomeClick{
        public void onHomeShortClick() ;
        public  void onHomeLongClick() ;
    }

  public   HomeKeyEventBroadCastReceiver(OnHomeClick click){
        this.click=click ;
    }
    static final String SYSTEM_REASON = "reason";
    static final String SYSTEM_HOME_KEY = "homekey";//home key
    static final String SYSTEM_RECENT_APPS = "recentapps";//long home key

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_REASON);
            if (reason != null) {
                if (reason.equals(SYSTEM_HOME_KEY)) {
                    // home key处理点
                    click.onHomeShortClick();
                } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                    // long home key处理点
                    click.onHomeLongClick();
                }
            }
        }
    }
}
