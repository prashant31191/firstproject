package com.showu.baogu.fragment.film;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.film.OnLineSeatActivity;
import com.showu.baogu.activity.message.ShowMapActivity;
import com.showu.baogu.bean.CinemaBean;
import com.showu.baogu.bean.film.ShowInfoBean;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.common.util.LogUtil;
import com.showu.common.widget.ShowInfoWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
@FragmentView(id=R.layout.activity_movie_scheduling)
public class MovieSchedulingFragment extends BaseFragment implements OnClickListener{
	private  String fname;
	private CinemaBean cinema;
	private List<ShowInfoBean> allShowList;
    @InjectView(id=R.id.cinemaName)
	private TextView  cinemaNameText;
    @InjectView(id=R.id.cinemaAddress)
   private TextView cinemaAddressText;
    @InjectView(id=R.id.activity_movie_scheduling_showInfo)
	private ShowInfoWidget showInfoWidget ;
	private MovieSchedulingFragment instance = this;
    @InjectView(id=R.id.activity_movie_scheduling_phone)
	private Button mPhone;
    @InjectView(id=R.id.activity_movie_scheduling_progressBar)
	private ProgressBar progressbar;

    public MovieSchedulingFragment() {
    }

    public MovieSchedulingFragment(String fname, CinemaBean cinema) {
        this.fname = fname;
        this.cinema = cinema;
    }

    @Override
    public void bindDataForUIElement() {
       cinemaAddressText.setText(cinema.getAddress());
        tWidget.setCenterViewText(fname);
        cinemaNameText.setText(cinema.getCinemaname());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(allShowList!=null){
        showInfoWidget.notifyDateSetChanged(allShowList);
        }
    }

    public void initShowuInfo(List<ShowInfoBean>  list){
        this.allShowList=list ;
        showInfoWidget.notifyDateSetChanged(list);
    }
    @Override
    protected void bindEvent() {
        showInfoWidget.setListener(onShowInfoClickListener) ;
        setPhoneCallListener(getActivity());
        cinemaAddressText.setOnClickListener(this);
    }


	






	private ShowInfoWidget.OnShowInfoClickListener onShowInfoClickListener= new ShowInfoWidget.OnShowInfoClickListener() {
		@Override
		public void onShowinfoClick(ShowInfoBean showInfo) {
            ((OnLineSeatActivity) getActivity()).onSchedulingItemClick(showInfo);
		}
	};
	


	
	private AlertDialog myDialog;
	private String phoneText;
	/** 电话拨打 */
	private void setPhoneCallListener(final Context context) {
		mPhone.setOnClickListener(new OnClickListener() {
			String phone = null;
			@Override
			public void onClick(View v) {
				if(cinema!=null && cinema.getCinemaphone()!=null) {
					phoneText = cinema.getCinemaphone();
				} else {
					return;
				}
				
				final AlertDialog.Builder builder = new AlertDialog.Builder(context);
				final String[] phoneArray = parsePhone(phoneText);
				LogUtil.e(getClass(), "phoneText==>"+phoneText);
				if(phoneArray != null) {
					LogUtil.e(getClass(), "phoneArray==>"+phoneArray.length);
					builder.setItems(phoneArray, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							phone = phoneArray[which];
							callPhone(phone);
						}
					});
					
					builder.setTitle("选择拨打电话");
					builder.setMessage(phone);
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							return;
						}
					});
					builder.create();
					myDialog = builder.show();
				}
			}
			
			private String[] parsePhone(String text) {
			    StringTokenizer strToken = new StringTokenizer(text, " ");
			    ArrayList<String> list = new ArrayList<String>();
			    String[] strArray = null;
			    String strBuffer = null;
			    while(strToken.hasMoreTokens()) {
				strBuffer = strToken.nextToken();
					list.add(strBuffer);
			    }
			    if(list.size()>0) {
			    	strArray = list.toArray(new String[list.size()]);
			    }
			    return strArray;
			}
			
			/** 拨打联系电话 */
			private void callPhone(String phone) {
				if(phone != null) {
					Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
					instance.startActivity(intent);
				}
			}
		});
	}


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), ShowMapActivity.class) ;
        intent.putExtra(ShowMapActivity.EXT_LONGITUDE,cinema.getLongitude());
        intent.putExtra(ShowMapActivity.EXT_LATITUDE,cinema.getLatitude());
        intent.putExtra(ShowMapActivity.EXT_CINEMANAME,cinema.getCinemaname());
        getActivity().startActivity(intent);
    }
}
