package com.showu.baogu.application;

import android.app.Activity;
import android.app.Application;

import com.showu.baogu.activity.MainActivity;
import com.showu.baogu.activity.user.UserInfoActivity;
import com.showu.baogu.xmpp.XmppManager;
import com.showu.common.util.GUIUtil;

import java.util.ArrayList;
import java.util.List;

public class BaoguApplication extends Application {
    public static final int NOTIFYCATION_ID = 0x000023;
    private  List<Activity> activityList = new ArrayList<Activity>();
    public  static XmppManager xmppManager ;
    public static MainActivity activity ;

    public void addActivityToList(Activity activity) {
        activityList.add(activity);
    }

    public void exit() {
        for (Activity a : activityList) {
            if(a instanceof UserInfoActivity) continue;
            a.finish();
        }
    }



}
