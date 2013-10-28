package com.showu.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.showu.baogu.R;
import com.showu.common.util.LogUtil;

public class TitleWidget extends RelativeLayout implements OnClickListener {

	private Context mContext;
	
	private View mView;
	private View left;
	private View center;
	private View right;
	
	private Button left_view;
	private TextView center_view;
	private Button right_view;

	public TitleWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		findViews();
		initViews(attrs);
	}
	
	private void findViews() {
		LayoutInflater inflater = (LayoutInflater)getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.title_bar, this);
		
		left = mView.findViewById(R.id.left);
		center = mView.findViewById(R.id.center);
		right = mView.findViewById(R.id.right);
		
		left_view = (Button)mView.findViewById(R.id.left_view);
		center_view = (TextView)mView.findViewById(R.id.center_view);
		right_view = (Button)mView.findViewById(R.id.right_view);
	}
	
    private Drawable leftBg;
	private Integer leftViewBg;
	private String leftViewStr;
	private String leftVisibility;
	
	private Drawable rightBg;
	private Integer rightViewBg;
	private String rightViewStr;
	private String rightVisibility;
	
	private String centerViewStr;
	private String centerVisibility;
	
	private void initViews(AttributeSet attrs) {
        //TypedArray是一个数组容器
		TypedArray array = mContext.obtainStyledAttributes(attrs,
                                                           R.styleable.TitleBar);
		leftBg = array.getDrawable(R.styleable.TitleBar_left_bg);
		leftViewBg = array.getResourceId(R.styleable.TitleBar_left_view_bg,
                                         R.drawable.title_back);
		leftViewStr = array.getString(R.styleable.TitleBar_left_view_text);
		leftVisibility = array.getString(R.styleable.TitleBar_left_visibility);
		setLeftBg(leftBg);
		setLeftViewBg(leftViewBg);
		setLeftViewText(leftViewStr);
		setLeftVisibility(leftVisibility);
		
		rightBg = array.getDrawable(R.styleable.TitleBar_right_bg);
		rightViewBg = array.getResourceId(R.styleable.TitleBar_right_view_bg,
                                          R.drawable.blue_placer);
		rightViewStr = array.getString(R.styleable.TitleBar_right_view_text);
		rightVisibility = array.getString(R.styleable.TitleBar_right_visibility);
		setRightBg(rightBg);
        setRightViewBg(rightViewBg);
		setRightBtnText(rightViewStr);
		setRightVisibility(rightVisibility);
		
		centerViewStr = array.getString(R.styleable.TitleBar_center_view_text);
		centerVisibility = array.getString(R.styleable.TitleBar_center_visibility);
		setCenterViewText(centerViewStr);
		setCenterVisibility(centerVisibility);
		
		left.setOnClickListener(this);
		right.setOnClickListener(this);
		center.setOnClickListener(this);
		
		array.recycle();
	}

	/****************start init left view*****************/
	private void setLeftBg(Drawable bg) {
		if(bg == null) {
			left.setBackgroundDrawable(getResources().
                    getDrawable(R.drawable.pressed_dark_bg));
		} else {
			left.setBackgroundDrawable(bg);
		}
	}

	public void setLeftViewBg(int resid) {
		left_view.setBackgroundResource(resid);
	}
	private void setLeftViewText(String str) {
		left_view.setText(str);
	}
    public View getLeftView() {
        return left_view;
    }
	
	/**
	 * @param visibility </br>
	 * visibility =>"invisible"为不可见</br>
	 * visibility =>"visible"为可见</br>
	 */
	private void setLeftVisibility(String visibility) {
		if(visibility != null) {
			if(visibility.toString().equals("visible")) {
				left.setVisibility(View.VISIBLE);
			} else if(visibility.toString().equals("invisible")) {
				left.setVisibility(View.INVISIBLE);
			}
		} else if(leftViewBg !=null || leftViewStr !=null){
			left.setVisibility(View.VISIBLE);
		} else {
			left.setVisibility(View.VISIBLE);
		}
	}
	/******************end init left view****************/
	
	/** =================start init right view======================== */
	private void setRightBg(Drawable bg) {
		if(bg==null) {
			right.setBackgroundDrawable(getResources().getDrawable(R.drawable.pressed_dark_bg));
		} else {
			right.setBackgroundDrawable(bg);
		}
	}

    public void setRightViewBg(int resid) {
        right_view.setBackgroundResource(resid);
        //right_view.setBackgroundDrawable(getResources().getDrawable(resid));
    }

	public void setRightBtnText(String str) {
		right_view.setText(str);
	}
    public View getRightView() {

        View v = findViewById(R.id.right_view);
        LogUtil.e(getClass(), "getRightView=>"+v);
        return v;
    }
	/**
	 * @param visibility </br>
	 * visibility =>"invisible"为不可见</br>
	 * visibility =>"visible"为可见</br>
	 */
	 public void setRightVisibility(String visibility) {
		if(visibility != null) {
			if(visibility.toString().equals("visible")) {
				right.setVisibility(View.VISIBLE);
			} else if(visibility.toString().equals("invisible")) {
				right.setVisibility(View.INVISIBLE);
			}
		} else if(rightViewBg!=R.drawable.tran_bg || rightViewStr!=null){
			right.setVisibility(View.VISIBLE);
		} else {
			right.setVisibility(View.INVISIBLE);
		}
	}
	
	/** =================end init right view======================== */
	
	
	
	/** =================start init center view======================== */
	public void setCenterViewText(CharSequence  str) {
		center_view.setText(str);
	}

    public void setCenterViewText(Integer stringid) {
        center_view.setText(stringid);
    }

    public View getCenterView() {
        return center_view;
    }

    public View getLeftL() {
        return left;
    }

    public View getRightL() {
        return right;
    }

    /**
     * @param visibility </br>
     * visibility =>"invisible"为不可见</br>
     * visibility =>"visible"为可见</br>
     */
	public void setCenterVisibility(String visibility) {
        if(visibility != null) {
            if(visibility.toString().equals("visible")) {
                center.setVisibility(View.VISIBLE);
            } else if(visibility.toString().equals("invisible")) {
                center.setVisibility(View.INVISIBLE);
            }
        }
	}
	/** =================end init center view======================== */
	

	
	public void setTitleListener(IOnClick l) {
		this.iOnClick=l ;
	}
	
	public interface IOnClick {
		public void leftClick();
		public void rightClick();
		public void centerClick();
	}
	private IOnClick iOnClick;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left:
			iOnClick.leftClick();
			break;

		case R.id.center:
			iOnClick.centerClick();
			break;
			
		case R.id.right:
			iOnClick.rightClick();
			break;
			
		default:
			LogUtil.e(getClass(), "no click");
			break;
		}
	}

}
