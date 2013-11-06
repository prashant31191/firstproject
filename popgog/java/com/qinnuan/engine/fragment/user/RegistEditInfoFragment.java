package com.qinnuan.engine.fragment.user;

import android.graphics.Bitmap;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.listener.IRegistEditInfoListener;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.common.widget.BottomPopWindow;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_regist_editeinfo)
public class RegistEditInfoFragment extends BaseFragment implements BottomPopWindow.OnMenuSelect {

    @InjectView(id = R.id.fragment_regist_editeinfo_head)
    ImageView head;
    @InjectView(id = R.id.fragment_regist_editeinfo_take)
    View take;
    @InjectView(id = R.id.fragment_regist_editeinfo_name)
    EditText nick;
    @InjectView(id = R.id.fragment_regist_editeinfo_psw)
    EditText psw;
    @InjectView(id = R.id.fragment_regist_editeinfo_sex_radioG)
    RadioGroup sex;
//    @InjectView(id = R.id.fragment_regist_editeinfo_male)
//    RadioButton male;
//    @InjectView(id = R.id.fragment_regist_editeinfo_female)
//    RadioButton female;
    @InjectView(id = R.id.fragment_regist_editeinfo_regist)
    Button regist;

    private IRegistEditInfoListener listener;
    public RegistEditInfoFragment(IRegistEditInfoListener l) {
        listener = l;
    }

    @Override
    public void bindDataForUIElement() {
        nick.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
    }

    String male = "1";
    String female = "2";
    String sextext = "0";
    @Override
    protected void bindEvent() {
        regist.setOnClickListener(onClickListener);
        take.setOnClickListener(onClickListener);
        head.setOnClickListener(onClickListener);
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //int checkid = checkedId;
                int checkid = group.getCheckedRadioButtonId();
                LogUtil.e(getClass(), "checkId==>" + checkedId);
                if (checkid == R.id.fragment_regist_editeinfo_male) {
                    sextext = male;
                } else if (checkedId == R.id.fragment_regist_editeinfo_female) {
                    sextext = female;
                }
                LogUtil.e(getClass(), "sex==>"+sextext);
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_regist_editeinfo_regist :
                    if(!isParamNull()) {
                        listener.regist(nickText, pswText, sextext);
                    }
                    break;
                case R.id.fragment_regist_editeinfo_take :
                    showPop();
                    break;
                case R.id.fragment_regist_editeinfo_head:
                    showPop();
                    break;
                default :
                    break;
            }
        }
    };

    private void showPop() {
        String[] menus=getResources().getStringArray(R.array.app_add_photo) ;
        showButtomPop(RegistEditInfoFragment.this,tWidget,menus);
    }

    private String nickText;
    private String pswText;
    //private String sexText;
    public boolean isParamNull() {
        //boolean hasNull  = false;
        nickText = nick.getText().toString();
        pswText = psw.getText().toString();
        //sexText = checkSex();

        if(TextUtil.isEmpty(nickText)) {
            GUIUtil.toast(getActivity(), "名字不能为空");
            return true;
        } else {
            nickText = nickText.trim();
        }

        if(TextUtil.isEmpty(pswText)) {
            GUIUtil.toast(getActivity(), "密码不能为空");
            return true;
        } else {
            pswText = pswText.trim();
            if(pswText.length()<4) {
                GUIUtil.toast(getActivity(), "密码不得小于4位");
                return true;
            }
        }

        if(sextext.equals("0")) {
            GUIUtil.toast(getActivity(), "请选择性别");
            return true;
        }
        return false;
    }

//    private String checkSex() {
//
//        String male = "1";
//        String female = "2";
//        String sextext = male;
//
//        Integer checkid = sex.getCheckedRadioButtonId();
//        if (checkid != null) {
//            if (checkid == R.id.fragment_regist_editeinfo_nan) {
//                sextext = male;
//            } else {
//                sextext = female;
//            }
//        }
//        return sextext;
//    }

   public void setHeadImg(Bitmap bitmap) {
       if(bitmap != null) {
           head.setImageBitmap(bitmap);
       }
   }

    @Override
    public void onItemMenuSelect(int position) {
        buttomPop.dismiss();
        listener.setHeadImg(position);
    }

    @Override
    public void onCancelSelect() {
        buttomPop.dismiss();
    }
}