package com.showu.baogu.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.ImageDetailActivity;
import com.showu.baogu.activity.film.OnLineSeatActivity;
import com.showu.baogu.activity.user.DynamicFanActivity;
import com.showu.baogu.application.Const;
import com.showu.baogu.application.MyPref;
import com.showu.baogu.bean.Comment;
import com.showu.baogu.bean.Dynamic;
import com.showu.baogu.bean.Like;
import com.showu.baogu.listener.IOnItemClickListener;
import com.showu.common.http.image.util.ImageFetcher;
import com.showu.common.util.DateUtil;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.ImageUtil;
import com.showu.common.util.LogUtil;
import com.showu.common.util.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by showu on 13-7-25.
 */
public class DynamicAdapter extends BaoguBaseAdapter<Dynamic> {

    private Map<Dynamic, View> mapView = new HashMap<Dynamic, View>();
    private LayoutInflater mInflater;

    ImageFetcher mImageFetcher;
    private IOnItemClickListener iclick;
    private int width = 480;

    public static Dynamic dynimc_delete;
    private List<Dynamic> list;
    private Animation animationroute;

    public void setListener(ImageFetcher l, IOnItemClickListener ic) {
        mImageFetcher = l;
        iclick = ic;
    }

    public DynamicAdapter(Context context, List<Dynamic> list) {
        super(context, list);
        this.list = list;
        animationroute = AnimationUtils.loadAnimation(context, R.anim.route);
        animationroute.setFillAfter(true);
        mInflater = LayoutInflater.from(context);
        width = MyPref.getInstance(context).getDisplayWidth()

                - GUIUtil.getDpi(mContext, R.dimen.margin_90);
    }



    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Dynamic bean = getItem(position);

        if(mapView.get(bean)!=null){
            return mapView.get(bean) ;
        }
        holder = new ViewHolder();
        view = mInflater.inflate(R.layout.item_dynamic, null);
        view.setTag(holder);
        holder.name = (TextView)view.findViewById(R.id.item_dynamic_name);
        holder.sign = (TextView) view.findViewById(R.id.item_dynamic_sign);
        holder.date = (TextView) view.findViewById(R.id.item_dynamic_date);
        holder.head = (ImageView)view.findViewById(R.id.item_dynamic_head);
        holder.like = (ImageView)view.findViewById(R.id.item_dynamic_like);
        holder.delete = (TextView)view.findViewById(R.id.item_dynamic_delete);

        holder.comment = (ImageView)view.findViewById(R.id.item_dynamic_comment);
        holder.imglist = (LinearLayout) view.findViewById(R.id.item_dynamic_imglist);

        holder.commentlayout = (LinearLayout) view.findViewById(R.id.item_dynamic_commentlayout);
        holder.commentlist = (LinearLayout) view.findViewById(R.id.item_dynamic_commentlist);
        holder.likePeoView = view.findViewById(R.id.item_dynamic_likePeoaLayout);
        holder.likePeo = (TextView) view.findViewById(R.id.item_dynamic_likepeople);
        mapView.put(bean, view);

        String likeid = bean.getLikecdynamicid();
        if(!TextUtil.isEmpty(likeid)) {
            holder.like.setImageResource(R.drawable.liked);
        }
        holder.like.setTag(bean);
        holder.like.setTag(R.id.holder, holder);
        holder.like.setOnClickListener(onClickL);

        mImageFetcher.loadImage(bean.getProfileimg(), holder.head,
                GUIUtil.getDpi(mContext, R.dimen.margin_8));
        holder.head.setTag(bean.getUserid());
        holder.head.setOnClickListener(onClickL);

        holder.name.setText(TextUtil.getProcessText(bean.getNickname(), mContext));
        if(!TextUtil.isEmpty(bean.getContent())) {
            holder.sign.setText(TextUtil.getProcessText(bean.getContent(), mContext));
        }

        String dateStr = DateUtil.getDisDate(bean.getCreatedate());
        holder.date.setText(dateStr);

