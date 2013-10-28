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

public class BlacklistAdapter extends BaoguBaseAdapter<Fan> {

	private Context mContext;
	private Map<Fan, View> mapView = new HashMap<Fan, View>();
    private ImageFetcher fetcher;
    private LayoutInflater mInflater;

	public BlacklistAdapter(Context context, List<Fan> fans, ImageFetcher fetcher) {
		super(context, fans);
        modelList = fans;
		mContext = context;
        this.fetcher=fetcher;
        initInflater(mContext);
	}

    public BlacklistAdapter(Context context, List<Fan> fans) {
        super(context, fans);
        mContext = context;
        initInflater(mContext);
    }

    public BlacklistAdapter(Context context) {
        super(context);
        initInflater(mContext);
    }

    private void initInflater(Context context) {
        mInflater = LayoutInflater.from(context);
    }

	public void initAdapter(List<Fan> fans, AdapterView adpterV) {
		adpterV.setAdapter(this);
	}
	
	public void notifyAdapter(List<Fan> list) {
        getModelList().addAll(list);
		notifyDataSetChanged();
	}

    private Fan deleteFan;
    public void notifyAdapter() {
        getModelList().remove(deleteFan);
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
        cView = mInflater.inflate(R.layout.item_blacklist, null);
        cView.setTag(holder);
        holder.nickname = (TextView)cView.findViewById(R.id.item_nearby_person_nickname);
        holder.head = (ImageView)cView.findViewById(R.id.item_nearby_person_head);
        holder.unblack = (Button)cView.findViewById(R.id.item_blacklist_unblack);
        mapView.put(fan, cView);
        String imgUrl = fan.getProfileimg();
        if (imgUrl != null && !imgUrl.equals("")) {
            fetcher.loadImage(imgUrl,holder.head, GUIUtil.getDpi(mContext, R.dimen.margin_8));
		}
		holder.nickname.setText(TextUtil.getProcessText(fan.getNickname(), mContext));

        holder.unblack.setTag(fan);
        holder.unblack.setOnClickListener(onClickL);

		return cView;
	}

    private View.OnClickListener onClickL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteFan = (Fan) v.getTag();
             onIteml.onClick(deleteFan);
        }
    };

    private IOnItemClickListener onIteml;
    public void setUnblackListener(IOnItemClickListener l) {
        onIteml = l;
    }

	private class ViewHolder {
		private TextView nickname;
        private Button unblack;
        private ImageView head;

	}

}
