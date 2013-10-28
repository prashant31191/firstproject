package com.showu.baogu.fragment.film;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.film.EtickitActivity;
import com.showu.baogu.activity.film.OnLineSeatActivity;
import com.showu.baogu.bean.film.CrashBean;
import com.showu.baogu.bean.film.EtitckBean;
import com.showu.baogu.bean.film.OrderBean;
import com.showu.baogu.bean.film.Seat;
import com.showu.baogu.bean.film.ShowInfoBean;
import com.showu.baogu.bean.film.TickitOrderBean;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.common.util.DateUtil;
import com.showu.common.util.GUIUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by showu on 13-7-29.
 */
@FragmentView(id = R.layout.fragment_tickit_pay_choice)
public class TickitPayChoiceFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    @InjectView(id = R.id.film_name)
    private TextView ticktType;
    @InjectView(id = R.id.film_cinema_name)
    private TextView cinemaNameText;
    @InjectView(id = R.id.film_screen)
    private TextView validateText;
    @InjectView(id = R.id.totalPrice)
    private TextView totalPriceText;
    @InjectView(id = R.id.yuer)
    private TextView yeerTextView;
    @InjectView(id = R.id.radio_group)
    private RadioGroup radioGroup;
    @InjectView(id = R.id.submit)
    private Button btnSubmit;
    @InjectView(id = R.id.spinner)
    private Spinner spinner;
    @InjectView(id = R.id.crash)
    private TextView crashTextView;

    public static final int PAYT_CLINET = 2;
    public static final int PAYT_ONLINE = 1;
    public static final int PAYT_UNITION = 3;
    public static final int PAYT_ACCOUNT = 0;

    private int payType = -1;
    private long minSeconds = 900000;
    private boolean isStart=false;
    public static  float newPrice ;
    private TickitOrderBean orderBean ;
//    private EtitckBean bean ;
    private float totalcashremain ;
     private String cinemaName ;
    private String validate ;
    private int type ;
    private float price ;
    private int crash = 0;
    private String crashId ;
    private int acccount ;
    private EticktOrderFragment.IAddOrderTickt addOrderTickt ;
    public TickitPayChoiceFragment(TickitOrderBean orderBean,String cinemaName,String validate,int type,int account,float price,float totalcashremain,EticktOrderFragment.IAddOrderTickt addOrderTickt) {
        this.orderBean=orderBean ;
        this.cinemaName=cinemaName ;
        this.validate=validate ;
        this.type=type ;
        this.price=price ;
        this.totalcashremain=totalcashremain ;
        this.addOrderTickt=addOrderTickt ;
        this.acccount=account;
    }

    @Override
    public void bindDataForUIElement() {
        String typeName ="2D" ;
        if(type==1){
            typeName="3D" ;
        }
        ticktType.setText(typeName);
        cinemaNameText.setText(cinemaName);
        validateText.setText(validate);
        yeerTextView.setText(totalcashremain+"");
        newPrice=price*acccount ;
        totalPriceText.setText(newPrice+"");
    }

    @Override
    protected void bindEvent() {
        radioGroup.setOnCheckedChangeListener(this);
        btnSubmit.setOnClickListener(this);
        final List<String> con = new ArrayList<String>();
        for (CrashBean bean : orderBean.getUcashcoupon()) {
            con.add(bean.getCashamount()+"");
        }
        if(con.size()==0){
            con.add("暂无代金券");
        }else{
            con.add("不使用代金券") ;
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, con);
        //设定下拉选单的样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //设定项目被选取之后的动作
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if(position<con.size()-1){
                    crash = Integer.parseInt(con.get(position));
                }else{
                    crash=0 ;
                }
                if (crash > 0) {
                    crashTextView.setVisibility(View.VISIBLE);
                    crashTextView.setText("(立减" + crash + ")");
                    newPrice=price*acccount-crash ;
                    totalPriceText.setText(newPrice+"");
                    crashId=orderBean.getUcashcoupon().get(position).getUcashcouponid();
                }else{
                    newPrice=price*acccount;
                    totalPriceText.setText(newPrice+"");
                    crashId=null;
                    crashTextView.setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView arg0) {
            }
        });
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
        if (minSeconds > 0) {
            if (payType != -1) {
                addOrderTickt.confireOrder(payType,crashId);
            } else {
                GUIUtil.toast(getActivity(), R.string.select_pay_type);
            }
        } else {
             GUIUtil.toast(getActivity(),R.string.time_out);
        }
    }

}
