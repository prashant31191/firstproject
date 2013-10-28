package com.showu.baogu.activity.film;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.showu.baogu.R;
import com.showu.baogu.activity.BaseActivity;
import com.showu.baogu.api.film.AddOrderTicket;
import com.showu.baogu.api.film.GetCinemaEticket;
import com.showu.baogu.api.film.UpdateCouponStatus;
import com.showu.baogu.api.film.UpdateOrderconfirm;
import com.showu.baogu.application.Const;
import com.showu.baogu.bean.film.EcinemaBean;
import com.showu.baogu.bean.film.EticketListBean;
import com.showu.baogu.bean.film.EtitckBean;
import com.showu.baogu.bean.film.Seat;
import com.showu.baogu.bean.film.TickitOrderBean;
import com.showu.baogu.fragment.film.EtickitFragment;
import com.showu.baogu.fragment.film.EticktOrderFragment;
import com.showu.baogu.fragment.film.PayChoiceFragment;
import com.showu.baogu.fragment.film.PayOnlineEticketFragment;
import com.showu.baogu.fragment.film.PayOnlineFragment;
import com.showu.baogu.fragment.film.PayTicktSuccessFragment;
import com.showu.baogu.fragment.film.TickitPayChoiceFragment;
import com.showu.baogu.fragment.set.WebFragment;
import com.showu.baogu.pay.UpompPay;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;
import com.showu.common.util.PayUtil;
import com.showu.common.util.TextUtil;
import com.upomp.pay.info.Upomp_Pay_Info;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by showu on 13-8-8.
 */
