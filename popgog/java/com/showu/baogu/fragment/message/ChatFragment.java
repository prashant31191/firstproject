package com.showu.baogu.fragment.message;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Handler;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.film.EtickitActivity;
import com.showu.baogu.activity.film.GroupActivity;
import com.showu.baogu.activity.message.ChatActivity;
import com.showu.baogu.adapter.ChatAdapter;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.baogu.xmpp.json.TextMessageJson;
import com.showu.baogu.xmpp.json.VoiceJson;
import com.showu.baogu.xmpp.message.BaseMessage;
import com.showu.baogu.xmpp.message.MessageType;
import com.showu.baogu.xmpp.provider.ChatDBManager;
import com.showu.baogu.xmpp.provider.SessionManager;
import com.showu.common.http.FileDownload;
import com.showu.common.http.IDownLoadListener;
import com.showu.common.http.image.util.ImageFetcher;
import com.showu.common.util.FileUtil;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;
import com.showu.common.util.RecoderVoice;
import com.showu.common.util.TextUtil;
import com.showu.common.widget.FaceGridView;
import com.showu.common.widget.TitleWidget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by showu on 13-7-15.
 */
@FragmentView(id = R.layout.fragment_chat_content)
public class ChatFragment extends BaseFragment implements TextWatcher, AdapterView.OnItemLongClickListener, View.OnTouchListener, AdapterView.OnItemClickListener, View.OnClickListener, RecoderVoice.RecorderListener {
    @InjectView(id = R.id.listView)
    private ListView listView;
    @InjectView(id = R.id.fgv_face)
    private FaceGridView faceGridView;
    @InjectView(id = R.id.editMessage)
    private EditText editTextMessage;
    @InjectView(id = R.id.face)
    private ImageView btnFace;
    @InjectView(id = R.id.chat_voice)
    private ImageView btnVoice;
    @InjectView(id = R.id.chat_more)
    private ImageView btnMore;
    @InjectView(id = R.id.moreLayout)
    private LinearLayout moreLayout;
    @InjectView(id = R.id.chat_camara)
    private Button btnCamara;
    @InjectView(id = R.id.chat_photo)
    private Button btnPhoto;
    @InjectView(id = R.id.chat_location)
    private Button btnLocation;
    @InjectView(id = R.id.voiceLayout)
    private LinearLayout voiceLayout;
    @InjectView(id = R.id.cancel)
    private Button btnCancel;
    @InjectView(id = R.id.showRecoder)
    private ImageView showRecoderImage;
    @InjectView(id = R.id.recoder_second)
    private TextView messageLengthText;
    @InjectView(id = R.id.chat_send)
    private ImageView btnSend;
    @InjectView(id = R.id.view_edit)
    private ImageView helpShowEditBtn;
    @InjectView(id = R.id.view_activity)
    private Button activityBtn;
    @InjectView(id = R.id.view_buy_tickit)
    private Button buyTicketBtn;

    @InjectView(id = R.id.recoderLayout)
    private LinearLayout reCoderLayout;
    @InjectView(id = R.id.help_layout)
    private LinearLayout helpLayout;
    @InjectView(id = R.id.editLayout)
    private LinearLayout editLayout;
    @InjectView(id = R.id.chat_account_layout)
    private ImageView changeHelp;


    private ChatAdapter chatAdapter;
    private List<BaseMessage> messageList;
    private IChatOption chatOption;
    private ImageFetcher fetcher;
    private long startTime = 0;
    private RecoderVoice recoderVoice;
    private Handler handler = new Handler();
    private String title;
    private int type;

    private boolean recoding = false;

    public ChatFragment() {
    }

    public ChatFragment(String title, List<BaseMessage> list, IChatOption option, ImageFetcher fetcher, int type) {
        this.messageList = list;
        this.chatOption = option;
        this.fetcher = fetcher;
        this.title = title;
        this.type = type;
        recoderVoice = new RecoderVoice(this);
    }

    @Override
    public void bindDataForUIElement() {
        tWidget.setCenterViewText(TextUtil.getProcessText(title, getActivity()));
        tWidget.setRightVisibility("invisible");
        if (type == 3) {
            helpLayout.setVisibility(View.VISIBLE);
            editLayout.setVisibility(View.INVISIBLE);
//            btnVoice.setBackgroundResource(R.drawable.account_input);
            changeHelp.setVisibility(View.VISIBLE);
            btnMore.setVisibility(View.GONE);
            tWidget.setRightViewBg(R.drawable.chat_icon);
            tWidget.setRightVisibility("visible");
        } else if (type == 2) {
            tWidget.setRightViewBg(R.drawable.group_icon);
            tWidget.setRightVisibility("visible");
        } else {
            helpLayout.setVisibility(View.INVISIBLE);
            editLayout.setVisibility(View.VISIBLE);
            tWidget.setRightViewBg(R.drawable.chat_icon);
            tWidget.setRightVisibility("visible");
        }
    }

