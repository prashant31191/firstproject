package com.qinnuan.engine.fragment.film;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.showu.baogu.R;
import com.qinnuan.engine.adapter.CinemasTogetherAdapter;
import com.qinnuan.engine.bean.CinemaBean;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.listener.IOnListVItemClick;
import com.qinnuan.engine.util.view.MyListView;

import java.util.List;

@FragmentView(id=R.layout.fragment_cinemas_together)
public class CinemasTogetherFragment extends BaseFragment {

	@InjectView(id=R.id.fragment_cinemas_together_listView)
    MyListView fansListv;

    private CinemasTogetherAdapter listAdapter;

    private IOnListVItemClick iOnClick;
    public CinemasTogetherFragment(IOnListVItemClick l) {
        iOnClick = l;
    }

	@Override
	public void bindDataForUIElement() {
	}

    @Override
	protected void bindEvent() {

	}

    @Override
    public void onResume() {
        super.onResume();
        if(listAdapter!=null){
            fansListv.setAdapter(listAdapter);
        }
    }

    public void initAdapter(List<CinemaBean> fans) {
        listAdapter = new CinemasTogetherAdapter(getActivity(), fans);
        fansListv.setAdapter(listAdapter);
        fansListv.setOnItemClickListener(onItemClick);
    }
	
    private OnItemClickListener onItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CinemaBean cinema = listAdapter.getItem(position);
            iOnClick.onItemClick(cinema);
        }
    };

}
