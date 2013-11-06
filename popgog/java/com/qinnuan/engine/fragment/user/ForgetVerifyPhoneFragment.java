package com.qinnuan.engine.fragment.user;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.listener.IForgetPswPhoneListener;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_forget_verifycode_create)
public class ForgetVerifyPhoneFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_forget_verifycode_phone_phone)
    EditText phone;
    @InjectView(id = R.id.fragment_forget_verifycode_phone_send)
    Button verify;

    private IForgetPswPhoneListener listener;
    public ForgetVerifyPhoneFragment(IForgetPswPhoneListener l) {
        listener = l;
    }

    @Override
    public void bindDataForUIElement() {
    }

    @Override
    protected void bindEvent() {
        verify.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_forget_verifycode_phone_send :
                    String phonetext = phone.getText().toString();
                    if(TextUtil.isEmpty(phonetext)) {
                        GUIUtil.toast(getActivity(), "手机号码不能为空");
                    } else if(!TextUtil.isMobilePhone(phonetext)){
                        GUIUtil.toast(getActivity(), "手机号码不正确");
                    } else {
                        listener.sendPhone(phonetext);
                    }
                    break;
                default :
                    break;
            }
        }
    };

    @Override
    public void leftClick() {
        getActivity().finish();
    }
}