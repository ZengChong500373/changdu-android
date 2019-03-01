package com.linyi.viva.extra.entity.comm;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/1 0001 上午 11:04
 */
public class ShareEntity {

    public static final int WECHAT = 1;
    public static final int FRIEND_CIRCLE = 2;
    public static final int QQ = 1;

    private String title;
    private String des;
    private String imgUrl;
    private String shareUrl;
    private int type;

    public ShareEntity(String title, String des, String imgUrl, String shareUrl, int type) {
        this.title = title;
        this.des = des;
        this.imgUrl = imgUrl;
        this.shareUrl = shareUrl;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDes() {
        return des;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    @Override
    public String toString() {
        return "ShareEntity{" +
                "title='" + title + '\'' +
                ", des='" + des + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", type=" + type +
                '}';
    }
}
