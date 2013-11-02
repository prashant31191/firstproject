package com.qinnuan.engine.fragment.user;

import android.widget.TextView;

import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.showu.baogu.R;

@FragmentView(id = R.layout.fragment_phone_bind_success)
public class PhoneBindSuccessFragment extends BaseFragment {


    @InjectView(id = R.id.fragment_phone_bind_success_phone) TextView phone;

    private String phonetext;

    private IUserInfoUpdateListener listener;
    public interface IUserInfoUpdateListener {
        public void update(String updatetext);
    }

    public PhoneBindSuccessFragment(String phonetext) {
        this.phonetext = phonetext;
    }

    @Override
    public void bindDataForUIElement() {
        phone.setText(phonetext);
    }

    @Override
    protected void bindEvent() {

    }





}