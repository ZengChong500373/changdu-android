package com.test001.reader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.entity.comm.ShareEntity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.linyi.base.VivaConstant;
import org.linyi.base.VivaSdk;
import org.linyi.base.utils.LogUtil;

/**
 * 分享参考： https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317340&token=445a2ca7e0a0b9cb868f088132a09ede95bf101e&lang=zh_CN
 */
public class WeChatProxy {

	public static final String APP_ID = "wxd00e6e3108f53d69";
	private final IWXAPI api;

	public WeChatProxy(Context context) {
		api = WXAPIFactory.createWXAPI(context, null);
		api.registerApp(APP_ID);
	}

	public void sendReq() {
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "wechat_sdk_demo_test";
		api.sendReq(req);
	}

	/**
	 * 执行分享
	 * 回调用EventBus监听WeChatEvent
	 */
	public void share(ShareEntity entity) {
		LogUtil.d("share", "entity = " + entity);
		if(entity == null) return;
		final int scene = entity.getType() == ShareEntity.WECHAT ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
		final String title = entity.getTitle();
		final String des = entity.getDes();
		final String url = entity.getShareUrl();
		String imgUrl = entity.getImgUrl();
		if (TextUtils.isEmpty(imgUrl)) {
			Bitmap bitmap = BitmapFactory.decodeResource(VivaSdk.getContext().getResources(), R.mipmap.ic_launcher);
			sendShareReq(bitmap, url, title, des, scene);
		} else {
			Glide.with(VivaSdk.getContext())
					.load(imgUrl)
					.asBitmap()
					.override(100, 100)
					.centerCrop()
					.into(new SimpleTarget<Bitmap>() {
						@Override
						public void onResourceReady(Bitmap bmp, GlideAnimation<? super Bitmap> glideAnimation) {
							sendShareReq(bmp, url, title, des, scene);
						}
					});
		}
	}

	private void sendShareReq(Bitmap bmp, String url, String title, String des, int scene) {
		//初始化 WXWebpageObject 和 WXMediaMessage 对象
		//初始化 WXImageObject 和 WXMediaMessage 对象
		WXWebpageObject webPage = new WXWebpageObject();
		webPage.webpageUrl = url;
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = webPage;
		msg.title = title;
		msg.description = des;
		msg.thumbData = Util.bmpToByteArray(bmp, true);

		//构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = "img:" + System.currentTimeMillis();
		req.message = msg;
		//分享到对话
		req.scene = scene;
		LogUtil.d(req.checkArgs());
		//req.userOpenId = getOpenId();
		//调用api接口，发送数据到微信
		api.sendReq(req);
//						api.handleIntent(getIntent(), this);
	}

}
