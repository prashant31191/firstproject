package com.showu.baogu.activity.message;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.showu.baogu.R;
import com.showu.baogu.activity.BaseActivity;
import com.showu.common.util.LogUtil;
import com.showu.common.util.TextUtil;
import com.showu.common.widget.TitleWidget;

public class ShowMapActivity extends BaseActivity implements OnMarkerClickListener,TitleWidget.IOnClick {
    public static final String EXT_LONGITUDE="longitude";
    public static final String EXT_LATITUDE="latitude";
    public static final String EXT_CINEMANAME="cinemaname";
	private AMap aMap;
	private String lontitude;
	private String latitude;

	private String titleName;
    private TitleWidget tWidget ;
	private String type;
	private final String ACTIVITY_TYPE = "activity_type";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.a_map);
		getDataByIntent();
		initUi();
		initMap();
	}

	private void getDataByIntent() {
		type = getIntent().getStringExtra(ACTIVITY_TYPE);
		if (ACTIVITY_TYPE.equalsIgnoreCase(type)) {
			titleName = getIntent().getStringExtra("activityName");
		} else {
			titleName = getIntent().getStringExtra(EXT_CINEMANAME);
		}
		lontitude = getIntent().getStringExtra(EXT_LONGITUDE);
		latitude = getIntent().getStringExtra(EXT_LATITUDE);
	}

	private void initUi() {
		tWidget = (TitleWidget)findViewById(R.id.titleBar);
		tWidget.setCenterViewText(titleName);
        tWidget.setTitleListener(this);
	}

	/**
	 * 初始化AMap对象
	 */
	private void initMap() {
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			aMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
			aMap.setOnMarkerClickListener(this);
			final LatLng latlng = new LatLng(Double.parseDouble(latitude),
					Double.parseDouble(lontitude));
			if (TextUtil.isEmpty(titleName)) {
				Marker marker = aMap.addMarker(new MarkerOptions()
						.position(latlng));
			} else {
				aMap.addMarker(new MarkerOptions()
				.position(latlng).title("影院名字")
				.snippet(titleName)
				.icon(BitmapDescriptorFactory.defaultMarker()));
			}
			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
		}

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		return false;
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

    class CustomInfoWindowAdapter implements InfoWindowAdapter {
		private final View mWindow;
		private final View mContents;

		CustomInfoWindowAdapter() {
			mWindow = getLayoutInflater().inflate(R.layout.custom_info_window,
					null);
			mContents = getLayoutInflater().inflate(
					R.layout.custom_info_contents, null);
		}

		@Override
		public View getInfoWindow(Marker marker) {
			render(marker, mWindow);
			return mWindow;
		}

		@Override
		public View getInfoContents(Marker marker) {
			render(marker, mContents);
			return mContents;
		}

		private void render(Marker marker, View view) {
			int badge;
			badge=R.drawable.badge_qld;
			((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);
			String title = marker.getTitle();
			TextView titleUi = ((TextView) view.findViewById(R.id.title));
			if (title != null) {
				SpannableString titleText = new SpannableString(title);
				titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
						titleText.length(), 0);
				titleUi.setText(titleText);
			} else {
				titleUi.setText("");
			}
			String snippet = marker.getSnippet();
			TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
			if (snippet != null) {
				SpannableString snippetText = new SpannableString(snippet);
				snippetUi.setText(snippetText);
			} else {
				snippetUi.setText("");
			}
		}
	}
}
