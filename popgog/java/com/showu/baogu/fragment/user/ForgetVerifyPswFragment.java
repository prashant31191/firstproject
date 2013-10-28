package com.showu.baogu.fragment.user;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.showu.baogu.R;
import com.showu.baogu.application.MyPref;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.baogu.listener.IForgetPswNewPswListener;
import com.showu.baogu.listener.IRegistVerifyListener;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.TextUtil;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_forget_new_psw)
public class ForgetVerifyPswFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_forget_new_psw_psw)
    EditText psw;
    @InjectView(id = R.id.fragment_forget_new_psw_send)
    Button send;

    private IForgetPswNewPswListener listener;
    public ForgetVerifyPswFragment(IForgetPswNewPswListener l) {
        listener = l;
    }

    @Override
    public void bindDataForUIElement() {
    }

    @Override
    protected void bindEvent() {
        send.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_forget_new_psw_send :
                    String pswtext = psw.getText().toString();
                    if(TextUtil.isEmpty(pswtext)) {
                        GUIUtil.toast(getActivity(), "密码不能为空");
                    } else {
                        listener.updatePsw(pswtext);
                    }
                    break;
                default :
                    break;
            }
        }
    };


}