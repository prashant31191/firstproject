package com.showu.baogu.fragment.film;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.film.OnLineSeatActivity;
import com.showu.baogu.bean.CinemaBean;
import com.showu.baogu.bean.film.Seat;
import com.showu.baogu.bean.film.ShowInfoBean;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.common.util.DateUtil;
import com.showu.common.util.FileUtil;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;
import com.showu.common.widget.SeatWidget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@FragmentView(id = R.layout.activity_seat)
public class SeatFragment extends BaseFragment implements SeatWidget.OnSeatSelected {
    private List<Seat> list;
    @InjectView(id = R.id.seatLayout)
    private SeatWidget seatContent;
    private ShowInfoBean showInfo;
    @InjectView(id = R.id.time)
    private TextView timeText;
    @InjectView(id = R.id.day)
    private TextView dayText;
    @InjectView(id = R.id.date)
    private TextView dateText;
    @InjectView(id = R.id.mounth)
    private TextView mounthText;
    @InjectView(id = R.id.price)
    private TextView priceText;
    @InjectView(id = R.id.seat)
    private TextView seatText;
    @InjectView(id=R.id.submit)
    private Button submitBut;

    private List<Seat> choiceSeat;
    private String filmName;
//    private CinemaBean cinema;
    private ProgressBar progress;

    public SeatFragment() {
    }

    public SeatFragment(String filmName, ShowInfoBean showInfo) {
        this.filmName = filmName;
        this.showInfo = showInfo;
//        this.cinema = cinema;
    }

    @Override
    public void bindDataForUIElement() {
        tWidget.setCenterViewText(filmName);
        timeText.setText(showInfo.getShowtime() + "   " + showInfo.getLanguage() + "/" + showInfo.getDimenstional());
        String[] d = showInfo.getShowdate().split("-");
        dayText.setText(d[2]);
        mounthText.setText(d[1] + "月");
        priceText.setText("￥" + showInfo.getPrice());
        dateText.setText(DateUtil.getDay(showInfo.getShowdate()));

    }

    @Override
    protected void bindEvent() {
        seatContent.setOnSeatSelected(this);
        submitBut.setOnClickListener(onClickListener);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (list != null) {
            seatContent.setDate(list);
        }
    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.submit:
                    if(seatContent.checkSeat()){
                        getImage();
                    ((OnLineSeatActivity)getActivity()).suerSeat(choiceSeat);
                    }else{
                        GUIUtil.toast(getActivity(),R.string.seat_error);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    public void initSeat(List<Seat> list) {
        this.list = list;
        seatContent.setDate(list);
    }


    @Override
    public void onSeatSelected(View view, Seat seat, boolean hasChoice) {
        this.choiceSeat=seatContent.getChoiceList();
        StringBuffer text = new StringBuffer("");
        for (Seat s : choiceSeat) {
            text.append(s.getRowid() + "排" + s.getColumnid() + "座    ");
        }
        seatText.setText(text.toString());
    }


    public  File getImage(){
        seatContent.setDrawingCacheEnabled(true);
        Bitmap bitmap = seatContent.getDrawingCache();
        File image = FileUtil.getNewFile(getActivity(), "1.jpg") ;
        LogUtil.d(getClass(), image.getAbsolutePath());
        try {
            FileOutputStream os = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90,os ) ;
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        seatContent.setDrawingCacheEnabled(false);
        return image;
    }
}
