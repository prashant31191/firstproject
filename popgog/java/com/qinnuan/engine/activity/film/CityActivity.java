package com.qinnuan.engine.activity.film;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.adapter.ProvinceAdapter;
import com.qinnuan.engine.api.film.GetCityList;
import com.qinnuan.engine.bean.film.CityBean;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.widget.TitleWidget;
import com.showu.baogu.R;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.film.ProvinceBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CityActivity extends BaseActivity implements TitleWidget.IOnClick {

    private List<ProvinceBean> provinceList = new ArrayList<ProvinceBean>();
    private List<CityBean> hotCityList = new ArrayList<CityBean>();
    private ProvinceAdapter adapter;
    private GetCityList getCityList=new GetCityList();
    private ExpandableListView expandListView;
    private RelativeLayout headView;
    private Button gpsCityBtn;
    private TitleWidget titleWidget ;
    private CityBean selectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        getDataByIntent();
        loadData();
        initUi();
    }

    private void initUi() {
        expandListView = (ExpandableListView) findViewById(R.id.expandListView);
        headView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.city_list_head, null);
        gpsCityBtn = (Button) headView.findViewById(R.id.gpsCity);
        titleWidget= (TitleWidget) findViewById(R.id.titleBar);
        titleWidget.setTitleListener(this);
        setGpsCity();
    }

    private void setGpsCity() {
        final CityBean gpsCity = new CityBean(Const.city.getCityid(), Const.city.getCityname());
        gpsCityBtn.setText(gpsCity.getLocalname());
        gpsCityBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setBundle(gpsCity);
            }
        });
    }

    private void getDataByIntent() {
        selectedCity = (CityBean) getIntent().getSerializableExtra("selectedCity");
    }

    private void loadData() {
       request(getCityList);
    }

    @Override
    protected void requestSuccess(String url, String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int status = jsonObject.getInt("status");
            if (status == 1) {
                JSONArray provinceArray = jsonObject.getJSONObject("data").getJSONArray("province");
                for (int i = 0; i < provinceArray.length(); i++) {
                    ProvinceBean proince = JSONTool.jsonToBean(
                            ProvinceBean.class, provinceArray.getJSONObject(i));
                    List<CityBean> citylist =JSONTool.getJsonToListBean(
                            CityBean.class, provinceArray.getJSONObject(i).getJSONArray("city"));
                    proince.getCityList().addAll(citylist);
                    provinceList.add(proince);
                }
                initHeadView();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initHeadView() {
        for (ProvinceBean p : provinceList) {
            for (CityBean c : p.getCityList()) {
                if (c.getIshot().equals("1")) {
                    hotCityList.add(c);
                }
            }
        }

        LinearLayout hotcityLayout = (LinearLayout) headView.findViewById(R.id.hotCityLayout);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, 63);
        hotcityLayout.setFocusable(false);
        for (int i = 0; i < hotCityList.size(); i++) {
            CityBean bean = hotCityList.get(i);
            TextView nameText = new TextView(this);
            nameText.setOnClickListener(onClickListener);
            nameText.setPadding(20, 53, 20, 53);
            nameText.setText(bean.getLocalname());
            if (selectedCity.getLocalid().equals(bean.getLocalid())) {
                nameText.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(R.drawable.gou), null);
            }
            nameText.setGravity(Gravity.CENTER_VERTICAL);
            if (i == 0) {
                nameText.setBackgroundResource(R.drawable.btn_shang_bg);
            } else if (i == hotCityList.size() - 1) {
                nameText.setBackgroundResource(R.drawable.btn_xia_bg);
            } else {
                nameText.setBackgroundResource(R.drawable.btn_zhong_bg);
            }
            nameText.setLayoutParams(params);
            nameText.setTag(bean);
            nameText.setId(R.id.cityItem);
            hotcityLayout.addView(nameText);
        }
        TextView nameText = new TextView(this);
        nameText.setPadding(20, 20, 20, 20);
        nameText.setGravity(Gravity.CENTER);
        nameText.setText("切换城市");
        nameText.setTextColor(0xFF000000);
        hotcityLayout.addView(nameText);
        expandListView.addHeaderView(headView);
        adapter = new ProvinceAdapter(this);
        adapter.setSelectedCity(selectedCity);
        adapter.getList().addAll(provinceList);
        expandListView.setAdapter(adapter);
        expandListView.setOnChildClickListener(onChildClickListener);
    }

    private OnChildClickListener onChildClickListener = new OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {
            CityBean bean = provinceList.get(groupPosition).getCityList().get(childPosition);
            setBundle(bean);
            return true;
        }
    };

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cityItem:
                    CityBean bean = (CityBean) v.getTag();
                    setBundle(bean);
                    break;

                default:
                    break;
            }
        }
    };

    private void setBundle(CityBean cityBean) {
        Bundle b = new Bundle();
        b.putSerializable("selectedCity", cityBean);
        Intent intent = new Intent();
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void leftClick() {
       finish();
    }

    @Override
    public void rightClick() {

    }

    @Override
    public void centerClick() {

    }
}
