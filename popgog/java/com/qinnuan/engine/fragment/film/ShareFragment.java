package com.qinnuan.engine.fragment.film;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.util.TextUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.activity.film.OnLineSeatActivity;
import com.qinnuan.engine.bean.film.Seat;
import com.qinnuan.engine.bean.film.ShowInfoBean;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.common.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@FragmentView(id = R.layout.fragment_share)
public class ShareFragment extends BaseFragment {
    private List<Seat> list;
    private ShowInfoBean showInfo;
    @InjectView(id = R.id.time)
    private TextView timeText;
    @InjectView(id = R.id.cinemaName)
    private TextView cinemaTextView;
    @InjectView(id = R.id.hallName)
    private TextView hallNameTextView;
    @InjectView(id = R.id.seat)
    private TextView seatText;
    @InjectView(id = R.id.share)
    private Button submitBut;
    @InjectView(id = R.id.film_img)
    private ImageView filmImage;
    @InjectView(id = R.id.seat_img)
    private ImageView seatImage;

    private List<Seat> choiceSeat;
    private String filmName;
    private String cinemaName;
    private String filmFront;
    private ImageFetcher mImageFetch;
    private Animation routeAngle;
    private String bitmapUrl;
    private File shareFile;

    public ShareFragment() {
    }

    public ShareFragment(String filmName, String frontUrl, ShowInfoBean showInfo, String cinemaName, File bitmapFile, ImageFetcher imageFetcher) {
        this.filmName = filmName;
        this.showInfo = showInfo;
        this.filmFront = frontUrl;
        this.mImageFetch = imageFetcher;
        this.cinemaName = cinemaName;
        this.shareFile = bitmapFile;
    }

    public ShareFragment(String filmName, String frontUrl, ShowInfoBean showInfo, String cinemaName, String bitmapUrl, ImageFetcher imageFetcher) {
        this.filmName = filmName;
        this.showInfo = showInfo;
        this.filmFront = frontUrl;
        this.mImageFetch = imageFetcher;
        this.cinemaName = cinemaName;
        this.bitmapUrl = bitmapUrl;
    }

    @Override
    public void bindDataForUIElement() {
        routeAngle = AnimationUtils.loadAnimation(getActivity(), R.anim.route);
        routeAngle.setFillAfter(true);
        tWidget.setCenterViewText("一起看电影");
        if (!TextUtil.isEmpty(showInfo.getPlanguage())) {
            showInfo.setLanguage(showInfo.getPlanguage());
        }
        if (!TextUtil.isEmpty(showInfo.getPdimenstional())) {
            showInfo.setDimenstional(showInfo.getPdimenstional());
        }
        timeText.setText(showInfo.getShowdate() + "  " + showInfo.getShowtime() + "  " + showInfo.getLanguage() + "/" + showInfo.getDimenstional());
        cinemaTextView.setText(cinemaName + "  " + showInfo.getHallname());
        hallNameTextView.setText(filmName);
        mImageFetch.loadImage(filmFront, filmImage);
        filmImage.startAnimation(routeAngle);
        if (TextUtil.isEmpty(bitmapUrl)) {
            Bitmap bitmap = BitmapFactory.decodeFile(shareFile.getAbsolutePath());
            seatImage.setImageBitmap(bitmap);
        } else {
            mImageFetch.loadImage(bitmapUrl, seatImage);
        }
    }

    @Override
    protected void bindEvent() {
        submitBut.setOnClickListener(onClickListener);
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.share:
                    getImage();
                    ((OnLineSeatActivity) getActivity()).shareFriend();
                    break;

                default:
                    break;
            }
        }
    };

    public File getImage() {
        getView().setDrawingCacheEnabled(true);
        Bitmap bitmap = getView().getDrawingCache();
        File image = FileUtil.getNewFile(getActivity(), "1.jpg");
        LogUtil.d(getClass(), image.getAbsolutePath());
        try {
            FileOutputStream os = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getView().setDrawingCacheEnabled(false);
        return image;
    }
}
