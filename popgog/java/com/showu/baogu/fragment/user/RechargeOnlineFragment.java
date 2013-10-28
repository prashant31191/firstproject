package com.showu.baogu.fragment.user;

import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.showu.baogu.R;
import com.showu.baogu.application.Const;
import com.showu.baogu.fragment.BaseFragment;
import com.showu.baogu.fragment.FragmentView;
import com.showu.baogu.fragment.InjectView;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;

import java.io.UnsupportedEncodingException;

@FragmentView(id = R.layout.activity_pay_wap)
public class RechargeOnlineFragment extends BaseFragment {
    @InjectView(id = R.id.webView)
    private WebView webView;

    private String subject;
    private String totalPrice;
    private String outTradeNo;

    public RechargeOnlineFragment() {
    }

    public RechargeOnlineFragment(String subject, String totalPrice, String outTradeNo) {
        this.subject = subject;
        this.totalPrice = totalPrice;
        this.outTradeNo = outTradeNo;
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
        StringBuffer sb = new StringBuffer();
        sb.append("out_trade_no=").append(outTradeNo).append("&");
        sb.append("subject=").append(subject).append("&");
        sb.append("total_fee=").append(totalPrice);

		/*
         * sb.append("&");
		 * sb.append("alipay_wap_url=").append(Const.alipay_wap_url);
		 */

        byte[] postData = null;
        try {
            postData = sb.toString().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        setting();
        webView.postUrl(Const.urlBean.getAlipay_wap_url(), postData);
        // webView.loadUrl(url) ;
    }

    @Override
    protected void bindEvent() {

    }

}