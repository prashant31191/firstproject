package com.qinnuan.engine.fragment.user;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.listener.IRegistPhoneListener;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.engine.R;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_regist_phone)
public class RegistPhoneFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_regist_phone_phone)
    EditText phone;
    @InjectView(id = R.id.fragment_regist_phone_getAuthNo)
    Button getAuthNo;

    private IRegistPhoneListener listener;
    public RegistPhoneFragment(IRegistPhoneListener l) {
        listener = l;
    }

    private String phonetext;
    @Override
    public void bindDataForUIElement() {
        phonetext = phone.getText().toString();
    }

    @Override
    protected void bindEvent() {
        getAuthNo.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_regist_phone_getAuthNo :
                    phonetext = phone.getText().toString();
                    if(TextUtil.isEmpty(phonetext)) {
                        GUIUtil.toast(getActivity(), R.string.regist_phone_null);
                    } else {
                        String p = phonetext.trim();
                        if(TextUtil.isMobilePhone(p)) {
                            listener.getAuthNo(p);
                        } else {
                            GUIUtil.toast(getActivity(), "手机号码格式不正确！");
                        }
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