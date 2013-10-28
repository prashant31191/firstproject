package com.showu.baogu.fragment.film;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.PopupWindow;

import com.showu.baogu.R;
import com.showu.baogu.activity.film.ActivityActivity;
import com.showu.baogu.bean.film.AcitivityBean;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;
import com.showu.common.widget.JoinPopContentView;

@FragmentView(id = R.layout.fragment_activity_detail)
public class ActivityDetailFragment extends BaseFragment implements View.OnClickListener,JoinPopContentView.CancellListener {

    @InjectView(id = R.id.wv_weibo)
    private WebView mWebview;
    @InjectView(id = R.id.buy_button)
    private Button button;

    private PopupWindow pop;
    private String url, mTitleName;

    public ActivityDetailFragment() {
    }

    public ActivityDetailFragment(String url, String mTitleName) {
        this.url = url;
        this.mTitleName = mTitleName;
    }

    public void canBuyTickit(){
       button.setEnabled(true);
    }

    private void webViewSetting() {
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginsEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        mWebview.getSettings().setSupportMultipleWindows(true);
        mWebview.setInitialScale(1);
        setting();
    }
    private void setting() {

        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                getActivity().setProgress(progress * 1000);
            }

        });
        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {

                GUIUtil.toast(getActivity(), "Oh no! " + description);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.v(WebViewClient.class, url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
            }
        });

    }
    @Override
    public void bindDataForUIElement() {
        webViewSetting();
        tWidget.setCenterViewText(mTitleName);
        mWebview.loadUrl(url);
    }

    @Override
    protected void bindEvent() {
       button.setOnClickListener(this);
    }
    public void showJoinPop(AcitivityBean acitivityBean) {
        JoinPopContentView view = new JoinPopContentView(getActivity());
        view.setListener(this);
        view.fillUITicket(acitivityBean.getTicket());
        int width = WindowManager.LayoutParams.FILL_PARENT;
        int hight = WindowManager.LayoutParams.WRAP_CONTENT;
        pop = new PopupWindow(view, width, hight);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        pop.update();
        pop.showAsDropDown(tWidget);
    }

    @Override
    public void onClick(View view) {
        ((ActivityActivity)getActivity()).showPop();
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    public void dismissPop(){
        pop.dismiss();
    }

    @Override
    public void cancel() {
        pop.dismiss();
    }
}
