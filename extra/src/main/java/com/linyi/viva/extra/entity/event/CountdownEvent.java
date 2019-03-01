package com.linyi.viva.extra.entity.event;

import com.api.changdu.proto.MissionApi;

public class CountdownEvent {

    private MissionApi.MissionType type;


    public CountdownEvent(MissionApi.MissionType type) {
        this.type = type;
    }

    public MissionApi.MissionType getType() {
        return type;
    }

    public void setType(MissionApi.MissionType type) {
        this.type = type;
    }
}

