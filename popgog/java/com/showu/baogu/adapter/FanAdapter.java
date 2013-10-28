package com.showu.baogu.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.bean.Fan;
import com.showu.baogu.listener.ILoadImgListener;
import com.showu.common.http.image.util.ImageFetcher;
import com.showu.common.util.LogUtil;
import com.showu.common.util.TextUtil;

public class FanAdapter extends BaoguBaseAdapter<Fan> {

	private Context mContext;
	private Map<Fan, View> mapView = new HashMap<Fan, View>();
    private ImageFetcher fetcher;
    private LayoutInflater mInflater;

	public FanAdapter(Context context, List<Fan> fans, ImageFetcher fetcher) {
		super(context, fans);
        modelList = fans;
		mContext = context;
        this.fetcher=fetcher;
        initInflater(mContext);
	}

    public FanAdapter(Context context, List<Fan> fans) {
        super(context, fans);
        modelList = fans;
        mContext = context;
        initInflater(mContext);
    }

    public FanAdapter(Context context) {
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
		if(mapView.get(fan)!=null){
			return mapView.get(fan) ;
		}
        holder = new ViewHolder();
        cView = mInflater.inflate(R.layout.item_nearby_person, null);
        cView.setTag(holder);
        holder.nickname = (TextView)cView.findViewById(R.id.item_nearby_person_nickname);
        holder.sex = (ImageView) cView.findViewById(R.id.item_nearby_person_sex);
        holder.dist_time = (TextView)cView.findViewById(R.id.item_nearby_person_dist_time);
        holder.sign = (TextView)cView.findViewById(R.id.item_nearby_person_sign);
        holder.head = (ImageView) cView.findViewById(R.id.item_nearby_person_head);
        mapView.put(fan, cView);
        String imgUrl = fan.getProfileimg();
        if (imgUrl != null && !imgUrl.equals("")) {
//            iLoadL.load(imgUrl,  holder.head);
            fetcher.loadImage(imgUrl,holder.head,10);
		}
		holder.nickname.setText(TextUtil.getProcessText(fan.getNickname(), mContext));
//        if(fan.getSex() == 2 ) {//默认为男,当2的时候为女
//            holder.sex.setImageResource(R.drawable.sex_female);
//        }

        if(fan.getUsertype() == 0) {
            if(fan.getSex() == 2 ) {//默认为男,当2的时候为女
                holder.sex.setImageResource(R.drawable.sex_female);
            } else if(fan.getSex() == 1) {
                holder.sex.setImageResource(R.drawable.sex_male);
            }
        } else if(fan.getUsertype() == 1) {

        }  else  if(fan.getUsertype() == 2) {
            if(fan.getSex() == 2 ) {//默认为男,当2的时候为女
                holder.sex.setImageResource(R.drawable.sex_female);
            } else if(fan.getSex() == 1) {
                holder.sex.setImageResource(R.drawable.sex_male);
            }
        } else  if(fan.getUsertype() == 3) {
            holder.sex.setImageResource(R.drawable.public_icon);
        } else if(fan.getUsertype() == 4) {
            //holder.sex.setImageResource();
        }

		holder.sign.setText(TextUtil.getProcessText(fan.getSignature(), mContext));
//        holder.dist_time.setText(TextUtil.getDistance(fan.getDistance()+"")
//                +"km | "+ TextUtil.getOffline(fan.getDatediff()));
        holder.dist_time.setText(TextUtil.getDistance(fan.getDistance()+"")+"km");
		return cView;
	}

	private class ViewHolder {
		private TextView nickname;
        private TextView dist_time;
        private TextView sign;
        private ImageView head;
        private ImageView sex;
	}

}
