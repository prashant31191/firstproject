package com.qinnuan.engine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qinnuan.engine.adapter.CouponAdapter;
import com.qinnuan.engine.api.user.AddUserCoupon;
import com.qinnuan.engine.api.user.GetUserCoupon;
import com.qinnuan.engine.bean.Coupon;
import com.qinnuan.engine.util.view.MyXListView;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.util.view.XListView;

import org.json.JSONObject;

import java.util.List;

public class CouponActivity extends BaseActivity {

    private Button save;
    private EditText no;
    private MyXListView listv;
    private CouponAdapter adapter;
    private int pageindex = 0;
    private Integer isnext = 0;
    private GetUserCoupon couponParam = new GetUserCoupon();
    private AddUserCoupon addCouponParam = new AddUserCoupon();
    private boolean isRefresh = true;
    private List<Coupon> coupons;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        save = (Button) findViewById(R.id.activity_coupon_save);
        save.setOnClickListener(onClickL);
        no = (EditText) findViewById(R.id.activity_coupon_no);
        listv = (MyXListView) findViewById(R.id.activity_coupon_listv);
        listv.setPullRefreshEnable(true);
        getTitleWidget().getLeftL().setOnClickListener(onClickL);
        httpCoupon();
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        try {
            if(url.contains(couponParam.getApi())) {
                parseCoupon(conent);
                listv.onLoaded();
            } else if(url.contains(addCouponParam.getApi())) {
                JSONObject jobj = new JSONObject(conent);
                int status = jobj.getInt("status");
                if(status == 1) {
                    GUIUtil.toast(this, "兑换成功");
                    isRefresh = true;
                    httpCoupon();
                } else {
                    GUIUtil.toast(this, "兑换失败,请输入正确的兑换码！");
                }
                no.setText(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void requestFailed() {
        super.requestFailed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void httpCoupon() {
        couponParam.setUserid(Const.user.getUserid());
        couponParam.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        couponParam.setDevicetype(Const.DEVICE_TYPE);
        if(isRefresh) {
            pageindex = 0;
        } else {
            ++pageindex;
        }
        couponParam.setPageindex(pageindex+"");
        request(couponParam);
    }

    private void parseCoupon(String json) throws Exception {
        JSONObject jObj = new JSONObject(json);
        if(Const.HTTP_OK != jObj.getInt("status")) return;
        JSONObject jdata = jObj.getJSONObject("data");
        isnext = jdata.getInt("isnext");
        coupons = JSONTool.getJsonToListBean(Coupon.class, jdata.getJSONArray("coupon"));
        initAdapter();
    }

    private void initAdapter() {
        if(isRefresh) {
            adapter = new CouponAdapter(this, coupons);
            listv.setAdapter(adapter);
        } else {
            adapter.getModelList().addAll(coupons);
            adapter.notifyDataSetChanged();
        }
        listv.setIHasNext(ihl);
        listv.setXListViewListener(ixl);
        if(isnext != 1)
            listv.removeFooter();
    }

    private MyXListView.IHasNext ihl = new MyXListView.IHasNext() {
        @Override
        public Integer getHasNext() {
            return isnext;
        }
    };

    private XListView.IXListViewListener ixl = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            isRefresh = true;
            httpCoupon();
        }

        @Override
        public void onLoadMore() {
            isRefresh = false;
            httpCoupon();
        }
    };

    private View.OnClickListener onClickL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.activity_coupon_save:
                    httpAddCoupon();
                    break;
                case R.id.left:
                    finish();
                    break;
                default:break;
            }
        }
    };

    private void httpAddCoupon() {
        String noStr = no.getText().toString();
        if(TextUtil.isEmpty(noStr)) {
            GUIUtil.toast(this, "兑换码不能为空");
        } else {
            addCouponParam.setDevicetype(Const.DEVICE_TYPE);
            addCouponParam.setDeviceidentifyid(GUIUtil.getDeviceId(this));
            addCouponParam.setUserid(Const.user.getUserid());
            addCouponParam.setCouponcode(noStr);
            request(addCouponParam);
        }
    }


}