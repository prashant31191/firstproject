package com.qinnuan.engine.activity.user;

import android.os.Bundle;

import com.qinnuan.engine.fragment.film.ShareFragment;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.film.OnLineSeatActivity;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.film.ShowInfoBean;
import com.qinnuan.engine.bean.user.OnlineOrder;
import com.qinnuan.engine.fragment.set.WebFragment;
import com.qinnuan.engine.fragment.user.OnlineOrderDetailFragment;

public class OnlineOrderDetailActivity extends OnLineSeatActivity {

    private OnlineOrderDetailActivity instance = this;
    public static final String ORDER_DETAIL_ID = "order_detail_id";
    private OnlineOrder order;
     private  OnlineOrderDetailFragment  onlineOrderDetailFragment;
    WebFragment fragment ;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_order_detail);
        order = (OnlineOrder) getIntent().getSerializableExtra(ORDER_DETAIL_ID);
        onlineOrderDetailFragment = new OnlineOrderDetailFragment(order, mImageFetcher);
        addFragment(onlineOrderDetailFragment, false);
    }

    public void howToGetTicket() {
        String url = Const.urlBean.getApp_plugin_url() + "/help/orderhelp";
        fragment = new WebFragment(url, "如何取票");
        replaceFragment(fragment, true);
    }

    public void showShare() {
        ShowInfoBean showInfoBean = new ShowInfoBean();
        showInfoBean.setDimenstional(order.getPdimenstional());
        showInfoBean.setCinemaname(order.getCinemaname());
        showInfoBean.setLanguage(order.getPlanguage());
        showInfoBean.setShowdate(order.getShowdate());
        showInfoBean.setHallname(order.getHallname());
        showInfoBean.setFrontcover(order.getFrontcover());
        ShareFragment shareFragment = new ShareFragment(order.getFilmname(),order.getFrontcover(),showInfoBean,order.getCinemaname(),order.getShowimgurl(),mImageFetcher);
        replaceFragment(shareFragment,true);
    }


}