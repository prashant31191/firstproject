package com.qinnuan.engine.fragment.filmFan;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.qinnuan.engine.adapter.FriendAdapter;
import com.qinnuan.engine.bean.Fan;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.util.view.MyXListView;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.filmFan.MyFriendActivity;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.listener.OnScrollItemChange;
import com.qinnuan.engine.util.view.XListView;
import com.qinnuan.common.http.image.util.ImageFetcher;

import java.util.List;

@FragmentView(id=R.layout.fragment_friends)
public class FriendsFragment extends BaseFragment {

	@InjectView(id=R.id.fragment_friends_listv)
    com.qinnuan.engine.util.view.MyXListView fansListv;
    private FriendAdapter listAdapter;

    //private ILoadImgListener lisnter;
    ImageFetcher mImageFetcher;
    private Integer ishasNext;

    public FriendsFragment() {
    }

    public FriendsFragment(ImageFetcher mImageFetcher) {
        //lisnter = l;
        this.mImageFetcher = mImageFetcher;
    }

	@Override
	public void bindDataForUIElement() {
        LogUtil.e(getClass(), "bindDataForUIElement ff==>");
//        if(listAdapter!=null) {
//            fansListv.setAdapter(listAdapter);
//            fansListv.setSelectionFromTop(index, top);
//            fansListv.setOnScrollItemChange(onScrollItemChange);
//            fansListv.setOnItemClickListener(onItemClick);
//        }
	}

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e(getClass(), "onResume ff==>");
        if(listAdapter!=null) {
            fansListv.setAdapter(listAdapter);
            fansListv.setSelectionFromTop(index, top);
            fansListv.setOnScrollItemChange(onScrollItemChange);
            fansListv.setOnItemClickListener(onItemClick);
            if(MyFriendActivity.isDeleteAFriend) {
                notifyChangeData(MyFriendActivity.deleteFan);
                MyFriendActivity.isDeleteAFriend = false;
            }
        }
        LogUtil.e(getClass(), "top1:"+top+", index1:"+index);
    }

    public void notifyChangeData(Fan deleteFan) {
        if(deleteFan != null) {
            LogUtil.e(getClass(), "list size notify=>"+listAdapter.getModelList().size());
            listAdapter.notifyAdapterByDelete(deleteFan);
            fansListv.setIHasNext(ihn);
            fansListv.setXListViewListener(ixl);
            fansListv.setOnScrollItemChange(onScrollItemChange);
            fansListv.setOnItemClickListener(onItemClick);
        }
    }

    private int top;
    private int index;
    @Override
    public void onPause() {
        super.onPause();
        View v = fansListv.getChildAt(0);
        top = (v == null) ? 0 : v.getTop();
        index =fansListv.getFirstVisiblePosition();
        LogUtil.e(getClass(), "top1:"+top+", index1:"+index);
    }

    @Override
	protected void bindEvent() {
        fansListv.setPullLoadEnable(true);
        LogUtil.e(getClass(), "bindEvent ff==>");
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.e(getClass(), "onActivityResult ff==>"+resultCode);
    }



    public void initAdapter(List<Fan> fans, Integer isnext) {
        ishasNext = isnext;
        fansListv.setIHasNext(ihn);
        fansListv.setXListViewListener(ixl);
        listAdapter = new FriendAdapter(getActivity(), fans, mImageFetcher);
        listAdapter.initAdapter(fans, fansListv);
        LogUtil.e(getClass(), "fans size in initAdapter=>"+fans.size());
        fansListv.setOnScrollItemChange(onScrollItemChange);
        fansListv.setOnItemClickListener(onItemClick);
    }
	
	public void notifyAdapter(List<Fan> fanList, Integer isnext) {
        ishasNext = isnext;
        fansListv.setIHasNext(ihn);
        fansListv.setXListViewListener(ixl);
		listAdapter.notifyAdapter(fanList);
		fansListv.setOnScrollItemChange(onScrollItemChange);
        fansListv.setOnItemClickListener(onItemClick);
	}

    private OnItemClickListener onItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (view!=fansListv.getFooterView() && view!=fansListv.getHeadView()) {
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

	private OnScrollItemChange onScrollItemChange = new OnScrollItemChange() {
		@Override
		public void loadHead(View view, int position) {
			if(position<listAdapter.getCount() && view!=null) {
				Fan fan = listAdapter.getItem(position);

                mImageFetcher.loadImage(fan.getProfileimg(),
                        (ImageView)view.findViewById(R.id.item_friend_head), GUIUtil.getDpi(getActivity(), R.dimen.margin_8));
			}
		}
	};

    public MyXListView getXListV() {
        return  fansListv;
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    public void removeFooter() {
        fansListv.removeFooter();
    }

    private MyXListView.IHasNext ihn = new MyXListView.IHasNext() {
        @Override
        public Integer getHasNext() {
            return ishasNext;
        }
    };

    public void setIXL(XListView.IXListViewListener l) {
        ixl = l;
    }
    private XListView.IXListViewListener ixl;

    public void onLoaded() {
        fansListv.onLoaded();
    }
}
