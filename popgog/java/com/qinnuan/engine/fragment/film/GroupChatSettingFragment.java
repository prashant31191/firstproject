package com.qinnuan.engine.fragment.film;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.qinnuan.engine.activity.film.GroupActivity;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.R;
import com.qinnuan.engine.bean.film.GroupBean;
import com.qinnuan.engine.fragment.InjectView;

/**
 * Created by showu on 13-8-13.
 */
@FragmentView(id = R.layout.fragment_group_chat_setting)
public class GroupChatSettingFragment extends BaseFragment implements View.OnClickListener ,CompoundButton.OnCheckedChangeListener{
    @InjectView(id = R.id.delete)
    private Button deleteBtn;
    @InjectView(id=R.id.check_notifycation)
    private CheckBox checkBox;
    @InjectView(id=R.id.group_name)
    private TextView groupNameText ;
    @InjectView(id=R.id.nameLayout)
    private View nameLayout ;
    private GroupBean groupBean ;

    public GroupChatSettingFragment() {
    }

    public GroupChatSettingFragment(GroupBean groupBean) {
        this.groupBean=groupBean;
    }

    @Override
    public void bindDataForUIElement() {
        checkBox.setChecked(groupBean.getIsnotice()>0);
        groupNameText.setText(groupBean.getGroupname());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(groupBean!=null){
            bindDataForUIElement();
        }
    }

    @Override
    protected void bindEvent() {
        deleteBtn.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(this);
        nameLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete:
                ((GroupActivity) getActivity()).deleteGroup();
                break;
            case R.id.nameLayout:
                ((GroupActivity) getActivity()).reNameGroup();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        ((GroupActivity)getActivity() ).changeNotice(b);
    }
}
