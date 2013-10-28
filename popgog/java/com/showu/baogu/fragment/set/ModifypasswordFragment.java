package com.showu.baogu.fragment.set;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.showu.baogu.R;
import com.showu.baogu.activity.user.SettingActivity;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.TextUtil;

@FragmentView(id=R.layout.activity_modify_password)
public class ModifypasswordFragment extends BaseFragment{

    @InjectView(id=R.id.et_old_pass)
    private EditText mEtOldpassword;
    @InjectView(id=R.id.et_new_pass)
    private EditText mEtNewpassword;
    @InjectView(id=R.id.et_repeat)
    private EditText mEtRepeat;

    private static final int RIGHT_OLD_PASS = 1001;
    private static final int AGAIN_INPUT_ERROR = 1002;

//    private UserLoginRequest userLoginRequest = new UserLoginRequest();


    @Override
    public void rightClick() {
        String newPsw =mEtNewpassword.getText().toString();
        String newPswRep = mEtRepeat.getText().toString();
        String oldPsw = mEtOldpassword.getText().toString();
        if(!TextUtil.isEmpty(newPsw)) {
            newPsw = newPsw.trim();
            if(newPsw.length()<4) {
                GUIUtil.toast(getActivity(), "新密码不得小于4位");
            } else {
                if(!newPsw.equals(newPswRep)) {
                    GUIUtil.toast(getActivity(), "新密码与重复密码不一致!");
                } else {
                    ((SettingActivity)getActivity()).modifyPassWord(oldPsw, newPsw);
                }
            }
        } else {
            GUIUtil.toast(getActivity(), "新密码不能为空");
        }
    }

    @Override
    public void bindDataForUIElement() {

    }

    @Override
    protected void bindEvent() {

    }

    @Override
    public void leftClick() {
        hideInput();
        super.leftClick();
    }

    private void hideInput() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtRepeat.getWindowToken(), 0);
    }

//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case HTTP_OK:
//                    AppContext.getInstance().passExit();
//                    toastString(R.string.modify_password_suc);
//                    break;
//
//                case HTTP_ERROR:
//                    toastString(R.string.modify_password_err);
//                    break;
//
//                case AGAIN_INPUT_ERROR:
//                    UIHelper.ToastMessage(ModifypasswordFragment.this, "两次输入的密码不一样,请重新输入");
//                    break;
//
//                case RIGHT_OLD_PASS:
//                    UIHelper.ToastMessage(ModifypasswordFragment.this, "请输入正确的旧密码");
//                    break;
//
//                default:
//                    break;
//            }
//        }
//
//        ;
//    };


}
