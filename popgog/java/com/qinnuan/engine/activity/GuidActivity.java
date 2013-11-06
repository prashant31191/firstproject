package com.qinnuan.engine.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.qinnuan.common.http.image.util.ImageCache;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.application.BaoguApplication;
import com.qinnuan.engine.xmpp.MessageSendListener;
import com.qinnuan.engine.xmpp.ServiceManager;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.common.http.image.util.ImageFetcher;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverItems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class GuidActivity extends BaseActivity implements MessageSendListener, View.OnClickListener {

//    private Button button;
    private ImageView imageView;
    private static final String IMAGE_CACHE_DIR = "thumbs";
    private ImageFetcher mImageFetcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_test);
//        button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.imageView);

        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(this, 20);
       // mImageFetcher.setLoadingImage(R.drawable.icon);
        mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
        mImageFetcher.loadImage("http://ww1.sinaimg.cn/large/6e5af39ftw1e66z74zvrwj2057057mxn.jpg", imageView);
//        mImageFetcher.setPauseWork(false);
//        loadImage();
        ServiceManager serviceManager=new ServiceManager(this) ;
        serviceManager.startService();

//        ServiceManager serviceManager=new ServiceManager(this) ;
//        serviceManager.startService();
    }

    public  void loadImage(){
        mImageFetcher.loadImage("http://ww1.sinaimg.cn/large/6e5af39ftw1e66z74zvrwj2057057mxn.jpg", imageView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.guid, menu);
        return true;
    }

    @Override
    public void sendSuccess(Packet message) {
        LogUtil.e(getClass(), message.toXML());
    }

    @Override
    public void sendFail(BaseMessage message, FailType type) {
        Toast.makeText(this, "sendFail", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        sendMessage();
//        joinMoreRoom();
//       sendMessageToRoom();
//        createRoom();
        try {
            getHostRooms();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(){

        ChatManager chatmanager = BaoguApplication.xmppManager.getConnection().getChatManager();
        Chat newChat = chatmanager.createChat("kankan@10.0.0.24", new MessageListener() {
            public void processMessage(Chat chat, Message message) {
                LogUtil.e(GuidActivity.class,message.toXML());
            }
        });

        try {
            Message msg = new Message() ;
            msg.setBody("hello");
            msg.addExtension(extension);
            newChat.sendMessage(msg);
        }
        catch (XMPPException e) {
            System.out.println("Error Delivering block");
        }

//        BaseMessage baseMessage = new BaseMessage();
//        baseMessage.setBody("hello");
//        baseMessage.setFrom("engine@10.0.0.24");
//        baseMessage.setTo("navy@10.0.0.24");
//        baseMessage.setType(Message.Type.chat);
//
//        baseMessage.addExtension(extension);
//        BaoguApplication.xmppManager.sendPacket(baseMessage, this);
    }
    PacketExtension extension=new PacketExtension() {
        @Override
        public String getElementName() {
            return "bType";
        }

        @Override
        public String getNamespace() {
            return null;
        }

        @Override
        public String toXML() {
            return "<bType>444</bType>";
        }
    };

    public  void createRoom(){

        MultiUserChat muc = new MultiUserChat(BaoguApplication.xmppManager.getConnection(), "engine1@conference.10.0.0.24");

        // Create the room
        try {
            muc.create("user2");
            //获取聊天室的配置表单
            Form form = muc.getConfigurationForm();
            //根据原始表单创建一个要提交的新表单
            Form submitForm = form.createAnswerForm();
            //向提交的表单添加默认答复
            for(Iterator<FormField> fields = form.getFields(); fields.hasNext();) {
                FormField field = (FormField) fields.next();
                if(!FormField.TYPE_HIDDEN.equals(field.getType()) && field.getVariable() != null) {
                    submitForm.setDefaultAnswer(field.getVariable());
                }
            }
            //重新设置聊天室名称
            submitForm.setAnswer("muc#roomconfig_roomname", "Room");
            //设置聊天室的新拥有者
            List<String> owners = new ArrayList<String>();
            owners.add("engine@10.0.0.24");
           // owners.add("navy@10.0.0.24");
            submitForm.setAnswer("muc#roomconfig_roomowners", owners);
            //设置密码
            //submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
            //submitForm.setAnswer("muc#roomconfig_roomsecret", "reserved");
            //设置描述
            submitForm.setAnswer("muc#roomconfig_roomdesc", "新创建的reserved聊天室");
            //设置聊天室是持久聊天室，即将要被保存下来
            submitForm.setAnswer("muc#roomconfig_persistentroom", true);
            //发送已完成的表单到服务器配置聊天室
            muc.sendConfigurationForm(submitForm);
            muc = new MultiUserChat(BaoguApplication.xmppManager.getConnection(), "engine1@conference.10.0.0.24");
            muc.join("喝醉的毛毛虫");

        } catch (XMPPException e) {
            e.printStackTrace();
        }
//        BaseMessage msg = new BaseMessage();
//        msg.setBody("bbbb");
//        muc.sendMessage(msg);
      //  muc.join("navy");
        // Send an empty room configuration form which indicates that we want
        // an instant room
//        muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
    }

    public void getHostRooms() throws Exception {
        XMPPConnection connection=BaoguApplication.xmppManager.getConnection();
        List<String> col = getConferenceServices(BaoguApplication.xmppManager.getConnection().getServiceName(), (BaoguApplication.xmppManager.getConnection()));
        for (Object aCol : col) {
            String service = (String) aCol;
            //查询服务器上的聊天室
            Collection<HostedRoom> rooms = MultiUserChat.getHostedRooms(connection, service);
            for(HostedRoom room : rooms) {
                //查看Room消息
                System.out.println(room.getName() + " - " +room.getJid());
                LogUtil.v(getClass(),room.getJid());
                RoomInfo roomInfo = MultiUserChat.getRoomInfo(connection, room.getJid());
                if(roomInfo != null) {
                    LogUtil.v(getClass(),roomInfo.getSubject());
                }
            }
        }}

    public static  List<String> getConferenceServices(String server, XMPPConnection connection) throws Exception {
        List<String> answer = new ArrayList<String>();
        ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);
        DiscoverItems items = discoManager.discoverItems(server);
        for (Iterator<DiscoverItems.Item> it = items.getItems(); it.hasNext();) {
            DiscoverItems.Item item = (DiscoverItems.Item)it.next();
            if (item.getEntityID().startsWith("conference") || item.getEntityID().startsWith("private")) {
                answer.add(item.getEntityID());
            }
            else {
                try {
                    DiscoverInfo info = discoManager.discoverInfo(item.getEntityID());
                    if (info.containsFeature("http://jabber.org/protocol/muc")) {
                        answer.add(item.getEntityID());
                    }
                }
                catch (XMPPException e) {
                }
            }
        }
        return answer;
    }
    public  void joinMoreRoom(){
        MultiUserChat muc = new MultiUserChat(BaoguApplication.xmppManager.getConnection(), "iphoneroom@conference.10.0.0.24");
        MultiUserChat muc1 = new MultiUserChat(BaoguApplication.xmppManager.getConnection(), "engine1@conference.10.0.0.24");
        try {
            muc1.join("haha");
            muc.join("haha");
        } catch (XMPPException e) {
            e.printStackTrace();
        }

    }

    public  void sendMessageToRoom(){
        MultiUserChat muc = new MultiUserChat(BaoguApplication.xmppManager.getConnection(), "iphoneroom@conference.10.0.0.24");

        // Create the room
        // muc.create("testbot");
        try {
            muc.join("xxxxx");
            Message message= muc.createMessage();
            message.addExtension(extension);
            message.setBody("dsfiaosdf");
//            muc.sendMessage("888888");
            muc.sendMessage(message);
            muc.addMessageListener(new PacketListener() {
                @Override
                public void processPacket(Packet packet) {
                    LogUtil.v(GuidActivity.class, packet.toXML());
                    if (packet instanceof BaseMessage) {
                        LogUtil.v(GuidActivity.class, "--------------baseMessage--------------------");
                    }
                }
            });
        } catch (XMPPException e) {
            e.printStackTrace();
        }

    }
}