public class EtickitActivity extends BaseActivity implements EticktOrderFragment.IAddOrderTickt {
    public static String EXT_MECHENT_ID = "id";
    public String id;
    private EtickitFragment etickitFragment;
    private EticktOrderFragment eticktOrderFragment;
    protected TickitPayChoiceFragment tickitPayChoiceFragment;
    private PayTicktSuccessFragment payTicktSuccessFragment;
    private GetCinemaEticket getCinemaEticket = new GetCinemaEticket();
    private AddOrderTicket addOrderTicket = new AddOrderTicket();
    private UpdateCouponStatus updateCouponStatus = new UpdateCouponStatus();
    private Gson gson = new Gson();
    private EticketListBean listBean;
    protected EtitckBean currentEticktBean;
    UpdateOrderconfirm updateOrderconfirm = new UpdateOrderconfirm();
    protected TickitOrderBean tickitOrderBean;
    private PayOnlineEticketFragment payOnlineFragment;
    protected String phoneNumber;
    protected int account = 0;
    private int payType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra(EXT_MECHENT_ID);
        etickitFragment = new EtickitFragment();
        addFragment(etickitFragment, false);
        getEticket();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            byte[] xml = bundle.getByteArray("xml");
            // 自行解析并输出
            String Sxml;
            try {
                Sxml = new String(xml, "utf-8");
                LogUtil.d(getClass(), "这是支付成功后，回调返回的报文，需自行解析" + Sxml);
                if (Sxml.indexOf("<respCode>0000</respCode>") > 0) {
                    buyTickSuccess();
                } else {
                    GUIUtil.toast(this, "银联付款失败");
                    ;
                }
            } catch (Exception e) {
                Log.d(Upomp_Pay_Info.tag, "Exception is " + e);
            }

        } else {
            Log.d(Upomp_Pay_Info.tag, "data is null");
        }
    }

    protected void parseUpdateConfire(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        int status = jsonObject.getInt("status");
        if (status == Const.HTTP_OK) {
            buyTickSuccess();
        }
    }

    private void buyTickSuccess() {
        Float totalPrice = currentEticktBean.getPrice() * account;
        Float acountPrice = Const.user.getTotalcashremain();
        if(acountPrice>=totalPrice) {
            Const.user.setTotalcashremain(acountPrice-totalPrice);
        }
        payTicktSuccessFragment = new PayTicktSuccessFragment(phoneNumber, currentEticktBean, tickitOrderBean, totalPrice);
        replaceFragment(payTicktSuccessFragment, true);
    }

    private void getEticket() {
        getCinemaEticket.setUserid(Const.user.getUserid());
        getCinemaEticket.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        getCinemaEticket.setLogincityid(Const.city.getCityid());
        getCinemaEticket.setUseridto(id);
        request(getCinemaEticket);
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        try {
            JSONObject jsonObject = new JSONObject(conent);
            if (url.contains(getCinemaEticket.getApi())) {
                parseTicket(jsonObject);
            } else if (url.contains(addOrderTicket.getApi())) {
                parseAddticket(conent);
            } else if (url.contains(updateOrderconfirm.getApi())) {
                parseUpdateConfire(conent);
            }else if(url.contains(updateCouponStatus.getApi())){
                parseUpdateCouponStatus(conent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void parseUpdateCouponStatus(String json) throws JSONException {
        JSONObject conent = new JSONObject(json);
        int status = conent.getInt("status");
        if (status == 1) {
            doPay();
        }else{
            GUIUtil.toast(this,"使用代金券失败!");
        }
    }

    protected void parseAddticket(String json) throws JSONException {
        JSONObject conent = new JSONObject(json);
        int status = conent.getInt("status");
        if (status == 1) {
            tickitOrderBean = gson.fromJson(conent.getJSONObject("data").toString(), TickitOrderBean.class);
            tickitPayChoiceFragment = new TickitPayChoiceFragment(tickitOrderBean, currentEticktBean.getParentCinema().getCinemaname(),
                    currentEticktBean.getValiddate(), currentEticktBean.getTickettype(),account, currentEticktBean.getPrice(), tickitOrderBean.getTotalcashremain(), this);
            replaceFragment(tickitPayChoiceFragment, true);
        }
    }

    private void parseTicket(JSONObject jsonObject) throws JSONException {
        int status = jsonObject.getInt("status");
        if (status == 1) {
            listBean = gson.fromJson(jsonObject.getJSONObject("data").toString(), EticketListBean.class);
            etickitFragment.setAdapter(listBean.getCinemas());
        }
    }

    public void buyTickt(EtitckBean bean) {
        currentEticktBean = bean;
        eticktOrderFragment = new EticktOrderFragment(bean.getParentCinema().getCinemaname(), bean.getValiddate(),
                bean.getPrice(), bean.getTickettype(), bean.getMaxbuynum(), this);
        replaceFragment(eticktOrderFragment, true);
    }

    public void confireOrder(int type, String crashId) {
        this.payType=type;
        if (TextUtil.isEmpty(crashId)) {
            doPay();
        } else {
            updateCouponStatus.setDeviceidentifyid(GUIUtil.getDeviceId(this));
            updateCouponStatus.setUorderid(tickitOrderBean.getUorderid());
            updateCouponStatus.setUserid(Const.user.getUserid());
            updateCouponStatus.setUcashcouponid(crashId);
            request(updateCouponStatus);
        }
    }

    private void doPay() {
        if (payType == PayChoiceFragment.PAYT_ACCOUNT) {
            float cashRemain = tickitOrderBean.getTotalcashremain();
            if(cashRemain < PayChoiceFragment.realPrice) {
                GUIUtil.toast(this, "你的爆谷余额不足,请选择其他支付方式或者进行充值!");
            } else {
                updateOrderconfirm.setPayment(payType);
                updateOrderconfirm.setUorderid(tickitOrderBean.getUorderid());
                request(updateOrderconfirm);
            }
//            updateOrderconfirm.setPayment(payType);
//            updateOrderconfirm.setUorderid(tickitOrderBean.getUorderid());
//            request(updateOrderconfirm);
        } else if (payType == PayChoiceFragment.PAYT_CLINET) {
            new PayUtil(this, payListener).payByClient(getSubject(), getBody(),
                    tickitOrderBean.getUorderid(), TickitPayChoiceFragment.newPrice+""
                            , Const.urlBean.getAlipay_app_url());
        } else if (payType == PayChoiceFragment.PAYT_UNITION) {
            new UpompPay(this).upomPayClient(Const.urlBean.getJson_api_url(),
                    tickitOrderBean.getUorderid());
        } else if (payType == PayChoiceFragment.PAYT_ONLINE) {
            payOnlineFragment = new PayOnlineEticketFragment(getSubject(), TickitPayChoiceFragment.newPrice, tickitOrderBean.getUorderid());
            replaceFragment(payOnlineFragment, true);
        }
    }

    protected String getSubject() {
        StringBuffer subjectBuffer = new StringBuffer();

        subjectBuffer.append(currentEticktBean.getParentCinema().getCinemaname()
                + currentEticktBean.getValiddate()
        );
        return subjectBuffer.toString();
    }

    protected String getBody() {
        StringBuffer subjectBuffer = new StringBuffer();

        subjectBuffer.append(currentEticktBean.getParentCinema().getCinemaname()
                + currentEticktBean.getValiddate()
        );

        return subjectBuffer.toString();
    }

    public void addEticktOrder(String phoneNumber, int count) {
        this.phoneNumber = phoneNumber;
        this.account = count;
        addOrderTicket.setUserid(Const.user.getUserid());
        addOrderTicket.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        addOrderTicket.setCinemaeticketid(currentEticktBean.getCinemaeticketid());
        addOrderTicket.setPhone(phoneNumber);
        addOrderTicket.setTicketcount(count);
        addOrderTicket.setTotalprice(count * currentEticktBean.getPrice());
        request(addOrderTicket);
    }

    public  void showHelpWap(){
        String url = Const.urlBean.getApp_plugin_url()+"/help/orderhelp" ;
        WebFragment fragment = new WebFragment(url,"如何取票") ;
        replaceFragment(fragment,true);
    }

    private PayUtil.PayListener payListener = new PayUtil.PayListener() {
        @Override
        public void paySuccess() {
            buyTickSuccess();
        }

        @Override
        public void payFail() {
            // TODO Auto-generated method stub
            GUIUtil.toast(EtickitActivity.this, R.string.pay_fail);
        }
    };

}
