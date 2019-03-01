package com.linyi.viva.extra.mvp.contract;

import com.api.changdu.proto.MissionApi;

import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.BaseView;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/31 0031 下午 4:03
 */
public interface MissionContract {

    interface Presenter extends BasePresenter{
        void requestData();

        void missionPerform(MissionApi.MissionType type, String code);

    }

    interface View extends BaseView{

        void setTemp(int type);

        void onRequestCompleted(MissionApi.AckMissionList ackMissionList);

        void onMissionPerformCompleted(MissionApi.MissionType type, MissionApi.AckMissionFinish ackMissionFinish);
    }


}
