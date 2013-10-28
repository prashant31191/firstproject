package com.showu.baogu.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.BaseActivity;
import com.showu.baogu.activity.film.OnLineSeatActivity;
import com.showu.baogu.api.GetUserOrderEticket;
import com.showu.baogu.api.GetUserOrderOnline;
import com.showu.baogu.application.Const;
import com.showu.baogu.bean.film.ShowInfoBean;
import com.showu.baogu.bean.user.EticketOrder;
import com.showu.baogu.bean.user.OnlineOrder;
import com.showu.baogu.fragment.film.ShareFragment;
import com.showu.baogu.fragment.set.WebFragment;
import com.showu.baogu.fragment.user.OnlineOrderDetailFragment;
import com.showu.baogu.fragment.user.UserETicketOrderFragment;
import com.showu.baogu.fragment.user.UserOnlineOrderFragment;
import com.showu.baogu.fragment.user.UserOrderFragment;
import com.showu.baogu.listener.IFilmTicketListener;
import com.showu.baogu.util.view.XListView;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.JSONTool;
import com.showu.common.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

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