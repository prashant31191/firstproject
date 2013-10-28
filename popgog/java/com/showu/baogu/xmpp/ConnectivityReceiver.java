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
package com.showu.baogu.xmpp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.showu.common.util.LogUtil;

/** 
 * A broadcast receiver to handle the changes in network connectiion states.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    

    private XmppService notificationService;

    public ConnectivityReceiver(XmppService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(getClass(), "ConnectivityReceiver.onReceive()...");
        String action = intent.getAction();
        LogUtil.d(getClass(), "action=" + action);

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
        	 LogUtil.d(getClass(), "Network Type  = " + networkInfo.getTypeName());
        	 LogUtil.d(getClass(), "Network State = " + networkInfo.getState());
            if (networkInfo.isConnected()) {
            	 LogUtil.d(getClass(), "Network connected");
//                notificationService.connect();
            }
        } else {
        	 LogUtil.d(getClass(), "Network unavailable");
//            notificationService.disconnect();
        }
    }

}
