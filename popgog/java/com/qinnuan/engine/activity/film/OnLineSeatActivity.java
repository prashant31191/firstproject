package com.qinnuan.engine.activity.film;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.qinnuan.engine.adapter.CinemaAdapter;
import com.qinnuan.engine.api.film.AddOrder;
import com.qinnuan.engine.api.film.DeleteOrderOnline;
import com.qinnuan.engine.api.film.GetCinemaShowlist;
import com.qinnuan.engine.api.film.GetOrderStatus;
import com.qinnuan.engine.api.film.GetSeatlist;
import com.qinnuan.engine.api.film.GetShowinfo;
import com.qinnuan.engine.api.film.GetTogetherseefilmShowinfo;
import com.qinnuan.engine.api.film.UpdateCouponStatus;
import com.qinnuan.engine.api.film.UpdateOrderconfirm;
import com.qinnuan.engine.bean.film.CrashBean;
import com.qinnuan.engine.bean.film.OrderBean;
import com.qinnuan.engine.bean.film.Seat;
import com.qinnuan.engine.bean.film.ShowInfoBean;
import com.qinnuan.engine.fragment.film.CinemaFragment;
import com.qinnuan.engine.fragment.film.MovieSchedulingFragment;
import com.qinnuan.engine.fragment.film.SeatFragment;
import com.qinnuan.engine.fragment.film.ShareFragment;
import com.qinnuan.engine.pay.UpompPay;
import com.qinnuan.common.share.WXShare;
import com.qinnuan.common.util.FileUtil;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.CinemaBean;
import com.qinnuan.engine.fragment.film.OnlineOrderFragment;
import com.qinnuan.engine.fragment.film.PayChoiceFragment;
import com.qinnuan.engine.fragment.film.PayOnlineFragment;
import com.qinnuan.engine.fragment.film.PaySuccessFragment;
import com.qinnuan.engine.fragment.film.SeeSeatFragment;
import com.qinnuan.common.util.ImageUtil;
import com.qinnuan.common.util.PayUtil;
import com.upomp.pay.info.Upomp_Pay_Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Created by showu on 13-7-25.
 */
public class OnLineSeatActivity extends BaseActivity {
    public static String EXT_FILM_ID = "film_id";
    public static String EXT_FILM_NAME = "film_name";
    public static String EXT_FILM_TYPE = "order_type";
    public static String EXT_FILM_IMAGE = "film_image";
    public static String EXT_UODER_ID = "uoderid";
    public static String EXT_SEE_FILMID = "togetherseefilmid";
    public static String EXT_IS_SHOW = "isshow";
    //Http param
    private GetCinemaShowlist getCinemaShowlist = new GetCinemaShowlist();
    private GetShowinfo getShowinfo = new GetShowinfo();
    private GetSeatlist getSeatlist = new GetSeatlist();
    private AddOrder addOrder = new AddOrder();
    private UpdateOrderconfirm updateOrderconfirm = new UpdateOrderconfirm();
    private GetTogetherseefilmShowinfo getTogetherseefilmShowinfo = new GetTogetherseefilmShowinfo();
    private DeleteOrderOnline deleteOrderOnline = new DeleteOrderOnline();
    private UpdateCouponStatus updateCouponStatus = new UpdateCouponStatus();
    private GetOrderStatus getOrderStatus = new GetOrderStatus();
    //fragment
    private CinemaFragment cinemaFragment;
    private MovieSchedulingFragment movieSchedulingFragment;
    private SeatFragment seatFragment;
    private OnlineOrderFragment onlineOrderFragment;
    private PayChoiceFragment payChoiceFragment;
    private PaySuccessFragment paySuccessFragment;
    private PayOnlineFragment payOnlineFragment;
    private ShareFragment shareFragment;
    private SeeSeatFragment seeSeatFragment;

