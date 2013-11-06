package com.qinnuan.engine.fragment.user;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qinnuan.engine.bean.Cash;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.listener.IOnItemClickListener;
import com.qinnuan.engine.util.view.MyXListView;
import com.qinnuan.engine.R;
import com.qinnuan.engine.adapter.UserCashAdapter;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.util.view.XListView;

import java.util.List;

/**
 * Created by showu on 13-7-25.
 */
@FragmentView(id=R.layout.fragment_user_account)
public class UserCashFragment extends BaseFragment {

    @InjectView(id=R.id.fragment_user_acount_records) private com.qinnuan.engine.util.view.MyXListView listView ;
    @InjectView(id=R.id.fragment_user_acount_recharge) private Button recharge;
    @InjectView(id=R.id.fragment_user_acount_balance) private TextView balance;
    @InjectView(id=R.id.nodata) private TextView nodata;

    private UserCashAdapter cashAdapter ;

    private Integer isnext = 0;

    private IOnItemClickListener itemClick;

    public UserCashFragment(IOnItemClickListener l) {
        itemClick = l;
    }

    @Override
    public void bindDataForUIElement() {
        balance.setText("￥"+Const.user.getTotalcashremain());
    }

    @Override
    protected void bindEvent() {
        recharge.setOnClickListener(onClickListener);
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(cashAdapter!=null){
            listView.setAdapter(cashAdapter);
        }
    }

    public  void setAdapter(List<Cash> cashs, int isnext){
        this.isnext = isnext;
        if(cashs.isEmpty()) {
            nodata.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            cashAdapter = new UserCashAdapter(getActivity(), cashs);
            listView.setIHasNext(iHasNext);
            listView.setXListViewListener(ixListViewL);
            listView.setAdapter(cashAdapter);
        }
    }

    public void notifyAdapter(List<Cash> cashs, int isnext){
        this.isnext = isnext;
        if(isnext!=1) {
            listView.removeFooter();
        }
        listView.setIHasNext(iHasNext);
        listView.setXListViewListener(ixListViewL);
        cashAdapter.getModelList().addAll(cashs);
        cashAdapter.notifyDataSetChanged();
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            itemClick.onClick(v);
        }
    };

    private MyXListView.IHasNext iHasNext = new MyXListView.IHasNext() {
        @Override
        public Integer getHasNext() {
            return isnext;
        }
    };

    private XListView.IXListViewListener ixListViewL;
    public void setIXL(XListView.IXListViewListener l) {
        ixListViewL = l;
    }

    public void onLoaded() {
        listView.onLoaded();
    }

}
