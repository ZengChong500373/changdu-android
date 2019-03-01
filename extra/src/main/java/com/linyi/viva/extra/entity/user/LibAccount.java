package com.linyi.viva.extra.entity.user;

import com.api.changdu.proto.UserApi;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/25 0025 下午 2:06
 */
public class LibAccount {

    private UserApi.LoginType type;
    private String token;
    private String userId;
    private String userName;
    private String imgUrl;
    //第三方登录名：SinaWeibo.NAME，QQ.NAME，Wechat.NAME
    private String libLoginName;
    private String unionid;

    public UserApi.LoginType getType() {
        return type;
    }

    public void setType(UserApi.LoginType type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLibLoginName() {
        return libLoginName;
    }

    public void setLibLoginName(String libLoginName) {
        this.libLoginName = libLoginName;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Override
    public String toString() {
        return "LibAccount{" +
                "type=" + type +
                ", token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", libLoginName='" + libLoginName + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
