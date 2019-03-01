package org.linyi.base.entity.event;

import android.os.Bundle;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/15 0015 上午 11:43
 */
public class ActivityResultEvent {

    private int requestCode;
    private int resultCode;
    private Bundle bundle;

    public ActivityResultEvent(int requestCode, int resultCode) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
    }

    public ActivityResultEvent(int requestCode, int resultCode, Bundle bundle) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.bundle = bundle;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public Bundle getBundle() {
        return bundle;
    }
}
