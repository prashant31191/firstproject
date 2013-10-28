package com.showu.baogu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.ImageDetailActivity;
import com.showu.baogu.activity.ImageGridActivity;
import com.showu.baogu.activity.film.BuyTicketTogetherActivity;
import com.showu.baogu.activity.user.UserActivity;
import com.showu.baogu.activity.film.OnLineSeatActivity;
import com.showu.baogu.activity.film.FilmIndexActivity;
import com.showu.baogu.animation.Rotate3dAnimation;
import com.showu.baogu.application.Const;
import com.showu.baogu.bean.film.FilmBean;
import com.showu.common.http.image.util.ImageFetcher;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.ImageFilterUtil;
import com.showu.common.util.ImageUtil;
import com.showu.common.util.LogUtil;
import com.showu.common.util.TextUtil;
import com.showu.common.widget.PressImageView;

import java.util.ArrayList;
import java.util.List;

public class PageViewAdapter extends PagerAdapter {
    private List<FilmBean> filmList;
    private Context mContext;
    private List<View> viewList = new ArrayList<View>();
    private Animation fadeIn, fadeOut;
    private LayoutInflater inflater;
    private ImageFetcher mFetcher;
    private int currentItemIndex = 0;

    public int getCurrentItemIndex() {
        return currentItemIndex;
    }

    public void setCurrentItemIndex(int currentItemIndex) {
        this.currentItemIndex = currentItemIndex;
    }

    public PageViewAdapter(Context context, List<FilmBean> list, ImageFetcher fetcher) {
        mContext = context;
        filmList = list;
        this.mFetcher = fetcher;
        inflater = LayoutInflater.from(mContext);
        initAnimation();
    }

    private void initAnimation() {
        fadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
        fadeOut.setAnimationListener(animationListener);
    }

