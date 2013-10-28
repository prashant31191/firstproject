package com.showu.baogu.fragment.filmFan;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.user.DynamicDetailActivity;
import com.showu.baogu.adapter.DynamicAdapter;
import com.showu.baogu.bean.Dynamic;
import com.showu.baogu.bean.DynamicMsg;
import com.showu.baogu.bean.User;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.baogu.listener.ILoadImgListener;
import com.showu.baogu.listener.IOnItemClickListener;
import com.showu.baogu.util.view.ListViewPro;
import com.showu.common.util.LogUtil;
import com.showu.common.util.TextUtil;

import java.util.List;


@FragmentView(id=R.layout.fragment_dynamic)
public class DynamicFragment extends BaseFragment {

	@InjectView(id=R.id.fragment_dynamic_listv)
    ListViewPro lv;

    private View itemHead;
    private DynamicAdapter dynamiAdapter;

    private ILoadImgListener iRoundImgL;
    private ILoadImgListener iImgL;
    private IOnItemClickListener iOnL;
    private User user;
    private TextView msg;
    private ImageView msgimg;
    private ImageView headBg;
    private ImageView userhead;
    private TextView name;
    private TextView sign;
    private View header_msg;
    private int location = -1;

    public DynamicFragment(IOnItemClickListener iL) {
        iOnL = iL;
    }

    public void setILoadImgL(ILoadImgListener iRoundImgL, ILoadImgListener iImgL) {
        this.iRoundImgL = iRoundImgL;
        this.iImgL = iImgL;
    }

    public void notifyLike(DynamicAdapter.ViewHolder holder, Dynamic bean, boolean isAdd) {
        dynamiAdapter.notifyLike(holder, bean, isAdd);
    }

    public void setMsgData(DynamicMsg dynamicMsg) {
        if (dynamicMsg != null) {
            msg.setText("你有" + dynamicMsg.getMsgcount() + "条新消息");
            iRoundImgL.load(dynamicMsg.getProfileimg(), msgimg);
        } else {
            msg.setText("无新消息");
        }
    }

    public void clearMsgUI() {

    }

    private void initHead() {
        if(itemHead != null) return;
        itemHead = LayoutInflater.from(getActivity()).inflate(
                R.layout.item_dynamic_header, null);

        userhead = (ImageView)itemHead.findViewById(R.id.item_dynamic_head_userhead);
        header_msg = itemHead.findViewById(R.id.item_dynamic_header_msg);
        msg = (TextView) itemHead.findViewById(R.id.item_dynamic_head_msg);
        msgimg = (ImageView) itemHead.findViewById(R.id.item_dynamic_head_msgimg);
        headBg = (ImageView)itemHead.findViewById(R.id.item_dynamic_head_bg);
        name  = (TextView) itemHead.findViewById(R.id.item_dynamic_head_name);
        sign = (TextView) itemHead.findViewById(R.id.item_dynamic_head_sign);
        header_msg.setOnClickListener(onClick);
        lv.setHeadViews(itemHead, headBg);
        lv.addHeaderView(itemHead);
    }

    public void setHeadData(User u) {
        user = u;
        name.setText(user.getNickname());
        sign.setText(user.getSignature());

        String bgUrl = user.getDynamicbackimage();
        if(!TextUtil.isEmpty(bgUrl))
            iImgL.load(bgUrl, headBg);
        iRoundImgL.load(user.getProfileimg(), userhead);
    }

    @Override
	public void bindDataForUIElement() {
        initHead();
	}

    @Override
	protected void bindEvent() { }

    public void initAdapter(List<Dynamic> l) {
        dynamiAdapter = new DynamicAdapter(getActivity(), l);
   //     dynamiAdapter.setListener(iImgL, iOnL);
        lv.setAdapter(dynamiAdapter);
        lv.setOnItemClickListener(onItemClick);
        //lv.setFooter(isnext);
    }

	public void notifyAdapter(List<Dynamic> dynamics) {
        LogUtil.e(getClass(), "list 11==>"+dynamiAdapter.getModelList().size());
        dynamiAdapter.getModelList().addAll(dynamics);
        LogUtil.e(getClass(), "list 22==>"+dynamiAdapter.getModelList().size());
        dynamiAdapter.notifyDataSetChanged();
        lv.setOnItemClickListener(onItemClick);
      //  lv.setFooter(isnext);
	}

    private OnItemClickListener onItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(view == itemHead) return;
            if(view == lv.getFooterView()) return;
            Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
            location = position - 1;
            intent.putExtra(DynamicDetailActivity.DYNAMIC_ID, dynamiAdapter.getItem(location));
            startActivity(intent);
        }
    };

    public void deleteADynamic() {
        dynamiAdapter.getModelList().remove(location);
        dynamiAdapter.notifyDataSetChanged();
        lv.setOnItemClickListener(onItemClick);
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    @Override
    public void rightClick() {
        LogUtil.e(getClass(), "====rightClick===");
        //startActivityForResult(new Intent(getActivity(), DynamicPulishActivity.class), getTargetRequestCode());
        iOnL.onClick(null);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.item_dynamic_header_msg:
                    iOnL.onClick(v);
                    break;

                default:break;
            }
        }
    };


    public void setIListVPro(ListViewPro.IListVPro l) {
        lv.setIListVPro(l);
    }

    public void onLoaded(int isnext) {
        lv.onLoaded(isnext);
    }

    public void removeFooter() {
        lv.removFooter();
    }
//	public interface IOnListVItemClick {
//		public void onItemClick(Fan fan);
//	}
//	private IOnListVItemClick iOnClick;
//	public void setIOnClick(IOnListVItemClick iO) {
//		this.iOnClick = iO;
//	}
//
//	private OnScrollItemChange onScrollItemChange = new OnScrollItemChange() {
//		@Override
//		public void loadHead(View view, int position) {
//			if(position<listAdapter.getCount() && view!=null) {
//				Fan fan = listAdapter.getItem(position);
//                lisnter.load(fan.getProfileimg(),
//                        (ImageView)view.findViewById(R.id.item_nearby_person_head));
//			}
//		}
//	};

}
