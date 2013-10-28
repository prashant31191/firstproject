package com.showu.baogu.activity.film;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.showu.baogu.R;
import com.showu.baogu.activity.BaseActivity;

public class BuyTicketStartActivity extends BaseActivity {

	private BuyTicketStartActivity instance = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initEvent();
	}

	private void initEvent() {
		findViewById(R.id.activity_buy_ticket_start_buy).setOnClickListener(
			new OnClickListener() {
				@Override
				public void onClick(View v) {
//					Intent intent = new Intent(instance, FilmListForTogetherWatchFilm.class);
//					instance.startActivity(intent);
				}
			});
	}

}
