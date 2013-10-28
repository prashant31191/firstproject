package com.showu.baogu.activity.film;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.showu.baogu.R;
import com.showu.baogu.activity.BaseActivity;
import com.showu.baogu.api.film.GetTogetherseefilmFilmlist;
import com.showu.baogu.application.Const;
import com.showu.baogu.bean.film.FilmBean;
import com.showu.baogu.fragment.film.BuyTicketStartFragment;
import com.showu.baogu.fragment.film.FilmsTogetherFragment;
import com.showu.baogu.listener.ILoadImgListener;
import com.showu.baogu.listener.IOnListVItemClick;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.JSONTool;
import com.showu.common.util.LogUtil;

import org.json.JSONObject;

import java.util.List;

public class BuyTicketTogetherActivity extends BaseActivity {

	private BuyTicketTogetherActivity instance = this;
    private List<FilmBean> films;

    private BuyTicketStartFragment startF;
    private FilmsTogetherFragment filmsF;

    private String filmName;
    private String filmId;
    private String filmFrontcover;
    private int isShow = 1;

    private GetTogetherseefilmFilmlist filmsParam = new GetTogetherseefilmFilmlist();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getDataByIntent();
        gotoStartPage();
	}

    private void getDataByIntent() {
        filmName = getIntent().getStringExtra(OnLineSeatActivity.EXT_FILM_NAME);
        filmId = getIntent().getStringExtra(OnLineSeatActivity.EXT_FILM_ID);
        filmFrontcover = getIntent().getStringExtra(OnLineSeatActivity.EXT_FILM_IMAGE);
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        try {
            if (url.contains(filmsParam.getApi())) {
                parseFilms(conent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseFilms(String json) throws Exception {
        JSONObject jobj = new JSONObject(json);
        if(jobj.getInt("status") == Const.HTTP_OK) {
            JSONObject jdata = jobj.getJSONObject("data");
            films = JSONTool.getJsonToListBean(FilmBean.class, jdata.getJSONArray("films"));
            if(films.size() > 0) {
                filmsF.initAdapter(films);
            } else {
                GUIUtil.toast(instance, "数据为空");
            }
        }
    }

    private void gotoStartPage() {
        startF = new BuyTicketStartFragment(iOnCheckClickL, mImageFetcher);
        replaceFragment(startF, false);
    }

    private void gotoFilmSelectPage() {
        filmsF = new FilmsTogetherFragment(iLoadL);
        replaceFragment(filmsF, true);
    }

    private IOnListVItemClick iOnCheckClickL = new IOnListVItemClick() {
        @Override
        public void onItemClick(Object obj) {

            if(startF.isPublish()) {
                isShow = 1;
            } else {
                isShow = 0;
            }

            LogUtil.e(getClass(), "isShow==>"+isShow);

            Intent intent = new Intent(instance, OnLineSeatActivity.class);
            intent.putExtra(OnLineSeatActivity.EXT_FILM_ID, filmId);
            intent.putExtra(OnLineSeatActivity.EXT_FILM_NAME, filmName);
            intent.putExtra(OnLineSeatActivity.EXT_FILM_IMAGE, filmFrontcover);
            intent.putExtra(OnLineSeatActivity.EXT_IS_SHOW, isShow);
            LogUtil.e(getClass(), "isShow=>"+isShow);
            startActivity(intent);
        }
    };

    private ILoadImgListener iLoadL = new ILoadImgListener() {
        @Override
        public void load(String url, ImageView img) {
          mImageFetcher.loadImage(url, img, GUIUtil.getDpi(instance, R.dimen.margin_8));

        }
    };

    private void httpGetFilms() {
        filmsParam.setDevicetype(Const.DEVICE_TYPE);
        filmsParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        filmsParam.setLogincityid(Const.city.getCityid());
        filmsParam.setUserid(Const.user.getUserid());
        request(filmsParam);
    }

}
