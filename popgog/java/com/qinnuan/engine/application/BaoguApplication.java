package com.qinnuan.engine.application;

import android.app.Activity;
import android.app.Application;

import com.qinnuan.engine.activity.MainActivity;
import com.qinnuan.engine.activity.user.UserInfoActivity;
import com.qinnuan.engine.xmpp.XmppManager;

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
