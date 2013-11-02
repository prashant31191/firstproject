package com.qinnuan.engine.fragment.user;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.TextUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.application.MyPref;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.listener.IRegistVerifyListener;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_regist_verify)
public class RegistVerifyFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_regist_verify_no)
    EditText verifycode;
    @InjectView(id = R.id.fragment_regist_verify_confirm)
    Button verify;

    private IRegistVerifyListener listener;
    public RegistVerifyFragment(IRegistVerifyListener l) {
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
                case R.id.fragment_regist_verify_confirm :
                    String verifycodetext = verifycode.getText().toString();
                    if(TextUtil.isEmpty(verifycodetext)) {
                        GUIUtil.toast(getActivity(), R.string.regist_auth_null);
                    } else {
                        String phone = MyPref.getInstance(getActivity()).getPhoneNo();
                        listener.sendConfrim(phone, verifycodetext.trim());
                    }
                    break;
                default :
                    break;
            }
        }
    };


}