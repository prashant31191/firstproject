package com.showu.baogu.fragment.user;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.showu.baogu.R;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.baogu.listener.IOnItemClickListener;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.TextUtil;


/**
 * Created by showu on 13-7-25.
 */
@FragmentView(id=R.layout.fragment_account_recharge)
public class AccountRechargeFragment extends BaseFragment {

    @InjectView(id=R.id.fragment_account_recharge_money) private EditText money;
    @InjectView(id=R.id.fragment_account_recharge_recharge) private Button recharge;

    private IOnItemClickListener itemClick;

    public AccountRechargeFragment(IOnItemClickListener l) {
        itemClick = l;
    }

    @Override
    public void bindDataForUIElement() {
    }

    @Override
    protected void bindEvent() {
        recharge.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String moneyStr = money.getText().toString();
            if(!TextUtil.isEmpty(moneyStr)) {
                itemClick.onClick(moneyStr);
            } else {
                GUIUtil.toast(getActivity(), "请输入一个有效数字");
            }
        }
    };

}
