package com.showu.baogu.activity.user;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.BaseActivity;
import com.showu.baogu.adapter.BaoguBaseAdapter;
import com.showu.baogu.application.Const;
import com.showu.baogu.bean.user.EticketOrder;
import com.showu.baogu.bean.user.Qrcode;
import com.showu.baogu.fragment.set.WebFragment;

import java.util.List;

public class EticketOrderDetailActivity extends BaseActivity {

    private EticketOrderDetailActivity instance = this;
    public static final String ORDER_DETAIL_ID = "order_detail_id";
    private EticketOrder order;

    private TextView date;
    private TextView uorderid;
    private TextView cinema;
    private TextView type;
    private TextView phone;
    private TextView acount;
    
    private Button howget;

    private ListView qrs;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eticket_order_detail);
        order = (EticketOrder) getIntent().getSerializableExtra(ORDER_DETAIL_ID);
        initView();
        fillData();
    }

    private void initView() {
        date = (TextView) findViewById(R.id.eticket_order_detail_date);
        uorderid = (TextView) findViewById(R.id.eticket_order_detail_no);
        cinema = (TextView) findViewById(R.id.eticket_order_detail_cinema);
        type = (TextView)findViewById(R.id.eticket_order_detail_type);
        phone = (TextView) findViewById(R.id.eticket_order_detail_phone);
        acount = (TextView) findViewById(R.id.eticket_order_detail_count);
        qrs  = (ListView)findViewById(R.id.activity_eticket_order_detail_qrs);
        howget = (Button)findViewById(R.id.eticket_order_detail_howgetticket);
        howget.setOnClickListener(onClickL);
        getTitleWidget().getLeftL().setOnClickListener(onClickL);
    }

    private void fillData() {
        date.setText(order.getCreatedate());
        uorderid.setText(order.getUorderid());
        cinema.setText(order.getCinemaname());
        int typeint = order.getTickettype();
        //类型 0=2D 1=3D
        if(typeint == 0) {
            type.setText("2D 电子券"+" ("+order.getTicketcount()+"张)");
        } else {
            type.setText("3D 电子券"+" ("+order.getTicketcount()+"张)");
        }
        phone.setText(order.getPhone());
        acount.setText(order.getTotalprice()+"");

        QrAdpter qrAdpter = new QrAdpter(instance, order.getQrcodes());
        qrs.setAdapter(qrAdpter);
       // mImageFetcher.loadImage(order.getQrcodeurl(), qur);
    }
    WebFragment fragment;
    private View.OnClickListener onClickL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.eticket_order_detail_howgetticket:
                    String url = Const.urlBean.getApp_plugin_url() + "/help/orderhelp";
                    fragment = new WebFragment(url, "如何取票");
                    replaceFragment(fragment, true);
                    break;
                case R.id.left:
                    finish();
                    break;
                default:break;
            }
        }
    };

    private class QrAdpter extends BaoguBaseAdapter<Qrcode> {

        public QrAdpter(Context context, List<Qrcode> list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            Qrcode bean = getItem(position) ;
            if(view==null){
                view= LayoutInflater.from(mContext).inflate(R.layout.item_eticket_qr, null) ;
            }
            TextView validate = (TextView) view.findViewById(R.id.item_eticket_qr_validate);
            TextView duihuan = (TextView) view.findViewById(R.id.item_eticket_qr_duihuan);
            ImageView qr = (ImageView) view.findViewById(R.id.item_eticket_qr_img);

            validate.setText(bean.getExpireddate());
            duihuan.setText(bean.getPconfirmationid());
            mImageFetcher.loadImage(bean.getQrcodeurl(), qr);

            return view;
        }
    }

}