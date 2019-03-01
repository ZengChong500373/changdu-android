package com.linyi.viva.extra.entity.event;

public class SignEvent {

    private boolean isSigned;
    private int signDay;

    public SignEvent(boolean isSigned, int signDay) {
        this.isSigned = isSigned;
        this.signDay = signDay;
    }

    public int getSignDay() {
        return signDay;
    }

    public boolean isSigned() {
        return isSigned;
    }
}
