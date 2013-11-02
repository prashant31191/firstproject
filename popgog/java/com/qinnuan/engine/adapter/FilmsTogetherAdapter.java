package com.qinnuan.engine.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.listener.ILoadImgListener;
import com.showu.baogu.R;
import com.qinnuan.engine.bean.film.FilmBean;

public class FilmsTogetherAdapter extends BaoguBaseAdapter<FilmBean> {

	private Context mContext;

	public FilmsTogetherAdapter(Context context, List<FilmBean> list) {
		super(context, list);
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FilmBean film = getItem(position);
		convertView = LayoutInflater.from(mContext).inflate(R.layout.item_films_together, null);
		ImageView filmFront = (ImageView) convertView.findViewById(R.id.item_films_together_front);
		TextView filmName = (TextView) convertView.findViewById(R.id.item_films_together_filmName);
		TextView filmDirectors = (TextView) convertView.findViewById(R.id.item_films_together_director);
        TextView filmActors = (TextView) convertView.findViewById(R.id.item_films_together_actorlist);
		TextView filmShowNumber = (TextView) convertView.findViewById(R.id.item_films_together_shownumber);

        iLoadImgL.load(film.getFrontcover(), filmFront); ;

		filmName.setText(film.getFilmname());
        filmDirectors.setText(film.getDirector());
        filmActors.setText(film.getActorlist());
		filmShowNumber.setText("工有"+film.getCinemanum()+"家影院上映");
		return convertView;
	}

    private ILoadImgListener iLoadImgL;
    public void setiLoadImgL(ILoadImgListener l) {
        iLoadImgL = l;
    }

}
