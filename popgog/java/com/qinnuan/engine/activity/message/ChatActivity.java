package com.qinnuan.engine.activity.message;

import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.google.gson.Gson;
import com.qinnuan.engine.api.GetOtheruserinfo;
import com.qinnuan.engine.bean.OtherUserInfo;
import com.qinnuan.engine.fragment.message.ChatFragment;
import com.qinnuan.engine.fragment.message.IChatOption;
import com.qinnuan.engine.xmpp.IReciveMessage;
import com.qinnuan.engine.xmpp.json.LocationJson;
import com.qinnuan.engine.xmpp.json.TextMessageJson;
import com.qinnuan.engine.xmpp.json.UserInfo;
import com.qinnuan.engine.xmpp.message.MessageUtil;
import com.qinnuan.engine.xmpp.message.SessionType;
import com.qinnuan.engine.xmpp.provider.SessionManager;
import com.qinnuan.common.util.FileUtil;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.activity.film.EtickitActivity;
import com.qinnuan.engine.activity.film.GroupActivity;
import com.qinnuan.engine.activity.filmFan.MyFriendActivity;
import com.qinnuan.engine.activity.filmFan.PublicInfoActivity;
import com.qinnuan.engine.application.BaoguApplication;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.bean.Point;
import com.qinnuan.engine.fragment.message.ChatUpload;
import com.qinnuan.engine.fragment.message.ChatUploadListener;
import com.qinnuan.engine.xmpp.MessageSendListener;
import com.qinnuan.engine.xmpp.json.ImageMessageJson;
import com.qinnuan.engine.xmpp.json.VoiceJson;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.message.ComparatorChat;
import com.qinnuan.engine.xmpp.message.MessageSRC;
import com.qinnuan.engine.xmpp.message.MessageType;
import com.qinnuan.engine.xmpp.provider.ChatDBManager;
import com.qinnuan.common.util.ImageUtil;
import com.qinnuan.common.util.Location;

