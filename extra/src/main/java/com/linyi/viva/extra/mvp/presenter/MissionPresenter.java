package com.linyi.viva.extra.mvp.presenter;

import com.api.changdu.proto.MissionApi;
import com.linyi.viva.extra.mvp.contract.MissionContract;
import com.linyi.viva.extra.net.module.MissionModule;

import org.linyi.base.inf.CommonObserver;
import org.linyi.base.ui.weidgt.TempView;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/31 0031 下午 4:02
 */
public class MissionPresenter implements MissionContract.Presenter{

    private MissionContract.View mView;

    public MissionPresenter(MissionContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void requestData() {
        MissionModule.missionList()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if(mView != null)
                            mView.setTemp(TempView.STATUS_LOADING);
                    }
                })
                .subscribe(new CommonObserver<MissionApi.AckMissionList>() {
                    @Override
                    public void onNext(MissionApi.AckMissionList ackMissionList) {
                        if(mView != null)
                            mView.onRequestCompleted(ackMissionList);
                    }
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(mView != null)
                            mView.setTemp(TempView.STATUS_ERROR);
                    }
                });
    }

    @Override
    public void missionPerform(final MissionApi.MissionType type, String code) {
        MissionModule.missionPerform(type, code)
                .subscribe(new CommonObserver<MissionApi.AckMissionFinish>() {
                    @Override
                    public void onNext(MissionApi.AckMissionFinish ackMissionList) {
                        if(mView != null)
                            mView.onMissionPerformCompleted(type, ackMissionList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(mView != null)
                            mView.onMissionPerformCompleted(type,null);
                    }
                });
    }


}
