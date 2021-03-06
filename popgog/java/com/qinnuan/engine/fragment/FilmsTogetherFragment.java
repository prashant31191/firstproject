package com.qinnuan.engine.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.qinnuan.engine.listener.ILoadImgListener;
import com.qinnuan.engine.util.view.MyListView;
import com.qinnuan.engine.R;
import com.qinnuan.engine.adapter.FilmsTogetherAdapter;
import com.qinnuan.engine.bean.film.FilmBean;
import com.qinnuan.engine.listener.OnScrollItemChange;

import java.util.List;


@FragmentView(id=R.layout.fragment_films_together)
public class FilmsTogetherFragment extends BaseFragment {

	@InjectView(id=R.id.fragment_films_together_listView)
    MyListView filmsListv;

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

    public void initAdapter(List<FilmBean> fans) {
        listAdapter = new FilmsTogetherAdapter(getActivity(), fans);
        listAdapter.setiLoadImgL(lisnter);
        filmsListv.setAdapter(listAdapter);
        filmsListv.setOnScrollItemChange(onScrollItemChange);
        filmsListv.setOnItemClickListener(onItemClick);
    }

    private OnItemClickListener onItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FilmBean fan = listAdapter.getItem(position);
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

    @Override
    public void leftClick() {
        getActivity().finish();
    }


}
