package com.showu.baogu.fragment.filmFan;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.filmFan.FriendAddActivity;
import com.showu.baogu.activity.filmFan.FriendAddHomeActivity;
import com.showu.baogu.activity.user.UserActivity;
import com.showu.baogu.application.Const;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.baogu.listener.IFriendHomeListener;
import com.showu.common.http.image.util.ImageFetcher;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.TextUtil;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_friend)
public class FriendFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_friend_myfriend)
    View myFriend;
    @InjectView(id = R.id.fragment_friend_addfriend)
    View addFriend;
    @InjectView(id = R.id.fragment_friend_nearbyfriend)
    View nearbyFriend;
    @InjectView(id = R.id.fragment_friend_cinemacircle)
    View cinemacircle;
    @InjectView(id = R.id.fragment_friend_count)
    TextView count;
    @InjectView(id = R.id.fragment_friend_fanhead)
    ImageView head;

    private IFriendHomeListener listener;
    public FriendFragment(IFriendHomeListener l) {
        listener = l;
    }

    @Override
    public void bindDataForUIElement() {

    }

    @Override
    protected void bindEvent() {
        myFriend.setOnClickListener(onClickListener);
        addFriend.setOnClickListener(onClickListener);
        nearbyFriend.setOnClickListener(onClickListener);
        cinemacircle.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(Const.user == null) {
                GUIUtil.checkIsOnline(getActivity(), UserActivity.class);
            } else {
                switch (v.getId()) {
                    case R.id.fragment_friend_myfriend :
                        listener.gotoMyFriend();
                        break;
                    case R.id.fragment_friend_nearbyfriend :
                        checkIsShowNearby();
                        break;
                    case R.id.fragment_friend_cinemacircle :
                        listener.gotoCinemaCircle();
                        break;
                    case R.id.fragment_friend_addfriend:
                        startActivity(new Intent(getActivity(), FriendAddHomeActivity.class));
                        break;

                    default :
                        break;
                }
            }
        }
    };

    private void checkIsShowNearby() {
        //是否在附近出现 0=否 1=是
        if(Const.user.getIsshow() == 0) {
//            new AlertDialog.Builder(getActivity())
//            .setTitle("密码设置")
//            .setIcon(android.R.drawable.ic_dialog_info)
//            .setPositiveButton(R.string.yes,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        listener.updateIsShow("1");
//                        listener.gotoFan();
//                    }
//                }).setNegativeButton(R.string.no, null).show();

            GUIUtil.showDialog(getActivity(), "是否要出现在附近列表", "出现", "不", new GUIUtil.IOnAlertDListener() {
                @Override
                public void ok() {
                    listener.updateIsShow("1");
                    listener.gotoFan();
                }

                @Override
                public void cancel() {  }
            });

        } else {
            listener.gotoFan();
        }
    }


    public void fillData(int countStr, String headUrl, ImageFetcher imgf) {
        if(countStr!=0) {
            count.setVisibility(View.VISIBLE);
            count.setText(countStr+"");
        } else {
            count.setVisibility(View.GONE);
            count.setText(null);
        }
        if(!TextUtil.isEmpty(headUrl)) {
            head.setVisibility(View.VISIBLE);
            imgf.loadImage(headUrl, head, GUIUtil.getDpi(getActivity(), R.dimen.margin_8));
        } else {
            head.setVisibility(View.GONE);
            head.setImageDrawable(null);
        }
    }

}