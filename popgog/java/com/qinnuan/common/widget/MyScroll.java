package com.qinnuan.common.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qinnuan.engine.activity.filmFan.FanInfoActivity;
import com.qinnuan.engine.bean.film.ShowTicketUser;
import com.qinnuan.engine.listener.ILoadImgListener;
import com.qinnuan.common.util.LogUtil;
import com.showu.baogu.R;

import java.util.List;

public class MyScroll extends HorizontalScrollView {

    private Context mContext;

    public MyScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }

    public MyScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public MyScroll(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private LinearLayout userlayout;
    private int tagLayoutWidth;

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.myscrollview, this);
        userlayout = (LinearLayout)view.findViewById(R.id.scroll_child_layout);
        this.setOnTouchListener(onTouchListener);
    }

    public void fillData(List<ShowTicketUser> userTickets, View parent, boolean isRefresh) {
        DisplayMetrics metric2 = new DisplayMetrics();

        DisplayMetrics metric1 = new DisplayMetrics();
        parent.measure(metric1.widthPixels, metric1.heightPixels);

        int height = parent.getHeight();
        int size = userTickets.size();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height-20, height-20);
        params.setMargins(4, 0, 4, 0);

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
                userlayout.addView(img);
                iLoadImg.load(url, img);
            }
        }
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ShowTicketUser showUser = (ShowTicketUser) v.getTag();
            Intent intent = new Intent(mContext, FanInfoActivity.class);
            intent.putExtra(FanInfoActivity.FAN_ID, showUser.getUserid());
            mContext.startActivity(intent);

//            switch (v.getId()) {
//                case R.id.myscroll_addme : {
//                    Intent intent = new Intent(mContext, BuyTicketTogetherActivity.class);
//                    mContext.startActivity(intent);
//                    break;
//                }

//                default: {
//					ShowFilmTicketBean showFilmTicketBean = (ShowFilmTicketBean)v.getTag();
//					Intent intent = new Intent(mContext, TogetherWatchfilmUserSeat.class);
//					intent.putExtra("showFilmTicketBean", showFilmTicketBean);
//					mContext.startActivity(intent);
//                    break;
//                }
//            }
        }
    };

    private boolean REFRESH = false;
    private boolean LOADMORE = false;
    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        Log.i("MyScroll", "====onScrollChanged===="+"[x->"+x+"] "+" [oldx->"+oldx+"]");
        if(mIScrollStop != null) mIScrollStop.operateHide(x, oldx);
        if(x == 0) {
            if(mIScrollStop != null) mIScrollStop.operateHide(x, oldx);
            if(oldx == 0) {
                REFRESH = true;
            }
        } else {
            if(x==(oldx+1) || x==(oldx-1) || x==oldx) {
                LogUtil.i(getClass(), "x==>" + x + ", width==>" + tagLayoutWidth);
                if (x >= tagLayoutWidth) {
                    if(mIScrollStop != null)  mIScrollStop.operateShow(x, oldx);
                }
                if(x==oldx) {
                    LOADMORE = true;
                }
            }
        }
        super.onScrollChanged(x, y, oldx, oldy);
    }

    private OnTouchListener onTouchListener = new OnTouchListener() {
        private int lastX = 0;
        private int touchEventId = -9983;

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                View scroller = (View)msg.obj;
                if(msg.what==touchEventId) {
                    if(lastX ==scroller.getScrollX()) {
                        handleStop(scroller);
                    }else {
                        handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 5);
                        lastX = scroller.getScrollX();
                    }
                }
            }
        };

        //当ScrollView停止滑动的时候，调用这个方法
        private void handleStop(Object view) {
            LogUtil.i(getClass(), "handleStop==>");
            if(REFRESH) {
                REFRESH = false;
                if(mIhttpGet != null) mIhttpGet.getFilmUser(true);
            } else if(LOADMORE) {
                LOADMORE = false;
                if(mIhttpGet != null)  mIhttpGet.getFilmUser(false);
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                handler.sendMessageDelayed(handler.obtainMessage(touchEventId,v), 5);
            }
            return false;
        }
    };

    public interface IScrollStop {
        public void operateShow(int x, int oldx);
        public void operateHide(int x, int oldx);
    }
    private IScrollStop mIScrollStop;
    public void setIScrollStop(IScrollStop i) {
        mIScrollStop = i;
    }

    public interface IhttpGetFilmUser {
        public void getFilmUser(boolean isRefresh);
    }
    private IhttpGetFilmUser mIhttpGet;
    public void setIhttpGetFilmUser(IhttpGetFilmUser i) {
        mIhttpGet = i;
    }

    private ILoadImgListener iLoadImg;
    public void setILoadImg(ILoadImgListener i) {
        iLoadImg = i;
    }
}

