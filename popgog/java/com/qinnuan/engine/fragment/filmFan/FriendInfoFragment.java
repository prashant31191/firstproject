package com.qinnuan.engine.fragment.filmFan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qinnuan.engine.bean.Fan;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.common.util.TextUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.activity.filmFan.MyFriendActivity;
import com.qinnuan.engine.activity.message.ChatActivity;
import com.qinnuan.engine.bean.Dynamic;
import com.qinnuan.engine.bean.Sns;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.listener.IFriendInfoListener;
import com.qinnuan.common.util.LogUtil;

import java.util.List;

@FragmentView(id = R.layout.fragment_friend_info)
public class FriendInfoFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_friend_info_head)
    ImageView head;
    @InjectView(id = R.id.fragment_friend_info_sex)
    ImageView sex;
    @InjectView(id = R.id.fragment_friend_info_nick)
    TextView nick;
    @InjectView(id = R.id.fragment_friend_info_sign)
    TextView sign;
    @InjectView(id = R.id.fragment_friend_info_popid)
    TextView popid;
    @InjectView(id = R.id.fragment_friend_info_dist_time)
    TextView dist_time;
    @InjectView(id = R.id.fragment_friend_info_sinaweibo_layout)
    RelativeLayout weibo;
    @InjectView(id = R.id.fragment_friend_info_sinaweibo)
    TextView weibotext;
    @InjectView(id = R.id.fragment_friend_info_dynamic)
    RelativeLayout dynamicLayout;
    @InjectView(id = R.id.fragment_friend_info_imglist)
    LinearLayout imglist;
    @InjectView(id = R.id.fragment_friend_info_send)
    Button send;
    @InjectView(id = R.id.fragment_friend_info_sayhi)
    Button sayhi;
    @InjectView(id = R.id.fragment_friend_info_report)
    Button report;
    @InjectView(id = R.id.fragment_friend_info_remarkname)
    TextView remarkName;
    @InjectView(id = R.id.fragment_friend_info_sendmsglayout)
    View sendlayout;
    @InjectView(id = R.id.fragment_friend_info_sayhilayout)
    View sayhilayout;

    private Fan fan;

    private IFriendInfoListener listener;
    public FriendInfoFragment(Fan fan, IFriendInfoListener l) {
        this.fan = fan;
        listener = l;
    }

    public FriendInfoFragment() { }

    @Override
    public void bindDataForUIElement() {
        listener.setHeadImg(fan.getProfileimg(), head);
        fillImgList(fan);
        LogUtil.e(getClass(), "nick=>"+fan.getNickname());
//        nick.setText(TextUtil.getProcessText(fan.getNickname(), getActivity()));
//        sign.setText(TextUtil.getProcessText(fan.getSignature(), getActivity()));
        nick.setText(fan.getNickname());
        sign.setText(fan.getSignature());
        popid.setText(fan.getPopgogid());
        String remk = fan.getRemarkname();
        if(!TextUtil.isEmpty(remk))
            remarkName.setText("("+remk+")");
        String dis_time_text = TextUtil.getDistance(fan.getDistance()+"")
                            +"km | " +
                          TextUtil.getOffline(fan.getDatediff());
        dist_time.setText(dis_time_text);
        if(fan.getSex()==1) {//1=男 2=女 0=未知
            sex.setImageDrawable(getActivity().getResources().
                    getDrawable(R.drawable.sex_male));
        } else if(fan.getSex() == 2) {
            sex.setImageDrawable(getActivity().getResources().
                    getDrawable(R.drawable.sex_female));
        }

        List<Sns> snss = fan.getSnss();
        if(snss!=null && !snss.isEmpty()) {
            weibo.setVisibility(View.VISIBLE);
            weibotext.setText(snss.get(0).getSnsname());
        }

        List<Dynamic> dys = fan.getDynamics();
        if(dys!=null && !dys.isEmpty()) {
            dynamicLayout.setVisibility(View.VISIBLE);
        }

//        if(fan.getUsertype() == 2) {
//            initTitle();
//        }
    }

    public void initTitle() {
        tWidget.getRightL().setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!TextUtil.isEmpty(MyFriendActivity.remarkStr)) {
            remarkName.setText("("+MyFriendActivity.remarkStr+")");
        }
        int isFriend = fan.getIsfriend();
        if(isFriend == 1) {//好友
            showSayHiLayout(1);
        } else if(isFriend == 0) {//非好友
            showSayHiLayout(2);
        }
        //showSayHiLayout(MyFriendActivity.type);
        LogUtil.e(getClass(), "type==>"+fan.getUsertype());
        if(fan.getUsertype()==2 || fan.getUsertype()==4) {
            initTitle();
        }
    }

    @Override
    protected void bindEvent() {
        weibo.setOnClickListener(onClickListener);
        dynamicLayout.setOnClickListener(onClickListener);
        send.setOnClickListener(onClickListener);
        sayhi.setOnClickListener(onClickListener);
        report.setOnClickListener(onClickListener);
    }

    public void updateRemark(String remark) {
        LogUtil.e(getClass(), "remar==>"+remark);
        if(!TextUtil.isEmpty(remark)) {

            remarkName.setText("("+remark+")");
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_friend_info_sinaweibo_layout:
                    listener.gotoWeibo(fan, v);
                break;
                case R.id.fragment_friend_info_dynamic:
                    listener.gotoDynamic(fan, v);
                break;
                case R.id.fragment_friend_info_send:
                    listener.sendMsg(fan, v);
                break;
                case R.id.fragment_friend_info_sayhi:
                    listener.sayhi();
                    break;
                case R.id.fragment_friend_info_report:
                    listener.report(fan, tWidget);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 1=show sendlayout</br>
     * 2=show sayhilayout
     */
    public void showSayHiLayout(int type) {
        if(type == 1) {
            sendlayout.setVisibility(View.VISIBLE);
            sayhilayout.setVisibility(View.GONE);
            tWidget.getRightL().setVisibility(View.VISIBLE);
        } else if(type == 2) {
            sendlayout.setVisibility(View.GONE);
            sayhilayout.setVisibility(View.VISIBLE);
            tWidget.getRightL().setVisibility(View.GONE);
        }
    }


    private String getText(TextView text) {
        String texttmp = text.getText().toString();
        if(!TextUtil.isEmpty(texttmp)) {
            texttmp = texttmp.trim();
        } else {
            texttmp = "";
        }
        return texttmp;
    }

    public void setHeadTmp(Bitmap bitmap) {
        if(bitmap != null)
            head.setImageBitmap(bitmap);
    }

    public ImageView getheadImgv() {
        return head;
    }

    @Override
    public void rightClick() {
        listener.showPop(tWidget);
    }

//    @Override
//    public void back() {
//        if(TextUtil.isEmpty(MyFriendActivity.fanid)) {
//            super.back();
//        } else {
//            getActivity().finish();
//        }
//    }

    @Override
    public void leftClick() {
        if(TextUtil.isEmpty(MyFriendActivity.fanid)) {
            super.back();
        } else {
            if (MyFriendActivity.isDeleteAFriend) {
                Intent intent = new Intent();
                intent.putExtra(ChatActivity.ACTIVITY_NEED_BACK, 1);
                getActivity().setResult(getActivity().RESULT_OK, intent);
            }
            getActivity().finish();
        }
    }

    /** 填充相册列表 */
    private void fillImgList(Fan fan) {
        List<Dynamic> dys = fan.getDynamics();
        //List<Sns> snss = fan.getSnss();
        //LogUtil.e(getClass(), "dys size=>"+dys.size());
        if(dys==null) return;
        imglist.removeAllViews();
        imglist.setGravity(Gravity.CENTER_VERTICAL);
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels / 5 - 10;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
        params.setMargins(3, 0, 0, 0);
        for (Dynamic dy : dys) {
            ImageView imgv = new ImageView(getActivity());
            imgv.setTag(dy);
            String imgUrl = dy.getImagelist();
            if(!TextUtil.isEmpty(imgUrl)) {
                LogUtil.e(getClass(), "imgUrl=>"+imgUrl);
                listener.setDynamicImg(imgUrl, imgv);
                imglist.addView(imgv, params);
            }

        }
//                PressImageView fground = (PressImageView) inflater.inflate(R.layout.film_detail_text, null);
//                TextView allImage = (TextView)fground.findViewById(R.id.film_detail_text_text);
//                allImage.setText(urls.length + "");
//                allImage.setGravity(Gravity.CENTER);
//                allImage.setTextColor(Color.BLACK);
//                FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(width, width);
//                p.setMargins(15, 0, 0, 0);
//                allImage.setLayoutParams(p);
//
//                fground.setTag(filmDetail.getImagelist());
//                LayoutParams fgroundparams = new LayoutParams(
//                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//                fgroundparams.setMargins(3, 0, 0, 0);
//                fground.setLayoutParams(fgroundparams);
//                imglist.addView(fground);
//                fground.setOnClickListener(onClickListener);

    }
}