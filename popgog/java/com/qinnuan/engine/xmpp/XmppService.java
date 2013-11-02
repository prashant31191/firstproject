/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qinnuan.engine.xmpp;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.qinnuan.common.util.GUIUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.activity.MainActivity;
import com.qinnuan.engine.application.BaoguApplication;
import com.qinnuan.engine.application.MyPref;
import com.qinnuan.engine.xmpp.json.TextMessageJson;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.message.MessageSRC;
import com.qinnuan.engine.xmpp.message.MessageType;
import com.qinnuan.common.util.LogUtil;

/**
 * Service that continues to run in background and respond to the push
 * notification events from the server. This should be registered as service
 * in AndroidManifest.xml.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class XmppService extends Service implements IReciveMessage {

    private static final String LOGTAG = LogUtil
            .makeLogTag(XmppService.class);

    public static final String SERVICE_NAME = "XmppService";

    private TelephonyManager telephonyManager;

    //    private WifiManager wifiManager;
    //
    //    private ConnectivityManager connectivityManager;


    private BroadcastReceiver connectivityReceiver;

    private PhoneStateListener phoneStateListener;

    private ExecutorService executorService;

    private TaskSubmitter taskSubmitter;

    private TaskTracker taskTracker;

    private XmppManager xmppManager;


    private String deviceId;
    private boolean canRing = true;
    private Handler handler = new Handler();
    private ActivityManager activityManager;

    public XmppService() {
        connectivityReceiver = new ConnectivityReceiver(this);
        phoneStateListener = new PhoneStateChangeListener(this);
        executorService = Executors.newSingleThreadExecutor();
        taskSubmitter = new TaskSubmitter(this);
        taskTracker = new TaskTracker(this);
    }

    @Override
    public void onCreate() {
        LogUtil.e(getClass(), "onCreate.....");
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        activityManager = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        xmppManager = new XmppManager(this);
        xmppManager.getReciveMessageList().add(this);
        xmppManager.connect();
        taskSubmitter.submit(new Runnable() {
            public void run() {
                XmppService.this.start();

            }
        });
    }


    @Override
    public void onDestroy() {
        Log.d(LOGTAG, "onDestroy()...");
        stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOGTAG, "onBind()...");
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(LOGTAG, "onRebind()...");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(LOGTAG, "onUnbind()...");
        return true;
    }

    public static Intent getIntent() {
        return new Intent(SERVICE_NAME);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public TaskSubmitter getTaskSubmitter() {
        return taskSubmitter;
    }

    public TaskTracker getTaskTracker() {
        return taskTracker;
    }

    public XmppManager getXmppManager() {
        return xmppManager;
    }


    public String getDeviceId() {
        return deviceId;
    }

    public void connect() {
        Log.d(LOGTAG, "connect()...");
        taskSubmitter.submit(new Runnable() {
            public void run() {
                XmppService.this.getXmppManager().connect();
            }
        });
    }

    public void disconnect() {
        Log.d(LOGTAG, "disconnect()...");
        taskSubmitter.submit(new Runnable() {
            public void run() {
                XmppService.this.getXmppManager().disconnect();
            }
        });
    }


    private void registerConnectivityReceiver() {
        Log.d(LOGTAG, "registerConnectivityReceiver()...");
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
        IntentFilter filter = new IntentFilter();
        // filter.addAction(android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, filter);
    }

    private void unregisterConnectivityReceiver() {
        Log.d(LOGTAG, "unregisterConnectivityReceiver()...");
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_NONE);
        if (connectivityReceiver != null) {
            unregisterReceiver(connectivityReceiver);
        }
    }

    private void start() {
        registerConnectivityReceiver();
        xmppManager.connect();
    }

    private void stop() {
        Log.d(LOGTAG, "stop()...");
        unregisterConnectivityReceiver();
        xmppManager.disconnect();
        executorService.shutdown();
    }

    @Override
    public boolean reciveMessage(BaseMessage msg) {
        LogUtil.d(getClass(), "reciveMessage" + msg.toXML());
        msg.setMessageSRC(MessageSRC.FROME);
        if (canRing&&needRing(msg.getMessageType())) {
            ringAndVoice();
        }
        if (isAppOnForeground()) {
            return true;
        } else {
            changeNotifyCation(msg);
            return true;
        }
    }

    private boolean needRing(MessageType type) {
        switch (type) {
            case LASTHEAED:
                return false;
            case DYNAMICNUMBER:
                return false;
            case DELETEFRIEND:
                return false;
            default:
                return true;
        }
    }

    private void changeNotifyCation(BaseMessage message) {
        if (message.getMessageType() == MessageType.VOICE) {
            showNotification(message.getUserinfo().getNickname()+"你的好友给你发来了声音!");
        } else if (message.getMessageType() == MessageType.LOCATION) {
            showNotification(message.getUserinfo().getNickname()+"你的好友给你发来了地图!");
        } else if (message.getMessageType() == MessageType.IMAGE) {
            showNotification(message.getUserinfo().getNickname()+"你的好友给你发来了图片!");
        } else if(message.getMessageType()==MessageType.TEXT) {
            TextMessageJson json = (TextMessageJson) message.getMessage();
            showNotification(json.getText());
        }else if(message.getMessageType()==MessageType.ADDFRIEND){
            showNotification("你的好友通过了你的好友请求");
        }
    }


    /**
     * Class for summiting a new runnable task.
     */
    public class TaskSubmitter {

        final XmppService notificationService;

        public TaskSubmitter(XmppService notificationService) {
            this.notificationService = notificationService;
        }

        @SuppressWarnings("unchecked")
        public Future submit(Runnable task) {
            Future result = null;
            if (!notificationService.getExecutorService().isTerminated()
                    && !notificationService.getExecutorService().isShutdown()
                    && task != null) {
                result = notificationService.getExecutorService().submit(task);
            }
            return result;
        }

    }

    /**
     * Class for monitoring the running task count.
     */
    public class TaskTracker {

        final XmppService notificationService;

        public int count;

        public TaskTracker(XmppService notificationService) {
            this.notificationService = notificationService;
            this.count = 0;
        }

        public void increase() {
            synchronized (notificationService.getTaskTracker()) {
                notificationService.getTaskTracker().count++;
                Log.d(LOGTAG, "Incremented task count to " + count);
            }
        }

        public void decrease() {
            synchronized (notificationService.getTaskTracker()) {
                notificationService.getTaskTracker().count--;
                Log.d(LOGTAG, "Decremented task count to " + count);
            }
        }

    }

    private void ringAndVoice() {
        canRing = false;
        MyPref p = MyPref.getInstance(this);
        if (p.isSoundOpen()) {
            GUIUtil.ring(this);
        }
        if (p.isShakeOpen()) {
            GUIUtil.vibrate(500, this);
        }
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                canRing = true;
            }
        }, 2000);
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals("com.showu.engine")
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    // 显示Notification
    public void showNotification(String messageContent) {
        // 创建一个NotificationManager的引用
        NotificationManager notificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(BaoguApplication.NOTIFYCATION_ID);
        // 定义Notification的各种属性
        Notification notification = new Notification(R.drawable.logo, "爆谷电影",
                System.currentTimeMillis());
        // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        // 点击后自动清除Notification
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.defaults = Notification.DEFAULT_LIGHTS;
        notification.ledARGB = Color.BLUE;
        notification.ledOnMS = 5000;

        // 设置通知的事件消息
        CharSequence contentTitle = "爆谷电影"; // 通知栏标题
        CharSequence contentText =messageContent;

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        LogUtil.e(getClass(), contentText);
        notification.setLatestEventInfo(this, contentTitle, contentText,
                contentIntent);
        notificationManager.notify(BaoguApplication.NOTIFYCATION_ID, notification);
    }

}