import org.jivesoftware.smack.packet.Packet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class ChatActivity extends BaseActivity implements MessageSendListener, IReciveMessage, IChatOption, ChatUploadListener, Location.LocationCallBack {
    public static String ACTIVTY_EX_ID = "targetId";
    //    public static String ACTIVTY_EX_DISTANCE = "distance";
    public static String ACTIVTY_EX_NIKENAME = "nikename";
    public static String ACTIVTY_EX_PROFILE = "profile";
    public static String ACTIVTY_EX_ROOM = "roomid";
    public static String ACTIVITY_HELP_ID = "help";
    public static String ACTIVTY_EX_TYPE = "sessiontype";
    public static String ACTIVITY_NEED_BACK = "back";

    private ChatFragment chatFragment;
    private Gson gson;
    private final int RECIVE_MESSAGE = 1;
    private final int GET_LOCAL_MESSAGE = 2;
    private final int START_CAMARA = 3;
    private final int CHOICE_IMAGE = 4;
    private final int START_GROUP = 5;
    private final int GET_LOCATION_SUCCESS = 6;
    private final int SEND_SUCCESS = 7;
    private final int START_USER_INFO = 8;
    private List<BaseMessage> messageList = new ArrayList<BaseMessage>();
    private Uri selectedImageUri = null;
    private String toUserid;
    private String nikeName;
    private int sessionType;
    private String roomId = "0";
    private boolean helpId = false;
    private String profileimg;
    private ComparatorChat chatCom;
    public boolean isFriend = true;
    private GetOtheruserinfo getOtheruserinfo = new GetOtheruserinfo();
    private OtherUserInfo otherUserInfo;
    private Map<Integer, SessionType> sessionTypeMap = new HashMap<Integer, SessionType>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECIVE_MESSAGE:
                    BaseMessage message = (BaseMessage) msg.obj;
                    if (message.getMessageType() == MessageType.DELETEFRIEND) {
                        isFriend = false;
                    } else {
                        messageList.add(message);
                        notifyData(message.getUserinfo());
                    }
//                    getLocalMessage();//去数据库刷新
                    break;
                case GET_LOCAL_MESSAGE:
                    notifyData(null);
                    chatFragment.selectLast();
                    break;
                case GET_LOCATION_SUCCESS:
                    sendLocation((Point) msg.obj);
                    break;
                case SEND_SUCCESS:
                    chatFragment.moveToLast();
                    break;
            }
        }
    };

    private void getOtheruserinfo() {
        getOtheruserinfo.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        getOtheruserinfo.setOtheruserid(toUserid);
        getOtheruserinfo.setUserid(Const.user.getUserid());
        getOtheruserinfo.setDevicetype("1");
        getOtheruserinfo.setLatitude(Const.point.getGeoLat().toString());
        getOtheruserinfo.setLongitude(Const.point.getGeoLng().toString());
        request(getOtheruserinfo);
    }

    private void registSessionMap() {
        for (SessionType type : SessionType.values()) {
            sessionTypeMap.put(Integer.parseInt(type.toString()), type);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        chatFragment.moveToLast();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registSessionMap();
        toUserid = getIntent().getStringExtra(ACTIVTY_EX_ID);
        nikeName = getIntent().getStringExtra(ACTIVTY_EX_NIKENAME);
        helpId = getIntent().getBooleanExtra(ACTIVITY_HELP_ID, false);
        profileimg = getIntent().getStringExtra(ACTIVTY_EX_PROFILE);
        roomId = getIntent().getStringExtra(ACTIVTY_EX_ROOM);
        if (roomId == null) {
            roomId = "0";
        }
        sessionType = getIntent().getIntExtra(ACTIVTY_EX_TYPE, 1);
        gson = new Gson();
        if (!roomId.equals("0")) {
            chatFragment = new ChatFragment(nikeName, messageList, this, mImageFetcher, 2);//群聊
        } else {
            chatFragment = new ChatFragment(nikeName, messageList, this, mImageFetcher, sessionType);
        }
        replaceFragment(chatFragment, false);
        BaoguApplication.xmppManager.getReciveMessageList().add(this);
        chatCom = new ComparatorChat();
        if (roomId == null) roomId = "0";
        if (!helpId || roomId.equals("0")) {
            getOtheruserinfo();
        }
        new Thread() {
            @Override
            public void run() {
                getLocalMessage();
            }
        }.start();
        if (helpId) {
            SessionManager.getInstance(this).readedHelp();
        } else {
            SessionManager.getInstance(this).markRead(toUserid);
        }
    }

    private void notifyData(UserInfo userInfo) {
        BaseMessage preMessage = null;
        for (int i = 0; i < messageList.size(); i++) {
            BaseMessage message = messageList.get(i);
            if (message.getMessage() == null) continue;
            if (userInfo != null) {
                UserInfo u = message.getUserinfo();
                if (u.getUserid().equals(userInfo.getUserid())) {
                    u.setHeadImage(userInfo.getHeadImage());
                }
            }
            if (i == 0) {
                message.setNeedShowTime(true);
                preMessage = message;
            } else {
                if (preMessage != null) {
                    if (message.getSendTime() - preMessage.getSendTime() > 120) {
                        message.setNeedShowTime(true);
                    }
                }
                preMessage = message;
            }
        }
        chatFragment.notifyDataSetChanged();
    }

    public void startGroupManager() {
        Intent intent = new Intent(this, GroupActivity.class);
        intent.putExtra(GroupActivity.EXT_GROUP_ID, roomId);
        startActivityForResult(intent, START_GROUP);
    }

    public void startUserInfo() {
        if(isFriend){
        if (sessionType == 3) {//公众账号
            Intent intent = new Intent(this, PublicInfoActivity.class);
            intent.putExtra(PublicInfoActivity.PUBLIC_FAN_ID, toUserid);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MyFriendActivity.class);
            intent.putExtra(MyFriendActivity.FAN_ID, toUserid);
            startActivityForResult(intent, START_USER_INFO);
        }
        }else{
            GUIUtil.toast(this,"你们已经不是好友！");
        }
    }

    @Override
    public void sendSuccess(Packet message) {
//        chatFragment.moveToLast();
        LogUtil.e(getClass(), message.toXML());
        sendHandMessage(SEND_SUCCESS, null);
        ChatDBManager.getInstance(this).updateMessage((BaseMessage) message);
    }


    public void saveSession(BaseMessage message) {
        MessageListBean messageListBean = new MessageListBean();
        if (message.getMessageType() == MessageType.LOCATION) {
            messageListBean.setContent("[地图]");
        } else if (message.getMessageType() == MessageType.IMAGE) {
            messageListBean.setContent("[图片]");
        } else if (message.getMessageType() == MessageType.VOICE) {
            messageListBean.setContent("[声音]");
        } else if (message.getMessageType() == MessageType.TEXT) {
            TextMessageJson json = (TextMessageJson) message.getMessage();
            messageListBean.setContent(json.getText());
        }
        messageListBean.setHeadImage(profileimg);
        messageListBean.setName(nikeName);
        messageListBean.setRoom(roomId);
        messageListBean.setTime(message.getSendTime());
        messageListBean.setType(sessionTypeMap.get(sessionType));
        messageListBean.setCount(0);
        messageListBean.setTargetId(message.getTargetId());
        if (roomId.equals("0")) {
            if (helpId) {
                messageListBean.setType(SessionType.HELP);
                LogUtil.e(getClass(),"saveHelpSession="+sessionType);
                SessionManager.getInstance(this).saveOrUpdateHelpSession(messageListBean, MessageSRC.TO);
            } else {
                SessionManager.getInstance(this).saveOrUpdateSession(messageListBean, MessageSRC.TO);
            }
        } else {
            SessionManager.getInstance(this).saveOrUpdateRoomSession(messageListBean, MessageSRC.TO);
        }
    }

    @Override
    public void sendFail(BaseMessage message, FailType type) {
        LogUtil.e(ChatActivity.class, message.toXML());
    }


    @Override
    public void sendTextMessage(String text) {
        TextMessageJson textJson = new TextMessageJson(text);
        textJson.setBaogu_send_time(MessageUtil.getInstance(this).getTime());
        BaseMessage message = MessageUtil.getInstance(this).createSendMessage(toUserid, MessageType.TEXT, textJson, roomId);
        message.setSessionType(SessionType.MESSAGE);
        BaoguApplication.xmppManager.sendPacket(message, this);
        messageList.add(message);
        notifyData(message.getUserinfo());
        chatFragment.moveToLast();
        if (helpId) {
            message.setHelpType(1);
        }
        ChatDBManager.getInstance(this).saveMessage(message);
        saveSession((BaseMessage) message);
    }

    @Override
    public void sendVoiceMessage(String path, int length) {
        if (!isFriend) {
            GUIUtil.toast(this, "对方不是你的好友，你不能发送消息！");
            return;
        }
        VoiceJson voiceJson = new VoiceJson();
        voiceJson.setBaogu_send_time(MessageUtil.getInstance(this).getTime());
        voiceJson.setBaogu_audio_length(length);
        BaseMessage message = MessageUtil.getInstance(this).createSendMessage(toUserid, MessageType.VOICE, voiceJson, roomId);
        message.setLocalPath(path);
        new ChatUpload(message, this).uploadFile("4", new File(path));
        messageList.add(message);
        notifyData(message.getUserinfo());
        chatFragment.moveToLast();
        if (helpId) {
            message.setHelpType(1);
        }
        ChatDBManager.getInstance(this).saveMessage(message);
        saveSession((BaseMessage) message);
    }

    @Override
    public boolean reciveMessage(BaseMessage msg) {
        LogUtil.e(getClass(), "reciveMessage" + msg.toXML());
        if(msg.getMessageType()==MessageType.HELPCINEMA||msg.getMessageType()==MessageType.HELPTEXT||msg.getMessageType()==MessageType.HELPACCOUNT){
            if(helpId){
                sendHandMessage(RECIVE_MESSAGE, msg);
                SessionManager.getInstance(this).readedHelp();
            }
        }
        if (msg.getTargetId().equalsIgnoreCase(toUserid) || (msg.getRoom().equals(roomId) && !roomId.equals("0"))) {
            SessionManager.getInstance(this).markRead(toUserid);
            sendHandMessage(RECIVE_MESSAGE, msg);
            if (helpId) {
                SessionManager.getInstance(this).readedHelp();
            } else {
                SessionManager.getInstance(this).markRead(toUserid);
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        BaoguApplication.xmppManager.getReciveMessageList().remove(this);
        super.onDestroy();
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        try {
            if (url.contains(getOtheruserinfo.getApi())) {
                parseOtherUserInfo(conent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseOtherUserInfo(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        if (jsonObject.getInt("status") == 1) {
            otherUserInfo = JSONTool.jsonToBean(OtherUserInfo.class, jsonObject.getJSONObject("data").getJSONObject("users"));
            nikeName = otherUserInfo.getNickname();
//            distance=otherUserInfo.getDistance();
            profileimg = otherUserInfo.getProfileimg();
            chatFragment.setTitle(otherUserInfo.getNickname());
        }
    }

    private void sendHandMessage(int wath, Object o) {
        Message message = new Message();
        message.what = wath;
        message.obj = o;
        handler.sendMessage(message);
    }

    private void getLocalMessage() {
        messageList.clear();
        if (roomId.equals("0")) {
            if (helpId) {
                messageList.addAll(ChatDBManager.getInstance(this).getHelpMessage());
            } else {
                messageList.addAll(ChatDBManager.getInstance(this).getMessage(toUserid));
            }
        } else {
            messageList.addAll(ChatDBManager.getInstance(this).getRoomMessage(roomId));
        }
        for (int i = 0; i < messageList.size(); i++) {
            BaseMessage msg = messageList.get(i);
            if (msg.getMessage() == null) {
                messageList.remove(msg);
            }
        }
        Collections.sort(messageList, chatCom);
        sendHandMessage(GET_LOCAL_MESSAGE, null);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == START_USER_INFO) {
                int needBack = data.getIntExtra(ACTIVITY_NEED_BACK, 0);
                if (needBack == 1) {
                    finish();
                }
            } else if (requestCode == START_GROUP) {
                if (data != null) {
                    String title = data.getStringExtra(GroupActivity.EXT_GROUP_NAME);
                    LogUtil.v(getClass(), "title" + title);
                    chatFragment.setTitle(title);
                    boolean deleted = data.getBooleanExtra(GroupActivity.EXT_GROUP_DELETE, false);
                    if (deleted) {
                        finish();
                    }
                }
            } else if (requestCode == START_CAMARA) {
                int degree = 0;
                try {
                    ExifInterface exifInterface = new ExifInterface(selectedImageUri.getPath());
                    int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            degree = 90;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            degree = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            degree = 270;
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendImageMessage(selectedImageUri.getPath(), degree);
            } else if (requestCode == CHOICE_IMAGE) {
                Uri originalUri = data.getData(); // 获得图片的ur
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                // 按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                // 最后根据索引值获取图片路径www.2cto.com
                String path = cursor.getString(column_index);
//                cursor.close();
                int angle = 0;
                sendImageMessage(path, angle);
            }
        }
    }

    public void buyTicket() {
        Intent intent = new Intent(this, EtickitActivity.class);
        intent.putExtra(EtickitActivity.EXT_MECHENT_ID, toUserid);
        startActivity(intent);
    }

    public void sendImageMessage(String path, int angle) {
        ImageMessageJson imageJson = new ImageMessageJson();
        imageJson.setBaogu_send_time(MessageUtil.getInstance(this).getTime());
        BaseMessage message = MessageUtil.getInstance(this).createSendMessage(toUserid, MessageType.IMAGE, imageJson, roomId);
        message.setStatus(2);
        message.setLocalPath(ImageUtil.getSmallBitmap(this, path, angle));
        message.setMessageBody(imageJson);
        new ChatUpload(message, this).uploadFile("3", new File(message.getLocalPath()));
        messageList.add(message);
        if (helpId) {
            message.setHelpType(1);
        }
        ChatDBManager.getInstance(this).saveMessage(message);
        saveSession((BaseMessage) message);
        notifyData(message.getUserinfo());
        chatFragment.moveToLast();
    }

    public void sendActivityMessage() {
        TextMessageJson textJson = new TextMessageJson("333");
        textJson.setBaogu_send_time(MessageUtil.getInstance(this).getTime());
        BaseMessage message = MessageUtil.getInstance(this).createSendMessage(toUserid, MessageType.TEXT, textJson, roomId);
        message.setMessageType(MessageType.LOOKACTIVITY);
        message.setSessionType(SessionType.MESSAGE);
        BaoguApplication.xmppManager.sendPacket(message, this);
    }

    @Override
    public void startCamara() {
        String FILE_NAME = UUID.randomUUID() + ".jpg";
        File photo = FileUtil.getNewFile(this, FILE_NAME);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (photo != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
            selectedImageUri = Uri.fromFile(photo);
            startActivityForResult(intent, START_CAMARA);
        }
    }

    @Override
    public void startImageSelect() {
        Intent intent = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CHOICE_IMAGE);
    }

    @Override
    public void startLocation() {
        new Location(this, this).getLocation();
    }


    @Override
    public void startUpload(BaseMessage message) {

    }

    @Override
    public void endUpload(BaseMessage message, String url, boolean isUpLoadSuccess) {
        if (message.getMessageType() == MessageType.IMAGE) {
            ImageMessageJson image = (ImageMessageJson) message.getMessage();
            image.setBaogu_imageurl(url);
            message.setMessageBody(image);
        } else if (message.getMessageType() == MessageType.VOICE) {
            VoiceJson voiceJson = (VoiceJson) message.getMessage();
            voiceJson.setBaogu_voiceurl(url);
            message.setMessageBody(voiceJson);
        }
        if (isUpLoadSuccess) {
            message.setStatus(0);
            BaoguApplication.xmppManager.sendPacket(message, this);
        } else {
            message.setStatus(1);
        }
        ChatDBManager.getInstance(this).updateMessage(message);
        chatFragment.notifyDataSetChanged();
    }

    private void sendLocation(Point point) {
        if (!isFriend) {
            GUIUtil.toast(this, "对方不是你的好友，你不能发送消息！");
            return;
        }
        LocationJson locationJson = new LocationJson();
        locationJson.setBaogu_send_time(MessageUtil.getInstance(this).getTime());
        locationJson.setBaogu_latitude(point.getGeoLat().toString());
        locationJson.setBaogu_longitude(point.getGeoLng().toString());
        BaseMessage message = MessageUtil.getInstance(this).createSendMessage(toUserid, MessageType.LOCATION, locationJson, roomId);
        BaoguApplication.xmppManager.sendPacket(message, this);
        messageList.add(message);
        notifyData(message.getUserinfo());
        chatFragment.moveToLast();
        if (helpId) {
            message.setHelpType(1);
        }
        ChatDBManager.getInstance(this).saveMessage(message);
        saveSession((BaseMessage) message);
    }

    @Override
    public void locationSuccess(Point point) {
        sendHandMessage(GET_LOCATION_SUCCESS, point);
    }

    @Override
    public void locaitonFailed() {

    }
}