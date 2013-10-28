package com.showu.common.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.showu.baogu.activity.film.OnLineSeatActivity;
import com.showu.baogu.activity.filmFan.FanInfoActivity;
import com.showu.baogu.activity.user.UserActivity;
import com.showu.baogu.bean.film.ShowTicketUser;
import com.showu.baogu.listener.IListVListener;
import com.showu.baogu.listener.ILoadImgListener;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;

import java.util.List;

/**
 * 水平弹性滑动的ScrollView
 * @author eilin
 *
 */
public class ElasticHScrollView extends HorizontalScrollView {

    private boolean isRefresh = false;
    private View inner;
    private Float x;
    private Rect normal = new Rect();

    public ElasticHScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView(context);
    }
    
    public ElasticHScrollView(Context context) {
        super(context);
        initView(context);
    }

    public ElasticHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
    }
    
    public boolean onTouchEvent(MotionEvent ev) {
        LogUtil.e(getClass(), "==onTouchEvent==");
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
        }
        //return super.onTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtil.e(getClass(), "==onInterceptTouchEvent==");
        return super.onInterceptTouchEvent(ev);
        //return true;
    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            if(x == null) {
                x = ev.getX();
            }
            LogUtil.e(getClass(), "x down=>"+x);
            break;
        case MotionEvent.ACTION_UP:
            LogUtil.e(getClass(), "x up=>"+x);
            if (isNeedAnimation()) {
                LogUtil.e(getClass(), "x up=>"+x);
                animation();
            }
            break;
        case MotionEvent.ACTION_MOVE:
            LogUtil.e(getClass(), "x move1=>"+x);
            final float preX = x;
            float nowX = ev.getX();
            int deltaX = (int) (preX - nowX);
            // 滚动
            scrollBy(deltaX, 0);
            x = nowX;
            LogUtil.e(getClass(), "x move2=>"+x);
            // 当滚动到最上或最下时就不会再滚动，这是移动布局
            if (isNeedMove()) {
                LogUtil.e(getClass(), "isNeedMove");
                if (normal.isEmpty()) {
                    // 保存正常的布局位置
                    normal.set(inner.getLeft(), inner.getTop(), inner
                            .getRight(), inner.getBottom());

                }
                // 移动布局
                inner.layout(inner.getLeft() - deltaX, inner.getTop(), inner
                        .getRight() - deltaX, inner.getBottom());
            }
            break;

        default:
            break;
        }
    }

    // 开启动画移动

    public void animation() {
        LogUtil.e(getClass(), "x animation=>"+x);
        LogUtil.e(getClass(), "sx animation=>"+getScrollX());
        // 开启移动动画
        TranslateAnimation ta=new TranslateAnimation(inner.getLeft(), normal.left, 0, 0);
        ta.setDuration(50);
        inner.startAnimation(ta);
        //设置回到正常的布局位置
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
        LogUtil.e(getClass(), "sx1 animation=>"+getScrollX());
        //x = 0;
    }

    //是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    // 是否需要移动布局
    public boolean isNeedMove() {

        int offset = inner.getMeasuredWidth() - getWidth();

        int scrollX = getScrollX();
        LogUtil.e(getClass(), "srxx=>"+scrollX+", offset==>"+offset);
//        if (scrollX == 0 || scrollX == offset) {
//            return true;
//        }
        if (scrollX == 0 ) {
            l.refresh();
            return true;
        } else if(scrollX == offset) {
            l.loadMore();
            return true;
        }
        return false;
    }

    private LinearLayout userlayout;
    private Context mContext;
    private void initView(Context context) {
        mContext = context;
        //userlayout = FilmIndexActivity.userHeads;
       if(userlayout==null && inner!=null) {
           userlayout = (LinearLayout) inner;
           //userlayout.setOnTouchListener(this);
       }
       //this.setOnTouchListener(this);
    }

    public void fillData(List<ShowTicketUser> userTickets, View parent, boolean isRefresh) {
        DisplayMetrics metric2 = new DisplayMetrics();

        DisplayMetrics metric1 = new DisplayMetrics();
        parent.measure(metric1.widthPixels, metric1.heightPixels);

        int height = parent.getHeight();
        int size = userTickets.size();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height-20, height-20);
        params.setMargins(4, 0, 4, 0);

        //if(userlayout == null) userlayout = FilmIndexActivity.userHeads;
        if(userlayout == null) {
            userlayout = (LinearLayout) inner;
           // userlayout.setOnTouchListener(this);
        }
        userlayout.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);

        LogUtil.i(getClass(), "MyScroll isRefresh==>" + isRefresh);
        if(isRefresh) {
            userlayout.removeAllViews();
            userlayout.invalidate();
        }
        if(size>0) {
            for(int i=0; i<size; i++) {
                //PressImageView img = new PressImageView(mContext);
                ImageView img = new ImageView(mContext);
                //img.getImageView().setScaleType(ImageView.ScaleType.FIT_XY);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                ShowTicketUser showBean = userTickets.get(i);
                String url = showBean.getProfileimg();
                LogUtil.e(getClass(), "img url==>"+url);

                img.setLayoutParams(params);
                img.setTag(showBean);

                img.setOnClickListener(onClickListener);
                img.setOnTouchListener(onT);
                userlayout.addView(img);
                iLoadImg.load(url, img);
            }
        }
    }

    private OnTouchListener onT = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            x = event.getX();
            LogUtil.e(getClass(), "x onT=>"+x);
            return false;
        }
    };

    private ILoadImgListener iLoadImg;
    public void setILoadImg(ILoadImgListener i) {
        iLoadImg = i;
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(GUIUtil.checkIsOnline(mContext, UserActivity.class)) {
                ShowTicketUser showUser = (ShowTicketUser) v.getTag();
                Intent intent = new Intent(mContext, OnLineSeatActivity.class);
             //   intent.putExtra(OnLineSeatActivity.FAN_ID, showUser.getUserid());
                intent.putExtra(OnLineSeatActivity.EXT_FILM_TYPE, 6);
                intent.putExtra(OnLineSeatActivity.EXT_SEE_FILMID, showUser.getTogetherseefilmid());
                intent.putExtra(OnLineSeatActivity.EXT_UODER_ID, showUser.getUorderid());
                mContext.startActivity(intent);
            }
        }
    };

    OnTochChildren onTochChildren;
    private interface OnTochChildren {
        public Float onChildren(View v, MotionEvent event);
    }

    private IListVListener l;

    public void setListVL(IListVListener l) {
        this.l = l;
    }

}
