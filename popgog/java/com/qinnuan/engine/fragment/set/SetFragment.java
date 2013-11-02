package com.qinnuan.engine.fragment.set;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.common.util.GUIUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.activity.FilmShareActivity;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.application.MyPref;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.widget.BottomPopWindow;
import com.qinnuan.common.widget.MySlipSwitchWidget;

/**
 * Created by showu on 13-7-22.
 */
@FragmentView(id=R.layout.activity_setting)
public class SetFragment extends BaseFragment implements View.OnClickListener {
    @InjectView(id = R.id.btn_logout)
    private Button mBtnLogout;
    @InjectView(id = R.id.btn_password)
    private Button mPassword;
    @InjectView(id = R.id.btn_black_list)
    private Button mBtnBlackList;
    @InjectView(id = R.id.btn_feedback)
    private Button mBtnFeedback;
    @InjectView(id = R.id.btn_recommendapp)
    private Button mBtnRecommendApp;
    @InjectView(id = R.id.btn_userhelp)
    private Button mBtnUserHelp;
    @InjectView(id = R.id.clean_cach)
    private Button mBtnClean;
    @InjectView(id = R.id.btn_shareto_friend)
    private Button mBtnShareToFriend;
    @InjectView(id = R.id.layout_version)
    private RelativeLayout mLayoutVersion;
    @InjectView(id = R.id.rlayout_shake)
    private RelativeLayout mLayoutShake;
    @InjectView(id = R.id.rlayout_update)
    private RelativeLayout mLayoutUpdate;
    @InjectView(id = R.id.tv_version)
    private TextView mTvVersion;
    @InjectView(id = R.id.cb_sound)
    private MySlipSwitchWidget sound;
    @InjectView(id = R.id.cb_shake)
    private MySlipSwitchWidget shake;
    @InjectView(id = R.id.cb_autoupdate)
    private MySlipSwitchWidget update;

    private MyPref basePref;
    private  SettingOperation operation ;
    private String[] menus = { "发送手机短信", "分享到新浪微博", "分享到微信" };
    public SetFragment() {
    }

    public SetFragment(SettingOperation operation) {
        this.operation = operation;
    }

    public interface SettingOperation {
        public void checkVersion();
        public void   showBlackList();
        public void  showFeedback();
        public void  showRecommendApp();
        public void  showUserHelp();
        public void   cleanCach();
        public void showUpdateDialog();
        //public void showModifyPassword();
        //public void showForgetPassword();

        public void logout() ;
    }

    @Override
    public void bindDataForUIElement() {
        basePref = MyPref.getInstance(getActivity());
        sound.setStatus(basePref.isSoundOpen());
        shake.setStatus(basePref.isShakeOpen());
        update.setStatus(basePref.isAutoUpdate());
        mTvVersion.setText(GUIUtil.getVersionName(getActivity())+" ");
    }

    @Override
    protected void bindEvent() {
        mPassword.setOnClickListener(this);
        mBtnUserHelp.setOnClickListener(this);
        mBtnRecommendApp.setOnClickListener(this);
        mBtnShareToFriend.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);
        sound.setMSaveInter(new MySlipSwitchWidget.SaveStatus() {
            @Override
            public void save(boolean status) {
                basePref.saveSoundOpen(sound.getStatus());
            }
        });
        shake.setMSaveInter(new MySlipSwitchWidget.SaveStatus() {
            @Override
            public void save(boolean status) {
                basePref.saveShakeOpen(shake.getStatus());
            }
        });
        update.setMSaveInter(new MySlipSwitchWidget.SaveStatus() {
            @Override
            public void save(boolean status) {
                basePref.saveAutoUpdate(update.getStatus());
            }
        });
        mBtnBlackList.setOnClickListener(this);
        mBtnClean.setOnClickListener(this);
        mBtnFeedback.setOnClickListener(this);
        mLayoutVersion.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:
                operation.logout();
//                BaoGuAlertUtil.showAlert(getActivity(), "提示",
//                        "退出账号将收不到消息推送", dialogClick);
                break;
            case R.id.btn_password:
                if (Const.user.getIsphoneverify() == 1) {
                    operation.showUpdateDialog();
                    //showPasswordSettingDialog();
                } else {
                    GUIUtil.toast(getActivity(), R.string.banding_emailorphone_forget);
                }
                break;

