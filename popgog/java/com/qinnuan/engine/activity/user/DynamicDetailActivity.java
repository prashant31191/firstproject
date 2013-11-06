package com.qinnuan.engine.activity.user;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qinnuan.engine.adapter.BaoguBaseAdapter;
import com.qinnuan.engine.api.filmFan.AddUserDynamiccomment;
import com.qinnuan.engine.api.filmFan.AddUserLikedynamic;
import com.qinnuan.engine.api.filmFan.DeleteUserLikedynamic;
import com.qinnuan.engine.bean.Comment;
import com.qinnuan.engine.bean.Like;
import com.qinnuan.engine.util.view.MyXListView;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.common.widget.FaceGridView;
import com.qinnuan.common.widget.TitleWidget;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.api.filmFan.DeleteUserDynamic;
import com.qinnuan.engine.api.filmFan.DeleteUserDynamiccomment;
import com.qinnuan.engine.api.filmFan.GetUserDynamicDetail;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.application.MyPref;
import com.qinnuan.engine.bean.Dynamic;
import com.qinnuan.engine.util.view.XListView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class DynamicDetailActivity extends BaseActivity {

    private DynamicDetailActivity instance = this;

    private int isnext = 0;
    private Dynamic dynamic;
    private int width = 480;
    public static final String DYNAMIC_ID="dynamic_id";
    private View headView;
    private MyXListView listView;
    private ViewHolder holder = new ViewHolder();
    private List<Comment> comments;
    private Comment comment;
    private String content;

    private EditText contentEdit;
    private ImageView faceButton;
    private Button sendButton;
    private FaceGridView faceGridView;

    private GetUserDynamicDetail detailParam = new GetUserDynamicDetail();
    private AddUserDynamiccomment addCommentParam = new AddUserDynamiccomment();
    private DeleteUserDynamiccomment deleteCommentParam = new DeleteUserDynamiccomment();
    private DeleteUserDynamic deleteDynamicParam  = new DeleteUserDynamic();
    private AddUserLikedynamic likeParam = new AddUserLikedynamic();
    private DeleteUserLikedynamic unLikeParam = new DeleteUserLikedynamic();

    private String likecdynamicid_;//bean.getLikecdynamicid() 不为空时用
    private String likecdynamicid;//bean.getLikecdynamicid() 为空时用

    private String cdynamiccommentid;  //评论ID 分页用
    private boolean notifyChange = false;
    public static boolean isComment = false;

    Like like;
    private int type = -1;
    private DynamicCommentAdapter adapter;

    private Dynamic dynamic_pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detail);
        dynamic_pre = (Dynamic)getIntent().getSerializableExtra(DYNAMIC_ID);
        if(Const.user.getUserid().equals(dynamic_pre.getUserid())) {
            type = 1;
        }
        initView();
        initEvent();
        LogUtil.e(getClass(), "type==========>" + type);
        httpDynamicDetail(dynamic_pre);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        try {
            if(url.contains(detailParam.getApi())) {
                parseDynamic(conent);

            } else if(url.contains(addCommentParam.getApi())) {
                parseAddComment(conent);
            } else if(url.contains(deleteCommentParam.getApi())) {
                parseDeleteComment(conent);
            } else if(url.contains(deleteDynamicParam.getApi())) {
                parseDeleteDynamic(conent);
            } else if(url.contains(likeParam.getApi())) {
                parseLike(conent);
            } else if(url.contains(unLikeParam.getApi())) {
                parseUnLike(conent);
            }
            onLoaded();
        } catch (Exception e) {
            e.printStackTrace();
        }
        contentClear();
    }

    private void parseDeleteDynamic(String conent) throws Exception {
        int status = new JSONObject(conent).getInt("status");
        if(status == Const.HTTP_OK) {
            Intent i = getIntent();
            i.putExtra(DynamicActivity.COMMENT_CODE_ID, DynamicActivity.COMMENT_CODE);
            i.putExtra(DynamicActivity.DYNAMIC_DELETED_ID, dynamic_pre);
            setResult(RESULT_OK, i);
            finish();
        }

    }

    private void parseDeleteComment(String conent) throws Exception {
        JSONObject jObj = new JSONObject(conent);
        if(jObj.getInt("status") != Const.HTTP_OK) return;
        adapter.getModelList().remove(comment);
        notifyAdapter();
    }

    private void parseAddComment(String conent) throws Exception {
        //httpDynamicDetail(dynamic);
        JSONObject jObj = new JSONObject(conent);
        if(jObj.getInt("status") != Const.HTTP_OK) return;

        JSONObject jData = jObj.getJSONObject("data");
        String comentid = jData.getString("cdynamiccommentid");
        String createdate = jData.getString("createdate");
        Comment c = new Comment();
        c.setProfileimg(Const.user.getProfileimg());
        c.setNickname(Const.user.getNickname());
        c.setCdynamiccommentid(comentid);
        c.setContent(content);
        c.setCreatedate(createdate);
        c.setUserid(Const.user.getUserid());
        adapter.getModelList().add(c);
        notifyAdapter();
    }

    private void parseLike(String json) throws Exception {
        JSONObject jObj = new JSONObject(json);
        if(jObj.getInt("status") == Const.HTTP_OK) {
            likecdynamicid = jObj.getJSONObject("data").getString("likecdynamicid");
            like = new Like();
            like.setLikedynamicid(likecdynamicid);
            like.setUserid(Const.user.getUserid());
            like.setNickname(Const.user.getNickname());
            like.setProfileimg(Const.user.getProfileimg());
            LogUtil.e(getClass(), "dynamic.getLikes().size like1=>"+dynamic.getLikes().size());
            dynamic.getLikes().add(like);
            LogUtil.e(getClass(), "dynamic.getLikes().size like2=>"+dynamic.getLikes().size());
            fillLikeData(dynamic);
        }
    }

    private void parseUnLike(String json) throws Exception {
        JSONObject jObj = new JSONObject(json);
        if(jObj.getInt("status") == Const.HTTP_OK) {
            if(like != null) {
                LogUtil.e(getClass(), "dynamic.getLikes().size unlike1=>"+dynamic.getLikes().size());
                dynamic.getLikes().remove(like);
                LogUtil.e(getClass(), "dynamic.getLikes().size unlike2=>"+dynamic.getLikes().size());
                fillLikeData(dynamic);
            } else {
                for(Like l : dynamic.getLikes()) {
                    if(l.getUserid().equals(Const.user.getUserid())) {
                        LogUtil.e(getClass(), "dynamic.getLikes().size unlike3=>"+dynamic.getLikes().size());
                        dynamic.getLikes().remove(l);
                        LogUtil.e(getClass(), "dynamic.getLikes().size unlike4=>"+dynamic.getLikes().size());
                        break;
                    }
                }
                fillLikeData(dynamic);
            }
//            Like like = new Like();
//            like.setLikedynamicid(likecdynamicid);
//            like.setUserid(Const.user.getUserid());
//            like.setNickname(Const.user.getNickname());
//            like.setProfileimg(Const.user.getProfileimg());
//            dynamic.getLikes().add(like);
//            fillLikeData(dynamic);
        }
    }

    private void httpDynamicDetail(Dynamic d) {
        detailParam.setCinemadynamicid(d.getCinemadynamicid());
        detailParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        detailParam.setUserid(Const.user.getUserid());
        detailParam.setDevicetype(Const.DEVICE_TYPE);
        request(detailParam);
    }

    private void httpDynamicDetailLoadmore(Dynamic d) {
        detailParam.setCinemadynamicid(d.getCinemadynamicid());
        detailParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        detailParam.setUserid(Const.user.getUserid());
        detailParam.setDevicetype(Const.DEVICE_TYPE);
        if(!TextUtil.isEmpty(cdynamiccommentid))
            detailParam.setCdynamiccommentid(cdynamiccommentid);
        request(detailParam);
    }

    private void initView() {
        listView = (MyXListView) findViewById(R.id.activity_dynamic_detail_listv);
        contentEdit = (EditText) findViewById(R.id.messageContent);
        faceButton = (ImageView) findViewById(R.id.messageFace);
        sendButton = (Button) findViewById(R.id.send);
        faceGridView = (FaceGridView) findViewById(R.id.fgv_face);

        initTitle();
        initHeadView();
        listView.addHeaderView(headView);
        listView.setXListViewListener(ixl);
    }

    private void initTitle() {
        TitleWidget t =  getTitleWidget();
        t.getLeftL().setOnClickListener(onClickListener);
    }

    private void initEvent() {
        faceButton.setOnClickListener(onClickListener);
        sendButton.setOnClickListener(onClickListener);
        if(type == 1) {
            holder.delete.setOnClickListener(onClickListener);
        } else {
            holder.delete.setVisibility(View.GONE);
        }
        faceGridView.bindEditText(contentEdit);
//        listView.setOnItemClickListener(onItemClickListener);
    }

    private void httpDeleteComment(Comment comment) {
        deleteCommentParam.setDevicetype(Const.DEVICE_TYPE);
        deleteCommentParam.setUserid(Const.user.getUserid());
        deleteCommentParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        deleteCommentParam.setCdynamiccommentid(comment.getCdynamiccommentid());
        request(deleteCommentParam);
    }

    /** 回复评论的时候，comment不能为空，isReplacement为true*/
    private void httpAddComment(boolean isReplyComment) {
        addCommentParam.setDevicetype(Const.DEVICE_TYPE);
        addCommentParam.setUserid(Const.user.getUserid());
        addCommentParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        addCommentParam.setCinemadynamicid(dynamic.getCinemadynamicid());
        addCommentParam.setContent(content);
        if(isReplyComment) {
            addCommentParam.setRootcdynamiccommentid(comment.getRootcdynamiccommentid());
            addCommentParam.setRefcdynamiccommentid(comment.getRefcdynamiccommentid());
        }
        request(addCommentParam);
        isComment = true;
    }

    private void httpDeleteDynamic() {
        deleteDynamicParam.setDevicetype(Const.DEVICE_TYPE);
        deleteDynamicParam.setUserid(Const.user.getUserid());
        deleteDynamicParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        deleteDynamicParam.setCinemadynamicid(dynamic.getCinemadynamicid());
        request(deleteDynamicParam);
    }

    private void submit() {
        content = TextUtil.getDecodStr(instance, contentEdit.getText().toString()) ;
        LogUtil.e(getClass(), "content=>"+content);
        if(TextUtil.isEmpty(content)) {
            GUIUtil.toast(instance,"内容不能为空");
            return;
        }

        CharSequence hint = contentEdit.getHint();
        if(hint == null) {
            httpAddComment(false);
        } else {
            content = hint + content;
            httpAddComment(true);
        }
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
                case R.id.messageFace:
                    faceGridView.setVisibility(View.VISIBLE);
                    hideInput();
                    break;
                case R.id.send:
                    submit();
                    break;
                case R.id.activity_dynamic_detail_delete:
                    GUIUtil.showDialog(instance, "是否删除这条动态", "删除", "不",
                            new GUIUtil.IOnAlertDListener() {
                        @Override
                        public void ok() {
                            httpDeleteDynamic();
                        }

                        @Override
                        public void cancel() {  }
                    });
                    break;
                case R.id.item_dynamic_head:
                    break;
                case R.id.activity_dynamic_detail_like:
                    httpLike((ImageView)v);
                    break;
//                case R.id.item_dynamic_comment:
//                    Intent in = new Intent(instance, DynamicDetailActivity.class);
//                    in.putExtra(DynamicDetailActivity.DYNAMIC_ID, (Dynamic)v.getTag());
//                    instance.startActivityForResult(in, Activity.RESULT_OK);
//                    break;
                case R.id.play:
                    Intent it = new Intent(Intent.ACTION_VIEW);
                    it.setDataAndType(Uri.parse((String) v.getTag()), "video/mp4");
                    startActivity(it);
                    break;

                case R.id.activity_dynamic_detail_comment:
                    contentClear();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(contentEdit, InputMethodManager.SHOW_IMPLICIT);

                    break;
                case R.id.left:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void initAdapter() {
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);
        listView.setIHasNext(ihn);
    }

    private void notifyAdapter() {
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(onItemClickListener);
        listView.setIHasNext(ihn);
    }

    private void notifyAdapter(List<Comment> comments) {
        adapter.getModelList().addAll(comments);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(onItemClickListener);
        listView.setIHasNext(ihn);
    }

    private AdapterView.OnItemClickListener onItemClickListener =  new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(view == headView)  return;
            if(view == listView.getHeadView() || view == listView.getFooterView()) return;
            comment = comments.get(position-2);
            if(comment != null) view.setTag(comment);

            if (!comment.getUserid().equals(Const.user.getUserid())) {
                contentClear();
                SpannableStringBuilder hint = SpannableStringBuilder.valueOf("@")
                        .append(TextUtil.getProcessText(comment.getNickname(), instance)).append(":");
                contentEdit.setHint(hint);
                showInput();
            } else {
               // final Comment c = dynamic.getComments().get(position-1);
                GUIUtil.showDialog(instance, "是否删除这条评论", "删除", "不",
                        new GUIUtil.IOnAlertDListener() {
                    @Override
                    public void ok() {
                        httpDeleteComment(comment);
                    }

                    @Override
                    public void cancel() {  }
                });
            }

        }
    };

    private void initHeadView() {
        width = MyPref.getInstance(instance).getDisplayWidth()
                - GUIUtil.getDpi(instance, R.dimen.margin_50)
                - GUIUtil.getDpi(instance, R.dimen.margin_16);

        headView = LayoutInflater.from(instance).inflate(R.layout.item_dynamic_comment_head, null);
        holder.iconType = (ImageView) headView.findViewById(R.id.activity_dynamic_detail_icontype);
        holder.head = (ImageView) headView.findViewById(R.id.activity_dynamic_detail_head);
        holder.name = (TextView) headView.findViewById(R.id.activity_dynamic_detail_name);
        holder.sign = (TextView) headView.findViewById(R.id.activity_dynamic_detail_sign);
        holder.signlayout = headView.findViewById(R.id.activity_dynamic_detail_signlayout);
        holder.date = (TextView) headView.findViewById(R.id.activity_dynamic_detail_date);
        holder.delete = (TextView) headView.findViewById(R.id.activity_dynamic_detail_delete);
        holder.comment = (ImageView) headView.findViewById(R.id.activity_dynamic_detail_comment);
        holder.like = (ImageView) headView.findViewById(R.id.activity_dynamic_detail_like);
        holder.likelist = (LinearLayout) headView.findViewById(R.id.activity_dynamic_detail_likelist);
        holder.imglist = (LinearLayout) headView.findViewById(R.id.activity_dynamic_detail_imglist);
        holder.commentlayout = (LinearLayout) headView.findViewById(R.id.activity_dynamic_detail_commenlayout);
    }

    private void fillData() {
        fillHeadData();
        comments = dynamic.getComments();
        if(!comments.isEmpty())
            cdynamiccommentid = comments.get(comments.size()-1).getCdynamiccommentid();
        if(!notifyChange) {
            adapter = new DynamicCommentAdapter(instance, comments);
            initAdapter();
        } else {
            notifyAdapter(comments);
        }
    }

    private void fillHeadData() {
        int dynamicType = dynamic.getDynamictype();
        if(dynamicType == 0) {
            holder.iconType.setImageResource(R.drawable.dynamic_seat_type);
        } else if(dynamicType == 1) {
            holder.iconType.setImageResource(R.drawable.dynamic_filmwill_type);
        } else if(dynamicType == 2) {
            holder.iconType.setImageResource(R.drawable.dynamic_sign_type);
        } else {
            holder.iconType.setImageResource(R.drawable.dynamic_sign_type);
        }

        String likeid = dynamic.getLikecdynamicid();
        if(!TextUtil.isEmpty(likeid)) {
            holder.like.setImageResource(R.drawable.liked);
        }
        holder.like.setTag(dynamic);
        holder.like.setOnClickListener(onClickListener);

        holder.comment.setTag(dynamic);
        holder.comment.setOnClickListener(onClickListener);

        fillLikeData(dynamic);

//        List<Like> liks = dynamic.getLikes();
//        if(liks!=null && !liks.isEmpty()) {
//            holder.commentlayout.setVisibility(View.VISIBLE);
//            holder.likelist.setVisibility(View.VISIBLE);
//            holder.likelist.removeAllViews();
//            ImageView image = new ImageView(instance);
//            image.setImageResource(R.drawable.comment);
//            holder.likelist.addView(image);
//            for(Like l : liks) {
//                ImageView img = new ImageView(instance);
//                img.setLayoutParams(new ViewGroup.LayoutParams(48, 48));
//                img.setTag(l);
//                holder.likelist.addView(img);
//                mImageFetcher.loadImage(l.getProfileimg(), img, GUIUtil.getDpi(instance, R.dimen.margin_8));
//            }
//        }

        mImageFetcher.loadImage(dynamic.getProfileimg(), holder.head, GUIUtil.getDpi(instance, R.dimen.margin_8));
        holder.name.setText(TextUtil.getProcessText(dynamic.getNickname(), instance));
        if(!TextUtil.isEmpty(dynamic.getContent())) {
            holder.signlayout.setVisibility(View.VISIBLE);
            holder.sign.setText(TextUtil.getProcessText(dynamic.getContent(), instance));
        }
        holder.date.setText(dynamic.getCreatedate());

        //动态类型 0=一起看电影1=打算去看(喜欢影片)2=自己发表
        int dtype = dynamic.getDynamictype();
        holder.imglist.removeAllViews();
        if(dtype == 0) {
            fillShowimg(dynamic);
        } else if(dtype == 1) {
            fillTrailer(dynamic);
        } else if(dtype == 2) {
            fillImglist(dynamic);
        }

//        String imglistStr = dynamic.getImagelist();
//        if(!TextUtil.isEmpty(imglistStr)) {
//            String[] imglist = imglistStr.split(",");
//            int lenght = imglist.length;
//            LinearLayout l1 = new LinearLayout(instance);
//            l1.setOrientation(l1.HORIZONTAL);
//            if(lenght != 4) {
//                int width = this.width/lenght;
//                for (int i=0; i<imglist.length; i++) {
//                    ImageView img = new ImageView(instance);
//                    img.setLayoutParams(new ViewGroup.LayoutParams(width, width));
//                    l1.addView(img);
//                    mImageFetcher.loadImage(imglist[i], img);
//                }
//                holder.imglist.addView(l1);
//            } else {
//                int width = this.width/2;
//                LinearLayout l2 = new LinearLayout(instance);
//                l2.setOrientation(l2.HORIZONTAL);
//                for (int i=0; i<imglist.length; i++) {
//                    ImageView img = new ImageView(instance);
//                    img.setLayoutParams(new ViewGroup.LayoutParams(width, width));
//                    if(i<=1) {
//                        l1.addView(img);
//                        mImageFetcher.loadImage(imglist[i], img);
//                    } else {
//                        l2.addView(img);
//                        mImageFetcher.loadImage(imglist[i], img);
//                    }
//                }
//                holder.imglist.addView(l1);
//                holder.imglist.addView(l2);
//            }
//        }
    }

    private void fillLikeData(Dynamic dynamic) {
        List<Like> liks = dynamic.getLikes();
        if(liks!=null && !liks.isEmpty()) {
            holder.commentlayout.setVisibility(View.VISIBLE);
            holder.likelist.setVisibility(View.VISIBLE);
            holder.likelist.removeAllViews();
            ImageView image = new ImageView(instance);
            image.setImageResource(R.drawable.comment);
            holder.likelist.addView(image);
            for(Like l : liks) {
                ImageView img = new ImageView(instance);
                img.setLayoutParams(new ViewGroup.LayoutParams(48, 48));
                img.setTag(l);
                holder.likelist.addView(img);
                mImageFetcher.loadImage(l.getProfileimg(), img, GUIUtil.getDpi(instance, R.dimen.margin_8));
            }
        } else {
            holder.commentlayout.setVisibility(View.GONE);
            holder.likelist.setVisibility(View.GONE);
            holder.likelist.removeAllViews();
        }
    }

    private void parseDynamic(String json) throws Exception {
        JSONObject jobj = new JSONObject(json);
        if(jobj.getInt("status") != Const.HTTP_OK) return;

        JSONObject jdata = jobj.getJSONObject("data");
        isnext = jdata.getInt("isnext");
        JSONObject dynObj = jdata.getJSONObject("dynamic");
        dynamic = JSONTool.jsonToBean(Dynamic.class, dynObj);
        List<Like> likes = JSONTool.getJsonToListBean(Like.class,
                jdata.getJSONArray("likedynamic"));
        dynamic.setLikes(likes);
        List<Comment> comments = JSONTool.getJsonToListBean(Comment.class,
                jdata.getJSONArray("comment"));
        dynamic.setComments(comments);
        fillData();
    }

    private class DynamicCommentAdapter extends BaoguBaseAdapter<Comment> {

        private Map<Comment, View> mapView = new HashMap<Comment, View>();
        private LayoutInflater mInflater;

        public DynamicCommentAdapter(Context context, List<Comment> list) {
            super(context, list);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            Comment bean = getItem(position);
            if(mapView.get(bean)!=null){
                return mapView.get(bean) ;
            }
            holderInner = new ViewHolderInner();
            view = mInflater.inflate(R.layout.item_dynamic_comment, null);
            view.setTag(holderInner);
            holderInner.name = (TextView)view.findViewById(R.id.item_dynamic_comment_name);
            holderInner.date = (TextView) view.findViewById(R.id.item_dynamic_comment_date);
            holderInner.head = (ImageView)view.findViewById(R.id.item_dynamic_comment_head);
            holderInner.comment = (TextView)view.findViewById(R.id.item_dynamic_comment_content);
            holderInner.commenttips = (ImageView)view.findViewById(R.id.item_dynamic_comment_icon);
            mapView.put(bean, view);

            mImageFetcher.loadImage(bean.getProfileimg(), holderInner.head);
            holderInner.name.setText(TextUtil.getProcessText(bean.getNickname(), instance));
            holderInner.date.setText(bean.getCreatedate());
            holderInner.comment.setText(TextUtil.getProcessText(bean.getContent(), instance));
            if(position==0) {
                holderInner.commenttips.setVisibility(View.VISIBLE);
            }
            return view;
        }

        private ViewHolderInner holderInner;
        private class ViewHolderInner {
            private ImageView head;
            private TextView comment;
            private ImageView commenttips;
            private TextView name;
            private TextView date;
        }
    }

    private void fillTrailer(Dynamic bean) {
        String front = bean.getFrontcover();
        String filmname = bean.getFilmname();
        String video = bean.getTrailer();
        if(TextUtil.isEmpty(front)) return;
        ImageView img = new ImageView(instance);
        ImageView play = new ImageView(instance);
        play.setImageResource(R.drawable.play);
        play.setTag(video);
        play.setId(R.id.play);
        play.setOnClickListener(onClickListener);

        TextView film = new TextView(instance);
        film.setText("打算去看 ["+filmname+"]");
        RelativeLayout rl = new RelativeLayout(instance);
        rl.addView(img);
        rl.addView(play);
        holder.imglist.setGravity(Gravity.LEFT);
        holder.imglist.addView(film);
        holder.imglist.addView(rl);
        mImageFetcher.loadImage(front, img);
    }

    private void fillShowimg(Dynamic bean) {
        String front = bean.getShowimgurl();
        if(TextUtil.isEmpty(front)) return;
        ImageView img = new ImageView(instance);
        holder.imglist.addView(img);
        mImageFetcher.loadImage(front, img);
    }

    private void fillImglist(Dynamic bean) {
        String imglistStr = bean.getImagelist();
        if(TextUtil.isEmpty(imglistStr)) return;
        String[] imglist = imglistStr.split(",");
        int lenght = imglist.length;
        LinearLayout l1 = new LinearLayout(instance);
        l1.setOrientation(l1.HORIZONTAL);
        if (lenght != 4) {
            int width = this.width / lenght;
            for (int i = 0; i < imglist.length; i++) {
                ImageView img = new ImageView(instance);
                img.setLayoutParams(new ViewGroup.LayoutParams(width, width));
                l1.addView(img);
                mImageFetcher.loadImage(imglist[i], img);
            }
            holder.imglist.addView(l1);
        } else {
            int width = this.width / 2;
            LinearLayout l2 = new LinearLayout(instance);
            l2.setOrientation(l2.HORIZONTAL);
            for (int i = 0; i < imglist.length; i++) {
                ImageView img = new ImageView(instance);
                img.setLayoutParams(new ViewGroup.LayoutParams(width, width));
                if (i <= 1) {
                    l1.addView(img);
                    mImageFetcher.loadImage(imglist[i], img);
                } else {
                    l2.addView(img);
                    mImageFetcher.loadImage(imglist[i], img);
                }
            }
            holder.imglist.addView(l1);
            holder.imglist.addView(l2);
        }
    }

    /*private View.OnClickListener onClickL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.item_dynamic_head:
                    break;
                case R.id.activity_dynamic_detail_like:
                   httpLike((ImageView)v);
                    break;
//                case R.id.item_dynamic_comment:
//                    Intent in = new Intent(instance, DynamicDetailActivity.class);
//                    in.putExtra(DynamicDetailActivity.DYNAMIC_ID, (Dynamic)v.getTag());
//                    instance.startActivityForResult(in, Activity.RESULT_OK);
//                    break;
                case R.id.play:
                    Intent it = new Intent(Intent.ACTION_VIEW);
                    it.setDataAndType(Uri.parse((String) v.getTag()), "video/mp4");
                    startActivity(it);
                    break;

                case R.id.activity_dynamic_detail_comment:

                    break;
                default:break;
            }
        }
    };*/

    private void httpLike(ImageView v) {
        Dynamic bean = (Dynamic)v.getTag();
        if(TextUtil.isEmpty(bean.getLikecdynamicid())) { //已赞,执行取消赞
            if(TextUtil.isEmpty(likecdynamicid)) {
                LogUtil.e(getClass(), "httplike-1");
                v.setImageResource(R.drawable.liked);
                likeParam.setUserid(Const.user.getUserid());
                likeParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
                likeParam.setDevicetype(Const.DEVICE_TYPE);
                likeParam.setCinemadynamicid(bean.getCinemadynamicid());
                request(likeParam);
            } else {
                v.setImageResource(R.drawable.like);
                LogUtil.e(getClass(), "httplike-1-2");
                unLikeParam.setUserid(Const.user.getUserid());
                unLikeParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
                unLikeParam.setDevicetype(Const.DEVICE_TYPE);
                unLikeParam.setLikecdynamicid(likecdynamicid);
                request(unLikeParam);
                likecdynamicid = null;//设置为空
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
            } else {
                LogUtil.e(getClass(), "httplike-2-1");
                v.setImageResource(R.drawable.liked);
                likeParam.setUserid(Const.user.getUserid());
                likeParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
                likeParam.setDevicetype(Const.DEVICE_TYPE);
                likeParam.setCinemadynamicid(bean.getCinemadynamicid());
                request(likeParam);
                likecdynamicid_ = null;
            }

        }
    }

    private class ViewHolder {
        private ImageView iconType;
        private ImageView head;
        private ImageView like;
        private ImageView comment;
        private TextView name;
        private TextView sign;
        private View signlayout;
        private TextView date;
        private TextView delete;
        private LinearLayout imglist;
        private LinearLayout commentlayout;
        private LinearLayout likelist;

    }

    private void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(contentEdit.getWindowToken(), 0);
    }

    private void showInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(contentEdit.getWindowToken(), 1);
    }

    private XListView.IXListViewListener ixl = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            notifyChange = false;
            httpDynamicDetail(dynamic);
        }

        @Override
        public void onLoadMore() {
            notifyChange = true;
            httpDynamicDetailLoadmore(dynamic);
        }
    };

    private void onLoaded() {
        listView.onLoaded();
    }

    private MyXListView.IHasNext ihn = new MyXListView.IHasNext() {
        @Override
        public Integer getHasNext() {
            return isnext;
        }
    };


}
