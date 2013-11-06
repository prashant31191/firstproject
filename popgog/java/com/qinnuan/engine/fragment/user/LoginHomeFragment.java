package com.qinnuan.engine.fragment.user;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.listener.ILoginListener;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.engine.R;
import com.qinnuan.common.util.NetWorkUtile;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_login_home)
public class LoginHomeFragment extends BaseFragment implements View.OnClickListener {

    @InjectView(id = R.id.fragment_login_home_qq)
    View qq;
    @InjectView(id = R.id.fragment_login_home_weibo)
    View weibo;
    @InjectView(id = R.id.fragment_login_home_name)
    EditText name;
    @InjectView(id = R.id.fragment_login_home_psw)
    EditText psw;
    @InjectView(id = R.id.fragment_login_home_login)
    Button login;
    @InjectView(id = R.id.fragment_login_home_forget)
    View forget;
    @InjectView(id = R.id.fragment_login_home_fail)
    TextView fail;

    private ILoginListener listener;
    public LoginHomeFragment(ILoginListener listener){
        this.listener=listener;
    }

    @Override
    public void bindDataForUIElement() {

    }

    @Override
    protected void bindEvent() {
        qq.setOnClickListener(this);
        weibo.setOnClickListener(this);
        login.setOnClickListener(this);
        forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fragment_login_home_qq :
                if(NetWorkUtile.isHasNetwork(getActivity())) {
                    listener.loginByQQ();
                } else {
                    GUIUtil.toast(getActivity(), "网络连接出错,请检查您的网络！");
                }
                break;
            case R.id.fragment_login_home_weibo :
                if(NetWorkUtile.isHasNetwork(getActivity())) {
                    listener.loginByWeibo();
                } else {
                    GUIUtil.toast(getActivity(), "网络连接出错,请检查您的网络！");
                }
                break;
            case R.id.fragment_login_home_login :
                String nameStr = name.getText().toString();
                String pswStr = psw.getText().toString();
                if(TextUtil.isEmpty(nameStr) || TextUtil.isEmpty(pswStr)) {
                    GUIUtil.toast(getActivity(), "名字或者密码都不能为空");
                } else {
                    listener.login(nameStr, pswStr);
                }
                break;
            case R.id.fragment_login_home_forget :
                listener.forgetPass();
                break;

            default :
                break;
        }
    }

    /**
     * @param type  1=>qq 2=>weibo
     */
    public void setFailTips(int type) {
        fail.setVisibility(View.VISIBLE);
        if(type ==1) {
            fail.setText(getString(R.string.login_fail_qq));
        } else if(type == 2){
            fail.setText(getString(R.string.login_fail_weibo));
        }
    }


    @Override
    public void rightClick() {
        listener.regist();
    }



}