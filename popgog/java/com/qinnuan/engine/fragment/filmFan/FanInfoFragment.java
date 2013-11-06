package com.qinnuan.engine.fragment.filmFan;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qinnuan.engine.activity.filmFan.FanInfoActivity;
import com.qinnuan.engine.listener.ILoadImgListener;
import com.qinnuan.engine.R;
import com.qinnuan.engine.bean.Dynamic;
import com.qinnuan.engine.bean.Fan;
import com.qinnuan.engine.bean.Sns;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.listener.IFanInfoListener;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.util.TextUtil;

import java.util.List;

@FragmentView(id = R.layout.fragment_fan_info)
public class FanInfoFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_fan_info_sayhilayout)
    View sayhilayout;
    @InjectView(id = R.id.fragment_fan_info_sendmsglayout)
    View sendmsglayout;
    @InjectView(id = R.id.fragment_fan_info_verifylayout)
    View verifylayout;
    @InjectView(id = R.id.fragment_fan_info_head)
    ImageView head;
    @InjectView(id = R.id.fragment_fan_info_sex)
    ImageView sex;
    @InjectView(id = R.id.fragment_fan_info_nick)
    TextView nick;
    @InjectView(id = R.id.fragment_fan_info_sign)
    TextView sign;
    @InjectView(id = R.id.fragment_fan_info_popid)
    TextView popid;
    @InjectView(id = R.id.fragment_fan_info_dist_time)
    TextView dist_time;
    @InjectView(id = R.id.fragment_fan_info_sinaweibo_layout)
    RelativeLayout weibo;
     @InjectView(id = R.id.fragment_fan_info_sinaweibo)
    TextView weibotext;
    @InjectView(id = R.id.fragment_fan_info_dynamic)
    RelativeLayout dynamicLayout;
    @InjectView(id = R.id.fragment_fan_info_imglist)
    LinearLayout imglist;
    @InjectView(id = R.id.fragment_fan_info_verified)
    Button verified;
    @InjectView(id = R.id.fragment_fan_info_forbid)
    Button forbid;
    @InjectView(id = R.id.fragment_fan_info_sayhi)
    Button sayhi;
    @InjectView(id = R.id.fragment_fan_info_report)
    Button report;
    @InjectView(id = R.id.fragment_fan_info_send)
    Button send;

    private Fan fan;
    private ILoadImgListener il;

    private IFanInfoListener listener;
    public FanInfoFragment(Fan fan, IFanInfoListener l) {
        this.fan = fan;
        listener = l;
    }

    @Override
    public void bindDataForUIElement() {
        listener.setHeadImg(fan.getProfileimg(), head);
        fillImgList(fan);

        nick.setText(fan.getNickname());
        sign.setText(fan.getSignature());
        popid.setText(fan.getPopgogid());
        String dis_time_text = TextUtil.getDistance(fan.getDistance()+"")
                            +"km | " +
                          TextUtil.getOffline(fan.getDatediff());
        dist_time.setText(dis_time_text);

        if(fan.getIsfriend()==0 && FanInfoActivity.check_value==-1) {//是否好友 0=否 1=是
            showLayout(LAYOUT_TYPE.SAYHI);
        } else if(fan.getIsfriend()==1) {
            showLayout(LAYOUT_TYPE.SENDMSG);
        } else if(FanInfoActivity.check_value != -1) { //从消息页面跳转来
            showLayout(LAYOUT_TYPE.VERIFY);
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

        if(fan.getSex()==1) {//1=男 2=女 0=未知
            sex.setImageDrawable(getActivity().getResources().
                    getDrawable(R.drawable.sex_male));
        } else if(fan.getSex() == 2) {
            sex.setImageDrawable(getActivity().getResources().
                    getDrawable(R.drawable.sex_female));
        }
    }

    public  void showLayout(LAYOUT_TYPE type) {
        switch (type) {
            case SAYHI:
                sendmsglayout.setVisibility(View.GONE);
                verifylayout.setVisibility(View.GONE);
                sayhilayout.setVisibility(View.VISIBLE);
                break;
            case SENDMSG:
                verifylayout.setVisibility(View.GONE);
                sayhilayout.setVisibility(View.GONE);
                sendmsglayout.setVisibility(View.VISIBLE);
                break;
            case VERIFY:
                sendmsglayout.setVisibility(View.GONE);
                sayhilayout.setVisibility(View.GONE);
                verifylayout.setVisibility(View.VISIBLE);
                break;
            default:break;
        }
    }

    public enum LAYOUT_TYPE {
        SAYHI,
        SENDMSG,
        VERIFY,
    }

    @Override
    protected void bindEvent() {
        weibo.setOnClickListener(onClickListener);
        dynamicLayout.setOnClickListener(onClickListener);
        verified.setOnClickListener(onClickListener);
        forbid.setOnClickListener(onClickListener);
        sayhi.setOnClickListener(onClickListener);
        report.setOnClickListener(onClickListener);
        send.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_fan_info_sinaweibo_layout:
                    listener.gotoWeibo(fan, v);
                break;
                case R.id.fragment_fan_info_dynamic:
                    listener.gotoDynamic(fan, v);
                break;
                case R.id.fragment_fan_info_verified:
                    listener.verified(fan, v);
                break;
                case R.id.fragment_fan_info_forbid:
                    listener.forbid(fan, v);
                break;
                case R.id.fragment_fan_info_sayhi:
                    listener.sayhi();
                break;
                case R.id.fragment_fan_info_report:
                    listener.report(fan, tWidget);
                break;
                case R.id.fragment_fan_info_send:
                    listener.sendMsg(fan, v);
                break;
                default:
                    break;
            }
        }
    };

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
    public void leftClick() {
        getActivity().finish();
    }

    /** 填充相册列表 */
    private void fillImgList(Fan fan) {
        Dynamic dynamic = fan.getDynamic();
        if(dynamic == null) return;
        String[] urls = dynamic.getImagelist().split(",");
//        if(urls == null) {
//            dynamicLayout.setVisibility(View.GONE);
//            return;
//        }
        LogUtil.e(getClass(), urls.length + "");
        if (urls.length < 1 || urls == null) {
            //imglist.setVisibility(View.GONE);
            return;
        }
        imglist.removeAllViews();
        imglist.setGravity(Gravity.CENTER_VERTICAL);
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels / 5 - 10;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
        params.setMargins(3, 0, 0, 0);
        if (urls != null && urls.length > 0) {
            if (urls.length < 4) {
                for (int i = 0; i < urls.length; i++) {
                    ImageView imgv = new ImageView(getActivity());
                    listener.setDynamicImg(urls[i], imgv);
                    imglist.addView(imgv, params);
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    ImageView imgv = new ImageView(getActivity());
                    listener.setDynamicImg(urls[i], imgv);
                    imglist.addView(imgv, params);
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
    }
}