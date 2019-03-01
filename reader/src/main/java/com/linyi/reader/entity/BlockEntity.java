package com.linyi.reader.entity;

import android.graphics.RectF;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/20 0020 下午 3:49
 */
public class BlockEntity extends RectF{

    //中心菜单区域
    public static final int MENU = 0;
    //订阅区域
    public static final int SUBSCRIBE = 1;
    //类型
    private int type;

    public BlockEntity(int type) {
        this.type = type;
    }

    public BlockEntity(float left, float top, float right, float bottom, int type) {
        super(left, top, right, bottom);
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
