package com.showu.baogu.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.showu.baogu.R;
import com.showu.common.util.ImageUtil;
import com.showu.common.util.ParseMsgUtil;
import com.showu.common.widget.FaceGridView;

import java.util.ArrayList;

/**
 * 用户表情Adapter类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-8-9
 */
public class EmojiViewFaceAdapter extends BaseAdapter
{
	// 定义Context
	private Context	 mContext;
	// 定义整型数组 即图片源
	private static ArrayList<String> paths;
	private LayoutInflater mInflater;
	
	@SuppressWarnings("static-access")
	public EmojiViewFaceAdapter(Context c ,ArrayList<String> paths)
	{
		mContext = c;
		this.paths = paths;
		this.mInflater = LayoutInflater.from(mContext);
	}
	
	// 获取图片的个数
	public int getCount()
	{
		return paths.size();
	}


	// 获取图片ID
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return ImageUtil.getDrawableByAssets(paths.get(position), mContext);
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null) {
			convertView = (LinearLayout) mInflater.inflate(R.layout.item_gridface_gv, null);
		} 
		ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_face);
		Drawable drawable = ImageUtil.getDrawableByAssets(paths.get(position), mContext);
		imageView.setBackgroundDrawable(drawable);
		convertView.setTag(new ParseMsgUtil().convertUnicode(new FaceGridView(mContext).getTagByPostion(position)));
		return convertView;
	}
}