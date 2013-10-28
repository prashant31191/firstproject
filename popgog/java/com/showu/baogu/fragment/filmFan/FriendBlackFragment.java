package com.showu.baogu.fragment.filmFan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.showu.baogu.R;
import com.showu.baogu.activity.filmFan.FriendBlackActivity;
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
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;

import java.util.List;


@FragmentView(id=R.layout.fragment_fan)
public class FriendBlackFragment extends BaseFragment implements AdapterView.OnItemLongClickListener {

	@InjectView(id=R.id.fragment_fan_listv)
    MyXListView fansListv;

    private FanAdapter listAdapter;

    private ImageFetcher imageFetcher;
    private XListView.IXListViewListener xListener;
    private List<Fan> fans;

    private int isnext = 0;
    public FriendBlackFragment(int isnext, List<Fan> fans, ImageFetcher imageFetcher) {
        this.isnext = isnext;
        this.fans = fans;
        this.imageFetcher=imageFetcher;
    }

    public void setIXListener(XListView.IXListViewListener l) {
        xListener = l;
    }

	@Override
	public void bindDataForUIElement() {
        initAdapter(isnext, fans);
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

    public void initAdapter(int isnext, List<Fan> fans) {
        this.isnext = isnext;
        fansListv.onLoaded();
        listAdapter = new FanAdapter(getActivity(), fans, imageFetcher);
        fansListv.setXListViewListener(xListener);
        fansListv.setOnItemLongClickListener(this);
        listAdapter.initAdapter(fans, fansListv);
        fansListv.setIHasNext(iIHasNext);
//        fansListv.setOnScrollItemChange(onScrollItemChange);
        fansListv.setOnItemClickListener(onItemClick);
        LogUtil.e(getClass(), "fansListv initAdapter=>"+fansListv);
        LogUtil.e(getClass(), "fans size in initAdapter=>"+fans.size());
    }
	
	public void notifyAdapter(int isnext, List<Fan> fanList) {
        fansListv.onLoaded();
        this.isnext = isnext;
        if(isnext == 0) {
            fansListv.removeFooter();
        }
        fansListv.setIHasNext(iIHasNext);
		listAdapter.notifyAdapter(fanList);
        fansListv.setOnItemClickListener(onItemClick);
	}

    private OnItemClickListener onItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            View footer = fansListv.getFooterView();
            if (view!=null && view!=footer) {
                Fan fan = listAdapter.getItem(position - 1);
                iOnClick.onItemClick(fan);
            }
        }
    };

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Fan fan = listAdapter.getItem(i-1) ;
        creatCleanDialog(fan);
        return false;
    }

    public interface IOnListVItemClick {
		public void onItemClick(Fan fan);
	}
	private IOnListVItemClick iOnClick;
	public void setIOnClick(IOnListVItemClick iO) {
		this.iOnClick = iO;
	}

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
    private void creatCleanDialog(final Fan fan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("你确定要解除黑名单吗？").setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //((FriendBlackActivity)getActivity()).unlockBlackName(fan);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).setTitle("解除黑名单");
        AlertDialog alert = builder.create();
        alert.show();
    }
}
