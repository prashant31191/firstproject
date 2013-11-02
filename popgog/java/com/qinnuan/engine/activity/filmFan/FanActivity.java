package com.qinnuan.engine.activity.filmFan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qinnuan.engine.api.GetOtheruserinfo;
import com.qinnuan.engine.bean.Fan;
import com.qinnuan.engine.fragment.filmFan.FanInfoFragment;
import com.qinnuan.engine.util.view.MyXListView;
import com.qinnuan.common.util.GUIUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.api.UpdateIsshow;
import com.qinnuan.engine.listener.IOnItemClickListener;
import com.qinnuan.engine.api.GetNearbyFans;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.fragment.filmFan.FanFragment;
import com.qinnuan.engine.util.view.XListView;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.widget.BottomPopWindow;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class FanActivity extends BaseActivity {

    private FanActivity instance = this;

    private List<Fan> fans = new ArrayList<Fan>();
    private GetNearbyFans fansParam = new GetNearbyFans();
    private GetOtheruserinfo userInfoParam = new GetOtheruserinfo();
    private UpdateIsshow isHowParam = new UpdateIsshow();

    private FanFragment fanF;
    private FanInfoFragment fanInfoF;
    private MyXListView xlisv;

    private final int SEX_NULL = 0;
    private final int SEX_MALE = 1;
    private final int SEX_FEMALE = 2;
    private int current_sex = SEX_NULL;
    private Integer isnext = 0;

    private int pageIndex = 0;
    private boolean notifyChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_bg);
        gotoFansPage();
        httpRefreshFnasBySex(current_sex);
    }

    @Override
    protected void requestFailed() {
        super.requestFailed();
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        try {
            if(url.contains(fansParam.getApi())) {
                parseFans(conent);
                xlisv.onLoaded();
            } else if(url.contains(userInfoParam.getApi())) {
                parseFanInfo(conent);
            } else if(url.contains(isHowParam.getApi())) {
                Const.user.setIsshow(0); //isshow  是否出现 0=否 1=是
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gotoFansPage() {
        fanF = new FanFragment(mImageFetcher);
        replaceFragment(fanF, false);
       // fanF.textlistv();
    }

//    private ILoadImgListener fanL = new ILoadImgListener() {
//        @Override
//        public void load(String url, ImageView img) {
//            mImageFetcher.loadImage(url, img, GUIUtil.getDpi(instance, R.dimen.margin_8));
//        }
//    };

    private void httpRefreshFnasBySex(int sex) {
        notifyChange = false;
        pageIndex = 0;
        httpFansBySex(sex);
    }

    private void httpFansBySex(int sex) {
        fansParam.setUserid(Const.user.getUserid());
        fansParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        fansParam.setDevicetype(Const.DEVICE_TYPE);
        fansParam.setLatitude(Const.point.getGeoLat()+"");
        fansParam.setLongitude(Const.point.getGeoLng()+"");
        if(sex != SEX_NULL) {
            fansParam.setSex(sex+"");
        } else {
            fansParam.setSex(null);
        }
        fansParam.setPageindex(pageIndex+"");
        request(fansParam);
    }

    private void parseFans(String json) throws Exception {
        JSONObject jobj = new JSONObject(json);
        if (Const.HTTP_OK == jobj.getInt("status")) {
            JSONObject jdata = jobj.getJSONObject("data");
            isnext = jdata.getInt("isnext");
            List<Fan> fansTmp = JSONTool.getJsonToListBean(Fan.class,
                    jdata.getJSONArray("nearbyfans"));
            if (!notifyChange) {
                fans.clear();
                fans = fansTmp;
                LogUtil.e(getClass(), "fans size in activity parse=>"+fans.size());
                initAdapter();
            } else {
                notifyAdapter(fansTmp);
            }
            LogUtil.e(getClass(), "fans size=>" + fans.size());
        }
    }

    private void notifyAdapter(List<Fan> fans) {
        if(isnext == 0) {
            fanF.removeFooter();
        }
        fanF.notifyAdapter(fans);
    }

    private void parseFanInfo(String json) throws  Exception {
        JSONObject jobj = new JSONObject(json);
        if (Const.HTTP_OK == jobj.getInt("status")) {

            JSONObject jdata = jobj.getJSONObject("data");
            JSONObject jFan = jdata.getJSONObject("users");
            //JSONArray jSnsArray = jdata.getJSONArray("sns");
            //JSONArray jDynArray = jdata.getJSONArray("dynamic");
            Fan fan = JSONTool.jsonToBean(Fan.class, jFan);
            //List<Sns> snss = JSONTool.getJsonToListBean(Sns.class, jSnsArray);
            //List<Dynamic> dynamics = JSONTool.getJsonToListBean(Dynamic.class, jDynArray);
            //fan.setSnss(snss);
            //fan.setDynamics(dynamics);
            LogUtil.e(getClass(), "fans=>" + fan);
            gotoFanInfo(fan);
        }
    }

    private void httpFanInfo(Fan fan) {
        userInfoParam.setUserid(Const.user.getUserid());
        userInfoParam.setLatitude(Const.point.getGeoLat()+"");
        userInfoParam.setLongitude(Const.point.getGeoLng()+"");
        userInfoParam.setDevicetype(Const.DEVICE_TYPE);
        userInfoParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        userInfoParam.setOtheruserid(fan.getUserid());
        request(userInfoParam);
    }

    private void initAdapter() {
        xlisv = fanF.getXListV();
        xlisv.setXListViewListener(XListener);
        xlisv.setIHasNext(iIHasNext);
        LogUtil.e(getClass(), "fans size in activity=>"+fans.size());
        fanF.initAdapter(fans);
        LogUtil.e(getClass(), "fans size in activity1=>"+fans.size());
        fanF.setIOnClick(iOnItemL);
        fanF.setRightClick(iOnRightClickL);
    }

    private MyXListView.IHasNext iIHasNext = new MyXListView.IHasNext() {
        @Override
        public Integer getHasNext() {
            return isnext;
        }
    };


    private IOnItemClickListener iOnItemL = new IOnItemClickListener() {
        @Override
        public void onClick(Object obj) {
            //用户类型0=普通用户1=枪手用户2=官方/系统用户3=公共帐号
            Fan fan = (Fan)obj;
            int type = fan.getUsertype();
            if(type == 3) {
                Intent in = new Intent(instance, PublicInfoActivity.class);
                in.putExtra(PublicInfoActivity.PUBLIC_FAN_ID, fan.getUserid());
                startActivityForResult(in, RESULT_OK);
            } else {
                Intent i = new Intent(instance, FanInfoActivity.class);
                i.putExtra(FanInfoActivity.FAN_ID, fan.getUserid());
                startActivity(i);
                //httpFanInfo(fan);
            }
        }
    };

    private IOnItemClickListener iOnRightClickL = new IOnItemClickListener() {
        @Override
        public void onClick(Object obj) {
            showBySex((View)obj);
        }

        private void showBySex(View view) {
            String[] items = new String[] { "只看女生", "只看男生", "查看全部", "清除位置并退出" };
            showButtomPop(onMenuSelect, view, items);
        }

        private BottomPopWindow.OnMenuSelect onMenuSelect = new BottomPopWindow.OnMenuSelect() {
            @Override
            public void onItemMenuSelect(int position) {
                buttomPop.dismiss();
                switch (position) {
                    case 0 :
                        current_sex = SEX_FEMALE;
                        httpRefreshFnasBySex(current_sex);
                        break;
                    case 1 :
                        current_sex = SEX_MALE;
                        httpRefreshFnasBySex(current_sex);
                        break;
                    case 2 :
                        current_sex = SEX_NULL;
                        httpRefreshFnasBySex(current_sex);
                        break;
                    case 3 :
                        GUIUtil.showDialog(instance,
                                "清除位置,周围的人看不到你,确定清除吗?",
                                "清除", "不要", l );
                        break;
                    default :
                        break;
                }
            }

            @Override
            public void onCancelSelect() {
                buttomPop.dismiss();
            }

            GUIUtil.IOnAlertDListener l = new GUIUtil.IOnAlertDListener() {
                @Override
                public void ok() {
                    //isshow  是否出现 0=否 1=是
                    isHowParam.setIsshow("0");
                    isHowParam.setUserid(Const.user.getUserid());
                    isHowParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
                    isHowParam.setDevicetype(Const.DEVICE_TYPE);
                    request(isHowParam);
                }
                @Override
                public void cancel() {

                }
            };
        };

    };

    private void gotoFanInfo(Fan fan) {
        Intent i = new Intent(this, FanInfoActivity.class);
        i.putExtra(FanInfoActivity.FAN_ID, fan.getUserid());
        startActivity(i);
    }

    private XListView.IXListViewListener XListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            notifyChange = false;
            pageIndex = 0;
            httpFansBySex(current_sex);

           // httpRefreshFnasBySex(current_sex);
        }

        @Override
        public void onLoadMore() {
            pageIndex++;
            notifyChange = true;
            httpFansBySex(current_sex);
        }
    };

}