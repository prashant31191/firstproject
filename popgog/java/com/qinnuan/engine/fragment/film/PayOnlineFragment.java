package com.qinnuan.engine.fragment.film;

import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qinnuan.engine.activity.film.OnLineSeatActivity;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.fragment.FragmentView;
import com.qinnuan.engine.fragment.InjectView;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.application.Const;

import java.io.UnsupportedEncodingException;

@FragmentView(id = R.layout.activity_pay_wap)
public class PayOnlineFragment extends BaseFragment {
    @InjectView(id = R.id.webView)
    private WebView webView;

    private String subject;
    private float totalPrice;
    private String outTradeNo;

    public PayOnlineFragment() {
    }

    public PayOnlineFragment(String subject, float totalPrice, String outTradeNo) {
        this.subject = subject;
        this.totalPrice = totalPrice;
        this.outTradeNo = outTradeNo;
    }

    private void setting() {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if(getActivity()!=null){
                    getActivity().setProgress(progress * 1000);
                }
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

    @Override
    public void leftClick() {
        ((OnLineSeatActivity)getActivity()).getOrderStatus();
    }

    private void paySuccess(String url) {

        if (url.contains("result=success")) {
            ((OnLineSeatActivity)getActivity()).buyTickSuccess();
        }
    }

    @Override
    public void bindDataForUIElement() {
        StringBuffer sb = new StringBuffer();
        sb.append("out_trade_no=").append(outTradeNo).append("&");
        sb.append("subject=").append(subject).append("&");
        sb.append("total_fee=").append(totalPrice);
        LogUtil.d(getClass(),sb.toString());
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
