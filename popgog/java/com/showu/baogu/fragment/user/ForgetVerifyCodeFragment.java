package com.showu.baogu.fragment.user;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.showu.baogu.R;
import com.showu.baogu.application.MyPref;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.baogu.listener.IForgetPswVerifycodeListener;
import com.showu.baogu.listener.IRegistVerifyListener;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.TextUtil;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_forget_verifycode_verify)
public class ForgetVerifyCodeFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_forget_verifycode_verify_code)
    EditText verifycode;
    @InjectView(id = R.id.fragment_forget_verifycode_verify_send)
    Button sendcode;

    private IForgetPswVerifycodeListener listener;
    public ForgetVerifyCodeFragment(IForgetPswVerifycodeListener l) {
        listener = l;
    }

    @Override
    public void bindDataForUIElement() {
    }

    @Override
    protected void bindEvent() {
        sendcode.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_forget_verifycode_verify_send :
                    String verifycodetext = verifycode.getText().toString();
                    if(TextUtil.isEmpty(verifycodetext)) {
                        GUIUtil.toast(getActivity(), "验证码不能为空");
                    } else {
                        listener.sendVerifyCode(verifycodetext);
                    }
                    break;
                default :
                    break;
            }
        }
    };


}