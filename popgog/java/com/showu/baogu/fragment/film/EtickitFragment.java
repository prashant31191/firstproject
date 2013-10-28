package com.showu.baogu.fragment.film;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.showu.baogu.R;
import com.showu.baogu.adapter.EtickitAdapter;
import com.showu.baogu.bean.film.EcinemaBean;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;

import java.util.List;

/**
 * Created by showu on 13-8-8.
 */
@FragmentView(id = R.layout.fragment_etickt)
public class EtickitFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @InjectView(id = R.id.listView)
    private ListView listView;
    private EtickitAdapter adapter ;

    @Override
    public void bindDataForUIElement() {
        tWidget.setCenterViewText("购买电子券");
    }

    @Override
    protected void bindEvent() {
        LogUtil.e(getClass(),"bindEvent");
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        View v = view.findViewById(R.id.buyLayout);
        if (v.getVisibility() == View.VISIBLE) {
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            listView.setAdapter(adapter);
        }
    }

    public void setAdapter(List<EcinemaBean> list) {
        adapter = new EtickitAdapter(getActivity(), list);
        listView.setAdapter(adapter);
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }
}