/*public class MyScroll extends HorizontalScrollView {

	private Context mContext;

	public MyScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initView();
	}

	public MyScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	public MyScroll(Context context) {
		super(context);
		mContext = context;
		initView();
	}
	
	private LinearLayout tagLayout;
	private ImageView addmeImg;
	private LinearLayout userlayout;
	private int tagLayoutWidth;

	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.myscrollview, this);
		tagLayout = (LinearLayout)view.findViewById(R.id.tag_layout);
		addmeImg = (ImageView)view.findViewById(R.id.myscroll_addme);
		addmeImg.setOnClickListener(onClickListener);
		userlayout = (LinearLayout)view.findViewById(R.id.scroll_child_layout);
		this.setOnTouchListener(onTouchListener);
	}
	
	public void fillData(List<ShowTicketUser> userTickets, View parent, boolean isRefresh) {
		DisplayMetrics metric2 = new DisplayMetrics();
		tagLayout.measure(metric2.widthPixels, metric2.heightPixels);
		tagLayoutWidth = tagLayout.getWidth();

		DisplayMetrics metric1 = new DisplayMetrics();
		parent.measure(metric1.widthPixels, metric1.heightPixels);

		int height = parent.getHeight();
		int size = userTickets.size();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height-20, height-20);
		params.setMargins(4, 0, 4, 0);

		addmeImg.setLayoutParams(params);
        userlayout.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);

		LogUtil.i(getClass(), "MyScroll isRefresh==>"+isRefresh);
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
                ShowTicketUser showFilmTicketBean = userTickets.get(i);
				String url = showFilmTicketBean.getProfileimg();
				LogUtil.e(getClass(), "img url==>"+url);

				img.setLayoutParams(params);
				img.setTag(showFilmTicketBean);
				img.setOnClickListener(onClickListener);
				userlayout.addView(img);
                iLoadImg.load(url, img);
			}
		}
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.myscroll_addme : {
					Intent intent = new Intent(mContext, BuyTicketTogetherActivity.class);
					mContext.startActivity(intent);
					break;
				}

				default: {
//					ShowFilmTicketBean showFilmTicketBean = (ShowFilmTicketBean)v.getTag();
//					Intent intent = new Intent(mContext, TogetherWatchfilmUserSeat.class);
//					intent.putExtra("showFilmTicketBean", showFilmTicketBean);
//					mContext.startActivity(intent);
					break;
				}
			}
		}
	};
	
	private boolean REFRESH = false;
	private boolean LOADMORE = false;
	@Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		Log.i("MyScroll", "====onScrollChanged===="+"[x->"+x+"] "+" [oldx->"+oldx+"]");
		if(mIScrollStop != null) mIScrollStop.operateHide(x, oldx);
		if(x == 0) {
			tagLayout.setVisibility(View.VISIBLE);
			if(mIScrollStop != null) mIScrollStop.operateHide(x, oldx);
			if(oldx == 0) {
				REFRESH = true;
			}
		} else {
			if(x==(oldx+1) || x==(oldx-1) || x==oldx) {
				LogUtil.i(getClass(), "x==>" + x + ", width==>" + tagLayoutWidth);
				if (x >= tagLayoutWidth) {
					if(mIScrollStop != null)  mIScrollStop.operateShow(x, oldx);
				}
				if(x==oldx) {
					LOADMORE = true;
				}
			}
		}
		super.onScrollChanged(x, y, oldx, oldy);
	}
	
	private OnTouchListener onTouchListener = new OnTouchListener() {
		private int lastX = 0;
		private int touchEventId = -9983;

		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				View scroller = (View)msg.obj;
				if(msg.what==touchEventId) {
					if(lastX ==scroller.getScrollX()) {
						handleStop(scroller);
					}else {
						handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 5);
						lastX = scroller.getScrollX();
					}
				}
			}
		};
		
		//当ScrollView停止滑动的时候，调用这个方法
		private void handleStop(Object view) {
			LogUtil.i(getClass(), "handleStop==>");
			if(REFRESH) {
				REFRESH = false;
//				if(mIhttpGet != null) mIhttpGet.httpRefresh(true);
				if(mIhttpGet != null) mIhttpGet.getFilmUser(true);
			} else if(LOADMORE) {
				LOADMORE = false;
//				if(mIhttpGet != null)  mIhttpGet.httpLoadMore(false);
				if(mIhttpGet != null)  mIhttpGet.getFilmUser(false);
			}
		}
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_UP) {
				handler.sendMessageDelayed(handler.obtainMessage(touchEventId,v), 5);
			}
			return false;
		}
	};
	
	public interface IScrollStop {
		public void operateShow(int x, int oldx);
		public void operateHide(int x, int oldx);
	}
	private IScrollStop mIScrollStop;
	public void setIScrollStop(IScrollStop i) {
		mIScrollStop = i;
	}

	public interface IhttpGetFilmUser {
		public void getFilmUser(boolean isRefresh);
	}
	private IhttpGetFilmUser mIhttpGet;
	public void setIhttpGetFilmUser(IhttpGetFilmUser i) {
		mIhttpGet = i;
	}

    private ILoadImgListener iLoadImg;
    public void setILoadImg(ILoadImgListener i) {
       iLoadImg = i;
    }
}*/
