package com.qinnuan.engine.activity.user;

import android.os.Bundle;

import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.api.GetUserOrderEticket;
import com.qinnuan.engine.api.GetUserOrderOnline;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.user.EticketOrder;
import com.qinnuan.engine.bean.user.OnlineOrder;
import com.qinnuan.engine.bean.user.Qrcode;
import com.qinnuan.engine.fragment.user.UserETicketOrderFragment;
import com.qinnuan.engine.fragment.user.UserOnlineOrderFragment;
import com.qinnuan.engine.fragment.user.UserOrderFragment;
import com.qinnuan.engine.listener.IFilmTicketListener;
import com.qinnuan.engine.util.view.XListView;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserOrderActivity extends BaseActivity {

    private UserOrderActivity instance = this;
    private UserOrderFragment ticketF;
    private UserOnlineOrderFragment onlineF;
    private UserETicketOrderFragment eticketF;

    private int isnext = 0;
    private List<OnlineOrder> onlines;
    private List<EticketOrder> etickets = new ArrayList<EticketOrder>();

    private GetUserOrderEticket eticketParam = new GetUserOrderEticket();
    private GetUserOrderOnline onlineParam = new GetUserOrderOnline();

    private int pageindex = 0;
    private boolean isNotifyAdapter = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gotoTicketPage();
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        try {
            if(url.contains(onlineParam.getApi())) {
                onlineF.onLoaded();
                parseOnline(conent);
            } else if(url.contains(eticketParam.getApi())) {
                eticketF.onLoaded();
                parseEticket(conent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseEticket(String json) throws Exception {
        JSONObject jObj = new JSONObject(json);
        if(Const.HTTP_OK == jObj.getInt("status")) {
            JSONObject jdata = jObj.getJSONObject("data");
            isnext = jdata.getInt("isnext");
            JSONArray jArray = jdata.getJSONArray("eticket");
            for(int i=0; i<jArray.length(); i++) {
                JSONObject jeticket = jArray.getJSONObject(i);
                EticketOrder eorder = JSONTool.jsonToBean(EticketOrder.class, jeticket);
                List<Qrcode> qrcodes = JSONTool.getJsonToListBean(Qrcode.class, jeticket.getJSONArray("qrcode"));
                eorder.setQrcodes(qrcodes);
                etickets.add(eorder);
            }
            if(isNotifyAdapter) {
                eticketF.notifyAdapter(etickets, isnext);
            } else {
                eticketF.initAdapter(etickets, isnext);
                eticketF.setXListener(xlEticket);
            }
        }
    }

    private void parseOnline(String json) throws Exception {
        JSONObject jObj = new JSONObject(json);
        if(Const.HTTP_OK == jObj.getInt("status")) {
            JSONObject jdata = jObj.getJSONObject("data");
            isnext = jdata.getInt("isnext");
            JSONArray jArray = jdata.getJSONArray("onlines");
            onlines = JSONTool.getJsonToListBean(OnlineOrder.class, jArray);
            if(isNotifyAdapter) {
                onlineF.notifyAdapter(onlines, isnext);
            } else {
                onlineF.initAdapter(onlines, isnext);
                onlineF.setXListener(xlOnline);
            }
        }
    }

    private IFilmTicketListener iTicketL = new IFilmTicketListener() {
        @Override
        public void gotoOnlineOreder() {
            gotoOnLineOrderPage();
        }

        @Override
        public void gotoEticketOrder() {
            gotoEticketOrderPage();
        }
    };

    private void gotoTicketPage() {
        ticketF = new UserOrderFragment(iTicketL);
        replaceFragment(ticketF, false);
    }

    private void gotoOnLineOrderPage() {
        isnext = 0;
        isNotifyAdapter = false;
        pageindex = 0;
        onlineF = new UserOnlineOrderFragment();
        replaceFragment(onlineF, true);
        httpOnline();
    }

    private void gotoEticketOrderPage() {
        isnext = 0;
        isNotifyAdapter = false;
        pageindex = 0;
        eticketF = new UserETicketOrderFragment();
        replaceFragment(eticketF, true);
        httpEticket();
    }

    private void httpEticket() {
        eticketParam.setDevicetype(Const.DEVICE_TYPE);
        eticketParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        eticketParam.setUserid(Const.user.getUserid());
        eticketParam.setPageindex(pageindex+"");
        request(eticketParam);
    }

    private void httpOnline() {
        onlineParam.setDevicetype(Const.DEVICE_TYPE);
        onlineParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        onlineParam.setUserid(Const.user.getUserid());
        onlineParam.setPageindex(pageindex+"");
        request(onlineParam);
    }

    private XListView.IXListViewListener xlOnline = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            isNotifyAdapter = false;
            pageindex=0;
            httpOnline();
        }

        @Override
        public void onLoadMore() {
            isNotifyAdapter = true;
            ++pageindex;
            httpOnline();
        }
    };

    private XListView.IXListViewListener xlEticket = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            isNotifyAdapter = false;
            pageindex=0;
            httpEticket();
        }

        @Override
        public void onLoadMore() {
            isNotifyAdapter = true;
            ++pageindex;
            httpEticket();
        }
    };

}