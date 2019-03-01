package org.linyi.base.entity.entity;

import com.api.changdu.proto.MissionApi;

import java.util.ArrayList;
import java.util.List;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/31 0031 下午 3:45
 */
public class BenefitEntity {

    //倒计时时间
    private long countdownTime;
    private MissionApi.AckMissionList.Mission mission;
    private MissionApi.ExtInfo extInfo;

    public BenefitEntity(long countdownTime, MissionApi.AckMissionList.Mission mission,
                         MissionApi.ExtInfo extInfo) {
        this.countdownTime = countdownTime;
        this.mission = mission;
        this.extInfo = extInfo;
    }

    public MissionApi.ExtInfo getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(MissionApi.ExtInfo extInfo) {
        this.extInfo = extInfo;
    }

    public long getCountdownTime() {
        return countdownTime;
    }

    public MissionApi.AckMissionList.Mission getMission() {
        return mission;
    }

    public void setCountdownTime(long countdownTime) {
        this.countdownTime = countdownTime;
    }

    public void setMission(MissionApi.AckMissionList.Mission mission) {
        this.mission = mission;
    }

    public MissionApi.MissionType getType() {
        return mission == null ? MissionApi.MissionType.GAME : mission.getMissionType();
    }
}
