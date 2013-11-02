package com.qinnuan.engine.activity.filmFan;

import android.content.Intent;
import android.os.Bundle;

import com.qinnuan.engine.fragment.filmFan.FriendAddHomeFragment;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.activity.FilmShareActivity;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.listener.IAddFriendHomeListener;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class FriendAddHomeActivity extends BaseActivity {

    private FriendAddHomeActivity instance = this;
    private FriendAddHomeFragment friendF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gotoFriendAddHome();
    }

    @Override
    protected void requestFailed() {
        super.requestFailed();
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
    }

    private void gotoFriendAddHome() {
        friendF = new FriendAddHomeFragment(friendAddL);
        replaceFragment(friendF, false);
    }

    private IAddFriendHomeListener friendAddL = new IAddFriendHomeListener() {

        @Override
        public void search() {
            startActivity(new Intent(instance, FriendAddActivity.class));
        }

        @Override
        public void contact() {
            startActivity(new Intent(instance, ContactActivity.class));
        }

        @Override
        public void shareToFriend() {
            Intent intent = new Intent(instance, FilmShareActivity.class);
            intent.putExtra(FilmShareActivity.EXT_SHARE_TYPE, FilmShareActivity.WECHAT);
            String shareContent = "我是手机应用#爆谷电影#的用户["+ Const.user.getNickname()+"],"
                    + "能发现身边的影迷, 约TA看电影, 还能买电影票,详情";

            intent.putExtra(FilmShareActivity.EXT_SHARE_CONTENT, shareContent);
            startActivity(intent);
        }

        @Override
        public void shareToSina() {
            Intent intent = new Intent(instance, FilmShareActivity.class);
            intent.putExtra(FilmShareActivity.EXT_SHARE_TYPE, FilmShareActivity.WEIBO);
            String shareContent = "我是手机应用#爆谷电影#的用户["+ Const.user.getNickname()+"],"
                    + "能发现身边的影迷, 约人看电影, 还能买电影票";

            intent.putExtra(FilmShareActivity.EXT_SHARE_CONTENT, shareContent);
            startActivity(intent);
        }
    };

}