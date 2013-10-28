package com.showu.common.widget;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.film.ActivityActivity;
import com.showu.baogu.bean.film.ActivityEticktBean;
import com.showu.baogu.bean.film.EtitckBean;
import com.showu.common.util.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinPopContentView extends LinearLayout {
    private LinearLayout contentLayout;
    private Map<String, String> idmap = new HashMap<String, String>();
    private ImageView cancel;

    private CancellListener listener;

    public void setListener(CancellListener listener) {
        this.listener = listener;
    }

    public interface CancellListener {
        public void cancel();
    }

    public JoinPopContentView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.join_pop_layout, this);
        contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
        findViewById(R.id.close).setOnClickListener(onClickListener);
    }

    public void fillUI(List<ActivityEticktBean> list) {
        group(list);
        contentLayout.removeAllViews();
        for (String id : idmap.keySet()) {
            for (ActivityEticktBean info : list) {
                if (info.getActeticcinemaid().equals(id)) {
                    contentLayout.addView(createItem(info));
                }
            }
        }
    }

    public void fillUITicket(List<ActivityEticktBean> list) {
        groupTicket(list);
        contentLayout.removeAllViews();
        for (String id : idmap.keySet()) {
            // contentLayout.addView(createGroup(idmap.get(id)));
            contentLayout.addView(createTicketGroup(idmap.get(id)));
            for (ActivityEticktBean info : list) {
                if (id.equals(info.getActeticcinemaid())) {
                    contentLayout.addView(createItemTicket(info));
                }
            }
        }
    }


    private View createTicketGroup(String cinemaName) {
        LogUtil.e(getClass(), cinemaName);
        LinearLayout group = (LinearLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.item_activity_ticket_head, null);
        TextView cinema = (TextView) group
                .findViewById(R.id.item_activity_ticket_headlayout);
        cinema.setText(cinemaName);
        return group;
    }

    private View createItem(ActivityEticktBean info) {
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.activity_item, null);
        TextView priceText = (TextView) view.findViewById(R.id.price);
        priceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        TextView newPriceText = (TextView) view.findViewById(R.id.newPrice);
        /*
		 * StringBuilder sb = new StringBuilder(info.getFdate()).append("  ");
		 * sb
		 * .append(info.getFtime()).append("  ").append(info.getDimensional());
		 */
        Button b = (Button) view.findViewById(R.id.select_choice);
        b.setTag(info);
//		if (info.getIsexpire() == 0) {
//			b.setBackgroundResource(R.drawable.choice_seat_on);
//			b.setOnClickListener(onClickListener);
//		}
        return view;
    }

    private View createItemTicket(ActivityEticktBean bean) {
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.checket_item, null);
        TextView typeText = (TextView) view.findViewById(R.id.type);
        TextView newPriceText = (TextView) view.findViewById(R.id.newprice);
        TextView priceText = (TextView) view.findViewById(R.id.price);
        Button buyButton = (Button) view.findViewById(R.id.buyButton);
        buyButton.setOnClickListener(onClickListener);
        buyButton.setTag(bean);
        priceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if (bean.getTickettype() == 0) {
            typeText.setText("2D兑换券");
        } else if (bean.getTickettype() == 1) {
            typeText.setText("3D兑换券");
        }
        priceText.setText("￥" + bean.getOldprice());
        newPriceText.setText("￥" + bean.getPrice());
//		if (bean.getIsexpire() == 0) {
//			view.setOnClickListener(onClickListener);
//		}
        view.setTag(bean);
        return view;

    }

    private void group(List<ActivityEticktBean> list) {
        idmap.clear();
        for (ActivityEticktBean info : list) {
            if (!idmap.keySet().contains(info.getActeticcinemaid())) {
                idmap.put(info.getActeticcinemaid(), info.getCinemaname());
            }
        }
    }

    private void groupTicket(List<ActivityEticktBean> list) {
        idmap.clear();
        for (ActivityEticktBean info : list) {
            if (!idmap.keySet().contains(info.getActeticcinemaid())) {
                idmap.put(info.getActeticcinemaid(), info.getCinemaname());
            }
        }
    }


    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.close) {
                listener.cancel();
            } else {
                ((ActivityActivity) getContext()).buyTickit((ActivityEticktBean) v.getTag());
            }
        }
    };
}
