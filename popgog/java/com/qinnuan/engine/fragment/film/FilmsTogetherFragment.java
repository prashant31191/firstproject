package com.qinnuan.engine.fragment.film;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.listener.ILoadImgListener;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.film.OnLineSeatActivity;
import com.qinnuan.engine.adapter.FilmsTogetherAdapter;
import com.qinnuan.engine.bean.film.FilmBean;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.listener.OnScrollItemChange;

import java.util.List;


@FragmentView(id=R.layout.fragment_films_together)
public class FilmsTogetherFragment extends BaseFragment {

	@InjectView(id=R.id.fragment_films_together_listView)
    com.qinnuan.engine.util.view.MyListView filmsListv;

    private FilmsTogetherAdapter listAdapter;

    private ILoadImgListener lisnter;

    public FilmsTogetherFragment(ILoadImgListener l) {
        lisnter = l;
    }

	@Override
	public void bindDataForUIElement() {

	}

    @Override
	protected void bindEvent() {

	}

    public void initAdapter(List<FilmBean> films) {
        listAdapter = new FilmsTogetherAdapter(getActivity(), films);
        listAdapter.setiLoadImgL(lisnter);
        filmsListv.setAdapter(listAdapter);
        filmsListv.setOnScrollItemChange(onScrollItemChange);
        filmsListv.setOnItemClickListener(onItemClick);
    }

    private static final int ORDER_TYPE = 6;
    private OnItemClickListener onItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FilmBean film = listAdapter.getItem(position);
            Intent i = new Intent(getActivity(), OnLineSeatActivity.class);
            i.putExtra(OnLineSeatActivity.EXT_FILM_ID, film.getFilmid());
            i.putExtra(OnLineSeatActivity.EXT_FILM_NAME, film.getFilmname());
            i.putExtra(OnLineSeatActivity.EXT_FILM_IMAGE,film.getFrontcover()) ;
            i.putExtra(OnLineSeatActivity.EXT_FILM_TYPE, ORDER_TYPE);
            getActivity().startActivity(i);
        }
    };


	private OnScrollItemChange onScrollItemChange = new OnScrollItemChange() {
		@Override
		public void loadHead(View view, int position) {
			if(position<listAdapter.getCount() && view!=null) {
                FilmBean fan = listAdapter.getItem(position);
                lisnter.load(fan.getFrontcover(), (ImageView)view.findViewById(R.id.item_films_together_front));
			}
		}
	};

}
