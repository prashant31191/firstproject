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

import android.content.Context;
import android.os.Handler;

import com.qinnuan.engine.application.BaoguApplication;
import com.qinnuan.engine.application.MyPref;
import com.qinnuan.common.util.LogUtil;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.Time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * This class is to manage the XMPP connection between client and server.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class XmppManager {

    private static final Class LOGTAG = XmppManager.class;

    private static final String XMPP_RESOURCE_NAME = "android";
    public static long bettewServerTiem = 0;

    private Context context;
    //    private static final String SERVIER_ADDRESS = "xmpptest.popgog.com";
//    private static final String USER_NAME = "user25f85b60f0ce11e2b93f00163e07636b";
//    private static final String PASSWORD = "491838";
    private XmppService.TaskSubmitter taskSubmitter;

    private XmppService.TaskTracker taskTracker;


    private XMPPConnection connection;


    private ConnectionListener connectionListener;

    private PacketListener notificationPacketListener;

    private Handler handler;

    private List<Runnable> taskList;

    private boolean running = false;

    private Future<?> futureTask;

    private Thread reconnection;

    public boolean disconnect = false;
    private List<IReciveMessage> reciveMessageList;

    public XmppManager(XmppService notificationService) {
        context = notificationService;
        taskSubmitter = notificationService.getTaskSubmitter();
        taskTracker = notificationService.getTaskTracker();
        connectionListener = new PersistentConnectionListener(this, notificationService);
        notificationPacketListener = new NotificationPacketListener(this);
        BaoguApplication.xmppManager = this;
        handler = new Handler();
        taskList = new ArrayList<Runnable>();
        reconnection = new ReconnectionThread(this);
        reciveMessageList = new ArrayList<IReciveMessage>();
    }

    public List<IReciveMessage> getReciveMessageList() {
        return reciveMessageList;
    }

    public void setReciveMessageList(List<IReciveMessage> reciveMessageList) {
        this.reciveMessageList = reciveMessageList;
    }

    public Context getContext() {
        return context;
    }

    public void connect() {
        LogUtil.d(LOGTAG, "connect()...");
        disconnect = false;
        submitConnectTask();
        submitLogUtilinTask();
    }

    public void disconnect() {
        LogUtil.d(LOGTAG, "disconnect()...");
        disconnect = true;
        LogUtil.d(LOGTAG, "terminatePersistentConnection()... run()");
        getConnection().removePacketListener(
                getNotificationPacketListener());
        getConnection().removeConnectionListener(getConnectionListener());
        getConnection().disconnect();

//        terminatePersistentConnection();
    }

    public void terminatePersistentConnection() {
        LogUtil.d(LOGTAG, "terminatePersistentConnection()...");
        Runnable runnable = new Runnable() {

            final XmppManager xmppManager = XmppManager.this;

            public void run() {
                if (xmppManager.isConnected()) {
                    LogUtil.d(LOGTAG, "terminatePersistentConnection()... run()");
                    xmppManager.getConnection().removePacketListener(
                            xmppManager.getNotificationPacketListener());
                    xmppManager.getConnection().disconnect();
                }
                xmppManager.runTask();
            }

        };
        addTask(runnable);
    }

    public XMPPConnection getConnection() {
        return connection;
    }

    public void sendPacket(Packet packet, MessageSendListener listener) {
        addTask(new SendMessageTask(packet, listener));
        runTask();
    }

    public void setConnection(XMPPConnection connection) {
        this.connection = connection;
    }


    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }

    public PacketListener getNotificationPacketListener() {
        return notificationPacketListener;
    }

    public void startReconnectionThread() {
        // reconnection.start();
        if (!isConnected()) {
            synchronized (reconnection) {
                if (!reconnection.isAlive()) {
                    reconnection.setName("Xmpp Reconnection Thread");
                    reconnection.start();
                }
            }
        }
    }

    public Handler getHandler() {
        return handler;
    }


    public List<Runnable> getTaskList() {
        return taskList;
    }

    public Future<?> getFutureTask() {
        return futureTask;
    }

    public void runTask() {
        LogUtil.d(LOGTAG, "runTask()...");
        synchronized (taskList) {
            running = false;
            futureTask = null;
            if (!taskList.isEmpty()) {
                Runnable runnable = (Runnable) taskList.get(0);
                taskList.remove(0);
                running = true;
                futureTask = taskSubmitter.submit(runnable);
                if (futureTask == null) {
                    taskTracker.decrease();
                }
            }
        }
        taskTracker.decrease();
        LogUtil.d(LOGTAG, "runTask()...done");
    }

    private String newRandomUUID() {
        String uuidRaw = UUID.randomUUID().toString();
        return uuidRaw.replaceAll("-", "");
    }

    private boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    private boolean isAuthenticated() {
        return connection != null && connection.isConnected()
                && connection.isAuthenticated();
    }


    private void submitConnectTask() {
        LogUtil.d(LOGTAG, "submitConnectTask()...");
        addTask(new ConnectTask());
    }

    public  void submitTimeIQ(){
        addTask(new ServerTimeTask());
        runTask();
    }

    private void submitLogUtilinTask() {
        LogUtil.d(LOGTAG, "submitLogUtilinTask()...");
        addTask(new LogUtilinTask());
        runTask();
    }

    private void addTask(Runnable runnable) {
        LogUtil.d(LOGTAG, "addTask(runnable)...");
        taskTracker.increase();
        synchronized (taskList) {
            if (taskList.isEmpty() && !running) {
                running = true;
                futureTask = taskSubmitter.submit(runnable);
                if (futureTask == null) {
                    taskTracker.decrease();
                }
            } else {
                taskList.add(runnable);
            }
        }
        LogUtil.d(LOGTAG, "addTask(runnable)... done");
    }

    static {
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
            LogUtil.e(XmppManager.class, "reconnection");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A runnable task to connect the server.
     */
    private class ConnectTask implements Runnable {

        final XmppManager xmppManager;

        private ConnectTask() {
            this.xmppManager = XmppManager.this;
        }

        public void run() {
            LogUtil.i(LOGTAG, "ConnectTask.run()...");

            if (!xmppManager.isConnected()) {
                // Create the configuration for this new connection
                MyPref myPref= MyPref.getInstance(getContext()) ;
                ConnectionConfiguration connConfig = new ConnectionConfiguration(
                        myPref.getOpenfirSever(), myPref.getOpenfirPort());
                // connConfig.setSecurityMode(SecurityMode.disabled);
                connConfig.setReconnectionAllowed(true);
                connConfig.setKeystoreType("NONE");
                connConfig.setSendPresence(true);
                connConfig.setCompressionEnabled(false);
                connConfig.setSelfSignedCertificateEnabled(false);
                connConfig.setSASLAuthenticationEnabled(false);
                connConfig.setVerifyChainEnabled(false);
                connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
                XMPPConnection connection = new XMPPConnection(connConfig);
                xmppManager.setConnection(connection);

                try {
                    // Connect to the server
                    connection.connect();
                    LogUtil.i(LOGTAG, "XMPP connected successfully");
                    connection.addPacketListener(new MessagePacketListener(context, XmppManager.this), null);

                } catch (XMPPException e) {
                    LogUtil.e(LOGTAG, "XMPP connection failed");
                    e.printStackTrace();
                }

                xmppManager.runTask();

            } else {
                LogUtil.i(LOGTAG, "XMPP connected already");
                xmppManager.runTask();
            }
        }
    }


    /**
     * A runnable task to log into the server.
     */
    private class LogUtilinTask implements Runnable {

        final XmppManager xmppManager;

        private LogUtilinTask() {
            this.xmppManager = XmppManager.this;
        }

        public void run() {
            LogUtil.i(LOGTAG, "LogUtilinTask.run()...");

            if (!xmppManager.isAuthenticated()) {

                try {
                    MyPref myPref=MyPref.getInstance(getContext());
                    LogUtil.d(XmppManager.class, "user name:" + myPref.getUserId()+ " password:" +myPref.getOpenfirPass());
                    xmppManager.getConnection().login(
                            myPref.getUserId(),
                            myPref.getOpenfirPass(), XMPP_RESOURCE_NAME);
                    Presence presence = new Presence(Presence.Type.available);
// Send the packet (assume we have a Connection instance called "con").
                    getConnection().sendPacket(presence);
                    addTask(new ServerTimeTask());
                    // connection listener
                    if (xmppManager.getConnectionListener() != null) {
                        xmppManager.getConnection().addConnectionListener(
                                xmppManager.getConnectionListener());
                    }

                    xmppManager.runTask();

                } catch (XMPPException e) {
                    LogUtil.e(LOGTAG, "LogUtilinTask.run()... xmpp error");
                    LogUtil.e(LOGTAG, "Failed to login to . Caused by: "
                            + e.getMessage());
//                    String INVALID_CREDENTIALS_ERROR_CODE = "401";
//                    String errorMessage = e.getMessage();
//                    if (errorMessage != null
//                            && errorMessage
//                            .contains(INVALID_CREDENTIALS_ERROR_CODE)) {
//                        return;
//                    }
                    xmppManager.startReconnectionThread();

                } catch (Exception e) {
                    LogUtil.e(LOGTAG, "LogUtilinTask.run()... other error");
                    LogUtil.e(LOGTAG, "Failed to login to xmpp server. Caused by: "
                            + e.getMessage());
                    e.printStackTrace();
                    xmppManager.startReconnectionThread();
                }

            } else {
                LogUtil.i(LOGTAG, "LogUtilged in already");
                xmppManager.runTask();
            }

        }
    }

    /**
     * A runnable task to send message.
     */
    private class SendMessageTask implements Runnable {

        final XmppManager xmppManager;
        private Packet packet;
        private MessageSendListener listener;

        private SendMessageTask(Packet packet, MessageSendListener listener) {
            this.xmppManager = XmppManager.this;
            this.packet = packet;
            this.listener = listener;
        }

        public void run() {
            LogUtil.i(LOGTAG, "SendMessageTask.run()...");
            if (xmppManager.isAuthenticated()) {
                xmppManager.getConnection().sendPacket(packet);
                LogUtil.d(LOGTAG, "Send Packet in successfully");
                listener.sendSuccess(packet);
                PacketListener packetListener = xmppManager
                        .getNotificationPacketListener();
//				connection.addPacketListener(packetListener, packetFilter);
                xmppManager.runTask();
            } else {
                LogUtil.i(LOGTAG, "Not LogUtilged in");
                addTask(new LogUtilinTask());
                xmppManager.runTask();
            }

        }
    }

    /**
     * A runnable task to send message.
     */
    private class ServerTimeTask implements Runnable {

        private ServerTimeTask() {
        }

        public void run() {
            LogUtil.i(LOGTAG, "ServerTimeTask.run()...");
            Time timeRequest = new Time();
            timeRequest.setType(IQ.Type.GET);
            PacketCollector collector = connection
                    .createPacketCollector(new PacketIDFilter(timeRequest
                            .getPacketID()));
            connection.sendPacket(timeRequest);
            IQ result = (IQ) collector.nextResult(3000);
            if (result != null && result.getType() == IQ.Type.RESULT) {
                LogUtil.d(ServerTimeTask.class,result.toXML());
                Time timeResult = (Time) result;
                bettewServerTiem = timeResult.getTime().getTime()
                        - new Date().getTime();
                LogUtil.i(LOGTAG, "time" + bettewServerTiem);

            }
        }
    }
}
