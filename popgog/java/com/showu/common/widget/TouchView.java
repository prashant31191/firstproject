package com.showu.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.showu.baogu.R;
import com.showu.baogu.activity.filmFan.FanInfoActivity;
import com.showu.baogu.activity.filmFan.MyFriendActivity;
import com.showu.baogu.bean.film.Seat;
import com.showu.common.http.image.util.ImageFetcher;
import com.showu.common.util.LogUtil;
import com.showu.common.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class TouchView extends FrameLayout {

    static final int BIGGER = 3; // 放大ing
    static final int SMALLER = 4; // 缩小ing

    private float scale = 0.04f;

    private int chWeight;
    private int chHeight;
    private int seatMinSize;
    private int seatMaxSize;
    /* 处理拖动 变量 */
    private Context mContext;
    private List<Seat> seatList;
    private List<Seat> choiceList = new ArrayList<Seat>();
    private List<Seat> rowIndex = new ArrayList<Seat>();
    private SeatViewOperationListener operationListener;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private boolean isScal = false;
    private Animation routAnimation;
    private ImageFetcher fetcher;
    private int row;
    private int col;
    private int seatRed;
    private int seatBlue;
    private int available;
    private int seatLoveRed;
    private int seatLoveBlue;
    private int seatLoveGreen;

    public List<Seat> getChoiceList() {
        return choiceList;
    }

    public void clearChoiceList() {
        if (choiceList != null) {
            choiceList.clear();
        }
    }

    public void setOperationListener(SeatViewOperationListener operationListener) {
        this.operationListener = operationListener;
    }

    public interface SeatViewOperationListener {
        public void seatOnClick(View view, Seat seat, boolean hasChoice);

        public void initNumber(List<Seat> list, int height);

        public void scroll(int y);
    }

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initResouce();
        routAnimation = AnimationUtils.loadAnimation(mContext, R.anim.seat_route);
        routAnimation.setFillAfter(true);
        seatMinSize = context.getResources().getDimensionPixelSize(
                R.dimen.seat_min_size);
        seatMaxSize = context.getResources().getDimensionPixelSize(
                R.dimen.seat_max_size);
        scaleGestureDetector = new ScaleGestureDetector(context,
                onScaleGestureListener);
        gestureDetector = new GestureDetector(onGestureListener);
    }

    private void initResouce() {
        if (Build.VERSION.SDK_INT > 10) {
            seatRed = R.drawable.rote_avaliable;
            seatBlue = R.drawable.rote_seat_red;
            available = R.drawable.rote_seat_green;
            seatLoveRed = R.drawable.rote_love_avaliabe;
            seatLoveBlue = R.drawable.rote_love_red;
            seatLoveGreen = R.drawable.rote_love_green;
        } else {
            seatRed = R.drawable.seat_red;
            seatBlue = R.drawable.seat_blue;
            available = R.drawable.film_available;
            seatLoveRed = R.drawable.seat_love_red;
            seatLoveBlue = R.drawable.seat_love_blue;
            seatLoveGreen = R.drawable.seat_love_green;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // return doTouch(ev);
        return scaleGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // return doTouch(event);
        boolean flag = scaleGestureDetector.onTouchEvent(event);
        LogUtil.e(getClass(), flag);
        if (!isScal) {
            flag = gestureDetector.onTouchEvent(event);
        } else {
            isScal = false;
        }
        return flag;
    }

    /**
     * 实现处理缩放
     */

    private void setScale(View view, float temp, int flag) {
        if (flag == BIGGER) {
            initView(seatList, (int) (chWeight + chWeight * temp),
                    (int) (chHeight + chHeight * temp));

        } else if (flag == SMALLER) {
            initView(seatList, (int) (chWeight - chWeight * temp),
                    (int) (chHeight - chHeight * temp));
        }

    }

    private boolean isNewRow(Seat seat) {
        for (Seat s : rowIndex) {
            if (seat.getRowid().equals(s.getRowid())) {
                return false;
            }
        }
        return true;
    }

    public void initView(List<Seat> list, int width, int height) {
        this.seatList = list;
        rowIndex.clear();
        // if (width >= seatMinSize && height >= seatMinSize) {
        removeAllViews();
        int id = 0;
        this.seatList = list;
        for (int i = 0; i < list.size(); i++) {
            Seat seat = list.get(i);
            if (seat.getRownum() > row) {
                row = seat.getRownum();
            }
            if (seat.getColumnnum() > col) {
                col = seat.getColumnnum();
            }
            if (isNewRow(seat)) {
                rowIndex.add(seat);
            }
            ImageView image = new ImageView(mContext);
            image.setId(id);
            image.setTag(seat);
            id++;
            LayoutParams params;

            if (seat.getLoveind().equals("0")) {// 非情侣座
                if (seat.getIssell().equals("0")
                        || seat.getDamagedflg().equals("Y")) {
                    if (seat.isHi()) {// 用户选择的
//                        image.setImageResource(R.drawable.man_selected);
                    } else {
                        image.setImageResource(seatRed);
                    }

                } else if (choiceList.contains(seat)) {
                    image.setImageResource(seatBlue);
                } else {
                    image.setImageResource(available);
                }
                if (seat.isHi()) {// 用户选择的
//                    image.setImageResource(R.drawable.man_selected);
                }
                MarginLayoutParams mParams = new MarginLayoutParams(width,
                        height);
                if (Build.VERSION.SDK_INT > 10) {
                    mParams.setMargins(seat.getColumnnum() * width + seat.getRownum() * 20,
                            seat.getRownum() * height+20, 0, 0);
                } else {
                    mParams.setMargins(seat.getColumnnum() * width,
                            seat.getRownum() * height, 0, 0);
                }
                params = new LayoutParams(mParams);
                params.gravity = Gravity.TOP;

            } else {// 情侣座
                if (seat.getIssell().equals("0")
                        || seat.getDamagedflg().equals("Y")) {
                    image.setImageResource(seatLoveRed);
                } else if (choiceList.contains(seat)) {
                    image.setImageResource(seatLoveBlue);
                } else {
                    image.setImageResource(seatLoveGreen);
                }



                MarginLayoutParams mParams = new MarginLayoutParams(width * 2, height);
                if (Build.VERSION.SDK_INT > 10) {
                    mParams.setMargins(seat.getColumnnum() * width + seat.getRownum() * 20,
                            seat.getRownum() * height+20, 0, 0);
                } else {
                    mParams.setMargins(seat.getColumnnum() * width, seat.getRownum() * height, 0, 0);
                }



               // MarginLayoutParams mParams = new MarginLayoutParams(width * 2, height);
               // mParams.setMargins(seat.getColumnnum() * width, seat.getRownum() * height, 0, 0);
                params = new LayoutParams(mParams);
                params.gravity = Gravity.TOP;

                i++;
            }
            image.setLayoutParams(params);

            addView(image);
        }
        operationListener.initNumber(rowIndex, height);
        chWeight = width;
        chHeight = height;
        if (Build.VERSION.SDK_INT > 10) {
            this.setRotation(159);
        }
    }

    public void drawHead(ImageFetcher fetcher) {
        this.fetcher = fetcher;
        for (int i = 0; i < seatList.size(); i++) {
            Seat seat = seatList.get(i);
            if (!TextUtil.isEmpty(seat.getProfileimg())) {
                CircleImageView imageLayout = new CircleImageView(getContext());
                imageLayout.setPadding(8, 8, 8, 8);
                imageLayout.setTag(seat);
                if (seat.getSex() == 2) {
                    imageLayout.setBackgroundResource(R.drawable.head_wemen_icon);
                } else {
                    imageLayout.setBackgroundResource(R.drawable.head_man_icon);
                }
                fetcher.loadImage(seat.getProfileimg(), imageLayout);
                imageLayout.setTag(seat);
                LayoutParams params;
                MarginLayoutParams mParams = new MarginLayoutParams(78,
                        92);
                if (Build.VERSION.SDK_INT > 10) {
//                    mParams.setMargins(seat.getColumnnum() * chWeight - 39 + chWeight / 2+seat.getRownum()*20,
//                            seat.getRownum() * chHeight - 92 + chHeight / 2, 0, 0);
                    mParams.setMargins(seat.getColumnnum() * chWeight  - chWeight / 2+seat.getRownum()*20,
                            seat.getRownum() * chHeight  + chHeight / 2+20, 0, 0);
                } else {
                    mParams.setMargins(seat.getColumnnum() * chWeight - 39 + chWeight / 2,
                            seat.getRownum() * chHeight - 92 + chHeight / 2, 0, 0);
                }
                params = new LayoutParams(mParams);
                params.gravity = Gravity.TOP;
                imageLayout.setLayoutParams(params);
                addView(imageLayout);
                if(Build.VERSION.SDK_INT>10){
                    imageLayout.setRotation(-159);
                }
            }
        }
    }

    private void seatClick(View v) {
        Seat seat = (Seat) v.getTag();
        if (choiceList.contains(seat)) {
            // 包含情侣座的情况
            if (!seat.getLoveind().equals("0")) {
                Seat s = choiceList.get(choiceList.indexOf(seat) + 1);
                s.setMySelect(false);
                choiceList.remove(choiceList.indexOf(seat) + 1);
            }

            seat.setMySelect(false);
            choiceList.remove(seat);
            // 为情侣座的情况
            if (!seat.getLoveind().equals("0")) {
                ((ImageView) v).setImageResource(seatLoveGreen);
            } else {
                // 非情侣座的情况
                ((ImageView) v).setImageResource(available);
            }
            operationListener.seatOnClick(v, seat, false);
        } else {
            if (choiceList.size() < 6) {
                if (canChoice(seat)) {
                    // 选情侣座的时候 "0"是一般座位，非情侣座
                    if (!seat.getLoveind().equals("0")) {
                        ((ImageView) v)
                                .setImageResource(seatLoveBlue);
                        seat.setMySelect(true);
                        choiceList.add(seat);
                        /** ===================情侣座的另一个座位=================== */
                        int seatAnotherIndex = seatList.indexOf(seat) + 1;
                        if (seatAnotherIndex <= seatList.size() - 1) {
                            Seat seatAnother = seatList.get(seatAnotherIndex);
                            choiceList.add(seatAnother);
                            seatAnother.setMySelect(true);
                        }

                        operationListener.seatOnClick(v, seat, true);
                    } else {
                        // 选一般座的时候
                        ((ImageView) v).setImageResource(seatBlue);
                        choiceList.add(seat);
                        seat.setMySelect(true);
                        operationListener.seatOnClick(v, seat, true);
                    }
                } else {

                }

            } else {
                Toast.makeText(mContext, "最多只能选择6个座位", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private boolean canChoice(Seat seat) {
        if (seat.getDamagedflg().equals("Y") || seat.getIssell().equals("0")
                || seat.isHi()) {
            return false;
        }
        if (choiceList.size() == 5 && !seat.getLoveind().equals("0")) {
            return false;
        }
        return true;
    }


    private boolean adjX(int x) {
        int maxScrollx = getChildAt(0).getWidth() * col - getWidth() / 2;
        if (x >= 0 && getScrollX() < maxScrollx) {
            return true;
        } else if (x < 0 && getScrollX() > 0) {
            return true;
        }
        return false;
    }

    private boolean adjY(int y) {
        int maxScrollY = getChildAt(0).getHeight() * row;
        if (y > 0 && getScrollY() < maxScrollY) {
            return true;
        } else if (y <= 0 && getScrollY() >= 0) {
            return true;
        }
        return false;
    }

    private void scrollLeft(int x, int y) {
        int maxScrollY = getChildAt(0).getHeight() * row - getHeight() / 2;
        if (x < 0) {
            if (getScrollX() >= 0 && getScrollY() <= maxScrollY) {
                super.scrollBy(x, y);
            } else if (getScrollX() < 0 && getScrollY() < maxScrollY) {
            }
        }
    }

    @Override
    public void scrollBy(int x, int y) {
        if (getChildCount() > 0) {
            if (adjX(x) && adjY(y)) {
                super.scrollBy(x, y);
                operationListener.scroll(y);
            } else if (adjX(x)) {
                super.scrollBy(x, 0);
            } else if (adjY(y)) {
                super.scrollBy(0, y);
                operationListener.scroll(y);
            }
        } else {
            LogUtil.i(getClass(), "Wait the seat loading");
        }
    }

    private SimpleOnScaleGestureListener onScaleGestureListener = new SimpleOnScaleGestureListener() {

        @TargetApi(8)
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            LogUtil.e(SimpleOnScaleGestureListener.class, "onScaleEnd");
            drawHead(fetcher);
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            LogUtil.e(SimpleOnScaleGestureListener.class, "onScale");

            if (detector.getCurrentSpan() > detector.getPreviousSpan()) {
                if (chWeight < seatMaxSize) {
                    setScale(TouchView.this, scale, BIGGER);
                }
            } else if (detector.getCurrentSpan() < detector.getPreviousSpan()) {
                if (chWeight > seatMinSize) {
                    setScale(TouchView.this, scale, SMALLER);
                }
            }
            return true;
        }
    };

    private OnGestureListener onGestureListener = new OnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                if (view instanceof CircleImageView) {
                    view.setVisibility(VISIBLE);
                }
            }
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                int left = view.getLeft() - getScrollX();
                int right = view.getRight() - getScrollX();
                int top = view.getTop() - getScrollY();
                int bottom = view.getBottom() - getScrollY();
                if (e.getX() > left && e.getX() < right && e.getY() < bottom
                        && e.getY() > top) {
                    if (view instanceof CircleImageView) {
                        Seat seat = (Seat) view.getTag();
                        Intent intent = new Intent(mContext, FanInfoActivity.class);
                        intent.putExtra(FanInfoActivity.FAN_ID, seat.getUserid());
                        mContext.startActivity(intent);
                        break;
                    } else {
                        seatClick(view);
                    }
                }
            }
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (e1.getAction() == MotionEvent.ACTION_DOWN) {
                for (int i = 0; i < getChildCount(); i++) {
                    View view = getChildAt(i);
                    if (view instanceof CircleImageView) {
                        view.setVisibility(GONE);
                    }
                }
            }

            scrollBy((int) distanceX, (int) distanceY);
            isScal = true;
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            return true;
        }
    };
}
