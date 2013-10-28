package com.showu.baogu.fragment.filmFan;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.showu.baogu.R;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.baogu.listener.IForgetPswPhoneListener;
import com.showu.baogu.listener.ISearchFanListener;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.TextUtil;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_friend_search)
public class FriendSearchFragment extends BaseFragment {

    @InjectView(id = R.id.fragment_friend_search_keyword)
    EditText keyword;
    @InjectView(id = R.id.fragment_friend_search_search)
    Button search;

    private ISearchFanListener listener;
    public FriendSearchFragment(ISearchFanListener l) {
        listener = l;
    }

    @Override
    public void bindDataForUIElement() {

    }

    @Override
    protected void bindEvent() {
        search.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_friend_search_search :
                    String keywordStr = keyword.getText().toString();
                    if(TextUtil.isEmpty(keywordStr)) {
                        GUIUtil.toast(getActivity(), "请输入爆谷号或者名字");
                    } else {
                        String saerch = TextUtil.getUrlEncode(keywordStr.trim());
                        listener.search(saerch);
                    }
                    break;
                default :
                    break;
            }
        }
    };

    @Override
    public void leftClick() {
        getActivity().finish();
    }
}