package com.qinnuan.engine.fragment.film;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qinnuan.engine.activity.film.OnLineSeatActivity;
import com.qinnuan.engine.bean.film.OrderBean;
import com.qinnuan.engine.bean.film.Seat;
import com.qinnuan.engine.bean.film.ShowInfoBean;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.showu.baogu.R;

import java.util.List;

/**
 * Created by showu on 13-7-29.
 */
@FragmentView(id= R.layout.fragment_order_paysuccess)
public class PaySuccessFragment extends BaseFragment implements View.OnClickListener {
    @InjectView(id=R.id.fragment_order_paysuccess_date)
    private TextView timeTextView ;
    @InjectView(id=R.id.fragment_order_paysuccess_no)
    private TextView orderNoTextView;
    @InjectView(id=R.id.fragment_order_paysuccess_cinema)
    private TextView cinemaNameTextView;
    @InjectView(id=R.id.fragment_order_paysuccess_film)
    private TextView filmNameTextView;
    @InjectView(id=R.id.fragment_order_paysuccess_screen)
    private TextView screenTextView;
    @InjectView(id=R.id.fragment_order_paysuccess_seat)
    private TextView seatTextView;
    @InjectView(id=R.id.fragment_order_paysuccess_phone)
    private TextView phoneTextView;
    @InjectView(id=R.id.fragment_order_paysuccess_status)
    private TextView totalPrice ;
    @InjectView(id=R.id.fragment_order_paysuccess__share)
    private Button shareButton ;

    private String filmName ;
    private  String  cinemaName;
    private ShowInfoBean showInfoBean ;
    private OrderBean orderBean ;
    private List<Seat> seatList ;
    private String phoneNumber;

    public PaySuccessFragment() {
    }

    public PaySuccessFragment(String phoneNumber,String filmName, String cinimaName, ShowInfoBean showInfoBean, OrderBean orderBean,List<Seat> list) {
        this.filmName = filmName;
       this.cinemaName=cinimaName ;
        this.showInfoBean = showInfoBean;
        this.orderBean = orderBean;
        this.seatList=list;
        this.phoneNumber=phoneNumber;
    }

    @Override
    public void bindDataForUIElement() {
        timeTextView.setText(orderBean.getOrdertime());
        filmNameTextView.setText(filmName);
        cinemaNameTextView.setText(cinemaName);
        screenTextView.setText(showInfoBean.getShowdate()+"  "+showInfoBean.getShowtime());
        StringBuilder text = new StringBuilder() ;
        for (Seat s : seatList) {
            text.append(s.getRowid() + "排" + s.getColumnid() + "座    ");
        }
       seatTextView.setText(text.toString());
        phoneTextView.setText(phoneNumber);
        float price = Float.parseFloat(showInfoBean.getPrice())*seatList.size();
        totalPrice.setText(price+"");
    }

    @Override
    protected void bindEvent() {
        shareButton.setOnClickListener(this);
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_order_paysuccess__share:
                ((OnLineSeatActivity)getActivity()).share();
                break;
        }
    }

}
