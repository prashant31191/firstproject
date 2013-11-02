package com.qinnuan.engine.activity.film;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.qinnuan.engine.api.film.DeleteGroupUser;
import com.qinnuan.engine.api.film.GetTogetherseefilmSeatlist;
import com.qinnuan.engine.fragment.film.GroupSeatFragment;
import com.qinnuan.engine.fragment.film.RenameGroupFragment;
import com.qinnuan.engine.xmpp.provider.RoomSessionDaoImpl;
import com.qinnuan.engine.xmpp.provider.SessionManager;
import com.qinnuan.common.share.WXShare;
import com.qinnuan.common.util.FileUtil;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.api.film.UpdateGroupName;
import com.qinnuan.engine.api.film.UpdateGroupNotice;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.film.GroupShowInfoBean;
import com.qinnuan.engine.fragment.film.GroupChatSettingFragment;
import com.qinnuan.engine.xmpp.provider.ChatDBManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by showu on 13-8-12.
 */
public class GroupActivity extends BaseActivity {
    public static String EXT_GROUP_ID = "group_id";
    public static String EXT_GROUP_NAME="group_name" ;
    public static String EXT_GROUP_DELETE="delete" ;
    private GetTogetherseefilmSeatlist getTogetherseefilmSeatlist = new GetTogetherseefilmSeatlist();
    private UpdateGroupName updateGroupName = new UpdateGroupName();
    private UpdateGroupNotice updateGroupNotice = new UpdateGroupNotice();
    private DeleteGroupUser deleteGroupUser = new DeleteGroupUser();
    private GroupShowInfoBean groupShowInfoBean;

    private GroupChatSettingFragment groupChatSettingFragment;
    private GroupSeatFragment groupSeatFragment;
    private RenameGroupFragment renameGroupFragment;
    private String groupId;
    private String roomName;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupId = getIntent().getStringExtra(EXT_GROUP_ID);
        groupSeatFragment = new GroupSeatFragment(mImageFetcher);
        addFragment(groupSeatFragment, false);
        getGroupShowuInfo();
    }
    public void shareFriend() {
        WXShare.getInstance(this).shareImg(FileUtil.getCachDir(this) + File.separator + "1.jpg");
    }
    private void getGroupShowuInfo() {
        getTogetherseefilmSeatlist.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        getTogetherseefilmSeatlist.setGroupid(groupId);
        if (Const.user != null) {
            getTogetherseefilmSeatlist.setUserid(Const.user.getUserid());
        }
        request(getTogetherseefilmSeatlist);
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        try {
            if (url.contains(getTogetherseefilmSeatlist.getApi())) {//获取场次信息
                parseGroupShowInfo(conent);
            } else if (url.contains(updateGroupName.getApi())) {//修改群名字
                parseUpdateGroupName(conent);
            } else if (url.contains(updateGroupNotice.getApi())) {//修改通知
            } else if (url.contains(deleteGroupUser.getApi())) {
                parseDeleteGroup(conent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseDeleteGroup(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        int status = jsonObject.getInt("status");
        if (status == 1) {
            SessionManager.getInstance(this).deleteRoomSession(groupId);
            ChatDBManager.getInstance(this).deleteRoomById(groupId);
            Intent i = new Intent() ;
            i.putExtra(EXT_GROUP_DELETE,true);
            setResult(RESULT_OK,i);
            finish();
        }
    }

    private void parseUpdateGroupName(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        int status = jsonObject.getInt("status");
        if (status == 1) {
            getSupportFragmentManager().popBackStack();
            RoomSessionDaoImpl dao = new RoomSessionDaoImpl(this);
            dao.updateRoomName(groupId, roomName);
            groupShowInfoBean.getGroups().setGroupname(roomName);
            GUIUtil.toast(this, "修改群名成功");
        } else {
            GUIUtil.toast(this, "修改群名失败");
        }
    }

    private void parseGroupShowInfo(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        int status = jsonObject.getInt("status");
        if (status == 1) {
            groupShowInfoBean = gson.fromJson(jsonObject.getJSONObject("data").toString(), GroupShowInfoBean.class);
            groupSeatFragment.initSeat(groupShowInfoBean);
        }
    }

    public void reNameGroup() {
        renameGroupFragment = new RenameGroupFragment(groupShowInfoBean.getGroups().getGroupname());
        replaceFragment(renameGroupFragment, true);
    }

    public void startGroupSetting() {
        groupChatSettingFragment = new GroupChatSettingFragment(groupShowInfoBean.getGroups());
        replaceFragment(groupChatSettingFragment, true);
    }


    public void deleteGroup() {
        deleteGroupUser.setUserid(Const.user.getUserid());
        deleteGroupUser.setGroupid(groupId);
        deleteGroupUser.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        request(deleteGroupUser);
    }

    public void changeNotice(boolean isSelect) {
        updateGroupNotice.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        updateGroupNotice.setUserid(Const.user.getUserid());
        updateGroupNotice.setGroupid(groupId);
        int flag = isSelect ? 1 : 0;
        updateGroupNotice.setIsnotice(flag);
        request(updateGroupNotice);
    }

    public void updateGroupName(String groupName) {
        this.roomName = groupName;
        groupShowInfoBean.getGroups().setGroupname(groupName);
        updateGroupName.setUserid(Const.user.getUserid());
        updateGroupName.setGroupid(groupId);
        updateGroupName.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        updateGroupName.setGroupname(groupName);
        request(updateGroupName);
    }
}
