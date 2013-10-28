package com.showu.baogu.fragment.user;

import android.graphics.Bitmap;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.showu.baogu.R;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.baogu.listener.IOtherLoginListener;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;
import com.showu.common.util.TextUtil;
import com.showu.common.widget.BottomPopWindow;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_otherlogin_home)
public class OtherLoginHomeFragment extends BaseFragment implements BottomPopWindow.OnMenuSelect {

    @InjectView(id = R.id.fragment_otherlogin_home_head)
    ImageView head;
    @InjectView(id = R.id.fragment_otherlogin_home_take)
    View take;
    @InjectView(id = R.id.fragment_otherlogin_home_name)
    EditText nick;
    @InjectView(id = R.id.fragment_otherlogin_home_sex_radioG)
    RadioGroup sex;
    @InjectView(id = R.id.fragment_otherlogin_home_login)
    Button login;

    private IOtherLoginListener listener;
    public OtherLoginHomeFragment(IOtherLoginListener listener){
        this.listener=listener;
    }

    @Override
    public void bindDataForUIElement() {
        nick.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
    }

    @Override
    protected void bindEvent() {
        login.setOnClickListener(onClickListener);
        take.setOnClickListener(onClickListener);
        head.setOnClickListener(onClickListener);

        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkid = checkedId;
                //int checkid = group.getCheckedRadioButtonId();
                LogUtil.e(getClass(), "checkId==>"+checkedId);
                if (checkid == R.id.fragment_otherlogin_home_male) {
                    sextext = male;
                } else if (checkedId == R.id.fragment_otherlogin_home_female) {
                    sextext = female;
                }
                LogUtil.e(getClass(), "sex==>"+sextext);
            }
        });
    }

    private View.OnClickListener onClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_otherlogin_home_login :
                    if(!isParamNull()) {
                        listener.regist(nickText, sextext);
                    }
                    break;
                case R.id.fragment_otherlogin_home_take :
                    showImgSelect();
                    break;
                case R.id.fragment_otherlogin_home_head: {
                    showImgSelect();
                }
                break;
                default :
                    break;
            }
        }
    };

    private void showImgSelect() {
        String[] menues= getResources().getStringArray(R.array.app_add_photo) ;
        showButtomPop(OtherLoginHomeFragment.this,tWidget,menues);
    }

    private String nickText;
    //private String sexText;
    public boolean isParamNull() {
        //boolean hasNull  = false;
        nickText = nick.getText().toString();
        //sexText = checkSex();

        if(TextUtil.isEmpty(nickText)) {
            GUIUtil.toast(getActivity(), "名字不能为空");
            return true;
        } else {
            nickText = nickText.trim();
        }

        if(sextext.equals("0")) {
            GUIUtil.toast(getActivity(), "请选择性别");
            return true;
        }
        return false;
    }

    String male = "1";
    String female = "2";
    String sextext="0";

//    private String checkSex() {
//
////        String male = "1";
////        String female = "2";
////        String sextext = "0";
//
//        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Integer checkid = checkedId;
//
//                    if (checkid == R.id.fragment_regist_editeinfo_male) {
//                        sextext = male;
//                    } else {
//                        sextext = female;
//                    }
//            }
//        });
//
////        Integer checkid = sex.getCheckedRadioButtonId();
////        if (checkid != null) {
////            if (checkid == R.id.fragment_regist_editeinfo_male) {
////                sextext = male;
////            } else {
////                sextext = female;
////            }
////        }
//        return sextext;
//    }



    public void setHeadImg(Bitmap bitmap) {
        if(bitmap != null) {
            head.setImageBitmap(bitmap);
        }
    }

    public ImageView getHeadImgv() {
        return head;
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

    @Override
    public void leftClick() {
        getActivity().finish();
    }
}