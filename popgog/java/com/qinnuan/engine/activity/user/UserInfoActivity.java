package com.qinnuan.engine.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.qinnuan.engine.api.UpdateUserPopgog;
import com.qinnuan.engine.bean.SNSBean;
import com.qinnuan.engine.fragment.user.UserInfoFragment;
import com.qinnuan.engine.listener.IOnItemClickListener;
import com.qinnuan.engine.listener.IUserInfoListener;
import com.qinnuan.common.share.SinaListener;
import com.qinnuan.common.share.SinaWeiboShare;
import com.qinnuan.common.util.FileUtil;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.api.BindingUserSns;
import com.qinnuan.engine.api.CancelUserSns;
import com.qinnuan.engine.api.UpdateUser;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.User;
import com.qinnuan.engine.fragment.user.BindSinaFragment;
import com.qinnuan.engine.fragment.user.PhoneBindSuccessFragment;
import com.qinnuan.engine.fragment.user.UserInfoUpdateFragment;
import com.qinnuan.common.util.image.crop.CropActivity;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.sso.SsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class UserInfoActivity extends BaseActivity {

    private UserInfoActivity instance = this;

    private UserInfoFragment userInfoF;
    private UserInfoUpdateFragment updateF;
    private BindSinaFragment sinaF;
    private PhoneBindSuccessFragment bindphoneF;


    private UpdateUser updateUserParam = new UpdateUser();
    private UpdateUserPopgog updatePopParam = new UpdateUserPopgog();
    private BindingUserSns bindingUserSns = new BindingUserSns();
    private CancelUserSns cancelSnsParam = new CancelUserSns();
    private static final int CHOICE_IMAGE = 0x1f;
    //上传图片返回json
    //{"status":1,
    // "httpurls":"http://mediatest.popgog.com/media/user/a831162b-1b09-4531-84cb-655fa53d65da.jpg"}
    private String imgUrl;
    public static  String snsName ;
    private final int LOCALE_IMG_CROP = 11111;
    private final int LOCALE_IMG_CROP_READ = 11112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gotoUserInfoPage(Const.user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SsoHandler ssHandler = SinaWeiboShare.getInstance().getmSsoHandler();
        if (ssHandler != null) {
            ssHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        if(resultCode != RESULT_OK) return;
        switch (requestCode) {
            case LOCALE_IMG_CROP: {
                LogUtil.e(getClass(), selectedImageUri);
                startPhotoCrop(selectedImageUri, null, LOCALE_IMG_CROP_READ); // 图片裁剪
            }
            break;
            case LOCALE_IMG_CROP_READ: {
                readCropImage(data);
                userInfoF.setHeadTmp(photo);
                if (imgFile != null) {
                    uploadFile("1", imgFile);
                }
            }
            break;
            case CHOICE_IMAGE:
                Uri originalUri = data.getData();
                startPhotoCrop(originalUri, null, LOCALE_IMG_CROP_READ);
                break;

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
        if (url.contains(updateUserParam.getApi())) {
            parseUpdateSeccuss(conent);
        } else if (url.contains(updatePopParam.getApi())) {
            parseUpdatePopidSuccess(conent);
        } else if(url.contains(bindingUserSns.getApi())){
            parseBindWeibo(conent);
        } else if(url.contains(cancelSnsParam.getApi())) {
            parseCancelSns(conent);
        } else{
            parseUploadImg(conent);
        }
    }

  private void parseBindWeibo(String content){
      try {
          JSONObject object = new JSONObject(content);
          if(Const.HTTP_OK==object.getInt("status")){
              userInfoF.bindingSuccess(snsName);
              startActivity(new Intent(instance, BindSinaShareActivity.class));
          }
      } catch (JSONException e) {
          e.printStackTrace();
      }
  }
    @Override
    protected void requestFailed() {
        super.requestFailed();
    }

    private void parseUpdateSeccuss(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            if (Const.HTTP_OK == jobj.getInt("status")) {
                refreshUserInfo(type);
                gotoUserInfoPage(Const.user);
            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseCancelSns(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            if (Const.HTTP_OK == jobj.getInt("status")) {
                GUIUtil.toast(instance, "成功解除新浪绑定");
                SinaWeiboShare.getInstance().cleanToken(instance);
                Const.user.getSnsList().clear();
                sinaF.back();
            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseUpdatePopidSuccess(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            if (Const.HTTP_OK == jobj.getInt("status")) {
                refreshUserInfo(type);
                GUIUtil.toast(instance, "爆谷号修改成功！");
                gotoUserInfoPage(Const.user);
            } else if (10 == jobj.getInt("status")){
                GUIUtil.toast(instance, "爆谷号只能修改一次！");
                updateF.back();
            } else {
                GUIUtil.toast(instance, "爆谷号修改失败！");
                updateF.back();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void refreshUserInfo(UPDATETYPE type) {
        if(type==UPDATETYPE.HEADIMG){
            Const.user.setProfileimg(imgUrl);
        }else{
        updateF.refreshUserInfo(type);
        }
    }



    private void parseUploadImg(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            if (Const.HTTP_OK == jobj.getInt("status")) {
                imgUrl = jobj.getString("httpurls");
                int size = GUIUtil.getDpi(instance, R.dimen.margin_8);
                mImageFetcher.loadImage(imgUrl, userInfoF.getheadImgv(), size);
                UpdateUser user = new UpdateUser();
                updateUserParam.setUserid(Const.user.getUserid());
                updateUserParam.setDevicetype("1");
                updateUserParam.setDeviceidentifyid(GUIUtil.getDeviceId(this));
                updateUserParam.setProfileimg(imgUrl);
                request(updateUserParam);
            } else {
                GUIUtil.toast(instance, R.string.regist_phone_upload_fail);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private IUserInfoListener userInfoL = new IUserInfoListener() {

        @Override
        public void update(String update, UPDATETYPE type) {
            updatePage(update, type);
        }

        @Override
        public void setHeadImg(String imgUrl, ImageView head) {
            int size = getResources().getDimensionPixelSize(R.dimen.message_round_agle) ;
            mImageFetcher.loadImage(imgUrl, head,size);
        }

        @Override
        public void updateHead(UPDATETYPE typ ,int position) {
             UserInfoActivity.this.type=typ;
            if(position==1){
            startCamera();
            }else{
                Intent mIntent = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(mIntent, CHOICE_IMAGE);
            }
        }

        @Override
        public void bindWeibo() {
            if(Const.user.getSnsList()!=null && Const.user.getSnsList().size()>0){
                gotoSinaPage();
            } else {
                SinaWeiboShare.getInstance().authorize(UserInfoActivity.this, sinaListener);
            }
        }

        @Override
        public void bindPhone() {
            if(Const.user.getIsphoneverify()==1) {
                gotoPhoneBindPage(Const.user.getPhone());
                //GUIUtil.toast(instance, "手机已经绑定");
            } else {
                startActivity(new Intent(instance, BindPhoneActivity.class));
            }
            //startActivity(new Intent(instance, BindPhoneActivity.class));
        }
    };

    private SinaListener sinaListener = new SinaListener() {
        @Override
        public void authSuccess(Oauth2AccessToken accessToken, String uid, String userName) {
            bindingUserSns.setUserid(Const.user.getUserid());
            bindingUserSns.setDeviceidentifyid(GUIUtil.getDeviceId(UserInfoActivity.this));
            bindingUserSns.setAccesstoken(accessToken.getToken());
            bindingUserSns.setExpireddate(accessToken.getExpiresTime() + "");
            //bindingUserSns.setSnsname(snsName);
            bindingUserSns.setSnstype("0");
            bindingUserSns.setSnsuserid(uid);
           // snsName=userName;
            LogUtil.e(getClass(), "snsName=>"+userName);
            SinaWeiboShare.getInstance().getUserInfo(Long.parseLong(uid), this);
        }

        @Override
        public void failed() {

        }

        @Override
        public void requestSucessSina(String response) {//发送通知给主线程
            LogUtil.e(getClass(), "response=>"+response);
            try {
                JSONObject object = new JSONObject(response) ;
                snsName = object.getString("name");
                bindingUserSns.setSnsname(snsName);
                boolean verified=object.getBoolean("verified") ;
                if(verified){
                    bindingUserSns.setIsverified("1");
                }else{
                    bindingUserSns.setIsverified("0");
                }
                Const.user.getSnsList().clear();
                SNSBean snsb = new SNSBean();
                snsb.setSnsname(snsName);
                Const.user.getSnsList().add(snsb);

                Message msg = new Message();
                msg.arg1 = 1111;
                handler.sendMessage(msg);

                //request(bindingUserSns);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1 == 1111) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        request(bindingUserSns);
                    }
                });
            }
        }
    };



    private UserInfoUpdateFragment.IUserInfoUpdateListener updateL =
            new UserInfoUpdateFragment.IUserInfoUpdateListener() {
                @Override
                public void update(String updatetext) {
                    initUpdateUserParam();
                    if (type == UPDATETYPE.HEADIMG) {
                        //updateUserParam.setProfileimg(updatetext);
                    } else if (type == UPDATETYPE.NICKNAME) {
                        updateUserParam.setNickname(updatetext);
                        request(updateUserParam);
                    } else if (type == UPDATETYPE.POPID) {
                        httpUpdatePop(updatetext);
                    } else if (type == UPDATETYPE.SIGN) {
                        updateUserParam.setSignature(updatetext);
                        request(updateUserParam);
                    }

                }
            };

    private void initUpdateUserParam() {
        updateUserParam.setDevicetype("1");
        updateUserParam.setUserid(Const.user.getUserid());
        updateUserParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance) + "");
    }

    private void httpUpdatePop(String popid) {
        updatePopParam.setDevicetype("1");
        updatePopParam.setUserid(Const.user.getUserid());
        updatePopParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance) + "");
        updatePopParam.setPopgogid(popid);
        request(updatePopParam);
    }

    private void gotoPhoneBindPage(String phone) {
        if(TextUtil.isEmpty(phone)) return;
        phone=phone.substring(0,phone.length() -
                (phone.substring(3)).length()) +"****"+phone.substring(7);
        bindphoneF = new PhoneBindSuccessFragment(phone);
        replaceFragment(bindphoneF, true);
    }

    private void gotoUserInfoPage(User user) {
        userInfoF = new UserInfoFragment(user, userInfoL);
        replaceFragment(userInfoF, false);
    }

    public static UPDATETYPE type;

    private void updatePage(String update, UPDATETYPE type) {
        this.type = type;
        if (type == UPDATETYPE.HEADIMG) {

        } else if (type == UPDATETYPE.POPID) {
            updateF = new UserInfoUpdateFragment(update, updateL);
            replaceFragment(updateF, true);
        } else {
            updateF = new UserInfoUpdateFragment(update, updateL);
            replaceFragment(updateF, true);
        }

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

    private void startPhotoCrop(Uri uri, String duplicatePath, int reqCode) {
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
                //userInfoF.setHeadTmp(photo);
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

    public enum UPDATETYPE {
        NICKNAME,
        HEADIMG,
        POPID,
        SIGN,
    }

    public void gotoSinaPage() {
        sinaF = new BindSinaFragment(onSinaL);
        replaceFragment(sinaF, true);
    }

    private IOnItemClickListener onSinaL = new IOnItemClickListener() {
        @Override
        public void onClick(Object obj) {
           // gotoSinaPage();
            httpCancelSns();
        }
    };

    private void httpCancelSns() {
        cancelSnsParam.setUserid(Const.user.getUserid());
        cancelSnsParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        cancelSnsParam.setDevicetype(Const.DEVICE_TYPE);
        cancelSnsParam.setSnstype("0");
        request(cancelSnsParam);
    }

}
