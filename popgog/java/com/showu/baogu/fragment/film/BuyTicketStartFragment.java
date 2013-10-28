package com.showu.baogu.fragment.film;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.showu.baogu.R;
import com.showu.baogu.application.Const;
import com.showu.baogu.bean.film.Seat;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.baogu.listener.IOnListVItemClick;
import com.showu.common.http.image.util.ImageFetcher;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.TextUtil;
import com.showu.common.widget.CircleImageView;

/**
 * Created by showu on 13-7-25.
 */
@FragmentView(id=R.layout.fragment_buy_ticket_start)
public class BuyTicketStartFragment extends BaseFragment {

    @InjectView(id=R.id.activity_buy_ticket_start_buy)
    private Button buy;
    @InjectView(id=R.id.activity_buy_ticket_start_head)
    private ImageView head;
    @InjectView(id=R.id.activity_buy_ticket_start_publish)
    private CheckBox publish;

    private boolean isPublish = true;

    public boolean isPublish() {
        return isPublish;
    }

    public void setPublish(boolean publish) {
        isPublish = publish;
    }

    private IOnListVItemClick listener;
    private ImageFetcher imgFet;
    public BuyTicketStartFragment(IOnListVItemClick l, ImageFetcher imgF) {
        listener = l;
        imgFet = imgF;
    }

    @Override
    public void bindDataForUIElement() {
        setHead();
    }

    @Override
    protected void bindEvent() {
        buy.setOnClickListener(onClickListener);
        publish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //isPublish = !isChecked;
                setPublish(!isChecked);
            }
        });
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    private View.OnClickListener onClickListener =
            new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.activity_buy_ticket_start_buy: {
                    listener.onItemClick(v);
                    break;
                }

                case R.id.activity_buy_ticket_start_publish: {
                    break;
                }

                default:break;
            }
        }
    };

    private void setHead() {
        //imgFet.loadImage(Const.user.getProfileimg(), head, GUIUtil.getDpi(getActivity(), R.dimen.margin_100));
        imgFet.loadImage(Const.user.getProfileimg(), head);
    }

}
