package com.showu.baogu.fragment.filmFan;

import java.util.List;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.showu.baogu.R;
import com.showu.baogu.adapter.FanAdapter;
import com.showu.baogu.bean.Fan;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.baogu.listener.ILoadImgListener;
import com.showu.baogu.listener.IOnItemClickListener;
import com.showu.baogu.listener.OnScrollItemChange;
import com.showu.baogu.util.view.MyXListView;
import com.showu.common.http.image.util.ImageFetcher;
import com.showu.common.util.LogUtil;


@FragmentView(id=R.layout.fragment_fan)
public class FanFragment extends BaseFragment {
	
	@InjectView(id=R.id.fragment_fan_listv)
    MyXListView fansListv;

    private FanAdapter listAdapter;

    private ImageFetcher imageFetcher;

    public FanFragment(ImageFetcher fetcher) {
        this.imageFetcher=fetcher;
    }

	@Override
	public void bindDataForUIElement() {
        LogUtil.e(getClass(), "fansListv in bindDataForUIElement=>"+fansListv);
	}

    @Override
	protected void bindEvent() {
        LogUtil.e(getClass(), "fansListv in bindEvent=>"+fansListv);
        fansListv.setPullLoadEnable(true);
        fansListv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    imageFetcher.setPauseWork(true);
                } else {
                    imageFetcher.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            }
        });
	}

    public void textlistv() {
        fansListv.setSelected(false);
    }

    public void initAdapter(List<Fan> fans) {
        listAdapter = new FanAdapter(getActivity(), fans, imageFetcher);
        listAdapter.initAdapter(fans, fansListv);
//        fansListv.setOnScrollItemChange(onScrollItemChange);
        fansListv.setOnItemClickListener(onItemClick);
        LogUtil.e(getClass(), "fansListv initAdapter=>"+fansListv);
        LogUtil.e(getClass(), "fans size in initAdapter=>"+fans.size());
    }
	
	public void notifyAdapter(List<Fan> fanList) {
		listAdapter.notifyAdapter(fanList);
//		fansListv.setOnScrollItemChange(onScrollItemChange);
        fansListv.setOnItemClickListener(onItemClick);
	}

    private OnItemClickListener onItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            View footer = fansListv.getFooterView();
            if (view!=null && view!=footer) {
                Fan fan = listAdapter.getItem(position - 1);
                iOnClick.onClick(fan);
            }
        }
    };

//	public interface IOnListVItemClick {
//		public void onItemClick(Fan fan);
//	}
	private IOnItemClickListener iOnClick;
	public void setIOnClick(IOnItemClickListener iO) {
		this.iOnClick = iO;
	}

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

    public MyXListView getXListV() {
        LogUtil.e(getClass(), "fansListv in getXListV=>"+fansListv);
        return  fansListv;
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    @Override
    public void rightClick() {
        iOnItemClickL.onClick(tWidget);
    }

    private IOnItemClickListener iOnItemClickL;
    public void setRightClick(IOnItemClickListener l) {
        iOnItemClickL = l;
    }

    public void removeFooter() {
        fansListv.removeFooter();
    }

}
