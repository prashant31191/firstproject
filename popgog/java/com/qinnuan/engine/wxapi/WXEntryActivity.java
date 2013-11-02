package com.qinnuan.engine.wxapi;

import android.os.Bundle;
import android.widget.Button;

import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.common.util.GUIUtil;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

	private Button gotoBtn, regBtn, launchBtn, checkBtn;

	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;
	private final String APP_ID = "wx644ba655b578815a";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, APP_ID, true);
		api.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq arg0) {
//		toastString("分享成功");
        GUIUtil.toast(this, "分享成功");
		this.finish();
	}

	@Override
	public void onResp(BaseResp arg0) {
		// TODO Auto-generated method stub
        GUIUtil.toast(this,"分享成功");
		this.finish();

	}

}