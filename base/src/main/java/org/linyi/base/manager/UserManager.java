package org.linyi.base.manager;

import android.text.TextUtils;

import com.api.changdu.proto.UserApi;
import com.google.protobuf.InvalidProtocolBufferException;

import org.linyi.base.constant.Key;
import org.linyi.base.constant.SystemConstant;
import org.linyi.base.http.RetrofitManager;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.help.ModuleHelp;

import java.io.UnsupportedEncodingException;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/29 0029 上午 11:50
 */
public class UserManager {

    private static final String TAG = "UserManager";
    private static volatile UserManager mManager = null;
    private boolean isLogin; //是否登录
    //用户ID
    private String mUserID;

    private UserManager() {
        String token = SharePreUtil.getString(Key.TOKEN);
        if (TextUtils.isEmpty(token)) {
            loginOut();
        } else {
            loginIn(token);
        }
    }

    public static UserManager getInstance() {
        if (mManager == null) {
            throw new RuntimeException("must call init() at App");
        }
        return mManager;
    }

    public static void init() {
        if (mManager == null) {
            synchronized (UserManager.class) {
                if (mManager == null) {
                    mManager = new UserManager();
                }
            }
        }
    }

    /**
     * 登出
     */
    public void loginOut() {
        saveLoginState(false);
        mUserID = null;
        SharePreUtil.removeKey(Key.TOKEN);
        SharePreUtil.removeKey(Key.USER_INFO);
    }

    /**
     * 登录
     */
    public void loginIn(String token) {
        if (!TextUtils.isEmpty(token)) {
            saveLoginState(true);
            RetrofitManager.getInstance().addHeaderParam("X-Auth-Token", token);
        }
    }

    private void saveLoginState(boolean state) {
        isLogin = state;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void saveToken(String token) {
        if (!TextUtils.isEmpty(token)) {
            SharePreUtil.putString(Key.TOKEN, token);
            loginIn(token);
        }
    }

    public void saveUser(UserApi.AckUserInfo userInfo) {
        if(userInfo == null && !isLogin)
            return;
        mUserID = userInfo.getUserID();
        SharePreUtil.putString(Key.USER_INFO, ModuleHelp.parseString(userInfo));
    }

    public UserApi.AckUserInfo getUser() {
        if(!isLogin)
            return null;
        String userInfoStr = SharePreUtil.getString(Key.USER_INFO);
        if (TextUtils.isEmpty(userInfoStr)) {
            return null;
        }
        try {
            UserApi.AckUserInfo userInfo = UserApi.AckUserInfo.parseFrom(userInfoStr.getBytes(SystemConstant.CHARSET_NAME));
            if(userInfo != null)
                mUserID = userInfo.getUserID();
            return userInfo;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUserID() {
        return mUserID;
    }

    /**
     * 保存积分
     */
    public void saveScore(int score) {
        UserApi.AckUserInfo user = getUser();
        if (user != null) {
            UserApi.AckUserInfo info = user.toBuilder().setScore(score).build();
            saveUser(info);
        }
    }


}
