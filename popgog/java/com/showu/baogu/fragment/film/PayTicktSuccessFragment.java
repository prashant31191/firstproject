package com.showu.baogu.fragment.film;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.film.EtickitActivity;
import com.showu.baogu.activity.film.OnLineSeatActivity;
import com.showu.baogu.bean.CinemaBean;
import com.showu.baogu.bean.film.EtitckBean;
import com.showu.baogu.bean.film.OrderBean;
import com.showu.baogu.bean.film.Seat;
import com.showu.baogu.bean.film.ShowInfoBean;
import com.showu.baogu.bean.film.TickitOrderBean;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;

import java.util.List;

/**
 * Created by showu on 13-7-29.
 */
@FragmentView(id= R.layout.fragment_tickit_paysuccess)
public class PayTicktSuccessFragment extends BaseFragment implements View.OnClickListener {
    @InjectView(id=R.id.fragment_order_paysuccess_date)
    private TextView timeTextView ;
    @InjectView(id=R.id.fragment_order_paysuccess_no)
    private TextView orderNoTextView;
    @InjectView(id=R.id.fragment_order_paysuccess_cinema)
    private TextView cinemaNameTextView;
    @InjectView(id=R.id.fragment_order_paysuccess_type)
    private TextView typeTextView;
    @InjectView(id=R.id.fragment_order_paysuccess_validate)
    private TextView validateTextView;
    @InjectView(id=R.id.fragment_order_paysuccess_phone)
    private TextView phoneTextView;
    @InjectView(id=R.id.fragment_order_paysuccess_status)
    private TextView totalPrice ;
    @InjectView(id=R.id.fragment_order_paysuccess__how)
    private Button howToGetTicket ;
    private String phoneNumber;
    private EtitckBean bean ;
    private TickitOrderBean tickitOrderBean;
    private float price  ;
    public PayTicktSuccessFragment() {
    }

    public PayTicktSuccessFragment(String phoneNumber, EtitckBean bean, TickitOrderBean tickitOrderBean ,float totalPrice) {
        this.phoneNumber=phoneNumber;
        this.bean=bean ;
        this.tickitOrderBean=tickitOrderBean ;
        this.price=totalPrice;
    }

    @Override
    public void bindDataForUIElement() {
        timeTextView.setText(tickitOrderBean.getOrdertime());
        cinemaNameTextView.setText(bean.getParentCinema().getCinemaname());
        phoneTextView.setText(phoneNumber);
        validateTextView.setText(bean.getValiddate());
        String type="2D" ;
        if(bean.getTickettype()==1){
            type="3D" ;
        }
        typeTextView.setText(type);
        totalPrice.setText(price+"");
    }

    @Override
    protected void bindEvent() {
       howToGetTicket.setOnClickListener(this);
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        ((EtickitActivity)getActivity()).showHelpWap();
    }

}
