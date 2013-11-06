package com.qinnuan.engine.activity.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.activity.filmFan.UnReadMsgActivity;
import com.qinnuan.engine.adapter.DynamicAdapter;
import com.qinnuan.engine.api.UpdateUserDynamicbackimg;
import com.qinnuan.engine.api.filmFan.AddUserDynamiccomment;
import com.qinnuan.engine.api.filmFan.AddUserLikedynamic;
import com.qinnuan.engine.api.filmFan.DeleteUserLikedynamic;
import com.qinnuan.engine.api.filmFan.GetUserDynamicMy;
import com.qinnuan.engine.api.filmFan.GetUserDynamicOther;
import com.qinnuan.engine.bean.Comment;
import com.qinnuan.engine.bean.Dynamic;
import com.qinnuan.engine.bean.Like;
import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.listener.IOnItemClickListener;
import com.qinnuan.engine.xmpp.IReciveMessage;
import com.qinnuan.engine.xmpp.message.MessageUtil;
import com.qinnuan.engine.xmpp.message.SessionType;
import com.qinnuan.engine.xmpp.provider.DynamicNumberSessionDaoImpl;
import com.qinnuan.engine.xmpp.provider.SessionManager;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.common.util.image.crop.CropImageUtil;
import com.qinnuan.common.widget.FaceGridView;
import com.qinnuan.common.widget.TitleWidget;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.api.filmFan.DeleteUserDynamic;
import com.qinnuan.engine.api.filmFan.DeleteUserDynamiccomment;
import com.qinnuan.engine.api.filmFan.GetUserDynamic;
import com.qinnuan.engine.application.BaoguApplication;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.DynamicMsg;
import com.qinnuan.engine.bean.User;
import com.qinnuan.engine.util.view.ListViewPro;
import com.qinnuan.engine.xmpp.MessageSendListener;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.common.widget.BottomPopWindow;

