package org.linyi.base.entity.event;

import org.linyi.base.entity.params.MsgDialogEntity;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/29 0029 下午 2:13
 */
public class DialogClickEvent {

    private boolean isClickEnter;

    private MsgDialogEntity entity;

    public DialogClickEvent(boolean isClickEnter, MsgDialogEntity entity) {
        this.isClickEnter = isClickEnter;
        this.entity = entity;
    }

    public boolean isClickEnter() {
        return isClickEnter;
    }

    public MsgDialogEntity getEntity() {
        return entity;
    }
}
