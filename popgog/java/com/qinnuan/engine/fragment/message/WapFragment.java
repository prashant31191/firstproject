package com.qinnuan.engine.fragment.message;

import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.LogUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.fragment.InjectView;

@FragmentView(id = R.layout.activity_pay_wap)
public class WapFragment extends BaseFragment {
    @InjectView(id = R.id.webView)
    private WebView webView;

    private String title ;
    private String url ;


    public WapFragment() {
    }

    public WapFragment(String title,  String url) {
        this.title=title ;
        this.url=url ;
    }

    @Override
    public void leftClick() {
        getActivity().finish();
    }

    private void setting() {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                getActivity().setProgress(progress * 1000);
            }

        });
        webView.setWebViewClient(new WebViewClient() {
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
                paySuccess(url);
                super.onPageFinished(view, url);
            }
        });

    }

    private void paySuccess(String url) {

        if (url.contains("result=success")) {

        }
    }

    @Override
    public void bindDataForUIElement() {

		/*
         * sb.append("&");
		 * sb.append("alipay_wap_url=").append(Const.alipay_wap_url);
		 */
        tWidget.setCenterViewText(title);
        webView.loadUrl(url);
        // webView.loadUrl(url) ;
    }

    @Override
    protected void bindEvent() {

    }
}