            case R.id.layout_version:
               operation.checkVersion();
                break;

            case R.id.btn_black_list:
                operation.showBlackList();
                break;

            case R.id.btn_shareto_friend:
                showButtomPop(onMenuSelect, tWidget, menus);
                break;

            case R.id.btn_feedback:
                operation.showFeedback();
                break;

            case R.id.btn_recommendapp:
                operation.showRecommendApp();
                break;

            case R.id.btn_userhelp:
                operation.showUserHelp();
                break;

            case R.id.clean_cach:
                operation.cleanCach();
                break;

            case R.id.rlayout_sound:
                soundClick();
                break;

            case R.id.rlayout_shake:
                shakeClick();
                break;

            case R.id.rlayout_update:
                updateClick();
                break;

            default:
                break;
        }
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    private void soundClick() {
        LogUtil.e(getClass(), "Sound state:" + sound.getStatus());
        sound.setStatus(!sound.getStatus());
        basePref.saveSoundOpen(sound.getStatus());
    }

    private void shakeClick() {
        shake.setStatus(!shake.getStatus());
        basePref.saveShakeOpen(shake.getStatus());
    }

    private void updateClick() {
        update.setStatus(!update.getStatus());
        basePref.saveAutoUpdate(update.getStatus());
    }

//    private void showPasswordSettingDialog() {
//        new AlertDialog.Builder(getActivity())
//                .setTitle("密码设置")
//                .setIcon(android.R.drawable.ic_dialog_info)
//                .setItems(R.array.app_setting_password,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                switch (which) {
//                                    case 0:
//                                        operation.showModifyPassword();
//                                        dialog.dismiss();
//                                        break;
//                                    case 1:
//                                        operation.showForgetPassword();
//                                        dialog.dismiss();
//                                        break;
//
//                                    default:
//                                        break;
//                                }
//                            }
//                        }).setNegativeButton(R.string.cancel, null).show();
//    }

    private int sms_position = 0;
    private int weibo_position = 1;
    private int weixin_position = 2;
    private int qq_zone = 3;
    private BottomPopWindow.OnMenuSelect onMenuSelect = new BottomPopWindow.OnMenuSelect() {

        @Override
        public void onItemMenuSelect(int position) {
            if (position == sms_position) {
                sendSMS();
            } else if (position == weibo_position) {
                // share to weibo
                shareToWeibo();
            } else if (position == weixin_position) {
                shareToWX();
            } else if (position == qq_zone) {
                // share to qq
                shareAddTopic();
            }

            buttomPop.dismiss();
        }

        @Override
        public void onCancelSelect() {
            buttomPop.dismiss();
        }
    };
    String shareContent = "推荐手机应用#爆谷电影#, 能发现身边的影迷, 约人看电影, 还能买电影票";
    private void sendSMS() {
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", shareContent);
        getActivity().startActivity(intent);
    }

    private void shareAddTopic() {
//		Intent intent = new Intent(getActivity(), FilmShareActivity.class);
//		intent.putExtra("content", shareContent);
//		intent.putExtra("type", 1);
//		startActivity(intent);
    }

    private void shareToWX() {
		Intent intent = new Intent(getActivity(), FilmShareActivity.class);
		intent.putExtra(FilmShareActivity.EXT_SHARE_CONTENT, shareContent);
		intent.putExtra(FilmShareActivity.EXT_SHARE_TYPE, FilmShareActivity.WECHAT);
		startActivity(intent);
    }

    /** share to weibo */
    private void shareToWeibo() {
		Intent intent = new Intent(getActivity(), FilmShareActivity.class);
		intent.putExtra(FilmShareActivity.EXT_SHARE_CONTENT, shareContent);
		intent.putExtra(FilmShareActivity.EXT_SHARE_TYPE, FilmShareActivity.WEIBO);
		startActivity(intent);
    }

//    private DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
//
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            operation.logout() ;
//        }
//    };
}
