package com.showu.baogu.pay;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.showu.baogu.api.film.UpompParam;
import com.showu.baogu.application.Const;
import com.showu.baogu.bean.UpomBean;
import com.showu.common.http.HttpClient;
import com.showu.common.http.HttpStringResult;
import com.showu.common.http.IResponse;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.JSONTool;
import com.showu.common.util.LogUtil;
import com.unionpay.upomp.lthj.util.PluginHelper;
import com.upomp.pay.info.Upomp_Pay_Info;
import com.upomp.pay.info.XmlDefinition;

import org.json.JSONException;
import org.json.JSONObject;

public class UpompPay implements IResponse{
	private final String upompPayUrl = "/api/order_enhance/get_order_sign.api";
	private Activity activity;
    private HttpClient client ;
	private final int REQUEST_SUCCESS = 1;
	private final int REQUEST_FAIL = 2;
	private final int JSON_ERROR = 3;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REQUEST_SUCCESS:
				sigl((UpomBean) msg.obj);
				break;

			case REQUEST_FAIL:

				break;

			case JSON_ERROR:

				break;
			default:
				break;
			}
		};
	};

	public UpompPay(Activity activity) {
		this.activity = activity;
        client=new HttpClient(this) ;
	}

	public void upomPayClient(String baseUrl, String uorderid) {
		StringBuilder sb = new StringBuilder(baseUrl);
		UpompParam upompParam = new UpompParam();
		upompParam.setUorderid(uorderid);
        upompParam.setUserid(Const.user.getUserid());
        upompParam.setDeviceidentifyid(GUIUtil.getDeviceId(activity));
//		sb.append(upompPayUrl).append(HttpUtil.getGetString(upompParam));
        client.get(sb.append(upompPayUrl).append(upompParam.getString()).toString());
	}




	private void sigl(UpomBean bean) {
		Upomp_Pay_Info.merchantId = bean.getMerchantid();
		Upomp_Pay_Info.merchantName = bean.getMerchantname();
		Upomp_Pay_Info.merchantOrderId = bean.getMerchantorderid();
		Upomp_Pay_Info.merchantOrderAmt=bean.getMerchantorderamt();
		Upomp_Pay_Info.merchantOrderTime=bean.getMerchantordertime();
		Upomp_Pay_Info.xmlSign = bean.getSign();
		String LanchPay = XmlDefinition.ReturnXml(Upomp_Pay_Info.xmlSign, 3);
		/*
		 * 向插件提交xml数据
		 */
		byte[] to_upomp = LanchPay.getBytes();
		Bundle mbundle = new Bundle();
		// to_upomp为商户提交的XML
        mbundle.putBoolean("test", false);
		mbundle.putByteArray("xml", to_upomp);
		mbundle.putString("action_cmd", Upomp_Pay_Info.CMD_PAY_PLUGIN);
		PluginHelper.LaunchPlugin(activity, mbundle);
	}

    @Override
    public void response(HttpStringResult result) {
        try {
            LogUtil.e(getClass(), result.getJson()+"");
            JSONObject jsonObject = new JSONObject(result.getJson());
            int status = jsonObject.getInt("status");
            if (status == 1) {
                UpomBean upomBean = JSONTool.jsonToBean(UpomBean.class,
                        jsonObject);
                sigl(upomBean);
            } else {
                GUIUtil.toast(activity,"获取银联签名失败");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
