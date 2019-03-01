package com.linyi.viva.extra.utils.help;

import android.content.Context;
import android.content.Intent;

import com.api.changdu.proto.MissionApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.ui.activity.AccountCenterActivity;
import com.linyi.viva.extra.ui.activity.FeedbackActivity;
import com.linyi.viva.extra.ui.activity.InviteCodeActivity;
import com.linyi.viva.extra.ui.activity.InviteFriendActivity;
import com.linyi.viva.extra.ui.activity.LevelActivity;
import com.linyi.viva.extra.ui.activity.MissionActivity;
import com.linyi.viva.extra.ui.activity.SetupActivity;
import com.linyi.viva.extra.ui.activity.UserInfoActivity;

import org.linyi.base.constant.Key;
import org.linyi.base.utils.CommUtils;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.base.utils.help.CheckHelp;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/30 0030 上午 10:27
 */
public class TurnHelp {

    private static boolean checkParams(Context context) {
        return context != null && CheckHelp.checkTurnTime();
    }

    /**
     * 经验等级
     */
    public static void levelExp(Context context) {
        if (checkParams(context)) {
            context.startActivity(new Intent(context, LevelActivity.class));
        }
    }

    /**
     * 账号中心
     */
    public static void accountCenter(Context context) {
        if (checkParams(context)) {
            context.startActivity(new Intent(context, AccountCenterActivity.class));
        }
    }

    /**
     * 反馈
     */
    public static void feedback(Context context) {
        if (checkParams(context)) {
            context.startActivity(new Intent(context, FeedbackActivity.class));
        }
    }

    /**
     * 个人资料
     */
    public static void userInfo(Context context) {
        if (checkParams(context)) {
            context.startActivity(new Intent(context, UserInfoActivity.class));
        }
    }

    /**
     * 设置页面
     */
    public static void setup(Context context) {
        if (checkParams(context)) {
            context.startActivity(new Intent(context, SetupActivity.class));
        }
    }

    /**
     * 分享
     */
    public static void inviteFriend(Context context, MissionApi.AckMissionList.Mission mission, MissionApi.ExtInfo extInfo) {
        if (checkParams(context)) {
            Intent intent = new Intent(context, InviteFriendActivity.class);
            intent.putExtra(Key.ONE, mission);
            intent.putExtra(Key.TWO, extInfo);
            context.startActivity(intent);
        }
    }

    /**
     * android程序间的分享文本
     */
    public static void shareTextToSystem(Context context, String content) {
        LogUtil.d("TurnHelp" , "shareToSystem");
        if (checkParams(context)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            //分享文本内容
            intent.putExtra(Intent.EXTRA_TEXT, content);
            intent.setType("text/plain");
            if (CommUtils.canTurn(context, intent)) {
                context.startActivity(Intent.createChooser(intent, UIUtils.getString(R.string.share_to)));
            }
        }
    }


    /**
     * 兑换邀请码
     */
    public static void inviteCode(Context context) {
        if (checkParams(context)) {
            context.startActivity(new Intent(context, InviteCodeActivity.class));
        }
    }


}
