package com.linyi.viva.extra.net.module;

import android.support.annotation.Nullable;

import com.api.changdu.proto.MissionApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.net.api.MissionAPI;

import org.linyi.base.http.RetrofitManager;
import org.linyi.base.utils.ToastUtils;
import org.linyi.base.utils.help.ModuleHelp;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @fun: 任务
 * @developer: rwz
 * @date: 2019/1/31 0031 下午 2:51
 */
public class MissionModule {

    private static MissionAPI getService() {
        return RetrofitManager.getInstance().getService(MissionAPI.class);
    }

    /**
     * 任务列表
     */
    public static Observable<MissionApi.AckMissionList> missionList() {
        return getService().missionList(ModuleHelp.geneRequestBody(null))
                .compose(ModuleHelp.commTrans(MissionApi.AckMissionList.class));
    }

    /**
     * 完成任务
     */
    public static Observable<MissionApi.AckMissionFinish> missionPerform(MissionApi.MissionType missionType, @Nullable String code) {
        MissionApi.ReqMissionFinish.Builder builder = MissionApi.ReqMissionFinish.newBuilder();
        if(code != null)
            builder.setCode(code);
        MissionApi.ReqMissionFinish missionFinish = builder
                .setMissionType(missionType)
                .build();
        return getService().missionPerform(ModuleHelp.geneRequestBody(missionFinish))
                .compose(ModuleHelp.commTrans(MissionApi.AckMissionFinish.class))
                .doOnNext(new Consumer<MissionApi.AckMissionFinish>() {
                    @Override
                    public void accept(MissionApi.AckMissionFinish finish) throws Exception {
                        if (finish.getIsMax()) {
                            ToastUtils.getInstance().showShortSingle(R.string.max_score_today);
                        }
                    }
                });
    }

    /**
     * 双倍积分
     */
    public static Observable<MissionApi.AckMissionDoubleScore> doubleScore(String code) {
        MissionApi.ReqMissionDoubleScore score = MissionApi.ReqMissionDoubleScore.newBuilder()
                .setCode(code)
                .build();
        return getService().doubleScore(ModuleHelp.geneRequestBody(score))
                .compose(ModuleHelp.commTrans(MissionApi.AckMissionDoubleScore.class));
    }

    /**
     * 积分列表
     */
    public static Observable<MissionApi.AckMissionList> scoreList() {
        return getService().scoreList(ModuleHelp.geneRequestBody(null))
                .compose(ModuleHelp.commTrans(MissionApi.AckMissionList.class));
    }


}
