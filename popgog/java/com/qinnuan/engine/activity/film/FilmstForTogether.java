package com.qinnuan.engine.activity.film;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.qinnuan.common.util.JSONTool;
import com.showu.baogu.R;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.bean.film.FilmBean;

public class FilmstForTogether extends BaseActivity {
	private ListView listView;
	private List<FilmBean> filmList;
//	private FilmSelectAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setContentView(R.layout.activity_film_select);
		initUI();
		initEvent();
		getFilmByCityId();
	}

	private void initUI() {
		listView = (ListView) findViewById(R.id.listView);
	}

	private void initEvent() {
		listView.setOnItemClickListener(onItemClickListener);
	}

	@Override
	protected void requestSuccess(String url, String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			int status = jsonObject.getInt("status");
			if (status == 1) {
				parseFilmJson(json);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private final String TYPE_PLAYING = "1";
	private final String TYPE_OPER = "watching";
	private void getFilmByCityId() {
		//String url = Const.JSON_API_URL + getString(R.string.get_films_together_watchfilm);
//		GetWatchFilmFilmsParam param = new GetWatchFilmFilmsParam();
//		param.setCalltype(TYPE_PLAYING);
//		param.setLocalid(Const.CITY_LOCAL_ID);
//		param.setOper(TYPE_OPER);
//		request(param, HttpMethod.GET);
	}

	private void parseFilmJson(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			int status = jsonObject.getInt("status");
			if (status == 1) {
				JSONArray array = jsonObject.getJSONArray("filmlist");
				filmList = JSONTool.getJsonToListBean(FilmBean.class, array);
				initListAdapter();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initListAdapter() {
//		if (listAdapter == null) {
//			listAdapter = new FilmSelectAdapter(this, filmList);
//			listView.setAdapter(listAdapter);
//		} else {
//			listAdapter.notifyDataSetChanged();
//		}
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//			Intent intent = new Intent(FilmListForTogetherWatchFilm.this,
//					CinemasForTogetherWatchFilmActivity.class);
//			intent.putExtra("film", filmList.get(position));
//			startActivity(intent);
		}
	};

}
