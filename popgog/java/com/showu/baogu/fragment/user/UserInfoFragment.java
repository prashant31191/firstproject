package com.showu.baogu.fragment.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.ImageDetailActivity;
import com.showu.baogu.activity.user.UserInfoActivity;
import com.showu.baogu.bean.User;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.baogu.listener.IUserInfoListener;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;
import com.showu.common.util.TextUtil;
import com.showu.common.widget.BottomPopWindow;

@FragmentView(id = R.layout.fragment_user_info)
public class UserInfoFragment extends BaseFragment implements BottomPopWindow.OnMenuSelect {

    @InjectView(id = R.id.fragment_user_info_headlayout)
    View headlayout;
    @InjectView(id = R.id.fragment_user_info_nick_layout)
    View nicklayout;
    @InjectView(id = R.id.fragment_user_info_sign_layout)
    View signlayout;
    @InjectView(id = R.id.fragment_user_info_sinaweibo_layout)
    View weibolayout;
    @InjectView(id = R.id.fragment_user_info_popid_layout)
    View popidlayout;
    @InjectView(id = R.id.fragment_user_info_phonebind_layout)
    View phonebindlayout;

    @InjectView(id = R.id.fragment_user_info_head)
    ImageView head;
    @InjectView(id = R.id.fragment_user_info_nick)
    TextView nick;
    @InjectView(id = R.id.fragment_user_info_sign)
    TextView sign;
    @InjectView(id = R.id.fragment_user_info_phonebind)
    TextView phonebind;
    @InjectView(id = R.id.fragment_user_info_popid)
    TextView popid;
    @InjectView(id = R.id.fragment_user_info_sinaweibo)
    TextView weiboText;
    @InjectView(id = R.id.fragment_user_info_sex)
    TextView sex;

    private User user;

    private IUserInfoListener listener;
    public UserInfoFragment(User user, IUserInfoListener l) {
        this.user = user;
        listener = l;
    }

    @Override
    public void bindDataForUIElement() {
        listener.setHeadImg(user.getProfileimg(), head);
        nick.setText(user.getNickname());
        sign.setText(user.getSignature());
        popid.setText(user.getPopgogid());

        int sexint = user.getSex();
        if(sexint == 1) {
            sex.setText("男");
        } else if(sexint == 2) {
            sex.setText("女");
        } else {
            sex.setText("未知");
        }

        if(user.getIsphoneverify()==1) {
            //GUIUtil.toast(getActivity(), "手机已经绑定");
            phonebind.setText(R.string.user_info_bind);
        } else {
            //phonebindlayout.setOnClickListener(onClickListener);
        }
        if(user.getSnsList()!=null && user.getSnsList().size()>0){
            LogUtil.e(getClass(), "sina name=>"+user.getSnsList().get(0).getSnsname());
            weiboText.setText(user.getSnsList().get(0).getSnsname());
        }
    }
    public void bindingSuccess(String name){
        weiboText.setText(name);
    }
    @Override
    protected void bindEvent() {
        headlayout.setOnClickListener(onClickListener);
        headlayout.setOnLongClickListener(longClickListener);
        nicklayout.setOnClickListener(onClickListener);
        signlayout.setOnClickListener(onClickListener);
        popidlayout.setOnClickListener(onClickListener);
        weibolayout.setOnClickListener(onClickListener);
        head.setOnClickListener(onClickListener);
        phonebindlayout.setOnClickListener(onClickListener);
    }

    public UserInfoActivity.UPDATETYPE type;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_user_info_head: {
                    final Intent i = new Intent(getActivity(), ImageDetailActivity.class);
                    i.putExtra("url", user.getProfileimg());
                    startActivity(i);
                }
                break;
                case R.id.fragment_user_info_headlayout: {
                    type = UserInfoActivity.UPDATETYPE.HEADIMG;
//                    listener.updateHead(type);
                    String[] menu=getResources().getStringArray(R.array.app_add_photo) ;
                    showButtomPop(UserInfoFragment.this,tWidget,menu);
                }
                break;
                case R.id.fragment_user_info_nick_layout: {
                    type = UserInfoActivity.UPDATETYPE.NICKNAME;
                    listener.update(getText(nick), type);
                }
                break;
                case R.id.fragment_user_info_sign_layout: {
                    type = UserInfoActivity.UPDATETYPE.SIGN;
                    listener.update(getText(sign), type);
                }
                break;
                case R.id.fragment_user_info_popid_layout: {
                    type = UserInfoActivity.UPDATETYPE.POPID;
                    listener.update(getText(popid), type);
                }
                break;
                case R.id.fragment_user_info_sinaweibo_layout: {
                    listener.bindWeibo();
                }
                break;
                case R.id.fragment_user_info_phonebind_layout: {
//                    if(user.getIsphoneverify()==1) {
//                        GUIUtil.toast(getActivity(), "手机已经绑定");
//                    } else {
//                        listener.bindPhone();
//                    }
                    listener.bindPhone();
                }
                break;
                default:
                    break;
            }
        }
    };

    private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            return false;
        }
    };

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    private String getText(TextView text) {
        String texttmp = text.getText().toString();
        if(!TextUtil.isEmpty(texttmp)) {
            texttmp = texttmp.trim();
        } else {
            texttmp = "";
        }
        return texttmp;
    }

    public void setHeadTmp(Bitmap bitmap) {
        LogUtil.e(getClass(), "bitmap=>"+bitmap);
        if(bitmap != null)
            LogUtil.e(getClass(), "bitmap1=>"+bitmap);
            //head.setImageBitmap(bitmap);
            head.setImageDrawable(new BitmapDrawable(getActivity().getResources(), bitmap));
    }

    public ImageView getheadImgv() {
        return head;
    }

    @Override
    public void onItemMenuSelect(int position) {
        buttomPop.dismiss();
        listener.updateHead(type,position);
    }

    @Override
    public void onCancelSelect() {
        buttomPop.dismiss();
    }
}