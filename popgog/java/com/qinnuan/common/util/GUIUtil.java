package com.qinnuan.common.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

import com.qinnuan.engine.application.Const;

/**
 * Created by JingHuiLiu on 13-7-6.
 */
public class GUIUtil {

    private static final int TOAST_LENGTH_SHORT = Toast.LENGTH_SHORT;
//  private static final int TOAST_LENGTH_LONG = Toast.LENGTH_LONG;

    public static void toast(Context context, int resId) {
        Toast myToast =  Toast.makeText(context,resId, TOAST_LENGTH_SHORT);
        myToast.setGravity(Gravity.CENTER, 0, 0);
        myToast.show();
    }

    public static void toast(Context context, CharSequence text) {
        Toast myToast =  Toast.makeText(context, text, TOAST_LENGTH_SHORT);
        myToast.setGravity(Gravity.CENTER, 0, 0);
        myToast.show();
    }

    public static String getDeviceId(Context context) {
        return ((TelephonyManager)context.getSystemService(
                Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    public static Integer getVersionCode(Context context) {
        Integer verCode = 0;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    public static String getVersionName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    public static void showDialog(Context context,
                                  String msg,  String ok, String cancel,
                                  IOnAlertDListener l) {
        il = l;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle(R.string.login_prompt_title);
        builder.setMessage(msg);
        builder.setPositiveButton(ok,
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        il.ok();
                    }
                });
        builder.setNegativeButton(cancel,
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        il.cancel();
                    }
                });
        builder.create().show();
    }

    public interface IOnAlertDListener {
        public void ok();
        public void cancel();
    }
    private static IOnAlertDListener il;

    public static Boolean checkIsOnline(Context c, Class<?> cls) {
         if(Const.user == null) {
             showNeedLoginDialog(c, cls);
             return false;
         } else {
             return true;
         }
    }

    private static void showNeedLoginDialog(final Context context, final Class<?> clazz) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle(R.string.login_prompt_title);
        builder.setMessage("你还没有登录，要登录吗？");
        builder.setPositiveButton("要",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        gotoLogin(context, clazz);
                    }
                });
        builder.setNegativeButton("不要",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private static void gotoLogin(Context context, Class<?> clazz) {
        Intent lIntent = new Intent(context, clazz);
        context.startActivity(lIntent);
    }

    public static boolean ring(Context context) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        Uri ringToneUri = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        try {
            mediaPlayer.setDataSource(context, ringToneUri);

            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
                mediaPlayer
                        .setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                mediaPlayer.setLooping(false);
                mediaPlayer.prepare();
                mediaPlayer.start();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void vibrate(int ms, Context context) {// 输入震动毫秒数
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(ms);
    }

    public static int getDpi(Context c, int resid) {
       return c.getResources().getDimensionPixelSize(resid);
    }


}
