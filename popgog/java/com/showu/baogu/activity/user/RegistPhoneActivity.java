package com.showu.baogu.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.showu.baogu.R;
import com.showu.baogu.activity.BaseActivity;
import com.showu.baogu.activity.MainActivity;
import com.showu.baogu.api.RegisterPhoneCreate;
import com.showu.baogu.api.RegisterPhoneVerify;
import com.showu.baogu.api.RegisterUserPhone;
import com.showu.baogu.application.Const;
import com.showu.baogu.bean.User;
import com.showu.baogu.fragment.user.RegistEditInfoFragment;
import com.showu.baogu.fragment.user.RegistPhoneFragment;
import com.showu.baogu.fragment.user.RegistVerifyFragment;
import com.showu.baogu.listener.IRegistEditInfoListener;
import com.showu.baogu.listener.IRegistPhoneListener;
import com.showu.baogu.listener.IRegistVerifyListener;
import com.showu.baogu.xmpp.ServiceManager;
import com.showu.common.util.FileUtil;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;
import com.showu.common.util.TextUtil;
import com.showu.common.util.image.crop.CropActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class RegistPhoneActivity extends BaseActivity {
    private static final int CHOICE_IMAGE = 0x1f;
    private RegistPhoneActivity instance = this;
    private RegistPhoneFragment phoneF;
    private RegistVerifyFragment verifyF;
    private RegistEditInfoFragment editInfoF;

    private RegisterPhoneCreate phoneCreate = new RegisterPhoneCreate();
    private RegisterPhoneVerify phoneVerify = new RegisterPhoneVerify();
    private RegisterUserPhone userPhone = new RegisterUserPhone();

    private String phoneTmp;
    private String uverifyid;
    //上传图片返回json
    //{"status":1,
    // "httpurls":"http://mediatest.popgog.com/media/user/a831162b-1b09-4531-84cb-655fa53d65da.jpg"}
    private String imgUrl;

    private final int LOCALE_IMG_CROP = 11111;
    private final int LOCALE_IMG_CROP_READ = 11112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gotoPhonePage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.e(getClass(), "resultCode==>"+requestCode);
        if(resultCode != RESULT_OK) return;
        switch (requestCode) {
            case LOCALE_IMG_CROP: {
                LogUtil.e(getClass(), selectedImageUri);
                startPhotoCrop(selectedImageUri, null, LOCALE_IMG_CROP_READ); // 图片裁剪
                break;
            }

            case LOCALE_IMG_CROP_READ: {
                if(data != null) {
                    readCropImage(data);
                    editInfoF.setHeadImg(photo);
                }
                break;
            }

            case CHOICE_IMAGE:
                if(data != null) {
                    Uri originalUri = data.getData();
                    startPhotoCrop(originalUri, null, LOCALE_IMG_CROP_READ);
                }
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
        if(url.contains(phoneCreate.getApi())) {
            parsePhoneCreate(conent);
        } else if(url.contains(phoneVerify.getApi())) {
            parsePhoneVerify(conent);
        } else if(url.contains(userPhone.getApi())) {
            User user = parseUserInfo(conent);
            if(user != null) {
                gotoUserHomePage();
                startOpenfire();
            } else {
                GUIUtil.toast(instance, "注册失败,该手机号码已经注册过！");
            }
        } else {
            parseUploadImg(conent);
        }
    }


    private void parseRigistSuccess(String json) {
        try {
            JSONObject jObj = new JSONObject(json);
            if(jObj.getInt("status") == Const.HTTP_OK) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void requestFailed() {
        super.requestFailed();
    }

    private void parsePhoneVerify(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            if(Const.HTTP_OK == jobj.getInt("status")) {
                String uverifyid = jobj.getJSONObject("data").getString("uverifyid");
                if(!TextUtil.isEmpty(uverifyid)) {
                    this.uverifyid = uverifyid;
                    GUIUtil.toast(instance, R.string.regist_auth_confirm_success);
                    gotoEditeInfoPage();
                }
            } else {
                GUIUtil.toast(instance, R.string.regist_auth_confirm_fail);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsePhoneCreate(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            if(Const.HTTP_OK == jobj.getInt("status")) {
                basePref.setPhoneNo(phoneTmp);
                GUIUtil.toast(instance, R.string.regist_phone_getAuthNo_success_tips);
                gotoVerifyPage();
            } else if (jobj.getInt("status") == 10){
                GUIUtil.toast(instance, "该手机号码已经注册过！");
            } else {
                GUIUtil.toast(instance, R.string.regist_phone_getAuthNo_fail);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseUploadImg(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            if(Const.HTTP_OK == jobj.getInt("status")) {
                imgUrl = jobj.getString("httpurls");
                if(!TextUtil.isEmpty(imgUrl))
                    httpRegist(nick, psw, sex, imgUrl);
            } else {
               // GUIUtil.toast(instance, R.string.regist_phone_upload_fail);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private void parseRegistSuccess(String json) {
//        try {
//            JSONObject jobj = new JSONObject(json);
//            if(Const.HTTP_OK == jobj.getInt("status")) {
//                User user = JSONTool.jsonToBean(User.class,
//                        jobj.getJSONObject("data").getJSONObject("users"));
//                cacheOpenfire(user);
//                gotoUserHomePage();
//                startOpenfire();
//            } else {
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private void startOpenfire() {
        if(!TextUtil.isEmpty(Const.user.getUserid())) {
            ServiceManager serviceManager = new ServiceManager(this);
            serviceManager.startService();
        }
    }

    private IRegistPhoneListener phoneL = new IRegistPhoneListener() {

        @Override
        public void getAuthNo(String phone) {
            String phoneText = phone.trim();
            httpGetAuthNo(phoneText);
        }

        private void httpGetAuthNo(String phoneText) {
            LogUtil.e(getClass(), "phone=>"+phoneText);
            phoneTmp = phoneText;
            phoneCreate.setPhone(phoneText);
            request(phoneCreate);
        }
    };

    private IRegistVerifyListener verifyL = new IRegistVerifyListener() {
        @Override
        public void sendConfrim(String phone, String verifycode) {
            httpSendConfirm(phone, verifycode);
        }

        private void httpSendConfirm(String phone, String verifycode) {
            phoneVerify.setPhone(phone);
            phoneVerify.setVerifycode(verifycode);
            request(phoneVerify);
        }

    };

    private String nick, psw, sex;
    private IRegistEditInfoListener editInfoL = new IRegistEditInfoListener() {
        @Override
        public void regist(String nick, String psw, String sex) {
            instance.nick = nick;
            instance.psw = psw;
            instance.sex = sex;

            LogUtil.e(getClass(), "regist:nick=>"+nick+", psw=>"+psw+", sex=>"+sex+"," +
                    " imgUrl=>"+imgUrl);
            if(imgFile == null) {
                GUIUtil.toast(instance, "头像不能为空！");
            } else {
                uploadFile("1", imgFile);
            }

        }

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

    };


    private void httpRegist(String nick, String psw, String sex, String imgUrl) {
        String type_android = "1";
        userPhone.setUverifyid(uverifyid);
        userPhone.setProfileimg(imgUrl);
        userPhone.setNickname(nick);
        userPhone.setPassword(psw);
        userPhone.setSex(sex);
        userPhone.setLongitude(Const.point.getGeoLng()+"");
        userPhone.setLatitude(Const.point.getGeoLat()+"");
        userPhone.setLogincityid(Const.city.getCityid());
        userPhone.setDeviceidentifyid(GUIUtil.getDeviceId(instance)+"");
        userPhone.setDevicetype(type_android);
        request(userPhone);
    }

    private void gotoVerifyPage() {
        verifyF = new RegistVerifyFragment(verifyL);
        replaceFragment(verifyF, true);
    }

    private void gotoPhonePage() {
        phoneF = new RegistPhoneFragment(phoneL);
        replaceFragment(phoneF, false);
    }

    private void gotoEditeInfoPage() {
        editInfoF = new RegistEditInfoFragment(editInfoL);
        replaceFragment(editInfoF, true);
    }

    private void gotoUserHomePage() {
        //startActivity(new Intent(instance, UserActivity.class));
        MainActivity.mHost.getTabWidget().setVisibility(View.VISIBLE);
        finish();
    }

    private Uri selectedImageUri = null;

    public void startCamera() {
        String FILE_NAME =  UUID.randomUUID()+".jpg";
        File photo = FileUtil.getNewFile(this, FILE_NAME) ;
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
    /** 读取裁剪好的图片 */
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

    /** 获取输入流 */
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

//    private void cacheOpenfire(User user) {
//        if(user != null) {
//            Const.user = user;
//            basePref.setOpenfire(new Openfire(user));
//        }
//    }

}
