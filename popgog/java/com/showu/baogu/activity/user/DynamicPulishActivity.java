package com.showu.baogu.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.BaseActivity;

import com.showu.baogu.api.filmFan.AddUserDynamic;
import com.showu.baogu.application.Const;
import com.showu.common.share.SinaListener;
import com.showu.common.share.SinaWeiboShare;
import com.showu.common.util.FontCountLimit;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;
import com.showu.common.util.TextUtil;
import com.showu.common.util.image.crop.CropImageUtil;
import com.showu.common.widget.BottomPopWindow;
import com.showu.common.widget.FaceGridView;
import com.showu.common.widget.TitleWidget;
import com.weibo.sdk.android.Oauth2AccessToken;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DynamicPulishActivity extends BaseActivity {
	private FaceGridView faceGridView;
	private DynamicPulishActivity instance = this;
	private int count = 140;
	private boolean shared = false;
	private String filePath;
    private int width = 70;

    private EditText content;
    private TextView textCount;
    private ImageView add;
    private LinearLayout imglist;
    private TitleWidget titleWidget;
    private TextView shareSina;

    private CropImageUtil cropU;
    private String contentStr;
    private String imglistStr;

    private String[] menu = new String[]{"用相机拍一张", "从相册选一张"};

    private int photoCount = 0;
    private int photoCountMax = 4;
    private List<File> photoFiles = new ArrayList<File>();
    private List<Bitmap> photoBitmaps = new ArrayList<Bitmap>();

    private AddUserDynamic addParam = new AddUserDynamic();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dynamic_publish);

		initView();
		initEvent();
		showInput();
	}



	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		LogUtil.e(getClass(), event.getKeyCode());
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
//			if (faceGridView.getVisibility() == View.VISIBLE) {
//				faceGridView.setVisibility(View.GONE);
//				return true;
//			}
			return super.dispatchKeyEvent(event);
		} else {
			return super.dispatchKeyEvent(event);
		}

	}

	private void initEvent() {
		FontCountLimit fcl = new FontCountLimit(content, textCount);
		fcl.setOnEditTextListener(count);
        titleWidget.getLeftL().setOnClickListener(onClickListener);
        titleWidget.getRightL().setOnClickListener(onClickListener);
        add.setOnClickListener(onClickListener);
        shareSina.setOnClickListener(onClickListener);

	}

	@Override
	protected void requestSuccess(String url, String json) {
        try {
            if(url.contains(addParam.getApi())) {
                parseAdd(json);
            } else {
                parseUploadImg(json);
                if(photoCount <= photoFiles.size() && photoCount>0) {
                    --photoCount;
                    if(photoCount == 0) {
                        imglistStr = sb.substring(0, sb.length()-1);
                        LogUtil.e(getClass(), "imglist=>"+imglistStr);
                        httpAdd();
                        if(shared)
                            shareToWeibo();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (share) {
//            SinaWeiboShare.getInstance().shareSina(
//                    "",
//                    sinaAuthListener);
//        } else {
//            setResult(RESULT_OK);
//            DynamicPulishActivity.this.finish();
//        }
    }

	private void initView() {
        width = basePref.getDisplayWidth()/4-32;
        titleWidget = (TitleWidget) findViewById(R.id.titleBar);
        content = (EditText) findViewById(R.id.activity_dynamic_publish_content);
        textCount = (TextView)findViewById(R.id.activity_dynamic_publish_textcount);
        add = (ImageView)findViewById(R.id.activity_dynamic_publish_add);
        imglist = (LinearLayout)findViewById(R.id.activity_dynamic_publish_imglist);
        shareSina = (TextView) findViewById(R.id.activity_dynamic_publish_share);
        cropU = CropImageUtil.getInstance(instance);
	}

//	@Override
//	public void onResume() {
//		super.onResume();
//		showInput();
//	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.e(getClass(), "===onActivityResult===");
       // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case CropImageUtil.IMG_CROP: {
                cropU.startPhotoCrop(cropU.getCameraImgUrl(), CropImageUtil.IMG_CROP_READ, instance); // 照相机图片裁剪
            }
            break;
            case CropImageUtil.IMG_CROP_READ: {
                cropU.readCropImage(data);              //读取裁剪的图片
                Bitmap photoBitmap = cropU.getPhotoBitmap();
                File photoFile = cropU.getPhotoFile();
                filePath = cropU.getFilePath();
                photoBitmaps.add(photoBitmap);
                photoFiles.add(photoFile);
                fillImglist(photoBitmaps.get(photoCount));
                ++photoCount;
                if(photoCount == photoCountMax) {
                    add.setVisibility(View.GONE);
                } else {
                    add.setVisibility(View.VISIBLE);
                }

                LogUtil.e(getClass(), "bitm=>"+photoBitmap+", file=>"+photoFile);
            }
            break;
            case CropImageUtil.CHOICE_IMAGE:
                if(data != null) {
                    Uri originalUri = cropU.getChoiceLocalImgUrl(data); // 获得图片的uri
                    cropU.startPhotoCrop(originalUri, CropImageUtil.IMG_CROP_READ, instance); // 相册图片裁剪
                }
                break;

            default:
                break;
        }

        //super.onActivityResult(requestCode, resultCode, data);
	}

    private void fillImglist(Bitmap photoBimap) {
        ImageView img = new ImageView(instance);
        img.setLayoutParams(new ViewGroup.LayoutParams(width, width));
        img.setImageBitmap(photoBimap);
        imglist.addView(img);
    }

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left:
//                    if (faceGridView.getVisibility() == View.VISIBLE) {
//                        faceGridView.setVisibility(View.GONE);
//                    }
                    finish();
                    break;
                case R.id.activity_dynamic_publish_add:

                    showButtomPop(onMenuSelect, titleWidget, menu);
                    break;
                case R.id.right:
                    uploadFile();
                    break;
                case R.id.activity_dynamic_publish_share: {
                    if(shared) {
                        shared = false;
                        shareSina.setTextColor(getResources().getColor(R.color.grey));
                    } else {
                        shared = true;
                        shareSina.setTextColor(getResources().getColor(R.color.white));
                    }
                } break;
                default:break;
            }
			/*switch (v.getId()) {
			case R.id.image_camera:
				PhotoUtil.doTakePhonto(DynamicPulishActivity.this);
				break;
			case R.id.image_choice:
				PhotoUtil.doPickPhotoWithPath(DynamicPulishActivity.this);
				break;
			case R.id.image_face:
				hideInput();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				faceGridView.setVisibility(View.VISIBLE);
				break;
			case R.id.cinemaName:
				Intent intent = new Intent(instance, SelectCenimaActivity.class);
				startActivityForResult(intent, SELECT_CINEMA);
				break;
			case R.id.submit:
				submitComment();
				break;
			case R.id.activity_film_commentImage:
				showButtomPop(onMenuSelect, findViewById(R.id.back),
						new String[] { "删除" });
				break;

			case R.id.activity_film_comment_sinaicon:

				if (!share) {
					sinaShare.setImageResource(R.drawable.comment_sina);
					share = true;
					// do share to sina
					// loadData();
				} else if (share) {
					sinaShare.setImageResource(R.drawable.comment_sina_down);
					share = false;
					// cancel share to sina
				}
				break;

			default:
				break;
			}*/
		}
	};

    private void httpAdd() {
        addParam.setDevicetype(Const.DEVICE_TYPE);
        addParam.setUserid(Const.user.getUserid());
        addParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        addParam.setContent(contentStr);
        addParam.setImagelist(imglistStr);
        request(addParam);
    }

