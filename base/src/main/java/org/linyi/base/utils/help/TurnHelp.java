package org.linyi.base.utils.help;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseArray;

import org.linyi.base.VivaSdk;
import org.linyi.base.constant.Key;
import org.linyi.base.entity.params.MsgDialogEntity;
import org.linyi.base.entity.params.ReaderParams;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.ToastUtils;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/25 0025 上午 11:15
 */
public class TurnHelp {

    private final static SparseArray<Class> sClassData = new SparseArray<>();

    public static void register(Class cls) {
        if (cls != null) {
            sClassData.put(cls.getSimpleName().hashCode(), cls);
        }
    }

    public static Class getClass(String className) {
        if(TextUtils.isEmpty(className))
            return null;
        return sClassData.get(className.hashCode());
    }

    private static boolean checkParams(Context context) {
        return context != null && CheckHelp.checkTurnTime();
    }

    /**
     * 登录
     */
    public static void login(Context context) {
        if (checkParams(context)) {
            Class cls = getClass("LoginActivity");
            if(cls != null)
                context.startActivity(new Intent(context, cls));
        }
    }

    /**
     * 阅读
     */
    public static void reader(Context context, String bookID, String bookName) {
        reader(context, new ReaderParams(bookID, bookName));
    }

    public static void reader(Context context, ReaderParams params) {
        LogUtil.d("reader", "context = " + context);
        if (checkParams(context) && params != null) {
            Class cls = getClass("ReaderActivity");
            if (cls != null) {
                Intent intent = new Intent(context, cls);
                intent.putExtra(Key.PARCELABLE_ENTITY, params);
                context.startActivity(intent);
            }
        }
    }

    /**
     * 跳转首页
     */
    public static void main(Context context) {
        if(checkParams(context)) {
            Class cls = getClass("MainActivity");
            if (cls != null) {
                context.startActivity(new Intent(context, cls));
                if(context instanceof Activity)
                    ((Activity) context).finish();
            } else {
                ToastUtils.getInstance().showShortSingle("【首页】未注册");
            }
        }
    }

    /**
     * 展示dialog形式的activity
     */
    public static void dialog(MsgDialogEntity entity) {
        if (entity != null && CheckHelp.checkTurnTime()) {
            Class cls = getClass("DialogActivity");
            if(cls != null) {
                Context context = VivaSdk.getContext();
                Intent intent = new Intent(context, cls);
                intent.putExtra(Key.PARCELABLE_ENTITY, entity);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }

    /**
     * 目录
     */
    public static void directory(Context context, String id, String bookName, int chapterSort) {
        if (checkParams(context)) {
            Class cls = getClass("ReadCatalogActivity");
            if (cls != null) {
                Intent intent = new Intent(context, cls);
                intent.putExtra("id",id);
                intent.putExtra("bookName",bookName);
                intent.putExtra("position",chapterSort);
                context.startActivity(intent);
            }
        }
    }

    /**
     * 任务中心
     */
    public static void mission(Context context) {
        if (checkParams(context)) {
            Class cls = getClass("MissionActivity");
            if (cls != null) {
                context.startActivity(new Intent(context,cls));
            }
        }
    }

    public static void history(Context context){
        if (checkParams(context)) {
            Class cls = getClass("HistoryRecordActivity");
            if (cls != null) {
                context.startActivity(new Intent(context,cls));
            }
        }
    }

}
