package com.qinnuan.engine.activity.film;

import android.os.Bundle;

import com.google.gson.Gson;
import com.qinnuan.engine.api.film.AddOrderTicketActivity;
import com.qinnuan.engine.api.film.GetActivityDetail;
import com.qinnuan.engine.bean.film.ActivityEticktBean;
import com.qinnuan.engine.bean.film.TickitOrderBean;
import com.qinnuan.engine.fragment.film.ActivityDetailFragment;
import com.qinnuan.engine.fragment.film.EticktOrderFragment;
import com.qinnuan.engine.fragment.film.TickitPayChoiceFragment;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.film.AcitivityBean;
import com.qinnuan.engine.bean.film.EcinemaBean;
import com.qinnuan.engine.bean.film.EtitckBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by showu on 13-8-14.
 */
public class ActivityActivity extends EtickitActivity {
    public static final String EXT_URL = "url";
    public static final String EXT_TITLE = "title";
    public static final String EXT_ID = "id";
    private ActivityDetailFragment activityDetailFragment;
    private String url;
    private String title;
    private String activityId;
    private AcitivityBean acitivityBean;
    private GetActivityDetail getActivityDetail = new GetActivityDetail();
    private AddOrderTicketActivity addOrderTicketActivity = new AddOrderTicketActivity();
    private Gson gson;
    private EticktOrderFragment eticktOrderFragment;
    private ActivityEticktBean currentActivityEticktBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra(EXT_URL);
        title = getIntent().getStringExtra(EXT_TITLE);
        activityId = getIntent().getStringExtra(EXT_ID);
        activityDetailFragment = new ActivityDetailFragment(url, title);
        addFragment(activityDetailFragment, false);
        gson = new Gson();
        getActivityDetail();
    }


    @Override
    protected void requestSuccess(String url, String conent) {
        try {
            if (url.contains(getActivityDetail.getApi())) {
                parseActivityDetail(conent);
            }else if(url.contains(addOrderTicketActivity.getApi())){
                parseAddticket(conent);
            }else if(url.contains(updateOrderconfirm.getApi())){
                parseUpdateConfire(conent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseActivityDetail(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        int status = jsonObject.getInt("status");
        if (status == 1) {
            acitivityBean = gson.fromJson(jsonObject.getJSONObject("data").toString(), AcitivityBean.class);
            if(acitivityBean.getStatus()==1){
                activityDetailFragment.canBuyTickit();
            }
        }
    }

    public void showPop(){
        activityDetailFragment.showJoinPop(acitivityBean);
    }

    protected void parseAddticket(String json) throws JSONException {
        JSONObject conent = new JSONObject(json);
        int status = conent.getInt("status");
        if (status == 1) {
            tickitOrderBean = gson.fromJson(conent.getJSONObject("data").toString(), TickitOrderBean.class);
            tickitPayChoiceFragment = new TickitPayChoiceFragment(tickitOrderBean, currentActivityEticktBean.getCinemaname(),
                    currentActivityEticktBean.getValiddate(), currentActivityEticktBean.getTickettype(),account, currentActivityEticktBean.getPrice(), tickitOrderBean.getTotalcashremain(),this);
            replaceFragment(tickitPayChoiceFragment, true);
        }
    }

    public void getActivityDetail() {
        getActivityDetail.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        getActivityDetail.setUserid(Const.user.getUserid());
        getActivityDetail.setOfficialactivityid(activityId);
        request(getActivityDetail);
    }

    @Override
    protected String getSubject() {
        StringBuffer subjectBuffer = new StringBuffer();

        subjectBuffer.append(currentActivityEticktBean.getCinemaname()
                + currentActivityEticktBean.getValiddate()
        );
        return subjectBuffer.toString();
    }

    @Override
    protected String getBody() {
        StringBuffer subjectBuffer = new StringBuffer();

        subjectBuffer.append(currentActivityEticktBean.getCinemaname()
                + currentActivityEticktBean.getValiddate()
        );

        return subjectBuffer.toString();
    }
    public void buyTickit(ActivityEticktBean bean) {
        this.currentActivityEticktBean = bean;
        EtitckBean etitckBean= new EtitckBean();
        etitckBean.setPrice(bean.getPrice());
        etitckBean.setMaxbuynum(bean.getMaxbuynum());
        etitckBean.setTickettype(bean.getTickettype());
        etitckBean.setValiddate(bean.getValiddate());
        etitckBean.setCinemaeticketid(bean.getActeticcinemaid());
        EcinemaBean ecinemaBean = new EcinemaBean();
        ecinemaBean.setCinemaname(bean.getCinemaname());
        ecinemaBean.setCinemaid(bean.getActeticcinemaid());
        etitckBean.setParentCinema(ecinemaBean);
        this.currentEticktBean=etitckBean;
        activityDetailFragment.dismissPop();
        eticktOrderFragment = new EticktOrderFragment(bean.getCinemaname(), bean.getValiddate(), bean.getPrice(), bean.getTickettype(),
                bean.getMaxbuynum(), this);
        replaceFragment(eticktOrderFragment, true);
    }

    @Override
    public void addEticktOrder(String phoneNumber, int account) {
        this.account=account;
        this.phoneNumber=phoneNumber;
        addOrderTicketActivity.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        addOrderTicketActivity.setOfficialactivityid(acitivityBean.getOfficialactivityid());
        addOrderTicketActivity.setUserid(Const.user.getUserid());
        addOrderTicketActivity.setActeticcinemaid(currentActivityEticktBean.getActeticcinemaid());
        addOrderTicketActivity.setPhone(phoneNumber);
        addOrderTicketActivity.setTicketcount(account);
        addOrderTicketActivity.setTotalprice(currentActivityEticktBean.getPrice() * account);
        addOrderTicketActivity.setTicketcount(account);
        request(addOrderTicketActivity);
    }
}