import org.jivesoftware.smack.packet.Packet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class DynamicActivity extends BaseActivity {

    private DynamicActivity instance = this;
    private GetUserDynamic dynamicParam = new GetUserDynamic();
    private GetUserDynamicMy dynamicMyParam = new GetUserDynamicMy();
    private GetUserDynamicOther dynamicOtherParam = new GetUserDynamicOther();
    private UpdateUserDynamicbackimg updateBgParam = new UpdateUserDynamicbackimg();
    private AddUserLikedynamic likeParam = new AddUserLikedynamic();
    private DeleteUserLikedynamic unLikeParam = new DeleteUserLikedynamic();

    private Integer isnext = 0;
    private String cinemadynamicid; //列表最后一条动态ID 点击更多传
    private boolean isNotifyChange = false;
    public static final String DELETE_ID = "delete_id";
    public static int RETURN_OK = 111;
    private static int submit_code = -1;
    private static final int REPY = 1;
    private static final int COMMENT= 2;

    private ListViewPro lv;
    private View itemHead;
    private DynamicAdapter dynamiAdapter;

    //head views
    private User user;
    private TextView msg;
    private ImageView msgimg;
    private ImageView headBg;
    private ImageView userhead;
    private TextView name;
    private TextView sign;
    private View header_msg;
    private int location = -1;
    private String imgUrl;

    private String likecdynamicid_;//bean.getLikecdynamicid() 不为空时用
    private String likecdynamicid;//bean.getLikecdynamicid() 为空时用

    private int count;
    private String headUrl;
    private DynamicNumberSessionDaoImpl numImpl;

    private CropImageUtil cropU;

    private final int MESSAGE_COME = 1;
    private SessionManager sessionManager;

    public static final int PUBLISH_CODE = 1111;
    public static final String PUBLISH_CODE_ID = "publish_code_id";

    public static final int COMMENT_CODE = 1112;
    public static final String COMMENT_CODE_ID = "comment_code_id";

    public static final String DYNAMIC_DELETED_ID = "dynamic_delete_id";

    private String[] menu = new String[]{"用相机拍一张", "从相册选一张"};
    private TitleWidget mTitleW;

    //input views
    private EditText contentEdit;
    private ImageView faceButton;
    private Button sendButton;
    private FaceGridView faceGridView;
    private View editLayout;

    //comments
    private AddUserDynamiccomment addCommentParam = new AddUserDynamiccomment();
    private DeleteUserDynamiccomment deleteCommentParam = new DeleteUserDynamiccomment();
    private DeleteUserDynamic deleteDynamicParam  = new DeleteUserDynamic();

    private String content;

    private String toId;
    private String toUserName;
    private String currentCdyid;
    //private String currentUserIdto;
    private Comment currentComment;
    private List<Comment> currentComments;
    private DynamicAdapter.ViewHolder currentHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dynamic);
        httpDynamic();
        initView();

        cropU = CropImageUtil.getInstance(instance);
        sessionManager = SessionManager.getInstance(this);
        if (BaoguApplication.xmppManager != null) {
            BaoguApplication.xmppManager.getReciveMessageList().add(irm);
        }
    }

    private void initView() {
        initTitle();
        initHead();
        initEditView();
        initEditEvent();
    }

    private void initTitle() {
        mTitleW = getTitleWidget();
        mTitleW.getLeftL().setOnClickListener(onClickL);
        mTitleW.getRightL().setOnClickListener(onClickL);
    }

    private void initHead() {
        lv = (ListViewPro)findViewById(R.id.fragment_dynamic_listv);
        if(itemHead != null) return;
        itemHead = LayoutInflater.from(instance).inflate(
                R.layout.item_dynamic_header, null);

        userhead = (ImageView)itemHead.findViewById(R.id.item_dynamic_head_userhead);
        header_msg = itemHead.findViewById(R.id.item_dynamic_head_msglayout);
        msg = (TextView) itemHead.findViewById(R.id.item_dynamic_head_msg);
        msgimg = (ImageView) itemHead.findViewById(R.id.item_dynamic_head_msgimg);
        headBg = (ImageView)itemHead.findViewById(R.id.item_dynamic_head_bg);
        name  = (TextView) itemHead.findViewById(R.id.item_dynamic_head_name);
        sign = (TextView) itemHead.findViewById(R.id.item_dynamic_head_sign);

        headBg.setOnClickListener(onClickL);
        header_msg.setOnClickListener(onClickL);
        userhead.setOnClickListener(onClickL);

        lv.setHeadViews(itemHead, headBg);
        lv.addHeaderView(itemHead);
        setIListVPro(ilp);
    }

    @Override
    public void onResume() {
        super.onResume();
        xmppDynamicMsg();
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        try {
            if (url.contains(dynamicParam.getApi())) {
                parseDynamic(conent);
                onLoaded(isnext);
            } else if (url.contains(dynamicMyParam.getApi())) {
                parseDynamic(conent);
            } else if (url.contains(dynamicOtherParam.getApi())) {
                parseDynamic(conent);
            } else if(url.contains(likeParam.getApi())) {
                sendXmppMsg();
                parseLike(conent);
            } else if(url.contains(unLikeParam.getApi())) {

            } else if(url.contains(updateBgParam.getApi())) {

            } else if(url.contains(addCommentParam.getApi())) {
                sendXmppMsg();
                parseAddComment(conent);
            } else if(url.contains(deleteCommentParam.getApi())){

            } else if(url.contains(deleteDynamicParam.getApi())){

            } else {
                parseUploadImg(conent);
            }
            //hideEditLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendXmppMsg() {
        BaseMessage message = MessageUtil.getInstance(instance).createDynamicNumber(toId);
        message.setSessionType(SessionType.DYNAMICNUMBER);
        BaoguApplication.xmppManager.sendPacket(message,new MessageSendListener() {
            @Override
            public void sendSuccess(Packet message) {
                LogUtil.e(getClass(), "sendSuccess=>" + message.toXML());
            }
            @Override
            public void sendFail(BaseMessage message, FailType type) {  }
        });
    }

    private void httpDynamic() {
        isNotifyChange = false;
        dynamicParam.setDevicetype(Const.DEVICE_TYPE);
        dynamicParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        dynamicParam.setUserid(Const.user.getUserid());
        dynamicParam.setCinemadynamicid(null);
        request(dynamicParam);
    }

    private void httpDynamicLoadmore() {
        isNotifyChange = true;
        dynamicParam.setDevicetype(Const.DEVICE_TYPE);
        dynamicParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        dynamicParam.setUserid(Const.user.getUserid());
        dynamicParam.setCinemadynamicid(cinemadynamicid);
        request(dynamicParam);
    }


    private void parseDynamic(String json) throws Exception {
        JSONObject jobj = new JSONObject(json);
        if(jobj.getInt("status") != Const.HTTP_OK) return;
        List<Dynamic> dynamics = new ArrayList<Dynamic>();
        JSONObject jdata = jobj.getJSONObject("data");
        isnext = jdata.getInt("isnext");
        user = JSONTool.jsonToBean(User.class, jdata.getJSONObject("user"));
        JSONArray dynamicArray = jdata.getJSONArray("dynamic");
        for(int i=0; i<dynamicArray.length(); i++) {
            JSONObject jdynamicObj = dynamicArray.getJSONObject(i);
            Dynamic dynamic = JSONTool.jsonToBean(Dynamic.class, jdynamicObj);
            List<Comment> comments = JSONTool.getJsonToListBean(Comment.class, jdynamicObj.getJSONArray("comment"));
            JSONObject jlikeObj = jdynamicObj.getJSONObject("like");
            if(!jlikeObj.isNull("likelist")) {
                List<Like> likes = JSONTool.getJsonToListBean(Like.class, jlikeObj.getJSONArray("likelist"));
                dynamic.setLikes(likes);
            }
            dynamic.setComments(comments);
            dynamics.add(dynamic);
        }

        if(!dynamics.isEmpty())
            cinemadynamicid = dynamics.get(dynamics.size()-1).getCinemadynamicid();

        if(!isNotifyChange) {
            setHeadData(user);
            initAdapter(dynamics);
        } else {
            if(isnext == 0) {
                removeFooter();
            }
            notifyAdapter(dynamics);
        }

    }

    public void setHeadData(User u) {
        user = u;
        name.setText(user.getNickname());
        sign.setText(user.getSignature());

        String bgUrl = user.getDynamicbackimage();
        if(!TextUtil.isEmpty(bgUrl)) {
            mImageFetcher.setLoadingImage(R.drawable.film_front_defalt);
            mImageFetcher.loadImage(bgUrl, headBg);
        }
        mImageFetcher.setLoadingImage(R.drawable.film_front_defalt);
        mImageFetcher.loadImage(user.getProfileimg(), userhead,
                GUIUtil.getDpi(instance, R.dimen.margin_8));
    }

    private IOnItemClickListener iOnItemClickL =  new IOnItemClickListener() {
        @Override
        public void onClick(Object obj) {
            LogUtil.e(getClass(), "==iOnItemClickL==");
            View v = (View)obj;
            switch (v.getId()) {
                case R.id.item_dynamic_like: {
                    httpLike((ImageView) v);
                    break;
                }

                case R.id.item_dynamic_delete: {
                    if (numImpl != null) {
                        numImpl.deleteSession(null);
                    }
                    httpDeleteDynamic((Dynamic)v.getTag());
                    break;
                }
                case R.id.item_dynamic_comment: {
                    submit_code = COMMENT;
                    LogUtil.e(getClass(), "comment type==>"+submit_code);

                    DynamicAdapter.CItemTag ctag = (DynamicAdapter.CItemTag)v.getTag();
                    toId = ctag.toUserId;

                    currentHolder = (DynamicAdapter.ViewHolder)v.getTag(R.id.holder);
                    currentCdyid = ctag.dynId;
                    currentComments = ctag.comments;

                    showEditLayout();
                    break;
                }

                case R.id.commenttext: {
                    submit_code = REPY;
                    LogUtil.e(getClass(), "commenttext type==>"+submit_code);
                    showEditLayout();
                    DynamicAdapter.CItemTag ctag = (DynamicAdapter.CItemTag)v.getTag();
                    currentHolder = (DynamicAdapter.ViewHolder)v.getTag(R.id.holder);
                    toId = ctag.toUserId;
                    toUserName = ctag.toUserName;
                    currentCdyid = ctag.cdynId;
                    currentComment = ctag.comment;
                    currentComments = ctag.comments;
                    break;
                }

                default:
                    break;
            }
        }
    };

    private void httpLike(ImageView v) {
        Dynamic bean = (Dynamic)v.getTag();
        toId = bean.getUserid();
        DynamicAdapter.ViewHolder holder = (DynamicAdapter.ViewHolder)v.getTag(R.id.holder);
        if(TextUtil.isEmpty(bean.getLikecdynamicid())) { //已赞,执行取消赞
            if(TextUtil.isEmpty(likecdynamicid)) {
                LogUtil.e(getClass(), "httplike-1");
                v.setImageResource(R.drawable.liked);
                likeParam.setUserid(Const.user.getUserid());
                likeParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
                likeParam.setDevicetype(Const.DEVICE_TYPE);
                likeParam.setCinemadynamicid(bean.getCinemadynamicid());
                request(likeParam);
                //dynamicF.notifyLike(holder, bean, true);
                notifyLike(holder, bean, true);
            } else {
                v.setImageResource(R.drawable.like);
                LogUtil.e(getClass(), "httplike-1-2");
                unLikeParam.setUserid(Const.user.getUserid());
                unLikeParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
                unLikeParam.setDevicetype(Const.DEVICE_TYPE);
                unLikeParam.setLikecdynamicid(likecdynamicid);
                request(unLikeParam);
                likecdynamicid = null;//设置为空
                notifyLike(holder, bean, false);
            }

        } else {//未赞,执行增加赞
            LogUtil.e(getClass(), "likecdynamicid_=>"+likecdynamicid_);
            if(TextUtil.isEmpty(likecdynamicid_)) {
                v.setImageResource(R.drawable.like);
                LogUtil.e(getClass(), "httplike-2");
                unLikeParam.setUserid(Const.user.getUserid());
                unLikeParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
                unLikeParam.setDevicetype(Const.DEVICE_TYPE);
                unLikeParam.setLikecdynamicid(bean.getLikecdynamicid());
                request(unLikeParam);
                likecdynamicid_ = bean.getLikecdynamicid();
                notifyLike(holder, bean, false);
            } else {
                LogUtil.e(getClass(), "httplike-2-1");
                v.setImageResource(R.drawable.liked);
                likeParam.setUserid(Const.user.getUserid());
                likeParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
                likeParam.setDevicetype(Const.DEVICE_TYPE);
                likeParam.setCinemadynamicid(bean.getCinemadynamicid());
                request(likeParam);
                likecdynamicid_ = null;
                notifyLike(holder, bean, true);
            }
        }
    }

    private void parseLike(String json) throws Exception {
        JSONObject jObj = new JSONObject(json);
        if(jObj.getInt("status") == Const.HTTP_OK) {
            likecdynamicid = jObj.getJSONObject("data").getString("likecdynamicid");
        }
    }

    private void xmppDynamicMsg() {
        numImpl = new DynamicNumberSessionDaoImpl(this);
        List<MessageListBean> msgCounts = numImpl.getSession();
        DynamicMsg msg = null;
        if(msgCounts != null) {
            count = msgCounts.get(0).getCount();
            headUrl = msgCounts.get(0).getHeadImage();
            LogUtil.e(getClass(), "count=>"+count);
            msg = new DynamicMsg();
            msg.setMsgcount(count);
            msg.setProfileimg(headUrl);
        }
        setMsgData(msg);

//        LastHeadSessionDaoImpl headImpl = new LastHeadSessionDaoImpl(this);
//        List<MessageListBean> msgHeads = headImpl.getSession();
//        if(msgHeads != null) {
//            headUrl = msgHeads.get(0).getHeadImage();
//        }


    }

    public void setMsgData(DynamicMsg dynamicMsg) {
        if (dynamicMsg != null) {
            header_msg.setVisibility(View.VISIBLE);
            msg.setText("你有" + dynamicMsg.getMsgcount() + "条新消息");
            //iRoundImgL.load(dynamicMsg.getProfileimg(), msgimg);
            mImageFetcher.loadImage(dynamicMsg.getProfileimg(), msgimg,
                    GUIUtil.getDpi(instance, R.dimen.margin_8));
        } else {
            header_msg.setVisibility(View.GONE);
        }
    }

    public void notifyLike(DynamicAdapter.ViewHolder holder, Dynamic bean, boolean isAdd) {
        dynamiAdapter.notifyLike(holder, bean, isAdd);
    }

    private ListViewPro.IListVPro ilp = new ListViewPro.IListVPro() {
        @Override
        public void onRefresh() {
            httpDynamic();
        }

        @Override
        public void onLoadMore() {
            httpDynamicLoadmore();
        }
    };

    private BottomPopWindow.OnMenuSelect onMenuSelect = new BottomPopWindow.OnMenuSelect() {

        @Override
        public void onItemMenuSelect(int position) {
            if (position == 0) {
                cropU.startCamera(instance);
            } else if(position == 1) {
                cropU.startPicPhoto(instance);
            }
            buttomPop.dismiss();
        }

        @Override
        public void onCancelSelect() {
            buttomPop.dismiss();
        }
    };

    public void setIListVPro(ListViewPro.IListVPro l) {
        lv.setIListVPro(l);
    }

    private void onLoaded(int isnext) {
        lv.onLoaded(isnext);
    }

    public void removeFooter() {
        lv.removFooter();
    }

    public void initAdapter(List<Dynamic> l) {
        dynamiAdapter = new DynamicAdapter(instance, l);
        dynamiAdapter.setListener(mImageFetcher, iOnItemClickL);
        lv.setAdapter(dynamiAdapter);
        lv.setOnItemClickListener(onItemClick);
    }

    public void notifyAdapter(List<Dynamic> dynamics) {
        dynamiAdapter.getModelList().addAll(dynamics);
        dynamiAdapter.notifyDataSetChanged();
        lv.setOnItemClickListener(onItemClick);
    }

    public void notifyAdapter(Dynamic d) {
        dynamiAdapter.getModelList().remove(d);
        dynamiAdapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            if(view == itemHead) return;
//            if(view == lv.getFooterView()) return;
//            Intent intent = new Intent(instance, DynamicDetailActivity.class);
//            location = position-1;
//            intent.putExtra(DynamicDetailActivity.DYNAMIC_ID, dynamiAdapter.getItem(location));
//            startActivity(intent);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.e(getClass(), "===onActivityResult===");
        if(DynamicDetailActivity.isComment) {
            httpDynamic();
        }
        // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case CropImageUtil.IMG_CROP: {
                cropU.startPhotoCrop(cropU.getCameraImgUrl(), CropImageUtil.IMG_CROP_READ, instance); // 照相机图片裁剪
            }
            break;
            case CropImageUtil.IMG_CROP_READ: {
                cropU.readCropImage(data);              //读取裁剪的图片
                Bitmap photoBitmap = cropU.getPhotoBitmap();
                File photoFile = cropU.getPhotoFile();
                uploadFile("1", photoFile);
                headBg.setImageDrawable(new BitmapDrawable(photoBitmap));
                LogUtil.e(getClass(), "bitm=>"+photoBitmap+", file=>"+photoFile);
            }
            break;
            case CropImageUtil.CHOICE_IMAGE: {
                if(data != null) {
                    Uri originalUri = cropU.getChoiceLocalImgUrl(data); // 获得图片的uri
                    cropU.startPhotoCrop(originalUri, CropImageUtil.IMG_CROP_READ, instance); // 相册图片裁剪
                }
            }
            break;
            case PUBLISH_CODE: {
                Integer code = data.getIntExtra(PUBLISH_CODE_ID, -1);
                if(code == PUBLISH_CODE ) {
                    httpDynamic();
                }
            }
            break;
            case COMMENT_CODE: {
                LogUtil.e(getClass(), "COMMENT_CODE");
                Integer code = data.getIntExtra(COMMENT_CODE_ID, -1);
                if(code == COMMENT_CODE ) {
                    //Dynamic d = (Dynamic)data.getSerializableExtra(DYNAMIC_DELETED_ID);
                    Dynamic d = DynamicAdapter.dynimc_delete;
                    LogUtil.e(getClass(), "d==>"+d);
                    if(d != null) {
                        LogUtil.e(getClass(), "list size ==>"+dynamiAdapter.getModelList().size());

                        LogUtil.e(getClass(), "list size ==>"+dynamiAdapter.getModelList().size());
                        notifyAdapter(d);
                    }
                }
            }
            break;

            default:

                break;
        }

        //super.onActivityResult(requestCode, resultCode, data);
    }

    private View.OnClickListener onClickL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.item_dynamic_head_bg:
                    showButtomPop(onMenuSelect, mTitleW, menu);
                    break;
                case R.id.left:
                    finish();
                    break;
                case R.id.right:
                    startActivityForResult(new Intent(instance, DynamicPulishActivity.class),
                            PUBLISH_CODE);
                    break;
                case R.id.item_dynamic_head_msglayout:
                    if(numImpl != null) {
                        int count = numImpl.getSession().get(0).getCount();
                        numImpl.deleteSession(null);
                        Intent intent = new Intent(instance, UnReadMsgActivity.class);
                        intent.putExtra(UnReadMsgActivity.COUNT_ID, count);
                        startActivity(intent);
                    }
                    break;
                case R.id.item_dynamic_head_userhead: {
                    Intent intent = new Intent(instance, DynamicFanActivity.class);
                    intent.putExtra(DynamicFanActivity.USER_TO_ID, Const.user.getUserid());
                    intent.putExtra(DynamicFanActivity.TYPE_ID, DynamicFanActivity.DYNAMIC_MINE);
                    startActivity(intent);
                }
                break;
                default:break;
            }
        }
    };

    private void parseUploadImg(String json) throws Exception {
        JSONObject jobj = new JSONObject(json);
        if (Const.HTTP_OK == jobj.getInt("status")) {
            imgUrl = jobj.getString("httpurls");
            httpUpdateBg();
        } else {
            GUIUtil.toast(instance, R.string.regist_phone_upload_fail);
        }
    }

    private void httpUpdateBg() {
        needDialog = false;
        updateBgParam.setUserid(Const.user.getUserid());
        updateBgParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        updateBgParam.setDevicetype(Const.DEVICE_TYPE);
        updateBgParam.setDynamicbackimage(imgUrl);
        request(updateBgParam);
    }

    @Override
    public void onDestroy() {
        BaoguApplication.xmppManager.getReciveMessageList().remove(irm);
        super.onDestroy();
    }

    private IReciveMessage irm = new IReciveMessage() {
        @Override
        public boolean reciveMessage(BaseMessage msg) {
            sendMessage(MESSAGE_COME, msg);
            return false;
        }
    };

    private void initMsgNumber() {
        if (Const.user != null) {
            xmppDynamicMsg();
        }
    }

    private void sendMessage(int what, Object o) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = o;
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_COME:
                    initMsgNumber();
                    break;
                default:break;
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
    }

    private void parseDeleteDynamic(String conent) throws Exception {
        int status = new JSONObject(conent).getInt("status");
        if(status == Const.HTTP_OK) {
//            Intent i = getIntent();
//            i.putExtra(DynamicActivity.COMMENT_CODE_ID, DynamicActivity.COMMENT_CODE);
//            i.putExtra(DynamicActivity.DYNAMIC_DELETED_ID, dynamic_pre);
//            setResult(RESULT_OK, i);
//            finish();
        }

    }

    private void parseDeleteComment(String conent) throws Exception {
        JSONObject jObj = new JSONObject(conent);
        if(jObj.getInt("status") != Const.HTTP_OK) return;
        //adapter.getModelList().remove(comment);
        //notifyAdapter();
    }

    private void parseAddComment(String conent) throws Exception {
        JSONObject jObj = new JSONObject(conent);
        if(jObj.getInt("status") != Const.HTTP_OK) return;

        JSONObject jData = jObj.getJSONObject("data");
        String comentid = jData.getString("cdynamiccommentid");
        String createdate = jData.getString("createdate");
        String rootcomentid= jData.getString("rootcdynamiccommentid");
        Comment c = new Comment();
        c.setProfileimg(Const.user.getProfileimg());
        c.setNickname(Const.user.getNickname());
        c.setCdynamiccommentid(comentid);
        c.setRootcdynamiccommentid(rootcomentid);
        LogUtil.e(getClass(), "==submit_code==>"+submit_code);
        LogUtil.e(getClass(), "==toId==>"+toId);
        if(submit_code == REPY) {
            if(!TextUtil.isEmpty(toId)) {
                c.setUseridto(toId);
                c.setNicknameto(toUserName);
            }
        }
        c.setContent(content);
        c.setCreatedate(createdate);
        c.setUserid(Const.user.getUserid());
        currentComments.add(c);
        dynamiAdapter.fillComment(currentHolder, currentCdyid, currentComments);

    }

    /** 回复评论的时候，comment不能为空，isReplacement为true*/
    private void httpAddComment(String cdynid) {
        addCommentParam = null;
        addCommentParam = new AddUserDynamiccomment();
        addCommentParam.setDevicetype(Const.DEVICE_TYPE);
        addCommentParam.setUserid(Const.user.getUserid());
        addCommentParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        addCommentParam.setCinemadynamicid(cdynid);
        addCommentParam.setContent(content);
        request(addCommentParam);
    }

    /** 回复评论的时候，comment不能为空，isReplacement为true*/
    private void httpAddRepyComment(String cdynid, Comment comment) {
        addCommentParam = null;
        addCommentParam = new AddUserDynamiccomment();
        addCommentParam.setDevicetype(Const.DEVICE_TYPE);
        addCommentParam.setUserid(Const.user.getUserid());
        addCommentParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        addCommentParam.setCinemadynamicid(cdynid);
        addCommentParam.setContent(content);
        addCommentParam.setRootcdynamiccommentid(comment.getRootcdynamiccommentid());
        addCommentParam.setRefcdynamiccommentid(comment.getCdynamiccommentid());
        request(addCommentParam);

    }


    private void httpDeleteDynamic(Dynamic dynamic) {
        deleteDynamicParam.setDevicetype(Const.DEVICE_TYPE);
        deleteDynamicParam.setUserid(Const.user.getUserid());
        deleteDynamicParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        deleteDynamicParam.setCinemadynamicid(dynamic.getCinemadynamicid());
        request(deleteDynamicParam);
    }

    private void submitComment() {

        content = TextUtil.getDecodStr(instance, contentEdit.getText().toString()) ;
        LogUtil.e(getClass(), "content=>"+content);
        if(TextUtil.isEmpty(content)) {
            GUIUtil.toast(instance,"内容不能为空");
            return;
        }
        httpAddComment(currentCdyid);
       // submit_code = -1;
    }

    private void submitRepyComment() {

        content = TextUtil.getDecodStr(instance, contentEdit.getText().toString()) ;
        LogUtil.e(getClass(), "content=>"+content);
        if(TextUtil.isEmpty(content)) {
            GUIUtil.toast(instance,"内容不能为空");
            return;
        }
        httpAddRepyComment(currentCdyid, currentComment);
        //submit_code = -1;
    }

    private void contentClear() {
        content = null;
        contentEdit.getText().clear();
        contentEdit.setHint(null);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.messageFace: {
                    faceGridView.setVisibility(View.VISIBLE);
                    hideInput();
                    break;
                }

                case R.id.send: {
                    if(submit_code == COMMENT) {
                        submitComment();
                    } else if(submit_code == REPY) {
                        submitRepyComment();
                    }
                    hideEditLayout();
                    break;
                }

                case R.id.activity_dynamic_detail_comment: {
                    contentClear();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(contentEdit, InputMethodManager.SHOW_IMPLICIT);
                    break;
                }

                default:
                    break;
            }
        }
    };

    private void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(contentEdit.getWindowToken(), 0);
    }

    private void showInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(contentEdit.getWindowToken(), 1);
    }

    private void initEditEvent() {
        faceButton.setOnClickListener(onClickListener);
        sendButton.setOnClickListener(onClickListener);
        faceGridView.bindEditText(contentEdit);
    }

    private void initEditView() {
        editLayout = findViewById(R.id.editLayout);
        contentEdit = (EditText) findViewById(R.id.messageContent);
        faceButton = (ImageView) findViewById(R.id.messageFace);
        sendButton = (Button) findViewById(R.id.send);
        faceGridView = (FaceGridView) findViewById(R.id.fgv_face);

    }

    private void showEditLayout() {
        contentClear();
        if(editLayout.getVisibility() != View.VISIBLE)
            editLayout.setVisibility(View.VISIBLE);
    }

    private void hideEditLayout() {
        editLayout.setVisibility(View.GONE);
        //contentClear();
    }


}
