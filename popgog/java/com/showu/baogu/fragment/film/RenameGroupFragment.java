package com.showu.baogu.fragment.film;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.showu.baogu.R;
import com.showu.baogu.activity.film.GroupActivity;
import com.showu.baogu.activity.user.UserInfoActivity;
import com.showu.baogu.application.Const;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.common.util.TextUtil;

@FragmentView(id = R.layout.fragment_user_info_update)
public class RenameGroupFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_user_info_update) EditText update;
    @InjectView(id = R.id.fragment_user_info_update_save) Button save;

    private String updatetext;



    public RenameGroupFragment(String updatetex) {
        this.updatetext=updatetex;
    }

    @Override
    public void bindDataForUIElement() {
        update.setText(updatetext);
        tWidget.setCenterViewText("修改群名字");
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
            if(!TextUtil.isEmpty(textTmp)){
                ((GroupActivity)getActivity()).updateGroupName(textTmp);
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