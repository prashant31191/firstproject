package com.showu.baogu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.bean.Fan;
import com.showu.baogu.listener.IOnItemClickListener;
import com.showu.common.http.image.util.ImageFetcher;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.TextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactAdapter extends BaoguBaseAdapter<Fan> {

    private Context mContext;
    private Map<Fan, View> mapView = new HashMap<Fan, View>();
    private ImageFetcher fetcher;
    private LayoutInflater mInflater;

    public ContactAdapter(Context context, List<Fan> fans, ImageFetcher fetcher) {
        super(context, fans);
        mContext = context;
        this.fetcher = fetcher;
        initInflater(mContext);
    }

//    public ContactAdapter(Context context, List<Fan> fans) {
//        super(context, fans);
//
//        mContext = context;
//        initInflater(mContext);
//    }

    public ContactAdapter(Context context) {
        super(context);
        mContext = context;
        initInflater(mContext);
    }

    private void initInflater(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void initAdapter(List<Fan> fans, AdapterView adpterV) {
        modelList = fans;
        adpterV.setAdapter(this);
    }

    public void notifyAdapter(List<Fan> list) {
        getModelList().addAll(list);
        notifyDataSetChanged();
    }

    private ViewHolder holder;

    @Override
    public View getView(int position, View cView, ViewGroup parent) {
        Fan fan = getItem(position);
        if (mapView.get(fan) != null) {
            return mapView.get(fan);
        }
        holder = new ViewHolder();
        cView = mInflater.inflate(R.layout.item_contact, null);
        cView.setTag(holder);
        holder.head = (ImageView) cView.findViewById(R.id.item_contact_head);
        holder.nickname = (TextView) cView.findViewById(R.id.item_contact_nickname);
        holder.addedtips = (TextView) cView.findViewById(R.id.item_contact_addedtips);
        holder.phonename = (TextView) cView.findViewById(R.id.item_contact_phonename);
        holder.sex = (ImageView) cView.findViewById(R.id.item_contact_sex);
        holder.add = (Button) cView.findViewById(R.id.item_contact_add);

        mapView.put(fan, cView);
        String imgUrl = fan.getProfileimg();
        if (imgUrl != null && !imgUrl.equals("")) {
            fetcher.loadImage(imgUrl, holder.head, GUIUtil.getDpi(mContext, R.dimen.margin_8));
        }
        holder.nickname.setText(TextUtil.getProcessText(fan.getNickname(), mContext));
        if (fan.getSex() == 2) {//默认为男,当2的时候为女
            holder.sex.setImageResource(R.drawable.sex_female);
        } else if (fan.getSex() == 1) {
            holder.sex.setImageResource(R.drawable.sex_male);
        }

        holder.phonename.setText(fan.getPhonename());
        if(fan.getIsadded() == 1) {
            showAddedTips(holder);
        } else {
            showAddBtn(holder);
        }

        holder.add.setTag(fan);
        holder.add.setOnClickListener(onAddBtnClick);

        return cView;
    }

    private class ViewHolder {
        private TextView nickname;
        private TextView phonename;
        private TextView addedtips;
        private Button add;
        private ImageView sex;
        private ImageView head;
    }

    public void showAddBtn(ViewHolder holder) {
        holder.addedtips.setVisibility(View.GONE);
        holder.add.setVisibility(View.VISIBLE);
    }

    public void showAddedTips(ViewHolder holder) {
        holder.add.setVisibility(View.GONE);
        holder.addedtips.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener onAddBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            iOnAddBtnClick.onClick(v.getTag());
        }
    };

    IOnItemClickListener iOnAddBtnClick;
    public void setIOnAddBtnClick(IOnItemClickListener l) {
        iOnAddBtnClick = l;
    }
}
