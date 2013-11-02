package com.qinnuan.engine.activity.film;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.qinnuan.engine.api.film.AddUserFilm;
import com.qinnuan.engine.api.film.DeleteUserFilm;
import com.qinnuan.engine.api.film.GetTogetherseefilmUserlist;
import com.qinnuan.engine.bean.film.CityBean;
import com.qinnuan.engine.bean.film.ShowTicketUser;
import com.qinnuan.engine.listener.IListVListener;
import com.qinnuan.engine.listener.ILoadImgListener;
import com.qinnuan.common.http.AsyncTask;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.widget.ElasticHScrollView;
import com.qinnuan.common.widget.FilmImagePop;
import com.showu.baogu.R;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.api.film.GetFilmShowlist;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.film.FilmBean;
import com.qinnuan.common.widget.MyScroll;
import com.qinnuan.common.widget.PagerContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FilmIndexActivity extends BaseActivity {
    private FilmIndexActivity instance = this;

    private List<FilmBean> filmList = new ArrayList<FilmBean>();
    private List<ShowTicketUser> userTickets = new ArrayList<ShowTicketUser>();

    private PagerContainer mContainer;
    private ListView listView;
    private ImageView tabImg;
    //电影海报视图类型
    private ViewType viewType = ViewType.GALLERYVIEW;

    private RelativeLayout scrollParent;
    private RelativeLayout viewChangeLayout;
    public static LinearLayout userHeads;
//    private MyScroll userScroll;
    private ElasticHScrollView userScroll;
    private Button cityNameText;
    private ImageView filmAddme;
    private FilmBean wantSessFilmBean ;
    private static FilmType filmType = FilmType.FILM_PLAYING;// 1 ：正在放映 ,2即将放映
    private final int CITY_STATUS = 8;
    private float offest = 0;
    private GetFilmShowlist getFilmShowlist = new GetFilmShowlist();
    private AddUserFilm addUserFilm = new AddUserFilm();
    private DeleteUserFilm deleteUserFilm= new DeleteUserFilm();
    private CityBean selectedCity ;
    private boolean isRefresh = true;
    private int isnext = 0;
    private int pageindex = 0;

    public enum ViewType {
        LISTVIEW,
        GALLERYVIEW
    }

    public enum FilmType {
        FILM_PLAYING,
        FILM_PLAY_WILL
    }

    public static FilmType getFilmType() {
        return filmType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        this.selectedCity=new CityBean(Const.city.getCityid(),Const.city.getCityname());
        initViews();
        initEvent();
//		initFilmByCache(getFilmShowlist.getApi());
		httpGetFilmUser(isRefresh);
        getFilm(Const.city.getCityid());
    }

    private void getFilm(String cityId) {
        if (Const.user != null) {
            getFilmShowlist.setUserid(Const.user.getUserid());
        }
        getFilmShowlist.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        getFilmShowlist.setLogincityid(cityId);
        request(getFilmShowlist);
    }
    public void showButtomPop(FilmBean bean) {
      FilmImagePop popLayout = new FilmImagePop(this,mImageFetcher,bean);
        popLayout.startAnimation(bottomIn);
        buttomPop = new PopupWindow(popLayout,
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.FILL_PARENT);
        buttomPop.setFocusable(true);
        buttomPop.setBackgroundDrawable(new BitmapDrawable());
        buttomPop.setOutsideTouchable(true);
        buttomPop.update();
        buttomPop.showAsDropDown(userScroll);
    }

    public void closePop(){
        buttomPop.dismiss();
    }

    private void initViews() {
        Display display = getWindowManager().getDefaultDisplay();
        userHeads = (LinearLayout) findViewById(R.id.activity_film_userheads);
       // viewChangeLayout = (RelativeLayout) findViewById(R.id.right_layout);
        scrollParent = (RelativeLayout) findViewById(R.id.scrolllayout);
        filmAddme = (ImageView) findViewById(R.id.film_addme);
        //userScroll = (MyScroll) findViewById(R.id.activity_film_user_layout);
        userScroll = (ElasticHScrollView) findViewById(R.id.activity_film_user_layout);
        listView = (ListView) findViewById(R.id.listView);
        cityNameText = (Button) findViewById(R.id.city);
        mContainer = (PagerContainer) findViewById(R.id.pager_container);

        userScroll.setListVL(il);

       // tabImg = (ImageView) findViewById(R.id.activity_film_tab_img);

//		if(NetWorkUtile.getNetworkState(this) != NetWorkUtile.WIFI) {
//			setPotoViewType(ViewType.LISTVIEW);
//		}

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int w = display.getWidth();
        int h = display.getHeight();

        //final float scale = getResources().getDisplayMetrics().density;
       // int h = (int)(display.getHeight() / scale + 0.5f);

        userScroll.setHorizontalFadingEdgeEnabled(false);
        userScroll.setHorizontalScrollBarEnabled(false);
        RelativeLayout.LayoutParams userScrlParam = new RelativeLayout.LayoutParams(w, h / 9);
        userScroll.setLayoutParams(userScrlParam);

//        userScroll.setIScrollStop(iScrollStop);
//        userScroll.setIhttpGetFilmUser(iHttpGetFilmUser);
        RelativeLayout.LayoutParams faddmeParams = new RelativeLayout.LayoutParams(h / 9 - 5, h / 9 - 5);
        faddmeParams.setMargins(0, 2, 0, 0);

        filmAddme.setLayoutParams(faddmeParams);

        filmAddme.setOnClickListener(onClickListener);
    }

    public  void addWantSess(FilmBean bean){
        wantSessFilmBean=bean ;
            addUserFilm.setDeviceidentifyid(GUIUtil.getDeviceId(this));
            addUserFilm.setUserid(Const.user.getUserid());
            addUserFilm.setFilmid(bean.getFilmid());
        request(addUserFilm);
    }
    public  void deletWantSess(FilmBean filmBean){
        wantSessFilmBean=filmBean;
        deleteUserFilm.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        deleteUserFilm.setUserid(Const.user.getUserid());
        deleteUserFilm.setUfilmid(filmBean.getUfilmid());
        request(deleteUserFilm);
    }
	private AsyncTask<Void, Void, String> initFilmByCache(final String api) {
		return new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				return getLocalData(api);
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				LogUtil.i(getClass(), "film json=>" + result);
				if(result != null) {
                    try {
                        parseFilmJson(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
			}
		}.execute();
	}

	private void initEvent() {
		cityNameText.setOnClickListener(onClickListener);
//		viewChangeLayout.setOnClickListener(onClickListener);
//		mContainer.setItemSelectListener(onItemSelectChangeListener);
	}


    private final int left_offest = 1;
    private final int right_offest = 2;
    private int position = left_offest;
    private Animation animation;
    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.city:
				Intent i= new Intent(instance, CityActivity.class);
				LogUtil.e(getClass(), "test==onclick cityname==>"+selectedCity.getLocalname());
				i.putExtra("selectedCity", selectedCity);
				instance.startActivityForResult(i, CITY_STATUS);
                    break;
//                case R.id.right_layout:
//				changeView(viewType);
//                    break;
//			case R.id.isplay: {
//				if(position == right_offest) {
//					position = left_offest;
//					animation = new TranslateAnimation(offest, 0, 0, 0);
//					animation.setFillAfter(true);// True:图片停在动画结束位置
//					animation.setDuration(150);
//					tabImg.startAnimation(animation);
//					chanageTab(FilmType.FILM_PLAYING);
//				}
//				break;
//			}
//			case R.id.will_play: {
//				if(position == left_offest) {
//					position = right_offest;
//					animation = new TranslateAnimation(0, offest, 0, 0);
//					animation.setFillAfter(true);// True:图片停在动画结束位置
//					animation.setDuration(150);
//					tabImg.startAnimation(animation);
//					chanageTab(FilmType.FILM_PLAY_WILL);
//				}
//				break;
//			}
                case R.id.film_addme: {
//                    if(GUIUtil.checkIsOnline(instance, UserActivity.class)) {
//                        Intent intent = new Intent(instance, BuyTicketTogetherActivity.class);
//                        startActivity(intent);
//                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    private OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
        }
    };

    private PagerContainer.OnItemSelectChangeListener onItemSelectChangeListener =
            new PagerContainer.OnItemSelectChangeListener() {
                @Override
                public void onItemSelectChangeListener(ViewPager viewPage, int position) {
                }
            };

    @Override
    protected void requestSuccess(String url, String json) {
        try {
            if (url.contains(getFilmShowlist.getApi())) {
                parseFilmJson(json);
            }else if(url.contains(addUserFilm.getApi())){
                parseAddWantSess(json);
            }else  if(url.contains(deleteUserFilm.getApi())){
                parseDelWantSess(json);
            } else if(url.contains(fansParam.getApi())) {
                parseShowTicketUser(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//		if (url.contains("get_local_byname")) {
//			parseCityJson(json);
//		} else if (url.contains(getfilmParam.getApi())) {
//			saveLocalData(getfilmParam.getApi(), json);
//			parseFilmJson(json);
//		} else if (url.contains(getActivityParam.getApi())) {
//			parseActivity(json);
//		} else if(url.contains(fansParam.getApi())) {
//			parseShowTicketUser(json);
//		}
    }

    private void parseAddWantSess(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json) ;
        int status = jsonObject.getInt("status") ;
        if(status==Const.HTTP_OK){
            String ufilmId = jsonObject.getJSONObject("data").getString("ufilmid") ;
            wantSessFilmBean.setUfilmid(ufilmId);
        }
    }

    private void parseDelWantSess(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json) ;
        int status = jsonObject.getInt("status") ;
        if(status==Const.HTTP_OK){
            wantSessFilmBean.setUfilmid(null);
        }
    }

    private void parseShowTicketUser(String json) throws Exception {
        JSONObject jObj = new JSONObject(json);
        int status = jObj.getInt("status");
        if (status == Const.HTTP_OK) {
            JSONObject jdata = jObj.getJSONObject("data");
            isnext = jdata.getInt("isnext");
            if (isRefresh) {
                userTickets.clear();
            }
            userTickets.addAll(JSONTool.getJsonToListBean(
                    ShowTicketUser.class, jdata.getJSONArray("seefilm")));
            LogUtil.e(getClass(), "userTickets size=>" + userTickets.size());

            userScroll.setILoadImg(iLoadImgL);
            userScroll.fillData(userTickets, scrollParent, isRefresh);

        } else if(status == 2) {
            userTickets.clear();
            userScroll.fillData(userTickets, scrollParent, isRefresh);
        }
        isFinished = true;
    }

    private ILoadImgListener iLoadImgL = new ILoadImgListener() {
        @Override
        public void load(String url, ImageView img) {
            mImageFetcher.loadImage(url, img, GUIUtil.getDpi(instance, R.dimen.margin_8));
        }
    };

    private void parseCityJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int status = jsonObject.getInt("status");
            if (status == Const.HTTP_OK) {
//				selectedCity = EATool.jsonToBean(CityBean.class, jsonObject);
//				Const.CITY_LOCAL_ID = selectedCity.getLocalid();
//				String url = Const.JSON_API_URL + getString(R.string.get_films_active,
//												selectedCity.getLocalid(), "1");
//				doGetHttp(url, 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CITY_STATUS) {
            selectedCity= (CityBean)data.getSerializableExtra("selectedCity");
            if (selectedCity!= null) {
                getFilm(selectedCity.getLocalid());
                isRefresh = true;
                httpGetFilmUser(isRefresh);
                cityNameText.setText(selectedCity.getLocalname());
            }
        }
    }
//	private void saveLocalData(String api, String json) {
//		try {
//			JSONObject jsonObject = new JSONObject(json);
//			int status = jsonObject.getInt("status");
//			if (status == HTTP_OK) {
//				baseAppPref.saveFilmJsonCache(api, json);
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}

	private String getLocalData(String api) {
		return basePref.getFilm(api);
	}

    private void parseFilmJson(String json) throws JSONException {
        JSONObject jObj = new JSONObject(json);
        int status = jObj.getInt("status");
        if (status == 1) {
            basePref.setFilm(getFilmShowlist.getApi(),json);
            JSONArray filmArray = jObj.getJSONObject("data").getJSONArray("films");
            filmList = JSONTool.getJsonToListBean(FilmBean.class, filmArray);
            mContainer.initViewPager(filmList,mImageFetcher);
        } else if(status == 2) {
            filmList = new ArrayList<FilmBean>();
            mContainer.initViewPager(filmList, mImageFetcher);
        }

    }

    public static boolean isFinished = false;
    private IListVListener il = new IListVListener() {
        @Override
        public void refresh() {
            //isFinished = false;
            isRefresh = true;
            if(isFinished)
                httpGetFilmUser(isRefresh);
        }

        @Override
        public void loadMore() {
            isRefresh = false;
            if(isnext == 1) {
                //isFinished = false;
                if(isFinished)
                    httpGetFilmUser(isRefresh);
            }
        }
    };



    //=============实现MyScroll类的内部类，当滑动停止时，控制标签的显示或者隐藏========
    private MyScroll.IScrollStop iScrollStop = new MyScroll.IScrollStop() {
        @Override
        public void operateShow(int x, int oldx) {
            sendShowMessage();
        }

        @Override
        public void operateHide(int x, int oldx) {
            //filmAddme.setVisibility(View.GONE);
        }

        private void sendShowMessage() {
            Message msg = new Message();
            msg.what = 10000;
            handler.sendMessageDelayed(msg, 250);
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 10000: {
//                        if (filmAddme.getVisibility() != View.VISIBLE) {
//                            filmAddme.setVisibility(View.VISIBLE);
//                        }
                        break;
                    }
                    default:
                        break;
                }
            }

            ;
        };
    };
