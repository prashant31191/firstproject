package com.qinnuan.engine.fragment.film;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.activity.film.OnLineSeatActivity;
import com.qinnuan.engine.bean.film.Seat;
import com.qinnuan.engine.bean.film.ShowInfoBean;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.common.util.FileUtil;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.widget.SeatWidget;
import com.qinnuan.engine.R;
import com.qinnuan.common.util.ImageUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@FragmentView(id = R.layout.fragment_see_seat)
public class SeeSeatFragment extends BaseFragment implements SeatWidget.OnSeatSelected {
    private List<Seat> list;
    @InjectView(id = R.id.seatLayout)
    private SeatWidget seatContent;
    private ShowInfoBean showInfo;
    @InjectView(id = R.id.time)
    private TextView timeText;
    @InjectView(id = R.id.cinemaName)
    private TextView cinemaTextView;
    @InjectView(id = R.id.hallName)
    private TextView hallNameTextView;
    @InjectView(id = R.id.seat)
    private TextView seatText;
    @InjectView(id = R.id.submit)
    private Button submitBut;
    @InjectView(id = R.id.film_img)
    private ImageView filmImage;
    @InjectView(id = R.id.expired)
    private TextView expiredText;

    private List<Seat> choiceSeat;
    private ImageFetcher mImageFetch;

    private Animation routeAngle;

    public SeeSeatFragment() {
    }

    public SeeSeatFragment(ImageFetcher imageFetcher) {
        this.mImageFetch = imageFetcher;

    }

    @Override
    public void bindDataForUIElement() {
        routeAngle = AnimationUtils.loadAnimation(getActivity(), R.anim.route);
        routeAngle.setFillAfter(true);
    }

    @Override
    protected void bindEvent() {
        seatContent.setOnSeatSelected(this);
        submitBut.setOnClickListener(onClickListener);
        expiredText.setOnClickListener(onClickListener);
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (list != null) {
            seatContent.setDate(list);
            initUI();
        }
    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.submit:
                    if (seatContent.checkSeat()) {
                        getImage();
                        ((OnLineSeatActivity) getActivity()).suerSeat(choiceSeat);
                    } else {
                        GUIUtil.toast(getActivity(), R.string.seat_error);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    public void initSeat(ShowInfoBean showInfo, List<Seat> list) {
        this.list = list;
        this.showInfo = showInfo;
        if(showInfo.getIsexpired()==1){
            expiredText.setVisibility(View.VISIBLE);
        }else{
            expiredText.setVisibility(View.GONE);
        }
       initUI();


    }

    private void initUI(){
         tWidget.setCenterViewText(showInfo.getFilmname());
        timeText.setText(showInfo.getShowdate() + "  " + showInfo.getShowtime() + "  " + showInfo.getPlanguage() + "/" + showInfo.getPdimenstional());
        cinemaTextView.setText(showInfo.getCinemaname());
        hallNameTextView.setText(showInfo.getHallname());
        filmImage.startAnimation(routeAngle);
        mImageFetch.setLoadingImage(R.drawable.defaul_head);
        mImageFetch.loadImage(ImageUtil.get2xUrl(showInfo.getFrontcover()), filmImage);
        seatContent.setDateWithHead(list, mImageFetch);
//        seatContent.setDate(list);
    }
    @Override
    public void onSeatSelected(View view, Seat seat, boolean hasChoice) {
        this.choiceSeat = seatContent.getChoiceList();
        StringBuffer text = new StringBuffer("");
        for (Seat s : choiceSeat) {
            text.append(s.getRowid() + "排" + s.getColumnid() + "座    ");
        }
        seatText.setText(text.toString());
    }




    public File getImage() {
        seatContent.setDrawingCacheEnabled(true);
        Bitmap bitmap = seatContent.getDrawingCache();
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
        seatContent.setDrawingCacheEnabled(false);
        return  image;
    }
}
