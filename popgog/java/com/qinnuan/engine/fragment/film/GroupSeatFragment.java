package com.qinnuan.engine.fragment.film;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.film.GroupActivity;
import com.qinnuan.engine.bean.film.GroupShowInfoBean;
import com.qinnuan.engine.bean.film.Seat;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.common.util.FileUtil;
import com.qinnuan.common.util.ImageUtil;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.widget.SeatWidget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@FragmentView(id = R.layout.fragment_group_seat)
public class GroupSeatFragment extends BaseFragment {
    private List<Seat> list;
    @InjectView(id = R.id.seatLayout)
    private SeatWidget seatContent;
    private GroupShowInfoBean showInfo;
    @InjectView(id = R.id.time)
    private TextView timeText;
    @InjectView(id = R.id.cinemaName)
    private TextView cinemaTextView;
    @InjectView(id = R.id.hallName)
    private TextView hallNameTextView;
    @InjectView(id = R.id.film_img)
    private ImageView filmImage;
    @InjectView(id = R.id.share)
    private Button shareBtn ;
    @InjectView(id = R.id.touch_text)
    private TextView tochText ;

    private List<Seat> choiceSeat;
    private ImageFetcher mImageFetch;

    private Animation routeAngle;
    public GroupSeatFragment() {
    }

    public GroupSeatFragment(ImageFetcher imageFetcher) {
        this.mImageFetch = imageFetcher;

    }

    @Override
    public void bindDataForUIElement() {
        routeAngle = AnimationUtils.loadAnimation(getActivity(), R.anim.route);
        routeAngle.setFillAfter(true);
    }

    @Override
    protected void bindEvent() {
        shareBtn.setOnClickListener(onClickListener);
//        tochText.setOnClickListener(onClickListener);
    }

    @Override
    public void leftClick() {
        if(showInfo != null) {
            Intent intent = new Intent() ;
            intent.putExtra(GroupActivity.EXT_GROUP_NAME,showInfo.getGroups().getGroupname()) ;
            getActivity().setResult(Activity.RESULT_OK,intent);
        }
        getActivity().finish();
    }

    @Override
    public void rightClick() {
        ((GroupActivity)getActivity()).startGroupSetting() ;
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
                case R.id.share:
                    ((GroupActivity)getActivity()).shareFriend();
                    break;

                default:
                    break;
            }
        }
    };

    public void initSeat(GroupShowInfoBean groupShowInfoBean) {
        this.list = groupShowInfoBean.getSection().get(0).getSeat();
        this.showInfo = groupShowInfoBean;
       initUI();

    }

    private void initUI(){
         tWidget.setCenterViewText(showInfo.getGroups().getGroupname());
        timeText.setText(showInfo.getShowdate() + "  " + showInfo.getShowtime() + "  " + showInfo.getPlanguage() + "/" + showInfo.getPdimenstional());
        cinemaTextView.setText(showInfo.getCinemaname());
        hallNameTextView.setText(showInfo.getHallname());
        filmImage.startAnimation(routeAngle);
        mImageFetch.setLoadingImage(R.drawable.defaul_head);
        mImageFetch.loadImage(ImageUtil.get2xUrl(showInfo.getFrontcover()), filmImage);
        seatContent.setDateWithHead(list, mImageFetch);
//        seatContent.setDate(list);
    }

    public void updateGroupname(String groupName){
        tWidget.setCenterViewText(groupName);
    }


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
        return  image;
    }
}
