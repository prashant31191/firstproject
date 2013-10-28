package com.showu.baogu.adapter;

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
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;
import com.showu.common.util.TextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendAdapter extends BaoguBaseAdapter<Fan> {

	private Context mContext;
	private Map<Fan, View> mapView = new HashMap<Fan, View>();
    //private ILoadImgListener iLoadL;
    ImageFetcher mImageFetcher;
    private LayoutInflater mInflater;

	public FriendAdapter(Context context, List<Fan> fans, ImageFetcher mImageFetcher) {
		super(context, fans);
        modelList = fans;
		mContext = context;
        this.mImageFetcher = mImageFetcher;
        initInflater(mContext);
	}

    public FriendAdapter(Context context) {
        super(context);
        mContext = context;
        initInflater(mContext);
    }

    private void initInflater(Context context) {
        LogUtil.e(getClass(), "context=>"+context);
        mInflater = LayoutInflater.from(context);
    }

	public void initAdapter(List<Fan> fans, AdapterView adpterV) {
       // modelList.clear();
        modelList = fans;
		adpterV.setAdapter(this);
	}
	
	public void notifyAdapter(List<Fan> list) {
        modelList.addAll(list);
		notifyDataSetChanged();
	}

    public void notifyAdapterByDelete(Fan deleteFan) {
        getModelList().remove(deleteFan);
        notifyDataSetChanged();
    }

    private ViewHolder holder;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        LogUtil.e(getClass(),"getView--------"+position);

		Fan fan = getItem(position);
		if(mapView.get(fan)!=null){
			return mapView.get(fan) ;
		}
        holder = new ViewHolder();
        convertView = mInflater.inflate(R.layout.item_friend, null);
        convertView.setTag(holder);
        holder.nickname = (TextView)convertView.findViewById(R.id.item_friend_nickname);
        holder.sign = (TextView)convertView.findViewById(R.id.item_friend_sign);
        holder.head = (ImageView) convertView.findViewById(R.id.item_friend_head);
        holder.sex = (ImageView) convertView.findViewById(R.id.item_friend_sex);
        mapView.put(fan, convertView);
        String imgUrl = fan.getProfileimg();
        mImageFetcher.loadImage(imgUrl,  holder.head, GUIUtil.getDpi(mContext, R.dimen.margin_8));
        if(fan.getIsfriend() == 1) {
            if(TextUtil.isEmpty(fan.getRemarkname())) {
                holder.nickname.setText(TextUtil.getProcessText(fan.getNickname(), mContext));
            } else {
                holder.nickname.setText(fan.getRemarkname());
            }
        } else {
            holder.nickname.setText(TextUtil.getProcessText(fan.getNickname(), mContext));
        }

		holder.sign.setText(TextUtil.getProcessText(fan.getSignature(), mContext));

        if(fan.getUsertype() == 0) {//普通用户
            if(fan.getSex() == 2 ) {//默认为男,当2的时候为女
                holder.sex.setImageResource(R.drawable.sex_female);
            } else if(fan.getSex() == 1) {
                holder.sex.setImageResource(R.drawable.sex_male);
            }
        } else if(fan.getUsertype() == 1) {//枪手
            //
        } else  if(fan.getUsertype() == 2) {//爆谷助手账号
            if(fan.getSex() == 2 ) {//默认为男,当2的时候为女
                holder.sex.setImageResource(R.drawable.sex_female);
            } else if(fan.getSex() == 1) {
                holder.sex.setImageResource(R.drawable.sex_male);
            }
        } else  if(fan.getUsertype() == 3) {//公共账号
            holder.sex.setImageResource(R.drawable.public_icon);
        } else if(fan.getUsertype() == 4) {//爆谷电影账号
            //holder.sex.setImageResource(null);
        }
		return convertView;
	}

	private class ViewHolder {
		private TextView nickname;
        private TextView sign;
        private ImageView head;
        private ImageView sex;
	}

}
