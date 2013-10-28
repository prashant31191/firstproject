package com.showu.baogu.activity.filmFan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.showu.baogu.R;
import com.showu.baogu.activity.BaseActivity;
import com.showu.baogu.adapter.BlacklistAdapter;
import com.showu.baogu.api.BlacklistSelect;
import com.showu.baogu.api.GetOtheruserinfo;
import com.showu.baogu.api.user.Giveyouwrite;
import com.showu.baogu.application.Const;
import com.showu.baogu.bean.Fan;
import com.showu.baogu.listener.IOnItemClickListener;
import com.showu.baogu.xmpp.provider.FanBlacklistManager;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.JSONTool;
import com.showu.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class FriendBlackActivity extends BaseActivity {

    private FriendBlackActivity instance = this;
    private List<Fan> fans;
    private BlacklistSelect blackParam = new BlacklistSelect();
    private GetOtheruserinfo userInfoParam = new GetOtheruserinfo();
    private Giveyouwrite giveyouwrite = new Giveyouwrite();

    private Fan deletBlackFan ;
    private ListView fansListv;
    private BlacklistAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);
        initViews();
        httpBlackList();
    }

    private void initViews() {
        getTitleWidget().getLeftL().setOnClickListener(onLeftL);
        fansListv = (ListView) findViewById(R.id.activity_blacklist_fans);
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
            } else if(url.contains(blackParam.getApi())) {
                parseFans(conent);
            }else if(url.contains(giveyouwrite.getApi())){
                parseUnBlack(conent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseUnBlack(String conent) throws JSONException {
        JSONObject jsonObject = new JSONObject(conent);
        int status = jsonObject.getInt("status");
        if(status==1){
            notifyAdapter();
            FanBlacklistManager.getInstance(this)
                    .deleteFanBlacklist(deletBlackFan.getUserid());
        }else{
           GUIUtil.toast(this,"解除失败");
        }
    }

    private void initAdapter(List<Fan> fans) {
        listAdapter = new BlacklistAdapter(this, fans, mImageFetcher);
        listAdapter.initAdapter(fans, fansListv);
        listAdapter.setUnblackListener(onClickListener);
    }

    private void notifyAdapter() {
        listAdapter.notifyAdapter();
        listAdapter.setUnblackListener(onClickListener);
    }

    private void parseFans(String json) throws Exception {
        JSONObject jobj = new JSONObject(json);
        if (Const.HTTP_OK == jobj.getInt("status")) {
            JSONObject jdata = jobj.getJSONObject("data");
            fans = JSONTool.getJsonToListBean(Fan.class, jdata.getJSONArray("users"));
            initAdapter(fans);
        }
    }

    private void parseFanInfo(String json) throws Exception {
        JSONObject jobj = new JSONObject(json);
        if (Const.HTTP_OK == jobj.getInt("status")) {
            JSONObject jdata = jobj.getJSONObject("data");
            Fan fan = JSONTool.jsonToBean(Fan.class, jdata.getJSONObject("users"));
            LogUtil.e(getClass(), "fans=>" + fan);
            gotoFanInfo(fan);
        }
    }

    private void httpUnlockBlackName(Fan unLockFan){
        giveyouwrite.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        giveyouwrite.setUserid(Const.user.getUserid());
        giveyouwrite.setUseridto(unLockFan.getUserid());
        request(giveyouwrite);
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

    private void gotoFanInfo(Fan fan) {
        Intent i = new Intent(this, FanInfoActivity.class);
        i.putExtra(FanInfoActivity.FAN_ID, fan.getUserid());
        startActivity(i);
    }

    private void httpBlackList() {
        blackParam.setDevicetype(Const.DEVICE_TYPE);
        blackParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        blackParam.setUserid(Const.user.getUserid());
        request(blackParam);
    }

    private IOnItemClickListener onClickListener = new IOnItemClickListener() {
        @Override
        public void onClick(Object obj) {
            deletBlackFan = (Fan)obj;
            httpUnlockBlackName(deletBlackFan);
        }
    };

    View.OnClickListener onLeftL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}