package com.qinnuan.engine.fragment.user;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.R;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.listener.IOnItemClickListener;

/**
 * Created by showu on 13-7-25.
 */
@FragmentView(id=R.layout.fragment_recharge_finish)
public class RechargeFinishFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_recharge_finish_img)
    private ImageView img;
    @InjectView(id = R.id.fragment_recharge_finish_tips)
    private TextView tips;
    @InjectView(id = R.id.fragment_recharge_finish_back)
    private Button back;


    private String moneyStr;
    private boolean isSuccess = false;
    private IOnItemClickListener listener;

    public RechargeFinishFragment(String money, boolean isSuccess, IOnItemClickListener l) {
        moneyStr = money;
        this.isSuccess = isSuccess;
        listener = l;
    }

    @Override
    public void bindDataForUIElement() {
        if(isSuccess) {
            img.setBackgroundResource(R.drawable.defaul_head);
            tips.setText("你已成功充值"+moneyStr+"元");
        } else {
            img.setBackgroundResource(R.drawable.defaul_head);
            tips.setText("充值失败，请联系客服");
        }
    }

    @Override
    protected void bindEvent() {
        back.setOnClickListener(clickl);
    }



    private View.OnClickListener clickl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            listener.onClick(view);
        }
    };
}
