package com.qinnuan.engine.fragment.filmFan;

import android.view.View;

import com.qinnuan.engine.R;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.listener.IAddFriendHomeListener;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_add_friendhome)
public class FriendAddHomeFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_add_friendhome_search)
    View search;
    @InjectView(id = R.id.fragment_add_friendhome_contact)
    View contact;
    @InjectView(id = R.id.fragment_add_friendhome_sharefriend)
    View sharef;
    @InjectView(id = R.id.fragment_add_friendhome_sharesina)
    View shares;

    private IAddFriendHomeListener listener;
    public FriendAddHomeFragment(IAddFriendHomeListener l) {
        listener = l;
    }


    @Override
    public void bindDataForUIElement() {

    }

    @Override
    protected void bindEvent() {
        search.setOnClickListener(onClickListener);
        contact.setOnClickListener(onClickListener);
        //sharef.setOnClickListener(onClickListener);
        //shares.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_add_friendhome_search : {
                    listener.search();
                    break;
                }

                case R.id.fragment_add_friendhome_contact : {
                    listener.contact();
                    break;
                }

                case R.id.fragment_add_friendhome_sharefriend : {
                    listener.shareToFriend();
                    break;
                }

                case R.id.fragment_add_friendhome_sharesina : {
                    listener.shareToSina();
                    break;
                }

                default :
                    break;
            }
        }
    };

    @Override
    public void leftClick() {
        getActivity().finish();
    }
}