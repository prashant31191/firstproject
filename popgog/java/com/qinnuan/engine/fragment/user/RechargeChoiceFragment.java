package com.qinnuan.engine.fragment.user;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.listener.IOnItemClickListener;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.fragment.InjectView;

/**
 * Created by showu on 13-7-29.
 */
@FragmentView(id = R.layout.fragment_recharge_select)
public class RechargeChoiceFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_recharge_select_money)
    private EditText money;
    @InjectView(id = R.id.fragment_recharge_select_moneyshow)
    private TextView moneyshow;
    @InjectView(id = R.id.fragment_recharge_select_radiogroup)
    private RadioGroup radioGroup;
    @InjectView(id = R.id.fragment_recharge_select_submit)
    private Button btnSubmit;

    public static final int PAYT_CLINET = 2;
    public static final int PAYT_ONLINE = 1;
    public static final int PAYT_UNITION = 3;


    private int payType = -1;
    private String moneyStr;

    private IOnItemClickListener listener;

    public RechargeChoiceFragment(String money, IOnItemClickListener l) {
        moneyStr = money;
        listener = l;
    }

    @Override
    public void bindDataForUIElement() {
        money.setText("￥"+moneyStr);
        moneyshow.setText("￥"+moneyStr);
    }

    @Override
    protected void bindEvent() {
        radioGroup.setOnCheckedChangeListener(checkl);
        btnSubmit.setOnClickListener(clickl);
        money.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
    }

    private RadioGroup.OnCheckedChangeListener checkl = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i) {
                case R.id.alipy_client:
                    payType = PAYT_CLINET;
                    break;
                case R.id.alipy_online:
                    payType = PAYT_ONLINE;
                    break;
                case R.id.unition_pay:
                    payType = PAYT_UNITION;
                    break;
                default:break;
            }
        }
    };

    private View.OnClickListener clickl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (payType != -1) {
                listener.onClick(payType);
                //((OnLineSeatActivity) getActivity()).confireOrder(payType);
            } else {
                GUIUtil.toast(getActivity(), R.string.select_pay_type);
            }
        }
    };

}
