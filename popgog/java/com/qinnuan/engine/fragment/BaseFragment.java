package com.qinnuan.engine.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.showu.baogu.R;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.widget.BottomPopWindow;
import com.qinnuan.common.widget.TitleWidget;

import java.lang.reflect.Field;

/**
 * base fragment
 *
 * @author JingHuiLiu
 */

public abstract class BaseFragment extends Fragment  implements TitleWidget.IOnClick{
    protected InputMethodManager imm;
    protected TitleWidget tWidget ;
    private  View conteView ;
    protected PopupWindow buttomPop;
    protected Animation bottomIn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentView fragmentView= this.getClass().getAnnotation(FragmentView.class);
        conteView =inflater.inflate(fragmentView.id(),container,false) ;

        tWidget= (TitleWidget) conteView.findViewById(R.id.titleBar);
        if(tWidget!=null){
            tWidget.setTitleListener(this);
        }
        LogUtil.i(getClass(), "onCreateView fragment======");
        return conteView;
    }

    @Override
    public void onStart() {
        super.onStart();

        injectView();
        bindDataForUIElement();
        bindEvent();
        LogUtil.i(getClass(), "onStart fragment======");
    }

    public void hidSoftInput(EditText editText){
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    @Override
    public void onResume() {
        super.onResume();
        LogUtil.i(getClass(), "onResume fragment======");
       imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        bottomIn = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_bottom_in);
    }


    public String getTagString(){
        return getClass().getName() ;
    }

    public abstract void bindDataForUIElement() ;

    protected abstract void bindEvent();

    private void injectView() {
        Field[] field = JSONTool.getField(this.getClass(), InjectView.class);
        for (Field f : field) {
            f.setAccessible(true);
            InjectView injectView = f.getAnnotation(InjectView.class);
            int rid = injectView.id();
            try {
                f.set(this, conteView.findViewById(rid));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        LogUtil.i(getClass(), "injectView fragment======");
    }

    @Override
    public void leftClick() {
       getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void rightClick() {

    }

    @Override
    public void centerClick() {

    }
    protected void showButtomPop(BottomPopWindow.OnMenuSelect onMenuSelect, View dropAsView,
                                 String[] menus) {
        BottomPopWindow popLayout = new BottomPopWindow(getActivity(), onMenuSelect);
        popLayout.setMenu(menus);
        popLayout.startAnimation(bottomIn);
        buttomPop = new PopupWindow(popLayout,
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.FILL_PARENT);
        buttomPop.setFocusable(true);
        buttomPop.setBackgroundDrawable(new BitmapDrawable());
        buttomPop.setOutsideTouchable(true);
        buttomPop.update();
        buttomPop.showAsDropDown(dropAsView);
    }

    public void back() {
        if(getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();

    }
}
