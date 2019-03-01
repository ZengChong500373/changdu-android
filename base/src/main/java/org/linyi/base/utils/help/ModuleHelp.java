package org.linyi.base.utils.help;

import android.support.annotation.Nullable;

import com.api.changdu.proto.Api;
import com.google.protobuf.Any;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;

import org.linyi.base.BaseApp;
import org.linyi.base.R;
import org.linyi.base.constant.RequestCode;
import org.linyi.base.constant.SystemConstant;
import org.linyi.base.entity.entity.WrapData;
import org.linyi.base.entity.params.MsgDialogEntity;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.manager.UserManager;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.ToastUtils;
import org.linyi.base.utils.UIUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ModuleHelp {

    public static final MediaType PROTO_BUF_TYPE = MediaType.parse("application/x-protobuf");

    //需要登录的观察者
    private static CommonObserver sLoginObserver;

    public static void setLoginObserver(CommonObserver sLoginObserver) {
        ModuleHelp.sLoginObserver = sLoginObserver;
    }

    /**
     * @param showErrorToast 是否显示error 提示消息
     * @return 是否请求成功
     * 注：失败时返回null
     */
    public static Boolean isRequestSuccess(Api.Response common, boolean showErrorToast) throws Exception {
        Boolean isSuccess = null;
        if (common != null) {
            LogUtil.ok("status = " + common.getStatus() + "\n msg = " + common.getMsg());
//            LogUtil.ok("status = " + common.getStatus() + "\n msg = " + common.getMsg() + "\n data = " + common.getData());
            Api.Status status = common.getStatus();
            if (status == Api.Status.SUCCESS) {
                isSuccess = true;
            }else if(status == Api.Status.USER_NOT_LOGIN){ //用户没有登录
                LogUtil.ok("isRequestSuccess", "msg = " + common.getMsg());
                //退出登录
                UserManager.getInstance().loginOut();
                if(sLoginObserver != null)
                    sLoginObserver.onNext(common.getMsg());
                  //提示用户重新登录
//                MsgDialogEntity entity = new MsgDialogEntity(UIUtils.getString(R.string.tips), common.getMsg(), RequestCode.LOGIN);
//                entity.setEnterText(UIUtils.getString(R.string.login));
//                TurnHelp.dialog(entity);
            } else if(showErrorToast){
                ToastUtils.getInstance().show(common.getMsg());
            }
        }
        return isSuccess;
    }

    /**
     * 从子线程切换到主线程
     * 跟compose()配合使用
     */
    public static <T> ObservableTransformer<T, T> toMainThread() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T extends Api.Response> ObservableTransformer<T, T> commTrans() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(new Predicate<T>() {
                            @Override
                            public boolean test(T t) throws Exception {
                                return isRequestSuccess(t, true);
                            }
                        });
            }
        };
    }

    public static <U extends Api.Response, D extends Message> ObservableTransformer<U, D> commTrans(final Class<D> cls) {
        return new ObservableTransformer<U, D>() {
            @Override
            public ObservableSource<D> apply(Observable<U> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<U, D>() {
                            @Override
                            public D apply(U u) throws Exception {
                                if (ModuleHelp.isRequestSuccess(u, true)) {
                                    return u.getData().unpack(cls);
                                }
                                return null;
                            }
                        });
            }
        };
    }

    public static RequestBody geneRequestBody(@Nullable Message message) {
        Map<String, Object> map = new HashMap<>();
        if (message != null) {
            Map<Descriptors.FieldDescriptor, Object> fields = message.getAllFields();
            if (fields != null && !fields.isEmpty()) {
                Set<Descriptors.FieldDescriptor> set = fields.keySet();
                for (Descriptors.FieldDescriptor descriptor : set) {
                    map.put(descriptor.getName(), fields.get(descriptor));
                }
            }
        }
        long timestamp = System.currentTimeMillis();
        map.put("systemTime", timestamp);
        String sign = SignUtils.geneSign(map, "e34addbc594d4aab9be7");
        Api.Request.Builder builder = Api.Request.newBuilder().setSign(sign).setSystemTime(timestamp);
        if(message != null)
            builder.setData(Any.pack(message));
        return RequestBody.create(PROTO_BUF_TYPE, builder.build().toByteArray());
    }

    public static String parseString(Message message) {
        if(message == null)
            return null;
        try {
            return new String(message.toByteArray(), SystemConstant.CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
