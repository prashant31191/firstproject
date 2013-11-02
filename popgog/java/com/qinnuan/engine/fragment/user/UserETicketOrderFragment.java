package com.qinnuan.engine.fragment.user;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.qinnuan.engine.activity.user.EticketOrderDetailActivity;
import com.qinnuan.engine.adapter.EticketOrderAdapter;
import com.qinnuan.engine.bean.user.EticketOrder;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.util.view.MyXListView;
import com.showu.baogu.R;
import com.qinnuan.engine.util.view.XListView;

import java.util.List;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_eticket_order)
public class UserETicketOrderFragment extends BaseFragment {


    @InjectView(id = R.id.fragment_eticket_order_listv)
    MyXListView listv;
    private Integer isnext = 0;

    private EticketOrderAdapter adapter;

    public UserETicketOrderFragment() {
    }

    @Override
    public void bindDataForUIElement() {
        listv.setPullRefreshEnable(true);
    }

    @Override
    protected void bindEvent() {
        listv.setOnItemClickListener(onItemL);
    }

    public void initAdapter(List<EticketOrder> list, Integer isnext) {
        this.isnext = isnext;
        adapter = new EticketOrderAdapter(getActivity(), list);
        listv.setAdapter(adapter);
        listv.setOnItemClickListener(onItemL);
        listv.setIHasNext(l);
    }

    public void notifyAdapter(List<EticketOrder> list, Integer isnext) {
        this.isnext = isnext;
        if(adapter != null) {
            adapter.getModelList().addAll(list);
            adapter.notifyDataSetChanged();
            listv.setOnItemClickListener(onItemL);
            listv.setIHasNext(l);
        }
    }

    private AdapterView.OnItemClickListener onItemL = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(view==listv.getHeadView() || view==listv.getFooterView()) return;
            Intent intent = new Intent(getActivity(), EticketOrderDetailActivity.class);
            intent.putExtra(EticketOrderDetailActivity.ORDER_DETAIL_ID, adapter.getItem(position-1));
            startActivity(intent);
        }
    };

    private MyXListView.IHasNext l = new MyXListView.IHasNext() {
        @Override
        public Integer getHasNext() {
            return isnext;
        }
    };

    public void setXListener(XListView.IXListViewListener xl ) {
        listv.setXListViewListener(xl);
    }

    public void onLoaded() {
        listv.onLoaded();
    }

}