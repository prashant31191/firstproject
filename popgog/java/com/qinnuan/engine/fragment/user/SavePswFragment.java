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
import com.qinnuan.engine.listener.IForgetPswNewPswListener;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_save_psw)
public class SavePswFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_save_psw_psw_repeat)
    EditText pswrepeat;
    @InjectView(id = R.id.fragment_save_psw_psw)
    EditText psw;
    @InjectView(id = R.id.fragment_save_psw_send)
    Button send;

    private IForgetPswNewPswListener listener;

    public SavePswFragment() { }

    public SavePswFragment(IForgetPswNewPswListener l) {
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
                case R.id.fragment_save_psw_send :
                    String pswtext = psw.getText().toString();
                    if(TextUtil.isEmpty(pswtext)) {
                        GUIUtil.toast(getActivity(), "密码不能为空");
                    } else {
                        pswtext = pswtext.trim();
                        if(pswtext.length()<4) {
                            GUIUtil.toast(getActivity(), "密码不能少于4位");
                        } else {
                            listener.updatePsw(pswtext);
                        }
                    }
                    break;
                default :
                    break;
            }
        }
    };


}