package com.showu.baogu.fragment.set;

import android.webkit.WebSettings;
import android.webkit.WebView;

import com.showu.baogu.R;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.common.util.LogUtil;

@FragmentView(id=R.layout.activity_fans_weibo)
public class WebFragment extends BaseFragment{

    @InjectView(id=R.id.wv_weibo)
	private WebView mWebview;
	
	private String url, mTitleName;

    public WebFragment() {
    }

    public WebFragment(String url, String mTitleName) {
        this.url = url;
        this.mTitleName = mTitleName;
        LogUtil.e(getClass(), "web url=>"+url);
    }

    private void webViewSetting() {
		WebSettings webSettings = mWebview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setPluginsEnabled(true);
		webSettings.setSupportZoom(true); 
		webSettings.setBuiltInZoomControls(true);
		mWebview.getSettings().setSupportMultipleWindows(true);
		mWebview.setInitialScale(1);
	}

    @Override
    public void bindDataForUIElement() {
        webViewSetting();
        tWidget.setCenterViewText(mTitleName);
        mWebview.loadUrl(url);
    }

    @Override
    protected void bindEvent() {

    }
}