//===================================END========================================

    //========实现MyScroll类的内部类，当滑动停止时，刷新列表或者加载更多事件===========

    private MyScroll.IhttpGetFilmUser iHttpGetFilmUser = new MyScroll.IhttpGetFilmUser() {
        @Override
        public void getFilmUser(boolean isRefresh) {
			instance.isRefresh = isRefresh;
			LogUtil.i(getClass(), "instance.isRefresh=>" + instance.isRefresh + ", isRefresh=>" + isRefresh);
			if(isRefresh) {
				httpGetFilmUser(isRefresh);
			} else {
				if(isnext == 1) {
					httpGetFilmUser(isRefresh);
				}
			}
        }
    };

    private GetTogetherseefilmUserlist fansParam = new GetTogetherseefilmUserlist();
    private void httpGetFilmUser(boolean isRefresh) {

        if(Const.user != null) {
            fansParam.setUserid(Const.user.getUserid());
        }
        fansParam.setLogincityid(selectedCity.getLocalid());
        fansParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        fansParam.setDevicetype(Const.DEVICE_TYPE);
        if(isRefresh) {
            fansParam.setPageindex(pageindex+"");
        } else {
            fansParam.setPageindex((++pageindex)+"");
        }
        request(fansParam);
        isFinished = false;
    }

//===================================END========================================

//	private void needSwitchCityDialog() {
//		Builder builder = new Builder(this);
//		builder.setTitle("提示");
//		builder.setMessage("当前的定位城市是:"+Const.CITY_LOCAL_NAME+"\n是否需要切换");
//		builder.setPositiveButton("切换",
//			new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					baseAppPref.setSelectedCity(new CityBean(Const.CITY_LOCAL_ID, Const.CITY_LOCAL_NAME));
//					cityNameText.setText(baseAppPref.getSelectedCity().getLocalname());
//					httpGetFilm(baseAppPref.getSelectedCity().getLocalid(), FilmType.FILM_PLAYING);
//				}
//			});
//		builder.setNegativeButton("不切换",
//			new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					cityNameText.setText(baseAppPref.getSelectedCity().getLocalname());
//					httpGetFilm(baseAppPref.getSelectedCity().getLocalid(), FilmType.FILM_PLAYING);
//					dialog.dismiss();
//				}
//			});
//		builder.create().show();
//	}
}
