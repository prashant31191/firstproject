package com.qinnuan.engine.fragment.user;

import android.view.View;

import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.R;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.listener.IFilmTicketListener;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_user_filmticket)
public class UserOrderFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_user_online_order)
    View onlineTicket;
    @InjectView(id = R.id.fragment_user_eticket_order)
    View eticket;

    private IFilmTicketListener listener;
    public UserOrderFragment(IFilmTicketListener l) {
        listener = l;
    }

    @Override
    public void bindDataForUIElement() {
    }

    @Override
    protected void bindEvent() {
        onlineTicket.setOnClickListener(onClickListener);
        eticket.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_user_online_order:
                    listener.gotoOnlineOreder();
                    break;
                case R.id.fragment_user_eticket_order:
                    listener.gotoEticketOrder();
                    break;
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