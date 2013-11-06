package com.qinnuan.engine.fragment.film;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.fragment.InjectView;

@FragmentView(id = R.layout.fragment_etickt_order)
public class EticktOrderFragment extends BaseFragment implements OnClickListener {
    @InjectView(id = R.id.cinemaName)
    private TextView cinemaNameText;
    @InjectView(id = R.id.validate)
    private TextView validText;
    @InjectView(id = R.id.ticktOrder)
    private TextView typeText;
    @InjectView(id = R.id.film_sigle)
    private TextView priceText;
    @InjectView(id = R.id.film_count)
    private TextView totalPriceText;
    @InjectView(id = R.id.cellphone)
    private EditText cellPhone;
    @InjectView(id = R.id.acitivity_seat_order_pay_submit)
    private Button button;

    @InjectView(id = R.id.minus)
    private View minsBtn;
    @InjectView(id = R.id.adds)
    private View addBtn;
    @InjectView(id = R.id.ticktNumber)
    private TextView tickitNumber;

//    private EtitckBean bean;

    private String cinemaName;
    private String validate;
    private float price;
    private int type;
    private int maxNumber;

    private IAddOrderTickt addOrderTickt;

    public EticktOrderFragment() {
    }

    public interface IAddOrderTickt {
        public void addEticktOrder(String phoneNumber, int account);

        public void confireOrder(int type, String crashId);
    }

    public EticktOrderFragment(String cinemaName, String validate, float price, int type, int maxNumber, IAddOrderTickt addOrderTickt) {
        this.cinemaName = cinemaName;
        this.validate = validate;
        this.price = price;
        this.type = type;
        this.maxNumber = maxNumber;
        this.addOrderTickt = addOrderTickt;
    }

    @Override
    public void bindDataForUIElement() {
        cinemaNameText.setText(cinemaName);
        validText.setText(validate);
        priceText.setText("￥" + price);
        String typeName = "2D";
        if (type == 1) {
            typeName = "3D";
        }
        typeText.setText(typeName);
    }

    @Override
    protected void bindEvent() {
        button.setOnClickListener(this);
        minsBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int acount = Integer.parseInt(tickitNumber.getText().toString());
        switch (view.getId()) {
            case R.id.minus:
                if (acount > 1) {
                    tickitNumber.setText(acount - 1 + "");
                }
                break;
            case R.id.adds:
                if (acount < maxNumber) {
                    tickitNumber.setText(acount + 1 + "");
                }
                break;
            case R.id.acitivity_seat_order_pay_submit:
                if (checkPhoneNumber()) {
                    addOrderTickt.addEticktOrder(cellPhone.getText().toString(), acount);
                } else {
                    GUIUtil.toast(getActivity(), "请输入合法的电话号码");
                }
                break;
        }
    }

    private boolean checkPhoneNumber() {
        return cellPhone.getText().toString().length() == 11;
    }
}
