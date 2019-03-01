package com.linyi.viva.extra.net.api;

import com.api.changdu.proto.Api;
import com.linyi.viva.extra.net.API;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/24 0024 上午 11:27
 */
public interface AppAPI {

    @POST(API.INIT)
    Observable<Api.Response> init(@Body RequestBody body);


}
