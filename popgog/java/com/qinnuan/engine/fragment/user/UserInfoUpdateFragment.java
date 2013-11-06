package com.qinnuan.engine.fragment.user;

import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qinnuan.engine.activity.user.UserInfoActivity;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.application.Const;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FragmentView(id = R.layout.fragment_user_info_update)
public class UserInfoUpdateFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_user_info_update) EditText update;
    @InjectView(id = R.id.fragment_user_info_update_save) Button save;
    @InjectView(id = R.id.fragment_user_info_tips) TextView tips;

    private String updatetext;

    private IUserInfoUpdateListener listener;
    public interface IUserInfoUpdateListener {
        public void update(String updatetext);
    }

    public UserInfoUpdateFragment(String updatetext, IUserInfoUpdateListener l) {
        this.updatetext = updatetext;
        listener = l;
    }

    @Override
    public void bindDataForUIElement() {
        if(UserInfoActivity.type == UserInfoActivity.UPDATETYPE.NICKNAME) {
            update.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        } else if(UserInfoActivity.type == UserInfoActivity.UPDATETYPE.POPID) {
            tips.setVisibility(View.VISIBLE);
        }
        //update.setMaxWidth(16);
        update.setText(updatetext);
    }

    @Override
    protected void bindEvent() {
        save.setOnClickListener(onClickListener);
    }

    private String textTmp;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            textTmp = update.getText().toString();
            if(!TextUtil.isEmpty(textTmp)) {
                if(UserInfoActivity.type == UserInfoActivity.UPDATETYPE.POPID) {
                    Pattern p= Pattern.compile("[a-z|A-Z][0-9a-z|A-Z-_]{3,19}") ;
                    Matcher m = p.matcher(textTmp);
                    if(m.matches()) {
                        listener.update(textTmp);
                    } else {
                        GUIUtil.toast(getActivity(), "爆谷号格式不对,请输入正确格式！");
                    }
                } else {
                    listener.update(textTmp);
                }

            }
        }
    };

//    private String textTmp;
//    @Override
//    public void rightClick() {
//        textTmp = update.getText().toString();
//        if(!TextUtil.isEmpty(textTmp)) {
//            listener.update(textTmp);
//        }
//    }

    public String refreshUserInfo(UserInfoActivity.UPDATETYPE type) {
        String texttmp = "";
        if(type == UserInfoActivity.UPDATETYPE.HEADIMG) {
            //
        } else if(type == UserInfoActivity.UPDATETYPE.NICKNAME) {
            Const.user.setNickname(textTmp);
        } else if(type == UserInfoActivity.UPDATETYPE.POPID) {
            Const.user.setPopgogid(textTmp);
        } else if(type == UserInfoActivity.UPDATETYPE.SIGN) {
            Const.user.setSignature(textTmp);
        }
        return texttmp;
    }

}