    @Override
    public void rightClick() {
        if (type == 2) {
            ((ChatActivity) getActivity()).startGroupManager();
        } else {
            ((ChatActivity) getActivity()).startUserInfo();
        }
    }


    public void moveToLast() {
        listView.setSelection(chatAdapter.getCount() - 1);
    }


    @Override
    protected void bindEvent() {
        chatAdapter = new ChatAdapter(getActivity(), messageList, fetcher);
        listView.setOnItemClickListener(this);
        listView.setAdapter(chatAdapter);
        faceGridView.bindEditText(editTextMessage);
        btnFace.setOnClickListener(this);
        editTextMessage.setOnClickListener(this);
        editTextMessage.addTextChangedListener(this);
        btnMore.setOnClickListener(this);
        btnCamara.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
        btnLocation.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnVoice.setOnClickListener(this);
        voiceLayout.setOnTouchListener(this);
        listView.setOnItemLongClickListener(this);
        btnSend.setOnClickListener(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    fetcher.setPauseWork(true);
                } else {
                    fetcher.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            }
        });
        helpShowEditBtn.setOnClickListener(this);
        activityBtn.setOnClickListener(this);
        buyTicketBtn.setOnClickListener(this);
        changeHelp.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        BaseMessage message = messageList.get(i);
        if (message.getMessageType() == MessageType.VOICE) {
            if (!TextUtil.isEmpty(message.getLocalPath())) {
                File file = new File(message.getLocalPath());
                recoderVoice.playMusic(file, view, message);
            } else {
                playRemoteVoice(message, view);
            }
        } else {
            message.action(getActivity());
        }
    }

    public void setTitle(String title) {
        this.title = title;
        bindDataForUIElement();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recoderVoice.stopVoice();
    }

    private void playRemoteVoice(BaseMessage message, View view) {
        VoiceJson json = (VoiceJson) message.getMessage();
        if (TextUtil.isEmpty(json.getBaogu_voiceurl())) {
            GUIUtil.toast(getActivity(), "无音频路径！");
        }
        File file = FileUtil.getRAM(getActivity(), json.getBaogu_voiceurl());
        if (file != null) {
            recoderVoice.playMusic(file, view, message);
        } else {
            file = FileUtil.creatRAM(getActivity(), json.getBaogu_voiceurl());
            DownloadListener listener = new DownloadListener(message, view);
            FileDownload fileDownload = new FileDownload(listener, file);
            fileDownload.execute(json.getBaogu_voiceurl());
        }
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }


    public BaseMessage getItem(int position) {
        return chatAdapter.getItem(position);
    }

    public void notifyDataSetChanged() {
        chatAdapter.notifyDataSetChanged();
        listView.setSelection(messageList.size() - 1);
    }

    public void removeMessage(BaseMessage message) {
        messageList.remove(message);
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_voice:
                voiceLayout.setVisibility(View.VISIBLE);
                editLayout.setVisibility(View.GONE);
                faceGridView.setVisibility(View.GONE);
                break;
            case R.id.chat_more:
                if (moreLayout.getVisibility() == View.GONE) {
                    moreLayout.setVisibility(View.VISIBLE);
                } else {
                    moreLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.face:
                if (faceGridView.getVisibility() == View.GONE) {
                    hidSoftInput(editTextMessage);
                    faceGridView.setVisibility(View.VISIBLE);
                } else {
                    faceGridView.setVisibility(View.GONE);
                }
                break;
            case R.id.editMessage:
                faceGridView.setVisibility(View.GONE);
                break;
            case R.id.chat_camara:
                chatOption.startCamara();
                moreLayout.setVisibility(View.GONE);
                break;
            case R.id.chat_photo:
                chatOption.startImageSelect();
                moreLayout.setVisibility(View.GONE);
                break;
            case R.id.chat_location:
                createLocationDialog();
                break;
            case R.id.cancel:
                voiceLayout.setVisibility(View.GONE);
                editLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.view_edit:
                editLayout.setVisibility(View.VISIBLE);
                helpLayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.view_buy_tickit:
                ((ChatActivity) getActivity()).buyTicket();
                break;
            case R.id.view_activity:
                ((ChatActivity) getActivity()).sendActivityMessage();
                break;
            case R.id.chat_send:
                if (((ChatActivity) getActivity()).isFriend) {
                    chatOption.sendTextMessage(editTextMessage.getText().toString());
                } else {
                    GUIUtil.toast(getActivity(), "对方不是你的好友，你不能发送消息！");
                }
                hidSoftInput(editTextMessage);
                editTextMessage.setText("");
                break;
            case R.id.chat_account_layout:
                editLayout.setVisibility(View.INVISIBLE);
                helpLayout.setVisibility(View.VISIBLE);
                break;
        }
    }


    int position = 0;
    private Runnable showRecoderRunnable = new Runnable() {
        @Override
        public void run() {
            if (position == 0) {
                showRecoderImage.setImageResource(R.drawable.message_recoder_1);
            } else if (position == 1) {
                showRecoderImage.setImageResource(R.drawable.message_recoder_2);
            } else if (position == 2) {
                showRecoderImage.setImageResource(R.drawable.message_recoder);
            }
            if (position == 2) {
                position = 0;
            } else {
                position++;
            }
            if (recoding) {
                handler.postDelayed(showRecoderRunnable, 500);
                int time = (int) ((System.currentTimeMillis() - startTime) / 1000);
                messageLengthText.setText(time + "s");
            }
        }
    };

    @Override
    public void startRecorder(MediaRecorder recorder) {
        recoding = true;
    }

    @Override
    public void stopedRecorder(MediaRecorder mr) {
        reCoderLayout.setVisibility(View.GONE);
        recoding = false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!recoding) {
                    LogUtil.d(getClass(), "startRecoding");
                    recoderVoice();
                }
                break;
            case MotionEvent.ACTION_UP:
                stopRecode();
                break;

            default:
                break;
        }
        return true;
    }

    private void recoderVoice() {
        reCoderLayout.setVisibility(View.VISIBLE);
        handler.post(showRecoderRunnable);
        startTime = System.currentTimeMillis();
        recoderVoice.start(getActivity());
//        new Thread() {
//            public void run() {
//                recoderVoice.start(getActivity());
//            }
//
//            ;
//        }.start();
    }

    private void stopRecode() {
        long endTime = System.currentTimeMillis();
        String path = recoderVoice.stop();
        reCoderLayout.setVisibility(View.GONE);
        if (endTime - startTime >= 2000) {
            chatOption.sendVoiceMessage(path, (int) (endTime - startTime) / 1000);
        } else {
            GUIUtil.toast(getActivity(), "录音时间太短");
            //toastString(getString(R.string.recorder_time_short));
        }
    }

    public void selectLast() {
        listView.setSelection(chatAdapter.getCount());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        BaseMessage message = messageList.get(i);
        if (message.getMessageType() == MessageType.TEXT) {
            createTextDialog(message);
        } else {
            createOtherDialog(message);
        }
        return false;
    }

    private void copy(BaseMessage message) {
        ClipboardManager clip = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
        clip.setText(((TextMessageJson) message.getMessage()).getText());
    }

    private void createTextDialog(final BaseMessage message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items;
        items = new String[]{"复制", "清除会话", "取消"};
        builder.setTitle("选择").setItems(items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                copy(message);
                                break;
                            case 1:
                                deleteMessage(message);
                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createOtherDialog(final BaseMessage message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items;
        items = new String[]{"清除会话", "取消"};
        builder.setTitle("选择").setItems(items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                deleteMessage(message);
                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items;
        items = new String[]{"发送", "取消"};
        builder.setTitle("选择").setItems(items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                chatOption.startLocation();
                                moreLayout.setVisibility(View.GONE);
                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteMessage(BaseMessage bean) {
        ChatDBManager.getInstance(getActivity()).deleteMessage(bean);
        chatAdapter.removeMessage(bean);
        listView.setAdapter(chatAdapter);
//        notifyDataSetChanged();
//        initMessage();
    }

//    public void clean() {
//        chatAdapter.getModelList().clear();
//    }

    private void resendMessage(BaseMessage message) {
        LogUtil.e(getClass(), message.toXML());
        // MessageUtil.sendMessage(message, messageSendListener);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (TextUtil.isEmpty(editTextMessage.getText().toString())) {
            btnVoice.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.GONE);
        } else {
            btnVoice.setVisibility(View.GONE);
            btnSend.setVisibility(View.VISIBLE);
        }
    }

    class DownloadListener implements IDownLoadListener {
        private BaseMessage message;
        private View view;

        DownloadListener(BaseMessage message, View view) {
            this.message = message;
            this.view = view;
        }

        @Override
        public void startDownload(int length) {

        }

        @Override
        public void downloading(int len) {

        }

        @Override
        public void downLoadEnd(File file) {
            recoderVoice.playMusic(file, view, message);
        }
    }
}
