package com.qinnuan.engine.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.qinnuan.engine.api.film.UpdateOrderconfirm;
import com.qinnuan.engine.api.user.AddUserCashremain;
import com.qinnuan.engine.bean.Cash;
import com.qinnuan.engine.fragment.user.AccountRechargeFragment;
import com.qinnuan.engine.fragment.user.RechargeChoiceFragment;
import com.qinnuan.engine.fragment.user.RechargeFinishFragment;
import com.qinnuan.engine.fragment.user.RechargeOnlineFragment;
import com.qinnuan.engine.fragment.user.UserCashFragment;
import com.qinnuan.engine.listener.IOnItemClickListener;
import com.qinnuan.engine.pay.UpompPay;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.LogUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.api.user.GetUserCashlog;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.util.view.XListView;
import com.qinnuan.common.util.PayUtil;
import com.upomp.pay.info.Upomp_Pay_Info;

import org.json.JSONObject;

import java.util.List;

/**
 * 
 * @author cand_tyliang
 * @create	2013-03-11
 * @modify 2013-05-04
 */
public class UserAccountActivity extends BaseActivity {

    private UserAccountActivity instance = this;

	private GetUserCashlog cashlogParam = new GetUserCashlog();
    private AddUserCashremain addCashParam = new AddUserCashremain();

    private UpdateOrderconfirm updateOrderconfirm = new UpdateOrderconfirm();

    private UserCashFragment userCashF;
    private AccountRechargeFragment rechargeF;
    private RechargeOnlineFragment rechargeOnlineF;
    private RechargeChoiceFragment rechargeChoiceF;
    private RechargeFinishFragment rechargeFinishF;

    private List<Cash> cashs;
    private int isnext;
    private String uorderid;
    private String money;

    //private String totalPrice = "0.01";
    private String totalPrice;
    private boolean isSuccess = false;
    private boolean isNotifyChange = false;
    private int pageindex = 0;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        gotoCashsPage();
        httpGetCashs();
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
//                    buyTickSuccess();
                } else {
                    GUIUtil.toast(this, "银联付款失败");
                }
            } catch (Exception e) {
                Log.d(Upomp_Pay_Info.tag, "Exception is " + e);
            }
        } else {
            Log.d(Upomp_Pay_Info.tag, "data is null");
        }
    }

    @Override
    protected void requestSuccess(String url, String json) {
        try {
            if(url.contains(cashlogParam.getApi())){
                parseCashs(json);
            } else if (url.contains(addCashParam.getApi())) {
                parseAddCash(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        userCashF.onLoaded();
    }

    private void httpGetCashs() {
        cashlogParam.setUserid(Const.user.getUserid());
        cashlogParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        cashlogParam.setDevicetype(Const.DEVICE_TYPE);
        cashlogParam.setPageindex(pageindex+"");
        request(cashlogParam);
    }

    private void httpRecharge(String money) {
        addCashParam.setDevicetype(Const.DEVICE_TYPE);
        addCashParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        addCashParam.setUserid(Const.user.getUserid());
        addCashParam.setCashremain(money);
        request(addCashParam);
    }

    private void parseCashs(String josn) throws Exception {
        JSONObject jobj = new JSONObject(josn);
        JSONObject jdata = jobj.getJSONObject("data");
        isnext = jdata.getInt("isnext");
        cashs = JSONTool.getJsonToListBean(Cash.class, jdata.getJSONArray("cash"));
        userCashF.setIXL(ixl);
        if(isNotifyChange) {
            userCashF.notifyAdapter(cashs, isnext);
        } else {
            userCashF.setAdapter(cashs, isnext);
        }
    }

    private void parseAddCash(String json) throws Exception {
        JSONObject jobj = new JSONObject(json);
        JSONObject jdata = jobj.getJSONObject("data");
        uorderid = jdata.getString("uorderid");
        LogUtil.e(getClass(), "uorderid=>"+uorderid);
        gotoSelectPage();
    }

    private IOnItemClickListener iUserCashL = new IOnItemClickListener() {
        @Override
        public void onClick(Object obj) {
            gotoRechargePage();
        }
    };

    private IOnItemClickListener iRechargeL = new IOnItemClickListener() {
        @Override
        public void onClick(Object obj) {
            money = (String)obj;
            totalPrice = money;
            httpRecharge(money);
        }
    };

    private IOnItemClickListener iRechargeSelectL = new IOnItemClickListener() {
        @Override
        public void onClick(Object obj) {
            int type = (Integer)obj;
            submitOrder(type);
        }
    };

    private IOnItemClickListener iRechargeFinishL = new IOnItemClickListener() {
        @Override
        public void onClick(Object obj) {

        }
    };

    private void gotoCashsPage() {
        userCashF = new UserCashFragment(iUserCashL);
        replaceFragment(userCashF, false);
    }

    private void gotoRechargePage() {
        rechargeF = new AccountRechargeFragment(iRechargeL);
        replaceFragment(rechargeF, true);
    }

    private void gotoSelectPage() {
        rechargeChoiceF = new RechargeChoiceFragment(money, iRechargeSelectL);
        replaceFragment(rechargeChoiceF, true);
    }

    private void gotoRechargeFinishPage() {
        rechargeFinishF = new RechargeFinishFragment(totalPrice, isSuccess, iRechargeFinishL);
        replaceFragment(rechargeFinishF, true);
    }

    public void submitOrder(int type) {
        if (type == RechargeChoiceFragment.PAYT_CLINET) {// 支付宝client端
            new PayUtil(this, payListener)
                    .payByClient(getSubject(), getBody(), uorderid, totalPrice,
                     Const.urlBean.getAlipay_app_url());
        } else if (type == RechargeChoiceFragment.PAYT_UNITION) {// 银联在线支付
            new UpompPay(this)
                    .upomPayClient(Const.urlBean.getJson_api_url(), uorderid);
        } else if (type == RechargeChoiceFragment.PAYT_ONLINE) { //支付宝在线支付
            rechargeOnlineF = new RechargeOnlineFragment(getSubject(),
                    totalPrice, uorderid);
            replaceFragment(rechargeOnlineF, true);
        }
    }

    private String getSubject() {
        StringBuffer subjectBuffer = new StringBuffer();
        subjectBuffer.append("本次充值 ").append("￥ ").append(money);
        return subjectBuffer.toString();
    }

    private String getBody() {
        StringBuffer bodyBuffer = new StringBuffer();
        bodyBuffer.append("￥").append(money);
        return bodyBuffer.toString();
    }

    private PayUtil.PayListener payListener = new PayUtil.PayListener() {
        @Override
        public void paySuccess() {
            buyTickSuccess();
        }

        @Override
        public void payFail() {
            GUIUtil.toast(instance, R.string.pay_fail);
            isSuccess = false;
            gotoRechargeFinishPage();
        }
    };

    private void buyTickSuccess() {

    }

    private XListView.IXListViewListener ixl = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            isNotifyChange = false;
            pageindex = 0;
            httpGetCashs();
        }

        @Override
        public void onLoadMore() {
            isNotifyChange = true;
            ++pageindex;
            httpGetCashs();
        }
    };
}
