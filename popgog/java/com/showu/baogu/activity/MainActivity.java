package com.showu.baogu.activity;

import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.film.FilmIndexActivity;
import com.showu.baogu.activity.filmFan.FriendActivity;
import com.showu.baogu.activity.message.MessageActivity;
import com.showu.baogu.activity.user.UserActivity;
import com.showu.baogu.application.BaoguApplication;
import com.showu.baogu.application.Const;
import com.showu.baogu.application.MyPref;
import com.showu.baogu.xmpp.IReciveMessage;
import com.showu.baogu.xmpp.message.BaseMessage;
import com.showu.baogu.xmpp.provider.ChatSession;
import com.showu.baogu.xmpp.provider.SessionManager;
import com.showu.common.http.HttpClient;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;

public class MainActivity extends TabActivity implements IReciveMessage, TabHost.OnTabChangeListener {
    public static TabHost mHost;
    public TabWidget mTabWidget;
    private HttpClient client;
    private SessionManager sessionManager;
    private final int MESSAGE_COME = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_COME:
                    initTabNumber();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = SessionManager.getInstance(this);
        BaoguApplication.activity=this;
        setContentView(R.layout.bottom_tabs);
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
        if (BaoguApplication.xmppManager != null) {
            BaoguApplication.xmppManager.getReciveMessageList().add(this);
        }
        setupIntent();
        initConst();

    }

    @Override
    protected void onResume() {
        super.onResume();
        sendMessage(MESSAGE_COME, null);
    }

    private void setupIntent() {

        mHost.addTab(createTab(R.string.tabs_tag_film,
                R.string.tabs_text_film,
                R.drawable.bottom_tabs_item_film,
                new Intent(this, FilmIndexActivity.class)));

        mHost.addTab(createTab(R.string.tabs_tag_message,
                R.string.tabs_text_message,
                R.drawable.bottom_tabs_item_message,
                new Intent(this, MessageActivity.class)));

        mHost.addTab(createTab(R.string.tabs_tag_friend,
                R.string.tabs_text_friend,
                R.drawable.bottom_tabs_item_friend,
                new Intent(this, FriendActivity.class)));

        mHost.addTab(createTab(R.string.tabs_tag_user,
                R.string.tabs_text_user,
                R.drawable.bottom_tabs_item_user,
                new Intent(this, UserActivity.class)));

        mHost.setOnTabChangedListener(this);
        if(Const.user == null) {
            mHost.getTabWidget().setVisibility(View.GONE);
            mHost.setCurrentTab(3);
        }
    }

    /**
     * @param tag     tab text tag
     * @param tabName tab text name
     * @param resId   tab item xml layout file
     * @return TabSpec
     */
    private TabHost.TabSpec createTab(
            Integer tag, Integer tabName, Integer resId, Intent content) {
        View view = getTabView(resId, tabName);
        return mHost.newTabSpec(getText(tag)).setIndicator(view).setContent(content);
    }

    private View getTabView(int resId, Integer tabName) {
        View view = View.inflate(this, R.layout.bottom_tabs_item, null);
        TextView tabText = (TextView) view.findViewById(R.id.bottom_tabs_item_text);
        tabText.setText(tabName);
        ImageView tabImg = (ImageView) view.findViewById(R.id.bottom_tabs_item_img);
        tabImg.setImageResource(resId);
        return view;
    }

    private String getText(Integer text) {
        return getResources().getString(text);
    }

    private void initConst() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        MyPref myPref = MyPref.getInstance(this);
        myPref.setDisplayWidth(dm.widthPixels);
        myPref.setDisplayHeight(dm.heightPixels);
    }


    @Override
    public boolean reciveMessage(BaseMessage msg) {
        sendMessage(MESSAGE_COME, msg);
        return false;
    }

    public  void deleteMessage(){
        sendMessage(MESSAGE_COME, null);
    }

    private void initTabNumber() {
        View view = mTabWidget.getChildAt(1);
        TextView textView = (TextView) view.findViewById(R.id.messageNumber);
        TextView dynamicTextView = (TextView) mTabWidget.getChildAt(2).findViewById(R.id.messageNumber);
        dynamicTextView.setBackgroundResource(R.drawable.dynamic_icon);
        if (Const.user != null) {
            int num = sessionManager.getMainNotRead();
            LogUtil.d(MainActivity.class, "number=" + num);
            textView.setText("" + num);
            if (num > 0) {
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
            }
            int dynamicNum = sessionManager.getDynamicNotRead();
            if (dynamicNum > 0) {
                dynamicTextView.setVisibility(View.VISIBLE);
            } else {
                dynamicTextView.setVisibility(View.GONE);
            }
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private void sendMessage(int what, Object o) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = o;
        handler.sendMessage(msg);
    }

    @Override
    public void onTabChanged(String tabId) {
       if(Const.user == null) {
           GUIUtil.toast(this, "请您登录！");
           mHost.setCurrentTab(3);
       }
    }


}
