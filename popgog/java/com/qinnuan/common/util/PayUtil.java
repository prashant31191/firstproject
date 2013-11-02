package com.qinnuan.common.util;

import java.net.URLEncoder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alipay.android.appDemo4.AlixId;
import com.alipay.android.appDemo4.BaseHelper;
import com.alipay.android.appDemo4.MobileSecurePayHelper;
import com.alipay.android.appDemo4.MobileSecurePayer;
import com.alipay.android.appDemo4.PartnerConfig;
import com.alipay.android.appDemo4.ResultChecker;
import com.alipay.android.appDemo4.Rsa;
import com.showu.baogu.R;

public class PayUtil {
	private String TAG = getClass().getSimpleName();
	private ProgressDialog mProgress = null;
	private Activity activity;
	private PayListener payListener;

	public interface PayListener {
		public void paySuccess();

		public void payFail();
	}

	public PayUtil(Activity activity, PayListener listener) {
		this.activity = activity;
		this.payListener = listener;
	}

	public void payByClient(String subject, String body, String uorderid,String price, String notifyUrl) {
		//
		// check to see if the MobileSecurePay is already installed.
		// 检测安全支付服务是否安装
		LogUtil.e(getClass(), uorderid) ;
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(activity);
		boolean isMobile_spExist = mspHelper.detectMobile_sp();
		if (!isMobile_spExist)
			return;

		// check some info.
		// 检测配置信息
		if (!checkInfo()) {
			BaseHelper
					.showDialog(
							activity,
							"提示",
							"缺少partner或者seller，请在src/com/alipay/android/appDemo4/PartnerConfig.java中增加。",
							R.drawable.infoicon);
			return;
		}

		// start pay for this order.
		// 根据订单信息开始进行支付
		try {
			// prepare the order info.
			// 准备订单信息

			String orderInfo = getOrderInfo(subject, body, uorderid,price,notifyUrl);
			// 这里根据签名方式对订单信息进行签名
			String signType = getSignType();
			String strsign = sign(signType, orderInfo);
			Log.v("sign:", strsign);
			// 对签名进行编码
			strsign = URLEncoder.encode(strsign);
			// 组装好参数
			String info = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&"
					+ getSignType();
			Log.v("orderInfo:", info);
			// start the pay.
			// 调用pay方法进行支付
			MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, activity);

			if (bRet) {
				// show the progress bar to indicate that we have started
				// paying.
				// 显示“正在支付”进度条
				closeProgress();
				mProgress = BaseHelper.showProgress(activity, null, "正在支付",
						false, true);
			} else
				;
		} catch (Exception ex) {
			Toast.makeText(activity, R.string.remote_call_failed,
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * check some info.the partner,seller etc. 检测配置信息
	 * partnerid商户id，seller收款帐号不能为空
	 * 
	 * @return
	 */
	private boolean checkInfo() {
		String partner = PartnerConfig.PARTNER;
		String seller = PartnerConfig.SELLER;
		if (partner == null || partner.length() <= 0 || seller == null
				|| seller.length() <= 0)
			return false;

		return true;
	}

	//
	// close the progress bar
	// 关闭进度框
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	String getSignType() {
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}

	String getOrderInfo(String subject, String body, String uorderid,String price,String notifyUrl) {

		LogUtil.e(getClass(), "alipay_app_url==>" + notifyUrl);
		if(TextUtil.isEmpty(notifyUrl)){
			payListener.payFail() ;
		}
		String strOrderInfo = "partner=" + "\"" + PartnerConfig.PARTNER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "seller=" + "\"" + PartnerConfig.SELLER + "\"";
		strOrderInfo += "&";
		// strOrderInfo += "out_trade_no=" + "\"" + getOutTradeNo() + "\"";
		strOrderInfo += "out_trade_no=" + "\"" + uorderid + "\"";
		strOrderInfo += "&";
		strOrderInfo += "subject=" + "\"" + subject + "\"";
		strOrderInfo += "&";
		strOrderInfo += "body=" + "\"" + body + "\"";
		strOrderInfo += "&";
		// strOrderInfo += "total_fee=" + "\"" + totalPrice + "\"";
		strOrderInfo += "total_fee=" + "\"" + price + "\"";
		strOrderInfo += "&";
		strOrderInfo += "notify_url=" + "\""
		// +
		// "http://pay.popgog.com/AliPayment/AliClientAppPay/popgog_alipay_notify.aspx"
				+ notifyUrl + "\"";

		return strOrderInfo;
	}

	String sign(String signType, String content) {
		return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;

				Log.e(TAG, strRet); // strRet范例：resultStatus={9000};memo={};result={partner="2088201564809153"&seller="2088201564809153"&out_trade_no="050917083121576"&subject="123456"&body="2010新款NIKE 耐克902第三代板鞋 耐克男女鞋 386201 白红"&total_fee="0.01"&notify_url="http://notify.java.jpxx.org/index.jsp"&success="true"&sign_type="RSA"&sign="d9pdkfy75G997NiPS1yZoYNCmtRbdOP0usZIMmKCCMVqbSG1P44ohvqMYRztrB6ErgEecIiPj9UldV5nSy9CrBVjV54rBGoT6VSUF/ufjJeCSuL510JwaRpHtRPeURS1LXnSrbwtdkDOktXubQKnIMg2W0PreT1mRXDSaeEECzc="}
				switch (msg.what) {
				case AlixId.RQF_PAY: {
					//
					closeProgress();

					BaseHelper.log(TAG, strRet);

					// 处理交易结果
					try {
						// 获取交易状态码，具体状态代码请参看文档
						String tradeStatus = "resultStatus={";
						int imemoStart = strRet.indexOf("resultStatus=");
						imemoStart += tradeStatus.length();
						int imemoEnd = strRet.indexOf("};memo=");
						tradeStatus = strRet.substring(imemoStart, imemoEnd);

						// 先验签通知
						ResultChecker resultChecker = new ResultChecker(strRet);
						int retVal = resultChecker.checkSign();
						// 验签失败
						if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
							BaseHelper.showDialog(
									activity,
									"提示",
									activity.getResources().getString(
											R.string.check_sign_failed),
									android.R.drawable.ic_dialog_alert);
						} else {// 验签成功。验签成功后再判断交易状态码
							if (tradeStatus.equals("9000")) {// 判断交易状态码，只有9000表示交易成功
								BaseHelper.showDialog(activity, "提示",
										"支付成功。交易状态码：" + tradeStatus,
										R.drawable.infoicon);
								payListener.paySuccess();
							} else {
								BaseHelper.showDialog(activity, "提示",
										"支付失败。交易状态码:" + tradeStatus,
										R.drawable.infoicon);
								payListener.payFail();
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
						BaseHelper.showDialog(activity, "提示", strRet,
								R.drawable.infoicon);
					}
				}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
}
