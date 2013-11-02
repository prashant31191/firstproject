package com.qinnuan.engine.activity.filmFan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.qinnuan.engine.fragment.filmFan.FanInfoFragment;
import com.qinnuan.engine.fragment.filmFan.FriendSearchFragment;
import com.qinnuan.engine.listener.ILoadImgListener;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.api.GetSearchUser;
import com.qinnuan.engine.fragment.filmFan.FriendSearchResultFragment;
import com.qinnuan.engine.listener.ISearchFanListener;
import com.qinnuan.engine.util.view.XListView;
import com.qinnuan.engine.api.GetOtheruserinfo;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.Fan;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.LogUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class FriendAddActivity extends BaseActivity {

    private FriendAddActivity instance = this;

    private List<Fan> fans;
    private GetSearchUser searchParam = new GetSearchUser();
    private GetOtheruserinfo userInfoParam = new GetOtheruserinfo();

    private FriendSearchFragment searchF;

    private FriendSearchResultFragment fanF;
    private FanInfoFragment fanInfoF;

    private Integer isnext = 0;

    private int pageIndex = 0;
    private boolean notifyChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i(getClass(), "onCreate activity======");
        gotoSearch();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void requestFailed() {
        super.requestFailed();
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        try {
            if(url.contains(userInfoParam.getApi())) {
                parseFanInfo(conent);
            } else if(url.contains(searchParam.getApi())) {
                gotoFansPage();
                parseFans(conent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fanF.onLoaded();
    }

    private void gotoFansPage() {
        if(fanF == null) {
            fanF = new FriendSearchResultFragment(mImageFetcher);
            replaceFragment(fanF, false);
        } else {
            //searchF.back();
        }
    }

    private ILoadImgListener fanL = new ILoadImgListener() {
        @Override
        public void load(String url, ImageView img) {
            mImageFetcher.loadImage(url, img);
        }
    };

    private void parseFans(String json) throws Exception {
        LogUtil.e(getClass(), "parseFans==>");
        JSONObject jobj = new JSONObject(json);
        if (Const.HTTP_OK == jobj.getInt("status")) {
            JSONObject jdata = jobj.getJSONObject("data");
            isnext = jdata.getInt("isnext");
            fans = JSONTool.getJsonToListBean(Fan.class, jdata.getJSONArray("search"));
            if (!notifyChange) {
                LogUtil.e(getClass(), "fanF1==>"+fanF);
                //fanF.initAdapter(isnext, fans);
                fanF.setData(isnext, fans);
                fanF.setIXListener(xListener);
                fanF.setIOnClick(iOnListItemClick);
                LogUtil.e(getClass(), "fanF3==>"+fanF);
            } else {
                LogUtil.e(getClass(), "fanF2==>"+fanF);
                fanF.notifyAdapter(isnext, fans);
                fanF.setIXListener(xListener);
                fanF.setIOnClick(iOnListItemClick);
            }
            LogUtil.e(getClass(), "fans size=>" + fans.size());
        }
    }

    private void parseFanInfo(String json) throws  Exception {
        JSONObject jobj = new JSONObject(json);
        if (Const.HTTP_OK == jobj.getInt("status")) {
            JSONObject jdata = jobj.getJSONObject("data");
            Fan fan = JSONTool.jsonToBean(Fan.class, jdata.getJSONObject("users"));
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

    private FriendSearchResultFragment.IOnListVItemClick iOnListItemClick = new FriendSearchResultFragment.IOnListVItemClick() {
        @Override
        public void onItemClick(Fan fan) {
            httpFanInfo(fan);
        }
    };

    private void gotoFanInfo(Fan fan) {
        Intent i ;
        if(fan.getUsertype() == 3) {
            i = new Intent(this, PublicInfoActivity.class);
            i.putExtra(PublicInfoActivity.PUBLIC_FAN_ID, fan.getUserid());
        } else {
            i = new Intent(this, FanInfoActivity.class);
            i.putExtra(FanInfoActivity.FAN_ID, fan.getUserid());
        }

        startActivity(i);
    }

    private void gotoSearch() {
        LogUtil.e(getClass(), "gotoSearch befor=====");
        searchF = new FriendSearchFragment(searchL);
        replaceFragment(searchF, false);
        LogUtil.e(getClass(), "gotoSearch after=====");
    }

    private String keyword;
    private ISearchFanListener searchL = new ISearchFanListener() {
        @Override
        public void search(String content) {
            keyword = content;
            httpSearch(content);
        }
    };

    private void httpSearch(String content) {
        searchParam.setDevicetype(Const.DEVICE_TYPE);
        searchParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        searchParam.setUserid(Const.user.getUserid());
        searchParam.setKeyword(content);
        searchParam.setLatitude(Const.point.getGeoLat()+"");
        searchParam.setLongitude(Const.point.getGeoLng()+"");
        searchParam.setPageindex(pageIndex+"");
        request(searchParam);
    }

    private XListView.IXListViewListener xListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            notifyChange = false;
            pageIndex = 0;
            httpSearch(keyword);
        }

        @Override
        public void onLoadMore() {
            needDialog = false;
            pageIndex++;
            notifyChange = true;
            httpSearch(keyword);
        }
    };

}