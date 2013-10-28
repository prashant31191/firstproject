package com.showu.baogu.fragment.user;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.showu.baogu.R;
import com.showu.baogu.activity.user.OnlineOrderDetailActivity;
import com.showu.baogu.adapter.OnlineOrderAdapter;
import com.showu.baogu.bean.user.OnlineOrder;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.baogu.util.view.MyXListView;
import com.showu.baogu.util.view.XListView;

import java.util.List;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_online_order)
public class UserOnlineOrderFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_online_order_listv)
    MyXListView listv;

    private Integer isnext = 0;

    private OnlineOrderAdapter adapter;

    public UserOnlineOrderFragment() {

    }

    @Override
    public void bindDataForUIElement() {
        listv.setPullRefreshEnable(true);
        listv.setPullLoadEnable(true);}

    @Override
    protected void bindEvent() {
        listv.setOnItemClickListener(onItemL);
    }

    public void initAdapter(List<OnlineOrder> list, Integer isnext) {
        this.isnext = isnext;
        adapter = new OnlineOrderAdapter(getActivity(), list);
        listv.setAdapter(adapter);
        listv.setOnItemClickListener(onItemL);
        listv.setIHasNext(l);
    }

    public void notifyAdapter(List<OnlineOrder> list, Integer isnext) {
        this.isnext = isnext;
        if(adapter != null) {
            adapter.getModelList().addAll(list);
            adapter.notifyDataSetChanged();
            listv.setOnItemClickListener(onItemL);
            listv.setIHasNext(l);
        }
        if(isnext != 1) {
            listv.removeFooter();
        }
    }

    private AdapterView.OnItemClickListener onItemL = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(view == listv.getFooterView()) return;
            Intent intent = new Intent(getActivity(), OnlineOrderDetailActivity.class);
            intent.putExtra(OnlineOrderDetailActivity.ORDER_DETAIL_ID, adapter.getItem(position-1));
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