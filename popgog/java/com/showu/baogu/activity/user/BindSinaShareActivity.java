package com.showu.baogu.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.BaseActivity;
import com.showu.baogu.activity.FilmShareActivity;
import com.showu.baogu.application.Const;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.listener.IOnItemClickListener;
import com.showu.common.share.SinaListener;
import com.showu.common.share.SinaWeiboShare;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;
import com.weibo.sdk.android.Oauth2AccessToken;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
@FragmentView(id = R.layout.fragment_binding_sina_share)
public class BindSinaShareActivity extends BaseActivity {

    private BindSinaShareActivity instance = this;
    private TextView nickname;
    private TextView popid;
    private ImageView head;
    private ImageView attent;
    private Button share;
    private boolean isAttent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_binding_sina_share);
        getTitleWidget().getLeftL().setOnClickListener(onClickL);
        initView();
        initEvent();
    }

    private void initView() {
        nickname = (TextView) findViewById(R.id.fragment_binding_sina_share_nick);
        popid = (TextView) findViewById(R.id.fragment_binding_sina_share_popid);
        head = (ImageView) findViewById(R.id.fragment_binding_sina_share_head);
        attent = (ImageView) findViewById(R.id.fragment_binding_sina_share_attent);
        share = (Button) findViewById(R.id.fragment_binding_sina_share_share);
        setHead();

        nickname.setText(Const.user.getNickname());
        popid.setText(Const.user.getPopgogid());
    }

    private void initEvent() {
        share.setOnClickListener(onClickL);
        attent.setOnClickListener(onClickL);
        head.setOnClickListener(onClickL);
    }

    private View.OnClickListener onClickL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           int id = v.getId();
            switch (id) {
                case R.id.left: {
                    finish();
                }
                break;
                case R.id.fragment_binding_sina_share_attent: {
                    if(isAttent == false) {
                        attent.setImageResource(R.drawable.weibo_attented);
                        attentSina();
                        isAttent = true;
                    } else {
                        attent.setImageResource(R.drawable.weibo_attent);
                        isAttent = false;
                    }
                }
                break;
                case R.id.fragment_binding_sina_share_share: {
                    Intent intent = new Intent(instance, FilmShareActivity.class);
                    intent.putExtra(FilmShareActivity.EXT_SHARE_TYPE, FilmShareActivity.WEIBO);
                    String shareContent = "我是手机应用#爆谷电影#的用户["+ Const.user.getNickname()+"],"
                            + "能发现身边的影迷, 约人看电影, 还能买电影票";

                    intent.putExtra(FilmShareActivity.EXT_SHARE_CONTENT, shareContent);
                    startActivity(intent);
                }
                break;
                default:break;
            }
        }
    };

    private void setHead() {
        mImageFetcher.loadImage(Const.user.getProfileimg(),
                head, GUIUtil.getDpi(this, R.dimen.margin_64));
    }


        SinaListener sinaL = new SinaListener() {
            @Override
            public void authSuccess(Oauth2AccessToken accessToken, String uid, String userName) {
            }

            @Override
            public void failed() {

            }

            @Override
            public void requestSucessSina(String response) {

            }
        };

    private void attentSina() {
        if (SinaWeiboShare.getInstance().isSessionValid()) {
            LogUtil.e(getClass(), "shareSina");
            long popUid = 2929621480L;
            SinaWeiboShare.getInstance().createFriend(popUid, "", sinaL);
        } else {
            SinaWeiboShare.getInstance().authorize(this, sinaL);
        }
    }

    private void shareSina() {
        if (SinaWeiboShare.getInstance().isSessionValid()) {
            LogUtil.e(getClass(), "shareSina");
            long popUid = 2929621480L;
            SinaWeiboShare.getInstance().createFriend(popUid, "", sinaL);
        } else {
            SinaWeiboShare.getInstance().authorize(this, sinaL);
        }
    }

}