    private AnimationListener animationListener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animation.setFillAfter(false);
        }
    };

    @Override
    public int getCount() {
        return filmList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LogUtil.e(getClass(),"instantiateItem...");
        if (position >= viewList.size()) {
            initView(position);
        }
        View view = viewList.get(position);
        if (position == 0) {
//            View fourGround = view.findViewById(R.id.foreground_fram);
            View bottomLayout = view.findViewById(R.id.film_gallery_item_bottomlayout);
//            FilmBean filmBean = (FilmBean) fourGround.getTag();
            bottomLayout.setVisibility(View.VISIBLE);
        }
        container.addView(view);
        return viewList.get(position);
    }

    private void initView(int position) {
        FilmBean filmBean = filmList.get(position);
        View convertView = inflater.inflate(R.layout.film_gallery_item, null);
        convertView.setTag(filmBean);

        ImageView frontFrame = (ImageView) convertView.findViewById(R.id.film_front_fullItem_x);
        frontFrame.setTag(position);
        frontFrame.setOnClickListener(onClickListener);
        TextView filmName = (TextView) convertView.findViewById(R.id.film_gallery_filmName);
        filmName.setText(filmBean.getFilmname());
        TextView filmRemark = (TextView) convertView.findViewById(R.id.film_gallery_remark);
        if (filmBean.getShowstatus() != 1) {
            filmRemark.setText("上映日期:"+filmBean.getShowdate());
        } else {
            filmRemark.setText("共" + filmBean.getCinemanum() + "家影院上映");
        }
        mFetcher.setLoadingImage(R.drawable.film_front_defalt);
        mFetcher.loadImage(filmBean.getFrontcover(), frontFrame);

//		if (filmBean.isFilm()) {
//			if(FilmIndexActivity.getFilmType() == FilmType.FILM_PLAYING) {
//				filmRemark.setText(filmBean.getRemark());
//			} else {
//				filmRemark.setText("上映时间:"+filmBean.getShowdate());
//			}
//		} else {
//			View bottomLayout = convertView.findViewById(R.id.film_gallery_item_bottomlayout);
//			bottomLayout.setVisibility(View.GONE);
//			convertView.findViewById(R.id.film_gallery_item_activitytag).setVisibility(View.VISIBLE);
//		}
        LinearLayout detailLayout = (LinearLayout) convertView.findViewById(R.id.film_detail);
        detailLayout.setTag(position);
        detailLayout.setOnClickListener(onClickListener);
        initHeadView(detailLayout, filmBean);
        viewList.add(convertView);
    }

    private void initHeadView(LinearLayout contain, FilmBean detailBean) {
        View headView = inflater.inflate(R.layout.film_detail_head, null);
        contain.removeAllViews();
        contain.addView(headView);
        TextView filmname = (TextView) headView.findViewById(R.id.film_detail_head_filmname);
        ImageView frontImage = (ImageView) headView.findViewById(R.id.frontcoverImage);
        TextView deroctorText = (TextView) headView.findViewById(R.id.director);
        TextView roleText = (TextView) headView.findViewById(R.id.actorlist);
        TextView typeText = (TextView) headView.findViewById(R.id.typelist);
        TextView dateText = (TextView) headView.findViewById(R.id.showdate);
        TextView areaText = (TextView) headView.findViewById(R.id.area);
        TextView lengthText = (TextView) headView.findViewById(R.id.flanguage);
        View playButton = headView.findViewById(R.id.play);
        Button btnOnline = (Button) headView.findViewById(R.id.on_line_seat);
        btnOnline.setTag(detailBean);
        if (detailBean.getIssellticket() == 1) {
            //btnOnline.setVisibility(View.VISIBLE);
            btnOnline.setEnabled(true);
            btnOnline.setOnClickListener(onClickListener);
        } else {
            //btnOnline.setVisibility(View.INVISIBLE);

            btnOnline.setEnabled(false);
        }
        if (detailBean.getShowstatus() != 1) {
            btnOnline.setVisibility(View.INVISIBLE);
        }
        TextView wantSessText = (TextView) headView.findViewById(R.id.want_sess_text);
        wantSessText.setText(mContext.getString(R.string.want_see_text, detailBean.getTotalwantnum()));
        ImageView wantIcon = (ImageView) headView.findViewById(R.id.want_see_icon);
        headView.findViewById(R.id.want_see_layout).setTag(detailBean);
        headView.findViewById(R.id.want_see_layout).setOnClickListener(onClickListener);
        if (!TextUtil.isEmpty(detailBean.getUfilmid())) {
            wantIcon.setImageResource(R.drawable.want_see_icon_on);
        } else {
            wantIcon.setImageResource(R.drawable.want_see_icon);
        }

        //播放按钮
//        if (TextUtil.isEmpty(detailBean.getTrailer())) {
//            playButton.setVisibility(View.GONE);
//        } else {
//            playButton.setVisibility(View.VISIBLE);
//            playButton.setTag(detailBean);
//            playButton.setOnClickListener(onClickListener);
//        }

        playButton.setVisibility(View.VISIBLE);
        playButton.setTag(detailBean);
        playButton.setOnClickListener(onClickListener);


        //剧情简介
        TextView introduction = (TextView) headView.findViewById(R.id.introduction);
        introduction.setMaxLines(4);
        introduction.setText("主演:" + detailBean.getActorlist());

        //填充view的值
//        new LoadLocalImage(this).load2xImag(detailBean.getFrontcover(), frontImage);
        mFetcher.setLoadingImage(R.drawable.film_front_defalt);
        mFetcher.loadImage(ImageUtil.get2xUrl(detailBean.getFrontcover()), frontImage);
        filmname.setText(detailBean.getFilmname());
        deroctorText.setText(detailBean.getDirector());
        roleText.setText(detailBean.getDimenstional());
        typeText.setText(detailBean.getTypelist());
        dateText.setText(detailBean.getShowdate());
        areaText.setText(detailBean.getCountry());
        lengthText.setText(detailBean.getDuration() + "分钟");

        //填充相册列表
//        if (!TextUtil.isEmpty(detailBean.getImagelist())) {
//            fillImgToImgLayout(headView, detailBean);
//        }
    }


    /**
     * 填充相册列表
     */
    private void fillImgToImgLayout(View headView, FilmBean detailBean) {
        LinearLayout imageLayout = (LinearLayout) headView.findViewById(R.id.imagelayout);
        String[] urls = detailBean.getImagelist().split(",");
        LogUtil.e(getClass(), urls.length + "");
        if (urls.length < 1 || urls == null) {
            imageLayout.setVisibility(View.GONE);
            return;
        }
        imageLayout.removeAllViews();
        imageLayout.setGravity(Gravity.CENTER_VERTICAL);
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels / 6 - 10;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
        params.setMargins(3, 0, 0, 0);
        if (urls != null && urls.length > 0) {
            if (urls.length < 4) {
                for (int i = 0; i < urls.length; i++) {
                    imageLayout.addView(loadImg(urls[i], params));
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    imageLayout.addView(loadImg(urls[i], params));
                }
                PressImageView fground = (PressImageView) inflater.inflate(R.layout.film_detail_text, null);
                TextView allImage = (TextView) fground.findViewById(R.id.film_detail_text_text);
                allImage.setText(urls.length + "");
                allImage.setGravity(Gravity.CENTER);
                allImage.setTextColor(Color.BLACK);
                FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(width, width);
                p.setMargins(3, 0, 0, 0);
                allImage.setLayoutParams(p);

                fground.setTag(detailBean.getIconimage());
                LinearLayout.LayoutParams fgroundparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                fgroundparams.setMargins(3, 0, 0, 0);
                fground.setLayoutParams(fgroundparams);
                imageLayout.addView(fground);
                fground.setOnClickListener(onClickListener);
            }
        }
    }

    private ImageView loadImg(String url, LinearLayout.LayoutParams params) {
        ImageView image = (ImageView) inflater.inflate(R.layout.film_detail_image, null);
        image.setLayoutParams(params);
        image.setOnClickListener(onClickListener);
        image.setTag(url);
//        mFetcher.setLoadingImage(R.drawable.film_front_defalt);
        mFetcher.loadImage(url, image);
        image.setId(R.id.film_detail_image);
        return image;
    }

    public View getCurrentView(int position) {
        return viewList.get(position);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.film_front_fullItem_x:
                    LinearLayout detail = (LinearLayout) getCurrentView((Integer) v.getTag()).findViewById(R.id.film_detail);
//                    detail.setVisibility(View.GONE);
                    FilmBean b = filmList.get(Integer.parseInt(v.getTag().toString()));
                    new Goss(detail, b.getFrontcover()).start();
                    applyRotation((Integer) v.getTag(), 0, 90, true);
                    break;
                case R.id.film_detail:
                    applyRotation((Integer) v.getTag(), 0, -90, false);
//                    getCurrentView((Integer)v.getTag()).findViewById(R.id.film_detail).setVisibility(View.GONE);
//                    applyRotation(getCurrentView((Integer) v.getTag()), 0, 360, 2);
                    break;
                case R.id.want_see_layout:
                    if (Const.user == null) {
                        GUIUtil.checkIsOnline(mContext, UserActivity.class);
                    } else {
                        TextView textView = (TextView) v.findViewById(R.id.want_sess_text);
                        FilmIndexActivity activity = (FilmIndexActivity) mContext;
                        FilmBean bean = (FilmBean) v.getTag();
                        if (TextUtil.isEmpty(bean.getUfilmid())) {
                            activity.addWantSess(bean);
                            ImageView image = (ImageView) v.findViewById(R.id.want_see_icon);
                            image.setImageResource(R.drawable.want_see_icon_on);
                            bean.setTotalwantnum(bean.getTotalwantnum() + 1);
                            textView.setText(mContext.getResources().getString(R.string.want_see_text, bean.getTotalwantnum()));
                        } else {
                            activity.deletWantSess(bean);
                            ImageView image = (ImageView) v.findViewById(R.id.want_see_icon);
                            image.setImageResource(R.drawable.want_see_icon);
                            bean.setTotalwantnum(bean.getTotalwantnum() - 1);
                            textView.setText(mContext.getResources().getString(R.string.want_see_text, bean.getTotalwantnum()));
                        }
                    }
                    break;
                case R.id.on_line_seat:
                    if (Const.user != null) {
                        FilmBean bean = (FilmBean) v.getTag();
                        //Intent intent = new Intent(mContext, OnLineSeatActivity.class);
                        Intent intent = new Intent(mContext, BuyTicketTogetherActivity.class);
                        intent.putExtra(OnLineSeatActivity.EXT_FILM_ID, bean.getFilmid());
                        intent.putExtra(OnLineSeatActivity.EXT_FILM_NAME, bean.getFilmname());
                        intent.putExtra(OnLineSeatActivity.EXT_FILM_IMAGE, bean.getFrontcover());
                        mContext.startActivity(intent);
                    } else {
                        GUIUtil.checkIsOnline(mContext, UserActivity.class);
                    }
                    break;
                case R.id.play:
                    FilmBean filmBean = (FilmBean) v.getTag();
                    ((FilmIndexActivity) mContext).showButtomPop(filmBean);
                    break;
                case R.id.film_detail_image:
                    Intent intent = new Intent(mContext, ImageDetailActivity.class);
                    FilmBean bean = filmList.get(getCurrentItemIndex());
                    intent.putExtra(ImageDetailActivity.EXTRA_RUL, v.getTag().toString());
                    intent.putExtra(ImageDetailActivity.EXTRA_RULS, bean.getImagelist());
                    mContext.startActivity(intent);
                    break;
                case R.id.film_detail_text:
                    Intent its = new Intent(mContext, ImageGridActivity.class);
                    FilmBean beans = filmList.get(getCurrentItemIndex());
                    its.putExtra(ImageGridActivity.EXT_URLS, beans.getImagelist());
                    its.putExtra(ImageGridActivity.EXT_TITLE, beans.getFilmname());
                    mContext.startActivity(its);
                    break;
            }
        }
    };
    View mContainer;

    private void applyRotation(int position, float start, float end, boolean isImage) {
        // Find the center of the container
//        mContainer=getCurrentView(position) ;
        if (isImage) {
            mContainer = getCurrentView(position).findViewById(R.id.film_gallery_item_front);
        } else {
            mContainer = getCurrentView(position).findViewById(R.id.film_detail);
        }
//        View image = mContainer.findViewById(R.id.film_front_fullItem_x) ;
        final float centerX = mContainer.getWidth() / 2.0f;
        final float centerY = mContainer.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Rotate3dAnimation rotation =
                new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(150);
//        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(position, isImage));

        mContainer.startAnimation(rotation);
    }

    private final class DisplayNextView implements Animation.AnimationListener {
        private final int mPosition;
        private boolean isImage;

        private DisplayNextView(int position, boolean isImage) {
            mPosition = position;
            this.isImage = isImage;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            mContainer.post(new SwapViews(mPosition, isImage));
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    /**
     * This class is responsible for swapping the views and start the second
     * half of the animation.
     */
    private final class SwapViews implements Runnable {
        private final int mPosition;
        private final boolean isImage;

        public SwapViews(int position, boolean isImage) {
            mPosition = position;
            this.isImage = isImage;
        }

        public void run() {
            final float centerX = mContainer.getWidth() / 2.0f;
            final float centerY = mContainer.getHeight() / 2.0f;
            Rotate3dAnimation rotation;
            View fullFrontView = getCurrentView(mPosition).findViewById(R.id.film_gallery_item_front);
            View detail = getCurrentView(mPosition).findViewById(R.id.film_detail);
            if (isImage) {
                rotation = new Rotate3dAnimation(-90, 0, centerX, centerY, 310.0f, false);
                rotation.setDuration(150);
                rotation.setFillAfter(true);
                rotation.setInterpolator(new DecelerateInterpolator());
                detail.startAnimation(rotation);
                fullFrontView.setVisibility(View.GONE);
                detail.setVisibility(View.VISIBLE);
            } else {
                rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, false);
                rotation.setDuration(150);
                rotation.setFillAfter(true);
                rotation.setInterpolator(new DecelerateInterpolator());
                fullFrontView.startAnimation(rotation);
                fullFrontView.setVisibility(View.VISIBLE);
                detail.setVisibility(View.GONE);
            }


//            mContainer.startAnimation(rotation);
        }


    }

    private class Goss extends Thread {
        View view;
        String url;

        private Goss(View view, String url) {
            this.view = view;
            this.url = url;
        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Drawable d = (Drawable) msg.obj;
                view.setBackgroundDrawable(d);
            }
        };

        @Override
        public void run() {
            Bitmap b = mFetcher.getBitmapFromCach(url);
            if (b != null) {
                Drawable drawable = ImageFilterUtil.boxBlurFilter(b);
                Message msg = new Message();
                msg.obj = drawable;
                handler.sendMessage(msg);
            }
        }
    }
}
