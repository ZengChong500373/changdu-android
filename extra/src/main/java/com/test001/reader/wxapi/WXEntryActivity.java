package com.test001.reader.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.linyi.viva.extra.constant.Constant;
import org.linyi.base.entity.event.WeChatEvent;
import com.linyi.viva.extra.manager.EventManager;
import com.test001.reader.WeChatProxy;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.linyi.base.utils.LogUtil;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, WeChatProxy.APP_ID);
		try {
// 第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，
// 如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数
// 的Intent导致停留在透明界面，引起用户的疑惑
			boolean result = api.handleIntent(getIntent(), this);
			LogUtil.d("result = " + result);
			if(!result)
				finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		if (!api.handleIntent(intent, this)) {
			finish();
		}
	}

	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
			case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
				goToGetMsg();
				break;
			case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
				break;
			default:
				break;
		}
	}

	@Override
	public void onResp(BaseResp resp) {
		String code = null;
		int result = Constant.ShareResult.FAIL;
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = Constant.ShareResult.SUCCESS;
				if(resp instanceof SendAuth.Resp)
					code = ((SendAuth.Resp) resp).code;
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = Constant.ShareResult.CANCEL;
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = Constant.ShareResult.FAIL;
				break;
			case BaseResp.ErrCode.ERR_UNSUPPORT:
				result = Constant.ShareResult.FAIL;
				break;
			default:
				result = Constant.ShareResult.FAIL;
				break;
		}
		LogUtil.d("WXEntryActivity",resp.getType(), resp.errStr, resp.openId, resp.transaction, resp.errCode, "code = " + code);
		if (resp.getType() == 1) {
			EventManager.getInstance().sendWeChat(new WeChatEvent(WeChatEvent.LOGIN, result, code));
		} else if (resp.getType() == 2) {
			EventManager.getInstance().sendWeChat(new WeChatEvent(WeChatEvent.SHARE, result));
		}
		finish();
	}

	private void goToGetMsg() {
	}

	private void goToShowMsg(ShowMessageFromWX.Req showReq) {
	}
}