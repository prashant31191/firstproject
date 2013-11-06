package com.qinnuan.engine.fragment.user;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.activity.user.SettingActivity;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.ImageDetailActivity;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.User;
import com.qinnuan.engine.listener.IUserHomeListener;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_user_home)
public class UserHomeFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_user_home_info)
    View userInfo;
    @InjectView(id = R.id.fragment_user_home_filmticket)
    View filmTicket;
    @InjectView(id = R.id.fragment_user_home_countcash)
    View countCash;
    @InjectView(id = R.id.fragment_user_home_chit)
    View chit;
    @InjectView(id = R.id.fragment_user_home_head)
    ImageView head;
    @InjectView(id = R.id.fragment_user_home_name)
    TextView name;
    @InjectView(id = R.id.fragment_user_home_popid)
    TextView popid;
    @InjectView(id = R.id.fragment_user_home_money)
    TextView money;

    private User user;
    private IUserHomeListener listener;

    public UserHomeFragment() {

    }

    public UserHomeFragment(IUserHomeListener l,User user) {
        listener = l;
        this.user=user;
    }

    @Override
    public void bindDataForUIElement() {
        name.setText(user.getNickname());
        popid.setText("爆谷号: "+user.getPopgogid());
        money.setText("￥"+user.getTotalcashremain());
        listener.setHead(user.getProfileimg(), head);
    }


    @Override
    protected void bindEvent() {
        userInfo.setOnClickListener(onClickListener);
        filmTicket.setOnClickListener(onClickListener);
        countCash.setOnClickListener(onClickListener);
        chit.setOnClickListener(onClickListener);
        head.setOnClickListener(onClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e(getClass(), "==onResume in fragment==");

        if(user!=null){
            bindDataForUIElement();
        }
        if(Const.user!=null) {
            LogUtil.e(getClass(), "totalcash1==>"+Const.user.getTotalcashremain());
            money.setText("￥"+Const.user.getTotalcashremain());
        }
    }

//    public void initData(User user) {
//        LogUtil.e(getClass(), "initData==");
//        this.user = user;
//        listener.setHead(user.getProfileimg(), head);
//        name.setText(user.getNickname());
//        sign.setText(user.getSignature());
//        money.setText("￥"+user.getTotalcashremain());
//
//        userInfo.setOnClickListener(onClickListener);
//        filmTicket.setOnClickListener(onClickListener);
//        countCash.setOnClickListener(onClickListener);
//        chit.setOnClickListener(onClickListener);
//        head.setOnClickListener(onClickListener);
//    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_user_home_info:
                    listener.gotoUserInfoDetail();
                    break;
                case R.id.fragment_user_home_filmticket:
                    listener.gotoUserFilmTicket();
                    break;
                case R.id.fragment_user_home_countcash:
                    listener.gotoCountCash();
                    break;
                case R.id.fragment_user_home_chit:
                    listener.gotoChit();
                    break;
                case R.id.fragment_user_home_head:
                    gotoHeadImg(user.getProfileimg());
                default:
                    break;
            }
        }
    };

    private void gotoHeadImg(String url) {
        Intent i = new Intent(getActivity(), ImageDetailActivity.class);
        i.putExtra("url", url);
        getActivity().startActivity(i);
    }

    @Override
    public void leftClick() {
        startActivity(new Intent(getActivity(), SettingActivity.class));
    }

}