//    private void shareToWeibo(String content, String file) {
//        SinaWeiboShare sinaShare = SinaWeiboShare.getInstance();
//        sinaShare.shareSinaWithImge(content, file
//    }

    private void parseAdd(String json) throws Exception {
        JSONObject jobj = new JSONObject(json);
        if(jobj.getInt("status") == Const.HTTP_OK) {
            GUIUtil.toast(instance, "发表成功！");
            if(!shared)
                backToDynamic();
        } else {
            GUIUtil.toast(instance, "发表失败,请重新发表");
        }
    }

    private void backToDynamic() {
        setResult(RESULT_OK, getIntent().
                putExtra(DynamicActivity.PUBLISH_CODE_ID, DynamicActivity.PUBLISH_CODE));
        finish();
    }

    private void uploadFile() {
        contentStr = content.getText().toString();
        if(!TextUtil.isEmpty(contentStr) && !photoFiles.isEmpty()) {
            for(File f : photoFiles) {
                uploadFile("2", f);
            }
        } else {
            GUIUtil.toast(instance, "内容或者图片不能为空");
        }
    }

    private StringBuilder sb = new StringBuilder();

    private void parseUploadImg(String json) throws Exception {
        JSONObject jobj = new JSONObject(json);
        if (Const.HTTP_OK == jobj.getInt("status")) {
            String imgUrl = jobj.getString("httpurls");
            sb.append(imgUrl).append(",");
        } else {
            GUIUtil.toast(instance, R.string.regist_phone_upload_fail);
        }
    }

	private BottomPopWindow.OnMenuSelect onMenuSelect = new BottomPopWindow.OnMenuSelect() {

		@Override
		public void onItemMenuSelect(int position) {
			if (position == 0) {
			    cropU.startCamera(instance);
			} else if(position == 1) {
                cropU.startPicPhoto(instance);
            }
            buttomPop.dismiss();
		}

		@Override
		public void onCancelSelect() {
			buttomPop.dismiss();
		}
	};

	private void showInput() {
		/*
		 * if(faceGridView != null) { faceGridView.setVisibility(View.GONE); }
		 */
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		//imm.showSoftInputFromInputMethod(contentEdit.getWindowToken(), 1);
	}

	private void hideInput() {
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		//imm.hideSoftInputFromWindow(contentEdit.getWindowToken(), 0);
	}

	/* ===============film share to sina weibo=========== */




    private void shareToWeibo() {
    //showDialog();
		if (SinaWeiboShare.getInstance().isSessionValid()) {
			LogUtil.e(getClass(), "shareSina");
            LogUtil.e(getClass(), "contentStr=>"+contentStr+", filePath=>"+filePath);
			SinaWeiboShare.getInstance().shareSinaWithImge(contentStr,
					filePath, sinaAuthListener);
		} else {
			SinaWeiboShare.getInstance().authorize(DynamicPulishActivity.this,
					sinaAuthListener);
		}
		//dimissDialog();
	}

	private SinaListener sinaAuthListener = new SinaListener() {

		@Override
		public void failed() {
            LogUtil.e(getClass(), "failed==");
		}

        @Override
        public void requestSucessSina(String response) {
            LogUtil.e(getClass(), "requestSucessSina==");
            if(shared)
                backToDynamic();
        }

        @Override
		public void authSuccess(Oauth2AccessToken accessToken, String uid,
				String userName) {
            shareToWeibo();
		}
	};

}
