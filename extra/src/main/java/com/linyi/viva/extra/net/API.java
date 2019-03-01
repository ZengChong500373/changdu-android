package com.linyi.viva.extra.net;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/24 0024 上午 11:25
 */
public interface API {

    //初始化
    String INIT = "v1/system/init";
    //登录
    String LOGIN = "v1/user/login";
    //短信验证码
    String SEND_CODE = "v1/user/sms";
    //用户偏好保存
    String FAVORITE = "v1/user/favorite";
    //用户信息
    String USER_INFO = "v1/user/info";
    //经验等级数据
    String EXP_INFO = "v1/user/expInfo";
    //设备登录
    String DEVICE_LOGIN = "v1/user/deviceLogin";
    //任务列表
    String MISSION_LIST = "v1/mission/list";
    //消耗任务
    String MISSION_PERFORM = "v1/mission/finish";
    //双倍积分
    String DOUBLE_SCORE = "v1/mission/doubleScore";
    //积分列表
    String SCORE_LIST = "v1/mission/log";

}
