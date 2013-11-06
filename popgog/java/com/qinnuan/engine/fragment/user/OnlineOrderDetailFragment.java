package com.qinnuan.engine.fragment.user;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.activity.user.OnlineOrderDetailActivity;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.bean.user.OnlineOrder;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.common.http.image.util.ImageFetcher;

/**
 * Created by showu on 13-8-31.
 */
@FragmentView(id= R.layout.activity_online_order_detail)
public class OnlineOrderDetailFragment extends BaseFragment {
    @InjectView(id=R.id.online_order_detail_date)
    private TextView date;
    @InjectView(id=R.id.online_order_detail_uorderid)
    private TextView uorderid;
    @InjectView(id=R.id.online_order_detail_cinema)
    private TextView cinema;
    @InjectView(id=R.id.online_order_detail_film)
    private TextView film;
    @InjectView(id=R.id.online_order_detail_screen)
    private TextView screen;
    @InjectView(id=R.id.online_order_detail_seat)
    private TextView seat;
    @InjectView(id=R.id.online_order_detail_phone)
    private TextView phone;
    @InjectView(id=R.id.online_order_detail_count)
    private TextView acount;
    @InjectView(id=R.id.online_order_detail_ticketno)
    private TextView ticketno;
    @InjectView(id=R.id.online_order_detail_sayfriend)
    private Button sayfriend;
    @InjectView(id=R.id.online_order_detail_howgetticket)
    private Button howget;
    @InjectView(id=R.id.online_order_detail_qr)
    private ImageView qur;

    private OnlineOrder order;
    private ImageFetcher imageFetcher ;
    public OnlineOrderDetailFragment() {
    }
    public OnlineOrderDetailFragment(OnlineOrder order,ImageFetcher imageFetcher) {
        this.order=order;
        this.imageFetcher=imageFetcher ;
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    @Override
    public void bindDataForUIElement() {
        date.setText(order.getCreatedate());
        uorderid.setText(order.getUorderid());
        cinema.setText(order.getCinemaname());
        film.setText(order.getFilmname());
        screen.setText(order.getShowdate());
        seat.setText(order.getSeatlist());
        phone.setText(order.getPhone());
        acount.setText(order.getTotalprice());
        ticketno.setText(order.getPconfirmationid());
        imageFetcher.loadImage(order.getQrcodeurl(), qur);
        LogUtil.e(getClass(), "qurl=>" + order.getQrcodeurl());
    }

    @Override
    protected void bindEvent() {
        sayfriend.setOnClickListener(onClickL);
        howget.setOnClickListener(onClickL);
    }

    private View.OnClickListener onClickL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.online_order_detail_sayfriend:
                    ((OnlineOrderDetailActivity)getActivity()).showShare();
                    break;
                case R.id.online_order_detail_howgetticket:
                    ((OnlineOrderDetailActivity)getActivity()).howToGetTicket();
                    break;
                default:break;
            }
        }
    };
}
