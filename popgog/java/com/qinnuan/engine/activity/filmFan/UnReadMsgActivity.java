package com.qinnuan.engine.activity.filmFan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.activity.user.DynamicFanActivity;
import com.qinnuan.engine.adapter.BaoguBaseAdapter;
import com.qinnuan.engine.util.view.MyXListView;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.api.filmFan.GetUserUnreadMessage;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.Message;
import com.qinnuan.engine.util.view.XListView;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class UnReadMsgActivity extends BaseActivity {

    public static String COUNT_ID = "count_id";
    private int count = -1;
    private int pagesize = 0;
    private Integer isnext = 0;
    private boolean isNotify = false;
    MsgAdapter msgAdapter;
    private String cdynamicmessageid;

    private UnReadMsgActivity instance = this;
    private MyXListView listv;
    private GetUserUnreadMessage unreadmsgParam = new GetUserUnreadMessage();

    private List<Message> msgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unreadmsg);
        getTitleWidget().getLeftL().setOnClickListener(onClickL);
        count = getIntent().getIntExtra(COUNT_ID, -1);
        listv = (MyXListView)findViewById(R.id.activity_unreadmsg_listv);
        listv.setPullRefreshEnable(false);
        listv.setPullLoadEnable(true);
        //listv.setPullLoadEnable(false);
        httpMsg();
    }

    private void httpMsg() {
        unreadmsgParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        unreadmsgParam.setUserid(Const.user.getUserid());
        unreadmsgParam.setDevicetype(Const.DEVICE_TYPE);
        if(isNotify) {
            pagesize = 12;
            unreadmsgParam.setCdynamicmessageid(cdynamicmessageid);
        } else  {
            if(count == -1) {
                pagesize = 12;
            } else {
                pagesize = count;
            }
        }
        unreadmsgParam.setPagesize(pagesize+"");
        request(unreadmsgParam);
    }

    @Override
    protected void requestFailed() {
        super.requestFailed();
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        try {
            parseMsg(conent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        listv.onLoaded();
    }

    private void parseMsg(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            if(Const.HTTP_OK != jobj.getInt("status")) return;
            JSONObject jdata = jobj.getJSONObject("data");
            isnext = jdata.getInt("isnext");
            msgs = JSONTool.getJsonToListBean(Message.class, jdata.getJSONArray("message"));
            if(!msgs.isEmpty()) {
                cdynamicmessageid = msgs.get(msgs.size()-1).getCdynamicmessageid();
            }
            initAdpater();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAdpater() {
        if(!isNotify) {
            msgAdapter = new MsgAdapter(instance, msgs);
            listv.setAdapter(msgAdapter);
        } else {
            msgAdapter.getModelList().addAll(msgs);
            msgAdapter.notifyDataSetChanged();
        }
        listv.setIHasNext(ihn);
        listv.setXListViewListener(ixl);
        listv.setOnItemClickListener(onItemClick);
        if(isnext != 1) listv.removeFooter();
    }

    private XListView.IXListViewListener ixl = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {

        }

        @Override
        public void onLoadMore() {
            isNotify = true;
            httpMsg();
        }
    };

    private View.OnClickListener onClickL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case  R.id.left: {
                    finish();
                } break;
                default:break;
            }
        }
    };

    private MyXListView.IHasNext ihn = new MyXListView.IHasNext() {
        @Override
        public Integer getHasNext() {
            return isnext;
        }
    };

    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(view != listv.getFooterView() && view!=listv.getHeadView()) {
                Message msg = msgAdapter.getItem(position-1);
                String userid = msg.getUserid();
                Intent intent = new Intent(instance, DynamicFanActivity.class);
                if(Const.user.getUserid().equals(userid)) {
                    intent.putExtra(DynamicFanActivity.TYPE_ID, DynamicFanActivity.DYNAMIC_MINE);
                } else {
                    intent.putExtra(DynamicFanActivity.TYPE_ID, DynamicFanActivity.DYNAMIC_OTHER);
                }
                intent.putExtra(DynamicFanActivity.USER_TO_ID, userid);
                startActivity(intent);
            }
        }
    };

  private class MsgAdapter extends BaoguBaseAdapter<Message> {

      public MsgAdapter(Context context, List<Message> list) {
          super(context, list);
      }

      @Override
      public View getView(int position, View view, ViewGroup parent) {
          Message bean = getItem(position) ;
          if(view==null){
              view= LayoutInflater.from(mContext).inflate(R.layout.item_unreadmsg, null) ;
          }
          TextView nikname = (TextView) view.findViewById(R.id.item_unreadmsg_nickname);
          TextView contentText = (TextView) view.findViewById(R.id.item_unreadmsg_contenttext);
          TextView date = (TextView) view.findViewById(R.id.item_unreadmsg_date);
          ImageView head = (ImageView)view.findViewById(R.id.item_unreadmsg_head);
          ImageView contentImg = (ImageView)view.findViewById(R.id.item_unreadmsg_contentimg);

          nikname.setText(bean.getNickname());
          contentText.setText(bean.getContent());
          date.setText(bean.getCreatedate());
          mImageFetcher.loadImage(bean.getProfileimg(), head, GUIUtil.getDpi(instance, R.dimen.margin_8));
          mImageFetcher.loadImage(bean.getDynamicimage(), contentImg);

          return view;
      }
  }

}