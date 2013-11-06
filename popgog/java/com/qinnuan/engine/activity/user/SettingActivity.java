package com.qinnuan.engine.activity.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qinnuan.engine.activity.message.ChatActivity;
import com.qinnuan.engine.api.UpdateUserPassword;
import com.qinnuan.engine.bean.VersionBean;
import com.qinnuan.engine.fragment.set.ModifypasswordFragment;
import com.qinnuan.engine.fragment.set.SetFragment;
import com.qinnuan.engine.fragment.set.WebFragment;
import com.qinnuan.engine.fragment.user.SavePswFragment;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.activity.filmFan.FriendBlackActivity;
import com.qinnuan.engine.api.GetVersioninfo;
import com.qinnuan.engine.api.UpdateUserPasswordPhone;
import com.qinnuan.engine.api.UpdateUserPasswordQuit;
import com.qinnuan.engine.api.UpdateUserQuit;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.listener.IForgetPswNewPswListener;
import com.qinnuan.engine.xmpp.ServiceManager;
import com.qinnuan.common.share.SinaWeiboShare;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class SettingActivity extends BaseActivity implements SetFragment.SettingOperation {

    private SettingActivity instance = this;
    private VersionBean versionBean;
    private GetVersioninfo getVersioninfo = new GetVersioninfo();
    private String[] menus = {"发送手机短信", "分享到新浪微博", "分享到微信"};

    private final int REQUEST_SUCCESS = 1;
    private final int CHECK_VERSION = 2;
    private final int CLEAN_CACH = 3;

    private SetFragment setFragment;
    private ModifypasswordFragment fragment;
    private SavePswFragment savePswF;

    private boolean isLogoutUpdatePsw = false;

    private UpdateUserPassword updateUserPswParam = new UpdateUserPassword();

    //用户退出
    private UpdateUserQuit quitParam = new UpdateUserQuit();

    //用户修改密码(第三方注册,手机绑定)
    private UpdateUserPasswordPhone updatePswPhone = new UpdateUserPasswordPhone();

    //用户退出(第三方绑定手机后退出修改密码)
    private UpdateUserPasswordQuit updateQuitParam = new UpdateUserPasswordQuit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragment = new SetFragment(this);
        addFragment(setFragment, false);
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        try {
            //JSONObject jsonObject = new JSONObject(conent) ;
            if (url.contains(updateUserPswParam.getApi())) {
                parseUpdatePsw(conent);
            } else if (url.contains(quitParam.getApi())) {
                paraseLogout(conent);
            } else if (url.contains(getVersioninfo.getApi())) {
                parseVersionInfo(conent);
            } else if (url.contains(updateQuitParam.getApi())) {
                //finish();
                //Const.user.setIspassword(0);
                //savePswF.back();
                clearUser();
            } else if (url.contains(updatePswPhone.getApi())) {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseUpdatePsw(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int status = jsonObject.getInt("status");
            if (status == Const.HTTP_OK) {
                GUIUtil.toast(this, R.string.modify_pass_success);
                getSupportFragmentManager().popBackStack();
            } else if(status == 3){
                GUIUtil.toast(this, "新密码和旧密码不能相同!");
            } else {
                GUIUtil.toast(this, R.string.modify_pass_fail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
//			case HTTP_OK:
//				SinaWeiboShare.getInstance().cleanToken(SettingActivity.this);
//				showLogin();
//				break;

                case CHECK_VERSION:
                    parseVersionInfo(msg.obj.toString());
                    break;

                case CLEAN_CACH:
                    dimissDialog();
//				toastString(R.string.cache_clean_suc);
                    break;
                default:
                    break;
            }
        }

    };

    private void parseVersionInfo(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            int status = jsonObject.getInt("status");
            if (status == 1) {
                versionBean = JSONTool.jsonToBean(VersionBean.class, jsonObject);
                if (versionBean.getAndroidversionnum() > GUIUtil.getVersionCode(this)) {
                    creatUpdateDialog();
                } else {
                    GUIUtil.toast(this, "当前版本已经是最新版本");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void creatUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(versionBean.getAndroiddescription()).setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        downloadApk();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).setTitle("版本更新");
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void creatCleanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("你确定清除缓存吗？").setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mImageFetcher.clearCache();
                        GUIUtil.toast(SettingActivity.this, "清理成功");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).setTitle("清理缓存");
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void downloadApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(versionBean
                .getAndroiddownloadurl()));
        startActivity(intent);
    }


//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN) {
//            Bundle bundle = new Bundle();
//            bundle.putInt("logout",0);
//            Intent intent=new Intent() ;
//            intent.putExtras(bundle);
//            setResult(Activity.RESULT_OK, intent);
//            SettingActivity.this.finish();
//            return super.dispatchKeyEvent(event);
//        } else {
//            return super.dispatchKeyEvent(event);
//        }
//    }


    public void modifyPassWord(String oldPassword, String newpassword) {
        updateUserPswParam.setUserid(Const.user.getUserid());
        updateUserPswParam.setOldpassword(oldPassword);
        updateUserPswParam.setNewpassword(newpassword);
        updateUserPswParam.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        request(updateUserPswParam);
    }

    public void cleanCach() {
        creatCleanDialog();
    }

    @Override
    public void showUpdateDialog() {
        if (Const.user.getIspassword() == 1) {
            isLogoutUpdatePsw = false;
            ShowsetPswSnsDialog();
        } else {
            showPasswordSettingDialog();
        }
    }


    /**
     * 删除文件夹里面的所有文件
     *
     * @param path String 文件夹路径 如 c:/fqf
     */
    public void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
            }
        }
    }

    public void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹

        } catch (Exception e) {
            System.out.println("删除文件夹操作出错");
            e.printStackTrace();

        }
    }

    private void showPasswordSettingDialog() {
        new AlertDialog.Builder(instance)
                .setTitle("密码设置")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setItems(R.array.app_setting_password,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        showModifyPassword();
                                        dialog.dismiss();
                                        break;
                                    case 1:
                                        showForgetPassword();
                                        dialog.dismiss();
                                        break;

                                    default:
                                        break;
                                }
                            }
                        })
                .setNegativeButton(R.string.cancel, null).show();
    }


    public void showModifyPassword() {
        fragment = new ModifypasswordFragment();
        replaceFragment(fragment, true);
    }

    public void showForgetPassword() {
        Intent intent = new Intent(this, ForgetPswActivity.class);
        startActivity(intent);
    }

    @Override
    public void logout() {
        if (Const.user.getIspassword() == 1) {
            isLogoutUpdatePsw = true;
            ShowsetPswSnsDialog();
        } else {
            GUIUtil.showDialog(SettingActivity.this, "退出账号将收不到消息推送", "退出", "取消",
                    new GUIUtil.IOnAlertDListener() {
                        @Override
                        public void ok() {
                            clearUser();
                        }

                        @Override
                        public void cancel() {

                        }
                    });
        }
    }

    //推出openfire
    private void clearUser() {
        ServiceManager manager = new ServiceManager(this);
        manager.stopService();
        basePref.setUser(null);
        SinaWeiboShare.getInstance().cleanToken(this);
        httpLogout();
        Const.user = null;
    }

    private IForgetPswNewPswListener savePswL = new IForgetPswNewPswListener() {
        @Override
        public void updatePsw(String psw) {
            httpUpdatePswBySnsLogout(psw);
        }
    };


    private void paraseLogout(String content) {
        try {
            JSONObject jsonObject = new JSONObject(content);
            if (Const.HTTP_OK == jsonObject.getInt("status")) {
                // finish();
            }
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finish();
        }
    }

    private void httpLogout() {
        quitParam.setDevicetype(Const.DEVICE_TYPE);
        quitParam.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        quitParam.setUserid(Const.user.getUserid());
        request(quitParam);
    }

    public void checkVersion() {
        request(getVersioninfo);
    }

    private void sendHandMessage(int wath, Object o) {
        Message msg = new Message();
        msg.what = wath;
        msg.obj = o;
        handler.sendMessage(msg);
    }


    public void showBlackList() {
        startActivity(new Intent(this, FriendBlackActivity.class));
    }

    public void showFeedback() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.ACTIVTY_EX_ID, Const.officalUser.getUserid());
        intent.putExtra(ChatActivity.ACTIVTY_EX_ROOM, "0");
        intent.putExtra(ChatActivity.ACTIVTY_EX_PROFILE, Const.officalUser.getUserid());
        intent.putExtra(ChatActivity.ACTIVTY_EX_NIKENAME, "");
        intent.putExtra(ChatActivity.ACTIVTY_EX_TYPE, 1);
        startActivity(intent);
    }

    /**
     * 推荐应用-暂时用的地址是ios版本的，发布新版本的时候需要改成android版本的网址
     */
    public void showRecommendApp() {
        WebFragment webFragment = new WebFragment(Const.urlBean.getApp_plugin_url() + "/android/rec/applist", "推荐应用");
        replaceFragment(webFragment, true);
    }

    public void showUserHelp() {
        WebFragment webFragment = new WebFragment(Const.urlBean.getApp_plugin_url() + "/android/doc/help", "用户帮助");
        replaceFragment(webFragment, true);
    }

    private String pswStr;
    private String pswRepStr;
    private EditText psw;
    private EditText pswrep;
    private AlertDialog alertDialog;

    private void ShowsetPswSnsDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_set_psw, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        psw = (EditText) promptsView.findViewById(R.id.dialog_set_psw_psw);
        pswrep = (EditText) promptsView.findViewById(R.id.dialog_set_psw_pswrepeat);
        final Button setpsw = (Button) promptsView.findViewById(R.id.dialog_set_psw_setpsw);
        final Button cancel = (Button) promptsView.findViewById(R.id.dialog_set_psw_cancel);
        setpsw.setOnClickListener(onClickSetPsw);
        cancel.setOnClickListener(onClickSetPsw);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private View.OnClickListener onClickSetPsw = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.dialog_set_psw_setpsw: {
                    updatePswSns();

                    break;
                }

                case R.id.dialog_set_psw_cancel: {
                    if (alertDialog != null)
                        alertDialog.cancel();
                    break;
                }
                default:
                    break;
            }
        }
    };

    //用户退出(第三方绑定手机后退出修改密码)
    private void httpUpdatePswBySnsLogout(String psw) {
        updateQuitParam.setDevicetype(Const.DEVICE_TYPE);
        updateQuitParam.setUserid(Const.user.getUserid());
        updateQuitParam.setDeviceidentifyid(GUIUtil.getDeviceId(SettingActivity.this));
        updateQuitParam.setNewpassword(psw);
        request(updateQuitParam);
    }

    //第三方绑定手机后修改密码
    private void httpUpdatePswBySns(String newPsw) {
        updatePswPhone.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        updatePswPhone.setDevicetype(Const.DEVICE_TYPE);
        updatePswPhone.setNewpassword(newPsw);
        updatePswPhone.setUserid(Const.user.getUserid());
        request(updatePswPhone);
    }

    private void updatePswSns() {
        pswStr = psw.getText().toString();
        pswRepStr = pswrep.getText().toString();
        if (TextUtil.isEmpty(pswStr) || TextUtil.isEmpty(pswRepStr)) {
            GUIUtil.toast(instance, "密码不能为空！");
        } else {
            if (pswStr.length() < 4) {
                GUIUtil.toast(instance, "密码不少于4位");
            } else {
                if (!pswStr.equals(pswRepStr)) {
                    GUIUtil.toast(instance, "密码不一致！");
                } else {
                    if (isLogoutUpdatePsw)
                        httpUpdatePswBySnsLogout(pswStr);
                    else
                        httpUpdatePswBySns(pswStr);
                    if (alertDialog != null)
                        alertDialog.cancel();
                }
            }
        }
    }

}
