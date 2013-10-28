package com.showu.baogu.fragment.message;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.showu.baogu.R;
import com.showu.baogu.activity.filmFan.FanInfoActivity;
import com.showu.baogu.adapter.HelloMessageAdapter;
import com.showu.baogu.bean.MessageListBean;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;

/**
 * Created by showu on 13-7-24.
 */
@FragmentView(id= R.layout.activity_hello_message)
public class HelloFragment extends BaseFragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    @InjectView(id=R.id.listView)
    private ListView listView ;
    private HelloMessageAdapter adapter ;
    @Override
    public void bindDataForUIElement() {

    }

    @Override
    protected void bindEvent() {
            listView.setOnItemClickListener(this);
    }
    public void setAdapter(HelloMessageAdapter adapter){
        this.adapter=adapter ;
        listView.setAdapter(adapter);
    }

    public void notifyDataSetChanged(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MessageListBean bean =adapter.getItem(i) ;
        Intent intent = new Intent(getActivity(), FanInfoActivity.class) ;
        intent.putExtra(FanInfoActivity.FAN_ID,bean.getTargetId()) ;
        intent.putExtra(FanInfoActivity.CHECK_TYPE,3) ;
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }
}
