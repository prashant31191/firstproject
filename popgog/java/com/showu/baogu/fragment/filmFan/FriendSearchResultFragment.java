package com.showu.baogu.fragment.filmFan;

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
import com.showu.baogu.listener.OnScrollItemChange;
import com.showu.baogu.util.view.MyXListView;
import com.showu.baogu.util.view.XListView;
import com.showu.common.http.image.util.ImageFetcher;
import com.showu.common.util.LogUtil;

import java.util.List;


@FragmentView(id=R.layout.fragment_search_result)
public class FriendSearchResultFragment extends BaseFragment {

	@InjectView(id=R.id.fragment_fan_listv)
    MyXListView fansListv;

    private FanAdapter listAdapter;

    private ImageFetcher imageFetcher ;
    private XListView.IXListViewListener xListener;
    private List<Fan> fans;


    private int isnext = 0;
    public FriendSearchResultFragment( ImageFetcher fetcher) {
        this.imageFetcher=fetcher;
    }

    public void setIXListener(XListView.IXListViewListener l) {
        xListener = l;
    }

	@Override
	public void bindDataForUIElement() {
        if(fans != null) {
            initAdapter(isnext, fans);
        }
	}

    public void setData(int isnext, List<Fan> fans) {
        this.isnext = isnext;
        this.fans = fans;

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

    public void initAdapter(int isnext, List<Fan> fans) {
        this.isnext = isnext;
        fansListv.onLoaded();
        listAdapter = new FanAdapter(getActivity(), fans, imageFetcher);
        fansListv.setXListViewListener(xListener);
        listAdapter.initAdapter(fans, fansListv);
        fansListv.setIHasNext(iIHasNext);
//        fansListv.setOnScrollItemChange(onScrollItemChange);
        fansListv.setOnItemClickListener(onItemClick);
        LogUtil.e(getClass(), "fansListv initAdapter=>"+fansListv);
        LogUtil.e(getClass(), "fans size in initAdapter=>"+fans.size());
    }
	
	public void notifyAdapter(int isnext, List<Fan> fanList) {
        this.isnext = isnext;
        fansListv.onLoaded();
        if(isnext == 0) {
            fansListv.removeFooter();
        }
        fansListv.setIHasNext(iIHasNext);
		listAdapter.notifyAdapter(fanList);
//		fansListv.setOnScrollItemChange(onScrollItemChange);
        fansListv.setOnItemClickListener(onItemClick);
	}

    private OnItemClickListener onItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            View footer = fansListv.getFooterView();
            if (view!=null && view!=footer && view!=fansListv.getHeadView()) {
                Fan fan = listAdapter.getItem(position - 1);
                iOnClick.onItemClick(fan);
            }
        }
    };

	public interface IOnListVItemClick {
		public void onItemClick(Fan fan);
	}
	private IOnListVItemClick iOnClick;
	public void setIOnClick(IOnListVItemClick iO) {
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

    private MyXListView.IHasNext iIHasNext = new MyXListView.IHasNext() {
        @Override
        public Integer getHasNext() {
            return isnext;
        }
    };

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    public void onLoaded() {
        if(fansListv != null)
            fansListv.onLoaded();
    }

}
