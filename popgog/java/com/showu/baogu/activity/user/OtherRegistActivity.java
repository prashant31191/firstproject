package com.showu.baogu.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.showu.baogu.R;
import com.showu.baogu.activity.BaseActivity;
import com.showu.baogu.api.RegisterUserSns;
import com.showu.baogu.application.Const;
import com.showu.baogu.fragment.user.OtherLoginHomeFragment;
import com.showu.baogu.listener.IOtherLoginListener;
import com.showu.baogu.xmpp.ServiceManager;
import com.showu.common.share.SinaListener;
import com.showu.common.share.SinaWeiboShare;
import com.showu.common.util.FileUtil;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;
import com.showu.common.util.image.crop.CropActivity;
import com.weibo.sdk.android.Oauth2AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class OtherRegistActivity extends BaseActivity {

    private static final int CHOICE_IMAGE = 0x1f;
    private OtherRegistActivity instance = this;

    private OtherLoginHomeFragment loginF;

    //上传图片返回json
    //{"status":1,
    // "httpurls":"http://mediatest.popgog.com/media/user/a831162b-1b09-4531-84cb-655fa53d65da.jpg"}
    private String imgUrl;
    public static String EXT_SNS_TYPE = "sns_type";
    public static String EXT_SNS_ID = "sns_id";
    private final int LOCALE_IMG_CROP = 11111;
    private final int LOCALE_IMG_CROP_READ = 11112;
    private int snsType;
    private String snsId;
    private RegisterUserSns registerUserSns = new RegisterUserSns();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snsType = getIntent().getIntExtra(EXT_SNS_TYPE, 0);
        snsId = getIntent().getStringExtra(EXT_SNS_ID);
        loginF = new OtherLoginHomeFragment(otherLoginL);
        replaceFragment(loginF, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;
        switch (requestCode) {
            case LOCALE_IMG_CROP: {
                LogUtil.e(getClass(), selectedImageUri);
                startPhotoCrop(selectedImageUri, LOCALE_IMG_CROP_READ); // 图片裁剪
                break;
            }

            case LOCALE_IMG_CROP_READ: {
                if(data != null) {
                    readCropImage(data);
                    loginF.setHeadImg(photo);
                }
                break;
            }

            case CHOICE_IMAGE: {
                if(data != null) {
                    Uri originalUri = data.getData(); // 获得图片的uri
                    startPhotoCrop(originalUri, LOCALE_IMG_CROP_READ); // 图片裁剪
                }
                break;
            }

		    /*case Constants.PHOTO_PICKED_WITH_DATA:
            readLocalImage(data, Constants.PHOTO_PICKED_WITH_DATA);
			break;*/
            default:
                break;
        }
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        if (url.contains(registerUserSns.getApi())) {
            parseRegist(conent);
        } else {
            parseUploadImg(conent);
        }
    }

    @Override
    protected void requestFailed() {
        super.requestFailed();
    }

    private void startOpenfire() {
        if (Const.user != null) {
            ServiceManager serviceManager = new ServiceManager(this);
            serviceManager.startService();
        }
    }
    private void parseRegist(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            if (Const.HTTP_OK == jobj.getInt("status")) {
                basePref.setUser(json);
                parseUserInfo(json);
                startOpenfire();
                finish();
            } else {
                GUIUtil.toast(instance, "注册失败,该号码已经注册过！");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseUploadImg(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            if (Const.HTTP_OK == jobj.getInt("status")) {
                imgUrl = jobj.getString("httpurls");
//                mImageFetcher.loadImage(imgUrl, loginF.getHeadImgv());
                registerUserSns.setProfileimg(imgUrl);
                if (snsType == UserActivity.SNS_TYPE_WEIBO) {
                    SinaWeiboShare.getInstance().getUserInfo(Long.parseLong(snsId), sinaListener);
                }else if(snsType==UserActivity.SNS_TYPE_QQ){
                    registerUserSns.setOpenid(snsId);
                    request(registerUserSns);
                }
            } else {
                GUIUtil.toast(instance, R.string.regist_phone_upload_fail);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    IOtherLoginListener otherLoginL = new IOtherLoginListener() {


        @Override
        public void setHeadImg(int type) {
            if(type==1){
            startCamera();
            }else{
                Intent mIntent = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(mIntent, CHOICE_IMAGE);
            }
        }

        @Override
        public void regist(String name, String sex) {
            registerUserSns.setSnstype(snsType + "");
            registerUserSns.setSnsuserid(snsId);
            registerUserSns.setLatitude(Const.point.getGeoLat().toString());
            registerUserSns.setLongitude(Const.point.getGeoLng().toString());
            registerUserSns.setLogincityid(Const.city.getCityid());
            registerUserSns.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
            registerUserSns.setNickname(name);
            registerUserSns.setSex(sex);
            if(imgFile != null) {
                uploadFile("1", imgFile);
            } else {
                GUIUtil.toast(instance, "请选择头像！");
            }
        }

    };

    private SinaListener sinaListener = new SinaListener() {
        @Override
        public void authSuccess(Oauth2AccessToken accessToken, String uid, String userName) {

        }

        @Override
        public void failed() {

        }

        @Override
        public void requestSucessSina(String response) {
//            try {
//                JSONObject object = new JSONObject(response);
//                boolean verified = object.getBoolean("verified");
//                if (verified) {
//                    registerUserSns.setIsverified("1");
//                } else {
//                    registerUserSns.setIsverified("0");
//                }
//                String name = object.getString("name") ;
//                registerUserSns.setSnsname(name);
//                registerUserSns.setAccesstoken(SinaWeiboShare.getInstance().accessToken.getToken());
//                registerUserSns.setExpireddate(SinaWeiboShare.getInstance().getAccessToken().getExpiresTime()+"");
//                request(registerUserSns);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            sendMsg(response);

        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1111) {
                final String response = (String)msg.obj;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        httpRegistSns(response);
                    }
                });
            }
        }
    };

    private void httpRegistSns(String response) {
        try {
            JSONObject object = new JSONObject(response);
            boolean verified = object.getBoolean("verified");
            if (verified) {
                registerUserSns.setIsverified("1");
            } else {
                registerUserSns.setIsverified("0");
            }
            String name = object.getString("name") ;
            registerUserSns.setSnsname(name);
            registerUserSns.setAccesstoken(SinaWeiboShare.getInstance().accessToken.getToken());
            registerUserSns.setExpireddate(SinaWeiboShare.getInstance().getAccessToken().getExpiresTime()+"");
            request(registerUserSns);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(String response) {
        Message msg = new Message();
        msg.obj = response;
        msg.what = 1111;
        handler.sendMessage(msg);
    }

    private Uri selectedImageUri = null;

    public void startCamera() {
        String FILE_NAME = UUID.randomUUID() + ".jpg";
        File photo = FileUtil.getNewFile(this, FILE_NAME);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (photo != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
            selectedImageUri = Uri.fromFile(photo);
            startActivityForResult(intent, LOCALE_IMG_CROP);
        }
    }

    private void startPhotoCrop(Uri uri, int reqCode) {
        Intent intent = new Intent(instance, CropActivity.class);
        intent.putExtra(Const.IMAGE_URI, uri);
        startActivityForResult(intent, reqCode);
    }

    private File imgFile;
    private Bitmap photo;

    /**
     * 读取裁剪好的图片
     */
    private void readCropImage(Intent data) {

        if (data != null) {
            Uri uri = data.getParcelableExtra(Const.CROP_IMAGE_URI);
            photo = getBitmap(uri);
            if (photo != null) {
                LogUtil.i(getClass(), "URI:" + uri);
                String thePath = FileUtil.getAbsolutePathFromNoStandardUri(uri);
                imgFile = new File(thePath);
                LogUtil.i(getClass(), "readCropImage->imgFile:" + imgFile);
            }
        }
    }

    private Bitmap getBitmap(Uri uri) {
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            is = getInputStreamByUri(uri);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 获取输入流
     */
    public InputStream getInputStreamByUri(Uri mUri) {
        try {
            if (mUri.getScheme().equals("file")) {
                return new java.io.FileInputStream(mUri.getPath());
            } else {
                return getContentResolver().openInputStream(mUri);
            }
        } catch (Exception ex) {
            return null;
        }
    }

}