    private CinemaAdapter adapter;
    private String filmId;
    private String filmName;
    private List<CinemaBean> cinemaBeans;
    private List<ShowInfoBean> showInfoBeans;
    private List<Seat> seats;
    private List<Seat> choiceSeat;
    private String cinemaName;
    private ShowInfoBean currentShowinfo;
    private OrderBean orderBean;
    private String phoneNumber;
    private boolean isOk = false;
    private String shareImageUrl;
    private int isShow = 1;
    private File shareFile;
    private Bitmap shareBitmap;
    private int payType = -1;
    //Intent data .
    private int orderType = 0;
    private String imageUrl;
    private String uoderId;
    private String togetherseefilmid;

    private float price = 0.0f;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filmId = getIntent().getStringExtra(EXT_FILM_ID);
        filmName = getIntent().getStringExtra(EXT_FILM_NAME);
        orderType = getIntent().getIntExtra(EXT_FILM_TYPE, 0);
        isShow = getIntent().getIntExtra(EXT_IS_SHOW, 1);
        imageUrl = getIntent().getStringExtra(EXT_FILM_IMAGE);
        if (imageUrl != null) {
            imageUrl = ImageUtil.get2xUrl(imageUrl);
            LogUtil.e(getClass(), "imageURL=" + imageUrl);
        }
        uoderId = getIntent().getStringExtra(EXT_UODER_ID);
        togetherseefilmid = getIntent().getStringExtra(EXT_SEE_FILMID);
        if (TextUtil.isEmpty(uoderId)) {
            cinemaFragment = new CinemaFragment(filmName);
            addFragment(cinemaFragment, false);
            getCinema();
        } else {
            seeSeatFragment = new SeeSeatFragment(mImageFetcher);
            addFragment(seeSeatFragment, false);
            getSeeShowuInfo();
        }
    }

    private void getCinema() {
        getCinemaShowlist.setFilmid(filmId);
        getCinemaShowlist.setUserid(Const.user.getUserid());
        getCinemaShowlist.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        getCinemaShowlist.setLatitude(Const.point.getGeoLat().toString());
        getCinemaShowlist.setLogincityid(Const.city.getCityid());
        getCinemaShowlist.setLongitude(Const.point.getGeoLng().toString());
        request(getCinemaShowlist);
    }

    public void deletOrder() {
        deleteOrderOnline.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        deleteOrderOnline.setUserid(Const.user.getUserid());
        deleteOrderOnline.setUorderid(orderBean.getUorderid());
        request(deleteOrderOnline);
    }

    private void getSeeShowuInfo() {
        getTogetherseefilmShowinfo.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        getTogetherseefilmShowinfo.setUserid(Const.user.getUserid());
        getTogetherseefilmShowinfo.setUorderid(uoderId);
        request(getTogetherseefilmShowinfo);
    }

    private void getMoveSchedu(String cinemaId) {
        getShowinfo.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        getShowinfo.setFilmid(filmId);
        getShowinfo.setUserid(Const.user.getUserid());
        getShowinfo.setCinemaid(cinemaId);
        request(getShowinfo);
    }

    private void getSeatList(String showuInfoId) {
        getSeatlist.setUserid(Const.user.getUserid());
        getSeatlist.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        getSeatlist.setShowinfoid(showuInfoId);
        request(getSeatlist);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (isOk) {
                finish();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        try {
            if (url.contains(getCinemaShowlist.getApi())) {
                parseCinema(conent);
            } else if (url.contains(getShowinfo.getApi())) {
                parseShowInfo(conent);
            } else if (url.contains(getSeatlist.getApi())) {
                parseSeatList(conent);
            } else if (url.contains(addOrder.getApi())) {
                dimissDialog();
                needDialog = true;
                parseOrder(conent);
            } else if (url.contains(updateOrderconfirm.getApi())) {
                parseUpdateConfire(conent);
            } else if (url.contains(getTogetherseefilmShowinfo.getApi())) {
                parseSeesShowInfo(conent);
            } else if (url.contains(deleteOrderOnline.getApi())) {
                parseDeleteOrder(conent);
            } else if (url.contains(updateCouponStatus.getApi())) {
                parseUpdateCoupon(conent);
            } else if (url.contains(getOrderStatus.getApi())) {
                parseGetOrder(conent);
            } else {//上传图片
                shareImageUrl = parseImage(conent);
                addSeeOrder();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseGetOrder(String conent) throws JSONException {
        JSONObject jsonObject = new JSONObject(conent);
        int status = jsonObject.getInt("status");
        if (status == 1) {
            int orderstatus = jsonObject.getJSONObject("data").getInt("orderstatus");
            if (orderstatus != 0) {//正在支付
                buyTickSuccess();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    private void parseUpdateCoupon(String conent) throws JSONException {
        JSONObject jsonObject = new JSONObject(conent);
        int status = jsonObject.getInt("status");
        if (status == 1) {
            doPay();
        } else {
            GUIUtil.toast(this, "锁定代金券失败");
        }
    }

    private void parseDeleteOrder(String conent) throws JSONException {
        JSONObject jsonObject = new JSONObject(conent);
        int status = jsonObject.getInt("status");
        if (status == Const.HTTP_OK) {
            getSupportFragmentManager().popBackStack();
        } else {
            GUIUtil.toast(this, "取消订单失败");
        }
    }

    public void buyTickSuccess() {
        isOk = true;
//        if (orderType != 6) {
//            paySuccessFragment = new PaySuccessFragment(phoneNumber, filmName, cinemaName, currentShowinfo, orderBean, choiceSeat);
//            replaceFragment(paySuccessFragment, true);
//        } else {
        shareFragment = new ShareFragment(filmName, imageUrl, currentShowinfo, cinemaName, shareFile, mImageFetcher);
        replaceFragment(shareFragment, true);
//        }
        if (payType == PayChoiceFragment.PAYT_ACCOUNT) {
            price = PayChoiceFragment.realPrice;
            Const.user.setTotalcashremain(Const.user.getTotalcashremain() - price);
        }
    }

    private void parseCinema(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        int status = jsonObject.getInt("status");
        if (status == Const.HTTP_OK) {
            JSONArray cinemaArray = jsonObject.getJSONObject("data").getJSONArray("cinemas");
            cinemaBeans = JSONTool.getJsonToListBean(CinemaBean.class, cinemaArray);
            adapter = new CinemaAdapter(this, cinemaBeans);
            cinemaFragment.setAdapter(adapter);
        }
    }

    private void parseShowInfo(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        int status = jsonObject.getInt("status");
        if (status == Const.HTTP_OK) {
            JSONArray cinemaArray = jsonObject.getJSONObject("data").getJSONArray("showinfo");
            showInfoBeans = JSONTool.getJsonToListBean(ShowInfoBean.class, cinemaArray);
            movieSchedulingFragment.initShowuInfo(showInfoBeans);
        }else{
            GUIUtil.toast(this,"获取场次失败");
        }
    }

    private void parseSeatList(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        int status = jsonObject.getInt("status");
        if (status == Const.HTTP_OK) {
            JSONArray cinemaArray = jsonObject.getJSONObject("data").getJSONArray("section");
            JSONArray seatArray = cinemaArray.getJSONObject(0).getJSONArray("seat");
            seats = JSONTool.getJsonToListBean(Seat.class, seatArray);
            seatFragment.initSeat(seats);
        }else{
            GUIUtil.toast(this,"获取座位列表失败");
        }
    }

    private void parseOrder(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        int status = jsonObject.getInt("status");
        if (status == Const.HTTP_OK) {
            orderBean = JSONTool.jsonToBean(OrderBean.class, jsonObject.getJSONObject("data"));
            List<CrashBean> list = JSONTool.getJsonToListBean(CrashBean.class, jsonObject.getJSONObject("data").getJSONArray("ucashcoupon"));
            orderBean.setCrashBeanList(list);
            payChoiceFragment = new PayChoiceFragment(choiceSeat, filmName, cinemaName, currentShowinfo, orderBean);
            replaceFragment(payChoiceFragment, true);
        } else {
            GUIUtil.toast(this, "订单生成失败，请稍后再试");
        }
    }

    private void parseUpdateConfire(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        int status = jsonObject.getInt("status");
        if (status == Const.HTTP_OK) {
            buyTickSuccess();
        } else if (status == 13) {
            GUIUtil.toast(this, "代金券不可用!");
        } else {
            GUIUtil.toast(this, "支付失败!");
        }
    }

    private void parseSeesShowInfo(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        int status = jsonObject.getInt("status");
        if (status == Const.HTTP_OK) {
            currentShowinfo = JSONTool.jsonToBean(ShowInfoBean.class, jsonObject.getJSONObject("data"));
            cinemaName = currentShowinfo.getCinemaname();
            filmName = currentShowinfo.getFilmname();
            if (currentShowinfo.getFrontcover() != null) {
                imageUrl = ImageUtil.get2xUrl(currentShowinfo.getFrontcover());
            }
            seats = JSONTool.getJsonToListBean(Seat.class, jsonObject.getJSONObject("data").getJSONArray("section").getJSONObject(0).getJSONArray("seat"));
            seeSeatFragment.initSeat(currentShowinfo, seats);
        }
    }

    public void onCinemaItemClick(int postion) {
        cinemaName = cinemaBeans.get(postion).getCinemaname();
        movieSchedulingFragment = new MovieSchedulingFragment(filmName, cinemaBeans.get(postion));
        replaceFragment(movieSchedulingFragment, true);
        getMoveSchedu(cinemaBeans.get(postion).getCinemaid());
    }

    public void onSchedulingItemClick(ShowInfoBean showInfoBean) {
        currentShowinfo = showInfoBean;
        seatFragment = new SeatFragment(filmName, showInfoBean);
        replaceFragment(seatFragment, true);
        getSeatList(showInfoBean.getShowinfoid());
    }

    public void suerSeat(List<Seat> seats) {
        this.choiceSeat = seats;
        if (seatFragment != null) {
            shareFile = seatFragment.getImage();
        } else {
            shareFile = seeSeatFragment.getImage();
        }
        onlineOrderFragment = new OnlineOrderFragment(currentShowinfo, seats, filmName, cinemaName);
        replaceFragment(onlineOrderFragment, true);
    }

    public void addOrder(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        uploadFile("3", shareFile);
        showDialog();
        needDialog = false;
//        if (orderType != 6) {//不是创建一起看电影
//            addOrder.setShowinfoid(currentShowinfo.getShowinfoid());
//            addOrder.setUserid(Const.user.getUserid());
//            addOrder.setDeviceidentifyid(GUIUtil.getDeviceId(this));
//            addOrder.setPhone(phoneNumber);
//            addOrder.setTicketcount(choiceSeat.size());
//            StringBuilder sb = new StringBuilder();
//            for (Seat s : choiceSeat) {
//                sb.append(s.getRowid() + ":" + s.getColumnid()).append("|");
//            }
//            addOrder.setSeatlist(sb.substring(0, sb.length() - 1));
//            addOrder.setOrdertype(orderType);
//            float totalPrice = Float.parseFloat(currentShowinfo.getPrice()) * choiceSeat.size();
//            addOrder.setTotalprice(totalPrice + "");
//            addOrder.setShowimgurl(shareImageUrl);
//            addOrder.setTogetherseefilmid(togetherseefilmid);
//            request(addOrder);
//        } else {
//            uploadFile("3", shareFile);
//        }
    }

    public void addSeeOrder() {
        addOrder.setShowinfoid(currentShowinfo.getShowinfoid());
        addOrder.setUserid(Const.user.getUserid());
        addOrder.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        addOrder.setPhone(phoneNumber);
        addOrder.setTicketcount(choiceSeat.size());
        addOrder.setIsshow(isShow);
        StringBuilder sb = new StringBuilder();
        for (Seat s : choiceSeat) {
            sb.append(s.getRowid() + ":" + s.getColumnid()).append("|");
        }
        addOrder.setSeatlist(sb.substring(0, sb.length() - 1));
        addOrder.setOrdertype(6);
        float totalPrice = Float.parseFloat(currentShowinfo.getPrice()) * choiceSeat.size();
        addOrder.setTotalprice(totalPrice + "");
        addOrder.setShowimgurl(shareImageUrl);
        addOrder.setTogetherseefilmid(togetherseefilmid);
        request(addOrder);
    }

    public void confireOrder(int type, String crashId) {
        this.payType = type;
        if (!TextUtil.isEmpty(crashId)) {
//            price = PayChoiceFragment.realPrice;
            updateCouponStatus.setDeviceidentifyid(GUIUtil.getDeviceId(this));
            updateCouponStatus.setUorderid(orderBean.getUorderid());
            updateCouponStatus.setUserid(Const.user.getUserid());
            updateCouponStatus.setUcashcouponid(crashId);
            request(updateCouponStatus);
        } else {
            doPay();
        }
    }

    private void doPay() {
        if (payType == PayChoiceFragment.PAYT_ACCOUNT) {
            float cashRemain = Float.parseFloat(orderBean.getTotalcashremain());
            if(cashRemain < PayChoiceFragment.realPrice) {
                GUIUtil.toast(this, "你的爆谷余额不足,请选择其他支付方式或者进行充值!");
            } else {
                updateOrderconfirm.setPayment(payType);
                updateOrderconfirm.setUorderid(orderBean.getUorderid());
                request(updateOrderconfirm);
            }
        } else if (payType == PayChoiceFragment.PAYT_CLINET) {
            new PayUtil(this, payListener)
                    .payByClient(getSubject(), getBody(), orderBean.getUorderid(), PayChoiceFragment.realPrice+""
                            , Const.urlBean.getAlipay_app_url());
//            new PayUtil(this, payListener)
//                    .payByClient(getSubject(), getBody(), orderBean.getUorderid(), "0.01"
//                            , Const.urlBean.getAlipay_app_url());
        } else if (payType == PayChoiceFragment.PAYT_UNITION) {
            new UpompPay(this).upomPayClient(
                    Const.urlBean.getJson_api_url(), orderBean.getUorderid());
        } else if (payType == PayChoiceFragment.PAYT_ONLINE) {
            payOnlineFragment = new PayOnlineFragment(getSubject(), PayChoiceFragment.realPrice, orderBean.getUorderid());
            replaceFragment(payOnlineFragment, true);
        }
    }


    public void share() {
        shareFragment = new ShareFragment(filmName, imageUrl, currentShowinfo, cinemaName, shareFile, mImageFetcher);
        replaceFragment(shareFragment, true);
    }

    public void shareFriend() {
        if (WXShare.getInstance(this).isWXInstalled()) {
//            GUIUtil.toast(this,"shareFriend-->"+);
            WXShare.getInstance(this).shareImg(FileUtil.getCachDir(this) + File.separator + "1.jpg");
        } else {
            GUIUtil.toast(this, "请先安装微信客户端");
        }
    }

    private String getSubject() {
        StringBuffer subjectBuffer = new StringBuffer();
        subjectBuffer.append(cinemaName
                + filmName + "X"
                + choiceSeat.size());
        return subjectBuffer.toString();
    }

    private String getBody() {
        StringBuffer bodyBuffer = new StringBuffer();
        for (Seat seat : choiceSeat) {
            bodyBuffer.append(seat.getRowid() + "排" + seat.getColumnid()
                    + "座      ");
        }
        return bodyBuffer.toString();
    }

    private PayUtil.PayListener payListener = new PayUtil.PayListener() {
        @Override
        public void paySuccess() {
            buyTickSuccess();
        }

        @Override
        public void payFail() {
            // TODO Auto-generated method stub
            GUIUtil.toast(OnLineSeatActivity.this, R.string.pay_fail);
        }
    };

    public void getOrderStatus() {
        getOrderStatus.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        getOrderStatus.setUserid(Const.user.getUserid());
        getOrderStatus.setUorderid(orderBean.getUorderid());
        request(getOrderStatus);
    }
}
