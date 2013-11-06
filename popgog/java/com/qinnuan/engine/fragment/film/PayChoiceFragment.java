package com.qinnuan.engine.fragment.film;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.qinnuan.engine.activity.film.OnLineSeatActivity;
import com.qinnuan.engine.bean.film.CrashBean;
import com.qinnuan.engine.bean.film.OrderBean;
import com.qinnuan.engine.bean.film.Seat;
import com.qinnuan.engine.bean.film.ShowInfoBean;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.common.util.DateUtil;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.engine.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by showu on 13-7-29.
 */
@FragmentView(id = R.layout.fragment_pay_choice)
public class PayChoiceFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    @InjectView(id = R.id.film_name)
    private TextView filmNameText;
    @InjectView(id = R.id.film_cinema_name)
    private TextView cinemaNameText;
    @InjectView(id = R.id.film_screen)
    private TextView screenText;
    @InjectView(id = R.id.totalPrice)
    private TextView totalPriceText;
    @InjectView(id = R.id.yuer)
    private TextView yeerTextView;

    @InjectView(id = R.id.film_seat)
    private TextView seatTextView;
    @InjectView(id = R.id.film_sigle)
    private TextView siglePriceText;
    @InjectView(id = R.id.film_count)
    private TextView needTotalPriceText;

    @InjectView(id = R.id.radio_group)
    private RadioGroup radioGroup;
    @InjectView(id = R.id.submit)
    private Button btnSubmit;
    @InjectView(id = R.id.hour_1)
    private ImageView hour1;
    @InjectView(id = R.id.hour_2)
    private ImageView hour2;
    @InjectView(id = R.id.hour_point)
    private ImageView point;
    @InjectView(id = R.id.min_1)
    private ImageView min1;
    @InjectView(id = R.id.min_2)
    private ImageView min2;
    @InjectView(id = R.id.spinner)
    private Spinner spinner;
    @InjectView(id = R.id.crash)
    private TextView crashTextView;
    @InjectView(id = R.id.drop_dowan)
    private ImageView dropDownImage;
    @InjectView(id = R.id.dropLayout)
    private LinearLayout dropLayout;
    public static final int PAYT_CLINET = 2;
    public static final int PAYT_ONLINE = 1;
    public static final int PAYT_UNITION = 3;
    public static final int PAYT_ACCOUNT = 0;

    private List<Seat> seatList;
    private String filmName;
    private String cinemaName;
    private ShowInfoBean showInfoBean;
    private OrderBean orderBean;
    private int payType = -1;
    private long minSeconds = 900000;
    private boolean isStart = false;
    private int crash = 0;
    private float price;
    public static float realPrice = 0.0f;
    private String crashId;


    private int[] pNumber = {R.drawable.p_0, R.drawable.p_1, R.drawable.p_2,
            R.drawable.p_3, R.drawable.p_4, R.drawable.p_5, R.drawable.p_6,
            R.drawable.p_7, R.drawable.p_8, R.drawable.p_9,};

    private Handler handler = new Handler();

    public PayChoiceFragment(List<Seat> seatList, String filmName, String cinemaName, ShowInfoBean showInfoBean, OrderBean orderBean) {
        this.seatList = seatList;
        this.filmName = filmName;
        this.cinemaName = cinemaName;
        this.showInfoBean = showInfoBean;
        this.orderBean = orderBean;
    }

    @Override
    public void bindDataForUIElement() {
        filmNameText.setText(filmName);
        cinemaNameText.setText(cinemaName);
        StringBuffer text = new StringBuffer("");
        for (Seat s : seatList) {
            text.append(s.getRowid() + "排" + s.getColumnid() + "座    ");
        }
        siglePriceText.setText(showInfoBean.getPrice());
        needTotalPriceText.setText(Float.parseFloat(showInfoBean.getPrice()) * seatList.size() + "");
        screenText.setText(showInfoBean.getHallname());
        seatTextView.setText(text.toString());
        price = Float.parseFloat(showInfoBean.getPrice()) * seatList.size();
        realPrice=price;
        totalPriceText.setText(price + "");
        yeerTextView.setText("￥" + orderBean.getTotalcashremain());
        if (!isStart) {
            handler.post(timeRun);
            isStart = true;
        }
        creatUpdateDialog();
    }

    private void creatUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.pay_tips)).setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).setTitle("提示");
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void bindEvent() {
        radioGroup.setOnCheckedChangeListener(this);
        btnSubmit.setOnClickListener(this);
        dropDownImage.setOnClickListener(this);
        final List<String> con = new ArrayList<String>();
        for (CrashBean bean : orderBean.getCrashBeanList()) {
            con.add(bean.getCashamount() + "");
        }
        if(con.size()==0){
        con.add("暂无代金券");
        }else{
           con.add("不使用代金券");
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, con);
        //设定下拉选单的样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //设定项目被选取之后的动作
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if (position < con.size() - 1) {
                    crash = Integer.parseInt(con.get(position));
                } else {
                    crash = 0;
                }
                if (crash > 0) {
                    crashTextView.setVisibility(View.VISIBLE);
                    crashTextView.setText("(立减" + crash + ")");
                    realPrice = price - crash;
                    totalPriceText.setText(realPrice + "");
                    crashId = orderBean.getCrashBeanList().get(position).getUcashcouponid();
                }else{
                    realPrice=price;
                    crashId=null;
                    totalPriceText.setText(realPrice + "");
                    crashTextView.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView arg0) {
            }
        });
    }

    @Override
    public void leftClick() {
        ((OnLineSeatActivity) getActivity()).deletOrder();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.alipy_client:
                payType = PAYT_CLINET;
                break;
            case R.id.alipy_online:
                payType = PAYT_ONLINE;
                break;
            case R.id.unition_pay:
                payType = PAYT_UNITION;
                break;
            case R.id.account_pay:
                payType = PAYT_ACCOUNT;
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit) {
            if (minSeconds > 0) {
                if (payType != -1) {
                    realPrice = price - crash;
                    if(payType == PAYT_ACCOUNT) {
                        float cashRemain = Float.parseFloat(orderBean.getTotalcashremain());
                        if(realPrice > cashRemain) {
                            GUIUtil.toast(getActivity(), "你的爆谷余额不足,请选择其他支付方式或者进行充值!");
                            return;
                        }
                    }
                    ((OnLineSeatActivity) getActivity()).confireOrder(payType, crashId);
                } else {
                    GUIUtil.toast(getActivity(), R.string.select_pay_type);
                }
            } else {
                GUIUtil.toast(getActivity(), R.string.time_out);
            }
        } else if (view.getId() == R.id.drop_dowan) {
            if (dropLayout.getVisibility() == View.VISIBLE) {
                dropLayout.setVisibility(View.GONE);
                dropDownImage.setImageResource(R.drawable.drop_down_a);
            } else {
                dropLayout.setVisibility(View.VISIBLE);
                dropDownImage.setImageResource(R.drawable.drop_up);
            }
        }
    }

    private void initTime(String time) {
        char[] times = time.toCharArray();
        hour1.setImageResource(pNumber[Integer.parseInt(times[0] + "")]);
        hour2.setImageResource(pNumber[Integer.parseInt(times[1] + "")]);
        point.setImageResource(R.drawable.p_point);
        min1.setImageResource(pNumber[Integer.parseInt(times[3] + "")]);
        min2.setImageResource(pNumber[Integer.parseInt(times[4] + "")]);
    }

    private Runnable timeRun = new Runnable() {

        @Override
        public void run() {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(minSeconds);
            minSeconds -= 1000;
            initTime(DateUtil.getDateString(c.getTime(), "mm:ss"));
            if (minSeconds >= 0) {
                handler.postDelayed(timeRun, 1000);
            } else {

            }
        }
    };
}
