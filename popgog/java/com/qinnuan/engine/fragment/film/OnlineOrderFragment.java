package com.qinnuan.engine.fragment.film;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qinnuan.engine.activity.film.OnLineSeatActivity;
import com.qinnuan.engine.bean.film.Seat;
import com.qinnuan.engine.bean.film.ShowInfoBean;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.engine.R;

import java.util.List;

@FragmentView(id = R.layout.activity_seat_order)
public class OnlineOrderFragment extends BaseFragment implements OnClickListener {
    @InjectView(id = R.id.cinemaName)
    private TextView cinemaNameText;
    @InjectView(id = R.id.film_screen)
    private TextView screenText;
    @InjectView(id = R.id.film_name)
    private TextView filmNameText;
    @InjectView(id = R.id.film_seat)
    private TextView seatText;
    @InjectView(id = R.id.film_sigle)
    private TextView priceText;
    @InjectView(id = R.id.film_count)
    private TextView totalPriceText;
    @InjectView(id = R.id.cellphone)
    private EditText cellPhone;
    @InjectView(id = R.id.acitivity_seat_order_pay_submit)
    private Button button;

    private ShowInfoBean showInfo;
    private List<Seat> seats;
    private String filmName;
    private String cinemaName;

    public OnlineOrderFragment() {
    }

    public OnlineOrderFragment(ShowInfoBean showInfo, List<Seat> seats, String filmName, String cinemaName) {
        this.showInfo = showInfo;
        this.seats = seats;
        this.filmName = filmName;
        this.cinemaName = cinemaName;
    }

    @Override
    public void bindDataForUIElement() {
        filmNameText.setText(filmName);
        cinemaNameText.setText(cinemaName);
        screenText.setText(showInfo.getShowdate() + " " + showInfo.getShowtime() + " " + showInfo.getHallname());
        StringBuffer text = new StringBuffer("");
        for (Seat s : seats) {
            text.append(s.getRowid() + "排" + s.getColumnid() + "座    ");
        }
        seatText.setText(text.toString());
        priceText.setText(showInfo.getPrice());
        totalPriceText.setText("￥" + Float.parseFloat(showInfo.getPrice()) * seats.size());
    }

    @Override
    protected void bindEvent() {
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!TextUtil.isEmpty(cellPhone.getText().toString())) {
            imm.hideSoftInputFromWindow(cellPhone.getWindowToken(), 0);
            if (cellPhone.getText().toString().trim().length() != 11) {
                GUIUtil.toast(getActivity(), "请输入正确的手机号码!");
            } else {
                ((OnLineSeatActivity) getActivity()).addOrder(cellPhone.getText().toString().trim());
            }
        }
    }
}
