package com.showu.baogu.fragment.user;

import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.user.UserInfoActivity;
import com.showu.baogu.application.Const;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.TextUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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