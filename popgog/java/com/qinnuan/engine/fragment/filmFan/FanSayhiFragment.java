package com.qinnuan.engine.fragment.filmFan;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.listener.IFanSayhiListener;
import com.showu.baogu.R;
import com.qinnuan.engine.fragment.InjectView;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_fan_sayhi)
public class FanSayhiFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_fan_sayhi_content) EditText content;
    @InjectView(id = R.id.fragment_fan_sayhi_send) Button send;

    private IFanSayhiListener listener;
    public FanSayhiFragment(IFanSayhiListener l) {
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
                case R.id.fragment_fan_sayhi_send :
                    String contentStr = content.getText().toString();
//                    if(!TextUtil.isEmpty(contentStr)) {
//                        listener.send(contentStr);
//                    }
                      listener.send(contentStr);
                    break;

                default :
                    break;
            }
        }
    };
}