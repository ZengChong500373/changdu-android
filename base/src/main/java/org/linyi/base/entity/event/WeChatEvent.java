package org.linyi.base.entity.event;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/28 0028 上午 11:05
 */
public class WeChatEvent {

    public static final int LOGIN  = 1;
    public static final int SHARE  = 2;

    private int type;

    private int result;

    private Object params;

    public WeChatEvent(int type, int result, Object params) {
        this.type = type;
        this.result = result;
        this.params = params;
    }

    public WeChatEvent(int type, int result) {
        this.type = type;
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }
}
