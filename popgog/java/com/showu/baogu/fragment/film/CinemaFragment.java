package com.showu.baogu.fragment.film;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.showu.baogu.R;
import com.showu.baogu.activity.film.OnLineSeatActivity;
import com.showu.baogu.adapter.CinemaAdapter;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;

/**
 * Created by showu on 13-7-25.
 */
@FragmentView(id=R.layout.fragment_cinima)
public class CinemaFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @InjectView(id=R.id.listView)
    private ListView listView ;
    private String filmName ;
    private CinemaAdapter cinemaAdapter ;
    public CinemaFragment() {
    }

    public CinemaFragment(String filmName) {
        this.filmName = filmName;
    }

    @Override
    public void bindDataForUIElement() {
        tWidget.setCenterViewText(filmName);
    }

    @Override
    protected void bindEvent() {
        listView.setOnItemClickListener(this);
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(cinemaAdapter!=null){
            listView.setAdapter(cinemaAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ((OnLineSeatActivity)getActivity()).onCinemaItemClick(i);
    }

    public  void setAdapter(CinemaAdapter adapter){
        this.cinemaAdapter=adapter;
        listView.setAdapter(adapter);
    }

}
