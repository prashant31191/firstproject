package com.qinnuan.engine.fragment.filmFan;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.listener.ILoadImgListener;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.user.DynamicDetailActivity;
import com.qinnuan.engine.adapter.DynamicFanAdapter;
import com.qinnuan.engine.bean.Dynamic;
import com.qinnuan.engine.bean.User;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.listener.IOnItemClickListener;
import com.qinnuan.engine.util.view.ListViewPro;
import com.qinnuan.common.util.TextUtil;

import java.util.List;


@FragmentView(id=R.layout.fragment_dynamic_fan)
public class DynamicFanFragment extends BaseFragment {

	@InjectView(id=R.id.fragment_dynamic_listv) ListViewPro lv;

    public static View itemHead;
    private DynamicFanAdapter dynamiAdapter;

    private ILoadImgListener iLoadL;
    private IOnItemClickListener iClickL;
    private int type = -1;
    private User user;

    public DynamicFanFragment() {  }

    public void setListener(ILoadImgListener iL, IOnItemClickListener iC) {
        iLoadL = iL;
        iClickL = iC;
    }

    public void initTitle(int type, User user) {
        this.type = type;
//        if(type == DynamicFanActivity.DYNAMIC_MINE) {
//            tWidget.setCenterViewText("我的动态");
//            tWidget.setRightViewBg(R.drawable.more);
//        } else if(type == DynamicFanActivity.DYNAMIC_OTHER) {
//            tWidget.setCenterViewText(user.getNickname());
//        }
    }

    public void setHeadData(User user) {
        this.user = user;
        itemHead = LayoutInflater.from(getActivity()).inflate(R.layout.item_dynamic_fan_header, null);
        lv.addHeaderView(itemHead);

        ImageView headBg = (ImageView)itemHead.findViewById(R.id.item_dynamic_head_bg);
        TextView name = (TextView) itemHead.findViewById(R.id.item_dynamic_head_name);
        TextView sign = (TextView) itemHead.findViewById(R.id.item_dynamic_head_sign);

        name.setText(user.getNickname());
        sign.setText(user.getSignature());

        String bgUrl = user.getDynamicbackimage();
        if(!TextUtil.isEmpty(bgUrl))
            iLoadL.load(bgUrl, headBg);

        iLoadL.load(user.getProfileimg(),
                (ImageView) itemHead.findViewById(R.id.item_dynamic_head_userhead));
    }

	@Override
	public void bindDataForUIElement() {
	}

    @Override
	protected void bindEvent() {
	}

    public void initAdapter(List<Dynamic> list) {
        dynamiAdapter = new DynamicFanAdapter(getActivity(), list);
      //  dynamiAdapter.setListener(iLoadL, iClickL);

        lv.setAdapter(dynamiAdapter);
        lv.setOnItemClickListener(onItemClick);
    }

	public void notifyAdapter(List<Dynamic> dynamic) {
	}

    private OnItemClickListener onItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(view == itemHead)
                return;
            startActivity(new Intent(getActivity(), DynamicDetailActivity.class)
                    .putExtra(DynamicDetailActivity.DYNAMIC_ID, dynamiAdapter.getItem(position-1)));

        }
    };

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    @Override
    public void rightClick() {
//        if(type == DynamicFanActivity.DYNAMIC_MINE) {
//            View v = tWidget.getRightL();
//            v.setTag(user);
//            iClickL.onClick(v);
//        }
    }

}
