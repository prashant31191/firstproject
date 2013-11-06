package com.qinnuan.engine.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qinnuan.common.util.ImageUtil;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.widget.Face;
import com.qinnuan.engine.R;

import java.util.List;

/**
 * 用户表情Adapter类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-8-9
 */
public class GridViewFaceAdapter extends BaseAdapter {
	// 定义Context
	private Context mContext;
	private List<Face> data;
	
	private LayoutInflater mInflater;

	public List<Face> getData() {
		return data;
	}

	public GridViewFaceAdapter(Context c) {
		mContext = c;
	}

	public GridViewFaceAdapter(Context context, List<Face> faces) {
		// TODO Auto-generated constructor stub
		mContext = context;
		data = faces;
		this.mInflater = LayoutInflater.from(mContext);
	}

	// 获取图片的个数
	public int getCount() {
		return data.size();
	}

	// 获取图片在库中的位置
	public Object getItem(int position) {
		return ImageUtil.getDrawableFromAssetFile(mContext,
				"Custom/" + data.get(position).value);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
        LogUtil.e(getClass(), "getView=" + position);
		if (convertView == null) {
			convertView = (LinearLayout) mInflater.inflate(R.layout.item_gridface_gv, null);
		} 
		ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_face);
		Face face = data.get(position);
		String path = face.value;
		Drawable drawable = ImageUtil.getDrawableFromAssetFile(mContext, "Custom/" + path);
		imageView.setImageDrawable(drawable);
		convertView.setTag(face.key);
		return convertView;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}