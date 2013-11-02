package com.qinnuan.engine.fragment.filmFan;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qinnuan.engine.bean.Fan;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.common.util.TextUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.listener.IUpdateRemarkListener;
import com.qinnuan.common.util.GUIUtil;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_friendinfo_remark)
public class FriendRemarkFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_friendinfo_remark_name)
    EditText name;
    @InjectView(id = R.id.fragment_friendinfo_remark_save)
    Button save;

    private Fan fan;

    private IUpdateRemarkListener listener;
    public FriendRemarkFragment(Fan f, IUpdateRemarkListener l) {
        fan = f;
        listener = l;
    }

    @Override
    public void bindDataForUIElement() {
        name.setText(fan.getRemarkname());
    }

    @Override
    protected void bindEvent() {
        save.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_friendinfo_remark_save :
                    String nameText = name.getText().toString();
                    if(TextUtil.isEmpty(nameText)) {
                        GUIUtil.toast(getActivity(), "备注名不能为空");
                    } else {
                        nameText.trim();
                        listener.save(nameText);
                    }

                    break;
                default :
                    break;
            }
        }
    };


}