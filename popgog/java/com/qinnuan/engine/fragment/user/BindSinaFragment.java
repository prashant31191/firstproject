package com.qinnuan.engine.fragment.user;

import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.listener.IOnItemClickListener;
import com.showu.baogu.R;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_binding_sina)
public class BindSinaFragment extends BaseFragment {

    private IOnItemClickListener listener;
    public BindSinaFragment(IOnItemClickListener l) {
        listener = l;
    }

    @Override
    public void bindDataForUIElement() { }

    @Override
    protected void bindEvent() { }

    @Override
    public void rightClick() {
        listener.onClick(tWidget);
    }

}