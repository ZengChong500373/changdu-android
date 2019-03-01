package com.linyi.viva.extra.net.api;

import com.api.changdu.proto.Api;
import com.api.changdu.proto.MissionApi;
import com.linyi.viva.extra.net.API;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/31 0031 下午 2:50
 */
public interface MissionAPI {

    @POST(API.MISSION_LIST)
    Observable<Api.Response> missionList(@Body RequestBody body);

    @POST(API.MISSION_PERFORM)
    Observable<Api.Response> missionPerform(@Body RequestBody body);

    @POST(API.DOUBLE_SCORE)
    Observable<Api.Response> doubleScore(@Body RequestBody body);

    @POST(API.SCORE_LIST)
    Observable<Api.Response> scoreList(@Body RequestBody body);
    
}
