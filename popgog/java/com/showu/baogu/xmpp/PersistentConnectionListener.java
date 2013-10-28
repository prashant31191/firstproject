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

import android.content.Context;
import android.content.Intent;
import com.showu.common.util.LogUtil;
import org.jivesoftware.smack.ConnectionListener;

/** 
 * A listener class for monitoring connection closing and reconnection events.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class PersistentConnectionListener implements ConnectionListener {
	public static  final String CONNECTION_ACTION = "com.showu.baogu.xmpp.connection" ;
	public static final int ERROR =0 ; 
	public static final int RECONNECTION=1 ; 
	public static final int RECCONNECTION_SUCCESS=2 ; 
	public static final int CCONNECTION_CLOSED=3 ; 
	public static final int RECONNECTION_FAILD=4 ; 
   

    private final XmppManager xmppManager;
    private Context mContext ;
    public PersistentConnectionListener(XmppManager xmppManager,Context context) {
        this.xmppManager = xmppManager;
        this.mContext=context ;
    }

    @Override
    public void connectionClosed() {
        LogUtil.v(getClass(),"connectionClosed");
        Intent intent = new Intent(CONNECTION_ACTION);
		intent.putExtra("action", CCONNECTION_CLOSED) ;
		mContext.sendBroadcast(intent);
        if(!xmppManager.disconnect){
            xmppManager.startReconnectionThread();
        }
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        LogUtil.v(getClass()," connectionClosedOnError-->"+e.getMessage());
        Intent intent = new Intent(CONNECTION_ACTION);
		intent.putExtra("action", ERROR) ;
		mContext.sendBroadcast(intent);
        if (xmppManager.getConnection() != null
                && xmppManager.getConnection().isConnected()) {
            xmppManager.getConnection().disconnect();
        }
        xmppManager.startReconnectionThread();
    }

    @Override
    public void reconnectingIn(int seconds) {
        LogUtil.e(PersistentConnectionListener.class,"reconnecting in ....");
        Intent intent = new Intent(CONNECTION_ACTION);
		intent.putExtra("action", RECONNECTION) ;
		mContext.sendBroadcast(intent);
    }

    @Override
    public void reconnectionFailed(Exception e) {
        LogUtil.e(PersistentConnectionListener.class,"reconnection Failed ....");
        Intent intent = new Intent(CONNECTION_ACTION);
		intent.putExtra("action", RECONNECTION_FAILD) ;
		mContext.sendBroadcast(intent);
    }

    @Override
    public void reconnectionSuccessful() {
        LogUtil.v(getClass()," reconnectionSuccessful");
        Intent intent = new Intent(CONNECTION_ACTION);
		intent.putExtra("action", RECCONNECTION_SUCCESS) ;
		mContext.sendBroadcast(intent);
    }

}
