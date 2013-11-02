package com.qinnuan.engine.fragment.message;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.engine.xmpp.message.ComparatorMessage;
import com.qinnuan.common.util.DateUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.adapter.MessageListAdapter;
import com.qinnuan.common.http.image.util.ImageFetcher;

import java.util.Collections;
import java.util.List;

@FragmentView(id = R.layout.fragment_message)
public class ChatSessionFragment extends BaseFragment implements
        OnItemClickListener, OnItemLongClickListener {
    @InjectView(id = R.id.listView)
    private ListView listView;

    private MessageListAdapter adapter;
    private ChatSessionOperation operation;
    private ImageFetcher imageFetcher;

    public ChatSessionFragment() {
    }

    public ChatSessionFragment(ImageFetcher imageFetcher) {
        this.imageFetcher = imageFetcher;
    }

    public interface ChatSessionOperation {
        public void onItemClick(MessageListBean bean);

        public void onItemLongClick(MessageListBean bean);
    }

    public void setOperation(ChatSessionOperation operation) {
        this.operation = operation;
    }


    @Override
    public void bindDataForUIElement() {
    }

    @Override
    protected void bindEvent() {
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    public void notifyListUI(List<MessageListBean> list) {
        Collections.sort(list, new ComparatorMessage());
        MessageListBean preBean = null;
        for (int i = 0; i < list.size(); i++) {
            MessageListBean bean = list.get(i);
            if (i == 0) {
                bean.setNeedTime(true);
                preBean = bean;
            } else {
                if (DateUtil.getBetweenDay(preBean.getTime(), bean.getTime()) >= 1) {
                    bean.setNeedTime(true);
                }
                preBean = bean;
            }
        }
        adapter = new MessageListAdapter(getActivity(), list, imageFetcher);
        listView.setAdapter(adapter);
//		adapter.getList().clear();
//		for (MessageListBean bean : list) {
//			adapter.getList().add(bean);
//		}
//		adapter.sortList();
//		adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        MessageListBean bean = adapter.getItem(position);
//		if (bean.getType() != SessionTypeTIME
//				&& bean.getType() != SessionTypeTRENDS) {
        operation.onItemLongClick(bean);
//		}
        return true;
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        MessageListBean bean = adapter.getItem(position);
        operation.onItemClick(bean);
    }
}