        CItemTag cItemTag = new CItemTag();
        cItemTag.toUserId = bean.getUserid();
        cItemTag.dynId = bean.getCinemadynamicid();
        cItemTag.comments = bean.getComments();

        holder.comment.setTag(cItemTag);
        holder.comment.setTag(R.id.holder, holder);
        holder.comment.setOnClickListener(onClickL);

        if(Const.user.getUserid().equals(bean.getUserid())) {
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setTag(bean);
            holder.delete.setOnClickListener(onClickL);
        }

        //动态类型 0=一起看电影1=打算去看(喜欢影片)2=自己发表
        int dtype = bean.getDynamictype();
        if(dtype == 0) {
            holder.sign.setText("发布了 “一起看电影”");
            fillShowimg(bean);
        } else if(dtype == 1) {
            holder.sign.setText("打算去看 《" + bean.getFilmname() + "》");
            fillTrailer(bean);
        } else if(dtype == 2) {
            fillImglist(bean);
        }

        List<Like> liks = bean.getLikes();
        if(liks!=null && !liks.isEmpty()) {
            fillLike(liks);
        }

        fillComment(bean.getCinemadynamicid(), bean);

        return view;
    }

    private void fillComment(String cdyid, Dynamic bean) {
        List<Comment> comments = bean.getComments();
        if(!comments.isEmpty()) {
            holder.commentlayout.setVisibility(View.VISIBLE);
            holder.commentlist.setVisibility(View.VISIBLE);
            holder.commentlist.removeAllViews();

            for(Comment c : comments) {
                LinearLayout l = new LinearLayout(mContext);
                l.setPadding(8, 6, 8, 6);
                l.setBackgroundDrawable(mContext.getResources().
                        getDrawable(R.drawable.item_white_bg));
                LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT );

                lParams.setMargins(2, 0, 2, 0);
                l.setLayoutParams(lParams);

                TextView textContent = new TextView(mContext);
                TextView textName = new TextView(mContext);
                textName.setTextColor(mContext.getResources().getColor(R.color.fan_list_name));

                if(!TextUtil.isEmpty(c.getUseridto())) {
                    TextView utoName = new TextView(mContext);
                    utoName.setTextColor(mContext.getResources().getColor(R.color.fan_list_name));
                    textName.setText(TextUtil.getProcessText(c.getNickname()+" 回复了 ", mContext));
                    utoName.setText(TextUtil.getProcessText(c.getNicknameto() + ":", mContext));
                    textContent.setText(TextUtil.getProcessText(c.getContent(), mContext));

                    l.addView(textName);
                    l.addView(utoName);
                    l.addView(textContent);

                    if(!Const.user.getUserid().equals(c.getUserid())) {
                        CItemTag ctag = new CItemTag();
                        ctag.toUserId = c.getUseridto();
                        ctag.toUserName = c.getNicknameto();
                        ctag.userId = c.getUserid();
                        ctag.cdynId = cdyid;
                        ctag.comment = c;
                        ctag.comments = comments;
                        l.setTag(R.id.holder, holder);
                        l.setTag(ctag);
                        l.setId(R.id.commenttext);
                        l.setOnClickListener(onClickL);
                    }
                } else {
                    textName.setText(TextUtil.getProcessText(c.getNickname()+":", mContext));
                    textContent.setText(TextUtil.getProcessText(c.getContent(), mContext));
                    l.addView(textName);
                    l.addView(textContent);
                    if(!Const.user.getUserid().equals(c.getUserid())) {
                        CItemTag ctag = new CItemTag();
                        ctag.toUserId = bean.getUserid();
                        ctag.toUserName = c.getNickname();
                        ctag.userId = c.getUserid();
                        ctag.cdynId = cdyid;
                        ctag.comment = c;
                        ctag.comments = comments;
                        l.setTag(R.id.holder, holder);
                        l.setTag(ctag);
                        l.setId(R.id.commenttext);
                        l.setOnClickListener(onClickL);
                    }
                }
                holder.commentlist.addView(l);
            }
        }
    }

    public void fillComment(ViewHolder holder, String cdyid, List<Comment> comments) {
        if(!comments.isEmpty()) {
            LogUtil.e(getClass(), "fillComment run==");
            holder.commentlayout.setVisibility(View.VISIBLE);
            holder.commentlist.setVisibility(View.VISIBLE);
            holder.commentlist.removeAllViews();

            for(Comment c : comments) {
                //CItemTag ctag = new CItemTag();
                LinearLayout l = new LinearLayout(mContext);
                TextView textContent = new TextView(mContext);
                TextView textName = new TextView(mContext);
                textName.setTextColor(mContext.getResources().getColor(R.color.fan_list_name));
                if(!TextUtil.isEmpty(c.getUseridto())) {
                    TextView utoName = new TextView(mContext);
                    utoName.setTextColor(mContext.getResources().getColor(R.color.fan_list_name));

                    textName.setText(TextUtil.getProcessText(c.getNickname()+" 回复 ", mContext));
                    utoName.setText(TextUtil.getProcessText(c.getNicknameto() + ":", mContext));
                    textContent.setText(TextUtil.getProcessText(c.getContent(), mContext));


                    l.addView(textName);
                    l.addView(utoName);
                    l.addView(textContent);

                    if(!Const.user.getUserid().equals(c.getUserid())) {
                        CItemTag ctag = new CItemTag();
                        ctag.toUserId = c.getUseridto();
                        ctag.toUserName = c.getNicknameto();
                        ctag.userId = c.getUserid();
                        ctag.cdynId = cdyid;
                        ctag.comment = c;
                        ctag.comments = comments;
                        l.setTag(R.id.holder, holder);
                        l.setTag(ctag);
                        l.setId(R.id.commenttext);
                        l.setOnClickListener(onClickL);
                    }
                } else {
                    textName.setText(TextUtil.getProcessText(c.getNickname()+":", mContext));
                    textContent.setText(TextUtil.getProcessText(c.getContent(), mContext));

                    l.addView(textName);
                    l.addView(textContent);

                    if(!Const.user.getUserid().equals(c.getUserid())) {
                        CItemTag ctag = new CItemTag();
                        l.setId(R.id.commenttext);
                        ctag.userId = c.getUserid();
                        ctag.cdynId = cdyid;
                        l.setTag(R.id.holder, holder);
                        ctag.toUserName = c.getNickname();
                        ctag.toUserId = c.getUserid();
                        ctag.comment = c;
                        ctag.comments = comments;
                        l.setTag(ctag);
                        l.setOnClickListener(onClickL);
                    }
                }
                holder.commentlist.addView(l);
            }
        }
    }


    private void fillLike(List<Like> liks) {
        holder.likePeo.setText(" ");
        holder.commentlayout.setVisibility(View.VISIBLE);
        holder.likePeoView.setVisibility(View.VISIBLE);
        SpannableStringBuilder sb = new SpannableStringBuilder(" ");

        for (Like l : liks) {
            sb.append(TextUtil.getProcessText(l.getNickname(), mContext));
            sb.append(",");
        }

        holder.likePeo.setText(sb);
    }

    private void fillLike(ViewHolder holder, List<Like> liks) {
        holder.likePeo.setText(" ");
        holder.commentlayout.setVisibility(View.VISIBLE);
        holder.likePeoView.setVisibility(View.VISIBLE);
        SpannableStringBuilder sb = new SpannableStringBuilder(" ");
        for (Like l : liks) {
            sb.append(TextUtil.getProcessText(l.getNickname(), mContext));
            sb.append(",");
        }

        holder.likePeo.setText(sb);
        if(liks!=null && liks.isEmpty()) {
            if(holder.commentlist.getChildCount()<=0) {
                holder.commentlayout.setVisibility(View.GONE);
            }
            holder.likePeoView.setVisibility(View.GONE);
        }
    }

    public void notifyLike(ViewHolder holder, Dynamic bean, boolean isAdd) {
        if(bean.getLikes() == null) bean.setLikes(new ArrayList<Like>());
        if(isAdd) {
            Like like = new Like();
            like.setUserid(Const.user.getUserid());
            like.setNickname(Const.user.getNickname());
            bean.getLikes().add(like);
            fillLike(holder, bean.getLikes());
            LogUtil.e(getClass(), "filllike2===");
        } else {
            for(Iterator<Like> it = bean.getLikes().iterator(); it.hasNext(); ) {
                Like l = it.next();
                if (l.getUserid().equals(Const.user.getUserid())) {
                    it.remove();
                    fillLike(holder, bean.getLikes());
                    LogUtil.e(getClass(), "filllike1===");
                    break;
                }
            }
        }
    }

    private void fillTrailer(Dynamic bean) {
        String front = bean.getFrontcover();
        String filmname = bean.getFilmname();
        String video = bean.getTrailer();
        if(TextUtil.isEmpty(front)) return;
        ImageView img = new ImageView(mContext);
        ImageView play = new ImageView(mContext);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        play.setLayoutParams(rlp);

        play.setImageResource(R.drawable.play_big);
        play.setTag(video);
        play.setId(R.id.play);
        play.setOnClickListener(onClickL);


        RelativeLayout rl = new RelativeLayout(mContext);
        rl.addView(img);
        if(!TextUtil.isEmpty(video)) {
            rl.addView(play);
        }

        holder.imglist.setGravity(Gravity.LEFT);
        holder.imglist.addView(rl);
        mImageFetcher.loadImage(front, img);
    }

    private void fillShowimg(Dynamic bean) {
        View seatview = View.inflate(mContext, R.layout.item_dynamic_seat, null);
        ImageView filmfront = (ImageView) seatview.findViewById(R.id.item_dynamic_seat_front);
      //  filmfront.startAnimation(animationroute);
        TextView filmname = (TextView)seatview.findViewById(R.id.item_dynamic_seat_filmname);
        holder.imglist.addView(seatview);
        mImageFetcher.loadImage(ImageUtil.get2xUrl(bean.getFrontcover()), filmfront);
        mImageFetcher.loadImage(bean.getProfileimg(),
                (ImageView)seatview.findViewById(R.id.item_dynamic_seat_head));
        filmname.setText(bean.getFilmname());
        seatview.setTag(bean);
        seatview.setId(R.id.show_seat);
        seatview.setOnClickListener(onClickL);
//        String front = bean.getShowimgurl();
//        if(TextUtil.isEmpty(front)) return;
//        ImageView img = new ImageView(mContext);
//        img.setTag(bean);
//        img.setId(R.id.show_seat);
//        img.setOnClickListener(onClickL);
//        holder.imglist.addView(img);
//        mImageFetcher.loadImage(front, img);
    }

    private void fillImglist(Dynamic bean) {
        String imglistStr = bean.getImagelist();
        if(TextUtil.isEmpty(imglistStr)) return;
        String[] imglist = imglistStr.split(",");
        int lenght = imglist.length;
        LinearLayout l1 = new LinearLayout(mContext);
        l1.setOrientation(l1.HORIZONTAL);
        if (lenght != 4) {
            if(lenght == 1) {
                for (int i = 0; i < imglist.length; i++) {
                    ImageView img = new ImageView(mContext);
                    sohwImgDetail(img, imglist[i], imglistStr);
                    img.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    img.setScaleType(ImageView.ScaleType.FIT_XY);
                    l1.addView(img);
                    mImageFetcher.loadImage(imglist[i], img);
                }
            } else {
                int width = this.width / lenght;
                for (int i = 0; i < imglist.length; i++) {
                    ImageView img = new ImageView(mContext);
                    img.setPadding(0, 0, 1, 1);
                    sohwImgDetail(img, imglist[i], imglistStr);
                    img.setLayoutParams(new ViewGroup.LayoutParams(width, width));
                    img.setScaleType(ImageView.ScaleType.FIT_XY);
                    l1.addView(img);
                    mImageFetcher.loadImage(imglist[i], img);
                }
            }
            holder.imglist.addView(l1);
        } else {
            int width = this.width / 2;
            LinearLayout l2 = new LinearLayout(mContext);
            l2.setOrientation(l2.HORIZONTAL);
            for (int i = 0; i < imglist.length; i++) {
                ImageView img = new ImageView(mContext);
                img.setPadding(0, 0, 1, 1);
                sohwImgDetail(img, imglist[i], imglistStr);
                img.setLayoutParams(new ViewGroup.LayoutParams(width, width));
                img.setScaleType(ImageView.ScaleType.FIT_XY);
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

    private void sohwImgDetail(ImageView img, final String url, final String urls) {
        img.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               LogUtil.e(getClass(), "url=>"+url+", urls=>"+urls);
               Intent imgIntent = new Intent(mContext, ImageDetailActivity.class);
               imgIntent.putExtra(ImageDetailActivity.EXTRA_RUL, url);
               imgIntent.putExtra(ImageDetailActivity.EXTRA_RULS, urls);
               mContext.startActivity(imgIntent);
           }
       });

    }

    private View.OnClickListener onClickL = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            int id = v.getId();
            switch (id) {
                case R.id.item_dynamic_head: {
                    Intent intent = new Intent(mContext, DynamicFanActivity.class);
                    String userToId = (String)v.getTag();
                    if(Const.user.getUserid().equals(userToId)) {
                        intent.putExtra(DynamicFanActivity.TYPE_ID, DynamicFanActivity.DYNAMIC_MINE);
                    } else {
                        intent.putExtra(DynamicFanActivity.TYPE_ID, DynamicFanActivity.DYNAMIC_OTHER);
                        intent.putExtra(DynamicFanActivity.USER_TO_ID, userToId);
                    }
                    mContext.startActivity(intent);
                }
                break;

                case R.id.item_dynamic_like: {
                    iclick.onClick(v);
                }
                break;

                case R.id.item_dynamic_comment: {
                    iclick.onClick(v);
                }
                break;

                case R.id.play: {
                    Intent it = new Intent(Intent.ACTION_VIEW);
                    it.setDataAndType(Uri.parse((String)v.getTag()), "video/mp4");
                    mContext.startActivity(it);
                }
                break;

                case R.id.show_seat: {
                    Dynamic d = (Dynamic) v.getTag();
                    Intent intent1 = new Intent(mContext, OnLineSeatActivity.class);
                    intent1.putExtra(OnLineSeatActivity.EXT_FILM_TYPE, 6);
                    intent1.putExtra(OnLineSeatActivity.EXT_SEE_FILMID, d.getTogetherseefilmid());
                    intent1.putExtra(OnLineSeatActivity.EXT_UODER_ID, d.getUorderid());
                    mContext.startActivity(intent1);
                }
                break;

                case R.id.item_dynamic_delete: {
                    final Dynamic bean = (Dynamic)v.getTag();

                    notifyDataSetChanged();
                    GUIUtil.showDialog(mContext, "你要删除这条动态?", "删除", "不",
                            new GUIUtil.IOnAlertDListener() {
                                @Override
                                public void ok() {
                                    iclick.onClick(v);
                                    getModelList().remove(bean);
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void cancel() {
                                }
                            });
                }
                break;

                case R.id.commenttext: {
                    iclick.onClick(v);
                    break;
                }

                default:break;
            }

        }
    };

    private ViewHolder holder;

    public class ViewHolder {

        private ImageView head;
        private ImageView like;
        private ImageView comment;
        private TextView name;
        private TextView sign;
        private TextView date;
        private LinearLayout imglist;
        private LinearLayout commentlist;
        private LinearLayout commentlayout;
        private View likePeoView;
        private TextView likePeo;
        private TextView delete;
    }

    public class CItemTag {
        public CItemTag() {}
        public String dynId;
        public String cdynId;
        public String userId;
        public String toUserId;
        public String toUserName;
        public String content;
        public List<Comment> comments;
        public Comment comment;
    }

}
