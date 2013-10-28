package com.showu.baogu.fragment.user;

import com.showu.baogu.R;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.listener.IOnItemClickListener;

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